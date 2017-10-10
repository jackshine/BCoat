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
import org.testng.annotations.BeforeMethod;
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

import junit.framework.Assert;

public class GoogleDriveExposureCombinationTests extends ProtectInitializeTests{
	GDrive gDrive;
	Client restClient;
	String folderId;
	String user;
	String clientId = null;
	String clientSecret = null;
	String refreshToken = null;
	String policyName = null;
	UniversalApi universalApi;
	PolicyBean policyBean = new PolicyBean();
	ProtectFunctions protectFunctions = new ProtectFunctions();
	String filePath = "";
	Map<String, String> fileCollection = new HashMap<String, String>();
	
	
	@BeforeClass
	public void initialize() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		gDrive = universalApi.getgDrive();
		String folderName = "A_" + UUID.randomUUID().toString();
		folderId = universalApi.createFolder(folderName);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
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
				
				try {
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
					fileCollection.put(policyName, file.getName());
				} catch (Exception e) {
					Reporter.log("File not uploaded", true);
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				}
				protectFunctions.waitForMinutes(0.5);
				if(data[21].contains(ProtectTestConstants.EXTERNAL)){
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), "mayurbelekar@gmail.com", "user", policyBean.getPolicyType());
				}
				if(data[21].contains(ProtectTestConstants.INTERNAL)){
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), suiteData.getDomainName(), "domain", policyBean.getPolicyType());
				}
				if(data[21].contains(ProtectTestConstants.PUBLIC)){
					gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, "anyone", policyBean.getPolicyType());
				}
			}catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(15);
	}
	
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "GoogleDriveExposureDataProvider", priority = 2)
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
		universalApi.deleteFolder(folderId, true, "");
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
		list.add(GDriveDataProvider.getGoogleDriveExposureCombinationTests());
		return new Object[][] { { list } };  

	}
}
