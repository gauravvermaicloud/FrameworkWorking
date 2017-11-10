package com.boilerplate.database.redis.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;

public class RedisUserRoles extends BaseRedisDataAccessLayer implements IUserRole{

	@Autowired private RedisUsers redisUsers;
	
	public void setRedisUsers(RedisUsers redisUsers){
		this.redisUsers =redisUsers;
	}
	
	@Override
	public void grantUserRole(ExternalFacingUser user, List<Role> roles) throws NotFoundException, ConflictException {
		ExternalFacingReturnedUser returnedUser = this.redisUsers.getUser(user.getUserId(), null);
		for(Role role: roles){
			returnedUser.getRoles().add(role);
		}
		this.redisUsers.update(returnedUser);
	}

}
