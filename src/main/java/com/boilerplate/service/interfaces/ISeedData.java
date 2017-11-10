package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;

/**
 * This interface provides the signatures of methods required in creating seed dat 
 * @author mohit
 *
 */
public interface ISeedData {
	/**
	 * This method creates the seed data
	 * @throws BadRequestException 
	 * @throws NotFoundException 
	 * @throws UnauthorizedException 
	 */
	public void createSeedData() throws NotFoundException, BadRequestException, UnauthorizedException;

}
