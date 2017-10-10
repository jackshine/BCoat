package com.universal.unifiedapi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class ProfileFieldsConfig {

	@JsonProperty("enable_job_title")
	private Boolean enableJobTitle;
	@JsonProperty("enable_work_phone")
	private Boolean enableWorkPhone;
	@JsonProperty("enable_mobile_phone")
	private Boolean enableMobilePhone;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The enableJobTitle
	 */
	@JsonProperty("enable_job_title")
	public Boolean getEnableJobTitle() {
		return enableJobTitle;
	}

	/**
	 * 
	 * @param enableJobTitle
	 * The enable_job_title
	 */
	@JsonProperty("enable_job_title")
	public void setEnableJobTitle(Boolean enableJobTitle) {
		this.enableJobTitle = enableJobTitle;
	}

	/**
	 * 
	 * @return
	 * The enableWorkPhone
	 */
	@JsonProperty("enable_work_phone")
	public Boolean getEnableWorkPhone() {
		return enableWorkPhone;
	}

	/**
	 * 
	 * @param enableWorkPhone
	 * The enable_work_phone
	 */
	@JsonProperty("enable_work_phone")
	public void setEnableWorkPhone(Boolean enableWorkPhone) {
		this.enableWorkPhone = enableWorkPhone;
	}

	/**
	 * 
	 * @return
	 * The enableMobilePhone
	 */
	@JsonProperty("enable_mobile_phone")
	public Boolean getEnableMobilePhone() {
		return enableMobilePhone;
	}

	/**
	 * 
	 * @param enableMobilePhone
	 * The enable_mobile_phone
	 */
	@JsonProperty("enable_mobile_phone")
	public void setEnableMobilePhone(Boolean enableMobilePhone) {
		this.enableMobilePhone = enableMobilePhone;
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
