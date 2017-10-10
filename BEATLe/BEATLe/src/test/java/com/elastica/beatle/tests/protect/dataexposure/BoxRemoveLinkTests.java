package com.elastica.beatle.tests.protect.dataexposure;

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
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;

/**
 * @author Mayur
 *
 */

public class BoxRemoveLinkTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	PolicyBean policyBean;
	BoxFolder folder;

	/**
	 * Test case Initialization
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		String uniqueId = UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
		folder = universalApi.createFolder("A_" + uniqueId);
		restClient = new Client();
	}

	/**
	 * This test method is to test data exposure policies(creation, activate) with various combinations for Box 
	 * and perform file upload in Box SaaS application
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				Reporter.log("Check File created: "+String.valueOf(file.exists()), true);
				fileCollection.put(policyBean.getPolicyName(), file.getName());
				policyBean.setFileName(file.getName());
				// Create a policy
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				protectFunctions.waitForMinutes(0.5);
				
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				FileEntry fileEntry = protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folder, policyBean.getExposureType());
				fileEntryCollection.put(policyName, fileEntry);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * This test method verify policies alert and remediation log population in
	 * elastica portal
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "RemoveLinkDataProvider", priority = 2)
	public void verifyDataExposurePolicyDisplayLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyDataExposurePolicyViolationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		if (policyBean.getRemediationActivity().contains("unshare") && policyBean.getExposureType().contains("public")) {
			String unshareMessage = "User unshared " + fileName;
			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, unshareMessage);
			protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.UNSHARE, "User unshared " + fileName);
			FileEntry sharedFile = universalApi.getFileInfo(fileEntryCollection.get(policyName).getId());
			Assert.assertNull(sharedFile.getSharedLink(), fileName+" is not unshared");
		}
		Reporter.log("Completed testcase: verifyDataExposurePolicyDisplayLogs - " + policyName, true);
	}

	/**
	 * This test method validates policy deletion action
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
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
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
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
		list.add(BoxDataProvider.getRemoveLinkDataProvider());
		return new Object[][] { { list } };  

	}
}