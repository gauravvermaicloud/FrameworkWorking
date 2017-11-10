package com.boilerplate.service.implemetations;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ClientSideDiagnostic;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Ping;
import com.boilerplate.queue.QueueFactory;
import com.boilerplate.service.interfaces.IPingService;
import com.boilerplate.sessions.SessionManager;

public class PingService implements IPingService{
	/**
	 * The queue reader job
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	/**
	 * Sets the queue reader job
	 * @param queueReaderJob The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	
	/**
	 * The session manager
	 */
	@Autowired
	SessionManager sesionManager;
	
	/**
	 * Sets the session manager
	 * @param sessionManager The session manager
	 */
	public void setSesionManager(SessionManager sessionManager){
		this.sesionManager = sessionManager;
	}

	/**
	 * These are subjects for client side logs
	 */
	BoilerplateList<String> subjectForClientSideLog = new BoilerplateList();
	
	/**
	 * Initilizes the bean
	 */
	public void initilize(){
		subjectForClientSideLog.add("ClientSideLog");
	}
	
	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(PingService.class);
	/**
	 * @see IPingService.setPingStatus
	 */
	@Override
	public void setPingStatus(Ping ping) {
		this.setBackgroundProcessingJobPing(ping);
		this.setPublishingJobPing(ping);
		this.setDatabasePing(ping);
		this.setQueuePing(ping);
	}
	
	/**
	 * Sets the status of cache, if cache is not working or accessable from
	 * this server it is okey as cache is not a critical system
	 * @param ping The ping status to be updated
	 */
	private void setCachePing(Ping ping){
		try{
			if(
				//check if cache is working
				CacheFactory.getInstance().isCacheEnabled()
			){
				ping.addStatus("Cache", "OK", true);
			}
			else{
				ping.addStatus("Cache", "FAILED", true);
			}
		}//if there is an exception then fail cache
		catch(Exception ex){
			ping.addStatus("Cache", "FAILED", true);
			logger.logException("PingService", "setCachePing", "Exception Handler", ex.toString(), ex);
		}
	}
	
	/**
	 * Sets the status of queue, if queue fails or is not accessable
	 * then overall system is marked to be failed
	 * @param ping The ping information
	 */
	private void setQueuePing(Ping ping){
		try{
			if(
					QueueFactory.getInstance().isQueueEnabled()
			){
				ping.addStatus("Queue", "OK", true);
			}
			else{
				ping.addStatus("Queue", "FAILED", false);
			}
		}//fail if there is an exception in accessing queue
		catch(Exception ex){
			ping.addStatus("Queue", "FAILED", false);
			logger.logException("PingService", "setQueuePing", "Exception Handler", ex.toString(), ex);
		}
	}
	
	/**
	 * Sets the status of publishing queue, if the job is not working from this server
	 * it is okay because some other background server will compensate
	 * @param ping The ping information
	 */
	private void setPublishingJobPing(Ping ping){
		try{
			if(
					queueReaderJob.getPublishingJobStatus()
			){
				ping.addStatus("Publishing Queue Processing", "OK", true);
			}
			else{
				ping.addStatus("Publishing Queue Processing", "FAILED", true);
			}
		}
		catch(Exception ex){
			ping.addStatus("Publishing Queue Processing", "FAILED", true);
			logger.logException("PingService", "setPublishingJobPing", "Exception Handler", ex.toString(), ex);
		}
	}
	
	/**
	 * Sets the status of background jobs, if the job is not working from this server
	 * it is okey because someother server will compendate
	 * @param ping The ping infomration
	 */
	private void setBackgroundProcessingJobPing(Ping ping){
		try{
			if(
					queueReaderJob.getBackgroundJobStatus()	
			){
				ping.addStatus("Background Queue Processing", "OK", true);
			}
			else{
				ping.addStatus("Background Queue Processing", "FAILED", true);
			}
		}
		catch(Exception ex){
			ping.addStatus("Background Queue Processing", "FAILED", true);
			logger.logException("PingService", "setBackgroundProcessingJobPing", "Exception Handler", ex.toString(), ex);
		}
	}
	
	/**
	 * Sets the status of database ping, if there is an error accessing the db then
	 * the server is concidered non functinonal and overall status will be failed
	 * @param ping The status of ping to be updated.
	 */
	private void setDatabasePing(Ping ping){
		try{
				//This method will be forced to goto database and return a null session
				sesionManager.getSession("-1");
				ping.addStatus("Database", "OK", true);
					}
		catch(Exception ex){
			ping.addStatus("Database", "FAILED", false);
			logger.logException("PingService", "setDatabasePing", "Exception Handler", ex.toString(), ex);
		}
	}

	/**
	 * @see IPingService.publishClientSideLog
	 */
	@Override
	public void publishClientSideLog(ClientSideDiagnostic clientSideDiagnostic) {
		try{
			queueReaderJob.requestBackroundWorkItem(
					clientSideDiagnostic, subjectForClientSideLog, "PingService", "publishClientSideLog");
		}	
		catch(Exception ex){
				logger.logException("PingService", "publishClientSideLog", "catch", ex.toString(), ex);
		}
	}
	
}
