package com.elastica.beatle.tests.protect.miscellaneous;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.http.NameValuePair;
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
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.FileUploadResponse;

/**
 * 
 * @author Shri
 *
 */

public class ProtectAlertFilterTests extends ProtectInitializeTests {

	UniversalApi boxUniversalApi; UniversalApi dropboxUniversalApi; UniversalApi gDriveUniversalApi;
	Client restClient; GDrive gDrive;
	ProtectFunctions protectFunctions;
	
	HashMap<String, String> preFacilityMap = null; HashMap<String, String> prePolicyTypeMap = null;
	HashMap<String, String> preUserMap = null; HashMap<String, String> prePolicyViolatedMap = null;
	HashMap<String, String> preSeverityMap = null;
	
	HashMap<String, String> postFacilityMap = null; HashMap<String, String> postPolicyTypeMap = null;
	HashMap<String, String> postUserMap = null; HashMap<String, String> postPolicyViolatedMap = null;
	HashMap<String, String> postSeverityMap = null;
	

	@BeforeClass
	public void init() throws Exception {
		restClient = new Client();
		protectFunctions = new ProtectFunctions();
		boxUniversalApi = protectFunctions.loginToApp(suiteData, "Box");
		dropboxUniversalApi = protectFunctions.loginToApp(suiteData, "Dropbox");
		gDriveUniversalApi = protectFunctions.loginToApp(suiteData, "Google Drive");
	    gDrive = gDriveUniversalApi.getgDrive();
	}

