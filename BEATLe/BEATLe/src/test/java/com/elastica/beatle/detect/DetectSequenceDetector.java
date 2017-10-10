package com.elastica.beatle.detect;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.Groups;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.detect.dto.Source;
import com.elastica.beatle.detect.dto.Steps;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;

public class DetectSequenceDetector {
	
	private static final String INSERT = "insert";
	private static final String UPDATE = "update";
	DetectCommonutils utils = new DetectCommonutils();
	 Client restClient  = new Client();
	 
	 
	 public SDInput createSDInput(String name,int threshold, int window, boolean facility_individually, 
				boolean source_individually, boolean user_individually,
				String[] activityTypes , String[] facilities ,String[] sources ,String[] users ,String[] objects) {
			
			
			Map<Integer, String[] > activityTypeMap = new HashMap<>();
			for (int j = 0; j <activityTypes.length;) {
				activityTypeMap.put(j, new String[]{activityTypes[j]});
				j++;
			}
				
			Map<Integer, String[] > facilityMap = new HashMap<>();
			for (int j = 0; j <facilities.length;) {
				facilityMap.put(j, new String[]{facilities[j]});
				j++;
			}
			
			Map<Integer, String[] > sourcesMap = new HashMap<>();
			for (int j = 0; j <sources.length;) {
				sourcesMap.put(j, new String[]{sources[j]});
				j++;
			}
				
			Map<Integer, String[] > userMap = new HashMap<>();
			for (int j = 0; j <users.length;) {
				userMap.put(j, new String[]{users[j]});
				j++;
			}
		
			Map<Integer, String[] > objectTypeMap = new HashMap<>();
			for (int j = 0; j <objects.length;) {
				objectTypeMap.put(j, new String[]{objects[j]});
				j++;
			}
			
			SDInput sdInput = new SDInput(1,  name,  name,
					threshold, window, facility_individually,source_individually, user_individually,
												activityTypeMap, facilityMap, sourcesMap, userMap, objectTypeMap);
			return sdInput;
		}
	 
	 
	 public SDInput createSDInputForBBIinSD(String name,int threshold, int window, boolean facility_individually, 
				boolean source_individually, boolean user_individually,
				String[] activityTypes , String[] facilities ,String[] sources ,String[] users ,String[] objects, Integer[] steps) {
			
			
			Map<Integer, String[] > activityTypeMap = new HashMap<>();
			for (int j = 0; j <activityTypes.length;) {
				activityTypeMap.put(j, new String[]{activityTypes[j]});
				j++;
			}
				
			Map<Integer, String[] > facilityMap = new HashMap<>();
			for (int j = 0; j <facilities.length;) {
				facilityMap.put(j, new String[]{facilities[j]});
				j++;
			}
			
			Map<Integer, String[] > sourcesMap = new HashMap<>();
			for (int j = 0; j <sources.length;) {
				sourcesMap.put(j, new String[]{sources[j]});
				j++;
			}
				
			Map<Integer, String[] > userMap = new HashMap<>();
			for (int j = 0; j <users.length;) {
				userMap.put(j, new String[]{users[j]});
				j++;
			}
		
			Map<Integer, String[] > objectTypeMap = new HashMap<>();
			for (int j = 0; j <objects.length;) {
				objectTypeMap.put(j, new String[]{objects[j]});
				j++;
			}
			
			 Map<Integer, Integer> stepType =  new HashMap<>();
			 for (int j = 0; j <steps.length;) {
				 stepType.put(j, steps[j]);
					j++;
				}
			
			SDInput sdInput = new SDInput(1,  name,  name,stepType,
					threshold, window, facility_individually,source_individually, user_individually,
												activityTypeMap, facilityMap, sourcesMap, userMap, objectTypeMap);
			return sdInput;
		}
	
	
	
