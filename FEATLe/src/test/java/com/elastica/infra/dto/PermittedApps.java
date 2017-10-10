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
"Dashboard",
"Users"
})
public class PermittedApps {

@JsonProperty("Dashboard")
private List<String> Dashboard = new ArrayList<String>();
@JsonProperty("Users")
private List<String> Users = new ArrayList<String>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The Dashboard
*/
@JsonProperty("Dashboard")
public List<String> getDashboard() {
return Dashboard;
}

/**
* 
* @param Dashboard
* The Dashboard
*/
@JsonProperty("Dashboard")
public void setDashboard(List<String> Dashboard) {
this.Dashboard = Dashboard;
}

/**
* 
* @return
* The Users
*/
@JsonProperty("Users")
public List<String> getUsers() {
return Users;
}

/**
* 
* @param Users
* The Users
*/
@JsonProperty("Users")
public void setUsers(List<String> Users) {
this.Users = Users;
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
