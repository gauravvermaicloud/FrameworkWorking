package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This method is a base of data access layer
 * 
 * @author gaurav.verma.icloud
 *
 */
public class MySQLBaseDataAccessLayer {
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLBaseDataAccessLayer.class);
	/**
	 * This method creates an object in the database
	 * 
	 * @param t
	 *            The object to be created
	 * @return The object as it stands after creation
	 */
	public <T> T create(T t) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(t);
			// commit the transaction
			transaction.commit();
			return t;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This method creates a list of objects in the database
	 * 
	 * @param ts
	 *            The list of object's to be created
	 * @return The object as it stands after creation
	 */
	public <T> List<T> create(List<T> ts) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			for (T t : ts) {
				session.saveOrUpdate(t);
			}
			// commit the transaction
			transaction.commit();
			return ts;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This method is used to update an item in the database
	 * 
	 * @param t
	 *            The item to be updated
	 * @return The item after update
	 */
	public <T> T update(T t) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(t);
			// commit the transaction
			transaction.commit();
			return t;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * This api deletes the object from database
	 * 
	 * @param t
	 *            The item to be deleted
	 */
	public <T> void delete(T t) {
		BoilerplateList<Object> objects = new BoilerplateList<Object>();
		objects.add(t);
		this.delete(objects);
	}

	/**
	 * This method deletes a list of objects
	 * 
	 * @param objects
	 *            The objects to be deleted.
	 */
	public void delete(List<Object> objects) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			// delete the object
			for (Object object : objects) {
				session.delete(object);
			}
			// commit
			transaction.commit();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}

	/**
	 * This method execute the select native query and return the list of object
	 * 
	 * @param sqlQuery
	 *            The query to get the data from the database.
	 * @sqlQuery example: "Select MyBankStatusClick as publishData from
	 *           PublishData where PrimaryKey = :PRIMARYKEY"
	 * @param parameters
	 *            The parameter map parameter: {"PRIMARYKEY":"5425"}
	 * @return The list of object
	 * @throws BadRequestException
	 *             This exception is thrown if the mechanism to query the
	 *             database is invalid in the context of the mysql database.
	 * @response: [{publishData=[[{"result": "Hello world"}, {"result": "Hello
	 *            india"}], [{"result": "Hello world"}, {"result": "Hello
	 *            india"}]]}]
	 * 
	 */
	// TODO - test this method by calling 1000 times and check connection leak
	public List<Map<String, Object>> executeSelectNative(String sqlQuery,
			BoilerplateMap<String, Object> parameters)
			throws BadRequestException {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getOnlySelectSessionFactory()
					.openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// create query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, (parameters.get(key)));
			}
			// this function convert the data to column name and value pair
			query.setResultTransformer(
					AliasToEntityMapResultTransformer.INSTANCE);
			List<Map<String, Object>> aliasToValueMapList = query.list();
			transaction.commit();
			return aliasToValueMapList;
		} catch (Exception e) {
			logger.logException("MySQLBaseDataAccessLayer", "executeSelectNative",
					"Exception", e.toString(), e);
			throw new BadRequestException("Data",
					"The request is invalid in the context of the MYSQl database.",
					null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method execute the scalor native query and return the number of rows
	 * affected.
	 * 
	 * @param sqlQuery
	 *            The sql query string to update or create the data in the
	 *            database.
	 * @sqlQuery example Update PublishData set MyBankStatusClick =
	 *           '[[{"result": "Hello world"}, {"result": "Hello india"}],
	 *           [{"result": "Hello world"}, {"result": "Hello
	 *           india"}],[{"result": "Hello world"}, {"result": "Hello US"}]]'
	 *           where PrimaryKey = :PRIMARYKEY
	 * @param parameters
	 *            The parameters map .
	 * @parameters map {"PRIMARYKEY":"5425"}
	 * @return number of rows affected
	 * @throws BadRequestException
	 *             This exception is thrown if the mechanism to query the
	 *             database is invalid in the context of the mysql database.
	 */
	public int executeScalorNative(String sqlQuery,
			BoilerplateMap<String, Object> parameters)
			throws BadRequestException {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// get the user using a hsql query
			SQLQuery query = session.createSQLQuery(sqlQuery);
			for (String key : parameters.keySet()) {
				query.setParameter(key, parameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			transaction.commit();
			return rowsEffected;
		} catch (Exception e) {
			logger.logException("MySQLBaseDataAccessLayer", "executeScalorNative",
					"Exception", e.toString(), e);
			throw new BadRequestException("Data",
					"The request is invalid in the context of the data store.",
					null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method executes a given query
	 * 
	 * @param hSQLQuery
	 *            The query to be executed
	 * @param queryParameters
	 *            The query parameters
	 * @return result of query execution
	 */
	public <T> List<T> executeSelect(String hSQLQuery,
			BoilerplateMap<String, Object> queryParameters) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);

			for (String key : queryParameters.keySet()) {
				query.setParameter(key, queryParameters.get(key));
			}
			List<T> ts = query.list();
			transaction.commit();
			return ts;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method execute the update query
	 * 
	 * @param hSQLQuery
	 *            The hsql query
	 * @param queryParameters
	 *            The query parameters
	 * @return The number of rows affected.
	 */
	public int executeUpdate(String hSQLQuery,
			BoilerplateMap<String, Object> queryParameters) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			// get the user using a hsql query
			Query query = session.createQuery(hSQLQuery);

			for (String key : queryParameters.keySet()) {
				query.setParameter(key, queryParameters.get(key));
			}
			int rowsEffected = query.executeUpdate();
			transaction.commit();
			return rowsEffected;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}// end method

	/**
	 * This method get the list of object
	 * 
	 * @param claz
	 *            The class
	 * @return The object list
	 */
	public List get(Class claz) {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			return session.createCriteria(claz).list();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}
}
