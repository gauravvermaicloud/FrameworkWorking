package com.boilerplate.jobs;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.aspects.LogAndTraceExceptionAspect;
import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.AsyncWorkItemList;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.jobs.exceptions.QueueServiceUnavailableException;
import com.boilerplate.queue.IQueue;
import com.boilerplate.queue.QueueFactory;
import com.boilerplate.service.interfaces.IAuthTokenService;

/**
 * This class is used to implement a job which is read to process pending items
 * from the queue. The method readQueueAndDispatch is scheduled.
 * We are using a Spring based job engine instead of quartz because quartz has servlet
 * container dependent code and Spring jobs will be independent.
 * This job may be turned off from configuration for servers on which background jobs are
 * not expected to execute.
 * @author gaurav
 * 
 */
public class QueueReaderJob {
	/**
	 * This is an instance of the work dispatcher. This is not governed by an interface
	 * but we still autowire it so that all the dispatch maps for subjects are set and 
	 * there observers are set at start.
	 */
	@Autowired
	com.boilerplate.asyncWork.AsyncWorkDispatcher asyncWorkDispatcher;
	
	/**
	 * Sets the async work dispatcher
	 * @param asyncWorkDispatcher The instance of async work dispatcher.
	 */
	public void setAsyncWorkDispatcher(com.boilerplate.asyncWork.AsyncWorkDispatcher asyncWorkDispatcher){
		this.asyncWorkDispatcher = asyncWorkDispatcher;
	}
	
	/**
	 * This is the session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;
	
	/**
	 * This sets the session manager
	 * @param sessionManager The session manager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(QueueReaderJob.class);
	
	/**
	 * This is the instance of the configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * The setter for configuration manager used by spring
	 * @param configurationManager The instance of configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
		
	

	

	/**
	 * Turns on background services
	 */
	public void setBackgroundServiceOn(){
		this.isBackgroundJobEnabled = true;
	}
	
	/**
	 * Turns off background services
	 */
	public void setBackgroundServiceOff(){
		this.isBackgroundJobEnabled=false;
	}
	
	/**
	 * Checks if the jobs are enabled, this is always enabled
	 * To disable the queue job remove the config in bean.
	 * We can also disable it from properties however that is not recomended as
	 * the method will still be called hence to turn it off turn it from bean and not config
	 * However this variable can be explicilty turned off
	 * in a running server from an api call.
	 */
	private boolean isBackgroundJobEnabled = true;
	
	/**
	 * Checks if the queue history jobs are enabled, this is always enabled
	 * To disable the queue job remove the config in bean.
	 * We can also disable it from properties however that is not recomended as
	 * the method will still be called hence to turn it off turn it from bean and not config
	 * However this variable can be explicilty turned off
	 * in a running server from an api call.
	 */
	private boolean isMaintainQueueHistory = false;
	
	/**
	 * Returns the status of background jobs 
	 */
	public boolean getBackgroundJobStatus(){
		return this.isBackgroundJobEnabled;
	}
	
	/**
	 * Turns on background services
	 */
	public void setPublishServiceOn(){
		this.isPublishQueueEnabled = true;
	}
	
	/**
	 * Turns off background services
	 */
	public void setPublishServiceOff(){
		this.isPublishQueueEnabled=false;
	}
	
	/**
	 * Checks if the jobs are enabled, this is always enabled
	 * To disable the queue job remove the config in bean.
	 * We can also disable it from properties however that is not recomended as
	 * the method will still be called hence to turn it off turn it from bean and not config
	 * However this variable can be explicilty turned off
	 * in a running server from an api call.
	 */
	private boolean isPublishQueueEnabled = true;
	
	/**
	 * Returns the status of background jobs 
	 */
	public boolean getPublishingJobStatus(){
		return this.isPublishQueueEnabled;
	}
	/**
	 * This tells if this is the first run of the queue, this is used to set the
	 * isBackgroundJobEnabled
	 */
	private boolean isFirstRun = false;
	/**
	 * Instance of the Auth token service
	 */
	@Autowired IAuthTokenService authTokenService;
	
