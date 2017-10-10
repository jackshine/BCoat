package com.elastica.beatle.tests.detect;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectBBITests_GW extends  DetectUtils{
	
	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	DetectCommonutils dcUtils = new DetectCommonutils();
	
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	
	@BeforeClass()
	public void beforeClass() throws Exception {
		
	 props =	utils.getPropertyValues();
	String responseBody =  getListOfIncidents();
	JsonNode jnode = unmarshall(responseBody, JsonNode.class);
	if (jnode.isArray()) {
	    for (final JsonNode objNode : jnode) {
	    	String report_id = getJSONValue(objNode.toString(), "id").toString().replace("\"", "");
	    	String facilty = getJSONValue(objNode.toString(), "f").toString().replace("\"", "");
	    	String email = getJSONValue(objNode.toString(), "e").toString().replace("\"", "");
	    	String index = getJSONValue(objNode.toString(), "index").toString().replace("\"", "");
	    	String source = getJSONValue(objNode.toString(), "src").toString().replace("\"", "");
	    	String attr_set = getJSONValue(objNode.toString(), "ats").toString().replace("\"", "");
	    	String Activity_type = getJSONValue(objNode.toString(), "at").toString().replace("\"", "");
	    	String severity = getJSONValue(objNode.toString(), "s").toString().replace("\"", "");
	    	String ObjectType = getJSONValue(objNode.toString(), "ot").toString().replace("\"", "");
	    	String cdrs = getJSONValue(objNode.toString(), "cdrs").toString().replace("\"", "");
	    	
	    	updateLog(report_id, severity, ObjectType, email, Activity_type, index, facilty,cdrs, source, attr_set);
	    	
	       }
	    }
		
	}
	
	@Test(description = "This test operates on real API data, and generates BBI large upload incidents.")
	public void large_Uploads_Tests(Method method, String fileName, AttributeBean attributeBean) throws Exception {
		
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_LARGE_UPLOAD";
		
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		}
		

		List<String> messageList = new ArrayList<>();
		for (int j = 1; j < 45; j++) {
			
			if(11>=j){
			dcUtils.replayLogs("Test,AFolder,upload_txt.log", suiteData, suiteData.getSaasAppUsername());
			messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
			Thread.sleep(1*45*1000);
			}else if(j>11&&22>=j){
				dcUtils.replayLogs("Test,AFolder,upload_txt.log", suiteData, suiteData.getSaasAppUsername());
				messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
				Thread.sleep(1*60*1000);
				
			}else if(j>22&&33>=j){
				dcUtils.replayLogs("Test,AFolder,upload_txt.log", suiteData, suiteData.getSaasAppUsername());
				messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
				Thread.sleep(1*90*1000);
				
			}else if(j>=33){
				dcUtils.replayLogs("Test,AFolder,upload_txt.log", suiteData, suiteData.getSaasAppUsername());
				messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
				Thread.sleep(1*1*1000);
				
			}
			
			
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList,20,2);
		
		
		Thread.sleep(60000);
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioi_Code);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
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
    	cal.add(Calendar.MINUTE, -5);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		try {
		
				String payload = esQueryBuilder.getSearchQueryForDetect(strDateTimeFrom,
						strDateTimeTo);
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Reporter.log(" validateIncidents::::  Response::::   " + responseBody, true);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				List<String> iois = new ArrayList<>();
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String ioi = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	iois.add(ioi);
				    	
				       }
				    }
		
				Assert.assertTrue(iois.contains(ioicode), "incident not listed");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return responseBody;
	}

	
	
	private void Log(Method method, String name, String user, String userName) {
		Reporter.log("       ##################:::::::::::###################     ");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log(" This test  will create squence detector , inject activities and verify the incident::: name of squence detector:::"
				+ ": "+name+"  ::::user::::  "+user+"  :::::: user name:::: "+userName,true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #################::::::::::::####################    ");
	}

}
