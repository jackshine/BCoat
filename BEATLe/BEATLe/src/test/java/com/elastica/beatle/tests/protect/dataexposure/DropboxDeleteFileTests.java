package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
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
public class DropboxDeleteFileTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdMap = new HashMap<String, String>();
	PolicyBean policyBean;
	//String applianceId;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
/*		if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe") || suiteData.getEnvironmentName().equalsIgnoreCase("prod")) 
			applianceId = dciFunctions.addDLPAppliance(suiteData, restClient, "VontuDLP");*/
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
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "DeleteFileDataProvider", priority = 2)
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
		if (policyBean.getRemediationActivity().contains("quarantine")) {
			Reporter.log("Verifing the quarantine remediation logs", true);
			String quarantineMessage = fileName.substring(0, fileName.indexOf('.') + 1)+ "quarantined"
					+ fileName.substring(fileName.indexOf('.'), fileName.length());
			quarantineMessage = "User added file " + quarantineMessage;
			Map<String, String> quarantineLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), fileCollection
					.get(policyName), requestHeader, suiteData, quarantineMessage);
			protectFunctions.assertRemediation(quarantineLogMessage, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName, ProtectTestConstants.UNSHARE, quarantineMessage);
			Reporter.log("Retriving the file trash logs", true);
			String trashMessage = "User trashed " + fileName;
			Map<String, String> trashLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, trashMessage);
			protectFunctions.assertRemediation(trashLogMessage, ProtectTestConstants.WARNING, policyBean.getCloudService(), fileName, ProtectTestConstants.TRASH, trashMessage);
			Reporter.log("Verified the file trash activity parameters - " + trashLogMessage, true);
		}
		if (policyBean.getRemediationActivity().contains("delete")) {
			Reporter.log("Verifing the delete file remediation logs", true);
			String trashMessage = "User trashed " + fileName;
			Map<String, String> trashLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName,  requestHeader, suiteData, trashMessage);
			Reporter.log(policyName+" - "+trashLogMessage, true);
			protectFunctions.assertRemediation(trashLogMessage, ProtectTestConstants.WARNING, policyBean.getCloudService(), fileName, ProtectTestConstants.TRASH, trashMessage);
			Reporter.log("Verified the file trash activity parameters - " + trashLogMessage, true);
			Reporter.log("Verifing the file trash activity on SaaS App", true);
			Map foldersItems = universalApi.getFoldersItems(File.separator+"FileTransfer", 0, 0);
		    boolean containsKey = foldersItems.containsKey(fileName);
		    Assert.assertTrue(!containsKey);
		    
	/*	    if (policyBean.getRiskType().equalsIgnoreCase("HIPAA") && (suiteData.getEnvironmentName().equalsIgnoreCase("eoe") || suiteData.getEnvironmentName().equalsIgnoreCase("prod"))) {
		    	String ciqMessage = "File "+fileName+ " has risk(s)";
				Map<String, String> ciqLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
						fileName,  requestHeader, suiteData, ciqMessage);
				ciqMessage = ciqLogMessage.get(ProtectTestConstants.MESSAGE);
				protectFunctions.assertDLPRisk(ciqLogMessage, ProtectTestConstants.CRITICAL, policyBean.getCloudService(), fileName, ProtectTestConstants.CONTENT_INSPECTION, ciqMessage);
		    }*/
		    
		}
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
	}

	@AfterClass(alwaysRun = true)
	public void deleteFiles() throws Exception {
/*		if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe") || suiteData.getEnvironmentName().equalsIgnoreCase("prod")) 
			dciFunctions.removeDLPAppliance(applianceId, suiteData, restClient);*/
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
		list.add(DropboxDataProvider.getDeleteFileDataProvider());
		return new Object[][] { { list } };  
	}
}