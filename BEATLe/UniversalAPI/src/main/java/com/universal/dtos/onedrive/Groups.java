package com.universal.dtos.onedrive;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Groups {

	@JsonProperty("__deferred")
	private com.universal.dtos.onedrive.Deferred Deferred;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The Deferred
	 */
	@JsonProperty("__deferred")
	public com.universal.dtos.onedrive.Deferred getDeferred() {
		return Deferred;
	}

	/**
	 * 
	 * @param Deferred
	 * The __deferred
	 */
	@JsonProperty("__deferred")
	public void setDeferred(com.universal.dtos.onedrive.Deferred Deferred) {
		this.Deferred = Deferred;
	}

	

}
