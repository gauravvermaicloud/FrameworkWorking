package com.boilerplate.database.redis.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.collections.BoilerplateSet;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.MethodForwardingTuringMachine;
import com.boilerplate.java.entities.MethodPermissions;
import com.boilerplate.java.entities.MethodState;
import com.boilerplate.java.entities.Role;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import scala.annotation.varargs;

public class BaseRedisDataAccessLayer{

	/**
	 * Redis database logger
	 */
	private static Logger  logger = Logger.getInstance(BaseRedisDataAccessLayer.class);

	/**
	 * Creates a base DB layer
	 */
	public BaseRedisDataAccessLayer(){
		
	}	
	
	/**
	 * The properties
	 */
	Properties properties  = null;
	
	/**
	 * SalesForce Production Base Url
	 */
	public static final String SalesForceProductionBaseUrl = "https://clearmydues.my.salesforce.com";
	
	/**
	 * SalesForce Development Base Url
	 */
	public static final String SalesForceDevelopmentBaseUrl = "https://clearmydues--Developmen.cs31.my.salesforce.com";
	
	/**
	 * SalesForce UAT Base Url
	 */
	public static final String SalesForceTestBaseUrl = "https://clearmydues--Developmen.cs31.my.salesforce.com";
	
	
	private static JedisPool pool = null;
	
	/**
	 * Gets a connection as a singleton
	 * @return A Jedis command
	 */
	protected Jedis getConnection(){
		

		if(BaseRedisDataAccessLayer.pool != null){
			return BaseRedisDataAccessLayer.pool.getResource();
		}
		//First check if this is just one local machine or a cluster
		
		//This is the connections to redis it is expected in format host:port;host:port;...
		String redisConnections = this.getDatabaseConnectionFormPropertiesFile();
		
		String[] hostPort;
		String host;
		int port;
		//split the connections with ;
		String[] connections = redisConnections.split(";");
		//if there are more than one connections we have a cluster
		hostPort = connections[0].split(":");
		host = hostPort[0];
		port = Integer.parseInt(hostPort[1]);
		//and create a non clustered command
				
		BaseRedisDataAccessLayer.pool = new JedisPool(new JedisPoolConfig(),host,port);
		logger.logInfo("RedisDatabase", "getConnection", "Creating Single",host+":"+port);

		createSeedData();
		return BaseRedisDataAccessLayer.pool.getResource();
	}
	
	
	//THIS IS A VERY BAD WAY TO DO THIS, need to find a better mechanism
	private void createSeedData(){
		BoilerplateMap<String,String> v1E1 = new BoilerplateMap<String,String>();
		BoilerplateMap<String,String> vAllEQA = new BoilerplateMap<String,String>();
		BoilerplateMap<String,String> v2EQA = new BoilerplateMap<String,String>();
		BoilerplateMap<String,String> vAllETest = new BoilerplateMap<String,String>();
		
		v1E1.put("V1_All", "V1_All");
		v1E1.put("V1_Dev", "V1_Dev");
		
		vAllEQA.put("V_All_QA", "V_All_QA");
		v2EQA.put("V_2_Dev", "V_2_Dev");
		v2EQA.put("V_2_QA","V_2_QA");
		
		v1E1.put("V1_All_B", "V1_All_B");		
		// load server specific configuration
		this.set("CONFIGURATION:V_ALL_E_TEST", Base.toXML(createSeedTestData()));

		this.set("CONFIGURATION:V_ALL_E_DEVELOPMENT", Base.toXML(
				createSeedDevelopmentData()));
		
		this.set("CONFIGURATION:V_ALL_E_PRODUCTION", Base.toXML(
				createSeedProductionData()));
			
		// load common configuration
		this.set("CONFIGURATION:V_ALL_E_ALL", Base.toXML(createSeedCommonData()));
		this.set("CONFIGURATION:V_1_E_1", Base.toXML(v1E1));
		
		this.set("CONFIGURATION:V_All_E_QA", Base.toXML(vAllEQA));
		
		//Storing seed content
		createSeedContent();
		// create seed user and role
		createSeedUserAndRole();
		// create method permission
		createMethodPermission();	
		}
	
	/**
	 * Gets the connection string from the properties file
	 * @return The connection string
	 */
	private String getDatabaseConnectionFormPropertiesFile(){
		InputStream inputStream =null;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					this.getClass().getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			return properties.getProperty("RedisDatabaseServer");
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
		return properties.getProperty("RedisDatabaseServer");
	}//end method
	
