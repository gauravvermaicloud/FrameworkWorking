package com.boilerplate.service.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;

public interface IUserRoleService {
	/**
	 * This method grants the user roles
	 * @param userId The id of the user to be granted roles
	 * @param roles The roles to be granted
	 * @param granter This is the user who is performing the operation.
	 * Some roles can only be granted by priviglaed users
	 * @throws NotFoundException This is thrown if the user or the role is not found
	 * @throws UnauthorizedException This exception is thrown if the user cant grant a given role.
	 * @throws BadRequestException If the user data is not proper.
	 */
	public void grantUserRoles(String userId, List<String> roles, ExternalFacingReturnedUser granter) 
			throws   NotFoundException,UnauthorizedException, BadRequestException, ConflictException;
}
