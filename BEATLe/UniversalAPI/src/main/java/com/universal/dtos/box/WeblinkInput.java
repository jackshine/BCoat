package com.universal.dtos.box;

import org.codehaus.jackson.annotate.JsonProperty;

public class WeblinkInput {

	@JsonProperty("url")
	private String url;
	@JsonProperty("parent")
	private Parent parent;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	

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
	 * The parent
	 */
	@JsonProperty("parent")
	public Parent getParent() {
		return parent;
	}

	/**
	 *
	 * @param parent
	 * The parent
	 */
	@JsonProperty("parent")
	public void setParent(Parent parent) {
		this.parent = parent;
	}

	/**
	 *
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 *
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	
}