package com.elastica.beatle.securlets.dto;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class TotalObject {

	@JsonProperty("_id")
	private String Id;
	@JsonProperty("total")
	private int total;
	@JsonProperty("count")
	private int count;
	@JsonProperty("term")
	private String term;
	
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 *
	 * @return
	 * The Id
	 */
	@JsonProperty("_id")
	public String getId() {
		return Id;
	}

	/**
	 *
	 * @param Id
	 * The _id
	 */
	@JsonProperty("_id")
	public void setId(String Id) {
		this.Id = Id;
	}

	/**
	 *
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public Integer getTotal() {
		return total;
	}

	/**
	 *
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
	/**
	 * @return the count
	 */
	@JsonProperty("count")
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	@JsonProperty("count")
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return the term
	 */
	@JsonProperty("term")
	public String getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	@JsonProperty("term")
	public void setTerm(String term) {
		this.term = term;
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