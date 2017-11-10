package com.boilerplate.java.entities;

import com.boilerplate.java.collections.BoilerplateList;

/**
 * This class provides details of who can execute a given method
 * @author gaurav.verma.icloud
 *
 */
public class MethodPermissions {
	
	/**
	 * This is the method forward turing
	 */
	MethodForwardingTuringMachine methodForwardTuring = new MethodForwardingTuringMachine();

	/**
	 * Gets the id of the permission
	 * @return The id of the permission
	 */
	public String getId() {
		return id;
	}

	/**
	 * This method gets the method forward turing
	 * @return The methodForwardTuring
	 */
	public MethodForwardingTuringMachine getMethodForwardTuring() {
		return methodForwardTuring;
	}

	/**
	 * This method sets the method forward turing
	 * @param methodForwardTuring
	 */
	public void setMethodForwardTuring(MethodForwardingTuringMachine methodForwardTuring) {
		this.methodForwardTuring = methodForwardTuring;
	}

	/**
	 * Sets the id of the permission
	 * @param id The id of the permission
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the method name
	 * @return The method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets the method name
	 * @param methodName The method name
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * This tells if authentication is required
	 * @return True if user is to be authenticated to execute the method
	 */
	public boolean getIsAuthenticationRequired() {
		return isAuthenticationRequired;
	}

	/**
	 * Sets if user needs to be authenticated for executing the method
	 * @param isAuthenticationRequired The value of authentication status.
	 */
	public void setIsAuthenticationRequired(boolean isAuthenticationRequired) {
		this.isAuthenticationRequired = isAuthenticationRequired;
	}

	/**
	 * This returns if logging is required for the api
	 * @return true if is required false if not
	 */
	public boolean getIsLoggingRequired() {
		return isLoggingRequired;
	}

	/**
	 * This sets if logging is required for this api 
	 * @param isLoggingRequired True if logging is required
	 */
	public void setIsLoggingRequired(boolean isLoggingRequired) {
		this.isLoggingRequired = isLoggingRequired;
	}

	/**
	 * This method gets the Url to publish
	 * @return The Url to publish
	 */
	public String getUrlToPublish() {
		return urlToPublish;
	}

	/**
	 * This method sets the Url to publish
	 * @param urlToPublish The Url to publish
	 */
	public void setUrlToPublish(String urlToPublish) {
		this.urlToPublish = urlToPublish;
	}

	/**
	 * This method gets the publish method
	 * @return The publish method
	 */
	public String getPublishMethod() {
		return publishMethod;
	}

	/**
	 * This method sets the publish method
	 * @param publishMethod The Publish method
	 */
	public void setPublishMethod(String publishMethod) {
		this.publishMethod = publishMethod;
	}

	/**
	 * This method gets the publish business subject
	 * @return The publish business subject
	 */
	public String getPublishBusinessSubject() {
		return publishBusinessSubject;
	}

	/**
	 * This method sets the publish business subject
	 * @param publishBusinessSubject The publish business subject
	 */
	public void setPublishBusinessSubject(String publishBusinessSubject) {
		this.publishBusinessSubject = publishBusinessSubject;
	}

	/**
	 * This method gets the isPublishRequired
	 * @return The isPublishRequired
	 */
	public boolean isPublishRequired() {
		return isPublishRequired;
	}

	/**
	 * This method sets the isPublishRequired
	 * @param isPublishRequired The isPublishRequired
	 */
	public void setPublishRequired(boolean isPublishRequired) {
		this.isPublishRequired = isPublishRequired;
	}

	/**
	 * This method gets the publish template
	 * @return The publishTemplate
	 */
	public String getPublishTemplate() {
		return publishTemplate;
	}

	/**
	 * This method sets the publish Template
	 * @param publishTemplate The publishTemplate
	 */
	public void setPublishTemplate(String publishTemplate) {
		this.publishTemplate = publishTemplate;
	}

	/**
	 * This is the id of the permission object
	 */
	private String id;
	
	/**
	 * This is the fully qualified method name
	 */
	private String methodName;
	
	/**
	 * This tells if the user should be authenticated to execute the given method
	 */
	private boolean isAuthenticationRequired;
	
	/**
	 * This tells if logging is required for this api
	 */
	private boolean isLoggingRequired;
	
	private String urlToPublish;
	
	private String publishMethod;
	
	private String publishBusinessSubject;
	
	private boolean isPublishRequired;
	/**
	 * This is publish template
	 */
	private String publishTemplate;
	/**
	 * This tells if dynamic url is required for publish
	 */
	private boolean isDynamicPublishURl;
	/**
	 * This method get the dynamic publish URL.
	 * @return the dynamic url
	 */
	public boolean isDynamicPublishURl() {
		return isDynamicPublishURl;
	}
	/**
	 * This method set the dynamic publish URL.
	 * @return isDynamicPublishURl the dynamic url
	 */
	public void setDynamicPublishURl(boolean isDynamicPublishURl) {
		this.isDynamicPublishURl = isDynamicPublishURl;
	}
		
}
