package com.elastica.beatle.infra.dto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class Groups {

@JsonProperty("api_response")
private String apiResponse;
@JsonProperty("groupslist")
private List<Groupslist> groupslist = new ArrayList<Groupslist>();
@JsonProperty("meta")
private Meta meta;
@JsonProperty("action_status")
private String actionStatus;
@JsonProperty("key")
private String key;
@JsonProperty("offset")
private String offset;
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
* The groupslist
*/
@JsonProperty("groupslist")
public List<Groupslist> getGroupslist() {
return groupslist;
}

/**
* 
* @param groupslist
* The groupslist
*/
@JsonProperty("groupslist")
public void setGroupslist(List<Groupslist> groupslist) {
this.groupslist = groupslist;
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
* The offset
*/
@JsonProperty("offset")
public String getOffset() {
return offset;
}

/**
* 
* @param offset
* The offset
*/
@JsonProperty("offset")
public void setOffset(String offset) {
this.offset = offset;
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
