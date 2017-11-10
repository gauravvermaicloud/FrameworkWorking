package com.boilerplate.aspects;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;

public class PublishLibrary {
	
	private Logger logger = Logger.getInstance(PublishLibrary.class);
	
	private BoilerplateList<String> subjects = null;
	
	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;
	
	/**
	 * This sets the configuration Manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
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
	
	
	
	public void requestPublishAsyncOffline(String url, String publishMethod,Object[] input, Object returnValue,String methodCalled, String publishSubject,
			String publishTemplate, boolean isDynamicPublishURl){
		if(subjects == null){
			subjects = new BoilerplateList<>();
			subjects.add("Publish");
		}
		
		
	}
	
}
