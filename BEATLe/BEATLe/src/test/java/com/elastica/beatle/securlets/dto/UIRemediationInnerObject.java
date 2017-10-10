package com.elastica.beatle.securlets.dto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class UIRemediationInnerObject {

	@JsonProperty("objects")
	private List<SecurletRemediation> objects;
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The objects1
	 */
	@JsonProperty("objects")
	public List<SecurletRemediation> getObjects() {
		return objects;
	}

	/**
	 * 
	 * @param objects1
	 * The objects1
	 */
	@JsonProperty("objects")
	public void setObjects(List<SecurletRemediation> objects) {
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

}

