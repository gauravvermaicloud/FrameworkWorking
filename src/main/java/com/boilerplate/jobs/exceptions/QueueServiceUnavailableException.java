package com.boilerplate.jobs.exceptions;

import com.boilerplate.exceptions.BaseBoilerplateException;

/**
 * Instance of the queue service not available
 * @author gaurav
 *
 */
public class QueueServiceUnavailableException extends BaseBoilerplateException{

	/**
	 * Creates a new instance of queue service not available
	 * @param entityName The entity which was accessing the queue
	 * @param reason The cause of the excetpion
	 */
	public QueueServiceUnavailableException(String entityName, String reason) {
		super(entityName, reason);
	}

}
