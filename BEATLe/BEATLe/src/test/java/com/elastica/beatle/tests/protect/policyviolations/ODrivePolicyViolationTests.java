package com.elastica.beatle.tests.protect.policyviolations;

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

public class ODrivePolicyViolationTests extends ProtectInitializeTests{

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileMapCollection = new HashMap<String, String>();
	Map<String, ItemResource> itemResourceCollection = new HashMap<String, ItemResource>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	String folderId; Folder folder;
	
	
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
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				fileCollection.put(policyName, file.getName());
				fileMapCollection.put(policyName, file.getName());

				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				ItemResource itemResourse = universalApi.uploadSimpleFile(folderId, file.getAbsolutePath(), file.getName());
				itemResourceCollection.put(policyName, itemResourse);
				
				
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		
		protectFunctions.waitForMinutes(10);
		shareFileInOneDrive(itemResourceCollection, arrList);
		protectFunctions.waitForMinutes(10);
	}
	
	private void shareFileInOneDrive(Map<String, ItemResource> itemResourceCollection, List<Object> arrList) throws Exception {
		String exposureType = null; String exposureTypeTemp = null; String[] exposures = null;
		
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			String[] data = (String[]) iterator.next();
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
			exposureType = policyBean.getExposureType();
			exposureTypeTemp = policyBean.getExposureType();
			exposures = exposureTypeTemp.split(",");
			
			if (policyBean.getSeverity() != null) {
				if (policyBean.getExposureType().contains(ProtectTestConstants.ALL)) {
					for (String s : exposures) {
						if (!s.equalsIgnoreCase(ProtectTestConstants.ALL)) {
							shareFileInODrive(itemResourceCollection.get(policyBean.getPolicyName()), s);
						}
					}
				} else {
					String[] selExposures = policyBean.getSeverity().split(",");
					for (String s : selExposures) {
						shareFileInODrive(itemResourceCollection.get(policyBean.getPolicyName()), s);
					}
				}
			} else {
				shareFileInODrive(itemResourceCollection.get(policyBean.getPolicyName()), exposureType);
			}
		}
	}

	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "ODrivePolicyViolation", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);		
		
		
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
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
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
		list.add(O365DataProvider.getPolicyViolationsInputData());
		return new Object[][] { { list } };  
	}
	
	/**
	 * 
	 * @param itemResourse
	 * @param exposureType
	 * @throws Exception
	 */
	private void shareFileInODrive(ItemResource itemResourse,
			String exposureType) throws Exception {
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			SharingUserRoleAssignment sharingUserRoleAssignment = new OneDriveUtils().getPublicShareObject(itemResourse.getWebUrl(), 1);
			universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EVERYONE)){
			protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EVERYONE);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.INTERNAL);
		}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EXTERNAL);
		}
	}
}
