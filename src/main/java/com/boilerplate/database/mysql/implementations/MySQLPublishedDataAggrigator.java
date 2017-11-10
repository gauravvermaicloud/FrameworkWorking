package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IPublishedDataAggrigator;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.PushTaskEntity;

/**
 * This class implements IPublishedDataAggrigator
 * 
 * @author shiva
 *
 */

public class MySQLPublishedDataAggrigator extends MySQLBaseDataAccessLayer
		implements IPublishedDataAggrigator {

	/**
	 * This hold the configuration key for select publish data query
	 */
	private static String selectPublishDataOnPrimaryKey = "Select_PublishData_On_PrimaryKey";

	/**
	 * This hold the configuration key for insert publish data query
	 */
	private static String insertPublishData = "Insert_PublishData";

	/**
	 * This hold the configuration key for update publish data query
	 */
	private static String updatePublishData = "Update_PublishData";

	/**
	 * This is used for hold publishData
	 */
	private static String publishData = "publishData";

	/**
	 * This is used for hold PrimaryKey value
	 */
	private static String primaryKeyvalue = "PRIMARYKEY";

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 *            to set configurationManager
	 */
	public void setConfigurationManager(
			ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/*
	 * @see IPublishedDataAggrigator.createNewPublishData
	 */
	@Override
	public void createNewPublishData(PushTaskEntity pushTaskEntity)
			throws BadRequestException {

		// convert the string into list type
		pushTaskEntity.setDataPayload(pushTaskEntity.toJSONList());

		if (this.getPublishData(pushTaskEntity).size() == 0) {
			// if the table does not have any record for that
			// primaryKey then insert new data into the table
			this.insertPublishedData(pushTaskEntity);
		} else {
			// if record found then Update the stored data
			this.updateRowData(pushTaskEntity);
		}
	}

	/*
	 * @see IPublishedDataAggrigator.updatePublishData
	 */
	@Override
	public void updatePublishData(PushTaskEntity pushTaskEntity)
			throws BadRequestException {
		// Check published primary key is exist or not in database
		List<Map<String, Object>> objectList = this
				.getPublishData(pushTaskEntity);

		// If the table does not have any record for published primaryKey
		// then insert new data into the table
		if (objectList.size() == 0) {
			// convert the string into list type
			pushTaskEntity.setDataPayload(pushTaskEntity.toJSONList());
			this.insertPublishedData(pushTaskEntity);
		}
		// If record found then Update the stored data
		else {
			// Check if select column is not null
			if (objectList.get(0).get(publishData) != null) {
				// convert the string into list type
				pushTaskEntity.setDataPayload(pushTaskEntity.toJSONList(
						(objectList.get(0).get(publishData)).toString()));
			} else {
				// convert the string into list type
				pushTaskEntity.setDataPayload(pushTaskEntity.toJSONList());
			}
			// call update method
			this.updateRowData(pushTaskEntity);
		}
	}

	/**
	 * This method is used for collecting data from table regarding published
	 * primaryKey
	 * 
	 * @param pushTaskEntity
	 *            Published Data
	 * @return collecting data
	 * @throws BadRequestException
	 *             If error comes in process the request.
	 */
	private List<Map<String, Object>> getPublishData(
			PushTaskEntity pushTaskEntity) throws BadRequestException {
		// This Query is used for collecting data from table regarding primary
		// key

		String sqlQuery = this.configurationManager
				.get(selectPublishDataOnPrimaryKey);
		sqlQuery = sqlQuery.replace("@Subject", pushTaskEntity.getSubject());
		sqlQuery = sqlQuery.replace("@PublishDataSubjectalias", publishData);
		sqlQuery = sqlQuery.replace("@PrimaryKeyValue", primaryKeyvalue);

		BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();

		// Get all the configuration for the version ALL
		queryParameterMap.put(primaryKeyvalue, pushTaskEntity.getPrimaryKey());

		List<Map<String, Object>> objectList = super.executeSelectNative(
				sqlQuery, queryParameterMap);
		return objectList;
	}

	/**
	 * This method is used for insert data into table if there is no entry for
	 * published primaryKey
	 * 
	 * @param pushTaskEntity
	 *            Published Data
	 * @return affectedRows Number of rows affected
	 * @throws BadRequestException
	 *             If error comes in process the request.
	 */
	private int insertPublishedData(PushTaskEntity pushTaskEntity)
			throws BadRequestException {

		// This query is used for inserting the data
		String sqlQuery = this.configurationManager.get(insertPublishData);
		sqlQuery = sqlQuery.replace("@Subject", pushTaskEntity.getSubject());
		sqlQuery = sqlQuery.replace("@DataPayLoad",
				pushTaskEntity.getDataPayload().replace("'", ""));
		sqlQuery = sqlQuery.replace("@PrimaryKeyValue",
				pushTaskEntity.getPrimaryKey());
		BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
		int affectedRows = super.executeScalorNative(sqlQuery,
				queryParameterMap);
		return affectedRows;
	}

	/**
	 * This method is used to update the selected column data
	 * 
	 * @param pushTaskEntity
	 *            Published Data
	 * @return affectedRows Number of rows affected
	 * @throws BadRequestException
	 *             If error comes in process the request.
	 */
	private int updateRowData(PushTaskEntity pushTaskEntity)
			throws BadRequestException {

		// This Query used for update column based on selected subject and
		// primaryKey
		String sqlQuery = this.configurationManager.get(updatePublishData);
		sqlQuery = sqlQuery.replace("@Subject", pushTaskEntity.getSubject());
		sqlQuery = sqlQuery.replace("@DataPayLoad",
				pushTaskEntity.getDataPayload().replace("'", ""));
		sqlQuery = sqlQuery.replace("@PrimaryKeyValue", primaryKeyvalue);

		BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
		queryParameterMap.put(primaryKeyvalue, pushTaskEntity.getPrimaryKey());
		int affectedRows = super.executeScalorNative(sqlQuery,
				queryParameterMap);
		return affectedRows;

	}

}
