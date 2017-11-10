package com.boilerplate.database.mysql.implementations.entities;

/**
 * This is a MySQL Specific entity to manage user role mapping. It should be
 * noted that user id and role id are not mapped to the specific user role
 * because we do not want the lazy binding to be employed this is because this
 * information will be used either while creating a mapping or while filling the
 * user while login. When it comes to role id's they will be managed using cache
 * for performance and user id is not taken because this will cause to many
 * seek's
 * 
 * @author gaurav.verma.icloud
 *
 */
public class UserRoleMapping {
	/**
	 * This is the id of the user role mapping
	 */
	private long id;

	/**
	 * This is the id of the user
	 */
	private long userId;

	/**
	 * This is the id of the role
	 */
	private long roleId;

	/**
	 * Gets the user id
	 * 
	 * @return The user id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * 
	 * @param userId
	 *            The user id
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the role id
	 * 
	 * @return The role id
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id
	 * 
	 * @param roleId
	 *            The role id
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

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
}
