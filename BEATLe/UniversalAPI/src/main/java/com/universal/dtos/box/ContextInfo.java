package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;



@JsonPropertyOrder({
	"conflicts"
})
public class ContextInfo {

	@JsonProperty("conflicts")
	private Conflicts conflicts;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The conflicts
	 */
	@JsonProperty("conflicts")
	public Conflicts getConflicts() {
		return conflicts;
	}

	/**
	 * 
	 * @param conflicts
	 * The conflicts
	 */
	@JsonProperty("conflicts")
	public void setConflicts(Conflicts conflicts) {
		this.conflicts = conflicts;
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
