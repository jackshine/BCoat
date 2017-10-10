/**
 * 
 */
package com.elastica.beatle;

import java.util.Map;

import com.elastica.beatle.audit.AuditGoldenSetTestDataSetup;

/**
 * @author anuvrath
 *
 */
public class TestSuiteDTO {
	private String platform;
	private String cloudSocUname;
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
	private String saasAppExternalUserName;
	private String saasAppExternalUserToken;
	private String saasAppGroupMailId;
	private String socUserName;
	private AuditGoldenSetTestDataSetup auditGoldenSetTestDataSetup;
	private String saasAppLoginHost;
	private String goldenInputTmplPath;
	private String goldenInputFilePath;
	private String esScriptsHostName;
	private String esScriptsUserName;
	private String dpoUsername;
	private String dpoPassword;
	private String spanvaIp;
	private String spanvaVersion;
	private String spanvausername;
	private String spanvapwd;
	private String spanvaAgentName;
	private String logCompressionFormat;
	private String spanvaUpdatedVersion;
	private String firewallSet;
	private String loadApparraziServicesCheck;
	private String proxyUrl;
	private String proxyExtension;
	private String proxyExtensionVersion;
	private boolean isInternal;
	
	
	private String user2Name;
	private String user2Password;
	private String user2TenantName;
	private String user2TenantToken;
	private String user2AuthParam;
	private String user2CsrfToken;
	private String user2SessionID;
	
	private String tenant2Aclcheck;
	
	private String adminUser;
	private String adminUserPwd;
	private String adminUserTen;
	private String adminUserTenToken;
	private String adminUserAuthParam;
	private String adminUserCsrfToken;
	private String adminUserSessionID;
	
