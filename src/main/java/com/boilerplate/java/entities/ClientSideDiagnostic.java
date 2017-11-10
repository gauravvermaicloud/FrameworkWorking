package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A client side diagnostic message", description="A client side diagnostic message", parent=BaseEntity.class)
public class ClientSideDiagnostic extends BaseEntity implements Serializable{

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

	/**
	 * This is the id of the user
	 */
	@ApiModelProperty(value="The Id of the user")
	String userId;
	
	/**
	 * This is the session id of the user
	 */
	@ApiModelProperty(value="The id of the session")
	String sessionId;
	
	/**
	 * This is the location of origination of message
	 */
	@ApiModelProperty(value="This is the location of origination of message")
	String locationIdentifier;
	
	/**
	 * This is location event
	 */
	@ApiModelProperty(value="This is location of event")
	String locationEvent;
	
	/**
	 * This is log message
	 */
	@ApiModelProperty(value="This is log message")
	String message;
	
	/**
	 * This is any custom data 
	 */
	@ApiModelProperty(value="This is any custom data ")
	String customData;
	
	/**
	 * This is the name of application from which the message was sent
	 */
	@ApiModelProperty(value="This is the name of application from which the message was sent")
	String applicationName;
	
	
	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLocationIdentifier() {
		return locationIdentifier;
	}

	public void setLocationIdentifier(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}

	public String getLocationEvent() {
		return locationEvent;
	}

	public void setLocationEvent(String locationEvent) {
		this.locationEvent = locationEvent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}
}
