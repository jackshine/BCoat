package com.elastica.beatle.gateway.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class gDriveGWSource {
	
	@JsonProperty("version")
	private String version;
	@JsonProperty("host")
	private String host;
	@JsonProperty("inserted_timestamp")
	private String insertedTimestamp;
	@JsonProperty("created_timestamp")
	private String createdTimestamp;
	@JsonProperty("message")
	private String message;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("facility")
	private String facility;
	@JsonProperty("user")
	private String user;
	@JsonProperty("_ObjectName")
	private String ObjectName;
	@JsonProperty("Activity_type")
	private String ActivityType;
	@JsonProperty("Object_type")
	private String ObjectType;
	@JsonProperty("user_name")
	private String userName;
	@JsonProperty("user-agent")
	private String userAgent;
	@JsonProperty("req_uri")
	private String reqUri;
	@JsonProperty("req_size")
	private String reqSize;
	@JsonProperty("resp_size")
	private String respSize;
	@JsonProperty("resp_code")
	private String respCode;
	@JsonProperty("web_path")
	private String webPath;
	@JsonProperty("browser")
	private String browser;
	@JsonProperty("device")
	private String device;
	@JsonProperty("location")
	private String location;
	@JsonProperty("country")
	private String country;
	@JsonProperty("city")
	private String city;
	@JsonProperty("time_zone")
	private String timeZone;
	@JsonProperty("region")
	private String region;
	@JsonProperty("is_anonymous_proxy")
	private String isAnonymousProxy;
	@JsonProperty("__source")
	private String Source;
	@JsonProperty("referer_uri")
	private String refererUri;
	@JsonProperty("elastica_user")
	private String elasticaUser;
	@JsonProperty("transit_hosts")
	private String transitHosts;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 *
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	/**
	 *
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 *
	 * @return
	 * The host
	 */
	@JsonProperty("host")
	public String getHost() {
		return host;
	}

	/**
	 *
	 * @param host
	 * The host
	 */
	@JsonProperty("host")
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 *
	 * @return
	 * The insertedTimestamp
	 */
	@JsonProperty("inserted_timestamp")
	public String getInsertedTimestamp() {
		return insertedTimestamp;
	}

	/**
	 *
	 * @param insertedTimestamp
	 * The inserted_timestamp
	 */
	@JsonProperty("inserted_timestamp")
	public void setInsertedTimestamp(String insertedTimestamp) {
		this.insertedTimestamp = insertedTimestamp;
	}

	/**
	 *
	 * @return
	 * The createdTimestamp
	 */
	@JsonProperty("created_timestamp")
	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 *
	 * @param createdTimestamp
	 * The created_timestamp
	 */
	@JsonProperty("created_timestamp")
	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	/**
	 *
	 * @return
	 * The message
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 *
	 * @param message
	 * The message
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 *
	 * @return
	 * The severity
	 */
	@JsonProperty("severity")
	public String getSeverity() {
		return severity;
	}

	/**
	 *
	 * @param severity
	 * The severity
	 */
	@JsonProperty("severity")
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 *
	 * @return
	 * The facility
	 */
	@JsonProperty("facility")
	public String getFacility() {
		return facility;
	}

	/**
	 *
	 * @param facility
	 * The facility
	 */
	@JsonProperty("facility")
	public void setFacility(String facility) {
		this.facility = facility;
	}

	/**
	 *
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public String getUser() {
		return user;
	}

	/**
	 *
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 *
	 * @return
	 * The ObjectName
	 */
	@JsonProperty("_ObjectName")
	public String getObjectName() {
		return ObjectName;
	}

	/**
	 *
	 * @param ObjectName
	 * The Object_name
	 */
	@JsonProperty("_ObjectName")
	public void setObjectName(String ObjectName) {
		this.ObjectName = ObjectName;
	}

	/**
	 *
	 * @return
	 * The ActivityType
	 */
	@JsonProperty("Activity_type")
	public String getActivityType() {
		return ActivityType;
	}

	/**
	 *
	 * @param ActivityType
	 * The Activity_type
	 */
	@JsonProperty("Activity_type")
	public void setActivityType(String ActivityType) {
		this.ActivityType = ActivityType;
	}

	/**
	 *
	 * @return
	 * The ObjectType
	 */
	@JsonProperty("Object_type")
	public String getObjectType() {
		return ObjectType;
	}

	/**
	 *
	 * @param ObjectType
	 * The Object_type
	 */
	@JsonProperty("Object_type")
	public void setObjectType(String ObjectType) {
		this.ObjectType = ObjectType;
	}

	/**
	 *
	 * @return
	 * The userName
	 */
	@JsonProperty("user_name")
	public String getUserName() {
		return userName;
	}

	/**
	 *
	 * @param userName
	 * The user_name
	 */
	@JsonProperty("user_name")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 *
	 * @return
	 * The userAgent
	 */
	@JsonProperty("user-agent")
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 *
	 * @param userAgent
	 * The user-agent
	 */
	@JsonProperty("user-agent")
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 *
	 * @return
	 * The reqUri
	 */
	@JsonProperty("req_uri")
	public String getReqUri() {
		return reqUri;
	}

	/**
	 *
	 * @param reqUri
	 * The req_uri
	 */
	@JsonProperty("req_uri")
	public void setReqUri(String reqUri) {
		this.reqUri = reqUri;
	}

	/**
	 *
	 * @return
	 * The reqSize
	 */
	@JsonProperty("req_size")
	public String getReqSize() {
		return reqSize;
	}

	/**
	 *
	 * @param reqSize
	 * The req_size
	 */
	@JsonProperty("req_size")
	public void setReqSize(String reqSize) {
		this.reqSize = reqSize;
	}

	/**
	 *
	 * @return
	 * The respSize
	 */
	@JsonProperty("resp_size")
	public String getRespSize() {
		return respSize;
	}

	/**
	 *
	 * @param respSize
	 * The resp_size
	 */
	@JsonProperty("resp_size")
	public void setRespSize(String respSize) {
		this.respSize = respSize;
	}

	/**
	 *
	 * @return
	 * The respCode
	 */
	@JsonProperty("resp_code")
	public String getRespCode() {
		return respCode;
	}

	/**
	 *
	 * @param respCode
	 * The resp_code
	 */
	@JsonProperty("resp_code")
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 *
	 * @return
	 * The webPath
	 */
	@JsonProperty("web_path")
	public String getWebPath() {
		return webPath;
	}

	/**
	 *
	 * @param webPath
	 * The web_path
	 */
	@JsonProperty("web_path")
	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}

	/**
	 *
	 * @return
	 * The browser
	 */
	@JsonProperty("browser")
	public String getBrowser() {
		return browser;
	}

	/**
	 *
	 * @param browser
	 * The browser
	 */
	@JsonProperty("browser")
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 *
	 * @return
	 * The device
	 */
	@JsonProperty("device")
	public String getDevice() {
		return device;
	}

	/**
	 *
	 * @param device
	 * The device
	 */
	@JsonProperty("device")
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 *
	 * @return
	 * The location
	 */
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	/**
	 *
	 * @param location
	 * The location
	 */
	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 *
	 * @return
	 * The country
	 */
	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	/**
	 *
	 * @param country
	 * The country
	 */
	@JsonProperty("country")
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 *
	 * @return
	 * The city
	 */
	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	/**
	 *
	 * @param city
	 * The city
	 */
	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 *
	 * @return
	 * The timeZone
	 */
	@JsonProperty("time_zone")
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 *
	 * @param timeZone
	 * The time_zone
	 */
	@JsonProperty("time_zone")
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 *
	 * @return
	 * The region
	 */
	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	/**
	 *
	 * @param region
	 * The region
	 */
	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 *
	 * @return
	 * The isAnonymousProxy
	 */
	@JsonProperty("is_anonymous_proxy")
	public String getIsAnonymousProxy() {
		return isAnonymousProxy;
	}

	/**
	 *
	 * @param isAnonymousProxy
	 * The is_anonymous_proxy
	 */
	@JsonProperty("is_anonymous_proxy")
	public void setIsAnonymousProxy(String isAnonymousProxy) {
		this.isAnonymousProxy = isAnonymousProxy;
	}

	/**
	 *
	 * @return
	 * The Source
	 */
	@JsonProperty("__source")
	public String getSource() {
		return Source;
	}

	/**
	 *
	 * @param Source
	 * The __source
	 */
	@JsonProperty("__source")
	public void setSource(String Source) {
		this.Source = Source;
	}

	/**
	 *
	 * @return
	 * The refererUri
	 */
	@JsonProperty("referer_uri")
	public String getRefererUri() {
		return refererUri;
	}

	/**
	 *
	 * @param refererUri
	 * The referer_uri
	 */
	@JsonProperty("referer_uri")
	public void setRefererUri(String refererUri) {
		this.refererUri = refererUri;
	}

	/**
	 *
	 * @return
	 * The elasticaUser
	 */
	@JsonProperty("elastica_user")
	public String getElasticaUser() {
		return elasticaUser;
	}

	/**
	 *
	 * @param elasticaUser
	 * The elastica_user
	 */
	@JsonProperty("elastica_user")
	public void setElasticaUser(String elasticaUser) {
		this.elasticaUser = elasticaUser;
	}

	/**
	 *
	 * @return
	 * The transitHosts
	 */
	@JsonProperty("transit_hosts")
	public String getTransitHosts() {
		return transitHosts;
	}

	/**
	 *
	 * @param transitHosts
	 * The transit_hosts
	 */
	@JsonProperty("transit_hosts")
	public void setTransitHosts(String transitHosts) {
		this.transitHosts = transitHosts;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
