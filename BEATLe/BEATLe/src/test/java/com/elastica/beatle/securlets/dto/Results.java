package com.elastica.beatle.securlets.dto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class Results {

	@JsonProperty("vl_types")
	private List<VlType> vlTypes = new ArrayList<VlType>();
	
	@JsonProperty("ciq_profiles")
	private List<VlType> ciqProfiles = new ArrayList<VlType>();

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	
	/**
	 * @return the ciqProfiles
	 */
	public List<VlType> getCiqProfiles() {
		return ciqProfiles;
	}

	/**
	 * @param ciqProfiles the ciqProfiles to set
	 */
	public void setCiqProfiles(List<VlType> ciqProfiles) {
		this.ciqProfiles = ciqProfiles;
	}
	
	
	/**
	 * 
	 * @return
	 * The vlTypes
	 */
	@JsonProperty("vl_types")
	public List<VlType> getVlTypes() {
		return vlTypes;
	}

	/**
	 * 
	 * @param vlTypes
	 * The vl_types
	 */
	@JsonProperty("vl_types")
	public void setVlTypes(List<VlType> vlTypes) {
		this.vlTypes = vlTypes;
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
