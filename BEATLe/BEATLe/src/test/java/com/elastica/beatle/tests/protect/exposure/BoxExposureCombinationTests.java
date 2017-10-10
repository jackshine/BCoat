package com.elastica.beatle.tests.protect.exposure;

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
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;

import junit.framework.Assert;

public class BoxExposureCombinationTests extends ProtectInitializeTests{

	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	UniversalApi universalApi = null;
	Client restClient;
	BoxFolder folder;
	String filePath = "";
	
	@BeforeClass
	public void initialize() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
	}
	
	@SuppressWarnings("null")
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Google Drive : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		while (iterator.hasNext()) {
			try{
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				FileUploadResponse fileUploadResponse = null;
				String chiledFolderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + chiledFolderName, true);
				BoxFolder childFolder = universalApi.createFolder(chiledFolderName, folder.getId());
				//universalApi.uploadFile(childFolder.getId(), file.getAbsolutePath(), file.getName());
				//Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				protectFunctions.waitForMinutes(0.5);
				FileEntry sharedFile = null;
				String[] collaborator = null;
				fileCollection.put(policyBean.getPolicyName(), file.getName());
				if(data[21].contains(ProtectTestConstants.PUBLIC)){
					collaborator = new String[0];
					protectFunctions.shareTheFolderPubliclyOrWithCollaborators(universalApi, childFolder.getId(), file.getAbsolutePath(), file.getName(), "open", collaborator);
					Reporter.log("File link shared as public", true);
				}
				if(data[21].contains(ProtectTestConstants.INTERNAL)){
					collaborator = new String[0];
					protectFunctions.shareTheFolderPubliclyOrWithCollaborators(universalApi, childFolder.getId(), file.getAbsolutePath(), file.getName(), "company", collaborator);
					Reporter.log("File link shared as internal", true);
				}
				if(data[21].contains(ProtectTestConstants.EXTERNAL)){
					collaborator = new String[]{"mayurbelekar@gmail.com"};
					protectFunctions.shareTheFolderPubliclyOrWithCollaborators(universalApi, childFolder.getId(), file.getAbsolutePath(), file.getName(), "collaborators", collaborator);
				}
				System.out.println(sharedFile.getSharedLink().getAccess());
				System.out.println(sharedFile.getSharedLink().getEffectiveAccess());
			}catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			} catch (Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "BoxExposureDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String...data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		if(data[22].equalsIgnoreCase("yes")){
			protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		}else{
			Map<String, String> sharePolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
	 				policyName, requestHeader, suiteData);
			Map<String, String> ciPolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationCIAlertLogMessage(restClient, fileName,
					policyName, requestHeader, suiteData);
	 		Map<String, String> shareDisplayLogsMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient,
	 				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
			Map<String, String> ciDisplayLogsMessage = protectFunctions.getSecurletCIPolicyViolationLogs(restClient,
					policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
			Assert.assertTrue(sharePolicyViolationLogsMessage.isEmpty());
			Assert.assertTrue(ciPolicyViolationLogsMessage.isEmpty());
			Assert.assertTrue(shareDisplayLogsMessage.isEmpty());
			Assert.assertTrue(ciDisplayLogsMessage.isEmpty());
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}
	
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
		universalApi.deleteFolder(folder.getId(), true, "");
	}
	
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
		list.add(BoxDataProvider.getBoxExposureCombinationTests());
		return new Object[][] { { list } };  

	}
}
