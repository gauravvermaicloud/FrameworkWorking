package com.boilerplate.database.interfaces;

import java.util.Date;

import com.boilerplate.sessions.Session;

public interface ISession {
	
	/**
	 * This method saves a session in the database
	 * @param session The session to be saved
	 * @return A session
	 */
	public Session create(Session session);

	/**
	 * This method gets the given session by id from the database
	 * @param sessionId The id of the session
	 * @return The session
	 */
	public Session getSession(String sessionId);
	
	/**
	 * Updates a session in the database
	 * @param session session to be updated
	 * @return The session 
	 */
	public Session update(Session session);

	/**
	 * This method deletes any session entry older than given date
	 * @param date The date before whcich all session etries should be deleted
	 */
	public void deleteSessionOlderThan(Date date);
	

	public void deleteSession(String sessionId);
}
