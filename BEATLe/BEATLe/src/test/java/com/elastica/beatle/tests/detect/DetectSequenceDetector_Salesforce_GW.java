package com.elastica.beatle.tests.detect;

import java.io.IOException;
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
import com.elastica.beatle.detect.DetectGWDataSource;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectSequenceDetector_Salesforce_GW extends DetectUtils{
	
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
	
	
	
	@DataProvider
	public Object[][] CreateShareDataProvider() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Create", "View", "Share"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"Account", "Account", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_SHARE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_SHARE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_SHARE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_SHARE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs("Account,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE_SHARE_ONE_SF,  messageList   },
			{ SequenceDetectorConstants.CREATE_SHARE_TWO_SF,    messageList   },
			{ SequenceDetectorConstants.CREATE_SHARE_THREE_SF,     messageList   },
			{ SequenceDetectorConstants.CREATE_SHARE_FOUR_SF,   messageList   },
				};
	}

	
	
	
	
	
	
	@Test(dataProvider="CreateShareDataProvider")
	public void CreateShareSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{
			
		String user = suiteData.getSaasAppUsername();
		Log(method, SequenceName, user, user );
			
			
			
			String ioicode = TOO_MANY_SEQUENCE+SequenceName;
			
			validateIncidents(ioicode);
			 
            //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
		
		
	@DataProvider
	public Object[][] UploadViewShareDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		
		
		String[] activities = new String[]{"Upload", "View", "Share"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"File", "__any", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("File,Salesforce,upload.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_TWO_SF, messageList},    
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_THREE_SF, messageList},  
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_FOUR_SF, messageList},   
				};
	}
	
	@Test(dataProvider="UploadViewShareDataProvider")
	public void UploadViewShareDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );	
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] DownloadUploadDataProvider() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Download", "Upload"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs("File,Salesforce,download.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			
			dcUtils.replayLogs("File,Salesforce,upload.log", suiteData, suiteData.getSaasAppUsername());
			//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
				Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_ONE_SF, messageList},   
			{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_TWO_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_THREE_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_FOUR_SF, messageList}, 
				};
	}
	
	@Test(dataProvider="DownloadUploadDataProvider")
	public void DownloadSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

	
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );

		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] CreateViewShareDeleteDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Create", "View", "Share", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"Account", "Account", "File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Account,Salesforce,create.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
				
			dcUtils.replayLogs("file,salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);	
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_ONE_SF, messageList},      
			{ SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_TWO_SF,  messageList},     
			{SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_THREE_SF ,  messageList},   
			{SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE_FOUR_SF,    messageList},   
				};
	}
	
	@Test(dataProvider="CreateViewShareDeleteDataProvider")
	public void CreateViewShareDeleteSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		
		validateIncidents(ioicode);
		
		verifyDetectedUser(suiteData.getSaasAppUsername());
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	@DataProvider
	public Object[][] UploadViewShareDeleteDataProviderDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Upload", "View", "Share", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"File", "__any", "File", "File"} ;
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("File,Salesforce,upload.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
				
			dcUtils.replayLogs("file,salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);	
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_ONE_SF, messageList},      
			{ SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_TWO_SF,  messageList},     
			{SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_THREE_SF ,  messageList},   
			{SequenceDetectorConstants.UPLOAD_VIEW_SHARE_DELETE_FOUR_SF,    messageList},   
				};
	}
	@Test(dataProvider="UploadViewShareDeleteDataProviderDataProvider")
	public void UploadViewShareDeleteSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
				
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	
	@DataProvider
	public Object[][] DownloadDeleteDataProvider() throws UnsupportedEncodingException, JAXBException, Exception  {
		
		String[] activities = new String[]{"Download", "Delete"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		 
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs("File,Salesforce,download.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			
			dcUtils.replayLogs("file,salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
				Thread.sleep(1*5*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.DOWNLOAD_DELETE_ONE_SF, messageList},   
			{ SequenceDetectorConstants.DOWNLOAD_DELETE_TWO_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_DELETE_THREE_SF, messageList},
			{ SequenceDetectorConstants.DOWNLOAD_DELETE_FOUR_SF, messageList}, 
				};
	}
	
	@Test(dataProvider="DownloadDeleteDataProvider")
	public void DownloadDeleteSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

	
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );

		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	

	@DataProvider
	public Object[][] UploadViewEditShareDeleteDataProviderDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Upload", "View","Edit", "Share", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(),suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any", "__any","__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW", "GW","GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any", "__any","__any"} ;
		String[] objects1 = new String[]{"File", "__any", "File", "File","File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_ONE_SF, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_TWO_SF, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_THREE_SF, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_FOUR_SF, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 8; j++) {
			dcUtils.replayLogs("File,Salesforce,upload.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
			
			dcUtils.replayLogs("file,Salesforce,edit.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);	
			
			dcUtils.replayLogs("file,Salesforce,share_link.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);
				
			dcUtils.replayLogs("file,salesforce,delete.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
			Thread.sleep(1*5*1000);	
			
			
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_ONE_SF, messageList},      
			{ SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_TWO_SF,  messageList},     
			{SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_THREE_SF ,  messageList},   
			{SequenceDetectorConstants.UPLOAD_VIEW_EDIT_SHARE_DELETE_FOUR_SF,    messageList},   
				};
	}
	@Test(dataProvider="UploadViewEditShareDeleteDataProviderDataProvider")
	public void UploadViewEditShareDeleteSDGWTest1(Method method,String SequenceName, List<String> messageList) throws Exception{

		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );		
				
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
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
