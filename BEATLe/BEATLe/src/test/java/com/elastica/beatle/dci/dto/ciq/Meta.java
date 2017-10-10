package com.elastica.beatle.dci.dto.ciq;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Meta {

	@JsonProperty("previous")
	private java.lang.Object previous;
	@JsonProperty("total_count")
	private Integer totalCount;
	@JsonProperty("offset")
	private Integer offset;
	@JsonProperty("limit")
	private Integer limit;
	@JsonProperty("next")
	private java.lang.Object next;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The previous
	 */
	@JsonProperty("previous")
	public java.lang.Object getPrevious() {
		return previous;
	}

	/**
	 * 
	 * @param previous
	 * The previous
	 */
	@JsonProperty("previous")
	public void setPrevious(java.lang.Object previous) {
		this.previous = previous;
	}

	/**
	 * 
	 * @return
	 * The totalCount
	 */
	@JsonProperty("total_count")
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * @param totalCount
	 * The total_count
	 */
	@JsonProperty("total_count")
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 
	 * @return
	 * The offset
	 */
	@JsonProperty("offset")
	public Integer getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 * The offset
	 */
	@JsonProperty("offset")
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 * The limit
	 */
	@JsonProperty("limit")
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 * The limit
	 */
	@JsonProperty("limit")
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return
	 * The next
	 */
	@JsonProperty("next")
	public java.lang.Object getNext() {
		return next;
	}

	/**
	 * 
	 * @param next
	 * The next
	 */
	@JsonProperty("next")
	public void setNext(java.lang.Object next) {
		this.next = next;
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

