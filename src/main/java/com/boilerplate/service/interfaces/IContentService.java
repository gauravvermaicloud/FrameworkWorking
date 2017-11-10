package com.boilerplate.service.interfaces;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IContent;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericKeyValuePairEncapsulationEntity;

/**
 * This interface has service for managing content
 * @author gaurav.verma.icloud
 *
 */
public interface IContentService {
	/**
	 * Gets the key for the users locale as peresnt on thread, if none found then returns 
	 * the default ALL value
	 * @param key The key
	 * @param locale The locale
	 * @return The content
	 */
	public String getContent(String key);
	
	/**
	 * Returns entire configuration map
	 * @return Configuration map
	 */
	public BoilerplateMap<String,BoilerplateMap<String,String>> getContentLibrary();
	
	/**
	 * This method creates content.
	 * @param keyValue The key value pair to be created / updated
	 * @param organizationId The id of the organization
	 * @param userId The id of the user
	 */
	public void createOrUpdate(GenericKeyValuePairEncapsulationEntity keyValue
			,String organizationId,String userId,String locale);
	
	/**
	 * This api gets the content for an organization
	 * @param organizationId The id of the organization
	 * @return
	 */
	public BoilerplateMap<String,BoilerplateMap<String,String>> getOrganizationContent(String organizationId);
}
