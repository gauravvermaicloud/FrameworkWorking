package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;;

public class PreconditionFailedException extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public PreconditionFailedException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}
}
