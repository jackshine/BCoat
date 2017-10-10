package com.elastica.beatle.tests.protect.policyviolations;

import java.io.File;
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
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.i18n.I18N;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
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
public class PolicyI18NTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	DCIFunctions dciFunctions = new DCIFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> policyMap = new HashMap<String, String>();
	PolicyBean policyBean;
	DropBox dropbox;
	String rootFolder = "/PolicyViolations" + System.currentTimeMillis();

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		dropbox = universalApi.getDropbox();
		universalApi.createFolder(rootFolder);
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
		File file; String locale; String oldPolicyName; String newPolicyName;
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				oldPolicyName = policyBean.getPolicyName();
				locale = oldPolicyName.substring(oldPolicyName.indexOf("_") + 1);
				file = createDynamicFile(policyBean, oldPolicyName, locale.toLowerCase());
				newPolicyName = setPolicyNameAndDesc(policyBean, oldPolicyName, locale.toLowerCase());
				
				fileCollection.put(newPolicyName, file.getName());
				policyMap.put(oldPolicyName, newPolicyName);
				policyBean.setFileName(file.getName());
				Logger.info("FileName:"+file.getName());
				
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);

				String childFolderName = "AA_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Dropbox : " + childFolderName, true);
				String childFolder = rootFolder + "/" + childFolderName;
				universalApi.uploadFile(childFolder, file.getAbsolutePath());
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + oldPolicyName, true);
			} catch (Exception e) {
				Logger.info("Error occured on Policy Creation");
				continue;
			} catch (Error e) {
				continue;
			}
		}

		protectFunctions.waitForMinutes(10);
	}



	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "PolicyI18NData", priority = 2)
	public void verifyPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyMap.get(policyBean.getPolicyName());
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		

		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);

		
		if (policyBean.getRemediationActivity().contains("delete")) {
			Reporter.log("Verifing the delete file remediation logs", true);
			String trashMessage = "User trashed " + fileName;
			Map<String, String> trashLogMessage = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName,  requestHeader, suiteData, trashMessage);
			
			if (trashLogMessage.get(ProtectTestConstants.MESSAGE) == null) {
				Logger.info("Remediation Logs is not found in the portal, hence test case failed");
				protectFunctions.checkSplunkLogs(suiteData, fileName, false);
				Assert.assertTrue(false);
			}
			
			Reporter.log(policyName+" - "+trashLogMessage, true);
			protectFunctions.assertRemediation(trashLogMessage, ProtectTestConstants.WARNING, policyBean.getCloudService(), fileName, ProtectTestConstants.TRASH, trashMessage);
			Reporter.log("Verified the file trash activity parameters - " + trashLogMessage, true);
			Reporter.log("Verifing the file trash activity on SaaS App", true);
			Map foldersItems = universalApi.getFoldersItems(File.separator+"FileTransfer", 0, 0);
		    boolean containsKey = foldersItems.containsKey(fileName);
		    Assert.assertTrue(!containsKey);
		}		
		
		
		
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
		list.add(DropboxDataProvider.getI18NInputData());
		return new Object[][] { { list } };
	}


	
	
	
	private String setPolicyNameAndDesc(PolicyBean policyBean, String policyName, String locale) {
		String policyDesc = policyBean.getPolicyDesc();
		if (policyName.startsWith("I18NPOLNAME")){
			if (locale.equals("cn"))
				policyName = policyName+"_"+I18N.getString("language", "zh_cn");
			else if (locale.equals("es"))
				policyName = policyName+"_"+I18N.getString("language", "es_es");
			policyBean.setPolicyName(policyName);
		}
		

		if (policyName.startsWith("I18NPOLDESC")){
			if (locale.equals("cn"))			
				policyDesc = I18N.getString("language", "zh_cn");
			else if (locale.equals("es"))
				policyDesc = I18N.getString("language", "es_es");
			
			policyBean.setPolicyDesc(policyDesc);
		}
		//policyMap.put(policyBean.getPolicyName(), policyName);
		return policyName;
	}

	private File createDynamicFile(PolicyBean policyBean, String policyName, String locale) {
		File file;
		if (policyName.startsWith("I18NFILENAME")) {
			if (locale.equals("cn"))
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File("f"+protectFunctions.generateAlphaNumericString(5)+ I18N.getString("language", "zh_cn")),
							policyBean.getFileExt());
			else if (locale.equals("es"))
					file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File("f"+protectFunctions.generateAlphaNumericString(5)+ I18N.getString("language", "es_es")),
							policyBean.getFileExt());
			else
				file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
		}
		else
			file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
					policyBean.getFileExt());
		return file;
	}	
}