package com.elastica.infra.dto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.elastica.infra.dto.Profile;

@JsonPropertyOrder({
"api_response",
"action_status",
"meta",
"profiles",
"key",
"exceptions"
})
public class ProfileList {

@JsonProperty("api_response")
private String apiResponse;
@JsonProperty("action_status")
private String actionStatus;
@JsonProperty("meta")
private Meta meta;
@JsonProperty("profiles")
private List<Profile> profiles = new ArrayList<Profile>();
@JsonProperty("key")
private String key;
@JsonProperty("exceptions")
private String exceptions;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The apiResponse
*/
@JsonProperty("api_response")
public String getApiResponse() {
return apiResponse;
}

/**
* 
* @param apiResponse
* The api_response
*/
@JsonProperty("api_response")
public void setApiResponse(String apiResponse) {
this.apiResponse = apiResponse;
}

/**
* 
* @return
* The actionStatus
*/
@JsonProperty("action_status")
public String getActionStatus() {
return actionStatus;
}

/**
* 
* @param actionStatus
* The action_status
*/
@JsonProperty("action_status")
public void setActionStatus(String actionStatus) {
this.actionStatus = actionStatus;
}

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
* The profiles
*/
@JsonProperty("profiles")
public List<Profile> getProfiles() {
return profiles;
}

/**
* 
* @param profiles
* The profiles
*/
@JsonProperty("profiles")
public void setProfiles(List<Profile> profiles) {
this.profiles = profiles;
}

/**
* 
* @return
* The key
*/
@JsonProperty("key")
public String getKey() {
return key;
}

/**
* 
* @param key
* The key
*/
@JsonProperty("key")
public void setKey(String key) {
this.key = key;
}

/**
* 
* @return
* The exceptions
*/
@JsonProperty("exceptions")
public String getExceptions() {
return exceptions;
}

/**
* 
* @param exceptions
* The exceptions
*/
@JsonProperty("exceptions")
public void setExceptions(String exceptions) {
this.exceptions = exceptions;
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