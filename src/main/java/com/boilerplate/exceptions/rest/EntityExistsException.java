package com.boilerplate.exceptions.rest;

import com.boilerplate.exceptions.BaseBoilerplateException;

public class EntityExistsException  extends BaseBoilerplateException {

	/**
	 *@see BaseBoilerplateException 
	 */
	public EntityExistsException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}


}
