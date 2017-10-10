package com.universal.dtos.salesforce;

import org.codehaus.jackson.annotate.JsonProperty;

public class Resources {


	@JsonProperty("sobjects")
	private String sobjects;
	@JsonProperty("licensing")
	private String licensing;
	@JsonProperty("connect")
	private String connect;
	@JsonProperty("search")
	private String search;
	@JsonProperty("query")
	private String query;
	@JsonProperty("tooling")
	private String tooling;
	@JsonProperty("chatter")
	private String chatter;
	@JsonProperty("recent")
	private String recent;

	/**
	 * 
	 * @return
	 * The sobjects
	 */
	@JsonProperty("sobjects")
	public String getSobjects() {
		return sobjects;
	}

	/**
	 * 
	 * @param sobjects
	 * The sobjects
	 */
	@JsonProperty("sobjects")
	public void setSobjects(String sobjects) {
		this.sobjects = sobjects;
	}

	/**
	 * 
	 * @return
	 * The licensing
	 */
	@JsonProperty("licensing")
	public String getLicensing() {
		return licensing;
	}

	/**
	 * 
	 * @param licensing
	 * The licensing
	 */
	@JsonProperty("licensing")
	public void setLicensing(String licensing) {
		this.licensing = licensing;
	}

	/**
	 * 
	 * @return
	 * The connect
	 */
	@JsonProperty("connect")
	public String getConnect() {
		return connect;
	}

	/**
	 * 
	 * @param connect
	 * The connect
	 */
	@JsonProperty("connect")
	public void setConnect(String connect) {
		this.connect = connect;
	}

	/**
	 * 
	 * @return
	 * The search
	 */
	@JsonProperty("search")
	public String getSearch() {
		return search;
	}

	/**
	 * 
	 * @param search
	 * The search
	 */
	@JsonProperty("search")
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * 
	 * @return
	 * The query
	 */
	@JsonProperty("query")
	public String getQuery() {
		return query;
	}

	/**
	 * 
	 * @param query
	 * The query
	 */
	@JsonProperty("query")
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * 
	 * @return
	 * The tooling
	 */
	@JsonProperty("tooling")
	public String getTooling() {
		return tooling;
	}

	/**
	 * 
	 * @param tooling
	 * The tooling
	 */
	@JsonProperty("tooling")
	public void setTooling(String tooling) {
		this.tooling = tooling;
	}

	/**
	 * 
	 * @return
	 * The chatter
	 */
	@JsonProperty("chatter")
	public String getChatter() {
		return chatter;
	}

	/**
	 * 
	 * @param chatter
	 * The chatter
	 */
	@JsonProperty("chatter")
	public void setChatter(String chatter) {
		this.chatter = chatter;
	}

	/**
	 * 
	 * @return
	 * The recent
	 */
	@JsonProperty("recent")
	public String getRecent() {
		return recent;
	}

	/**
	 * 
	 * @param recent
	 * The recent
	 */
	@JsonProperty("recent")
	public void setRecent(String recent) {
		this.recent = recent;
	}
}