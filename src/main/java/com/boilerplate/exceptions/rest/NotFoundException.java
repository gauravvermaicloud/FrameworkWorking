package com.boilerplate.exceptions.rest;

import java.io.Serializable;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.boilerplate.exceptions.BaseBoilerplateException;

public final class NotFoundException  extends BaseBoilerplateException implements Serializable{

	/**
	 *@see BaseBoilerplateException 
	 */
	public NotFoundException(String entityName,
			String reason, Exception innerException){
		super(entityName,reason,innerException);
	}
	
	@Override
	public String toString(){
		return super.getEntityName()+":"+super.getReason();
	}
}
