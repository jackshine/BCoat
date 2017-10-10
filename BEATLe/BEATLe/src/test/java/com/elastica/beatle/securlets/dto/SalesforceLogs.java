package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesforceLogs {

	@JsonProperty("hits")
	private SalesforceHits hits;
	@JsonProperty("ts")
	private String ts;
	@JsonProperty("_shards")
	private com.elastica.beatle.securlets.dto.Shards Shards;
	@JsonProperty("timed_out")
	private Boolean timedOut;
	@JsonProperty("action_status")
	private String actionStatus;
	@JsonProperty("took")
	private Long took;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hits
	 */
	@JsonProperty("hits")
	public SalesforceHits getHits() {
		return hits;
	}

	/**
	 * 
	 * @param hits
	 * The hits
	 */
	@JsonProperty("hits")
	public void setHits(SalesforceHits hits) {
		this.hits = hits;
	}

	/**
	 * 
	 * @return
	 * The ts
	 */
	@JsonProperty("ts")
	public String getTs() {
		return ts;
	}

	/**
	 * 
	 * @param ts
	 * The ts
	 */
	@JsonProperty("ts")
	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	 * 
	 * @return
	 * The Shards
	 */
	@JsonProperty("_shards")
	public com.elastica.beatle.securlets.dto.Shards getShards() {
		return Shards;
	}

	/**
	 * 
	 * @param Shards
	 * The _shards
	 */
	@JsonProperty("_shards")
	public void setShards(com.elastica.beatle.securlets.dto.Shards Shards) {
		this.Shards = Shards;
	}

	/**
	 * 
	 * @return
	 * The timedOut
	 */
	@JsonProperty("timed_out")
	public Boolean getTimedOut() {
		return timedOut;
	}

	/**
	 * 
	 * @param timedOut
	 * The timed_out
	 */
	@JsonProperty("timed_out")
	public void setTimedOut(Boolean timedOut) {
		this.timedOut = timedOut;
	}

	/**
	 * 
	 * @return
	 * The actionStatus
	 */
	@JsonProperty("action_status")
	public String getActionStatus() {
		return actionStatus;
	}

	/**
	 * 
	 * @param actionStatus
	 * The action_status
	 */
	@JsonProperty("action_status")
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}

	/**
	 * 
	 * @return
	 * The took
	 */
	@JsonProperty("took")
	public Long getTook() {
		return took;
	}

	/**
	 * 
	 * @param took
	 * The took
	 */
	@JsonProperty("took")
	public void setTook(Long took) {
		this.took = took;
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