	public HttpResponse createSequenceDetector(SDInput sDInput,TestSuiteDTO  suiteData)
					throws JAXBException, UnsupportedEncodingException, Exception {
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setId("");
		dsdto.setRequestType(INSERT);
		Source source = new Source();
		source.setName(sDInput.getName());
		source.setDescription(sDInput.getDescription());
		source.setEnabled(true);
		source.setImportance(sDInput.getImportance());
		source.setTs_label("");
		
		
		List<Groups> groups = new ArrayList<>();
		
		for (int i = 1; i <= sDInput.getSequenceGroups(); i++) {
			Groups group = new Groups();
			group.setThreshold(sDInput.getThreshold());
			group.setWindow(sDInput.getWindow());
			List<Steps> steps = new ArrayList<>();
			for (int j = 0; j < sDInput.getActivityTypeMap().size(); j++) {
				Steps step = new Steps();
				step.setActivityType(sDInput.getActivityTypeMap().get(j));
				step.setFacility(sDInput.getFacilityMap().get(j));
				step.setFacility_individually(sDInput.isFacility_individually());
				step.setMax_gap_time(-1);
				step.setObjectType(sDInput.getObjectTypeMap().get(j));
				step.setSource(sDInput.getSourcesMap().get(j));
				step.setSource_individually(sDInput.isSource_individually());
				step.setThreshold(0);
				step.setWindow(1);
				step.setUser_individually(sDInput.isUser_individually());
				if(sDInput.isMultiuser()){
				step.setUser(sDInput.getUsers());
				}else{
					step.setUser(sDInput.getUserMap().get(j));
				}
				steps.add(step);
			}
			
			group.setSteps(steps);
			groups.add(group);
		}
		
		source.setGroups(groups);
		
		
		
		dsdto.setSource(source);
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		Logger.info("Sequence name :::::: "+sDInput.getName());
		Logger.info("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		 Client restClient  = new Client();
		 List<NameValuePair> requestHeader =  utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID());
		HttpResponse response = esLogs.detectsequences(restClient, requestHeader, se, suiteData);
		Thread.sleep(1 * 5 * 1000);
		
//		System.out.println("dsdto:::::: "+response.getStatusLine());
//		String  responseBody = utils.getResponseBody(response);
//		System.out.println("dsdto:::::: "+responseBody);
		
		return response;
	}
	
