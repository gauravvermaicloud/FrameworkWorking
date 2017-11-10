package com.boilerplate.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This is a hibernate utiltiy. It alows using multiple databases.
 * 
 * @author gaurav.verma.icloud
 */
public class HibernateUtility {
	/**
	 * This is the property name of core connection file.
	 */
	private static String coreConnectionFileName = "CoreDatabaseConnection";
	/**
	 * This is the property name of only select connection file.
	 */
	private static String onlySelectDatabaseConnection = "onlySelectDatabaseConnection";

	/**
	 * This is the map of session factory, it has a session factory for each
	 * connection string file
	 */
	private static BoilerplateMap<String, SessionFactory> sessionFactoryMap = new BoilerplateMap<String, SessionFactory>();

	/**
	 * This is the name of the core connection string.
	 */
	private static String coreConnectionStringFileName = HibernateUtility
			.getConnectionName(coreConnectionFileName);

	/**
	 * This is the name of the only select connection string.
	 */
	private static String onlySelectConnectionStringFileName = HibernateUtility
			.getConnectionName(onlySelectDatabaseConnection);

	/**
	 * This methods creates a new session factory.
	 * 
	 * @param hibernateCfgFileName
	 *            The name of the session factory configuration file
	 * @return An instance of session factory
	 */
	private static SessionFactory buildSessionFactory(
			String hibernateCfgFileName) {
		try {

			Configuration configuration = new Configuration();
			configuration.configure(coreConnectionStringFileName);
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			return configuration.buildSessionFactory(serviceRegistry);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			throw ex;
		}
	}

	/**
	 * Gets an instance of session factory for the given file name
	 * 
	 * @param hibernateCfgFileName
	 *            The name of hibernate configuration file
	 * @return The session factory
	 */
	public static SessionFactory getSessionFactory(
			String hibernateCfgFileName) {
		if (sessionFactoryMap.containsKey(hibernateCfgFileName)) {
			return sessionFactoryMap.get(hibernateCfgFileName);
		} else {
			SessionFactory sessionFactory = new org.hibernate.cfg.Configuration()
					.configure(hibernateCfgFileName).buildSessionFactory();
			// SessionFactory sessionFactory =
			// HibernateUtility.buildSessionFactory(hibernateCfgFileName);
			sessionFactoryMap.put(hibernateCfgFileName, sessionFactory);
			return sessionFactoryMap.get(hibernateCfgFileName);
		}
	}

	/**
	 * This method returns session factory for core connection file
	 * 
	 * @return The session factory
	 */
	public static SessionFactory getSessionFactory() {
		return HibernateUtility.getSessionFactory(coreConnectionStringFileName);
	}

	/**
	 * This method returns the session factory for only select conection file.
	 * 
	 * @return The session factory.
	 */
	public static SessionFactory getOnlySelectSessionFactory() {
		return HibernateUtility
				.getSessionFactory(onlySelectConnectionStringFileName);
	}

	/**
	 * This method closes the core connection
	 */
	public static void close() {
		HibernateUtility.close(coreConnectionStringFileName);
	}

	/**
	 * This method closes the connection for the given file name
	 * 
	 * @param hibernateCfgFileName
	 *            The hibernate config file.
	 */
	public static void close(String hibernateCfgFileName) {
		if (sessionFactoryMap.containsKey(hibernateCfgFileName)) {
			sessionFactoryMap.get(hibernateCfgFileName).close();
		}
	}

	/**
	 * This method gets the name of the core connection string file from the
	 * properties file
	 * 
	 * @param propertyName
	 *            The property name
	 * @return The name of the core connection string
	 */
	private static String getConnectionName(String propertyName) {
		Properties properties = null;
		InputStream inputStream = null;
		String coreConnection = null;
		try {
			properties = new Properties();
			// Using the .properties file in the class path load the file
			// into the properties class
			inputStream = HibernateUtility.class.getClassLoader()
					.getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			coreConnection = (String) properties.get(propertyName);
		} catch (IOException ioException) {
			// we do not generally expect an exception here
			// and because we are start of the code even before loggers
			// have been enabled if something goes wrong we will have to print
			// it to
			// console. We do not throw this exception because we do not expect
			// it
			// and if we do throw it then it would have to be handeled in all
			// code
			// making it bloated, it is hence a safe assumption this exception
			// ideally will not
			// happen unless the file access has issues
			System.out.println(ioException.toString());
		} finally {
			// close the input stream if it is not null
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					// if there is an issue closing it we just print it
					// and move forward as there is not a good way to inform
					// user.
					System.out.println(ex.toString());
				}
			}
		} // end finally
		return coreConnection;
	}// end method
}
