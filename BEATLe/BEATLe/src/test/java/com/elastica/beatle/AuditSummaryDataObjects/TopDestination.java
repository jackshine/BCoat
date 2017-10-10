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
@JsonPropertyOrder({ "location_id", "sessions", "traffic" })
public class TopDestination {

	@JsonProperty("location_id")
	private Integer locationId;
	@JsonProperty("sessions")
	private Integer sessions;
	@JsonProperty("traffic")
	private Integer traffic;
	@JsonIgnore
	private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

	/**
	 * 
	 * @return The locationId
	 */
	@JsonProperty("location_id")
	public Integer getLocationId() {
		return locationId;
	}

	/**
	 * 
	 * @param locationId
	 *            The location_id
	 */
	@JsonProperty("location_id")
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

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

	@JsonAnyGetter
	public Map<String, java.lang.Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, java.lang.Object value) {
		this.additionalProperties.put(name, value);
	}

}