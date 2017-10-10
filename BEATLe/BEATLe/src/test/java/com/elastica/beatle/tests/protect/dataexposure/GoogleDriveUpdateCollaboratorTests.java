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

public class GoogleDriveUpdateCollaboratorTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	GDrive gDrive;
	ProtectFunctions protectFunctions = new ProtectFunctions();
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
	}

	/**
	 * 1. Verifying the policy exists by Policy Name 
	 * 2. If policy already exists delete it 
	 * 3. Create a policy by the same name and activate it 
	 * 4. Create a  new File and upload to the SaaS App 
	 * 5. Share the file as public/external/internal(optional)
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testProtectDataExposurePolicyGoogleDriveRemoveCollaborator(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				String uniqueId = UUID.randomUUID().toString();
				Reporter.log("Starting testcase: testProtectDataExposurePolicyGoogleDriveRemoveCollaborator - " + policyName, true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				String folderId = universalApi.createFolder("A_" + uniqueId);
				folderCollection.put(policyName, folderId);
				fileCollection.put(policyName, file.getName());
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				protectFunctions.waitForMinutes(1);
				gDrive = universalApi.getgDrive();
				if (policyBean.getExposureType().equalsIgnoreCase("external") && policyBean.getPolicyType().equalsIgnoreCase("reader")) {
					gDrive.insertPermission(gDrive.getDriveService(), folderId, "mayurbelekar@gmail.com", "user", "reader");
				}
				if (policyBean.getExposureType().equalsIgnoreCase("external") && policyBean.getPolicyType().equalsIgnoreCase("writer")) {
					gDrive.insertPermission(gDrive.getDriveService(), folderId, "mayurbelekar@gmail.com", "user", "writer");
				}
				Reporter.log("Completed testcase: testProtectDataExposurePolicyGoogleDriveRemoveCollaborator - " + policyName, true);
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
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "UpdateCollaboratorDataProvider", priority = 2)
	public void testPolicyViolationAndDisplayLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		String fileId = fileIdCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndDisplayLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		String updatePermissionMessage = "User changed permission on file " + fileName;
		Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				fileName, requestHeader, suiteData, updatePermissionMessage);
		protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
				fileName, ProtectTestConstants.ROLE_CHANGE, updatePermissionMessage);
		if(policyBean.getRemediationActivity().equals("updateWriter")){
			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), "writer");
		}
		if(policyBean.getRemediationActivity().equals("updateReader")){
			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getRole(), "reader");
		}
		if(policyBean.getRemediationActivity().equals("updateCommenter")){
			Reporter.log("Gdrive SaasApp Remediation Role : "+gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), true);
			Assert.assertEquals(gDrive.retrievePermissionList(fileId).getItems().get(1).getAdditionalRoles().get(0), "commenter");
		}
		Reporter.log("Completed testcase: testPolicyViolationAndDisplayLogs - " + policyName, true);
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
				universalApi.deleteFolder(folderCollection.get(policyName), true, "");
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
		list.add(GDriveDataProvider.getUpdateCollaboratorDataProvider());
		return new Object[][] { { list } };  

	}
}
