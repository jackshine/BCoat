package com.universal.dtos.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class UserCollection {

	@JsonProperty("total_count")
	private Long totalCount;
	@JsonProperty("entries")
	private List<BoxUserInfo> entries = new ArrayList<BoxUserInfo>();
	@JsonProperty("offset")
	private Long offset;
	@JsonProperty("limit")
	private Long limit;
	@JsonProperty("order")
	private List<Order> order = new ArrayList<Order>();
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
	public List<BoxUserInfo> getEntries() {
		return entries;
	}

	/**
	 * 
	 * @param entries
	 * The entries
	 */
	@JsonProperty("entries")
	public void setEntries(List<BoxUserInfo> entries) {
		this.entries = entries;
	}

	/**
	 * 
	 * @return
	 * The offset
	 */
	@JsonProperty("offset")
	public Long getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 * The offset
	 */
	@JsonProperty("offset")
	public void setOffset(Long offset) {
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 * The limit
	 */
	@JsonProperty("limit")
	public Long getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 * The limit
	 */
	@JsonProperty("limit")
	public void setLimit(Long limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return
	 * The order
	 */
	@JsonProperty("order")
	public List<Order> getOrder() {
		return order;
	}

	/**
	 * 
	 * @param order
	 * The order
	 */
	@JsonProperty("order")
	public void setOrder(List<Order> order) {
		this.order = order;
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
