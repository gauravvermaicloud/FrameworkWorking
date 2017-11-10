package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * This is the base algorithm configuration
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="BaseAlgorithmConfiguration", description="BaseAlgorithmConfiguration", parent=BaseEntity.class)
public class BaseAlgorithmConfiguration extends BaseEntity implements Serializable{

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return this;
	}

}
