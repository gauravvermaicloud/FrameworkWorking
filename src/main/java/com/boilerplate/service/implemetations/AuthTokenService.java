package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.service.interfaces.IAuthTokenService;


public class AuthTokenService implements IAuthTokenService {
	
	/**
	 * The configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * Sets the configuration manager
	 * @param configurationManager The configuration manager
	 */
	
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * This method get the auth token from CRM.
	 */
	@Override
	public String getAuthToken() throws IOException, NotFoundException {
		
		BoilerplateMap<String,BoilerplateList<String>> requestHeaders = new BoilerplateMap();
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(
				configurationManager.get("Salesforce_Authtoken_URL")
				, null, null, null, "POST");
		if(httpResponse.getHttpStatus() != 200){
			throw new NotFoundException("Salesforce Auth Token", "Issue in getting auth token.", null);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String,Object> responseBodyMap= objectMapper.readValue(httpResponse.getResponseBody(), Map.class);
		String accessToken = responseBodyMap.get("access_token").toString();
		return accessToken;
	}
	
}
