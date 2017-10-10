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

public class DetectSequenceDetector_Office_365_GW extends  DetectUtils{
	
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
	public Object[][] DownloadViewDeleteDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Download", "View", "Delete"};
		String[] failities = new String[]{"Office 365", "Office 365", "Office 365"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"Document", "Document", "Document"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogs("O365,Test,download_txt.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("WordDocument,Embed_Word.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("DocumentOneD,Delete_doc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.DOWNLOAD_VIEW_DELETE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="DownloadViewDeleteDataprovider")
	public void DownloadViewDeleteSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] copyMoveRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Copy", "Move", "Rename"};
		String[] failities = new String[]{"Office 365", "Office 365", "Office 365"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"Document/Folder", "Document/Folder", "Document/Folder"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("DocumentOneD,Copy_doc.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Move_Excel.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Rename_Excel.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.COPY_MOVE_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="copyMoveRenameDataprovider")
	public void copyMoveRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] viewRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"View", "Rename"};
		String[] failities = new String[]{"Office 365",  "Office 365"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),  suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any",  "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any",  "__any"} ;
		String[] objects1 = new String[]{"Excel workbook", "Document/Folder"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				//dcUtils.replayLogsEPDV3("ExcelWorkBook,Share_External_Excel.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("ExcelWorkBook,Rename_Excel.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.VIEW_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.VIEW_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.VIEW_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.VIEW_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="viewRenameDataprovider")
	public void viewRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] shareRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Share", "Rename"};
		String[] failities = new String[]{"Office 365",  "Office 365"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),  suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any",  "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any",  "__any"} ;
		String[] objects1 = new String[]{"Document/Folder", "Document/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("O365,Test,share_txt.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("WordDocument,Rename_word.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.SHARE_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="shareRenameDataprovider")
	public void shareRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] createRenameDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Create", "Rename"};
		String[] failities = new String[]{"Office 365",  "Office 365"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),  suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any",  "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any",  "__any"} ;
		String[] objects1 = new String[]{"Document/Folder", "Document/Folder"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_RENAME_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_RENAME_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_RENAME_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_RENAME_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Create_OneNote.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("WordDocument,Rename_word.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.CREATE_RENAME_OFFICE, messageList},
			
	{ SequenceDetectorConstants.CREATE_RENAME_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.CREATE_RENAME_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.CREATE_RENAME_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="createRenameDataprovider")
	public void createRenameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] createDeleteDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Create", "Delete"};
		String[] failities = new String[]{"Office 365",  "Office 365"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),  suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any",  "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any",  "__any"} ;
		String[] objects1 = new String[]{"Document/Folder", "Document"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_DELETE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_DELETE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_DELETE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_DELETE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Create_OneNote.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("OneNote,Delete_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.CREATE_DELETE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.CREATE_DELETE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.CREATE_DELETE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.CREATE_DELETE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="createDeleteDataprovider")
	public void createDeleteSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] viewMoveDataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"View", "Move"};
		String[] failities = new String[]{"Office 365",  "Office 365"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(),  suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any",  "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any",  "__any"} ;
		String[] objects1 = new String[]{"Document", "Document/Folder"} ;
		
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_MOVE_OFFICE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_MOVE_OFFICE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_MOVE_OFFICE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_MOVE_OFFICE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				dcUtils.replayLogsEPDV3("OneNote,Open_In_OneNote_Online.log", suiteData, suiteData.getSaasAppUsername());
				dcUtils.replayLogsEPDV3("OneNote,Move_OneNote.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*2*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			Thread.sleep(3*60*1000);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.VIEW_MOVE_OFFICE, messageList},
			
	{ SequenceDetectorConstants.VIEW_MOVE_OFFICE_TWO, messageList},
		
	{ SequenceDetectorConstants.VIEW_MOVE_OFFICE_THREE , messageList},
	
	{ SequenceDetectorConstants.VIEW_MOVE_OFFICE_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="viewMoveDataprovider")
	public void viewMoveSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
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