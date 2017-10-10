package com.universal.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.google.common.base.Splitter;
import com.universal.constants.BoxConstants;
import com.universal.constants.CommonConstants;
import com.universal.constants.OneDriveBusinessConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxToken;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.onedrive.AccessUrl;
import com.universal.dtos.onedrive.CopyObject;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.FolderInput;
import com.universal.dtos.onedrive.ItemCollection;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.ItemRoleAssignment;
import com.universal.dtos.onedrive.ListItemAllFields;
import com.universal.dtos.onedrive.ListItemValue;
import com.universal.dtos.onedrive.Parameters;
import com.universal.dtos.onedrive.ParentReference;
import com.universal.dtos.onedrive.RecycleBinList;
import com.universal.dtos.onedrive.RecycleItem;
import com.universal.dtos.onedrive.Result;
import com.universal.dtos.onedrive.RoleDefinitions;
import com.universal.dtos.onedrive.SPLists;
import com.universal.dtos.onedrive.ShareType;
import com.universal.dtos.onedrive.SharedLink;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.SiteFileResource;
import com.universal.dtos.onedrive.SiteFolderInput;
import com.universal.dtos.onedrive.SiteFolderUpdateInput;
import com.universal.dtos.onedrive.SiteInput;
import com.universal.dtos.onedrive.SiteResponse;
import com.universal.dtos.onedrive.SiteUserList;
import com.universal.dtos.onedrive.UpdateList;
import com.universal.dtos.onedrive.UserInput;
import com.universal.dtos.onedrive.UserList;
import com.universal.dtos.onedrive.UserValue;
import com.universal.dtos.onedrive.Value;
import com.universal.util.OAuth20Token;
import com.universal.util.TokenProducer;
import com.universal.util.Utility;

public class OneDriveBusiness extends CommonTest implements UniversalCore{

	private TokenProducer tokenProducer;
	private UserAccount userAccount;
	ResourceBundle bundle;
	String tenant;
	String resource;
	String environment;
	String domain;
	String apiHost;
	String sharePointHost;

	public OneDriveBusiness() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public OneDriveBusiness(TokenProducer tokenProducer, UserAccount userAccount) throws Exception {
		this.tokenProducer = tokenProducer;
		this.userAccount   = userAccount;
		bundle = new Utility().getProperties("com.universal.configuration.onedrivebusiness_configuration");
		tenant = StringUtils.split(this.userAccount.getUsername(), "@")[1].replace(".", "");
		domain = StringUtils.split(this.userAccount.getUsername(), "@")[1].toLowerCase();
		sharePointHost = StringUtils.split(domain, ".")[0].toLowerCase();
		//Assign the resource
		if(userAccount.getResource() != null) {
			resource = userAccount.getResource(); 
		}

		//Assign the resource
		if(userAccount.getEnvironment() != null) {
			environment = userAccount.getEnvironment(); 
		}
		
		apiHost = OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_HOST;
		apiHost = apiHost.replace("{tenant}", this.userAccount.getDomainName());

	}

