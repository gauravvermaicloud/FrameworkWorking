package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.ClientSideDiagnostic;

public interface IDiagnostic {
	/**
	 * Writes a log to the database
	 * @param clientSideDiagnostic The log message
	 */
	public void publishClientSideLog(ClientSideDiagnostic clientSideDiagnostic);
}
