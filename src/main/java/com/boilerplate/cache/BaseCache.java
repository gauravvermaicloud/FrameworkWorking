package com.boilerplate.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is a base class to all caches, its primary purpose is more
 * like a cache helper to provide the prefix for the cache key
 * @author gaurav
 *
 */
public class BaseCache {
	/**
	 * This method provides a concatination of
	 * ApplicationName:EnviornmentName:Version which can be used to 
	 * concateinate with cache key.
	 * @return A sting with cache prefix as per desceiption
	 */
	protected static String getCachePrefix(){
		// we are reading the property file instad of using 
		// configuration which is not a good thing
		//but because the methods to use the said code are static hence
		//this code is being rewritten which is a bad thing.
		//ideally we need to figure out how to read this from
		//the configuration
		
		Properties properties  = null;
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					BaseCache.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			return 	properties.getProperty("ApplicationName")+":"+
					properties.getProperty("Enviornment")+":"+
					properties.getProperty("Version");
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
		return null;
	}//end method
}
