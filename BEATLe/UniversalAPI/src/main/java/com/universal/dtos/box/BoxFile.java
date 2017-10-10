package com.universal.dtos.box;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class BoxFile {

	private Long totalCount;
	private List<FileEntry> entries = new ArrayList<FileEntry>();
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The totalCount
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * @param totalCount
	 * The total_count
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public BoxFile withTotalCount(Long totalCount) {
		this.totalCount = totalCount;
		return this;
	}

	/**
	 * 
	 * @return
	 * The entries
	 */
	public List<FileEntry> getEntries() {
		return entries;
	}

	/**
	 * 
	 * @param entries
	 * The entries
	 */
	public void setEntries(List<FileEntry> entries) {
		this.entries = entries;
	}

	public BoxFile withEntries(List<FileEntry> entries) {
		this.entries = entries;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public BoxFile withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
