package com.boilerplate.database.mysql.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IDynamicCustomRequest;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.DynamicCustomRequestEntity;
import com.boilerplate.database.interfaces.RDBMSUtilities;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class implements the IDynamicCustomRequest
 * 
 * @author shiva
 *
 */

public class MySQLDynamicCustomRequest extends MySQLBaseDataAccessLayer
        implements IDynamicCustomRequest {

    /**
     * This variable is used to hold the key for data count when we are execute
     * getRequestedDataCount then the result is in the form of (key : value) for
     * extract the value from this result we use 'keyForResultCount'
     */
    private static String keyForResultCount = "COUNT(1)";

    /**
     * This variable is used to hold the key for get default limit from
     * configuration Manager
     */
    private static String defaultLimit = "Default_Select_Limit";

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

    /**
     * @see IDynamicCustomRequests.dynamicCustomRequests
     */
    @Override
    public DynamicCustomRequestEntity getRequestedData(
            DynamicCustomRequestEntity dynamicCustomRequestEntity)
            throws ValidationFailedException, BadRequestException {
        // Validate query
        RDBMSUtilities.validate(dynamicCustomRequestEntity.getQueryString());
        // Check limit it is defined in request or greater than zero
        if (dynamicCustomRequestEntity.getLimit() <= 0) {
            // If limit is not defined in request then set limit by default
            // limit
            dynamicCustomRequestEntity.setLimit(Long
                    .parseLong(this.configurationManager.get(defaultLimit)));
        }
        // Get requested data count and then set resultCount
        dynamicCustomRequestEntity.setResultCount(this.getRequestedDataCount(
                dynamicCustomRequestEntity.getQueryString()));
        // Process request and return fetch data from database
        return this.processRequest(dynamicCustomRequestEntity);
    }

    /**
     * @see IDynamicCustomRequests.getRequestedResultCount
     */
    @Override
    public long getRequestedDataCount(String queryString)
            throws BadRequestException {
        // Make a new instance of BoilerplateMap used for define query
        // parameters
        BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
        // Execute query and get the result then initialize 'requestedData' with
        // the result
        List<Map<String, Object>> requestedData = super.executeSelectNative(
                // Modify query string
                RDBMSUtilities.getResultCountQuery(queryString),
                queryParameterMap);
        // Return requested data count
        return Long.parseLong(
                (requestedData.get(0).get(keyForResultCount)).toString());
    }

    /**
     * This method is used for processing the request and then return the
     * requested data
     * 
     * @param dynamicCustomRequestEntity
     *            have information about what data to be fetch from the data store.
     * @return The requested data along with the input data.
     * @throws BadRequestException
     *             This exception is thrown when the request is invalid in the
	 *             context of the data store.for example if against a RDBMS we
	 *             execute a command which is not valid for the database in the
	 *             form of column name or table name etc.
     * 
     */
    private DynamicCustomRequestEntity processRequest(
            DynamicCustomRequestEntity dynamicCustomRequestEntity)
            throws BadRequestException {
        // Make a new instance of BoilerplateMap ,used to define query
        // parameters
        BoilerplateMap<String, Object> queryParameterMap = new BoilerplateMap<String, Object>();
        // Make a new instance of list of BoilerplateMap
        BoilerplateList<BoilerplateMap<String, BoilerplateList<BoilerplateMap<String, Object>>>> listOfObject = new BoilerplateList<>();
        // Execute request
        List<Map<String, Object>> requestedDataList = super.executeSelectNative(
                // Modify query string
                RDBMSUtilities.modifyQueryString(dynamicCustomRequestEntity),
                queryParameterMap);

        // Declared a new variable for count the for-loop action
        long count = 0;
        for (Map<String, Object> requestedData : requestedDataList) {
            // Make a new instance of BoilerplateMap used to map count and
            // requestedData
            BoilerplateMap<String, Object> requestedDataMap = new BoilerplateMap<>();
            requestedDataMap.put(String.valueOf(count), requestedData);
            listOfObject.add(requestedDataMap);
            // Increment the count by 1
            count++;
        }
        // Set queryResponseList by requested data
        dynamicCustomRequestEntity.setQueryResponseList(listOfObject);
        // return requested data
        return dynamicCustomRequestEntity;
    }
}
