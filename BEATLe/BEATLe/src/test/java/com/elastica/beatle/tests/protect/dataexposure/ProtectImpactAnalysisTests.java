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

import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonProcessingException;
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
import com.elastica.beatle.protect.dataprovider.DropboxDataProvider;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.FileUploadResponse;

/**
 * 
 * @author Shri
 *
 */

public class ProtectImpactAnalysisTests extends ProtectInitializeTests {

	UniversalApi boxUniversalApi;
	UniversalApi dropboxUniversalApi;
	UniversalApi gDriveUniversalApi;
	Client restClient;
	GDrive gDrive;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, BoxFolder> folderCollection = new HashMap<String, BoxFolder>();
	Map<String, String> gDriveFolderCollection = new HashMap<String, String>();
	Map<String, String> gDriveFileCollection = new HashMap<String, String>();
	Map<String, String> gDriveFileIdCollection = new HashMap<String, String>();


	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		UserAccount userAccount = new UserAccount("box-admin@protectautobeatle.com", "f)XCj13#B5rpEd*", "ADMIN");
		boxUniversalApi = new UniversalApi("Box", userAccount);
		userAccount = new UserAccount("protectauto@protectautobeatle.com", 
				"Elastica@123", "ADMIN", "tV4pHsJqX6AAAAAAAAAAB_8lVhp_Q1h8pnIOw1Dsm1Fpa6mnr7UFIqrEIeXnupqc");
		dropboxUniversalApi = new UniversalApi("DROPBOX", userAccount);
        String CLIENT_SECRET="upyz2s3goHfiS2-2isF6fj49";
        String CLIENT_ID="26371699126-2jkp5fppcq5oorcb03lutpu5d4mvkoir.apps.googleusercontent.com";
        String refreshToken="1/giL31lQiW2Bvx0LPf_ZsX4ZgfXgG8IdcwcrlVbQ7msg";
        Logger.info("Clent id: "+CLIENT_ID);
        GDriveAuthorization gDriveAuthorization = new GDriveAuthorization(CLIENT_ID, CLIENT_SECRET);
        String newAccessToken=gDriveAuthorization.getAceessTokenFromRefreshAccessToken(refreshToken);
		userAccount = new UserAccount("admin@protectautobeatle.com", "#qGZ9pWbiMNN&xk", "ADMIN",newAccessToken);
		gDriveUniversalApi = new UniversalApi("GDRIVE", userAccount);
	    gDrive = gDriveUniversalApi.getgDrive();
		
	}

	/**
	 * This test method creates policy and uploads file
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void testCreatePolicyAndUploadFile(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		Logger.info("###########################################################################################################################");
		Logger.info("This test method creates policies and performs corresponding SaaS app operations ");
		Logger.info("###########################################################################################################################");
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
				fileCollection.put(policyName, file.getName());
				policyBean.setFileName(file.getName());

				performSaaSOperation(policyBean, file);
				createDataExposurePolicy(policyBean);
				if (policyBean.getPolicyName().equalsIgnoreCase(ProtectTestConstants.DEACTIVATEDPOL))
					protectFunctions.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Completed testcase: testCreatePolicyAndUploadFile - " + policyName, true);
			} catch (Exception e) {
				continue;
			} catch (Error e) {
				continue;
			}
		}
		Thread.sleep(1000 * 60 * 25);

	}	

	/**
	 * This test method verifies impact analysis for all policies uploaded
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProviderClass = DropboxDataProvider.class, dataProvider = "PolicyImpactData", priority = 2)
	public void testPolicyImpactAnalysis(String... data) throws Exception {
		Logger.info("###########################################################################################################################");
		Logger.info("This test method calculates and verifies the impact details for the policies created  ");
		Logger.info("###########################################################################################################################");

		// Populate policy Bean
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		policyBean.setFileName(fileName);
		policyBean.setPolicyName(policyName);
		suiteData.setSaasApp(policyBean.getCloudService());
		
		// Impact Analysis api call
		HttpResponse response = protectFunctions.calculateImpact(restClient, requestHeader, suiteData, policyBean);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Impact Analysis Response......:" + responseBody);
		String identification = protectFunctions.verifyImpactDetails(policyBean, fileName, responseBody);

		// Validate External/ Internal Collaborator Details
		if (policyBean.getRemediationActivity().endsWith(ProtectTestConstants.COLLAB) && identification != null) {
			// Get Collaborator details
			HttpResponse collabResponse = protectFunctions
					.getCollabDetailsForImpact(restClient, requestHeader, suiteData, policyBean, identification);

			// Asserting Impact Details pop up
			String collabResponseBody = ClientUtil.getResponseBody(collabResponse);
			Logger.info("Collaborator Response......:" + collabResponseBody);
			String detailCollabs = protectFunctions.assertCollaboratorDetails(policyBean, collabResponseBody);
			Logger.info("identification......:" + ClientUtil.getJSONValue(detailCollabs, ProtectTestConstants.IDENTIFICATION));
			//Logger.info("name................:" + ClientUtil.getJSONValue(detailCollabs, ProtectTestConstants.NAME));
			Logger.info("Actual Collaborator Identification:"+identification);
			Logger.info("Expected Collaborator Identification:"+ClientUtil.getJSONValue(detailCollabs, "identification"));
			Assert.assertEquals(identification, ClientUtil.getJSONValue(detailCollabs, ProtectTestConstants.IDENTIFICATION), "Actual and Expected Collaborator Identificatino doesn't match");
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
	 * @param policyBean
	 * @param file
	 * @throws Exception
	 */
	private void performSaaSOperation(PolicyBean policyBean, File file) throws Exception {
		String policyName = policyBean.getPolicyName();
		if (policyBean.getCloudService().equalsIgnoreCase(ProtectTestConstants.DROPBOX)) {
			suiteData.setSaasApp(ProtectTestConstants.DROPBOX);
			FileUploadResponse fileUploadResponse = dropboxUniversalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
			if (fileUploadResponse.getFileId() == null) {
				Reporter.log("File not uploaded, again uploading a file");
				String filename = file.getName().substring(0, file.getName().indexOf("."));
				file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename), policyBean.getFileExt());
				dropboxUniversalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
				policyBean.setFileName(file.getName());
			}
			Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
			if (policyBean.getExposureType().equalsIgnoreCase(ProtectTestConstants.PUBLIC)) {
				Reporter.log("Creating a public link for the file - " + file.getName(), true);
				String createSharedLink = dropboxUniversalApi.createSharedLinkForFolder("/FileTransfer" + File.separator + file.getName());
				System.out.println("Create shared Link: " + createSharedLink);
			}
		} else if (policyBean.getCloudService().equalsIgnoreCase(ProtectTestConstants.BOX)) {
			suiteData.setSaasApp(ProtectTestConstants.BOX);
			String folderName = "A_" + UUID.randomUUID().toString();
			Reporter.log("Creating a folder in Box : " + folderName, true);
			BoxFolder folder = boxUniversalApi.createFolder(folderName);
			folderCollection.put(policyName, folder);
			FileUploadResponse fileUploadResponse = boxUniversalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
			Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
			
			if (policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL)) {
				protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, ProtectTestConstants.EXTERNAL);
				protectFunctions.waitForMinutes(1);
			}
        	if (policyBean.getExposureType().equals(ProtectTestConstants.PUBLIC)) {
        		Logger.info("Sharing file in Box as public ");
        		protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, ProtectTestConstants.PUBLIC);
        	}
        	if (policyBean.getExposureType().equals(ProtectTestConstants.INTERNAL)) {
        		Logger.info("Sharing file in Box as internal ");
        		protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, ProtectTestConstants.INTERNAL);
        	}
			
		} else if (policyBean.getCloudService().equalsIgnoreCase(ProtectTestConstants.GOOGLE_DRIVE)) {
			String uniqueId = UUID.randomUUID().toString();
			String folderId = gDriveUniversalApi.createFolder("A_" + uniqueId);
			gDriveFolderCollection.put(policyName, folderId);
			gDriveFileCollection.put(policyName, file.getName());
			FileUploadResponse fileUploadResponse = gDriveUniversalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
			protectFunctions.waitForMinutes(0.5);
			gDriveFileIdCollection.put(policyName, fileUploadResponse.getFileId());

			if(policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), ProtectTestConstants.EXT_USER, ProtectTestConstants.USER, policyBean.getPolicyType());
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.INTERNAL)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), suiteData.getDomainName(), ProtectTestConstants.DOMAIN1, policyBean.getPolicyType());
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, ProtectTestConstants.ANYONE, policyBean.getPolicyType());
			}
			
		}
	}

	/**
	 * 
	 * @param policyBean
	 * @throws Exception
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private void createDataExposurePolicy(PolicyBean policyBean) throws Exception, JsonProcessingException, IOException {
		String policyName = policyBean.getPolicyName();
		if (protectFunctions.isPolicyExists(restClient, policyName, requestHeader, suiteData)) {
			protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
		}
		protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposureList")
	public Object[][] getDataList() {
		List<Object[][]> list = new ArrayList<Object[][]>();
		list.add(DropboxDataProvider.getPolicyImpactData());
		return new Object[][] { { list } };
	}

}