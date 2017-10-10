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
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

public class GoogleDrivePreventDownloadPrintCopyTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	GDrive gDrive;
	String folderId;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();
	String externalUser = "mayurbelekar@gmail.com";
	
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		gDrive = universalApi.getgDrive();
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
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Google Drive : " + folderName, true);
		folderId = universalApi.createFolder(folderName);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				FileUploadResponse fileUploadResponse = null;
				File file = null;
				file = protectFunctions
						.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				protectFunctions.waitForMinutes(0.5);
				gDrive = universalApi.getgDrive();
				try{
					gDrive.printAbout();
				} catch(Exception e){
					System.out.println("Issue Found with GDrive Initialization.. Initializing Again...");
					universalApi = protectFunctions.loginToApp(suiteData);
					gDrive = universalApi.getgDrive();
				}
				fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				fileCollection.put(policyName, file.getName());
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, "anyone", policyBean.getPolicyType());
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), externalUser, "user", policyBean.getPolicyType());
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), suiteData.getDomainName(), "domain", "reader");
				}
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			} 
		}
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "PreventDownloadPrintCopyDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		String remediationMessage = "Prevent copy, print and download of " + fileName;
		Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				fileName, requestHeader, suiteData, remediationMessage);
		protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
				fileName, ProtectTestConstants.RESTRICT, remediationMessage);
		// TODO SaaS App Side remediation is pending 
		
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
		universalApi.deleteFolder(folderId, true, "");
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
		list.add(GDriveDataProvider.getPreventDownloadPrintCopyDataProvider());
		return new Object[][] { { list } };  

	}
}
