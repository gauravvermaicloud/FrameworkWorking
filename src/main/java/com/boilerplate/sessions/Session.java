package com.boilerplate.sessions;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This class is used to implement a session.
 * It contains the session id along with a number of 
 * session objects
 * @author gaurav
 *
 */
@ApiModel(value="Session", description="A session entity", parent=BaseEntity.class)
public class Session extends BaseEntity implements Serializable{
	
	/**
	 * This is the session Id.
	 */
	@ApiModelProperty(value="This is the id of the session")
	private String sessionId;
	
	/**
	 * This is the user associated with the session.
	 */
	@ApiModelProperty(value="This is the user associated with the session")
	private ExternalFacingReturnedUser user;
	
	/**
	 * This method returns the external facing user
	 * @return The user for the session.
	 */
	public ExternalFacingReturnedUser getExternalFacingUser(){
		return this.user;
	}
	
	/**
	 * This is the map of session objects
	 */
	private BoilerplateMap<String, Object> sessionObjects;
	
	/**
	 * This returns a session object
	 * @param user The user whose session is being created
	 */
	public Session(ExternalFacingReturnedUser user){
		this.setSessionId(UUID.randomUUID().toString().toUpperCase());
		this.setUserId(user.getId());
		this.user = user;
		super.setCreationDate(new Date());
		super.setUpdationDate(new Date());
	}

	/**
	 * Default constructor for use with hibernate
	 */
	public Session(){
		
	}
	
	/**
	 * This method sets the user for the session
	 * @param user The user for the session
	 */
	public void setExternalFacingUser(ExternalFacingReturnedUser user){
		this.user = user;
	}
	
	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		//session is valid if it is not expired
		long nowTime = new Date().getTime();
		long lastUpdateTime = this.getUpdationDate().getTime();
		long diff =(nowTime-lastUpdateTime)/1000l;
		int timeOut = SessionManager.getSessionTimeout();
		if(diff>timeOut){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

	/**
	 * The user id associated with the session
	 */
	@JsonIgnore
	private String userId;
	
	/**
	 * This methods returns the session id
	 * @return Returns a session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * This method sets a session id
	 * @param sessionId The session id 
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Sets the user id
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the user id
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 *A map to store custom session attributes. This map should not be a persistant store
	 *and will be ignored by JSON in interest of security
	 */
	@JsonIgnore
	private BoilerplateMap<String,Object> sessionAttribute = new BoilerplateMap();
	
	/**
	 * This method adds a custom attribute to session 
	 * @param key The key of the attribute
	 * @return The value of the attribute
	 */
	public Object getSessionAttribute(String key){
		return this.sessionAttribute.get(key);
	}
	
	/**
	 * This method adds a custom attribute to session
	 * @param key The key of the attribute
	 * @param attribute The value of the attribute
	 */
	public void addSessionAttribute(String key, Object attribute){
		this.sessionAttribute.put(key, attribute);
	}
}
