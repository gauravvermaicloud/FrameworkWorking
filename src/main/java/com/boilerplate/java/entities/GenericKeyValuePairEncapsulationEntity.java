package com.boilerplate.java.entities;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@ApiModel(value="A generic key value pair entity")
public class GenericKeyValuePairEncapsulationEntity implements Serializable{
	/**
	 * The key of the entity
	 */
	@ApiModelProperty(value="The key of the entity")
	private String key;
	
	/**
	 * This is the value of the entity
	 */
	@ApiModelProperty(value="The value of the entity")
	private String value;

	/**
	 * This is the key
	 * @return The key
	 */
	public String getKey() {
		return key;
	}


	/**
	 * Sets the key
	 * @param key The key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the value
	 * @return The value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value
	 * @param value The value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
