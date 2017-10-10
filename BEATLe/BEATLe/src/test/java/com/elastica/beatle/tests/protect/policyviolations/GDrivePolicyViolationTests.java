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
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;

public class GDrivePolicyViolationTests extends ProtectInitializeTests{

	GDrive gDrive;
	Client restClient;
	String folderId;

	UniversalApi universalApi;
	PolicyBean policyBean = new PolicyBean();
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();
	
	
	@BeforeClass
	public void initialize() throws Exception{
		restClient = new Client();
		universalApi = protectFunctions.loginToApp(suiteData);
		String folderName = "A_" + UUID.randomUUID().toString();
		folderId = universalApi.createFolder(folderName);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try{
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);

				FileUploadResponse fileUploadResponse = null;
				
				try {
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
					fileCollection.put(policyName, file.getName());
					fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				} catch (Exception e) {
					Reporter.log("File not uploaded", true);
					fileUploadResponse = universalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
				}

				
			}catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(5);
		shareFileInGDrive(fileIdCollection, arrList);
		protectFunctions.waitForMinutes(15);
	}



	
	@Test(dataProviderClass = GDriveDataProvider.class, dataProvider = "GDrivePolicyViolation", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String...data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
			protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testdeletePolicy(List<String[]> list) throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testdeletePolicy - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: testdeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			} 
		}
		universalApi.deleteFolder(folderId, true, "");
	}
	
	@AfterClass
	public void deleteFolders() throws Exception {

		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + ProtectTestConstants.NEW_FILES;
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
		list.add(GDriveDataProvider.getPolicyViolationsInputData());
		return new Object[][] { { list } };  

	}
	
	/**
	 * 
	 * @param policyBean
	 * @param fileUploadResponse
	 * @param exposureType
	 * @throws IOException
	 */
	private void shareFile(PolicyBean policyBean,
			String fileId, String exposureType)
			throws IOException {
		gDrive = universalApi.getgDrive();
		
		Logger.info("Sharing file on GDrive for Policy:"+ policyBean.getPolicyName() +": File Id: "+ fileId + "Exposure: "+exposureType);
		
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, ProtectTestConstants.EXT_USER_GMAIL, ProtectTestConstants.USER, policyBean.getPolicyType());
		}
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, suiteData.getDomainName(), ProtectTestConstants.DOMAIN1, policyBean.getPolicyType());
		}
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, null, ProtectTestConstants.ANYONE, policyBean.getPolicyType());
		}
	}
	
	private void shareFileInGDrive(Map<String, String> fileIdCollection, List<Object> arrList) throws IOException {
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
							shareFile(policyBean, fileIdCollection.get(policyBean.getPolicyName()), s);
						}
					}
				} else {
					String[] selExposures = policyBean.getSeverity().split(",");
					for (String s : selExposures) {
						shareFile(policyBean, fileIdCollection.get(policyBean.getPolicyName()), s);
					}
				}
				
			} else {
				shareFile(policyBean, fileIdCollection.get(policyBean.getPolicyName()), exposureType);
			}
		}
	}

}
