package com.universal.dtos.box;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class GroupInput {

	@JsonProperty("name")
	private String name;
	@JsonProperty("provenance")
	private String provenance;
	@JsonProperty("external_sync_identifier")
	private String externalSyncIdentifier;
	@JsonProperty("description")
	private String description;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	/**
	 *
	 * @return
	 * The provenance
	 */
	@JsonProperty("provenance")
	public String getProvenance() {
		return provenance;
	}

	/**
	 *
	 * @param provenance
	 * The provenance
	 */
	@JsonProperty("provenance")
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	/**
	 *
	 * @return
	 * The externalSyncIdentifier
	 */
	@JsonProperty("external_sync_identifier")
	public String getExternalSyncIdentifier() {
		return externalSyncIdentifier;
	}

	/**
	 *
	 * @param externalSyncIdentifier
	 * The external_sync_identifier
	 */
	@JsonProperty("external_sync_identifier")
	public void setExternalSyncIdentifier(String externalSyncIdentifier) {
		this.externalSyncIdentifier = externalSyncIdentifier;
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

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}