package com.elastica.beatle.dci.dto.notification;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Object {

	@JsonProperty("url")
	private String url;
	@JsonProperty("email")
	private String email;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("details")
	private String details;
	@JsonProperty("action")
	private String action;
	@JsonProperty("seen")
	private boolean seen;
	@JsonProperty("id")
	private String id;
	@JsonProperty("subject")
	private String subject;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The url
	 */
	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	/**
	 * 
	 * @param url
	 * The url
	 */
	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * @return
	 * The email
	 */
	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 * The email
	 */
	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return
	 * The createdOn
	 */
	@JsonProperty("created_on")
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * 
	 * @param createdOn
	 * The created_on
	 */
	@JsonProperty("created_on")
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return
	 * The details
	 */
	@JsonProperty("details")
	public String getDetails() {
		return details;
	}

	/**
	 * 
	 * @param details
	 * The details
	 */
	@JsonProperty("details")
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * 
	 * @return
	 * The action
	 */
	@JsonProperty("action")
	public String getAction() {
		return action;
	}

	/**
	 * 
	 * @param action
	 * The action
	 */
	@JsonProperty("action")
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * 
	 * @return
	 * The seen
	 */
	@JsonProperty("seen")
	public boolean getSeen() {
		return seen;
	}

	/**
	 * 
	 * @param seen
	 * The seen
	 */
	@JsonProperty("seen")
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The subject
	 */
	@JsonProperty("subject")
	public String getSubject() {
		return subject;
	}

	/**
	 * 
	 * @param subject
	 * The subject
	 */
	@JsonProperty("subject")
	public void setSubject(String subject) {
		this.subject = subject;
	}

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}