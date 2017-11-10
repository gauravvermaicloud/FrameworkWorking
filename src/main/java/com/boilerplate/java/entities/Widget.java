package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Date;

import org.crsh.shell.impl.command.system.repl;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the widget type entity.
 * @author gaurav
 */
@ApiModel(value="A widget", description="This is a widger", parent=BaseEntity.class)
public class Widget extends BaseEntity implements Serializable{
	
	/**
	 * Gets the name of the widget
	 * @return The name of the widget
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the widget
	 * @param name The name of the widget
	 */
	public void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value="This is the name of the widget"
			,required=true,notes="The name of the widget can have any length")
	/**
	 * The name of the widget.
	 */
	private String name;
	
	/**
	 * @see super.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	/**
	 * @see super.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see super.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
}
