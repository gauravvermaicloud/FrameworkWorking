package com.boilerplate.java.entities;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="An input for authentication of user", 
	description="This is an entity for sending user credentials for authentication")
public class AuthenticationRequest implements Serializable{
	
	@ApiModelProperty(value="The name of the user, it should be in Default:UserName, if"
			+ "the same is not provided then Default is added to it"
			,required=true,notes="The name of the user to be authenticated")
	/**
	 * The name of the user
	 */
	private String userId;
	
	@ApiModelProperty(value="This is the password of the user"
			,required=true,notes="The password of the user")
	/**
	 * This is the password
	 */
	private String password;

	/**
	 * This returns the name of the use
	 * @return The user name
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * This method sets the user name
	 * @param userId The name of the user
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This returns the password
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This sets the password
	 * @param password The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
