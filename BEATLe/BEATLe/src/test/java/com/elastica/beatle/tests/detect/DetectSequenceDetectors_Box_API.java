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
import java.util.Random;
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
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.SharedLink;

public class DetectSequenceDetectors_Box_API extends  DetectUtils {
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
	
	
	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		clearIncidents();
		try {
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			Reporter.log("exception      ::::::::: "+e, true);
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
	public Object[][] AddEditDeleteDataProvider() throws JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Add", "Edit", "Delete"};
		
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"API", "API", "API"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"User", "User", "User"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"__any", "__any", "__any"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;
		
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_EDIT_DELETE_USER_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_EDIT_DELETE_USER_TWO, 2,600, true   ,true, true,
				activities, failities1, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_EDIT_DELETE_USER_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_EDIT_DELETE_USER_FOUR, 1,400, true  ,true, true,
				activities, failities1, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		

			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 6; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					BoxUserInfo userInfo;
					userInfo = new BoxUserInfo();
					userInfo.setLogin("Detect_tests"+j+"@"+suiteData.getTenantDomainName());
					userInfo.setName("Detect_tests"+j);
					userInfo.setTrackingCodes(null);
					//userInfo.setRole(role);
					//Create the user
					Logger.info("Input user info:"+MarshallingUtils.marshall(userInfo));
					BoxUserInfo createdUser = universalApi.createUser(userInfo);
					//messageList.add("User created a new user "+"Detect_tests"+j);
					Thread.sleep(1*5*1000);
					createdUser.setJobTitle("QA Engineer");
					BoxUserInfo updatedUser =	universalApi.updateUser(createdUser);
					Thread.sleep(1*5*1000);
					universalApi.deleteUser(updatedUser);
					Thread.sleep(1*5*1000);
					
					
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.ADD_EDIT_DELETE_USER_ONE},
			
			{ SequenceDetectorConstants.ADD_EDIT_DELETE_USER_TWO},
				
			{ SequenceDetectorConstants.ADD_EDIT_DELETE_USER_THREE},
			
