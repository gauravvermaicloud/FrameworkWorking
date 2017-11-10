package com.boilerplate.framework;

import java.net.HttpCookie;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

public class HttpResponse{
	String url;
	BoilerplateMap<String,BoilerplateList<String>> requestHeaders;
	BoilerplateList<HttpCookie> requestCookies;
	String requestBody;
	java.util.Map<String,java.util.List<String>> responseHeaders;
	java.util.List<HttpCookie> responseCookies;
	String responseBody;
	String method;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public BoilerplateMap<String, BoilerplateList<String>> getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(
			BoilerplateMap<String, BoilerplateList<String>> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	public BoilerplateList<HttpCookie> getRequestCookies() {
		return requestCookies;
	}
	public void setRequestCookies(BoilerplateList<HttpCookie> requestCookies) {
		this.requestCookies = requestCookies;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public java.util.Map<String, java.util.List<String>> getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(
			java.util.Map<String, java.util.List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	public java.util.List<HttpCookie> getResponseCookies() {
		return responseCookies;
	}
	public void setResponseCookies(java.util.List<HttpCookie> responseCookies) {
		this.responseCookies = responseCookies;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	
	public int httpStatus;
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
