package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;


public class Entry {

	@JsonProperty("type")
	private String type;
	@JsonProperty("id")
	private String id;
	@JsonProperty("sequence_id")
	private Object sequenceId;
	@JsonProperty("etag")
	private Object etag;
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
	public Object getSequenceId() {
		return sequenceId;
	}

	/**
	 *
	 * @param sequenceId
	 * The sequence_id
	 */
	@JsonProperty("sequence_id")
	public void setSequenceId(Object sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 *
	 * @return
	 * The etag
	 */
	@JsonProperty("etag")
	public Object getEtag() {
		return etag;
	}

	/**
	 *
	 * @param etag
	 * The etag
	 */
	@JsonProperty("etag")
	public void setEtag(Object etag) {
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	

}