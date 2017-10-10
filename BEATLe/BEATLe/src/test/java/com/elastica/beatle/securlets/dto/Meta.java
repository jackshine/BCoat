package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;


public class Meta {

	@JsonProperty("limit")
	private int limit;
	@JsonProperty("next")
	private String next;
	@JsonProperty("offset")
	private int offset;
	@JsonProperty("previous")
	private String previous;
	@JsonProperty("total_count")
	private int totalCount;
	
	/**
	 *
	 * @return
	 * The limit
	 */
	@JsonProperty("limit")
	public int getLimit() {
		return limit;
	}

	/**
	 *
	 * @param limit
	 * The limit
	 */
	@JsonProperty("limit")
	public void setLimit(int limit) {
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
	public void setNext(String next) {
		this.next = next;
	}

	/**
	 *
	 * @return
	 * The offset
	 */
	@JsonProperty("offset")
	public int getOffset() {
		return offset;
	}

	/**
	 *
	 * @param offset
	 * The offset
	 */
	@JsonProperty("offset")
	public void setOffset(int offset) {
		this.offset = offset;
	}

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
	public void setPrevious(String previous) {
		this.previous = previous;
	}

	/**
	 *
	 * @return
	 * The totalCount
	 */
	@JsonProperty("total_count")
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 *
	 * @param totalCount
	 * The total_count
	 */
	@JsonProperty("total_count")
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	

}