package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class InvalidStateException extends BaseBoilerplateException {
	
	/**
	 *@see BaseBoilerplateException 
	 */
	public InvalidStateException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}

}
