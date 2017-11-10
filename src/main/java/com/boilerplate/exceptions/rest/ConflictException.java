package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class ConflictException extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public ConflictException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}

}
