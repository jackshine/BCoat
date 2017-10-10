package com.elastica.beatle.securlets;

import static org.testng.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.testng.Reporter;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.RemedialAction;
import com.elastica.beatle.securlets.dto.RemediationMetaInfo;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.SelectiveScanPolicy;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.UIRemediationObject;
import com.elastica.beatle.securlets.dto.UISource;
import com.elastica.beatle.securlets.dto.UIVulnerabilityTotals;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.google.api.services.gmail.model.Message;
import com.universal.common.GoogleMailServices;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.Shares;

public class SecurletUtils extends CommonTest{
	public enum facility {Box, Office365, Gmail, Salesforce, Elastica};
	protected enum SaasApp {ONEDRIVE_BUSINESS, BOX}; 
	protected enum Remediation {UNSHARE, SHARE_ACCESS, SHARE_EXPIRE};
	protected enum Server {UIServer, APIServer};
	public enum ExposureTypes{PUBLIC, EXTERNAL, INTERNAL, INTERNAL_EXTERNAL, COMPANY}; 
	public enum Severity{informational, warning, critical};
	public enum ActivityType {ScopeAdd, ScopeDelete, Unsync, Add, Update, Delete, Edit, Move, Create, Remove, Upload, Download, Rename, Share, Unshare, Copy, Undelete, Restore, Lock, Unlock, Send, Receive,Trash, SubSiteCreated, SubSiteDeleted};
	public enum ObjectType{File,Folder,Web_Link, User, Session, Group, Email_Message,Lead, Account, Opportunity, Contact, Case, Contract, Solution, Campaign, Report, Task, Event, Site};


	protected enum UserRoles {ADMIN, ENDUSER, COADMIN};
	public enum elapp {el_box, el_google_drive, el_dropbox, el_office_365, el_google_apps, el_salesforce};

	//protected UniversalApi universalApi;
	//protected UserAccount userAccount;

	public SecurletUtils() throws Exception {
		super();
	}


	public FileEntry uploadFileAndShareit(String folderId, String sourceFile, String access) throws Exception {
		Reporter.log("Going to upload the file to Box...", true);
		//Upload a file and share it publicly
		String uniqueId = UUID.randomUUID().toString();
		String destinationFile = uniqueId + "_" + sourceFile;
		String destinationFolderId = (folderId == null) ? "/" : folderId;

		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = this.universalApi.uploadFile(destinationFolderId, sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
		Reporter.log("File "+ uploadResponse.getFileName() + " uploaded successfully", true);

		//Share the file 
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);

		FileEntry sharedFile = universalApi.createSharedLink(uploadResponse.getFileId(), sharedLink); 
		Reporter.log("Shared file access:" + sharedFile.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess(), true);
		Reporter.log("File shared with "+ uploadResponse.getFileName() + " access", true);

		return sharedFile;
	}


	public FileEntry uploadFolderAndShareit(String folderId, String sourceFile, String access) throws Exception {

		//Upload a file and share it publicly
		String uniqueId = UUID.randomUUID().toString();
		String destinationFile = uniqueId + "_" + sourceFile;

		Reporter.log("Going to upload a file into the folder...", true);

		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = this.universalApi.uploadFile(folderId, sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());
		Reporter.log("File uploaded into the folder successfully...", true);

		//Share the file publicly
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);

