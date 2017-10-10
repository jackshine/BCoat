package com.universal.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Reporter;

import com.gargoylesoftware.htmlunit.util.UrlUtils;
import com.universal.constants.CommonConstants;
import com.universal.constants.SalesforceConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxToken;
import com.universal.dtos.salesforce.APIVersion;
import com.universal.dtos.salesforce.Case;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.Comment;
import com.universal.dtos.salesforce.CommentInput;
import com.universal.dtos.salesforce.FeedItem;
import com.universal.dtos.salesforce.FeedItemInput;
import com.universal.dtos.salesforce.FileShareLink;
import com.universal.dtos.salesforce.FileShares;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.FilesList;
import com.universal.dtos.salesforce.GroupDetail;
import com.universal.dtos.salesforce.GroupInput;
import com.universal.dtos.salesforce.InternalFileShare;
import com.universal.dtos.salesforce.Lead;
import com.universal.dtos.salesforce.LeadInput;
import com.universal.dtos.salesforce.Like;
import com.universal.dtos.salesforce.ProfileRecords;
import com.universal.dtos.salesforce.Resources;
import com.universal.dtos.salesforce.SObject;
import com.universal.dtos.salesforce.SObjectInput;
import com.universal.util.OAuth20Token;
import com.universal.util.TokenProducer;
import com.universal.util.Utility;

public class Salesforce extends CommonTest{
	private TokenProducer tokenProducer;
	private UserAccount userAccount;
	ResourceBundle bundle;
	String tenant;
	String apiHost;

	public Salesforce(UserAccount userAccount) throws Exception{

	}

	public Salesforce(TokenProducer tokenProducer, UserAccount userAccount) throws Exception {
		this.tokenProducer = tokenProducer;
		this.userAccount   = userAccount;
		bundle = new Utility().getProperties("com.universal.configuration.salesforce_configuration");
		
		apiHost = userAccount.getApiHost();
		
		System.out.println("Api Host:"+apiHost);
		
		if(apiHost == null) {
			apiHost = SalesforceConstants.SF_API_HOST;
		}
		
		System.out.println("Api Host:"+apiHost);
		int dotcount = StringUtils.countMatches(userAccount.getUsername(), ".");
		if (dotcount >= 2) {
			//This is added for salesforce users appended with the sandbox name
			String valid = userAccount.getUsername().substring(0,userAccount.getUsername().lastIndexOf("."));
			this.tenant = StringUtils.split(valid, "@")[1].replace(".", "");
		} else {
			this.tenant = StringUtils.split(userAccount.getUsername(), "@")[1].replace(".", "");
		}
	}

	
	
