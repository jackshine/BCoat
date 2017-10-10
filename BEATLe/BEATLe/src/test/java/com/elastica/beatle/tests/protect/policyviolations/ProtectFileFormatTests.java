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
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.DropBox;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;

/**
 * @author Shri
 *
 */
public class ProtectFileFormatTests extends ProtectInitializeTests {

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
	BoxFolder folder;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData, "Box");
		String uniqueId = UUID.randomUUID().toString();
		Reporter.log("Creating a folder in Box : " + "A_" + uniqueId, true);
		folder = universalApi.createFolder("A_" + uniqueId);
		//dropbox = universalApi.getDropbox();
		//universalApi.createFolder(rootFolder);
		/*externalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppUsername");
		externalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("externalSaasAppToken");
		internalUser = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppUsername");
		internalUserToken = ProtectInitializeTests.getRegressionSpecificSuitParameters("internalSaasAppToken");*/
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
				//protectFunctions.createContentIQProfileForFileFormat(restClient, requestHeader, suiteData, fileFormat);
				
				
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				String policyName = policyBean.getPolicyName();
				
				file = protectFunctions.createDynamicFileFormat(ProtectTestConstants.PROTECT_RESOURCE_FILEFORMAT_PATH, new File(""),
							policyBean.getFileFormat());
				
				
				fileCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());


				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);


				String childFolderName = "AA_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in Box : " + childFolderName, true);
				BoxFolder childFolder = universalApi.createFolder(childFolderName, folder.getId());
				
				universalApi.uploadFile(childFolder.getId(), file.getAbsolutePath(), file.getName());
				
				Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}

		//protectFunctions.waitForMinutes(5);
		//shareFileInDropbox(arrList, fileCollection, folderCollection);
		//protectFunctions.waitForMinutes(10);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "FileFormat", priority = 2)
	public void verifyPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: verifyPolicyViolationAndRemediationLogs - " + policyName, true);
		Reporter.log("Starting testcase: file name - " + fileName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		
		if (policyBean.getPolicyName().startsWith("DISABLE") || policyBean.getPolicyName().startsWith("DEL")) {
			String polViolLog = protectFunctions.getPolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
			
			if (polViolLog != null) {
				Logger.info("Policy Violation/ Alerts are not triggered for disabled/ delete policy as expected");			
				Assert.assertTrue(false, "Policy is in DeActivated/ Delete state, but still got violated");
			} else
				Logger.info("Policy Violation/ Alerts are not triggered for disabled/ delete policy as expected");
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
		//list.add(DropboxDataProvider.getFileFormatData());
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

				if (policyBean.getExposureType().contains("all")) {
					for (String s : exposures) {
						if (!s.equalsIgnoreCase("all")) {
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
		if (exposureType.equalsIgnoreCase("public")) {
			Reporter.log("Creating a public link for the file - " + fileName, true);
			String createSharedLink = universalApi.createSharedLinkForFolder(childFolder + File.separator + fileName);
			System.out.println("Create shared Link: " + createSharedLink);
		}
		if (exposureType.equalsIgnoreCase("external")) {
			dropbox.shareAndMountFolderToUser(childFolder, externalUser, DbxSharing.AccessLevel.editor, externalUserToken);
		}
		if (exposureType.equalsIgnoreCase("internal")) {
			//dropbox.shareAndMountFolderToUser(childFolder, internalUser, DbxSharing.AccessLevel.editor, internalUserToken);
		}
	}
}