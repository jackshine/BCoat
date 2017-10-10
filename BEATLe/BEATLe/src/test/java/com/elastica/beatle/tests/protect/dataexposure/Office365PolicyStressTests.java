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

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.protect.PolicyBean;
import com.elastica.beatle.protect.ProtectFunctions;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.tests.securlets.OneDriveUtils;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;

import junit.framework.Protectable;

/**
 * @author Mayur
 *
 */
public class Office365PolicyStressTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, File> fileCollection = new HashMap<String, File>();
	Map<String, Folder> folderCollection = new HashMap<String, Folder>();
	Map<String, ItemResource> itemResourceCollection = new HashMap<String, ItemResource>();
	List<String> folderList = new ArrayList<String>();
	String filePath = ProtectTestConstants.PROTECT_RESOURCE_PATH + "policyfiletransfer.txt";
	

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public void init() throws Exception {
		universalApi = protectFunctions.loginToApp(suiteData);
		restClient = new Client();
	}

	@Test(priority=1)
	public void test123() throws Exception{
		String[][] stringArray = getLevel();
		folderCollection = protectFunctions.createFolderStructureFor200Files(stringArray, universalApi);
		 
	}
	
	public String[][] getLevel(){
		return new String[][]{
			/*
			 * 1. Folder Prefix
			 * 2. Number of folder levels
			 * 3. Number of sub folders in each folder
			 * 4. Exposure type of the file
			 * 5. Role
			 */
			new String[] {"A", "3", "2", "internal", "1"},
			new String[] {"B", "3", "2", "internal", "1"},
			new String[] {"C", "3", "2", "external", "1"},
			new String[] {"D", "3", "2", "external", "2"},
		};
	}
	
	
	
	
	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 2)
	public void createPolicyAndUploadFile(List<String[]> list) throws Exception {
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
				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in One Drive : " + folderName, true);
				Folder folder = universalApi.createFolder(folderName, folderCollection.get(data[21]).getId());
				folderCollection.put(policyName, folder);
				ItemResource itemResourse = universalApi.uploadSimpleFile(folder.getId(), file.getAbsolutePath(), file.getName());
				// protectFunctions.waitForMinutes(0.5);
				// Check and delete if policy exist already
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				itemResourceCollection.put(policyName, itemResourse);
				
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		
		protectFunctions.waitForMinutes(5);
		shareFileInOneDrive(itemResourceCollection, arrList);
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposure", priority = 3)
	public void testPolicyViolationAndRemediation(String... data) throws Exception {
		policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName).getName();
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		universalApi = protectFunctions.loginToApp(suiteData);
		
		Reporter.log("Starting testcase: testPolicyViolationAndRemediation - " + policyName, true);
		Reporter.log("Retriving the Policy Alert logs", true);
		Map<String, String> policyViolationLogsMessage = protectFunctions.getProtectPolicyViolationAlertLogMessage(restClient,
				fileName, policyName, requestHeader, suiteData);
		Reporter.log(policyName + " - " + policyViolationLogsMessage, true);
		protectFunctions.assertPolicyViolation(policyViolationLogsMessage, policyBean.getCloudService(), fileName, policyName);
		Reporter.log("Verified all the parameters for policy alert logs", true);
		Reporter.log("Retriving the display logs for policy violation", true);
		Map<String, String> displayLogsMessage = protectFunctions.getSecurletPolicyViolationLogs(restClient, policyBean.getCloudService(),
				fileName, policyName, requestHeader, suiteData);
		Reporter.log(policyName + " - " + displayLogsMessage, true);
		protectFunctions.assertPolicyViolation(displayLogsMessage, policyBean.getCloudService(), fileName, policyName);
		Reporter.log("Verified all the parameters for display logs", true);
		Reporter.log("Retriving the remediation log", true);
		if(policyBean.getRemediationActivity().equalsIgnoreCase("unshare")){
			String remediationMessage = "User unshared '"+fileName+"'";
			String unsharedWithMessage = "";
			if(policyBean.getExposureType().equals(ProtectTestConstants.INTERNAL)){
				unsharedWithMessage = ProtectTestConstants.EVERYONE_EXCEPT_EXTERNAL_USERS;
			}
			if(policyBean.getPolicyType().equalsIgnoreCase("1")){
				unsharedWithMessage = unsharedWithMessage + "(Read)";
			} else if(policyBean.getPolicyType().equalsIgnoreCase("2")){
				unsharedWithMessage = unsharedWithMessage + "(Contribute)";
			}
			remediationMessage = remediationMessage + " with '" + unsharedWithMessage + "'."; 
			Map<String, String> remediationLogs = protectFunctions.getInformationalDisplayLogs(restClient, policyBean.getCloudService(),
					fileName, requestHeader, suiteData, remediationMessage);
			Reporter.log(policyName + " - " + remediationLogs, true);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.MESSAGE), remediationMessage);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.FACILITY), policyBean.getCloudService());
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SEVERITY), ProtectTestConstants.INFORMATIONAL);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.SOURCE), ProtectTestConstants.API);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.ACTIVITY_TYPE), ProtectTestConstants.UNSHARE);
			Assert.assertEquals(remediationLogs.get(ProtectTestConstants.UNSHARED_WITH), unsharedWithMessage);
		}
		Reporter.log("Completed testcase: testPolicyViolationAndRemediation - " + policyName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 4)
	public void deletePolicies(List<String[]> list) throws Exception {
		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		universalApi = protectFunctions.loginToApp(suiteData);
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
				Reporter.log("Deleted policy - " + policyName, true);
				universalApi.deleteFolderV2(folderCollection.get(policyName).getId(), folderCollection.get(policyName).getETag());
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public void deleteFolders() throws Exception {
		try {
			String directoryName = ProtectTestConstants.PROTECT_RESOURCE_PATH + "newFiles";
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

	public void assertPolicyVoilation() {

	}

	/**
	 * 
	 * @return
	 */
	@DataProvider(name = "DataExposure")
	public Object[][] getData() {
		return new Object[][] {
			// Unshare file
			new String[] { "ODStress001", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A1"},
			new String[] { "ODStress002", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" , "A1"},
			new String[] { "ODStress003", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "A1" },
			new String[] { "ODStress004", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "A1" },
			new String[] { "ODStress005", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "A1" },
			new String[] { "ODStress006", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "A1" },
			new String[] { "ODStress007", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "A1" },
			
			new String[] { "ODStress008", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "A2" },
			new String[] { "ODStress009", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "A2" },
			new String[] { "ODStress010", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "A2" },
			new String[] { "ODStress011", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "A2"},
			new String[] { "ODStress012", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare" , "A2"},
			new String[] { "ODStress013", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A2"},
			new String[] { "ODStress014", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" , "A2"},
			
			new String[] { "ODStress015", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "A3" },
			new String[] { "ODStress016", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "A3" },
			new String[] { "ODStress017", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "A3" },
			new String[] { "ODStress018", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "A3" },
			new String[] { "ODStress019", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "A3" },
			new String[] { "ODStress020", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "A3" },
			new String[] { "ODStress021", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "A3" },
			
			new String[] { "ODStress022", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "A4" },
			new String[] { "ODStress023", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "A4"},
			new String[] { "ODStress024", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare" , "A4"},
			new String[] { "ODStress025", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A4"},
			new String[] { "ODStress026", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" , "A4"},
			new String[] { "ODStress027", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "A4" },
			new String[] { "ODStress028", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "A4" },
			
			new String[] { "ODStress029", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "A5" },
			new String[] { "ODStress030", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "A5" },
			new String[] { "ODStress031", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "A5" },
			new String[] { "ODStress032", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "unshare" , "A5"},
			new String[] { "ODStress033", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "A5" },
			new String[] { "ODStress034", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "A5" },
			new String[] { "ODStress035", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "A5" },
			
			new String[] { "ODStress036", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "A6" },
			new String[] { "ODStress037", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "unshare", "A6" },
			new String[] { "ODStress038", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "no", "jpg", "0,0", "no", "no", "unshare", "A6" },
			new String[] { "ODStress039", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "exe", "2097152,0", "no", "no", "unshare", "A6" },
			new String[] { "ODStress040", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "unshare", "A6" },
			new String[] { "ODStress041", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A6"},
			new String[] { "ODStress042", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare" , "A6"},
			
			new String[] { "ODStress043", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "A7" },
			new String[] { "ODStress044", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "A7" },
			new String[] { "ODStress045", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "A7" },
			new String[] { "ODStress046", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "A7" },
			new String[] { "ODStress047", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "A7" },
			new String[] { "ODStress048", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "A7" },
			new String[] { "ODStress049", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "A7" },
			new String[] { "ODStress050", "Policy Desc", "1", "Office 365", "internal", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "A7" },
			
			
			new String[] { "ODStress051", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "B1"},
			new String[] { "ODStress052", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "removeCollab" , "B1"},
			new String[] { "ODStress053", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "B1"},
			new String[] { "ODStress054", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab" , "B1"},
			new String[] { "ODStress055", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "B1" },
			new String[] { "ODStress056", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "B1" },
			new String[] { "ODStress057", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "B1" },
			
			new String[] { "ODStress058", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "B2" },
			new String[] { "ODStress059", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "B2" },
			new String[] { "ODStress060", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "removeCollab", "B2" },
			new String[] { "ODStress061", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "B2" },
			new String[] { "ODStress062", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "B2" },
			new String[] { "ODStress063", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "B2"},
			new String[] { "ODStress064", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "removeCollab" , "B2"},
			
			new String[] { "ODStress065", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "B3"},
			new String[] { "ODStress066", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab" , "B3"},
			new String[] { "ODStress067", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "B3" },
			new String[] { "ODStress068", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "B3" },
			new String[] { "ODStress069", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "B3" },
			new String[] { "ODStress070", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "B3" },
			new String[] { "ODStress071", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "B3" },
			
			new String[] { "ODStress072", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "removeCollab" , "B4"},
			new String[] { "ODStress073", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "B4" },
			new String[] { "ODStress074", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "B4" },
			new String[] { "ODStress075", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "B4" },
			new String[] { "ODStress076", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "removeCollab", "B4" },
			new String[] { "ODStress077", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "removeCollab", "B4" },
			new String[] { "ODStress078", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "no", "jpg", "0,0", "no", "no", "removeCollab", "B4" },
			
			new String[] { "ODStress079", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "exe", "2097152,0", "no", "no", "removeCollab", "B5" },
			new String[] { "ODStress080", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "removeCollab", "B5" },
			new String[] { "ODStress081", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab", "B5" },
			new String[] { "ODStress082", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "removeCollab", "B5" },
			new String[] { "ODStress083", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "removeCollab", "B5" },
			new String[] { "ODStress084", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "B5" },
			new String[] { "ODStress085", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "B5" },
			
			new String[] { "ODStress086", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "B6" },
			new String[] { "ODStress087", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "removeCollab", "B6" },
			new String[] { "ODStress088", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "removeCollab", "B6" },
			new String[] { "ODStress089", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "removeCollab", "B6" },
			new String[] { "ODStress090", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "removeCollab", "B6" },
			new String[] { "ODStress091", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "removeCollab", "B6" },
			new String[] { "ODStress092", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "removeCollab", "B6" },
			
			new String[] { "ODStress093", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress094", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress095", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress096", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "removeCollab", "B7" },
			new String[] { "ODStress097", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress098", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress099", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "removeCollab", "B7" },
			new String[] { "ODStress100", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "removeCollab", "B7" },

			new String[] { "ODStress101", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress102", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress103", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress104", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress105", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress106", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "updateCollabRead", "C1" },
			new String[] { "ODStress107", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "updateCollabRead", "C1" },
			
			new String[] { "ODStress108", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress109", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress110", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress111", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress112", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress113", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "updateCollabRead", "C2" },
			new String[] { "ODStress114", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabRead", "C2" },
			
			new String[] { "ODStress115", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress116", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress117", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress118", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress119", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress120", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "updateCollabRead", "C3" },
			new String[] { "ODStress121", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabRead", "C3" },
			
			new String[] { "ODStress122", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress123", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress124", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress125", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress126", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress127", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "updateCollabRead", "C4" },
			new String[] { "ODStress128", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "updateCollabRead", "C4" },
			
			new String[] { "ODStress129", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress130", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress131", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress132", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress133", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress134", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "updateCollabRead", "C5" },
			new String[] { "ODStress135", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "updateCollabRead", "C5" },
			
			new String[] { "ODStress136", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress137", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress138", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress139", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress140", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress141", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "updateCollabRead", "C6" },
			new String[] { "ODStress142", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "updateCollabRead", "C6" },
			
			new String[] { "ODStress143", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress144", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress145", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress146", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress147", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress148", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress149", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "updateCollabRead", "C7" },
			new String[] { "ODStress150", "Policy Desc", "2", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "updateCollabRead", "C7" },
			
			new String[] { "ODStress151", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress152", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress153", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress154", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress155", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress156", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "updateCollabContribute", "D1" },
			new String[] { "ODStress157", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "updateCollabContribute", "D1" },
			
			new String[] { "ODStress158", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress159", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress160", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress161", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress162", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "txt", "no", "0,0", "no", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress163", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "no", "no", "updateCollabContribute", "D2" },
			new String[] { "ODStress164", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "updateCollabContribute", "D2" },
			
			new String[] { "ODStress165", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress166", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress167", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PCI", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress168", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "no", "0,0", "PII", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress169", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "rtf", "no", "0,0", "no", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress170", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "no", "no", "updateCollabContribute", "D3" },
			new String[] { "ODStress171", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "docx", "0,0", "no", "no", "updateCollabContribute", "D3" },
			
			new String[] { "ODStress172", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "docx", "no", "0,0", "no", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress173", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "doc", "doc", "no", "0,0", "no", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress174", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "docx", "no", "doc", "0,0", "no", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress175", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "java", "no", "0,0", "no", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress176", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "no", "0,0", "Source Code", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress177", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "html", "no", "0,0", "no", "no", "updateCollabContribute", "D4" },
			new String[] { "ODStress178", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "html", "no", "html", "0,0", "no", "no", "updateCollabContribute", "D4" },
			
			new String[] { "ODStress179", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "exe", "no", "0,0", "no", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress180", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "exe", "no", "no", "2097152,0", "no", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress181", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "jpg", "jpg", "no", "0,0", "no", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress182", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "pdf", "no", "0,0", "no", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress183", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "no", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress184", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "updateCollabContribute", "D5" },
			new String[] { "ODStress185", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "cs", "no", "cs", "0,0", "Source Code", "no", "updateCollabContribute", "D5" },
			
			new String[] { "ODStress186", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "no", "0,0", "Encryption", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress187", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "no", "0,0", "VBA Macros", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress188", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "xlsm", "no", "0,0", "no", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress189", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "no", "0,0", "Encryption", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress190", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "xls", "no", "0,0", "no", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress191", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "no", "no", "updateCollabContribute", "D6" },
			new String[] { "ODStress192", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "rar", "no", "0,0", "no", "no", "updateCollabContribute", "D6" },
			
			new String[] { "ODStress193", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "no", "0,0", "Virus", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress194", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "PII", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress195", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "no", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress196", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "no", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress197", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "no", "0,0", "Virus", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress198", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress199", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "no", "0,0", "Source Code", "no", "updateCollabContribute", "D7" },
			new String[] { "ODStress200", "Policy Desc", "1", "Office 365", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "js", "no", "js", "0,0", "no", "no", "updateCollabContribute", "D7" },
			
		};
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
	
	private void shareFileInOneDrive(Map<String, ItemResource> itemResourceCollection, List<Object> arrList) throws Exception {
		String exposureType = null;
		
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			String[] data = (String[]) iterator.next();
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
			exposureType = policyBean.getExposureType();
			if (itemResourceCollection.get(policyBean.getPolicyName()) != null)
				shareFileInODrive(itemResourceCollection.get(policyBean.getPolicyName()), exposureType);
		}
	}
	
	private void shareFileInODrive(ItemResource itemResourse,
			String exposureType) throws Exception {
		
			if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
				SharingUserRoleAssignment sharingUserRoleAssignment = new OneDriveUtils().getPublicShareObject(itemResourse.getWebUrl(), 1);
				universalApi.shareWithCollaborators(sharingUserRoleAssignment);
			}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EVERYONE)){
				protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EVERYONE);
			}if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
				protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.INTERNAL);
			}if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
				protectFunctions.shareFileOnOneDrive(itemResourse, universalApi, 1, ProtectTestConstants.EXTERNAL);
			}
			
	}
	
}
