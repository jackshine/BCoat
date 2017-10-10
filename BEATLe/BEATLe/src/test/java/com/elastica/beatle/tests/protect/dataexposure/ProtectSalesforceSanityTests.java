
package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
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
import com.universal.dtos.salesforce.FileShares;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.InternalFileShare;
import com.universal.util.OAuth20Token;

/**
 * @author Mayur
 *
 */
public class ProtectSalesforceSanityTests extends ProtectInitializeTests {

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
	String internalUserId;
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
		OAuth20Token tokenObj = sfapi.getTokenObject();
		instanceUrl = tokenObj.getInstanceUrl();
		Reporter.log("Token Id:" + tokenObj.getId(), true);
		instanceId = tokenObj.getId().split("/")[4];
		this.externalUserId1	= getRegressionSpecificSuitParameters("saasAppExternalUser1Name");
		this.externalUserId2	= getRegressionSpecificSuitParameters("saasAppExternalUser2Name");
		this.internalUserId	= getRegressionSpecificSuitParameters("saasAppEndUser1Name");
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
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
						policyBean.getFileExt());
				policyBean.setFileName(file.getName());
				fileCollection.put(policyBean.getPolicyName(), file);
				
				Logger.info("Uploading the file to salesforce chatter");
				ChatterFile chatterfile = sfapi.uploadFileToChatter(file.getAbsolutePath(), file.getName());
				fileIdCollection.put(policyName, chatterfile.getId());
				Logger.info("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ file.getName());
				protectFunctions.waitForMinutes(0.5);
				if(policyBean.getPolicyType().equals("shareFilePublic")){
					Logger.info("Share the file publicly.");
					sfapi.createFileShareLink(chatterfile.getId());
				}
				protectFunctions.createAndActivateDataExposurePolicyWithCIQ(restClient, policyBean, requestHeader, suiteData);
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(5);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "Sanity", priority = 2)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);		
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean);
		FileShares fileShares = sfapi.getFileShares(fileIdCollection.get(policyName));
		//Verify Remediation
		if(policyBean.getRemediationActivity().contains("deleteSharedLink")){
			Logger.info("==============================");
			Logger.info("Verify Sharing Removed: "+fileShares.getShares().size());
			Logger.info("==============================");
			Assert.assertTrue(fileShares.getShares().size()==1);
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
		list.add(SalesforceDataProvider.getSalesforceSanityDataProvider());
		return new Object[][] { { list } };  
	}
	
	
}