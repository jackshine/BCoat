package com.elastica.infra.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"modified_by",
"description",
"name",
"notes",
"is_active",
"created_by",
"id",
"parent_group",
"domains",
"created_on",
"modified_on",
"email",
"resource_uri"
})
public class Groupslist {

@JsonProperty("modified_by")
private Object modifiedBy;
@JsonProperty("description")
private String description;
@JsonProperty("name")
private String name;
@JsonProperty("notes")
private String notes;
@JsonProperty("is_active")
private Boolean isActive;
@JsonProperty("created_by")
private String createdBy;
@JsonProperty("id")
private String id;
@JsonProperty("parent_group")
private Object parentGroup;
@JsonProperty("domains")
private List<Object> domains = new ArrayList<Object>();
@JsonProperty("created_on")
private String createdOn;
@JsonProperty("modified_on")
private Object modifiedOn;
@JsonProperty("email")
private String email;
@JsonProperty("resource_uri")
private String resourceUri;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The modifiedBy
*/
@JsonProperty("modified_by")
public Object getModifiedBy() {
return modifiedBy;
}

/**
* 
* @param modifiedBy
* The modified_by
*/
@JsonProperty("modified_by")
public void setModifiedBy(Object modifiedBy) {
this.modifiedBy = modifiedBy;
}

/**
* 
* @return
* The description
*/
@JsonProperty("description")
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The name
*/
@JsonProperty("name")
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The notes
*/
@JsonProperty("notes")
public String getNotes() {
return notes;
}

/**
* 
* @param notes
* The notes
*/
@JsonProperty("notes")
public void setNotes(String notes) {
this.notes = notes;
}

/**
* 
* @return
* The isActive
*/
@JsonProperty("is_active")
public Boolean getIsActive() {
return isActive;
}

/**
* 
* @param isActive
* The is_active
*/
@JsonProperty("is_active")
public void setIsActive(Boolean isActive) {
this.isActive = isActive;
}

/**
* 
* @return
* The createdBy
*/
@JsonProperty("created_by")
public String getCreatedBy() {
return createdBy;
}

/**
* 
* @param createdBy
* The created_by
*/
@JsonProperty("created_by")
public void setCreatedBy(String createdBy) {
this.createdBy = createdBy;
}

/**
* 
* @return
* The id
*/
@JsonProperty("id")
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
@JsonProperty("id")
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The parentGroup
*/
@JsonProperty("parent_group")
public Object getParentGroup() {
return parentGroup;
}

/**
* 
* @param parentGroup
* The parent_group
*/
@JsonProperty("parent_group")
public void setParentGroup(Object parentGroup) {
this.parentGroup = parentGroup;
}

/**
* 
* @return
* The domains
*/
@JsonProperty("domains")
public List<Object> getDomains() {
return domains;
}

/**
* 
* @param domains
* The domains
*/
@JsonProperty("domains")
public void setDomains(List<Object> domains) {
this.domains = domains;
}

/**
* 
* @return
* The createdOn
*/
@JsonProperty("created_on")
public String getCreatedOn() {
return createdOn;
}

/**
* 
* @param createdOn
* The created_on
*/
@JsonProperty("created_on")
public void setCreatedOn(String createdOn) {
this.createdOn = createdOn;
}

/**
* 
* @return
* The modifiedOn
*/
@JsonProperty("modified_on")
public Object getModifiedOn() {
return modifiedOn;
}

/**
* 
* @param modifiedOn
* The modified_on
*/
@JsonProperty("modified_on")
public void setModifiedOn(Object modifiedOn) {
this.modifiedOn = modifiedOn;
}

/**
* 
* @return
* The email
*/
@JsonProperty("email")
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
@JsonProperty("email")
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The resourceUri
*/
@JsonProperty("resource_uri")
public String getResourceUri() {
return resourceUri;
}

/**
* 
* @param resourceUri
* The resource_uri
*/
@JsonProperty("resource_uri")
public void setResourceUri(String resourceUri) {
this.resourceUri = resourceUri;
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