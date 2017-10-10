package com.elastica.beatle.dci.dto.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Object {

	@JsonProperty("count")
	private Integer count;
	@JsonProperty("modified_by")
	private String modifiedBy;
	@JsonProperty("description")
	private String description;
	@JsonProperty("operands")
	private List<java.lang.Object> operands = new ArrayList<java.lang.Object>();
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("threshold")
	private Integer threshold;
	@JsonProperty("created_on")
	private String createdOn;
	@JsonProperty("version")
	private Integer version;
	@JsonProperty("modified_on")
	private String modifiedOn;
	@JsonProperty("operator")
	private String operator;
	@JsonProperty("dictionary_type")
	private String dictionaryType;
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

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
	 * The modifiedBy
	 */
	@JsonProperty("modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * 
	 * @param modifiedBy
	 * The modified_by
	 */
	@JsonProperty("modified_by")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * 
	 * @return
	 * The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 * The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 * The operands
	 */
	@JsonProperty("operands")
	public List<java.lang.Object> getOperands() {
		return operands;
	}

	/**
	 * 
	 * @param operands
	 * The operands
	 */
	@JsonProperty("operands")
	public void setOperands(List<java.lang.Object> operands) {
		this.operands = operands;
	}

	/**
	 * 
	 * @return
	 * The createdBy
	 */
	@JsonProperty("created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 * The created_by
	 */
	@JsonProperty("created_by")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * 
	 * @return
	 * The threshold
	 */
	@JsonProperty("threshold")
	public Integer getThreshold() {
		return threshold;
	}

	/**
	 * 
	 * @param threshold
	 * The threshold
	 */
	@JsonProperty("threshold")
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	/**
	 * 
	 * @return
	 * The createdOn
	 */
	@JsonProperty("created_on")
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * 
	 * @param createdOn
	 * The created_on
	 */
	@JsonProperty("created_on")
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return
	 * The version
	 */
	@JsonProperty("version")
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 * The version
	 */
	@JsonProperty("version")
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 
	 * @return
	 * The modifiedOn
	 */
	@JsonProperty("modified_on")
	public String getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * 
	 * @param modifiedOn
	 * The modified_on
	 */
	@JsonProperty("modified_on")
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * 
	 * @return
	 * The operator
	 */
	@JsonProperty("operator")
	public String getOperator() {
		return operator;
	}

	/**
	 * 
	 * @param operator
	 * The operator
	 */
	@JsonProperty("operator")
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 
	 * @return
	 * The dictionaryType
	 */
	@JsonProperty("dictionary_type")
	public String getDictionaryType() {
		return dictionaryType;
	}

	/**
	 * 
	 * @param dictionaryType
	 * The dictionary_type
	 */
	@JsonProperty("dictionary_type")
	public void setDictionaryType(String dictionaryType) {
		this.dictionaryType = dictionaryType;
	}

	/**
	 * 
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 * The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 * The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
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