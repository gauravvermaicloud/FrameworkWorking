package com.boilerplate.framework.web;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import scala.collection.immutable.Stream.Cons;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.sessions.Session;
import com.boilerplate.sessions.SessionManager;

/**
 * This class adds unique id to every http request. This id may be used for
 * debugging and client escalation / logging process. 
 * The said id is sent back to users on the header X-Http-Request-Id
 * The class also sets things like session etc on the thread
 */
@Component
public class HttpRequestIdInterceptor extends HandlerInterceptorAdapter{

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * sets the instance of configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;
	
	/**
	 * Sets the instance of session managaer
	 * @param sessionManager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	/**
	 * The user service
	 */
	@Autowired
	com.boilerplate.service.interfaces.IUserService userService;
	
	/**
	 * Sets the user service
	 * @param userService The user service
	 */
	public void setUserService(com.boilerplate.service.interfaces.IUserService userService){
		this.userService = userService;
	}
	
	/**
	 * This method sets a http request id for the request.
	 */

	@Override
	 public boolean preHandle(HttpServletRequest request,
		      HttpServletResponse response, Object handler) throws Exception {
		 	//Generate a random request id,
			//this id is tied to each http request and is available on the thread
			String requestId = UUID.randomUUID().toString();
		 	response.setHeader(Constants.X_Http_Request_Id, requestId);
		 	Session session =null;
		 
		 	//if the request for authentication then session doesnt make sense so we will not check for
		 	//session and also if there is a session detail coming with the request
		 	//we will ignore it
		 	if(this.isNonSessionURL(request) ==false){
		 		String sessionId = null;
		 		
		 		sessionId = getSessionId(request);
			 	
			 	//if the session id is not null get the session from the cache/database
			 	session = sessionManager.getSession(sessionId);
			 	//put the session id and the associated user id on header
			 	if(session !=null){
			 		setSessionParametersInHttpResponse(response,session);
			 	}
			}
		 	RequestThreadLocal.setRequest(requestId, request, response,session);
		 	return true;
	 }
	
	/**
	 * This method sets the paramteres of session in the response, including Header and cookie
	 * @param reponse The http response
	 * @param session the session
	 */
	private void setSessionParametersInHttpResponse(HttpServletResponse reponse,Session session){
		reponse.addHeader(Constants.AuthTokenHeaderKey, session.getSessionId());
		reponse.addHeader(Constants.X_User_Id,session.getExternalFacingUser().getUserId());
	 	//reset cookie so that it can live for additional time
	 	Cookie cookie = new Cookie(Constants.AuthTokenCookieKey,session.getSessionId());
	 	cookie.setMaxAge(sessionManager.getSessionTimeout());
	 	reponse.addCookie(cookie);
	}
	
	/**
	 * This method returns true if the request is an authentication request
	 * and false if it is not
	 * @param request The Http request
	 * @return true for an authentication request and false for none
	 */
	private boolean isNonSessionURL(HttpServletRequest request){
		return request.getRequestURI().contains("/authenticate") ||
				request.getRequestURI().contains("/startExpressSession") ;
	}
	
	
	/**
	 * This method returns true if the request is an authentication request
	 * and false if it is not
	 * @param request The Http request
	 * @return true for an authentication request and false for none
	 */
	private boolean isLogoutURL(HttpServletRequest request){
		return request.getRequestURI().contains("/logout");
	}

	/**
	 * This method is called just before the response is sent back to client.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response
		
			, Object handler, Exception ex)
			throws Exception {
		//during the execution of thread
		//the session was on the thread so we know
		//it did not get lost however it may have been evicted from cache
		//so we need to put it back on cache and DB
		if(RequestThreadLocal.getSession()!=null){
			//if the ur; was not logout
			if(this.isLogoutURL(request) == false){
				sessionManager.saveSessionOnExit(RequestThreadLocal.getSession());
			}
		}
		RequestThreadLocal.remove();
	}
	
	/**
	 * This method finds the session token if it exists in
	 * cookie, header or query string.
	 * @param request This is the http request
	 * @return The session token id
	 * @throws UnauthorizedException 
	 * @throws BadRequestException 
	 * @throws NotFoundException 
	 */
	private String getSessionId(HttpServletRequest request) throws UnauthorizedException, NotFoundException, BadRequestException{
		
		//firstly we check the case of impersination
		if(request.getHeader(Constants.X_Impersinator)!= null){
			String impersonator = request.getHeader(Constants.X_Impersinator);
			String impersinatorPassword = request.getHeader(Constants.X_Impersinator_Password);
			String impersonatedUser =  request.getHeader(Constants.X_User_LoginId_To_Be_Impersonated);
			
			//check if the ipersinator is a correct user
			AuthenticationRequest authenitcationRequest = new AuthenticationRequest();
			authenitcationRequest.setUserId(impersonator);
			authenitcationRequest.setPassword(impersinatorPassword);
			Session session = this.userService.authenticate(authenitcationRequest);
			boolean isImpersinatorOrAdminFound = false;
			for(Role role : session.getExternalFacingUser().getRoles()){
				if(role.getRoleName().toUpperCase().equals("ADMIN")){
					isImpersinatorOrAdminFound = true;
					break;
				}
				
				if(role.getRoleName().toUpperCase().equals("IMPERSINATOR")){
					isImpersinatorOrAdminFound = true;
					break;
				}
			}
			if(isImpersinatorOrAdminFound == false){
				throw new UnauthorizedException("User", "User not allowed impersination", null);
			}
			//if so get the user being impesontaed
			ExternalFacingReturnedUser user = this.userService.get(impersonatedUser);
			//create a session
			Session sessionCreated = this.sessionManager.createNewSession(user);
			return sessionCreated.getSessionId();
		}
		//check the session Id in cookie
		String value = "";
		if(request.getCookies() !=null){
			for(Cookie cookie : request.getCookies()){
				if(cookie.getName().equals(Constants.AuthTokenCookieKey)){
					value =  cookie.getValue();
				}
			}
		}
		if(value.equals("") ==  false) return value;
		//check the session Id in Header
		if(request.getHeader(Constants.AuthTokenHeaderKey)!= null){
			return request.getHeader(Constants.AuthTokenHeaderKey);
		}
		//check the session Id in query string
		if(request.getQueryString() !=null){
			String queryStringValues[] = request.getQueryString().toUpperCase().split("&");
			for(String queryString : queryStringValues){
				if(queryString.startsWith(Constants.AuthTokenQueryStringKey.toUpperCase())){
					return queryString.split("=")[1];
				}
			}
		}
		
		return null;
	}
	
	
}
