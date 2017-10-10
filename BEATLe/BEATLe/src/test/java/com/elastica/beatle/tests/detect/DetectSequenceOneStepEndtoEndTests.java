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

public class DetectSequenceOneStepEndtoEndTests extends  DetectUtils{

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
	
	@DataProvider
	public Object[][] uploaddataprovider() throws Exception  {
		
		String[] activities = new String[]{"Upload"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				

				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 0; j <= 5; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("1mb001.bin");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(templfileName, filedId);
					messageList.add("User uploaded file "+templfileName);
					Thread.sleep(1*5*1000);
				}
				Thread.sleep(3*60*1000);
				verifyActivityCount(suiteData.getUsername(), messageList);
		
		
				return new Object[][]{
					
					
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UPLOAD, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_THREE , messageList},
	
	{ SequenceDetectorConstants.UPLOAD_FOUR, messageList},
			
		};
	}
	
	
	@Test(dataProvider="uploaddataprovider")
	public void uploadSDTest(Method method, String name, List<String> messageList) throws Exception{
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			
			 validateIncidents(ioicode);
			 
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	
	@DataProvider
	public Object[][] ShareDataProvider() throws JAXBException, Exception  {
		
		String[] activities = new String[]{"Share"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				List<String> filedIdList = new ArrayList<>();
				
				for (int j = 0; j <= 4; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("1mb001.bin");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(filedId ,templfileName);
					filedIdList.add(filedId);
					messageList.add("User uploaded file "+templfileName);
					Thread.sleep(1*5*1000);
					GDrive gDrive = universalApi.getgDrive();
				
				Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), filedId, null, "anyone", "writer");
				System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
				Thread.sleep(1*5*1000);
				
				}
				Thread.sleep(1*30*1000);
				for (String fileId : filedIdList) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					GDrive gDrive = universalApi.getgDrive();
					Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), fileId, null, "anyone", "writer");
					System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
					messageList.add("User shared "+filedIdMap.get(fileId));
					Thread.sleep(1*5*1000);
				
				}
				Thread.sleep(3*60*1000);
				verifyActivityCount(suiteData.getUsername(), messageList);
				
				
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.SHARE,      messageList},
			
			{ SequenceDetectorConstants.SHARE_TWO,      messageList},
				
			{ SequenceDetectorConstants.SHARE_THREE,      messageList},
			
			{ SequenceDetectorConstants.SHARE_FOUR,     messageList},
					
				};
	}
	
	
	@Test(dataProvider="ShareDataProvider")
	public void shareSDTest(Method method, String name, List<String> messageList) throws Exception{
		
			
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			
			 validateIncidents(ioicode);
			 
			 verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	
	@DataProvider
	public Object[][] UnShareDataProvider() throws JAXBException, Exception  {
		
		String[] activities = new String[]{"Unshare"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				

				
				List<String> messageList = new ArrayList<>();
				List<String> filedIdList = new ArrayList<>();
				Map<String, String> permissionMap = new HashMap<>();
				
				for (int j = 0; j <= 4; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("1mb001.bin");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(filedId, templfileName);
					filedIdList.add(filedId);
					messageList.add("User uploaded file "+templfileName);
					Thread.sleep(1*5*1000);
					GDrive gDrive = universalApi.getgDrive();
				
				Permission newPermission = gDrive.insertPermission(gDrive.getDriveService(), filedId, null, "anyone", "writer");
				permissionMap.put(filedId, newPermission.getId());
				System.out.println("share responce :::::::::: "+newPermission.toPrettyString());
				Thread.sleep(1*5*1000);
				}
				
				Thread.sleep(1*10*1000);
				
				for (String fileId : filedIdList) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					GDrive gDrive = universalApi.getgDrive();
					gDrive = universalApi.getgDrive();
					gDrive.removePermission(fileId, permissionMap.get(fileId));
					messageList.add("User unshared "+filedIdMap.get(fileId));
					Thread.sleep(1*10*1000);
					
				}
				Thread.sleep(3*60*1000);
				verifyActivityCount(suiteData.getUsername(), messageList);
				
		
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UNSHARE},
			
			{ SequenceDetectorConstants.UNSHARE_TWO},
				
			{ SequenceDetectorConstants.UNSHARE_THREE},
			
			{ SequenceDetectorConstants.UNSHARE_FOUR},
					
				};
	}
	

	
	
	
	@Test(dataProvider="UnShareDataProvider")
	public void unShareSDTest(Method method,String name) throws Exception{
			
			
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			
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

	public String createSampleFileType(String fileName) {
		String uuId= UUID.randomUUID().toString();
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (!dir.exists()) {
				if (dir.mkdir()) {
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);
			
			File dest = new File(DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + uuId + "_" + fileName);
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
