package com.elastica.beatle.dci.dto.contentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ContentTypes {

	@JsonProperty("objects")
	private List<Objects> objects = new ArrayList<Objects>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The results
	 */
	@JsonProperty("objects")
	public List<Objects> getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param results
	 * The results
	 */
	@JsonProperty("objects")
	public void setObjects(List<Objects> objects) {
		this.objects = objects;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
	
	public int getContentTypeCount(String contentType) {
		int count = 0;
		for ( Objects o : getObjects()) {
			if(o.getId().equalsIgnoreCase(contentType)) {
				count =  o.getTotal();
			}
		}
		return count;
	}

}
