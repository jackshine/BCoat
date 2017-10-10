package com.elastica.common;

import java.util.Map;

/**
 * 
 * @author eldorajan
 *
 */
public class SuiteData {
	
	private String browser;
	private String mode;
	private String hubUrl;
	private String baseUrl;
	private String envName;
	private String proxyUrl;
	private String proxyExtension;
	private String proxyExtensionVersion;
	private String sslCertificate;
	private String exportHar;
	private String exportHarFolder;
	private String firefoxProfile;
	private String sauceLabUrl;
	private String sslMailFlag;
	private String sslMailClientId;
	private String sslMailClientSecret;
	private String sslMailRefreshToken;
	private String accessToken;
	
	private String loginUrl;
	private String dashboardUrl;
	private String apiServer;
	private String csrfToken;
	private String sessionID;
	private String host;
	private String scheme;
	private String referer;
	private String baseVersion;
	private Map<String, String> APIMap;
	
	private String username;
	private String password;
	private String tenantName;
	private String tenantDomainName;
	private String tenantToken;
	private String testUsername;
	private String testPassword;
	
	private String adminUsername;
	private String adminPassword;
	private String dpoUsername;
	private String dpoPassword;
	private String endUsername;
	private String endUserPassword;
	
	private String saasAppName;
	private String saasAppUsername;
	private String saasAppPassword;
	private String saasAppMetaData;
	private String saasAppBaseUrl;
	private String saasAppToken;
	private String saasAppBaseDomain;
	private String saasAppUserRole;
	private String saasAppClientId;
	private String saasAppClientSecret;
	private String saasAppLoginHost;
	private String saasAppTestUsername;
	private String saasAppTestPassword;
	private String saasAppExternalUsername;
	private String saasAppExternalPassword;
	private String saasAppExternalUserToken;
	private String accountType;
	private String city;
	private String country;
	private String longitude;
	private String latitude;
	private String testRunHost;
	private String runRegion;
	private String timeZone;
	private String userAgent;
	private String deviceName;
	
	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}
	/**
	 * @param browser the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return the hubUrl
	 */
	public String getHubUrl() {
		return hubUrl;
	}
	/**
	 * @param hubUrl the hubUrl to set
	 */
	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}
	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	/**
	 * @return the envName
	 */
	public String getEnvName() {
		return envName;
	}
	/**
	 * @param envName the envName to set
	 */
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	/**
	 * @return the sauceLabUrl
	 */
	public String getSauceLabUrl() {
		return sauceLabUrl;
	}
	/**
	 * @param sauceLabUrl the sauceLabUrl to set
	 */
	public void setSauceLabUrl(String sauceLabUrl) {
		this.sauceLabUrl = sauceLabUrl;
	}
	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	/**
	 * @return the dashboardUrl
	 */
	public String getDashboardUrl() {
		return dashboardUrl;
	}
	/**
	 * @param dashboardUrl the dashboardUrl to set
	 */
	public void setDashboardUrl(String dashboardUrl) {
		this.dashboardUrl = dashboardUrl;
	}
	/**
	 * @return the apiServer
	 */
	public String getApiServer() {
		return apiServer;
	}
	/**
	 * @param apiServer the apiServer to set
	 */
	public void setApiServer(String apiServer) {
		this.apiServer = apiServer;
	}
	/**
	 * @return the csrfToken
	 */
	public String getCsrfToken() {
		return csrfToken;
	}
	/**
	 * @param csrfToken the csrfToken to set
	 */
	public void setCsrfToken(String csrfToken) {
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
	 * @return the referer
	 */
	public String getReferer() {
		return referer;
	}
	/**
	 * @param referer the referer to set
	 */
	public void setReferer(String referer) {
		this.referer = referer;
	}
	/**
	 * @return the baseVersion
	 */
	public String getBaseVersion() {
		return baseVersion;
	}
	/**
	 * @param baseVersion the baseVersion to set
	 */
	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
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
	/**
	 * @return the tenantDomainName
	 */
	public String getTenantDomainName() {
		return tenantDomainName;
	}
	/**
	 * @param tenantDomainName the tenantDomainName to set
	 */
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
	 * @return the testUsername
	 */
	public String getTestUsername() {
		return testUsername;
	}
	/**
	 * @param testUsername the testUsername to set
	 */
	public void setTestUsername(String testUsername) {
		this.testUsername = testUsername;
	}
	/**
	 * @return the testPassword
	 */
	public String getTestPassword() {
		return testPassword;
	}
	/**
	 * @param testPassword the testPassword to set
	 */
	public void setTestPassword(String testPassword) {
		this.testPassword = testPassword;
	}
	/**
	 * @return the saasAppName
	 */
	public String getSaasAppName() {
		return saasAppName;
	}
	/**
	 * @param saasAppName the saasAppName to set
	 */
	public void setSaasAppName(String saasAppName) {
		this.saasAppName = saasAppName;
	}
	/**
	 * @return the saasAppUserName
	 */
	public String getSaasAppUsername() {
		return saasAppUsername;
	}
	/**
	 * @param saasAppUserName the saasAppUserName to set
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
	 * @return the saasAppTestUserName
	 */
	public String getSaasAppTestUsername() {
		return saasAppTestUsername;
	}
	/**
	 * @param saasAppTestUserName the saasAppTestUserName to set
	 */
	public void setSaasAppTestUsername(String saasAppTestUsername) {
		this.saasAppTestUsername = saasAppTestUsername;
	}
	/**
	 * @return the saasAppTestPassword
	 */
	public String getSaasAppTestPassword() {
		return saasAppTestPassword;
	}
	/**
	 * @param saasAppTestPassword the saasAppTestPassword to set
	 */
	public void setSaasAppTestPassword(String saasAppTestPassword) {
		this.saasAppTestPassword = saasAppTestPassword;
	}
	/**
	 * @return the saasAppMetaData
	 */
	public String getSaasAppMetaData() {
		return saasAppMetaData;
	}
	/**
	 * @param saasAppMetaData the saasAppMetaData to set
	 */
	public void setSaasAppMetaData(String saasAppMetaData) {
		this.saasAppMetaData = saasAppMetaData;
	}
	/**
	 * @return the saasBaseurl
	 */
	public String getSaasAppBaseUrl() {
		return saasAppBaseUrl;
	}
	/**
	 * @param saasBaseurl the saasBaseurl to set
	 */
	public void setSaasAppBaseUrl(String saasAppBaseUrl) {
		this.saasAppBaseUrl = saasAppBaseUrl;
	}
	/**
	 * @return the saasToken
	 */
	public String getSaasAppToken() {
		return saasAppToken;
	}
	/**
	 * @param saasToken the saasToken to set
	 */
	public void setSaasAppToken(String saasAppToken) {
		this.saasAppToken = saasAppToken;
	}
	/**
	 * @return the saasBaseDomain
	 */
	public String getSaasAppBaseDomain() {
		return saasAppBaseDomain;
	}
	/**
	 * @param saasBaseDomain the saasBaseDomain to set
	 */
	public void setSaasAppBaseDomain(String saasAppBaseDomain) {
		this.saasAppBaseDomain = saasAppBaseDomain;
	}
	/**
	 * @return the saasAppUserRole
	 */
	public String getSaasAppUserRole() {
		return saasAppUserRole;
	}
	/**
	 * @return the saasAppClientId
	 */
	public String getSaasAppClientId() {
		return saasAppClientId;
	}
	/**
	 * @param saasAppClientId the saasAppClientId to set
	 */
	public void setSaasAppClientId(String saasAppClientId) {
		this.saasAppClientId = saasAppClientId;
	}
	/**
	 * @return the saasAppClientSecret
	 */
	public String getSaasAppClientSecret() {
		return saasAppClientSecret;
	}
	/**
	 * @param saasAppClientSecret the saasAppClientSecret to set
	 */
	public void setSaasAppClientSecret(String saasAppClientSecret) {
		this.saasAppClientSecret = saasAppClientSecret;
	}
	/**
	 * @param saasAppUserRole the saasAppUserRole to set
	 */
	public void setSaasAppUserRole(String saasAppUserRole) {
		this.saasAppUserRole = saasAppUserRole;
	}	
	/**
	 * @return the proxyUrl
	 */
	public String getProxyUrl() {
		return proxyUrl;
	}
	/**
	 * @param proxyUrl the proxyUrl to set
	 */
	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}
	/**
	 * @return the proxyExtension
	 */
	public String getProxyExtension() {
		return proxyExtension;
	}
	/**
	 * @param proxyExtension the proxyExtension to set
	 */
	public void setProxyExtension(String proxyExtension) {
		this.proxyExtension = proxyExtension;
	}
	/**
	 * @return the proxyExtensionVersion
	 */
	public String getProxyExtensionVersion() {
		return proxyExtensionVersion;
	}
	/**
	 * @param proxyExtensionVersion the proxyExtensionVersion to set
	 */
	public void setProxyExtensionVersion(String proxyExtensionVersion) {
		this.proxyExtensionVersion = proxyExtensionVersion;
	}
	
	/**
	 * @return the sslCertificate
	 */
	public String getSSlCertificate() {
		return sslCertificate;
	}
	/**
	 * @param sslCertificate the sslCertificate to set
	 */
	public void setSSlCertificate(String sslCertificate) {
		this.sslCertificate = sslCertificate;
	}

	private String sauceUsername;
	/**
	 * @return the sauceUsername
	 */
	public String getSauceUsername() {
		return sauceUsername;
	}
	/**
	 * @param sauceUsername the sauceUsername to set
	 */
	public void setSauceUsername(String sauceUsername) {
		this.sauceUsername = sauceUsername;
	}
	/**
	 * @return the saucePassword
	 */
	public String getSaucePassword() {
		return saucePassword;
	}
	/**
	 * @param saucePassword the saucePassword to set
	 */
	public void setSaucePassword(String saucePassword) {
		this.saucePassword = saucePassword;
	}
	private String saucePassword;
	/**
	 * @return the browser
	 */
	public String getAccountType() {
		return accountType;
	}
	/**
	 * @param browser the browser to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	/**
	 * @return the adminUsername
	 */
	public String getAdminUsername() {
		return adminUsername;
	}
	/**
	 * @param adminUsername the adminUsername to set
	 */
	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}
	/**
	 * @return the adminPassword
	 */
	public String getAdminPassword() {
		return adminPassword;
	}
	/**
	 * @param adminPassword the adminPassword to set
	 */
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
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
	 * @return the endUsername
	 */
	public String getEndUsername() {
		return endUsername;
	}
	/**
	 * @param endUsername the endUsername to set
	 */
	public void setEndUsername(String endUsername) {
		this.endUsername = endUsername;
	}
	/**
	 * @return the endUserPassword
	 */
	public String getEndUserPassword() {
		return endUserPassword;
	}
	/**
	 * @param endUserPassword the endUserPassword to set
	 */
	public void setEndUserPassword(String endUserPassword) {
		this.endUserPassword = endUserPassword;
	}
	
	/**
	 * @return the exportHar
	 */
	public String getExportHar() {
		return exportHar;
	}
	/**
	 * @param exportHar the exportHar to set
	 */
	public void setExportHar(String exportHar) {
		this.exportHar = exportHar;
	}
	/**
	 * @return the exportHarFolder
	 */
	public String getExportHarFolder() {
		return exportHarFolder;
	}
	/**
	 * @param exportHarFolder the exportHarFolder to set
	 */
	public void setExportHarFolder(String exportHarFolder) {
		this.exportHarFolder = exportHarFolder;
	}
	/**
	 * @return the firefoxProfile
	 */
	public String getFirefoxProfile() {
		return firefoxProfile;
	}
	/**
	 * @param firefoxProfile the firefoxProfile to set
	 */
	public void setFirefoxProfile(String firefoxProfile) {
		this.firefoxProfile = firefoxProfile;
	}
	/**
	 * @return the sslCertificate
	 */
	public String getSslCertificate() {
		return sslCertificate;
	}
	/**
	 * @param sslCertificate the sslCertificate to set
	 */
	public void setSslCertificate(String sslCertificate) {
		this.sslCertificate = sslCertificate;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the testRunHost
	 */
	public String getTestRunHost() {
		return testRunHost;
	}
	/**
	 * @param testRunHost the testRunHost to set
	 */
	public void setTestRunHost(String testRunHost) {
		this.testRunHost = testRunHost;
	}
	/**
	 * @return the runRegion
	 */
	public String getRunRegion() {
		return runRegion;
	}
	/**
	 * @param runRegion the runRegion to set
	 */
	public void setRunRegion(String runRegion) {
		this.runRegion = runRegion;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	/**
	 * @return the timeZone
	 */
	public String getUserAgent() {
		return userAgent;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	/**
	 * @return the timeZone
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	/**
	 * @return the sslMailFlag
	 */
	public String getSSlMailFlag() {
		return sslMailFlag;
	}
	/**
	 * @param sslMailFlag the sslMailFlag to set
	 */
	public void setSSlMailFlag(String sslMailFlag) {
		this.sslMailFlag = sslMailFlag;
	}
	/**
	 * @return the sslMailClientId
	 */
	public String getSSlMailClientId() {
		return sslMailClientId;
	}
	/**
	 * @param sslMailClientId the sslMailClientId to set
	 */
	public void setSSlMailClientId(String sslMailClientId) {
		this.sslMailClientId = sslMailClientId;
	}
	/**
	 * @return the sslMailClientSecret
	 */
	public String getSSlMailClientSecret() {
		return sslMailClientSecret;
	}
	/**
	 * @param sslMailClientSecret the sslMailClientSecret to set
	 */
	public void setSSlMailClientSecret(String sslMailClientSecret) {
		this.sslMailClientSecret = sslMailClientSecret;
	}
	/**
	 * @return the sslMailRefreshToken
	 */
	public String getSSlMailRefreshToken() {
		return sslMailRefreshToken;
	}
	/**
	 * @param sslMailRefreshToken the sslMailRefreshToken to set
	 */
	public void setSSlMailRefreshToken(String sslMailRefreshToken) {
		this.sslMailRefreshToken = sslMailRefreshToken;
	}
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	/**
	 * @return the saasAppExternalUsername
	 */
	public String getSaasAppExternalUsername() {
		return saasAppExternalUsername;
	}
	/**
	 * @param saasAppExternalUsername the saasAppExternalUsername to set
	 */
	public void setSaasAppExternalUsername(String saasAppExternalUsername) {
		this.saasAppExternalUsername = saasAppExternalUsername;
	}
	/**
	 * @return the saasAppExternalPassword
	 */
	public String getSaasAppExternalPassword() {
		return saasAppExternalPassword;
	}
	/**
	 * @param saasAppExternalPassword the saasAppExternalPassword to set
	 */
	public void setSaasAppExternalPassword(String saasAppExternalPassword) {
		this.saasAppExternalPassword = saasAppExternalPassword;
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
	
	
		
}
