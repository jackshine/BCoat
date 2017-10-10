/**
 * 
 */
package com.elastica.action.infra;

import java.util.Map;

//import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;

/**
 * @author anuvrath
 *
 */
public class TestSuiteDTO {
	private String username;
	private String password;
	private String loginURL;
	private Map<String, String> APIMap;
	private String environmentName;
	private String tenantName;
	private String tenantDomainName;
	private String tenantToken;
	private String scheme;
	private int port;
	private String host;
	private String apiserverHostName;
	private boolean isLoggingOn;
	private String baseVersion;
	private String referer;
	private String csrfToken;
	private String sessionID;
	private String authParam;
	private String saasApp;
	private String saasAppUsername;
	private String saasAppPassword;
	private String saasAppUserRole;
	private String saasAppToken;
	private String saasAppClientId;
	private String saasAppClientSecret;
	private String domainName;
	private String saasAppEndUser1Name;
	private String saasAppEndUser1Password;
	private String saasAppEndUser2Name;
	private String saasAppEndUser2Password;
	private String saasAppExternalUser1Name;
	private String saasAppExternalUser1Password;
	private String saasAppExternalUser2Name;
	private String saasAppExternalUser2Password;
	private String saasAppGroupMailId;
	private String socUserName;
//	private AuditGoldenSetTestDataSetup auditGoldenSetTestDataSetup;
	
		
	/**
	 * @return the socUserName
	 */
	public String getSocUserName() {
		return socUserName;
	}
	/**
	 * @param socUserName the socUserName to set
	 */
	public void setSocUserName(String socUserName) {
		this.socUserName = socUserName;
	}
	/**
	 * @return the authParam
	 */
	public String getAuthParam() {
		return authParam;
	}
	/**
	 * @param authParam the authParam to set
	 */
	public void setAuthParam(String authParam) {
		this.authParam = authParam;
	}
	/**
	 * @return the saasAppUsername
	 */
	public String getSaasAppUsername() {
		return saasAppUsername;
	}
	/**
	 * @param saasAppUsername the saasAppUsername to set
	 */
	public void setSaasAppUsername(String saasAppUsername) {
		this.saasAppUsername = saasAppUsername;
	}
	
