package com.elastica.beatle.dci.dto.tp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class TrainingFiles {

	@JsonProperty("fp")
	private List<Fp> fp = new ArrayList<Fp>();
	@JsonProperty("tp")
	private List<Tp> tp = new ArrayList<Tp>();
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return
	 * The fp
	 */
	@JsonProperty("fp")
	public List<Fp> getFp() {
		return fp;
	}

	/**
	 * 
	 * @param fp
	 * The fp
	 */
	@JsonProperty("fp")
	public void setFp(List<Fp> fp) {
		this.fp = fp;
	}

	/**
	 * 
	 * @return
	 * The tp
	 */
	@JsonProperty("tp")
	public List<Tp> getTp() {
		return tp;
	}

	/**
	 * 
	 * @param tp
	 * The tp
	 */
	@JsonProperty("tp")
	public void setTp(List<Tp> tp) {
		this.tp = tp;
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

