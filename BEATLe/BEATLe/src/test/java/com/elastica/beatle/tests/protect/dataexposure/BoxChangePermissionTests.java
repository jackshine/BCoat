
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
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;

/**
 * @author Mayur
 *
 */
public class BoxChangePermissionTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	String filePath = "";
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	Map<String, BoxCollaboration> collaboratorCollection = new HashMap<String, BoxCollaboration>();

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
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				Reporter.log("Check File created: "+String.valueOf(file.exists()), true);
				policyBean.setFileName(file.getName());
				fileCollection.put(policyName, file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				protectFunctions.waitForMinutes(0.5);
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				BoxFolder folder = universalApi.createFolder(folderName);
				folderCollection.put(policyName, folder);
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				if(policyBean.getExposureType().equalsIgnoreCase("external")){
					CollaborationInput collabInput = new CollaborationInput();
					Item item = new Item();
					item.setId(folder.getId());
					item.setType("folder");
					String uniqueName = UUID.randomUUID().toString();
					AccessibleBy aby = new AccessibleBy();
					aby.setName(uniqueName);
					aby.setType("user");
					aby.setLogin("mayurbelekar@gmail.com");
					collabInput.setItem(item);
					collabInput.setAccessibleBy(aby);
					collabInput.setRole("editor");
					BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
					collaboratorCollection.put(policyName, collaboration);
				}else{
					FileEntry fileEntry = protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folder, policyBean.getExposureType());
					fileEntryCollection.put(policyName, fileEntry);
				}
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
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
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "ChangePermissionDataProvider", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);

		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		
		if (policyBean.getRemediationActivity().contains("company") && policyBean.getExposureType().contains("public")) {
			String sharePermissionMessage = "User updated share permissions on " + fileName;
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, sharePermissionMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), fileName,
					ProtectTestConstants.SHARE, sharePermissionMessage);
			FileEntry sharedFile = universalApi.getFileInfo(fileEntryCollection.get(policyName).getId());
			Assert.assertEquals(sharedFile.getSharedLink().getAccess(), policyBean.getRemediationActivity());
		}
		if (policyBean.getRemediationActivity().contains("open") && policyBean.getExposureType().contains("external")) {
			String sharePermissionMessage = "User updated share permissions on " + folderCollection.get(policyName).getName();
			Map<String, String> remediationLog = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					folderCollection.get(policyName).getName(), requestHeader, suiteData, sharePermissionMessage);
			protectFunctions.assertRemediation(remediationLog, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderCollection.get(policyName)
					.getName(), ProtectTestConstants.SHARE, sharePermissionMessage);
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}

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
				universalApi.deleteFolder(folderCollection.get(policyName).getId(), true, folderCollection.get(policyName).getEtag());
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
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
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
		list.add(BoxDataProvider.getChangePermissionDataProvider());
		return new Object[][] { { list } };  
	}
}
