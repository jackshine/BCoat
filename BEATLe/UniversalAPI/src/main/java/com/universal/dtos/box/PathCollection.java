package com.universal.dtos.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class PathCollection {

	@JsonProperty("total_count")
	private Long totalCount;
	@JsonProperty("entries")
	private List<Object> entries = new ArrayList<Object>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The totalCount
	 */
	@JsonProperty("total_count")
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * @param totalCount
	 * The total_count
	 */
	@JsonProperty("total_count")
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 
	 * @return
	 * The entries
	 */
	@JsonProperty("entries")
	public List<Object> getEntries() {
		return entries;
	}

	/**
	 * 
	 * @param entries
	 * The entries
	 */
	@JsonProperty("entries")
	public void setEntries(List<Object> entries) {
		this.entries = entries;
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