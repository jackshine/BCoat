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
"all",
"DETECT",
"INVESTIGATE",
"AUDIT"
})
public class InformationLevel_ {

@JsonProperty("all")
private Boolean all;
@JsonProperty("DETECT")
private List<String> DETECT = new ArrayList<String>();
@JsonProperty("INVESTIGATE")
private List<String> INVESTIGATE = new ArrayList<String>();
@JsonProperty("AUDIT")
private List<String> AUDIT = new ArrayList<String>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The all
*/
@JsonProperty("all")
public Boolean getAll() {
return all;
}

/**
* 
* @param all
* The all
*/
@JsonProperty("all")
public void setAll(Boolean all) {
this.all = all;
}

/**
* 
* @return
* The DETECT
*/
@JsonProperty("DETECT")
public List<String> getDETECT() {
return DETECT;
}

/**
* 
* @param DETECT
* The DETECT
*/
@JsonProperty("DETECT")
public void setDETECT(List<String> DETECT) {
this.DETECT = DETECT;
}

/**
* 
* @return
* The INVESTIGATE
*/
@JsonProperty("INVESTIGATE")
public List<String> getINVESTIGATE() {
return INVESTIGATE;
}

/**
* 
* @param INVESTIGATE
* The INVESTIGATE
*/
@JsonProperty("INVESTIGATE")
public void setINVESTIGATE(List<String> INVESTIGATE) {
this.INVESTIGATE = INVESTIGATE;
}

/**
* 
* @return
* The AUDIT
*/
@JsonProperty("AUDIT")
public List<String> getAUDIT() {
return AUDIT;
}

/**
* 
* @param AUDIT
* The AUDIT
*/
@JsonProperty("AUDIT")
public void setAUDIT(List<String> AUDIT) {
this.AUDIT = AUDIT;
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
