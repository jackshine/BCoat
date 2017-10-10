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
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;


public class OneDriveRemediationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	String folderId;
	Folder folder;
	String ciqDictProfileName="DCI_DIS"; String ciqTermsProfileName="DCI_USALPN";
	String ciqDictProfileType="Diseases";String ciqTermsProfileType="US License Plate Numbers";
	String ciqDictFileName = "Diseases.txt"; String ciqTermsFileName = "US_License_Plate_Number.txt";
	String customTermsFileName = "Wholesale doc with PCI.docx";
	String customTermsProfileName = "CIQ_CT";
	
	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		restClient = new Client();
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void createPolicyAndUploadFile(List<String[]> list) throws Exception {
		
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		ItemResource itemResourse = null;

		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				fileCollection.put(policyName, file);
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
				protectFunctions.waitForMinutes(1);

				// Remove Link, Update File Permissions, Remove External Collab, Update Extenal Collab
				if(!policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)){
					this.shareFileOnOneDrive(itemResourse, Long.valueOf(policyBean.getPolicyType()));
				}
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ODriveRemediation", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediation - " + policyName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		Reporter.log("Verified all the parameters for display logs", true);

		Reporter.log("Retriving the remediation log", true);
		protectFunctions.o365RemediationVerification(restClient, fileName, requestHeader, suiteData, policyBean);
		Reporter.log("Completed testcase: testPolicyViolationAndRemediation - " + policyName, true);
		protectFunctions.checkSplunkLogs(suiteData, fileName, true);
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
		universalApi = protectFunctions.loginToApp(suiteData);
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
		universalApi.deleteFolderV2(folder.getId(), folder.getETag());
	}

	/**
	 * 
	 * @throws Exception
	 */
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
		list.add(O365DataProvider.getOneDriveRemediationData());
		return new Object[][] { { list } };  

	}	
	
	
	private void shareFileOnOneDrive(ItemResource itemResourse, Long roleId) throws Exception{
		SharingUserRoleAssignment sharingUserRoleAssignment = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> userRoleAssignmentList = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);
		userRoleAssignment.setRole(roleId);
		if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			userRoleAssignment.setUserId(ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS);
		}else if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			userRoleAssignment.setUserId(ProtectTestConstants.EVERYONE);
		}else if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			userRoleAssignment.setUserId(ProtectTestConstants.EXTERNAL_USER_O365);
		}
		userRoleAssignmentList.add(userRoleAssignment);
		sharingUserRoleAssignment.setUserRoleAssignments(userRoleAssignmentList);
		sharingUserRoleAssignment.setResourceAddress(itemResourse.getWebUrl());
		sharingUserRoleAssignment.setValidateExistingPermissions(false);
		sharingUserRoleAssignment.setAdditiveMode(true);
		sharingUserRoleAssignment.setSendServerManagedNotification(false);
		sharingUserRoleAssignment.setCustomMessage("This is a custom message");
		sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(true);
		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		System.out.println(documentSharingResult);
	}

}