	public HttpResponse createSequenceDetectorforBBIinSD(SDInput sDInput,TestSuiteDTO  suiteData)
					throws JAXBException, UnsupportedEncodingException, Exception {
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setId("");
		dsdto.setRequestType(INSERT);
		Source source = new Source();
		source.setName(sDInput.getName());
		source.setDescription(sDInput.getDescription());
		source.setEnabled(true);
		source.setImportance(2);
		source.setTs_label("");
		
		
		List<Groups> groups = new ArrayList<>();
		
		for (int i = 1; i <= sDInput.getSequenceGroups(); i++) {
			Groups group = new Groups();
			group.setThreshold(sDInput.getThreshold());
			group.setWindow(sDInput.getWindow());
			List<Steps> steps = new ArrayList<>();
			for (int j = 0; j <sDInput.getActivityTypeMap().size(); j++) {
				Steps step = new Steps();
				if(sDInput.getStepType().get(j)==0){
					step.setObjectType(sDInput.getObjectTypeMap().get(j));
					step.setStep_type(sDInput.getStepType().get(j));
				step.setActivityType(sDInput.getActivityTypeMap().get(j));
				}else if(sDInput.getStepType().get(j)==1){
					step.setStep_type(sDInput.getStepType().get(j));
					step.setIoi_code(sDInput.getActivityTypeMap().get(j));
				}
				step.setFacility(sDInput.getFacilityMap().get(j));
				step.setFacility_individually(sDInput.isFacility_individually());
				step.setMax_gap_time(-1);
				step.setSource(sDInput.getSourcesMap().get(j));
				step.setSource_individually(sDInput.isSource_individually());
				step.setThreshold(0);
				step.setWindow(1);
				step.setUser_individually(sDInput.isUser_individually());
				step.setUser(sDInput.getUserMap().get(j));
				steps.add(step);
			}
			
			group.setSteps(steps);
			groups.add(group);
		}
		
		source.setGroups(groups);
		
		
		
		dsdto.setSource(source);
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		Client restClient  = new Client();
		 List<NameValuePair> requestHeader =  utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID());
		HttpResponse response = esLogs.detectsequences(restClient, requestHeader, se, suiteData);
		Thread.sleep(1 * 10 * 1000);
		return response;
	}
	
	protected HttpResponse updateSequenceDetector(SDInput sDInput,DetectSuiteParams  suiteData, String csfrToken, String sessionID)
					throws JAXBException, UnsupportedEncodingException, Exception {
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setId(sDInput.getId());
		dsdto.setRequestType(UPDATE);
		Source source = new Source();
		source.setName(sDInput.getName());
		source.setDescription(sDInput.getDescription());
		source.setEnabled(true);
		source.setImportance(2);
		source.setTs_label("");
		
		
		List<Groups> groups = new ArrayList<>();
		
		for (int i = 1; i <= sDInput.getSequenceGroups(); i++) {
			Groups group = new Groups();
			group.setThreshold(sDInput.getThreshold());
			group.setWindow(sDInput.getWindow());
			List<Steps> steps = new ArrayList<>();
			for (int j = 0; j <= sDInput.getActivityTypeMap().size(); j++) {
				Steps step = new Steps();
				step.setActivityType(sDInput.getActivityTypeMap().get(j));
				step.setFacility(sDInput.getFacilityMap().get(j));
				step.setFacility_individually(sDInput.isFacility_individually());
				step.setMax_gap_time(-1);
				step.setObjectType(sDInput.getObjectTypeMap().get(j));
				step.setSource(sDInput.getSourcesMap().get(j));
				step.setSource_individually(sDInput.isSource_individually());
				step.setThreshold(0);
				step.setWindow(1);
				step.setUser_individually(sDInput.isUser_individually());
				step.setUser(sDInput.getUserMap().get(j));
				steps.add(step);
			}
			
			group.setSteps(steps);
			groups.add(group);
		}
		
		source.setGroups(groups);
		
		
		
		dsdto.setSource(source);
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		 Client restClient  = new Client();
		 List<NameValuePair> requestHeader =  utils.buildCookieHeaders(suiteData, csfrToken, sessionID);
		HttpResponse response = esLogs.detectsequences(restClient, requestHeader, se, suiteData);
		Thread.sleep(1 * 10 * 1000);
		return response;
	}
	
	public void deleteSequenceDetectors(String sequencedetectorName, TestSuiteDTO  suiteData) throws Exception{
		
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setLimit(20);
		dsdto.setOffset(0);
		dsdto.setRequestType("list");
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		 Client restClient  = new Client();
		HttpResponse response = esLogs.detectsequences(restClient, utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID()), se, suiteData);
		String responseBody = utils.getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(utils.getJSONValue(responseBody.toString(), "detect_sequences"));
		if (detect_sequences.isArray()) {
		    for (final JsonNode objNode : detect_sequences) {
		    String name =	utils.getJSONValue(objNode.toString(), "name");
		    if(sequencedetectorName.equalsIgnoreCase(name.replace("\"", ""))){
		    	//{"id":"55ee94809dfa5114de801642","requestType":"delete"}
		    	DetectSequenceDto sddto = new DetectSequenceDto();
		    	String id = utils.getJSONValue(objNode.toString(), "id").replace("\"", "");
		    	sddto.setId(id);
		    	sddto.setRequestType("delete");
		    	 esLogs = new ElasticSearchLogs();
				 se =new StringEntity(utils.marshall(sddto));
				 System.out.println("Sequence detector :::::: "+utils.marshall(sddto));
				 response = esLogs.detectsequences(restClient, utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID()), se, suiteData);
				 responseBody = utils.getResponseBody(response);
				 System.out.println("delete responce body :::::: "+responseBody);
				 
		    	
		    	}
		    	}
		    }
		Thread.sleep(1 * 30 * 1000);
	}

	
	public void deleteSequenceDetectors(TestSuiteDTO  suiteData) throws Exception{
		//TODO::Added all the sequence detector names here
		
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setLimit(200);
		dsdto.setOffset(0);
		dsdto.setRequestType("list");
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		Client restClient  = new Client();
		HttpResponse response = esLogs.detectsequences(restClient, utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID()), se, suiteData);
		String responseBody = utils.getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(utils.getJSONValue(responseBody.toString(), "detect_sequences"));
		if (detect_sequences.isArray()) {
		    for (final JsonNode objNode : detect_sequences) {
		    String name =	utils.getJSONValue(objNode.toString(), "name");
		    	//{"id":"55ee94809dfa5114de801642","requestType":"delete"}
		    	DetectSequenceDto sddto = new DetectSequenceDto();
		    	String id = utils.getJSONValue(objNode.toString(), "id").replace("\"", "");
		    	sddto.setId(id);
		    	sddto.setRequestType("delete");
		    	 esLogs = new ElasticSearchLogs();
				 se =new StringEntity(utils.marshall(sddto));
				 System.out.println("Sequence detector :::::: "+utils.marshall(sddto));
				 response = esLogs.detectsequences(restClient, utils.buildCookieHeaders(suiteData, suiteData.getCSRFToken(), suiteData.getSessionID()), se, suiteData);
				 responseBody = utils.getResponseBody(response);
				 System.out.println("delete responce body :::::: "+responseBody);
				 
		    	}
		    }
		Thread.sleep(1 * 30 * 1000);
		
	}



	
}
