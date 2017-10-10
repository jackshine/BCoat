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

public class DetectOnestepSequence_GDRIVE_GW extends  DetectUtils{
	
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
	public Object[][] uploaddataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			

		String[] activities = new String[]{"Upload"};
		String[] failities = new String[]{"Google Drive"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Admin_2,Upload_GWFolder_root_exe.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*5*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UPLOAD, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_THREE , messageList},
	
	{ SequenceDetectorConstants.UPLOAD_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="uploaddataprovider")
	public void uploadSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] sharedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Share"};
		String[] failities = new String[]{"Google Drive"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Admin_2,Share_File_ppt.log", suiteData, suiteData.getSaasAppUsername());
				
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE, messageList},
			
	{ SequenceDetectorConstants.SHARE_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="sharedataprovider")
	public void shareSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] unSharedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Unshare"};
		String[] failities = new String[]{"Google Drive"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogsEPDV3("FolderGD,Unshare_Folder.log", suiteData, suiteData.getSaasAppUsername());
				
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UNSHARE, messageList},
			
	{ SequenceDetectorConstants.UNSHARE_TWO, messageList},
		
	{ SequenceDetectorConstants.UNSHARE_THREE , messageList},
	
	{ SequenceDetectorConstants.UNSHARE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="unSharedataprovider")
	public void unshareSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] deletedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Delete"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
	
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Folder_2,Detete_Forever.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DELETE, messageList},
			
	{ SequenceDetectorConstants.DELETE_TWO, messageList},
		
	{ SequenceDetectorConstants.DELETE_THREE , messageList},
	
	{ SequenceDetectorConstants.DELETE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="deletedataprovider")
	public void deleteSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] createdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		String[] activities = new String[]{"Create"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Document"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("File,Admin_2,Create_Document_root.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.CREATE, messageList},
			
	{ SequenceDetectorConstants.CREATE_TWO, messageList},
		
	{ SequenceDetectorConstants.CREATE_THREE , messageList},
	
	{ SequenceDetectorConstants.CREATE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="createdataprovider")
	public void createSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] renamedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Rename"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("File,Admin_2,Rename_TextDocument_TestDoc.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.RENAME, messageList},
			
	{ SequenceDetectorConstants.RENAME_TWO, messageList},
		
	{ SequenceDetectorConstants.RENAME_THREE , messageList},
	
	{ SequenceDetectorConstants.RENAME_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="renamedataprovider")
	public void renameSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] downloaddataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		String[] activities = new String[]{"Download"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
	
			
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Folder_2,Download1.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOWNLOAD, messageList},
			
	{ SequenceDetectorConstants.DOWNLOAD_TWO, messageList},
		
	{ SequenceDetectorConstants.DOWNLOAD_THREE , messageList},
	
	{ SequenceDetectorConstants.DOWNLOAD_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="downloaddataprovider")
	public void downloadSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] viewdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"View"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.VIEW_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

		List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Folder_2,View.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.VIEW, messageList},
			
	{ SequenceDetectorConstants.VIEW_TWO, messageList},
		
	{ SequenceDetectorConstants.VIEW_THREE , messageList},
	
	{ SequenceDetectorConstants.VIEW_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="viewdataprovider")
	public void viewSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] movedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		String[] activities = new String[]{"Move"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.MOVE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.MOVE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.MOVE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.MOVE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

			
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Folder_2,Move.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.MOVE, messageList},
			
	{ SequenceDetectorConstants.MOVE_TWO, messageList},
		
	{ SequenceDetectorConstants.MOVE_THREE , messageList},
	
	{ SequenceDetectorConstants.MOVE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="movedataprovider")
	public void moveSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FileRestoredataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
					
		String[] activities = new String[]{"Restore"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Folder"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_RESTORE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_RESTORE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_RESTORE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_RESTORE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);			
			
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs("Google Drive,Folder_2,Restore.log", suiteData, suiteData.getSaasAppUsername());
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FILE_RESTORE, messageList},
			
	{ SequenceDetectorConstants.FILE_RESTORE_TWO, messageList},
		
	{ SequenceDetectorConstants.FILE_RESTORE_THREE , messageList},
	
	{ SequenceDetectorConstants.FILE_RESTORE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FileRestoredataprovider")
	public void FileRestoreSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
		
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderMovedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		
		String[] activities = new String[]{"Move"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;

		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MOVE_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);	
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Google Drive,Folder_2,Move.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*5*1000);
		}
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FOLDER_MOVE, messageList},
			
	{ SequenceDetectorConstants.FOLDER_MOVE_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_MOVE_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_MOVE_FOUR, messageList},
			
		};
	}
	
							
	@Test(dataProvider="FolderMovedataprovider")
	public void FolderMoveSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] LogoutDataProvider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Logout"};
		String[] failities = new String[]{"Google Drive"} ;;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"__any"} ;

		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.LOGOUT_BOX, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LOGOUT_BOX_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LOGOUT_BOX_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.LOGOUT_BOX_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);

			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Google Drive,Admin_2,Logout.log", suiteData, suiteData.getSaasAppUsername());
			Thread.sleep(1*5*1000);
		}
			
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		Thread.sleep(3*60*1000);
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.LOGOUT_BOX, messageList},
			
	{ SequenceDetectorConstants.LOGOUT_BOX_TWO, messageList},
		
	{ SequenceDetectorConstants.LOGOUT_BOX_THREE , messageList},

	{ SequenceDetectorConstants.LOGOUT_BOX_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="LogoutDataProvider")
	public void LogoutSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		validateIncidents(ioicode);
		 
		//verifyDetectedUser(suiteData.getSaasAppUsername());
		 
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
    	cal.add(Calendar.DATE, -1);
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
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
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
						Thread.sleep(1 * 5 * 1000);	//wait for 1 minute.
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
