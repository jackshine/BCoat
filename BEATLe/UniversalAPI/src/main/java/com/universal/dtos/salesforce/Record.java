package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Record {

	@JsonProperty("attributes")
	private Attributes attributes;
	@JsonProperty("Id")
	private String Id;
	@JsonProperty("Name")
	private String Name;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * 
	 * @return
	 * The attributes
	 */
	@JsonProperty("attributes")
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributes
	 * The attributes
	 */
	@JsonProperty("attributes")
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	/**
	 * 
	 * @return
	 * The Id
	 */
	@JsonProperty("Id")
	public String getId() {
		return Id;
	}

	/**
	 * 
	 * @param Id
	 * The Id
	 */
	@JsonProperty("Id")
	public void setId(String Id) {
		this.Id = Id;
	}

}
