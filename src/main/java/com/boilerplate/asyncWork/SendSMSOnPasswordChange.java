package com.boilerplate.asyncWork;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class sends a SMS when user password is changed
 * @author gaurav.verma.icloud
 *
 */
public class SendSMSOnPasswordChange implements IAsyncWorkObserver {

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
			this.sendSMS(externalFacingUser.getFirstName(),getDsaPhoneNumber(externalFacingUser.getDsaId()));
		}else{
			this.sendSMS(externalFacingUser.getFirstName(),externalFacingUser.getPhoneNumber());
		}
	}

	/**
	 * This method sends the SMS.
	 * The reason we are keeping it public is so that
	 * compensating sync code may be written if queues go down
	 * @param firstName The first name of the user
	 * @param The phone number
	 * @throws Exception If there is an error in processing
	 */
	public void sendSMS(String firstName,String phoneNumber) throws Exception{
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("PASSWORD_CHANGE_SMS");
		message = message.replace("@FirstName",firstName);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		HttpResponse smsGatewayResponse= HttpUtility.makeHttpRequest(url, null, null, null, "GET");
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
