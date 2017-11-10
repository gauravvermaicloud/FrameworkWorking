package com.boilerplate.framework;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.Base;
import com.boilerplate.java.Constants;
import com.boilerplate.sessions.Session;

public class Logger {

	
	/**
	 * This is the log4j logger
	 */
	org.apache.log4j.Logger logger = null;

	/**
	 * This is the message format to be used for normal non exception messages.
	 * The 3 ~ in the end have been put so that there are an equal number of ~
	 * when the logs are parsed. The messages are separated by ~ because they
	 * are not used frequently. The message structure will be class name, method
	 * name, identifier which is used to uniquely identify an area of code in a
	 * method followed by message
	 */
	private static final String MESSAGE_FORMAT = "%s~%s ~ %s ~ %s ~ %s ~~~~";

	/**
	 * This is the message format to be used for exception messages. The
	 * messages are separated by ~ because they are not used frequently. The
	 * message structure will be class name, method name, identifier which is
	 * used to uniquely identify an area of code in a method followed by message
	 */
	private static final String ERROR_FORMAT = "%s~%s ~ %s ~ %s ~ %s ~%s~%s";

	private static final String ERROR_FORMAT_WITH_SESSION = "%s~%s ~ %s ~ %s ~ %s ~%s~%s ~%s";
	/**
	 * This the method exit format
	 */
	private static final String TRACE_EXIT_FORMAT = "%s : %s : %s";
	
	private static boolean isDebugEnabled = Logger.isDebugEnabledInternal();
	/**
	 * This is a private instance of the logger.
	 */
	private Logger() {

	}

	/**
	 * This method returns the value from property file if debug is enabled.
	 * As logger is not initialized by the spring framework hence the config value is seperately picked up.
	 * @return
	 */
	private static boolean isDebugEnabledInternal(){
		Properties properties  = null;
		InputStream inputStream =null;
		boolean isDebugEnabled = false;
		try{
			properties = new Properties();
			//Using the .properties file in the class  path load the file
			//into the properties class
			inputStream = 
					Logger.class.getClassLoader().getResourceAsStream("boilerplate.properties");
			properties.load(inputStream);
			//for each key that exists in the properties file put it into
			//the configuration map
			isDebugEnabled = Boolean.parseBoolean(properties.getProperty(Constants.IsDebugEnabled));
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
		return isDebugEnabled;
	}
	
	/**
	 * This method gets an instance of the logger.
	 * @param clazz The class using the logger
	 * @return An instance of the logger
	 */
	public static com.boilerplate.framework.Logger getInstance(Class clazz) {
		Logger thisLogger = new Logger();
		thisLogger.logger = org.apache.log4j.Logger.getLogger(clazz);
		return thisLogger;
	}

	/**
	 * Checks the configuration and tells if debug level logging is enabled
	 * @return True if debug level logging is enabled
	 */
	public boolean isDebugEnabled(){
		return Logger.isDebugEnabled;
	}
	
	/**
	 * This is used to log a debug message. It is recommended that a check be made to logger's
	 * isDebugEnabled method before this method is called to avoid cost of creation of strings.
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 */
	public void logDebug(String className, String methodName,
			String identifier, String message) {
		this.logger.debug(String.format(RequestThreadLocal.getRequestId(),
				Logger.MESSAGE_FORMAT,className, methodName, identifier, message));

	}
	/**
	 * This is used to log a info message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 */
	public void logInfo(String className, String methodName, String identifier,
			String message) {
		this.logger.info(String.format(Logger.MESSAGE_FORMAT,
				RequestThreadLocal.getRequestId(),className, 
				methodName, identifier, message));
	}


	/**
	 * This is used to log a warn message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 */
	public void logWarning(String className, String methodName,
			String identifier, String message) {
		this.logger.warn(String.format(Logger.MESSAGE_FORMAT,
				RequestThreadLocal.getRequestId(),
				className, methodName, identifier, message));
	}


	/**
	 * This is used to log a fatel message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 */
	public void logFatel(String className, String methodName,
			String identifier, String message) {
		this.logger.fatal(String.format(Logger.MESSAGE_FORMAT,
				RequestThreadLocal.getRequestId(),className, methodName
				, identifier, message));
	}

	/**
	 * This method converts a throable's stack trace into a string seperated by ::
	 * @param throwable The class
	 * @return A string with stack trace as a string message
	 */
	private String getStackTraceString(Throwable throwable) {
		//Create a string builder, which will have better perfomrance than string
		StringBuilder stackTraceString = new StringBuilder();
		//get the array of stack trace elements
		StackTraceElement[] stackTraceElements = throwable.getStackTrace();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			//for each element convert the message to string and seerate it with ::
			stackTraceString.append(stackTraceElement.toString() + "::");
		}
		//return the constructre string
		return stackTraceString.toString();
	}


