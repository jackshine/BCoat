package com.elastica.beatle.infra.dto;

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
"information_level"
})
public class InformationLevel {

@JsonProperty("information_level")
private List<InformationLevel_> informationLevel = new ArrayList<InformationLevel_>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The informationLevel
*/
@JsonProperty("information_level")
public List<InformationLevel_> getInformationLevel() {
return informationLevel;
}

/**
* 
* @param informationLevel
* The information_level
*/
@JsonProperty("information_level")
public void setInformationLevel(List<InformationLevel_> informationLevel) {
this.informationLevel = informationLevel;
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