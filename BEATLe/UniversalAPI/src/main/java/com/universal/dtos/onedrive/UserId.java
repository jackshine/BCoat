package com.universal.dtos.onedrive;
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


public class UserId {

	@JsonProperty("NameId")
	private String NameId;
	@JsonProperty("NameIdIssuer")
	private String NameIdIssuer;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The NameId
	 */
	@JsonProperty("NameId")
	public String getNameId() {
		return NameId;
	}

	/**
	 * 
	 * @param NameId
	 * The NameId
	 */
	@JsonProperty("NameId")
	public void setNameId(String NameId) {
		this.NameId = NameId;
	}

	/**
	 * 
	 * @return
	 * The NameIdIssuer
	 */
	@JsonProperty("NameIdIssuer")
	public String getNameIdIssuer() {
		return NameIdIssuer;
	}

	/**
	 * 
	 * @param NameIdIssuer
	 * The NameIdIssuer
	 */
	@JsonProperty("NameIdIssuer")
	public void setNameIdIssuer(String NameIdIssuer) {
		this.NameIdIssuer = NameIdIssuer;
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
