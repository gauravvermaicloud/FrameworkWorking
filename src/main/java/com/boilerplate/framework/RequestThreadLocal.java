package com.boilerplate.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is used to set and unset a request id on thread
 * @author gaurav
 *
 */
public class RequestThreadLocal {
	
	/**
	 * This is the instance of thread local
	 */
	public static final ThreadLocal<RequestParameters> threadLocal
		= new ThreadLocal<RequestParameters>();
	
	/**
	 * This sets the request id on thread
	 * @param requestId The request id
	 * @param httpServletRequest The http request
	 * @param httpServletResponse The http response
	 * @param session The user session if available
	 */
	public static void setRequest(String requestId,HttpServletRequest httpServletRequest
			, HttpServletResponse httpServletResponse, com.boilerplate.sessions.Session session){
		RequestParameters requestParameters = new RequestParameters();
		requestParameters.setRequestId(requestId);
		requestParameters.setHttpServletRequest(httpServletRequest);
		requestParameters.setHttpResponse(httpServletResponse);
		requestParameters.setSession(session);
		RequestThreadLocal.threadLocal.set(requestParameters);
		
	}
	
	/**
	 * This gets the request id on thread
	 * @return The request id
	 */
	public static String getRequestId(){
		if(RequestThreadLocal.threadLocal.get() != null){ 
			return RequestThreadLocal.threadLocal.get().getRequestId();
		}
		else{
			return null;
		}
	}
	
	/**
	 * This gets the Session from thread
	 * @return The session
	 */
	public static com.boilerplate.sessions.Session getSession(){
		if(RequestThreadLocal.threadLocal.get() != null){ 
			return RequestThreadLocal.threadLocal.get().getSession();
		}
		else{
			return null;
		}
	}
	
	/**
	 * This method returns the Http request 
	 * @return The Http request
	 */
	public static HttpServletRequest getHttpRequest(){
		if(RequestThreadLocal.threadLocal.get() != null){
			return RequestThreadLocal.threadLocal.get().getHttpServletRequest();
		}
		else{
			return null;
		}
	}
	
	/**
	 * This method sends back the http response
	 * @return
	 */
	public static HttpServletResponse getHttpResponse(){
		if(RequestThreadLocal.threadLocal.get() != null){
			return RequestThreadLocal.threadLocal.get().getHttpServletResponse();
		}
		else{
			return null;
		}
	}
	
	/**
	 * This removes the request id from thread.
	 */
	public static void remove(){
		if(RequestThreadLocal.threadLocal != null){
			RequestThreadLocal.threadLocal.remove();
		}
	}
	
	/**
	 * This method sets a custom attribute on the thread
	 * @param key This is the key
	 * @return The object for the key
	 */
	public static Object getAttribute(String key){
		if(RequestThreadLocal.threadLocal != null){
			return RequestThreadLocal.threadLocal.get().getAttribute(key);
		}
		else{
			return null;
		}
	}
	
	/**
	 * This method sets a custom attribute on the thread
	 * @param key The key
	 * @param object The object
	 */
	public static void setAttribute(String key, Object object){
		if(RequestThreadLocal.threadLocal != null){
			RequestThreadLocal.threadLocal.get().setAttribute(key, object);
		}
	}
	
	/**
	 * This method sets a thread to sleep for particular amount of time
	 * @param time Time for the thread needs to sleep
	 */
	public static void sleepThread(Integer time) throws Exception{
		if(RequestThreadLocal.threadLocal != null)
		{
			Thread.sleep(time); //1000 milliseconds is one second.
		}
	}
}
