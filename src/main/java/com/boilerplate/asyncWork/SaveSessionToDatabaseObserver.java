package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.sessions.Session;

/**
 * This job saves session to the database
 * @author gaurav.verma.icloud
 *
 */
public class SaveSessionToDatabaseObserver implements IAsyncWorkObserver{

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
	 * This method saves the session to the database asynchronously 
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) {
		//get the session
		Session session =(Session) asyncWorkItem.getPayload();
		sessionManager.updateSession(session);
	}

}
