package com.boilerplate.database.mysql.implementations;

import java.util.List;

import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.database.mysql.implementations.entities.UserRoleMapping;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;

public class MySQLUserRole extends MySQLBaseDataAccessLayer
		implements IUserRole {

	/**
	 * @see IUserRole.grantUserRole
	 */
	@Override
	public void grantUserRole(ExternalFacingUser user, List<Role> roles) {
		List<UserRoleMapping> userRoleMappings = new BoilerplateList<UserRoleMapping>();
		UserRoleMapping userRoleMapping;
		// basically create a list of mapping entity
		for (Role role : roles) {
			userRoleMapping = new UserRoleMapping();
			userRoleMapping.setUserId(Long.parseLong(user.getId()));
			userRoleMapping.setRoleId(Long.parseLong(role.getId()));
			userRoleMappings.add(userRoleMapping);
		}
		// and then save it to database
		super.create(userRoleMappings);
	}
}
