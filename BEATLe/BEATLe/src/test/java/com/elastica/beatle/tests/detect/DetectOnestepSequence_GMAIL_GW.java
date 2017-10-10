package com.elastica.beatle.tests.detect;

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

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectGWDataSource;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.Groups;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.detect.dto.Source;
import com.elastica.beatle.detect.dto.Steps;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.securlets.SAASAPP;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectOnestepSequence_GMAIL_GW extends  DetectUtils{
	
	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	DetectCommonutils dcUtils = new DetectCommonutils();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	
	@BeforeClass()
	public void beforeClass()  {
		
	 clearIncidents();
		
	}

	
	@BeforeClass
	public void deleteallSDs(){
		try {
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] deleteEmaildataprovider() throws UnsupportedEncodingException, JAXBException, Exception {

		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Email"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_EMAIL, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_EMAIL_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_EMAIL_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_EMAIL_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("Any,Gmail,Trash.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			Thread.sleep(3*60*1000);
				return new Object[][]{
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DELETE_EMAIL, messageList},
			
	{ SequenceDetectorConstants.DELETE_EMAIL_TWO, messageList},
		
	{ SequenceDetectorConstants.DELETE_EMAIL_THREE , messageList},
	
	{ SequenceDetectorConstants.DELETE_EMAIL_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="deleteEmaildataprovider")
	public void deleteEmailSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DownLoadAttachmentdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {

		String[] activities = new String[]{"Download"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_ATTACH, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_ATTACH_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_ATTACH_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_ATTACH_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("File,Download_attachment.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			Thread.sleep(3*60*1000);
				return new Object[][]{
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOWNLOAD_ATTACH, messageList},
			
	{ SequenceDetectorConstants.DOWNLOAD_ATTACH_TWO, messageList},
		
	{ SequenceDetectorConstants.DOWNLOAD_ATTACH_THREE , messageList},
	
	{ SequenceDetectorConstants.DOWNLOAD_ATTACH_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DownLoadAttachmentdataprovider")
	public void DownLoadAttachmentSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] UploadAttachmentdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {

		String[] activities = new String[]{"Upload"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_ATTACH, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_ATTACH_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_ATTACH_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_ATTACH_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("File,Upload_attachment0.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			Thread.sleep(3*60*1000);
				return new Object[][]{
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UPLOAD_ATTACH, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_ATTACH_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_ATTACH_THREE , messageList},
	
	{ SequenceDetectorConstants.UPLOAD_ATTACH_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="UploadAttachmentdataprovider")
	public void uploadAttachmentSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] sendEmaildataprovider() throws UnsupportedEncodingException, JAXBException, Exception {

		String[] activities = new String[]{"Send"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Email"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SEND, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SEND_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SEND_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SEND_ONE, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("Any,Gmail,DCompose,Compose.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			Thread.sleep(3*60*1000);
				return new Object[][]{
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SEND, messageList},
			
	{ SequenceDetectorConstants.SEND_ONE, messageList},
		
	{ SequenceDetectorConstants.SEND_TWO , messageList},
	
	{ SequenceDetectorConstants.SEND_THREE, messageList},
			
		};
	}
	
	
	@Test(dataProvider="sendEmaildataprovider")
	public void sendEmailSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
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
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	
		HttpResponse response;
		Boolean found_ioi =false;
		int ite=1;
		for (; ite<=5; ite++) {
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
				    	System.out.println("fond ioicode:::::::: "+ioi_found);
				    	System.out.println("ioicode:::::::: "+ioicode);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 15 * 1000);	//wait for 1 minute.
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
