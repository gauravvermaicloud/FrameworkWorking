package com.boilerplate.configurations;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This is the insance of configuration manager
 * @author gaurav.verma.icloud
 *
 */
public interface IConfiguratonManager {
	
	/**
	 * This sets the DB configuration
	 * @param databaseConfiguration The DB configuration
	 */
	public void setDatabaseConfiguration(IConfigurations databaseConfiguration);
	
	/**
	 * Initializes the configuration
	 */
	public void initialize();
	
	/**
	 * Gets the configuration based on key for given version and enviornment
	 * @param key The key
	 * @return The value
	 */
	public String get(String key);
	
	/**
	 * Gets the entire configuration map for the given version and enviornment
	 * @return
	 */
	public  BoilerplateMap<String,String> getConfigurations();
	
	/**
	 * Reloads configuration from all sources
	 */
	public void resetConfiguration();
}
