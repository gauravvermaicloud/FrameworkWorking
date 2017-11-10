package com.boilerplate.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.collections.BoilerplateMap;


public class CacheFactory {

	/**
	 * This is a map of all the configuration values.
	 */
	private static BoilerplateMap<String,String> configurations = null;
	
	/**
	 * This is the instance of the cache.
	 */
	private static ICache cache = null;
	
	/**
	 * This method gets an instance of the cache
	 * @return The ICache which is the instance of the cache 
	 * @throws Exception Throws an exception if the cache is not available.
	 */
	public static ICache getInstance() throws Exception{
		String cacheProvider = null;
		if(cache == null){
			configurations = new BoilerplateMap<String,String>();
			loadPropertyFile();
			cacheProvider = configurations.get("CacheProvider");
			cache = CacheFactory.getInstance(cacheProvider,configurations);
		}
		return CacheFactory.cache;
	}
	
	public static ICache getInstance(String cacheProvider,
			BoilerplateMap<String,String> configurations)throws Exception{
		switch (cacheProvider){
		case "BoilerplateNonProductionInMemoryCache" : 
			return BoilerplateNonProductionInMemoryCache.getInstance();
		case "RedisCache":
			return RedisCache.getInstance(configurations);
		case "MemCache":
			return MemCache.getInstance();
		}
		throw new NotImplementedException();
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
					CacheFactory.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			//for each key that exists in the properties file put it into
			//the configuration map
			for(String key : properties.stringPropertyNames()){
				CacheFactory.configurations.put(key, properties.getProperty(key));
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
	}//end method
}
