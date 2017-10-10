package com.elastica.beatle.dci.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class Hits {

	@JsonProperty("hits")
	private List<Hit> hits = new ArrayList<Hit>();
	@JsonProperty("total")
	private int total;
	@JsonProperty("max_score")
	private float maxScore;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The hits
	 */
	@JsonProperty("hits")
	public List<Hit> getHits() {
		return hits;
	}

	/**
	 * 
	 * @param hits
	 * The hits
	 */
	@JsonProperty("hits")
	public void setHits(List<Hit> hits) {
		this.hits = hits;
	}

	/**
	 * 
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public int getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * 
	 * @return
	 * The maxScore
	 */
	@JsonProperty("max_score")
	public float getMaxScore() {
		return maxScore;
	}

	/**
	 * 
	 * @param maxScore
	 * The max_score
	 */
	@JsonProperty("max_score")
	public void setMaxScore(float maxScore) {
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