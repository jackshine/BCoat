package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class VLObjects {

	@JsonProperty("ciq_profiles")
	private List<CiqProfile> ciqProfiles = new ArrayList<CiqProfile>();
	@JsonProperty("vl_types")
	private List<VlType> vlTypes = new ArrayList<VlType>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The ciqProfiles
	 */
	@JsonProperty("ciq_profiles")
	public List<CiqProfile> getCiqProfiles() {
		return ciqProfiles;
	}

	/**
	 * 
	 * @param ciqProfiles
	 * The ciq_profiles
	 */
	@JsonProperty("ciq_profiles")
	public void setCiqProfiles(List<CiqProfile> ciqProfiles) {
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



	public int getVulnerabilityCount(String vulnerability) {
		int count = 0;
		for ( VlType vltype : this.getVlTypes()) {
			if(vltype.getId().equalsIgnoreCase(vulnerability)) {
				count =  vltype.getTotal();
			}
		}
		return count;
	}

	public int getCIQCount(String ciq) {
		int count = 0;
		for ( CiqProfile ciqtype : this.getCiqProfiles()) {
			if(ciqtype.getId().equalsIgnoreCase(ciq)) {
				count =  ciqtype.getTotal();
			}
		}
		return count;
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