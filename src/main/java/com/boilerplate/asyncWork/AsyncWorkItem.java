package com.boilerplate.asyncWork;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;

/**
 * This represents an instance of item payload on which work will be done
 * @author gaurav
 * @param <T> This is the type of the work item
 */
public class AsyncWorkItem<T> extends Base{
	/**
	 * This is the source of the work item typically in form of
	 * Class.method name
	 */
	private String source;
	
	/**
	 * This is the list of sibjects on which this message will be processed.
	 */
	private BoilerplateList<String> subjects;
	
	/**
	 * This is the payload of work
	 */
	private T workPayload;
	
	/**
	 * This is the unique id of the request that created this.
	 */
	private String uniqueRequestIdOfCreator;
	
	/**
	 * This is the unique id of the job processing this request.
	 */
	private String uniqueRequestIdOfJob;
	
	/**
	 * Gets a list of subjects for this message.
	 * @return The list of subjects.
	 */
	public BoilerplateList<String> getSubjects() {
		return this.subjects;
	}
	
	public T getPayload(){
		return this.workPayload;
	}
	
	/**
	 * Creates a work item
	 * @param workPayload The payload
	 * @param subjects The list of sibjects
	 * @param sourceClass The name of the source class that created this message
	 * @param sourceMethod This is the name of the source method that created this class
	 */
	public AsyncWorkItem(T workPayload, BoilerplateList<String> subjects
			,String sourceClass, String sourceMethod){
		this.workPayload = workPayload;
		this.source = sourceClass+"."+sourceMethod;
		this.subjects = subjects;
		this.uniqueRequestIdOfCreator = RequestThreadLocal.getRequestId();
	}

	/**
	 * This gets the unique id of the job which is processing this
	 * @return the id of the job
	 */
	public String getUniqueRequestIdOfJob() {
		return this.uniqueRequestIdOfJob;
	}

	/**
	 * Sets the unique id of the job processing the request
	 * @param uniqueRequestIdOfJob The unique id of the job
	 */
	public void setUniqueRequestIdOfJob(String uniqueRequestIdOfJob) {
		this.uniqueRequestIdOfJob = uniqueRequestIdOfJob;
	}
}
