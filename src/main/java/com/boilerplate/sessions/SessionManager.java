package com.boilerplate.sessions;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.implemetations.UserService;

public class SessionManager {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);
	
	/**
	 * This is the instance of the configuration manager.
	 */
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
	
	/**
	 * This is the instance of the session database object
	 */
	@Autowired
	com.boilerplate.database.interfaces.ISession session;
	
	/**
	 * This method sets the instance of the session
	 * @param session The instance of the session.
	 */
	public void setSession(com.boilerplate.database.interfaces.ISession session){
		this.session = session;
	}
	
	/**
	 * This is the user service
	 */
	@Autowired
	com.boilerplate.service.interfaces.IUserService userService;
	
	/**
	 * This sets the user service
	 * @param userService An instance of user service
	 */
	public void setUserService(com.boilerplate.service.interfaces.IUserService userService){
		this.userService = userService;
	}
	/**
	 * This is an instance of the queue job, to save the session
	 * back on to the database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	/**
	 * This sets the queue reader jon
	 * @param queueReaderJob The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	
	/**
	 * This is an annonympus session which never expires and is not stored on cache or DB
	 */
	private Session annonymousSession;
	
	/**
	 * The time out of session	
	 */
	private static int sessionTimeOut =43200*60*1000;
	
	/**
	 * The subject for saving to background database
	 */
	private BoilerplateList<String> subjects = new BoilerplateList<String>();
	
	/**
	 * This method gets a session with the given id.
	 * The method first checks if the session is on cache, if not then the method gets it from database
	 * @param sessionId The id of the session
	 * @return A session
	 * @throws ValidationFailedException This exception will not be thrown as it is required by validation failed exception
	 * which in this case is made to false
	 */
	public Session getSession(String sessionId) throws ValidationFailedException{
		if(sessionId == null){
			return null;
		}
		//first check if the session exists in cache
		Session session =getSessionFromCache(sessionId);
		
		//if not then check session exists in DB
		if(session == null){
			session = this.session.getSession(sessionId);
		}
		if(session != null){
 			//if session has expired
 			if(!session.validate()){
 				session = null;
 			}
 			
 		}
		
		if(session == null){
			session = this.getAnnonymousSession();
		}
		//return the session or return null
		return session;
	}

	/**
	 * THis method saves the sesion before exiting the thread
	 * @param session The session to be saved
	 */
	public void saveSessionOnExit(Session session){
		if(session == null){
			return;
		}
		if(this.annonymousSession  != null){
			if(session.equals(this.annonymousSession)){
				return;
			}
		}
		session.setUpdationDate(new Date());
		//put the session back on cache with new expiry
		putSessionOnCache(session);
		
		//the queue job will put it back  in database we dont write back to DB
		//from here itself because it will cause performance issue
		//and we cant just rely upon cache because during a memeory preassure 
		//the cache may be evicted
		
		try {
			queueReaderJob.requestBackroundWorkItem(
					session, subjects, "SessionManager", "getSession");
		} catch (Exception ex) {
			//if there is an issue during accessing queue we should save the
			//session to the database
			this.saveSession(session);
		}
	}
	
	
	/**
	 * This method gets a session if available from cache
	 * @param sessionId This is the id of the session
	 * @return A session
	 */
	private Session getSessionFromCache(String sessionId){
		Session session = null;
		try {
			if(CacheFactory.getInstance().isCacheEnabled()){
				session = CacheFactory.getInstance().get(Constants.SESSION+sessionId.toUpperCase(), Session.class);
			}
		} catch (Exception ex) {
			logger.logException("SessionManager", "getSession"
					, "Get Session From cache",ex.toString(),ex);
		}
		return session;
	}
	
	/**
	 * This method puts session on cache
	 * @param session The session
	 */
	private void putSessionOnCache(Session session){
		try {
			if(CacheFactory.getInstance().isCacheEnabled()){
				CacheFactory.getInstance().add(Constants.SESSION+session.getSessionId()
						, session, sessionTimeOut);
			}
		} catch (Exception ex) {
			//although there is an error in accessing cache
			//but we will move ahead as the DB is getting saved
			//So someone who will be doing monitoring will know
			//cache issue from here or from the ping
			//which is expected to be run 
			logger.logException("SessionManager", "getSession"
					, "Get Session From cache",ex.toString(),ex);
		}
	}
	
	/**
	 * This method creates a session for the given user
	 * and saves it to the database and cache. If the user is not saved to
	 * cache it will not throw an exception however if there is an issue
	 * saving the session to DB there will be an error
	 * @param externalFacingUser This is the user for whom session is
	 * created
	 * @return The session created
	 */
	public Session createNewSession(ExternalFacingReturnedUser externalFacingUser){
		Session session = new Session(externalFacingUser);
		
		//Save session on DB
		this.saveSession(session);
		//Save session on cache
		putSessionOnCache(session);		
		return session;
	}
	
	/**
	 * Saves the session to the databaase
	 * @param session the session
	 * @return The session
	 */
	public Session saveSession(Session session){		
		//Save session on DB
		return this.session.create(session);
	}
	
	/**
	 * This method updates a session
	 * @param session The session
	 * @return The session
	 */
	public Session updateSession(Session session){
		return this.session.update(session);
	}
	
	/**
	 * Default constructor
	 */
	public SessionManager(){
	
	}
	
	/**
	 * Initializes configuration after bean creation
	 */
	public void initialize(){
		sessionTimeOut = Integer.parseInt(configurationManager.get("SessionTimeOutInMinutes") == null?configurationManager.get("SessionTimeOutInMinutes"):configurationManager.get("SessionTimeOutInMinutes"))*60;
		this.subjects.add(Constants.SaveSessionToDatabase);

	}
	

	/**
	 * This method returns the session time out.
	 * @return
	 */
	public static int getSessionTimeout(){
		return sessionTimeOut;
	}
	
	/**
	 * This method cleansup old and expired sessions.
	 * It is a good practice to cleanup sessions from code
	 * rather than to rely upon a DBA.
	 * Further the logs we generate keep all the information about the session and user
	 * to trace any actitvity.
	 */
	public void cleanupExpiredSession(){
		long time = new Date().getTime();
		time = time - this.getSessionTimeout()*1000;
		Date date = new Date(time);
		this.session.deleteSessionOlderThan(date);
	}
	
	/**
	 * This gets a session for annonymous user
	 * @return The annonympus user session
	 * @throws NotFoundException If the user is not found
	 */
	private Session getAnnonymousSession(){
		if(this.annonymousSession == null){
			try{
				ExternalFacingReturnedUser annonymousUser =userService.get("DEFAULT:ANNONYMOUS");
				this.annonymousSession = new Session(annonymousUser);
			}
			catch(Exception ex){
				//this exception is not expected as the said user is always found
			}
			
		}
		return this.annonymousSession;
	}
	
	/**
	 * This is a session associated with a background job
	 */
	private Session backroundJobSession = null;
	
	/**
	 * Gets a session for background job
	 * @return The session for background job
	 */
	public Session getBackgroundJobSession(){
		
		if(this.backroundJobSession == null){
			try{
				ExternalFacingReturnedUser backGroundJObUser =userService.get("DEFAULT:BACKGROUND");
				this.backroundJobSession = new Session(backGroundJObUser);
			}
			catch(Exception ex){
				//this exception is not expected as the said user is always found
				System.out.print(ex.toString());
			}
			
		}
		return this.backroundJobSession;
	}

	public void logout(String sessionId) {
		try {
			if(CacheFactory.getInstance().isCacheEnabled()){
				CacheFactory.getInstance().remove(Constants.SESSION+sessionId.toUpperCase());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.session.deleteSession(sessionId);
		
	}
}
