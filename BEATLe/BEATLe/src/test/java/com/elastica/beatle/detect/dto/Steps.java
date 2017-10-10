package com.elastica.beatle.detect.dto;

import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonProperty;

public class Steps
{
private int max_gap_time;

private int window;

public int getStep_type() {
	return step_type;
}

public void setStep_type(int step_type) {
	this.step_type = step_type;
}

public String[] getIoi_code() {
	return ioi_code;
}

public void setIoi_code(String[] ioi_code) {
	this.ioi_code = ioi_code;
}

private String[] facility;

private String[] source;
private int step_type;
private String[] ioi_code;

private boolean user_external =false;

public boolean isUser_external() {
	return user_external;
}

public void setUser_external(boolean user_external) {
	this.user_external = user_external;
}

@JsonProperty("Object_type")
private String[] objectType;

private boolean facility_individually;

private boolean source_individually;

@JsonProperty("Activity_type")
private String[] activityType;


private String[] user;

private int threshold;

private boolean user_individually;

public int getMax_gap_time() {
	return max_gap_time;
}

public void setMax_gap_time(int max_gap_time) {
	this.max_gap_time = max_gap_time;
}

public int getWindow() {
	return window;
}

public void setWindow(int window) {
	this.window = window;
}

public String[] getFacility() {
	return facility;
}

public void setFacility(String[] facility) {
	this.facility = facility;
}

public String[] getSource() {
	return source;
}

public void setSource(String[] source) {
	this.source = source;
}

public String[] getObjectType() {
	return objectType;
}

public void setObjectType(String[] objectType) {
	this.objectType = objectType;
}

public boolean isFacility_individually() {
	return facility_individually;
}

public void setFacility_individually(boolean facility_individually) {
	this.facility_individually = facility_individually;
}

public boolean isSource_individually() {
	return source_individually;
}

public void setSource_individually(boolean source_individually) {
	this.source_individually = source_individually;
}

public String[] getActivityType() {
	return activityType;
}

public void setActivityType(String[] activityType) {
	this.activityType = activityType;
}

public String[] getUser() {
	return user;
}

public void setUser(String[] user) {
	this.user = user;
}

public int getThreshold() {
	return threshold;
}

public void setThreshold(int threshold) {
	this.threshold = threshold;
}

public boolean isUser_individually() {
	return user_individually;
}

public void setUser_individually(boolean user_individually) {
	this.user_individually = user_individually;
}






}

