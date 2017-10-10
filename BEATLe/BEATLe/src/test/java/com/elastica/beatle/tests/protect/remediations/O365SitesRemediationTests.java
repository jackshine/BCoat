package com.elastica.beatle.tests.protect.remediations;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;
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
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.ItemRoleAssignment;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.RoleDefinitions;
import com.universal.dtos.onedrive.SiteFileResource;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UserResult;
import com.universal.dtos.onedrive.UserValue;
import junit.framework.Assert;

public class O365SitesRemediationTests extends ProtectInitializeTests{

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	String siteUrl = "protectsite";
	ProtectFunctions protectFunctions = new ProtectFunctions();
	HashMap<String, Long> usermap = new HashMap<String, Long>();
	HashMap<String, Long> rolemap = new HashMap<String, Long>();
	HashMap<String, SiteFileResource> fileData = new HashMap<String, SiteFileResource>();
	HashMap<String, ListItemAllFields> fileSharingData = new HashMap<String, ListItemAllFields>();
	
	@BeforeClass
	public void init() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		
		SiteUserList splist = universalApi.getRootSiteUserList("protectsite");
		for (UserResult ur : splist.getD().getResults()){
			usermap.put(ur.getTitle(), ur.getId());
		}

		RoleDefinitions roledefs = universalApi.getRootSiteRolesDefinitions("protectsite");
		for (UserValue ur : roledefs.getValue()){
			rolemap.put(ur.getName(), ur.getId());
		}
		Logger.info("============================");
		Logger.info(usermap);
		Logger.info(rolemap);
		Logger.info("============================");
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
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				Logger.info("====================================");
				Logger.info("File to upload: "+file.getName());
				Logger.info("====================================");
				SiteFileResource fileResource = universalApi.uploadFileToRootSite(siteUrl, file.getAbsolutePath(), "/", file.getName());
				ListItemAllFields listitemfields = protectFunctions.o365SiteExposureSharing(universalApi, policyBean, file, siteUrl, usermap, rolemap);
				fileData.put(policyName, fileResource);
				fileSharingData.put(policyName, listitemfields);
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicyWithCIQ(restClient, policyBean, requestHeader, suiteData);
				
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(10);
	}
	
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "SiteRemediation", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileData.get(policyName).getName();
		ListItemAllFields listitemfields = fileSharingData.get(policyName);
		policyBean.setCloudService("Office 365");
		policyBean.setFileName(fileName);		
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		Logger.info("====================================");
		Logger.info("Verifing Remediation");
		Logger.info("====================================");
		ItemRoleAssignment rolesAfterRemediation = universalApi.getRoleAssignmentForRootSiteItem(siteUrl, listitemfields.getOdataEditLink());
		if(policyBean.getRemediationActivity().equals("removeShare")){
			protectFunctions.o365SiteUnshareRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName);
			Assert.assertEquals(1, rolesAfterRemediation.getValue().size());
		}
		if(policyBean.getRemediationActivity().equals("everyoneRead") ||
				policyBean.getRemediationActivity().equals("everyoneContribute") ||
				policyBean.getRemediationActivity().equals("everyoneEdit") ||
				policyBean.getRemediationActivity().equals("everyoneFullControl") ||
				policyBean.getRemediationActivity().equals("everyoneDesign") ||
				policyBean.getRemediationActivity().equals("everyoneExceptExternalUserRead") ||
				policyBean.getRemediationActivity().equals("everyoneExceptExternalUserContribute") ||
				policyBean.getRemediationActivity().equals("everyoneExceptExternalUserDesign") ||
				policyBean.getRemediationActivity().equals("everyoneExceptExternalUserEdit") ||
				policyBean.getRemediationActivity().equals("everyoneExceptExternalUserFullControl")){
			protectFunctions.o365SiteUpdateshareRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName);
		}
		if(policyBean.getRemediationActivity().equals("deletePermissions")){
			protectFunctions.o365SiteDeletePermissionRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName);
		}
		if(policyBean.getRemediationActivity().equals("removeCollab")){
			protectFunctions.o365SiteRemoveCollaboratorRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName);
		}
		if(policyBean.getRemediationActivity().equals("updateCollabRead") ||
				policyBean.getRemediationActivity().equals("updateCollabContribute") ||
				policyBean.getRemediationActivity().equals("updateCollabDesign") ||
				policyBean.getRemediationActivity().equals("updateCollabEdit") ||
				policyBean.getRemediationActivity().equals("updateCollabFullControl")){
			protectFunctions.o365SiteUpdateCollaboratorRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName);
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediation - " + policyName, true);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void deletePolicies(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		//universalApi = protectFunctions.loginToApp(suiteData);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testDeletePolicy - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				universalApi.recycleRootSiteFileItem(siteUrl, fileData.get(policyName).getServerRelativeUrl(), false);
				Reporter.log("Completed testcase: testDeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
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
		list.add(O365DataProvider.getO365SitesRemediation());
		return new Object[][] { { list } };  
	}
}
