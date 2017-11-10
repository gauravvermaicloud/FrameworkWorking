package com.boilerplate.service.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.MethodPermissions;

/**
 * This class contains the code for method permissions
 * @author gaurav.verma.icloud
 *
 */
public interface IMethodPermissionService {

	/**
	 * This returns the method permissions for the methods
	 * @return The method permissions
	 */
	public BoilerplateMap<String, MethodPermissions> getMethodPermissions();
}
