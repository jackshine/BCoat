package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ProfileRecords {

	@JsonProperty("totalSize")
	private Integer totalSize;
	@JsonProperty("done")
	private Boolean done;
	@JsonProperty("records")
	private List<Record> records = new ArrayList<Record>();

	/**
	 * 
	 * @return
	 * The totalSize
	 */
	@JsonProperty("totalSize")
	public Integer getTotalSize() {
		return totalSize;
	}

	/**
	 * 
	 * @param totalSize
	 * The totalSize
	 */
	@JsonProperty("totalSize")
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * 
	 * @return
	 * The done
	 */
	@JsonProperty("done")
	public Boolean getDone() {
		return done;
	}

	/**
	 * 
	 * @param done
	 * The done
	 */
	@JsonProperty("done")
	public void setDone(Boolean done) {
		this.done = done;
	}

	/**
	 * 
	 * @return
	 * The records
	 */
	@JsonProperty("records")
	public List<Record> getRecords() {
		return records;
	}

	/**
	 * 
	 * @param records
	 * The records
	 */
	@JsonProperty("records")
	public void setRecords(List<Record> records) {
		this.records = records;
	}

}

