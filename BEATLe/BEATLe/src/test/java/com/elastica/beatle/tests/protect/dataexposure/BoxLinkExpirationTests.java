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
public class BoxLinkExpirationTests extends ProtectInitializeTests{
	
	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	String filePath = "";
	BoxFolder folder;
	
	@BeforeClass
	public void init() throws Exception{
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
		String uniqueId = UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
		folder = universalApi.createFolder("A_" + uniqueId);
	}
	
	@Test(dataProvider="DataExposureList", priority=1)
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
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				Reporter.log("Check File created: "+String.valueOf(file.exists()), true);
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				protectFunctions.waitForMinutes(0.5);
				
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
				fileCollection.put(policyName, file.getName());
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
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "LinkExpirationDataProvider", priority = 2)
	public void verifyPolicyViolationAndRemediation(String...data) throws Exception{
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyDataExposurePolicyViolationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		String expirationMessage = "User set share expiration on "+fileName;
		Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), fileName, requestHeader, suiteData, expirationMessage);
		protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName, ProtectTestConstants.EXPIRE_SHARING, expirationMessage);
		FileEntry sharedFile = universalApi.getFileInfo(fileEntryCollection.get(policyName).getId());
		Assert.assertNotNull(sharedFile.getSharedLink().getUnsharedAt());
		Reporter.log("Completed testcase: verifyPolicyViolationAndRemediation - "+policyName, true);
	}
	
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
	
	@AfterClass
	public void deleteFolders() throws Exception{
		try{
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH+"newFiles"; 
			System.out.println(directoryName);
			File directory = new File(directoryName);
			System.out.println(directory.exists());
			if(directory.exists()){
				FileUtils.deleteDirectory(directory);
			}
		}catch(IOException ioex){
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
		list.add(BoxDataProvider.getLinkExpirationDataProvider());
		return new Object[][] { { list } };  
	}
}