package com.elastica.beatle.tests.protect.policyviolations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectAlertFilter;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.FileUploadResponse;

/**
 * @author Shri
 *
 */

public class ProtectCustDataSeperationTests extends ProtectInitializeTests {

	UniversalApi universalApi; Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	BoxFolder folder; ProtectAlertFilter protectAlertFilter;
	TestSuiteDTO suiteDataB = new TestSuiteDTO();
	public List<NameValuePair> requestHeaderB;
	Map<String, String> policyNameMap = new HashMap<String, String>();
	Map<String, String> policyFileMap = new HashMap<String, String>();


	/**
	 * Test case Initialization
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		universalApi = protectFunctions.loginToSaaSApp(suiteData, ProtectTestConstants.BOX);
		setSuiteData2();
		setTenant2Data(suiteDataB);
		String uniqueId = UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
		folder = universalApi.createFolder("A_" + uniqueId);
		
	}



	/**
	 * This test method is to test data exposure policies(creation, activate)
	 * with various combinations for Box and perform file upload in Box SaaS
	 * application
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		
		Logger.info("1. Creates 3 different policies -> 2 in tenant:"+suiteData.getTenantName()+" and 1 in tenant: "+suiteData+ " to verify policy and violation alerts doesn't override between 2 tenants");
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();

		String fileName = null;
		File file = null;		
		PolicyBean policyBean = null;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				
				if (data[0].contains(ProtectTestConstants.ACCESS_MONITORING))
					policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
				else
					policyBean = protectFunctions.populatePolicyBean(data);
				
				String policyName = policyBean.getPolicyName();
				String newPolicyName = protectFunctions.generateAlphaNumericString(10);
				policyNameMap.put(policyName, newPolicyName);	
				
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				
				if (fileName == null){
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
					fileName = file.getName();
				}
				
				policyBean.setFileName(fileName);
				policyBean.setPolicyName(newPolicyName);
				policyFileMap.put(newPolicyName, fileName);

				// Create a policy
				if (policyName.contains(ProtectTestConstants.TENANT_A_POL)){
					protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				}
				else if (policyName.contains(ProtectTestConstants.TENANT_B_POL)){
					// set suitedata for tenant B and call create policy
					requestHeaderB = ProtectInitializeTests.buildCookieHeadersForUser2(suiteDataB);
					protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeaderB, suiteDataB);
				}
				else if (policyName.contains(ProtectTestConstants.ACCESS_MONITORING)){
					
					protectFunctions.createAndActivateAccessEnforcementPolicy(restClient, policyBean, requestHeader, suiteData);
					protectFunctions.waitForMinutes(0.5);
					protectFunctions.setAccessEnforcementBoxOperations(policyBean, universalApi, file, folder);
				}
					
				if (policyName.contains(ProtectTestConstants.TENANT_A_POL)){
					String childFolderName = "AA_" + UUID.randomUUID().toString();
					Reporter.log("Creating a folder in Box : " + childFolderName, true);
					BoxFolder childFolder = universalApi.createFolder(childFolderName, folder.getId());
					
					FileUploadResponse fileUploadResponse = universalApi.uploadFile(childFolder.getId(), file.getAbsolutePath(), file.getName());
					Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
					protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folder, policyBean.getExposureType());
				}
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);

			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}

	
	@Test(priority = 2)
	public void verifyPolicyVisibilityInAnotherTenant() throws Exception{
		Logger.info("*********************************************************************************************");
		Logger.info("=============================================================================================");
		Logger.info("1. Verify policy created in Tenant: "+suiteData.getTenantName()+" is visible");
		Logger.info("=============================================================================================");
		boolean isPolicyExistinTenantA = protectFunctions.isPolicyExists(restClient, policyNameMap.get(ProtectTestConstants.TENANT_A_POL), requestHeader, suiteData);
		Assert.assertTrue(isPolicyExistinTenantA, "Policy doesn't exist in tenant:"+suiteData.getTenantName()+ "where it was created, hence failing the test");
		Logger.info("As expected, policy is visible in the tenant:"+suiteData.getTenantName()+"where it is created");
		Logger.info("*********************************************************************************************");		
		// Get the list of policies from tenant B and verify policy doesn't exist
		Logger.info("=============================================================================================");
		Logger.info("2. Verify policy created in tenant: "+suiteData.getTenantName()+" is not visible for another tenant: "+suiteDataB.getUser2TenantName());
		Logger.info("=============================================================================================");		
		boolean isPolicyExistinTenantB = protectFunctions.isPolicyExists(restClient, policyNameMap.get(ProtectTestConstants.TENANT_A_POL), requestHeaderB, suiteDataB);
		Assert.assertTrue(!isPolicyExistinTenantB, "Policy exist in a different tenant:"+suiteDataB.getUser2TenantName()+ "where it was not created, hence failing the test");
		Logger.info("As expected, policy created in tenant: "+suiteData.getTenantName()+ " is not visible in another tenant: "+suiteDataB.getTenantName());
		Logger.info("*********************************************************************************************");		
	}
	
	@Test(priority = 3)
	public void verifyPolicyViolationAlertInAnotherTenant() throws Exception{
		// verify policy is not visible in tenant B and visible in tenant A
		// Get the list of policies from tenant A and verify policy exist
		Logger.info("*********************************************************************************************");
		Logger.info("=============================================================================================");		
		Logger.info("1. Verify policy violation alert in Tenant: "+suiteData.getTenantName()+" is triggered");		
		Logger.info("=============================================================================================");		
		PolicyBean policyBean = new PolicyBean();
		// get value from filePolicy Map
		String fileName = policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL));
		policyBean.setFileName(fileName);
		boolean isPolicyAlertFound = checkIfPolicyViolationAlertTriggered(suiteData, requestHeader, fileName, policyNameMap.get(ProtectTestConstants.TENANT_A_POL));
		if (isPolicyAlertFound) Logger.info("Policy Alert found for policy: "+policyNameMap.get(ProtectTestConstants.TENANT_A_POL)+ " in tenant: "+suiteData.getTenantName());
		Assert.assertTrue(isPolicyAlertFound, "Policy Alert Not Found in Tenant: "+suiteData.getTenantName()+", hence failing the test");
		Logger.info("As expected, policy violation alert in Tenant: "+suiteData.getTenantName()+" is triggered for policy: "+policyNameMap.get(ProtectTestConstants.TENANT_A_POL));
		Logger.info("*********************************************************************************************");		
		Logger.info("=============================================================================================");
		Logger.info("2. Verify policy violation alert in tenant: "+suiteData.getTenantName()+" is not visible for another tenant: "+suiteDataB.getUser2TenantName());
		Logger.info("=============================================================================================");		
		isPolicyAlertFound = checkIfPolicyViolationAlertTriggered(suiteDataB, requestHeaderB, fileName, ProtectTestConstants.TENANT_A_POL);
		if (!isPolicyAlertFound) Logger.info("Policy Alert are not found for policy: "+policyNameMap.get(ProtectTestConstants.TENANT_A_POL)+ " in a different tenant: "+suiteDataB.getUser2TenantName());
		boolean isPolicyAlertFoundForSimilarPolicy = checkIfPolicyViolationAlertTriggered(suiteDataB, requestHeaderB, fileName, policyNameMap.get(ProtectTestConstants.TENANT_B_POL));
		if (!isPolicyAlertFound) Logger.info("Policy Alert are not found for similar type policy: "+policyNameMap.get(ProtectTestConstants.TENANT_B_POL)+ " similar to " +policyNameMap.get(ProtectTestConstants.TENANT_A_POL) +"in a different tenant: "+suiteDataB.getUser2TenantName());		
		Logger.info("isPolicyAlertFoundForSimilarPolicy:  "+isPolicyAlertFoundForSimilarPolicy );
		Assert.assertTrue(!(isPolicyAlertFound || isPolicyAlertFoundForSimilarPolicy), "Policy Alert found in Tenant: "+policyNameMap.get(ProtectTestConstants.TENANT_B_POL)+" for the policy created in Tenant: "+policyNameMap.get(ProtectTestConstants.TENANT_A_POL)+", hence failing the test");
		Logger.info("As expected, policy violation alert in Tenant: "+suiteData.getTenantName()+" is not triggered for tenant: "+suiteDataB.getUser2TenantName());
		Logger.info("*********************************************************************************************");		
	}
	


	@Test(priority = 4)
	public void verifyPolicyImpactInAnotherTenant() throws Exception{
		
		// Populate policy Bean
		String[] data = new String[] { ProtectTestConstants.TENANT_A_POL, "Policy Desc", "DataExposure", "Box", "public", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "PCI", "no", "no" };
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String fileName = policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL));
		policyBean.setFileName(fileName);
		policyBean.setPolicyName(policyNameMap.get(ProtectTestConstants.TENANT_A_POL));
		suiteData.setSaasApp(ProtectTestConstants.BOX);

		Logger.info("*********************************************************************************************");
		Logger.info("=============================================================================================");
		Logger.info("1.) Verify policy:"+policyNameMap.get(ProtectTestConstants.TENANT_A_POL)+ " created in tenant:"+suiteData.getTenantName()+ "has impacted file:"+policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL))+" displayed");
		Logger.info("=============================================================================================");		
		HttpResponse response = protectFunctions.calculateImpact(restClient, requestHeader, suiteData, policyBean);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Impact Analysis Response......:" + responseBody);
		int totalImpactedFilesCount = Integer.parseInt(ClientUtil.getJSONValue(responseBody, "total_files_count"));
		Assert.assertTrue(totalImpactedFilesCount>0,"Policy has no impacted files corresponding to the tenant, hence test case failed");
		Logger.info("As expected, could see file: "+policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL))+" impacted has been populated in the impact tab api response");
		Logger.info("*********************************************************************************************");
		Logger.info("=============================================================================================");
		Logger.info("2.) Verify similar policy:"+policyNameMap.get(ProtectTestConstants.TENANT_B_POL)+ " created in tenant: "+suiteDataB.getUser2TenantName()+ "has impacted file:"+policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL))+" displayed");
		Logger.info("=============================================================================================");		
		policyBean.setPolicyName(policyNameMap.get(ProtectTestConstants.TENANT_B_POL));
		response = protectFunctions.calculateImpact(restClient, requestHeaderB, suiteDataB, policyBean);
		responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Impact Analysis Response......:" + responseBody);
		totalImpactedFilesCount = Integer.parseInt(ClientUtil.getJSONValue(responseBody, "total_files_count"));
		Assert.assertTrue(totalImpactedFilesCount==0,"Policy should not have any impacted files corresponding to this tenant, hence test case failed");
		Logger.info("As expected, could see file: "+policyFileMap.get(policyNameMap.get(ProtectTestConstants.TENANT_A_POL))+" impacted has not been populated in the impact tab api response");
		Logger.info("*********************************************************************************************");
		
	}
	
	
	@Test(priority = 5)
	public void verifyAccessMonitoringPolicyImpactInAnotherTenant() throws Exception{
		
		// Populate policy Bean
		String[] data = new String[] { ProtectTestConstants.ACCESS_MONITORING, "Box", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "0", "File:Upload",            "txt"};
		PolicyBean policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
		String policyName = policyNameMap.get(data[0]);
		String fileName = policyFileMap.get(policyNameMap.get(policyName));
		policyBean.setFileName(fileName);
		policyBean.setPolicyName(policyName);
		Reporter.log("Starting testcase: verifyPolicyViolationLogs - " + policyName, true);
		Map<String, String> policyViolationLogsMessage = protectFunctions.getAccessMonitoringPolicyViolationAlertLogDetails(restClient,
				policyBean, requestHeader, suiteData);
		
		// Verify from the logs
		Logger.info("*********************************************************************************************");
		Logger.info("=============================================================================================");		
		Logger.info("1.) Verify Access Monitoring Policy"+ policyNameMap.get(policyName) + "triggered alerts in tenant:"+suiteData.getTenantName());
		Logger.info("=============================================================================================");		
		Assert.assertTrue(!policyViolationLogsMessage.isEmpty() && policyViolationLogsMessage.get(ProtectTestConstants.MESSAGE) != null, "Access Monitoring Policy Violation Alerts are not triggered in tenant: for the activity performed");
		Logger.info("As expected, Alert Triggered: "+policyViolationLogsMessage.get(ProtectTestConstants.MESSAGE));		
		Logger.info("*********************************************************************************************");		
		// Login to tenant B and verify logs from there
		Logger.info("=============================================================================================");
		Logger.info("2.) Verify Access Monitoring Policy"+ policyNameMap.get(policyName) + "doesn't trigger alerts in different tenant:"+suiteDataB.getUser2TenantName());
		Logger.info("=============================================================================================");		
		Map<String, String> policyViolationLogsMessageTenantB = protectFunctions.getAccessMonitoringPolicyViolationAlertLogDetails(restClient,
				policyBean, requestHeaderB, suiteDataB);
		Assert.assertTrue(policyViolationLogsMessageTenantB.isEmpty(), "Access Monitoring Policy created in tenant A has impact on tenant B as the violation alerts in tenant B for the action performed in tenant A are triggered");
		if (policyViolationLogsMessageTenantB.isEmpty())
			Logger.info("As expected, Alert not Triggered for Access Monitoring policy: "+policyNameMap.get(policyName)+ "for tenant:"+suiteDataB.getUser2TenantName());
		Logger.info("*********************************************************************************************");				
		
	}

	/**
	 * This test method validates policy deletion action
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 6)
	public void deletePolicies(List<String[]> list) throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: deletePolicies - " + policyName, true);

				if (!policyName.contains(ProtectTestConstants.TENANT_B_POL))
					protectFunctions.deactivateAndDeletePolicy(restClient, policyNameMap.get(policyName), requestHeader, suiteData);
				else
					protectFunctions.deactivateAndDeletePolicy(restClient, policyNameMap.get(policyName), requestHeaderB, suiteDataB);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		universalApi.deleteFolder(folder.getId(), true, folder.getEtag());
	}
	
	
	/**
	 * Tear down method to delete files created as part of this test in SaaS app
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public void deleteFolders() throws Exception {
		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
			System.out.println(directoryName);
			File directory = new File(directoryName);
			System.out.println(directory.exists());
			if (directory.exists()) {
				FileUtils.deleteDirectory(directory);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {

		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(BoxDataProvider.getCustomerSeperationInputData());
		return new Object[][] { { list } };

	}
	
	private boolean checkIfPolicyViolationAlertTriggered(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, String fileName, String policyName) throws Exception {
		boolean isPolicyAlertFound;
		Map<String, String> alertLogsPolViolMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
 				policyName, requestHeader, suiteData);
		Map<String, String> alertLogsPolViolCIMessage = protectFunctions.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
				policyName, requestHeader, suiteData);
 		Map<String, String> investigateLogsPolViolMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient,
 				suiteData.getSaasApp(), fileName, policyName, requestHeader, suiteData);
		Map<String, String> investigateLogsPolViolCIMessage = protectFunctions.getSecurletCIPolicyViolationLogs(restClient,
				suiteData.getSaasApp(), fileName, policyName, requestHeader, suiteData);
		
		
	    String alertPolViolLog = alertLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String alertPolViolCILog = alertLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);
	    String investigatePolViolLog = investigateLogsPolViolMessage.get(ProtectTestConstants.MESSAGE);
	    String investigatePolViolCILog = investigateLogsPolViolCIMessage.get(ProtectTestConstants.MESSAGE);

	    
	    Logger.info("Alerts triggered for policy name:"+policyName);
	    Logger.info("**************************************************");
	    if (alertPolViolLog != null)
	    	Logger.info(alertPolViolLog);
	    else if (alertPolViolCILog != null)
	    	Logger.info(alertPolViolCILog);
	    else if (investigatePolViolLog != null)
	    	Logger.info(investigatePolViolLog);
	    else if (investigatePolViolCILog != null)
	    	Logger.info(investigatePolViolCILog);	    
	    
	    Logger.info("**************************************************");	    
	    
	    if (alertPolViolLog !=null || alertPolViolCILog !=null || investigatePolViolLog !=null || investigatePolViolCILog !=null)
	    	isPolicyAlertFound = true;
	    else
	    	isPolicyAlertFound = false;
		
		return isPolicyAlertFound;
	}	
	
	private void setSuiteData2() {
		suiteDataB.setHost(suiteData.getHost());
		suiteDataB.setScheme(suiteData.getScheme());
		suiteDataB.setEnvironmentName(suiteData.getEnvironmentName());
		suiteDataB.setAPIMap(suiteData.getAPIMap());
		suiteDataB.setBaseVersion(suiteData.getBaseVersion());
		suiteDataB.setTenantName(suiteData.getUser2TenantName());
		suiteDataB.setApiserverHostName(suiteData.getApiserverHostName());
		suiteDataB.setSaasApp(ProtectTestConstants.BOX);
		suiteDataB.setUsername(suiteData.getUser2Name());
	}

	private void setTenant2Data(TestSuiteDTO suiteData) {
		suiteData.setUser2Name(ProtectInitializeTests.getRegressionSpecificSuitParameters("userName2"));
		suiteData.setUser2Password(ProtectInitializeTests.getRegressionSpecificSuitParameters("userPassword2"));
		suiteData.setUser2TenantName(ProtectInitializeTests.getRegressionSpecificSuitParameters("tenantName2"));
		suiteData.setUser2TenantToken(ProtectInitializeTests.getRegressionSpecificSuitParameters("tenantToken2"));
	}

}