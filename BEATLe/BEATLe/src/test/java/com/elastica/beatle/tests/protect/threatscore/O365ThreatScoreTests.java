package com.elastica.beatle.tests.protect.threatscore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.Folder;


public class O365ThreatScoreTests extends ProtectInitializeTests {
	
	UniversalApi universalApi;
	Client restClient;
	String folderId;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	PolicyBean policyBean = new PolicyBean();
	Map<String, String> policyActualName = new HashMap<String, String>();

	
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
	@Test(dataProvider = "ThreatScoreList", priority = 1)
	public void testThreatScorePolicy(List<String[]> list) throws Exception {
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				// Create a policy for Threat Score
				String[] data = (String[]) iterator.next();
				String policyName = "P"+protectFunctions.generateAlphaNumericString(7);
				policyBean = protectFunctions.setThreatScorePolicyData(data);
				policyActualName.put(policyBean.getPolicyName(), policyName);
				policyBean.setPolicyName(policyName);
				protectFunctions.createAndActivateThreatScoreBasedPolicy(restClient, requestHeader, suiteData, policyBean);
				policyBean = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
				"exe");
		String folderName = "A_" + UUID.randomUUID().toString();
		
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		Folder folder = universalApi.createFolder(folderName);
		universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
		protectFunctions.waitForMinutes(5);
	}
	
	@Test(dataProvider = "ThreatScore", priority = 2)
	public void verifyPolicyViolationForThreatScore(String... data) throws Exception{
		policyBean = protectFunctions.setThreatScorePolicyData(data);
		policyBean.setPolicyName(policyActualName.get(policyBean.getPolicyName()));
		Map<String, String> policyViolationLogDetails = protectFunctions.getThreatScorePolicyViolationAlertLogDetails(restClient, policyBean, requestHeader, suiteData);
		protectFunctions.assertThreatScorePolicyViolation(policyViolationLogDetails, policyBean, suiteData);
	}
	
	@Test(dataProvider = "ThreatScoreList", priority = 3)
	public void clearBlock(List<String[]> list) throws Exception{
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			String[] data = (String[]) iterator.next();
			policyBean = protectFunctions.setThreatScorePolicyData(data);
			policyBean.setPolicyName(policyActualName.get(policyBean.getPolicyName()));
			Map<String, String> blockDetails = protectFunctions.getProtectBlockDetails(restClient, policyBean, requestHeader, suiteData);
			protectFunctions.clearBlock(restClient, policyBean, requestHeader, suiteData, blockDetails);
			break;
		}
	}
	
	@Test(dataProvider = "ThreatScoreList", priority = 4)
	public void deletePolicies(List<String[]> list){
		System.out.println("Input data printing...." + list.size());
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.setThreatScorePolicyData(data);
				policyBean.setPolicyName(policyActualName.get(policyBean.getPolicyName()));
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
	}
	
	/**
	 * List for all the policies
	 * @return
	 */
	@DataProvider(name = "ThreatScore")
	public Object[][] getData() {
		return new Object[][] {

			new String[] { "TSO365POLICY1", "Office 365", "any", 						 "Any", "Any", "testuser1@protecto365autobeatle.com", "No", "5", "BLOCK_SERVICE"},
			new String[] { "TSO365POLICY2", "Office 365", "admin@protecto365autobeatle.com", "Any", "Any", "testuser1@protecto365autobeatle.com", "No", "0",  "BLOCK_SERVICE"},
			new String[] { "TSO365POLICY3", "Office 365", "admin@protecto365autobeatle.com", "Any", "Any", "no",   						     "No", "5", "BLOCK_SERVICE"},			
			
		};
	}
	
	/**
	 * DataProvider to get the list of policies
	 * @return
	 */
	@DataProvider(name = "ThreatScoreList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>(); 
		list.add(getData());
		return new Object[][] { { list } };  
	}

}
