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
import org.codehaus.jackson.map.ObjectMapper;
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
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.Groups;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.detect.dto.Source;
import com.elastica.beatle.detect.dto.Steps;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.universal.dtos.UserAccount;

public class DetectSequenceEndtoEndTests_GW extends  DetectUtils{
	
	
	private static final String OBJECTS = "detect_attributes";
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	
	protected UserAccount saasAppUserAccount;
	protected UserAccount saasAppGraphUserAccount;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@BeforeClass()
	public void beforeClass()  {	
		clearIncidents();
		}
	
	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		try {
		dsd.deleteSequenceDetectors(suiteData);
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] shareUnshareDP() throws UnsupportedEncodingException, JAXBException, Exception {
			
		

		String[] activities = new String[]{"Share", "Unshare"};
		String[] failities = new String[]{"Box", "Box"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "Folder"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_UNSHARE_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
		
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				
				
				utils.replayLogs(DetectGWDataSource.BOX_SHARE_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_SHARE_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				utils.replayLogs(DetectGWDataSource.BOX_REMOVE_LINK, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_REMOVE_LINK_MESSAGE);
				Thread.sleep(1*5*1000);
				
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE_UNSHARE, messageList},
			
	{ SequenceDetectorConstants.SHARE_UNSHARE_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_UNSHARE_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_UNSHARE_FOUR, messageList},
			
		};
	}
	
