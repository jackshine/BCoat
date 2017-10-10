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
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

public class GoogleDriveChangeAccessTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	GDrive gDrive;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	String filePath = "";
	String folderId = null;
	Map<String, String> folderCollection = new HashMap<String, String>();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		gDrive = universalApi.getgDrive();
	}


	/**
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
		System.out.println("Input data printing...." + list.size());
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
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				FileUploadResponse fileUploadResponse = null;
				
				try {
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
					fileCollection.put(policyName, file.getName());
					fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				} catch (Exception e) {
					Reporter.log("File not uploaded", true);
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
					fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				}
				protectFunctions.waitForMinutes(1);
				gDrive = universalApi.getgDrive();
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, "anyone", policyBean.getPolicyType());
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			} 
		}
		protectFunctions.waitForMinutes(15);
		universalApi = protectFunctions.loginToApp(suiteData);
		gDrive = universalApi.getgDrive();
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "ChangeAccessDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String...data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		String fileId = fileIdCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		Map<String, String> messagesLogs = null;
		String roleChangeMessage = "User changed permission on file " + fileName;
		messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				fileName, requestHeader, suiteData, roleChangeMessage);
		protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
				ProtectTestConstants.ROLE_CHANGE, roleChangeMessage);
		Assert.assertEquals(messagesLogs.get(ProtectTestConstants.ROLE), policyBean.getRemediationActivity());
		Assert.assertEquals(messagesLogs.get(ProtectTestConstants.PREVIOUS_ROLE), policyBean.getPolicyType());
		if(policyBean.getRemediationActivity().equals("commenter")){
			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0)
					, policyBean.getRemediationActivity());
		}else{
			Reporter.log("Gdrive SaasApp Remediation Items : "+gDrive.retrievePermissionList(fileId).getItems(), true);
			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole()
					, policyBean.getRemediationActivity());
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testdeletePolicy(List<String[]> list) throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testdeletePolicy - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: testdeletePolicy - " + policyName, true);
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
			File directory = new File(directoryName);
			System.out.println(directory.exists());
			if (directory.exists()) {
				FileUtils.deleteDirectory(directory);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}
	
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(GDriveDataProvider.getChangeAccessDataProvider());
		return new Object[][] { { list } };  
	}
}