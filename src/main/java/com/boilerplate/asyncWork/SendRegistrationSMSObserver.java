package com.boilerplate.asyncWork;

import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class sends a SMS when user registers
 * @author gaurav.verma.icloud
 *
 */
public class SendRegistrationSMSObserver implements IAsyncWorkObserver {

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
		if (externalFacingUser.getDsaId() !=null){
			this.sendSMS(externalFacingUser.getFirstName(), externalFacingUser.getPassword(), getDsaPhoneNumber(externalFacingUser.getDsaId()));
		}else{
			this.sendSMS(externalFacingUser.getFirstName(), externalFacingUser.getPassword(), externalFacingUser.getPhoneNumber());
			if(!(externalFacingUser.getAuthenticationProvider().equalsIgnoreCase(configurationManager.get("DSAAuthenticationProvider")))){
				this.sendCMDWelcomeSMS(externalFacingUser.getPhoneNumber());
			}
			
		}
		
	}
	

	/**
	 * This method sends the SMS.
	 * The reason we are keeping it public is so that
	 * compensating sync code may be written if queues go down
	 * @param firstName The first name of the user
	 * @param password The password
	 * @param The phone number
	 * @throws Exception If there is an error in processing
	 */
	public void sendSMS(String firstName, String password,String phoneNumber) throws Exception{
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("WELCOME_MESSAGE_SMS");
		message = message.replace("@FirstName",firstName);
		message = message.replace("@Password",password);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		if(!Boolean.valueOf(configurationManager.get("Db_Migrate"))){
			HttpResponse smsGatewayResponse= HttpUtility.makeHttpRequest(url, null, null, null, "GET");
		}
		
	}
	/**
	 * This method send the cmd welcome message to user.
	 * @param phoneNumber The user phone number
	 * @throws IOException
	 */
	public void sendCMDWelcomeSMS(String phoneNumber) throws IOException{
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("WELCOME_USER_CMD_MSG");
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		if(!Boolean.valueOf(configurationManager.get("Db_Migrate"))){
			HttpResponse smsGatewayResponse= HttpUtility.makeHttpRequest(url, null, null, null, "GET");
		}
	}
	
	/**
	 * This method gets the DSA phone Number
	 * @param dsaId The dsaId of DSA
	 * @return The DSA Phone Number
	 */
	private String getDsaPhoneNumber(String dsaId){
		String[] keySplitArray = dsaId.split(":");
		return keySplitArray[1];
	}
}
