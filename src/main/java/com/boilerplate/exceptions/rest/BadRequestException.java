package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class BadRequestException extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public BadRequestException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}
}
