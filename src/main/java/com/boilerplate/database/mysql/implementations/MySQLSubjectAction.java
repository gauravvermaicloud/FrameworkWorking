package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.ISubjectAction;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class is used to get the subject action key value map.
 * 
 * @author love
 *
 */
public class MySQLSubjectAction extends MySQLBaseDataAccessLayer
		implements ISubjectAction {
	/**
	 * This hold the configuration key for select subject action query
	 */
	private static String selectSubjectActionKey = "Select_Subject_Action";
	/**
	 * This is used for hold subjectData
	 */
	private static String subjectData = "subject";
	/**
	 * This is used for hold actionData
	 */
	private static String actionData = "action";
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see ISubjectAction.getSubjectValue
	 */
	@Override
	public BoilerplateMap<String, String> getSubjectValue(String enviornment)
			throws BadRequestException {
		String sqlQuery = this.configurationManager.get(selectSubjectActionKey);
		sqlQuery = sqlQuery.replace("@subject", subjectData);
		sqlQuery = sqlQuery.replace("@action", actionData);
		BoilerplateMap<String, Object> queryparameterMap = new BoilerplateMap<String, Object>();

		List<Map<String, Object>> subjectActions = super.executeSelectNative(
				sqlQuery, queryparameterMap);
		BoilerplateMap<String, String> subjectActionMap = new BoilerplateMap<String, String>();
		for (Map subjectAction : subjectActions) {
			subjectActionMap.put(subjectAction.get(subjectData).toString(),
					subjectAction.get(actionData).toString());
		}

		return subjectActionMap;
	}

}