	/**
	 * Sets the organization service
	 * @param organizationService The organization service
	 */
	public void setAuthTokenService(IAuthTokenService authTokenService){
		this.authTokenService = authTokenService;
	}
	/**
	 * This method is used to read next message from queue and then
	 * send the response to observers.
	 * 
	 * This method should be diabled on servers from the config file where
	 * background service processing is not expected.
	 */
	public void readQueueAndDispatch(){
		if(this.isFirstRun ==false){
			this.isBackgroundJobEnabled = Boolean.parseBoolean(configurationManager.get("IsQueueProcessingEnabled"));
			this.isMaintainQueueHistory = Boolean.parseBoolean(configurationManager.get("IsMaintainQueueHistory"));
			this.isPublishQueueEnabled = Boolean.parseBoolean(configurationManager.get("IsPublishQueueEnabled"));
			this.isFirstRun = true;
		}
		//if the queue is enabled then work
		if(isBackgroundJobEnabled){
			//Create a unique request id for the job and set it on thread
			RequestThreadLocal.setRequest(UUID.randomUUID().toString(), null
					,null,this.sessionManager.getBackgroundJobSession());
			AsyncWorkItem asyncWorkItem =null;
			try{
				if(QueueFactory.getInstance().isQueueEnabled()){
				while(true){
					try{
						//read a job from queue
						asyncWorkItem =QueueFactory.getInstance().remove("_BACKGROUN_JOBS_QUEUE_"+configurationManager.get("Enviornment"));
						if(asyncWorkItem !=null){
							//execute the message on all observers
							asyncWorkItem.setUniqueRequestIdOfJob(RequestThreadLocal.getRequestId());
							asyncWorkDispatcher.dispatch(asyncWorkItem);
						}
						else{
							//when we get a null object the queue is empty, hence we get out
							break;
						}
						}catch(Exception ex){
							//A single job has failed
							logger.logException("QueueReaderJob", "QueueReaderJob"
									, "Job Failed Exception", 
									asyncWorkItem == null?"Null":asyncWorkItem.toJSON(), ex);
						}
						finally{
							//push into history queue if needed
							if(this.isMaintainQueueHistory){
								try {
									if(asyncWorkItem != null){	
										QueueFactory.getInstance().insert("_BACKGROUND_JOBS_HISTORY_"+configurationManager.get("Enviornment"), asyncWorkItem);
									}
								} catch (Exception e) {
									//if this queue is down we dont care and we cant do much
									logger.logException("QueueReaderJOb", "readQueueAndDispatch", "Maintain Queue History In Final", e.toString(), e);
								}
							}

						}
					}//end while
				}//end if
			}catch(Exception ex){
				//the job group has failed
				logger.logException("QueueReaderJob", "QueueReaderJob"
						, "Job Group Failed", "", ex);
			}
			finally{
				//clean up the thread so that this id is not available next time.
				RequestThreadLocal.remove();
			}
		}
	}

	
	/**
	 * This method is used to request a work item to be put into the queue.
	 * @param item The item on which processing will be done.
	 * @param subjects The subjects of the observers
	 * @param requestingClassName The name of class requesting operation
	 * @param requestingMethodName The name of the method requesting operation.
	 * @throws Exception If there is an error in adding job to queue.
	 */
	public <T> void requestBackroundWorkItem(T item, BoilerplateList<String> subjects,
			String requestingClassName, String requestingMethodName) throws Exception{
	this.requestBackroundWorkItem(item, subjects, requestingClassName, requestingMethodName, "_BACKGROUN_JOBS_QUEUE_");
	}
	
	public <T> void requestBackroundWorkItem(T item, BoilerplateList<String> subjects,
			String requestingClassName, String requestingMethodName,String queueName) throws Exception{
		try{
			if(QueueFactory.getInstance().isQueueEnabled()){
			AsyncWorkItem<T> asyncWorkItem = new AsyncWorkItem<T>(item, subjects, requestingClassName
				, requestingMethodName);
			QueueFactory.getInstance().insert(queueName + configurationManager.get("Enviornment"), asyncWorkItem);
			}
			else{
				throw new QueueServiceUnavailableException("QueueReader",
						"The queue is not available");
			}
		}
		catch(Exception ex){
			logger.logException("QueueReader", "requestBackroundWorkItem"
					, "exception block", ex.toString()
					, ex);
			throw ex;
		}
	}
	
	
}
