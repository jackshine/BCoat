package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BoxRemediation {

	@JsonProperty("db_name")
	private String dbName;
	@JsonProperty("user")
	private String user;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("doc_id")
	private String docId;
	@JsonProperty("doc_type")
	private String docType;
	@JsonProperty("instance")
	private String instance;
	

	@JsonProperty("actions")
	private List<BoxAction> actions = new ArrayList<BoxAction>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The dbName
	 */
	@JsonProperty("db_name")
	public String getDbName() {
		return dbName;
	}

	/**
	 * 
	 * @param dbName
	 * The db_name
	 */
	@JsonProperty("db_name")
	public void setDbName(String dbName) {
		this.dbName = dbName;
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
	 * The userId
	 */
	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 * The user_id
	 */
	@JsonProperty("user_id")
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return
	 * The docId
	 */
	@JsonProperty("doc_id")
	public String getDocId() {
		return docId;
	}

	/**
	 * @return the instance
	 */
	public String getInstance() {
		return instance;
	}

	/**
	 * @param instance the instance to set
	 */
	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	/**
	 * 
	 * @param docId
	 * The doc_id
	 */
	@JsonProperty("doc_id")
	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * 
	 * @return
	 * The docType
	 */
	@JsonProperty("doc_type")
	public String getDocType() {
		return docType;
	}

	/**
	 * 
	 * @param docType
	 * The doc_type
	 */
	@JsonProperty("doc_type")
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * 
	 * @return
	 * The actions
	 */
	@JsonProperty("actions")
	public List<BoxAction> getActions() {
		return actions;
	}

	/**
	 * 
	 * @param actions
	 * The actions
	 */
	@JsonProperty("actions")
	public void setActions(List<BoxAction> actions) {
		this.actions = actions;
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
