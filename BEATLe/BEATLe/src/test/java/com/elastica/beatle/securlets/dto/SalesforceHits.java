package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class SalesforceHits {

	@JsonProperty("hits")
	private List<SalesforceHit> hits = new ArrayList<SalesforceHit>();
	@JsonProperty("total")
	private Long total;
	@JsonProperty("max_score")
	private Double maxScore;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hits
	 */
	@JsonProperty("hits")
	public List<SalesforceHit> getHits() {
		return hits;
	}

	/**
	 * 
	 * @param hits
	 * The hits
	 */
	@JsonProperty("hits")
	public void setHits(List<SalesforceHit> hits) {
		this.hits = hits;
	}

	/**
	 * 
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public Long getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * 
	 * @return
	 * The maxScore
	 */
	@JsonProperty("max_score")
	public Double getMaxScore() {
		return maxScore;
	}

	/**
	 * 
	 * @param maxScore
	 * The max_score
	 */
	@JsonProperty("max_score")
	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
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