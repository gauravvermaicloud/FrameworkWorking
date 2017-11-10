package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.service.interfaces.IUserRoleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

@Api(description="This api has controllers for User-Role CRUD operations"
,value="User Role API's",basePath="/user/{userId}/role")
@Controller
public class UserRoleController extends BaseController{

	/**
	 * This is the user role service
	 */
	@Autowired
	IUserRoleService userRoleService;
	
	/**
	 * This api sets the user role service
	 * @param userRoleService
	 */
	public void setUserRoleService(IUserRoleService userRoleService){
		this.userRoleService = userRoleService;
	}
	
	/**
	 * Adds a user to given set of roles
	 * @param userId The user id to which roles are to be granted
	 * @param roles The list of roles to be granted
	 * @throws NotFoundException This is thrown if the user or the role is not found
	 * @throws UnauthorizedException This is thrown if the user is not allows to grant the given
	 * role
	 * @throws BadRequestException If the complete and required user information is not sent back
	 */
	@RequestMapping(value = "/user/{userId}/role", method = RequestMethod.POST)
	public  @ResponseBody void addUserToRole(
			@ApiParam(value="This is the id of the user to which roles are to be added"
			,required=true,name="userId",allowMultiple=false)@PathVariable String userId
			,@RequestBody(required=true) GenericListEncapsulationEntity<String> roles) 
					throws NotFoundException,UnauthorizedException, BadRequestException, ConflictException{
		
		//this is a good example how service layer should be shielded from
		//hidden inputs if possible
		//we are keeping session on the thread context and could have found
		//the user from the thread but that would mean that the underlying layer
		//have some sense of the fact this is a web application and the magical thread
		//hence it is best to provide any such information explictly 
		//rather than hoping thread or context will have it this is because
		//1. It will make unit testing harder
		//2. If we want to make gross framework changes it will be harder
		//3. This magical manner for informaiton to appear is always a bad thing
		
		//In this code we request the role to be granted
		this.userRoleService.grantUserRoles(userId, roles.getEntityList()
				, super.getSession().getExternalFacingUser());
	}
	
	/**
	 * Removes user from role
	 */
	public void removeUserRole(){
		
	}
	
}
