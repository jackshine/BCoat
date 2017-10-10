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


@JsonPropertyOrder({
"modified_by",
"permitted_apps",
"permitted_domains",
"global_settings",
"notes",
"is_active",
"created_by",
"name",
"information_level",
"created_on",
"modified_on",
"permitted_services",
"users",
"resource_uri",
"id",
"description"
})
public class Profile {

@JsonProperty("modified_by")
private Object modifiedBy;
@JsonProperty("permitted_apps")
private PermittedApps permittedApps;
@JsonProperty("permitted_domains")
private List<String> permittedDomains = new ArrayList<String>();
@JsonProperty("global_settings")
private GlobalSettings globalSettings;
@JsonProperty("notes")
private String notes;
@JsonProperty("is_active")
private Boolean isActive;
@JsonProperty("created_by")
private String createdBy;
@JsonProperty("name")
private String name;
@JsonProperty("information_level")
private InformationLevel informationLevel;
@JsonProperty("created_on")
private String createdOn;
@JsonProperty("modified_on")
private Object modifiedOn;
@JsonProperty("permitted_services")
private PermittedServices permittedServices;
@JsonProperty("users")
private List<String> users = new ArrayList<String>();
@JsonProperty("resource_uri")
private String resourceUri;
@JsonProperty("id")
private String id;
@JsonProperty("description")
private String description;
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
* The permittedApps
*/
@JsonProperty("permitted_apps")
public PermittedApps getPermittedApps() {
return permittedApps;
}

/**
* 
* @param permittedApps
* The permitted_apps
*/
@JsonProperty("permitted_apps")
public void setPermittedApps(PermittedApps permittedApps) {
this.permittedApps = permittedApps;
}

/**
* 
* @return
* The permittedDomains
*/
@JsonProperty("permitted_domains")
public List<String> getPermittedDomains() {
return permittedDomains;
}

/**
* 
* @param permittedDomains
* The permitted_domains
*/
@JsonProperty("permitted_domains")
public void setPermittedDomains(List<String> permittedDomains) {
this.permittedDomains = permittedDomains;
}

/**
* 
* @return
* The globalSettings
*/
@JsonProperty("global_settings")
public GlobalSettings getGlobalSettings() {
return globalSettings;
}

/**
* 
* @param globalSettings
* The global_settings
*/
@JsonProperty("global_settings")
public void setGlobalSettings(GlobalSettings globalSettings) {
this.globalSettings = globalSettings;
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
* The informationLevel
*/
@JsonProperty("information_level")
public InformationLevel getInformationLevel() {
return informationLevel;
}

/**
* 
* @param informationLevel
* The information_level
*/
@JsonProperty("information_level")
public void setInformationLevel(InformationLevel informationLevel) {
this.informationLevel = informationLevel;
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
* The permittedServices
*/
@JsonProperty("permitted_services")
public PermittedServices getPermittedServices() {
return permittedServices;
}

/**
* 
* @param permittedServices
* The permitted_services
*/
@JsonProperty("permitted_services")
public void setPermittedServices(PermittedServices permittedServices) {
this.permittedServices = permittedServices;
}

/**
* 
* @return
* The users
*/
@JsonProperty("users")
public List<String> getUsers() {
return users;
}

/**
* 
* @param users
* The users
*/
@JsonProperty("users")
public void setUsers(List<String> users) {
this.users = users;
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

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
