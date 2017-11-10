package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;

/**
 * This class is used to fetch configurations from a MySQL database. The end
 * user must enable this class in the DI configuration to use it.
 * 
 * @author gaurav
 *
 */
public class MySQLConfigurations extends MySQLBaseDataAccessLayer
		implements IConfigurations {

	public MySQLConfigurations() {

	}

	/**
	 * @see IConfigurations.getConfigurations
	 */
	@Override
	public List<Configuration> getConfirguations(String version,
			String enviornment) {
		List<Configuration> configurations = null;
		try {
			// get all the configurations from the DB as a list
			String hsql = "From Configuration C Where C.version = :Version AND C.enviornment= :Enviornment";
			BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();

			// Get all the configuration for the verson ALL
			queryParameterMap.put("Version", "ALL");
			queryParameterMap.put("Enviornment", "ALL");
			configurations = super.executeSelect(hsql, queryParameterMap);

			// Get all the configuration for this enviornments for this version
			// We are getting this version after ALL, so that we can override
			// any version specific configuration
			queryParameterMap.put("Version", "ALL");
			queryParameterMap.put("Enviornment", enviornment);
			configurations.addAll(super.executeSelect(hsql, queryParameterMap));

			// Get all the configuration for this version for all enviornments
			// We are getting this version after ALL, so that we can override
			// any version specific configuration
			queryParameterMap.put("Version", version);
			queryParameterMap.put("Enviornment", "ALL");
			configurations.addAll(super.executeSelect(hsql, queryParameterMap));

			// get the configuration for this enviornment and version
			queryParameterMap.put("Version", version);
			queryParameterMap.put("Enviornment", enviornment);
			configurations.addAll(super.executeSelect(hsql, queryParameterMap));
			// return the said list
		} catch (Exception ex) {
			System.out.println(ex.toString());
			throw ex;
			// Not catching the exception because we want the system to
			// crash if config is not working
			// further at this stage the logger is not setup, hence we cant log
			// anything
			// the JVM will crash and the terminal will display the details of
			// crash
		}
		return configurations;
	}
}
