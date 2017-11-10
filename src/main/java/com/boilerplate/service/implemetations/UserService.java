package com.boilerplate.service.implemetations;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.activemq.console.command.DstatCommand;
import org.bouncycastle.jce.provider.asymmetric.ec.Signature.ecCVCDSA;
import org.bouncycastle.ocsp.Req;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendRegistrationEmailObserver;
import com.boilerplate.asyncWork.SendSMSOnPasswordChange;
import com.boilerplate.cache.CacheFactory;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.Constants;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.MethodState;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.queue.QueueFactory;
import com.boilerplate.service.interfaces.IRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;
import java.util.Collections;

//import com.fasterxml.jackson.core.type.TypeReference;
import org.codehaus.jackson.type.TypeReference;


public class UserService implements IUserService {

	
	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);
	
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 */
	@Autowired
	com.boilerplate.asyncWork.SendRegistrationSMSObserver sendRegistrationSMSObserver;
	
	/**
	 * This is the observer for sending SMS for user registration
	 * @param sendRegistrationSMSObserver The sms observer
	 */
	public void setSendRegistrationSMSObserver(com.boilerplate.asyncWork.SendRegistrationSMSObserver sendRegistrationSMSObserver ){
		this.sendRegistrationSMSObserver = sendRegistrationSMSObserver;
	}
	

	
	/**
	 * This is the observer to send email
	 */
	@Autowired
	SendRegistrationEmailObserver sendRegistrationEmailObserver;
	
	/**
	 * Sets the observer for sending email
	 * @param sendRegistrationEmailObserver Sending email observer
	 */
	public void setSendRegistrationEmailObserver(SendRegistrationEmailObserver sendRegistrationEmailObserver){
		this.sendRegistrationEmailObserver = sendRegistrationEmailObserver;
	}
	
	/**
	 * The setter to set the configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager 
			configurationManager){
		this.configurationManager = configurationManager;
	}
	
	
	
	/**
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;
	
	/**
	 * This sets the session manager
	 * @param sessionManager The session manager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;
	
	/**
	 * This is the instance of the role service
	 */
	@Autowired
	private IRoleService roleService;
	
	/**
	 * Sets the role service
	 * @param roleService The role service
	 */
	public void setRoleService(IRoleService roleService){
		this.roleService = roleService;
	}
	
	/**
	 * This is the setter for user data acess
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser){
		this.userDataAccess = iUser;
	}
	
	/**
	 * This is the user role database access layer
	 */
	@Autowired
	private IUserRole userRole;
	
	/**
	 * This sets the user role
	 * @param userRole This is user role
	 */
	public void setUserRole(IUserRole userRole){
		this.userRole = userRole;
	}
	
	
	
	/**
	 * This is an instance of the queue job, to save the session
	 * back on to the database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	/**
	 * This sets the queue reader jon
	 * @param queueReaderJob The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	
	
	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();
	
	BoilerplateList<String> subjectsForAutomaticPasswordReset = new BoilerplateList();
	
	BoilerplateList<String> subjectForPasswordChange = new BoilerplateList();
	
	private int defaultUserStatus = 1;
	/**
	 * Initializes the bean
	 */
	public void initilize(){
		subjectsForCreateUser.add("CreateUser");
		subjectsForAutomaticPasswordReset.add("AutomaticPasswordReset");
		subjectForPasswordChange.add("PasswordChange");
		defaultUserStatus = Integer.parseInt(this.configurationManager.get("DefaultUserStatus")== null?"1":this.configurationManager.get("DefaultUserStatus"));
	}
	/**
	 * @throws BadRequestException 
	 * @throws NotFoundException 
	 * @see IUserService.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser) throws ValidationFailedException,
		ConflictException, NotFoundException, BadRequestException{
		//if password is blank set it
		Long otpPassword = 0L;
		if(externalFacingUser.getPassword()==null || externalFacingUser.getPassword().isEmpty()){
			
			otpPassword =Math.abs(UUID.randomUUID().getMostSignificantBits());
			externalFacingUser.setPassword(Long.toString(otpPassword).substring(0,6));
			otpPassword = Long.parseLong(Long.toString(otpPassword).substring(0,6));
		}
			
						
		
		String passwordReceived = externalFacingUser.getPassword();
		//if user Id is blank set it
		if(externalFacingUser.getUserId()==null || externalFacingUser.getUserId().isEmpty()){
			externalFacingUser.setUserId(externalFacingUser.getPhoneNumber());
		}
		externalFacingUser.validate();
		
		if(externalFacingUser.getAuthenticationProvider() ==null){
			externalFacingUser.setAuthenticationProvider(
					this.configurationManager.get("DefaultAuthenticationProvider"));
		}
		
		
		
		//check if a user with given Id exists
		if(this.userExists(externalFacingUser.getAuthenticationProvider()
				+":"+externalFacingUser.getUserId())){
			throw new ConflictException("User","User already exist with this mobile", null);
		}
		/*check if a user with given email exists then throw conflict 
		exception otherwise put the email entry in email list hash*/
		
		externalFacingUser.setUserId(externalFacingUser.getAuthenticationProvider()
				+":"+externalFacingUser.getUserId());
		
		//before save lets hash the password
		externalFacingUser.hashPassword();
		//set create and update date
		externalFacingUser.setCreationDate(new Date());
		externalFacingUser.setUpdationDate(externalFacingUser.getCreationDate());
		externalFacingUser.setUserStatus(this.defaultUserStatus);
		externalFacingUser.setUserKey(UUID.randomUUID().toString());
		
		if(externalFacingUser.getReferalSource() == null){
			externalFacingUser.setReferalSource("None");
		}
		
		if(externalFacingUser.getReferalSource().isEmpty()){
			externalFacingUser.setReferalSource("None");
		}
		
		//call the database to save the user
		externalFacingUser =  (ExternalFacingUser) userDataAccess.create(externalFacingUser).transformToExternal();
		//Check for is DSA ROle
		if(externalFacingUser.getAuthenticationProvider().equalsIgnoreCase(
				this.configurationManager.get("DSAAuthenticationProvider"))){
			ExternalFacingReturnedUser user = new ExternalFacingReturnedUser(externalFacingUser);
			BoilerplateList<Role> rolesList = new BoilerplateList<>();
			rolesList.add(new Role("DSA","DSA","DSA of the system",true,false));
			this.userRole.grantUserRole(user, rolesList);
		}
		
		//publish the created user
		ExternalFacingUser externalFacingUserClone= null;
		try{
			externalFacingUserClone = (ExternalFacingUser)externalFacingUser.clone();
			externalFacingUserClone.setPassword(passwordReceived);
			queueReaderJob.requestBackroundWorkItem(
					externalFacingUserClone, subjectsForCreateUser, "UserService", "create");
		}	
		catch(Exception ex){
			//now if the queue is not working we send sms and email on the thread
			try{
				sendRegistrationSMSObserver.sendSMS(
						externalFacingUserClone.getFirstName(), externalFacingUserClone.getPassword()
						, externalFacingUserClone.getPhoneNumber());
				sendRegistrationSMSObserver.sendCMDWelcomeSMS(externalFacingUserClone.getPhoneNumber());
			}
			catch(Exception exSms){
				//if an exception takes place here we cant do much hence just log it and move forward
				logger.logException("UserService", "create", "try-Queue Reader - Send SMS", exSms.toString(), exSms);
			}
			
			try{
				BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
				tosEmailList.add(externalFacingUserClone.getEmail());
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
				sendRegistrationEmailObserver.sendEmail(externalFacingUserClone.getFirstName(),tosEmailList, ccsEmailList, bccsEmailList
					,externalFacingUserClone.getPhoneNumber(),externalFacingUserClone.getUserKey());
						
			}
			catch(Exception exEmail){
				//if an exception takes place here we cant do much hence just log it and move forward
				logger.logException("UserService", "create", "try-Queue Reader - Send Email", exEmail.toString(), exEmail);
			}
			
			logger.logException("UserService", "create", "try-Queue Reader", ex.toString(), ex);
		}
		//we dont want to share the hash hence sending bacj the text
		externalFacingUser.setPassword("Password Encrypted");
		//generate otp list 
		
		return externalFacingUser;
	}

	
	private boolean userExists(String userId) {
		return this.userDataAccess.userExists(userId);
	}

	@Override
	public  String normalizeUserId(String userId){
		
		userId = userId.toUpperCase();
		//check if user id contains :
		if(userId.contains(":") ==false){
			//check if the user starts with DEFAULT:, if not then put in Default: before it
			if(!userId.startsWith(
						this.configurationManager.get("DefaultAuthenticationProvider").toUpperCase()+":")){
				
				userId = this.configurationManager.get("DefaultAuthenticationProvider")+":"+
								userId;
			}
		}
		return userId;
	}
	/** 
	 * @see IUserService.authenticate
	 */
	@Override
	public Session authenticate(AuthenticationRequest authenitcationRequest) throws UnauthorizedException{
		
		authenitcationRequest.setUserId(
				this.normalizeUserId(authenitcationRequest.getUserId()));
		ExternalFacingReturnedUser user =null;
		//Call the database and check if the user is
		//we store everything in upper case hence chanhing it to upper
		try{
			user = userDataAccess.getUser(
					authenitcationRequest.getUserId().toUpperCase()
					,roleService.getRoleIdMap());
		String hashedPassword = String.valueOf(Encryption.getHashCode(authenitcationRequest.getPassword()));
		if(!user.getPassword().equals(hashedPassword)){
			throw new UnauthorizedException("USER",
					"User name or password incorrect", null);
		}
		// check for dsa approval
		if(user.getUserId().startsWith(this.configurationManager.get("DSAAuthenticationProvider").toUpperCase()+":")){
			if(user.getApproved()!=null && !(user.getApproved().equals("1"))){
				throw new UnauthorizedException("USER",
						"Your account is waiting for CMD approval. Please contact us at 9599814087.", null);
			}
		
		
		}
		user.setPassword("Password Encrypted");
		
		//getthe roles, ACL and ther details of this user
		
		//if the user is valid create a new session, in the session add details 
		Session session = sessionManager.createNewSession(user);
		
		return session;
		}
		catch(NotFoundException nfe){
			logger.logException("UserService", "authenticate", "External Facing User: " +authenitcationRequest.getUserId().toUpperCase()+
					" With entered Password: " + authenitcationRequest.getPassword()+ " not found"
					, "Converting this exception to Unauthorized for security", nfe);
			throw new UnauthorizedException("USER",
					"User name or password incorrect", null);
		}
	}

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId) throws NotFoundException,BadRequestException {
		//retrun the user with password as a string
		return get(userId,true);
		
	}

	/**
	 * This method returns a user with given id
	 * @param userId The id of the user
	 * @param encryptPasswordString True if the user password to be 
	 * encryptied into a message string
	 * false if the password is to be sent as is
	 * @return The user entity
	 * @throws NotFoundException If the user is not found
	 * @ throws BadRequestException If the userId is not provided
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId
			, boolean encryptPasswordString) throws NotFoundException,BadRequestException {
		if(userId == null) throw new BadRequestException("User", "User Id not populated", null);
		//convert user names to upper
		userId = this.normalizeUserId(userId);
		//get the user from database
		ExternalFacingReturnedUser externalFacingUser = this.userDataAccess.getUser(
				userId,roleService.getRoleIdMap());
		//if no user with given id was found then throw exception
		if(externalFacingUser == null) throw new NotFoundException("ExternalFacingUser"
				, "User with id "+userId+" doesnt exist", null);
		//set the password as encrypted
		if(encryptPasswordString){
			externalFacingUser.setPassword("Password Encrypted");
		}
		//return the user
		return externalFacingUser;
		
	} 

	
	
	


	
}