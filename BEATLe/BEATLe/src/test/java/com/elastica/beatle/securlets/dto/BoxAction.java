package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;


@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class BoxAction {

	@JsonProperty("code")
	private String code;
	@JsonProperty("codeName")
	private String codeName;
	@JsonProperty("possible_values")
	private List<String> possibleValues = new ArrayList<String>();
	@JsonProperty("meta_info")
	private BoxMetaInfo metaInfo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The code
	 */
	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 * The code
	 */
	@JsonProperty("code")
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return the codeName
	 */
	public String getCodeName() {
		return codeName;
	}

	/**
	 * @param codeName the codeName to set
	 */
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}	
	

	/**
	 * 
	 * @return
	 * The possibleValues
	 */
	@JsonProperty("possible_values")
	public List<String> getPossibleValues() {
		return possibleValues;
	}

	/**
	 * 
	 * @param possibleValues
	 * The possible_values
	 */
	@JsonProperty("possible_values")
	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

	/**
	 * 
	 * @return
	 * The metaInfo
	 */
	@JsonProperty("meta_info")
	public BoxMetaInfo getMetaInfo() {
		return metaInfo;
	}

	/**
	 * 
	 * @param metaInfo
	 * The meta_info
	 */
	@JsonProperty("meta_info")
	public void setMetaInfo(BoxMetaInfo metaInfo) {
		this.metaInfo = metaInfo;
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
