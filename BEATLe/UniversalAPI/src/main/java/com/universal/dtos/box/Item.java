package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class Item {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("sequence_id")
	private String sequenceId;
	@JsonProperty("etag")
	private String etag;
	@JsonProperty("name")
	private String name;
	

	/**
	 * 
	 * @return
	 * The type
	 */
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 * The type
	 */
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
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
	 * The sequenceId
	 */
	@JsonProperty("sequence_id")
	public String getSequenceId() {
		return sequenceId;
	}

	/**
	 * 
	 * @param sequenceId
	 * The sequence_id
	 */
	@JsonProperty("sequence_id")
	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * 
	 * @return
	 * The etag
	 */
	@JsonProperty("etag")
	public String getEtag() {
		return etag;
	}

	/**
	 * 
	 * @param etag
	 * The etag
	 */
	@JsonProperty("etag")
	public void setEtag(String etag) {
		this.etag = etag;
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

}