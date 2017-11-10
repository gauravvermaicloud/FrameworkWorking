package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A generic list which encapsulates entity within to adhear to REST sepcifications"
, description="This is used when a list of items is request or response to any api")
public class GenericListEncapsulationEntity<T> implements Serializable{

	/**
	 * This is a list of objects
	 */
	@ApiModelProperty(value="The list of objects")
	private List<T> entityList;

	/**
	 * Gets the entity list
	 * @return a list of entities
	 */
	public List<T> getEntityList() {
		return entityList;
	}

	/**
	 * Sets the entity list
	 * @param entityList The entity list
	 */
	public void setEntityList(List<T> entityList) {
		this.entityList = entityList;
	}
}
