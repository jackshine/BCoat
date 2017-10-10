
package com.elastica.beatle.tests.protect.accessmonitoring;

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
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupList;
import com.universal.dtos.box.Item;

/**
 * @author Mayur
 *
 */
public class BoxGroupOperationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	String filePath = "";
	public static String groupname = "QA Bangalore";
	public static String UpdatedGroupname = "Updated QA Bangalore";
	BoxFolder folder;
	Map<String, String> policyActualName = new HashMap<String, String>();
	public static Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	public static Map<String, BoxCollaboration> collaboratorCollection = new HashMap<String, BoxCollaboration>();

	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + folderName, true);
		folder = universalApi.createFolder(folderName);
		
		GroupList grouplist = universalApi.listGroup();
        for (BoxGroup group : grouplist.getEntries()) {
            Reporter.log(group.getName(), true);
            universalApi.deleteGroup(group);
        }
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
		PolicyBean policyBean = null;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
				String policyName = "P"+protectFunctions.generateAlphaNumericString(7);
				policyActualName.put(policyBean.getPolicyName(), policyName);
				policyBean.setPolicyName(policyName);
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				/*File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				Reporter.log("Check File created: "+String.valueOf(file.exists()), true);
				policyBean.setFileName(file.getName());*/
				protectFunctions.createAndActivateAccessEnforcementPolicy(restClient, policyBean, requestHeader, suiteData);
				protectFunctions.waitForMinutes(0.5);
				
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.setAccessEnforcementBoxOperations(policyBean, universalApi, null, folder);
		protectFunctions.waitForMinutes(5);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposure", priority = 2)
	public void verifyPolicyViolationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
		String policyName = policyActualName.get(policyBean.getPolicyName());
		policyBean.setPolicyName(policyName);
		String detailMessage = null;
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyPolicyViolationLogs - " + policyName, true);
		Map<String, String> policyViolationLogsMessage = protectFunctions.getAccessMonitoringPolicyViolationAlertLogDetails(restClient,
				policyBean, requestHeader, suiteData);
		protectFunctions.assertAccessMonitoringViolationPolicyLogs(policyViolationLogsMessage, policyBean, suiteData);
		if(policyBean.getActivityScope().contains("Create")){
			detailMessage = "User created group "+groupname;
		}else if(policyBean.getActivityScope().contains("Edit")){
			detailMessage = "User edited group "+UpdatedGroupname;
		}else if(policyBean.getActivityScope().contains("Delete")){
			detailMessage = "User deleted group "+UpdatedGroupname;
		}
		Assert.assertEquals(policyViolationLogsMessage.get(ProtectTestConstants.DETAILS), detailMessage);
		Reporter.log("Completed testcase: verifyPolicyViolationLogs - " + policyName, true);
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
				PolicyBean policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
				String policyName = policyActualName.get(policyBean.getPolicyName());
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
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		return new Object[][] {
		/*
		 * 1. Policy Name
		 * 2. Cloud Application
		 * 3. User
		 * 4. Group
		 * 5. Domain
		 * 6. User Exception
		 * 7. Group Exception
		 * 8. ThreatScore
		 * 9. ActivityScope
		 * 10. File Extension	
		 */
			new String[]{"AMGROUPBOX1", "Box", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "0", "Group:Create",    "txt"},
			new String[]{"AMGROUPBOX2", "Box", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "0", "Group:Edit",   "txt"},
			new String[]{"AMGROUPBOX3", "Box", "box-admin@protectautobeatle.com", "any", "any", "no", "no", "0", "Group:Delete", "txt"},
			

		};
	}
	
	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(getData());
		return new Object[][] { { list } };  
	}
}