	/**
	 * Gets a key
	 * @param key The key
	 * @param typeOfClass
	 * @return
	 */
	public <T extends Base> T get(String key,Class<T> typeOfClass){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			Object o =jedis.get(key);
			if (o == null){
				return null;
			}
			T t= Base.fromXML(o.toString(),typeOfClass);
			return t;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		
	}
	
	/**
	 * Sets the value
	 * @param key The key
	 * @param value The value
	 */
	public <T extends Base> void set(String key,T value){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.set(key, value.toXML());
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		
	}
	
	/**
	 * Sets the value
	 * @param key The key
	 * @param value The value
	 */
	public <T extends Base> void set(String key,T value,int timeoutInSeconds){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.setex(key,timeoutInSeconds ,value.toXML());
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		
		
	}
	
	/**
	 * Deletes a key
	 * @param key The key
	 */
	public void delete(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.del(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		
	}
	
	/**
	 * Gets a key
	 * @param key The key
	 * @return
	 */
	public String get(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.get(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		
	}
	
	/**
	 * Sets the value
	 * @param key The key
	 * @param value The value
	 */
	public void set(String key,String value){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.set(key, value);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This Method insert the object at left head of Queue 
	 * @param queueName The Queue name in which we want to insert object
	 * @param object The object which we want to insert in to queue
	 */
	public void insert(String queueName, Object object){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.lpush(queueName, Base.toXML(object));
		}
		finally{
			if(jedis !=null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method remove last element of Queue  
	 * @param queueName The queue name from which we want to remove the last element
	 * @param typeOfClass The type of class in which we want to type cast 
	 * @return t The last element remove from the queue
	 */
	public <T> T remove(String queueName, Class<T> typeOfClass){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			String elementRemove = jedis.rpop(queueName);
			if (elementRemove == null){
				return null;
			}
			T t = Base.fromXML(elementRemove.toString(), typeOfClass);
			return t;
		}
		finally{
			if (jedis !=null){
				jedis.close();
			}
		}
	}

	/**
	 * This method remove last element of Queue  
	 * @param queueName The queue name from which we want to remove the last element
	 * @return t The last element remove from the queue
	 */
	public String remove(String queueName){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.rpop(queueName);
		}
		finally{
			if (jedis !=null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method get the queue size
	 * @param queueName The queue name from which we want to get the count
	 * @return the count of queue data present
	 */
	public Long getQueueSize(String queueName){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.llen(queueName);
		}
		finally{
			if (jedis !=null){
				jedis.close();
			}
		}
	}

	/**
	 * This method creates method permission.
	 */
	private void createMethodPermission() {
		// This attribute tells us about server name

		//method permissions
		BoilerplateMap<String, MethodPermissions> methodPermissionMap = new BoilerplateMap<>();
		
		MethodPermissions methodPermission = null;
		MethodForwardingTuringMachine methodForwardTuringMachine = null ;
		
	

		
		methodPermission = new MethodPermissions();
		methodPermission.setId("public com.boilerplate.java.entities.Ping com.boilerplate.java.controllers.HealthController.ping()");
		methodPermission.setMethodName("public com.boilerplate.java.entities.Ping com.boilerplate.java.controllers.HealthController.ping()");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodForwardTuringMachine = new MethodForwardingTuringMachine();
		methodForwardTuringMachine.setIsStateCheck(false);
		methodForwardTuringMachine.setIsGenericErrorUpdate(false);
		methodPermission.setMethodForwardTuring(methodForwardTuringMachine);
		methodPermission.setPublishBusinessSubject("CHECK_SERVER_STATUS");
		methodPermissionMap.put(methodPermission.getMethodName(),methodPermission);
		
		methodPermission = new MethodPermissions();
		methodPermission.setId("public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.authenticate(com.boilerplate.java.entities.AuthenticationRequest)");
		methodPermission.setMethodName("public com.boilerplate.sessions.Session com.boilerplate.java.controllers.UserController.authenticate(com.boilerplate.java.entities.AuthenticationRequest)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishBusinessSubject("USER_LOGIN");
		methodForwardTuringMachine = new MethodForwardingTuringMachine();
		BoilerplateSet<MethodState> userLoginInputStateSet = new BoilerplateSet<>();
		userLoginInputStateSet.add(MethodState.Registered);
		userLoginInputStateSet.add(MethodState.Validated);
		userLoginInputStateSet.add(MethodState.ExperianAttempt);
		userLoginInputStateSet.add(MethodState.ExperianError);
		userLoginInputStateSet.add(MethodState.AuthQuestions);
		userLoginInputStateSet.add(MethodState.KYCPending);
		userLoginInputStateSet.add(MethodState.KyCSubmitted);
		userLoginInputStateSet.add(MethodState.ReportGenerated);
		userLoginInputStateSet.add(MethodState.NegotiationInProgress);
		userLoginInputStateSet.add(MethodState.NegotiationCompleted);
		userLoginInputStateSet.add(MethodState.Paying);
		userLoginInputStateSet.add(MethodState.CompletedPayment);
		methodForwardTuringMachine.setInputStateSet(userLoginInputStateSet);
		methodForwardTuringMachine.setUserNextStateCheck(false);
		BoilerplateSet<MethodState> userLoginUserNextStateSet = new BoilerplateSet<>();
		userLoginUserNextStateSet.add(MethodState.Validated);
		userLoginUserNextStateSet.add(MethodState.ExperianAttempt);
		userLoginUserNextStateSet.add(MethodState.ExperianError);
		userLoginUserNextStateSet.add(MethodState.AuthQuestions);
		userLoginUserNextStateSet.add(MethodState.KYCPending);
		userLoginUserNextStateSet.add(MethodState.KyCSubmitted);
		userLoginUserNextStateSet.add(MethodState.ReportGenerated);
		userLoginUserNextStateSet.add(MethodState.NegotiationInProgress);
		userLoginUserNextStateSet.add(MethodState.NegotiationCompleted);
		userLoginUserNextStateSet.add(MethodState.Paying);
		userLoginUserNextStateSet.add(MethodState.CompletedPayment);
		methodForwardTuringMachine.setUserNextStateSet(userLoginUserNextStateSet);
		methodForwardTuringMachine.setIsStateCheck(false);
		methodForwardTuringMachine.setIsGenericErrorUpdate(false);
		methodForwardTuringMachine.setIsGenericSuccessUpdate(true);
		methodForwardTuringMachine.setSuccessState(MethodState.Validated);
		methodForwardTuringMachine.setMethodState(MethodState.Login);
		methodPermission.setMethodForwardTuring(methodForwardTuringMachine);
		methodPermissionMap.put(methodPermission.getMethodName(),methodPermission);
		
		methodPermission = new MethodPermissions();
		methodPermission.setId("public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setMethodName("public com.boilerplate.java.entities.ExternalFacingReturnedUser com.boilerplate.java.controllers.UserController.getCurrentUser()");
		methodPermission.setIsAuthenticationRequired(true);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("http://localhost");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodForwardTuringMachine = new MethodForwardingTuringMachine();
		methodForwardTuringMachine.setIsStateCheck(false);
		methodForwardTuringMachine.setIsGenericErrorUpdate(false);
		methodPermission.setMethodForwardTuring(methodForwardTuringMachine);
		methodPermission.setPublishBusinessSubject("GET_CURRENTLY_LOGGED_IN_USER");
		methodPermissionMap.put(methodPermission.getMethodName(),methodPermission);

		
		methodPermission = new MethodPermissions();
		methodPermission.setId("public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setMethodName("public com.boilerplate.java.entities.ExternalFacingUser com.boilerplate.java.controllers.UserController.createUser(com.boilerplate.java.entities.ExternalFacingUser)");
		methodPermission.setIsAuthenticationRequired(false);
		methodPermission.setIsLoggingRequired(true);
		methodPermission.setUrlToPublish("");
		methodPermission.setPublishMethod("POST");
		methodPermission.setPublishRequired(false);
		methodPermission.setDynamicPublishURl(false);
		methodPermission.setPublishTemplate("{\"id\": \"@Id\",\"userId\": \"@userId\",\"authenticationProvider\": \"@authenticationProvider\",\"externalSystemId\": \"@externalSystemId\",\"email\": \"@email\",\"firstName\": \"@firstName\",\"lastName\": \"@lastName\",\"middleName\": \"@middleName\",\"phoneNumber\": \"@phoneNumber\",\"experianRequestEmailKey\": \"@experianRequestEmailKey\",\"userState\": \"@userState\",\"ownerId\": \"@ownerId\",\"source\": \"@source\",\"subSource\": \"@subSource\",\"dsaId\": \"@dsaId\",\"referalSource\": \"@referalSource\"}");
		methodPermission.setPublishBusinessSubject("CREATE_USER");
		methodForwardTuringMachine = new MethodForwardingTuringMachine();
		methodForwardTuringMachine.setSuccessState(MethodState.Registered);
		methodForwardTuringMachine.setMethodState(MethodState.CreateUser);
		methodForwardTuringMachine.setIsStateCheck(false);
		methodForwardTuringMachine.setIsGenericErrorUpdate(false);
		methodForwardTuringMachine.setIsGenericSuccessUpdate(true);
		methodPermission.setMethodForwardTuring(methodForwardTuringMachine);
		methodPermissionMap.put(methodPermission.getMethodName(),methodPermission);
		
		

		
		
		
		
		
		
		
		
		
		

	}



	/**
	 * This method creates seed user and role.
	 */
	private void createSeedUserAndRole() {
		GenericListEncapsulationEntity<Role>  roles = new GenericListEncapsulationEntity<Role>();
		roles.setEntityList(new BoilerplateList<Role>());
		Role role =null;
		role = new Role("Admin","Admin","Admin of the system",true,false);
		roles.getEntityList().add(role);
		Role roleAdmin = role;
		role = new Role("RoleAssigner","RoleAssigner","This role can assign roles to other users",true,false);
		roles.getEntityList().add(role);
		Role roleAssigner = role;
		role = new Role("SelfAssign1","SelfAssign1","UT role",false,true);
		roles.getEntityList().add(role);
		role = new Role("SelfAssign2","SelfAssign2","UT role",false,true);
		roles.getEntityList().add(role);
		role = new Role("NonSelfAssign1","NonSelfAssign1","UT role",false,false);
		roles.getEntityList().add(role);
		role = new Role("NonSelfAssign2","NonSelfAssign2","UT role",false,false);
		roles.getEntityList().add(role);
		role = new Role("BackOfficeUser","BackOfficeUser","The back office user of the system",true,false);
		roles.getEntityList().add(role);
		role = new Role("BankAdmin","BankAdmin","The admin of the bank",true,false);
		roles.getEntityList().add(role);
		role = new Role("BankUser","BankUser","The user in the bank",true,false);
		roles.getEntityList().add(role);
		role = new Role("Impersinator","Impersinator","Is allowed impersination",true,false);
		roles.getEntityList().add(role);
		
		this.set("ROLES", Base.toXML(roles));
		
		
		//create annonnymous user
		ExternalFacingReturnedUser user = new ExternalFacingReturnedUser();
		user.setId("DEFAULT:ANNONYMOUS");
		user.setUserId("DEFAULT:ANNONYMOUS");
		user.setPassword("0");
		user.setAuthenticationProvider("DEFAULT");
		user.setExternalSystemId("DEFAULT:ANNONYMOUS");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		this.set("USER:"+user.getUserId(), user);
		
		//create admin
		user = new ExternalFacingReturnedUser();
		user.setId("DEFAULT:ADMIN");
		user.setUserId("DEFAULT:ADMIN");
		user.setPassword("password");
		user.setFirstName("Admin");
		user.hashPassword();
		user.setAuthenticationProvider("DEFAULT");
		user.setExternalSystemId("DEFAULT:ADMIN");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAdmin);
		this.set("USER:"+user.getUserId(), user);
		
		//create background
		user = new ExternalFacingReturnedUser();
		user.setId("DEFAULT:BACKGROUND");
		user.setUserId("DEFAULT:BACKGROUND");
		user.setPassword("0");
		user.setAuthenticationProvider("DEFAULT");
		user.setExternalSystemId("DEFAULT:BACKGROUND");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAdmin);
		this.set("USER:"+user.getUserId(), user);
		
		//create role assigner
		user = new ExternalFacingReturnedUser();
		user.setId("DEFAULT:ROLEASSIGNER");
		user.setUserId("DEFAULT:ROLEASSIGNER");
		user.setPassword("0");
		user.setAuthenticationProvider("DEFAULT");
		user.setExternalSystemId("DEFAULT:ROLEASSIGNER");
		user.setUserStatus(1);
		user.setRoles(new BoilerplateList<Role>());
		user.getRoles().add(roleAssigner);
		this.set("USER:"+user.getUserId(), user);
		
	}
	/**
	 * This method create seed test data configuration.
	 * @return configuration map
	 */
	private BoilerplateMap<String,String> createSeedTestData(){
		// This attribute tells us about server name
		
		BoilerplateMap<String,String> vAllETest = new BoilerplateMap<String,String>();
		vAllETest.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllETest.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllETest.put("SMS_SENDER", "CMDSMS");
		vAllETest.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");

		return vAllETest;		
	}
	/**
	 * This method create seed development data configuration.
	 * @return configuration map
	 */
	private BoilerplateMap<String,String>  createSeedDevelopmentData(){
		// This attribute tells us about server name

		BoilerplateMap<String,String> vAllEDev = new BoilerplateMap<String,String>();
		//put all configuration
		vAllEDev.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllEDev.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllEDev.put("SMS_SENDER", "CMDSMS");
		vAllEDev.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");
		vAllEDev.put("RootFileUploadLocation", "/downloads/");
		
		return vAllEDev;
	
		}
	/**
	 * This method create seed prod data configuration.
	 * @return configuration map
	 */
	private BoilerplateMap<String,String> createSeedProductionData(){
		// This attribute tells us about server name

		BoilerplateMap<String,String> vAllEProduction = new BoilerplateMap<String,String>();
		
		vAllEProduction.put("SMS_ROOT_URL", "http://alerts.solutionsinfini.com/api/v3/index.php");
		vAllEProduction.put("SMS_API_KEY", "A0f52a89f0ab2bf7d755ab9dada057eab");
		vAllEProduction.put("SMS_SENDER", "CMDSMS");
		vAllEProduction.put("SMS_URL", "?method=sms&api_key=@apiKey&to=@to&sender=@sender&message=@message");

		return vAllEProduction;
	}
	/**
	 * This method create seed common data configuration.
	 * @return configuration map
	 */
	private BoilerplateMap<String,String>  createSeedCommonData(){
		BoilerplateMap<String,String> vAllEAll = new BoilerplateMap<String,String>();
		//put all configuration
		vAllEAll.put("V_All_All", "V_All_All");
		vAllEAll.put("V_All_Dev","V_All_Dev");
		vAllEAll.put("DefaultAuthenticationProvider", "DEFAULT");
		vAllEAll.put("DSAAuthenticationProvider", "DSA");
		vAllEAll.put("SessionTimeOutInMinutes", "43200");
		vAllEAll.put("CMD_Organization_Id", "CMD001");
		vAllEAll.put("OrganizationTimeOutInSeconds", "2147483647");
	
		vAllEAll.put("CrossDomainHeaders", "Access-Control-Allow-Headers:*;Access-Control-Allow-Methods:*;Access-Control-Allow-Origin:*;Access-Control-Allow-Credentials: true;Access-Control-Expose-Headers:*");
		vAllEAll.put("DefaultUserStatus", "1");
		vAllEAll.put("Offer_Initial_Month_Size", "1");
		// Admin credentials
		vAllEAll.put("AdminId", "admin");
		vAllEAll.put("AdminPassword", "password");
		// Owner Allocation QUEUE NAme

		return vAllEAll;
	
		}
	
	
	/**
	 * This method creates seed content
	 */
	private void createSeedContent(){
		//Storing seed content
		BoilerplateMap<String,String> contentMap = new BoilerplateMap<>();
		contentMap.put("WELCOME_MESSAGE_EMAIL_SUBJECT" , "Welcome @FirstName");
		this.set("CONTENT:CMD001:VERSION_ALL:LOCALE_ALL", Base.toXML(contentMap));
	}
	
	
	
	/**
	 * Sets a key field value
	 * @param key The key
	 * @param field The field
	 * @param value The value
	 */
	public void hset(String key, String field, String value){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.hset(key, field, value);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * Gets a key field value
	 * @param key The key
	 * @param field The field
	 * @return
	 */
	public String hget(String key, String field){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.hget(key, field);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * Sets the key hash
	 * @param key The key
	 * @param hash The hash
	 */
	public void hmset(String key, Map<String, String> hash){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.hmset(key, hash);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * Gets a key all fields value
	 * @param key The key
	 * @return
	 */
	public Map<String, String> hgetAll(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.hgetAll(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * Dels the key
	 * @param key The key
	 */
	public void del(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.del(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * Getting the key/keys satisfying pattern
	 * @param pattern The pattern
	 * @return The set of keys
	 */
	public Set<String> keys(String pattern){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.keys(pattern);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method adds the members to set access by defined key
	 * @param key The set key
	 * @param members The members
	 */
	public void sadd(String key, String members){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			jedis.sadd(key, members);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method gets the set members
	 * @param key The set key
	 * @return The set of members
	 */
	public Set<String> smembers(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.smembers(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method tells whether key exists or not in our Redis
	 * @param key The key
	 * @return true/false
	 */
	public boolean exists(String key){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.exists(key);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}
	
	/**
	 * This method delete the field from map.
	 * @param key The key
	 * @param field The field name
	 * @return true/false
	 */
	public boolean delMapfield(String key,String field){
		Jedis jedis = null;
		try{
			jedis = this.getConnection();
			return jedis.hdel(key,field) != null;
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
	}


}
