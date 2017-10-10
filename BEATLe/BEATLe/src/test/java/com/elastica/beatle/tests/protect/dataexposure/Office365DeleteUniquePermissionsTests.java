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

/**
 * @author Mayur
 *
 */
public class Office365DeleteUniquePermissionsTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	OneDriveUtils oneDriveUtils = new OneDriveUtils();
	Map<String, String> fileCollection = new HashMap<String, String>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
	Folder folder;
	public static int filePointer;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
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
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		protectFunctions.shareFolderOnOneDrive(folder, universalApi, 2, "external");
		
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		String[] data = (String[]) iterator.next();
		policyBean = protectFunctions.populatePolicyBean(data);
		protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
		policyBean = protectFunctions.populatePolicyBean(data);
		File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
				policyBean.getFileExt());
		policyBean.setFileName(file.getName());
		fileCollection.put(policyBean.getPolicyName(),policyBean.getFileName());
		ItemResource itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
		protectFunctions.waitForMinutes(0.5);
		protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, Integer.parseInt(policyBean.getPolicyType()), 
				policyBean.getExposureType());
		Reporter.log("********************************", true);
		Reporter.log("Filename: "+policyBean.getFileName(), true);
		Reporter.log("********************************", true);
		while (iterator.hasNext()) {
			try {
				data = (String[]) iterator.next();
				policyBean = protectFunctions.populatePolicyBean(data);
				file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				fileCollection.put(policyBean.getPolicyName(),policyBean.getFileName());
				itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
				protectFunctions.waitForMinutes(0.5);
				protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, Integer.parseInt(policyBean.getPolicyType()), 
						policyBean.getExposureType());
				Reporter.log("********************************", true);
				Reporter.log("Filename: "+policyBean.getFileName(), true);
				Reporter.log("********************************", true);
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
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "DeleteUniquePermissionsDataProvider", priority = 2)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		
		//if (!policyBean.getPolicyName().equalsIgnoreCase("ODPOLICY301")){
		//String fileName = fileCollection.get(filePointer);
		//filePointer ++;
		String fileName = fileCollection.get(policyBean.getPolicyName());
		policyBean.setFileName(fileName);
		policyBean.setPolicyName("DELUNIQ301");
		protectFunctions.logTestDescription(policyBean);
		
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		Reporter.log("Retriving the remediation log", true);
		
		// Remediation verification
		protectFunctions.o365RemediationVerification(restClient, fileName, requestHeader, suiteData, policyBean);
		
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
		list.add(O365DataProvider.getDeleteUniquePermissionsProvider());
		return new Object[][] { { list } };  

	}	
	
}
