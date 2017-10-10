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

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectGWDataSource;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectOnestepSequence_Office_365_GW extends  DetectUtils{
	
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
	public void beforeClass()   {
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
	public Object[][] DownloadDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Download"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_OFFICE_ONE, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_OFFICE_TWO, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_OFFICE_THREE, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("O365,Test,download_txt.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOWNLOAD_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOWNLOAD_OFFICE_ONE, messageList},
		
	{ SequenceDetectorConstants.DOWNLOAD_OFFICE_TWO , messageList},
	
	{ SequenceDetectorConstants.DOWNLOAD_OFFICE_THREE, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DownloadDataprovider")
	public void DownloadSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentCreateDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"OneNote notebook"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Create_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_CREATE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentCreateDataprovider")
	public void DocumentCreateSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentDeleteDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 3; j++) {
				dcUtils.replayLogsEPDV3("DocumentOneD,Delete_doc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DELETE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DELETE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DELETE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DELETE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentDeleteDataprovider")
	public void DocumentDeleteSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentSharDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Share"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);

		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Share_External_Excel.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.SHARE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentSharDataprovider")
	public void Document_ShareSDTest1(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentCopyDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Copy"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"};
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_COPY_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_COPY_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_COPY_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_COPY_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("DocumentOneD,Copy_doc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.FOLDER_COPY_OFFICE, messageList},
			
	{ SequenceDetectorConstants.FOLDER_COPY_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_COPY_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_COPY_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentCopyDataprovider")
	public void DocumentCopySDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderCreateDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"OneNote notebook"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_CREATE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_CREATE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_CREATE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_CREATE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Create_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.FOLDER_CREATE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.FOLDER_CREATE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_CREATE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_CREATE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="FolderCreateDataprovider")
	public void FolderCreateSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderMoveDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Move"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Move_Excel.log", suiteData, suiteData.getSaasAppUsername());
		//	messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.FOLDER_MOVE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.FOLDER_MOVE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_MOVE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_MOVE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="FolderMoveDataprovider")
	public void FolderMoveSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Rename"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Rename_Excel.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.FOLDER_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.FOLDER_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="FolderRenameDataprovider")
	public void FolderRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
/*	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] EditDocumentDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Edit"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("PowerPointOne,Edit_In_PowerPoint_Online.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_EDIT_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="EditDocumentDataprovider")
	public void EditDocumentSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}*/
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentMoveDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Move"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("WordDocument,Move_word.log",suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_MOVE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentMoveDataprovider")
	public void DocumentMoveSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Rename"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("DocumentOneD,Rename_doc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentRenameDataprovider")
	public void DocumentRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] RecycleBinOpenDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Open"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Recycle Bin"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("RecycleBin,Restore_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_RESTORE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="RecycleBinOpenDataprovider")
	public void RecycleBinOpenSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
///*	@SuppressWarnings("unchecked")
//	@DataProvider()
//	public Object[][] DocumentShareDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
//			
//
//		String[] activities = new String[]{"Share"};
//		String[] failities = new String[]{"Office 365"} ;
//		String[] sources = new String[]{"__any"} ;
//		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
//		String[] objects = new String[]{"__any"} ;
//		
//		String[] sources1 = new String[]{"GW"} ;
//		String[] users1 = new String[]{"__any"} ;
//		String[] objects1 = new String[]{"Document"} ;
//		
//		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE, 2,400, false   ,false, false,
//				activities, failities, sources, users, objects);
//		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_TWO, 2,600, true   ,true, true,
//				activities, failities, sources1, users1, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		
//		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_THREE, 1,400, false   ,false, false,
//				activities, failities, sources1, users, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		
//		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_FOUR, 1,400, true  ,true, true,
//				activities, failities, sources1, users, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		Reporter.log("Waiting for a minute After creating sequences");
//		Thread.sleep(1*60*1000);
//		
//			List<String> messageList = new ArrayList<>();
//			for (int j = 0; j < 6; j++) {
//				dcUtils.replayLogsEPDV3("document,share.log", suiteData, suiteData.getSaasAppUsername());
//			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
//			Thread.sleep(1*2*1000);}
//			Thread.sleep(3*60*1000);
//			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//		
//	//
//		return new Object[][]{
//			
//			
//					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
//	{  SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE, messageList},
//			
//	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_TWO, messageList},
//		
//	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_THREE, messageList},
//	
//	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_FOUR, messageList},
//			
//		};
//	}*/
//	
//	
//	@Test(dataProvider="DocumentShareDataprovider")
//	public void DocumentShareSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
//		
//		String user = suiteData.getSaasAppUsername();
//		
//		Log(method, SequenceName, user, user );
//		
//		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
//		
//		 validateIncidents(ioicode);
//		 
////		 verifyDetectedUser(suiteData.getSaasAppUsername());
//		 
//		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
//		
//	}
	
	/*@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentUnshareDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Unshare"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("document,unshare.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_UNSHARE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentUnshareDataprovider")
	public void DocumentUnshareSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}*/
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderViewDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOLDER_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOLDER_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOLDER_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOLDER_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("WordDocument,Embed_Word.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.VIEW_FOLDER_OFFICE, messageList},
			
	{ SequenceDetectorConstants.VIEW_FOLDER_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.VIEW_FOLDER_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.VIEW_FOLDER_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="FolderViewDataprovider")
	public void FolderViewSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] RecycleBinEmptyDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Empty"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Recycle Bin"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("RecycleBin,Empty_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE, messageList},
			
	{ SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.RECYCLE_BIN_EMPTY_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="RecycleBinEmptyDataprovider")
	public void RecycleBinEmptySDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	/*@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] ExcelWorkBookViewDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Excel workbook"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Open_In_Excel_Online.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW, messageList},
			
	{ SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_TWO, messageList},
		
	{ SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_THREE , messageList},
	
	{ SequenceDetectorConstants.EXCEL_WORKBOOK_VIEW_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="ExcelWorkBookViewDataprovider")
	public void ExcelWorkBookViewSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}*/
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] DocumentFolderShareDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Share"};
		String[] failities = new String[]{"Office 365"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Share_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			Thread.sleep(3*60*1000);
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOCUMENT_SHARE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DocumentFolderShareDataprovider")
	public void DocumentFolderShareSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
//	@SuppressWarnings("unchecked")
//	@DataProvider()
//	public Object[][] FolderDeleteDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
//			
//
//		String[] activities = new String[]{"Delete"};
//		String[] failities = new String[]{"Office 365"} ;
//		String[] sources = new String[]{"__any"} ;
//		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
//		String[] objects = new String[]{"__any"} ;
//		
//		String[] sources1 = new String[]{"GW"} ;
//		String[] users1 = new String[]{"__any"} ;
//		String[] objects1 = new String[]{"Document/Folder"} ;
//		
//		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_DELETE, 2,400, false   ,false, false,
//				activities, failities, sources, users, objects);
//		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_DELETE_TWO, 2,600, true   ,true, true,
//				activities, failities, sources1, users1, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		
//		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_DELETE_THREE, 1,400, false   ,false, false,
//				activities, failities, sources1, users, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		
//		
//		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOCUMENT_DELETE_FOUR, 1,400, true  ,true, true,
//				activities, failities, sources1, users, objects1);
//		 response = dsd.createSequenceDetector(sdInput, suiteData);
//		Reporter.log("Waiting for a minute After creating sequences");
//		Thread.sleep(1*60*1000);
//		
//			List<String> messageList = new ArrayList<>();
//			for (int j = 0; j < 6; j++) {
//				dcUtils.replayLogs("O365,delete_Test_txt.log", suiteData, suiteData.getSaasAppUsername());
//			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
//			Thread.sleep(1*2*1000);}
//			Thread.sleep(3*60*1000);
//			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//		
//	//
//		return new Object[][]{
//			
//			
//					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
//	{  SequenceDetectorConstants.DOCUMENT_DELETE, messageList},
//			
//	{ SequenceDetectorConstants.DOCUMENT_DELETE_TWO, messageList},
//		
//	{ SequenceDetectorConstants.DOCUMENT_DELETE_THREE , messageList},
//	
//	{ SequenceDetectorConstants.DOCUMENT_DELETE_FOUR, messageList},
//			
//		};
//	}
//	
//	
//	@Test(dataProvider="DocumentFolderDeleteDataprovider")
//	public void FolderDeleteSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
//		
//		String user = suiteData.getSaasAppUsername();
//		
//		Log(method, SequenceName, user, user );
//		
//		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
//		
//		 validateIncidents(ioicode);
//		 
////		 verifyDetectedUser(suiteData.getSaasAppUsername());
//		 
//		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
//		
//	}
	
	
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
    	cal.add(Calendar.HOUR, -2);
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
						Thread.sleep(1 * 10 * 1000);	//wait for 1 minute.
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