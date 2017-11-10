package com.boilerplate.database.mysql.implementations;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.boilerplate.database.interfaces.IDiagnostic;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.ClientSideDiagnostic;

public class MySQLDiagnostic extends MySQLBaseDataAccessLayer
		implements IDiagnostic {

	@Override
	public void publishClientSideLog(
			ClientSideDiagnostic clientSideDiagnostic) {
		Session session = null;
		try {
			session = HibernateUtility
					.getSessionFactory("mysqlDignosticLogs.hibernate.cfg.xml")
					.openSession();
			Transaction transaction = session.beginTransaction();
			session.save(clientSideDiagnostic);
			transaction.commit();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
