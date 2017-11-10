package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IMethodPermissions;
import com.boilerplate.database.mysql.implementations.entities.Content;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.MethodPermissions;

public class MySqlMethodPermissions extends MySQLBaseDataAccessLayer
		implements IMethodPermissions {

	/**
	 * @see IMethodPermissions.getMethodPermissions
	 */
	@Override
	public BoilerplateMap<String, MethodPermissions> getMethodPermissions() {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// begin a transaction
			Transaction transaction = session.beginTransaction();
			List<MethodPermissions> permissions = session
					.createCriteria(MethodPermissions.class).list();
			BoilerplateMap<String, MethodPermissions> permissionMap = new BoilerplateMap<String, MethodPermissions>();
			for (MethodPermissions permission : permissions) {
				permissionMap.put(permission.getMethodName(), permission);
			}
			return permissionMap;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} // end finally
	}

}
