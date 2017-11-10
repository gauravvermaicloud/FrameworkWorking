package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A User", description="This is a user entity for update", parent=BaseEntity.class)
public class UpdateUserEntity extends BaseEntity{
	@ApiModelProperty(value="This contains the list of properties to extend the user model"
			,required=true,notes="The keys should be unique in this system.")
	/**
	 * This is the dictionary of the user meta data.
	 */
	private BoilerplateMap<String,String> userMetaData = new BoilerplateMap<String, String>(); 
	/**
	 * This method returns the user meta data
	 * @return
	 */
	public BoilerplateMap<String, String> getUserMetaData() {
		return userMetaData;
	}

	public void setUserMetaData(BoilerplateMap<String, String> userMetaData) {
		this.userMetaData = userMetaData;
	}

	@JsonIgnore
	/**
	 * This is the password.
	 */
	private String password;
	/**
	 * This method gets the password
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the password
	 * @param password The password
	 */
	public void setPassword(String password){
		this.password= password;
	}
	
	/**
	 * This method hash's the password
	 */
	public void hashPassword(){
		this.password = String.valueOf(Encryption.getHashCode(this.password));
	}

	/**
	 * This method checks if the password is null or empty. Null or empty
	 * passwords are not allowed in the system.
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if(this.getPassword() ==null) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
		if(this.getPassword().equals("")) throw new ValidationFailedException(
				"User","Password is null/Empty",null);
		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	/**
	 * This gets the use status
	 * @return The status of user
	 */
	public int getUserStatus() {
		return userStatus;
	}

	/**
	 * This sets the status of the user
	 * @param userStatus
	 */
	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * Gets user key
	 * @return The key
	 */
	public String getUserKey() {
		return userKey;
	}

	/**
	 * Sets the user key
	 * @param userKey The key
	 */
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getCrmid() {
		return crmid;
	}

	public void setCrmid(String crmid) {
		this.crmid = crmid;
	}

	private String crmid;

	/**
	 * This is the status of user
	 * 0 - disabled
	 * 1 - enabled
	 * This will be eventually set into an enum, however for now we are not sure
	 * how this enum will span out.
	 * Further as this stands we think this will not be used by end user.
	 */
	@JsonIgnore
	private int userStatus = 1;
	
	/**
	 * The user key for email authorization
	 */
	@JsonIgnore
	private String userKey;
	
	/**
	 * This method gets the state of the User.
	 * @return userState The User State
	 */
	public MethodState getUserState() {
		return userState;
	}

	/**
	 * This method sets the state of the User.
	 * @param methodState The User State
	 */
	public void setUserState(MethodState methodState) {
		this.userState = methodState;
	}

	/**
	 * This is state of user which tell us about milestones covered by the user.
	 */
	@ApiModelProperty(value="This is state of the user"
			,required=false)
	private MethodState userState;
	
	/** 
	 * This is the owner Id
	 */
	private String ownerId;
	
	/** 
	 * This is the source
	 */
	private String source;
	
	/** 
	 * This is the subsource
	 */
	private String subsource;
	
	/**
	 * This metthod gets the owner Id
	 * @return ownerId The owner Id
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * This method sets the ownerId
	 * @param ownerId The owner Id
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * This method gets the source
	 * @return source The source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * This method sets the source
	 * @param source The source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * This method gets the subsource
	 * @return The sub source
	 */
	public String getSubsource() {
		return subsource;
	}

	/**
	 * This method sets the sub source
	 * @param subsource The Sub source
	 */
	public void setSubsource(String subsource) {
		this.subsource = subsource;
	}
	
	/**
	 * This is the user experian report url
	 */
	private String experianReportUrl;
	
	/**
	 * This method gets the user experian report url
	 * @return The user experian report url
	 */
	public String getExperianReportUrl() {
		return experianReportUrl;
	}

	/**
	 * This method sets the user experian report url
	 * @param experianReportUrl
	 */
	public void setExperianReportUrl(String experianReportUrl) {
		this.experianReportUrl = experianReportUrl;
	}

	/**
	 * This method gets the expressStatus
	 * @return The expressStatus
	 */
	public String getExpressStatus() {
		return expressStatus;
	}

	/**
	 * This method sets the expressStatus
	 * @param expressStatus The expressStatus
	 */
	public void setExpressStatus(String expressStatus) {
		this.expressStatus = expressStatus;
	}

	/**
	 * This method gets the oldAttemptExperianData
	 * @return The oldAttemptExperianData
	 */
	public String getOldAttemptExperianData() {
		return oldAttemptExperianData;
	}

	/**
	 * This method sets the oldAttemptExperianData
	 * @param oldAttemptExperianData The oldAttemptExperianData
	 */
	public void setOldAttemptExperianData(String oldAttemptExperianData) {
		this.oldAttemptExperianData = oldAttemptExperianData;
	}

	/*
	 * This is the expressStatus
	 */
	private String expressStatus;
	
	/**
	 * This is the oldAttemptExperianData
	 */
	@JsonIgnore
	private String oldAttemptExperianData;
	
	/**
	 * This method get the approved
	 * @return the approved
	 */
	public String getApproved() {
		return approved;
	}

	/**
	 * This method set the approved
	 * @param approved the approved to set
	 */
	public void setApproved(String approved) {
		this.approved = approved;
	}

	private String approved;
	
	/**
	 * This method get the value of disableForReport
	 * @return the disableForReport
	 */
	public String getDisableForReport() {
		return disableForReport;
	}

	/**
	 * This method set the value of disableForReport
	 * @param disableForReport the disableForReport to set
	 */
	public void setDisableForReport(String disableForReport) {
		this.disableForReport = disableForReport;
	}
	/**
	 * We are taking string because for null check.
	 */
	private String disableForReport;
	
}


