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

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonProcessingException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectAlertFilter;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.BoxDataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.FileUploadResponse;

/**
 * @author Shri
 *
 */

public class BoxPolicyViolationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();
	Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	PolicyBean policyBean;
	BoxFolder folder;
	ProtectAlertFilter protectAlertFilter;

	/**
	 * Test case Initialization
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		String uniqueId = UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
		folder = universalApi.createFolder("A_" + uniqueId);
		restClient = new Client();
	}

	/**
	 * This test method is to test data exposure policies(creation, activate)
	 * with various combinations for Box and perform file upload in Box SaaS
	 * application
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
		File file;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				
				if (policyBean.getFileFormat() == null)
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
				else
					file = protectFunctions.createDynamicFileFormat(ProtectTestConstants.PROTECT_RESOURCE_FILEFORMAT_PATH, new File(""), policyBean.getFileFormat());
				
				Reporter.log("Check File created: " + String.valueOf(file.exists()), true);
				fileCollection.put(policyBean.getPolicyName(), file);
				policyBean.setFileName(file.getName());
				// Create a policy
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);

				String childFolderName = "AA_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + childFolderName, true);
				BoxFolder childFolder = universalApi.createFolder(childFolderName, folder.getId());
				
				FileUploadResponse fileUploadResponse = universalApi.uploadFile(childFolder.getId(), file.getAbsolutePath(), file.getName());
				folderCollection.put(policyBean.getPolicyName(), childFolder);
				fileIdCollection.put(policyBean.getPolicyName(), fileUploadResponse.getFileId());
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);

			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}

		protectFunctions.waitForMinutes(5);
		shareFileInBox(arrList, fileCollection, folderCollection, fileIdCollection);
		protectFunctions.waitForMinutes(10);
	}

	/**
	 * This test method verify policies alert and remediation log population in
	 * elastica portal
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = BoxDataProvider.class, dataProvider = "BoxPolicyViolation", priority = 2)
	public void verifyDataExposurePolicyDisplayLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		
		protectAlertFilter = new ProtectAlertFilter();
		protectAlertFilter.setFileName(fileName);
		protectAlertFilter.setSearchFilter(true);

		// search by policy name
		String policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsSearchResponse, "hits"), "total"));
		Logger.info("Total number of policies found on policy name search:"+totalCount);
		// iterate the list and assert for service
		protectAlertFilter.setService(policyBean.getCloudService());
		assertFilters(policyAlertsSearchResponse, protectAlertFilter);
		
		Reporter.log("Starting testcase: verifyDataExposurePolicyViolationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		Reporter.log("Completed testcase: verifyDataExposurePolicyDisplayLogs - " + policyName, true);
	}

	/**
	 * This test method validates policy deletion action
	 * 
	 * @param data
	 * @throws Exception
	 */
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
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		universalApi.deleteFolder(folder.getId(), true, folder.getEtag());
	}

	/**
	 * Tear down method to delete files created as part of this test in SaaS app
	 * 
	 * @throws Exception
	 */
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

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {

		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(BoxDataProvider.getPolicyViolationsInputData());
		return new Object[][] { { list } };

	}
	
	/**
	 * 
	 * @param arrList
	 * @param fileCollection
	 * @param folderCollection
	 * @throws Exception
	 */
	private void shareFileInBox(List<Object> arrList, Map<String, File> fileCollection, Map<String, BoxFolder> folderCollection, Map<String, String> fileIdCollection)
			throws Exception {
		String exposureType = null;
		String exposureTypeTemp = null;
		String[] exposures = null;

		Iterator<Object> iterator = arrList.iterator();
		FileUploadResponse fileUploadResponse = new FileUploadResponse();
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
							fileUploadResponse.setFileId(fileIdCollection.get(policyBean.getPolicyName()));
							protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folderCollection.get(policyBean.getPolicyName()), s);

						}
					}
				} else {
					String[] selExposures = policyBean.getSeverity().split(",");
					for (String s : selExposures) {
						fileUploadResponse.setFileId(fileIdCollection.get(policyBean.getPolicyName()));
						protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folderCollection.get(policyBean.getPolicyName()), s);
					}
				}

			} else {
				fileUploadResponse.setFileId(fileIdCollection.get(policyBean.getPolicyName()));
				protectFunctions.shareFileOnBox(universalApi, fileUploadResponse, folderCollection.get(policyBean.getPolicyName()), exposureType);

			}
			//protectFunctions.waitForMinutes(0.5);
		}
	}
	
	/**
	 * 
	 * @param policyAlertsResponse
	 * @param protectAlertFilter
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void assertFilters(String policyAlertsResponse, ProtectAlertFilter protectAlertFilter) throws JsonProcessingException,
			IOException {

		String hits = ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, ProtectTestConstants.HITS), ProtectTestConstants.HITS);
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsResponse, ProtectTestConstants.HITS), ProtectTestConstants.TOTAL));
		int serviceCount = 0;
		int userCount = 0;
		int policyCount = 0;
		int policyTypeCount = 0;
		int severityCount = 0;

		if (jArray.size() == 0)
			Logger.info("No Alert combination is not found for this combination");

		for (int i = 0; i < jArray.size(); i++) {
			String source = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), ProtectTestConstants._SOURCE1);
			String service = ClientUtil.getJSONValue(source, ProtectTestConstants.FACILITY);
			if (protectAlertFilter.getService() != null) {
				serviceCount = serviceCount + 1;
				Logger.info("Actual:" + protectAlertFilter.getService());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, ProtectTestConstants.FACILITY));
				Assert.assertTrue(protectAlertFilter.getService().equals(service.substring(1, service.length() - 1)));
			}
			if (protectAlertFilter.getUser() != null) {
				userCount = userCount + 1;
				String user = ClientUtil.getJSONValue(source, ProtectTestConstants.USER);
				Logger.info("Actual:" + protectAlertFilter.getUser());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, ProtectTestConstants.USER));
				Assert.assertTrue(protectAlertFilter.getUser().equals(user.substring(1, user.length() - 1)));
			}
			if (protectAlertFilter.getPolicy() != null) {
				policyCount = policyCount + 1;
				String policy = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_VIOLATED);
				Logger.info("Actual:" + protectAlertFilter.getPolicy());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_VIOLATED));
				Assert.assertTrue(protectAlertFilter.getPolicy().equals(policy.substring(1, policy.length() - 1)));
			}
			if (protectAlertFilter.getPolicyType() != null) {
				policyTypeCount = policyTypeCount + 1;
				String policyType = ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_TYPE);
				Logger.info("Actual:" + protectAlertFilter.getPolicyType());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, ProtectTestConstants.POLICY_TYPE));
				Assert.assertTrue(protectAlertFilter.getPolicyType().equals(policyType.substring(1, policyType.length() - 1)));
			}
			if (protectAlertFilter.getSeverity() != null) {
				severityCount = severityCount + 1;
				String severity = ClientUtil.getJSONValue(source, ProtectTestConstants.SEVERITY);
				Logger.info("Actual:" + protectAlertFilter.getSeverity());
				Logger.info("Expected:" + ClientUtil.getJSONValue(source, ProtectTestConstants.SEVERITY));
				Assert.assertTrue(protectAlertFilter.getSeverity().equals(severity.substring(1, severity.length() - 1)));
			}

		}

		if (serviceCount > 0){
			Logger.info("Service Count:"+serviceCount);
			Logger.info("Display Total Service Count:"+totalCount);
			Assert.assertTrue(serviceCount == totalCount);
		}
		if (userCount > 0){
			Logger.info("User Count:"+userCount);
			Logger.info("Display Total Service Count:"+totalCount);
			Assert.assertTrue(userCount == totalCount);
		}
		if (policyCount > 0){
			Logger.info("Policy Count:"+policyCount);
			Logger.info("Display Total Service Count:"+totalCount);
			Assert.assertTrue(policyCount == totalCount);
		}
		if (policyTypeCount > 0){
			Logger.info("Policy Type Count:"+policyTypeCount);
			Logger.info("Display Total Service Count:"+totalCount);
			Assert.assertTrue(policyTypeCount == totalCount);
		}
		if (severityCount > 0){
			Logger.info("Severity Count:"+severityCount);
			Logger.info("Display Total Service Count:"+totalCount);
			Assert.assertTrue(severityCount == totalCount);
		}

	}

}