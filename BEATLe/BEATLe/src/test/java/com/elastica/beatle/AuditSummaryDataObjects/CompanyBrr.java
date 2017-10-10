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
@JsonPropertyOrder({ "buckets", "value" })
public class CompanyBrr {

	@JsonProperty("buckets")
	private List<Bucket> buckets = new ArrayList<Bucket>();
	@JsonProperty("value")
	private Integer value;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The buckets
	 */
	@JsonProperty("buckets")
	public List<Bucket> getBuckets() {
		return buckets;
	}

	/**
	 * 
	 * @param buckets
	 *            The buckets
	 */
	@JsonProperty("buckets")
	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}

	/**
	 * 
	 * @return The value
	 */
	@JsonProperty("value")
	public Integer getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            The value
	 */
	@JsonProperty("value")
	public void setValue(Integer value) {
		this.value = value;
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