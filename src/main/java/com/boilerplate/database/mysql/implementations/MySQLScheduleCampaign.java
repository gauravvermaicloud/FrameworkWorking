package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.boilerplate.database.interfaces.IScheduleCampaign;
import com.boilerplate.database.interfaces.RDBMSUtilities;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ScheduleCampaignEntity;

public class MySQLScheduleCampaign extends MySQLBaseDataAccessLayer implements IScheduleCampaign {
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLScheduleCampaign.class);
	/** 
	 * @see IScheduleCampaign.saveScheduleRequest
	 */
	@Override
	public void saveScheduleRequest(
			ScheduleCampaignEntity scheduleCampaignEntity) throws BadRequestException {
		Session session = null;
		try {
			// Validate query
	        RDBMSUtilities.validate(scheduleCampaignEntity.getQuery());
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			session.save(scheduleCampaignEntity);
			transaction.commit();
		} catch (Exception ex) {
			logger.logException("MySQLScheduleCampaign", "saveScheduleRequest",
					"Exception", ex.toString(), ex);
			throw new BadRequestException("ScheduleCampaign", "Data couldn't save into the database.",
					null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}
	/**
	 * @see IScheduleCampaign.getScheduledData
	 */
	@Override
	public List<Map<String, Object>> getScheduledData(String query) 
			throws BadRequestException {
		// Make a new instance of BoilerplateMap ,used to define query
        // parameters
        BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
        // Make a new instance of list of BoilerplateMap
        BoilerplateList<BoilerplateMap<String, Object>> 
        listOfObject = new BoilerplateList<>();
        // Execute request
        return super.executeSelectNative(query,queryParameterMap);

        
        
	}
		
}