	@Test(dataProvider="shareUnshareDP")
	public void ShareUnshareWaitRepeatSDTest1(Method method, String name, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		
		Log(method, name, suiteData.getSaasAppUsername(), suiteData.getUsername() );

		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		 validateIncidents(ioicode);
		 
		
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] uploadshareDP() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		
		String[] activities = new String[]{"Upload", "Share"};
		String[] failities = new String[]{"Box", "Box"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				
				utils.replayLogs(DetectGWDataSource.BOX_UPLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				utils.replayLogs(DetectGWDataSource.BOX_SHARE_PDF, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_SHARE_PDF_MESSAGE);
				Thread.sleep(1*5*1000);
				System.out.println("Message List is::::"+messageList);
				
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT, messageList},
			
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_TWO, messageList},
		
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_THREE , messageList},
	
	{ SequenceDetectorConstants.UPLOAD_SHARE_WAIT_REPEAT_FOUR, messageList},
			
		};
	}
	

	
	@Test(dataProvider="uploadshareDP")
	public void UploadShareWaitRepeatSDTest(Method method, String name, List<String> messageList) throws Exception{
		
		
		Log(method, name, suiteData.getSaasAppUsername(), suiteData.getUsername() );
		
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		 validateIncidents(ioicode);
		
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] downloadUploadDP() throws UnsupportedEncodingException, JAXBException, Exception {
			
		
		

		String[] activities = new String[]{"Download", "Upload"};
		String[] failities = new String[]{"Box", "Box"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any"} ;
		String[] objects1 = new String[]{"File", "File"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
		
	
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				
				utils.replayLogs(DetectGWDataSource.BOX_DOWNLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DOWNLOAD_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				utils.replayLogs(DetectGWDataSource.BOX_UPLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				
				
			}
			
			//verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT, messageList},
			
	{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_TWO, messageList},
		
	{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_THREE , messageList},
	
	{ SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT_FOUR, messageList},
			
		};
	}
	
	
	
	
	@Test(dataProvider="downloadUploadDP")
	public void DownloadUploadWaitRepeatSDTest(Method method, String name,List<String> messageList  ) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, name, user, user );
		
		
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		 validateIncidents(ioicode);
		 
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] copyDownloadDeleteDP() throws UnsupportedEncodingException, JAXBException, Exception {
			
		String[] activities = new String[]{"Copy", "Download", "Delete"};
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_DOWNLOAD_DELETE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
				
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 6; j++) {
				
				utils.replayLogs(DetectGWDataSource.BOX_COPY_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_COPY_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				
				utils.replayLogs(DetectGWDataSource.BOX_DOWNLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DOWNLOAD_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				
				utils.replayLogs(DetectGWDataSource.BOX_DELETE_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DELETE_TXT_MESSAGE);
				
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.COPY_DOWNLOAD_DELETE, messageList},
			
	{ SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_TWO, messageList},
		
	{ SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_THREE , messageList},
	
	{ SequenceDetectorConstants.COPY_DOWNLOAD_DELETE_FOUR, messageList},
			
		};
	}
	
	
	
	@Test(dataProvider="copyDownloadDeleteDP")
	public void CopyDownloadDeleteWaitRepeatSDTest(Method method, String name, List<String> messageList) throws Exception{
		String user = suiteData.getSaasAppUsername();
		
		Log(method, name, user, user);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		 validateIncidents(ioicode);
		 
			// verifyDetectedUser(suiteData.getSaasAppUsername());
		 
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		System.out.println("Message list::"+messageList);
		
		
	}
	
	
	@SuppressWarnings("unchecked")
	@DataProvider()
	public Object[][] shareAnyUnshareDP() throws UnsupportedEncodingException, JAXBException, Exception {
		
		String[] activities = new String[]{"Share", "__any", "Unshare"};
		String[] failities = new String[]{"Box", "Box", "Box"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername(), suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		String[] failities1 = new String[]{"__any", "__any", "__any"} ;
		String[] sources1 = new String[]{"GW", "GW", "GW"} ;
		String[] users1 = new String[]{"__any", "__any", "__any"} ;
		String[] objects1 = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE, 2,400, false   ,false, false,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		 sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_TWO, 2,600, true   ,true, true,
					activities, failities1, sources1, users1, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_THREE, 1,400, false   ,false, false,
					activities, failities, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			
			
			
			sdInput = dsd.createSDInput(SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_FOUR, 1,400, true  ,true, true,
					activities, failities1, sources1, users, objects1);
			 response = dsd.createSequenceDetector(sdInput, suiteData);
			 Reporter.log("Waiting for a minute After creating sequences");
				Thread.sleep(1*30*1000);
	
			List<String> messageList = new ArrayList<>();
			for (int j = 0; j < 4; j++) {
				
				utils.replayLogs(DetectGWDataSource.BOX_SHARE_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_SHARE_TXT_MESSAGE);
				
				Thread.sleep(1*5*1000);
				
				utils.replayLogs(DetectGWDataSource.BOX_DELETE_TXT, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_DELETE_TXT_MESSAGE);
				Thread.sleep(1*5*1000);
				
				utils.replayLogs(DetectGWDataSource.BOX_REMOVE_LINK, suiteData, suiteData.getSaasAppUsername());
				messageList.add(DetectGWDataSource.BOX_REMOVE_LINK_MESSAGE);
				
				
			}
			
		//	verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
			
		
	//
		return new Object[][]{
			
			
					//name									//sequenceGroups  threshold  	window   facility_individually        source_individually   user_individually   activityTypeMap                facilityMap                   sourcesMap             userMap           objectTypeMap
	{  SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE, messageList},
			
	{ SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_TWO, messageList},
		
	{ SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_THREE , messageList},
	
	{ SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE_FOUR, messageList},
			
		};
	}
	
	@Test(dataProvider="shareAnyUnshareDP")
	public void ShareAnyActivityUnshareWaitRepeatSDTest(Method method, String name, List<String> messageList) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		
		Log(method, name, user, user);
		
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		Thread.sleep(60000);
		// verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioicode);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
		
		
	}
	
	
	
	
	
	/*@Test
	public void ShareDownloadshareUnshareWaitRepeatSDTest(Method method) throws Exception{
		
		
		
		String user = suiteData.getSaasAppUsername();
		String name = SequenceDetectorConstants.SHARE_DOWNLOAD_UNSHARE_WAIT_REPEAT;
		
		Log(method, name, user, user);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =600;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"Share"});
		activityTypeMap.put(2, new String[]{"Download"});
		activityTypeMap.put(3, new String[]{"Share"});
		activityTypeMap.put(4, new String[]{"Unshare"});
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"Box"});
		facilityMap.put(2, new String[]{"Box"});
		facilityMap.put(3, new String[]{"Box"});
		facilityMap.put(4, new String[]{"Box"});

		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"GW"});
		sourcesMap.put(2, new String[]{"GW"});
		facilityMap.put(3, new String[]{"GW"});
		facilityMap.put(4, new String[]{"GW"});

		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{user});
		userMap.put(2, new String[]{user});
		userMap.put(3, new String[]{user});
		userMap.put(4, new String[]{user});

		
										
		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"File"});
		objectTypeMap.put(2, new String[]{"File/Folder"});
		objectTypeMap.put(3, new String[]{"Folder"});
		objectTypeMap.put(4, new String[]{"Folder"});

	
										
		
		HttpResponse response = createSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		System.out.println("dsdto:::::: "+responseBody);
		
		

		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			dcUtils.replayLogs(DetectGWDataSource.BOX_SHARE_TXT, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_SHARE_TXT_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs(DetectGWDataSource.BOX_DOWNLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_DOWNLOAD_TXT_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs(DetectGWDataSource.BOX_REMOVE_LINK, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_REMOVE_LINK_MESSAGE);
			Thread.sleep(1*5*1000);
			
			
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		Thread.sleep(60000);
		 
		
		 validateIncidents(ioicode);
		 
		// verifyDetectedUser(name);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}
	*/
	/*//@Test
	public void CreateShareSDTest(Method method) throws Exception{
		
		String user = suiteData.getSaasAppUsername();

		String name = SequenceDetectorConstants.CREATE_SHARE_TRASH_DELETE;
		
		Log(method, name, user, user);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =600;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"Create"});
		activityTypeMap.put(2, new String[]{"Share"});
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"Box"});
		facilityMap.put(2, new String[]{"Box"});
		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"GW"});
		sourcesMap.put(2, new String[]{"GW"});
		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{user});
		userMap.put(2, new String[]{user});
										
		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"File"});
		objectTypeMap.put(2, new String[]{"File"});
		
										
		
		HttpResponse response = createSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		System.out.println("dsdto:::::: "+responseBody);
		
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 7; j++) {
			dcUtils.replayLogs(DetectGWDataSource.BOX_CREATE_DOC, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_CREATE_DOC_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs(DetectGWDataSource.BOX_SHARE_TXT, suiteData, suiteData.getSaasAppUsername());
			 messageList.add(DetectGWDataSource.BOX_SHARE_TXT_MESSAGE);
			Thread.sleep(1*5*1000);
			
			
			
		}
			
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		String ioicode = TOO_MANY_SEQUENCE+name;

		
		Thread.sleep(60000);
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioicode);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}
		
	
	
	//@Test
	public void CreateDeleteViewRenameSDTest(Method method) throws Exception{
		
		String user = suiteData.getSaasAppUsername();
		String name = SequenceDetectorConstants.CREATE_DELETE_RENAME;
		Log(method, name, user, user);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =600;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"Create"});
		activityTypeMap.put(2, new String[]{"Delete"});
		activityTypeMap.put(3, new String[]{"View"});
		activityTypeMap.put(4, new String[]{"Rename"});
		
		
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"Box"});
		facilityMap.put(2, new String[]{"Box"});
		facilityMap.put(3, new String[]{"Box"});
		facilityMap.put(4, new String[]{"Box"});

		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"GW"});
		sourcesMap.put(2, new String[]{"GW"});
		sourcesMap.put(3, new String[]{"GW"});
		sourcesMap.put(4, new String[]{"GW"});

		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{suiteData.getSaasAppUsername()});
		userMap.put(2, new String[]{suiteData.getSaasAppUsername()});
		userMap.put(3, new String[]{suiteData.getSaasAppUsername()});
		userMap.put(4, new String[]{suiteData.getSaasAppUsername()});

		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"__any"});
		objectTypeMap.put(2, new String[]{"__any"});
		objectTypeMap.put(3, new String[]{"__any"});
		objectTypeMap.put(4, new String[]{"__any"});

	
										
		
		HttpResponse response = createSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		System.out.println("dsdto:::::: "+responseBody);
		
		
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			
			dcUtils.replayLogs(DetectGWDataSource.BOX_CREATE_DOC, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_CREATE_DOC_MESSAGE);
			Thread.sleep(1*5*1000);
			dcUtils.replayLogs(DetectGWDataSource.BOX_DELETE_TXT, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_DELETE_TXT_MESSAGE);
			dcUtils.replayLogs(DetectGWDataSource.BOX_RENAME_FILE, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_RENAME_FILE_MESSAGE);
			
			
		}
		String ioicode = TOO_MANY_SEQUENCE+name;

		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);

		
		Thread.sleep(60000);
		// verifyDetectedUser(suiteData.getSaasAppUsername());
		
		validateIncidents(ioicode);
		System.out.println("String IOI"+ioicode);

		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}*/
	
