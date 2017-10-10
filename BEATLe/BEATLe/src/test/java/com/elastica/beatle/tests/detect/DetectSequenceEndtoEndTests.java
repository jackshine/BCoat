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

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
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
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectSequenceEndtoEndTests extends  DetectUtils{
	
	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	public static final String DETECT_FILE_UPLOAD_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload";
	public static final String DETECT_FILE_UPLOAD_PATH_TEMP = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload"+File.separator+"temp";
	protected UniversalApi universalApi;
	protected UniversalApi universalGraphApi;
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@BeforeClass()
	public void beforeClass() throws Exception {	
		clearIncidents();
		}
	
	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		try {
			Thread.sleep(1*60*1000);
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] uploadShareSequencedetectordataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Upload", "Share"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] sources1 = new String[]{"API", "API"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_TWO, 2,600, true   ,true, true,
					activities, failities, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_FOUR, 1,400, true  ,true, true,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
			Reporter.log("Waiting for a minute After creating sequences");
			Thread.sleep(1*60*1000);
			
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				String templfileName = createSampleFileType("1mb001.bin");
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				filedIdMap.put(templfileName, filedId);
				messageList.add("User uploaded file "+templfileName);
				Thread.sleep(1*30*1000);
				
				GDrive gDrive = universalApi.getgDrive();
				
				Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), filedId, null, "anyone", "writer");
				System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
				//User shared 60d51807-3f67-43d6-ab5a-6f0f64a733c0_GDriveRemediationTest.txt
				messageList.add("User shared "+templfileName);
				
				Thread.sleep(1*10*1000);
			}
			
			verifyActivityCount(suiteData.getUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_ONE, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_THREE , messageList},
	
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="uploadShareSequencedetectordataprovider")
	public void UploadShareWaitRepeatSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		
		Log(method, SequenceName, suiteData.getSaasAppUsername(), suiteData.getUsername() );
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] shareUnshareSequencedetectordataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		
		String[] activities = new String[]{"Share", "Unshare"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] sources1 = new String[]{"API", "API"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_TWO, 2,600, true   ,true, true,
					activities, failities, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_FOUR, 1,400, true  ,true, true,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
		
		
			
			List<String> messageList = new ArrayList<>();
			
			
			for (int j = 0; j <4; j++) {
				UserAccount account = detectFunctions.getUserAccount(suiteData);
				UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
				String templfileName = createSampleFileType("1mb001.bin");
				String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
				filedIdMap.put(templfileName, filedId);
				messageList.add("User uploaded file "+templfileName);
				Thread.sleep(1*30*1000);
				GDrive gDrive = universalApi.getgDrive();
			
			Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), filedId, null, "anyone", "writer");
			System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
			//User shared 60d51807-3f67-43d6-ab5a-6f0f64a733c0_GDriveRemediationTest.txt
			messageList.add("User shared "+templfileName);
			Thread.sleep(1*30*1000);
			gDrive = universalApi.getgDrive();
			gDrive.removePermission(filedId, newPermission.getId());
			messageList.add("User unshared "+templfileName);
				
				Thread.sleep(1*10*1000);
			}
			verifyActivityCount(suiteData.getUsername(), messageList);
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_ONE, messageList},
			
	{ SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT_FOUR, messageList},
			
		};
	}
	
	@Test( dataProvider= "shareUnshareSequencedetectordataprovider",description = "Potential Data Exfiltration through Copy-Paste and Unauthorized Sharing")
	public void shareUnShareWaitRepeatSDTest(Method method,String SequenceName, List<String> messageList) throws Exception{
		
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
		
		String ioicode = TOO_MANY_SEQUENCE+SequenceName;
		
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] trashDelete() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		

		String[] activities = new String[]{"Trash", "Delete"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] sources1 = new String[]{"API", "API"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.TRASH_DELETE_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.TRASH_DELETE_TWO, 2,600, true   ,true, true,
					activities, failities, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.TRASH_DELETE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.TRASH_DELETE_FOUR, 1,400, true  ,true, true,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
		
			List<String> messageList = new ArrayList<>();
		
			
		for (int j = 0; j <= 4; j++) {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			String templfileName = createSampleFileType("1mb001.bin");
			String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
			filedIdMap.put(templfileName, filedId);
			messageList.add("User uploaded file "+templfileName);
			Thread.sleep(1*30*1000);
			GDrive gDrive = universalApi.getgDrive();
		
			gDrive.trashFile(filedId);
			//User trashed 60f30f9d-69fa-40d5-bccf-28e0a5b1a2ab_1mb001.bin 
			//messageList.add("User trashed "+templfileName);
			Thread.sleep(1*30*1000);
			gDrive.deleteFile(filedId);
			//User permanently deleted cfd13ddb-54e6-4b03-8da2-ce0e98b22fde_1mb001.bin from trash
			//messageList.add("User permanently deleted "+templfileName+" from trash");
			
			
			Thread.sleep(1*10*1000);
		}
		
		
		
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{ SequenceDetectorConstants.TRASH_DELETE_ONE, messageList},
			
	{ SequenceDetectorConstants.TRASH_DELETE_TWO, messageList},
		
	{ SequenceDetectorConstants.TRASH_DELETE_THREE, messageList},
	
	{ SequenceDetectorConstants.TRASH_DELETE_FOUR, messageList},
			
		};
	}
	
	
	@Test( dataProvider= "trashDelete",description = "Potential Data Exfiltration through Copy-Paste and Unauthorized Sharing")
	public void trashDeleteWaitRepeatSDTest(Method method,String name, List<String> messageList)  throws Exception{
		
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] uploadShareUnshareSequencedetectordataprovider() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		
		String[] activities = new String[]{"Upload", "Share", "Unshare"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] sources1 = new String[]{"API", "API", "API"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_TWO, 2,600, true   ,true, true,
					activities, failities, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_FOUR, 1,400, true  ,true, true,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
		
			List<String> messageList = new ArrayList<>();
		
			
		for (int j = 0; j <= 4; j++) {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			String templfileName = createSampleFileType("1mb001.bin");
			String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
			filedIdMap.put(templfileName, filedId);
			messageList.add("User uploaded file "+templfileName);
			Thread.sleep(1*30*1000);
			GDrive gDrive = universalApi.getgDrive();
		
		Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), filedId, null, "anyone", "writer");
		System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
		//User shared 60d51807-3f67-43d6-ab5a-6f0f64a733c0_GDriveRemediationTest.txt
	//	messageList.add("User shared "+templfileName);
		Thread.sleep(1*30*1000);
		gDrive = universalApi.getgDrive();
		gDrive.removePermission(filedId, newPermission.getId());
		//messageList.add("User unshared "+templfileName);
			
		Thread.sleep(1*30*1000);
		}
		
		
		
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{ SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_ONE, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_THREE, messageList},
	
	{ SequenceDetectorConstants.UPLOAD_SHARE_UNSHARE_FOUR, messageList},
			
		};
	}
	
	@Test( dataProvider= "uploadShareUnshareSequencedetectordataprovider",description = "Potential Data Exfiltration through Copy-Paste and Unauthorized Sharing")
	public void uploadShareUnShareWaitRepeatSDTest(Method method,String name,List<String>  messageList) throws Exception{
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		Thread.sleep(60000);
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	@Test()
	public void createDeleteWaitRepeatSDTest(Method method) throws Exception{
		
		String name = "Potential Data Destruction using Unauthorized Deletion and Decoy Document Creation";
		
		Log(method, name, suiteData.getSaasAppUsername(), suiteData.getUsername() );
		UserAccount account;
		UniversalApi universalApi = null ;
		try {
			 account = detectFunctions.getUserAccount(suiteData);
			 universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
		
		List<String> messageList = new ArrayList<>();
		 	
			
			
			
		for (int j = 0; j < 12; j++) {
			GDrive gDrive = universalApi.getgDrive();
			com.google.api.services.drive.model.File file = gDrive.createFile("Sagar_test", "detect_tests", folderInfo.get("folderId"), null, "Sagar_test");
			String fileId = file.getId();
			Thread.sleep(1*30*1000);
			//messageList.add("User uploaded file ");
			gDrive.deleteFile(fileId);
			Thread.sleep(1*30*1000);
		}
		
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		Thread.sleep(60000);
		
		
		 validateIncidents(ioicode);
		 
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	
	@AfterClass
	public void deleteFolder() {
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			detectFunctions.deleteFolder(universalApi, suiteData.getSaasApp(), folderInfo);
			detectFunctions.cleanupTempFolder();
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
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

	/**
	 * Generate sample file type from files and current date time
	 * @param fileName
	 * @param currentDateTime
	 * @return
	 */
	public String createSampleFileType(String fileName) {
		String uuId= UUID.randomUUID().toString();
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);
			
			File dest = new File(DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + uuId + "_" + fileName);
			// Logger.info(dest);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uuId + "_" + fileName;
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
