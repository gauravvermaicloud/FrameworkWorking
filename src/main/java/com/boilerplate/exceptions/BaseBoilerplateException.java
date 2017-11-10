package com.boilerplate.exceptions;

import com.boilerplate.framework.Logger;

/**
 * This is the Base boiler plate exception. All
 * exceptions in the project are derived from this.
 * @author gaurav
 */
public class BaseBoilerplateException extends Exception{
	
	Logger logger = Logger.getInstance(BaseBoilerplateException.class);
	/**
	 * This is the message which will be shown to the end client.
	 */
	ClientFacingExceptionMessage clientFacingExceptionMessage;
	
	/**
	 * Returns the object for end consumer consumption
	 * @return An instance of the client message
	 */
	public ClientFacingExceptionMessage getClientFacingExceptionMessage(){
		return this.clientFacingExceptionMessage;
	}
	/**
	 * This is the name of the entity causing exception
	 */
	private String entityName;
	
	/**
	 * This is the cause of the exception
	 */
	private String reason;
	
	/**
	 * This is the internal exception
	 */
	private Exception innerException;
	
	/**
	 * This is the name of the entity causing the exception
	 * @return The name of the entity causing exception.
	 */
	public String getEntityName() {
		return this.entityName;
	}
	
	/**
	 * This is the reason the exception was caused
	 * @return The reason for exception
	 */
	public String getReason() {
		return this.reason;
	}
	
	/**
	 * This is the internal exception
	 * @return The internal exception
	 */
	public Exception getInternalException() {
		return this.innerException;
	}
	
	/**
	 * This is the cconstructor for the boiler plate exception
	 * @param entityName This is the name of the entity for example 
	 * User or Widget etc
	 * @param reason This is the reason for the exception
	 * @param innerException This is the inner exception
	 */
	public BaseBoilerplateException(String entityName,
		String reason, Exception innerException){
			this.entityName = entityName;
			this.reason = reason;
			this.innerException = innerException;
			this.clientFacingExceptionMessage = 
					new ClientFacingExceptionMessage(this.getEntityName(),this.getMessage() 
							, this.getReason());
		}
	
	/**
	 * This creates an instance of the exception
	 * @param entityName
	 * @param reason
	 */
	public BaseBoilerplateException(String entityName, String reason){
		this.entityName = entityName;
		this.reason = reason;
		this.clientFacingExceptionMessage = 
				new ClientFacingExceptionMessage(this.getEntityName(),this.getMessage() 
						, this.getReason());
	}
}
