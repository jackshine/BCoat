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

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectAlertFilter;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.DropBox;
import com.universal.common.UniversalApi;

/**
 * @author Shri
 *
 */
public class DropboxPolicyViolationTests extends ProtectInitializeTests {


	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> folderCollection = new HashMap<String, String>();
	PolicyBean policyBean;
	DropBox dropbox;
	String externalUser = null;
	String externalUserToken = null;
	String internalUser = null;
	String internalUserToken = null;
	String rootFolder = "/PolicyViolations" + System.currentTimeMillis();
	ProtectAlertFilter protectAlertFilter;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		dropbox = universalApi.getDropbox();
		universalApi.createFolder(rootFolder);
		externalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppUsername");
		externalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppToken");
		internalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppUsername");
		internalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppToken");
		restClient = new Client();
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		File file;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				String policyName = policyBean.getPolicyName();
				
				if (policyBean.getFileFormat() == null || policyBean.getFileFormat() == "")
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
							policyBean.getFileExt());
				else
					file = protectFunctions.createDynamicFileFormat(ProtectTestConstants.PROTECT_RESOURCE_FILEFORMAT_PATH, new File(""), policyBean.getFileFormat());
				
				
				
				fileCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());
				Logger.info("FileName:"+file.getName());
				
				
				if (policyBean.getPolicyName().startsWith(ProtectTestConstants.FILE1)) 
					policyBean.setFileName(file.getName().substring(0, file.getName().indexOf(".")+1) + data[16]);
				
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);

				String childFolderName = "AA_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Dropbox : " + childFolderName, true);
				String childFolder = rootFolder + "/" + childFolderName;
				String internalFolder = "/Elastica QA Team Folder";
				folderCollection.put(policyName, childFolder);
				
				// check if disable tests and delete tests
				if (policyBean.getPolicyName().startsWith(ProtectTestConstants.DISABLE)) {
					protectFunctions.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
					protectFunctions.waitForMinutes(1);
				}
				else if (policyBean.getPolicyName().startsWith(ProtectTestConstants.DEL)) {
					protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
					protectFunctions.waitForMinutes(1);
				}
				
				if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.INTERNAL) || (policyBean.getExposureType().contains(ProtectTestConstants.INTERNAL) && policyBean.getSeverity().contains(ProtectTestConstants.INTERNAL)))
					universalApi.uploadFile(internalFolder, file.getAbsolutePath());
				else
					universalApi.uploadFile(childFolder, file.getAbsolutePath());
				
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}

		protectFunctions.waitForMinutes(3);
		shareFileInDropbox(arrList, fileCollection, folderCollection);
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "DropboxPolicyViolation", priority = 2)
	public void verifyPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		
		protectAlertFilter = new ProtectAlertFilter();
		protectAlertFilter.setPolicy(policyName);
		protectAlertFilter.setSearchFilter(true);

		// search by policy name
		String policyAlertsSearchResponse = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, protectAlertFilter, 50);
		int totalCount = Integer.parseInt(ClientUtil.getJSONValue(ClientUtil.getJSONValue(policyAlertsSearchResponse, ProtectTestConstants.HITS), ProtectTestConstants.TOTAL));
		Logger.info("Total number of policies found on policy name search:"+totalCount);
		// iterate the list and assert for service
		protectAlertFilter.setService(policyBean.getCloudService());
		assertFilters(policyAlertsSearchResponse, protectAlertFilter);
		
		if (policyBean.getPolicyName().startsWith(ProtectTestConstants.DISABLE) || policyBean.getPolicyName().startsWith(ProtectTestConstants.DEL) || policyBean.getPolicyName().startsWith(ProtectTestConstants.FILETYPEEXC) || policyBean.getPolicyName().startsWith("CIEXCEPPOL")) {
			String polViolLog = protectFunctions.getPolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
			
			if (polViolLog != null) {
				Logger.info("Policy Violation/ Alerts are not triggered for disabled/ delete/ CI Exception policy as expected");			
				Assert.assertTrue(false, "Policy is in DeActivated/ Delete state, but still got violated");
			} else
				Logger.info("Policy Violation/ Alerts are not triggered for disabled/ delete/ CI Exception policy as expected");
		} else 
			protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		
		
		
		Reporter.log("Completed testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
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

		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: deletePolicies - " + policyName, true);
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Deleted policy - " + policyName, true);
				Reporter.log("Completed testcase: deletePolicies - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
	}

	@AfterClass(alwaysRun = true)
	public void deleteFiles() throws Exception {
		String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
		File directory = new File(directoryName);
		System.out.println(directory.exists());
		if (directory.exists()) {
			FileUtils.deleteDirectory(directory);
		}
	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(DropboxDataProvider.getPolicyViolationsInputData());
		return new Object[][] { { list } };
	}

	/**
	 * 
	 * @param arrList
	 * @param fileCollection
	 * @param folderCollection
	 * @throws IOException
	 * @throws DbxException
	 * @throws InterruptedException
	 * @throws Exception
	 */
	private void shareFileInDropbox(List<Object> arrList, Map<String, String> fileCollection, Map<String, String> folderCollection)
			throws IOException, DbxException, InterruptedException, Exception {
		String exposureType = null;
		String exposureTypeTemp = null;
		String[] exposures = null;

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
							shareFileInDropbox(fileCollection.get(policyBean.getPolicyName()),
									folderCollection.get(policyBean.getPolicyName()), s);
						}
					}
				} else {
					String[] selExposures = policyBean.getSeverity().split(",");
					for (String s : selExposures) {
						shareFileInDropbox(fileCollection.get(policyBean.getPolicyName()),
								folderCollection.get(policyBean.getPolicyName()), s);
					}
				}

			} else {
				shareFileInDropbox(fileCollection.get(policyBean.getPolicyName()), folderCollection.get(policyBean.getPolicyName()),
						exposureType);
			}

		}

	}

	/**
	 * 
	 * @param file
	 * @param childFolder
	 * @param exposureType
	 * @throws Exception
	 * @throws IOException
	 * @throws DbxException
	 * @throws InterruptedException
	 */
	private void shareFileInDropbox(String fileName, String childFolder, String exposureType) throws Exception, IOException, DbxException,
			InterruptedException {
		if (exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
			Reporter.log("Creating a public link for the file - " + fileName, true);
			String createSharedLink = universalApi.createSharedLinkForFolder(childFolder + File.separator + fileName);
			System.out.println("Create shared Link: " + createSharedLink);
		}
		if (exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)) {
			dropbox.shareAndMountFolderToUser(childFolder, externalUser, DbxSharing.AccessLevel.editor, externalUserToken);
		}
		if (exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)) {
			//dropbox.shareAndMountFolderToUser(childFolder, internalUser, DbxSharing.AccessLevel.editor, internalUserToken);
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