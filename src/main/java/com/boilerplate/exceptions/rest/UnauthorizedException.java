package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class UnauthorizedException extends BaseBoilerplateException {
	
	/**
	 *@see BaseBoilerplateException 
	 */
	public UnauthorizedException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}
}