	private String dpoUserTen;
	private String dpoTenToken;
	private String dpoAuthParam;
	private String dpoCsrfToken;
	private String dpoSessionID;
	//Added for DDD 
	private String custDomain;
	private String gwHostname;
	
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the custDomain
	 */
	public String getCustDomain() {
		return custDomain;
	}
	/**
	 * @param custDomain the custDomain to set
	 */
	public void setCustDomain(String custDomain) {
		this.custDomain = custDomain;
	}
	public String getCsrfToken() {
		return csrfToken;
	}
	public void setCsrfToken(String csrfToken) {
		this.csrfToken = csrfToken;
	}
	public String getUser2Name() {
		return user2Name;
	}
	public void setUser2Name(String user2Name) {
		this.user2Name = user2Name;
	}
	public String getUser2Password() {
		return user2Password;
	}
	public void setUser2Password(String user2Password) {
		this.user2Password = user2Password;
	}
	public String getUser2TenantName() {
		return user2TenantName;
	}
	public void setUser2TenantName(String user2TenantName) {
		this.user2TenantName = user2TenantName;
	}
	public String getUser2TenantToken() {
		return user2TenantToken;
	}
	public void setUser2TenantToken(String user2TenantToken) {
		this.user2TenantToken = user2TenantToken;
	}
	public String getUser2AuthParam() {
		return user2AuthParam;
	}
	public void setUser2AuthParam(String user2AuthParam) {
		this.user2AuthParam = user2AuthParam;
	}
	public String getUser2CsrfToken() {
		return user2CsrfToken;
	}
	public void setUser2CsrfToken(String user2CsrfToken) {
		this.user2CsrfToken = user2CsrfToken;
	}
	public String getUser2SessionID() {
		return user2SessionID;
	}
	public void setUser2SessionID(String user2SessionID) {
		this.user2SessionID = user2SessionID;
	}
	public String getTenant2Aclcheck() {
		return tenant2Aclcheck;
	}
	public void setTenant2Aclcheck(String tenant2Aclcheck) {
		this.tenant2Aclcheck = tenant2Aclcheck;
	}
	public String getAdminUser() {
		return adminUser;
	}
	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}
	public String getAdminUserPwd() {
		return adminUserPwd;
	}
	public void setAdminUserPwd(String adminUserPwd) {
		this.adminUserPwd = adminUserPwd;
	}
	public String getAdminUserTen() {
		return adminUserTen;
	}
	public void setAdminUserTen(String adminUserTen) {
		this.adminUserTen = adminUserTen;
	}
	public String getAdminUserTenToken() {
		return adminUserTenToken;
	}
	public void setAdminUserTenToken(String adminUserTenToken) {
		this.adminUserTenToken = adminUserTenToken;
	}
	public String getAdminUserAuthParam() {
		return adminUserAuthParam;
	}
	public void setAdminUserAuthParam(String adminUserAuthParam) {
		this.adminUserAuthParam = adminUserAuthParam;
	}
	public String getAdminUserCsrfToken() {
		return adminUserCsrfToken;
	}
	public void setAdminUserCsrfToken(String adminUserCsrfToken) {
		this.adminUserCsrfToken = adminUserCsrfToken;
	}
	public String getAdminUserSessionID() {
		return adminUserSessionID;
	}
	public void setAdminUserSessionID(String adminUserSessionID) {
		this.adminUserSessionID = adminUserSessionID;
	}
	public String getDpoUserTen() {
		return dpoUserTen;
	}
	public void setDpoUserTen(String dpoUserTen) {
		this.dpoUserTen = dpoUserTen;
	}
	public String getDpoTenToken() {
		return dpoTenToken;
	}
	public void setDpoTenToken(String dpoTenToken) {
		this.dpoTenToken = dpoTenToken;
	}
	public String getDpoAuthParam() {
		return dpoAuthParam;
	}
	public void setDpoAuthParam(String dpoAuthParam) {
		this.dpoAuthParam = dpoAuthParam;
	}
	public String getDpoCsrfToken() {
		return dpoCsrfToken;
	}
	public void setDpoCsrfToken(String dpoCsrfToken) {
		this.dpoCsrfToken = dpoCsrfToken;
	}
	public String getDpoSessionID() {
		return dpoSessionID;
	}
	public void setDpoSessionID(String dpoSessionID) {
		this.dpoSessionID = dpoSessionID;
	}
	public String getLoadApparraziServicesCheck() {
		return loadApparraziServicesCheck;
	}
	public void setLoadApparraziServicesCheck(String loadApparraziServicesCheck) {
		this.loadApparraziServicesCheck = loadApparraziServicesCheck;
	}
	public String getFirewallSet() {
		return firewallSet;
	}
	public void setFirewallSet(String firewallSet) {
		this.firewallSet = firewallSet;
	}
	public String getSpanvaUpdatedVersion() {
		return spanvaUpdatedVersion;
	}
	public void setSpanvaUpdatedVersion(String spanvaUpdatedVersion) {
		this.spanvaUpdatedVersion = spanvaUpdatedVersion;
	}
	/**
	 * @return the saasAppLoginHost
	 */
	public String getSaasAppLoginHost() {
		return saasAppLoginHost;
	}
	/**
	 * @param saasAppLoginHost the saasAppLoginHost to set
	 */
	public void setSaasAppLoginHost(String saasAppLoginHost) {
		this.saasAppLoginHost = saasAppLoginHost;
	}	
	
	public String getLogCompressionFormat() {
		return logCompressionFormat;
	}
	public void setLogCompressionFormat(String logCompressionFormat) {
		this.logCompressionFormat = logCompressionFormat;
	}
	public String getSpanvausername() {
		return spanvausername;
	}
	public void setSpanvausername(String spanvausername) {
		this.spanvausername = spanvausername;
	}
	public String getSpanvapwd() {
		return spanvapwd;
	}
	public void setSpanvapwd(String spanvapwd) {
		this.spanvapwd = spanvapwd;
	}
	public String getSpanvaAgentName() {
		return spanvaAgentName;
	}
	public void setSpanvaAgentName(String spanvaAgentName) {
		this.spanvaAgentName = spanvaAgentName;
	}
	public String getSpanvaIp() {
		return spanvaIp;
	}
	public void setSpanvaIp(String spanvaIp) {
		this.spanvaIp = spanvaIp;
	}
	
	public String getGoldenInputTmplPath() {
		return goldenInputTmplPath;
	}
	public void setGoldenInputTmplPath(String goldenInputTmplPath) {
		this.goldenInputTmplPath = goldenInputTmplPath;
	}
	public String getGoldenInputFilePath() {
		return goldenInputFilePath;
	}
	public void setGoldenInputFilePath(String goldenInputFilePath) {
		this.goldenInputFilePath = goldenInputFilePath;
	}
	public String getEsScriptsHostName() {
		return esScriptsHostName;
	}
	public void setEsScriptsHostName(String esScriptsHostName) {
		this.esScriptsHostName = esScriptsHostName;
	}
	public String getEsScriptsUserName() {
		return esScriptsUserName;
	}
	public void setEsScriptsUserName(String esScriptsUserName) {
		this.esScriptsUserName = esScriptsUserName;
	}
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
	
	/**
	 * @param referer
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	/**
	 * @return
	 */
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
	public AuditGoldenSetTestDataSetup getAuditGoldenSetTestDataSetup() {
		return auditGoldenSetTestDataSetup;
	}
	public void setAuditGoldenSetTestDataSetup(AuditGoldenSetTestDataSetup auditGoldenSetTestDataSetup) {
		this.auditGoldenSetTestDataSetup = auditGoldenSetTestDataSetup;
	}
	
	/**
	 * @return the dpoUsername
	 */
	public String getDpoUsername() {
		return dpoUsername;
	}
	/**
	 * @param dpoUsername the dpoUsername to set
	 */
	public void setDpoUsername(String dpoUsername) {
		this.dpoUsername = dpoUsername;
	}
	/**
	 * @return the dpoPassword
	 */
	public String getDpoPassword() {
		return dpoPassword;
	}
	/**
	 * @param dpoPassword the dpoPassword to set
	 */
	public void setDpoPassword(String dpoPassword) {
		this.dpoPassword = dpoPassword;
	}
	
	/**
	 * @return the cloudSocUname
	 */
	public String getCloudSocUname() {
		return cloudSocUname;
	}
	/**
	 * @param cloudSocUname the cloudSocUname to set
	 */
	public void setCloudSocUname(String cloudSocUname) {
		this.cloudSocUname = cloudSocUname;
	}
	
	/**
	 * @return
	 */
	public String getSpanvaVersion() {
		return spanvaVersion;
	}
	/**
	 * @param spanvaVersion
	 */
	public void setSpanvaVersion(String spanvaVersion) {
		this.spanvaVersion = spanvaVersion;
	}
	/**
	 * @return the saasAppExternalUsername
	 */
	public String getSaasAppExternalUserName() {
		return saasAppExternalUserName;
	}
	/**
	 * @param saasAppExternalUsername the saasAppExternalUsername to set
	 */
	public void setSaasAppExternalUserName(String saasAppExternalUserName) {
		this.saasAppExternalUserName = saasAppExternalUserName;
	}
	/**
	 * @return the saasAppExternalUserToken
	 */
	public String getSaasAppExternalUserToken() {
		return saasAppExternalUserToken;
	}
	/**
	 * @param saasAppExternalUserToken the saasAppExternalUserToken to set
	 */
	public void setSaasAppExternalUserToken(String saasAppExternalUserToken) {
		this.saasAppExternalUserToken = saasAppExternalUserToken;
	}
	
	/**
	 * @return
	 */
	public String getProxyUrl() {
		return proxyUrl;
	}
	
	/**
	 * @param proxyUrl
	 */
	public void setProxyUrl(String proxyUrl){
		this.proxyUrl = proxyUrl;
	}
	
	/**
	 * @return
	 */
	public String getProxyExtension() {
		return proxyExtension;
	}
	
	/**
	 * @param proxyExtension
	 */
	public void setProxyExtension(String proxyExtension){
		this.proxyExtension = proxyExtension;
	}
	
	/**
	 * @return
	 */
	public String getProxyExtensionVersion() {
		return proxyExtensionVersion;
	}
	
	/**
	 * @param proxyExtensionVersion
	 */
	public void setProxyExtensionVersion(String proxyExtensionVersion){
		this.proxyExtensionVersion = proxyExtensionVersion;
	}
	public boolean isInternal() {
		return isInternal;
	}
	public void setInternal(boolean isInternal) {
		this.isInternal = isInternal;
	}
	
	public String getGwHostname() {
		return gwHostname;
	}
	
	public void setGwHostname(String gwHostname) {
		this.gwHostname = gwHostname;
	}
}
