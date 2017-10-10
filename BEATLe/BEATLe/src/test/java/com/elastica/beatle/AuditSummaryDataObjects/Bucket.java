package com.elastica.beatle.AuditSummaryDataObjects;

import java.util.HashMap;
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
@JsonPropertyOrder({ "bucket_id", "score" })
public class Bucket {

	@JsonProperty("bucket_id")
	private Integer bucketId;
	@JsonProperty("score")
	private Integer score;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The bucketId
	 */
	@JsonProperty("bucket_id")
	public Integer getBucketId() {
		return bucketId;
	}

	/**
	 * 
	 * @param bucketId
	 *            The bucket_id
	 */
	@JsonProperty("bucket_id")
	public void setBucketId(Integer bucketId) {
		this.bucketId = bucketId;
	}

	/**
	 * 
	 * @return The score
	 */
	@JsonProperty("score")
	public Integer getScore() {
		return score;
	}

	/**
	 * 
	 * @param score
	 *            The score
	 */
	@JsonProperty("score")
	public void setScore(Integer score) {
		this.score = score;
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