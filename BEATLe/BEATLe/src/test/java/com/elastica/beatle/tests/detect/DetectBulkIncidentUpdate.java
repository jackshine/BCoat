package com.elastica.beatle.tests.detect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;

public class DetectBulkIncidentUpdate extends  DetectUtils {
	
	
	
	@Test(priority=1)
	public void testBulkUpdateMessage(Method method) throws UnsupportedEncodingException, Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
		
		String responseBody =  getListOfIncidentsforDays(1);
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		String jsonPayload = null;
		List<String> incidentList = new ArrayList<>();
		List<String> incidentIDlist = new ArrayList<>();
		
		if (jnode.isArray()) {
			System.out.println("Count of incidents :::::"+jnode.size());
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
		    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
		    	incidentIDlist.add(report_id);
		    	
		    String jsonObject = 	"{\"index\":\""+index+"\",\"type\":\"elastica_state\",\"id\":\""+report_id+"\",\"notes\":\"bulk comment update\"}";
		    	
		    incidentList.add(jsonObject);
		    
		    	}
		    	
		       }
		
		int listSize = incidentList.size();
			
		String delim = "";
		StringBuilder sb = new StringBuilder();
		for (String i : incidentList) {
		    sb.append(delim).append(i);
		    delim = ",";
		}
			 jsonPayload = "{\"source\":{\"source\":["+sb+"]}}";

				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 HttpResponse	 response = esLogs.bulkIncidentUpdate(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(jsonPayload),suiteData);
				 Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				 Logger.info(" List of Incidents::::::  Response::::   " + responseBody);
				 JsonNode output = unmarshall(responseBody, JsonNode.class);
				 String message = getJSONValue(output.toString(), "message").toString().replace("\"", "");
				 Assert.assertEquals(message, "All incidents were successfully updated", "Message is not as expected");
				 String action_status = getJSONValue(output.toString(), "action_status").toString().replace("\"", "");
				 Assert.assertEquals(action_status, "true", "action_status is not true");
				 String error = getJSONValue(output.toString(), "__error").toString().replace("\"", "");
				 Assert.assertEquals(error, "", "error should be empty");
				 Logger.info("      Waiting 2 minutes to refresh the incident list: ");
				 Thread.sleep(1 * 60 * 1000);

					 responseBody =  getListOfIncidentsforDays(1);
					 jnode = unmarshall(responseBody, JsonNode.class);
					 
					 if (jnode.isArray()) {/*
							System.out.println("Count of incidents :::::"+jnode.size());
						    for (final JsonNode objNode : jnode) {
						    	String notes = getJSONValue(objNode.toString(), "notes").toString().replace("\"", "");
						    	JsonNode	jnode1 = unmarshall(notes, JsonNode.class);
						    	if(jnode1.isArray()){
						    		for (final JsonNode objNode1 : jnode1) {
						    			String note = getJSONValue(objNode1.toString(), "note").toString().replace("\"", "");
						    			 Assert.assertEquals(note, "bulk comment update", "comment is not as expected :::  note ::: "+note);
						    		}
						    	}
						    
						    	}
						    	
						       */}
					
					
		
		Logger.info("Execution Completed - Test Case Name: " + method.getName());	    
	}
	
	
	
	@Test(priority=2)
	public void testBulkVerifyIncident(Method method) throws UnsupportedEncodingException, Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
		
		String responseBody =  getListOfIncidentsforDays(1);
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		String jsonPayload = null;
		List<String> incidentList = new ArrayList<>();
		List<String> incidentIDlist = new ArrayList<>();
		
		if (jnode.isArray()) {
			System.out.println("Count of incidents :::::"+jnode.size());
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
		    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
		    	incidentIDlist.add(report_id);
		    	
		    String jsonObject = 	"{\"index\":\""+index+"\",\"type\":\"elastica_state\",\"id\":\""+report_id+"\",\"notes\":\"Cleared Alert\",\"anomaly_status\":0,\"updated_sev\":\"informational\",\"event_type\":\"AnomalyReport\",\"severity\":\""+severity+"\"}";
		    	
		    incidentList.add(jsonObject);
		    
		    	}
		    	
		       }
		
		int listSize = incidentList.size();
		if(listSize>2){
			
			 jsonPayload = "{\"source\":{\"source\":["+incidentList.get(0)+","+incidentList.get(1)+"]}}";
			 List<String> incidentIDlistafterclearingIncidents = validateBulkVerifyIncidents(jsonPayload, incidentIDlist,
					listSize);
				Assert.assertFalse(incidentIDlistafterclearingIncidents.contains(incidentIDlist.get(1)), incidentIDlist.get(1) +" is not cleared");
			
		}else{
			
			jsonPayload = "{\"source\":{\"source\":["+incidentList.get(0)+"]}}";
			 List<String> incidentIDlistafterclearingIncidents = validateBulkVerifyIncidents(jsonPayload, incidentIDlist,
						listSize);
		}
		
		Logger.info("Execution Completed - Test Case Name: " + method.getName());	    
	}
	
	
	
	
	@Test(priority=3)
	public void testBulkVerifyAllIncident(Method method) throws UnsupportedEncodingException, Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
		
		String responseBody =  getListOfIncidentsforDays(1);
		JsonNode jnode = unmarshall(responseBody, JsonNode.class);
		String jsonPayload = null;
		List<String> incidentList = new ArrayList<>();
		List<String> incidentIDlist = new ArrayList<>();
		
		if (jnode.isArray()) {
			System.out.println("Count of incidents :::::"+jnode.size());
		    for (final JsonNode objNode : jnode) {
		    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
		    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
		    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
		    	incidentIDlist.add(report_id);
		    	
		    String jsonObject = 	"{\"index\":\""+index+"\",\"type\":\"elastica_state\",\"id\":\""+report_id+"\",\"notes\":\"Cleared Alert\",\"anomaly_status\":0,\"updated_sev\":\"informational\",\"event_type\":\"AnomalyReport\",\"severity\":\""+severity+"\"}";
		    	
		    incidentList.add(jsonObject);
		    
		    	}
		    	
		       }
		
		int listSize = incidentList.size();
			
		String delim = "";
		StringBuilder sb = new StringBuilder();
		for (String i : incidentList) {
		    sb.append(delim).append(i);
		    delim = ",";
		}
			 jsonPayload = "{\"source\":{\"source\":["+sb+"]}}";

				ElasticSearchLogs esLogs = new ElasticSearchLogs();
				 HttpResponse	 response = esLogs.bulkIncidentUpdate(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(jsonPayload),suiteData);
				 Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				 Logger.info(" List of Incidents::::::  Response::::   " + responseBody);
				 JsonNode output = unmarshall(responseBody, JsonNode.class);
				 String message = getJSONValue(output.toString(), "message").toString().replace("\"", "");
				 Assert.assertEquals(message, "All incidents were successfully updated", "Message is not as expected");
				 String action_status = getJSONValue(output.toString(), "action_status").toString().replace("\"", "");
				 Assert.assertEquals(action_status, "true", "action_status is not true");
				 String error = getJSONValue(output.toString(), "__error").toString().replace("\"", "");
				 Assert.assertEquals(error, "", "error should be empty");
				 Logger.info("      Waiting 2 minutes to refresh the incident list: ");
				 Thread.sleep(1 * 60 * 1000);

					 responseBody =  getListOfIncidentsforDays(1);
					 jnode = unmarshall(responseBody, JsonNode.class);
					
					 Assert.assertEquals(jnode.size(),0, "All incidents are not cleared");
		
		Logger.info("Execution Completed - Test Case Name: " + method.getName());	    
	}

	
	
	
	
	private List<String> validateBulkVerifyIncidents(String jsonPayload, List<String> incidentIDlist, int listSize)
			throws Exception, UnsupportedEncodingException, JAXBException, InterruptedException {
		String responseBody;
		JsonNode jnode;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		 HttpResponse	 response = esLogs.bulkIncidentUpdate(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(jsonPayload),suiteData);
		 Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		 responseBody = getResponseBody(response);
		 Logger.info(" List of Incidents::::::  Response::::   " + responseBody);
		 JsonNode output = unmarshall(responseBody, JsonNode.class);
		 String message = getJSONValue(output.toString(), "message").toString().replace("\"", "");
		 Assert.assertEquals(message, "All incidents were successfully updated", "Message is not as expected");
		 String action_status = getJSONValue(output.toString(), "action_status").toString().replace("\"", "");
		 Assert.assertEquals(action_status, "true", "action_status is not true");
		 String error = getJSONValue(output.toString(), "__error").toString().replace("\"", "");
		 Assert.assertEquals(error, "", "error should be empty");
		 Logger.info("      Waiting 2 minutes to refresh the incident list: ");
		 Thread.sleep(1 * 60 * 1000);

			 responseBody =  getListOfIncidentsforDays(1);
			 jnode = unmarshall(responseBody, JsonNode.class);
			 List<String> incidentIDlistafterclearingIncidents = new ArrayList<>();
			 
			if (jnode.isArray()) {
				 Logger.info("Count of incidents :::::"+jnode.size());
				int  incidentCountAfterclearing = jnode.size();
				Assert.assertTrue(listSize>incidentCountAfterclearing, "incident size is not less than actual incident size::: size before clearing::: "+listSize+"  size after clearing incidets:::  "+incidentCountAfterclearing);
				 for (final JsonNode objNode : jnode) {
				    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
				    	incidentIDlistafterclearingIncidents.add(report_id);
				 }
			
			}
			
			Assert.assertFalse(incidentIDlistafterclearingIncidents.contains(incidentIDlist.get(0)), incidentIDlist.get(0) +" is not cleared");
		return incidentIDlistafterclearingIncidents;
	}
		
		
}
