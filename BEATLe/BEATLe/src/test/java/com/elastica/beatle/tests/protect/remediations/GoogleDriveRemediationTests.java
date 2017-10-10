
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
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

public class GoogleDriveRemediationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	GDrive gDrive;
	String folderId;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();

	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
	}

	/**
	 * 
	 * 1. Verifying the policy exists by Policy Name 
	 * 2. If policy already exists delete it
	 * 3. Create a policy by the same name and activate it 
	 * 4. Create a new File and upload to the SaaS App 
	 * 5. Share the file as public/external/internal(optional)
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {

		System.out.println("Input data size printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Google Drive : " + folderName, true);
		folderId = universalApi.createFolder(folderName);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				FileUploadResponse fileUploadResponse = null;
				File file = null;
				file = protectFunctions
						.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				gDrive = universalApi.getgDrive();
				try{
					gDrive.printAbout();
				} catch(Exception e){
					System.out.println("Issue Found with GDrive Initialization.. Initializing Again...");
					universalApi = protectFunctions.loginToApp(suiteData);
					gDrive = universalApi.getgDrive();
				}
				fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				fileCollection.put(policyName, file.getName());
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				protectFunctions.waitForMinutes(0.5);
				
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			} 
		}
		
		protectFunctions.waitForMinutes(5);
		shareFileInGDrive(fileIdCollection, arrList);
		protectFunctions.waitForMinutes(20);
	}



	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "GDriveRemediation", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		String fileId = fileIdCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		// Remediation Verification
		protectFunctions.googleDriveRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName, fileId);
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.checkSplunkLogs(suiteData, fileName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testDeletePolicy(List<String[]> list) throws Exception {

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
			} catch (Error e) {
				continue;
			} 
		}
		universalApi.deleteFolder(folderId, true, "");
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
		list.add(GDriveDataProvider.getGoogleDriveRemediationData());
		return new Object[][] { { list } };  

	}
	
	
	private void shareFileInGDrive(Map<String, String> fileIdCollection, List<Object> arrList) throws IOException {

		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			String[] data = (String[]) iterator.next();
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);

			String policyName = policyBean.getPolicyName();
			gDrive = universalApi.getgDrive();

			if (policyName.startsWith(ProtectTestConstants.GDCHANGEACCESS)){
				gDrive.insertPermission(gDrive.getDriveService(), fileIdCollection.get(policyBean.getPolicyName()), null, "anyone",
						policyBean.getPolicyType());
				protectFunctions.waitForMinutes(0.5);
			}
			if (policyName.startsWith(ProtectTestConstants.GDREMOVELINK)) {
				shareFileOnGDrive(fileIdCollection.get(policyBean.getPolicyName()), policyBean, gDrive);
			} 
			if (policyName.startsWith(ProtectTestConstants.GDPREVENTWRITER) || policyName.startsWith(ProtectTestConstants.GDPREVENTDOWNLOAD)) {
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileIdCollection.get(policyBean.getPolicyName()), null, "anyone",
							policyBean.getPolicyType());
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileIdCollection.get(policyBean.getPolicyName()),
							ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, policyBean.getPolicyType());
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileIdCollection.get(policyBean.getPolicyName()),
							suiteData.getDomainName(), ProtectTestConstants.DOMAIN1, policyBean.getPolicyType());
				}
			} 
			if (policyName.startsWith(ProtectTestConstants.GDREMOVECOLLAB) || policyName.startsWith(ProtectTestConstants.GDUPDATECOLLAB)) {
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL) && policyBean.getPolicyType().equalsIgnoreCase(ProtectTestConstants.READER)) {
					Logger.info("External Sharing as Reader:"+gDrive.insertPermission(gDrive.getDriveService(), folderId, ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, ProtectTestConstants.READER).toPrettyString());
					protectFunctions.waitForMinutes(1);
				}
				else if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL) && policyBean.getPolicyType().equalsIgnoreCase(ProtectTestConstants.WRITER)) {
					Logger.info("External Sharing as Writer:"+gDrive.insertPermission(gDrive.getDriveService(), folderId, ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, ProtectTestConstants.WRITER).toPrettyString());
					protectFunctions.waitForMinutes(1);
				}
			}

		}
	}	
	
	private void shareFileOnGDrive(String fileId, PolicyBean policyBean, GDrive gDrive) {
			try {
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileId,
							null, ProtectTestConstants.ANYONE, ProtectTestConstants.READER);
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.EXTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileId,
							ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, ProtectTestConstants.READER);
				}
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
					gDrive.insertPermission(gDrive.getDriveService(), fileId,
							ProtectTestConstants.PROTECTBEATLECOM, ProtectTestConstants.DOMAIN1, ProtectTestConstants.READER);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
}