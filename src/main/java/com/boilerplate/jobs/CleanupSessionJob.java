package com.boilerplate.jobs;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This job will cleanup any session older than session time out.
 * This is not required in Cassandra based databases where we use time to live
 * on records.
 * The job is enabled or diabled from the spring configuration file.
 * @author gaurav.verma.icloud
 *
 */
public class CleanupSessionJob{
	
	/**
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;
	
	/**
	 * Sets the session manager for the job
	 * @param sessionManager The session manager instance
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}

	/**
	 * Cleansup the session
	 */
	public void cleanup(){
		this.sessionManager.cleanupExpiredSession();
	}
}