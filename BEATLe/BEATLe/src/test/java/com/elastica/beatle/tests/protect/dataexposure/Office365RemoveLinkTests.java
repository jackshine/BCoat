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
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.dci.DCIConstants;
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

/**
 * @author Mayur
 *
 */
public class Office365RemoveLinkTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
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
				itemResourse = createContentIQProfileAndUploadFile(policyName);
				
				// Check and delete if policy exist already
				if (protectFunctions.isPolicyExists(restClient, policyName, requestHeader, suiteData)) {
					protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
				}
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);


				protectFunctions.waitForMinutes(1);
				if(!policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)){
					this.shareFileOnOneDrive(itemResourse, Long.valueOf(policyBean.getPolicyType()));
				}
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "RemoveLinkDataProvider", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediation - " + policyName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		Reporter.log("Verified all the parameters for display logs", true);
		Reporter.log("Retriving the remediation log", true);
		if(policyBean.getRemediationActivity().equalsIgnoreCase("unshare")){
			String remediationMessage = "User unshared '"+fileName+"'";
			String unsharedWithMessage = "";
			if(policyBean.getExposureType().equals(ProtectTestConstants.INTERNAL)){
				unsharedWithMessage = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
			}
			if(policyBean.getPolicyType().equalsIgnoreCase("1")){
				unsharedWithMessage = unsharedWithMessage + "(Read)";
			} else if(policyBean.getPolicyType().equalsIgnoreCase("2")){
				unsharedWithMessage = unsharedWithMessage + "(Contribute)";
			}
			remediationMessage = remediationMessage + " with '" + unsharedWithMessage + "'.";
			Reporter.log("Remediation Message: "+remediationMessage, true);
			Map<String, String> remediationLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, remediationMessage);
			Reporter.log(policyName + " - " + remediationLogs, true);
			
			if (!policyName.endsWith("Exception")) {
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unsharedWithMessage);
			} else {
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), null);
				Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), null);
			}
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediation - " + policyName, true);
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
		list.add(O365DataProvider.getRemoveLinkDataProvider());
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
			userRoleAssignment.setUserId("Everyone except external users");
		}else if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			userRoleAssignment.setUserId("Everyone");
		}else if(policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			userRoleAssignment.setUserId("mayurbelekar@hotmail.com");
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
	
	private ItemResource createContentIQProfileAndUploadFile(String policyName) throws Exception {
		ItemResource itemResourse;
		suiteData.setCSRFToken(requestHeader.get(0).getValue());
		suiteData.setSessionID(requestHeader.get(4).getValue());
	
		if (policyBean.getCiqProfile().equals("PreDefDict")){
			dciFunctions.createContentIqProfile(restClient, suiteData, "PreDefDict", ciqDictProfileName, ciqDictProfileType+" Description", ciqDictProfileType, false);
			dciFunctions.waitForSeconds(1);
			ciqDictFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH,
					ciqDictFileName, dciFunctions.getCurrentDateTime());
			fileCollection.put(policyName, ciqDictFileName);
			policyBean.setFileName(ciqDictFileName);
			policyBean.setCiqProfile(ciqDictProfileName);
			
			itemResourse = universalApi.uploadSimpleFile(folderId,
					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + ciqDictFileName, ciqDictFileName);

		} else if (policyBean.getCiqProfile().equals("PreDefTerms")){
			dciFunctions.createContentIqProfile(restClient, suiteData, "PreDefTerms", ciqTermsProfileName, ciqTermsProfileType+" Description", ciqTermsProfileType, false);
			dciFunctions.waitForSeconds(1);
			ciqTermsFileName = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_CIQ_EXPOSURE_PATH,
					ciqTermsFileName, dciFunctions.getCurrentDateTime());
			fileCollection.put(policyName, ciqTermsFileName);
			policyBean.setFileName(ciqTermsFileName);
			policyBean.setCiqProfile(ciqTermsProfileName);
			itemResourse = universalApi.uploadSimpleFile(folderId,
					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + ciqTermsFileName, ciqTermsFileName);

		} else if (policyBean.getCiqProfile().equals("CustomTerms")){
			
			// have a properties file which has wholesale mapping
			dciFunctions.createContentIqProfile(restClient, suiteData, "CustomTerms", customTermsProfileName, customTermsProfileName +" Description", "CLASSIFICATION=INTERNAL", false);
			customTermsFileName = protectFunctions.createSampleFileType(ProtectTestConstants.PROTECT_RESOURCE_PATH,
					customTermsFileName, dciFunctions.getCurrentDateTime());
			fileCollection.put(policyName, customTermsFileName);
			policyBean.setFileName(customTermsFileName);
			policyBean.setCiqProfile(customTermsProfileName);
			
			itemResourse = universalApi.uploadSimpleFile(folderId,
					ProtectTestConstants.PROTECT_RESOURCE_PATH +"temp" + File.separator + customTermsFileName, customTermsFileName);
			
		}
		else {
			File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
					policyBean.getFileExt());
			fileCollection.put(policyName, file.getName());
			policyBean.setFileName(file.getName());
			itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
		}
		return itemResourse;
	}
}