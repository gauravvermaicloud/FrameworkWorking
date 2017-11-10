package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.entities.Configuration;

/**
 * This interface is implemented by the data store to obtain 
 * configuration details.
 * @author gaurav
 */
public interface IConfigurations {
	/**
	 * This method returns the configurations for the given version.
	 * @param version This is the version of configuration.
	 * The system fetches a union of ALL and version.
	 * If two configuration values have same keys, then the 
	 * version specific configuration overrides the configuration.
	 * @param enviornment This is the name of the enviornment
	 * @return A List of all configuration items
	 */
	public List<Configuration> getConfirguations(String version,String enviornment);
}
