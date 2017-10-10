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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.openqa.selenium.logging.NeedsLocalLogs;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.SequenceDetectorConstants;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectThreatScoreBasedPolicyTests extends  DetectUtils  {
	
	private static final String OBJECTS = "detect_attributes";
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
	AttributeBean attributeBean ;
	
	//DetectUtils utils = new DetectUtils();
	DetectFunctions detectFunctions = new DetectFunctions();
	DetectCommonutils utils = new DetectCommonutils();
	Map<String, String> props =null;
	Map<String, String> filedIdMap = new HashMap<>();
	ProtectFunctions protectFunctions = new ProtectFunctions();
	PolicyBean policyBean = new PolicyBean();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@BeforeClass()
	public void beforeClass() throws Exception {
		
	//	clearIncidents();
		
		dsd.deleteSequenceDetectors(suiteData);
		
		try {
			UserAccount account = detectFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = detectFunctions.createFolder(universalApi, suiteData.getSaasApp(), "DETECT_BE_AUTOMATION"+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
		
		
	}
	
	
	
	
	@Test
	public void testThreatScoreBasedPolicies() throws Exception{
		
		//TODO:Create sequence
		String[] activities = new String[]{"Upload"};
		String[] failities = new String[]{"__any"} ;
		String[] sources = new String[]{"__any"} ;
		String[] users = new String[]{suiteData.getSaasAppUsername()} ;
		String[] objects = new String[]{"__any"} ;
		
		String sequenceName = SequenceDetectorConstants.UPLOAD;
		SDInput sdInput = dsd.createSDInput(sequenceName, 1,400, false   ,false, false,
				activities, failities, sources, users, objects);
		sdInput.setImportance(4);
		HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
		Logger.info("Sequnce created sucessfully ::::: Created sequence name ::::: "+sequenceName);
		
		//TODO:Create Policy
		String policyName = "GDTSPolicy_"+protectFunctions.generateAlphaNumericString(3);
		String[] data = new String[] { policyName, "Google Drive", "Any", "Any", "Any", "no", "No", "4",  "BLOCK_SERVICE"};
		policyBean = protectFunctions.setThreatScorePolicyData(data);
		protectFunctions.createAndActivateThreatScoreBasedPolicy(restClient, buildCookieHeaders(), suiteData, policyBean);
		
		Thread.sleep(1*60*1000);
		
		Logger.info("Threat score policy created suceesfully   ::::: Policy name ::::: "+policyBean.getPolicyName());
		
		//TODO:generate incident
		List<String> messageList = new ArrayList<>();
		UserAccount account = detectFunctions.getUserAccount(suiteData);
		UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
		String templfileName = DetectCommonutils.createSampleFileType("1mb001.bin");
		String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
		filedIdMap.put(templfileName, filedId);
		messageList.add("User uploaded file "+templfileName);
		Logger.info("Uploaded file to saasApp  ::::: file name ::::: "+templfileName);
		Thread.sleep(1*5*1000);
		
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		//TODO:verify incident
		String ioicode = TOO_MANY_SEQUENCE+sequenceName;
		 validateIncidents(ioicode);
		 
		//verify policy tri
		 Thread.sleep(3*60*1000);
		 Map<String, String> policyViolationLogDetails = protectFunctions.getThreatScorePolicyViolationAlertLogDetails(restClient, policyBean, buildBasicHeaders(), suiteData);
			protectFunctions.assertThreatScorePolicyViolation(policyViolationLogDetails, policyBean, suiteData);
			
			Map<String, String> blockDetails = protectFunctions.getProtectBlockDetails(restClient, policyBean, buildCookieHeaders(), suiteData);
			protectFunctions.clearBlock(restClient, policyBean, buildCookieHeaders(), suiteData, blockDetails);
			protectFunctions.deactivateAndDeletePolicy(restClient, policyName, buildCookieHeaders(), suiteData);
		
	}
	
	
	
	@Test
	public void testThreatScoreBasedPoliciesforTBI(Method method) throws Exception{
		
		
		//TODO:Create Policy
				String policyName = "GDTSPolicy_"+protectFunctions.generateAlphaNumericString(3);
				String[] data = new String[] { policyName, "Google Drive", "Any", "Any", "Any", "no", "No", "4",  "BLOCK_SERVICE"};
				policyBean = protectFunctions.setThreatScorePolicyData(data);
				protectFunctions.createAndActivateThreatScoreBasedPolicy(restClient, buildCookieHeaders(), suiteData, policyBean);
				Logger.info("Threat score policy created suceesfully   ::::: Policy name ::::: "+policyBean.getPolicyName());
				Thread.sleep(1*60*1000);
				
				
				 attributeBean = new AttributeBean(2, 5, 4, null);
					attributeBean.setEnabled(true);
					
					
					HttpResponse resp = getDetectAttributes();
					String  responseBody = getResponseBody(resp);
					org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
					String ioicode = "TOO_MANY_SUM_LARGE_UPLOADS";
					boolean enabled = attributeBean.isEnabled();
					updateDetectAttributes(enabled, attributeBean, getResponseArray, ioicode);
					Logger.info("updated preferences ::::::     " + attributeBean.toString());
					
				
					final String[] FILE_NAME = {"1mb001.bin","1mb002.bin","1mb003.bin","1mb004.bin", "1mb005.bin"};
					//Set preferences 
					
					Log(method, attributeBean);
					
					
					UserAccount account = detectFunctions.getUserAccount(suiteData);
					UniversalApi universalApi = detectFunctions.getUniversalApi(suiteData, account);
					List<String> messageList = new ArrayList<>();
					for (int j = 0; j < FILE_NAME.length; j++) {
						String templfileName = DetectCommonutils.createSampleFileType(FILE_NAME[j] );
						String filedId = detectFunctions.uploadFile(universalApi, suiteData.getSaasApp(), folderInfo.get("folderId"), templfileName);
						filedIdMap.put(templfileName, filedId);
						messageList.add("User uploaded file "+templfileName);
						Thread.sleep(1*2*1000);
					}
					
		
		verifyActivityCount(suiteData.getUsername(), messageList);
		
		//TODO:verify incident
		//String ioicode = TOO_MANY_SEQUENCE+sequenceName;
		 validateIncidents(ioicode);
		 
		//verify policy tri
		 Thread.sleep(3*60*1000);
		 Map<String, String> policyViolationLogDetails = protectFunctions.getThreatScorePolicyViolationAlertLogDetails(restClient, policyBean, buildBasicHeaders(), suiteData);
			protectFunctions.assertThreatScorePolicyViolation(policyViolationLogDetails, policyBean, suiteData);
			
			Map<String, String> blockDetails = protectFunctions.getProtectBlockDetails(restClient, policyBean, buildCookieHeaders(), suiteData);
			protectFunctions.clearBlock(restClient, policyBean, buildCookieHeaders(), suiteData, blockDetails);
			protectFunctions.deactivateAndDeletePolicy(restClient, policyName, buildCookieHeaders(), suiteData);
		
	}
	
	
	
	@AfterMethod
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
	
	
	
	
	private void Log(Method method, AttributeBean attributeBean) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name::::: " + method.getName(), true);
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" window:: "
				+ ""+attributeBean.getWindow()+" event::: "+attributeBean.getThreshold()+" importance:::: "+attributeBean.getImportance(), true);
		Reporter.log(" ");
		Reporter.log("Test Details:::: saas app name::  "+suiteData.getSaasApp()+" :::username:::::  "+suiteData.getSaasAppUsername()+" :::password::::  "
				+ ""+suiteData.getSaasAppPassword()+" :::user role::::    "+suiteData.getSaasAppUserRole(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user name:: "+suiteData.getUsername()+" and password for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}
	
	

	
	
	

}
