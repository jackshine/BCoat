package com.elastica.beatle.dci.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Hit {

	@JsonProperty("_index")
	private String Index;
	@JsonProperty("_type")
	private String Type;
	@JsonProperty("_id")
	private String Id;
	@JsonProperty("_score")
	private String Score;
	@JsonProperty("_source")
	private Source Source;
	@JsonProperty("sort")
	private List<Long> sort = new ArrayList<Long>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Index
	 */
	@JsonProperty("_index")
	public String getIndex() {
		return Index;
	}

	/**
	 * 
	 * @param Index
	 * The _index
	 */
	@JsonProperty("_index")
	public void setIndex(String Index) {
		this.Index = Index;
	}

	/**
	 * 
	 * @return
	 * The Type
	 */
	@JsonProperty("_type")
	public String getType() {
		return Type;
	}

	/**
	 * 
	 * @param Type
	 * The _type
	 */
	@JsonProperty("_type")
	public void setType(String Type) {
		this.Type = Type;
	}

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
	 * The Score
	 */
	@JsonProperty("_score")
	public String getScore() {
		return Score;
	}

	/**
	 * 
	 * @param Score
	 * The _score
	 */
	@JsonProperty("_score")
	public void setScore(String Score) {
		this.Score = Score;
	}

	/**
	 * 
	 * @return
	 * The Source
	 */
	@JsonProperty("_source")
	public Source getSource() {
		return Source;
	}

	/**
	 * 
	 * @param Source
	 * The _source
	 */
	@JsonProperty("_source")
	public void setSource(Source Source) {
		this.Source = Source;
	}

	/**
	 * 
	 * @return
	 * The sort
	 */
	@JsonProperty("sort")
	public List<Long> getSort() {
		return sort;
	}

	/**
	 * 
	 * @param sort
	 * The sort
	 */
	@JsonProperty("sort")
	public void setSort(List<Long> sort) {
		this.sort = sort;
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
