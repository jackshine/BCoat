package com.elastica.beatle.AuditSummaryDataObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "meta", "objects" })
public class AuditSummary {

	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("objects")
	private List<com.elastica.beatle.AuditSummaryDataObjects.Object> objects = new ArrayList<com.elastica.beatle.AuditSummaryDataObjects.Object>();
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The meta
	 */
	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	/**
	 * 
	 * @param meta
	 *            The meta
	 */
	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 * 
	 * @return The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.AuditSummaryDataObjects.Object> getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param objects
	 *            The objects
	 */
	@JsonProperty("objects")
	public void setObjects(
			List<com.elastica.beatle.AuditSummaryDataObjects.Object> objects) {
		this.objects = objects;
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