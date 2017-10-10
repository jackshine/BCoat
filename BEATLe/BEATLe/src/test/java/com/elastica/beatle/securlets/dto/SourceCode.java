package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class SourceCode {

	@JsonProperty("updated_timestamp")
	private String updatedTimestamp;

	/**
	 * @return the updatedTimestamp
	 */
	@JsonProperty("updated_timestamp")
	public String getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	/**
	 * @param updatedTimestamp the updatedTimestamp to set
	 */
	@JsonProperty("updated_timestamp")
	public void setUpdatedTimestamp(String updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}
}