	/**
	 * This is used to log a fatel message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 * @param throwable This is the exception which caused the fatel error.
	 */
	public void logFatel(String className, String methodName,
			String identifier, String message, Throwable throwable) {
		this.logger.fatal(String.format(Logger.ERROR_FORMAT,
				RequestThreadLocal.getRequestId(), className, methodName,
				identifier,message, throwable.getClass().toString(),
				throwable.getMessage()));
	}

	/**
	 * This is used to log a error message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 */
	public void logError(String className, String methodName,
			String identifier, String message) {
		this.logger.error( String.format(Logger.MESSAGE_FORMAT,
				RequestThreadLocal.getRequestId(),className
				, methodName, identifier, message));
	}

	/**
	 * This is used to log a error/exception message. 
	 * The log is made in a ~ separated format for loading into the database
	 * @param className This is the name of the class, although this may be inferred from 
	 * the logger, it is being explicitly requested for some cases required AOP
	 * @param methodName This is the name of the method making the log. This may be inferred 
	 * however foe improved performance this is being explicitly requested. 
	 * @param identifier This is the area inside the method being logged for example a loop 
	 * or an if condition.
	 * @param message This is the message to be logged.
	 * @param throwable This is the exception which caused the error.
	 */
	public void logException(String className, String methodName,
			String identifier, String message, Throwable throwable) {

		this.logger.error(String.format(Logger.ERROR_FORMAT
				, RequestThreadLocal.getRequestId(),className, methodName,
				identifier,message ,throwable.getClass().toString(),
				throwable.getMessage()));
	}
	/**
	 * This method is used to log a method when exiting. If the configuration 
	 * value of LogArgs is enabled then method arguments are logged. 
	 * If the LogReturnValue is enabled then return value is logged.
	 * @param className This is the name of the class.
	 * @param methodName This is the name of the method being called
	 * @param args This is the arguments
	 * @param returnValue This is the return value
	 * @param The session details of the user who executed the api
	 */
	public void logTraceExit(String className,String methodName
			,Object[] args,Object returnValue,Session session){
		
		this.logInfo(className, methodName, "TraceExit", 
				String.format(Logger.TRACE_EXIT_FORMAT, Base.toJSON(args)
						,Base.toJSON(returnValue), session == null?"":Base.toJSON(session))
				);
	}
	
	/**
	 * This method is used to log a method when an exception takes place. 
	 * @param className The name of class
	 * @param methodName The name of the method
	 * @param args The arguments when exception took place
	 * @param throwable The instance of throwable
	 * @param session The user session if any
	 */
	
	//TODO check here for logging
	public void logTraceExitException(String className,String methodName
			,Object[] args, Throwable throwable,Session session){
		this.logger.error(String.format(Logger.ERROR_FORMAT_WITH_SESSION
				, RequestThreadLocal.getRequestId(),className, methodName,
				"TraceException", Base.toJSON(args) ,throwable.getClass().toString(),
				throwable.getMessage(),
				session == null? "":
				Base.toJSON(session)));
	}

}
