package com.elastica.beatle.tests.protect.exposure;

import java.io.File;
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

import com.dropbox.core.v2.DbxSharing;
import com.dropbox.core.v2.DbxFiles.FolderMetadata;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.DropBox;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

import junit.framework.Assert;

public class DropboxExposureCombinationTests extends ProtectInitializeTests{

	UniversalApi universalApi;
	Client restClient;
	DropBox dropbox;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> folderCollection = new HashMap<String, String>();
	PolicyBean policyBean;
	FolderMetadata folderMetaData = null;
	String policyName = null;
	
	String externalUser = null;
	String externalUserToken = null;
	String internalUser = null;
	String internalUserToken = null;
	String rootFolder = "/Exposure";
	
	@BeforeClass
	public void init() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		dropbox = universalApi.getDropbox();
		universalApi.createFolder(rootFolder);
		externalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppUsername");
		externalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppToken");;
		internalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppUsername");;
		internalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppToken");;
	}
	
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
				
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Google Drive : " + folderName, true);
				universalApi.createFolder(folderName, rootFolder);
				folderCollection.put(policyName, folderName);
				String childFolder = rootFolder+"/"+folderName;
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(childFolder, file.getAbsolutePath());
				if (fileUploadResponse.getFileId() == null) {
					Reporter.log("File not uploaded, again uploading a file");
					String filename = file.getName().substring(0, file.getName().indexOf("."));
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename),
							policyBean.getFileExt());
					universalApi.uploadFile(childFolder, file.getAbsolutePath());
					policyBean.setFileName(file.getName());
				}
				if(data[21].contains("public")) {
					Reporter.log("Creating a public link for the file - " + file.getName(), true);
					String createSharedLink = universalApi.createSharedLinkForFolder(childFolder + File.separator + file.getName());
					System.out.println("Create shared Link: " + createSharedLink);
				}
				if(data[21].contains("external")) {
					dropbox.shareAndMountFolderToUser(childFolder, externalUser, DbxSharing.AccessLevel.editor, externalUserToken);
				}
				if(data[21].contains("internal")) {
					dropbox.shareAndMountFolderToUser(childFolder, internalUser, DbxSharing.AccessLevel.editor, internalUserToken);
				}
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
			}catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}
	
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "DropboxExposureDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String...data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		if(data[22].equalsIgnoreCase("yes")){
			Map<String, String> sharePolicyViolationLogsMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient, fileName,
	 				policyName, requestHeader, suiteData);
			protectFunctions.assertPolicyViolation(sharePolicyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);
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
		universalApi.deleteFolder("/Exposure", false, null);
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
	
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(DropboxDataProvider.getDropboxExposureCombinationTests());
		return new Object[][] { { list } };  

	}
}
