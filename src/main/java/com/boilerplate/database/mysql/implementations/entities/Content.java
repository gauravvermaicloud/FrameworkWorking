package com.boilerplate.database.mysql.implementations.entities;

/**
 * This class represents the content in the system
 * 
 * @author gaurav.verma.icloud
 *
 */
public class Content {
	/**
	 * This is the id for the content
	 */
	private long id;

	/**
	 * This is the key for content
	 */
	private String Key;

	/**
	 * This is the value for content
	 */
	private String value;

	/**
	 * This is the locale for content
	 */
	private String locale;

	/**
	 * This is the version for content
	 */
	private String version;

	/**
	 * Gets the id
	 * 
	 * @return The id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the content key
	 * 
	 * @return The content key
	 */
	public String getKey() {
		return Key;
	}

	/**
	 * Sets the content key
	 * 
	 * @param key
	 *            The content key
	 */
	public void setKey(String key) {
		Key = key;
	}

	/**
	 * Gets the value for content
	 * 
	 * @return The value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value for content
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the locale for content
	 * 
	 * @return The locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Sets the locale for content
	 * 
	 * @param locale
	 *            The locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Gets the version of content
	 * 
	 * @return The version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version of content
	 * 
	 * @param version
	 *            The version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the user id
	 * 
	 * @return The user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * 
	 * @param userId
	 *            The user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * The user id who created the content
	 */
	private String userId;

}
