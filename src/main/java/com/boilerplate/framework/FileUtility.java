package com.boilerplate.framework;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * This is used to create new files 
 * @author mohit
 *
 */
public class FileUtility {
	
	/**
	 * The propeorties for the email
	 */
	private static Properties properties  = null;
	/**
	 * This method creates new file with unique name
	 * @param fileData The file Data
	 * @return The file name
	 * @throws IOException The IO Exception
	 */
	public static String createNewFile(String fileData) throws IOException{
		if(properties == null){
			loadProperties();
		}
		String fileName = "file" + UUID.randomUUID();
		String filePath = properties.getProperty("AttachmentFileBasePath") + "/" + fileName;
		File file = new File(filePath);
		FileWriter writer = null;
		try{
	        if (file.createNewFile()){
	        	// creates a FileWriter Object
			    writer = new FileWriter(file); 
			    writer.write(fileData); 
	        }
		}
		catch(Exception exception){
			//we do not generally expect an exception here
			//and because we are start of the code even before loggers
			//have been enabled if something goes wrong we will have to print it to
			//console. We do not throw this exception because we do not expect it
			//and if we do throw it then it would have to be handeled in all code 
			//making it bloated, it is hence a safe assumption this exception ideally will not
			//happen unless the file access has  issues
			System.out.println(exception.toString());
		}
		finally{
			//close the input stream if it is not null
			if(writer !=null){
				try{
					writer.flush();
					writer.close();
				}
				catch(Exception ex){
					//if there is an issue closing it we just print it
					//and move forward as there is not a good way to inform user.
					System.out.println(ex.toString());
				}
			}
			}
		return fileName;
	}
	
	/**
	 * This method loads properties
	 */
	private static void loadProperties(){
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					EmailUtility.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
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
}