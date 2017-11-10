package com.boilerplate.database.interfaces;

import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericKeyValuePairEncapsulationEntity;

public interface IContent {

	/**
	 * This method returns the entire content.
	 * @param enviornment The name of the enviornment for which content is being retreived
	 * @param organizationId - The id of the organization for which content is reterived
	 * @return A map of content for each locale for the given version.
	 * The content itself has key and value
	 */
	public BoilerplateMap<String,BoilerplateMap<String, String>> 
		getContent(String enviornment, String organizationId);
	
	/**
	 * This method creates a content
	 * @param keyValue The content key and value
	 * @param organizationId The organization id
	 * @param userId The user id
	 * @param locale The locale of content
	 */
	public void createOrUpdate(GenericKeyValuePairEncapsulationEntity keyValue
			,String organizationId,String userId,String locale);
}
