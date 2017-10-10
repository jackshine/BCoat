
package com.elastica.beatle.tests.protect.remediations;

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
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;

/**
 * @author Mayur
 *
 */
public class BoxRemediationTests extends ProtectInitializeTests {

	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>(); Map<String, String> fileIdCollection = new HashMap<String, String>();
	Map<String, String> folderCollection = new HashMap<String, String>();  Map<String, String> folderNameCollection = new HashMap<String, String>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	UniversalApi universalApi;
	Client restClient;
	BoxFolder folder;
	Map<String, String> preActivityCount = new HashMap<String, String>(); Map<String, String> postActivityCount = new HashMap<String, String>();

	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		preActivityCount = protectFunctions.getAllActivityCount(restClient, ProtectTestConstants.BOX, requestHeader, suiteData);
		Reporter.log("Initial Activity Count: "+preActivityCount, true);
	}

	/**
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
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				fileCollection.put(policyBean.getPolicyName(), file);
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				String childFolderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + childFolderName, true);
				BoxFolder childFolder = universalApi.createFolder(childFolderName, folder.getId());
				
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(childFolder.getId(), file.getAbsolutePath(), file.getName());
				folderCollection.put(policyBean.getPolicyName(), childFolder.getId());
				folderNameCollection.put(policyBean.getPolicyName(), childFolderName);				
				fileIdCollection.put(policyBean.getPolicyName(), fileUploadResponse.getFileId());				
				
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(5);		
		shareFileInBox(arrList);
		protectFunctions.waitForMinutes(10);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "BoxRemediationDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);		
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		
		String[] remActivities = policyBean.getRemediationActivity().split(",");
		for (String remediationActivity : remActivities) {

		// TODO: Below logic to be moved to ProtectFunctions	
		if (remediationActivity.contains(ProtectTestConstants.COMPANY)) {
			String sharePermissionMessage = "User updated share permissions on " + fileName;
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, sharePermissionMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.SHARE, sharePermissionMessage);
			FileEntry sharedFile = universalApi.getFileInfo(fileIdCollection.get(policyName));
			Assert.assertEquals(sharedFile.getSharedLink().getAccess(), policyBean.getRemediationActivity());
		}
		if (remediationActivity.contains(ProtectTestConstants.OPEN) && !policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
			String sharePermissionMessage = "User updated share permissions on " + folderNameCollection.get(policyName);
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					folderNameCollection.get(policyName), requestHeader, suiteData, sharePermissionMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderNameCollection.get(policyName)
					, ProtectTestConstants.SHARE, sharePermissionMessage);
		}
		if (remediationActivity.contains(ProtectTestConstants.OPEN) && policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)) {
			String sharePermissionMessage = "User updated share permissions on " + fileName;
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, sharePermissionMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderNameCollection.get(policyName)
					, ProtectTestConstants.SHARE, sharePermissionMessage);
		}
		if (remediationActivity.contains(ProtectTestConstants.EXPIRES)){
			String expirationMessage = "User set share expiration on "+fileName;
			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), fileName, requestHeader, suiteData, expirationMessage);
			protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName, ProtectTestConstants.EXPIRE_SHARING, expirationMessage);
			FileEntry sharedFile = universalApi.getFileInfo(fileIdCollection.get(policyName));
			Assert.assertNotNull(sharedFile.getSharedLink().getUnsharedAt());
		}if (remediationActivity.contains(ProtectTestConstants.REMOVE_COLLAB)){
			String removeCollaboratorMessage = "User removed a collaborator";
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), 
					folderNameCollection.get(policyName), requestHeader, suiteData, removeCollaboratorMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderNameCollection.get(policyName), ProtectTestConstants.UNSHARE, removeCollaboratorMessage);
			Assert.assertEquals(remediationLog.get(ProtectTestConstants.OBJECT), folderNameCollection.get(policyName));
			Collaborations collaborations = universalApi.getFolderCollaborations(folderCollection.get(policyName));
			Assert.assertEquals(collaborations.getTotalCount(), new Long(0));
		}if (remediationActivity.contains(ProtectTestConstants.UNSHARE1)){
			String unshareMessage = "User unshared " + fileName;
			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, unshareMessage);
			protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(),
					fileName, ProtectTestConstants.UNSHARE, "User unshared " + fileName);
			FileEntry sharedFile = universalApi.getFileInfo(fileIdCollection.get(policyName));
			Assert.assertNull(sharedFile.getSharedLink(), fileName+" is not unshared");
		}
		if(remediationActivity.contains(ProtectTestConstants.EDITOR) ||
				policyBean.getRemediationActivity().contains(ProtectTestConstants.VIEWER) ||
				policyBean.getRemediationActivity().contains(ProtectTestConstants.UPLOADER) ||
				policyBean.getRemediationActivity().contains(ProtectTestConstants.PREVIEWER) ||
				policyBean.getRemediationActivity().contains(ProtectTestConstants.CO_OWNER) ||
				policyBean.getRemediationActivity().contains("viewer uploader") ||
				policyBean.getRemediationActivity().contains("previewer uploader")){
			
			if (policyBean.getRemediationActivity().contains(ProtectTestConstants.VIEWER))
				policyBean.setRemediationActivity(remediationActivity);
			
			
			String updateCollabMessage = "User changed a collaboration role";
			Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), folderNameCollection.get(policyName), requestHeader, suiteData, updateCollabMessage);
			
			if (messagesLogs.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			
			protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderNameCollection.get(policyName), ProtectTestConstants.CHANGE_COLLABORATION_ROLE, updateCollabMessage);
			Collaborations collaborations = universalApi.getFolderCollaborations(folderCollection.get(policyName));
			Reporter.log("Changed Role: "+collaborations.getEntries().get(0).getRole(), true);
			//Assert.assertEquals(policyBean.getRemediationActivity().contains(collaborations.getEntries().get(0).getRole()), "Remediation role not applied");
		}
		
		protectFunctions.checkSplunkLogs(suiteData, fileName, true);
		
		}
		
		
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}
	
/*	@Test(priority = 3)
	public void getPostActivityCount() throws Exception {
		postActivityCount = protectFunctions.getAllActivityCount(restClient, "Box", requestHeader, suiteData);
		Reporter.log("Updated Activity Count: "+postActivityCount, true);
		
		String preExpireSharing = preActivityCount.get("Expire Sharing");
		String postExpireSharing = postActivityCount.get("Expire Sharing");
		Reporter.log("Pre Exiration Count: "+preExpireSharing, true);
		Reporter.log("Post Exiration Count: "+postExpireSharing, true);
		Assert.assertEquals(Integer.parseInt(postExpireSharing), Integer.parseInt(preExpireSharing)+1);
		
		String changeCollaboratorRole = preActivityCount.get("Change Collaboration Role");
		String updateChangeCollaboratorRole = postActivityCount.get("Change Collaboration Role");
		Reporter.log("Pre Change Collaboration Role Count: "+changeCollaboratorRole, true);
		Reporter.log("Post Change Collaboration Role Count: "+updateChangeCollaboratorRole, true);
		Assert.assertEquals(Integer.parseInt(updateChangeCollaboratorRole), Integer.parseInt(changeCollaboratorRole)+42);
	}*/

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testDeletePolicy(List<String[]> list) throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
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
			} catch(Error e) {
				continue;
			}
		}
		universalApi.deleteFolder(folder.getId(), true, "");
	}

	@AfterClass
	public void deleteFolders() throws Exception {
		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
			File directory = new File(directoryName);
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
		list.add(BoxDataProvider.getBoxRemediationTests());
		return new Object[][] { { list } };  
	}
	
	/**
	 * 
	 * @param arrList
	 * @param fileCollection
	 * @param folderCollection
	 * @throws Exception
	 */
	private void shareFileInBox(List<Object> arrList)
			throws Exception {
		String[] collaborator = null;
		
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {

			String[] data = (String[]) iterator.next();
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		
			if(policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)){
				collaborator = new String[0];
				FileEntry fileEntry = protectFunctions.shareTheFilePublicly(universalApi, fileIdCollection.get(policyBean.getPolicyName()), fileCollection.get(policyBean.getPolicyName()).getAbsolutePath(), fileCollection.get(policyBean.getPolicyName()).getName(), ProtectTestConstants.OPEN);
				fileEntryCollection.put(policyBean.getPolicyName(), fileEntry);
				Reporter.log("File link shared as public", true);
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.INTERNAL)){
				collaborator = new String[0];
				protectFunctions.shareTheFolderPubliclyOrWithCollaborators(universalApi, folderCollection.get(policyBean.getPolicyName()), fileCollection.get(policyBean.getPolicyName()).getAbsolutePath(), fileCollection.get(policyBean.getPolicyName()).getName(), ProtectTestConstants.COMPANY, collaborator, policyBean.getPolicyType());
				Reporter.log("File link shared as internal", true);
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL)){
				collaborator = new String[]{ProtectTestConstants.EXT_USER};
				BoxFolder sharedFolder = protectFunctions.shareTheFolderPubliclyOrWithCollaborators(universalApi, folderCollection.get(policyBean.getPolicyName()), fileCollection.get(policyBean.getPolicyName()).getAbsolutePath(), fileCollection.get(policyBean.getPolicyName()).getName(), ProtectTestConstants.COLLABORATORS, collaborator, policyBean.getPolicyType());
				Logger.info("Folder Status Link Printing....:"+sharedFolder.getSharedLink());
				Logger.info("Folder Status Item Status Printing....:"+sharedFolder.getItemStatus());
				Reporter.log("File link shared as external", true);
			}
			//protectFunctions.waitForMinutes(0.5);
		}
	}
	
}