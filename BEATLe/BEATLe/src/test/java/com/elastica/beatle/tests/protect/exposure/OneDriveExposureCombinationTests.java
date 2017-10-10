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
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.elastica.beatle.tests.securlets.OneDriveUtils;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;

import junit.framework.Assert;

public class OneDriveExposureCombinationTests extends ProtectInitializeTests{

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, String> fileMapCollection = new HashMap<String, String>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
	String folderId;
	Folder folder;
	
	
	@BeforeClass
	public void init() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		folderId = folder.getId();
	}
	
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void createPolicyAndUploadFile(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				fileCollection.put(policyName, file);
				fileMapCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				ItemResource itemResourse = universalApi.uploadSimpleFile(folderId, file.getAbsolutePath(), file.getName());
				protectFunctions.waitForMinutes(0.5);
				if(data[21].contains(ProtectTestConstants.PUBLIC)){
					//protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.PUBLIC);
					SharingUserRoleAssignment sharingUserRoleAssignment = new OneDriveUtils().getPublicShareObject(itemResourse.getWebUrl(), 1);
					universalApi.shareWithCollaborators(sharingUserRoleAssignment);
				}if(data[21].contains(ProtectTestConstants.EVERYONE)){
					protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EVERYONE);
				}if(data[21].contains(ProtectTestConstants.INTERNAL)){
					protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.INTERNAL);
				}if(data[21].contains(ProtectTestConstants.EXTERNAL)){
					protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EXTERNAL);
				}
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "OneDriveExposureDataProvider", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		universalApi = protectFunctions.loginToApp(suiteData);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediation - " + policyName, true);
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
		Reporter.log("Completed testcase: testPolicyViolationAndRemediation - " + policyName, true);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void deletePolicies(List<String[]> list) throws Exception {
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
				universalApi.deleteFolderV2(folderCollection.get(policyName).getId(), folderCollection.get(policyName).getETag());
				Reporter.log("Completed testcase: testDeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
	}
	
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

	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(O365DataProvider.getOneDriveExposureDataProvider());
		return new Object[][] { { list } };  
	}
}
