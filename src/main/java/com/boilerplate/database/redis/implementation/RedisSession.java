package com.boilerplate.database.redis.implementation;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.ISession;
import com.boilerplate.framework.Logger;
import com.boilerplate.sessions.Session;

public class RedisSession extends BaseRedisDataAccessLayer implements ISession{

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(RedisSession.class);

	private static final String Session = "Session:";
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * The setter to set the configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager 
			configurationManager){
		this.configurationManager = configurationManager;
	}
	@Override
	public Session create(Session session) {
		return this.update(session);
	}

	@Override
	public Session getSession(String sessionId) {
		return super.get(Session+sessionId, Session.class);
	}

	@Override
	public Session update(Session session) {
		session.setId(session.getSessionId());
		super.set(Session+session.getSessionId(), session,Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes"))*60); 
		return session;
	}

	@Override
	public void deleteSessionOlderThan(Date date) {
		
	}

	@Override
	public void deleteSession(String sessionId) {
		super.delete(Session+sessionId.toUpperCase());		
	}

}
