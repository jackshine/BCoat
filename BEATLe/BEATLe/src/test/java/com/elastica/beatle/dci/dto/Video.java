package com.elastica.beatle.dci.dto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Video {

	@JsonProperty("reasons")
	private List<Reason> reasons = new ArrayList<Reason>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The reasons
	 */
	@JsonProperty("reasons")
	public List<Reason> getReasons() {
		return reasons;
	}

	/**
	 * 
	 * @param reasons
	 * The reasons
	 */
	@JsonProperty("reasons")
	public void setReasons(List<Reason> reasons) {
		this.reasons = reasons;
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