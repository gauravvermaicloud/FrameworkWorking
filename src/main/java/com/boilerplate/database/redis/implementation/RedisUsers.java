package com.boilerplate.database.redis.implementation;

import java.util.Set;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;

import redis.clients.jedis.Jedis;

/**
 * This class is a redis implementation of user
 * @author gaurav.verma.icloud
 *
 */
public class RedisUsers extends BaseRedisDataAccessLayer implements IUser{

	/**
	 * This is the key used to store user mapping against experian emails
	 */
	
		
	public String OrganizationProductUserMapping = "ORGANIZATIONPRODUCT:USER:";
	
	/**
	 * This is a user mapping for storing against user key
	 */
	private static final String UserKey ="USER_KEY:";
	
	
	private static final String User = "USER:";
	
	/**
	 * @see create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser) throws ConflictException {
		
		ExternalFacingReturnedUser user = new ExternalFacingReturnedUser(externalFacingUser);
		if(user.getRoles() == null){
			user.setRoles(new BoilerplateList<Role>());
		}
		
		if(super.get(user.getUserId()) !=null){
			 new ConflictException("User","User with given id already exists",null);
		}
		
		//save user
		user.setId(user.getUserId());
		super.set(User+user.getUserId(), user);
		return user;
	}

	/**
	 * @see update
	 */
	
	

	/**
	 * @see getUser
	 */
	@Override
	public ExternalFacingReturnedUser getUser(String userId, BoilerplateMap<String, Role> roleIdMap)
			throws NotFoundException {
		ExternalFacingReturnedUser user =  super.get(User+userId, ExternalFacingReturnedUser.class);
		if(user ==null) throw new NotFoundException("User", "No records found with this mobile", null);
		if(user.getUserStatus() == 0){
			 throw new NotFoundException("User", "User with Id = "+userId+"not found", null);
		}
		return user;
	}
	
	/**
	 * @see getUser
	 */
	@Override
	public boolean userExists(String userId) {
		Jedis jedis =null;
		
		try{
			jedis = super.getConnection();
		return jedis.exists(User+userId);
		}
		finally{
			if(jedis!=null){
				jedis.close();
			}
		}
	}

	/**
	 * @see deleteUser
	 */
	@Override
	public void deleteUser(ExternalFacingUser user) {
		super.delete(user.getUserId());
		
	}
	private static final String PASSWORD_ENCRYPTED = "PASSWORD ENCRYPTED";

	@Override
	public ExternalFacingReturnedUser update(ExternalFacingReturnedUser user) throws ConflictException {
		//save user
		if(user.getPassword().toUpperCase().equals(PASSWORD_ENCRYPTED)){
			ExternalFacingReturnedUser userFromDatabase = super.get(User+user.getUserId(), ExternalFacingReturnedUser.class);
			user.setPassword(userFromDatabase.getPassword());
		}
		super.set(User+user.getUserId(), user);
		
		//save mapping against experian request email key
		
		
		
		return user;
		
		
	}

	
	


}
