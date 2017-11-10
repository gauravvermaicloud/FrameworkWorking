package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;

/**
 * This has interfaces for user management
 * @author gaurav
 *
 */
public interface IUser {
	
	/**
	 * This method creates a user in the database.
	 * @param user This is the user to be created.
	 * @throws ConflictException This is thrown if the user already exists in the database for
	 * the given provider.
	 * @return The created user.
	 */
	public ExternalFacingUser create(ExternalFacingUser user) throws ConflictException;

	
	/**
	 * This method is used to get a user from database given the userId
	 * @param userId The id of the user
	 * @param roleIdMap The map of user roles with there id's
	 * @return an instance of the user.
	 * @throws NotFoundException if the user doesnt exist
	 */
	public ExternalFacingReturnedUser getUser(String userId
			, BoilerplateMap<String, Role> roleIdMap) throws NotFoundException ;

	/**
	 * This method deletes the given user
	 * @param user The user to be deleted
	 */
	public void deleteUser(ExternalFacingUser user); 

	
	public boolean userExists(String userId);


	ExternalFacingReturnedUser update(ExternalFacingReturnedUser user) throws ConflictException;

}
