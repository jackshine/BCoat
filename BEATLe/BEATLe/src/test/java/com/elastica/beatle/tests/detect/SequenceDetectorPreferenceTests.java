package com.elastica.beatle.tests.detect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.Groups;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.detect.dto.Source;
import com.elastica.beatle.detect.dto.Steps;
import com.elastica.beatle.es.ElasticSearchLogs;

public class SequenceDetectorPreferenceTests extends DetectUtils {
	
	DetectCommonutils utils = new DetectCommonutils();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@Test
	public void downloadUploadWaitRepeatPreferenceTest() throws UnsupportedEncodingException, JAXBException, Exception{
		
		String name = UUID.randomUUID().toString();

		String[] activities = new String[]{"Download", "Upload"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		String responseBody = utils.getResponseBody(response);
		
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		
		//TODO:::add validation here
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

		
		//to get the list of sequence detector
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setLimit(20);
		dsdto.setOffset(0);
		dsdto.setRequestType("list");
		Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
		responseBody = getResponseBody(response);
		
		detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

		//TODO:: Add validation here
		 String id=null;
		 if (detect_sequences.isArray()) {
			 for (final JsonNode objNode : detect_sequences) {
				String sequenceName =	getJSONValue(objNode.toString(), "name");
				if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
					JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
			 			if(groupInfo.isArray()){
			 				for (final JsonNode objNode1 : groupInfo){
			 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
						 		if(stepInfo.isArray()){
						 			for (final JsonNode objNode2 : stepInfo){
					 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
							 			if(activitytype.isArray()){
							 				for (final JsonNode objNode3 : activitytype){
								 				String activityTypeCheckActual = formatString1(objNode3);
									 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
									 			if(activityTypeCheckActual.contains("Download")){
									 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Download is not there");
									 			}
									 			else if( activityTypeCheckActual.contains("Upload")){
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"upload is not there");

									 			}
									 			
													 			
									 		}
													 		
									 	}
							 		}
					 			}
					 		}
		 				} 
		 			}
				}
			 
			 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
			}
		
		Reporter.log("Sequence Detector Created Succesfuuly");

		 //update the sequence detector
		//TODO::pass id here
		 activities = new String[]{"Download", "Upload", "ADD"};
		 failities = new String[]{"__any", "__any", "__any"} ;
		 sources = new String[]{"__any", "__any", "__any"} ;
		 users = new String[]{"__any", "__any", "__any"} ;
		 objects = new String[]{"__any", "__any", "__any"} ;
		
		 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		Reporter.log("response :::::"+response);
		
		DetectSequenceDto dsdto1 = new DetectSequenceDto();
		dsdto1.setLimit(20);
		dsdto1.setOffset(0);
		dsdto.setRequestType("list");
		Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
		ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
		StringEntity se1 =new StringEntity(utils.marshall(dsdto));
		response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
		responseBody = getResponseBody(response);
		Reporter.log("responseBody:::::"+responseBody);
		
		detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("Updated sequence detector"+detect_sequences);
		if (detect_sequences.isArray()) {
			for (final JsonNode objNode : detect_sequences) {
				String sequenceName =	getJSONValue(objNode.toString(), "name");
				if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
					JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
			 			if(groupInfo.isArray()){
			 				for (final JsonNode objNode1 : groupInfo){
			 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
						 		if(stepInfo.isArray()){
						 			for (final JsonNode objNode2 : stepInfo){
					 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
							 			if(activitytype.isArray()){
							 				for (final JsonNode objNode3 : activitytype){
								 				String activityTypeCheckActual = formatString1(objNode3);
									 			if(activityTypeCheckActual.contains("Download")){
									 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Download is not there");
									 			}
									 			else if( activityTypeCheckActual.contains("Upload")){
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"upload is not there");

									 			}
									 			else{
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)), "Add is not there");
									 			}
													 			
												 		}
													 		
													 	}
										 		}
						 			}
						 		}
			 				} 
			 			}
				}
			 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		 }
		Reporter.log("Sequence Detector Updated Succesfully");

		//TODO::::Add validation here
			// delete sequence detector
		dsd.deleteSequenceDetectors(name, suiteData);
			 Reporter.log("Sequence Detector Deleted Succesfully");
		
	}
	
	
	@Test
	public void viewWaitShare() throws UnsupportedEncodingException, JAXBException, Exception{
		
		String name = UUID.randomUUID().toString();
		String[] activities = new String[]{"View", "Wait"};
		String[] failities = new String[]{"Google Drive", "Google Drive"} ;
		String[] sources = new String[]{"API", "API"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		
		String responseBody = getResponseBody(response);
		
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		
		//TODO:::add validation here
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

		
		//to get the list of sequence detector
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setLimit(20);
		dsdto.setOffset(0);
		dsdto.setRequestType("list");
		Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
		responseBody = getResponseBody(response);
		
		detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

		//TODO:: Add validation here
		 String id=null;
		 if (detect_sequences.isArray()) {
			 for (final JsonNode objNode : detect_sequences) {
				String sequenceName =	getJSONValue(objNode.toString(), "name");
				if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
					JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
			 			if(groupInfo.isArray()){
			 				for (final JsonNode objNode1 : groupInfo){
			 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
						 		if(stepInfo.isArray()){
						 			for (final JsonNode objNode2 : stepInfo){
					 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
							 			if(activitytype.isArray()){
							 				for (final JsonNode objNode3 : activitytype){
								 				String activityTypeCheckActual = formatString1(objNode3);
									 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
									 			if(activityTypeCheckActual.contains("View")){
									 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"View is not there");
									 			}
									 			else if( activityTypeCheckActual.contains("Wait")){
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Wait is not there");

									 			}
									 			
													 			
									 		}
													 		
									 	}
							 		}
					 			}
					 		}
		 				} 
		 			}
				}
			 
			 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
			}
		
		Reporter.log("Sequence Detector Created Succesfuuly");

		 //update the sequence detector
		//TODO::pass id here
		 activities = new String[]{"View", "Wait", "Share"};
		 failities = new String[]{"Google Drive", "Google Drive", "Google Drive"} ;
		 sources = new String[]{"API", "API" , "API"} ;
		 users = new String[]{"__any", "__any", "__any"} ;
		 objects = new String[]{"__any", "__any", "__any"} ;
		
		 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		
		Reporter.log("response :::::"+response);
		
		DetectSequenceDto dsdto1 = new DetectSequenceDto();
		dsdto1.setLimit(20);
		dsdto1.setOffset(0);
		dsdto.setRequestType("list");
		Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
		ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
		StringEntity se1 =new StringEntity(utils.marshall(dsdto));
		response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
		responseBody = getResponseBody(response);
		Reporter.log("responseBody:::::"+responseBody);
		
		detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("Updated sequence detector"+detect_sequences);
		if (detect_sequences.isArray()) {
			for (final JsonNode objNode : detect_sequences) {
				String sequenceName =	getJSONValue(objNode.toString(), "name");
				if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
					JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
			 			if(groupInfo.isArray()){
			 				for (final JsonNode objNode1 : groupInfo){
			 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
						 		if(stepInfo.isArray()){
						 			for (final JsonNode objNode2 : stepInfo){
					 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
							 			if(activitytype.isArray()){
							 				for (final JsonNode objNode3 : activitytype){
								 				String activityTypeCheckActual = formatString1(objNode3);
									 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
									 			if(activityTypeCheckActual.contains("View")){
									 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"View is not there");
									 			}
									 			else if( activityTypeCheckActual.contains("Wait")){
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Wait is not there");

									 			}
									 			else{
										 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(3)), "Share is not there");
									 			}
													 			
												 		}
													 		
													 	}
										 		}
						 			}
						 		}
			 				} 
			 			}
				}
			 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		 }
		Reporter.log("Sequence Detector Updated Succesfully");

		//TODO::::Add validation here
			// delete sequence detector
			 dsd.deleteSequenceDetectors(name, suiteData);
			 Reporter.log("Sequence Detector Deleted Succesfully");
		
	}
	
	@Test
	public void uploadShareDelete() throws UnsupportedEncodingException, JAXBException, Exception{
	String name = UUID.randomUUID().toString();
	
	String[] activities = new String[]{"Upload", "Share"};
	String[] failities = new String[]{"__any", "__any"} ;
	String[] sources = new String[]{"__any", "__any"} ;
	String[] users = new String[]{"__any", "__any"} ;
	String[] objects = new String[]{"__any", "__any"} ;
	
	SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	

	
	String responseBody = getResponseBody(response);
	
	JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	//TODO:::add validation here
	Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

	
	//to get the list of sequence detector
	DetectSequenceDto dsdto = new DetectSequenceDto();
	dsdto.setLimit(20);
	dsdto.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	StringEntity se =new StringEntity(utils.marshall(dsdto));
	response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
	responseBody = getResponseBody(response);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

	//TODO:: Add validation here
	 String id=null;
	 if (detect_sequences.isArray()) {
		 for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Upload")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Upload is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Share is not there");

								 			}
								 			
												 			
								 		}
												 		
								 	}
						 		}
				 			}
				 		}
	 				} 
	 			}
			}
		 
		 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		}
	
	Reporter.log("Sequence Detector Created Succesfuuly");

	 //update the sequence detector
	//TODO::pass id here
	 activities = new String[]{"Upload", "Share","Delete"};
	 failities = new String[]{"__any", "__any", "__any"} ;
	 sources = new String[]{"__any", "__any", "__any"} ;
	 users = new String[]{"__any", "__any", "__any"} ;
	 objects = new String[]{"__any", "__any", "__any"} ;
	
	 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	 response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	
	Reporter.log("response :::::"+response);
	
	DetectSequenceDto dsdto1 = new DetectSequenceDto();
	dsdto1.setLimit(20);
	dsdto1.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
	ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
	StringEntity se1 =new StringEntity(utils.marshall(dsdto));
	response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
	responseBody = getResponseBody(response);
	Reporter.log("responseBody:::::"+responseBody);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	System.out.println("Updated sequence detector"+detect_sequences);
	if (detect_sequences.isArray()) {
		for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Upload")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Upload is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Share is not there");

								 			}
								 			else{
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)), "Delete is not there");
									 			
								 			}
												 			
											 		}
												 		
												 	}
									 		}
					 			}
					 		}
		 				} 
		 			}
			}
		 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
	 }
	Reporter.log("Sequence Detector Updated Succesfully");

	//TODO::::Add validation here
		// delete sequence detector
		 dsd.deleteSequenceDetectors(name, suiteData);
		 Reporter.log("Sequence Detector Deleted Succesfully");
	
}
	
	@Test
	public void shareWaitUnshare() throws UnsupportedEncodingException, JAXBException, Exception{
	String name = UUID.randomUUID().toString();
	
	String[] activities = new String[]{"Share", "Wait"};
	String[] failities = new String[]{"__any", "__any"} ;
	String[] sources = new String[]{"__any", "__any"} ;
	String[] users = new String[]{"__any", "__any"} ;
	String[] objects = new String[]{"__any", "__any"} ;
	
	SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	
	String responseBody = getResponseBody(response);
	
	JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	//TODO:::add validation here
	Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

	
	//to get the list of sequence detector
	DetectSequenceDto dsdto = new DetectSequenceDto();
	dsdto.setLimit(20);
	dsdto.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	StringEntity se =new StringEntity(utils.marshall(dsdto));
	response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
	responseBody = getResponseBody(response);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

	//TODO:: Add validation here
	 String id=null;
	 if (detect_sequences.isArray()) {
		 for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Share")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Share is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Wait")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Wait is not there");

								 			}
								 			
												 			
								 		}
												 		
								 	}
						 		}
				 			}
				 		}
	 				} 
	 			}
			}
		 
		 id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		}
	
	Reporter.log("Sequence Detector Created Succesfuuly");

	 //update the sequence detector
	//TODO::pass id here
	 activities = new String[]{"Share", "Wait", "Unshare"};
	failities = new String[]{"__any", "__any" , "__any"} ;
	 sources = new String[]{"__any", "__any" , "__any"} ;
	 users = new String[]{"__any", "__any" , "__any"} ;
	 objects = new String[]{"__any", "__any" , "__any"} ;
	
	 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	 response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	Reporter.log("response :::::"+response);
	
	DetectSequenceDto dsdto1 = new DetectSequenceDto();
	dsdto1.setLimit(20);
	dsdto1.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
	ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
	StringEntity se1 =new StringEntity(utils.marshall(dsdto));
	response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
	responseBody = getResponseBody(response);
	Reporter.log("responseBody:::::"+responseBody);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	System.out.println("Updated sequence detector"+detect_sequences);
	if (detect_sequences.isArray()) {
		for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Share")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Share is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Wait")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Wait is not there");

								 			}
								 			else{
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)), "Unshare is not there");
								 			}
												 			
											 		}
												 		
												 	}
									 		}
					 			}
					 		}
		 				} 
		 			}
			}
		id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		
		 
	 }
	Reporter.log("Sequence Detector Updated Succesfully");

	//TODO::::Add validation here
		// delete sequence detector
		 dsd.deleteSequenceDetectors(name, suiteData);
		 Reporter.log("Sequence Detector Deleted Succesfully");
	
}
	@Test
	public void createShareDelete() throws UnsupportedEncodingException, JAXBException, Exception{
	String name = UUID.randomUUID().toString();
	

	String[] activities = new String[]{"Create", "Share"};
	String[] failities = new String[]{"__any", "__any"} ;
	String[] sources = new String[]{"__any", "__any"} ;
	String[] users = new String[]{"__any", "__any"} ;
	String[] objects = new String[]{"__any", "__any"} ;
	
	SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	
	String responseBody = getResponseBody(response);
	
	JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	//TODO:::add validation here
	Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

	
	//to get the list of sequence detector
	DetectSequenceDto dsdto = new DetectSequenceDto();
	dsdto.setLimit(20);
	dsdto.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	StringEntity se =new StringEntity(utils.marshall(dsdto));
	response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
	responseBody = getResponseBody(response);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

	//TODO:: Add validation here
	 String id=null;
	 if (detect_sequences.isArray()) {
		 for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Create")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Create is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Share is not there");

								 			}
								 			
												 			
								 		}
												 		
								 	}
						 		}
				 			}
				 		}
	 				} 
	 			}
			}
		 
			id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		}
	
	Reporter.log("Sequence Detector Created Succesfuuly");

	 //update the sequence detector
	//TODO::pass id here
	 activities = new String[]{"Create", "Share", "Delete"};
		failities = new String[]{"__any", "__any" , "__any"} ;
		 sources = new String[]{"__any", "__any" , "__any"} ;
		 users = new String[]{"__any", "__any" , "__any"} ;
		 objects = new String[]{"__any", "__any" , "__any"} ;
		
		 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	Reporter.log("response :::::"+response);
	
	DetectSequenceDto dsdto1 = new DetectSequenceDto();
	dsdto1.setLimit(20);
	dsdto1.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
	ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
	StringEntity se1 =new StringEntity(utils.marshall(dsdto));
	response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
	responseBody = getResponseBody(response);
	Reporter.log("responseBody:::::"+responseBody);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	System.out.println("Updated sequence detector"+detect_sequences);
	if (detect_sequences.isArray()) {
		for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("Create")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"Create is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Share is not there");

								 			}
								 			else{
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)), "Delete is not there");
								 			}
												 			
											 		}
												 		
												 	}
									 		}
					 			}
					 		}
		 				} 
		 			}
			}
		id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
	 }
	Reporter.log("Sequence Detector Updated Succesfully");

	//TODO::::Add validation here
		// delete sequence detector
		 dsd.deleteSequenceDetectors(name, suiteData);
		 Reporter.log("Sequence Detector Deleted Succesfully");
	
}
	
	@Test
	public void viewShareDelete() throws UnsupportedEncodingException, JAXBException, Exception{
	String name = UUID.randomUUID().toString();
	
	String[] activities = new String[]{"View", "Share"};
	String[] failities = new String[]{"Box", "Box"} ;
	String[] sources = new String[]{"__any", "__any"} ;
	String[] users = new String[]{"__any", "__any"} ;
	String[] objects = new String[]{"__any", "__any"} ;
	
	SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	
	String responseBody = getResponseBody(response);
	
	JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	//TODO:::add validation here
	Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

	
	//to get the list of sequence detector
	DetectSequenceDto dsdto = new DetectSequenceDto();
	dsdto.setLimit(20);
	dsdto.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	StringEntity se =new StringEntity(utils.marshall(dsdto));
	response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
	responseBody = getResponseBody(response);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));

	//TODO:: Add validation here
	 String id=null;
	 if (detect_sequences.isArray()) {
		 for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("View")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"View is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			//Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)),"Share is not there");

								 			}
								 			
												 			
								 		}
												 		
								 	}
						 		}
				 			}
				 		}
	 				} 
	 			}
			}
		 
			id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
					 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
					 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
		}
	
	Reporter.log("Sequence Detector Created Succesfuuly");

	 //update the sequence detector
	//TODO::pass id here
	
	 activities = new String[]{"View", "Share", "Delete"};
		failities = new String[]{"Box", "Box" , "Box"} ;
		 sources = new String[]{"__any", "__any" , "__any"} ;
		 users = new String[]{"__any", "__any" , "__any"} ;
		 objects = new String[]{"__any", "__any" , "__any"} ;
		
		 sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		 
	Reporter.log("response :::::"+response);
	
	DetectSequenceDto dsdto1 = new DetectSequenceDto();
	dsdto1.setLimit(20);
	dsdto1.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto1));
	ElasticSearchLogs esLogs1 = new ElasticSearchLogs();
	StringEntity se1 =new StringEntity(utils.marshall(dsdto));
	response = esLogs1.detectsequences(restClient, buildCookieHeaders(), se1, suiteData);
	responseBody = getResponseBody(response);
	Reporter.log("responseBody:::::"+responseBody);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	System.out.println("Updated sequence detector"+detect_sequences);
	if (detect_sequences.isArray()) {
		for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){
				JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
		 			if(groupInfo.isArray()){
		 				for (final JsonNode objNode1 : groupInfo){
		 				JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
					 		if(stepInfo.isArray()){
					 			for (final JsonNode objNode2 : stepInfo){
				 				JsonNode activitytype = new ObjectMapper().readTree(getJSONValue(objNode2.toString(), "Activity_type"));
						 			if(activitytype.isArray()){
						 				for (final JsonNode objNode3 : activitytype){
							 				String activityTypeCheckActual = formatString1(objNode3);
								 			System.out.println("activityTypeCheck"+activityTypeCheckActual);
								 			if(activityTypeCheckActual.contains("View")){
								 				Assert.assertEquals(activityTypeCheckActual,formattingString(sdInput.getActivityTypeMap().get(0)),"View is not there");
								 			}
								 			else if( activityTypeCheckActual.contains("Share")){
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(1)),"Share is not there");

								 			}
								 			else{
									 			Assert.assertEquals(activityTypeCheckActual, formattingString(sdInput.getActivityTypeMap().get(2)), "Delete is not there");
								 			}
												 			
											 		}
												 		
												 	}
									 		}
					 			}
					 		}
		 				} 
		 			}
			}
		id = validateCreateAndUpdateSequenceDetector(name, detect_sequences, id, sdInput.getThreshold(), sdInput.getWindow(), sdInput.getActivityTypeMap(), 
				 sdInput.getFacilityMap(), sdInput.getSourcesMap(),
				 sdInput.getUserMap(), sdInput.getObjectTypeMap() );
	 }
	Reporter.log("Sequence Detector Updated Succesfully");

	//TODO::::Add validation here
		// delete sequence detector
		 dsd.deleteSequenceDetectors(name, suiteData);
		 Reporter.log("Sequence Detector Deleted Succesfully");
	
}


	
	private String validateCreateAndUpdateSequenceDetector(String name, JsonNode detect_sequences, String id, int threshold, int window,
			Map<Integer, String[] > activityTypeMap, Map<Integer, String[] > facilityMap, Map<Integer, String[] > sourcesMap, 
			Map<Integer, String[] > userMap, Map<Integer, String[] > objectTypeMap ) throws IOException, JsonProcessingException {
				return id;/*

		for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			if(name.equalsIgnoreCase(sequenceName.replace("\"", ""))){

				id  = getJSONValue(objNode.toString(), "id").replace("\"", "");
				Reporter.log("Id is ::::"+id);
				System.out.println("Id is --"+id);

				int k=sequenceName.length();
				String Sequencename1 = sequenceName.substring(1, k-1);
				Assert.assertEquals(Sequencename1, name, "Sequence are not created");

					 JsonNode groupInfo = new ObjectMapper().readTree(getJSONValue(objNode.toString(), "groups"));
				 		if(groupInfo.isArray()){
				 			for (final JsonNode objNode1 : groupInfo){

					 			Assert.assertEquals(Integer.parseInt(getJSONValue(objNode1.toString(), "threshold")), threshold);
					 			Assert.assertEquals(Integer.parseInt(getJSONValue(objNode1.toString(), "window")), window);		
							 
					 			JsonNode stepInfo = new ObjectMapper().readTree(getJSONValue(objNode1.toString(), "steps"));
								 	if(stepInfo.isArray()){
								 		for (final JsonNode objNode2 : stepInfo){
								 			
										 			String activityTypeActual = formattingString1(objNode2,"Activity_type");
										 			String facilityTypeActual = formattingString1(objNode2,"facility");
										 			String sourceMapActual= formattingString1(objNode2,"source");
										 			String objectTypeActual= formattingString1(objNode2,"Object_type");
										 			String userTypeActual= formattingString1(objNode2,"user");
										 			
										 			
									 				if(activityTypeActual.contains("Download")){	
										 					
										 					String activityTypeExpected = formattingString(1, activityTypeMap);
															String facilityTypeExpected =formattingString(1,facilityMap);
															String sourceMapExpected = formattingString(1, sourcesMap);
															String userMapExpected = formattingString(1,userMap);
															String objectTypeMapExpected = formattingString(1,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Download is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Download ");
															
										 			
										 			}
														else if(activityTypeActual.contains("Upload")){
															
															String activityTypeExpected = formattingString(2, activityTypeMap);
															String facilityTypeExpected =formattingString(2,facilityMap);
															String sourceMapExpected = formattingString(2, sourcesMap);
															String userMapExpected = formattingString(2,userMap);
															String objectTypeMapExpected = formattingString(2,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "upload is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("your in upload");

														}
														
														else if(activityTypeActual.contains("Add")){
															String activityTypeExpected = formattingString(3, activityTypeMap);
															String facilityTypeExpected =formattingString(3,facilityMap);
															String sourceMapExpected = formattingString(3, sourcesMap);
															String userMapExpected = formattingString(3,userMap);
															String objectTypeMapExpected = formattingString(3,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Add is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Add");
														}
									 					
														else if(activityTypeActual.contains("Create")){
															String activityTypeExpected = formattingString(8, activityTypeMap);
															String facilityTypeExpected =formattingString(8,facilityMap);
															String sourceMapExpected = formattingString(8, sourcesMap);
															String userMapExpected = formattingString(8,userMap);
															String objectTypeMapExpected = formattingString(8,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Add is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Create");
										 		
														}
														else if(activityTypeActual.contains("Delete")){
															String activityTypeExpected = formattingString(7, activityTypeMap);
															String facilityTypeExpected =formattingString(7,facilityMap);
															String sourceMapExpected = formattingString(7, sourcesMap);
															String userMapExpected = formattingString(7,userMap);
															String objectTypeMapExpected = formattingString(7,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Delete is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Delete");
										 		
														}
														else if(activityTypeActual.contains("Share")){
															String activityTypeExpected = formattingString(6, activityTypeMap);
															String facilityTypeExpected =formattingString(6,facilityMap);
															String sourceMapExpected = formattingString(6, sourcesMap);
															String userMapExpected = formattingString(6,userMap);
															String objectTypeMapExpected = formattingString(6,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Share is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Share");
										 		
														}
														else if(activityTypeActual.contains("Wait")){
															String activityTypeExpected = formattingString(5, activityTypeMap);
															String facilityTypeExpected =formattingString(5,facilityMap);
															String sourceMapExpected = formattingString(5, sourcesMap);
															String userMapExpected = formattingString(5,userMap);
															String objectTypeMapExpected = formattingString(5,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "Wait is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in Wait");
										 		
														}
														else if(activityTypeActual.contains("View")){
															String activityTypeExpected = formattingString(4, activityTypeMap);
															String facilityTypeExpected =formattingString(4,facilityMap);
															String sourceMapExpected = formattingString(4, sourcesMap);
															String userMapExpected = formattingString(4,userMap);
															String objectTypeMapExpected = formattingString(4,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "View is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in View");
										 		
														}
														else if(activityTypeActual.contains("Unshare")){
															String activityTypeExpected = formattingString(10, activityTypeMap);
															String facilityTypeExpected =formattingString(10,facilityMap);
															String sourceMapExpected = formattingString(10, sourcesMap);
															String userMapExpected = formattingString(10,userMap);
															String objectTypeMapExpected = formattingString(10,objectTypeMap);
															
															Assert.assertEquals(activityTypeActual, activityTypeExpected, "UnShare is not there");
															Assert.assertEquals(facilityTypeActual, facilityTypeExpected);
															Assert.assertEquals(sourceMapActual, sourceMapExpected);
															Assert.assertEquals(objectTypeActual, objectTypeMapExpected);
															Assert.assertEquals(userTypeActual, userMapExpected);
															System.out.println("ur in UnShare");
										 		
														}
														
									 			}
								 		
							 			}

				 				}
				 		}
			  break;
				
			}
			
		}
		return id;
	*/}


	private String formattingString1(final JsonNode objNode2, String name) {
		String typeActual= getJSONValue(objNode2.toString(), name );
		String typeActual1=typeActual.replace("[", "").replace("]", "");
		String typeActualFormatted = typeActual1.replace("\"", "");
		return typeActualFormatted;
	}


	private String formattingString(String[] actyvityType) {
		String map1 = actyvityType[0];
		String mapFormated= map1.replace("[", "").replace("]", "");
		return mapFormated;
	}
	
	private String formatString1(final JsonNode objNode) {
		String activityTypeCheckActual=objNode.toString(); 
		int i= activityTypeCheckActual.length();
		String typeActualFormatted = activityTypeCheckActual.substring(1, i-1);
		return typeActualFormatted;
	}
	
	private String formattingString(int i,Map<Integer, String[]> map) {
		String map1 = Arrays.toString(map.get(i));
		String mapFormated= map1.replace("[", "").replace("]", "");
		return mapFormated;
	}

}
	
