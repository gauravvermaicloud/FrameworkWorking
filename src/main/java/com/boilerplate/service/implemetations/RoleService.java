package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IRoleService;

public class RoleService implements IRoleService{

	/**
	 * The list of roles,they are initialized for performance
	 */
	private static GenericListEncapsulationEntity<Role> roles = null;
	
	/**
	 * A map of roles based on the role id.
	 */
	private static BoilerplateMap<String,Role> roleIdMap = null;
	
	private static BoilerplateMap<String,Role> roleNameMap = null;
	
	/**
	 * The DAL layer for role
	 */
	@Autowired
	IRole role;
	
	public void setRole(IRole role){
		this.role =role;
	}
	
	/**
	 * This method gets all the roles
	 */
	@Override
	public GenericListEncapsulationEntity<Role> getRoles() {
		if(roles == null){
			initialize();
		}
		return roles;
	}
	
	
	/**
	 * initializes the roles
	 */
	public void initialize(){
		//get all the roles
		roles = role.getRoles();
		//prepare a map for roles by id
		roleIdMap = new BoilerplateMap<String, Role>();
		roleNameMap = new BoilerplateMap<String, Role>();
		for(Role role : roles.getEntityList()){
			roleIdMap.put(role.getId(), role);
			roleNameMap.put(role.getRoleName().toUpperCase(), role);
		}
	}

	/**
	 * @see IRoleService.getRoleIdMap
	 */
	@Override
	public BoilerplateMap<String, Role> getRoleIdMap() {
		return roleIdMap;
	}

	/**
	 * @see IRoleService.getRoleNameMap
	 */
	@Override
	public BoilerplateMap<String, Role> getRoleNameMap() {
		return roleNameMap;
	}

	/**
	 * @see IRoleService.reloadRoles
	 */
	@Override
	public void reloadRoles() {
		roles =null;
		initialize();
	}

}
