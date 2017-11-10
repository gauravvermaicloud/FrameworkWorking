package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IContent;
import com.boilerplate.database.mysql.implementations.entities.Content;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;
import com.boilerplate.java.entities.GenericKeyValuePairEncapsulationEntity;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.Role;

public class MySQLContent extends MySQLBaseDataAccessLayer implements IContent {

	/**
	 * @see IContent.getContent
	 */
	@Override
	public BoilerplateMap<String, BoilerplateMap<String, String>> getContent(
			String enviornment, String organizationId) {
		// As we are not sure how this will span out in future
		// we are essentially doing some bad coding here.
		// We may end up emplying a DMS
		// hence for now hardcoding the logic to get content across versions
		// and across locales.
		// we assume for now locale will be en-US and version will be 1.0 or ALL
		// ideally however this code should work like the configuration
		// management code
		String hsql = "From Content C";
		BoilerplateMap<String, Object> queryparameterMap = new BoilerplateMap<String, Object>();

		List<Content> contents = super.executeSelect(hsql, queryparameterMap);
		BoilerplateMap<String, String> contentMap = new BoilerplateMap<String, String>();
		for (Content content : contents) {
			contentMap.put(content.getKey(), content.getValue());
		}

		BoilerplateMap<String, BoilerplateMap<String, String>> contentMapPerLocale = new BoilerplateMap<String, BoilerplateMap<String, String>>();
		// TODO - We should not be hard coding this but figuring this out
		// dynamically.
		contentMapPerLocale.put("en-US", contentMap);
		return contentMapPerLocale;
	}

	/**
	 * @see IContent.createOrUpdate
	 */
	@Override
	public void createOrUpdate(GenericKeyValuePairEncapsulationEntity keyValue,
			String organizationId, String userId, String locale) {
		if (locale == null) {
			locale = "ALL";
		}
		Content content = new Content();
		content.setKey(keyValue.getKey());
		content.setLocale(locale);
		content.setUserId(userId);
		content.setValue(keyValue.getValue());
		// hardocing version for now to ALL as we are not sure how this will
		// span out
		content.setVersion("ALL");
		super.create(content);

	}

}
