package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IDiagnostic;
import com.boilerplate.java.entities.ClientSideDiagnostic;

public class RedisDiagnostics extends BaseRedisDataAccessLayer implements IDiagnostic{

	@Override
	public void publishClientSideLog(ClientSideDiagnostic clientSideDiagnostic) {
		super.insert("_Client_Side_Log", clientSideDiagnostic);
	}

}
