package com.elastica.beatle.dci.dto.fileType;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Result {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("term")
	private String term;
	@JsonProperty("total")
	private Integer total;
	@JsonProperty("_id")
	private String id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The count
	 */
	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 * The count
	 */
	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * 
	 * @return
	 * The term
	 */
	@JsonProperty("term")
	public String getTerm() {
		return term;
	}

	/**
	 * 
	 * @param term
	 * The term
	 */
	@JsonProperty("term")
	public void setTerm(String term) {
		this.term = term;
	}

	/**
	 * 
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public Integer getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(Integer total) {
		this.total = total;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("_id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The _id
	 */
	@JsonProperty("_id")
	public void setId(String id) {
		this.id = id;
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