			{ SequenceDetectorConstants.ADD_EDIT_DELETE_USER_FOUR},
				};
	}
	
	
	@Test(dataProvider="AddEditDeleteDataProvider")
	public void addEditDeleteUserSDTest(Method method, String name) throws Exception{

		Logger.info("Execution Started - Test Case Name: " + method.getName());	
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	
	@DataProvider
	public Object[][] CreateDeleteEditGroupDataProvider() throws JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Create", "Edit", "Delete"};
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"API", "API", "API"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"Group", "Group", "Group"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"__any", "__any", "__any"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;

		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_TWO, 2,600, true   ,true, true,
				activities, failities1, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_FOUR, 1,400, true  ,true, true,
				activities, failities1, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 5; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					//create a group
					GroupInput ginput = new GroupInput();
					String name = uniqueId+"_detect";
					ginput.setName(name);
					ginput.setProvenance("Detect-Elastica_"+j);
					ginput.setDescription("Detect Team");
					BoxGroup boxGroup = universalApi.createGroup(ginput);
					messageList.add("User created group "+name);
					Thread.sleep(1*5*1000);
					boxGroup.setName(uniqueId +"_blr_team");
					boxGroup = universalApi.updateGroup(boxGroup);
					Thread.sleep(1*5*1000);
					universalApi.deleteGroup(boxGroup);
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_ONE},
			{ SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_TWO},
			{ SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_THREE},
			{ SequenceDetectorConstants.CREATE_EDIT_DELETE_USER_FOUR},
				};
	}
	
	
	@Test(dataProvider="CreateDeleteEditGroupDataProvider")
	public void createDeleteEditGroupSDTest(Method method, String name) throws Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] fileOperationsDataProvider() throws JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Upload", "Download", "Share"};
		
		String[] activities1 = new String[]{"Share", "Unshare", "Rename"};
		
		String[] activities2 = new String[]{"Lock", "Unlock", "Delete"};
		
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"API", "API", "API"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"File", "File", "File"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"__any", "__any", "__any"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_TWO, 2,600, true   ,true, true,
				activities, failities1, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_FOUR, 1,400, true  ,true, true,
				activities, failities1, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		 
		  sdInput = dsd.createSDInput(SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_ONE, 2,400, false   ,false, false,
				  activities1, failities, sources, users, objects);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			 sdInput = dsd.createSDInput(SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_TWO, 2,600, true   ,true, true,
					 activities1, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_THREE, 1,400, false   ,false, false,
					activities1, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_FOUR, 1,400, true  ,true, true,
					activities1, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			 sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_UNLOCK_DELETE_ONE, 2,400, false   ,false, false,
					  activities2, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_UNLOCK_DELETE_TWO, 2,600, true   ,true, true,
						 activities2, failities1, sources1, users1, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_UNLOCK_DELETE_THREE, 1,400, false   ,false, false,
						activities2, failities, sources1, users, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_UNLOCK_DELETE_FOUR, 1,400, true  ,true, true,
						activities2, failities1, sources1, users, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
		

			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 5; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("1mb001.bin");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(templfileName, filedId);
					Thread.sleep(1*5*1000);
					//Download the file
					universalApi.downloadFile(filedId, templfileName);
					Logger.info("File downloaded from box:"+ templfileName);
					Thread.sleep(1*5*1000);
					
//					String updateFile = "Detect.txt";
//					universalApi.updateFile(filedId, updateFile, sharedFile.getEtag(), templfileName);
//					Thread.sleep(1*5*1000);
					
					FileEntry sharedFile = null;
					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("open");
					sharedFile = universalApi.createSharedLink(filedId, sharedLink);
					//messageList.add("User shared "+filedIdMap.get(filedId));
					Thread.sleep(1*5*1000);
					
					FileEntry unsharedfile = universalApi.disableSharedLink(filedId);
					Thread.sleep(1*5*1000);
					
					
					String uuId= UUID.randomUUID().toString();
					 templfileName = uuId+templfileName;
					universalApi.renameFile(filedId, templfileName);
					Thread.sleep(1*5*1000);
					universalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
					Logger.info("File deleted from onedrive:"+ templfileName);
					
					Thread.sleep(1*5*1000);
					FileEntry bfile = universalApi.restoreFileFromTrash(filedId, templfileName);
					Thread.sleep(1*5*1000);
					universalApi.lockFile(filedId, false);
					Thread.sleep(1*5*1000);
					universalApi.unlockFile(filedId);
					Thread.sleep(1*5*1000);
					universalApi.deleteFile(filedId, unsharedfile.getEtag());
					Thread.sleep(1*5*1000);
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_ONE},
			{ SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_TWO},
			{ SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_THREE},
			{ SequenceDetectorConstants.UPLOAD_DOWNLOAD_SHARE_FOUR},
			
			{ SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_ONE},
			{ SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_TWO},
			{ SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_THREE},
			{ SequenceDetectorConstants.SAHRE_UNSHARE_RENAME_FOUR},
			
			{ SequenceDetectorConstants.LOCK_UNLOCK_DELETE_ONE},
			{ SequenceDetectorConstants.LOCK_UNLOCK_DELETE_TWO},
			{ SequenceDetectorConstants.LOCK_UNLOCK_DELETE_THREE},
			{ SequenceDetectorConstants.LOCK_UNLOCK_DELETE_FOUR},
			
				};
	}


	
	@Test(dataProvider="fileOperationsDataProvider")
	public void fileOperationsSDTest(Method method, String name) throws Exception{
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
		}
	
	
	
	@DataProvider
	public Object[][] folderOperationsDataProvider() throws JAXBException, Exception  {
		
		
		String[] activities = new String[]{"Upload", "Copy", "Rename"};
		
		String[] activities1 = new String[]{"Copy", "Rename", "Delete"};
		
		String[] activities2 = new String[]{"Share", "Unshare", "Delete"};
		
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"API", "API", "API"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"Folder", "Folder", "Folder"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"__any", "__any", "__any"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;
		

		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_COPY_RENAME_ONE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_COPY_RENAME_TWO, 2,600, true   ,true, true,
				activities, failities1, sources1, users1, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_COPY_RENAME_THREE, 1,400, false   ,false, false,
				activities, failities, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_COPY_RENAME_FOUR, 1,400, true  ,true, true,
				activities, failities1, sources1, users, objects1);
		 response = dsd.createSequenceDetector(sdInput, suiteData);
		 
		  sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_DELETE_ONE, 2,400, false   ,false, false,
				  activities1, failities, sources, users, objects);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_DELETE_TWO, 2,600, true   ,true, true,
					 activities1, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_DELETE_THREE, 1,400, false   ,false, false,
					activities1, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_DELETE_FOUR, 1,400, true  ,true, true,
					activities1, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			 sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_REANME_DELETE_ONE, 2,400, false   ,false, false,
					  activities2, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_REANME_DELETE_TWO, 2,600, true   ,true, true,
						 activities2, failities1, sources1, users1, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_REANME_DELETE_THREE, 1,400, false   ,false, false,
						activities2, failities, sources1, users, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_REANME_DELETE_FOUR, 1,400, true  ,true, true,
						activities2, failities1, sources1, users, objects1);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
		
			 Logger.info("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 5; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);String randomId = UUID.randomUUID().toString();
					 randomId = UUID.randomUUID().toString();
					String uniqueFolder1   = randomId + "_folder1";
					BoxFolder folderObj1 = universalApi.createFolder(uniqueFolder1);
					Thread.sleep(1*5*1000);
					String folderId1 = folderObj1.getId();
					String uniqueFolder2   = randomId + "_Detect_folder1";
					BoxFolder folderObj2 = universalApi.createFolder(uniqueFolder2);
					Thread.sleep(1*5*1000);
					String folderId2 = folderObj2.getId();
					BoxFolder copiedFolder = universalApi.copyFolder(folderId1, folderId2);
					Thread.sleep(1*5*1000);
					String updatedFolderName3 = randomId +"_updated_folder3";
					universalApi.updateFolder(folderId2, updatedFolderName3);
					Thread.sleep(1*5*1000);
					universalApi.deleteFolder(folderId2, true, null);
					Thread.sleep(1*5*1000);
					
					BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId1);
					Thread.sleep(1*5*1000);
					SharedLink folderSharedLink =  sharedFolder.getSharedLink();
					folderSharedLink.setUnsharedAt(DateUtils.getDaysFromCurrentTime(2));
					universalApi.createSharedLinkForFolder(folderId1, folderSharedLink);
					Thread.sleep(1*5*1000);
					universalApi.disableSharedLinkForFolder(folderId1);
					Thread.sleep(1*5*1000);
					universalApi.restoreFolder(folderId2, updatedFolderName3);
					Thread.sleep(1*5*1000);
					universalApi.moveFolder(folderId1, folderId2);
					Thread.sleep(1*5*1000);
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD_COPY_RENAME_ONE},
			{ SequenceDetectorConstants.UPLOAD_COPY_RENAME_TWO},
			{ SequenceDetectorConstants.UPLOAD_COPY_RENAME_THREE},
			{ SequenceDetectorConstants.UPLOAD_COPY_RENAME_FOUR},
			
			{ SequenceDetectorConstants.COPY_REANME_DELETE_ONE},
			{ SequenceDetectorConstants.COPY_REANME_DELETE_TWO},
			{ SequenceDetectorConstants.COPY_REANME_DELETE_THREE},
			{ SequenceDetectorConstants.COPY_REANME_DELETE_FOUR},
			
			{ SequenceDetectorConstants.SHARE_UNSHARE_DELETE_ONE},
			{ SequenceDetectorConstants.SHARE_UNSHARE_DELETE_TWO},
			{ SequenceDetectorConstants.SHARE_UNSHARE_DELETE_THREE},
			{ SequenceDetectorConstants.SHARE_UNSHARE_DELETE_FOUR},
				};
	}
	
	
	@Test(dataProvider="folderOperationsDataProvider")
	public void folderOperationsSDTest(Method method, String name) throws Exception{
		
		Logger.info("Execution Started - Test Case Name: " + method.getName());
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Logger.info("Execution Completed - Test Case Name: " + method.getName());
			
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
	
	
/*	@AfterClass
	public void deleteFolder() {
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			detectFunctions.deleteFolder(universalApi, suiteData.getSaasApp(), folderInfo);
			detectFunctions.cleanupTempFolder();
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}*/
	
	
	public String validateIncidents(String ioicode) {
		Logger.info("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		String responseBody = null;
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MONTH, -3);
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
				response = esLogs.getCloudServiceAnomalies(restClient, buildCookieHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()), new StringEntity(payload),suiteData);
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				 responseBody = getResponseBody(response);
				Logger.info(" validateIncidents::::  Response::::   " + responseBody);
				JsonNode jnode = unmarshall(responseBody, JsonNode.class);
				
				if (jnode.isArray()) {
				    for (final JsonNode objNode : jnode) {
				    	String    	ioi_found = getJSONValue(objNode.toString(), "ioi").toString().replace("\"", "");
				    	Logger.info("found ioicode:::::::: "+ioi_found);
				    	Logger.info("Expected ioicode:::::::: "+ioicode);
				    	if (ioicode.equalsIgnoreCase(ioi_found)) {
							found_ioi = true;
							break;
						}	
				    	
				       }
				    if (found_ioi) {
						break;
					} else {
						Thread.sleep(1 * 10 * 1000);	//wait for 1 minute.
						Logger.info("Incident  is not found in the detect page Retrying::::::: " + ite + "Minutes of waiting");
					}
				    
				    }
		
				
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		Logger.info("Going to Assert after " + ite + "Minutes of waiting");
		
		Logger.info("Expected IOI_Code:::::: " + ioicode + " not found in the incident list");
		Assert.assertTrue(found_ioi, "incident not listed");
		
		return responseBody;
	}
	
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Logger.info("Retrieving the logs from Elastic Search ...");
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		Date dateTo = new Date();
    	SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'HH");
    	formatDate.setTimeZone(TimeZone.getTimeZone("GMT"));
    	String strDateTimeTo = formatDate.format(dateTo);
    	System.out.println(strDateTimeTo);
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.MINUTE, -50);
    	Date dateFrom = cal.getTime();
    	String strDateTimeFrom = formatDate.format(dateFrom);
    	System.out.println(strDateTimeFrom);
    	com.elastica.beatle.securlets.ESQueryBuilder esqueryBuilder1 = new com.elastica.beatle.securlets.ESQueryBuilder();
    	
		HttpResponse response = null;
		try {
			int messageCount = messageList.size();
			int count = 0;
		HashMap<String, String> terms = new HashMap<>();
			for (int i = 1; i<=20; i++) {
				String payload = 	esqueryBuilder1.getESQueryForInvestigate(strDateTimeFrom+ ":00:00.000Z", strDateTimeTo+":59:59.999Z", "Elastica",
						terms, query, apiServer, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 1000);
//				String payload = esQueryBuilder.getSearchQueryForDisplayLogs(strDateTimeFrom + ":00:00.000Z",
//						strDateTimeTo + ":59:59.999Z", query, "Elastica", 1000, apiServer, csfrToken, sessionID,
//						suiteData.getUsername());
				
				Logger.info("getting investigate logs ::::::  payload::::   " + payload);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				//Logger.info("responseBody:::::::::::::: "+responseBody);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Logger.info("Activity log message on invetsigate page::::::::::  "+message);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
				Logger.info("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min");
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
				break;
				}
			}
			if(messageCount!=count){
			Logger.info(" Expected::: "+messageCount+ " Actual:::::: "+count);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}

}
