package com.elastica.beatle.gateway.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class BoxGWHit {

	@JsonProperty("sort")
	private List<Long> sort = new ArrayList<Long>();
	@JsonProperty("_type")
	private String Type;
	@JsonProperty("_index")
	private String Index;
	@JsonProperty("_score")
	private Object Score;
	@JsonProperty("_source")
	private BoxGWSource Source;
	@JsonProperty("_id")
	private String Id;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * The Score
	 */
	@JsonProperty("_score")
	public Object getScore() {
		return Score;
	}

	/**
	 * 
	 * @param Score
	 * The _score
	 */
	@JsonProperty("_score")
	public void setScore(Object Score) {
		this.Score = Score;
	}

	/**
	 * 
	 * @return
	 * The Source
	 */
	@JsonProperty("_source")
	public BoxGWSource getSource() {
		return Source;
	}

	/**
	 * 
	 * @param Source
	 * The _source
	 */
	@JsonProperty("_source")
	public void setSource(BoxGWSource Source) {
		this.Source = Source;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}