package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the base entity of the system. All entities in the 
 * system must be derived from this. All entities are expected to be
 * serializable
 * @author gaurav
 */
public  abstract class BaseEntity extends Base implements Serializable{
	
	@ApiModelProperty(value="This is the id of the entity"
			,required=true,notes="The id is a String")
	/**
	 * This is the id of the entity.
	 */
	private String id;
	
	/**
	 * Gets the creation date of the entity.
	 * @return The creation date.
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date of the entity.
	 * @param creationDate The creation date.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the last updation date of the entity.
	 * @return The updation date.
	 */
	public Date getUpdationDate() {
		return updationDate;
	}
	
	/**
	 * Sets the updation date of the entity.
	 * @param updationDate The updation date.
	 */
	public void setUpdationDate(Date updationDate) {
		this.updationDate = updationDate;
	}

	/**
	 * The creation date of the entity.
	 */
	@JsonIgnore
	private Date creationDate;
	

	/**
	 * The updation date of the entity.
	 */
	@JsonIgnore
	private Date updationDate;
	
	/**
	 * This is the update date in string;
	 */
	private String stringUpdateDate;
	
	/**
	 * This method get the update date into string.
	 * @return the stringUpdateDate
	 */
	public String getStringUpdateDate() {
		return stringUpdateDate;
	}

	/**
	 * This method set the update date into string.
	 * @param stringUpdateDate the stringUpdateDate to set
	 */
	public void setStringUpdateDate(String stringUpdateDate) {
		this.stringUpdateDate = stringUpdateDate;
	}

	/**
	 * This method get the creation date into string.
	 * @return the stringCreationDate
	 */
	public String getStringCreationDate() {
		return stringCreationDate;
	}

	/**
	 * This method set the creation date into string.
	 * @param stringCreationDate the stringCreationDate to set
	 */
	public void setStringCreationDate(String stringCreationDate) {
		this.stringCreationDate = stringCreationDate;
	}

	/**
	 * This is the create date in string;
	 */
	private String stringCreationDate;
	
	/**
	 * Gets the id of the entity.
	 * @return The id of the entity
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the entity. 
	 * @param id The id of the entity
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * This validates the entity
	 * @return
	 */
	public abstract boolean validate() throws ValidationFailedException;

	/**
	 * Transforms an entity into an internal entity which will
	 * be used by service layer
	 * @return An internal entity
	 */
	public abstract BaseEntity transformToInternal();
	

	/**
	 * Transforms an entity into an external entity which will
	 * be used by Controller layer
	 * @return An external entity
	 */
	public abstract BaseEntity transformToExternal();

	/**
	 * Checks if a string is null or empty
	 * @param string The string to be checked
	 * @return true if the string is null or empty else false
	 */
	public static boolean isNullOrEmpty(String string){
		if (string ==  null) return true;
		return string.isEmpty();
	}
	
}


