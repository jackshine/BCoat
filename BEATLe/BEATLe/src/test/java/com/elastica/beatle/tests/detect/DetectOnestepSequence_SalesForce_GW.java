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

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.codehaus.jackson.map.ObjectMapper;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectOnestepSequence_SalesForce_GW extends DetectUtils{
	
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
	DetectSequenceDetector dsd = new DetectSequenceDetector();
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
	
	@DataProvider
	public Object[][] CreateDataProvider() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Account,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE_ONE_SF,  messageList   },
			{ SequenceDetectorConstants.CREATE_TWO_SF,    messageList   },
			{ SequenceDetectorConstants.CREATE_THREE_SF,     messageList   },
			{ SequenceDetectorConstants.CREATE_FOUR_SF,   messageList   },
				};
	}
	
	@Test(dataProvider="CreateDataProvider")
	public void AccountCreateSDGWTest(Method method,String SequenceName, List<String> messageList) throws Exception{
			
		String user = suiteData.getSaasAppUsername();
		Log(method, SequenceName, user, user );
			
			
			
			String ioicode = TOO_MANY_SEQUENCE+SequenceName;
			
			validateIncidents(ioicode);
			 
            //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
		
		
	@DataProvider
	public Object[][] DeleteDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("file,salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.DELETE_TWO_SF, messageList},    
			{ SequenceDetectorConstants.DELETE_THREE_SF, messageList},  
			{ SequenceDetectorConstants.DELETE_FOUR_SF, messageList},   
				};
	}
	
	@Test(dataProvider="DeleteDataProvider")
	public void DeleteSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );	
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		Thread.sleep(60000);
	  // verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioicode);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] DownloadDataProvider() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		String[] activities = new String[]{"Download"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("File,Salesforce,download.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.DOWNLOAD_ONE_SF, messageList},   
			{ SequenceDetectorConstants.DOWNLOAD_TWO_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_THREE_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_FOUR_SF, messageList}, 
				};
	}
	
	@Test(dataProvider="DownloadDataProvider")
	public void DownloadSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

	
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );

		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		//Thread.sleep(60000);
		// verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioicode);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] EditDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("file,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.EDIT_ONE_SF, messageList},      
			{ SequenceDetectorConstants.EDIT_TWO_SF,  messageList},     
			{SequenceDetectorConstants.EDIT_THREE_SF ,  messageList},   
			{SequenceDetectorConstants.EDIT_FOUR_SF,    messageList},   
				};
	}
	
	@Test(dataProvider="EditDataProvider")
	public void EditSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		Thread.sleep(60000);
		//verifyDetectedUser(suiteData.getSaasAppUsername());
		
		validateIncidents(ioicode);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] ExportDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		String[] activities = new String[]{"Export"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPORT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPORT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPORT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPORT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,export.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.EXPORT_ONE_SF,  messageList},    
			{ SequenceDetectorConstants.EXPORT_TWO_SF,  messageList},    
			{ SequenceDetectorConstants.EXPORT_THREE_SF,  messageList},    
			{ SequenceDetectorConstants.EXPORT_FOUR_SF, messageList},     
				};
	}
	@Test(dataProvider="ExportDataProvider")
	public void ExportSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
				
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	
	@DataProvider
	public Object[][] RunDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		String[] activities = new String[]{"Run"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.RUN_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.RUN_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RUN_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RUN_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,run.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.RUN_ONE_SF,  messageList},    
			{ SequenceDetectorConstants.RUN_TWO_SF,  messageList},    
			{ SequenceDetectorConstants.RUN_THREE_SF,  messageList},  
			{ SequenceDetectorConstants.RUN_FOUR_SF,   messageList},  
				};
	}
	
	@Test(dataProvider="RunDataProvider")
	public void RunSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] ShareDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		String[] activities = new String[]{"Share"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.SHARE_ONE_SF, messageList},      
			{ SequenceDetectorConstants.SHARE_TWO_SF, messageList},    
			{ SequenceDetectorConstants.SHARE_THREE_SF, messageList},   
			{ SequenceDetectorConstants.SHARE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ShareDataProvider")
	public void ShareSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{
	
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	@DataProvider
	public Object[][] UploadDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Upload"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("File,Salesforce,upload.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_ONE_SF, messageList},    
			{ SequenceDetectorConstants.UPLOAD_TWO_SF, messageList},    
			{ SequenceDetectorConstants.UPLOAD_THREE_SF, messageList},   
			{ SequenceDetectorConstants.UPLOAD_FOUR_SF, messageList},    
				};
	}
	
	@Test(dataProvider="UploadDataProvider")
	public void UploadSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] ViewDataProvider()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Account,Salesforce,view.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.VIEW_ONE_SF, messageList},   
			{ SequenceDetectorConstants.VIEW_TWO_SF, messageList},   
			{ SequenceDetectorConstants.VIEW_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.VIEW_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ViewDataProvider")
	public void ViewSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
				/*Test cases according to Sheet*/
	
	@DataProvider
	public Object[][] AccountDeleteDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Account,Salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_TWO_SF, messageList},   
			{ SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.DELETE_ACCOUNT_ONE_SF_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="AccountDeleteDataProvider1")
	public void DeleteSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] AccountViewDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Account"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Account,Salesforce,view.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_TWO_SF, messageList},   
			{ SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.VIEW_ACCOUNT_ONE_SF_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="AccountViewDataProvider1")
	public void AccountViewSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@DataProvider
	public Object[][] ContactEditDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Contact"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_EDIT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_EDIT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_EDIT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_EDIT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Contact,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CONTACT_EDIT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_EDIT_TWO_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_EDIT_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.CONTACT_EDIT_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ContactEditDataProvider1")
	public void ContactEditSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ContactViewDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Contact"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_VIEW_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_VIEW_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_VIEW_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_VIEW_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Contact,Salesforce,view.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CONTACT_VIEW_ONE_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_VIEW_TWO_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_VIEW_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.CONTACT_VIEW_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ContactViewDataProvider1")
	public void ContactViewSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ContactCreateDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Contact"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_CREATE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_CREATE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_CREATE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Contact,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CONTACT_CREATE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_CREATE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_CREATE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.CONTACT_CREATE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ContactCreateDataProvider1")
	public void CreateSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ContactDeleteDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Contact"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CONTACT_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Contact,Salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CONTACT_DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_DELETE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.CONTACT_DELETE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.CONTACT_DELETE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ContactDeleteDataProvider1")
	public void ContactDeleteSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] LeadCreateDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Lead"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_CREATE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_CREATE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_CREATE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Lead,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.LEAD_CREATE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_CREATE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_CREATE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.LEAD_CREATE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="LeadCreateDataProvider1")
	public void LeadCreateSDGWTest(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] LeadEditDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Lead"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_EDIT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_EDIT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_EDIT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_EDIT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Lead,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.LEAD_EDIT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_EDIT_TWO_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_EDIT_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.LEAD_EDIT_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="LeadEditDataProvider1")
	public void LeadEditSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@DataProvider
	public Object[][] LeadDeleteDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Lead"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LEAD_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Lead,Salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.LEAD_DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_DELETE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.LEAD_DELETE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.LEAD_DELETE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="LeadDeleteDataProvider1")
	public void LeadDeleteSDGWTest(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] OppurtunityCreateDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Opportunity"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_CREATE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_CREATE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_CREATE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Opportunity,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.OPPORTUNITY_CREATE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_CREATE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_CREATE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.OPPORTUNITY_CREATE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="OppurtunityCreateDataProvider1")
	public void OppurtunityCreateSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] OppurtunityEditDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Opportunity"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_EDIT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_EDIT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_EDIT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_EDIT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Opportunity,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.OPPORTUNITY_EDIT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_EDIT_TWO_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_EDIT_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.OPPORTUNITY_CREATE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="OppurtunityEditDataProvider1")
	public void OppurtunityEditSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportCreateDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_CREATE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_CREATE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_CREATE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_CREATE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_CREATE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_CREATE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_CREATE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_CREATE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportCreateDataProvider1")
	public void ReportCreateSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportDeleteDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_DELETE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_DELETE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_DELETE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportDeleteDataProvider1")
	public void ReportDeleteSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportEditDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EDIT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EDIT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EDIT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EDIT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_EDIT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_EDIT_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_EDIT_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_EDIT_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportEditDataProvider1")
	public void ReportEditSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportExportDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		String[] activities = new String[]{"Export"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EXPORT_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EXPORT_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EXPORT_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_EXPORT_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,export.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_EXPORT_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_EXPORT_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_EXPORT_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_EXPORT_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportExportDataProvider1")
	public void ReportExportSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportRunDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		
		String[] activities = new String[]{"Run"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_RUN_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_RUN_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_RUN_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_RUN_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,run.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_RUN_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_RUN_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_RUN_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_RUN_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportRunDataProvider1")
	public void ReportRunSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] ReportViewDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Report"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_VIEW_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_VIEW_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_VIEW_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.REPORT_VIEW_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Report,Salesforce,view.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.REPORT_VIEW_ONE_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_VIEW_TWO_SF, messageList},   
			{ SequenceDetectorConstants.REPORT_VIEW_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.REPORT_VIEW_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="ReportViewDataProvider1")
	public void ReportViewSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] OppurtunityViewDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Opportunity"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_VIEW_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_VIEW_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_VIEW_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_VIEW_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Opportunity,Salesforce,view.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.OPPORTUNITY_VIEW_ONE_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_VIEW_TWO_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_VIEW_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.OPPORTUNITY_VIEW_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="OppurtunityViewDataProvider1")
	public void OppurtunityViewSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] OppurtunityDeleteDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Opportunity"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.OPPORTUNITY_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Opportunity,Salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.OPPORTUNITY_DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_DELETE_TWO_SF, messageList},   
			{ SequenceDetectorConstants.OPPORTUNITY_DELETE_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.OPPORTUNITY_DELETE_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="OppurtunityDeleteDataProvider1")
	public void OppurtunityDeleteSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@DataProvider
	public Object[][] FileDownloadDataProvider1()  throws UnsupportedEncodingException, JAXBException, Exception{
		
	
		String[] activities = new String[]{"Download"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_DOWNLOAD_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_DOWNLOAD_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_DOWNLOAD_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_DOWNLOAD_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("File,Salesforce,download.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     
			{ SequenceDetectorConstants.FILE_DOWNLOAD_ONE_SF, messageList},   
			{ SequenceDetectorConstants.FILE_DOWNLOAD_TWO_SF, messageList},   
			{ SequenceDetectorConstants.FILE_DOWNLOAD_THREE_SF, messageList}, 
			{ SequenceDetectorConstants.FILE_DOWNLOAD_FOUR_SF, messageList},  
				};
	}
	
	@Test(dataProvider="FileDownloadDataProvider1")
	public void FileDownloadSDGWTest2(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
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

