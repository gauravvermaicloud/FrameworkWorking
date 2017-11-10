package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.Widget;

/**
 * This service interface is used to operate on widgets.
 * @author gaurav
 */
public interface IWidgetService{
	/**
	 * This gets the entities
	 * @param entities The list of entities to be get
	 * @return A list of widgets
	 * @throws BadRequestException If the request is not properly constructed
	 * @throws ConflictException If there is a conflict
	 * @throws NotFoundException If the entity was not found
	 * @throws UnauthorizedException If the user is not authorized for the operation
	 * @throws ValidationFailedException if the validation of entities failed
	 */
	public BoilerplateList<Widget> get(BoilerplateList<BaseEntity> entities)
			throws BadRequestException, ConflictException,NotFoundException,
			UnauthorizedException,ValidationFailedException;

}
