package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonProcessingException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;

/**
 * @author Shri
 *
 */
public class ProtectSanityTests extends ProtectInitializeTests {

	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();
	Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	Map<String, String> gDrivefolderCollection = new HashMap<String, String>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	HashSet<String> failureSet = new HashSet<String>();
	DCIFunctions dciFunctions = new DCIFunctions();
	PolicyBean policyBean; String adminNumber;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Create and Activate " + policyBean.getCloudService() + " Policy: " + policyBean.getPolicyName(), true);
				protectFunctions.logTestDescription(policyBean);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				fileCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());
				try {
					Reporter.log("***************File(" + policyBean.getFileName() + ") upload in SaaS app:" + policyBean.getCloudService()
							+ " starts**************", true);

					uploadFileInSaaSApp(policyBean, policyName, file);
				} catch (Exception e) {
					Reporter.log("Failure in File Upload in SaaS App: " + policyBean.getCloudService(), true);
					e.printStackTrace();
					failureSet.add(policyBean.getPolicyName());
					continue;
				}

				try {
					Reporter.log(
							"***************Policy(" + policyBean.getPolicyName() + ") Creation/ Activation in SaaS app:"
									+ policyBean.getCloudService() + " starts**************", true);

					createPolicy(policyBean, policyName);
				} catch (Exception e) {
					Reporter.log("Failure in Policy creation and activation: " + policyBean.getPolicyName(), true);
					e.printStackTrace();
					failureSet.add(policyBean.getPolicyName());
					continue;
				}

				Reporter.log(
						"**********************" + "Created and Activated " + policyBean.getCloudService() + " Policy: "
								+ policyBean.getPolicyName() + " **********************", true);
				Reporter.log("*****************************************************************************************", true);
			} catch (Exception e) {
				continue;
			}
		}
		Thread.sleep(1000 * 60 * 25);
	}

	/**
	 * This test has to be modified based on throttle logs verification
	 */
	@Test(dependsOnMethods={"testPolicyViolationAndRemediationLogsForGDriveDataExposure"})
	public void testPolicyEmailTrigger() {
		String email = "admin@protectbeatle.com";
		Reporter.log("*****************************************************************************************", true);
		Logger.info("This test looks if the emailing service is up and and running by checking the logs in Splunk");
		Reporter.log("*****************************************************************************************", true);
		Reporter.log("Looking for any email triggered to "+email+" in past 30 mins", true);
		Reporter.log("*****************************************************************************************", true);		
		SplunkQueryResult splunkResult = SplunkQueries.lookForEmailInEmailLogs(email, suiteData.getEnvironmentName(), "-30m");
		Reporter.log("*****************************************************************************************", true);
		Reporter.log("Counting number of emails sent to "+email+" in past 24 hrs", true);
		Reporter.log("*****************************************************************************************", true);
		SplunkQueryResult splunkResult24hrs = SplunkQueries.lookForEmailInEmailLogs(email, suiteData.getEnvironmentName(), "-1440m");		
		Reporter.log("*****************************************************************************************", true);
		
		if (splunkResult.getEventsCount() >= 1){
			Logger.info("Email Event logs are found in splunk. its working fine.");
			Logger.info("Email Event count for the past 24 hrs:"+splunkResult24hrs.getEventsCount());
		} else if (splunkResult.getEventsCount() == 0){
			Logger.info("Email Event Logs are not found in last 25 mins. Hence, verifying email event count for the past 24 hrs to check if user is throttled");
			Logger.info("*******************************************");
			Logger.info("Email Event count for the past 24 hrs:"+splunkResult24hrs.getEventsCount());
			
			if (splunkResult24hrs.getEventsCount() >= 50) {
				Logger.info("Max Email Count 50 is reached and so email is not triggered.");
				return;
			} else {
				Assert.assertTrue(false, "Email Count is less then 50 and not delivered, hence failing the test");
			}
			
/*			Logger.info("*******************************************");
			Logger.info("Verifying whether user is throttled");
			Logger.info("*******************************************");
			SplunkQueryResult throttleResult = SplunkQueries.checkUserIsThrottled(suiteData.getEnvironmentName(), "protectauto@protectbeatle.com", "-1440m");
			if (throttleResult.getEventsCount() > 0)
				Logger.info("User is throttled and so email is not been sent.");
		    else 
				Assert.assertNotEquals(throttleResult.getEventsCount(), 0, "None of the email event logs found in splunk in last 25minutes. Also, don't see user is throttled as no throttle logs are found in splunk for the past 24 hrs");*/
			
/*			if (splunkResult.getEventsCount() == 50) {
				Logger.info("Email Service event logs are found in splunk for the past 24 hrs and count matches 50 and the user is throttled. Email works fine.");
			} else {
				Assert.assertEquals(splunkResult.getEventsCount(), 50, "Email Service event logs count for the past 24 hrs hasn't occured 50 times");
			}*/
		}
		
	}
	
	/**
	 * This test has to be modified based on throttle logs verification
	 */
	@Test(dependsOnMethods={"testPolicyViolationAndRemediationLogsForGDriveDataExposure"})
	public void testPolicySmsTrigger() {
		Reporter.log("*****************************************************************************************", true);
		Logger.info("This test looks if the sms service is up and and running by checking the logs in Splunk");
		Reporter.log("*****************************************************************************************", true);
		setAdminNumber();
		Reporter.log("Looking for any sms triggered to "+adminNumber+" in past 45 mins", true);
		Reporter.log("*****************************************************************************************", true);		
		SplunkQueryResult splunkResult = SplunkQueries.lookForSMSSentConfirmationForPolicyViolation(
				suiteData.getEnvironmentName(), "GDrive_P1", adminNumber, "-45m");
		Reporter.log("*****************************************************************************************", true);
		Reporter.log("Counting number of sms sent to "+adminNumber+" in past 24 hrs", true);
		Reporter.log("*****************************************************************************************", true);
		SplunkQueryResult splunkResult24hrs = SplunkQueries.lookForSMSSentConfirmationForPolicyViolation(
				suiteData.getEnvironmentName(), "GDrive_P1", adminNumber, "-1440m");		
		Reporter.log("*****************************************************************************************", true);
		
		if (splunkResult.getEventsCount() >= 1){
			Logger.info("Sms Service logs are found in splunk. its working fine.");
			Logger.info("Sms event count found in past 24 hrs:"+splunkResult24hrs.getEventsCount());
			
		} else if (splunkResult.getEventsCount() == 0){
			Logger.info("SMS Event Logs are not found in last 25 mins. Hence, verifying sms event count for the past 24 hrs to check if user is throttled");
/*			splunkResult = SplunkQueries.lookForSMSSentConfirmationForPolicyViolation(
					suiteData.getEnvironmentName(), "Dropbox_P1", adminNumber, "-1440m");*/
			Logger.info("*******************************************");
			Logger.info("sms event count for the past 24 hrs:"+splunkResult24hrs.getEventsCount());
			if (splunkResult24hrs.getEventsCount() >= 8) {
				Logger.info("Max SMS Count 10 is reached and so SMS is not triggered.");
				return;
			} else {
				Assert.assertTrue(false, "SMS Count is less then 10 and not delivered, hence failing the test");
			}
			
/*			Logger.info("*******************************************");
			Logger.info("Verifying whether user is throttled");
			Logger.info("*******************************************");
			
			SplunkQueryResult throttleResult = SplunkQueries.checkUserIsThrottled(suiteData.getEnvironmentName(), "admin@protectbeatle.com", "-1440m");
			if (throttleResult.getEventsCount() > 0)
				Logger.info("User is throttled and so sms is not been sent.");
			else 
				Assert.assertNotEquals(throttleResult.getEventsCount(), 0, "None of the sms event logs found in splunk in last 25minutes. Also, don't see user is throttled as no throttle logs are found in splunk for the past 24 hrs");*/
			
/*			if (splunkResult.getEventsCount() == 10) {
				Logger.info("Sms Service event logs are found in splunk for the past 24 hrs and count matches 10 and the user is throttled. SMS works fine.");
			} else {
				Assert.assertEquals(splunkResult.getEventsCount(), 10, "Sms Service event logs count for the past 24 hrs hasn't occured 10 times");
			}*/
		}
		
		Reporter.log("*****************************************************************************************", true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
/*	@Test(priority = 4)
	public void testPolicyViolationAndRemediationLogsForBoxDataExposure() throws Exception {
		String[] data = new String[] { "Box_P1", "Policy Desc", "DataExposure", "Box", "public", "any", "any", "any", "no", "no", "any",
				"any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "company"};
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		suiteData.setSaasApp("Box");
		
		if (!failureSet.contains(policyBean.getPolicyName())) {
			verifyPolicyViolationAndRemediation(policyBean, suiteData);
		}

		else
			throw new SkipException("Skipping this test as Policy Creation/ File upload in SaaS app:" + policyBean.getCloudService()
					+ " failed");
	}*/

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(priority = 5)
	public void testPolicyViolationAndRemediationLogsForDropboxDataExposure() throws Exception {
		String[] data = new String[] { "Dropbox_P1", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no",
				"any", "any", "no", "no", "pdf", "no", "no", "0,0", "HIPAA", "no", "delete"};
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		suiteData.setSaasApp("Dropbox");
		if (!failureSet.contains(policyBean.getPolicyName()))
			verifyPolicyViolationAndRemediation(policyBean, suiteData);
		else
			throw new SkipException("Skipping this test as Policy Creation/ File upload in SaaS app:" + policyBean.getCloudService()
					+ " failed");
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(priority = 6)
	public void testPolicyViolationAndRemediationLogsForGDriveDataExposure() throws Exception {
		String[] data = new String[] { "GDrive_P1", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no",
				"any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "commenter", null, null, "admin@protectbeatle.com"  };
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		suiteData.setSaasApp("GDrive");
		if (!failureSet.contains(policyBean.getPolicyName()))
			verifyPolicyViolationAndRemediation(policyBean, suiteData);
		else
			throw new SkipException("Skipping this test as Policy Creation/ File upload in SaaS app:" + policyBean.getCloudService()
					+ " failed");
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(priority = 7)
	public void testPolicyImpactTab() throws Exception {
		String[] data = new String[] { "GDrive_Impact_P1", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "extcollab" };

		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);

		if (!failureSet.contains(policyBean.getPolicyName()))
			verifyImpactDetails(policyBean);
		else
			throw new SkipException("Skipping this test as Policy Creation/ File upload in SaaS app:" + policyBean.getCloudService()
					+ " failed");
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(priority = 6)
	public void testProtect() throws Exception {
		try {

			String[] data = new String[] { "Protect_Box", "Policy Desc", "DataExposure", "Box", "public", "any", "any", "any", "no", "no",
					"any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "company" };
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
			suiteData.setSaasApp("Box");
			
			String policyName = policyBean.getPolicyName();
			File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
					policyBean.getFileExt());
			uploadFileInSaaSApp(policyBean, policyName, file);
			policyBean.setFileName(file.getName());
			fileCollection.put(policyName, file.getName());
			createPolicy(policyBean, policyName);
			protectFunctions.waitForMinutes(4.0);
			verifyPolicyViolationAndRemediation(policyBean, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 6)
	public void deletePolicies(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("***********************************************************", true);
				protectFunctions.logTestDescription(policyBean);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
				Reporter.log("***********************************************************", true);
			} catch (Exception e) {
				continue;
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void deleteFiles() throws Exception {
		String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
		File directory = new File(directoryName);
		System.out.println(directory.exists());
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}
	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		return new Object[][] {
/*				new String[] { "Box_P1", "Policy Desc", "DataExposure", "Box", "public", "any", "any", "any", "no", "no", "any", "any",
						"no", "no", "txt", "no", "txt", "0,0", "PII", "no", "company"},*/
				new String[] { "Dropbox_P1", "Policy Desc", "DataExposure", "Dropbox", "public", "any", "any", "any", "no", "no", "any",
						"any", "no", "no", "pdf", "no", "no", "0,0", "HIPAA", "no", "delete" },
				new String[] { "GDrive_P1", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any",
						"any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "commenter", null, null, "admin@protectbeatle.com" },				

		};
	}

	/**
	 * @param policyBean
	 * @throws Exception
	 */
	private void verifyPolicyViolationAndRemediation(PolicyBean policyBean, TestSuiteDTO suiteData) throws Exception {
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Logger.info("*****************************************************************************************");
		Logger.info("After Policy Creation and File Upload(in SaaS app), policy violation and remediation logs are verified after a wait time of 20 mins");
		Logger.info("*****************************************************************************************");
		Logger.info("Fetching Policy Violation Logs based on the File Name and Policy Name after wait time");
		Logger.info("*****************************************************************************************");		
		
		// Check for share and CI activity log
		Map<String, String> sharePolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient,
				fileName, policyName, requestHeader, suiteData);

		Map<String, String> ciPolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationCIAlertLogMessage(restClient,
				fileName, policyName, requestHeader, suiteData);

		Map<String, String> shareDisplayLogsMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient,
				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);

		Map<String, String> ciDisplayLogsMessage = protectFunctions.getSecurletCIPolicyViolationLogs(restClient,
				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);

		String shareLog = sharePolicyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
		String ciLog = ciPolicyViolationLogsMessage.get(ProtectTestConstants.MESSAGE);
		String shareDisplayLog = shareDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);
		String ciDisplayLog = ciDisplayLogsMessage.get(ProtectTestConstants.MESSAGE);

		// if both share and ci logs are null, skip verification and fail the
		// tests
		if (shareLog == null && ciLog == null) {
			Logger.info("Test case failed because Policy Violation Logs are not triggered for the activity performed after a wait time of 10 minutes");
			protectFunctions.checkSplunkLogs(suiteData, fileName, false);
			Assert.assertTrue(false, "Test case failed because Policy Violation Logs are not triggered for the activity performed after a wait time of 10 minutes");
			return;
		}

		// if share log is not null or if share log and ci log are not null,
		// then verify only share logs
		if (((shareLog != null && shareDisplayLog != null) && ciLog == null)
				|| ((shareLog != null && shareDisplayLog != null) && (ciLog != null && ciDisplayLog != null))) {
			if (!policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)) {

				Reporter.log("Verification of Policy Violation and Remediation Log for Policy: " + policyName + " and File: " + fileName);
				Reporter.log("Get Policy Alert logs", true);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Policy Alert Logs Response:" + sharePolicyViolationLogsMessage, true);
				Reporter.log(
						"#############################Asserting Policy Alerts from Protect#####################################################################",
						true);
				protectFunctions.assertPolicyViolation(sharePolicyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);

				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Investigate Logs Response:" + shareDisplayLogsMessage, true);
				Reporter.log(
						"#############################Asserting Policy Violation Log From Investigate Page#####################################################",
						true);
				protectFunctions.assertPolicyViolation(shareDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);

			}
		}

		// is share log is null, verify ci log
		else if ((ciLog != null && shareLog == null)) {
			if (!policyBean.getRiskType().equalsIgnoreCase("no")) {
				Reporter.log("Verification of Content Inspection Policy Violation and Remediation Log for Policy: " + policyName
						+ " and File: " + fileName);
				Reporter.log("Get Policy Alert logs", true);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Policy Alert Logs Response:" + ciPolicyViolationLogsMessage, true);
				Reporter.log(
						"#############################Asserting Content Inspection  Policy Alerts from Protect#####################################################################",
						true);
				protectFunctions.assertCIPolicyViolation(ciPolicyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);

				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Investigate Logs Response:" + ciDisplayLogsMessage, true);
				Reporter.log(
						"#############################Asserting Content Inspection Policy Violation Log From Investigate Page#####################################################",
						true);
				protectFunctions.assertCIPolicyViolation(ciDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);

			}
		}

		Reporter.log(
				"#############################Asserting Remediation Log From Investigate Page##########################################################",
				true);

		if (!policyBean.getRemediationActivity().equalsIgnoreCase(ProtectTestConstants.NO))
			verifyRemediation(policyBean, policyName, fileName);

		Logger.info("*****************************************************************************************");
		Logger.info("Policy Violations/ Remediations are verified as above for - " + policyName);
		Logger.info("*****************************************************************************************");		
	}

	/**
	 * @param policyBean
	 * @param policyName
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void createPolicy(PolicyBean policyBean, String policyName) throws Exception, JsonProcessingException, IOException {
		if (protectFunctions.isPolicyExists(restClient, policyName, requestHeader, suiteData)) {
			protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
		}
		protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
		Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
	}

	/**
	 * @param policyBean
	 * @param policyName
	 * @param file
	 * @throws Exception
	 * @throws IOException
	 */
	private void uploadFileInSaaSApp(PolicyBean policyBean, String policyName, File file) throws Exception, IOException {
		String cloudService = policyBean.getCloudService();
		Reporter.log("Login to SaaS app: " + cloudService);
		Reporter.log("********************************************", true);
		UniversalApi universalApi = protectFunctions.loginToApp(suiteData, policyBean);
		Reporter.log("********************************************", true);
		Reporter.log("Uploading and sharing file to " + cloudService);
		Reporter.log("********************************************", true);
		if (cloudService.equalsIgnoreCase("Dropbox")) {
			FileUploadResponse fileUploadResponse = universalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
			Reporter.log("*****************File Upload Response***************************", true);
			Reporter.log("File Id printing:" + fileUploadResponse.getFileId(), true);
			Reporter.log("File Name printing:" + fileUploadResponse.getFileName(), true);
			Reporter.log("Response Code:" + fileUploadResponse.getResponseCode(), true);
			Reporter.log("Response Message:" + fileUploadResponse.getResponseMessage(), true);
			Reporter.log("****************************************************************", true);
			if (fileUploadResponse.getFileId() == null) {
				Reporter.log("File not uploaded, again uploading a file");
				String filename = file.getName().substring(0, file.getName().indexOf("."));
				file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename),
						policyBean.getFileExt());
				universalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
				policyBean.setFileName(file.getName());
			}
			Reporter.log("File Uploaded to " + cloudService + " - " + file.getName(), true);
			if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
				Reporter.log("Creating a public link for the file - " + file.getName(), true);
				String createSharedLink = universalApi.createSharedLinkForFolder("/FileTransfer" + File.separator + file.getName());
				System.out.println("Create shared Link: " + createSharedLink);
			} else if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)
					|| policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
				// TODO - Not yet implemented in universal API
			}
		} else if (cloudService.equalsIgnoreCase(ProtectTestConstants.BOX)) {

			if (!policyBean.getRemediationActivity().equalsIgnoreCase("extcollab")) {
				String uniqueId = UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
				BoxFolder folder = universalApi.createFolder("A_" + uniqueId);
				folderCollection.put(policyBean.getPolicyName(), folder);
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
				Reporter.log("*****************File Upload Response***************************", true);
				Reporter.log("File Id printing:" + fileUploadResponse.getFileId(), true);
				Reporter.log("File Name printing:" + fileUploadResponse.getFileName(), true);
				Reporter.log("Response Code:" + fileUploadResponse.getResponseCode(), true);
				Reporter.log("Response Message:" + fileUploadResponse.getResponseMessage(), true);
				Reporter.log("****************************************************************", true);

				Reporter.log("File Uploaded to " + cloudService + " - " + file.getName(), true);
				FileEntry fileEntry = protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folder,
						policyBean.getExposureType());
				fileEntryCollection.put(policyName, fileEntry);
			} else {
				suiteData.setSaasApp(ProtectTestConstants.BOX);
				// protectFunctions.waitForMinutes(0.5);
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				BoxFolder folder = universalApi.createFolder(folderName);
				folderCollection.put(policyName, folder);
				universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				CollaborationInput collabInput = new CollaborationInput();
				Item item = new Item();
				item.setId(folder.getId());
				item.setType(ProtectTestConstants.FOLDER);
				String uniqueName = UUID.randomUUID().toString();
				AccessibleBy aby = new AccessibleBy();
				aby.setName(uniqueName);
				aby.setType(ProtectTestConstants.USER);
				aby.setLogin(ProtectTestConstants.EXT_USER);
				collabInput.setItem(item);
				collabInput.setAccessibleBy(aby);
				collabInput.setRole(ProtectTestConstants.EDITOR);
				universalApi.createCollaboration(collabInput);

			}
		} else if (cloudService.equalsIgnoreCase(ProtectTestConstants.GOOGLE_DRIVE)) {
			FileUploadResponse fileUploadResponse = null;
			String folderId = null;
			try {
				protectFunctions.waitForMinutes(1);
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Google Drive : " + folderName, true);
				folderId = universalApi.createFolder(folderName);
				gDrivefolderCollection.put(policyName, folderId);
				fileCollection.put(policyName, file.getName());
				fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				Reporter.log("*****************File Upload Response***************************", true);
				Reporter.log("File Id printing:" + fileUploadResponse.getFileId(), true);
				Reporter.log("File Name printing:" + fileUploadResponse.getFileName(), true);
				Reporter.log("Response Code:" + fileUploadResponse.getResponseCode(), true);
				Reporter.log("Response Message:" + fileUploadResponse.getResponseMessage(), true);
				Reporter.log("****************************************************************", true);
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
			} catch (Exception e) {
				Reporter.log("File not uploaded", true);
				fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
			}
			GDrive gDrive = universalApi.getgDrive();
			if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC) && policyBean.getPolicyType().equalsIgnoreCase(ProtectTestConstants.READER)) {
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, ProtectTestConstants.ANYONE, ProtectTestConstants.READER);
			}
			if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC) && policyBean.getPolicyType().equalsIgnoreCase(ProtectTestConstants.WRITER)) {
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, ProtectTestConstants.ANYONE, ProtectTestConstants.WRITER);
			}
			if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL))
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, policyBean.getPolicyType());
				
		}
		Reporter.log("********************************************", true);
	}

	/**
	 * @param policyBean
	 * @param policyName
	 * @param fileName
	 * @throws Exception
	 */
	private void verifyRemediation(PolicyBean policyBean, String policyName, String fileName) throws Exception {
		UniversalApi universalApi = protectFunctions.loginToApp(suiteData, policyBean);

		String cloudService = policyBean.getCloudService();
		String remediationActivity = policyBean.getRemediationActivity();
		if (cloudService.equalsIgnoreCase(ProtectTestConstants.DROPBOX)) {
			suiteData.setSaasApp(ProtectTestConstants.DROPBOX);
			if (remediationActivity.contains(ProtectTestConstants.DELETE)) {
				Reporter.log("Verifing the delete file remediation logs", true);
				String trashMessage = "User trashed " + fileName;
				Map<String, String> trashLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, trashMessage);

				if (trashLogMessage.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false, "Remediation Logs is not found in the portal, hence test case failed");
				} else {

					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + trashLogMessage, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(trashLogMessage, ProtectTestConstants.WARNING, cloudService, fileName,
							ProtectTestConstants.TRASH, trashMessage);
					Reporter.log("Verified the file trash activity parameters - " + trashLogMessage, true);
					Reporter.log("Verifing the file trash activity on SaaS App", true);
					Reporter.log("*****************************************************************************************", true);
					Map foldersItems = universalApi.getFoldersItems(File.separator + "FileTransfer", 0, 0);
					boolean containsKey = foldersItems.containsKey(fileName);
					Reporter.log("Is file available in dropbox after remediation: " + containsKey, true);
					Reporter.log("*****************************************************************************************", true);
					Assert.assertTrue(!containsKey);
				}
			}
		} else if (cloudService.equalsIgnoreCase(ProtectTestConstants.BOX)) {
			suiteData.setSaasApp(ProtectTestConstants.BOX);
			if (remediationActivity.contains(ProtectTestConstants.COMPANY) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
				String sharePermissionMessage = "User updated share permissions on " + fileName;
				Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, sharePermissionMessage);

				if (remediationLog.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {

					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + remediationLog, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.SHARE, sharePermissionMessage);
					Reporter.log("*****************************************************************************************", true);					
/*					Reporter.log("Verifing the change file permission remediation on SaaS App", true);
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Getting file information - " + fileCollection.get(policyName), true);
					FileEntry sharedFile = universalApi.getFileInfo(fileEntryCollection.get(policyName).getId());
					if (sharedFile != null) {
						Reporter.log("File Access Actual: " + sharedFile.getSharedLink().getAccess(), true);
						Reporter.log("File Access Expected: " + policyBean.getRemediationActivity(), true);
						Assert.assertEquals(sharedFile.getSharedLink().getAccess(), policyBean.getRemediationActivity());
					} else {
						Reporter.log(
								"***********File: " + policyBean.getFileName() + "  not found in SaaS app: " + policyBean.getCloudService()
										+ " ***********", true);
						throw new SkipException("Skipping this test as file not found in SaaS app:" + policyBean.getCloudService()
								+ " File Name: " + policyBean.getFileName());
					}*/
					Reporter.log("*****************************************************************************************", true);
				}
			}
			if (remediationActivity.contains(ProtectTestConstants.OPEN) && policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL)) {
				String sharePermissionMessage = "User updated share permissions on " + folderCollection.get(policyName).getName();
				Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, cloudService,
						folderCollection.get(policyName).getName(), requestHeader, suiteData, sharePermissionMessage);
				if (remediationLog.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + remediationLog, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, cloudService, folderCollection
							.get(policyName).getName(), ProtectTestConstants.SHARE, sharePermissionMessage);
				}
			}

			if (remediationActivity.contains(ProtectTestConstants.EXPIRES) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
				String expirationMessage = "User set share expiration on " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, expirationMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.EXPIRE_SHARING, expirationMessage);
				}
			}

			if (remediationActivity.contains(ProtectTestConstants.REMOVE_COLLAB)) {
				String removeCollaboratorMessage = "User removed a collaborator";
				Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, cloudService,
						folderCollection.get(policyName).getName(), requestHeader, suiteData, removeCollaboratorMessage);

				if (remediationLog.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {

					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + remediationLog, true);
					Reporter.log("*****************************************************************************************", true);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.MESSAGE), removeCollaboratorMessage);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.FACILITY), cloudService);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
					Assert.assertEquals(remediationLog.get(ProtectTestConstants.OBJECT), folderCollection.get(policyName).getName());
				}
			}

			if (remediationActivity.contains(ProtectTestConstants.UNSHARE1) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
				String unshareMessage = "User unshared " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, unshareMessage);
				Reporter.log("*****************************************************************************************", true);
				Reporter.log("Informational Logs:" + messagesLogs, true);
				Reporter.log("*****************************************************************************************", true);
				protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
						ProtectTestConstants.UNSHARE, "User unshared " + fileName);
			}
		} else if (cloudService.equalsIgnoreCase(ProtectTestConstants.GOOGLE_DRIVE)) {
			String fileId = fileIdCollection.get(policyName);
			String policyType = policyBean.getPolicyType();
			suiteData.setSaasApp(ProtectTestConstants.GDRIVE);
			GDrive gDrive = universalApi.getgDrive();
			if (policyType.contains("writer") && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC) && remediationActivity.contains(ProtectTestConstants.READER)) {
				String roleChangeMessage = "User changed permission on file " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, roleChangeMessage);

				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
				}
			}
			if (policyType.contains(ProtectTestConstants.WRITER) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)
					&& remediationActivity.contains(ProtectTestConstants.COMMENTER)) {
				String roleChangeMessage = "User changed permission on file " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, roleChangeMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
				}
			}
			if (policyType.contains(ProtectTestConstants.READER) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC) && remediationActivity.contains(ProtectTestConstants.WRITER)) {
				String roleChangeMessage = "User changed permission on file " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, roleChangeMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
				}
			}
			if (policyType.contains(ProtectTestConstants.READER) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)
					&& remediationActivity.contains(ProtectTestConstants.COMMENTER)) {
				String roleChangeMessage = "User changed permission on file " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, roleChangeMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
					Reporter.log("Verifing the file change access permission remediation on SaaS App", true);
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Getting file information - " + fileCollection.get(policyName), true);
					Reporter.log(gDrive.retrievePermissionList(fileId).getItems().toString(), true);
					Reporter.log("Actual File Permission: "
							+ gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), true);
					Reporter.log("Expected File Permission: " + policyBean.getRemediationActivity(), true);
					Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0),
							policyBean.getRemediationActivity());
					Reporter.log("*****************************************************************************************", true);
				}
			}

			if (remediationActivity.contains(ProtectTestConstants.REMOVE_COLLAB)) {
				String unshareMessage = "User unshared " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, "unshared",
						requestHeader, suiteData, unshareMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.UNSHARE, unshareMessage);
				}
			}

			if (remediationActivity.contains(ProtectTestConstants.UNSHARE1) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
				String unshareMessage = "User unshared " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, "unshared",
						requestHeader, suiteData, unshareMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.UNSHARE, unshareMessage);
				}
			}

			if (remediationActivity.equalsIgnoreCase(ProtectTestConstants.UPDATE_COMMENTER)) {
				String updatePermissionMessage = "User changed permission on file " + fileName;
				Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, cloudService, fileName,
						requestHeader, suiteData, updatePermissionMessage);
				if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
					Logger.info("Remediation Logs is not found in the portal, hence test case failed");
					protectFunctions.checkSplunkLogs(suiteData, fileName, false);
					Assert.assertTrue(false);
				} else {
					Reporter.log("*****************************************************************************************", true);
					Reporter.log("Informational Logs:" + messagesLogs, true);
					Reporter.log("*****************************************************************************************", true);
					protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, cloudService, fileName,
							ProtectTestConstants.ROLE_CHANGE, updatePermissionMessage);
				}
			}

		} else if (cloudService.equalsIgnoreCase(ProtectTestConstants.OFFICE_365)) {

		}
	}

	/**
	 * This function verifies Impact Details from Policies
	 * 
	 * @param policyBean
	 * @throws Exception
	 */
	private void verifyImpactDetails(PolicyBean policyBean) throws Exception {
		// Impact Analysis api call
		String fileName = fileCollection.get(policyBean.getPolicyName());
		policyBean.setFileName(fileName);
		HttpResponse response = protectFunctions.calculateImpact(restClient, requestHeader, suiteData, policyBean);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Impact Analysis Response......:" + responseBody);
		String identification = protectFunctions.verifyImpactDetails(policyBean, fileName, responseBody);
		// Validate External Collaborator
		if (policyBean.getRemediationActivity().equalsIgnoreCase("extcollab") && identification != null) {
			// Get Collaborator details
			suiteData.setSaasApp(policyBean.getCloudService());
			HttpResponse collabResponse = protectFunctions.getCollabDetailsForImpact(restClient, requestHeader, suiteData, policyBean,
					identification);

			// Asserting Impact Details pop up
			String collabResponseBody = ClientUtil.getResponseBody(collabResponse);
			protectFunctions.assertCollaboratorDetails(policyBean, collabResponseBody);
		}

	}
	
	/**
	 * 
	 */
	private void setAdminNumber() {
		if (suiteData.getEnvironmentName().equalsIgnoreCase(ProtectTestConstants.ENV_CEP) || suiteData.getEnvironmentName().equalsIgnoreCase(ProtectTestConstants.ENV_EOE) )
			adminNumber = ProtectTestConstants.PHONE_CEP;
		else if (suiteData.getEnvironmentName().equalsIgnoreCase(ProtectTestConstants.ENV_PROD))
			adminNumber = ProtectTestConstants.PHONE_PROD;
	}	

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(getData());
		return new Object[][] { { list } };
	}

	/**
	 * Adds Appliance Id for DLP
	 * 
	 * @throws JsonProcessingException
	 * @throws IOException
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	/*
	 * private void addAppliance() throws JsonProcessingException, IOException,
	 * Exception, UnsupportedEncodingException { String applianceList =
	 * ClientUtil.getJSONValue(dciFunctions.getApplianceList(suiteData,
	 * restClient), "appliances");
	 * Logger.info("Appliances List before creation...."+applianceList); if
	 * (applianceList.equals("[]")) { if
	 * (!suiteData.getEnvironmentName().equalsIgnoreCase("CEP")) applianceId =
	 * dciFunctions.addDLPAppliance(suiteData, restClient, "vontuDLPSanity"); }
	 * else { JSONArray appliancesArray = (JSONArray) new
	 * JSONTokener(applianceList).nextValue();
	 * Logger.info("Number of Appliances added: "+appliancesArray.size()); for
	 * (int i = 0; i < appliancesArray.size(); i++) { String name =
	 * ClientUtil.getJSONValue(appliancesArray.getJSONObject(i).toString(),
	 * "name"); if (name.substring(1, name.length() -
	 * 1).equals("vontuDLPSanity")) applianceId =
	 * ClientUtil.getJSONValue(appliancesArray.getJSONObject(i).toString(),
	 * "id"); } } }
	 */

}