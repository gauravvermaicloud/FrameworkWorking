package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class UpdateFailedException  extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public UpdateFailedException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}


}