//	@Test
//	public void InvalidloginLoginUploadSDTest(Method method) throws Exception{
//		
//		String user = suiteData.getSaasAppUsername();
//		String name = SequenceDetectorConstants.INVALID_LOGIN;
//		
//		Log(method, name, user, user);
//		
//		int sequenceGroups=1;
//		int threshold = 2;
//		int window =600;
//		
//		
//		boolean facility_individually = false;
//		boolean source_individually = false;
//		boolean user_individually =true;
//		boolean user_external =false;
//		
//		Map<Integer, String[] > activityTypeMap = new HashMap<>();
//		activityTypeMap.put(1, new String[]{"InvalidLogin"});
//		activityTypeMap.put(2, new String[]{"Login"});
//		activityTypeMap.put(3, new String[]{"Upload"});
//		
//		
//
//		Map<Integer, Integer > thresholdMap = new HashMap<>();
//		thresholdMap.put(1, 10);
//		thresholdMap.put(2, 1);
//		thresholdMap.put(3, 1);
//		
//		Map<Integer, Integer > windowMap = new HashMap<>();
//		windowMap.put(1, 20);
//		windowMap.put(2, 1);
//		windowMap.put(3, 1);
//		
//		
//		Map<Integer, String[] > facilityMap = new HashMap<>();
//		facilityMap.put(1, new String[]{"__any"});
//		facilityMap.put(2, new String[]{"__any"});
//		facilityMap.put(3, new String[]{"__any"});
//		
//		
//		Map<Integer, String[] > sourcesMap = new HashMap<>();
//		sourcesMap.put(1, new String[]{"__any"});
//		sourcesMap.put(2, new String[]{"__any"});
//		sourcesMap.put(3, new String[]{"__any"});
//		
//		
//		Map<Integer, String[] > userMap = new HashMap<>();
//		userMap.put(1, new String[]{user});
//		userMap.put(2, new String[]{user});
//		userMap.put(3, new String[]{user});
//										
//		Map<Integer, String[] > objectTypeMap = new HashMap<>();
//		objectTypeMap.put(1, new String[]{"__any"});
//		objectTypeMap.put(2, new String[]{"__any"});
//		objectTypeMap.put(3, new String[]{"__any"});
//	
//										
//		
//		HttpResponse response = createSelfLoopSequenceDetector(sequenceGroups, threshold, window, 
//				facility_individually,source_individually, user_individually, 
//				activityTypeMap, name, facilityMap, sourcesMap, userMap,
//				objectTypeMap, thresholdMap, windowMap, user_external);
//		
//		
//		System.out.println("dsdto:::::: "+response.getStatusLine());
//		String responseBody = getResponseBody(response);
//		System.out.println("dsdto:::::: "+responseBody);
//		
//		
//		List<String> messageList = new ArrayList<>();
//		for (int j = 0; j < 4; j++) {
//			
//			dcUtils.replayLogs("InvalidLogin.log", suiteData, suiteData.getSaasAppUsername());
//			//messageList.add("User invalid login file \"Test.txt\" folder \"\"");
//			Thread.sleep(1*5*1000);
//			dcUtils.replayLogs("login.log", suiteData, suiteData.getSaasAppUsername());
//			//messageList.add("User log in \"\"");
//			dcUtils.replayLogs("Test,AFolder,upload_txt.log", suiteData, suiteData.getSaasAppUsername());
//			messageList.add(DetectGWDataSource.BOX_USER_UPLOADED_FILE_TEST_TXT_TO_FOLDER_D_FOLDER);
//			
//			
//		}
//		
//		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
//		String ioicode = TOO_MANY_SEQUENCE+name;
//
//		
//		Thread.sleep(60000);
//		 verifyDetectedUser(suiteData.getSaasAppUsername());
//		
//		 validateIncidents(ioicode);
//		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
//		
//		
//		
//	}

	

		

	//@Test()
	public void Potential_Data_Exfiltration_through_Unauthorized_Uploads_in_a_Hijacked_Account(Method method) throws Exception{
		
		String name = "Potential Data Exfiltration through Unauthorized Uploads in a Hijacked Account: There have been a number of i"
				+ "nvalid login attempts possibly indicating a brute force attack. Then there is a valid login followed quickly by an upload. "
				+ "This may that the brute force attack succeeded, and some company data was exfiltrated.";
		
		Log(method, name, suiteData.getSaasAppUsername(), suiteData.getUsername() );
		
		
		
		
	
		List<String> messageList = new ArrayList<>();
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 10; i++) {
				utils.replayLogs("InvalidLogin.log", suiteData, suiteData.getSaasAppUsername());
				//messageList.add("User uploaded file \"Test.txt\" to folder \"\"");
			}
			utils.replayLogs("login.log", suiteData, suiteData.getSaasAppUsername());
			
			Thread.sleep(1*2*1000);
			utils.replayLogs(DetectGWDataSource.BOX_UPLOAD_TXT, suiteData, suiteData.getSaasAppUsername());
			messageList.add(DetectGWDataSource.BOX_UPLOAD_TXT_MESSAGE);
			Thread.sleep(1*2*1000);
		}
		
		verifyActivityCount(suiteData.getSaasAppUsername(), messageList);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		Thread.sleep(60000);
		 verifyDetectedUser(suiteData.getSaasAppUsername());
		
		 validateIncidents(ioicode);
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

	private HttpResponse createSelfLoopSequenceDetector(int sequenceGroups, int threshold, int window,
			boolean facility_individually, boolean source_individually, boolean user_individually,
			Map<Integer, String[]> activityTypeMap, String name, Map<Integer, String[]> facilityMap,
			Map<Integer, String[]> sourcesMap, Map<Integer, String[]> userMap, Map<Integer, String[]> objectTypeMap,
			Map<Integer, Integer> thresholdMap, Map<Integer, Integer> windowMap, boolean user_external)
					throws JAXBException, UnsupportedEncodingException, Exception {
		DetectSequenceDto dsdto = new DetectSequenceDto();
		dsdto.setId("");
		dsdto.setRequestType(INSERT);
		Source source = new Source();
		source.setName(name);
		source.setDescription(name);
		source.setEnabled(true);
		source.setImportance(2);
		source.setTs_label("");
		
		List<Groups> groups = new ArrayList<>();
		
		for (int i = 1; i <= sequenceGroups; i++) {
			Groups group = new Groups();
			group.setThreshold(threshold);
			group.setWindow(window);
			List<Steps> steps = new ArrayList<>();
			for (int j = 1; j <= activityTypeMap.size(); j++) {
				Steps step = new Steps();
				step.setActivityType(activityTypeMap.get(j));
				step.setFacility(facilityMap.get(j));
				step.setFacility_individually(facility_individually);
				step.setMax_gap_time(-1);
				step.setObjectType(objectTypeMap.get(j));
				step.setSource(sourcesMap.get(j));
				step.setSource_individually(source_individually);
				step.setThreshold(thresholdMap.get(j));
				step.setWindow(windowMap.get(j));
				step.setUser_individually(user_individually);
				step.setUser_external(user_external);
				step.setUser(userMap.get(j));
				steps.add(step);
			}
			
			group.setSteps(steps);
			groups.add(group);
		}
		
		source.setGroups(groups);
		
		
		
		dsdto.setSource(source);
		System.out.println("Sequence detector :::::: "+utils.marshall(dsdto));
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		StringEntity se =new StringEntity(utils.marshall(dsdto));
		HttpResponse response = esLogs.detectsequences(restClient, buildCookieHeaders(), se, suiteData);
		Thread.sleep(1 * 30 * 1000);
		return response;
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
