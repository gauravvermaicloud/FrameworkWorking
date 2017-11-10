package com.boilerplate.queue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.SaslConfig;
import com.rabbitmq.client.SaslMechanism;

/**
 * This class is used to implement a rabbit mq based queue proxy.
 * @author gaurav
 */
public class RabbitMQ implements IQueue{

	/**
	 * This is the instance of logger
	 */
	private static Logger logger = Logger.getInstance(RabbitMQ.class);
	
	/**
	 * This is the instance of connection
	 */
	private Connection connection;
	
	/**
	 * static instance of Rabbit MQ for creation of a sinleton
	 */
	private static RabbitMQ rabbitMQ;
	
	/**
	 * This is the number of times queue had an error
	 */
	private int queueExceptionCount =0;
	
	/**
	 * This is the maximum number of exceptions that can occur after
	 * which queue is turned off
	 */
	private int maximumQueueExceptionCount=0;
	
	/**
	 * The default timeout for queue
	 */
	private int queueDefaultGetTimeout;
	/**
	 * Private constructor
	 * @throws Exception This exception is thrown if there is an 
	 * exception in creating a queue
	 */
	private RabbitMQ(BoilerplateMap<String,String> configurations)throws Exception{
		try{
			ConnectionFactory connectionFactroy = new ConnectionFactory();
			//TODO - Read this from config in db
			connectionFactroy.setHost(configurations.get("QueueServer"));
			this.maximumQueueExceptionCount = Integer.parseInt(configurations.get("MaximumQueueExceptionCount"));
			this.queueDefaultGetTimeout = Integer.parseInt(configurations.get("QueueTimeoutInMilliSeconds"));
			connectionFactroy.setUsername(configurations.get("QueueUserName"));
			connectionFactroy.setPassword(configurations.get("QueuePassword"));
			
			
		    //create a connection
			this.connection = connectionFactroy.newConnection();
		}
		catch(Exception ex){
			this.queueExceptionCount++;
			logger.logException("RabbitMQ", "RabbitMQ"
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
	public static RabbitMQ getInstance(BoilerplateMap<String,String> configurations) throws Exception{
		if(RabbitMQ.rabbitMQ ==null){
			synchronized (RabbitMQ.class) {
				if(RabbitMQ.rabbitMQ ==null){
					RabbitMQ.rabbitMQ = new RabbitMQ(configurations);
				}//end if
			}//end sync
		}//end if
		return RabbitMQ.rabbitMQ;
	}
	
	/**
	 * @see IQueue.insert
	 */
	@Override
	public <T> void insert(String subject, T item) throws Exception{
		Channel channel=null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream  objectOutputStream = null;
		try{
			 channel = connection.createChannel();
			 channel.queueDeclare();
			 byteArrayOutputStream = new ByteArrayOutputStream();
		     objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		     objectOutputStream.writeObject(item);
			 channel.basicPublish("", subject, null,byteArrayOutputStream.toByteArray());
		}
		catch(Exception ex){
			this.queueExceptionCount++;
			logger.logException("RabbitMQ", "insert", "catch block"
					, "Error inserting message "+item.toString()+" into "+subject
					, ex);
			throw ex;
		}
		finally{
			if(channel != null){
				try{
					channel.close();
				}
				catch(Exception  ex){
					//we cant do much if this happens
				}

				if(byteArrayOutputStream != null){
					try{
						byteArrayOutputStream.close();
					}
					catch(Exception  ex){
						//we cant do much if this happens
				}

				if(objectOutputStream != null){
					try{
						objectOutputStream.close();
					}
					catch(Exception  ex){
						//we cant do much if this happens
				}	
			}//end if
		}//end finally
	}
	}
}

	
	@Override
	public <T> T remove(String subject, int timeoutInMilliSeconds) throws Exception{
		Channel channel = null;
		ByteArrayInputStream byteArrayInputStream =  null;
		ObjectInputStream objectInputStream =null;
		T t = null;
	    
		try{
			channel = connection.createChannel();
		    channel.queueDeclare(subject, false, false, false, null);
		    QueueingConsumer consumer = new QueueingConsumer(channel);
		    channel.basicConsume(subject, true, consumer);
		    QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeoutInMilliSeconds);
		    if(delivery != null){
		    	byte[] object =delivery.getBody();
		    	byteArrayInputStream = new ByteArrayInputStream(object);
		    	objectInputStream = new ObjectInputStream(byteArrayInputStream);
		        t = (T) objectInputStream.readObject();
		    }
		    return t;
		}//end try
		catch(Exception ex){
			this.queueExceptionCount++;
			logger.logException("RabbitMQ", "remove", "catch block"
					, "Error removing message from "+subject
					, ex);
			throw ex;
		}
		finally{
			if(channel != null){
				try{
					channel.close();
				}
				catch(Exception  ex){
					//we cant do much if this happens
				}
			}//end if
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
			}//end if

		}//end finally
	}//end method

	/**
	 * @see IQueue.remove
	 */
	@Override
	public <T> T remove(String subject) throws Exception{
		return this.remove(subject,this.queueDefaultGetTimeout);
	}

	/**
	 * @see IQueue.isQueueEnabled
	 */
	@Override
	public boolean isQueueEnabled() {
		return this.queueExceptionCount <= this.maximumQueueExceptionCount;
	}

	/**
	 * @see IQueue.resetQueueErrorCount
	 */
	@Override
	public void resetQueueErrorCount() {
		this.queueExceptionCount = 0;		
	}

	@Override
	public long getQueueSize(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

}
