package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;

public interface IUserRole {
	/**
	 * Grants given set of roles to the user
	 * @param user This is the user to whom roles are being granted
	 * @param roles This is the set of role
	 */
	public void grantUserRole(ExternalFacingUser user, List<Role> roles) throws NotFoundException,ConflictException;
}
