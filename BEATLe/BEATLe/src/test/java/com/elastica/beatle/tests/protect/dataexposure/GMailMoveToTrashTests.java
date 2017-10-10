package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GoogleMailServices;
import com.universal.common.UniversalApi;

public class GMailMoveToTrashTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	String folderId;
	GoogleMailServices googleMailServices;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> subjectCollection = new HashMap<String, String>();
	
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
	}
	
	/**
	 * 
	 * 1. Verifying the policy exists by Policy Name 
	 * 2. If policy already exists delete it
	 * 3. Create a policy by the same name and activate it 
	 * 4. Create a new File and upload to the SaaS App 
	 * 5. Share the file as public/external/internal(optional)
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {

		System.out.println("Input data size printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		String mailSubject = null;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions
						.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				
				googleMailServices =  protectFunctions.loginToGmail();
				mailSubject = ProtectTestConstants.MAIL_SUBJECT+file.getName();
				Reporter.log("Sending mail from: "+suiteData.getSaasAppUsername(),true);
				Reporter.log("Sending mail to: "+policyBean.getSharedWithUser(),true);
				Reporter.log("Sending mail subject: "+mailSubject,true);
				List<String> sendMailTo=new ArrayList<>();
				sendMailTo.add(policyBean.getSharedWithUser());
				googleMailServices.sendMessageWithAttachment(sendMailTo, null, null, mailSubject, "bodyText", file.getAbsolutePath(), file.getName());
				
				protectFunctions.waitForMinutes(1);
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				subjectCollection.put(policyName, mailSubject);
				fileCollection.put(policyName, file.getName());
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			} catch (Error e) {
				continue;
			} 
		}
		protectFunctions.waitForMinutes(20);
	}
	
	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposure", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		/*	String clientId = null;
		String clientSecret = null;
		String refreshToken = null;
		String recipient = null;

		if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			clientId = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppClientId");
			clientSecret = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppClientSecret");
			refreshToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppRefreshToken");
			recipient = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppUsername");
		}else if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)){
			clientId = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppClientId");
			clientSecret = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppClientSecret");
			refreshToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppRefreshToken");
			recipient = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppUsername");
		}*/
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		String trashMessage = "User trashed email with subject \\\"" + subjectCollection.get(policyName) + "\\\"";
		Map<String, String> remediationLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), fileName, requestHeader, suiteData, trashMessage);
		
		if (remediationLogs.get(ProtectTestConstants.MESSAGE) == null) {
			Logger.info("Remediation Logs is not found in the portal, hence test case failed");
			protectFunctions.checkSplunkLogs(suiteData, fileName, false);
			Assert.assertTrue(false);
			return;
		}
		
		System.out.println(remediationLogs);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.DOMAIN), suiteData.getDomainName());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.OBJECT_TYPE), ProtectTestConstants.EMAIL_MESSAGE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.TRASH);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.NAME), subjectCollection.get(policyName));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), trashMessage);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		//Assert.assertEquals(remediationLogs.get(ProtectTestConstants.USER), suiteData.getSaasAppUsername());
		//Assert.assertTrue(remediationLogs.get(ProtectTestConstants.EXTERNAL_RECIPIENTS).contains(recipient));
		//googleMailServices = protectFunctions.loginToGmail(clientId, clientSecret, refreshToken);
		//Message message = googleMailServices.getMessageWithSubject(subjectCollection.get(policyName), ProtectTestConstants.TRASH.toUpperCase());
		//String messageId = message.getId();
		//Assert.assertNull(messageId);
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testDeletePolicy(List<String[]> list) throws Exception {

		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		universalApi = protectFunctions.loginToApp(suiteData);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testDeletePolicy - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: testDeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			} 
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public void deleteFolders() throws Exception {

		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
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
		if (suiteData.getSaasApp().endsWith("Sanity"))
			list.add(GDriveDataProvider.getGmailMoveToTrashSanityData());
		else
			list.add(GDriveDataProvider.getGmailMoveToTrashData());
		return new Object[][] { { list } };  
	}	
	
	
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		if (suiteData.getSaasApp().endsWith("Sanity"))
			return GDriveDataProvider.getGmailMoveToTrashSanityData();
		else
			return GDriveDataProvider.getGmailMoveToTrashData();
	}		
	
}