package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IMethodPermissions;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.MethodPermissions;

public class RedisMethodPermissions extends BaseRedisDataAccessLayer implements IMethodPermissions{

	@Override
	public BoilerplateMap<String, MethodPermissions> getMethodPermissions() {
		String methodPermissionAsXML = super.get("METHOD_PERMISSIONS");
		return (BoilerplateMap<String, MethodPermissions>)Base.fromXML(methodPermissionAsXML, BoilerplateMap.class);
	}

}
