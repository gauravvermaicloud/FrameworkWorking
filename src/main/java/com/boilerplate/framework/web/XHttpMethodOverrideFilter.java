package com.boilerplate.framework.web;

import java.io.IOException;
import java.util.Locale;
 
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This class is used to create an X-Http-Method-Override filter.
 * Using this configuration a POST method may be overloaded to perform PUT or
 * DELETE operations by adding a header X-HTTP-Method-Override : PUT or DELETE.
 * @author gaurav
 *
 */
public class XHttpMethodOverrideFilter extends OncePerRequestFilter {
	/**
	 * @see OncePerRequestFilter.doFildterInternal
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request
			, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		//get the hdader value for X-Http-Method-Override
		String headerValue = request.getHeader("X-HTTP-Method-Override");
		//if the method is POST and the header has a value
		if ("POST".equals(request.getMethod()) && StringUtils.hasLength(headerValue)) 	{
			//this means we have overriden the header and hence
			//create a new http request and change its method
			HttpServletRequest wrapper = new HttpMethodRequestWrapper(request
					, headerValue.toUpperCase(Locale.ENGLISH));
			//and continue the filter with new requeest
			filterChain.doFilter(wrapper, response);
		}
		else {
			//on the other hand if there is no need to override
			//let us move as is.
			filterChain.doFilter(request, response);
		}
	}
	
	/**
	 * This internal class is used to make Http requests 
	 * @author gaurav
	 *
	 */
	private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
	 
		/**
		 * This is the method on http it can be GET, PUT, POST etc
		 */
		private final String method;
	 
		/**
		 * Constructor which takes an existing http request and creates a new one
		 * with changed method
		 * @param request The original request
		 * @param method The new method applied to the request
		 */
		public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
			//as we will use most of the features of original request mechanism
			//we called parent class default constructor
			super(request);
			//we then se the method
			this.method = method;
		}
	 
		/**
		 * @see HttpServletRequestWrapper.getMethod
		 */
		@Override
		public String getMethod() {
			//return the new method for the request.
			return this.method;
		}
	}//end internal class
	 
}