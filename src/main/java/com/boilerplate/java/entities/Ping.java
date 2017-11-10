package com.boilerplate.java.entities;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.boilerplate.java.collections.BoilerplateMap;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is a response to the ping to the server.
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value="Ping", description="A response to the ping request", parent=BaseEntity.class)
public class Ping implements Serializable{
	
	/**
	 * The name of the mahchine
	 */
	@ApiModelProperty(value="This is the name of the server responding to ping requests")
	private String machineName;
	
	/**
	 * This is the overall status of the response
	 */
	@ApiModelProperty(value="This is the oerall response of status")
	private boolean overallStatus = true;
	
	/**
	 * This method returns the machine name
	 * @return The machine name
	 */
	public String getMachineName(){
		return this.machineName;	
	}
	
	/**
	 * This method returns the overall status
	 * @return The overall status
	 */
	public boolean getOverallStatus(){
		return this.overallStatus;
	}
	
	/**
	 * This method returns the status map, which is details of 
	 * individual status of each service
	 * @return The individual status
	 */
	public BoilerplateMap<String, String> getStatusMap(){
		return this.statusMap;
	}
	
	/**
	 * The status of each and every indivdiual service being used by ping
	 */
	@ApiModelProperty(value="The status of each and every indivdiual service being used by ping")
	private BoilerplateMap<String, String> statusMap = new BoilerplateMap<String,String>();
	
	/**
	 * Default constructor, it adds the name of the host
	 * @throws UnknownHostException This is ideally never thrown
	 */
	public Ping() throws UnknownHostException{
		this.machineName = InetAddress.getLocalHost().getHostName();
	}
	
	/**
	 * This method adds a status of a given service
	 * @param service The name of the service
	 * @param status The status of the servcice
	 * @param dontFailOverAllstatus If this is false overall ping request will be marked failed
	 */
	public void addStatus(String service, String status,boolean dontFailOverAllstatus){
		statusMap.put(service, status);
		this.overallStatus =dontFailOverAllstatus;
	}
}
