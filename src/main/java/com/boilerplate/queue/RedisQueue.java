package com.boilerplate.queue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.h2.util.New;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.database.redis.implementation.RedisQueueDataAccessLayer;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * This is the class used to implement a Redis Queue
 * @author mohit
 */
public class RedisQueue implements IQueue {
	
	/**
	 * This is the instance of the Logger
	 */
	private static Logger logger = Logger.getInstance(RedisQueue.class);

	/**
	 * This is the number of times queue had encountered an error
	 */
	private int queueErrorCount = 0;
	
	/**
	 * This is the maximum number of errors that can occur after
	 * which queue is turned off
	 */
	private int maximumQueueErrorCount = 0;
	
	/**
	 * This is the instance of the redisQueueDataAccessLayer
	 */
	@Autowired 
	com.boilerplate.database.redis.implementation.RedisQueueDataAccessLayer redisQueueDataAccessLayer
		= new com.boilerplate.database.redis.implementation.RedisQueueDataAccessLayer();
	
	/**
	 * The setter to set the Redis Queue DataAccessLayer
	 * @param redisQueueDataAccessLayer The redisQueueDataAccessLayer
	 */
	public void setRedisQueueDataAccessLayer(
			com.boilerplate.database.redis.implementation.RedisQueueDataAccessLayer 
			redisQueueDataAccessLayer){
		this.redisQueueDataAccessLayer = redisQueueDataAccessLayer;
	}

	/**
	 * This is the private constructor of the queue
	 */
	private RedisQueue() {
		
	}
	/**
	 * @see IQueue.insert
	 */
	@Override
	public <T> void insert(String subject, T item) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream  objectOutputStream = null;
		try{
			//convert this item into a byte array and then into an string
			 byteArrayOutputStream = new ByteArrayOutputStream();
		     objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		     objectOutputStream.writeObject(item);
		     byte[] bytes = byteArrayOutputStream.toByteArray();
			 this.redisQueueDataAccessLayer.insert(subject, bytes);
		}
		catch(Exception ex){
			this.maximumQueueErrorCount++;
			AsyncWorkItem<T> asyncWorkItem = (AsyncWorkItem<T>) item;
			logger.logException("RedisQueue", "insert", "catch block"
					, "Error inserting message " +item.toString()  +" Subjects:" + Base.toJSON(asyncWorkItem.getSubjects())+ " Payload: " +Base.toJSON(asyncWorkItem.getPayload()) +" into "+subject
					, ex);
			throw ex;
		}
		finally{
			if(byteArrayOutputStream != null){
				try{
					byteArrayOutputStream.close();
				}
				catch(Exception  ex){
					//we cant do much if this happens
				}
			}

			if(objectOutputStream != null){
				try{
					objectOutputStream.close();
				}
				catch(Exception  ex){
					//we cant do much if this happens
				}
			}
		}
	}

	/**
	 * @see IQueue.insert
	 */
	@Override
	public <T> T remove(String subject, int timeoutInMilliSeconds) throws Exception {
		return this.remove(subject);
	}

	/**
	 * @see IQueue.remove
	 */
	@Override
	public <T> T remove(String subject) throws Exception {
		ByteArrayInputStream byteArrayInputStream =  null;
		ObjectInputStream objectInputStream =null;
		T t = null;
		try{
			byte[] returnValue = this.redisQueueDataAccessLayer.remove(subject,byte[].class);
			if(returnValue !=null){
				byteArrayInputStream = new ByteArrayInputStream(returnValue);
		    	objectInputStream = new ObjectInputStream(byteArrayInputStream);
		        t = (T) objectInputStream.readObject();
				}
			}//end try
			catch(Exception ex){
				this.queueErrorCount++;
				logger.logException("RedisQueue", "RedisQueue"
						, "catch block", "Error creating instance", ex);
				throw ex;
			}
			finally{
				if(byteArrayInputStream != null){
					try{
						byteArrayInputStream.close();
					}
					catch(Exception  ex){
						//we cant do much if this happens
					}
				}
				if(objectInputStream != null){
					try{
						objectInputStream.close();
					}
					catch(Exception  ex){
						//we cant do much if this happens
					}
				}
			}
		return t;
	}

	/**
	 * @see IQueue.isQueueEnabled
	 */
	@Override
	public boolean isQueueEnabled() {
		return this.queueErrorCount <= this.maximumQueueErrorCount;
	}

	/**
	 * @see IQueue.resetQueueErrorCount
	 */
	@Override
	public void resetQueueErrorCount() {
		this.queueErrorCount = 0;

	}
	
	/**
	 * The private static instance for making queue a singleton.
	 */
	private static RedisQueue redisQueue;// = new RedisQueue();
	
	/**
	 * This method returns an instance of queue
	 * @return an instance of queue
	 */
	public static RedisQueue getInstance() {
		return RedisQueue.redisQueue;
	}
	
	/**
	 * This is the instance of connection
	 */
	private Connection connection;
	
	/**
	 * The default timeout for queue
	 */
	private int queueDefaultGetTimeout;
	
	/**
	 * Private constructor
	 * @throws Exception This exception is thrown if there is an 
	 * exception in creating a queue
	 */
	private RedisQueue(BoilerplateMap<String,String> configurations)throws Exception{
		try{
			// Setting the maximumQueueErrorCount and queueDefaultGetTimeout from configuration
			this.maximumQueueErrorCount = Integer.parseInt(configurations.get("MaximumQueueExceptionCount"));
			this.queueDefaultGetTimeout = Integer.parseInt(configurations.get("QueueTimeoutInMilliSeconds"));
		}
		catch(Exception ex){
			this.queueErrorCount++;
			logger.logException("RedisQueue", "RedisQueue"
					, "catch block", "Error creating instance", ex);
			throw ex;
		}
	}

	/**
	 * Returns an instance  of queue proxy
	 * @return An instance of queue 
	 * @param configurations The configuration for the queue
	 * @throws Exception If there is an issue in creating queue
	 */
	public static RedisQueue getInstance(BoilerplateMap<String,String> configurations) throws Exception{
		if(RedisQueue.redisQueue ==null){
			synchronized (RedisQueue.class) {
				if(RedisQueue.redisQueue ==null){
					RedisQueue.redisQueue = new RedisQueue(configurations);
				}//end if
			}//end sync
		}//end if
		return RedisQueue.redisQueue;
	}
	
	/**
	 * Returns the size of the queue
	 * @return Size of Queue
	 * @param Queue Name
	 * @throws Exception If there is an issue in fetching size of queue
	 */
	public long getQueueSize(String queueName) throws Exception{
		try{
			long returnValue = this.redisQueueDataAccessLayer.getQueueSize(queueName);
			return returnValue;
		}
		catch (Exception exception){
			logger.logException("RedisQueue", "getQueueSize",
					"Catch block of getQueueSize", "Error creating instance", exception);
		}
		return 0;
	}
}
