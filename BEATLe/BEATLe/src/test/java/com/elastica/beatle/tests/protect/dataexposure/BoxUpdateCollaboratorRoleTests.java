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
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.Item;

/**
 * @author Mayur
 *
 */
public class BoxUpdateCollaboratorRoleTests extends ProtectInitializeTests{
	
	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, FileEntry> fileEntryCollection = new HashMap<String, FileEntry>();
	Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	Map<String, BoxCollaboration> collaboratorCollection = new HashMap<String, BoxCollaboration>();
	//Map<String, String> localFolders = new HashMap<String, String>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH;
	
	@BeforeClass
	public void init() throws Exception{
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
	}
	
	@Test(dataProvider="DataExposureList", priority=1)
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
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + folderName, true);
				BoxFolder folder = universalApi.createFolder(folderName);
				Thread.sleep(15000);
				universalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
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
				collabInput.setRole(policyBean.getPolicyType());
				BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
				fileCollection.put(policyName, file.getName());
				folderCollection.put(policyName, folder);
				collaboratorCollection.put(policyName, collaboration);
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(15);
		
	}
	
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "UpdateCollaboratorDataProvider", priority = 2)
	public void verifyPolicyViolationAndRemediation(String...data) throws Exception{
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		String folderName = folderCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyDataExposurePolicyViolationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		String updateCollabMessage = "User changed a collaboration role";
		Map<String, String> messagesLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(), folderName, requestHeader, suiteData, updateCollabMessage);
		protectFunctions.assertRemediation(messagesLogs, ProtectTestConstants.INFORMATIONAL, policyBean.getCloudService(), folderName, ProtectTestConstants.CHANGE_COLLABORATION_ROLE, updateCollabMessage);
		Collaborations collaborations = universalApi.getFolderCollaborations(folderCollection.get(policyName).getId());
		Reporter.log("Changed Role: "+collaborations.getEntries().get(0).getRole(), true);
		Assert.assertEquals(collaborations.getEntries().get(0).getRole(), policyBean.getRemediationActivity(), "Remediation role not applied");
		Reporter.log("Completed testcase: verifyPolicyViolationAndRemediation - "+policyName, true);
	}
	
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void deletePolicies(List<String[]> list) throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: deletePolicies - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				universalApi.deleteFolder(folderCollection.get(policyName).getId(), true, folderCollection.get(policyName).getEtag());
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
	}
	
	@AfterClass
	public void deleteFolders() throws Exception{
		try{
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH+"newFiles"; 
			System.out.println(directoryName);
			File directory = new File(directoryName);
			System.out.println(directory.exists());
			if(directory.exists()){
				FileUtils.deleteDirectory(directory);
			}
		}catch(IOException ioex){
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
		list.add(BoxDataProvider.getUpdateCollaboratorDataProvider());
		return new Object[][] { { list } };  
	}
}