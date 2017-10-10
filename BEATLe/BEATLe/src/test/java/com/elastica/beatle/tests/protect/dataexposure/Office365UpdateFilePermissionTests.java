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
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;

/**
 * @author Mayur
 * #30582 - Tests are not executing doe to this issue
 */
public class Office365UpdateFilePermissionTests extends ProtectInitializeTests {

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

				// Check and delete if policy exist already
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				ItemResource itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
				protectFunctions.waitForMinutes(0.5);
				protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, Integer.parseInt(policyBean.getPolicyType()), policyBean.getExposureType());
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
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "UpdateFilePermissionsDataProvider", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediation - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileMapCollection);
		Reporter.log("Retriving the remediation log", true);
		String remediationMessage = "'"+fileName+"'" + " has been shared";
		String unsharedWithMessage = "";
/*		if(policyBean.getRemediationActivity().contains(ProtectTestConstants.EVERYONE.toLowerCase())){
			unsharedWithMessage = ProtectTestConstants.EVERYONE;
		}else{
			unsharedWithMessage = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
		}
		if(policyBean.getRemediationActivity().toLowerCase().contains("read")){
			unsharedWithMessage = unsharedWithMessage + "(Read)";
		} else if(policyBean.getRemediationActivity().toLowerCase().contains("contribute")){
			unsharedWithMessage = unsharedWithMessage + "(Contribute)";
		}*/
		//remediationMessage = remediationMessage + " with '" + unsharedWithMessage;
		Reporter.log("Expected Remediation log: "+remediationMessage, true);
		Map<String, String> remediationLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
				fileName, requestHeader, suiteData, remediationMessage);
		Reporter.log(policyName + " - " + remediationLogs, true);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
		//Assert.assertEquals(remediationLogs.get(ProtectTestConstants.DOMAIN), suiteData.getDomainName());
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.OBJECT_TYPE), ProtectTestConstants.FILE);
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.SHARE);
		//Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
		Assert.assertTrue(remediationLogs.get(ProtectTestConstants.MESSAGE).contains(remediationMessage));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
		//Assert.assertTrue(remediationLogs.get(ProtectTestConstants.SHARED_WITH).contains(unsharedWithMessage));
		Assert.assertEquals(remediationLogs.get(ProtectTestConstants.USER), suiteData.getSaasAppUsername());
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
		list.add(O365DataProvider.getUpdateFilePermissionsDataProvider());
		return new Object[][] { { list } };  

	}	
}