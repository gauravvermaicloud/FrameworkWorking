package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;

/**
 * This interface has role related database operations
 * @author gaurav.verma.icloud
 *
 */
public interface IRole {
	/**
	 * This method returns the roles
	 * @return The list of roles
	 */
	GenericListEncapsulationEntity<Role> getRoles();
}
