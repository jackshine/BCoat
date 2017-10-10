package com.universal.dtos.salesforce;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Share {

	@JsonProperty("entity")
	private Entity entity;
	@JsonProperty("sharingType")
	private String sharingType;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The entity
	 */
	@JsonProperty("entity")
	public Entity getEntity() {
		return entity;
	}

	/**
	 * 
	 * @param entity
	 * The entity
	 */
	@JsonProperty("entity")
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * 
	 * @return
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public String getSharingType() {
		return sharingType;
	}

	/**
	 * 
	 * @param sharingType
	 * The sharingType
	 */
	@JsonProperty("sharingType")
	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
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
