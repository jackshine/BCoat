package com.elastica.beatle.tests.protect.remediations;

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

import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.DropBox;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

public class DropboxRemediationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> sharedLinkMap = new HashMap<String, String>();
	PolicyBean policyBean;
	DropBox dropbox;
	Map<String, String> preActivityCount = new HashMap<String, String>();
	Map<String, String> postActivityCount = new HashMap<String, String>();
	String externalUser; String externalUserToken;
	String internalUser; String internalUserToken;

	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		dropbox = universalApi.getDropbox();
		restClient = new Client();
		externalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppUsername");
		externalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppToken");
		internalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppUsername");
		internalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppToken");
		preActivityCount = protectFunctions.getAllActivityCount(restClient, ProtectTestConstants.DROPBOX, requestHeader, suiteData);
	}

	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		FileUploadResponse fileUploadResponse;
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
				String internalFolder = "/Elastica QA Team Folder";
				String exposureType = policyBean.getExposureType();
				String fileName = file.getName();
				
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL) || (policyBean.getExposureType().contains(ProtectTestConstants.INTERNAL) && policyBean.getSeverity().contains(ProtectTestConstants.INTERNAL)))
					fileUploadResponse = universalApi.uploadFile(internalFolder, file.getAbsolutePath());
				else
					fileUploadResponse = universalApi.uploadFile("/Remediation", file.getAbsolutePath());
				
				
				if (fileUploadResponse.getFileId() == null) {
					Reporter.log("File not uploaded, again uploading a file");
					String filename = file.getName().substring(0, file.getName().indexOf("."));
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename),
							policyBean.getFileExt());
					universalApi.uploadFile("/Remediation", file.getAbsolutePath());
					policyBean.setFileName(file.getName());
				}
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				
				if (exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
					Reporter.log("Creating a public link for the file - " + fileName, true);
					String createSharedLink = universalApi.createSharedLinkForFolder("/Remediation" + File.separator + fileName);
					System.out.println("Create shared Link: " + createSharedLink);
				}
				if (exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)) {
					dropbox.shareAndMountFolderToUser("/Remediation", externalUser, DbxSharing.AccessLevel.editor, externalUserToken);
				}
				if (exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
					//dropbox.shareAndMountFolderToUser(childFolder, internalUser, DbxSharing.AccessLevel.editor, internalUserToken);
				}

				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}

	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "DropboxRemediationsDataProvider", priority = 2)
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
		if (policyBean.getRemediationActivity().contains("sharedLinkRevoke")) {
			Reporter.log("Verifing remediation log for the shared link of the file deleted", true);
			String removeLinkMessage = "Removed a link " + fileName;
			Map<String, String> removeLinkLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName,  requestHeader, suiteData, removeLinkMessage);
			
			if (removeLinkLogMessage.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			Reporter.log(policyName+" - "+removeLinkLogMessage, true);
			protectFunctions.assertRemediation(removeLinkLogMessage, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName, ProtectTestConstants.UNSHARE, removeLinkMessage);
			Reporter.log("Verified the removed share link activity parameters - " + removeLinkLogMessage, true);
			Reporter.log("Verifing the file trash activity on SaaS App", true);
			//HttpResponse sharedLinkRes = restClient.doGet(new URI(sharedLinkMap.get(policyName)), null);
		    //Assert.assertTrue(ClientUtil.getResponseBody(sharedLinkRes).contains("We can't find what you're looking for"));
		}
		if (policyBean.getRemediationActivity().contains(ProtectTestConstants.DELETE)) {
			Reporter.log("Verifing the delete file remediation logs", true);
			String trashMessage = "User trashed " + fileName;
			Map<String, String> trashLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName,  requestHeader, suiteData, trashMessage);
			
			if (trashLogMessage.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			Reporter.log(policyName+" - "+trashLogMessage, true);
			protectFunctions.assertRemediation(trashLogMessage, ProtectTestConstants.WARNING, policyBean.getCloudService(), fileName, ProtectTestConstants.TRASH, trashMessage);
			Reporter.log("Verified the file trash activity parameters - " + trashLogMessage, true);
			Reporter.log("Verifing the file trash activity on SaaS App", true);
			Map foldersItems = universalApi.getFoldersItems(File.separator+"FileTransfer", 0, 0);
		    boolean containsKey = foldersItems.containsKey(fileName);
		    Assert.assertTrue(!containsKey);
		}
		Reporter.log("Completed testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.checkSplunkLogs(suiteData, fileName, true);
	}
	
/*	@Test(priority = 3)
	public void getPostActivityCount() throws Exception {
		postActivityCount = protectFunctions.getAllActivityCount(restClient, "Dropbox", requestHeader, suiteData);
		Reporter.log("Updated Activity Count: "+postActivityCount, true);
	}
	
	@Test(priority = 4)
	public void verifyUnshareCount(){
		int preUnshare = Integer.parseInt(preActivityCount.get("Unshare"));
		int postUnshare = Integer.parseInt(postActivityCount.get("Unshare"));
		Reporter.log("Initial Unshare Count: "+preUnshare, true);
		Reporter.log("Updated Unshare Count: "+postUnshare, true);
		Assert.assertEquals(postUnshare, preUnshare+1);
	}
	
	@Test(priority = 5)
	public void verifyTrashCount(){
		int preTrash = Integer.parseInt(preActivityCount.get("Trash"));
		int postTrash = Integer.parseInt(postActivityCount.get("Trash"));
		Reporter.log("Initial Trash Count: "+preTrash, true);
		Reporter.log("Updated Trash Count: "+postTrash, true);
		Assert.assertEquals(postTrash, preTrash+1);
	}*/

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
		universalApi.deleteFolder("/Remediation", false, null);
	}

	@AfterClass(alwaysRun = true)
	public void deleteFiles() throws Exception {
		String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
		File directory = new File(directoryName);
		Logger.info("Directory Exists:"+ directory.exists());
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}
	}

	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(DropboxDataProvider.getDropboxRemediationsTests());
		return new Object[][] { { list } };  
	}
}