package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;

public class BoxDocument {

	@JsonProperty("meta")
	private Meta meta;
	@JsonProperty("objects")
	private List<com.elastica.beatle.securlets.dto.Object> objects = new ArrayList<com.elastica.beatle.securlets.dto.Object>();
	

	/**
	 *
	 * @return
	 * The meta
	 */
	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	/**
	 *
	 * @param meta
	 * The meta
	 */
	@JsonProperty("meta")
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	/**
	 *
	 * @return
	 * The objects
	 */
	@JsonProperty("objects")
	public List<com.elastica.beatle.securlets.dto.Object> getObjects() {
		return objects;
	}

	/**
	 *
	 * @param objects
	 * The objects
	 */
	@JsonProperty("objects")
	public void setObjects(List<com.elastica.beatle.securlets.dto.Object> objects) {
		this.objects = objects;
	}

	

}

