package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.java.Base;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;

public class RedisRole extends BaseRedisDataAccessLayer implements IRole{

	@Override
	public GenericListEncapsulationEntity<Role> getRoles() {
		String roleXML =  this.get("ROLES");
		GenericListEncapsulationEntity<Role>  roles  = (GenericListEncapsulationEntity<Role>)Base.fromXML(roleXML,GenericListEncapsulationEntity.class);
		return roles;
	}

}
