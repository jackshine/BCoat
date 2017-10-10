package com.elastica.beatle.detect.dto;

import java.util.HashMap;
import java.util.Map;

public class SDInput {

	int importance;
	public int getImportance() {
		return importance;
	}

	public void setImportance(int importance) {
		this.importance = importance;
	}
	
	boolean multiuser =false;
	String[] users = new String[10] ;
	
	
	
	public String[] getUsers() {
		return users;
	}

	public void setUsers(String[] users) {
		this.users = users;
	}

	public boolean isMultiuser() {
		return multiuser;
	}

	public void setMultiuser(boolean multiuser) {
		this.multiuser = multiuser;
	}

	public SDInput(int sequenceGroups, String name, String description, 
			int threshold, int window, boolean facility_individually, boolean source_individually,
			boolean user_individually, Map<Integer, String[]> activityTypeMap, Map<Integer, String[]> facilityMap,
			Map<Integer, String[]> sourcesMap, Map<Integer, String[]> userMap, Map<Integer, String[]> objectTypeMap) {
		super();
		this.sequenceGroups = sequenceGroups;
		this.name = name;
		this.description = description;
		this.threshold = threshold;
		this.window = window;
		this.facility_individually = facility_individually;
		this.source_individually = source_individually;
		this.user_individually = user_individually;
		this.activityTypeMap = activityTypeMap;
		this.facilityMap = facilityMap;
		this.sourcesMap = sourcesMap;
		this.userMap = userMap;
		this.objectTypeMap = objectTypeMap;
		this.importance = 2;
	}
	
	public SDInput(int sequenceGroups,  String name, String description, Map<Integer, Integer> stepType,
			int threshold, int window, boolean facility_individually, boolean source_individually,
			boolean user_individually, Map<Integer, String[]> activityTypeMap, Map<Integer, String[]> facilityMap,
			Map<Integer, String[]> sourcesMap, Map<Integer, String[]> userMap, Map<Integer, String[]> objectTypeMap) {
		super();
		this.sequenceGroups = sequenceGroups;
		this.name = name;
		this.description = description;
		this.stepType = stepType;
		this.threshold = threshold;
		this.window = window;
		this.facility_individually = facility_individually;
		this.source_individually = source_individually;
		this.user_individually = user_individually;
		this.activityTypeMap = activityTypeMap;
		this.facilityMap = facilityMap;
		this.sourcesMap = sourcesMap;
		this.userMap = userMap;
		this.objectTypeMap = objectTypeMap;
		this.importance = 2;
	}
	
	public SDInput(int sequenceGroups, String id, String name, String description, Map<Integer, Integer> stepType,
			int threshold, int window, boolean facility_individually, boolean source_individually,
			boolean user_individually, Map<Integer, String[]> activityTypeMap, Map<Integer, String[]> facilityMap,
			Map<Integer, String[]> sourcesMap, Map<Integer, String[]> userMap, Map<Integer, String[]> objectTypeMap) {
		super();
		this.sequenceGroups = sequenceGroups;
		this.id = id;
		this.name = name;
		this.description = description;
		this.stepType = stepType;
		this.threshold = threshold;
		this.window = window;
		this.facility_individually = facility_individually;
		this.source_individually = source_individually;
		this.user_individually = user_individually;
		this.activityTypeMap = activityTypeMap;
		this.facilityMap = facilityMap;
		this.sourcesMap = sourcesMap;
		this.userMap = userMap;
		this.objectTypeMap = objectTypeMap;
		this.importance = 2;
	}


	int sequenceGroups;
	String id ;
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	String name ;
	String description;
	 Map<Integer, Integer > stepType;
	
	public Map<Integer, Integer> getStepType() {
		return stepType;
	}


	public void setStepType(Map<Integer, Integer> stepType) {
		this.stepType = stepType;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getSequenceGroups() {
		return sequenceGroups;
	}


	public void setSequenceGroups(int sequenceGroups) {
		this.sequenceGroups = sequenceGroups;
	}


	public int getThreshold() {
		return threshold;
	}


	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


	public int getWindow() {
		return window;
	}


	public void setWindow(int window) {
		this.window = window;
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


	public boolean isUser_individually() {
		return user_individually;
	}


	public void setUser_individually(boolean user_individually) {
		this.user_individually = user_individually;
	}


	public Map<Integer, String[]> getActivityTypeMap() {
		return activityTypeMap;
	}


	public void setActivityTypeMap(Map<Integer, String[]> activityTypeMap) {
		this.activityTypeMap = activityTypeMap;
	}


	public Map<Integer, String[]> getFacilityMap() {
		return facilityMap;
	}


	public void setFacilityMap(Map<Integer, String[]> facilityMap) {
		this.facilityMap = facilityMap;
	}


	public Map<Integer, String[]> getSourcesMap() {
		return sourcesMap;
	}


	public void setSourcesMap(Map<Integer, String[]> sourcesMap) {
		this.sourcesMap = sourcesMap;
	}


	public Map<Integer, String[]> getUserMap() {
		return userMap;
	}


	public void setUserMap(Map<Integer, String[]> userMap) {
		this.userMap = userMap;
	}


	public Map<Integer, String[]> getObjectTypeMap() {
		return objectTypeMap;
	}


	public void setObjectTypeMap(Map<Integer, String[]> objectTypeMap) {
		this.objectTypeMap = objectTypeMap;
	}


	int threshold ;
	int window ;
	
	
	boolean facility_individually = false;
	boolean source_individually = false;
	boolean user_individually =true;
	
	Map<Integer, String[] > activityTypeMap = new HashMap<>();
	
	
	Map<Integer, String[] > facilityMap = new HashMap<>();

	
	Map<Integer, String[] > sourcesMap = new HashMap<>();
	
	
	Map<Integer, String[] > userMap = new HashMap<>();
	
									
	Map<Integer, String[] > objectTypeMap = new HashMap<>();
	
}
