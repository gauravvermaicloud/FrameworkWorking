package com.boilerplate.asyncWork;

import java.io.File;
import java.net.URLEncoder;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class sends a Email when user registers
 * @author gaurav.verma.icloud
 *
 */
public class SendRegistrationEmailObserver implements IAsyncWorkObserver {

	/**
	 * This is the content service.
	 */
	@Autowired
	IContentService contentService;
	
	/**
	 * This sets the content service
	 * @param contentService This is the content service
	 */
	public void setContentService(IContentService contentService){
		this.contentService = contentService;
	}
	
	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;
	
	/**
	 * This sets the configuration Manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	
	
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ExternalFacingUser externalFacingUser = (ExternalFacingUser)asyncWorkItem.getPayload();
		
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(externalFacingUser.getEmail());
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();

		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		
		this.sendEmail(externalFacingUser.getFirstName(),tosEmailList, ccsEmailList, bccsEmailList
				,externalFacingUser.getPhoneNumber(),externalFacingUser.getUserKey());

	}
	
	public void sendEmail(String firstName,BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList, BoilerplateList<String> bccsEmailList, String phoneNumber,String userKey) throws Exception{
		String subject=contentService.getContent("WELCOME_MESSAGE_EMAIL_SUBJECT");
		subject = subject.replace("@FirstName",firstName);
		
	
 
		
		
		
		String body = contentService.getContent("WELCOME_MESSAGE_EMAIL_BODY");
		body = body.replace("@FirstName",firstName);
		body = body.replace("@Email",(String) tosEmailList.get(0));
		body = body.replace("@PhoneNumber",phoneNumber);
		body = body.replace("@UserKey",userKey==null?"":userKey);
		if(!Boolean.valueOf(configurationManager.get("Db_Migrate"))){
			EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body,null);
		}
		
	}

}
