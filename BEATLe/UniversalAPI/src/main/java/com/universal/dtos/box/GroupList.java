package com.universal.dtos.box;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class GroupList {

	@JsonProperty("total_count")
	private Integer totalCount;
	@JsonProperty("entries")
	private List<BoxGroup> entries = new ArrayList<BoxGroup>();
	@JsonProperty("limit")
	private Integer limit;
	@JsonProperty("offset")
	private Integer offset;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The entries
	 */
	@JsonProperty("entries")
	public List<BoxGroup> getEntries() {
		return entries;
	}

	/**
	 * 
	 * @param entries
	 * The entries
	 */
	@JsonProperty("entries")
	public void setEntries(List<BoxGroup> entries) {
		this.entries = entries;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
