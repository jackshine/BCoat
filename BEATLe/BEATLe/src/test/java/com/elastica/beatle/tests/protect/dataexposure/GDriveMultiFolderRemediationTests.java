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
import com.elastica.beatle.protect.dataprovider.GDriveDataProvider;
import com.universal.common.GDrive;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.onedrive.ItemResource;

/**
 * @author Shri
 *
 */
public class GDriveMultiFolderRemediationTests extends ProtectInitializeTests {

	UniversalApi universalApi;
	Client restClient;
	PolicyBean policyBean;
	GDrive gDrive; String folderId;
	ProtectFunctions protectFunctions = new ProtectFunctions();
	Map<String, String> fileCollection = new HashMap<String, String>();
	Map<String, String> folderCollection = new HashMap<String, String>();
	Map<String, String> fileIdCollection = new HashMap<String, String>();
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
	public void createFolder() throws Exception{
		String[][] stringArray = getLevel();
		String mainFolderName = "MF"+UUID.randomUUID().toString();;
		Reporter.log("Creating a top level folder in GDrive : " + mainFolderName, true);
		folderId = universalApi.createFolder(mainFolderName);
		folderCollection = protectFunctions.createFolderStructureFor200Files(stringArray, universalApi, universalApi.getgDrive(), folderId);
		 
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
		FileUploadResponse fileUploadResponse = null;
		
		while (iterator.hasNext()) {
			try {
				String[] data = (String[]) iterator.next();
				policyBean = protectFunctions.populatePolicyBean(data);
				String policyName = policyBean.getPolicyName();
				File file = protectFunctions.createDynamicFile(ProtectTestConstants.PROTECT_RESOURCE_PATH, new File(filePath),
						policyBean.getFileExt());
				//fileCollection.put(policyName, file);
				policyBean.setFileName(file.getName());
/*				String folderName = "A_" + UUID.randomUUID().toString();
				Reporter.log("Creating a folder in  GDrive : " + folderName, true);
				Folder folder = universalApi.createFolder(folderCollection.get(data[21]).getName()+ "/"+ folderName);
				folderCollection.put(policyName, folder);*/
				//ItemResource itemResourse = universalApi.uploadSimpleFile(folderCollection.get(data[21]).getId(), file.getAbsolutePath(), file.getName());
				
				fileUploadResponse = universalApi.uploadFile(folderCollection.get(data[21]), file.getAbsolutePath(), file.getName());				
				
				// protectFunctions.waitForMinutes(0.5);
				// Check and delete if policy exist already
				protectFunctions.createAndActivateDataExposurePolicy(restClient, policyBean, requestHeader, suiteData);
				//itemResourceCollection.put(policyName, itemResourse);
				fileIdCollection.put(policyName, fileUploadResponse.getFileId());
				fileCollection.put(policyName, file.getName());
				
			} catch (Exception e) {
				continue;
			} catch(Error e) {
				continue;
			}
		}
		
		protectFunctions.waitForMinutes(5);
		//shareFileInOneDrive(itemResourceCollection, arrList);
		shareFileInGDrive(fileIdCollection, arrList);
		protectFunctions.waitForMinutes(15);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposure", priority = 3)
	public void testPolicyViolationAndRemediationLogs(String... data) throws Exception {
		PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
		String policyName = policyBean.getPolicyName();
		String fileName = fileCollection.get(policyName);
		String fileId = fileIdCollection.get(policyName);
		policyBean.setFileName(fileName);
		protectFunctions.logTestDescription(policyBean);
		Reporter.log("Starting testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
		protectFunctions.verifyAllDataExposurePolicyViolationLogs(restClient, requestHeader, suiteData, policyBean, fileCollection);
		// Remediation Verification
		protectFunctions.googleDriveRemediationVerification(restClient, requestHeader, suiteData, policyBean, fileName, fileId);
		Reporter.log("Completed testcase: testPolicyViolationAndRemediationLogs - " + policyName, true);
	}

	/**
	 * 
	 * @param data
	 * @throws Exception
	 */
	@Test(dataProvider = "DataExposureList", priority = 4)
	public void testDeletePolicy(List<String[]> list) throws Exception {

		Object[] arr = list.get(0);
		List<Object> arrList = Arrays.asList(arr);
		Iterator<Object> iterator = arrList.iterator();
		universalApi = protectFunctions.loginToApp(suiteData);
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
		universalApi.deleteFolder(folderId, true, "");
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
				
			// Remove Collaborator
			
				new String[] { "GDStress001", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "A1" },
				new String[] { "GDStress002", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab", "A1"  },
				new String[] { "GDStress003", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "A1"  },
				new String[] { "GDStress004", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "A1"  },
				new String[] { "GDStress005", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "A1"  },
				new String[] { "GDStress006", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "A1"  },
				new String[] { "GDStress007", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "A1"  },
				
				new String[] { "GDStress008", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "removeCollab", "B1"  },
				new String[] { "GDStress009", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "B1"  },
				new String[] { "GDStress010", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "B1"  },
				new String[] { "GDStress011", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "B1"  },
				new String[] { "GDStress012", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "removeCollab", "B1"  },
				new String[] { "GDStress013", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "B1"  },
				new String[] { "GDStress014", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab", "B1"  },	
				
				new String[] { "GDStress015", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "C1"  },
				new String[] { "GDStress016", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "C1"  },
				new String[] { "GDStress017", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "C1"  },
				new String[] { "GDStress018", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "C1"  },
				new String[] { "GDStress019", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "C1"  },
				new String[] { "GDStress020", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "removeCollab", "C1"  },
				new String[] { "GDStress021", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "C1"  },
				
				new String[] { "GDStress022", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "D1"  },
				new String[] { "GDStress023", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "D1"  },
				new String[] { "GDStress024", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "removeCollab", "D1"  },				
				new String[] { "GDStress025", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "D1" },
				new String[] { "GDStress026", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab", "D1"  },
				new String[] { "GDStress027", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "D1"  },
				new String[] { "GDStress028", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "D1"  },
			
				new String[] { "GDStress029", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "A2"  },
				new String[] { "GDStress030", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "A2"  },
				new String[] { "GDStress031", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "A2"  },
				new String[] { "GDStress032", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "removeCollab", "A2"  },
				new String[] { "GDStress033", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "A2"  },
				new String[] { "GDStress034", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "A2"  },
				new String[] { "GDStress035", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "A2"  },
		
				new String[] { "GDStress036", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "B2" },
				new String[] { "GDStress037", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab", "B2"  },
				new String[] { "GDStress038", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "B2"  },
				new String[] { "GDStress039", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "B2"  },
				new String[] { "GDStress040", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "removeCollab", "B2"  },
				new String[] { "GDStress041", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "removeCollab", "B2"  },
				new String[] { "GDStress042", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "removeCollab", "B2"  },
			
				new String[] { "GDStress043", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "removeCollab", "C2"  },
				new String[] { "GDStress044", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "removeCollab", "C2"  },
				new String[] { "GDStress045", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "removeCollab", "C2"  },
				new String[] { "GDStress046", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "removeCollab", "C2"  },
				new String[] { "GDStress047", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "removeCollab", "C2" },
				new String[] { "GDStress048", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "removeCollab", "C2"  },
				new String[] { "GDStress049", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "removeCollab", "C2"  },
				new String[] { "GDStress050", "Policy Desc", "reader", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "removeCollab", "C2"  },				
				

				
				
			// Remove Link
				
				new String[] { "GDStress051", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "D2" },
				new String[] { "GDStress052", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare", "D2" },
				new String[] { "GDStress053", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "D2" },
				new String[] { "GDStress054", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "D2" },
				new String[] { "GDStress055", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "D2" },
				new String[] { "GDStress056", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "D2" },
				new String[] { "GDStress057", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "D2" },
				
				new String[] { "GDStress058", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "A3" },
				new String[] { "GDStress059", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "A3" },
				new String[] { "GDStress060", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "A3" },
				new String[] { "GDStress061", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "A3" },
				new String[] { "GDStress062", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "A3" },	
				new String[] { "GDStress063", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A3" },
				new String[] { "GDStress064", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare", "A3" },
				
				new String[] { "GDStress065", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "B3" },
				new String[] { "GDStress066", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "B3" },
				new String[] { "GDStress067", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "B3" },
				new String[] { "GDStress068", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "B3" },
				new String[] { "GDStress069", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "B3" },
				new String[] { "GDStress070", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "B3" },
				new String[] { "GDStress071", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "B3" },
				
				new String[] { "GDStress072", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "C3" },
				new String[] { "GDStress073", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "C3" },
				new String[] { "GDStress074", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "C3" },
				new String[] { "GDStress075", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "C3" },
				new String[] { "GDStress076", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare", "C3" },
				new String[] { "GDStress077", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "C3" },
				new String[] { "GDStress078", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "C3" },
				
				new String[] { "GDStress079", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "D3" },
				new String[] { "GDStress080", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "D3" },
				new String[] { "GDStress081", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "D3" },
				new String[] { "GDStress082", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "D3" },
				new String[] { "GDStress083", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "D3" },
				new String[] { "GDStress084", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "D3" },
				new String[] { "GDStress085", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "D3" },
				
				new String[] { "GDStress086", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "A4" },		
				new String[] { "GDStress087", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "unshare", "A4" },
				new String[] { "GDStress088", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "unshare", "A4" },
				new String[] { "GDStress089", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "unshare", "A4" },
				new String[] { "GDStress090", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "unshare", "A4" },
				new String[] { "GDStress091", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "unshare", "A4" },
				new String[] { "GDStress092", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "unshare", "A4" },
				
				new String[] { "GDStress093", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "unshare", "B4" },
				new String[] { "GDStress094", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "unshare", "B4" },
				new String[] { "GDStress095", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "unshare", "B4" },
				new String[] { "GDStress096", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "unshare", "B4" },
				new String[] { "GDStress097", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "B4" },
				new String[] { "GDStress098", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "B4" },					
				new String[] { "GDStress099", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "unshare", "B4" },
				new String[] { "GDStress100", "Policy Desc", "reader", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "unshare", "B4" },
				
			// Prevent Download
				
				new String[] { "GDStress101", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress102", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress103", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress104", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress105", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress106", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventCopyPrintDownload", "C4" },
				new String[] { "GDStress107", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventCopyPrintDownload", "C4" },
				
				new String[] { "GDStress108", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress109", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress110", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress111", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress112", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress113", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload", "D4" },
				new String[] { "GDStress114", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload", "D4" },
				
				new String[] { "GDStress115", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress116", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress117", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress118", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress119", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress120", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "preventCopyPrintDownload", "A5" },
				new String[] { "GDStress121", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventCopyPrintDownload", "A5" },
				
				new String[] { "GDStress122", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress123", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress124", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress125", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress126", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress127", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "preventCopyPrintDownload", "B5" },
				new String[] { "GDStress128", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventCopyPrintDownload", "B5" },

				new String[] { "GDStress129", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress130", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress131", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress132", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress133", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress134", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventCopyPrintDownload", "C5" },
				new String[] { "GDStress135", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventCopyPrintDownload", "C5" },
				
				new String[] { "GDStress136", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress137", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress138", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress139", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress140", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress141", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "preventCopyPrintDownload", "D5" },
				new String[] { "GDStress142", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "preventCopyPrintDownload", "D5" },

				new String[] { "GDStress143", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress144", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress145", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress146", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress147", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress148", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress149", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "preventCopyPrintDownload", "A6" },
				new String[] { "GDStress150", "Policy Desc", "writer", "Google Drive", "external", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "preventCopyPrintDownload", "A6" },				
				
				
			// Change Access
				
				new String[] { "GDStress151", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress152", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress153", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress154", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress155", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress156", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyLinkCommenter", "B6" },
				new String[] { "GDStress157", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "companyLinkCommenter", "B6" },
				
				new String[] { "GDStress158", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress159", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress160", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress161", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress162", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress163", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "companyLinkCommenter", "C6" },
				new String[] { "GDStress164", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "companyLinkCommenter", "C6" },

				new String[] { "GDStress165", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress166", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress167", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress168", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress169", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress170", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "companyLinkCommenter", "D6" },
				new String[] { "GDStress171", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "companyLinkCommenter", "D6" },

				new String[] { "GDStress172", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress173", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress174", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress175", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress176", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress177", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "companyLinkCommenter", "A7" },
				new String[] { "GDStress178", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyLinkCommenter", "A7" },

				new String[] { "GDStress179", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress180", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress181", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress182", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress183", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress184", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "companyLinkCommenter", "B7" },
				new String[] { "GDStress185", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "companyLinkCommenter", "B7" },

				new String[] { "GDStress186", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress187", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress188", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress189", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "java", "no", "java", "0,0", "Source Code", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress190", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PII", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress191", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "PCI", "no", "companyLinkCommenter", "C7" },
				new String[] { "GDStress192", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "txt", "no", "txt", "0,0", "GLBA", "no", "companyLinkCommenter", "C7" },

				new String[] { "GDStress193", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PCI", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress194", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xlsm", "no", "xlsm", "0,0", "VBA Macros", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress195", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "xls", "no", "xls", "0,0", "Encryption", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress196", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "zip", "no", "zip", "0,0", "Virus", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress197", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rar", "no", "rar", "0,0", "Virus", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress198", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "bin", "no", "bin", "0,0", "Encryption", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress199", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "rtf", "no", "rtf", "0,0", "PII", "no", "companyLinkCommenter", "D7" },
				new String[] { "GDStress200", "Policy Desc", "writer", "Google Drive", "public", "any", "any", "any", "no", "no", "any", "any", "no", "no", "pdf", "no", "pdf", "0,0", "HIPAA", "no", "companyLinkCommenter", "D&" },
			
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
	
	
	private void shareFileInGDrive(Map<String, String> fileIdCollection, List<Object> arrList) throws IOException {
		
		Iterator<Object> iterator = arrList.iterator();
		while (iterator.hasNext()) {
			String[] data = (String[]) iterator.next();
			PolicyBean policyBean = protectFunctions.populatePolicyBean(data);
			shareFile(policyBean, fileIdCollection.get(policyBean.getPolicyName()), policyBean.getExposureType());
		}
	}
	
	/**
	 * 
	 * @param policyBean
	 * @param fileUploadResponse
	 * @param exposureType
	 * @throws IOException
	 */
	private void shareFile(PolicyBean policyBean,
			String fileId, String exposureType)
			throws IOException {
		gDrive = universalApi.getgDrive();
		try {
		Logger.info("Sharing file on GDrive for Policy:"+ policyBean.getPolicyName() +": File Id: "+ fileId + "Exposure: "+exposureType);
		
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.EXTERNAL)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, "protectelastica@gmail.com", ProtectTestConstants.USER, policyBean.getPolicyType());
		}
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.INTERNAL)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, suiteData.getDomainName(), ProtectTestConstants.DOMAIN1, policyBean.getPolicyType());
		}
		if(exposureType.equalsIgnoreCase(ProtectTestConstants.PUBLIC)){
			gDrive.insertPermission(gDrive.getDriveService(), fileId, null, ProtectTestConstants.ANYONE, policyBean.getPolicyType());
		}
		} catch (Exception e) {
			Reporter.log("Issue in Applying Permission with admin :"+e.getLocalizedMessage(),true);
		}
	}
	
	
}
