package com.elastica.beatle.AuditSummaryDataObjects;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "sessions", "traffic", "user_id" })
public class TopUser_ {

	@JsonProperty("sessions")
	private Integer sessions;
	@JsonProperty("traffic")
	private Integer traffic;
	@JsonProperty("user_id")
	private Integer userId;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The sessions
	 */
	@JsonProperty("sessions")
	public Integer getSessions() {
		return sessions;
	}

	/**
	 * 
	 * @param sessions
	 *            The sessions
	 */
	@JsonProperty("sessions")
	public void setSessions(Integer sessions) {
		this.sessions = sessions;
	}

	/**
	 * 
	 * @return The traffic
	 */
	@JsonProperty("traffic")
	public Integer getTraffic() {
		return traffic;
	}

	/**
	 * 
	 * @param traffic
	 *            The traffic
	 */
	@JsonProperty("traffic")
	public void setTraffic(Integer traffic) {
		this.traffic = traffic;
	}

	/**
	 * 
	 * @return The userId
	 */
	@JsonProperty("user_id")
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 *            The user_id
	 */
	@JsonProperty("user_id")
	public void setUserId(Integer userId) {
		this.userId = userId;
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