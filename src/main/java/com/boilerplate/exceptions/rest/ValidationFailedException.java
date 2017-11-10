package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class ValidationFailedException  extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public ValidationFailedException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}

}
