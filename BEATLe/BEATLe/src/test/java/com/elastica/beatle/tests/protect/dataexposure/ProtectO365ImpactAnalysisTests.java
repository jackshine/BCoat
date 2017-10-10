package com.elastica.beatle.tests.protect.dataexposure;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.protect.dataprovider.O365DataProvider;
import com.universal.common.UniversalApi;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;

/**
 * 
 * @author Shri
 *
 */

public class ProtectO365ImpactAnalysisTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
	String folderId; Folder folder;


	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
		String folderName = "A_" + UUID.randomUUID().toString();
		Reporter.log("Creating a folder in One Drive : " + folderName, true);
		folder = universalApi.createFolder(folderName);
	}

	/**
	 * This test method creates policy and uploads file
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
				policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				fileCollection.put(policyName, file);
				policyBean.setFileName(file.getName());
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				folderId = folder.getId();
				ItemResource itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
				protectFunctions.waitForMinutes(1);
				if(!policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.UNEXPOSED)){
					protectFunctions.shareFileInODrive(universalApi, itemResourse, policyBean.getExposureType());
				}
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		protectFunctions.waitForMinutes(15);

	}

	/**
	 * This test method verifies impact analysis for all policies uploaded
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "PolicyImpactData", priority = 2)	
	public void testPolicyImpactAnalysis(String... data) throws Exception {
		Logger.info("###########################################################################################################################");
		Logger.info("This test method calculates and verifies the impact details for the policies created  ");
		Logger.info("###########################################################################################################################");

		// Populate policy Bean
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		policyBean.setPolicyName(policyName);
		suiteData.setSaasApp(policyBean.getCloudService());
		
		// Impact Analysis api call
		HttpResponse response = protectFunctions.calculateImpact(restClient, requestHeader, suiteData, policyBean);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Impact Analysis Response......:" + responseBody);
		String identification = protectFunctions.verifyImpactDetails(policyBean, fileName, responseBody);

		// Validate External Collaborator
		if (policyBean.getRemediationActivity().equalsIgnoreCase("extcollab")) {
			// Get Collaborator details
			HttpResponse collabResponse = protectFunctions
					.getCollabDetailsForImpact(restClient, requestHeader, suiteData, policyBean, identification);

			// Asserting Impact Details pop up
			String collabResponseBody = ClientUtil.getResponseBody(collabResponse);
			Logger.info("Collaborator Response......:" + collabResponseBody);
			String detailCollabs = protectFunctions.assertCollaboratorDetails(policyBean, collabResponseBody);
			Logger.info("identification......:" + ClientUtil.getJSONValue(detailCollabs, "identification"));
			//Logger.info("name................:" + ClientUtil.getJSONValue(detailCollabs, "name"));
			Logger.info("Actual Collaborator Identification:"+identification);
			Logger.info("Expected Collaborator Identification:"+ClientUtil.getJSONValue(detailCollabs, "identification"));
			Assert.assertEquals(identification, ClientUtil.getJSONValue(detailCollabs, "identification"), "Actual and Expected Collaborator Identificatino doesn't match");
			Logger.info("Actual and Expected Collaborator Identification matches as expected");
		}

	}



	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 3)
	public void testDeletePolicy(List<String[]> list) throws Exception {
		Logger.info("###########################################################################################################################");
		Logger.info("This test method deletes all policies created for impact analysis tests ");
		Logger.info("###########################################################################################################################");
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
				Reporter.log("Completed testcase: testDeletePolicy - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
	}


	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(O365DataProvider.getPolicyImpactData());
		return new Object[][] { { list } };
	}


}
