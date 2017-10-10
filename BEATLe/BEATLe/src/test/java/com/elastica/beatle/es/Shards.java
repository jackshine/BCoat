package com.elastica.beatle.es;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class Shards {

	@JsonProperty("successful")
	private Long successful;
	@JsonProperty("failed")
	private Long failed;
	@JsonProperty("total")
	private Long total;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The successful
	 */
	@JsonProperty("successful")
	public Long getSuccessful() {
		return successful;
	}

	/**
	 * 
	 * @param successful
	 * The successful
	 */
	@JsonProperty("successful")
	public void setSuccessful(Long successful) {
		this.successful = successful;
	}

	/**
	 * 
	 * @return
	 * The failed
	 */
	@JsonProperty("failed")
	public Long getFailed() {
		return failed;
	}

	/**
	 * 
	 * @param failed
	 * The failed
	 */
	@JsonProperty("failed")
	public void setFailed(Long failed) {
		this.failed = failed;
	}

	/**
	 * 
	 * @return
	 * The total
	 */
	@JsonProperty("total")
	public Long getTotal() {
		return total;
	}

	/**
	 * 
	 * @param total
	 * The total
	 */
	@JsonProperty("total")
	public void setTotal(Long total) {
		this.total = total;
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