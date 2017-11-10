package com.boilerplate.java.entities;

import java.io.Serializable;

/**
 * This class is used contain one configuration item.
 * @author gaurav
 *
 */
public class Configuration implements Serializable {

	/**
	 * The id of the configuration
	 */
	long id;
	
	/**
	 * The key of the configuration.
	 */
	String key;
	
	/**
	 * The value of configuration
	 */
	String value;
	
	/**
	 * The version of configuration.
	 */
	String version;

	/**
	This is the name of the enviornment
	*/
	String enviornment;
	
	
	/**
	 * Creates a configuration entity
	 */
	public Configuration(){
		
	}
	
	/**
	 * This method creates a configuration entity
	 * @param key The config key
	 * @param value The config value
	 * @param version The version of config
	 * @param enviornment The enviornment
	 */
	public Configuration(String key,String value, String version, String enviornment){
		this.key = key;
		this.value = value;
		this.version = version;
		this.enviornment= enviornment;
	}
	
	/**
	 * This method returns the id of the configuration
	 * @return The id of the configuration.
	 */
	public long getId() {
		return id;
	}

	/**
	 * This method sets the id of the configuration
	 * @param id The id of the configuration.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * This method gets the key of the configuration.
	 * @return The key of the configuration.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * This method sets the key of the configuration
	 * @param key The key of the configuration
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * This method gets the value for the given configuration
	 * key
	 * @return The value of configuration.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * This method sets the value of configuration
	 * @param value The value of configuration.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * This method gets the version of configuration
	 * @return  The version of configuration
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * This method sets the version of configuration.
	 * @param version The version of configuration.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * This method gets the enviornment name of configuration
	 * @return  The enviornment of configuration
	 */
	public String getEnviornment() {
		return version;
	}

	/**
	 * This method sets the enviornment name of configuration.
	 * @param enviornment The enviornment of configuration.
	 */
	public void setEnviornment(String enviornment) {
		this.enviornment = enviornment;
	}
}
