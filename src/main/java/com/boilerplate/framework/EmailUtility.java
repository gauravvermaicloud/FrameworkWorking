package com.boilerplate.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.cache.CacheFactory;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class contains code for email
 * @author gaurav.verma.icloud
 *
 */
public class EmailUtility {

	/**
	 * The propeorties for the email
	 */
	private static Properties properties  = null;
	
	/**
	 * This is used to send email
	 * @param tos The tos for the email
	 * @param subject The subject of message
	 * @param body The body of the message
	 * @param attachments the attachments to be sent
	 * @throws Exception If an error occurs while sending the email.
	 */
	public static void send(BoilerplateList<String> tos, BoilerplateList<String> ccs,
		BoilerplateList<String> bccs,
		String subject, String body,BoilerplateList<String> attachments) throws Exception{
		if(properties == null){
			loadProperties();
		}
		Session session = Session.getDefaultInstance(properties);
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(properties.getProperty("SmtpFrom"), properties.getProperty("SmtpFromName")));
		for(Object to:tos){
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress((String)to));	
		}
		for(Object cc:ccs){
			mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress((String)cc));	
		}
		for(Object bcc:bccs){
			mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress((String)bcc));	
		}
		mimeMessage.setSubject(subject);

		Multipart multipart = new MimeMultipart();
		
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body, properties.getProperty("SmtpContentType"));
		multipart.addBodyPart(messageBodyPart);
		if(attachments !=null){
			DataSource dataSource = null;
			for(Object file: attachments){
				String fileName = (String)file;
				messageBodyPart= new MimeBodyPart();
				dataSource = new FileDataSource(fileName);
				messageBodyPart.setDataHandler(new DataHandler(dataSource));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
			}
		}
		mimeMessage.setContent(multipart);
		
        Transport transport = session.getTransport();
        transport.connect((String)properties.get("SmptHost"), (String)properties.get("SmtpUsername")
        		, (String)properties.get("SmtpPassword"));
        
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        
        
	}
	
	private static void loadProperties(){
		
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					EmailUtility.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
		}
		catch(IOException ioException){
			//we do not generally expect an exception here
			//and because we are start of the code even before loggers
			//have been enabled if something goes wrong we will have to print it to
			//console. We do not throw this exception because we do not expect it
			//and if we do throw it then it would have to be handeled in all code 
			//making it bloated, it is hence a safe assumption this exception ideally will not
			//happen unless the file access has  issues
			System.out.println(ioException.toString());
		}
		finally{
			//close the input stream if it is not null
			if(inputStream !=null){
				try{
					inputStream.close();
				}
				catch(Exception ex){
					//if there is an issue closing it we just print it
					//and move forward as there is not a good way to inform user.
					System.out.println(ex.toString());
				}
			}
		}//end finally
	}
	
	/**
	 * this method provide the list of top ten email attachemnt
	 * @param userEmail The user name
	 * @param password The password
	 * @throws Exception The Exception
	 */
	public static BoilerplateList<MultipartFile> provideTopUnseenEmailAttachment(String userEmail, String password) throws Exception {

		if (properties == null) {
			loadProperties();
		}
		try{
			Session emailSession = Session.getDefaultInstance(properties);

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore("pop3s");
			//createconnection
			store.connect((String)properties.get("mail.pop3.host"), userEmail, password);
			//get inbox folder
			Folder folder = store.getFolder("inbox");
			//open folder in read write mode
			folder.open(Folder.READ_WRITE);
			// search for all "unseen" messages
			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			// search unseen messages
			Message message[] = folder.search(unseenFlagTerm);

			int c = folder.getUnreadMessageCount();

			BoilerplateList<MultipartFile> multipartUnreadAttachmentList = new BoilerplateList<>();
			
			boolean messageBreak =false;
			for (int a = 0; a < message.length; a++) {

				Multipart multipart = (Multipart) message[a].getContent();
				// start index from 1 skip text message content
				System.out.println(message[a].getSubject());
				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					InputStream stream = bodyPart.getInputStream();
					if(bodyPart.getFileName()!=null){
						MultipartFile multipartFileToUpload = new MockMultipartFile(bodyPart.getFileName(),
								bodyPart.getFileName(), bodyPart.getContentType(), IOUtils.toByteArray(stream));
						multipartUnreadAttachmentList.add(multipartFileToUpload);
						if(multipartUnreadAttachmentList.size()==10){
							messageBreak = true;
							break;
						}
					}
					
					
				}
				// set message seen
				message[a].setFlag(Flags.Flag.SEEN, true);
				if(messageBreak){
					break;
				}
			}
			//close folder and store connection
			folder.close(true);
			store.close();
			return multipartUnreadAttachmentList;
		}
		catch(Exception ex){
			System.out.println(ex.toString());
			
		}
		return null;

	}
}
