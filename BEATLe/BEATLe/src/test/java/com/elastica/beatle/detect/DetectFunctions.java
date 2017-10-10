package com.elastica.beatle.detect;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.SaasType;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.google.api.services.drive.model.Permission;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.onedrive.Folder;

public class DetectFunctions {
	
	public static final String DETECT_FILE_UPLOAD_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload";
	public static final String DETECT_FILE_UPLOAD_PATH_TEMP = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"detect"+File.separator+"upload"+File.separator+"temp";
	
	
	
	public String createSampleFileType(String fileName, String currentDateTime) {
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(DETECT_FILE_UPLOAD_PATH + File.separator + fileName);
			File dest = new File(DETECT_FILE_UPLOAD_PATH + File.separator + currentDateTime + "_" + fileName);
			Logger.info(dest);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  fileName;
	}

	public String uploadFile(UniversalApi universalApi, String saasType, String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				FileUploadResponse fileUploadResponse =	universalApi.uploadFile(folderId,
						DETECT_FILE_UPLOAD_PATH_TEMP + File.separator + fileName, fileName);
				String fileId_InternalExposedTest = fileUploadResponse.getFileId();
				 return fileId_InternalExposedTest;
				//break;
			}
			case Dropbox: {
				universalApi.uploadFile(folderId,
						DETECT_FILE_UPLOAD_PATH_TEMP+ File.separator + fileName);
				break;
			}
			case GoogleDrive: {
				 FileUploadResponse fileUploadResponse = universalApi.uploadFile(folderId,
						 DETECT_FILE_UPLOAD_PATH_TEMP + File.separator + fileName);
				 String fileId_InternalExposedTest = fileUploadResponse.getFileId();
				 return fileId_InternalExposedTest;
				
			}
			case OneDrive: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH_TEMP + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case Office365: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH_TEMP + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH_TEMP + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
			Thread.sleep(2000);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		
		Logger.info("Uploading file "+fileName+" in saas app is completed");
		return fileName;
	}
	
	
	public Map<String,String> shareFile(TestSuiteDTO suiteData, UniversalApi universalApi,
			Map<String,String> folderInfo,Map<String,String> fileInfo, String shareType) {

		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);

		FileEntry sharedFile = null;
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				if(shareType.equalsIgnoreCase("Public")){
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");String fileEtag=fileInfo.get("fileEtag");

					Logger.info("Sharing of file "+fileName+" in saas app is in progress");

					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("open");
					sharedFile = universalApi.createSharedLink(fileId, sharedLink); 

					fileInfo.put("fileId", sharedFile.getId());
					fileInfo.put("fileEtag", sharedFile.getEtag());
					fileInfo.put("fileName", fileName);

					Logger.info("Shared file access:" + sharedFile.getSharedLink().getAccess());
					Logger.info("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess());

					Logger.info("Sharing of file "+fileName+" in saas app is completed");

				}else if(shareType.equalsIgnoreCase("Internal")){
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");String fileEtag=fileInfo.get("fileEtag");

					Logger.info("Sharing of file "+fileName+" in saas app is in progress");

					SharedLink sharedLink = new SharedLink();
					sharedLink.setAccess("company");
					sharedFile = universalApi.createSharedLink(fileId, sharedLink); 

					fileInfo.put("fileId", sharedFile.getId());
					fileInfo.put("fileEtag", sharedFile.getEtag());
					fileInfo.put("fileName", fileName);

					Logger.info("Shared file access:" + sharedFile.getSharedLink().getAccess());
					Logger.info("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess());

					Logger.info("Sharing of file "+fileName+" in saas app is completed");

				}else if(shareType.equalsIgnoreCase("External")){
					String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

					Logger.info("Sharing of folder "+folderName+" in saas app is in progress");

					CollaborationInput collabInput = new CollaborationInput();
					Item item = new Item();
					item.setId(folderId);
					item.setType(folderInfo.get("folderType"));

					AccessibleBy aby = new AccessibleBy();
					aby.setName("Automation Tester");
					aby.setType("user");
					aby.setLogin("theautomationtester87@gmail.com");

					collabInput.setItem(item);
					collabInput.setAccessibleBy(aby);
					collabInput.setRole("editor");

					//Create the collaboration
					BoxCollaboration collaboration = universalApi.createCollaboration(collabInput);
					Logger.info("Collaboration Id:" + collaboration.getId());

					fileInfo.put("folderId", folderId);
					fileInfo.put("folderEtag", "*");
					fileInfo.put("folderName", folderName);

					Logger.info("Sharing of folder "+folderName+" in saas app is completed");
				}

				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case Dropbox: {
				String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");String fileEtag=fileInfo.get("fileEtag");
				String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

				universalApi.createSharedLinkForFolder(folderName + File.separator + fileName);
				break;
			}
			case GoogleDrive: {
				String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");String fileEtag=fileInfo.get("fileEtag");
				String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

				GDrive gDrive = universalApi.getgDrive();
				Permission insertPermission = gDrive.
						insertPermission(gDrive.getDriveService(), fileId, null, "anyone", "reader");

				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");String fileEtag=fileInfo.get("fileEtag");
				String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				String sharefile = StringUtils.split(fileName, ".")[0];
				universalApi.createSharedLink(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
						sharefile);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
			Thread.sleep(1*2*1000);
			//waitForSeconds(2);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}


		return fileInfo;
	}

	
	
	public void downloadFile(UniversalApi universalApi, String saasType, String fileId, String filename) {
		Logger.info("Uploading file "+filename+" in saas app is in progress");
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				/*universalApi.uploadFile(folderId,
						DETECT_FILE_UPLOAD_PATH + File.separator + fileName, fileName);*/
				break;
			}
			case Dropbox: {
				/*universalApi.uploadFile(folderId,
						DETECT_FILE_UPLOAD_PATH+ File.separator + fileName);*/
				break;
			}
			case GoogleDrive: {
				universalApi.downloadFile(fileId, filename);
				break;
			}
			case OneDrive: {
				/*org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);*/

				break;
			}
			case Office365: {
/*				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);*/

				break;
			}
			case OneDriveBusiness: {
				/*org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.uploadSimpleFile(folderId,
						DETECT_FILE_UPLOAD_PATH + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);*/

				break;
			}
			default: {

				break;
			}
			}
			Thread.sleep(2000);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		
		Logger.info("Uploading file "+filename+" in saas app is completed");
	}

	

	public UserAccount getUserAccount(TestSuiteDTO suiteData) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
		UserAccount account = null;
		try {
			switch (stype) {
			case Box: {
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole());
				break;
			}
			case Dropbox: {
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getSaasAppToken());
				break;
			}
			case GoogleDrive: {
				GDriveAuthorization gDriveAuthorization=new GDriveAuthorization(suiteData.getSaasAppClientId(), 
						suiteData.getSaasAppClientSecret());
				String authToken = gDriveAuthorization.getAceessTokenFromRefreshAccessToken(suiteData.getSaasAppToken());
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(),authToken);
				break;
			}
			case OneDrive: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Office365: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			default: {

				break;
			}
			}
			Thread.sleep(2000);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		return account;
	}
	public UniversalApi getUniversalApi(TestSuiteDTO suiteData,UserAccount account) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
		UniversalApi universalApi = null;

		try {
			switch (stype) {
			case Box: {
				universalApi = new UniversalApi("BOX", account);
				break;
			}
			case Dropbox: {
				universalApi = new UniversalApi("DROPBOX", account);
				break;
			}
			case GoogleDrive: {
				universalApi = new UniversalApi("GDRIVE", account);
				break;
			}
			case OneDrive: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Office365: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			default: {

				break;
			}
			}
			Thread.sleep(2000);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}
		return universalApi;
	}	
	public Map<String,String> createFolder(UniversalApi universalApi,String saasType, String folderName) {
		Logger.info("Creating new folder "+folderName+" in saas app is in progress");
		SaasType stype = SaasType.getSaasType(saasType);
		String folderId = null;String folderEtag = null;
		Map<String,String> folderInfo = new HashMap<String,String>();
		try {
			switch (stype) {
			case Box: {
				BoxFolder folderObj = universalApi.createFolder(folderName);
				folderId = folderObj.getId();
				folderEtag = (String) folderObj.getEtag();
				break;
			}
			case Dropbox: {
				universalApi.createFolder(File.separator+folderName);
				folderId = File.separator+folderName;folderEtag = "";
				break;
			}
			case GoogleDrive: {
				String folderObj = universalApi.createFolder(folderName);
				folderId = folderObj;folderEtag = "";
				break;
			}
			case OneDrive: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				Folder folderObj = universalApi.createFolderV2(folderName);
				folderId = folderObj.getId();folderEtag = (String) folderObj.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case Office365: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				Folder folderObj = universalApi.createFolderV2(folderName);
				folderId = folderObj.getId();folderEtag = (String) folderObj.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				Folder folderObj = universalApi.createFolderV2(folderName);
				folderId = folderObj.getId();folderEtag = (String) folderObj.getETag();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

			ex.printStackTrace();
		}

		Logger.info("Creating new folder "+folderName+" in saas app is completed");
		
		folderInfo.put("folderId", folderId);
		folderInfo.put("folderEtag", folderEtag);
		return folderInfo;
	}
	
	/**
	 * delete folder for saastype in saas app
	 * @param saasType
	 * @param folderId
	 */
	public void deleteFolder(UniversalApi universalApi,String saasType, Map<String,String> folderInfo) {
		Logger.info("Deleting folder "+folderInfo.get("folderId")+" in saas app is in progress");
		
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				break;
			}
			case Dropbox: {
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				break;
			}
			case GoogleDrive: {
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				break;
			}
			case OneDrive: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolderV2(folderInfo.get("folderId"), folderInfo.get("folderEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Office365: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolderV2(folderInfo.get("folderId"), folderInfo.get("folderEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolderV2(folderInfo.get("folderId"), folderInfo.get("folderEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		
		Logger.info("Deleting new folder "+folderInfo.get("folderId")+" in saas app is completed");

	}
	
	/**
	 * Cleanup temp folder
	 */
	public void cleanupTempFolder() {
		try {
			File dir = new File(DETECT_FILE_UPLOAD_PATH_TEMP);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					if (dir.listFiles().length == 0) {
						// dir.delete();
					} else {
						for (File f : dir.listFiles())
							f.delete();
					}
					// dir.delete();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCurrentDateTime() {
		DateTime currentTime = DateTime.now();
		String ts = Long.toString(currentTime.getMillis() / 1000);
		return ts;
	}

}
