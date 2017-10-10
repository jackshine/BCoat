package com.elastica.beatle.dci.dto.contentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.elastica.beatle.dci.dto.vulnerability.CiqProfile;


public class Results {

	@JsonProperty("total")
	private Integer total;
	@JsonProperty("_id")
	private String Id;
	@JsonProperty("ciq_profiles")
	private List<CiqProfile> ciqProfiles = new ArrayList<CiqProfile>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public Integer getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(Integer total) {
		this.total = total;
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
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}