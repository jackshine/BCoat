package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;

public class BBINodeinSequenceDetector extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	String user = null;
	DetectSequenceDetector dsd = new DetectSequenceDetector();

	@BeforeClass
	public void deleteallSDs(){
		try {
			dsd.deleteSequenceDetectors(suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void frequentUserActionsAndDownloadTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("frequentUserActionsProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SESSIONS";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.FREQUENT_SESSION_DELETE;
		
		String[] activityTypes = new String[]{"Download", "IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString()"};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		
		Integer[] steps = new Integer[]{0,1};
		int threshold = 1;
		int window =300;
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, threshold, window, facility_individually, source_individually, 
				user_individually, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("downloadFrequentUserActionsSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString());
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void invalidLoginLoginfrequentUserActionsSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("frequentUserActionsProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "ANOMALOUSLY_FREQUENT_SESSIONS";
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.INVALID_LOGIN_LOGIN_FREQUENT_USER_ACTIONS;
		
		
		String[] activityTypes = new String[]{"InvalidLogin", "Login","IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString()"};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		
		Integer[] steps = new Integer[]{0,0,1};
		int threshold = 1;
		int window =300;
		
		boolean facility_individually = false;
		boolean source_individually = false;
		boolean user_individually =true;
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, threshold, window, facility_individually, source_individually, 
				user_individually, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("InvalidLoginLoginFrequentUserActionsSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString());
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void frequentShareUnshareSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("frequentShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.FREQUENT_SHARE_UNSHARE;
		
		String[] activityTypes = new String[]{"Unshare", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("frequentShareUnshareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void loginFrequentShareUnshareSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("frequentShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LOGIN_FREQUENT_SHARE_UNSHARE;
		
		String[] activityTypes = new String[]{"Login", ioi_Code,"Unshare"};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1,0};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("loginFrequentShareUnshareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void invaliLoginLoginfrequentShareSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("frequentShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.INVALID_LOGIN_LOGIN_FREQUENT_SHARE;
		
		String[] activityTypes = new String[]{"InvalidLogin","Login", ioi_Code};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("invalidLoginLoginfrequentShareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void largeDownloadAndDeleteTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDownloadProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LARGE_DOWNLOAD_DELETE;
		String[] activityTypes = new String[]{"Delete", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("largeDownloadAndDeleteTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString());
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void loginLargeDownloadAndDeleteTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDownloadProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LOGIN_LARGE_DOWNLOAD_DELETE;
		String[] activityTypes = new String[]{"Login", ioi_Code, "Delete"};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1,0};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("loginLargeDownloadAndDeleteTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString());
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	//@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void loginLargeDownloadLargeDeleteTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDownloadProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code1 = IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString();
		String ioi_Code2 = IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString();
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes1(enabled, attributeBean, getResponseArray, new String[]{ioi_Code1,ioi_Code2});
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code1);
		
		String name = SequenceDetectorConstants.LOGIN_LARGE_DOWNLOAD_LARGE_DELETE;
		
		String[] activityTypes = new String[]{"Login", ioi_Code1, ioi_Code2};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
	
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("loginLargeDownloadAndDeleteTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString());
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void largeUploadAndDeleteTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeUploadProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LARGE_UPLOAD_DELETE;
		String[] activityTypes = new String[]{"Delete", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
	
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("largeuploadDeleteSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void invalidLoginLoginlargeUploadSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeUploadProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.INALID_LOGIN_LOGIN_LARGE_UPLOAD;
		
		String[] activityTypes = new String[]{"InvalidLogin","Login", ioi_Code};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("InvalidLoginLoginlargeuploadSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void largeDeleteAndDownloadTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDeleteProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LARGE_DELETE_DOWNLOAD;
		
		String[] activityTypes = new String[]{"Download", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("largeDeleteDownloadSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void frequentDeleteAndDownloadTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDeleteProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.FREQUENT_DELETE_DOWNLOAD;
		
		String[] activityTypes = new String[]{"Download", ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("frequentDeleteDownloadSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void invalidLoginLoginfrequentDeleteSDTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeDeleteProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.INVALID_LOGIN_LOGIN_FREQUENT_DELETE;
		
		String[] activityTypes = new String[]{"InvalidLogin", "Login",  ioi_Code};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("invalidLoginLoginfrequentDeleteSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void largeShareAndUnshareTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LARGE_SHARE_UNSHARE;
		
		String[] activityTypes = new String[]{"Unshare",  ioi_Code};
		String[] facilities = new String[]{"__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any"} ;
		String[] users = new String[]{"__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("largeShareUnshareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	@Test(description = "This test operates on real API data, and generates BBI frequent sessions  incidents.")
	public void loginLargeShareAndUnshareTest(Method method) throws Exception {
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		InputBean inputBean = createFileUpdateDataforfrequent("largeShareProfile", attributeBean);
		
		Log(method, attributeBean,inputBean);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		verifyProfile(inputBean.getUser(), ioi_Code);
		
		String name = SequenceDetectorConstants.LOGIN_LARGE_SHARE_UNSHARE;
		
		String[] activityTypes = new String[]{"Login",  ioi_Code, "Unshare"};
		String[] facilities = new String[]{"__any", "__any", "__any"} ;
		String[] sources = new String[]{"__any", "__any", "__any"} ;
		String[] users = new String[]{"__any", "__any", "__any"} ;
		String[] objects = new String[]{"__any", "__any", "__any"} ;
		Integer[] steps = new Integer[]{0,1,0};
		
		SDInput sdInput = dsd.createSDInputForBBIinSD(name, 1, 300, false, false, 
				true, activityTypes, facilities, sources, users, objects, steps);
		
		HttpResponse response = dsd.createSequenceDetectorforBBIinSD(sdInput, suiteData);
		
		 responseBody = getResponseBody(response);
		JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
		System.out.println("detect_sequences::::::::::: "+detect_sequences.toString());
		Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		
		 
		//TODO::InjectdownloadActivity And frequent view
		 inputBean =  createFileUpdateDataforBBIInSequenceDetector("loginLargeShareUnshareSDTest", attributeBean, inputBean);
		 scpActivityLogsAndValidate1(method, inputBean);
		 
		 String ioicode = TOO_MANY_SEQUENCE+name;
			
			HashSet<String> expIncidents = new HashSet<String>();
			expIncidents.add(ioicode);
			expIncidents.add(ioi_Code);
			verifyStateIncidents(inputBean.getUser(), expIncidents);
		
	
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	

	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" confidence:: "
				+ ""+attributeBean.getConfidence()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}

	
	
	
	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		System.out.println("Cleaning up Resources");
		
		HttpResponse response;
		String responseBody;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		if (results.isSuccess() && user != null) {
			
			response = esLogs.clearThreatscore(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearState(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearDetectProfile(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);
			
			user = null;
		} else {
			System.out.println("No Cleanup Required - I am not cleaning up data to debug if needed!");
		}
		
		System.out.println("Cleaning up Resources - COMPLETED");
	}

}
