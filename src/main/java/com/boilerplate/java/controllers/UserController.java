package com.boilerplate.java.controllers;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on users. These inlcude
 * Creation, update and get user along with authenticate operation
 * @author gaurav
 *
 */
@Api(description="This api has controllers for User CRUD operations" ,value="User API's",basePath="/user")
@Controller
public class UserController extends BaseController{

	/**
	 * This is the instance of the user service
	 */
	@Autowired
	IUserService userService;
	
	@Autowired
	SessionManager sessionManager;
	
	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * This method is used to create a user.
	 * @externalFacingUser This is the user to be created.
	 * @throws ValidationFailedException This exception is thrown if the user name or password is blank
	 * @throws ConflictException This exception is thrown if the user already exists in the system
	 * @throws BadRequestException 
	 * @throws NotFoundException 
	 */
	@ApiOperation(	value="Creates a new User entity in the system"
					,notes="The user is unique in the system, The creation date and updated "
							+ "date are automatically filled."
				 )
	@ApiResponses(value={
							@ApiResponse(code=200, message="Ok")
						,	@ApiResponse(code=404, message="Not Found")
						,	@ApiResponse(code=400, message="Bad request, User name or password is empty")
						,	@ApiResponse(code=409, message="The user already exists in the system for the provider")	
						})
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public @ResponseBody ExternalFacingUser createUser(@RequestBody ExternalFacingUser externalFacingUser)
			throws ValidationFailedException,ConflictException, NotFoundException, BadRequestException{
		//call the business layer
		ExternalFacingUser userReturned =  userService.create(externalFacingUser);
		return userReturned;
	}
	
	/**
	 * This method authenticates a user with a given user name and password
	 * @param authenticationRequest The request for authenticate
	 * @return This returns a session entity of the user. This method also
	 * sets a header and cookie for session id.
	 * @throws UnauthorizedException This excption is thrown if the user name password
	 * is incorrect
	 */
	@ApiOperation(	value="Authenticates a user by passing user name, password for default provider"
			,notes="The user is unique in the system for a given provider."
					+ "The user may passed as a user id or as DEFAULT:userId"
		 )
@ApiResponses(value={
					@ApiResponse(code=200, message="Ok")
				,	@ApiResponse(code=404, message="Not Found")
				})
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public @ResponseBody Session authenticate(@RequestBody AuthenticationRequest 
			authenticationRequest) throws UnauthorizedException{
	
		//Call authentication service to check if user name and password are valid
		Session session = this.userService.authenticate(authenticationRequest);
		
		//now put sessionId in a cookie, header and also as response back
		super.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		
		boolean isDSA = (authenticationRequest.getUserId()).contains("DSA:");
		if (isDSA)
			super.addCookie(Constants.DsaAuthTokenCookieKey, session.getSessionId()
					, sessionManager.getSessionTimeout());
		else
			super.addCookie(Constants.AuthTokenCookieKey, session.getSessionId()
					,sessionManager.getSessionTimeout());
		
		//add the user id for logging, we have to explictly do it here only in this case all other cases
		//are handeled by HttpRequestInterseptor
		super.addHeader(Constants.X_User_Id, session.getExternalFacingUser().getUserId());
		RequestThreadLocal.setRequest(
				RequestThreadLocal.getRequestId(),
				RequestThreadLocal.getHttpRequest(), 
				RequestThreadLocal.getHttpResponse(),
				session); 
		return session;
	}
	
	/**
	 * Gets the currently logged in user
	 * @return The user
	 * @throws NotFoundException If user is not found
	 */
	@ApiOperation(	value="Gets currently logged in user"
		 )
	@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					,	@ApiResponse(code=404, message="Not Found")
					})
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody ExternalFacingReturnedUser getCurrentUser() throws Exception, NotFoundException,BadRequestException{
		return this.userService.get(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
	}
	
	/**
	 * This method updates a user with given id. The id is expected to be in form
	 * Provider:userId. If no provider is specified then it is defaulted to DEFAULT.
	 * @param userId This is the id of the user in the format Provider:user id
	 * @param updateUserEntity The user entity to be updated. 
	 * @return The user
	 * @throws NotFoundException If user is not found
	 */
	
	
	
	
	
}