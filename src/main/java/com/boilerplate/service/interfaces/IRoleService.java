package com.boilerplate.service.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;

/**
 * This interface implements the code for roles
 * @author gaurav.verma.icloud
 *
 */
public interface IRoleService {
	/**
	 * This method returns the roles
	 * @return The list of roles
	 */
	GenericListEncapsulationEntity<Role> getRoles();
	
	/**
	 * Gets a map of role and the id
	 * @return A role map with key as id
	 */
	BoilerplateMap<String,Role> getRoleIdMap();
	
	/**
	 * Gets a map of role and the name
	 * @return A role map with key as name in upper case
	 */
	BoilerplateMap<String,Role> getRoleNameMap();
	
	/**
	 * Reloads roles from database
	 */
	void reloadRoles();
}
