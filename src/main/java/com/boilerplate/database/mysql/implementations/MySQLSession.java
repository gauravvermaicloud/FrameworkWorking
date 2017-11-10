package com.boilerplate.database.mysql.implementations;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.boilerplate.database.interfaces.ISession;
import com.boilerplate.database.mysql.implementations.entities.DatabaseSession;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.sessions.Session;

/**
 * This class implements session store on MySQL Server
 * 
 * @author gaurav.verma.icloud
 */
public class MySQLSession extends MySQLBaseDataAccessLayer implements ISession {
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLSession.class);

	/**
	 * @see ISession.create
	 */
	@Override
	public Session create(Session session) {
		return this.update(session);
	}

	/**
	 * @see ISession.getSession
	 */
	@Override
	public Session getSession(String sessionId) {
		List<String> sessionsJSON = null;
		// The session is a complex object which can
		// be tied to user, role, the session itself, permissions
		// recreating it can be difficult, further there will be custom
		// items in the session so it will be easier to just deserialize the
		// session
		// and take create a session object
		// this has the issue that if something like user role and permissions
		// havce changed then they are not available till next login
		// unless application programmer makes some cleaver coding and updates
		// sessions
		// across users
		String hsql = "Select S.sessionEntity From DatabaseSession S Where S.sessionId = :SessionId";
		BoilerplateMap<String, Object> queryPatameterMap = new BoilerplateMap<String, Object>();
		queryPatameterMap.put("SessionId", sessionId);
		sessionsJSON = super.executeSelect(hsql, queryPatameterMap);
		if (sessionsJSON.isEmpty()) {
			// if the session doesnt exist
			// cases could be - the id is invalid, session has expired and has
			// been cleaned up
			// return null
			return null;
		} else {
			// else deserialize the session and send it back.
			return Session.fromXML(sessionsJSON.get(0), Session.class);
		}
	}

	/**
	 * @see ISession.update
	 */
	@Override
	public Session update(Session session) {
		DatabaseSession databaseSession = new DatabaseSession();
		databaseSession.setCreationDate(session.getCreationDate());
		databaseSession.setUpdationDate(session.getUpdationDate());
		databaseSession.setUserId(session.getUserId());
		databaseSession.setId(session.getId());
		databaseSession.setSessionEntity(Base.toXML(session));
		databaseSession.setSessionId(session.getSessionId());
		super.create(databaseSession);
		session.setId(databaseSession.getId());
		return session;
	}

	/**
	 * @see ISession.deleteSessionOlderThan
	 */
	@Override
	public void deleteSessionOlderThan(Date date) {
		String hsql = "Delete From DatabaseSession S where S.updationDate < :UpdationDate";
		BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
		queryParameterMap.put("UpdationDate", date);
		super.executeUpdate(hsql, queryParameterMap);
	}

	@Override
	public void deleteSession(String sessionId) {
		// TODO Auto-generated method stub

	}

}