	/**
	 * @return the saasAppPassword
	 */
	public String getSaasAppPassword() {
		return saasAppPassword;
	}
	/**
	 * @param saasAppPassword the saasAppPassword to set
	 */
	public void setSaasAppPassword(String saasAppPassword) {
		this.saasAppPassword = saasAppPassword;
	}
	
	
	/**
	 * @return the saasAppUserRole
	 */
	public String getSaasAppUserRole() {
		return saasAppUserRole;
	}
	/**
	 * @param saasAppUserRole the saasAppUserRole to set
	 */
	public void setSaasAppUserRole(String saasAppUserRole) {
		this.saasAppUserRole = saasAppUserRole;
	}
	
	
	/**
	 * @return the baseVersion
	 */
	public String getBaseVersion() {
		return baseVersion;
	}
	/**
	 * @return the isLoggingOn
	 */
	public boolean isLoggingOn() {
		return isLoggingOn;
	}
	/**
	 * @param isLoggingOn the isLoggingOn to set
	 */
	public void setLoggingOn(boolean isLoggingOn) {
		this.isLoggingOn = isLoggingOn;
	}
	/**
	 * @param baseVersion the baseVersion to set
	 */
	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the aPIMap
	 */
	public Map<String, String> getAPIMap() {
		return APIMap;
	}
	/**
	 * @param aPIMap the aPIMap to set
	 */
	public void setAPIMap(Map<String, String> aPIMap) {
		APIMap = aPIMap;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the loginURL
	 */
	public String getLoginURL() {
		return loginURL;
	}
	/**
	 * @param loginURL the loginURL to set
	 */
	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}
	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}
	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	/**
	 * @return the tenantName
	 */
	public String getTenantName() {
		return tenantName;
	}
	/**
	 * @param tenantName the tenantName to set
	 */
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getTenantDomainName() {
		return tenantDomainName;
	}

	public void setTenantDomainName(String tenantDomainName) {
		this.tenantDomainName = tenantDomainName;
	}

	/**
	 * @return the tenantToken
	 */
	public String getTenantToken() {
		return tenantToken;
	}

	/**
	 * @param tenantToken the tenantToken to set
	 */
	public void setTenantToken(String tenantToken) {
		this.tenantToken = tenantToken;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}
	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	/**
	 * @return the apiserverHostName
	 */
	public String getApiserverHostName() {
		return apiserverHostName;
	}
	/**
	 * @param apiserverHostName the apiserverHostName to set
	 */
	public void setApiserverHostName(String apiserverHostName) {
		this.apiserverHostName = apiserverHostName;
	}
	
	public String getReferer() {
		return referer;
	}
	
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	public String getSaasApp() {
		return saasApp;
	}
	
	public void setSaasApp(String saasapp) {
		this.saasApp = saasapp;
	}
	
	
	public String getSaasAppToken() {
		return saasAppToken;
	}
	
	public void setSaasAppToken(String saasappToken) {
		this.saasAppToken = saasappToken;
	}
	
	public String getSaasAppClientId() {
		return saasAppClientId;
	}
	
	public void setSaasAppClientId(String saasAppClientId) {
		this.saasAppClientId = saasAppClientId;
	}
	
	public String getSaasAppClientSecret() {
		return saasAppClientSecret;
	}
	
	public void setSaasAppClientSecret(String saasAppClientSecret) {
		this.saasAppClientSecret = saasAppClientSecret;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	/**
	 * @return the csrfToken
	 */
	public String getCSRFToken() {
		return csrfToken;
	}
	/**
	 * @param csrfToken the csrfToken to set
	 */
	public void setCSRFToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}
	/**
	 * @return the sessionID
	 */
	public String getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID the sessionID to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	/**
	 * @return the saasAppEndUser1Name
	 */
	public String getSaasAppEndUser1Name() {
		return saasAppEndUser1Name;
	}
	/**
	 */
	/**
	 * @param saasAppEndUser1Name
	 */
	public void setSaasAppEndUser1Name(String saasAppEndUser1Name) {
		this.saasAppEndUser1Name = saasAppEndUser1Name;
	}
	
	/**
	 * @return the saasAppEndUser1Password
	 */
	public String getSaasAppEndUser1Password() {
		return saasAppEndUser1Password;
	}
	/**
	 * @param saasAppEndUser1Password
	 */
	public void setSaasAppEndUser1Password(String saasAppEndUser1Password) {
		this.saasAppEndUser1Password = saasAppEndUser1Password;
	}
	/**
	 * @return the saasAppEndUser2Name
	 */
	public String getSaasAppEndUser2Name() {
		return saasAppEndUser2Name;
	}
	/**
	 */
	/**
	 * @param saasAppEndUser2Name
	 */
	public void setSaasAppEndUser2Name(String saasAppEndUser2Name) {
		this.saasAppEndUser2Name = saasAppEndUser2Name;
	}
	
	/**
	 * @return the saasAppEndUser2Password
	 */
	public String getSaasAppEndUser2Password() {
		return saasAppEndUser2Password;
	}
	/**
	 * @param saasAppEndUser2Password
	 */
	public void setSaasAppEndUser2Password(String saasAppEndUser2Password) {
		this.saasAppEndUser2Password = saasAppEndUser2Password;
	}
	/**
	 * @return the saasAppExternalUser
	 */
	@Deprecated
	public String getSaasAppExternalUser() {
		return saasAppExternalUser1Name;
	}
	/**
	 * @param saasAppExternalUser
	 */
	@Deprecated
	public void setSaasAppExternalUser(String saasAppExternalUser1Name) {
		this.saasAppExternalUser1Name = saasAppExternalUser1Name;
	}
	
	/**
	 * @param saasAppExternalUser1Name
	 */
	public String getSaasAppExternalUser1Name() {
		return saasAppExternalUser1Name;
	}
	/**
	 * @param saasAppExternalUser1Name
	 */
	public void setSaasAppExternalUser1Name(String saasAppExternalUser1Name) {
		this.saasAppExternalUser1Name = saasAppExternalUser1Name;
	}
	/**
	 * @param saasAppExternalUser1Password
	 */
	public String getSaasAppExternalUser1Password() {
		return saasAppExternalUser1Password;
	}
	/**
	 * @param saasAppExternalUser1Password
	 */
	public void setSaasAppExternalUser1Password(String saasAppExternalUser1Password) {
		this.saasAppExternalUser1Password = saasAppExternalUser1Password;
	}
	
	/**
	 * @param saasAppExternalUser2Name
	 */
	public String getSaasAppExternalUser2Name() {
		return saasAppExternalUser2Name;
	}
	/**
	 * @param saasAppExternalUser2Name
	 */
	public void setSaasAppExternalUser2Name(String saasAppExternalUser2Name) {
		this.saasAppExternalUser2Name = saasAppExternalUser2Name;
	}
	/**
	 * @param saasAppExternalUser2Password
	 */
	public String getSaasAppExternalUser2Password() {
		return saasAppExternalUser2Password;
	}
	/**
	 * @param saasAppExternalUser2Password
	 */
	public void setSaasAppExternalUser2Password(String saasAppExternalUser2Password) {
		this.saasAppExternalUser2Password = saasAppExternalUser2Password;
	}
	/**
	 * @param saasAppGroupMailId
	 */
	public String getSaasAppGroupMailId() {
		return saasAppGroupMailId;
	}
	/**
	 * @param saasAppGroupMailId
	 */
	public void setSaasAppGroupMailId(String saasAppGroupMailId) {
		this.saasAppGroupMailId = saasAppGroupMailId;
	}

//	public AuditGoldenSetTestDataSetup getAuditGoldenSetTestDataSetup() {
//		return auditGoldenSetTestDataSetup;
//	}
//	public void setAuditGoldenSetTestDataSetup(AuditGoldenSetTestDataSetup auditGoldenSetTestDataSetup) {
//		this.auditGoldenSetTestDataSetup = auditGoldenSetTestDataSetup;
//	}
	
	
	
}
