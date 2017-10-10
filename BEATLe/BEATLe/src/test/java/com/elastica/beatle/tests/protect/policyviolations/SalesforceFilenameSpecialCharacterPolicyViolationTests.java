
package com.elastica.beatle.tests.protect.policyviolations;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.elastica.beatle.protect.dataprovider.SalesforceDataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.InternalFileShare;
import com.universal.util.OAuth20Token;

import groovy.json.internal.ArrayUtils;
import junit.framework.Assert;

/**
 * @author Mayur
 *
 */
public class SalesforceFilenameSpecialCharacterPolicyViolationTests extends ProtectInitializeTests {

	String saasAppUsername;
	String saasAppUser;
	String instanceUrl;
	String instanceId;
	Salesforce sfapi;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>(); 
	Map<String, String> fileIdCollection = new HashMap<String, String>();
	UniversalApi universalApi;
	String externalUserId1;
	String externalUserId2;
	String internalUserId1;
	String internalUserId2;
	Client restClient;
	SecurletUtils securletUtils;

	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		securletUtils = new SecurletUtils();
		sfapi = protectFunctions.salesforceLogin(suiteData);
		this.saasAppUsername 	= getRegressionSpecificSuitParameters("saasAppUsername");
		if(saasAppUsername.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUsername.toLowerCase()).replace(".protectsb", "");
		} else {
			saasAppUser = saasAppUsername;
		}
		System.out.println(suiteData.getSaasAppClientId());
		System.out.println(suiteData.getSaasAppClientSecret());
		System.out.println(suiteData.getSaasAppToken());
		OAuth20Token tokenObj = sfapi.getTokenObject();
		instanceUrl = tokenObj.getInstanceUrl();
		Reporter.log("Token Id:" + tokenObj.getId(), true);
		instanceId = tokenObj.getId().split("/")[4];
		this.externalUserId1	= getRegressionSpecificSuitParameters("saasAppExternalUser1Name");
		this.externalUserId2	= getRegressionSpecificSuitParameters("saasAppExternalUser2Name");
		this.internalUserId1	= getRegressionSpecificSuitParameters("saasAppEndUser1Name");
		this.internalUserId2	= getRegressionSpecificSuitParameters("saasAppEndUser2Name");
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
				data = Arrays.copyOf(data, 21);
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				if(data[16] != "no"){
					policyBean.setFileFormat(data[16]);
				}
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyName, true);
				File file = protectFunctions.createSpecialCharacterFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				fileCollection.put(policyBean.getPolicyName(), file);
				
				Logger.info("Uploading the file to salesforce chatter");
				ChatterFile chatterfile = sfapi.uploadFileToChatter(file.getAbsolutePath(), file.getName());
				fileIdCollection.put(policyName, chatterfile.getId());
				Logger.info("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ file.getName());
				
				if(policyBean.getPolicyType().contains("shareFilePublic")){
					Logger.info("=======================================");
					Logger.info("Share the file publicly.");
					sfapi.createFileShareLink(chatterfile.getId());
					Logger.info("=======================================");
				}
				protectFunctions.createAndActivateDataExposurePolicyWithCIQ(restClient, policyBean, requestHeader, suiteData);
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
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "SpecialCharacter", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);		
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
 		Map<String, String> shareDisplayLogsMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient,
 				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		Map<String, String> ciDisplayLogsMessage = protectFunctions.getSecurletCIPolicyViolationLogs(restClient,
				policyBean.getCloudService(), fileName, policyName, requestHeader, suiteData);
		if(!shareDisplayLogsMessage.isEmpty()){
			protectFunctions.assertCIPolicyViolation(shareDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);
		} else if(!ciDisplayLogsMessage.isEmpty()){
			protectFunctions.assertCIPolicyViolation(ciDisplayLogsMessage, policyBean.getCloudService(), fileName, policyName);
		} else{
			Assert.assertNotNull(shareDisplayLogsMessage);
		}
		
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
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				sfapi.deleteFile(fileIdCollection.get(policyBean.getPolicyName()));
				String policyName = policyBean.getPolicyName();
				protectFunctions.deactivateAndDeletePolicy(restClient, policyName, requestHeader, suiteData);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
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
		list.add(SalesforceDataProvider.getSalesforceSpecialCharacterPolicyViolationsDataProvider());
		return new Object[][] { { list } };  
	}
	
	
}