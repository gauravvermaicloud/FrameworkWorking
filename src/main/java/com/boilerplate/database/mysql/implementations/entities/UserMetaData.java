package com.boilerplate.database.mysql.implementations.entities;

/**
 * This class implements user metadata. The reason it is implemented as an
 * internal class is due to the nature of DB implementation. Ideally we want to
 * expose this information as a map, however given we have made choice of MySql
 * with additional fields of id and UserId in a seperate join table along with
 * Hibernate such implementation is not trivial, but this implementation is
 * specific to MySQL Users only, hence we implement this class.
 * 
 * I am not sure if this is a bad design or an anti pattern as I implement this,
 * and this should be reviewed from both design perspective and code.
 * 
 * There is also a further possibility that I am not aware of doing this work
 * with some clever mapping in hibernate and removing the need if any for this
 * kind of mapping.
 * 
 * An alternate implementation could have been saving all this information as a
 * JSON in User table but this was not done (while we did it in case of Session)
 * was because we expect this to grow and be used in reports.
 * 
 * Metadata is expected to be used in things like address etc which was not put
 * in user table unless someone wants to make them first class citizens and
 * extend the existing user model.
 * 
 * @author gaurav.verma.icloud
 *
 */
public class UserMetaData {
	/**
	 * The id of the metadata
	 */
	private long id;

	/**
	 * The user id of the user with which this metadata is assocuated
	 */
	private long userId;

	/**
	 * The key of the metadata. There is only one key for a given user
	 */
	private String metaDataKey;

	/**
	 * The value of metadata for the user
	 */
	private String metaDataValue;

	/**
	 * Gets the id of the metadata
	 * 
	 * @return The id of the metadata
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id of the metadata
	 * 
	 * @param id
	 *            The id of metadata
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * This method gets the user id of the user
	 * 
	 * @return The id of the user
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * This sets the user id of the user
	 * 
	 * @param userId
	 *            The user id of the user
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * This gets the metadata key
	 * 
	 * @return The metadata key
	 */
	public String getMetaDataKey() {
		return metaDataKey;
	}

	/**
	 * This method sets the key of metadata
	 * 
	 * @param metaDataKey
	 *            The key of metadata
	 */
	public void setMetaDataKey(String metaDataKey) {
		this.metaDataKey = metaDataKey;
	}

	/**
	 * This method gets metadata valye
	 * 
	 * @return gets the metadata value
	 */
	public String getMetaDataValue() {
		return metaDataValue;
	}

	/**
	 * sets the metdata value
	 * 
	 * @param metaDataValue
	 *            The value to be set
	 */
	public void setMetaDataValue(String metaDataValue) {
		this.metaDataValue = metaDataValue;
	}

}