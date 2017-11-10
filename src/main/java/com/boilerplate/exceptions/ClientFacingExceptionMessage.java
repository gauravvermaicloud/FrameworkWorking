package com.boilerplate.exceptions;

import java.io.Serializable;
/**
 * This is the message to be sent to the end client.
 * This class could have been avoided by selectivly serializing elements
 * of BaseBoilerplateeException, however for security reasons a seperate class
 * is made
 * @author gaurav
 *
 */
public final class ClientFacingExceptionMessage implements Serializable{

	/**
	 * This is the default constructor
	 * @param entity This is the entity causing exception.
	 * @param message The message
	 * @param cause The cause of exception
	 */
	public ClientFacingExceptionMessage(String entity, String message, String cause){
		this.entity = entity;
		this.message = message;
		this.cause = cause;
	}
	/**
	 * The entity
	 */
	private String entity;
	
	/**
	 * The message
	 */
	private String message;
	
	/**
	 * The cause
	 */
	private String cause;

	/**
	 * The entity
	 * @return   The entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * The message
	 * @return The message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The cause
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}

	
}
