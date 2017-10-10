package com.elastica.beatle.tests.detect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectBoxBBITests_API extends DetectUtils{

	
	
	
	@Test()    
	public void box_large_upload_BBI_Test(Method method) throws Exception{
		
		try {
			//TODO:: Check for Profile
			String IOI_code = IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString();
			Log(method, IOI_code, suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername());
			validateIncidents(IOI_code);
				//Thread.sleep(60000);
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
		} catch (Exception e) {
			throw e;
		}finally {
			Reporter.log("       #####################################");
			Reporter.log(" ");
			Reporter.log("Execution Completed - Test Case Name::::: " + method.getName(), true);
			Reporter.log(" ");
			Reporter.log("       #####################################");
			
			
		}
		
	}
	
	
	
	@Test()    
	public void box_large_download_BBI_Test(Method method) throws Exception{
		try {
			String IOI_code = IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString();
			Log(method, IOI_code, suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername());
			
			validateIncidents(IOI_code);
			
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
			
			
			
		} catch (Exception e) {
			throw e;
		}finally {
			
		}
		
	}
	
	
	@Test
	public void box_large_Share_BBI_Test(Method method) throws Exception{
		try {
			
			
			validateIncidents(IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString());
			
				//Thread.sleep(60000);
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
			
			
			
		} catch (Exception e) {
			throw e;
		}finally {
			
		}
		
	}
	
	@Test
	public void box_lagre_delete_BBI_Test(Method method) throws Exception{
		try {
			
			
			validateIncidents(IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString());
			
				//Thread.sleep(60000);
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
			
			
			
		} catch (Exception e) {
			throw e;
		}finally {
			
		}
		
	}
	
	
	//@Test
	public void box_Frequent_Share_BBI_Test(Method method) throws Exception{
		try {
			
			validateIncidents(IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString());
			
				//Thread.sleep(60000);
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			
			
			
			
		} catch (Exception e) {
			throw e;
		}finally {
			
		}
		
	}
	
	
	


	

	
	public String validateIncidents(String ioicode) {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -5);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=10; ite++) {
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Reporter.log("found ioicode:::::::: "+ioi_found, true);
				    	Reporter.log("Expected ioicode:::::::: "+ioicode, true);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 60 * 1000);	//wait for 1 minute.
						Reporter.log("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Reporter.log("Going to Assert after " + ite + "Minutes of waiting");
		
		Reporter.log("Expected IOI_Code:::::: " + ioicode + " not found in the incident list",true);
		Assert.assertTrue(found_ioi, "incident not listed");
		
		return responseBody;
	}
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -3);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		
			for (int i = 1; i<=10; i++) {
				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
						strDateTimeTo + ":59:59.999Z", query, "Elastica", 1000, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(),
						suiteData.getUsername());
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				Reporter.log("responseBody:::::::::::::: "+responseBody, true);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Reporter.log("Activity log message on invetsigate page::::::::::  "+message, true);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				Reporter.log("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min", true);
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				break;
				}
			}
			if(messageCount!=count){
			Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}
	
	private void Log(Method method, String name, String user, String userName) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
			Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + userName+"  user::::: "+user, true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}
	
	
	/*//			if(suiteData.getEnvironmentName().equalsIgnoreCase("eoe")){
//	boolean profileCreated = 	verifyProfile(suiteData.getSaasAppUsername(), suiteData.getSaasApp() ,IOI_code);
//		
//	if(!profileCreated){
//		List<String> messageList =	createProfile(suiteData.getSaasAppUsername(), IOI_code, attributeBean);
//		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//	}
//	
//}else{
//	List<String> messageList =	createProfile(suiteData.getSaasAppUsername(), IOI_code, attributeBean);
//	
//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//}
*/

	
}
