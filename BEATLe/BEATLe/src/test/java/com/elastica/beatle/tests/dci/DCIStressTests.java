package com.elastica.beatle.tests.dci;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCICommonTest;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;
import com.elastica.beatle.logger.Logger;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

/**
 * 
 * @author eldorajan
 *
 */

public class DCIStressTests extends DCICommonTest{

	DCIFunctions dciFunctions = null;
	Map<String,String> folderInfo = new HashMap<String,String>();
	String uniqueId = UUID.randomUUID().toString();
	
	/**********************************************TEST METHODS***********************************************/

	
	@Test(groups ={"All"})
	public void testUploadFiles() throws Exception {
		dciFunctions = new DCIFunctions();
		
		
		for(int j = 0; j < 100; j++){
			String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PCI_P2_FILES_PATH);
			
			
			for (int i = 0; i < fileName.length; i++) {	
				fileName[i] = dciFunctions.createSampleFileType(DCIConstants.DCI_FILE_UPLOAD_PCI_P2_FILES_PATH,
						fileName[i]);	
			}
			
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			
			for (int i = 0; i < fileName.length; i++) {
				Logger.info("Uploading file:"+fileName[i]+" is in progress to Saas App:"+suiteData.getSaasApp());
				dciFunctions.uploadFile(universalApi, suiteData, folderInfo.get("folderId"), fileName[i]);
				
					account = dciFunctions.getUserAccount(suiteData);
					universalApi = dciFunctions.getUniversalApi(suiteData, account);
				Logger.info("Uploading file:"+fileName[i]+" is completed to Saas App:"+suiteData.getSaasApp());
			}	
		}
	}


	/**********************************************TEST METHODS***********************************************/
	/**********************************************BEFORE/AFTER CLASS*****************************************/
	
	/**
	 * Create folders in saas apps
	 */
	@BeforeClass(groups ={"All"})
	public void createFolder() {
		dciFunctions = new DCIFunctions();
		try {
			UserAccount account = dciFunctions.getUserAccount(suiteData);
			UniversalApi universalApi = dciFunctions.getUniversalApi(suiteData, account);
			
			folderInfo = dciFunctions.createFolder(universalApi, suiteData, 
					DCIConstants.DCI_FOLDER+uniqueId);
		} catch (Exception ex) {
			Logger.info("Issue with Create Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	@BeforeSuite(alwaysRun=true)
	public void downloadFileFromS3() {
		try {
			S3ActionHandler s3 = new S3ActionHandler();
			s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/GoldenSet", 
					new File(DCIConstants.DCI_FILE_TEMP_PATH));
			
		} catch (Exception ex) {
			Logger.info("Downloading folder from S3 is failed with exception " + ex.getLocalizedMessage());
		}
	}

	/**********************************************BEFORE/AFTER CLASS*****************************************/


}
