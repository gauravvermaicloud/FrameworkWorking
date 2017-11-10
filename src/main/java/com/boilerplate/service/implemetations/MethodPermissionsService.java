package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IMethodPermissions;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.service.interfaces.IMethodPermissionService;

public class MethodPermissionsService implements IMethodPermissionService{

	/**
	 * This gets the method permissions
	 */
	@Autowired
	com.boilerplate.database.interfaces.IMethodPermissions methodPermissions;
	
	/**
	 * This sets method permissions
	 * @param methodPermissions The method permissions DAL
	 */
	public void setMethodPermissions(com.boilerplate.database.interfaces.IMethodPermissions methodPermissions){
		this.methodPermissions = methodPermissions;
	}
	
	/**
	 * This is the method permission map
	 */
	public static BoilerplateMap<String, com.boilerplate.java.entities.MethodPermissions> methodPermissionsMap = null;
	
	/**
	 * 
	 */
	@Override
	public BoilerplateMap<String, com.boilerplate.java.entities.MethodPermissions> getMethodPermissions() {
		if(this.methodPermissionsMap == null){
			this.methodPermissionsMap = methodPermissions.getMethodPermissions();
		}
		return this.methodPermissionsMap;
	}
	
	/**
	 * Initializes the map
	 */
	public void initialize(){
		this.methodPermissionsMap = methodPermissions.getMethodPermissions();
	}

}
