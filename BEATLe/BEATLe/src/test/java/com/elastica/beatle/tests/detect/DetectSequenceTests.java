package com.elastica.beatle.tests.detect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.groovy.util.HashCodeHelper;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.elastica.beatle.FixRetryListener;
import com.elastica.beatle.RetryAnalyzer;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.Groups;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.detect.dto.Source;
import com.elastica.beatle.detect.dto.Steps;
import com.elastica.beatle.es.ActivityLogs;
import com.elastica.beatle.es.ElasticSearchLogs;

@Listeners(value = FixRetryListener.class)
public class DetectSequenceTests extends DetectUtils {
	
	
	private static final String INSERT = "insert";
	private static final String TOO_MANY_SEQUENCE = "TOO_MANY_SEQUENCE_";
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	

	@BeforeClass
	public void deleteSequenceDetectors() throws Exception{
		try {
		dsd.deleteSequenceDetectors(suiteData);
	} catch (Exception e) {
		e.printStackTrace();
	}
		}
	
	@Test()
	public void DownloadUploadWaitRepeatSDTest(Method method) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.DOWNLOAD_UPLOAD_WAIT_REPEAT;
		
		Log(method, name, user, userName );
		
		String[] activities = new String[]{"Download", "Upload"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	
		
		InputBean inputBean = createFileUpdateData("sequence1", user,userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
	}
	
	@Test
	public void ShareUnshareWaitRepeatSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.SHARE_UNSHARE_WAIT_REPEAT;
		
		Log(method, name, user, userName );
		
		String[] activities = new String[]{"Share", "Unshare"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("shareUnshare", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}
	
	@Test
	public void ShareDownloadUnshareWaitRepeatSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.SHARE_DOWNLOAD_UNSHARE_WAIT_REPEAT;
		
		Log(method, name, user, userName);
		
		String[] activities = new String[]{"Share", "Download", "Unshare"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		
		InputBean inputBean = createFileUpdateData("shareDownloadUnshare", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
		
	}
	
	@Test
	public void CreateShareTrashDeleteSDTest(Method method) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.CREATE_SHARE_TRASH_DELETE;
		
		Log(method, name, user, userName);
		
		String[] activities = new String[]{"Create", "Share", "Trash", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 2,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
	
		
		InputBean inputBean = createFileUpdateData("createShareTrashDelete", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	@Test
	public void TrashDeleteSDTest(Method method) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.TRASH_DELETE;
		
		Log(method, name, user, userName);
		String[] activities = new String[]{"Trash", "Delete"};
		String[] failities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{user, user} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("trashDelete", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test
	public void CreateViewShareDeleteSDTest(Method method) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.CREATE_VIEW_SHARE_DELETE;
		
		Log(method, name, user, userName);
		
		String[] activities = new String[]{"Create", "View", "Share", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 2,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("createViewShareDelete", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test
	public void CreateDeleteRenameSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.CREATE_DELETE_RENAME;
		
		Log(method, name, user, userName);
		
		String[] activities = new String[]{"Create", "Delete", "Rename"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 2,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("createDeleteRename", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	@Test
	public void InvalidloginLoginUploadSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.INVALID_LOGIN_LOGIN_UPLOAD;
		
		Log(method, name, user, userName);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =36;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		boolean user_external =false;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"InvalidLogin"});
		activityTypeMap.put(2, new String[]{"Login"});
		activityTypeMap.put(3, new String[]{"Upload"});
		
		
		
		

		Map<Integer, Integer > thresholdMap = new HashMap<>();
		thresholdMap.put(1, 10);
		thresholdMap.put(2, 1);
		thresholdMap.put(3, 1);
		
		Map<Integer, Integer > windowMap = new HashMap<>();
		windowMap.put(1, 20);
		windowMap.put(2, 1);
		windowMap.put(3, 1);
		
		
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"__any"});
		facilityMap.put(2, new String[]{"__any"});
		facilityMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"__any"});
		sourcesMap.put(2, new String[]{"__any"});
		sourcesMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{user});
		userMap.put(2, new String[]{user});
		userMap.put(3, new String[]{user});
										
		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"__any"});
		objectTypeMap.put(2, new String[]{"__any"});
		objectTypeMap.put(3, new String[]{"__any"});
	
										
		
		HttpResponse response = createSelfLoopSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap, thresholdMap, windowMap, user_external);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("invalidLoginloginUpload", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test
	public void InvalidloginLoginDownloadSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.INVALIDLOGIN_LOGIN_DOWNLOAD;
		
		Log(method, name, user, userName);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =36;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		boolean user_external =false;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"InvalidLogin"});
		activityTypeMap.put(2, new String[]{"Login"});
		activityTypeMap.put(3, new String[]{"Download"});
		
		
		
		

		Map<Integer, Integer > thresholdMap = new HashMap<>();
		thresholdMap.put(1, 10);
		thresholdMap.put(2, 1);
		thresholdMap.put(3, 1);
		
		Map<Integer, Integer > windowMap = new HashMap<>();
		windowMap.put(1, 20);
		windowMap.put(2, 1);
		windowMap.put(3, 1);
		
		
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"__any"});
		facilityMap.put(2, new String[]{"__any"});
		facilityMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"__any"});
		sourcesMap.put(2, new String[]{"__any"});
		sourcesMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{user});
		userMap.put(2, new String[]{user});
		userMap.put(3, new String[]{user});
										
		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"__any"});
		objectTypeMap.put(2, new String[]{"__any"});
		objectTypeMap.put(3, new String[]{"__any"});
	
										
		
		HttpResponse response = createSelfLoopSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap, thresholdMap, windowMap, user_external);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("invalidLoginloginDownload", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test
	public void InvalidloginLoginAnySDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.INVALID_LOGIN_LOGIN_ANY;
		
		Log(method, name, user, userName);
		
		int sequenceGroups=1;
		int threshold = 2;
		int window =36;
		
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		boolean user_external =false;
		
		Map<Integer, String[] > activityTypeMap = new HashMap<>();
		activityTypeMap.put(1, new String[]{"InvalidLogin"});
		activityTypeMap.put(2, new String[]{"Login"});
		activityTypeMap.put(3, new String[]{"__any"});
		
		
		
		

		Map<Integer, Integer > thresholdMap = new HashMap<>();
		thresholdMap.put(1, 10);
		thresholdMap.put(2, 1);
		thresholdMap.put(3, 1);
		
		Map<Integer, Integer > windowMap = new HashMap<>();
		windowMap.put(1, 20);
		windowMap.put(2, 1);
		windowMap.put(3, 1);
		
		
		
		Map<Integer, String[] > facilityMap = new HashMap<>();
		facilityMap.put(1, new String[]{"__any"});
		facilityMap.put(2, new String[]{"__any"});
		facilityMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > sourcesMap = new HashMap<>();
		sourcesMap.put(1, new String[]{"__any"});
		sourcesMap.put(2, new String[]{"__any"});
		sourcesMap.put(3, new String[]{"__any"});
		
		
		Map<Integer, String[] > userMap = new HashMap<>();
		userMap.put(1, new String[]{user});
		userMap.put(2, new String[]{user});
		userMap.put(3, new String[]{user});
										
		Map<Integer, String[] > objectTypeMap = new HashMap<>();
		objectTypeMap.put(1, new String[]{"__any"});
		objectTypeMap.put(2, new String[]{"__any"});
		objectTypeMap.put(3, new String[]{"__any"});
	
										
		
		HttpResponse response = createSelfLoopSequenceDetector(sequenceGroups, threshold, window, 
				facility_individually,source_individually, user_individually, 
				activityTypeMap, name, facilityMap, sourcesMap, userMap,
				objectTypeMap, thresholdMap, windowMap, user_external);
		
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		//Assert.assertTrue(getJSONValue(responseBody, "action_status").toString().replace("\"", "").equals("success"), getJSONValue(responseBody, "action_status").toString()+"Assertion failed");
		InputBean inputBean = createFileUpdateData("invalidLoginloginAny", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}


	@Test
	public void ShareAnyActivityUnshareWaitRepeatSDTest(Method method) throws Exception{
		
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.SHARE_ANY_ACTIVITY_UNSHARE;
		
		Log(method, name, user, userName);
		
		String[] activities = new String[]{"Share", "__any", "Unshare"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 2,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		InputBean inputBean = createFileUpdateData("shareDownloadUnshare", user, userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		

		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	@Test
	public void CopyDownloadDeleteWaitRepeatSDTest(Method method) throws Exception{
		
		String randomString = RandomStringUtils.randomAlphanumeric(12);
		String userName = "detect_" + randomString;
		// String userName = "tarak.elastica";
		String user = userName + "@" + suiteData.getTenantDomainName();
		String name = SequenceDetectorConstants.COPY_DOWNLOAD_DELETE;
		
		Log(method, name, user, userName);
		String[] activities = new String[]{"Copy", "Download", "Delete"};
		String[] failities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{user, user, user} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		SDInput sdInput = dsd.createSDInput(name, 2,36, false   ,false, true,
				activities, failities, sources, users, objects);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		
		System.out.println("dsdto:::::: "+response.getStatusLine());
		String responseBody = getResponseBody(response);
		Reporter.log("Sequence detectors response body:::::: "+responseBody, true);
		
		InputBean inputBean = createFileUpdateData("copyDownloadDelete", user,userName, randomString);

		scpActivityLogsAndValidate(method, inputBean);
		
		String ioicode = TOO_MANY_SEQUENCE+name;
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioicode);
		verifyStateIncidents(inputBean.getUser(), expIncidents);
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
		
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
