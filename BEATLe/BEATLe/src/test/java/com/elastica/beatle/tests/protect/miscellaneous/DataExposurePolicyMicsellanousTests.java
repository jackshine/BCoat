package com.elastica.beatle.tests.protect.miscellaneous;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonProcessingException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;

public class DataExposurePolicyMicsellanousTests extends ProtectInitializeTests{

	Client restClient;
	String policyName = null;
	PolicyBean policyBean = new PolicyBean();
	ProtectFunctions protectFunctions = new ProtectFunctions();
	UniversalApi dropboxUniversalApi;
	String[] data =  { "POLICYTEST", "Policy Desc", "DataExposure", "Box", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "file.txt", "txt", "0,0", "no", "no", "no" };
	
	public DataExposurePolicyMicsellanousTests() throws Exception{
		restClient = new Client();
		policyBean = protectFunctions.populatePolicyBean(data);
		policyName = policyBean.getPolicyName();
		dropboxUniversalApi = protectFunctions.loginToApp(suiteData, "Dropbox");
		
	}
	
	@Test(priority = 1)
	public void verifyPolicyCreated() throws Exception{
		Reporter.log("Starting testcase verifyPolicyCreated", true);
		protectFunctions.deleteExistingPolicy(policyName, restClient, requestHeader, suiteData);
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse response = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyName);
		//HttpResponse response = protectFunctions.createDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
		protectFunctions.verifyCreateAndUpdatePolicyResponse(response);
		Reporter.log("Completed testcase verifyPolicyCreated", true);
		HttpResponse duplicatePolicyResponse = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyName);
		String responseBody = ClientUtil.getResponseBody(duplicatePolicyResponse);
		String createPolicyStatus = ClientUtil.getJSONValue(ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "api_response"), "documentshareapipolicy"), "policy");
		Assert.assertEquals(createPolicyStatus, "\"Another policy exists with the same name\"");
	}
	
	@Test(priority = 2)
	public void verifyDuplicatePolicyCreation() throws Exception{
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse duplicatePolicyResponse = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyName);
		String responseBody = ClientUtil.getResponseBody(duplicatePolicyResponse);
		String createPolicyStatus = ClientUtil.getJSONValue(ClientUtil.getJSONValue(ClientUtil.getJSONValue(responseBody, "api_response"), "documentshareapipolicy"), "policy");
		Assert.assertEquals(createPolicyStatus, "\"Another policy exists with the same name\"");
	}
	
	@Test(priority = 3)
	public void verifyPolicyActivated() throws Exception{
		Reporter.log("Starting testcase verifyPolicyActivated", true);
		protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		Assert.assertTrue(protectFunctions.isPolicyActive(restClient, policyName, requestHeader, suiteData));
		Reporter.log("Completed testcase verifyPolicyActivated", true);
	}
	
	@Test(priority = 4)
	public void verifyCreatedPolicyExists() throws Exception{
		Reporter.log("Starting testcase verifyCreatedPolicyExists", true);
		boolean policyExists = protectFunctions.isPolicyExists(restClient, policyName, requestHeader, suiteData);
		Assert.assertTrue(policyExists);
		Reporter.log("Completed testcase verifyCreatedPolicyExists", true);
	}
	
	@Test(priority = 5)
	public void verifyPolicyDetails() throws Exception{
		Reporter.log("Starting testcase verifyPolicyDetails", true);
		Map<String, String> policyDetails = protectFunctions.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String exposureType = policyDetails.get(ProtectTestConstants.EXPOSURE_TYPE);
		Assert.assertEquals(exposureType, policyBean.getExposureType());
		Reporter.log("Completed testcase verifyPolicyDetails", true);
	}
	
	@Test(priority = 6)
	public void verifyEditPolicy() throws Exception{
		Reporter.log("Starting testcase verifyEditPolicy", true);
		policyBean = null;
		policyBean = protectFunctions.populatePolicyBean(data);
		// Make a get policy call to get the policy details
		String exposureType = getExposureTypeForPolicy(policyBean.getPolicyName(), "documentshareapi");
		Logger.info("Previous Exposure type set in the policy: "+ exposureType);
		Logger.info("Modifying the exposure type to external");
		
		policyBean.setExposureType("external");
		Logger.info(protectFunctions.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData));
		String policyId = protectFunctions.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData).get(ProtectTestConstants.POLICY_SUB_ID);
		String payload = protectFunctions.generateEditDataExposurePolicyPayload(restClient, policyBean, requestHeader, suiteData, policyId);
		HttpResponse response = protectFunctions.editDataExposurePolicy(restClient, payload, requestHeader, suiteData, policyName);
		Logger.info("Edit policy Response:"+ClientUtil.getResponseBody(response));
		//HttpResponse response = protectFunctions.editDataExposurePolicy(restClient, policyBean, requestHeader, suiteData, policyId);
		Logger.info("Validating the exposure type after modification");
		exposureType = getExposureTypeForPolicy(policyBean.getPolicyName(), "documentshareapi");
		Assert.assertTrue(exposureType.equals("external"));
		
		// Make a get policy call to get the policy details and verify policy is edited
		//protectFunctions.verifyCreateAndUpdatePolicyResponse(response);
		Reporter.log("Completed testcase verifyEditPolicy", true);
	}

	@Test(priority = 7)
	public void verifyPolicyDetailsAfterEdit() throws Exception{
		Reporter.log("Starting testcase verifyPolicyDetailsAfterEdit", true);
		Map<String, String> policyDetails = protectFunctions.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		String exposureType = policyDetails.get(ProtectTestConstants.EXPOSURE_TYPE);
		Assert.assertEquals(policyBean.getExposureType(), exposureType);
		Reporter.log("Completed testcase verifyPolicyDetailsAfterEdit", true);
	}
	
	@Test(priority = 8)
	public void verifyUnableToCreatePolicyWithExistingName() throws IOException{
		Reporter.log("Starting testcase verifyUnableToCreatePolicyWithExistingName", true);
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse response = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyName);
		String policyResponseBody = ClientUtil.getResponseBody(response);
		String createPolicyStatus = ClientUtil.getJSONValue(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyResponseBody, "api_response"), "documentshareapipolicy"), "policy");
		Assert.assertEquals(createPolicyStatus, "\"Another policy exists with the same name\"");
		Reporter.log("Completed testcase verifyUnableToCreatePolicyWithExistingName", true);
	}
	
	@Test(priority = 9)
	public void verifyPolicyDeactivate() throws Exception{
		Reporter.log("Starting testcase verifyPolicyDeactivate", true);
		protectFunctions.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
		Reporter.log("Completed testcase verifyPolicyDeactivate", true);
	}
	
	@Test(priority = 10)
	public void verifyPolicyDelete() throws Exception{
		Reporter.log("Starting testcase verifyPolicyDelete", true);
		protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
		Reporter.log("Completed testcase verifyPolicyDelete", true);
		// get the list and verify if deleted policy exist
		
		String restAPI = protectFunctions.replaceGenericParams(suiteData.getAPIMap().get("policyList"), suiteData);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			Logger.info("Policy Name:"+name);
			name = name.substring(1, name.length()-1);
			
			if (policyName.equalsIgnoreCase(name))
				Assert.assertTrue(false, "Deleted Policy still exist in the policy list");
			
		}		
		
		
	}
	
	@Test(priority = 11)
	public void verifyPolicyNotExists() throws Exception{
		Reporter.log("Starting testcase verifyPolicyNotExists", true);
		boolean policyExists = protectFunctions.isPolicyExists(restClient, policyName, requestHeader, suiteData);
		Assert.assertFalse(policyExists);
		Reporter.log("Completed testcase verifyPolicyNotExists", true);
	}
	
	@Test(priority = 12)
	public void verifyPolicyCreationWithoutPolicyName() throws Exception{
		Reporter.log("Starting testcase verifyPolicyCreationWithoutPolicyName", true);
		policyBean.setPolicyName("");
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse response = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyBean.getPolicyName());
		String policyResponseBody = ClientUtil.getResponseBody(response);		
		String createPolicyStatus = ClientUtil.getJSONValue(policyResponseBody,"__error");
		Assert.assertEquals(createPolicyStatus, "\"Unreadable body.\"");
		Reporter.log("Completed testcase verifyPolicyNotCreatedWithNoName", true);
	}
	
	@Test(priority = 13)
	public void verifyPolicyNameLength255Chars() throws Exception{
		Reporter.log("Starting testcase verifyPolicyNameLength255Chars", true);
		policyBean.setPolicyName(protectFunctions.generateAlphaNumericString(255));
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse response = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, protectFunctions.generateAlphaNumericString(255));
		protectFunctions.verifyCreateAndUpdatePolicyResponse(response);
		Reporter.log("Completed testcase to verify policy could be created with 255 characters long", true);
	}	
	
	@Test(priority = 14)
	public void verifyDeletedPolicyIsViolated() throws Exception{
		Reporter.log("Starting testcase verify deleted policy violation", true);
		String[] data = { "DBPVPOLTEST", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PII", "no", "no" };
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
				policyBean.getFileExt());
		policyBean.setFileName(file.getName());
		String payload = protectFunctions.generatePayloadForDataExposurePolicy(policyBean, suiteData);
		HttpResponse response = protectFunctions.createPolicy(restClient, payload, requestHeader, suiteData, policyName);		
		protectFunctions.verifyCreateAndUpdatePolicyResponse(response);		

		// delete the policy
		protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
		
		// upload the file in saas app and verify policy alerts are triggered
		String childFolderName = "AA_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Dropbox : " + childFolderName, true);
		String childFolder = "/PolicyViolations" + System.currentTimeMillis() + "/" + childFolderName;
		dropboxUniversalApi.uploadFile(childFolder, file.getAbsolutePath());
		protectFunctions.waitForMinutes(5);
		String policyViolationLog = protectFunctions.getPolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		if (policyViolationLog != null) {
			Assert.assertTrue(false, "Even after policy is deleted could see policy violation log");
		}
			
	}
	
	private String getExposureTypeForPolicy(String policyName, String policyType) throws Exception, JsonProcessingException, IOException {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("policy_name", policyName);
		additionalParams.put("policy_type", policyType);
		String responseBody = protectFunctions.getPolicyInfoByName(restClient, policyName, requestHeader, suiteData, additionalParams);
		String policyDetails = ClientUtil.getJSONValue(responseBody, "policy_details");
		String exposure = ClientUtil.getJSONValue(policyDetails, "exposure_scope");
		JSONArray jArray = (JSONArray) new JSONTokener(exposure).nextValue();
		String exposureType = (String)jArray.get(0);
		return exposureType;
	}
	
}
