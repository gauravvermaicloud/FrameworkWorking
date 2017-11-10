package com.boilerplate.queue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.collections.BoilerplateMap;


/**
 * This method returns a queue
 * @author gaurav
 */
public class QueueFactory {
	/**
	 * This is a map of all the configuration values.
	 */
	private static BoilerplateMap<String,String> configurations = null;
	
	/**
	 * This is the instance of the RedisQueue
	 */
	@Autowired 
	com.boilerplate.queue.RedisQueue redisQueue;
	
	/**
	 * Returns an instance of the default queue type
	 * @return The instance of queue
	 */
	public static IQueue getInstance() throws Exception{
		//TODO - Read this from config
		if(configurations == null){
			loadPropertyFile();
		}
		String queueProvider = configurations.get("QueueProvider");
		return QueueFactory.getInstance(queueProvider);
	}
	
	/**
	 * Returns an instance of given queue provider type.
	 * @param queueProvider The queue provider.
	 * @return The instance of queue
	 * @throws Exception 
	 */
	public static IQueue getInstance(String queueProvider) throws Exception{
		if(configurations == null){
			loadPropertyFile();
		}
		switch (queueProvider){
		// based on queue provider return an instance
		case "BoilerplateNonProductionInMemoryQueue" : 
			return BoilerplateNonProductionInMemoryQueue.getInstance();
		case "RabbitMQ" :
			return RabbitMQ.getInstance(configurations);
		case "RedisQueue" :
			return RedisQueue.getInstance(configurations);
		}
		throw new Exception("Method not implemented");
	}
	
	/**
	 * This method loads configuration from the property file
	 */
	private static void loadPropertyFile(){
		Properties properties  = null;
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					QueueFactory.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			//for each key that exists in the properties file put it into
			//the configuration map
			QueueFactory.configurations= new BoilerplateMap<String, String>();
			for(String key : properties.stringPropertyNames()){
				QueueFactory.configurations.put(key, properties.getProperty(key));
			}
		}
		catch(IOException ioException){
			//we do not generally expect an exception here
			//and because we are start of the code even before loggers
			//have been enabled if something goes wrong we will have to print it to
			//console. We do not throw this exception because we do not expect it
			//and if we do throw it then it would have to be handeled in all code 
			//making it bloated, it is hence a safe assumption this exception ideally will not
			//happen unless the file access has  issues
			System.out.println(ioException.toString());
		}
		finally{
			//close the input stream if it is not null
			if(inputStream !=null){
				try{
					inputStream.close();
				}
				catch(Exception ex){
					//if there is an issue closing it we just print it
					//and move forward as there is not a good way to inform user.
					System.out.println(ex.toString());
				}
			}
		}//end finally	
	}
	
	
	/**
	 * Returns the size of an instance of the default queue type.
	 * @param queueProvider The queue provider.
	 * @return The Size of an instance of queue.
	 * @throws Exception 
	 */
	public Long getQueueSize(String queueName) throws Exception{
		if(configurations == null){
			loadPropertyFile();
		}
		String queueProvider = configurations.get("QueueProvider");
		switch (queueProvider){
		case "BoilerplateNonProductionInMemoryQueue" : 
			return null;
		case "RabbitMQ" :
			return null;
		case "RedisQueue" :
			return redisQueue.getQueueSize(queueName);
		}
		throw new Exception("Queue Size Method not implemented");
	}
}
