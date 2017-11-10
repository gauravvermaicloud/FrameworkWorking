package com.boilerplate.java.entities;

import java.io.File;
import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A File", description="This is a file entity", parent=BaseEntity.class)
public class FileEntity extends BaseEntity implements Serializable{

	/**
	 * This is the name of the file
	 */
	@JsonIgnore
	private String fileName;
	
	/**
	 * This gets the file name
	 * @return The file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * This sets the file name
	 * @param fileName The file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This gets content type
	 * @return The content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * This sets the content type
	 * @param contentType The content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * This gets the user id
	 * @return The user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This sets the user id
	 * @param userId The user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This gets the master file tag
	 * @return the master file tag
	 */
	public String getFileMasterTag() {
		return fileMasterTag;
	}

	/**
	 * This sets the master file tag
	 * @param fileMasterTag The master file tag
	 */
	public void setFileMasterTag(String fileMasterTag) {
		this.fileMasterTag = fileMasterTag;
	}

	/**
	 * This is the file name on the disk
	 * @return The file name on disk
	 */
	public String getFileNameOnDisk() {
		return fileNameOnDisk;
	}

	/**
	 * This sets the file name on disk
	 * @param fileNameOnDisk The file name on disk
	 */
	public void setFileNameOnDisk(String fileNameOnDisk) {
		this.fileNameOnDisk = fileNameOnDisk;
	}

	/**
	 * This gets the full file name on disk
	 * @return The full file name on disk
	 */
	public String getFullFileNameOnDisk() {
		return fullFileNameOnDisk;
	}
	
	/**
	 * This sets the full file name on disk
	 * @param fullFileNameOnDisk The full file name on disk
	 */
	public void setFullFileNameOnDisk(String fullFileNameOnDisk) {
		this.fullFileNameOnDisk = fullFileNameOnDisk;
	}

	/**
	 * This is the content type of file
	 */
	@ApiModelProperty(value="This is the file content type")
	private String contentType;
	
	/**
	 * This is the user id of the file
	 */
	@ApiModelProperty(value="This is the id of the user who is saving the file")
	private String userId;
	
	/**
	 * This is the file master tag
	 */
	@ApiModelProperty(value="This is the file master tag")
	private String fileMasterTag;
	
	/**
	 * This is the file name on disk
	 */
	@JsonIgnore
	private String fileNameOnDisk;
	
	/**
	 * This is the full file name on disk
	 */
	private String fullFileNameOnDisk;
	
	/**
	 * @see base.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	/**
	 * @see base.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see base.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

	/**
	 * Gets the file
	 * @return The file
	 */
	public java.io.File getFile() {
		if(this.file == null){
			this.file = new File(this.fullFileNameOnDisk);
		}
		return file;
	}

	/**
	 * Sets the file
	 * @param file The file
	 */
	public void setFile(java.io.File file) {
		this.file = file;
	}

	/**
	 * This method gets metadata
	 * @return The metadata
	 */
	public BoilerplateMap<String,String> getMetaData() {
		return metaData;
	}

	/**
	 * This method sets metadata
	 * @param metaData The metadata
	 */
	public void setMetaData(BoilerplateMap<String,String> metaData) {
		this.metaData = metaData;
	}

	/**
	 * Gets the organization Id
	 * @return The organization id
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * Sets the organization id
	 * @param organizationId The organization id
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * Gets the relative sftp url
	 * @return
	 */
	public String getRelativeSFTPURL() {
		return "/"+this.fileNameOnDisk;
	}

	/**
	 * Sets the relative sftp url
	 * @param relativeSFTPURL
	 */
	public void setRelativeSFTPURL(String relativeSFTPURL) {
		this.relativeSFTPURL = relativeSFTPURL;
	}

	/**
	 * This is the file
	 */
	@JsonIgnore
	private java.io.File file;
	
	/**
	 * This is the boiler plate map
	 */
	private BoilerplateMap<String,String> metaData = new BoilerplateMap();
	
	/**
	 * This is the orgnization id of the user creating the file
	 */
	@JsonIgnore
	private String organizationId;
	
	/**
	 * This is the relative url for accessfrom sftp
	 */
	@ApiModelProperty(value="This is the relative url for accessfrom sftp, to access this over sftp add the shared base sftp url")
	private String relativeSFTPURL;
		
}
