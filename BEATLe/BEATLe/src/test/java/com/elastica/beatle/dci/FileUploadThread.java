package com.elastica.beatle.dci;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.logger.Logger;
import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.salesforce.ChatterFile;

public class FileUploadThread implements Runnable {

	private UniversalApi universalApi; 
	private TestSuiteDTO suiteData; 
	private String folderId;
	private String fileName;
	private Map<String,String> fileInfo = new HashMap<String,String>();
	
	public FileUploadThread(UniversalApi universalApi, TestSuiteDTO suiteData, 
			String folderId, String fileName){
		this.universalApi=universalApi;
		this.suiteData=suiteData;
		this.folderId=folderId;
		this.fileName=fileName;
	}

	@Override
	public synchronized void run() {
		Logger.info(Thread.currentThread().getName()+" Uploading filename:"+fileName+" is in progress");
		uploadFile(universalApi, suiteData, folderId, fileName);
		Logger.info(Thread.currentThread().getName()+" Uploading filename:"+fileName+" is completed");
	}

	public synchronized Map<String,String> uploadFile(UniversalApi universalApi, TestSuiteDTO suiteData, 
			String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);

		String fileId = null;String fileEtag = null;
		

		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				if(fResponse.getResponseCode()==429||
						fResponse.getResponseCode()==500){
					waitForSeconds(20);
					fResponse = universalApi.uploadFile(folderId,
							DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				}
				
				
				fileId = fResponse.getFileId();
				fileEtag = (String) fResponse.getEtag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName);
				fileId = fResponse.getFileId();fileEtag = "";
				break;
			}
			case GoogleDrive: {
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName);
				fileId = fResponse.getFileId();
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				ItemResource fResponse = universalApi.uploadSimpleFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				fileId = fResponse.getId();
				fileEtag = (String) fResponse.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case SalesForce: {
				Salesforce sfapi = universalApi.getSalesforce();
				ChatterFile chatterfile = sfapi.uploadFileToChatter(DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				fileId = chatterfile.getId();
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		fileInfo.put("fileName", fileName);
		fileInfo.put("fileId", fileId);
		fileInfo.put("fileEtag", fileEtag);

		Logger.info("Uploading file "+fileName+" in saas app is completed");

		return fileInfo;
	}

	public synchronized Map<String, String> getFileInfo(){
		return fileInfo;
	}
	
	public synchronized void waitForSeconds(int time){
		Logger.info("Waiting for " + (time) + " second(s)");
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
	}
}