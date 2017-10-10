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
public class ApiScanPolicy {

	@JsonProperty("exclusion_rules")
	private List<ExclusionRule> exclusionRules = new ArrayList<ExclusionRule>();
	@JsonProperty("inclusion_rules")
	private List<InclusionRule> inclusionRules = new ArrayList<InclusionRule>();
	@JsonProperty("scan_all")
	private boolean scanAll;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	/**
	 * @return the scanAll
	 */
	public boolean isScanAll() {
		return scanAll;
	}

	/**
	 * @param scanAll the scanAll to set
	 */
	public void setScanAll(boolean scanAll) {
		this.scanAll = scanAll;
	}

		
	/**
	 * 
	 * @return
	 * The exclusionRules
	 */
	@JsonProperty("exclusion_rules")
	public List<ExclusionRule> getExclusionRules() {
		return exclusionRules;
	}

	/**
	 * 
	 * @param exclusionRules
	 * The exclusion_rules
	 */
	@JsonProperty("exclusion_rules")
	public void setExclusionRules(List<ExclusionRule> exclusionRules) {
		this.exclusionRules = exclusionRules;
	}

	/**
	 * 
	 * @return
	 * The inclusionRules
	 */
	@JsonProperty("inclusion_rules")
	public List<InclusionRule> getInclusionRules() {
		return inclusionRules;
	}

	/**
	 * 
	 * @param inclusionRules
	 * The inclusion_rules
	 */
	@JsonProperty("inclusion_rules")
	public void setInclusionRules(List<InclusionRule> inclusionRules) {
		this.inclusionRules = inclusionRules;
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