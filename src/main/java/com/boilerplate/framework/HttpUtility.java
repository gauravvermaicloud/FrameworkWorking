package com.boilerplate.framework;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import com.boilerplate.framework.Logger;

import javax.servlet.http.Cookie;

import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class is used to send http messages
 * @author gaurav.verma.icloud
 *
 */
public  class HttpUtility {


	/**
	 * This method makes a HTTP call
	 * @param url The URL to be called
	 * @param requestHeaders The headers to be sent
	 * @param requestCookies The cookies to be sent
	 * @param requestBody The request body
	 * @param responseHeaders The response headers
	 * @param responseCookies The response cookie's
	 * @param responseBody The response body
	 * @param method The http method
	 * @return The http status
	 * @throws IOException If there is an error in creating the http request.
	 */
	public static HttpResponse makeHttpRequest(
			String url,
			BoilerplateMap<String,BoilerplateList<String>> requestHeaders,
			BoilerplateList<HttpCookie> requestCookies,
			String requestBody,
			String method
			) throws IOException{
		String cookieString="";
		java.util.Map<String,java.util.List<String>> responseHeaders;
		java.util.List<HttpCookie> responseCookies = null;
		String responseBody;
		Logger logger = Logger.getInstance(HttpUtility.class);
		
		if(requestCookies !=null){
			for(Object object:requestCookies){
				java.net.HttpCookie cookie = (java.net.HttpCookie)object;
				cookieString+=cookie.getName()+"="+cookie.getValue()+";";
			}
		}
		
		URL uRL = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) uRL.openConnection();
		httpURLConnection.setRequestMethod(method);
		try{
			httpURLConnection.setConnectTimeout(120000);
		}
		catch (Exception exception){
			logger.logError("HttpUtility", "makeHttpRequest", "Catch Exception Block - setConnectTimeout ", "URL :"+ url + "& Request Header:"+ Base.toJSON(requestHeaders) + "& Request Cookies:"+ Base.toJSON(requestCookies) + "& Request Body:" + requestBody + "& Method:" + method + "& Exception:" + exception);
		}
		if(cookieString.equals("") == false){
			httpURLConnection.addRequestProperty("Cookie", cookieString);
		}
		
		if(requestHeaders !=null){
			for(String key: requestHeaders.keySet()){
				for(Object value:requestHeaders.get(key) ){
					httpURLConnection.addRequestProperty(key, (String)value);
				}
			}
		}
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;
		StringBuffer responseBuffer = null;
		if(!method.equals("GET") &&  requestBody !=null){
			try{
				 outputStream = httpURLConnection.getOutputStream();
				 bufferedWriter = new BufferedWriter(
				        new OutputStreamWriter(outputStream, "UTF-8"));
				bufferedWriter.write(requestBody);
				bufferedWriter.flush();
				bufferedWriter.close();
				outputStream.close();
			}
			catch(Exception ex){
				// Log Error
				logger.logError("HttpUtility", "makeHttpRequest", "Catch Exception Block 1", "URL :"+ url + "& Request Header:"+ Base.toJSON(requestHeaders) + "& Request Cookies:"+ Base.toJSON(requestCookies) + "& Request Body:" + requestBody + "& Method:" + method + "& Exception:" + ex);
				throw ex;
			}
			finally{
				if(bufferedWriter !=null)bufferedWriter.close();
				if(outputStream!=null)outputStream.close();
			}
		}
		
		int responseCode = httpURLConnection.getResponseCode();
		
		BufferedReader bufferdReader = null;
		if(responseCode == 200){
		 bufferdReader = new BufferedReader(
		        new InputStreamReader(httpURLConnection.getInputStream()));
		}
		else
		{
			 bufferdReader = new BufferedReader(
				        new InputStreamReader(httpURLConnection.getErrorStream()));
		}		
		String inputLine;
		try{
			responseBuffer = new StringBuffer();
			while ((inputLine = bufferdReader.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
		responseBody = responseBuffer.toString();
		responseHeaders =httpURLConnection.getHeaderFields();
		String cookieHeader = "";
		if((responseHeaders.get("Set-Cookie") != null)){
			if(responseHeaders.get("Set-Cookie").size() >0){
				cookieHeader = responseHeaders.get("Set-Cookie").get(0);
			}
			responseCookies = HttpCookie.parse(cookieHeader);
			}
		}
		catch(Exception ex){
			// Log Error
			logger.logError("HttpUtility", "makeHttpRequest", "Catch Exception Block 2", "URL :"+ url + "& Request Header:"+ Base.toJSON(requestHeaders) + "& Request Cookies:"+ Base.toJSON(requestCookies) + "& Request Body:" + requestBody + "& Method:" + method + "& Exception:" + ex);
			throw ex;
		}
		finally{
			bufferdReader.close();
			responseBuffer.setLength(0);
			httpURLConnection.disconnect();
		}
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setMethod(method);
		httpResponse.setRequestBody(requestBody);
		httpResponse.setRequestCookies(requestCookies);
		httpResponse.setRequestHeaders(requestHeaders);
		httpResponse.setResponseBody(responseBody);
		httpResponse.setResponseCookies(responseCookies);
		httpResponse.setResponseHeaders(responseHeaders);
		httpResponse.setUrl(url);
		httpResponse.setHttpStatus(responseCode);
		
		if (logger.isDebugEnabled()){
		    logger.logDebug("HttpUtility", "makeHttpRequest", "httpResponse", httpResponse.toString());

		}
		return httpResponse;
	}
	
    
}