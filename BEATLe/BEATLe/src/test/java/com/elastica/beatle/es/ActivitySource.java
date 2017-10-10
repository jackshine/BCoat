package com.elastica.beatle.es;

import org.codehaus.jackson.annotate.JsonProperty;

public class ActivitySource {
	private String req_size;
	private String Activity_type;
	@JsonProperty("user-agent")
	private String user_agent;
	private String created_timestamp;
	private String inserted_timestamp;
	private String test_id;
	private String browser;
	private String req_uri;
	private String facility;
	private String __source;
	private String resp_code;
	private String user;
	private String severity;
	private String referer_uri;
	private String web_path;
	private String location;
	private String version;
	private String device;
	private String host;
	private String message;
	private String user_name;
	private String message_hash_value;
	private String Object_type;
	private String File_Size;
	
	public String getReq_size() {
		return req_size;
	}
	public void setReq_size(String req_size) {
		this.req_size = req_size;
	}
	public String getActivity_type() {
		return Activity_type;
	}
	public void setActivity_type(String activity_type) {
		Activity_type = activity_type;
	}
	public String getUser_agent() {
		return user_agent;
	}
	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}
	public String getCreated_timestamp() {
		return created_timestamp;
	}
	public void setCreated_timestamp(String created_timestamp) {
		this.created_timestamp = created_timestamp;
	}
	public String getInserted_timestamp() {
		return inserted_timestamp;
	}
	public void setInserted_timestamp(String inserted_timestamp) {
		this.inserted_timestamp = inserted_timestamp;
	}
	public String getTest_id() {
		return test_id;
	}
	public void setTest_id(String test_id) {
		this.test_id = test_id;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getReq_uri() {
		return req_uri;
	}
	public void setReq_uri(String req_uri) {
		this.req_uri = req_uri;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String get__source() {
		return __source;
	}
	public void set__source(String __source) {
		this.__source = __source;
	}
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getReferer_uri() {
		return referer_uri;
	}
	public void setReferer_uri(String referer_uri) {
		this.referer_uri = referer_uri;
	}
	public String getWeb_path() {
		return web_path;
	}
	public void setWeb_path(String web_path) {
		this.web_path = web_path;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getMessage_hash_value() {
		return message_hash_value;
	}
	public void setMessage_hash_value(String message_hash_value) {
		this.message_hash_value = message_hash_value;
	}
	public String getObject_type() {
		return Object_type;
	}
	public void setObject_type(String object_type) {
		Object_type = object_type;
	}
	public String getFile_Size() {
		return File_Size;
	}
	public void setFile_Size(String file_Size) {
		File_Size = file_Size;
	}
	
	@Override
	public String toString() {
		return "Source: " + Activity_type + "," + user_agent;
		
	}
	
}
