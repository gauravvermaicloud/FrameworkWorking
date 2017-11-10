package com.boilerplate.java.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.boilerplate.exceptions.ClientFacingExceptionMessage;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.EntityExistsException;
import com.boilerplate.exceptions.rest.InvalidStateException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.sessions.Session;

/**
 * This is the base controller of the system. All 
 * controllers are expected to derive from this class.
 * This class provides expected abstract methods for CRUD 
 * operations.
 * @author gaurav
 */
@Controller
public abstract class BaseController extends Base {
		
	/**
	 * This method is used to return a Notfound message when a 
	 * NotFoundException is thrown
	 * @param notFoundException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(value=NotFoundException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			NotFoundException notFoundException){
		return notFoundException.getClientFacingExceptionMessage();
	}
	
	/**
	 * This method is used to return a BadRequestException 
	 * message when a BadRequestException is thrown
	 * @param badRequestException The instance of Not found exception
	 * @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value=BadRequestException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			BadRequestException badRequestException){
		return badRequestException.getClientFacingExceptionMessage();
	}
	
	/**
	 * This method is used to return a ConflictException 
	 * message when a ConflictException is thrown
	 * @param conflictException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.CONFLICT)
	@ExceptionHandler(value=ConflictException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			ConflictException conflictException){
		return conflictException.getClientFacingExceptionMessage();
	}

	/**
	 * This method is used to return a EntityExistsException 
	 * message when a EntityExistsException is thrown
	 * @param entityExistsException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.CONFLICT)
	@ExceptionHandler(value=EntityExistsException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			EntityExistsException entityExistsException){
		return entityExistsException.getClientFacingExceptionMessage();
	}
	
	/**
	 * This method is used to return a PreconditionFailedException 
	 * message when a PreconditionFailedException is thrown
	 * @param preconditionFailedException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler(value=PreconditionFailedException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			PreconditionFailedException preconditionFailedException){
		return preconditionFailedException.getClientFacingExceptionMessage();
	}	
	
	/**
	 * This method is used to return a UnauthorizedException 
	 * message when a UnauthorizedException is thrown
	 * @param unauthorizedException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(value=UnauthorizedException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			UnauthorizedException unauthorizedException){
		return unauthorizedException.getClientFacingExceptionMessage();
	}

	/**
	 * This method is used to return a UnauthorizedException
	 * message when a UnauthorizedException is thrown
	 * @param invalidStateException The instance of Not found exception
	 * @return The response with error details
	 */
	@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(value=InvalidStateException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			InvalidStateException invalidStateException){
		return invalidStateException.getClientFacingExceptionMessage();
	}
	
	/**
	 * This method is used to return a UpdateFailedException 
	 * message when a UpdateFailedException is thrown
	 * @param updateFailedException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.CONFLICT)
	@ExceptionHandler(value=UpdateFailedException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			UpdateFailedException updateFailedException){
		return updateFailedException.getClientFacingExceptionMessage();
	}
	
	/**
	 * This method is used to return a ValidationFailedException 
	 * message when a ValidationFailedException is thrown
	 * @param validationFailedException The instance of Not found exception
	 *  @return The Response with error details
	 */
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value=ValidationFailedException.class)
	protected @ResponseBody ClientFacingExceptionMessage handleException(
			ValidationFailedException validationFailedException){
		return validationFailedException.getClientFacingExceptionMessage();
	}	

	/**
	 * This method returns an instance of the session
	 * @return This returns an instance of the session.
	 */
	public Session getSession() {
		return (Session)RequestThreadLocal.threadLocal.get().getSession();
	}

	/**
	 * This method returns an instance of the http servelet for the request.
	 * @return This is the instance of Http Servlet
	 */
	public HttpServletRequest getHttpServletRequest() {
		return RequestThreadLocal.threadLocal.get().getHttpServletRequest();
	}

	/**
	 * This method returns an instance of the request id
	 * @return The instance of the request id.
	 */
	public String getRequestId() {
		return RequestThreadLocal.threadLocal.get().getRequestId();
	}
	
	/**
	 * This method adds a cookie to response
	 * @param key The key for the cookie
	 * @param value The value for the cookie
	 */
	public void addCookie(String key, String value,int maxAge){	
		Cookie cookie=new Cookie (key, value);
		cookie.setMaxAge(maxAge);
		RequestThreadLocal.threadLocal.get().getHttpServletResponse().addCookie(cookie);
	}
	
	/**
	 * This method adds a header to response
	 * @param key The key to the header
	 * @param value The value for the header
	 */
	public void addHeader(String key, String value){
		RequestThreadLocal.threadLocal.get().getHttpServletResponse().setHeader(key, value);
	}

	/**
	 * This method returns the Http Servlet response
	 * @return The http servlet response
	 */
	public HttpServletResponse getHttpServletResponse(){
		return RequestThreadLocal.threadLocal.get().getHttpServletResponse();
	}
	
}
