package com.elastica.beatle.dci.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Violation {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("keyword")
	private String keyword;
	@JsonProperty("dictionary")
	private String dictionary;
	@JsonProperty("predefined_term")
	private String predefinedTerm;
	@JsonProperty("predefined_dictionary")
	private String predefinedDictionary;
	@JsonProperty("regex")
	private String regex;
	@JsonProperty("training_profile")
	private List<String> trainingProfile;
	@JsonProperty("risk_type")
	private List<String> riskType;
	@JsonProperty("content_type")
	private List<String> contentType;
	@JsonProperty("file_format")
	private String fileFormat;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The count
	 */
	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 * The count
	 */
	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * 
	 * @return
	 * The keyword
	 */
	@JsonProperty("keyword")
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 
	 * @param keyword
	 * The keyword
	 */
	@JsonProperty("keyword")
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * 
	 * @return
	 * The dictionary
	 */
	@JsonProperty("dictionary")
	public String getDictionary() {
		return dictionary;
	}

	/**
	 * 
	 * @param dictionary
	 * The dictionary
	 */
	@JsonProperty("dictionary")
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
	
	/**
	 * 
	 * @param predefinedDictionary
	 * The predefinedDictionary
	 */
	@JsonProperty("predefined_dictionary")
	public void setPredefinedDictionary(String predefinedDictionary) {
		this.predefinedDictionary = predefinedDictionary;
	}
	
	/**
	 * 
	 * @return
	 * The predefinedDictionary
	 */
	@JsonProperty("predefined_dictionary")
	public String getPredefinedDictionary() {
		return predefinedDictionary;
	}

	/**
	 * 
	 * @return
	 * The predefinedTerm
	 */
	@JsonProperty("predefined_term")
	public String getPredefinedTerm() {
		return predefinedTerm;
	}

	/**
	 * 
	 * @param predefinedTerm
	 * The predefined_term
	 */
	@JsonProperty("predefined_term")
	public void setPredefinedTerm(String predefinedTerm) {
		this.predefinedTerm = predefinedTerm;
	}

	/**
	 * 
	 * @return
	 * The regex
	 */
	@JsonProperty("regex")
	public String getRegex() {
		return regex;
	}

	/**
	 * 
	 * @param regex
	 * The regex
	 */
	@JsonProperty("regex")
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	/**
	 * 
	 * @return
	 * The training_profile
	 */
	@JsonProperty("training_profile")
	public List<String> getTrainingProfile() {
		return trainingProfile;
	}

	/**
	 * 
	 * @param training_profile
	 * The training_profile
	 */
	@JsonProperty("training_profile")
	public void setTrainingProfile(List<String> trainingProfile) {
		this.trainingProfile = trainingProfile;
	}

	/**
	 * 
	 * @return
	 */
	@JsonProperty("risk_type")
	public List<String> getRiskType() {
		return riskType;
	}

	/**
	 * @param riskType the riskType to set
	 */
	@JsonProperty("risk_type")
	public void setRiskType(List<String> riskType) {
		this.riskType = riskType;
	}

	/**
	 * @return the contentType
	 */
	@JsonProperty("content_type")
	public List<String> getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	@JsonProperty("content_type")
	public void setContentType(List<String> contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * @return the fileFormat
	 */
	@JsonProperty("file_format")
	public String getFileFormat() {
		return fileFormat;
	}

	/**
	 * @param fileFormat the fileFormat to set
	 */
	@JsonProperty("file_format")
	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
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