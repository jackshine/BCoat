package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentitySet {
	
	Identity user;
	Identity application;
	Identity device;

}
