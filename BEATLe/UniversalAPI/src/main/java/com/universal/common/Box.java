package com.universal.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.universal.constants.BoxConstants;
import com.universal.constants.CommonConstants;
import com.universal.dtos.UserAccount;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFile;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxToken;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.Collaborations;
import com.universal.dtos.box.EmbeddedLink;
import com.universal.dtos.box.Entry;
import com.universal.dtos.box.FileConflict;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.FileVersion;
import com.universal.dtos.box.GroupList;
import com.universal.dtos.box.ItemCollection;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.UserCollection;
import com.universal.util.OAuth20Token;
import com.universal.util.TokenProducer;
import com.universal.util.Utility;

public class Box extends CommonTest implements UniversalCore {
	private TokenProducer tokenProducer;
	private UserAccount userAccount;
	ResourceBundle bundle;
	String tenant;

	public Box() throws Exception {

	}

	public Box(TokenProducer tokenProducer, UserAccount userAccount) throws Exception {
		this.tokenProducer = tokenProducer;
		this.userAccount   = userAccount;
		bundle = new Utility().getProperties("com.universal.configuration.box_configuration");
		tenant = StringUtils.split(this.userAccount.getUsername(), "@")[1].replace(".", "");
	}


	public <T> T getFile(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T getFolder(String folderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T uploadFile(String folderId, String filename) throws Exception {

		//		File codeSource = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		//		String currentPath = Paths.get(codeSource.getPath()).getParent().getParent().toString();

		String currentPath = System.getProperty("user.dir");
		String path = BoxConstants.BOX_API_UPLOADCONTENT_URI;
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_UPLOAD_HOST, getPort(), path, queryParams, null);

		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));

		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(BoxConstants.BOX_UPLOADSFOLDER_PATH);

		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);

			if (!uploadFile.exists()) {
				Reporter.log("Filename:" + uploadFile.getAbsolutePath());
				Reporter.log("File not exists in resources folder too... quiting", true);
				//System.exit(0);
			}

			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}

		if(folderId == null || folderId.equals("/") ) {
			//Retrieve the root folder id
			folderId = "0";
		}

		String attributes = "{\"name\":\""+filename+ "\", \"parent\":{\"id\":\""+folderId+"\"}}";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();		
		builder.addTextBody("attributes", attributes, ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_OCTET_STREAM, filename);		
		HttpEntity multipart = builder.build();
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), multipart, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		FileUploadResponse fupload = new FileUploadResponse();
		//If file uploaded successfully
		if (this.getResponseStatusCode(response) == HttpStatus.SC_OK) {
			BoxFile boxFile = unmarshall(responseBody, BoxFile.class);

			fupload.setFileId(boxFile.getEntries().get(0).getId());
			fupload.setFileName(boxFile.getEntries().get(0).getName());
			fupload.setResponseCode(HttpStatus.SC_OK);
			fupload.setResponseMessage("File Uploaded Successfully");

		} else if (this.getResponseStatusCode(response) == HttpStatus.SC_CONFLICT){
			//return (T) unmarshall(responseBody, FileConflict.class);
			FileConflict boxFile = unmarshall(responseBody, FileConflict.class);
			fupload.setFileId(boxFile.getContextInfo().getConflicts().getId());
			fupload.setFileName(boxFile.getContextInfo().getConflicts().getName());
			fupload.setResponseCode(HttpStatus.SC_CONFLICT);
			fupload.setResponseMessage(boxFile.getMessage());

		} else {

			fupload.setFileId("00");
			fupload.setFileName("00");
			fupload.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			fupload.setResponseMessage("Unknown Error While trying to upload");
		}
		return (T) fupload;
	}


	@SuppressWarnings("unchecked")
	public synchronized <T> T uploadFile(String folderId, String filename, String destinationFilename) throws Exception {

		//		File codeSource = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		//		String currentPath = Paths.get(codeSource.getPath()).getParent().getParent().toString();

		String currentPath = System.getProperty("user.dir");
		String path = BoxConstants.BOX_API_UPLOADCONTENT_URI;
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_UPLOAD_HOST, getPort(), path, queryParams, null);

		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));

		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(BoxConstants.BOX_UPLOADSFOLDER_PATH);

		//check the filename provided is absolute or not
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename).trim());

		//If not read from default location
		if(!uploadFile.exists()) {
			uploadFile = new java.io.File(currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename);

			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				System.exit(0);
			}

			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}

		if(folderId == null || folderId.equals("/") ) {
			//Retrieve the root folder id
			folderId = "0";
		}

		String attributes = "{\"name\":\""+destinationFilename+ "\", \"parent\":{\"id\":\""+folderId+"\"}}";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();	
		Charset chars = Charset.forName("UTF-8");
		builder.setCharset(chars);
		builder.addTextBody("attributes", attributes, ContentType.create("text/plain", Consts.UTF_8));
		builder.addBinaryBody("file", uploadFile, ContentType.APPLICATION_OCTET_STREAM, destinationFilename);		
		HttpEntity multipart = builder.build();
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), multipart, null);	
		String responseBody = getResponseBody(response);
		/*
		Reporter.log("########### ALL HEADERS ###############", true);

		for(Header header : response.getAllHeaders()) {
			Reporter.log(header.getName()  + "::" + header.getValue(), true);
		}
		Reporter.log("#######################################", true);
		 */
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		FileUploadResponse fupload = new FileUploadResponse();
		//If file uploaded successfully
		if (this.getResponseStatusCode(response) == HttpStatus.SC_OK || this.getResponseStatusCode(response) == HttpStatus.SC_CREATED) {
			BoxFile boxFile = unmarshall(responseBody, BoxFile.class);

			fupload.setFileId(boxFile.getEntries().get(0).getId());
			fupload.setFileName(boxFile.getEntries().get(0).getName());
			fupload.setResponseCode(HttpStatus.SC_OK);
			fupload.setResponseMessage("File Uploaded Successfully");
			fupload.setEtag(boxFile.getEntries().get(0).getEtag());
			fupload.setSize(boxFile.getEntries().get(0).getSize());

		} else if (this.getResponseStatusCode(response) == HttpStatus.SC_CONFLICT){
			//return (T) unmarshall(responseBody, FileConflict.class);
			FileConflict boxFile = unmarshall(responseBody, FileConflict.class);
			fupload.setFileId(boxFile.getContextInfo().getConflicts().getId());
			fupload.setFileName(boxFile.getContextInfo().getConflicts().getName());
			fupload.setResponseCode(HttpStatus.SC_CONFLICT);
			fupload.setResponseMessage(boxFile.getMessage());
			fupload.setEtag(boxFile.getContextInfo().getConflicts().getEtag());

		} else {

			fupload.setFileId("00");
			fupload.setFileName("00");
			fupload.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			fupload.setResponseMessage("Unknown Error While trying to upload");
			fupload.setEtag("0");
		}
		return (T) fupload;
	}


	@SuppressWarnings("unchecked")
	public <T> T updateFile(String fileId, String filename, String etag, String destinationFile) throws Exception {

		String currentPath = System.getProperty("user.dir");

		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATE_FILE_URI).replace("{file_id}", fileId);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_UPLOAD_HOST, getPort(), uriPath, queryParams, null);

		headers = getHeaders();
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, etag));
		headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=----------------------"+Long.toString(new Date().getTime())));

		//System.out.println("All Headers:"+headers);
		String uploadsPath = bundle.getString(BoxConstants.BOX_UPLOADSFOLDER_PATH);
		//System.out.println("All Headers:"+headers);

		//check the filename provided is absolute or not
		Reporter.log("Filepath:"+FilenameUtils.separatorsToSystem(filename), true);
		File uploadFile = new java.io.File(FilenameUtils.separatorsToSystem(filename));

		//If not read from default location
		if(!uploadFile.exists()) {
			String filepath = currentPath + FilenameUtils.separatorsToSystem(uploadsPath) + filename;
			Reporter.log("Resources folder file path:"+filepath, true);
			uploadFile = new java.io.File(filepath);

			if (!uploadFile.exists()) {
				Reporter.log("File not exists in resources folder too... quiting", true);
				System.exit(0);
			}

			Reporter.log("file path:"+uploadFile.getAbsolutePath(), true);
		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("filename", uploadFile, ContentType.APPLICATION_OCTET_STREAM, destinationFile);		
		HttpEntity multipart = builder.build();
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), multipart, null);	
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		FileUploadResponse fupload = new FileUploadResponse();
		//If file uploaded successfully
		if (this.getResponseStatusCode(response) == HttpStatus.SC_OK || this.getResponseStatusCode(response) == HttpStatus.SC_CREATED) {
			BoxFile boxFile = unmarshall(responseBody, BoxFile.class);

			fupload.setFileId(boxFile.getEntries().get(0).getId());
			fupload.setFileName(boxFile.getEntries().get(0).getName());
			fupload.setResponseCode(HttpStatus.SC_OK);
			fupload.setResponseMessage("File updated Successfully");
			fupload.setEtag(boxFile.getEntries().get(0).getEtag());

		} else if (this.getResponseStatusCode(response) == HttpStatus.SC_CONFLICT){
			//return (T) unmarshall(responseBody, FileConflict.class);
			FileConflict boxFile = unmarshall(responseBody, FileConflict.class);
			fupload.setFileId(boxFile.getContextInfo().getConflicts().getId());
			fupload.setFileName(boxFile.getContextInfo().getConflicts().getName());
			fupload.setResponseCode(HttpStatus.SC_CONFLICT);
			fupload.setResponseMessage(boxFile.getMessage());
			fupload.setEtag(boxFile.getContextInfo().getConflicts().getEtag());

		} else {

			fupload.setFileId("00");
			fupload.setFileName("00");
			fupload.setResponseCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			fupload.setResponseMessage("Unknown Error While trying to upload");
			fupload.setEtag("0");
		}
		return (T) fupload;
	}


	/**
	 * Get Information about the file
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T copyFile(String fileId, String destinationFolderId) throws Exception {

		String payload = "{\"parent\": {\"id\" :"+  destinationFolderId +"}}";
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = bundle.getString(BoxConstants.BOX_API_COPY_FILE_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		//If file exists
		Entry fileEntry = unmarshall(responseBody, Entry.class);
		return (T) fileEntry;
	}

	public <T> T downloadFile(String fileId, String targetFilename) throws Exception {

		String currentPath = System.getProperty("user.dir");

		String uriPath = bundle.getString(BoxConstants.BOX_API_DOWNLOADCONTENT_URI).replace("{file_id}", fileId);
		String downloadsPath = bundle.getString(BoxConstants.BOX_DOWNLOADSFOLDER_PATH);
		String destinationFile = currentPath + FilenameUtils.separatorsToSystem(downloadsPath) + targetFilename;
		headers = getAuthHeadersOnly();
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
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

	public <T> T previewFile(String fileId, String targetFilename, String width, String height) throws Exception {

		String currentPath = System.getProperty("user.dir");

		String uriPath = bundle.getString(BoxConstants.BOX_API_PREVIEW_FILE_URI).replace("{file_id}", fileId).
				replace("{width}", width).replace("{height}", height);
		uriPath +=".txt";

		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		qParams.add(new BasicNameValuePair("min_height", height));
		qParams.add(new BasicNameValuePair("min_width", width));

		String downloadsPath = bundle.getString(BoxConstants.BOX_DOWNLOADSFOLDER_PATH);
		String destinationFile = currentPath + FilenameUtils.separatorsToSystem(downloadsPath) + targetFilename;
		headers = getAuthHeadersOnly();
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qParams, null);
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

	public <T> T previewFile(String fileId, String targetFilename) throws Exception {

		String url = this.getEmbeddedLink(fileId);
		URI uri = new URI(url);
		String currentPath = System.getProperty("user.dir");
		String downloadsPath = bundle.getString(BoxConstants.BOX_DOWNLOADSFOLDER_PATH);
		String destinationFile = currentPath + FilenameUtils.separatorsToSystem(downloadsPath) + targetFilename;
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36"));

		HttpResponse response = executeRestRequest(GET_METHOD, uri, null, null, null);
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


	public String getEmbeddedLink(String fileId) throws Exception {

		ArrayList<NameValuePair> qParams = new ArrayList<NameValuePair>();
		qParams.add(new BasicNameValuePair("fields", "expiring_embed_link"));

		String uriPath = BoxConstants.BOX_API_FILEPREVIEW_URI.replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();

		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String strResponse = response.getStatusLine().getStatusCode() +"::" + response.getStatusLine().getReasonPhrase();
		Reporter.log("Response line:"+strResponse, true);	
		String responseBody = getResponseBody(response);
		EmbeddedLink embeddedLink = unmarshall(responseBody, EmbeddedLink.class);
		return embeddedLink.getExpiringEmbedLink().getUrl();
	}






	@SuppressWarnings("unchecked")
	public <T> T renameFile(String fileId, String newname) throws Exception {

		String payload = "{\"name\":\""+newname+ "\"}";
		StringEntity entity = new StringEntity(payload);

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, entity, null);

		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		FileUploadResponse fupload = new FileUploadResponse();
		//If file uploaded successfully
		FileVersion boxFile = unmarshall(responseBody, FileVersion.class);
		fupload.setFileId(boxFile.getId());
		fupload.setFileName(newname);
		fupload.setResponseCode(response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			fupload.setResponseMessage("File Renamed Successfully");
		} else {
			fupload.setResponseMessage("Error while renaming the file");
		}
		return (T) fupload;
	}


	/**
	 * Get Information about the file
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T getFileInfo(String fileId) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);

		//If file exists
		FileEntry fileEntry = unmarshall(responseBody, FileEntry.class);
		return (T) fileEntry;
	}

	@SuppressWarnings("unchecked")
	public <T> T updateFileInfo(String fileId, String updateJson) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);

		StringEntity stringEntity = new StringEntity(updateJson);

		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		//If file exists
		Entry fileEntry = unmarshall(responseBody, Entry.class);
		return (T) fileEntry;
	}


	public void lockFile(String fileId, boolean preventDownload) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		DateTime currentTime = DateTime.now(DateTimeZone.UTC);
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");//.withZone(DateTimeZone.forID("America/Los_Angeles"));
		String expiresAt   = currentTime.plusDays(1).toString(df);
		String payload = "{\"lock\": {\"type\": \"lock\",\"expires_at\": \"" + expiresAt +"\",\"is_download_prevented\":" + preventDownload +"}}";
		StringEntity stringEntity = new StringEntity(payload);

		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
	}

	public void unlockFile(String fileId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		String payload = "{\"lock\": null}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
	}


	@SuppressWarnings("unchecked")
	public <T> T createDefaultSharedLink(String fileId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		String payload = "{\"shared_link\": {}}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		FileEntry file  = unmarshall(responseBody, FileEntry.class);
		return (T) file;
	}


	@SuppressWarnings("unchecked")
	public <T> T createSharedLink(String fileId, SharedLink sharedLink) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		String payload = "{\"shared_link\":" + marshall(sharedLink) +"}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		System.out.println("Headers:" + headers);
		System.out.println("Payload:" + payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		FileEntry file  = unmarshall(responseBody, FileEntry.class);
		return (T) file;
	}

	@SuppressWarnings("unchecked")
	public <T> T disableSharedLink(String fileId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		String payload = "{\"shared_link\": null}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		FileEntry file  = unmarshall(responseBody, FileEntry.class);
		return (T) file;
	}



	@SuppressWarnings("unchecked")
	public void deleteFile(String fileId, String etag) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, etag));
		System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
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
	public void purgeFile(String fileId) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEFROMTRASH_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);

		System.out.println("Response code:" + response.getStatusLine().getStatusCode());

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
			Reporter.log("File purged successfully", 1);
		}

		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_PRECONDITION_FAILED) {
			Reporter.log("Problem in file purging.", 1);
		}
	}

	public <T> T restoreFileFromTrash(String fileId, String name) throws Exception {

		String payload = "{\"name\":\""+name+ "\"}";
		StringEntity entity = new StringEntity(payload);

		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEINFO_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);

		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		FileEntry fileEntry = unmarshall(responseBody, FileEntry.class);
		return (T) fileEntry;
	}


	@SuppressWarnings("unchecked")
	public <T> T getTrashedFile(String fileId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FILEFROMTRASH_URI).replace("{file_id}", fileId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		//If file exists
		Entry fileEntry = unmarshall(responseBody, Entry.class);
		return (T) fileEntry;
	}




	public <T> T getDefaultDrive() throws Exception {
		String path = BoxConstants.BOX_API_USERINFO;
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), path, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		System.out.println("Get User Info Response body:"+strResponse);			    	    	
		return null;		
	}

	@SuppressWarnings("unchecked")
	public <T> T getUserInfo() throws Exception {
		String path = BoxConstants.BOX_API_USERINFO;
		//System.out.println("All headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), path, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		//System.out.println("Get User Info Response body:"+strResponse);		
		return (T) unmarshall(strResponse, BoxUserInfo.class);
	}

	//Folder related operations

	@SuppressWarnings("unchecked")
	public <T> T getFolderInfo(String folderId) throws Exception {
		folderId = (folderId != null) ? folderId : "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERINFO_URI).replace("{folder_id}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T getFoldersItems(String folderId, int limit, int offset) throws Exception {
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("limit", Integer.toString(limit)));
		qparams.add(new BasicNameValuePair("offset", Integer.toString(offset)));
		folderId = (folderId != null) ? folderId : "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERITEMS_URI).replace("{folder_id}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, ItemCollection.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T getFoldersItemsAsUser(String folderId, int limit, int offset, String asUser) throws Exception {
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers = getHeaders();
		headers.add(new BasicNameValuePair("As-User", asUser));
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("limit", Integer.toString(limit)));
		qparams.add(new BasicNameValuePair("offset", Integer.toString(offset)));
		folderId = (folderId != null) ? folderId : "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERITEMS_URI).replace("{folder_id}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, ItemCollection.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T createFolder(String folderName, String parentFolderId) throws Exception {
		parentFolderId = (parentFolderId != null) ? parentFolderId : "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEFOLDER_URI);
		String payload = "{\"name\":\"" + folderName + "\", \"parent\": {\"id\": \"" + parentFolderId +"\"}}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}


	@SuppressWarnings("unchecked")
	public <T> T createFolderAsUser(String folderName, String parentFolderId, String asUser) throws Exception {
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers = getHeaders();
		headers.add(new BasicNameValuePair("As-User", asUser));

		parentFolderId = (parentFolderId != null) ? parentFolderId : "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEFOLDER_URI);
		String payload = "{\"name\":\"" + folderName + "\", \"parent\": {\"id\": \"" + parentFolderId +"\"}}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}



	@SuppressWarnings("unchecked")
	public <T> T updateFolder(String folderId, String updatedName) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEFOLDER_URI).replace("{folder_id}", folderId);
		String payload = "{\"name\":\"" + updatedName + "\"}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T moveFolder(String sourceFolderId, String destinationFolderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEFOLDER_URI).replace("{folder_id}", sourceFolderId);
		String payload = "{\"id\":\"" + sourceFolderId + "\", \"parent\":{\"id\":\""+ destinationFolderId +"\"}}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}

	public void deleteFolder(String folderId, boolean recursive, String etag) throws Exception {
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("recursive", Boolean.toString(recursive)));
		//optional etag
		headers = getHeaders();
		if (etag != null) {
			headers.add(new BasicNameValuePair(HttpHeaders.IF_MATCH, etag));
		}

		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETEFOLDER_URI).replace("{folder_id}", folderId);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
			Reporter.log("Response body:" + getResponseBody(response), true);
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T copyFolder(String folderId, String destinationFolderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_COPYFOLDER_URI).replace("{folder_id}", folderId);
		String payload = "{\"parent\": {\"id\" : \"" + destinationFolderId +"\"}}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);

	}


	@SuppressWarnings("unchecked")
	public <T> T createSharedLinkForFolder(String folderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERINFO_URI).replace("{folder_id}", folderId);
		String payload = "{\"shared_link\": {}}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		return (T) unmarshall(responseBody, BoxFolder.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T createSharedLinkForFolder(String folderId, SharedLink sharedLink) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERINFO_URI).replace("{folder_id}", folderId);
		String payload = "{\"shared_link\":" + marshall(sharedLink) +"}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		return (T) unmarshall(responseBody, BoxFolder.class);

	}


	@SuppressWarnings("unchecked")
	public <T> T disableSharedLinkForFolder(String folderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERINFO_URI).replace("{folder_id}", folderId);
		String payload = "{\"shared_link\": null}";
		StringEntity stringEntity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, BoxFolder.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T getFolderCollaborations(String folderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERCOLLABORATIONS_URI).replace("{folder_id}", folderId);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, Collaborations.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T getFolderTrashedItems(String fields, int limit, int offset) throws Exception {
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("fields", fields));
		qparams.add(new BasicNameValuePair("limit", Integer.toString(limit)));
		qparams.add(new BasicNameValuePair("offset", Integer.toString(offset)));

		String uriPath = bundle.getString(BoxConstants.BOX_API_FOLDERTRASHEDITEMS_URI);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, ItemCollection.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T getTrashedFolder(String folderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_TRASHEDFOLDER_URI).replace("{folder_id}", folderId);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, headers, null, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		return (T) unmarshall(responseBody, BoxFolder.class);

	}

	public void purgeFolder(String folderId) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_TRASHEDFOLDER_URI).replace("{folder_id}", folderId);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, headers, null, null);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
	}


	@SuppressWarnings("unchecked")
	public <T> T restoreFolder(String folderId, String restoreFolderName) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_RESTOREFOLDER_URI).replace("{folder_id}", folderId);

		String payload = "{\"name\": \"" + restoreFolderName + "\"}";
		StringEntity entity = new StringEntity(payload);
		headers = getAuthHeadersOnly();
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);
		String responseBody = getResponseBody(response);

		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, BoxFolder.class);

	}

	@SuppressWarnings("unchecked")
	public <T> T createFolder(String folderName) throws Exception {
		String parentFolderId = "0"; // 
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEFOLDER_URI);
		String payload = "{\"name\":\"" + folderName + "\", \"parent\": {\"id\": \"" + parentFolderId +"\"}}";
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true );
		return (T) unmarshall(strResponse, BoxFolder.class);

	}

	//User related operations

	@SuppressWarnings("unchecked")
	public <T> T createUser(Object object) throws Exception {
		String payload = marshall(object);
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEUSER_URI);

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		return (T) unmarshall(strResponse, BoxUserInfo.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T updateUser(BoxUserInfo userInfo) throws Exception {

		String payload = marshall(userInfo);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEUSER_URI).replace("{user_id}", userInfo.getId());

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		return (T) unmarshall(strResponse, BoxUserInfo.class);
	}


	@SuppressWarnings("unchecked")
	public void deleteUser(BoxUserInfo userInfo) throws Exception {
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("force", "true"));

		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETEUSER_URI).replace("{user_id}", userInfo.getId());
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}

	@SuppressWarnings("unchecked")
	public <T> T listUser() throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_LISTUSER_URI);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true);
		return (T) unmarshall(strResponse, UserCollection.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T listAllUsers(int offset, int limit) throws Exception {
		ArrayList<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("offset", String.valueOf(offset)));
		qparams.add(new BasicNameValuePair("limit" , String.valueOf(limit)));

		String uriPath = bundle.getString(BoxConstants.BOX_API_LISTUSER_URI);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, qparams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true);
		return (T) unmarshall(strResponse, UserCollection.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T transferOwner(String folderId, String currentOwnerId, String futureOwnerId) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_TRANSFEROWNER_URI).replace("{user_id}", currentOwnerId)
				.replace("{folder_id}", folderId);
		String payload = "{\"owned_by\": {\"id\": \"" +futureOwnerId+ "\"}}";

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxFolder.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T inviteUser(String enterpriseId, String actionableBy) throws Exception {

		String uriPath = bundle.getString(BoxConstants.BOX_API_INVITEUSER_URI);

		String payload = "{\"enterprise\":{\"id\" : \""+enterpriseId+"\"},\"actionable_by\":{\"login\":\""+actionableBy+"\"}}";

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true);
		//return (T) unmarshall(strResponse, BoxFolder.class);
		return (T) strResponse;
	}




	@SuppressWarnings("unchecked")
	public <T> T createGroup(Object object) throws Exception {
		String payload = marshall(object);
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEGROUPS_URI);

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		

		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, BoxGroup.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T updateGroup(BoxGroup group) throws Exception {

		String payload = marshall(group);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEGROUPS_URI).replace("{group_id}", group.getId());

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, BoxGroup.class);
	}


	@SuppressWarnings("unchecked")
	public void deleteGroup(BoxGroup group) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETEGROUPS_URI).replace("{group_id}", group.getId());
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);		
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
	}


	@SuppressWarnings("unchecked")
	public <T> T listGroup() throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEGROUPS_URI);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(GET_METHOD, uri, getHeaders(), null, null);		
		//System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		String responseBody = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + responseBody, true);
		return (T) unmarshall(responseBody, GroupList.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T createMembership(Object object) throws Exception {
		String payload = marshall(object);
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEMEMBERSHIPS_URI);

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxMembership.class);
	}



	@SuppressWarnings("unchecked")
	public <T> T updateMembership(BoxMembership membership) throws Exception {

		String payload = marshall(membership);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEMEMBERSHIPS_URI).replace("{membership_id}", membership.getId());

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		return (T) unmarshall(strResponse, BoxMembership.class);
	}

	@SuppressWarnings("unchecked")
	public void deleteMembership(BoxMembership membership) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETEMEMBERSHIPS_URI).replace("{membership_id}", membership.getId());
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		if(response.getStatusLine().getStatusCode() != HttpStatus.SC_NO_CONTENT) {
			Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
			Reporter.log("Response body:" + getResponseBody(response), true);
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T createCollaboration(Object object) throws Exception {
		String payload = marshall(object);
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATECOLLABORATIONS_URI);
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxCollaboration.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T updateCollaboration(BoxCollaboration collaboration) throws Exception {

		String payload = "{\"role\": \"" + collaboration.getRole() +"\"}";   //marshall(collaboration);
		Reporter.log("Request payload:"+payload, true);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATECOLLABORATIONS_URI).replace("{collaboration_id}", collaboration.getId());

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		Reporter.log("Headers:"+getHeaders(), true);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxCollaboration.class);
	}

	@SuppressWarnings("unchecked")
	public void deleteCollaboration(BoxCollaboration collaboration) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETECOLLABORATIONS_URI).replace("{collaboration_id}", collaboration.getId());
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
	}


	@SuppressWarnings("unchecked")
	public <T> T createWeblink(Object object) throws Exception {
		String payload = marshall(object);
		String uriPath = bundle.getString(BoxConstants.BOX_API_CREATEWEBLINK_URI);
		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxWeblink.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T updateWeblink(BoxWeblink weblink) throws Exception {

		String payload = marshall(weblink);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEWEBLINK_URI).replace("{weblink_id}", weblink.getId());

		StringEntity stringEntity = new StringEntity(payload);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, getHeaders(), stringEntity, null);		
		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxWeblink.class);
	}

	@SuppressWarnings("unchecked")
	public void deleteWeblink(BoxWeblink weblink) throws Exception {
		String uriPath = bundle.getString(BoxConstants.BOX_API_DELETEWEBLINK_URI).replace("{weblink_id}", weblink.getId());
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(DELETE_METHOD, uri, getHeaders(), null, null);		
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true );
	}

	@SuppressWarnings("unchecked")
	public <T> T restoreWeblink(BoxWeblink weblink) throws Exception {
		String payload = marshall(weblink);
		StringEntity entity = new StringEntity(payload);

		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEWEBLINK_URI).replace("{weblink_id}", weblink.getId());
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);

		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxWeblink.class);
	}

	@SuppressWarnings("unchecked")
	public <T> T copyWeblink(String weblinkId, String parentId) throws Exception {
		String payload = "{\"parent\": {\"id\" : \"" + parentId + "\"}}" ;//"{\"name\": \"" + restoreFolderName + "\"}";
		StringEntity entity = new StringEntity(payload);

		String uriPath = bundle.getString(BoxConstants.BOX_API_COPYWEBLINK_URI).replace("{weblink_id}", weblinkId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, headers, entity, null);

		String strResponse = getResponseBody(response);
		Reporter.log("Response code:" + response.getStatusLine().getStatusCode(), true);
		Reporter.log("Response body:" + strResponse, true);
		return (T) unmarshall(strResponse, BoxWeblink.class);
	}


	@SuppressWarnings("unchecked")
	public <T> T createSharedLinkForWeblink(String weblinkId, SharedLink sharedLink) throws Exception {

		String payload = "{\"shared_link\":"+marshall(sharedLink)+"}";
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEWEBLINK_URI).replace("{weblink_id}", weblinkId);
		headers = getAuthHeadersOnly();
		System.out.println("Shared link:" + marshall(sharedLink));
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		Entry file  = unmarshall(responseBody, Entry.class);
		return (T) file;
	}

	@SuppressWarnings("unchecked")
	public <T> T updateSharedLinkForWeblink(String weblinkId, SharedLink sharedLink) throws Exception {

		String payload = "{\"shared_link\":"+marshall(sharedLink)+"}";
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEWEBLINK_URI).replace("{weblink_id}", weblinkId);
		headers = getAuthHeadersOnly();
		System.out.println("Shared link:" + marshall(sharedLink));
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		Entry file  = unmarshall(responseBody, Entry.class);
		return (T) file;
	}

	@SuppressWarnings("unchecked")
	public <T> T disableSharedLinkForWeblink(String weblinkId) throws Exception {

		String payload = "{\"shared_link\": null}";
		StringEntity stringEntity = new StringEntity(payload);

		String uriPath = bundle.getString(BoxConstants.BOX_API_UPDATEWEBLINK_URI).replace("{weblink_id}", weblinkId);
		headers = getAuthHeadersOnly();
		//System.out.println("Headers:" + headers);
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(PUT_METHOD, uri, headers, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);
		Entry file  = unmarshall(responseBody, Entry.class);
		return (T) file;
	}


	public void logoutUser() throws Exception {
		String payload = "client_id="+tokenProducer.getClientId()+"&client_secret="+tokenProducer.getClientSecret()+"&token="+tokenProducer.getoAuth20Token().getAccessToken();
		StringEntity stringEntity = new StringEntity(payload);
		String uriPath = BoxConstants.BOX_OAUTH20_REVOKE_URL;
		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_API_HOST, getPort(), uriPath, queryParams, null);
		HttpResponse response = executeRestRequest(POST_METHOD, uri, null, stringEntity, null);
		String responseBody = getResponseBody(response);
		System.out.println("Response code:" + response.getStatusLine().getStatusCode());
		System.out.println("response body:" + responseBody);

		//invalidate the token file
		String currentPath = System.getProperty("user.dir"); 
		String filepath = currentPath + "/src/main/java/com/universal/tokenstore/box"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";

		File file = new File(FilenameUtils.separatorsToSystem(filepath));
		if(!file.exists()) {
			file.createNewFile();
		}
		PrintWriter writer = new PrintWriter(file);
		writer.print("");
		writer.close();
		//Generate the token again
		this.initAccessTokenByRefreshTokenAndClientId();
	}








	// Utils

	private List<NameValuePair> getHeaders() throws Exception {
		if (this.tokenProducer.getoAuth20Token().getAccessToken() == null) {
			//regenerate it
			this.initAccessTokenByRefreshTokenAndClientId();
		}

		headers.clear();
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken())); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		Reporter.log(HttpHeaders.AUTHORIZATION + "Bearer "+ this.tokenProducer.getoAuth20Token().getAccessToken(), true);
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
		return bundle.getString(CommonConstants.BOX_HOST);
	}

	private int getPort() {
		if (bundle.getString(CommonConstants.BOX_PORT).length() > 0) {
			return Integer.parseInt(bundle.getString(CommonConstants.BOX_PORT));
		} else {
			return -1;
		}
	}

	private String getNetworkProtocol() {
		return bundle.getString(CommonConstants.BOX_NETWORK_PROTOCOL);
	}

	private String getDefaultDriveUri() {
		return bundle.getString(BoxConstants.DEFAULT_DRIVE_URI);
	}

	public OAuth20Token getAccessTokenByRefreshTokenAndClientId() throws Exception {

		long startTime = System.currentTimeMillis();


		String tokenfilepath = System.getProperty("user.dir") + "/src/main/java/com/universal/tokenstore/box"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";

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

		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_OAUTH20_HOST, getPort(), BoxConstants.BOX_OAUTH20_TOKEN_URL, queryParams, null);

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
		System.out.println("Response body:"+strResponse);
		System.out.println("Response Code:"+response.getStatusLine().getStatusCode());
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
			System.out.println("Headers:"+headers);
			response = executeRestRequest(POST_METHOD, uri, headers, strEntity, null);		
			strResponse = getResponseBody(response);
			System.out.println("Response body:"+strResponse);	
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

		URI uri = Utility.getURI(getNetworkProtocol(), BoxConstants.BOX_OAUTH20_HOST, getPort(), BoxConstants.BOX_OAUTH20_TOKEN_URL, queryParams, null);

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
		System.out.println("Response body:"+strResponse);
		System.out.println("Response Code:"+response.getStatusLine().getStatusCode());
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
			System.out.println("Headers:"+headers);
			response = executeRestRequest(POST_METHOD, uri, headers, strEntity, null);		
			strResponse = getResponseBody(response);
			System.out.println("Response body:"+strResponse);	
			oAuth20Token =  unmarshall(strResponse, OAuth20Token.class);    	    	
			tokenProducer.setoAuth20Token(oAuth20Token);			
		} 
		return oAuth20Token;
	}


	private  void updatePropertiesFile() throws FileNotFoundException, UnsupportedEncodingException, URISyntaxException {

		File codeSource = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		//String currentPath = Paths.get(codeSource.getPath()).getParent().getParent().toString();

		String currentPath = System.getProperty("user.dir");

		//TODO define this path in common properties
		String filepath = "/src/main/java/com/universal/properties/box" + this.tenant + this.userAccount.getRole().toLowerCase() +".properties";

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

		File codeSource = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		//String currentPath = Paths.get(codeSource.getPath()).getParent().getParent().toString();

		String currentPath = System.getProperty("user.dir"); 
		String filepath = currentPath + "/src/main/java/com/universal/tokenstore/box"+ this.tenant + this.userAccount.getRole().toLowerCase()+".token";

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

		WebDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_38);
		String code = null;
		String authUrl = bundle.getString(BoxConstants.BOX_AUTHORIZE_URL)
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
			driver.findElement(By.name("consent_accept")).click();		
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