	/**
	 * This test method creates policy and uploads file
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 1)
	public void populateData(List<String[]> list) throws Exception {

		protectFunctions.deleteAllPolicies(restClient, requestHeader, suiteData);
		protectFunctions.waitForMinutes(2);
		PolicyBean policyBean = null;

		
		String responseBody = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, null, 30);
		while (responseBody.startsWith("Error")) {
			responseBody = protectFunctions.getPolicyAlerts(restClient, requestHeader, suiteData, null, 30);
		}
		
		// facets
		String facets = ClientUtil.getJSONValue(responseBody, "facets");	
		String facility = ClientUtil.getJSONValue(facets, "facility");
		String policyType = ClientUtil.getJSONValue(facets, "policy_type");
		String user = ClientUtil.getJSONValue(facets, "user");
		String policyViolated = ClientUtil.getJSONValue(facets, "_PolicyViolated");
		String severity = ClientUtil.getJSONValue(facets, "severity");
		
		// populate map
		preFacilityMap = populateAlertFilterDtlsMap(facility);
		prePolicyTypeMap = populateAlertFilterDtlsMap(policyType);
		preUserMap = populateAlertFilterDtlsMap(user);
		prePolicyViolatedMap = populateAlertFilterDtlsMap(policyViolated);
		preSeverityMap = populateAlertFilterDtlsMap(severity);
		
		// populate policies and alerts
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				if (data[0].startsWith("DATA_EXP")) {
					policyBean = protectFunctions.populatePolicyBean(data);
					Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
					File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""), policyBean.getFileExt());
					//fileCollection.put(policyName, file.getName());
					policyBean.setFileName(file.getName());
					performSaaSOperation(policyBean, file);
					createDataExposurePolicy(policyBean);
				} else if (data[0].startsWith("ACC_MON")) {
					policyBean = protectFunctions.setAccessEnforcementPolicyData(data);
					//String policyName = "P"+protectFunctions.generateAlphaNumericString(7);
					policyBean.setPolicyName(policyBean.getPolicyName());
					Reporter.log("Starting testcase: testCreatePolicyAndUploadFile - " + policyBean.getPolicyName(), true);
					File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(""),
							policyBean.getOptionalField());
					Reporter.log("Check File created: "+String.valueOf(file.exists()), true);
					policyBean.setFileName(file.getName());
					String folderName = "A_" + UUID.randomUUID().toString();
					Reporter.log("Creating a folder in Box : " + folderName, true);
					BoxFolder folder = boxUniversalApi.createFolder(folderName);
					//fileCollection.put(policyBean.getPolicyName(), file.getName());
					protectFunctions.createAndActivateAccessEnforcementPolicy(restClient, policyBean, requestHeader, suiteData);
					protectFunctions.waitForMinutes(0.5);
					protectFunctions.setAccessEnforcementBoxOperations(policyBean, boxUniversalApi, file, folder);
					
				} else if (data[0].startsWith("THREAT_SCORE")) {
					policyBean = protectFunctions.setThreatScorePolicyData(data);
					policyBean.setPolicyName(policyBean.getPolicyName());
					protectFunctions.createAndActivateThreatScoreBasedPolicy(restClient, requestHeader, suiteData, policyBean);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		protectFunctions.waitForMinutes(20);
		
		List<NameValuePair> reqHeaders = ClientUtil.buildCookieHeaders(suiteData);
		String postResponseBody = protectFunctions.getPolicyAlerts(new Client(), reqHeaders, suiteData, null, 30);
		while (postResponseBody.startsWith("Error")) {
			postResponseBody = protectFunctions.getPolicyAlerts(new Client(), reqHeaders, suiteData, null, 30);
		}
		
		// facets
		facets = ClientUtil.getJSONValue(postResponseBody, "facets");	
		facility = ClientUtil.getJSONValue(facets, "facility");
		policyType = ClientUtil.getJSONValue(facets, "policy_type");
		user = ClientUtil.getJSONValue(facets, "user");
		policyViolated = ClientUtil.getJSONValue(facets, "_PolicyViolated");
		severity = ClientUtil.getJSONValue(facets, "severity");
		
		postFacilityMap = populateAlertFilterDtlsMap(facility);
		postPolicyTypeMap = populateAlertFilterDtlsMap(policyType);
		postUserMap = populateAlertFilterDtlsMap(user);
		postPolicyViolatedMap = populateAlertFilterDtlsMap(policyViolated);
		postSeverityMap = populateAlertFilterDtlsMap(severity);

	}
	
	@Test(priority = 2)
	public void testAlertBoxServices() throws Exception {
		
		String preBoxCount = preFacilityMap.get("Box");
		String postBoxCount = postFacilityMap.get("Box");
		
		Logger.info("PreBox:PostBox"+preBoxCount+":"+postBoxCount);
		Assert.assertEquals(Integer.parseInt(preBoxCount) + 3, Integer.parseInt(postBoxCount), "Box Services Count doesn't match after policies are added and alerts are triggered in the SaaS app");
	}
	
	@Test(priority = 3)
	public void testAlertGDriveServices() throws Exception {
		String preGDriveCount = preFacilityMap.get("Google Drive");
		String postGDriveCount = postFacilityMap.get("Google Drive");
		Logger.info("PreGDrive:PostGDrive"+preGDriveCount+":"+postGDriveCount);
		Assert.assertEquals(Integer.parseInt(preGDriveCount) + 2, Integer.parseInt(postGDriveCount), "GDrive Services Count doesn't match after policies are added and alerts are triggered in the SaaS app");
	}	
	
	
	@Test(priority = 4)
	public void testAlertDropboxServices() throws Exception {
		String preDropboxCount = preFacilityMap.get("Dropbox");
		String postDropboxCount = postFacilityMap.get("Dropbox");
		Logger.info("PreDropbox:PostDropbox"+preDropboxCount+":"+postDropboxCount);
		Assert.assertEquals(Integer.parseInt(preDropboxCount) + 1, Integer.parseInt(postDropboxCount), "Dropbox Services Count doesn't match after policies are added and alerts are triggered in the SaaS app");
	}
	
	@Test(priority = 5)
	public void testAlertUsers() throws Exception {
		String preBoxAdminUserCount = preUserMap.get("box-admin@protectbeatle.com");
		String preAdminUserCount = preUserMap.get("admin@protectbeatle.com");
		
		String postBoxAdminUserCount = postUserMap.get("box-admin@protectbeatle.com");
		String postAdminUserCount = postUserMap.get("admin@protectbeatle.com");
		
		Logger.info("preBoxAdminUserCount:postBoxAdminUserCount"+preBoxAdminUserCount+":"+postBoxAdminUserCount);
		Logger.info("preAdminUserCount:postAdminUserCount"+preAdminUserCount+":"+postAdminUserCount);
		
		Assert.assertEquals(Integer.parseInt(preBoxAdminUserCount) + 3, Integer.parseInt(postBoxAdminUserCount), "box-admin@protectbeatle.com count doesn't match after policies are added and alerts are triggered");
		Assert.assertEquals(Integer.parseInt(preAdminUserCount) + 2, Integer.parseInt(postAdminUserCount), "admin@protectbeatle.com count doesn't match after policies are added and alerts are triggered");		
		
		
	}
	
	@Test(priority = 6)
	public void testAlertGDrivePolicyName() throws Exception {
		String preGDrivePolicy = prePolicyViolatedMap.get("DATA_EXP_GDRIVE");
		String postGDrivePolicy = postPolicyViolatedMap.get("DATA_EXP_GDRIVE");
		
		Logger.info("GDrive Policy Name:" +preGDrivePolicy+":"+postGDrivePolicy);
		Assert.assertEquals(Integer.parseInt(preGDrivePolicy) + 2, Integer.parseInt(postGDrivePolicy), "DATA_EXP_GDRIVE Count doesn't match after policies are added and alerts are triggered");
		
	}
	
	@Test(priority = 7)
	public void testAlertBoxPolicyName() throws Exception {
		String preBoxPolicy = prePolicyViolatedMap.get("DATA_EXP_BOX");
		String postBoxPolicy = postPolicyViolatedMap.get("DATA_EXP_BOX");
		
		Logger.info("Box Policy Name:" +preBoxPolicy+":"+postBoxPolicy);
		Assert.assertEquals(Integer.parseInt(preBoxPolicy) + 1, Integer.parseInt(postBoxPolicy), "DATA_EXP_BOX Count doesn't match after policies are added and alerts are triggered");
		
	}
	
	@Test(priority = 8)
	public void testAlertDropboxPolicyName() throws Exception {
		String preDropboxPolicy = prePolicyViolatedMap.get("DATA_EXP_DROPBOX");
		String postDropboxPolicy = postPolicyViolatedMap.get("DATA_EXP_DROPBOX");
		
		Logger.info("Dropbox Policy Name:" +preDropboxPolicy+":"+postDropboxPolicy);
		Assert.assertEquals(Integer.parseInt(preDropboxPolicy) + 1, Integer.parseInt(postDropboxPolicy), "DATA_EXP_DROPBOX Count doesn't match after policies are added and alerts are triggered");
	}	
	
	@Test(priority = 9)
	public void testAlertPolicyTypeDataExposure() throws Exception {
		
		String preDataExposure = prePolicyTypeMap.get("FileSharingAPI");
		String postDataExposure = postPolicyTypeMap.get("FileSharingAPI");
		
		Logger.info("Dataexposure Policy:" +preDataExposure+":"+postDataExposure);
		Assert.assertEquals(Integer.parseInt(preDataExposure) + 3, Integer.parseInt(postDataExposure), "Data Exposure Policy Count doesn't match after policies are added and alerts are triggered");
		
		
	}
	
	@Test(priority = 10)
	public void testAlertPolicyTypeAccessEnforcement() throws Exception {
		
		String preAccessMonitoring = prePolicyTypeMap.get("AccessEnforcementAPI");
		String postAccessMonitoring = postPolicyTypeMap.get("AccessEnforcementAPI");
		
		Logger.info("AccessEnforcement Policy:" +preAccessMonitoring+":"+postAccessMonitoring);
		Assert.assertEquals(Integer.parseInt(preAccessMonitoring) + 2, Integer.parseInt(postAccessMonitoring), "Access Enforcement Policy Count doesn't match after policies are added and alerts are triggered");
		
	}	
	
	@Test(priority = 11)
	public void testAlertPolicySeverityCritical() throws Exception {
		
		String preCritical = preSeverityMap.get("critical");
		String postCritical = postSeverityMap.get("critical");
		
		Logger.info("Critical:"+preCritical+":"+postCritical);
		Assert.assertEquals(Integer.parseInt(preCritical) + 3, Integer.parseInt(postCritical), "Alert Severity Critical Count doesn't match after policies are added and alerts are triggered");
		
	}
	
	@Test(priority = 12)
	public void testAlertPolicySeverityHigh() throws Exception {
		
		String preHigh = preSeverityMap.get("high");
		String postHigh = postSeverityMap.get("high");
		
		Logger.info("High:"+preHigh+":"+postHigh);
		Assert.assertEquals(Integer.parseInt(preHigh) + 2, Integer.parseInt(postHigh), "Alert Severity High Count doesn't match after policies are added and alerts are triggered");
		
	}		
	
	@Test(priority = 13)
	public void testAlertPolicySeverityMedium() throws Exception {
		
		String preMedium = preSeverityMap.get("medium");
		String postMedium = postSeverityMap.get("medium");
		
		Logger.info("Medium:"+preMedium+":"+postMedium);
		Assert.assertEquals(Integer.parseInt(preMedium) + 1, Integer.parseInt(postMedium), "Alert Severity Medium Count doesn't match after policies are added and alerts are triggered");
		
	}
	

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 14)
	public void testDeletePolicy(List<String[]> list) throws Exception {
		Logger.info("###########################################################################################################################");
		Logger.info("This test method deletes all policies created for impact analysis tests ");
		Logger.info("###########################################################################################################################");
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		protectFunctions.deletePolicy(restClient, "ACC_MON_BOX", requestHeader, suiteData);
		protectFunctions.deletePolicy(restClient, "THREAT_SCORE_DROPBOX", requestHeader, suiteData);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				Reporter.log("Starting testcase: testDeletePolicy - " + policyName, true);
				protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
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
		if (policyBean.getCloudService().equalsIgnoreCase("Dropbox")) {
			suiteData.setSaasApp("Dropbox");
			FileUploadResponse fileUploadResponse = dropboxUniversalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
			if (fileUploadResponse.getFileId() == null) {
				Reporter.log("File not uploaded, again uploading a file");
				String filename = file.getName().substring(0, file.getName().indexOf("."));
				file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filename), policyBean.getFileExt());
				dropboxUniversalApi.uploadFile("/FileTransfer", file.getAbsolutePath());
				policyBean.setFileName(file.getName());
			}
			Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
			if (policyBean.getExposureType().equalsIgnoreCase("public")) {
				Reporter.log("Creating a public link for the file - " + file.getName(), true);
				String createSharedLink = dropboxUniversalApi.createSharedLinkForFolder("/FileTransfer" + File.separator + file.getName());
				System.out.println("Create shared Link: " + createSharedLink);
			}
		} else if (policyBean.getCloudService().equalsIgnoreCase("Box")) {
			suiteData.setSaasApp("Box");
			String folderName = "A_" + UUID.randomUUID().toString();
			Reporter.log("Creating a folder in Box : " + folderName, true);
			BoxFolder folder = boxUniversalApi.createFolder(folderName);
			//folderCollection.put(policyName, folder);
			FileUploadResponse fileUploadResponse = boxUniversalApi.uploadFile(folder.getId(), file.getAbsolutePath(), file.getName());
			Reporter.log("File Uploaded to " + policyBean.getCloudService() + " - " + file.getName(), true);
			
			if (policyBean.getExposureType().contains("external"))
				protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, "external");
        	if (policyBean.getExposureType().equals("public")) {
        		Logger.info("Sharing file in Box as public ");
        		protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, "public");
        	}
        	if (policyBean.getExposureType().equals("internal")) {
        		Logger.info("Sharing file in Box as internal ");
        		protectFunctions.shareFileOnBox(boxUniversalApi, fileUploadResponse, folder, "internal");
        	}
			
		} else if (policyBean.getCloudService().equalsIgnoreCase("Google Drive")) {
			String uniqueId = UUID.randomUUID().toString();
			String folderId = gDriveUniversalApi.createFolder("A_" + uniqueId);
			//gDriveFolderCollection.put(policyName, folderId);
			//gDriveFileCollection.put(policyName, file.getName());
			FileUploadResponse fileUploadResponse = gDriveUniversalApi.uploadFile(folderId, file.getAbsolutePath(), file.getName());
			protectFunctions.waitForMinutes(0.5);
			//gDriveFileIdCollection.put(policyName, fileUploadResponse.getFileId());

			if(policyBean.getExposureType().contains(ProtectTestConstants.EXTERNAL)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), "mayurbelekar@gmail.com", "user", policyBean.getPolicyType());
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.INTERNAL)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), suiteData.getDomainName(), "domain", policyBean.getPolicyType());
			}
			if(policyBean.getExposureType().contains(ProtectTestConstants.PUBLIC)){
				gDrive.insertPermission(gDrive.getDriveService(), fileUploadResponse.getFileId(), null, "anyone", policyBean.getPolicyType());
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
		list.add(getData());
		return new Object[][] { { list } };
	}
	
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		return new Object[][] {
			
				// Data Exposure
				new String[] { "DATA_EXP_DROPBOX", "Policy Desc", "DataExposure", "Dropbox", "unexposed", "any", "any","any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "no", "medium" },
				new String[] { "DATA_EXP_GDRIVE", "Policy Desc", "DataExposure", "Google Drive", "unexposed", "admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "high" },
				new String[] { "DATA_EXP_BOX", "Policy Desc", "DataExposure", "Box", "unexposed", "box-admin@protectbeatle.com", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "no", "critical" },
				
				// Access Monitoring
				new String[] { "ACC_MON_BOX", "Box", "box-admin@protectbeatle.com", "any", "any", "no", "no", "0", "File:Upload", "txt"},
				
				// ThreatSore Based
				new String[] { "THREAT_SCORE_DROPBOX", "Dropbox", "admin@protectbeatle.com", "Any", "Any", "no", "No", "10", "BLOCK_SERVICE"},
		};
	}
	
	

	

	private HashMap<String, String> populateAlertFilterDtlsMap(String filter) throws JsonProcessingException, IOException {
		String terms = ClientUtil.getJSONValue(filter, "terms");
		JSONArray jsonArray = (JSONArray) new JSONTokener(terms).nextValue();
		HashMap<String, String> filterMap = new HashMap<String, String>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			String term = ClientUtil.getJSONValue(jsonArray.getJSONObject(i).toString(), "term");
			term = term.substring(1, term.length()-1);
			String count = ClientUtil.getJSONValue(jsonArray.getJSONObject(i).toString(), "count");
			//Logger.info(term +":"+count);
			filterMap.put(term, count);
		}
		
		return filterMap;
	}	



}