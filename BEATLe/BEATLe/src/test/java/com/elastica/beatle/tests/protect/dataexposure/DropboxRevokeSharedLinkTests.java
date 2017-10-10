package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

/**
 * @author Shri
 *
 */
public class DropboxRevokeSharedLinkTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> sharedLinkMap = new HashMap<String, String>();
	PolicyBean policyBean;
	String applianceId;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
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
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				fileCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());
				FileUploadResponse fileUploadResponse = universalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
				if (fileUploadResponse.getFileId() == null) {
					Reporter.log("File not uploaded, again uploading a file");
					String filename = file.getName().substring(0, file.getName().indexOf("."));
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename),
							policyBean.getFileExt());
					universalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
					policyBean.setFileName(file.getName());
				}
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				if (policyBean.getExposureType().equalsIgnoreCase("public")) {
					Reporter.log("Creating a public link for the file - " + file.getName(), true);
					String createSharedLink = universalApi.createSharedLinkForFolder("/FileTransfer" + File.separator + file.getName());
					sharedLinkMap.put(policyName, createSharedLink);
					System.out.println("Create shared Link: " + createSharedLink);
				}
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		Thread.sleep(1000 * 60 * 10);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "RevokeSharedLink", priority = 2)
	public void verifyPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		Reporter.log("Verifing remediation log for the shared link of the file deleted", true);
		String removeLinkMessage = "Removed a link " + fileName;
		Map<String, String> removeLinkLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				fileName,  requestHeader, suiteData, removeLinkMessage);
		Reporter.log(policyName+" - "+removeLinkLogMessage, true);
		protectFunctions.assertRemediation(removeLinkLogMessage, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName, ProtectTestConstants.UNSHARE, removeLinkMessage);
		Reporter.log("Verified the removed share link activity parameters - " + removeLinkLogMessage, true);
		Reporter.log("Verifing the file trash activity on SaaS App", true);
	    HttpResponse sharedLinkRes = restClient.doGet(new URI(sharedLinkMap.get(policyName)), null);
	    Assert.assertTrue(ClientUtil.getResponseBody(sharedLinkRes).contains("We can't find what you're looking for"));
		Reporter.log("Completed testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void deletePolicies(List<String[]> list) throws Exception {
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
				Reporter.log("Deleted policy - " + policyName, true);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		universalApi.deleteFolder("/FileTransfer", false, null);
	}

	@AfterClass(alwaysRun = true)
	public void deleteFiles() throws Exception {
		String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
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
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(DropboxDataProvider.getRevokeSharedLinkDataProvider());
		return new Object[][] { { list } };  
	}
}