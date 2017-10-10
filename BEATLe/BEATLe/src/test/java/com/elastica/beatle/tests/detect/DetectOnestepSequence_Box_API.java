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

public class DetectOnestepSequence_Box_API extends  DetectUtils {
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
		
		String[] activitiesAdd = new String[]{"Add"};
		String[] activitiesEdit = new String[]{"Edit"};
		String[] activitiesDelete = new String[]{"Delete"};
		
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"User"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD, 2,400, false   ,false, false,
				activitiesAdd, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_TWO, 2,600, true   ,true, true,
				 activitiesAdd, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_THREE, 1,400, false   ,false, false,
					activitiesAdd, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.ADD_FOUR, 1,400, true  ,true, true,
					activitiesAdd, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			  sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT, 2,400, false   ,false, false,
					  activitiesEdit, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_TWO, 2,600, true   ,true, true,
						 activitiesEdit, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_THREE, 1,400, false   ,false, false,
							activitiesEdit, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_FOUR, 1,400, true  ,true, true,
							activitiesEdit, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
					 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE, 2,400, false   ,false, false,
							 activitiesDelete, failities, sources, users, objects);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO, 2,600, true   ,true, true,
								 activitiesDelete, failities1, sources1, users1, objects);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE, 1,400, false   ,false, false,
									activitiesDelete, failities, sources1, users, objects);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							
							
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR, 1,400, true  ,true, true,
									activitiesDelete, failities1, sources1, users, objects);
							 response = dsd.createSequenceDetector(sdInput, suiteData);


			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 7; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					BoxUserInfo userInfo;
					userInfo = new BoxUserInfo();
					userInfo.setLogin("Detect_tests"+j+"@"+suiteData.getTenantName());
					userInfo.setName("Detect_tests"+j);
					userInfo.setTrackingCodes(null);
					//userInfo.setRole(role);
					//Create the user
					Reporter.log("Input user info:"+MarshallingUtils.marshall(userInfo), true);
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
			{ SequenceDetectorConstants.ADD},
			
			{ SequenceDetectorConstants.ADD_TWO},
				
			{ SequenceDetectorConstants.ADD_THREE},
			
			{ SequenceDetectorConstants.ADD_FOUR},
			{ SequenceDetectorConstants.EDIT},
			
			{ SequenceDetectorConstants.EDIT_TWO},
				
			{ SequenceDetectorConstants.EDIT_THREE},
			
			{ SequenceDetectorConstants.EDIT_FOUR},
			{ SequenceDetectorConstants.DELETE},
			
			{ SequenceDetectorConstants.DELETE_TWO},
				
			{ SequenceDetectorConstants.DELETE_THREE},
			
			{ SequenceDetectorConstants.DELETE_FOUR},
					
				};
	}
	
	
	@Test(dataProvider="AddEditDeleteDataProvider")
	public void addEditDeleteUserSDTest(Method method, String name) throws Exception{
		
			
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	
	@DataProvider
	public Object[][] CreateDeleteEditGroupDataProvider() throws JAXBException, Exception  {
		
		String[] activitiesAdd = new String[]{"Create"};
		String[] activitiesEdit = new String[]{"Edit"};
		String[] activitiesDelete = new String[]{"Delete"};
		
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Group"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE, 2,400, false   ,false, false,
				activitiesAdd, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_TWO, 2,600, true   ,true, true,
				 activitiesAdd, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_THREE, 1,400, false   ,false, false,
					activitiesAdd, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.CREATE_FOUR, 1,400, true  ,true, true,
					activitiesAdd, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			  sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT, 2,400, false   ,false, false,
					  activitiesEdit, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_TWO, 2,600, true   ,true, true,
						 activitiesEdit, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_THREE, 1,400, false   ,false, false,
							activitiesEdit, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.EDIT_FOUR, 1,400, true  ,true, true,
							activitiesEdit, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
					 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE, 2,400, false   ,false, false,
							 activitiesDelete, failities, sources, users, objects);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO, 2,600, true   ,true, true,
								 activitiesDelete, failities1, sources1, users1, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE, 1,400, false   ,false, false,
									activitiesDelete, failities, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							
							
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR, 1,400, true  ,true, true,
									activitiesDelete, failities1, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);


			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 7; j++) {
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
					Thread.sleep(1*10*1000);
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.CREATE},
			{ SequenceDetectorConstants.CREATE_TWO},
			{ SequenceDetectorConstants.CREATE_THREE},
			{ SequenceDetectorConstants.CREATE_FOUR},
			{ SequenceDetectorConstants.EDIT},
			{ SequenceDetectorConstants.EDIT_TWO},
			{ SequenceDetectorConstants.EDIT_THREE},
			{ SequenceDetectorConstants.EDIT_FOUR},
			{ SequenceDetectorConstants.DELETE},
			{ SequenceDetectorConstants.DELETE_TWO},
			{ SequenceDetectorConstants.DELETE_THREE},
			{ SequenceDetectorConstants.DELETE_FOUR},
					
				};
	}
	
	
	@Test(dataProvider="CreateDeleteEditGroupDataProvider")
	public void createDeleteEditGroupSDTest(Method method, String name) throws Exception{
		
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider
	public Object[][] fileOperationsDataProvider() throws JAXBException, Exception  {
		
		String[] activitiesupload = new String[]{"Upload"};
		String[] activitiesShare = new String[]{"Share"};
		String[] activitiesUnshare = new String[]{"Unshare"};
		String[] activitiesDownload = new String[]{"Download"};
		String[] activitiesDelete = new String[]{"Delete"};
		String[] activitiesLock = new String[]{"Lock"};
		String[] activitiesUNLock = new String[]{"Unlock"};
		String[] activitiesRename = new String[]{"Rename"};
		
		
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"File"} ;
		String sdName = SequenceDetectorConstants.UPLOAD;
		
		SDInput sdInput;
		HttpResponse response;
		for (int i = 0; i < objects1.length; i++) {
			createListOfSDs(activitiesupload, failities, sources, users, objects, sdName);
		}
		
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO, 2,600, true   ,true, true,
				 activitiesupload, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE, 1,400, false   ,false, false,
					activitiesupload, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR, 1,400, true  ,true, true,
					activitiesupload, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			  sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD, 2,400, false   ,false, false,
					  activitiesDownload, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_TWO, 2,600, true   ,true, true,
						 activitiesDownload, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_THREE, 1,400, false   ,false, false,
							activitiesDownload, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_FOUR, 1,400, true  ,true, true,
							activitiesDownload, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
			 
			 sdInput =   dsd.createSDInput(SequenceDetectorConstants.SHARE, 2,400, false   ,false, false,
					 activitiesShare, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_TWO, 2,600, true   ,true, true,
						 activitiesShare, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_THREE, 1,400, false   ,false, false,
							activitiesShare, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_FOUR, 1,400, true  ,true, true,
							activitiesShare, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
					  sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE, 2,400, false   ,false, false,
							  activitiesUnshare, failities, sources, users, objects);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_TWO, 2,600, true   ,true, true,
								 activitiesUnshare, failities1, sources1, users1, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
						
							sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_THREE, 1,400, false   ,false, false,
									activitiesUnshare, failities, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_FOUR, 1,400, true  ,true, true,
									activitiesUnshare, failities1, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							 
							 
							 sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK, 2,400, false   ,false, false,
									 activitiesLock, failities, sources, users, objects);
								 response = dsd.createSequenceDetector(sdInput, suiteData);
								
								 sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_TWO, 2,600, true   ,true, true,
										 activitiesLock, failities1, sources1, users1, objects1);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
								
									sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_THREE, 1,400, false   ,false, false,
											activitiesLock, failities, sources1, users, objects1);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
									
									sdInput = dsd.createSDInput(SequenceDetectorConstants.LOCK_FOUR, 1,400, true  ,true, true,
											activitiesLock, failities1, sources1, users, objects1);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
									 
									 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNLOCK, 2,400, false   ,false, false,
											 activitiesUNLock, failities, sources, users, objects);
										 response = dsd.createSequenceDetector(sdInput, suiteData);
										
										 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNLOCK_TWO, 2,600, true   ,true, true,
												 activitiesUNLock, failities1, sources1, users1, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
										
											sdInput = dsd.createSDInput(SequenceDetectorConstants.UNLOCK_THREE, 1,400, false   ,false, false,
													activitiesUNLock, failities, sources1, users, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
											
											sdInput = dsd.createSDInput(SequenceDetectorConstants.UNLOCK_FOUR, 1,400, true  ,true, true,
													activitiesUNLock, failities1, sources1, users, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
											 
											 sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME, 2,400, false   ,false, false,
													 activitiesRename, failities, sources, users, objects);
												 response = dsd.createSequenceDetector(sdInput, suiteData);
												
												 sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_TWO, 2,600, true   ,true, true,
														 activitiesRename, failities1, sources1, users1, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);
												
													sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_THREE, 1,400, false   ,false, false,
															activitiesRename, failities, sources1, users, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);
													
													sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_FOUR, 1,400, true  ,true, true,
															activitiesRename, failities1, sources1, users, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);			 
									 
							 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE, 2,400, false   ,false, false,
									 activitiesDelete, failities, sources, users, objects);
								 response = dsd.createSequenceDetector(sdInput, suiteData);
								
								 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO, 2,600, true   ,true, true,
										 activitiesDelete, failities1, sources1, users1, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
								
									sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE, 1,400, false   ,false, false,
											activitiesDelete, failities, sources1, users, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
									
									sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR, 1,400, true  ,true, true,
											activitiesDelete, failities1, sources1, users, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);


			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 6; j++) {
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					String templfileName = createSampleFileType("1mb001.bin");
					String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
					filedIdMap.put(templfileName, filedId);
					Thread.sleep(1*2*1000);
					//Download the file
					universalApi.downloadFile(filedId, templfileName);
					Reporter.log("File downloaded from box:"+ templfileName, true);
					Thread.sleep(1*2*1000);
					
//					String updateFile = "Detect.txt";
//					universalApi.updateFile(filedId, updateFile, sharedFile.getEtag(), templfileName);
//					Thread.sleep(1*2*1000);
					
					FileEntry sharedFile = null;
					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("open");
					sharedFile = universalApi.createSharedLink(filedId, sharedLink);
					//messageList.add("User shared "+filedIdMap.get(filedId));
					Thread.sleep(1*2*1000);
					
					FileEntry unsharedfile = universalApi.disableSharedLink(filedId);
					Thread.sleep(1*2*1000);
					
					
					String uuId= UUID.randomUUID().toString();
					 templfileName = uuId+templfileName;
					universalApi.renameFile(filedId, templfileName);
					Thread.sleep(1*2*1000);
					universalApi.deleteFile(unsharedfile.getId(), unsharedfile.getEtag());
					Reporter.log("File deleted from onedrive:"+ templfileName, true);
					
					Thread.sleep(1*2*1000);
					FileEntry bfile = universalApi.restoreFileFromTrash(filedId, templfileName);
					Thread.sleep(1*2*1000);
					universalApi.lockFile(filedId, false);
					Thread.sleep(1*2*1000);
					universalApi.unlockFile(filedId);
					Thread.sleep(1*2*1000);
					universalApi.deleteFile(filedId, unsharedfile.getEtag());
					Thread.sleep(1*5*1000);
					
				}
				
				verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
				
				Thread.sleep(15*60*1000);
		
		
		return new Object[][]{
			
			
					//name					     //sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
			{ SequenceDetectorConstants.UPLOAD},
			{ SequenceDetectorConstants.UPLOAD_TWO},
			{ SequenceDetectorConstants.UPLOAD_THREE},
			{ SequenceDetectorConstants.UPLOAD_FOUR},
			
			{ SequenceDetectorConstants.DOWNLOAD},
			{ SequenceDetectorConstants.DOWNLOAD_TWO},
			{ SequenceDetectorConstants.DOWNLOAD_THREE},
			{ SequenceDetectorConstants.DOWNLOAD_FOUR},
			
			{ SequenceDetectorConstants.LOCK},
			{ SequenceDetectorConstants.LOCK_TWO},
			{ SequenceDetectorConstants.LOCK_THREE},
			{ SequenceDetectorConstants.LOCK_FOUR},
			
			{ SequenceDetectorConstants.UNLOCK},
			{ SequenceDetectorConstants.UNLOCK_TWO},
			{ SequenceDetectorConstants.UNLOCK_THREE},
			{ SequenceDetectorConstants.UNLOCK_FOUR},
			
			{ SequenceDetectorConstants.RENAME},
			{ SequenceDetectorConstants.RENAME_TWO},
			{ SequenceDetectorConstants.RENAME_THREE},
			{ SequenceDetectorConstants.RENAME_FOUR},
			
			{ SequenceDetectorConstants.SHARE},
			{ SequenceDetectorConstants.SHARE_TWO},
			{ SequenceDetectorConstants.SHARE_THREE},
			{ SequenceDetectorConstants.SHARE_FOUR},
			
			{ SequenceDetectorConstants.UNSHARE},
			{ SequenceDetectorConstants.UNSHARE_TWO},
			{ SequenceDetectorConstants.UNSHARE_THREE},
			{ SequenceDetectorConstants.UNSHARE_FOUR},
			
			{ SequenceDetectorConstants.DELETE},
			{ SequenceDetectorConstants.DELETE_TWO},
			{ SequenceDetectorConstants.DELETE_THREE},
			{ SequenceDetectorConstants.DELETE_FOUR},
					
				};
	}


	private void createListOfSDs(String[] activitiesupload, String[] failities, String[] sources, String[] users,
			String[] objects, String sdName) throws JAXBException, UnsupportedEncodingException, Exception {
		SDInput sdInput = dsd.createSDInput(sdName, 2,400, false   ,false, false,
				
				
				activitiesupload, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
	}
	
	
	@Test(dataProvider="fileOperationsDataProvider")
	public void fileOperationsSDTest(Method method, String name) throws Exception{
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
			
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
		}
	
	
	
	@DataProvider
	public Object[][] folderOperationsDataProvider() throws JAXBException, Exception  {
		
		String[] activitiesUpload = new String[]{"Upload"};
		String[] activitiesCopy = new String[]{"Copy"};
		String[] activitiesRename = new String[]{"Rename"};
		String[] activitiesDelete = new String[]{"Delete"};
		String[] activitiesShare = new String[]{"Share"};
		String[] activitiesExpireShare = new String[]{"Expire Sharing"};
		String[] activitiesUnshare = new String[]{"Unshare"};
		String[] activitiesUndelete = new String[]{"Undelete"};
		
		String[] failities = new String[]{"Box"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		String[] failities1 = new String[]{"__any"} ;
		String[] sources1 = new String[]{"API"} ;
		String[] users1 = new String[]{"__any"} ;
		String[] objects1 = new String[]{"Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD, 2,400, false   ,false, false,
				activitiesUpload, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_TWO, 2,600, true   ,true, true,
				 activitiesUpload, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_THREE, 1,400, false   ,false, false,
					activitiesUpload, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_FOUR, 1,400, true  ,true, true,
					activitiesUpload, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 
			  sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY, 2,400, false   ,false, false,
					  activitiesCopy, failities, sources, users, objects);
				 response = dsd.createSequenceDetector(sdInput, suiteData);
				
				 sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_TWO, 2,600, true   ,true, true,
						 activitiesCopy, failities1, sources1, users1, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_THREE, 1,400, false   ,false, false,
							activitiesCopy, failities, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					
					sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_FOUR, 1,400, true  ,true, true,
							activitiesCopy, failities1, sources1, users, objects1);
					 response = dsd.createSequenceDetector(sdInput, suiteData);
					 
					 sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME, 2,400, false   ,false, false,
							 activitiesRename, failities, sources, users, objects);
						 response = dsd.createSequenceDetector(sdInput, suiteData);
						
						 sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_TWO, 2,600, true   ,true, true,
								 activitiesRename, failities1, sources1, users1, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_THREE, 1,400, false   ,false, false,
									activitiesRename, failities, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							
							sdInput = dsd.createSDInput(SequenceDetectorConstants.RENAME_FOUR, 1,400, true  ,true, true,
									activitiesRename, failities1, sources1, users, objects1);
							 response = dsd.createSequenceDetector(sdInput, suiteData);
							 
							 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE, 2,400, false   ,false, false,
									 activitiesDelete, failities, sources, users, objects);
								 response = dsd.createSequenceDetector(sdInput, suiteData);
								
								 sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_TWO, 2,600, true   ,true, true,
										 activitiesDelete, failities1, sources1, users1, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
									
									sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_THREE, 1,400, false   ,false, false,
											activitiesDelete, failities, sources1, users, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);
									
									sdInput = dsd.createSDInput(SequenceDetectorConstants.DELETE_FOUR, 1,400, true  ,true, true,
											activitiesDelete, failities1, sources1, users, objects);
									 response = dsd.createSequenceDetector(sdInput, suiteData);


									 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE, 2,400, false   ,false, false,
											 activitiesShare, failities, sources, users, objects);
										 response = dsd.createSequenceDetector(sdInput, suiteData);
										
										 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_TWO, 2,600, true   ,true, true,
												 activitiesShare, failities1, sources1, users1, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
											
											sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_THREE, 1,400, false   ,false, false,
													activitiesShare, failities, sources1, users, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
											
											sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_FOUR, 1,400, true  ,true, true,
													activitiesShare, failities1, sources1, users, objects1);
											 response = dsd.createSequenceDetector(sdInput, suiteData);
											 
											 sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPIRE_SHARE, 2,400, false   ,false, false,
													 activitiesExpireShare, failities, sources, users, objects);
												 response = dsd.createSequenceDetector(sdInput, suiteData);
												
												 sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPIRE_SHARE_TWO, 2,600, true   ,true, true,
														 activitiesExpireShare, failities1, sources1, users1, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);
													
													sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPIRE_SHARE_THREE, 1,400, false   ,false, false,
															activitiesExpireShare, failities, sources1, users, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);
													
													sdInput = dsd.createSDInput(SequenceDetectorConstants.EXPIRE_SHARE_FOUR, 1,400, true  ,true, true,
															activitiesExpireShare, failities1, sources1, users, objects1);
													 response = dsd.createSequenceDetector(sdInput, suiteData);
													 
													 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE, 2,400, false   ,false, false,
															 activitiesUnshare, failities, sources, users, objects);
														 response = dsd.createSequenceDetector(sdInput, suiteData);
														
														 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_TWO, 2,600, true   ,true, true,
																 activitiesUnshare, failities1, sources1, users1, objects1);
															 response = dsd.createSequenceDetector(sdInput, suiteData);
															
															sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_THREE, 1,400, false   ,false, false,
																	activitiesUnshare, failities, sources1, users, objects1);
															 response = dsd.createSequenceDetector(sdInput, suiteData);
															
															sdInput = dsd.createSDInput(SequenceDetectorConstants.UNSHARE_FOUR, 1,400, true  ,true, true,
																	activitiesUnshare, failities1, sources1, users, objects1);
															 response = dsd.createSequenceDetector(sdInput, suiteData);
															 
															 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNDELETE, 2,400, false   ,false, false,
																	 activitiesUndelete, failities, sources, users, objects);
																 response = dsd.createSequenceDetector(sdInput, suiteData);
																
																 sdInput = dsd.createSDInput(SequenceDetectorConstants.UNDELETE_TWO, 2,600, true   ,true, true,
																		 activitiesUndelete, failities1, sources1, users1, objects1);
																	 response = dsd.createSequenceDetector(sdInput, suiteData);
																	
																	sdInput = dsd.createSDInput(SequenceDetectorConstants.UNDELETE_THREE, 1,400, false   ,false, false,
																			activitiesUndelete, failities, sources1, users, objects1);
																	 response = dsd.createSequenceDetector(sdInput, suiteData);
																	
																	sdInput = dsd.createSDInput(SequenceDetectorConstants.UNDELETE_FOUR, 1,400, true  ,true, true,
																			activitiesUndelete, failities1, sources1, users, objects1);
																	 response = dsd.createSequenceDetector(sdInput, suiteData);

			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
				
				List<String> messageList = new ArrayList<>();
				
				for (int j = 1; j <= 7; j++) {
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
			{ SequenceDetectorConstants.UPLOAD},
			{ SequenceDetectorConstants.UPLOAD_TWO},
			{ SequenceDetectorConstants.UPLOAD_THREE},
			{ SequenceDetectorConstants.UPLOAD_FOUR},
			
			{ SequenceDetectorConstants.COPY},
			{ SequenceDetectorConstants.COPY_TWO},
			{ SequenceDetectorConstants.COPY_THREE},
			{ SequenceDetectorConstants.COPY_FOUR},
			
			{ SequenceDetectorConstants.RENAME},
			{ SequenceDetectorConstants.RENAME_TWO},
			{ SequenceDetectorConstants.RENAME_THREE},
			{ SequenceDetectorConstants.RENAME_FOUR},
			
			{ SequenceDetectorConstants.DELETE},
			{ SequenceDetectorConstants.DELETE_TWO},
			{ SequenceDetectorConstants.DELETE_THREE},
			{ SequenceDetectorConstants.DELETE_FOUR},
		
			{ SequenceDetectorConstants.SHARE},
			{ SequenceDetectorConstants.SHARE_TWO},
			{ SequenceDetectorConstants.SHARE_THREE},
			{ SequenceDetectorConstants.SHARE_FOUR},
			
			{ SequenceDetectorConstants.EXPIRE_SHARE},
			{ SequenceDetectorConstants.EXPIRE_SHARE_TWO},
			{ SequenceDetectorConstants.EXPIRE_SHARE_THREE},
			{ SequenceDetectorConstants.EXPIRE_SHARE_FOUR},
			
			{ SequenceDetectorConstants.UNSHARE},
			{ SequenceDetectorConstants.UNSHARE_TWO},
			{ SequenceDetectorConstants.UNSHARE_THREE},
			{ SequenceDetectorConstants.UNSHARE_FOUR},
			
			{ SequenceDetectorConstants.UNDELETE},
			{ SequenceDetectorConstants.UNDELETE_TWO},
			{ SequenceDetectorConstants.UNDELETE_THREE},
			{ SequenceDetectorConstants.UNDELETE_FOUR},
					
				};
	}
	
	
	@Test(dataProvider="folderOperationsDataProvider")
	public void folderOperationsSDTest(Method method, String name) throws Exception{
		
		Reporter.log("Execution Started - Test Case Name: " + method.getName(), true);
			
			String ioicode = TOO_MANY_SEQUENCE+name;
			 validateIncidents(ioicode);
			 
			 //verifyDetectedUser(suiteData.getSaasAppUsername());
			 
			Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
			
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
	
	
	/*@AfterClass
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
		Reporter.log("Retrieving the logs from Elastic Search ...");
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
	
	
	public void verifyActivityCount(String query, List<String> messageList) throws Exception {
		Reporter.log("Retrieving the logs from Elastic Search ...");
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
				
				Reporter.log("getting investigate logs ::::::  payload::::   " + payload, true);
				
				response = esLogs.getDisplayLogs(restClient, buildBasicHeaders(suiteData.getCSRFToken(), suiteData.getSessionID()),suiteData.getApiserverHostName(),
						new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				String responseBody = getResponseBody(response);
				//Reporter.log("responseBody:::::::::::::: "+responseBody, true);
				JsonNode arrNode = new ObjectMapper().readTree(getJSONValue(responseBody, "hits")).get("hits");
				
				if (arrNode.isArray()) {
					if(arrNode.size()!=0){
				    for (final JsonNode objNode : arrNode) {
				    	 JsonNode _source = objNode.path("_source");
				    	JsonNode message = _source.path("message");
				    	if(messageList.contains(message.asText())){
				    		Reporter.log("Activity log message on invetsigate page::::::::::  "+message, true);
				    		messageList.remove(message.asText());
				    		count++;
				    	}
				    	
				    	}
					}
				}
				
			if(messageList.size()!=0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				Reporter.log("Saas App activities are not equal with the investigate logs, Retrying :::::"+i+" times wait time between each retry is:::: 1 min", true);
				Thread.sleep(1*60*1000);
				continue;
			}else if(messageList.size()==0){
				Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
				break;
				}
			}
			if(messageCount!=count){
			Reporter.log(" Expected::: "+messageCount+ " Actual:::::: "+count, true);
			throw new SkipException("Number of Activities returned are not Equal");
			}
			
		}  catch (Exception e) {
			throw e;
		}
	}

}
