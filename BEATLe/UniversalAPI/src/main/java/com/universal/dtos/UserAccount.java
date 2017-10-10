package com.universal.dtos;

import java.util.HashMap;

public class UserAccount {
	private String username;
	private String password;
	private String role;
	private String access_token=null;
	private String environment;
	private String resource;
	private String domainName;
	//To identify the user is internal or external user
	private String userType = "INTERNAL";
	
	//For salesforce oauth host and api host
	private String apiHost;
	
	private HashMap<String, String> instanceParams;
	

	

	public UserAccount(String access_token){
		this.access_token=access_token;
	}

	public UserAccount(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role	  = role;
	}
	
	public UserAccount(String username, String password, String role, HashMap<String, String> instanceParams) {
		this.username = username;
		this.password = password;
		this.role	  = role;
		this.apiHost  = instanceParams.get("apiHost");
		this.instanceParams = instanceParams;
	}
	
	public UserAccount(String username, String password, String role, String access_token) {
		this.username = username;
		this.password = password;
		this.role	  = role;
		this.access_token=access_token;
	}
	
	public UserAccount(String username, String password, String role, String environment, String resource) {
		this(username, password, role);
		this.environment = environment;
		this.resource = resource;
	}

	public UserAccount(String username, String password, String role, String environment, String resource, String domainName) {
		this(username, password, role, environment, resource);
		this.setDomainName(domainName);
	}	
	
	public UserAccount(String username, String password, String role, int userType) {
		this(username, password, role);
		this.setUserType("INTERNAL");
		if(userType != 1) {
			this.setUserType("EXTERNAL");
		} 
	}
	
	
	/**
	 * @return the instanceParams
	 */
	public HashMap<String, String> getInstanceParams() {
		return instanceParams;
	}

	/**
	 * @param instanceParams the instanceParams to set
	 */
	public void setInstanceParams(HashMap<String, String> instanceParams) {
		this.instanceParams = instanceParams;
	}
	
	/**
	 * @return the apiHost
	 */
	public String getApiHost() {
		return apiHost;
	}

	/**
	 * @param apiHost the apiHost to set
	 */
	public void setApiHost(String apiHost) {
		this.apiHost = apiHost;
	}
	
	public String getAccess_token() {
		return access_token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