		FileEntry sharedFile = universalApi.createSharedLinkForFolder(folderId, sharedLink); 
		Reporter.log("Shared file access:" + sharedFile.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFile.getSharedLink().getEffectiveAccess(), true);
		Reporter.log("File shared with the "+ sharedFile.getSharedLink().getEffectiveAccess() + " access.", true);
		return sharedFile;
	}


	public BoxFolder shareTheFolderPubliclyOrWithCollaborators(String folderId, String sourceFile, String access, String[] collaborators, String uniqueFolderString) throws Exception {
		//Upload a file and share it publicly


		String uniqueId = (uniqueFolderString == null) ? UUID.randomUUID().toString() : uniqueFolderString;
		String destinationFile = uniqueId + "_" + sourceFile;
		//String destinationFolderId = (folderId == null) ? "/" : folderId;

		//Upload a source code file to box root folder
		FileUploadResponse uploadResponse = universalApi.uploadFile(folderId, sourceFile, destinationFile);
		Reporter.log(uploadResponse.getFileName() + " " + uploadResponse.getResponseMessage());


		for (String collaborator : collaborators) {

			Reporter.log("Creating the collaborators : "+collaborator, true);
			//Prepare the input for collaboration
			CollaborationInput collabInput = new CollaborationInput();
			Item item = new Item();
			item.setId(folderId);
			item.setType("folder");

			AccessibleBy aby = new AccessibleBy();
			aby.setName(uniqueId);
			aby.setType("user");
			aby.setLogin(collaborator);

			collabInput.setItem(item);
			collabInput.setAccessibleBy(aby);
			collabInput.setRole("editor");

			//create collaboration
			universalApi.createCollaboration(collabInput);

		}


		//Share the file publicly
		SharedLink sharedLink = new SharedLink();
		sharedLink.setAccess(access);

		BoxFolder sharedFolder = universalApi.createSharedLinkForFolder(folderId, sharedLink); 
		Reporter.log("Shared file access:" + sharedFolder.getSharedLink().getAccess(), true);
		Reporter.log("Shared file effective access:" + sharedFolder.getSharedLink().getEffectiveAccess(), true);

		return sharedFolder;
	}






	//Need to be deprecated
	//@Deprecated
	public BoxDocument getBoxDocuments(String isInternal, String appname, HashMap<String, String> additionalParams) throws Exception {

		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		qparams.add(new BasicNameValuePair("app",  appname));

		if(additionalParams != null) {
			//Add all the keys
			for(String key : additionalParams.keySet()) {
				qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
			}
		}

		String path = suiteData.getAPIMap().get("getBoxDocuments")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		BoxDocument documents = MarshallingUtils.unmarshall(responseBody, BoxDocument.class);
		return documents;
	}


	//Updated method to be used going forward to get the list of documents

	public SecurletDocument getExposedDocuments(String elappname, List<NameValuePair> qparams) throws Exception {
		int retry = 0; 
		HttpResponse response = null;
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getExposedDocuments")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);

		do {
			response =  restClient.doGet(uri, headers);
			if (response.getStatusLine().getStatusCode() >=HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				retry++;
				Reporter.log(" Retrying as previous request failed with error code: "+response.getStatusLine().getStatusCode(),true);
				try{
					sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				} catch (Exception e) {}

			} else {
				break;
			}
		} while(retry <= FrameworkConstants.retryCount );

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}

	public SecurletDocument getUIExposedDocuments(String payload, List<NameValuePair> qparams) throws Exception {
		int retry = 0; 
		HttpResponse response = null;
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8"));

		String path = suiteData.getAPIMap().get("getUIExposedDocuments");
		StringEntity stringEntity =  new StringEntity(payload, "UTF-8");

		Logger.info("Payload:"+payload);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);

		do {
			response =  restClient.doPost(uri, headers, null, stringEntity);
			if (response.getStatusLine().getStatusCode() >=HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				retry++;
				Reporter.log(" Retrying as previous request failed with error code: "+response.getStatusLine().getStatusCode(),true);
				try{
					sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				} catch (Exception e) {}

			} else {
				break;
			}
		} while(retry <= FrameworkConstants.retryCount );

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}

	
	public SecurletDocument getUIRiskyDocuments(String payload, List<NameValuePair> qparams) throws Exception {
		int retry = 0; 
		HttpResponse response = null;
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8"));
		
		qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("requestType", "risky_docs"));
		
		String path = suiteData.getAPIMap().get("getUIExposedDocuments");
		StringEntity stringEntity =  new StringEntity(payload, "UTF-8");

		Logger.info("Payload:"+payload);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);

		do {
			response =  restClient.doPost(uri, headers, null, stringEntity);
			if (response.getStatusLine().getStatusCode() >=HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				retry++;
				Reporter.log(" Retrying as previous request failed with error code: "+response.getStatusLine().getStatusCode(),true);
				try{
					sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
				} catch (Exception e) {}

			} else {
				break;
			}
		} while(retry <= FrameworkConstants.retryCount );

		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}
	

	public SecurletDocument getExposedUsers(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getExposedUsers")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}


	public SecurletDocument getUserExposures(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getUserExposures")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}

	public SecurletDocument getUserVulnerabilities(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getUserVulnerabilities")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}

	public SecurletDocument getCollaborators(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getCollaborators")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}



	public SecurletDocument getRiskyDocuments(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getRiskyDocuments")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		SecurletDocument documents = MarshallingUtils.unmarshall(responseBody, SecurletDocument.class);
		return documents;
	}


	//Updated method.. to be used everywhere

	public ExposureTotals getUserTotals(String elappname, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getUserTotals")
				.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
				.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
				.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());


		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Response body:"+ responseBody);
		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	}


	public ExposureTotals getExposuresMetricsTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;

		for (int i = 0; i< 3; i++) {
			try{
				List<NameValuePair> headers = getHeaders();

				String path = suiteData.getAPIMap().get("getExposureTotals")
						.replace(SecurletsConstants.ELAPPNAME_PLACEHOLDER, elappname)
						.replace(SecurletsConstants.TENANT_PLACEHOLDER, suiteData.getTenantName())
						.replace(SecurletsConstants.VERSION_PLACEHOLDER, suiteData.getBaseVersion());


				//System.out.println("Path:" + path);

				URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
				HttpResponse response =  restClient.doGet(uri, headers);

				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
					Logger.info("Retrying the request ...");
					continue;
				}
				String responseBody = ClientUtil.getResponseBody(response);
				Logger.info("Response body:"+ responseBody);
				exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
				break;
			}
			catch(Exception e){
				sleep(CommonConstants.THIRTY_SECONDS_SLEEP);
			}
		}

		Reporter.log("API Public   Exposure Count:"+exposureTotals.getPublicExposouresCount(), true);
		Reporter.log("API Internal Exposure Count:"+exposureTotals.getInternalExposouresCount(), true);
		Reporter.log("API External Exposure Count:"+exposureTotals.getExternalExposouresCount(), true);
		return exposureTotals;
	}


	/**
	 * Utility method to retrieve the metrics to be displayed in Venn Diagram
	 * @param elappname
	 * @param qparams
	 * @return
	 * @throws Exception
	 */
	public ExposureTotals getUIExposuresMetricsTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		//		List<NameValuePair> headers = getHeaders();
		//
		//		String path = suiteData.getAPIMap().get("getUIExposureTotals");
		//		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		//		HttpResponse response =  restClient.doGet(uri, headers);
		//		String responseBody = ClientUtil.getResponseBody(response);
		//		Logger.info("Response body:"+ responseBody);
		//		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		//		
		//		Reporter.log("API Public   Exposure Count:"+exposureTotals.getPublicExposouresCount(), true);
		//		Reporter.log("API Internal Exposure Count:"+exposureTotals.getInternalExposouresCount(), true);
		//		Reporter.log("API External Exposure Count:"+exposureTotals.getExternalExposouresCount(), true);
		//		return exposureTotals;
		//		
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIExposureTotals");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		Reporter.log("UI Public   Exposure Count:"+exposureTotals.getPublicExposouresCount(), true);
		Reporter.log("UI Internal Exposure Count:"+exposureTotals.getInternalExposouresCount(), true);
		Reporter.log("UI External Exposure Count:"+exposureTotals.getExternalExposouresCount(), true);
		return exposureTotals;
	}


	/**
	 * Utility method to retrieve the metrics UI content types
	 * @param elappname
	 * @param qparams
	 * @return
	 * @throws Exception
	 */
	public ExposureTotals getUIContentTypesTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIContentTypesTotal");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}	


	/**
	 * Utility method to retrieve the metrics UI file types
	 * @param elappname
	 * @param qparams
	 * @return
	 * @throws Exception
	 */
	public ExposureTotals getUIFileTypesTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIFileTypesTotal");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}	


	/**
	 * Utility method to retrieve the metrics UI file types
	 * @param elappname
	 * @param qparams
	 * @return
	 * @throws Exception
	 */
	public UIVulnerabilityTotals getUIVulnerabilityTypesTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		UIVulnerabilityTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIVulnerabilityTypesTotal");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, UIVulnerabilityTotals.class);
			break;
		}
		return exposureTotals;
	}	


	public ExposureTotals getUISubFeaturesTotal(String elappname, List<NameValuePair> qparams) throws Exception {
		//https://eoe.elastica-inc.com/admin/application/list/get_sub_features?app=Office+365&isInternal=true
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUISubFeatures");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}



	public ExposureTotals getUIMetricsUsersTotal(List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIMetricsUsersTotal");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}

	public ExposureTotals getUIMetricsUsersExposures(List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIMetricsUsersExposures");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}


	public ExposureTotals getUIMetricsUsersVulnerabilities(List<NameValuePair> qparams) throws Exception {
		ExposureTotals exposureTotals = null;
		for (int i = 0; i< 3; i++) {
			List<NameValuePair> headers = getHeaders();

			String path = suiteData.getAPIMap().get("getUIMetricsUsersVulnerabilities");
			//System.out.println("Path:" + path);

			URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
			HttpResponse response =  restClient.doGet(uri, headers);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Logger.info("Response code returned by server is :"+ response.getStatusLine().getStatusCode());
				Logger.info("Retrying the request ...");
				continue;
			}
			String responseBody = ClientUtil.getResponseBody(response);
			Logger.info("Response body:"+ responseBody);
			exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
			break;
		}
		return exposureTotals;
	}

	//Need to be deprecated
	@Deprecated
	public ExposureTotals getExposuresMetricsTotal(String isInternal, String appname) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		qparams.add(new BasicNameValuePair("app",  appname));

		String path = suiteData.getAPIMap().get("getBoxExposureTotals")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	}




	public ExposureTotals getExposuresMetricsTotal(String isInternal, String appname, HashMap<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		qparams.add(new BasicNameValuePair("app",  appname));

		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}

		String path = suiteData.getAPIMap().get("getBoxExposureTotals")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	}


	public BoxDocument getDocumentCollaborators(String isInternal, String appname, String elapp) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		qparams.add(new BasicNameValuePair("app",  appname));

		String path = suiteData.getAPIMap().get("getDocumentCollaborators")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{elappname}", elapp)
				.replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		BoxDocument documents = MarshallingUtils.unmarshall(responseBody, BoxDocument.class);
		return documents;
	}


	public ExposureTotals getExposedFileTypes(String elappname, String isInternal, String top) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", isInternal));
		qparams.add(new BasicNameValuePair("top",  top));

		String path = suiteData.getAPIMap().get("getExposedFileTypes")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	}


	public VulnerabilityTypes getVulnerabilityTypes(String elappname, HashMap<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}


		String path = suiteData.getAPIMap().get("getVulnerabilityTypes")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);
		VulnerabilityTypes vulnerabilityTypes = MarshallingUtils.unmarshall(responseBody, VulnerabilityTypes.class);
		return vulnerabilityTypes;
	}


	public void getDocumentClass(String elappname, boolean isInternal, int top, boolean exposed) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair("top", String.valueOf(top)));

		if (exposed) {
			qparams.add(new BasicNameValuePair("exposed", "false"));
			qparams.add(new BasicNameValuePair("vl_types", "all"));
			//qparams.add(new BasicNameValuePair("doc_class", "legal"));

		}

		String path = suiteData.getAPIMap().get("getDocumentClass")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);

		//VulnerabilityTypes vulnerabilityTypes = unmarshall(responseBody, VulnerabilityTypes.class);
		//return vulnerabilityTypes;
	}


	protected void getRiskyDocuments(boolean isInternal, int limit ) throws Exception {
		List<NameValuePair> headers = getHeaders();

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair("is_internal", String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair("limit", String.valueOf(limit)));

		String path = suiteData.getAPIMap().get("getBoxRiskyDocuments")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);

	}


	public ExposureTotals getFileTypes(String elappname, HashMap<String, String> queryParams) throws Exception {
		List<NameValuePair> headers = getHeaders();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 

		if(queryParams !=null) {
			//Add all the keys
			for(String key : queryParams.keySet()) {
				qparams.add(new BasicNameValuePair(key,  queryParams.get(key)));
			}
		}

		String path = suiteData.getAPIMap().get("getExposedFileTypes")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);

		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	}



	public ExposureTotals getExposedContentTypes(String elappname, HashMap<String, String> queryParams) throws Exception {
		List<NameValuePair> headers = getHeaders();
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 

		if(queryParams !=null) {
			//Add all the keys
			for(String key : queryParams.keySet()) {
				qparams.add(new BasicNameValuePair(key,  queryParams.get(key)));
			}
		}

		String path = suiteData.getAPIMap().get("getDocumentClass")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);

		Logger.info("Response body:"+ responseBody);

		ExposureTotals exposureTotals = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return exposureTotals;
	} 

	public String getTenantAccountInfo() throws Exception {
		String path = suiteData.getAPIMap().get("getUITenantAccountInfo") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> headers = getHeaders();
		HttpResponse response =  restClient.doGet(dataUri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log(responseBody, true);
		return responseBody;
	}

	public String getTenantAccountId() throws Exception {
		String accountInfo = this.getTenantAccountInfo();
		JSONObject jso = new JSONObject(accountInfo);
		JSONObject jsdetails = jso.getJSONObject("details");
		org.json.JSONArray tenantArray = jsdetails.getJSONArray("tenant");
		String id = tenantArray.getJSONObject(0).getString("id");
		Reporter.log("Tenant Account Id:" + id, true);
		return id;
	}

	public String getTenantParams(String appName) throws Exception {
		String entity = "{\"app_name\":\""+appName +"\"}";
		StringEntity se =  new StringEntity(entity);
		String path = suiteData.getAPIMap().get("getAdminTenantParams") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> headers = getHeaders();
		HttpResponse response =  restClient.doPost(dataUri, headers, null, se);
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log(responseBody, true);
		return responseBody;
	}

	
	public String getTenantInstanceId(String appName) throws Exception {
		String results = this.getTenantParams(appName);
		JSONObject jso = new JSONObject(results);
		org.json.JSONArray tenantArray = jso.getJSONArray("results");
		String id = tenantArray.getJSONObject(0).getString("instance");
		return id;
	}
	
	
	public HashMap<String, String> getTenantScanPolicyId(String appName) throws Exception {
		HashMap<String, String> hmap = new HashMap<String, String>();
		String id = "";
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  appName));
		String scanpolicy = this.getScanPolicy(qparams);
		JSONObject jso = new JSONObject(scanpolicy);
		org.json.JSONArray policies = jso.getJSONArray("scanPolicy");
		for(int i=0; i<policies.length(); i++) {
			id = policies.getJSONObject(0).getString("id");
			String accountdetails = policies.getJSONObject(0).getString("active_accounts");
			//Reporter.log(policies.getJSONObject(0).getString("active_accounts"), true);
			String accountName = StringUtils.substringBetween(StringUtils.split(accountdetails, ":")[0], "'", "'");
			hmap.put(accountName, id);
		}
		System.out.println(hmap);
		return hmap;
	}


	public String updateUserAnonymization(String payload) throws Exception {
		String path = suiteData.getAPIMap().get("getUIUserAnonymizationInfo") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		List<NameValuePair> headers = getHeaders();
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		return responseBody;
	}



	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public void remediateExposureWithAPI(SecurletRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getOnedriveRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}	


	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public void remediateSalesforceExposureWithAPI(SecurletRemediation remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = "{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getSalesforceRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatchWithRetry(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}


	/**
	 * This is the utility method to remediate the exposure thro' api. 
	 * @param tenant
	 * @param facility
	 * @param user
	 * @param documentId
	 * @param userId
	 * @param action
	 * @throws Exception
	 */
	public void remediateExposureWithUIServer(UIRemediationObject remediationObject) throws Exception {

		List<NameValuePair> headers = getHeaders();

		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = MarshallingUtils.marshall(remediationObject);//"{\"objects\":[" + MarshallingUtils.marshall(remediationObject) + "]}";

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("getUIRemediation")
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
		HttpResponse response =  restClient.doPost(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
	}


	public ForensicSearchResults getInvestigateLogs(int from, int to, String facility, HashMap<String, String> hmap, String email, 
			String apiServerUrl, String csrfToken, String sessionId, int offset, int limit, String sourceName) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);	

		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = "";
		payload = esQueryBuilder.getESQuery(tsfrom, tsto, facility, hmap, email.toLowerCase(), apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return fsr;

	}



	public String getInvestigateLogsWithFacets(String from, String to, String facility, HashMap<String, String> terms, ArrayList<String> missing, 
			HashMap<String, String> facets, String histoInterval, String timezone, HashMap<String, String> filters, String email, String apiServerUrl, 
			String csrfToken, String sessionId, long offset, long limit, String sourceName) throws Exception {

		int fromValue =  Integer.parseInt(from.replaceAll("[^0-9?!\\.\\-]",""));
		int toValue =  Integer.parseInt(to.replaceAll("[^0-9?!\\.\\-]",""));

		String tsfrom = ""; String tsto = "";

		//If from contains the letter M, then treat it as Minutes
		if(from.contains("M") || to.contains("M")) {
			tsfrom 	= DateUtils.getMinutesFromCurrentTime(fromValue);
			tsto 	= DateUtils.getMinutesFromCurrentTime(toValue);
		} else if (from.contains("D")|| to.contains("D")) {
			tsfrom 	= DateUtils.getDaysFromCurrentTime(fromValue);
			tsto 	= DateUtils.getDaysFromCurrentTime(toValue);
		} else {
			tsfrom 	= DateUtils.getDaysFromCurrentTime(fromValue);
			tsto 	= DateUtils.getDaysFromCurrentTime(toValue);
		}

		//		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

		//Get headers
		List<NameValuePair> headers = getHeaders();
		String payload = "";
		payload = esQueryBuilder.getInvestigateQueryWithFacets(tsfrom, tsto, facility, terms, missing, facets, histoInterval, timezone, filters,  email.toLowerCase(), apiServerUrl, csrfToken, sessionId, offset, limit, sourceName);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);
		//ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		return responseBody;

	}


	public String getFileContents(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while(scanner.hasNextLine()) {        
				fileContents.append(scanner.nextLine() + lineSeparator);
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}


	public Map<String, byte[]> getEmailAttachment(String query, String clientId, String clientSecret, String refreshToken) throws Exception {

		if (clientId == null) {
			clientId = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
		}

		if (clientSecret == null) {
			clientSecret = "YkMqf5GWiHQgHbA1P7BNhxto";
		}

		if(refreshToken == null) {
			refreshToken = "1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";
		}

		GoogleMailServices googleMailServices = new GoogleMailServices(clientId, clientSecret, refreshToken);

		String attachmentName = "";

		Message message = googleMailServices.getLatestMail(query);

		Map<String, byte[]> attachments = googleMailServices.getAttachments(message);

		return attachments;
	}


	public String getEmailDownloadLink(String query, String clientId, String clientSecret, String refreshToken) throws Exception {

		if (clientId == null) {
			clientId = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
		}

		if (clientSecret == null) {
			clientSecret = "YkMqf5GWiHQgHbA1P7BNhxto";
		}

		if(refreshToken == null) {
			refreshToken = "1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";
		}

		GoogleMailServices googleMailServices = new GoogleMailServices(clientId, clientSecret, refreshToken);

		Message message = googleMailServices.getLatestMail(query);
		String downloadHref = googleMailServices.getDownloadHref(message);
		return downloadHref;
	}
	
	public void storeGmailAttachment(String query, String destinationDir, String clientId, String clientSecret, String refreshToken) {
		if (clientId == null) {
			clientId = "998314684213-34jm3g4k92nejio174qnnb32vojbqg0n.apps.googleusercontent.com";
		}

		if (clientSecret == null) {
			clientSecret = "YkMqf5GWiHQgHbA1P7BNhxto";
		}

		if(refreshToken == null) {
			refreshToken = "1/fPDwECsPhJAPh6F_TsHHhz7Q1AArPFC-fa6XdV_bNko";
		}
		GoogleMailServices googleMailServices = new GoogleMailServices(clientId, clientSecret, refreshToken);
		Message message = googleMailServices.getLatestMail(query);
	    googleMailServices.getAndStoreAttacments(message.getId(), destinationDir);
	}

	public String getETagAsDocumentId(String etag) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matchPattern = pattern.matcher(etag);
		matchPattern.find();
		Reporter.log("Document Id as Etag:" + matchPattern.group(1), true);
		//System.out.println(matchPattern.group(1));
		return matchPattern.group(1).toLowerCase();
	}

	protected SecurletRemediation getSalesforceRemediationObject(String userEmail, String userId, String docType, String docId, String instanceId, String currentLink, String[] remedialAction, String[] metaInfo) {
		SecurletRemediation remediation = new SecurletRemediation();

		remediation.setDbName(suiteData.getTenantName().toLowerCase());
		remediation.setUser(userEmail);
		remediation.setUserId(userId);
		remediation.setDocType(docType);
		remediation.setDocId(docId);
		remediation.setInstance(instanceId);

		remediation.setInline(Boolean.TRUE);
		remediation.setIssuer(suiteData.getUsername());
		remediation.setRequestTime(DateUtils.getCurrentTimeWithoutT());


		List<String> readonlyValues = new ArrayList<String>();
		readonlyValues.add("open"); 

		List<RemedialAction> actions = new ArrayList<RemedialAction>();

		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("company-C"); 
		possibleValues.add("company-V");


		for(int i=0; i<remedialAction.length; i++) {
			RemedialAction remedAction = new RemedialAction();

			if(remedialAction[i].equals("UNSHARE")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(readonlyValues);
				remedAction.setPossibleValues(null);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedMetaInfo.setObjectType("unicode");
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);

			}


			if(remedialAction[i].equals("SHARE_ACCESS")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(null);
				remedAction.setPossibleValues(possibleValues);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setAccess(metaInfo[i]);
				remedMetaInfo.setCurrentLink("company"); // currentLink
				remedMetaInfo.setObjectType("unicode");
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			}
		}

		remediation.setActions(actions);
		return remediation;
	}


	protected SecurletRemediation getSalesforceRemediationObject(String userEmail, String userId, String docType, String docId, String instanceId, String currentLink, String[] remedialAction, String[] metaInfo, String[] collabs) {
		SecurletRemediation remediation = new SecurletRemediation();

		remediation.setDbName(suiteData.getTenantName().toLowerCase());
		remediation.setUser(userEmail);
		remediation.setUserId(userId);
		remediation.setDocType(docType);
		remediation.setDocId(docId);
		remediation.setInstance(instanceId);

		remediation.setInline(Boolean.TRUE);
		remediation.setIssuer(suiteData.getUsername());
		remediation.setRequestTime(DateUtils.getCurrentTimeWithoutT());


		List<String> readonlyValues = new ArrayList<String>();
		readonlyValues.add("open"); 

		List<RemedialAction> actions = new ArrayList<RemedialAction>();

		List<String> possibleValues = new ArrayList<String>();
		possibleValues.add("company-C"); 
		possibleValues.add("company-V");

		//Possible values for collabUpdate only
		List<String> possibleValuesCollabUpdate = new ArrayList<String>();
		possibleValuesCollabUpdate.add("C"); 
		possibleValuesCollabUpdate.add("V");

		for(int i=0; i<remedialAction.length; i++) {
			RemedialAction remedAction = new RemedialAction();

			if(remedialAction[i].equals("UNSHARE")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(readonlyValues);
				remedAction.setPossibleValues(null);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setCurrentLink(metaInfo[i]);
				remedMetaInfo.setObjectType("unicode");
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);

			}


			if(remedialAction[i].equals("SHARE_ACCESS")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(null);
				remedAction.setPossibleValues(possibleValues);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				remedMetaInfo.setCollabs(null);
				remedMetaInfo.setAccess(metaInfo[i]);
				remedMetaInfo.setCurrentLink("company"); // currentLink
				remedMetaInfo.setObjectType("unicode");
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			} 

			if(remedialAction[i].equals("COLLAB_REMOVE")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(null);
				remedAction.setPossibleValues(null);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				//Collaborator info is passed thro metainfo
				ArrayList<String> collab = new ArrayList<String>();
				collab.add(collabs[i]);

				remedMetaInfo.setCollabs(collab);
				remedMetaInfo.setAccess(null);
				remedMetaInfo.setCurrentLink(null); // currentLink
				remedMetaInfo.setObjectType(null);
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			} 

			if(remedialAction[i].equals("COLLAB_UPDATE")) {

				remedAction.setCode(remedialAction[i]);
				remedAction.setReadonlyValues(null);
				remedAction.setPossibleValues(possibleValuesCollabUpdate);

				RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
				//Collaborator info is passed thro metainfo
				ArrayList<String> collab = new ArrayList<String>();
				collab.add(collabs[i]);

				remedMetaInfo.setCollabs(collab);
				remedMetaInfo.setAccess(null);
				remedMetaInfo.setRole(metaInfo[i]);
				remedMetaInfo.setCurrentLink(null); // currentLink
				remedMetaInfo.setObjectType("unicode");
				remedAction.setMetaInfo(remedMetaInfo);
				actions.add(remedAction);
			} 
		}

		remediation.setActions(actions);
		return remediation;
	}



	protected SecurletRemediation getSecurletBulkRemediationObject(ArrayList<String> collaboratorName) {
		SecurletRemediation securletRemediation = new SecurletRemediation();

		securletRemediation.setDbName(suiteData.getTenantName());
		securletRemediation.setUser("__ALL_EL__");
		securletRemediation.setUserId("__ALL_EL__");
		securletRemediation.setDocType("__ALL_EL__");
		securletRemediation.setDocId("__ALL_EL__");

		//Meta Info
		RemediationMetaInfo remedMetaInfo = new RemediationMetaInfo();
		remedMetaInfo.setCurrentLink(null);
		remedMetaInfo.setCollabs(collaboratorName);
		remedMetaInfo.setExpireOn(null);
		remedMetaInfo.setAccess(null);

		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		RemedialAction remedialAction = new RemedialAction();
		remedialAction.setCode("COLLAB_REMOVE");
		remedialAction.setPossibleValues(new ArrayList<String>());
		remedialAction.setMetaInfo(remedMetaInfo);
		remedialAction.setCodeName("Remove");
		actions.add(remedialAction);

		securletRemediation.setActions(actions);
		return securletRemediation;
	}	


	public FileSharesInput frameFileShareInputObject(String id[], String sharingType[], String msg) throws Exception {
		FileSharesInput fsi = new FileSharesInput();

		fsi.setMessage(msg);
		ArrayList<Shares> allshares = new ArrayList<Shares>();
		for(int i= 0; i < id.length; i++) {
			Shares shares = new Shares();
			shares.setId(id[i]);
			shares.setSharingType(sharingType[i]);
			allshares.add(shares);
		}
		fsi.setShares(allshares);
		return fsi;
	}

	protected UIExposedDoc getUIPayload(String app, String fileExposureType, String objectType, String exportType, int offset, int limit, boolean isInternal, String searchText, String orderBy) {
		UIExposedDoc payload = new UIExposedDoc();
		UISource uisource = new UISource();
		uisource.setApp(app);
		uisource.setLimit(limit);
		uisource.setOffset(offset);
		uisource.setIsInternal(isInternal);
		uisource.setSearchTextFromTable(searchText);
		uisource.setSearchText(searchText);
		uisource.setOrderBy(orderBy);
		uisource.setExportType(exportType);
		uisource.setObjectType(objectType);
		uisource.setFileExposure(fileExposureType);
		if(exportType != null) {
			if (exportType.equals("risky_docs") || exportType.equals("users")|| exportType.equals("collabs")) {
				uisource.setRequestType(exportType);
			}
		}
		if(exportType != null) {
			if (exportType.equals("ext_collabs")) {
				uisource.setRequestType("collabs");
			}
		}
		
		payload.setSource(uisource);
		return payload;

		/*UIExposedDoc payload = new UIExposedDoc();
		UISource uisource = new UISource();
		uisource.setApp("Office 365");
		uisource.setLimit(20);
		uisource.setOffset(0);
		uisource.setIsInternal(Boolean.TRUE.booleanValue());
		uisource.setSearchTextFromTable("");
		uisource.setOrderBy("name");
		uisource.setExportType("docs");
		uisource.setObjectType("OneDrive");
		payload.setSource(uisource);
		return payload;*/


	}

	protected UIExposedDoc getUIPayload(String app, String fileExposureType, String objectType, String exportType, 
			int offset, int limit, boolean isInternal, String searchText, String orderBy, String vlType, String format, String contentType) {

		UIExposedDoc payload = getUIPayload(app, fileExposureType, objectType, exportType, offset, limit, isInternal, searchText, orderBy);
		payload.getSource().setVulnerabilityType(vlType);
		payload.getSource().setFormat(format);
		payload.getSource().setContentType(contentType);
		return payload;

	}


	public String getScanPolicy(List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();

		String path = suiteData.getAPIMap().get("getScanPolicy");
		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Response body:"+ responseBody);
		//String scanpolicy = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return responseBody;
	}
	
	public String getApiScanPolicy(String tenant, List<NameValuePair> qparams) throws Exception {
		List<NameValuePair> headers = getHeaders();
		//<tenant_dbname>/api/admin/v1/scan_policy/?app_name=<app_name>
		String path = "/securlet365mailbeatlecom/api/admin/v1/scan_policy/56c2d1ede43736776e7e5c17/"; //suiteData.getAPIMap().get("getApiScanPolicy").replace("{tenant}", tenant);
		//System.out.println("Path:" + path);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Response body:"+ responseBody);
		//String scanpolicy = MarshallingUtils.unmarshall(responseBody, ExposureTotals.class);
		return responseBody;
	}
	
	

	public String updateApiScanPolicy(String tenant, String scanpolicy) throws Exception {
		//<tenant_dbname>/api/admin/v1/scan_policy/<id>/
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = scanpolicy;

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("updateApiScanPolicy").replace("{tenant}", tenant).replace("{policyid}", "56c2d1ede43736776e7e5c17");

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, null);
		HttpResponse response =  restClient.doPatch(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
		return responseBody;
	}
	
	public String updateScanPolicy(SelectiveScanPolicy scanpolicy) throws Exception {
		List<NameValuePair> headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		String payload = MarshallingUtils.marshall(scanpolicy);

		Reporter.log("Request body:" + payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String path = suiteData.getAPIMap().get("configureScanPolicy");

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, null);
		HttpResponse response =  restClient.doPost(uri, headers, null, stringEntity);
		String responseBody = ClientUtil.getResponseBody(response);

		Reporter.log("Response body:"+ responseBody, true);
		Reporter.log("Response code:"+ response.getStatusLine().getReasonPhrase(), true);
		return responseBody;
	}
	
	protected boolean checkDocumentInDB(String name, String fileId) throws Exception {
		// TODO Auto-generated method stub
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair("identification", fileId));
		SecurletDocument documents = getExposedDocuments(name, docparams);
		if (documents.getMeta().getTotalCount() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	protected SecurletDocument getDocuments(String name, String fileId) throws Exception {
		// TODO Auto-generated method stub
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair("identification", fileId));
		SecurletDocument documents = getExposedDocuments(name, docparams);
		return documents;
	}
	
	
	public ArrayList<String> searchDisplayLogs(int from, int to, String facilty, String query, String email, String apiServerUrl, String csrfToken, String sessionId) throws Exception {

		Reporter.log("Retrieving the logs from Elastic Search ...", true);
		String tsfrom = DateUtils.getMinutesFromCurrentTime(from);
		String tsto   = DateUtils.getMinutesFromCurrentTime(to);

		//Get headers
		List<NameValuePair> headers = getHeaders();
		//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON +"; charset=UTF-8"));
		ESQueryBuilder esQueryBuilder = new ESQueryBuilder();
		String payload = esQueryBuilder.getSearchQueryForDisplayLogs(tsfrom, tsto, facilty, query, email, apiServerUrl, csrfToken, sessionId);

		Reporter.log("Request body:"+ payload, true);

		//HttpRequest
		String path = suiteData.getAPIMap().get("getInvestigateLogs") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload, "UTF-8"));
		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("==============================================================================");
		Reporter.log("Response body:"+ responseBody, true);

		ForensicSearchResults fsr = MarshallingUtils.unmarshall(responseBody, ForensicSearchResults.class);
		assertEquals(HttpStatus.SC_OK, getResponseStatusCode(response), "Response code verification failed");
		return this.retrieveActualMessages(fsr);
	}
	
	private ArrayList<String> retrieveActualMessages(ForensicSearchResults fsr) {
		ArrayList<String> alist = new ArrayList<String>();

		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		return alist;
	}
	
	
	public List<String> getAllCombinationsOfAList(List<String> inputList, int size) {
		List<List<String>> powerSet = new LinkedList<List<String>>();

		if (size > 1) {
			for (int i = 1; i <= size; i++) {
				powerSet.addAll(combination(inputList, i));
			}
		} else {
			powerSet.addAll(combination(inputList, size));
		}

		List<String> allcombinations = new ArrayList<String>();

		for (List<String> setlist : powerSet) {
			allcombinations.add(StringUtils.join(setlist, ","));
		}
		return allcombinations;
	}


	//Utility method to generate all combinations of a list
	public  <T> List<List<T>> combination(List<T> values, int size) {

		if (0 == size) {
			return Collections.singletonList(Collections.<T> emptyList());
		}

		if (values.isEmpty()) {
			return Collections.emptyList();
		}

		List<List<T>> combination = new LinkedList<List<T>>();

		T actual = values.iterator().next();

		List<T> subSet = new LinkedList<T>(values);
		subSet.remove(actual);

		List<List<T>> subSetCombination = combination(subSet, size - 1);

		for (List<T> set : subSetCombination) {
			List<T> newSet = new LinkedList<T>(set);
			newSet.add(0, actual);
			combination.add(newSet);
		}

		combination.addAll(combination(subSet, size));

		return combination;
	}




	public List<String> generateCombinations(List<String> exposureList, List<String> vltypeList, List<String> filetypeList, List<String> contenttypeList) {
		List<String> resultList = new ArrayList<String>();

		/*List<String> exposureList = new ArrayList<String>(), vltypeList= new ArrayList<String>(),  
				filetypeList=new ArrayList<String>(), contenttypeList=new ArrayList<String>();
		if (exposure != null) {
			exposureList 	 = Arrays.asList(exposure);
		}
		if (vltype != null) {
			vltypeList 	 = Arrays.asList(vltype);
		}
		if (filetype != null) {
			filetypeList 	 = Arrays.asList(filetype);
		}
		if (contenttype != null) {
			contenttypeList 	 = Arrays.asList(contenttype);
		}*/


		LinkedList<List <String>> lists = new LinkedList<List <String>>();
		if(exposureList != null && exposureList.size() > 0 ) { 
			lists.add(exposureList);
		}
		if(vltypeList != null && vltypeList.size() > 0 ) { 
			lists.add(vltypeList);
		}

		if(filetypeList != null && filetypeList.size() > 0 ) { 
			lists.add(filetypeList);
		}

		if(contenttypeList != null &&  contenttypeList.size() > 0  ) { 
			lists.add(contenttypeList);
		}


		//lists.add(list3);

		Set<String> combinations = new TreeSet<String>();
		Set<String> newCombinations;

		for (String s: lists.removeFirst())
			combinations.add(s);

		while (!lists.isEmpty()) {
			List<String> next = lists.removeFirst();
			newCombinations =  new TreeSet<String>();
			for (String s1: combinations) 
				for (String s2 : next) 
					newCombinations.add(s1 +","+ s2);               

			combinations = newCombinations;
		}
		Reporter.log("Number of combinations:" + combinations.size(), true);
		for (String s: combinations) {
			resultList.add(s);
		}
		return resultList;
	}

	/**
	 * This utility will read a csv file and return the each row of a list as hashMap
	 * @param file
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public static List<Map<String, String>> unmarshallCSVIntoList(File file, String sortfield) throws JsonProcessingException, IOException {
		List<Map<String, String>> response = new LinkedList<Map<String, String>>();
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class)
				.with(schema)
				.readValues(file);
		while (iterator.hasNext()) {
			response.add(iterator.next());
		}
		//Reporter.log(response, true);
		String sfield = (sortfield == null) ? "Name" : sortfield;
		Collections.sort(response, new MapComparator(sfield));
		return response;
	}



	public void uncompressgz(String source, String destination) throws Exception {
		FileInputStream fin = new FileInputStream(source);
		BufferedInputStream in = new BufferedInputStream(fin);
		FileOutputStream out = new FileOutputStream(destination);
		GzipCompressorInputStream gzIn = new GzipCompressorInputStream(in);
		final byte[] buffer = new byte[1024];
		int n = 0;
		while (-1 != (n = gzIn.read(buffer))) {
			out.write(buffer, 0, n);
		}
		out.close();
		gzIn.close();
	}
	
	
	public int getLineCountOfAFile(String file) throws Exception {
		LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(file)));
		lnr.skip(Long.MAX_VALUE);
		int nooflines = lnr.getLineNumber();
		//System.out.println(nooflines); 
		lnr.close();
		return nooflines;
	}


}

class MapComparator implements Comparator<Map<String, String>>
{
	private final String key;

	public MapComparator(String key)
	{
		this.key = key;
	}

	public int compare(Map<String, String> first,
			Map<String, String> second)
	{
		// TODO: Null checking, both for maps and values
		String firstValue = first.get(key);
		String secondValue = second.get(key);
		return firstValue.compareTo(secondValue);
	}
}
