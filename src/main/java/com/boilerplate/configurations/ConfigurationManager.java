package com.boilerplate.configurations;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;

/**
 * This class is the configuration manager.
 * It loads all the configuration from various sources - property file,
 * database, enviornment variables and jvm arguments.
 * 
 * As a strategy we would like to keep as much information in database Configurations table
 * as possible this is because it will allow changes to be made in one place only and be available
 * to all the systems using the same.
 * 
 * The class has been implemented as a singleton with read only access to properties.
 * Classes may extend this class to have type safety in the values returned.
 * @author gaurav
 *
 */
public class ConfigurationManager implements IConfiguratonManager{
	/**
	 * This is the configuration
	 */
	private boolean Configuration = false;

	/**
	 * An instance of autowired database confirgutation
	 */
	@Autowired
	public IConfigurations databaseConfiguration;
	
	/**
	 * Sets the DB configuration
	 * @param databaseConfiguration Instance of DB config
	 */
	@Override
	public void setDatabaseConfiguration(IConfigurations databaseConfiguration) {
		this.databaseConfiguration = databaseConfiguration;
		
	}
	
	/**
	 * This method initialises the configuration store by loading configuration from
	 * the property file, enviornment variables, JVM arguments and DB.
	 * It is recomended that the DB be the primary source of all configuration.
	 */
	@Override
	public void initialize(){
		this.configurations = new BoilerplateMap<String,String>();
		this.jvmArguments = new BoilerplateList<String>();
		//load config from properties
		this.loadPropertyFile();
		//load config from enviornment variables
		this.loadEnviornmentVariables();
		//load config from JVM
		this.loadJVMArguments();
		//load config from DB
		this.loadDatabaseConfiguration();
	}
	
	/**
	 * resets and force relods the configuration
	 */
	public void resetConfiguration(){
		initialize();
	}
	
	/**
	 * This is a map of all the configuration values.
	 */
	private BoilerplateMap<String,String> configurations = null;

	/**
	 * This is the list of JVM arguments
	 */
	private BoilerplateList<String> jvmArguments = null;

	/**
	 * Returns list of configurations
	 * @return
	 */
	public  BoilerplateMap<String,String> getConfigurations(){
		return this.configurations;
	}
	
	/**
	 * This creates an instance of configuration manager which will be used.
	 * config values from various sources are loaded here.
	 */
	public ConfigurationManager(){
	}
	
	/**
	 * This method loads configuration from the database.
	 * The version of the application as in .properties file is loaded along with any
	 * item where version is ALL
	 * This allows same DB to be run against multiple versions of the app.
	 */
	private void loadDatabaseConfiguration() {
		//Get the version of the the config from the properties file
		for(Configuration confirguration : 
			databaseConfiguration.getConfirguations(
					//for each value found in DB put it in the config key store.
					this.configurations.get("Version"),
					this.configurations.get("Enviornment"))){
			this.configurations.put(confirguration.getKey()
					, confirguration.getValue());
		}
	}

	/**
	 * This method loads the JVM arguments
	 */
	private void loadJVMArguments() {
		//gets all the JVM arguments
		for(String jvmArgument : 
			ManagementFactory.getRuntimeMXBean().getInputArguments()){
				this.configurations.put(jvmArgument, jvmArgument);
				this.jvmArguments.add(jvmArgument);
		}
	}

	/**
	 * This method loads the enviornment variables
	 */
	private void loadEnviornmentVariables() {
		//get a list of enviornment variables
		for(String enviornmentVarable :System.getenv().keySet() ){
			//load the values
			this.configurations.put(
					enviornmentVarable,System.getenv().get(enviornmentVarable));
		}
	}

	/**
	 * This method returns the configuration for the given key
	 * @param key The key of configuration
	 * @return The value of configuration
	 */
	@Override
	public String get(String key){
		return this.configurations.get(key);
	}
	
	/**
	 * This method returns a list of all the JVM arguments.
	 * @return A list of all the JVM arguments passed.
	 */
	public BoilerplateList<String> getJVMArguments(){
		return this.jvmArguments;
	}
	
	/**
	 * This method returns a set of all the configuration keys
	 * @return A set of all the keys.
	 */
	public Set<String> getConfigurationKeys(){
		return this.configurations.keySet();
	}
	/**
	 * This method loads configuration from the property file
	 */
	private void loadPropertyFile(){
		Properties properties  = null;
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					this.getClass().getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			//for each key that exists in the properties file put it into
			//the configuration map
			for(String key : properties.stringPropertyNames()){
				this.configurations.put(key, properties.getProperty(key));
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
}//end class
