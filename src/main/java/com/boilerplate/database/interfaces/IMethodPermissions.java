package com.boilerplate.database.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.MethodPermissions;

/**
 * This gets the method permissions from the database
 * @author gaurav.verma.icloud
 *
 */
public interface IMethodPermissions {

	/**
	 * This method gets all the method permissions for a given method.
	 * @return
	 */
	public BoilerplateMap<String, MethodPermissions> getMethodPermissions();
}
