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

public class DetectOnestepSequence_BOX_GW extends  DetectUtils{
	
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
		String[] failities = new String[]{"Box"} ;
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_UPLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*5*1000);}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_SHARE_PDF, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
				
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Folder"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_REMOVE_LINK, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_REMOVE_LINK_MESSAGE);
				
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_DELETE_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DELETE_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_CREATE_DOC, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_CREATE_DOC_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_RENAME_FILE, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_RENAME_FILE_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_DOWNLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DOWNLOAD_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_VIEW_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_VIEW_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
		String[] failities = new String[]{"Box"} ;
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
				dcUtils.replayLogs(DetectGWDataSource.BOX_MOVE_FOLDER, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_MOVE_FOLDER_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
	public Object[][] editpropertiesdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Edit Properties"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				dcUtils.replayLogs(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.EDIT, messageList},
			
	{ SequenceDetectorConstants.EDIT_TWO, messageList},
		
	{ SequenceDetectorConstants.EDIT_THREE , messageList},
	
	{ SequenceDetectorConstants.EDIT_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="editdataprovider")
	public void editPropertiesSDTest1(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] DeleteCommentdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Delete Comment"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_COMMENT, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_COMMENT_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_COMMENT_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_COMMENT_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("testgdoc,CommentCreateDelete.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.DELETE_COMMENT, messageList},
			
	{ SequenceDetectorConstants.DELETE_COMMENT_TWO, messageList},
		
	{ SequenceDetectorConstants.DELETE_COMMENT_THREE , messageList},
	
	{ SequenceDetectorConstants.DELETE_COMMENT_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="DeleteCommentdataprovider")
	public void DeleteCommentSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] PostCommentdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Post Comment"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.POST_COMMENT, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.POST_COMMENT_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.POST_COMMENT_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.POST_COMMENT_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("testgdoc,CommentCreateDelete.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.POST_COMMENT, messageList},
			
	{ SequenceDetectorConstants.POST_COMMENT_TWO, messageList},
		
	{ SequenceDetectorConstants.POST_COMMENT_THREE , messageList},
	
	{ SequenceDetectorConstants.POST_COMMENT_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="PostCommentdataprovider")
	public void PostCommentSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] FileLockdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Lock"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_LOCK, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_LOCK_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_LOCK_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_LOCK_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);	
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Test,AFolder,lockFileTestdoc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FILE_LOCK, messageList},
			
	{ SequenceDetectorConstants.FILE_LOCK_TWO, messageList},
		
	{ SequenceDetectorConstants.FILE_LOCK_THREE, messageList},
	
	{ SequenceDetectorConstants.FILE_LOCK_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="FileLockdataprovider")
	public void FileLockSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] FolderModifyPermissiondataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Modify Permissions"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Folder"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);	
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("AFolder,Box,modify_permission_Folder.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION, messageList},
			
	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_THREE, messageList},

	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FolderModifyPermissiondataprovider")
	public void FolderModifyPermissionDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] FileSearchdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		String[] activities = new String[]{"Search"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_SEARCH, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_SEARCH_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_SEARCH_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_SEARCH_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);	
			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("searchfile1.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FILE_SEARCH, messageList},
			
	{ SequenceDetectorConstants.FILE_SEARCH_TWO, messageList},
		
	{ SequenceDetectorConstants.FILE_SEARCH_THREE , messageList},
	
	{ SequenceDetectorConstants.FILE_SEARCH_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FileSearchdataprovider")
	public void FileSearchPermissionDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] FileUnlockdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Unlock"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_UNLOCK, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_UNLOCK_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_UNLOCK_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_UNLOCK_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("Test,AFolder,unlockFileTestdoc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FILE_UNLOCK, messageList},
			
	{ SequenceDetectorConstants.FILE_UNLOCK_TWO, messageList},
		
	{ SequenceDetectorConstants.FILE_UNLOCK_THREE , messageList},
	
	{ SequenceDetectorConstants.FILE_UNLOCK_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FileUnlockdataprovider")
	public void FileUnlockDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] FileCreateTagdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Create Tag"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_CREATE_TAG, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_CREATE_TAG_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_CREATE_TAG_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FILE_CREATE_TAG_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);			
			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("AFile,AFolder,addedittaggDoc.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FILE_CREATE_TAG, messageList},
			
	{ SequenceDetectorConstants.FILE_CREATE_TAG_TWO, messageList},
		
	{ SequenceDetectorConstants.FILE_CREATE_TAG_THREE , messageList},
	
	{ SequenceDetectorConstants.FILE_CREATE_TAG_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FileCreateTagdataprovider")
	public void FileCreateTagDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
	public Object[][] DeleteTagdataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Delete Tag"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;

		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TAG, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TAG_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TAG_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TAG_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
			
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("GFolder,AddEditDeletetag.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.DELETE_TAG, messageList},
			
	{ SequenceDetectorConstants.DELETE_TAG_TWO, messageList},
		
	{ SequenceDetectorConstants.DELETE_TAG_THREE , messageList},
	
	{ SequenceDetectorConstants.DELETE_TAG_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="DeleteTagdataprovider")
	public void DeleteTagSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
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
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File/Folder"} ;

		
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
				dcUtils.replayLogs("restore,RestoreFile.log", suiteData, suiteData.getSaasAppUsername());
				//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
				Thread.sleep(1*5*1000);
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
	public Object[][] FolderModifyPermissiondataprovider1() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Modify Permissions"};
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] sources1 = new String[]{"GW"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Folder"} ;

		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_TWO, 2,600, true   ,true, true,
				activities, failities, sources1, users1, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_FOUR, 1,400, true  ,true, true,
				activities, failities, sources1, users, objects1);
		response = dsd.createSequenceDetector(sdInput, suiteData);
		Reporter.log("Waiting for a minute After creating sequences");
		Thread.sleep(1*60*1000);
			
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 6; j++) {
			dcUtils.replayLogs("AFolder,Box,modify_permission_Folder.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name
	{  SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1, messageList},
			
	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_TWO, messageList},
		
	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_THREE , messageList},
	
	{ SequenceDetectorConstants.FOLDER_MODIFY_PERMISSION1_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="FolderModifyPermissiondataprovider1")
	public void FolderModifyPermissionDTest2(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, SequenceName, user, user );
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 //verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] FolderMovedataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
		
		
		String[] activities = new String[]{"Move"};
		String[] failities = new String[]{"Box"} ;
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
			dcUtils.replayLogs("AFolder,Box,move_Folder.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
		
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

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
		String[] failities = new String[]{"Box"} ;
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
			dcUtils.replayLogs("logout.log", suiteData, suiteData.getSaasAppUsername());
			//messageList.add(DetectGWDataSource.BOX_EDIT_PROPERTIES_FILE_MESSAGE);
			Thread.sleep(1*5*1000);
		}
			
		//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
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
