package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IContent;
import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericKeyValuePairEncapsulationEntity;
import com.boilerplate.service.interfaces.IContentService;

public class ContentService implements IContentService{
	/**
	 * The DAL layer for contnent
	 */
	@Autowired
	IContent content;
	
	/**
	 * Sets the dal for content
	 * @param content The content dal
	 */
	public void setContent(IContent content){
		this.content =content;
	}
	
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
	

	
	public void initialize(){
		contentByLocale = content.getContent(configurationManager.get("Enviornment"), 
				configurationManager.get("CMD_Organization_Id")
				);
	}
	/**
	 * This gets the content by locale
	 */
	private BoilerplateMap<String,BoilerplateMap<String,String>> contentByLocale = null;

	/**
	 * @see IContentService.getContent
	 */
	@Override
	public String getContent(String key) {
		//TODO - We should find the locale from thread
		//although to do this we should put it on thread too
		String locale = "en-US";
		return contentByLocale.get(locale).get(key);
	}

	/**
	 * @see IContentService.getContentLibrary
	 */
	@Override
	public BoilerplateMap<String, BoilerplateMap<String, String>> getContentLibrary() {
		return contentByLocale;
	}



	@Override
	public void createOrUpdate(GenericKeyValuePairEncapsulationEntity keyValue,
			String organizationId, String userId, String locale) {
		
		
	}



	@Override
	public BoilerplateMap<String, BoilerplateMap<String, String>> getOrganizationContent(
			String organizationId) {
		return content.getContent(configurationManager.get("Enviornment"), 
				organizationId
				);
	}

}
