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
public class ExposureTotals {
	long countPublic = 0;
	long countExternal = 0;
	long countInternal = 0;

	@JsonProperty("objects")
	private List<com.elastica.beatle.securlets.dto.TotalObject> objects = new ArrayList<com.elastica.beatle.securlets.dto.TotalObject>();
	
	@JsonProperty("results")
	private List<com.elastica.beatle.securlets.dto.TotalObject> results = new ArrayList<com.elastica.beatle.securlets.dto.TotalObject>();
	
	

	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	
	/**
	 * @return the results
	 */
	public List<com.elastica.beatle.securlets.dto.TotalObject> getResults() {
		return results;
	}


	/**
	 * @param results the results to set
	 */
	public void setResults(List<com.elastica.beatle.securlets.dto.TotalObject> results) {
		this.results = results;
	}
	
	/**
	 * 
	 * @return
	 * The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.securlets.dto.TotalObject> getObjects() {
		return objects;
	}


	public long getPublicExposouresCount() {

		if (this.objects != null) {
			for (TotalObject totalObject : objects) {
				if (totalObject.getId().equals("public")) {
					countPublic = totalObject.getTotal() ;
				}
			}
		}
		if (this.results != null) {
			for (TotalObject totalObject : results) {
				if (totalObject.getId().equals("public")) {
					countPublic = totalObject.getTotal() ;
				}
			}
		}
		
		return countPublic;

	}


	public long getExternalExposouresCount() {

		if (this.objects != null) {
			for (TotalObject totalObject : objects) {
				if (totalObject.getId().equals("external")) {
					countExternal = totalObject.getTotal() ;
				}
			}
		}
		if (this.results != null) {
			for (TotalObject totalObject : results) {
				if (totalObject.getId().equals("external")) {
					countExternal = totalObject.getTotal() ;
				}
			}
		}
		return countExternal;

	}


	public long getInternalExposouresCount() {

		if (this.objects != null) {
			for (TotalObject totalObject : objects) {
				if (totalObject.getId().equals("internal")) {
					countInternal = totalObject.getTotal() ;
				}
			}
		}
		if (this.results != null) {
			for (TotalObject totalObject : results) {
				if (totalObject.getId().equals("internal")) {
					countInternal = totalObject.getTotal() ;
				}
			}
		}
		return countInternal;

	}
	
	public long getTotal(String key) {
		long total=0;
		
		if (this.results != null) {
			for (TotalObject totalObject : results) {
				if (totalObject.getId().equals(key)) {
					total = totalObject.getTotal() ;
				}
			}
		}
		return total;

	}
	
	
	

	/**
	 * 
	 * @param objects
	 * The objects
	 */
	@JsonProperty("objects")
	public void setObjects(List<com.elastica.beatle.securlets.dto.TotalObject> objects) {
		this.objects = objects;
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