	public <T> T getFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getFolder(String folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T uploadFile(String filename) throws Exception {
		return this.uploadFile("/", filename);
	}

	
	@SuppressWarnings("unchecked")
	public <T> T getFileInfo(String fileId) throws Exception {
		
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		Reporter.log("Headers:" + headers, true);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		ItemResource itemResource = unmarshall(responseBody, ItemResource.class);
		return (T) itemResource;
	}
	

	public <T> T uploadFile(String folderId, String filename) throws Exception {
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}


		if(folderId == null || folderId.equals("/") ) {
			//Retrieve the root folder id
			folderId = ((ItemResource) getDefaultDrive()).getId();//"01CREHNAN6Y2GOVW7725BZO354PWSELRRZ";
		}

		queryParams.add(new BasicNameValuePair("@name.conflictBehavior", "replace"));
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_UPLOAD_BY_PARENT_ID).
				replace("{parent_id}", folderId).
				replace("{filename}", filename);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		FileEntity file = new FileEntity(uploadFile);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), file, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		ItemResource itemResource = unmarshall(responseBody, ItemResource.class);
		return (T) itemResource;
	}

	//onedrivebusiness.uri.uploadFile=/_api/v1.0/me/files/{parent_id}/children/{filename}/content
	public <T> T uploadFile(String folderId, String filename, String destinationFilename) throws Exception {
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}


		if(folderId == null || folderId.equals("/") ) {
			//Retrieve the root folder id
			folderId = ((ItemResource) getDefaultDrive()).getId();//"01CREHNAN6Y2GOVW7725BZO354PWSELRRZ";
		}                                                           

		queryParams.add(new BasicNameValuePair(URLDecoder.decode("%40name.conflictBehavior", "UTF-8"), "replace"));
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_UPLOAD_BY_PARENT_ID).
				replace("{parent_id}", folderId).
				replace("{filename}", destinationFilename);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		FileEntity file = new FileEntity(uploadFile);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), file, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
		}
		queryParams.clear();
		ItemResource itemResource = unmarshall(responseBody, ItemResource.class);
		return (T) itemResource;
	}
	
	
	public ArrayList<HashMap<String, String>> listAllItems(String listId, boolean fetchCompleteList) throws Exception {
		ArrayList<HashMap<String, String>> itemMap = new ArrayList<HashMap<String, String>>();
		
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		//System.out.println(headers);
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json;"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_GET_LIST_ITEMS;
		uriPath = uriPath.replace("{guid}", listId);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		ItemCollection itemlist = unmarshall(strResponse, ItemCollection.class);
		for (ListItemValue lv : itemlist.getValue()) {
			HashMap<String, String> hmap= new HashMap<String, String>();
			hmap.put("odataEditLink", lv.getOdataEditLink());
			hmap.put("id", String.valueOf(lv.getId()));
			hmap.put("title", lv.getTitle());
			itemMap.add(hmap);
		}
		
		if (fetchCompleteList) {
			while(itemlist.getOdataNextLink() != null) {
				uri = new URI(itemlist.getOdataNextLink());
				response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
				strResponse = getResponseBody(response);
				Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
				Reporter.log("Response Body:"+ strResponse, true);
				itemlist = unmarshall(strResponse, ItemCollection.class);
				for (ListItemValue lv : itemlist.getValue()) {
					HashMap<String, String> hmap= new HashMap<String, String>();
					hmap.put("odataEditLink", lv.getOdataEditLink());
					hmap.put("id", String.valueOf(lv.getId()));
					hmap.put("title", lv.getTitle());
					itemMap.add(hmap);
				}
			}
		}
		return itemMap;
	}
	
	
	@SuppressWarnings("unchecked")
	public void deleteListItem(String editLink) throws Exception {
		
		String uriPath =  "/" + editLink;

		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));

		System.out.println("Headers:" + headers);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);

		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
			Reporter.log("File deleted successfully", 1);
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_PRECONDITION_FAILED) {
			Reporter.log("Problem in file deletion. Please check the etag value", 1);
		}
	}
	

	
	public <T> T createFile(String filename) throws Exception {
		
		String payload = "{\"name\":\"" + filename + "\", \"type\" : \"File\"}";
		Reporter.log("Payload:"+ payload, true);
		String uriPath = OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATEFILE_URI;
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		
		
		StringEntity entity = new StringEntity(payload);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File created successfully", true);
		}
		
		ItemResource itemResource = unmarshall(responseBody, ItemResource.class);
		return (T) itemResource;
	}
	
	public void updateFile(String fileid, String filename) throws Exception {
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_UPDATEFILE_URI;
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}

		
		FileEntity file = new FileEntity(uploadFile);
		uriPath = uriPath.replace("{fileid}", fileid);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		
		headers = getHeaders();
		
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, file, null);	
		
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File updated successfully", true);
		}
	}
	
	
	
	public <T> T downloadFile(String fileId, String targetFilename) throws Exception {

		String currentPath = System.getProperty("user.dir");

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DOWNLOAD_BY_ITEM_ID).replace("{file_id}", fileId);
		String downloadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_DOWNLOADSFOLDER_PATH);
		String destinationFile = currentPath + FilenameUtils.separatorsToSystem(downloadsPath) + targetFilename;
		headers = getAuthHeadersOnly();
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String strResponse = response.getStatusLine().getStatusCode() +"::" + response.getStatusLine().getReasonPhrase();
		System.out.println("Response line:"+strResponse);	

		BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destinationFile), false));
		int inByte;
		while((inByte = bis.read()) != -1) {
			bos.write(inByte);
		}
		bis.close();
		bos.close();
		return null;
	}


	@SuppressWarnings("unchecked")
	public void deleteFile(String fileId, String etag) throws Exception {

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		if(etag != null) {
			headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		}
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);

		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
			Reporter.log("File deleted successfully", 1);
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_PRECONDITION_FAILED) {
			Reporter.log("Problem in file deletion. Please check the etag value", 1);
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T renameItem(ItemResource itemResource) throws Exception {

		String payload = "{\"name\":\"" + itemResource.getName()+"\"}"; 

		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_UPDATE_ITEM_PATH).replace("{item-id}", itemResource.getId());
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		if(itemResource.getETag() != null) {
			headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		}

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, ItemResource.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T createSharedLink(String itemId, ShareType shareType) throws Exception {
		String payload = marshall(shareType);
		StringEntity stringEntity = new StringEntity(payload);

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATE_SHAREDLINK_PATH).replace("{item-id}", itemId);
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, SharedLink.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T copyItem(String itemId, CopyObject copyObject, boolean asyncCopy) throws Exception {
		String payload = marshall(copyObject);
		StringEntity stringEntity = new StringEntity(payload);

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_COPYITEM_PATH).replace("{item-id}", itemId);
		headers = getAuthHeadersOnly();
		if(asyncCopy) {
			headers.add(new BasicNameValuePair("Prefer", "respond-async"));
		}

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, SharedLink.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T moveItem(String itemId, ParentReference parent) throws Exception {
		String payload = marshall(parent);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_MOVEITEM_PATH).replace("{item-id}", itemId);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, SharedLink.class);
	}	

	@SuppressWarnings("unchecked")
	public <T> T getDefaultDrive() throws Exception {
		//System.out.println("Access Token:" + this.tokenProducer.getoAuth20Token().getAccessToken());
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DEFAULT_DRIVE);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		System.out.println("Get User Info Response body:"+strResponse);	
		ItemResource itemResource = unmarshall(strResponse, ItemResource.class);
		return (T) itemResource;
	}
	
	


	@SuppressWarnings("unchecked")
	public <T> T createFolder(String folderName, String parentId) throws Exception {
		if(parentId == null || parentId.equals("/")) {
			parentId = ((ItemResource) this.getDefaultDrive()).getId();
		}
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATEFOLDER_URI).
				replace("{parent_id}", parentId).replace("{folder_name}", folderName);

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		System.out.println("Response body:"+strResponse);
		//return null;
		return (T) unmarshall(strResponse, Folder.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T createFolderV2(String folderName) throws Exception {

		String payload = "{\"name\": \""+ folderName + "\", \"folder\": {}}";
		StringEntity entity = new StringEntity(payload);

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATEFOLDERV2_URI);

		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
		String strResponse = getResponseBody(response);
		System.out.println("Response body:"+strResponse);
		//return null;
		return (T) unmarshall(strResponse, Folder.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T createFolderV2(String folderName, String parentId) throws Exception {

		String payload = "{\"name\": \""+ folderName + "\", \"folder\": {}}";
		StringEntity entity = new StringEntity(payload);
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATESUBFOLDERV2_URI).replace("{parent_id}", parentId);
		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		//return null;
		return (T) unmarshall(strResponse, Folder.class);
	}


	public <T> T downloadFileV2(String fileId, String targetFilename) throws Exception {

		String currentPath = System.getProperty("user.dir");

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DOWNLOAD_BY_ITEM_IDV2_URI).replace("{file_id}", fileId);
		String downloadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_DOWNLOADSFOLDER_PATH);
		String destinationFile = currentPath + FilenameUtils.separatorsToSystem(downloadsPath) + targetFilename;
		headers = getAuthHeadersOnly();
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String strResponse = response.getStatusLine().getStatusCode() +"::" + response.getStatusLine().getReasonPhrase();
		System.out.println("Response line:"+strResponse);	

		BufferedInputStream bis = new BufferedInputStream(response.getEntity().getContent());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(destinationFile), false));
		int inByte;
		while((inByte = bis.read()) != -1) {
			bos.write(inByte);
		}
		bis.close();
		bos.close();
		return null;
	}



	@SuppressWarnings("unchecked")
	public void deleteFolderV2(String itemId, String etag) throws Exception {
		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));

		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DELETEFOLDERV2_URI).replace("{item_id}", itemId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		//return null;
	}

	@SuppressWarnings("unchecked")
	public void deleteFolder(String folderId, boolean recursive, String etag) throws Exception {
		headers = getAuthHeadersOnly();
		if(etag != null) {
			headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		}
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DELETEFOLDER_URI).replace("{folder_id}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
	}

	@SuppressWarnings("unchecked")
	public <T> T getFolderProperties(String folderId) throws Exception {
		headers = getAuthHeadersOnly();
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_GETFOLDERPROPERTIES_URI;
		uriPath = uriPath.replace("{folderid}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, Folder.class);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T renameFolder(String folderId, String updatedName) throws Exception {
		String payload = "{\"name\": \"" + updatedName +"\"}";
		
		headers = getAuthHeadersOnly();
		StringEntity stringEntity = new StringEntity(payload);
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_GETFOLDERPROPERTIES_URI;
		uriPath = uriPath.replace("{folderid}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, Folder.class);
		
	}
	
	
	public <T> T updateFileProperties(String folderName) throws Exception {
		String payload = "{\"name\": \""+ folderName + "\", \"folder\": {}}";
		StringEntity entity = new StringEntity(payload);
		String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DELETEFOLDER_URI).replace("{folder_id}", folderName);
		
		//String uriPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_CREATESUBFOLDERV2_URI).replace("{parent_id}", parentId);
		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		//return null;
		return (T) unmarshall(strResponse, ItemResource.class);
	}
	
	
	//Graph apis

	//Api to create user
	@SuppressWarnings("unchecked")
	public <T> T createUser(UserInput userInput) throws Exception {
		String payload = marshall(userInput);
		Reporter.log("user input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		qParams.add(new BasicNameValuePair("api-version", "1.6"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVEGRAPH_API_CREATE_USERS_PATH.replace("{domain}", this.domain);
		URI uri = Utility.getURI(getNetworkProtocol(), OneDriveBusinessConstants.ONEDRIVEGRAPH_API_HOST, getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response:"+ strResponse);
		Value userValue = unmarshall(strResponse, Value.class);
		return (T) userValue;
	}


	//Api to list all users
	@SuppressWarnings("unchecked")
	public <T> T listUsers() throws Exception {
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		qParams.add(new BasicNameValuePair("api-version", "1.6"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVEGRAPH_API_LIST_USERS_PATH.replace("{domain}", this.domain);
		URI uri = Utility.getURI(getNetworkProtocol(), OneDriveBusinessConstants.ONEDRIVEGRAPH_API_HOST, getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		UserList userList = unmarshall(strResponse, UserList.class);
		return (T) userList;
	}


	@SuppressWarnings("unchecked")
	public void deleteUser(Value userValue) throws Exception {
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		qParams.add(new BasicNameValuePair("api-version", "1.6"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVEGRAPH_API_DELETE_USERS_PATH.
				replace("{domain}", this.domain).
				replace("{user-id}", userValue.getObjectId());
		URI uri = Utility.getURI(getNetworkProtocol(), OneDriveBusinessConstants.ONEDRIVEGRAPH_API_HOST, getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode());
	}

	//Api to create user
	@SuppressWarnings("unchecked")
	public <T> T updateUser(Value userValue) throws Exception {
		String payload = marshall(userValue);
		Reporter.log("user input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		qParams.add(new BasicNameValuePair("api-version", "1.6"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVEGRAPH_API_UPDATE_USERS_PATH.
												replace("{domain}", this.domain).
												replace("{user-id}", userValue.getObjectId());
		URI uri = Utility.getURI(getNetworkProtocol(), OneDriveBusinessConstants.ONEDRIVEGRAPH_API_HOST, getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response:"+ strResponse);
		return (T) unmarshall(strResponse, Value.class);
	}

	
	//Sharepoint list apis
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointList() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		//qParams.add(new BasicNameValuePair("api-version", "1.6"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_LIST_USERS_PATH;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		SPLists splist = unmarshall(strResponse, SPLists.class);
		return (T) splist;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public Result  getSharePointDocumentList() throws Exception{
		
		SPLists splists =  getSharePointList();
		Result documentResult = null;
		for (Result result :splists.getD().getResults()) {
			if (result.getEntityTypeName().equals("Documents")) {
				documentResult = result;
			}
		}
		return documentResult;
		
	}
	
	@SuppressWarnings("unchecked")
	public void  updateSharePointList(String guid, UpdateList updateList) throws Exception {
		String payload = marshall(updateList);
		Reporter.log("Payload:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "MERGE"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_UPDATE_LIST_PATH;
		uriPath = uriPath.replace("{guid}", guid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	
	public DocumentSharingResult  shareWithCollaborators(SharingUserRoleAssignment assignment) throws Exception {
		String payload = marshall(assignment);
		Reporter.log("Payload:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_USER_ROLE_ASSIGNMENT;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		DocumentSharingResult docSharingResult = unmarshall(strResponse, DocumentSharingResult.class);
		return docSharingResult;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointUserList() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SITEUSERS;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointRolesAssignments() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROLEASSIGNMENTS;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointRolesDefinitions() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROLEDEFINITIONS;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		RoleDefinitions roleDefinitions = unmarshall(strResponse, RoleDefinitions.class);
		return (T) roleDefinitions ;
	}
	
	public void breakRoleInheritanceForList(String guid, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   "/Lists(guid'"+ guid +"')" + OneDriveBusinessConstants.ONEDRIVESP_API_BREAKROLEINHERITANCE 
				+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		//Reporter.log("Response Body:"+ strResponse, true);
		//SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		//return (T) spuserlist ;
	}
	
	
	public void breakRoleInheritanceForListItem(String guid, String itemId, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   "/Lists(guid'"+ guid +"')/Items("+itemId+")" + OneDriveBusinessConstants.ONEDRIVESP_API_BREAKROLEINHERITANCE 
				+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		//Reporter.log("Response Body:"+ strResponse, true);
		//SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		//return (T) spuserlist ;
	}
	
	
	public void breakRoleInheritanceForListItem(String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_BREAKITEMROLEINHERITANCE ;
		uriPath = uriPath.replace("{itemlink}", itemlink)
				+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		//Reporter.log("Response Body:"+ strResponse, true);
		//SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		//return (T) spuserlist ;
	}
	
	
	public void breakRoleInheritanceForListItemByRelativeUrl(String relativeUrl, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		///_api/web/GetFileByServerRelativeUrl('<file relative url>')/ListItemAllFields/breakroleinheritance(true)
		
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   "/GetFileByServerRelativeUrl('"+ relativeUrl +"')/ListItemAllFields" + OneDriveBusinessConstants.ONEDRIVESP_API_BREAKROLEINHERITANCE 
				+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		//Reporter.log("Response Body:"+ strResponse, true);
		//SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		//return (T) spuserlist ;
	}
	
	
	@SuppressWarnings("unchecked")
	public void copyFileTo(String sourcefileId, String destination, String filename, boolean boverwrite) throws Exception {
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		//String relativePath = "/personal/"+ role + "_" + sptenant + "/Documents/" +sourcefile;
		
		//GetFileById
		//GetFolderById
		
		String destinationPath = "/personal/"+ role + "_" + sptenant + "/Documents/" + destination + "/"+filename;
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_COPYITEM;
		uriPath = uriPath.replace("{fileid}", sourcefileId)
						 .replace("{destination}", destinationPath)
						 .replace("{boverwrite}", String.valueOf(boverwrite)) ;
		
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	@SuppressWarnings("unchecked")
	public void moveFileTo(String sourcefileId, String destination, String filename, int boverwrite) throws Exception {
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		
		//GetFileById
		//GetFolderById
		
		String destinationPath = "/personal/"+ role + "_" + sptenant + "/Documents/" + destination + "/"+filename;
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_MOVEITEM;
		uriPath = uriPath.replace("{fileid}", sourcefileId)
						 .replace("{destination}", destinationPath)
						 .replace("{boverwrite}", String.valueOf(boverwrite)) ;
		
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointItemRolesAssignments(String filename) throws Exception{
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String relativePath = "/personal/"+ role + "_" + sptenant + "/Documents/" +filename;
		
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ITEMROLEASSIGNMENTS;
		uriPath = uriPath.replace("{filename}", relativePath);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ItemRoleAssignment itemRoleAssignment = unmarshall(strResponse, ItemRoleAssignment.class);
		return (T) itemRoleAssignment ;
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointFolderRolesAssignments(String foldername) throws Exception{
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String relativePath = "/personal/"+ role + "_" + sptenant + "/Documents/" +foldername;
		
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_FOLDERROLEASSIGNMENTS;
		uriPath = uriPath.replace("{foldername}", relativePath);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ItemRoleAssignment itemRoleAssignment = unmarshall(strResponse, ItemRoleAssignment.class);
		return (T) itemRoleAssignment ;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSharePointDocumentListRolesAssignments(String uri) throws Exception{
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		URI uripath = new URI(uri);
		HttpResponse response = executeRestRequest(GET_METHOD, uripath, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ItemRoleAssignment itemRoleAssignment = unmarshall(strResponse, ItemRoleAssignment.class);
		return (T) itemRoleAssignment ;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T addSharePointListRolesAssignments(String principalId, String roleDefId) throws Exception {
		
		String baseUrl 	= this.getSharePointDocumentList().getMetadata().getId();
		
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_ADDLISTROLEASSIGNMENT ;
		uriPath = baseUrl + uriPath.replace("{principalid}", principalId)
						 		   .replace("{roleDefId}",   roleDefId);
		URI uri = new URI(uriPath);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		return null;
	}
	
	
	
	
	public void disableSharing(String filename) throws Exception{
		
		ItemRoleAssignment itemRoleAssignment  = getSharePointItemRolesAssignments(filename);
		
		for (UserValue userValue : itemRoleAssignment.getValue()) {
			URI uri = new URI (userValue.getOdataId());
			HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response Body:"+ strResponse, true);
		}
	}
	
	
	public void disableFolderSharing(String filename) throws Exception{

		ItemRoleAssignment itemRoleAssignment  = getSharePointFolderRolesAssignments(filename);

		for (UserValue userValue : itemRoleAssignment.getValue()) {
			URI uri = new URI (userValue.getOdataId());
			HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response Body:"+ strResponse, true);
		}
	}
	
	public void disableDocumentListSharing(Result documentresult) throws Exception{

		ItemRoleAssignment itemRoleAssignment  = getSharePointDocumentListRolesAssignments(documentresult.getRoleAssignments().getDeferred().getUri());

		for (UserValue userValue : itemRoleAssignment.getValue()) {
			URI uri = new URI (userValue.getOdataId());
			HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response Body:"+ strResponse, true);
		}
	}
	
	
	
	public <T> T getRecyleBinItems() throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_RECYCLEBIN;
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		RecycleBinList list = unmarshall(strResponse, RecycleBinList.class);
		return (T) list ;
	}
	
	public void restoreRecyleBinItem(String fileId) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_RECYCLEBIN_ITEM_RESTORE;
		uriPath = uriPath.replace("{fileId}", fileId);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
	}
	
	public <T> T recycleFileItem(String name, boolean isFolder) throws Exception {
		
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String relativePath = "/personal/"+ role + "_" + sptenant + "/Documents/" +name;
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_RECYCLEBIN_ITEM_RECYCLE;
			
		if (isFolder) {
			uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_RECYCLEBIN_FOLDER_RECYCLE;
		}
		uriPath = uriPath.replace("{name}", relativePath);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		RecycleItem item = unmarshall(strResponse, RecycleItem.class);
		return (T) item;
		
	}
	
	public <T> T getListItemAllField(String name, boolean isFolder) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_FILEITEMBYRELATIVEURL;;
		if (isFolder) {
			uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_FOLDERITEMBYRELATIVEURL;
		}
		
		String relativePath = getRelativePathOfDocumentList() + name;
		uriPath = uriPath.replace("{name}", relativePath);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ListItemAllFields list = unmarshall(strResponse, ListItemAllFields.class);
		return (T) list ;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getListItemAllFieldsByUrl(String listname, String fileurl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_LISTITEMALLFIELDS;
		uriPath = uriPath.replace("{listname}", listname).replace("{fileurl}", fileurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ListItemAllFields listitemfields = unmarshall(strResponse, ListItemAllFields.class);
		return (T) listitemfields ;
	}
	
	public void listItemResetRoleInheritance(String itemlink) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_RESETROLEINHERITANCE;
		uriPath = uriPath.replace("{itemlink}", itemlink);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	
	public void addRoleAssignmentForListItem(String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ADDLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{itemlink}", itemlink).replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	
	
	
	
	public void updateListItem(String editlink, String payload) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "MERGE"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_EDITITEM;;
		uriPath = uriPath.replace("{editlink}", editlink);
		
		StringEntity stringEntity = new StringEntity(payload);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	public String getRelativePathOfDocumentList() {
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String relativePath = "/personal/"+ role + "_" + sptenant + "/Documents/";
		return relativePath;
	}
	
	
	
	public SiteResponse createSite(SiteInput siteInput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json;"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_CREATE_SITE;
		StringEntity stringEntity = new StringEntity(marshall(siteInput));
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Reporter.log("Site " +siteInput.getParameters().getUrl() +" created successfully.", true);
		}
		SiteResponse siteresponse = unmarshall(strResponse, SiteResponse.class);
		return siteresponse;
	}
	
	
	public void updateSite(Parameters updateInput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json;"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "MERGE"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_UPDATE_SITE;
		uriPath =   uriPath.replace("{siteurl}", updateInput.getUrl()) ;
		Reporter.log("Request Body:"+marshall(updateInput), true);
		StringEntity stringEntity = new StringEntity(marshall(updateInput));
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
			Reporter.log("Site " +updateInput.getUrl() +" updated successfully.", true);
		}
	}
	
	
	//https://securleto365beatle-my.sharepoint.com/personal/admin_securleto365beatle_com/RestWeb3/_api/web
	public void deleteSite(String siteUrl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json;"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "DELETE"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_DELETE_SITE;
		uriPath =   uriPath.replace("{siteurl}", siteUrl) ;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			Reporter.log("Site " + siteUrl +" deleted successfully.", true);
		}
	}
	
	
	public ItemResource uploadFileToSite(String filename, String foldername, String Siteurl, String destinationFilename) throws Exception {
		String listId = getSiteListGuid(Siteurl, "Documents");
		
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_SITE_SD_FILE_UPLOAD; 
		
		uriPath = uriPath.replace("{foldername}", foldername).replace("{siteurl}", Siteurl).replace("{filename}", destinationFilename)
									.replace("{guid}", listId);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		FileEntity file = new FileEntity(uploadFile);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), file, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
		}
		queryParams.clear();
		ItemResource itemResource = unmarshall(responseBody, ItemResource.class);
		return itemResource;
	}
	
	public String getSiteListGuid(String siteurl, String listname) throws Exception{
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SITE_LIST	;
		uriPath = uriPath.replace("{siteurl}", siteurl);

		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		SPLists splists = unmarshall(strResponse, SPLists.class);
		
		Result documentResult = null;
		for (Result result :splists.getD().getResults()) {
			
			Reporter.log(result.getEntityTypeName(), true);
			if (result.getTitle().equals("Documents")) {
				documentResult = result;
			}
		}
		return documentResult.getId() ;
	}
	
	
	
	//root site methods
	public SiteFileResource uploadFileToRootSite(String rootsiteUrl, String filename, String foldername, String destinationFilename) throws Exception {
		//String listId = getSiteListGuid(Siteurl, "Documents");
		if (foldername == null || foldername == "/") {
			foldername = "Shared Documents";
		}
		
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too...may create an empty file", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_FILE_UPLOAD;
		uriPath = uriPath.replace("{foldername}", foldername).replace("{siteurl}", rootsiteUrl).replace("{filename}", destinationFilename);
									
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		FileEntity file = new FileEntity(uploadFile);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), file, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
		}
		queryParams.clear();
		SiteFileResource itemResource = unmarshall(responseBody, SiteFileResource.class);
		return itemResource;
	}
	
	
	
	public DocumentSharingResult  getRootSiteFileSharedLink(String rootsiteurl, SharingUserRoleAssignment assignment) throws Exception {
		String payload = marshall(assignment);
		Reporter.log("Payload:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_FILE_SHAREDLINK;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		DocumentSharingResult docSharingResult = unmarshall(strResponse, DocumentSharingResult.class);
		return docSharingResult;
	}
	
	
	///sites/securletautomationsite/_api/Web/GetFileByServerRelativeUrl('/sites/securletautomationsite/Shared Documents/shareFile1.txt')/GetPreAuthorizedAccessUrl(24)
	
	public AccessUrl  getRootSiteFilePreAuthorizedAccessUrl(String rootsiteurl, String filerelativeurl, int expirytime) throws Exception {
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; "));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; "));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_FILE_PREAUTHORIZED_ACCESS_URL;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{filerelativeurl}", filerelativeurl).replace("{expirytime}", String.valueOf(expirytime));
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		AccessUrl docSharingResult = unmarshall(strResponse, AccessUrl.class);
		return docSharingResult;
	}
	
	
	public ItemCollection listAllItemsInRootSiteList() throws Exception {
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteLists() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));

		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LISTS;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		SPLists splist = unmarshall(strResponse, SPLists.class);
		return (T) splist;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteDocumentListItems(String rootsiteurl) throws Exception {
		
		String listId = getRootSiteListGuid(rootsiteurl, "Documents");
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json;"));

		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LIST_ITEMS;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl).replace("{guid}", listId);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		ItemCollection itemlist = unmarshall(strResponse, ItemCollection.class);
		return (T) itemlist;
		
	}
	
	
	
	public void restoreRootSiteRecyleBinItem(String rootsiteurl,String id) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_ITEM_RESTORE;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{id}", id);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
	}
	
	public <T> T recycleRootSiteFileItem(String rootsiteurl,  String relativeurl, boolean isFolder) throws Exception {
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_ITEM_RECYCLE;
			
		if (isFolder) {
			uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_RECYCLEBIN_FOLDER_RECYCLE;
		}
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		RecycleItem item = unmarshall(strResponse, RecycleItem.class);
		return (T) item;
		
	}
	
	
	public String getRootSiteListGuid(String rootsiteurl, String listname) throws Exception{
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LISTS	;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl);

		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		SPLists splists = unmarshall(strResponse, SPLists.class);
		
		Result documentResult = null;
		for (Result result :splists.getD().getResults()) {
			
			//Reporter.log(result.getEntityTypeName(), true);
			if (result.getTitle().equalsIgnoreCase(listname)) {
				documentResult = result;
			}
		}
		return documentResult.getId() ;
	}
	
	
	public String listAllFilesForRootSiteFolder(String rootsiteUrl, String foldername) throws Exception {
		if (foldername == null || foldername == "/") {
			foldername = "Shared Documents";
		}
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		System.out.println(headers);
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LIST_FILES;
		uriPath = uriPath.replace("{siteurl}", rootsiteUrl).replace("{foldername}", foldername);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String responseBody = getResponseBody(response);
		System.out.println("response body:" + responseBody);
		return responseBody;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRoleAssignmentForRootSiteItem(String rootsiteurl, String itemlink) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_GETLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{itemlink}", itemlink);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ItemRoleAssignment itemRoleAssignment = unmarshall(strResponse, ItemRoleAssignment.class);
		return (T) itemRoleAssignment ;
		
	}
	
	
	
	public void addRoleAssignmentForRootSiteItem(String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ADDLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{itemlink}", itemlink).replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	public void disableRootSiteItemSharing(String rootsiteurl, String itemlink) throws Exception{
		
		ItemRoleAssignment itemRoleAssignment  = getRoleAssignmentForRootSiteItem(rootsiteurl, itemlink);
		
		for (UserValue userValue : itemRoleAssignment.getValue()) {
			URI uri = new URI (userValue.getOdataId());
			HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response Body:"+ strResponse, true);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteItemAllFieldsByUrl(String rootsiteurl, String listname, String filename) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LISTITEMALLFIELDS;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl).replace("{listname}", listname).replace("{filename}", filename);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ListItemAllFields listitemfields = unmarshall(strResponse, ListItemAllFields.class);
		return (T) listitemfields ;
	}
	
	//This method add role assignments for an item in the list
	
	//Web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(3)
	//https://securletdddo365beatle.sharepoint.com/sites/securletautomationsite/_api/web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(1363)/roleassignments/addroleassignment(principalid=4,roleDefId=1073741827)
	public void addRootSiteRoleAssignmentForListItem(String rootsiteurl, String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_ADDLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{itemlink}", itemlink)
							.replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	public void removeRootSiteRoleAssignmentForListItem(String rootsiteurl, String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_REMOVELISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{itemlink}", itemlink)
							.replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	
	public void breakRoleInheritanceForRootSiteListItem(String rootsiteurl, String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_BREAKITEMROLEINHERITANCE ;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{itemlink}", itemlink)
										+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteUserList(String rootsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_USERS;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteRolesAssignments(String rootsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_ROLEASSIGNMENTS;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRootSiteRolesDefinitions(String rootsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_ROLEDEFINITIONS;
		uriPath = uriPath.replace("{siteurl}", rootsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		RoleDefinitions roleDefinitions = unmarshall(strResponse, RoleDefinitions.class);
		return (T) roleDefinitions ;
	}
	
	public <T> T createRootSiteFolder(String rootsiteurl, SiteFolderInput  sitefolderinput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		StringEntity stringEntity = new StringEntity(marshall(sitefolderinput));
		
		System.out.println(headers);
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_CREATE_FOLDER;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteFileResource siteFileResource = unmarshall(strResponse, SiteFileResource.class);
		return (T) siteFileResource;
	}
	
	public void deleteRootSiteFolder(String rootsiteurl, String relativeurl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "DELETE"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_FOLDER;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String strResponse = getResponseBody(response);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	public <T> T getRootSiteFolderListItemAllFields(String rootsiteurl, String relativeurl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LISTITEMALLFIELDSFOLDER;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String strResponse = getResponseBody(response);
		ListItemAllFields list = unmarshall(strResponse, ListItemAllFields.class);
		Reporter.log("Response Body:"+ strResponse, true);
		return (T) list;
	}
	
	
	public void renameRootSiteFolder(String rootsiteurl, String relativeurl, SiteFolderUpdateInput updateInput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json;odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json;odata=verbose"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "MERGE"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		StringEntity stringEntity = new StringEntity(marshall(updateInput));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ROOTSITE_LISTITEMALLFIELDSFOLDER;
		uriPath = uriPath.replace("{rootsiteurl}", rootsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		
		if (response.getStatusLine().getStatusCode() == 400) {
			String strResponse = getResponseBody(response);
			Reporter.log("Response Body:"+ strResponse, true);
		}
		
	}
	
	//root site methods overhere
	
	
	
	//subsite methods starts
	
	
	public SiteFileResource uploadFileToSubSite(String subsiteurl, String filename, String foldername, String destinationFilename) throws Exception {
		//String listId = getSiteListGuid(Siteurl, "Documents");
		if (foldername == null || foldername == "/") {
			foldername = "Shared Documents";
		}
		
		String currentPath = System.getProperty("user.dir");

		headers = getHeaders();
		System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_UPLOADSFOLDER_PATH);
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too...may create an empty file", true);
				//System.exit(0);
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}
		
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_FILE_UPLOAD;
		uriPath = uriPath.replace("{foldername}", foldername).replace("{subsiteurl}", subsiteurl).replace("{filename}", destinationFilename);
									
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		FileEntity file = new FileEntity(uploadFile);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), file, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK || response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
		}
		queryParams.clear();
		SiteFileResource itemResource = unmarshall(responseBody, SiteFileResource.class);
		return itemResource;
	}
	
	
	
	public DocumentSharingResult  getSubSiteFileSharedLink(String subsiteurl, SharingUserRoleAssignment assignment) throws Exception {
		String payload = marshall(assignment);
		Reporter.log("Payload:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_FILE_SHAREDLINK;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		DocumentSharingResult docSharingResult = unmarshall(strResponse, DocumentSharingResult.class);
		return docSharingResult;
	}
	
	
	///sites/securletautomationsite/_api/Web/GetFileByServerRelativeUrl('/sites/securletautomationsite/Shared Documents/shareFile1.txt')/GetPreAuthorizedAccessUrl(24)
	
	public AccessUrl  getSubSiteFilePreAuthorizedAccessUrl(String subsiteurl, String filerelativeurl, int expirytime) throws Exception {
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; "));
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; "));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_FILE_PREAUTHORIZED_ACCESS_URL;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{filerelativeurl}", filerelativeurl).replace("{expirytime}", String.valueOf(expirytime));
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		AccessUrl docSharingResult = unmarshall(strResponse, AccessUrl.class);
		return docSharingResult;
	}
	
	
	public ItemCollection listAllItemsInSubSiteList() throws Exception {
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteLists() throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));

		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LISTS;
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		SPLists splist = unmarshall(strResponse, SPLists.class);
		return (T) splist;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteDocumentListItems(String subsiteurl) throws Exception {
		
		String listId = getSubSiteListGuid(subsiteurl, "Documents");
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json;"));

		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LIST_ITEMS;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{guid}", listId);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		ItemCollection itemlist = unmarshall(strResponse, ItemCollection.class);
		return (T) itemlist;
		
	}
	
	
	
	public void restoreSubSiteRecyleBinItem(String subsiteurl,String id) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_RECYCLEBIN_ITEM_RESTORE;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{id}", id);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
	}
	
	public <T> T recycleSubSiteFileItem(String subsiteurl,  String relativeurl, boolean isFolder) throws Exception {
		
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_RECYCLEBIN_ITEM_RECYCLE;
			
		if (isFolder) {
			uriPath = OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_RECYCLEBIN_FOLDER_RECYCLE;
		}
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		RecycleItem item = unmarshall(strResponse, RecycleItem.class);
		return (T) item;
		
	}
	
	
	public String getSubSiteListGuid(String subsiteurl, String listname) throws Exception{
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LISTS	;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);

		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		
		SPLists splists = unmarshall(strResponse, SPLists.class);
		
		Result documentResult = null;
		for (Result result :splists.getD().getResults()) {
			
			//Reporter.log(result.getEntityTypeName(), true);
			if (result.getTitle().equalsIgnoreCase(listname)) {
				documentResult = result;
			}
		}
		return documentResult.getId() ;
	}
	
	
	public String listAllFilesForSubSiteFolder(String subsiteurl, String foldername) throws Exception {
		if (foldername == null || foldername == "/") {
			foldername = "Shared Documents";
		}
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		System.out.println(headers);
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LIST_FILES;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{foldername}", foldername);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String responseBody = getResponseBody(response);
		System.out.println("response body:" + responseBody);
		return responseBody;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getRoleAssignmentForSubSiteItem(String subsiteurl, String itemlink) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_GETLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{itemlink}", itemlink);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ItemRoleAssignment itemRoleAssignment = unmarshall(strResponse, ItemRoleAssignment.class);
		return (T) itemRoleAssignment ;
		
	}
	
	
	
	public void addRoleAssignmentForSubSiteItem(String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_ADDLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{itemlink}", itemlink).replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
	}
	
	
	public void disableSubSiteItemSharing(String subsiteurl, String itemlink) throws Exception{
		
		ItemRoleAssignment itemRoleAssignment  = getRoleAssignmentForSubSiteItem(subsiteurl, itemlink);
		
		for (UserValue userValue : itemRoleAssignment.getValue()) {
			URI uri = new URI (userValue.getOdataId());
			HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response Body:"+ strResponse, true);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteItemAllFieldsByUrl(String subsiteurl, String listname, String filename) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LISTITEMALLFIELDS;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{listname}", listname).replace("{filename}", filename);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		ListItemAllFields listitemfields = unmarshall(strResponse, ListItemAllFields.class);
		return (T) listitemfields ;
	}
	
	//This method add role assignments for an item in the list
	
	//Web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(3)
	//https://securletdddo365beatle.sharepoint.com/sites/securletautomationsite/_api/web/Lists(guid'dc94ca7d-d09e-4916-b746-d596d4820cf4')/Items(1363)/roleassignments/addroleassignment(principalid=4,roleDefId=1073741827)
	public void addSubSiteRoleAssignmentForListItem(String subsiteurl, String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_ADDLISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{itemlink}", itemlink)
							.replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	public void removeSubSiteRoleAssignmentForListItem(String subsiteurl, String itemlink, String principalid, String roledefid) throws Exception {
		headers = getAuthHeadersOnly();
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_REMOVELISTITEMROLEASSIGNMENT;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{itemlink}", itemlink)
							.replace("{principalid}", principalid).replace("{roledefid}", roledefid);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	
	public void breakRoleInheritanceForSubSiteListItem(String subsiteurl, String itemlink, boolean copyRoleAssignments, boolean clearSubscopes) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_BREAKITEMROLEINHERITANCE ;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{itemlink}", itemlink)
										+ "(copyRoleAssignments=" + String.valueOf(copyRoleAssignments) + ",clearSubscopes=" + String.valueOf(clearSubscopes)+")";
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteUserList(String subsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_USERS;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteRolesAssignments(String subsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json; odata=verbose"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_ROLEASSIGNMENTS;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteUserList spuserlist = unmarshall(strResponse, SiteUserList.class);
		return (T) spuserlist ;
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSubSiteRolesDefinitions(String subsiteurl) throws Exception{
		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_ROLEDEFINITIONS;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		RoleDefinitions roleDefinitions = unmarshall(strResponse, RoleDefinitions.class);
		return (T) roleDefinitions ;
	}
	
	public <T> T createSubSiteFolder(String subsiteurl, SiteFolderInput  sitefolderinput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		StringEntity stringEntity = new StringEntity(marshall(sitefolderinput));
		
		System.out.println(headers);
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_CREATE_FOLDER;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response Body:"+ strResponse, true);
		SiteFileResource siteFileResource = unmarshall(strResponse, SiteFileResource.class);
		return (T) siteFileResource;
	}
	
	public void deleteSubSiteFolder(String subsiteurl, String relativeurl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "DELETE"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_FOLDER;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String strResponse = getResponseBody(response);
		Reporter.log("Response Body:"+ strResponse, true);
	}
	
	public <T> T getSubSiteFolderListItemAllFields(String subsiteurl, String relativeurl) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json"));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LISTITEMALLFIELDSFOLDER;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		String strResponse = getResponseBody(response);
		ListItemAllFields list = unmarshall(strResponse, ListItemAllFields.class);
		Reporter.log("Response Body:"+ strResponse, true);
		return (T) list;
	}
	
	
	public void renameSubSiteFolder(String subsiteurl, String relativeurl, SiteFolderUpdateInput updateInput) throws Exception {
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "application/json;odata=verbose"));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "application/json;odata=verbose"));
		headers.add(new BasicNameValuePair("X-HTTP-Method", "MERGE"));
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, "*"));
		StringEntity stringEntity = new StringEntity(marshall(updateInput));
		
		String uriPath =   OneDriveBusinessConstants.ONEDRIVESP_API_SUBSITE_LISTITEMALLFIELDSFOLDER;
		uriPath = uriPath.replace("{subsiteurl}", subsiteurl).replace("{relativeurl}", relativeurl);
		
		URI uri = Utility.getURI(getNetworkProtocol(), getSharePointSiteBaseUri(), getPort(), uriPath, null, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		
		Reporter.log("Response Code:"+ response.getStatusLine().getStatusCode(), true);
		
		if (response.getStatusLine().getStatusCode() == 400) {
			String strResponse = getResponseBody(response);
			Reporter.log("Response Body:"+ strResponse, true);
		}
		
	}
	
	
	
	
	//subsite methods ends
	
	
	
	
	public String getSharePointBaseUri() {
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String baseUri = userAccount.getDomainName().toLowerCase()+"-my.sharepoint.com/personal/"+ role + "_" + sptenant +"/_api/web";
		return baseUri;
	}
	
	public String getSharePointUri() {
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String baseUri = userAccount.getDomainName().toLowerCase()+"-my.sharepoint.com/personal/"+ role + "_" + sptenant +"/_api";
		return baseUri;
	}
	
	public String getSharePointTenantBaseUri() {
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String baseUri = userAccount.getDomainName().toLowerCase()+"-my.sharepoint.com/personal/"+ role + "_" + sptenant +"/";
		return baseUri;
	}
	
	public String getSharePointSiteBaseUri() {
		String role = StringUtils.split(userAccount.getUsername(), "@")[0].toLowerCase();
		String sptenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "_").toLowerCase();
		String baseUri = userAccount.getDomainName().toLowerCase()+".sharepoint.com";
		return baseUri;
	}
	
	
	
	//Shared link creation
	public void shareLink(String username,String password,String fileName) throws Exception{
		WebDriver driver = null;
		try{	
			Logger logger = Logger.getLogger("");
			logger.setLevel(Level.OFF);
			
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);


			driver = setWebdriver();
			loginIntoO365(username, password, driver);

			List<WebElement> fileElements = driver.findElements(By.cssSelector(".ms-vb-title"));
			for(int i=0;i<fileElements.size();i++){
				Reporter.log(fileElements.get(i).getText(), true);
				//If the filename has extension, remove it
				if(fileName.contains(".")) {
					fileName = StringUtils.split(fileName, ".")[0];
					Reporter.log("File found:"+ fileName, true);
				}
				//filename = StringUtils
				if(fileElements.get(i).getText().equalsIgnoreCase(fileName)){
					System.out.println("File is found and share file in progress");
					fileElements.get(i).click();
					Thread.sleep(10000);


					List<WebElement> fileElementsButton = driver.
							findElements(By.cssSelector(".ms-qcb-item>button"));
					fileElementsButton.get(3).click();
					Thread.sleep(10000);

					driver.findElement(By.linkText("Get a link")).click();
					Thread.sleep(10000);
					List<WebElement> createLinks = driver.findElements(By.cssSelector(".ms-manageLink-create>a"));
					for(int j=createLinks.size()-1;j>=0;j--){
						createLinks = driver.findElements(By.cssSelector(".ms-manageLink-create>a"));
						createLinks.get(j).click();
						Thread.sleep(5000);
					}

					driver.findElement(By.cssSelector("#CloseBtn")).click();
					Thread.sleep(5000);

					break;
				}

			}

		}finally{	
			driver.quit();
		}	
	}
	
	
	private  void loginIntoO365(String username, String password, WebDriver driver) throws InterruptedException {
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

		
		String loginUrl = "https://login.microsoftonline.com/";
		driver.get(loginUrl);
		Thread.sleep(10000);		

		Reporter.log("Trying to login to onedrive with the credentials:" + username, true);

		driver.findElement(By.xpath(".//*[@id='cred_userid_inputtext']")).sendKeys(username);
		driver.findElement(By.xpath(".//*[@id='cred_password_inputtext']")).sendKeys(password);
		driver.findElement(By.xpath(".//*[@id='cred_sign_in_button']")).click();
		
		Thread.sleep(8000);
		
		try{
			visibilityOfElementLocated(driver, By.cssSelector(".bigtext.tile_primary_name.windows_tile_text.aad_account_tile")).click();
			//driver.findElement(By.cssSelector(".bigtext.tile_primary_name.windows_tile_text.aad_account_tile")).click();
			Thread.sleep(10000);
			driver.findElement(By.cssSelector("#cred_sign_in_button")).click();
			//driver.findElement(By.xpath(".//*[@id='cred_sign_in_button']")).click();
		}catch(Exception e) {}
		
		//driver.findElement(By.cssSelector("#cred_sign_in_button")).click();
		String currentUrl = driver.getCurrentUrl();
		System.out.println(currentUrl);
		
		Reporter.log("Cookies:" + driver.manage().getCookies(), true);
		
		driver.findElement(By.linkText("OneDrive")).click();
		Thread.sleep(10000);
		currentUrl = driver.getCurrentUrl();
		System.out.println(currentUrl);

		Reporter.log("login to onedrive with the credentials:" + username+" is successful", true);
	}
	
	
	public  void unshareLink(String username,String password,String fileName) throws Exception{
		WebDriver driver = null;


		try{	
			Logger logger = Logger.getLogger("");
			logger.setLevel(Level.OFF);
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

			driver = setWebdriver();
			loginIntoO365(username, password, driver);

			List<WebElement> fileElements = driver.findElements(By.cssSelector(".ms-vb-title"));
			for(int i=0;i<fileElements.size();i++){
				System.out.println(fileElements.get(i).getText());
				if(fileElements.get(i).getText().equalsIgnoreCase(fileName)){
					System.out.println("File is found and unshare file in progress");
					fileElements.get(i).click();
					Thread.sleep(5000);


					List<WebElement> fileElementsButton = driver.
							findElements(By.cssSelector(".ms-qcb-item>button"));
					fileElementsButton.get(3).click();
					Thread.sleep(5000);

					driver.findElement(By.linkText("Get a link")).click();
					Thread.sleep(5000);
					List<WebElement> createLinks = driver.findElements(By.cssSelector(".ms-manageLink-disable>a"));
					for(int j=createLinks.size()-1;j>=0;j--){
						createLinks = driver.findElements(By.cssSelector(".ms-manageLink-disable>a"));
						createLinks.get(j).click();
						Thread.sleep(5000);
						driver.findElement(By.cssSelector("#js-disableLinkBtn")).click();
						Thread.sleep(15000);
					}

					driver.findElement(By.cssSelector("#CloseBtn")).click();
					Thread.sleep(5000);

					break;
				}

			}

		}finally{	
			driver.quit();
		}	
	}

	public  void invitePeople(String username,String password,String fileName,String email) throws Exception{
		WebDriver driver = null;


		try{	
			Logger logger = Logger.getLogger("");
			logger.setLevel(Level.OFF);
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

			driver = setWebdriver();
			loginIntoO365(username, password, driver);

			List<WebElement> fileElements = driver.findElements(By.cssSelector(".ms-vb-title"));
			for(int i=0;i<fileElements.size();i++){
				System.out.println(fileElements.get(i).getText());
				if(fileElements.get(i).getText().equalsIgnoreCase(fileName)){
					System.out.println("File is found and invite people by email in progress");
					fileElements.get(i).click();
					Thread.sleep(5000);


					List<WebElement> fileElementsButton = driver.
							findElements(By.cssSelector(".ms-qcb-item>button"));
					fileElementsButton.get(3).click();
					Thread.sleep(5000);

					//driver.findElement(By.cssSelector("#peoplePicker_TopSpan_EditorInput")).clear();
					driver.findElement(By.cssSelector("#peoplePicker_TopSpan_EditorInput")).sendKeys(email);
					driver.findElement(By.id("btnShare")).click();
					Thread.sleep(5000);

					break;
				}

			}

		}finally{	
			driver.quit();
		}	
	}
	
	
	
	private static WebDriver setWebdriver() {
		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);
		return driver;
	}
	
	//Util methods

	private List<NameValuePair> getHeaders() throws Exception {
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByRefreshTokenAndClientId();
		}

		headers.clear();
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken())); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		//Reporter.log("All headers:"+headers, true);
		return headers;


	}

	private List<NameValuePair> getAuthHeadersOnly() throws Exception {
		headers.clear();

		//If the token is expired
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByRefreshTokenAndClientId();
		} 

		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken()));  
		return headers;
	}

	//Oauth related methods
	public boolean initAccessTokenByRefreshTokenAndClientId() throws Exception {
		if (hasRefreshTokenAndClientId()) {
			this.tokenProducer.getoAuth20Token().setAccessToken(getAccessTokenByRefreshTokenAndClientId().getAccessToken());
			return true;
		} else {
			return false;
		}
	}

	private boolean hasRefreshTokenAndClientId() {
		return (StringUtils.isNotEmpty(tokenProducer.getoAuth20Token().getRefreshToken()) && StringUtils.isNotEmpty(tokenProducer.getClientId()));
	}


	private String getHost() {
		return bundle.getString(CommonConstants.ONEDRIVEBUSINESS_HOST);
	}

	private int getPort() {
		if (bundle.getString(CommonConstants.ONEDRIVEBUSINESS_PORT).length() > 0) {
			return Integer.parseInt(bundle.getString(CommonConstants.ONEDRIVEBUSINESS_PORT));
		} else {
			return -1;
		}
	}

	private String getNetworkProtocol() {
		return bundle.getString(CommonConstants.ONEDRIVEBUSINESS_NETWORK_PROTOCOL);
	}

	private String getDefaultDriveUri() {
		return bundle.getString(OneDriveBusinessConstants.ONEDRIVEBUSINESS_API_DEFAULT_DRIVE);
	}

	public OAuth20Token getAccessTokenByRefreshTokenAndClientId() throws Exception {

		String tokenfilepath = null;
		if(this.resource == null) {
			tokenfilepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/"+this.environment+"_onedrivebusiness"+this.tenant+this.userAccount.getRole().toLowerCase()+".token";
		} else {
			//if resource is graph
			tokenfilepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/"+this.environment+"_onedrive"+this.resource+this.tenant+this.userAccount.getRole().toLowerCase()+".token";
		}

		Reporter.log("Token file :" + tokenfilepath, true);
		try {
			if (new File(FilenameUtils.separatorsToSystem(tokenfilepath)).exists()) {
				Reporter.log("Token file found", true);
				BoxToken boxToken = retrieveTokenFromTokenStore(tokenfilepath);
				if(!boxToken.isTokenExpired()) {
					Reporter.log("Token not expired...Retrieved the token from token store...");
					tokenProducer.setoAuth20Token(boxToken.getOauthToken());
					return boxToken.getOauthToken();
				} else {
					Reporter.log("Token expired...Trying to get the token again...", true);
				}
			} else {
				Reporter.log("Token File not found"+tokenfilepath, true);
			}
		}
		catch(InvalidClassException e){
			Reporter.log("Invalid class exception caught "+StringUtils.join(e.getStackTrace()), true);
		}
		
		catch(Exception e){
			Reporter.log("Exception caught "+StringUtils.join(e.getStackTrace()), true);
		}
		
		OAuth20Token oAuth20Token = new OAuth20Token();

		URI uri = Utility.getURI(getNetworkProtocol(), OneDriveBusinessConstants.ONEDRIVEBUSINESS_OAUTH20_HOST, getPort(), OneDriveBusinessConstants.ONEDRIVEBUSINESS_OAUTH20_TOKEN_URL, queryParams, null);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
		formparams.add(new BasicNameValuePair(CommonConstants.REFRESH_TOKEN, tokenProducer.getoAuth20Token().getRefreshToken()));
		formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.REFRESH_TOKEN));
		formparams.add(new BasicNameValuePair(CommonConstants.REDIRECT_URI, "https://eoe.elastica-inc.com/static/ng/appDashboards/index.html"));
		
		if(this.resource == null) {
			String resourceUri = OneDriveBusinessConstants.ONEDRIVEBUSINESS_RESOURCE_URI;
			resourceUri = resourceUri.replace("{tenant}", this.userAccount.getDomainName());
			formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, resourceUri));
			Reporter.log("Resource URI:"+resourceUri, true);
			
		} else if(this.resource.equals("graph")) {
			formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, OneDriveBusinessConstants.ONEDRIVEGRAPH_RESOURCE_URI));
		} else if(this.resource.equals("site")) {
			String resourceUri = OneDriveBusinessConstants.ONEDRIVEBUSINESS_SITECOLLECTION_RESOURCE_URI;
			resourceUri = resourceUri.replace("{tenant}", this.userAccount.getDomainName());
			formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, resourceUri));
		}
		
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);    	
		//Add the entity header application/x-www-form-urlencoded"
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	

		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);
		System.out.println("Form Params:"+formparams);
		String strResponse = getResponseBody(response);
		System.out.println("Response body:"+strResponse);
		System.out.println("Response Code:"+response.getStatusLine().getStatusCode());
		oAuth20Token =  unmarshall(strResponse, OAuth20Token.class); 

		//If the refresh token is expired
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

			int noOfRetries = 5;
			Map<String, String> map = null;
			try {
				map = this.regenerateRefreshTokenUsingCodeFlow();
			} catch (Exception e) {
				for (int i = 0; i < noOfRetries; i++) {
					Reporter.log("Retry count:"+ (i+1), true);
					Reporter.log("Waiting for 60 secs before trying to login again", true);
					Thread.sleep(60000);
					try {
					map = this.regenerateRefreshTokenUsingCodeFlow();
					} catch(Exception e1) {}
				}
			}

			formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
			formparams.add(new BasicNameValuePair(CommonConstants.CODE, map.get(CommonConstants.CODE)));
			formparams.add(new BasicNameValuePair(CommonConstants.SESSION_STATE, map.get(CommonConstants.SESSION_STATE)));
			formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.AUTHORIZATION_CODE));
			formparams.add(new BasicNameValuePair(CommonConstants.REDIRECT_URI, "https://eoe.elastica-inc.com/static/ng/appDashboards/index.html"));
			if(this.resource == null) {
				String resourceUri = OneDriveBusinessConstants.ONEDRIVEBUSINESS_RESOURCE_URI;
				resourceUri = resourceUri.replace("{tenant}", this.userAccount.getDomainName());
				formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, resourceUri));
				Reporter.log("Resource URI:"+resourceUri, true);
				
			} else if(this.resource.equals("graph")) {
				formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, OneDriveBusinessConstants.ONEDRIVEGRAPH_RESOURCE_URI));
				
			} else if(this.resource.equals("site")) {
				String resourceUri = OneDriveBusinessConstants.ONEDRIVEBUSINESS_SITECOLLECTION_RESOURCE_URI;
				resourceUri = resourceUri.replace("{tenant}", this.userAccount.getDomainName());
				formparams.add(new BasicNameValuePair(CommonConstants.RESOURCE, resourceUri));
			}

			//entity = new UrlEncodedFormEntity(formparams); 
			HttpEntity strEntity = new UrlEncodedFormEntity(formparams); //new StringEntity(StringUtils.join(formparams, "&"));

			headers.clear();	    	
			//Add the entity header application/x-www-form-urlencoded"
			headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	
			System.out.println("Headers:"+headers);
			response = executeRestRequest(POST_METHOD, uri, headers, strEntity, null);		
			strResponse = getResponseBody(response);
			System.out.println("Response body:"+strResponse);	
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class);    	    	
			tokenProducer.setoAuth20Token(oAuth20Token);			
		} 

		//Write the token to the properties file again
		updatePropertiesFile();

		//Write the token to the tokenstore
		this.saveTokenToTokenStore();
		return oAuth20Token;
	}

	private  void updatePropertiesFile() throws FileNotFoundException, UnsupportedEncodingException, URISyntaxException {
		String currentPath = System.getProperty("user.dir"); 
		String filepath = null;
		if(this.resource == null) {
			filepath = "/src/main/java/com/universal/properties/"+this.environment+"_onedrivebusiness" + this.tenant + this.userAccount.getRole().toLowerCase() +".properties";
		} else {
			filepath = "/src/main/java/com/universal/properties/"+this.environment+"_onedrive"+ this.resource + this.tenant + this.userAccount.getRole().toLowerCase() +".properties";
		}

		Reporter.log("Property file loaded:" + filepath, true);
		PrintWriter writer = new PrintWriter(currentPath + FilenameUtils.separatorsToSystem(filepath), "UTF-8");

		//userAccount
		writer.println("clientid="+ this.tokenProducer.getClientId());
		writer.println("clientsecret="+ this.tokenProducer.getClientSecret());
		writer.println("refreshtoken="+ this.tokenProducer.getoAuth20Token().getRefreshToken());
		writer.close();

	}

	private  void saveTokenToTokenStore() throws Exception {
		//Update the expires_in time
		//Get the current time added with expires in time and saves that object
		long now = System.currentTimeMillis();
		long expiresOn = now + TimeUnit.SECONDS.toMillis(this.tokenProducer.getoAuth20Token().getExpiresIn());
		BoxToken boxToken = new BoxToken(this.tokenProducer.getoAuth20Token(), expiresOn);

		//String currentPath = System.getProperty("user.dir"); 

		String filepath = null;

		if(this.resource == null) {
			filepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/"+this.environment+"_onedrivebusiness"+this.tenant+this.userAccount.getRole().toLowerCase()+".token";
		} else {
			//if resource is graph
			filepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/"+this.environment+"_onedrive"+this.resource+this.tenant+this.userAccount.getRole().toLowerCase()+".token";
		}

		File f = new File(FilenameUtils.separatorsToSystem(filepath));
		if(!f.exists()) {
			f.createNewFile();
		}

		FileOutputStream fout = new FileOutputStream(FilenameUtils.separatorsToSystem(filepath));
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(boxToken);
		oos.close();
		fout.close();
	}


	private  BoxToken retrieveTokenFromTokenStore(String filepath) throws Exception {
		ObjectInputStream objectInputStream = null;
		FileInputStream   streamIn = null;
		streamIn = new FileInputStream(FilenameUtils.separatorsToSystem(filepath));
		objectInputStream = new ObjectInputStream(streamIn);
		BoxToken boxToken = (BoxToken) objectInputStream.readObject();
		objectInputStream.close();
		streamIn.close();
		return boxToken;
	}


	//Todo need to regenerate the token if refresh token is expired
	private Map<String, String> regenerateRefreshTokenUsingCodeFlow() throws Exception {
		Reporter.log("Trying to generate the refresh token using code flow...");
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);

		// open auth 

		//Auth URL
		String authUrl = "";
		if (this.userAccount.getDomainName().toLowerCase().contains("securleto365beatle")) {
			authUrl = "https://login.microsoftonline.com/b52845f2-d1e7-4bdf-afe6-cfaa4e2c9d2e/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("gwo365beatle")) {
			authUrl = "https://login.microsoftonline.com/ff37d92d-a779-40df-b323-0994d71639ed/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("dcio365beatle")) {
			authUrl = "https://login.microsoftonline.com/21b9e37d-7690-4138-829e-053cdcd5b3eb/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("protecto365autobeatle")) {
			authUrl = "https://login.microsoftonline.com/72f19fa9-6fb3-4d0c-8c1f-3eea63cf2504/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("o365securitynet")) {
			authUrl = "https://login.microsoftonline.com/ddabd284-11be-4de4-bf90-ceac0063cbc7/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("securlet365mailbeatle")) {
			authUrl = "https://login.microsoftonline.com/ecba5b98-6293-4ed0-ab98-7de90eecf4ec/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("securleto365featle")) {
			authUrl = "https://login.microsoftonline.com/59269456-9ebd-4389-b90f-41a6164c8b51/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("securletautoo365featle")) {
			authUrl = "https://login.microsoftonline.com/9ade1a92-d383-4b59-bd68-717e7c63fd58/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else if (this.userAccount.getDomainName().toLowerCase().contains("securletdddo365beatle")) {
			authUrl = "https://login.microsoftonline.com/2b437fb9-66b6-4178-a0fa-c8ce9248ea9d/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";
		}else {
			authUrl = "https://login.microsoftonline.com/oauth2/authorize?response_type=code&client_id="+tokenProducer.getClientId()+"&redirect_uri=https://eoe.elastica-inc.com/static/ng/appDashboards/index.html";	
		}
		Reporter.log("Auth url:"+authUrl, true);
		driver.get(authUrl);
		Reporter.log("Waiting for the page to load ...", true);
		Thread.sleep(CommonConstants.THIRTY_SECONDS_SLEEP);		

		// Print the title
		Reporter.log("Trying to login to onedrive with the credentials:" + userAccount.getUsername() + "/" +userAccount.getPassword(), true);

		driver.findElement(By.xpath(".//*[@id='cred_password_inputtext']")).sendKeys(userAccount.getPassword());
		driver.findElement(By.xpath(".//*[@id='cred_userid_inputtext']")).sendKeys(userAccount.getUsername());
		driver.findElement(By.xpath(".//*[@id='cred_sign_in_button']")).click();
		
		try{
			visibilityOfElementLocated(driver, By.cssSelector(".bigtext.tile_primary_name.windows_tile_text.aad_account_tile")).click();
			//driver.findElement(By.cssSelector(".bigtext.tile_primary_name.windows_tile_text.aad_account_tile")).click();
		}
		catch(Exception e) {}
		
		try{
			Thread.sleep(8000);
			driver.findElement(By.xpath(".//*[@id='cred_sign_in_button']")).click();
		}catch(Exception e){
			
		}
		
		String currentUrl = driver.getCurrentUrl();

		String decodedUrl = URLDecoder.decode(currentUrl, "UTF-8");
		Reporter.log("Current url:"+currentUrl, true);
		Reporter.log("Encoded Utils:"+decodedUrl, true);

		String query = decodedUrl.split("\\?")[1];
		final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);
		Reporter.log("Code:" + map.get("code"), true);
		Reporter.log("Session state:" + map.get("session_state"), true);

		driver.quit();
		return map;
	}
	
	public WebElement visibilityOfElementLocated(WebDriver driver, By locator){
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, 120);
        element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element;
    }

	

	
}