	public ChatterFile uploadFileToChatter(String filename, String destinationFilename) throws Exception {
		String currentPath = System.getProperty("user.dir");
		String path = SalesforceConstants.SF_CHATTER_UPLOAD_URI;
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), path, queryParams, null);
		
		headers = this.getAuthHeadersOnly();
		//headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
		String uploadsPath = bundle.getString(SalesforceConstants.SF_UPLOADS_PATH);
		
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());
		
		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				return null;
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}
		
		String attributes = "{\"title\":\""+destinationFilename+ "\"}";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
		builder.addTextBody("json", attributes, ContentType.APPLICATION_JSON);
		builder.addBinaryBody("fileData", uploadFile, ContentType.APPLICATION_OCTET_STREAM, destinationFilename);		
		HttpEntity multipart = builder.build();
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, multipart, null);	
		String responseBody = getResponseBody(response);
		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		ChatterFile chatterfile = unmarshall(responseBody, ChatterFile.class);
		
		//If file uploaded successfully
		if (this.getResponseStatusCode(response) == HttpStatus.SC_OK || this.getResponseStatusCode(response) == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
			return chatterfile;	
		} else {
			Reporter.log("File upload failed", true);
			return null;
		}
	}
	
	public ChatterFile uploadFileToChatterGroup(String filename, String destinationFilename, String groupId) throws Exception {
		String currentPath = System.getProperty("user.dir");
		String path = SalesforceConstants.SF_CHATTER_FEEDELEMENT_CREATE_URI;
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), path, queryParams, null);
		
		headers = this.getAuthHeadersOnly();
		//headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		//headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));
		String uploadsPath = bundle.getString(SalesforceConstants.SF_UPLOADS_PATH);
		
		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());
		
		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);
			
			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				return null;
			}
			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}
		
		String attributes = "{\"attachment\": {\"attachmentType\":\"NewFile\",\"description\":\"File uploaded to group\",\"title\":\"Group Post.txt\"},\"feedElementType\" : \"FeedItem\",\"subjectId\" :\"0F95B0000008Oz9\"}";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
		builder.addTextBody("json", attributes, ContentType.APPLICATION_JSON);
		builder.addBinaryBody("feedElementFileUpload", uploadFile, ContentType.APPLICATION_OCTET_STREAM, destinationFilename);		
		HttpEntity multipart = builder.build();
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, multipart, null);	
		String responseBody = getResponseBody(response);
		ByteArrayOutputStream out = new ByteArrayOutputStream((int) multipart.getContentLength());

		// write content to stream
		multipart.writeTo(out);

		// either convert stream to string
		String retSrc = out.toString();
		 Reporter.log("Request body:"+retSrc, true);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		ChatterFile chatterfile = unmarshall(responseBody, ChatterFile.class);
		
		//If file uploaded successfully
		if (this.getResponseStatusCode(response) == HttpStatus.SC_OK || this.getResponseStatusCode(response) == HttpStatus.SC_CREATED) {
			Reporter.log("File uploaded successfully", true);
			return chatterfile;	
		} else {
			Reporter.log("File upload failed", true);
			return null;
		}
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void deleteFile(String fileId) throws Exception {
		String path = SalesforceConstants.SF_CHATTER_FILE_DELETE_URI;
		String uriPath = path.replace("{fileid}", fileId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		headers = this.getAuthHeadersOnly();HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
			Reporter.log("File deleted successfully", 1);
		}
	}
	
	
	
	public List<NameValuePair> getToken() throws Exception {
		return this.getAuthHeadersOnly();
	}
	
	
	//This method returns the available api versions for the organization
	public APIVersion[] getAvailableVersions() throws Exception {
		String uriPath = SalesforceConstants.SF_API_VERSIONS;
		headers = getAuthHeadersOnly();
		
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		APIVersion[] versions  = unmarshall(responseBody, APIVersion[].class);
		return versions;
	}
	
	public Resources getResourcesByVersion(String version) throws Exception {
		String uriPath = SalesforceConstants.SF_RESOURCES_BY_VERSIONS;
		uriPath = uriPath.replace("{version}", version);
		headers = getAuthHeadersOnly();
		
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		Resources resources  = unmarshall(responseBody, Resources.class);
		return resources;
	}
	
	
	//Create a chatter group
	public GroupDetail createGroup(GroupInput groupInput) throws Exception {
		String payload = marshall(groupInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_GROUP_CREATE_URI;
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		GroupDetail groupdetail = unmarshall(responseBody, GroupDetail.class);
		return groupdetail;
	}
	
	//Retrieve info about a chatter group
	public GroupDetail getGroup(String groupId) throws Exception {
		Reporter.log("Get the chatter group with id "+groupId, true);
		String uriPath = SalesforceConstants.SF_CHATTER_GROUP_GET_URI;
		uriPath = uriPath.replace("{groupid}", groupId);
		
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		
		GroupDetail groupdetail = unmarshall(responseBody, GroupDetail.class);
		return groupdetail;
	}
	
	//Retrieve info about a chatter group
	public int deleteGroup(String groupId) throws Exception {
		Reporter.log("Delete the chatter group with id "+groupId, true);
		String uriPath = SalesforceConstants.SF_CHATTER_GROUP_DELETE_URI;
		uriPath = uriPath.replace("{groupid}", groupId);
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		return response.getStatusLine().getStatusCode();
	}

	
	//post a feed element
	public FeedItem createFeedElement(FeedItemInput feedItemInput) throws Exception {
		String payload = marshall(feedItemInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_FEEDELEMENT_CREATE_URI;
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FeedItem feedItem = unmarshall(responseBody, FeedItem.class);
		return feedItem;
	}
	
	public FeedItem updateFeedElement(String feedElementId, FeedItemInput feedItemInput) throws Exception {
		String payload = marshall(feedItemInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_FEEDELEMENT_UPDATE_URI;
		uriPath = uriPath.replace("{feedelementid}", feedElementId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FeedItem feedItem = unmarshall(responseBody, FeedItem.class);
		return feedItem;
	}
	
	
	public FeedItem shareFeedElement(FeedItemInput feedItemInput) throws Exception {
		String payload = marshall(feedItemInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_FEEDELEMENT_CREATE_URI;
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FeedItem feedItem = unmarshall(responseBody, FeedItem.class);
		return feedItem;
	}
	
	public Comment postComment(String feedElementId, CommentInput commentInput) throws Exception {
		String payload = marshall(commentInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_COMMENT_CREATE_URI;
		uriPath = uriPath.replace("{feedelementid}", feedElementId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Comment comment = unmarshall(responseBody, Comment.class);
		return comment;
	}
	
	
	public Comment updateComment(String commentId, CommentInput commentInput) throws Exception {
		String payload = marshall(commentInput);
		Reporter.log("Input:"+payload, true);
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = SalesforceConstants.SF_CHATTER_COMMENT_UPDATE_URI;
		uriPath = uriPath.replace("{commentid}", commentId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Comment comment = unmarshall(responseBody, Comment.class);
		return comment;
	}
	
	
	public Like likeFeedItem(String feedElementId) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FEEDELEMENTLIKE_URI;
		uriPath = uriPath.replace("{feedelementid}", feedElementId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Like like = unmarshall(responseBody, Like.class);
		return like;
	}
	
	
	public FileShareLink createFileShareLink(String fileId) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FILESHARELINK_URI;
		uriPath = uriPath.replace("{fileid}", fileId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FileShareLink fileShareLink = unmarshall(responseBody, FileShareLink.class);
		return fileShareLink;
	}
	
	public void deleteFileShareLink(String fileId) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FILESHARELINK_URI;
		uriPath = uriPath.replace("{fileid}", fileId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}
	
	public FileShareLink getFileShareLink(String fileId) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FILESHARELINK_URI;
		uriPath = uriPath.replace("{fileid}", fileId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		FileShareLink fileShareLink = null;
		
		if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			fileShareLink = unmarshall(responseBody, FileShareLink.class);
		} 
		return fileShareLink;
	}
	

	public Lead createLead(LeadInput leadInput) throws Exception {
		String uriPath = SalesforceConstants.SF_LEAD_CREATE_URI;
		
		Reporter.log("Input:"+marshall(leadInput), true);
		StringEntity stringEntity = new StringEntity(marshall(leadInput));
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Lead lead = unmarshall(responseBody, Lead.class);
		return lead;
	}
	
	public void updateLead(LeadInput leadInput, String leadId) throws Exception {
		String uriPath = SalesforceConstants.SF_LEAD_UPDATE_URI;
		
		uriPath = uriPath.replace("{leadid}", leadId);
		Reporter.log("Input:"+marshall(leadInput), true);
		StringEntity stringEntity = new StringEntity(marshall(leadInput));
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}
	

	public void deleteLead(String leadId) throws Exception {
		String uriPath = SalesforceConstants.SF_LEAD_DELETE_URI;
		uriPath = uriPath.replace("{leadid}", leadId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}
	
	
	public Lead getLead(String leadId) throws Exception {
		String uriPath = SalesforceConstants.SF_LEAD_GET_URI;
		uriPath = uriPath.replace("{leadid}", leadId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Lead lead = unmarshall(responseBody, Lead.class);
		return lead;
	}
	
	public SObject createSObject(String sObjectName, SObjectInput sObjectInput) throws Exception {
		String uriPath = SalesforceConstants.SF_SOBJECT_CREATE_URI;
		
		uriPath = uriPath.replace("{sobjectname}", sObjectName);
		Reporter.log("Input:"+marshall(sObjectInput), true);
		StringEntity stringEntity = new StringEntity(marshall(sObjectInput));
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		Reporter.log("SObject "+sObjectName + " created.", true);
		SObject sobject = unmarshall(responseBody, SObject.class);
		return sobject;
	}
	
	public void updateSObject(String sObjectName, SObjectInput sObjectInput, String sObjectId) throws Exception {
		String uriPath = SalesforceConstants.SF_SOBJECT_UPDATE_URI;
		uriPath = uriPath.replace("{sobjectname}", sObjectName)
						 .replace("{sobjectid}", sObjectId);
		
		Reporter.log("Input:"+marshall(sObjectInput), true);
		StringEntity stringEntity = new StringEntity(marshall(sObjectInput));
		
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PATCH_METHOD, uri, headers, stringEntity, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}
	

	public void deleteSObject(String sObjectName, String sObjectId) throws Exception {
		String uriPath = SalesforceConstants.SF_SOBJECT_DELETE_URI;
		uriPath = uriPath.replace("{sobjectname}", sObjectName)
				 		 .replace("{sobjectid}", sObjectId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}
	
	
	public SObject getSObject(String sObjectName, String sObjectId) throws Exception {
		String uriPath = SalesforceConstants.SF_SOBJECT_GET_URI;
		uriPath = uriPath.replace("{sobjectname}", sObjectName)
		 		 		 .replace("{sobjectid}", sObjectId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		SObject sobject = unmarshall(responseBody, SObject.class);
		return sobject;
	}	
	
	public Case getCaseObject(String sObjectName, String sObjectId) throws Exception {
		String uriPath = SalesforceConstants.SF_SOBJECT_GET_URI;
		uriPath = uriPath.replace("{sobjectname}", sObjectName)
		 		 		 .replace("{sobjectid}", sObjectId);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		Case sobject = unmarshall(responseBody, Case.class);
		return sobject;
	}	
	
	/**
	 * This method return the file shared information
	 * if linkshare is null, it means public link is not available
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public FileShares getFileShares(String fileid) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FILESHARES_URI;
		uriPath = uriPath.replace("{fileid}", fileid);
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FileShares shares = unmarshall(responseBody, FileShares.class);
		return shares;
	}
	
	/**
	 * Share the file with collaborator or external user
	 * @param fileid
	 * @param fileSharesInput
	 * @return
	 * @throws Exception
	 */
	public FileShares shareFilewithCollaborator(String fileid, FileSharesInput fileSharesInput) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_FILESHARES_URI;
		uriPath = uriPath.replace("{fileid}", fileid);
		headers = getHeaders();
		StringEntity stringEntity = new StringEntity(marshall(fileSharesInput));
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FileShares shares = unmarshall(responseBody, FileShares.class);
		return shares;
	}
	
	/**
	 * This method return the profile id of chatter free user profile
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public String getChatterFreeUserProfileId() throws Exception {
		String uriPath = SalesforceConstants.SF_API_QUERY;
		
		headers = getHeaders();
		queryParams.clear();
		queryParams.add(new BasicNameValuePair("q", "select Id from Profile where Name = 'Chatter Free User' limit 1"));  
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		ProfileRecords profileRecords = unmarshall(responseBody, ProfileRecords.class);
		queryParams.clear();
		return profileRecords.getRecords().get(0).getId();
	}
	
	/**
	 * This method return the profile id of chatter free user profile
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public String getChatterExternalUserProfileId() throws Exception {
		String uriPath = SalesforceConstants.SF_API_QUERY;
		
		headers = getHeaders();
		queryParams.clear();
		queryParams.add(new BasicNameValuePair("q", "select Id from Profile where Name = 'Chatter External User' limit 1"));  
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		ProfileRecords profileRecords = unmarshall(responseBody, ProfileRecords.class);
		queryParams.clear();
		return profileRecords.getRecords().get(0).getId();
	}
	
	
	
	/**
	 * This method return the all user profiles
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public ProfileRecords getUserProfiles() throws Exception {
		String uriPath = SalesforceConstants.SF_API_QUERY;
		
		headers = getHeaders();
		queryParams.clear();
		queryParams.add(new BasicNameValuePair("q", "select Id,Name from Profile"));  
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		ProfileRecords profileRecords = unmarshall(responseBody, ProfileRecords.class);
		queryParams.clear();
		return profileRecords;
	}
	
	/**
	 * This method execute the soql query and return the results
	 * @param fileid
	 * @return
	 * @throws Exception
	 */
	public String executeQuery(String query) throws Exception {
		String uriPath = SalesforceConstants.SF_API_QUERY;
		
		headers = getHeaders();
		queryParams.clear();
		queryParams.add(new BasicNameValuePair("q", query));  
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);		
		queryParams.clear();
		return responseBody;
	}
	
	public String getUserId(String email, String lastName, String alias, String communityNickName) throws Exception {
		
		String results = this.executeQuery("SELECT Id FROM User Where Username='"+email+"'");
		ProfileRecords profileRecords = unmarshall(results, ProfileRecords.class);
		if (profileRecords.getTotalSize().intValue() >= 1) {
			return profileRecords.getRecords().get(0).getId();
		} else {
			String freeUserProfileId = getChatterFreeUserProfileId();
			SObjectInput sInput = new SObjectInput();
			sInput.setUsername(email);
			sInput.setAlias(alias);
			sInput.setCommunityNickname(communityNickName);
			sInput.setTimeZoneSidKey("America/Los_Angeles");
			sInput.setLocaleSidKey("en_GB");
			sInput.setEmailEncodingKey("ISO-8859-1");
			sInput.setLanguageLocaleKey("en_US");
			sInput.setEmail(email);
			sInput.setProfileId(freeUserProfileId);
			sInput.setLastName(lastName);
			SObject userObject = this.createSObject("User", sInput);
			return userObject.getId();
		}
	}
	
	
	public String getExternalUserId(String email, String lastName, String alias, String communityNickName) throws Exception {
		
		String results = this.executeQuery("SELECT Id FROM User Where Username='"+email+"'");
		ProfileRecords profileRecords = unmarshall(results, ProfileRecords.class);
		if (profileRecords.getTotalSize().intValue() >= 1) {
			return profileRecords.getRecords().get(0).getId();
		} else {
			String externalUserProfileId = getChatterExternalUserProfileId();
			SObjectInput sInput = new SObjectInput();
			sInput.setUsername(email);
			sInput.setAlias(alias);
			sInput.setCommunityNickname(communityNickName);
			sInput.setTimeZoneSidKey("America/Los_Angeles");
			sInput.setLocaleSidKey("en_GB");
			sInput.setEmailEncodingKey("ISO-8859-1");
			sInput.setLanguageLocaleKey("en_US");
			sInput.setEmail(email);
			sInput.setProfileId(externalUserProfileId);
			sInput.setLastName(lastName);
			SObject userObject = this.createSObject("User", sInput);
			return userObject.getId();
		}
	}
	
	
	public void cleanChatterFiles() throws Exception {
		FilesList files;
		do {

			String uriPath = SalesforceConstants.SF_CHATTER_UPLOAD_URI;
			headers = getHeaders();

			URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
			HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
			String responseBody = getResponseBody(response);
			Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
			Reporter.log("response body:" + responseBody, true);
			files = unmarshall(responseBody, FilesList.class);

			for (ChatterFile chatterfile : files.getFiles()) {
				this.deleteFile(chatterfile.getId());
			}
			
		} while(files.getNextPageUrl() != null);
	}
	
	
	public FilesList getChatterFiles() throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_UPLOAD_URI;
		headers = getHeaders();
		
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		FilesList files = unmarshall(responseBody, FilesList.class);
		return files;
		
	}
	
	
	public SObject shareFileInternally(InternalFileShare ifs) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_CONTENTDOCUMENTLINK_URI;
		headers = getHeaders();
		Reporter.log("Request body:" + marshall(ifs), true);
		StringEntity stringEntity = new StringEntity(marshall(ifs));
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		SObject shares = unmarshall(responseBody, SObject.class);
		return shares;
	}
	
	public void removeInternalShare(String fileId) throws Exception {
		String uriPath = SalesforceConstants.SF_CHATTER_CONTENTDOCUMENTLINK_URI +"/"+fileId;
		headers = getHeaders();
		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		
	}
	
	public void removeCollaborator(String fileId, String shareType) throws Exception {
		String query = "SELECT Title, (SELECT Id, ContentDocumentId, LinkedEntityId, ShareType, Visibility FROM ContentDocumentLinks Where ShareType='"+shareType+"')+FROM+ContentDocument+WHERE+Id+in+('"+fileId+"')";
		String resultset = this.executeQuery(URLDecoder.decode(query, "UTF-8"));
		
		JSONObject jso = new JSONObject(resultset);
		JSONArray jsa = jso.getJSONArray("records");
		JSONObject cda = jsa.getJSONObject(0);
		JSONObject cdl = cda.getJSONObject("ContentDocumentLinks");
		JSONArray cdlrecords = cdl.getJSONArray("records");
		JSONObject cdlRecObj = cdlrecords.getJSONObject(0);
		String cdlId = cdlRecObj.getString("Id");
		Reporter.log("Content Document Link:"+cdlId, true);
		this.removeInternalShare(cdlId);
	}
	
	

	private List<NameValuePair> getHeaders() throws Exception {
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByUsernameAndPassword();
		}

		headers.clear();
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken())); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		
		Reporter.log(HttpHeaders.AUTHORIZATION + " Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken(), true);
		return headers;
	}

	private List<NameValuePair> getAuthHeadersOnly() throws Exception {
		headers.clear();

/*		//If the token is expired
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByUsernameAndPassword();
		} 
*/
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, " Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken()));  
		return headers;
	}


	public boolean initAccessTokenByUsernameAndPassword() throws Exception {
		if (userAccount.getUsername() != null || userAccount.getPassword() != null) {
			
			OAuth20Token oauthToken= getAccessTokenByUsernameAndPassword();
			
			this.tokenProducer.getoAuth20Token().setAccessToken(oauthToken.getAccessToken());
			
			if (oauthToken.getId() !=null) { 
				this.tokenProducer.getoAuth20Token().setId(oauthToken.getId());
			}
			
			if (oauthToken.getInstanceUrl() !=null) { 
				this.tokenProducer.getoAuth20Token().setInstanceUrl(oauthToken.getInstanceUrl());
			}
			
		}
		return false;
	}




	public boolean initAccessTokenByRefreshTokenAndClientId() throws Exception {
		if (hasRefreshTokenAndClientId()) {
			if (userAccount.getUserType() == null || userAccount.getUserType().equals("INTERNAL")) {
				this.tokenProducer.getoAuth20Token().setAccessToken(getAccessTokenByRefreshTokenAndClientId().getAccessToken());
			} else {
				this.tokenProducer.getoAuth20Token().setAccessToken(getAccessTokenForExternalUserByRefreshTokenAndClientId().getAccessToken());
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean hasRefreshTokenAndClientId() {
		return (StringUtils.isNotEmpty(tokenProducer.getoAuth20Token().getRefreshToken()) && StringUtils.isNotEmpty(tokenProducer.getClientId()));
	}


	private String getHost() {
		return bundle.getString(CommonConstants.SF_HOST);
	}

	private int getPort() {
		if (bundle.getString(CommonConstants.SF_PORT).length() > 0) {
			return Integer.parseInt(bundle.getString(CommonConstants.SF_PORT));
		} else {
			return -1;
		}
	}

	private String getNetworkProtocol() {
		return bundle.getString(CommonConstants.SF_NETWORK_PROTOCOL);
	}


	public OAuth20Token getAccessTokenByUsernameAndPassword() throws Exception {

		//String tokenfilepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/sf"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";
		//Reporter.log("Token file :" + tokenfilepath, true);

		OAuth20Token oAuth20Token = new OAuth20Token();

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), SalesforceConstants.SF_OAUTH20_TOKEN_URL, queryParams, null);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, 		tokenProducer.getClientId()));
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, 	tokenProducer.getClientSecret()));
		formparams.add(new BasicNameValuePair(CommonConstants.USERNAME, 		this.userAccount.getUsername()));
		formparams.add(new BasicNameValuePair(CommonConstants.PASSWORD, 		this.userAccount.getPassword()));
		formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, 		CommonConstants.PASSWORD));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams); 
		Reporter.log("Request body:"+ IOUtils.toString(entity.getContent(), "UTF-8"), true);
		//Add the entity header application/x-www-form-urlencoded"
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, 		 MediaType.APPLICATION_JSON)); 

		boolean isTokenGenerated = false;
		int attempts = 1;
		//Try for 
		while(!isTokenGenerated ) {
			Reporter.log("Trying to get auth token for salesforce...Attempt:"+attempts, true);
			HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
			String strResponse = getResponseBody(response);
			Reporter.log("Response body:"+strResponse, true);
			Reporter.log("Response Code:"+response.getStatusLine().getStatusCode(), true);
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class); 

			
			//If the response is not 200
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Reporter.log("Unable to fetch the token. Please check user name and password", true);
				attempts++;
				if(attempts > CommonConstants.REQEUST_RETRY_ATTEMPTS) {
					Reporter.log("Unable to generate the token after "+CommonConstants.REQEUST_RETRY_ATTEMPTS+ " attempts.", true);
					isTokenGenerated = true;
				}
			} else {
				isTokenGenerated = true;
			}
		}
		return oAuth20Token;
	}



	public OAuth20Token getAccessTokenByRefreshTokenAndClientId() throws Exception {

		long startTime = System.currentTimeMillis();

		String tokenfilepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/sf"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";

		Reporter.log("Token file :" + tokenfilepath, true);
		/*
    	if (new File(FilenameUtils.separatorsToSystem(tokenfilepath)).exists()) {
    		System.out.println("Token file found");
    		try {
    			BoxToken boxToken = retrieveTokenFromTokenStore(tokenfilepath);
    			if(!boxToken.isTokenExpired()) {
    				System.out.println("Token not expired...Retrieved the token from token store...");
    				tokenProducer.setoAuth20Token(boxToken.getOauthToken());
    				return boxToken.getOauthToken();
    			} else {
    				System.out.println("Token expired...Trying to get the token again...");
    			}
    		}
    		catch(Exception e) {}
    	} else {
    		System.out.println("Token File not found"+tokenfilepath);
    	}
		 */

		OAuth20Token oAuth20Token = new OAuth20Token();

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), SalesforceConstants.SF_OAUTH20_TOKEN_URL, queryParams, null);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
		formparams.add(new BasicNameValuePair(CommonConstants.REFRESH_TOKEN, tokenProducer.getoAuth20Token().getRefreshToken()));
		formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.REFRESH_TOKEN));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);    	
		//Add the entity header application/x-www-form-urlencoded"
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	

		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response body:"+strResponse, true);
		Reporter.log("Response Code:"+response.getStatusLine().getStatusCode(), true);
		oAuth20Token =  unmarshall(strResponse, OAuth20Token.class); 

		//If the refresh token is expired
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {


			int noOfRetries = 5;
			String code = null;
			try {
				code = regenerateRefreshTokenUsingCodeFlow();
			} catch (Exception e) {
				for (int i = 0; i < noOfRetries; i++) {
					Reporter.log("Retry count:"+ (i+1), true);
					Reporter.log("Waiting for 30 secs before trying to login again", true);
					Thread.sleep(30000);
					code = regenerateRefreshTokenUsingCodeFlow();	
				}
			}

			//String code = this.regenerateRefreshTokenUsingCodeFlow();

			formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
			formparams.add(new BasicNameValuePair(CommonConstants.CODE, code));
			formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.AUTHORIZATION_CODE));

			//entity = new UrlEncodedFormEntity(formparams); 
			HttpEntity strEntity = new UrlEncodedFormEntity(formparams); //new StringEntity(StringUtils.join(formparams, "&"));

			headers.clear();	    	
			//Add the entity header application/x-www-form-urlencoded"
			headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	
			Reporter.log("Headers:"+headers, true);
			response = executeRestRequest(POST_METHOD, uri, headers, strEntity, null);		
			strResponse = getResponseBody(response);
			Reporter.log("Response body:"+strResponse, true);	
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class);    	    	
			tokenProducer.setoAuth20Token(oAuth20Token);			
		} 

		long elapsedTime = System.currentTimeMillis() - startTime;		
		//Write the token to the properties file again
		updatePropertiesFile();

		//Write the token to the tokenstore
		this.saveTokenToTokenStore();
		return oAuth20Token;
	}


	public OAuth20Token getAccessTokenForExternalUserByRefreshTokenAndClientId() throws Exception {

		OAuth20Token oAuth20Token = new OAuth20Token();

		URI uri = Utility.getURI(getNetworkProtocol(), apiHost, getPort(), SalesforceConstants.SF_OAUTH20_TOKEN_URL, queryParams, null);

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
		formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
		formparams.add(new BasicNameValuePair(CommonConstants.REFRESH_TOKEN, tokenProducer.getoAuth20Token().getRefreshToken()));
		formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.REFRESH_TOKEN));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);    	
		//Add the entity header application/x-www-form-urlencoded"
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	

		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response body:"+strResponse, true);
		Reporter.log("Response Code:"+response.getStatusLine().getStatusCode(), true);
		oAuth20Token =  unmarshall(strResponse, OAuth20Token.class); 

		//If the refresh token is expired
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			String code = this.regenerateRefreshTokenUsingCodeFlow();

			formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_ID, tokenProducer.getClientId()));
			formparams.add(new BasicNameValuePair(CommonConstants.CLIENT_SECRET, tokenProducer.getClientSecret()));
			formparams.add(new BasicNameValuePair(CommonConstants.CODE, code));
			formparams.add(new BasicNameValuePair(CommonConstants.GRANT_TYPE, CommonConstants.AUTHORIZATION_CODE));

			//entity = new UrlEncodedFormEntity(formparams); 
			HttpEntity strEntity = new UrlEncodedFormEntity(formparams); 

			headers.clear();	    	
			//Add the entity header application/x-www-form-urlencoded"
			headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE));   	    	
			Reporter.log("Headers:"+headers, true);
			response = executeRestRequest(POST_METHOD, uri, headers, strEntity, null);		
			strResponse = getResponseBody(response);
			Reporter.log("Response body:"+strResponse, true);	
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class);    	    	
			tokenProducer.setoAuth20Token(oAuth20Token);			
		} 
		return oAuth20Token;
	}
	
	public OAuth20Token getTokenObject() throws Exception {
		return this.tokenProducer.getoAuth20Token();
	}
	

	private  void updatePropertiesFile() throws FileNotFoundException, UnsupportedEncodingException, URISyntaxException {
		String currentPath = System.getProperty("user.dir");

		//TODO define this path in common properties
		String filepath = "/src/main/java/com/universal/properties/sf" + this.tenant + this.userAccount.getRole().toLowerCase() +".properties";

		Reporter.log("Property file loaded:" + filepath, true);

		File f = new File(currentPath + FilenameUtils.separatorsToSystem(filepath));

		PrintWriter writer = new PrintWriter(currentPath + FilenameUtils.separatorsToSystem(filepath), "UTF-8");
		//PrintWriter writer = new PrintWriter(FilenameUtils.separatorsToSystem(filepath), "UTF-8");
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

		String currentPath = System.getProperty("user.dir"); 
		String filepath = currentPath + "/src/main/java/com/universal/tokenstore/sf"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";

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

	private String regenerateRefreshTokenUsingCodeFlow() throws Exception {

		WebDriver driver = new HtmlUnitDriver();
		String code = null;
		String authUrl = bundle.getString(SalesforceConstants.SF_AUTHORIZE_URL)
				.replace("{client_id}", tokenProducer.getClientId());

		driver.get(authUrl);
		Thread.sleep(3000);
		Reporter.log("Current url:"+driver.getCurrentUrl(), true);
		Reporter.log("Trying to login to box with the credentials:" + userAccount.getUsername(), true);
		driver.findElement(By.id("login")).sendKeys(this.userAccount.getUsername());
		driver.findElement(By.id("password")).sendKeys(this.userAccount.getPassword());
		driver.findElement(By.name("login_submit")).click();
		Thread.sleep(6000);
		//System.out.println("Current url:"+driver.getCurrentUrl());
		driver.findElement(By.name("consent_accept")).click();		
		Thread.sleep(3000);		
		String currentUrl = driver.getCurrentUrl();		
		Reporter.log("Current url:"+driver.getCurrentUrl(), true);	

		if(currentUrl.contains("app.box.com")) {
			driver.findElement(By.name("login")).sendKeys(this.userAccount.getUsername());
			driver.findElement(By.name("password")).sendKeys(this.userAccount.getPassword());
			driver.findElement(By.name("login_submit")).click();
			Thread.sleep(3000);
			driver.findElement(By.id("consent_accept")).click();		
			Thread.sleep(1000);	
		}
		currentUrl = driver.getCurrentUrl();	

		List<NameValuePair> params = URLEncodedUtils.parse(new URI(currentUrl), "UTF-8");
		for (NameValuePair param : params) {
			if (param.getName().equals("code")) {
				code = param.getValue();
			}			
		}		
		Reporter.log("Code:"+code, true);
		driver.quit();		
		return code;
	}

}
