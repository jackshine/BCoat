/**
 * 
 */
package com.elastica.beatle.dci;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.JSONParser;
import org.testng.Assert;

import com.dropbox.core.v2.DbxSharing;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.dci.dto.Audio;
import com.elastica.beatle.dci.dto.ContentChecks;
import com.elastica.beatle.dci.dto.ContentIqViolation;
import com.elastica.beatle.dci.dto.CryptographicKeys;
import com.elastica.beatle.dci.dto.Executable;
import com.elastica.beatle.dci.dto.Glba;
import com.elastica.beatle.dci.dto.Hipaa;
import com.elastica.beatle.dci.dto.Hit;
import com.elastica.beatle.dci.dto.Hits;
import com.elastica.beatle.dci.dto.Image;
import com.elastica.beatle.dci.dto.Pci;
import com.elastica.beatle.dci.dto.Pii;
import com.elastica.beatle.dci.dto.Source;
import com.elastica.beatle.dci.dto.VbaMacros;
import com.elastica.beatle.dci.dto.Video;
import com.elastica.beatle.dci.dto.Violation;
import com.elastica.beatle.dci.dto.Virus;
import com.elastica.beatle.dci.dto.ciq.CIQProfile;
import com.elastica.beatle.dci.dto.contentType.ContentType;
import com.elastica.beatle.dci.dto.contentType.ContentTypes;
import com.elastica.beatle.dci.dto.contentVulnerability.ContentVulnerabilities;
import com.elastica.beatle.dci.dto.contentVulnerability.Expression;
import com.elastica.beatle.dci.dto.contentVulnerability.Reason;
import com.elastica.beatle.dci.dto.contentVulnerability.Value;
import com.elastica.beatle.dci.dto.dictionary.Dictionary;
import com.elastica.beatle.dci.dto.fileType.FileType;
import com.elastica.beatle.dci.dto.notification.Notification;
import com.elastica.beatle.dci.dto.tp.Data;
import com.elastica.beatle.dci.dto.tp.DataDetailed;
import com.elastica.beatle.dci.dto.tp.Meta;
import com.elastica.beatle.dci.dto.tp.TrainingProfile;
import com.elastica.beatle.dci.dto.tp.TrainingProfileDetailed;
import com.elastica.beatle.dci.dto.vulnerability.Vulnerability;
import com.elastica.beatle.dci.splunk.dto.DCISplunkResults;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.VulnerabilityTypes;
import com.elastica.beatle.splunk.SplunkQueries;
import com.elastica.beatle.splunk.SplunkQueryResult;
import com.google.api.services.gmail.model.Message;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.universal.common.DropBox;
import com.universal.common.GDrive;
import com.universal.common.GDriveAuthorization;
import com.universal.common.GoogleMailServices;
import com.universal.common.Office365MailActivities;
import com.universal.common.Salesforce;
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
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.Folder;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.Metadata;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;
import com.universal.dtos.onedrive.UserRoleAssignment;
import com.universal.dtos.salesforce.ChatterFile;

import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

/**
 * DCI Functions
 * @author eldorajan
 *
 */
public class DCIFunctions {

	public enum SeverityType {
		Critical("critical"),
		Informational("informational"),
		Warning("warning")
		;
		private String severityType;

		SeverityType(final String severityType) {
			this.severityType = severityType;
		}

		public String getSaasType() {
			return this.severityType;
		}
	}



	/**
	 * Get JSON Value
	 * @param json
	 * @param key
	 * @return
	 */
	public String getJSONValue(String json, String key) {
		JsonFactory factory = new JsonFactory();

		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rootNode.get(key).toString();

	}



	/**
	 * Get search query builder
	 * @param tsfrom
	 * @param tsto
	 * @param facility
	 * @param sourceType
	 * @param severityType
	 * @param query
	 * @param sourceName
	 * @param apiServerUrl
	 * @param csrftoken
	 * @param sessionid
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForDCI(TestSuiteDTO suiteData, String tsFrom, String tsTo, String facility,
			String sourceType, String severity, String query, String sourceName, String activityType) throws Exception {
		SaasType saasType = SaasType.getSaasType(suiteData.getSaasApp());
		if(saasType.equals(SaasType.Office365MailAttachment)){
			if(severity.equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
				/** No need of updating the query **/
			}else{
				query = "Email file attachment "+ query;
			}

		}else if(saasType.equals(SaasType.Office365MailBody)){
			if(severity.equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
				/** No need of updating the query **/
			}else{
				query = "Email message "+ query;
			}
		}

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				field("from", 0).
				field("size", 160).
				startObject("sort").
				startObject("created_timestamp").
				field("order", "desc").
				field("ignore_unmapped", "true").
				endObject().
				endObject().
				startObject("query").
				startObject("filtered").
				startObject("query").
				startObject("bool").
				startArray("must").
				startObject().
				startObject("range").
				startObject("created_timestamp").
				field("from", tsFrom).
				field("to", tsTo).
				endObject().
				endObject().
				endObject().
				startObject().
				startObject("query_string").
				field("query", query).
				field("default_operator", "AND").
				field("analyzer", "custom_search_analyzer").
				field("allow_leading_wildcard", "false").
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("facility", facility).
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("severity", severity).
				endObject().
				endObject();

		if(activityType.equalsIgnoreCase(DCIConstants.CIActivityType)){
			builder = builder.startObject().
					startObject("term").
					field("__source", sourceType).
					endObject().
					endObject();
		}


		builder = builder.startObject().
				startObject("term").
				field("Activity_type", activityType).
				endObject().
				endObject().
				endArray().
				endObject().	
				endObject().
				startObject("filter").
				endObject().
				endObject().
				endObject().
				endObject().
				field("sourceName", sourceName).
				field("apiServerUrl", "https://"+suiteData.getApiserverHostName()+"/").
				field("csrftoken", suiteData.getCSRFToken()).
				field("sessionid", suiteData.getSessionID()).
				field("userid", suiteData.getUsername()).
				endObject(); 
		return builder.string();
	}

	public String getSearchQueryForDCI(String tsFrom, String tsTo, String facility,
			String sourceType, String query, String sourceName,
			TestSuiteDTO suiteData) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				field("from", 0).
				field("size", 160).
				startObject("sort").
				startObject("created_timestamp").
				field("order", "desc").
				field("ignore_unmapped", "true").
				endObject().
				endObject().
				startObject("query").
				startObject("filtered").
				startObject("query").
				startObject("bool").
				startArray("must").
				startObject().
				startObject("range").
				startObject("created_timestamp").
				field("from", tsFrom).
				field("to", tsTo).
				endObject().
				endObject().
				endObject().
				startObject().
				startObject("query_string").
				field("query", query).
				field("default_operator", "AND").
				field("analyzer", "custom_search_analyzer").
				field("allow_leading_wildcard", "false").
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("facility", facility).
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("__source", sourceType).
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("Activity_type", DCIConstants.CIActivityType).
				endObject().
				endObject().
				endArray().
				endObject().	
				endObject().
				startObject("filter").
				endObject().
				endObject().
				endObject().
				endObject().
				field("sourceName", sourceName).
				field("apiServerUrl", "https://"+suiteData.getApiserverHostName()+"/").
				field("csrftoken", suiteData.getCSRFToken()).
				field("sessionid", suiteData.getSessionID()).
				field("userid", suiteData.getUsername()).
				endObject(); 
		return builder.string();
	}

	public String getSearchQueryForFileName(String tsFrom, String tsTo, String facility,
			String sourceType, String query, String sourceName,
			TestSuiteDTO suiteData) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				startObject("source").
				field("from", 0).
				field("size", 160).
				startObject("sort").
				startObject("created_timestamp").
				field("order", "desc").
				field("ignore_unmapped", "true").
				endObject().
				endObject().
				startObject("query").
				startObject("filtered").
				startObject("query").
				startObject("bool").
				startArray("must").
				startObject().
				startObject("range").
				startObject("created_timestamp").
				field("from", tsFrom).
				field("to", tsTo).
				endObject().
				endObject().
				endObject().
				startObject().
				startObject("query_string").
				field("query", query).
				field("default_operator", "AND").
				field("analyzer", "custom_search_analyzer").
				field("allow_leading_wildcard", "false").
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("facility", facility).
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("__source", sourceType).
				endObject().
				endObject().
				endArray().
				endObject().	
				endObject().
				startObject("filter").
				endObject().
				endObject().
				endObject().
				endObject().
				field("sourceName", sourceName).
				field("apiServerUrl", "https://"+suiteData.getApiserverHostName()+"/").
				field("csrftoken", suiteData.getCSRFToken()).
				field("sessionid", suiteData.getSessionID()).
				field("userid", suiteData.getUsername()).
				endObject(); 
		return builder.string();
	}


	/**
	 * Get current date time
	 * @return
	 */
	public String getCurrentDateTime() {
		DateTime currentTime = DateTime.now();
		String ts = Long.toString(currentTime.getMillis() / 1000);
		return ts;
	}

	/**
	 * Get current time
	 * @return
	 */
	public String getCurrentTime() {
		DateTime currentTime = DateTime.now();
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				.withZone(DateTimeZone.forID("UTC"));
		String ts = currentTime.toString(df);
		return ts;
	}

	/**
	 * get plus minutes from current time
	 * @param minutes
	 * @return
	 */
	public String getPlusMinutesFromCurrentTime(int minutes) {
		DateTime currentTime = DateTime.now();
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				.withZone(DateTimeZone.forID("UTC"));
		String ts = currentTime.plusMinutes(minutes).toString(df);
		return ts;
	}

	/**
	 * get minus minutes from current time
	 * @param minutes
	 * @return
	 */
	public String getMinusMinutesFromCurrentTime(int minutes) {
		DateTime currentTime = DateTime.now();
		DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				.withZone(DateTimeZone.forID("UTC"));
		String ts = currentTime.minusMinutes(minutes).toString(df);
		return ts;
	}

	/**
	 * Generate sample file type from files and current date time
	 * @param fileName
	 * @param currentDateTime
	 * @return
	 */
	//	public String createSampleFileType(String filePath, String fileName, String currentDateTime) {
	//		try {
	//			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
	//			if (!dir.exists()) {
	//				if (dir.mkdir()) {
	//					// Logger.info("Temp Directory is created!");
	//				} else {
	//					Logger.info("Failed to create temp directory!");
	//				}
	//			}
	//
	//			File src = new File(filePath+ File.separator + fileName);
	//			File dest = new File(
	//					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + currentDateTime + "_" + fileName);
	//			// Logger.info(dest);
	//			if (!dest.exists()) {
	//				dest.createNewFile();
	//			}
	//			FileUtils.copyFile(src, dest);
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		return currentDateTime + "_" + fileName;
	//	}

	/**
	 * Generate sample file type from files and current date time
	 * @param fileName
	 * @param currentDateTime
	 * @return
	 */
	public String createSampleFileType(String filePath, String fileName, 
			String currentDateTime,String fileExtension) {
		String fileNameActual ="";
		try {
			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(filePath+ File.separator + fileName);

			//String fileExtensionActual = FilenameUtils.getExtension(filePath+ File.separator + fileName);
			fileNameActual = FilenameUtils.getBaseName(filePath+ File.separator + fileName);

			File dest = new File(
					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + currentDateTime + "_" + fileNameActual+"."+fileExtension);

			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currentDateTime + "_" + fileNameActual+"."+fileExtension;
	}

	public String createSampleFileType(String filePath, String fileName, 
			String fileExtension) {
		String fileNameActual ="";
		String uniqueId = UUID.randomUUID().toString();
		try {
			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(filePath+ File.separator + fileName);

			//String fileExtensionActual = FilenameUtils.getExtension(filePath+ File.separator + fileName);
			fileNameActual = FilenameUtils.getBaseName(filePath+ File.separator + fileName);

			File dest = new File(
					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + uniqueId + "_" + fileNameActual+"."+fileExtension);

			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueId + "_" + fileNameActual+"."+fileExtension;
	}

	public String createSampleFileType(String filePath, String fileName) {
		String uniqueId = UUID.randomUUID().toString();
		try {
			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			if (!dir.exists()) {
				if (dir.mkdir()) {
					// Logger.info("Temp Directory is created!");
				} else {
					Logger.info("Failed to create temp directory!");
				}
			}

			File src = new File(filePath+ File.separator + fileName);
			File dest = new File(
					DCIConstants.DCI_FILE_TEMP_PATH + File.separator + uniqueId + "_" + fileName);
			// Logger.info(dest);
			if (!dest.exists()) {
				dest.createNewFile();
			}
			FileUtils.copyFile(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uniqueId + "_" + fileName;
	}

	public String[] createSampleFileType(String folderPath, String[] fileName) {
		for (int i = 0; i < fileName.length; i++) {	
			fileName[i] = createSampleFileType(folderPath,
					fileName[i]);
		}

		return fileName;
	}




	/**
	 * Cleanup temp folder
	 */
	public void cleanupTempFolder() {
		try {
			File directory = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			FileUtils.cleanDirectory(directory);

		} catch (Exception e) {
		}finally{

		}
	}

	public boolean checkStringInArray(String[] array, String value) {
		boolean flag=false;
		for(String s:array){
			if(s.equals(value.replaceAll("^(.*?)_", ""))){
				flag=true;
				break;
			}
		}

		return flag;
	}

	/**
	 * Cleanup a file from temp folder
	 * @param filename
	 */
	public void cleanupFileFromTempFolder(String filename) {
		try {
			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					if (dir.listFiles().length == 0) {
						// dir.delete();
					} else {
						for (File f : dir.listFiles()){
							if (f.getName().equalsIgnoreCase(filename)) {
								f.delete();
								break;
							}
						}	
					}
					// dir.delete();
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String validationContentTypesLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] contentTypes, String[] docClass, int rCount) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Content classification logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple content classification logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user +"\n";
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity +"\n";
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility +"\n";
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source +"\n";
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp +"\n";
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp +"\n";

					String contentTypesExpected = null;



					if(message.contains("Mail With Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Body has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has been classified but was " + message +"\n";
						contentTypesExpected = message.replace("File " + fileName + " Mail With Body has been classified - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Attachment and Body has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has been classified but was " + message +"\n";
						contentTypesExpected = message.replace("File " + fileName + " Mail With Attachment and Body has been classified - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has been classified - but was " + message +"\n";
						contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
								"");
					}


					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected +"\n";
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name +"\n";
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type +"\n";
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson +"\n";
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName +"\n";


					if (contentTypes != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, contentTypes, mimeTypesForAFile(fileName));
					}
					if (docClass != null) {
						validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationContentTypesLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] contentTypes) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Content classification logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple content classification logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (contentTypes != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, contentTypes, mimeTypesForAFile(fileName));
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationContentTypeLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] contentTypes) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Content classification logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple content classification logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (contentTypes != null) {
						validationMessage +=validationContentCheckContentTypeLog(fileName, sourceJson, contentTypes);
					}

				}
			}

		}
		return validationMessage;
	}


	/**
	 * Method for validation of risk logs
	 * @param hits
	 * @param fileName
	 * @param userName
	 * @param fullName
	 * @param saasType
	 * @param sourceType
	 * @param risks
	 * @param docClass
	 * @return
	 */
	public String validationRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String[] docClass, int rCount) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}

		for (int i = 0; i < jArray.size(); i++) {

			String index = getJSONValue(jArray.getJSONObject(i).toString(), "_index");
			String type = getJSONValue(jArray.getJSONObject(i).toString(), "_type");
			String id = getJSONValue(jArray.getJSONObject(i).toString(), "_id");
			String score = getJSONValue(jArray.getJSONObject(i).toString(), "_score");

			validationMessage +=(!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage +=(!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage +=(!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage +=(!score.isEmpty()) ? ""
					: "score is not matching Expected:some value but was " + score;

			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String activityType = getJSONValue(sourceJson, "Activity_type");	

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity Type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage +=(message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksExpected = risksExpected.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
							if (docClass != null) {
								validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
							}
						}
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + activityType;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");}catch (Exception e) {doc_class = "";}
					JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
					if(docClassArray.size()==0){
						Logger.info(fileName+" did not get classified");
					}else{
						Logger.info(fileName+"="+doc_class);
						if (docClass != null) {
							validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
						}
					}
				}
			}
		}
		return validationMessage;
	}

	public String validationRiskLogsWithCIQ(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String[] docClass, int rCount, Map<String,String> ciqValues) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}

		for (int i = 0; i < jArray.size(); i++) {

			String index = getJSONValue(jArray.getJSONObject(i).toString(), "_index");
			String type = getJSONValue(jArray.getJSONObject(i).toString(), "_type");
			String id = getJSONValue(jArray.getJSONObject(i).toString(), "_id");
			String score = getJSONValue(jArray.getJSONObject(i).toString(), "_score");

			validationMessage +=(!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage +=(!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage +=(!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage +=(!score.isEmpty()) ? ""
					: "score is not matching Expected:some value but was " + score;

			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String activityType = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity Type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage +=(message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksExpected = risksExpected.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risks != null) {
						for (String r : risks) {

							validationMessage +=(risksExpected.contains(r)) ? ""
									: "message does not contain risk type Expected:" + r + " but was"
									+ risksExpected;

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckRiskLogs(sourceJson, risks, risksExpected, ciqValues);
					}
					if (docClass != null) {
						validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + activityType;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationRiskLogsWithCIQProfile(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String[] docClass, int rCount, Map<String,String> ciqValues) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}

		for (int i = 0; i < jArray.size(); i++) {

			String index = getJSONValue(jArray.getJSONObject(i).toString(), "_index");
			String type = getJSONValue(jArray.getJSONObject(i).toString(), "_type");
			String id = getJSONValue(jArray.getJSONObject(i).toString(), "_id");
			String score = getJSONValue(jArray.getJSONObject(i).toString(), "_score");

			validationMessage +=(!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage +=(!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage +=(!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage +=(!score.isEmpty()) ? ""
					: "score is not matching Expected:some value but was " + score;

			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String activityType = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity Type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage +=(message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksExpected = risksExpected.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risks != null) {
						for (String r : risks) {

							validationMessage +=(risksExpected.contains(r)) ? ""
									: "message does not contain risk type Expected:" + r + " but was"
									+ risksExpected;

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckCIQ(sourceJson, risks, risksExpected, ciqValues);
					}
					if (docClass != null) {
						validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
					}

				}
			}
		}
		return validationMessage;

	}

	public String validationRiskLogsContentIQProfile(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, List<String> risks, Map<String,String> ciqValues) {

		String[] risksExpected = risks.toString().replaceAll("\\[", "").replaceAll("\\]", "").split(",");

		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}

		for (int i = 0; i < jArray.size(); i++) {

			String index = getJSONValue(jArray.getJSONObject(i).toString(), "_index");
			String type = getJSONValue(jArray.getJSONObject(i).toString(), "_type");
			String id = getJSONValue(jArray.getJSONObject(i).toString(), "_id");
			String score = getJSONValue(jArray.getJSONObject(i).toString(), "_score");

			validationMessage +=(!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage +=(!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage +=(!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage +=(!score.isEmpty()) ? ""
					: "score is not matching Expected:some value but was " + score;

			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String activityType = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(activityType.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity Type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksActual="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksActual = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage +=(message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksActual = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksActual = risksActual.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksActual = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage +=(message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksActual = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksActual = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risksExpected != null) {
						for (String r : risksExpected) {

							validationMessage +=(risksActual.contains(r)) ? ""
									: "message does not contain risk type Expected:" + r + " but was"
									+ risksExpected;

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksActual)) ? ""
							: "Risks is not matching Expected:" + risksActual + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckCIQPDTPDD(sourceJson, risksExpected, risksActual, ciqValues);
					}
				}
			}
		}
		return validationMessage;

	}


	public String validationRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
							//validationMessage +=validationContentCheckDocClassLogs(sourceJson, risks);
						}
					}

				}
			}

			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
							validationMessage +=validationContentCheckDocClassLogs(sourceJson, risks);
						}
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationPCIPIIRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;

					//String file = fileName.replaceFirst("[0-9]*_", "");
					if (risksExpected.contains("PCI")) {
						String pciJson = pciRisksForAFile(fileName);
						validationMessage +=validationContentCheckPCIRiskLogs(sourceJson, risks, risksExpected, pciJson);
					}
					if (risksExpected.contains("PII")) {
						String piiJson = piiRisksForAFile(fileName);
						validationMessage +=validationContentCheckPIIRiskLogs(sourceJson, risks, risksExpected, piiJson);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationPCIRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String pciJson) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null && pciJson != null) {
						validationMessage +=validationContentCheckPCIRiskLogs(sourceJson, risks, risksExpected, pciJson);
					}

				}
			}

		}
		return validationMessage;
	}


	public String validationPIIRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String piiJson) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckPIIRiskLogs(sourceJson, risks, risksExpected, piiJson);
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
							String[] docClass= docClassTypesForAFile(fileName);
							if (docClass != null) {
								validationMessage +=validationContentCheckDocClassLogs(sourceJson, docClass);
							}
						}
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationHIPAARiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						String hipaaJson = hipaaRisksForAFile(fileName);
						validationMessage +=validationContentCheckHIPAARiskLogs(sourceJson, risks, risksExpected, hipaaJson);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationGLBARiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						String glbaJson = glbaRisksForAFile(fileName);
						validationMessage +=validationContentCheckGLBARiskLogs(sourceJson, risks, risksExpected, glbaJson);
					}
				}
			}
		}
		return validationMessage;
	}

	public String validationVBAMacrosRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						String vbaJson = vbaRisksForAFile(fileName);
						validationMessage +=validationContentCheckVBARiskLogs(sourceJson, risks, risksExpected, vbaJson);
					}
				}
			}
		}
		return validationMessage;
	}

	public String validationEncryptionRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
						}
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

					String contentJson = getJSONValue(sourceJson, "content_checks");
					String doc_class = "";try {doc_class = getJSONValue(contentJson, "doc_class");} 
					catch (Exception e) {doc_class = "";}
					if(doc_class.equalsIgnoreCase("")){

					}else{
						JSONArray docClassArray = (JSONArray) new JSONTokener(doc_class).nextValue();
						if(docClassArray.size()==0){
							Logger.info(fileName+" did not get classified");
						}else{
							Logger.info(fileName+"="+doc_class);
							validationMessage +=validationContentCheckDocClassLogs(sourceJson, risks);
						}
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationVirusRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						String virusJson = virusRisksForAFile(fileName);
						validationMessage +=validationContentCheckVirusRiskLogs(sourceJson, risks, risksExpected, virusJson);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CIInformationalSeverityType)) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage +=(contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage +=validationContentCheckContentTypeLogs(sourceJson, risks, mimeTypesForAFile(fileName));
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationFERPARiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");
					// String _ObjectName = getJSONValue(sourceJson,
					// "_ObjectName");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage +=(risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage +=(risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage +=(Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage +=validationContentCheckFERPARiskLogs(sourceJson, risks, risksExpected);
					}

				}
			}


		}
		return validationMessage;
	}


	/**
	 * 
	 * @param hits
	 * @param fileName
	 * @param userName
	 * @param fullName
	 * @param saasType
	 * @param sourceType
	 * @param risk
	 * @param contentIQProfileName
	 * @param dictionaries
	 * @param keywords
	 * @return
	 */
	public String validationRiskLogsContentIQProfilePreDefinedDictionaries(String hits, String fileName, String userName, String fullName,
			String saasType, String sourceType, String risk, String contentIQProfileName, String dictionaries, String[] keywords, int rCount) {
		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
					}
					validationMessage +=(Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage +=validationContentCheckRiskLogs
							(sourceJson,contentIQProfileName,dictionaries,keywords,name);
				}
			}

		}
		return validationMessage;
	}

	/**
	 * 
	 * @param hits
	 * @param fileName
	 * @param userName
	 * @param fullName
	 * @param saasType
	 * @param sourceType
	 * @param risk
	 * @param contentIQProfileName
	 * @param dictionaries
	 * @param count
	 * @return
	 */
	public String validationRiskLogsContentIQProfilePreDefinedTerms(String hits, String fileName, String userName,
			String fullName,String saasType, String sourceType, String risk, String contentIQProfileName, 
			String terms, String count, int rCount) {

		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == rCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == rCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+rCount+" but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}

					validationMessage +=(Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage +=validationContentCheckRiskLogs
							(sourceJson,contentIQProfileName,terms,count,risksExpected);
				}
			}

		}
		return validationMessage;
	}

	public String validationRiskLogsContentIQProfileHighSensitivityPreDefinedTerms(String hits, String fileName, String userName,
			String fullName,String saasType, String sourceType, String risk, String contentIQProfileName, 
			String terms, String count) {

		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}

					validationMessage +=(Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage +=validationContentCheckRiskLogsHighSensitivity
							(sourceJson,contentIQProfileName,terms,count,risksExpected);
				}
			}

		}
		return validationMessage;
	}

	public String validationRiskLogsContentIQProfilePreDefinedTerms(String hits, int riskCount, String fileName, String userName,
			String fullName,String saasType, String sourceType, String[] risk, String contentIQProfileName, 
			String terms, String count) {

		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		Assert.assertTrue(jArray.size() == riskCount, "Expected only "+riskCount+" logs, but was "
				+jArray.size()+" for file upload of " + fileName);
		for (int i = 0; i < 1; i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage +=(message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk[i])) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk[i])) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}

					String[] risks = risk[i].split(",");  String flag="";
					for(String r:risks){
						flag+=Boolean.toString(Risks.contains(r));
					}

					validationMessage +=(!flag.contains("false")) ? ""
							: "message does not contain risk type Expected:"+risk[i]+" but was"+ Risks;
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage +=validationContentCheckRiskLogs
							(sourceJson,contentIQProfileName,terms,count,risksExpected);
				}
			}

		}
		return validationMessage;
	}

	public String validationRiskLogsContentIQProfileCustomTerms(String hits, String fileName, String userName,
			String fullName,String saasType, String sourceType, String risk, String contentIQProfileName, 
			String terms, String count) {

		String validationMessage = "";
		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.assertTrue(jArray.size() == 1, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(jArray.size() == 1, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: 1 but was "+jArray.size());
		}
		for (int i = 0; i < jArray.size(); i++) {
			String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
			String user = getJSONValue(sourceJson, "user");
			String severity = getJSONValue(sourceJson, "severity");
			String facility = getJSONValue(sourceJson, "facility");
			String __source = getJSONValue(sourceJson, "__source");
			String created_timestamp = getJSONValue(sourceJson, "created_timestamp");
			String inserted_timestamp = getJSONValue(sourceJson, "inserted_timestamp");
			String message = getJSONValue(sourceJson, "message");
			String name = getJSONValue(sourceJson, "name");
			String Activity_type = getJSONValue(sourceJson, "Activity_type");

			if (severity.equalsIgnoreCase("\"critical\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");
					String Risks = getJSONValue(sourceJson, "Risks");

					validationMessage +=(!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage +=(severity.contains(DCIConstants.CICriticalSeverityType)) ? ""
							: "Severity is not matching Expected:"+DCIConstants.CICriticalSeverityType+" but was " + severity;
					validationMessage +=(facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage +=(__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage +=(!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage +=(!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage +=(message.contains("File " + fileName + " has risk(s) - ")||
							message.contains(risk)) ? ""
									: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
									+ "but was " + message;
					validationMessage +=(Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage +=(name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage +=(Activity_type.contains(DCIConstants.CIActivityType)) ? ""
							: "Activity type is not matching Expected:"+DCIConstants.CIActivityType+" but was " + Activity_type;
					validationMessage +=(!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");

					validationMessage +=validationContentCheckRiskLogs
							(sourceJson,contentIQProfileName,terms,count,risksExpected);
				}
			}

		}
		return validationMessage;
	}

	/**
	 * Validation of content checks fields from risk logs
	 * @param sourceJson
	 * @param risks
	 * @return
	 */
	public String validationContentCheckRiskLogs(String sourceJson, String[] risks, String risksExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}


		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = "";
			try{
				hipaaJson = getJSONValue(contentJson, "hipaa");

				String expressionsJson="";
				try {
					expressionsJson = getJSONValue(hipaaJson, "expressions");
				} catch (Exception e) {}
				String keywordsJson="";
				try {
					keywordsJson = getJSONValue(hipaaJson, "keywords");
				} catch (Exception e) {}

				String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage +=(vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckRiskLogs(String sourceJson, String[] risks, String risksExpected,
			Map<String,String> ciqValues) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}


		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = "";
			try{
				hipaaJson = getJSONValue(contentJson, "hipaa");

				String expressionsJson="";
				try {
					expressionsJson = getJSONValue(hipaaJson, "expressions");
				} catch (Exception e) {}
				String keywordsJson="";
				try {
					keywordsJson = getJSONValue(hipaaJson, "keywords");
				} catch (Exception e) {}

				String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage +=(vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		//String[] ciqTerms = ciqValues.get("ciqProfileName").split(",");

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

			ContentChecks contentChecks =  unmarshall(contentJson, ContentChecks.class);
			List<String> ciqViolationsNameList = contentChecks.getVkContentIqViolations();

			List<String> ciqViolationsList = Arrays.asList(ciqValues.get("ciqProfileName").split(","));

			if(compareEqualList(ciqViolationsNameList, ciqViolationsList)){
				Logger.info(ciqValues.get("ciqProfileName")+" exists in content vulnerabilities json");
			}else{
				validationMessage +="CIQ profile name:"+ ciqValues.get("ciqProfileName")+
						" is not found in content vulnerabilities json";
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}


			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				if(ciqValues.get("ciqProfileName").contains(key)){
					validationMessage +=(!updated_timestamp.isEmpty()) ? ""
							: "Expecting some updated_timestamp but was " + updated_timestamp;
					validationMessage +=(ciqValues.get("ciqProfileName").contains(key)) ? "" : 
						"Expecting CIQ Profile name:"+ciqValues.get("ciqProfileName")+" but was " + key;

					if(ciqValues.get("ciqType").equalsIgnoreCase("OnlyRisks")){
						Logger.info("No violationsJson to verify for only risks ciq profile:"+violationsJson);
					}
					if(ciqValues.get("ciqType").equalsIgnoreCase("PDT")){
						JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
						for (int j = 0; j < violationsArray.size(); j++) {
							String actualTerm=""; 
							try{	
								actualTerm = violationsArray.getJSONObject(j).get("predefined_term").toString();
								int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

								validationMessage +=(actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
									"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
								validationMessage +=(actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
									"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;
							}catch(NullPointerException nux){
								Logger.info("No predefined term json to be validated");
							}
						}
					}
					if(ciqValues.get("ciqType").equalsIgnoreCase("PDD")){
						JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
						for (int j = 0; j < violationsArray.size(); j++) {
							String actualTerm=""; 
							try{	
								actualTerm = violationsArray.getJSONObject(j).get("predefined_dictionary").toString();
								String actualKeyword = violationsArray.getJSONObject(j).get("keyword").toString();
								int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

								validationMessage +=(actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
									"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
								validationMessage +=(actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
									"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;
								validationMessage +=(actualKeyword.contains(ciqValues.get("ciqProfileKeyword"))) ? "" : 
									"Expecting CIQ Profile Term Keyword:"+ciqValues.get("ciqProfileKeyword")+" but was " + actualKeyword;

							}catch(NullPointerException nux){
								Logger.info("No predefined dictionary json to be validated");
							} 
						}
					}
					if(ciqValues.get("ciqType").equalsIgnoreCase("CT")){
						JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
						for (int j = 0; j < violationsArray.size(); j++) {
							String actualTerm=""; 
							try{
								actualTerm = violationsArray.getJSONObject(j).get("regex").toString();
								int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

								validationMessage +=(actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
									"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
								validationMessage +=(actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
									"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;

							}catch(NullPointerException nux){
								Logger.info("No regex json to be validated");
							}
						}
					}
					if(ciqValues.get("ciqType").equalsIgnoreCase("CD")){
						JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
						for (int j = 0; j < violationsArray.size(); j++) {
							String actualTerm=""; 
							try{
								actualTerm = violationsArray.getJSONObject(j).get("dictionary").toString();
								String actualKeyword = violationsArray.getJSONObject(j).get("keyword").toString();
								int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

								validationMessage +=(actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
									"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
								validationMessage +=(actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
									"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;
								validationMessage +=(actualKeyword.contains(ciqValues.get("ciqProfileKeyword"))) ? "" : 
									"Expecting CIQ Profile Term Keyword:"+ciqValues.get("ciqProfileKeyword")+" but was " + actualKeyword;

							}catch(NullPointerException nux){
								Logger.info("No dictionary json to be validated");
							}
						}
					}
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			ContentChecks contentChecks =  unmarshall(contentJson, ContentChecks.class);
			List<String> vkContentIqViolations = contentChecks.getVkContentIqViolations();
			validationMessage +=(vkContentIqViolations.size() == 0) ? "" : 
				"Expecting Vk ContentIq Violations as 0 but was " + vkContentIqViolations.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}


	public String validationContentCheckCIQ(String sourceJson, String[] risks, String risksExpected,
			Map<String,String> ciqValues) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}


		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = "";
			try{
				hipaaJson = getJSONValue(contentJson, "hipaa");

				String expressionsJson="";
				try {
					expressionsJson = getJSONValue(hipaaJson, "expressions");
				} catch (Exception e) {}
				String keywordsJson="";
				try {
					keywordsJson = getJSONValue(hipaaJson, "keywords");
				} catch (Exception e) {}

				String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage +=(vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		//String[] ciqTerms = ciqValues.get("ciqProfileName").split(",");

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			ContentChecks contentChecks =  unmarshall(contentJson, ContentChecks.class);
			System.out.println(contentJson);
			List<String> ciqViolationsNameList = contentChecks.getVkContentIqViolations();
			if(checkValueExistsInList(ciqViolationsNameList, ciqValues.get("ciqProfileName"))){
				Logger.info(ciqValues.get("ciqProfileName")+" exists in content vulnerabilities json");
			}else{
				validationMessage +="CIQ profile name:"+ ciqValues.get("ciqProfileName")+" is not found in content vulnerabilities json";
			}

			List<ContentIqViolation> ciqViolationsList = contentChecks.getContentIqViolations();

			Map<String,List<Violation>> ciqViolationMap = new HashMap<String,List<Violation>>();
			for(ContentIqViolation ciqViolation:ciqViolationsList){
				String key = ciqViolation.getKey();
				List<Violation> value = ciqViolation.getViolations();
				ciqViolationMap.put(key, value);
			}	

			if(ciqViolationMap.containsKey(ciqValues.get("ciqProfileName"))){
				Logger.info(ciqValues.get("ciqProfileName")+" exists in content vulnerabilities json");

				String key=ciqValues.get("ciqProfileName");
				List<Violation> value = ciqViolationMap.get(key);

				if(key.equalsIgnoreCase("risks_only")){
					Logger.info("No validation is required for risks only file");
				}else if(key.equalsIgnoreCase("content_only")){
					Logger.info("No validation is required for content only file");
				}else if(key.equalsIgnoreCase("ff_only")){
					Logger.info("No validation is required for file format only file");
				}else if(key.equalsIgnoreCase("pdt_only")){
					if(value.size()==1){
						for(Violation v:value){
							String pdt = v.getPredefinedTerm();int count=v.getCount();
							validationMessage +=(pdt.equalsIgnoreCase("Brazil CPF Number")) ? "" : "pdt value not matching Expecting Brazil CPF Number but was " + pdt;
							validationMessage +=(count==1) ? "" : "count for pdt is not matching Expecting 1 but was " + count;
						}
					}else{
						validationMessage += "Extra content is detected for pdt only file";
					}
				}else if(key.equalsIgnoreCase("pdd_only")){
					if(value.size()==1){
						for(Violation v:value){
							String pdd = v.getPredefinedDictionary();String pddTerm = v.getKeyword();int count=v.getCount();
							validationMessage +=(pdd.equalsIgnoreCase("Illegal Drugs")) ? "" : "pdd value not matching Expecting Illegal Drugs but was " + pdd;
							validationMessage +=(pddTerm.equalsIgnoreCase("anadrol")) ? "" : "pdd term not matching Expecting anadrol but was " + pddTerm;
							validationMessage +=(count==1) ? "" : "count for pdt is not matching Expecting 1 but was " + count;
						}
					}else{
						validationMessage += "Extra content is detected for pdt only file";
					}
				}else if(key.equalsIgnoreCase("ct_only")){
					if(value.size()==1){
						for(Violation v:value){
							String ct = v.getRegex();int count=v.getCount();
							validationMessage +=(ct.equalsIgnoreCase("custom_terms_only")) ? "" : "ct value not matching Expecting custom_terms_only but was " + ct;
							validationMessage +=(count==1) ? "" : "count for ct is not matching Expecting 1 but was " + count;
						}
					}else{
						validationMessage += "Extra content is detected for pdt only file";
					}
				}else if(key.equalsIgnoreCase("cd_only")){
					if(value.size()==1){
						for(Violation v:value){
							String cd = v.getDictionary();String cdTerm = v.getKeyword();int count=v.getCount();
							validationMessage +=(cd.equalsIgnoreCase("Custom_Dictionaries_Only")) ? "" : "cd value not matching Expecting Custom_Dictionaries_Only but was " + cd;
							validationMessage +=(cdTerm.equalsIgnoreCase("custom_dictionaries_only")) ? "" : "cd term not matching Expecting custom_dictionaries_only but was " + cdTerm;
							validationMessage +=(count==1) ? "" : "count for cd is not matching Expecting 1 but was " + count;
						}
					}else{
						validationMessage += "Extra content is detected for pdt only file";
					}
				}else{
					if(key.contains("Risk")){
						Logger.info("No validation is required for risks file");
					}
					if(key.contains("Content")){
						Logger.info("No validation is required for content file");
					}
					if(key.contains("FileFormat")){
						Logger.info("No validation is required for file format file");
					}
					if(key.contains("PDT")){
						for(Violation v:value){
							try{
								if(!v.getPredefinedTerm().isEmpty()){
									String pdt = v.getPredefinedTerm();int count=v.getCount();
									validationMessage +=(pdt.equalsIgnoreCase("Brazil CPF Number")) ? "" : "pdt value not matching Expecting Brazil CPF Number but was " + pdt;
									validationMessage +=(count==1) ? "" : "count for pdt is not matching Expecting 1 but was " + count;
								}
							}catch(NullPointerException nux){}
						}
					}
					if(key.contains("PDD")){
						for(Violation v:value){
							try{
								if(!v.getDictionary().contains("Custom")){
									String pdd = v.getPredefinedDictionary();String pddTerm = v.getKeyword();int count=v.getCount();
									validationMessage +=(pdd.equalsIgnoreCase("Illegal Drugs")) ? "" : "pdd value not matching Expecting Illegal Drugs but was " + pdd;
									validationMessage +=(pddTerm.equalsIgnoreCase("anadrol")) ? "" : "pdd term not matching Expecting anadrol but was " + pddTerm;
									validationMessage +=(count==1) ? "" : "count for pdt is not matching Expecting 1 but was " + count;
								}
							}catch(NullPointerException nux){}
						}
					}
					if(key.contains("CT")){
						for(Violation v:value){
							try{
								if(!v.getRegex().isEmpty()){
									String ct = v.getRegex();int count=v.getCount();
									validationMessage +=(ct.equalsIgnoreCase("custom_terms_only")) ? "" : "ct value not matching Expecting custom_terms_only but was " + ct;
									validationMessage +=(count==1) ? "" : "count for ct is not matching Expecting 1 but was " + count;
								}
							}catch(NullPointerException nux){}
						}
						if(key.contains("CD")){
							for(Violation v:value){
								try{
									if(v.getDictionary().contains("Custom")){
										String cd = v.getDictionary();String cdTerm = v.getKeyword();int count=v.getCount();
										validationMessage +=(cd.equalsIgnoreCase("Custom_Dictionaries_Only")) ? "" : "cd value not matching Expecting Custom_Dictionaries_Only but was " + cd;
										validationMessage +=(cdTerm.equalsIgnoreCase("custom_dictionaries_only")) ? "" : "cd term not matching Expecting custom_dictionaries_only but was " + cdTerm;
										validationMessage +=(count==1) ? "" : "count for cd is not matching Expecting 1 but was " + count;
									}
								}catch(NullPointerException nux){}
							}
						}
					}
				}
			}else{
				validationMessage +="CIQ profile name:"+ ciqValues.get("ciqProfileName")+" is not found in content vulnerabilities json";
			}
		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}


	public String validationContentCheckCIQPDTPDD(String sourceJson, String[] risksActual, String risksExpected,
			Map<String,String> ciqValues) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}

		/*String[] pddExpected = {"Company Confidential","Diseases","Energy","Gambling","Illegal Drugs",
				"Obscenities","Pharmaceutical Drugs","Ticker Symbols","USG Export Controlled Items","Violence"};
		 */

		if (Arrays.asList(risksActual).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risksActual).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risksActual).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = "";
			try{
				hipaaJson = getJSONValue(contentJson, "hipaa");

				String expressionsJson="";
				try {
					expressionsJson = getJSONValue(hipaaJson, "expressions");
				} catch (Exception e) {}
				String keywordsJson="";
				try {
					keywordsJson = getJSONValue(hipaaJson, "keywords");
				} catch (Exception e) {}

				String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risksActual).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risksActual).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risksActual).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risksActual).contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage +=(vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}


		if (Arrays.asList(risksActual).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			ContentChecks contentChecks =  unmarshall(contentJson, ContentChecks.class);
			List<String> ciqViolationsNameList = contentChecks.getVkContentIqViolations();
			if(checkValueExistsInList(ciqViolationsNameList, ciqValues.get("ciqProfileName"))){
				Logger.info(ciqValues.get("ciqProfileName")+" exists in content vulnerabilities json");
			}else{
				validationMessage +="CIQ profile name:"+ ciqValues.get("ciqProfileName")+" is not found in content vulnerabilities json";
			}

			List<ContentIqViolation> ciqViolationsList = contentChecks.getContentIqViolations();

			Map<String,List<Violation>> ciqViolationMap = new HashMap<String,List<Violation>>();
			for(ContentIqViolation ciqViolation:ciqViolationsList){
				String key = ciqViolation.getKey();
				List<Violation> value = ciqViolation.getViolations();
				ciqViolationMap.put(key, value);
			}	

			if(ciqViolationMap.containsKey(ciqValues.get("ciqProfileName"))){
				Logger.info(ciqValues.get("ciqProfileName")+" exists in content vulnerabilities json");

				String key=ciqValues.get("ciqProfileName");
				List<Violation> value = ciqViolationMap.get(key);

				Map<String,String> ciqPDD = new HashMap<String,String>();
				Map<String,String> ciqPDT = new HashMap<String,String>();

				if(value.size()==2){
					validationMessage +=(value.size()==2) ? "" : 
						"Expection 2 violations array for PDT/PDD combinations but was " + value.size();

					for(Violation v:value){
						String pdd = v.getPredefinedDictionary();String pdt = v.getPredefinedTerm();
						int count=v.getCount();
						if(pdt==null){
							String pddTerm = v.getKeyword();
							ciqPDD.put(pdd, pddTerm+":::"+count);
						}else if(pdd==null){
							ciqPDT.put(pdt, count+"");
						}
					}
				}else if(value.size()>2){
					validationMessage +=(value.size()>2) ? "" : 
						"Expection atleast 2 violations array for PDT/PDD combinations but was " + value.size();
					for(Violation v:value){
						String pdd = v.getPredefinedDictionary();String pdt = v.getPredefinedTerm();
						int count=v.getCount();
						if(pdt==null){
							String pddTerm = v.getKeyword();
							ciqPDD.put(pdd, pddTerm+":::"+count);
						}else if(pdd==null){
							ciqPDT.put(pdt, count+"");
						}
					}
				}

				String pdt = ciqValues.get("ciqPdt");
				String pdd = ciqValues.get("ciqPdd");


				if(pdt==null){
					String pddTerm =ciqPDD.get(pdd).split(":::")[0];
					String pddTermCount =ciqPDD.get(pdd).split(":::")[1];

					if(checkKeyExistsInMap(ciqPDD, pdd)){
						validationMessage +=(pddTermCount.equalsIgnoreCase("1")) ? "" : 
							"count for PDD is not matching Expecting 1 but was " + pddTermCount;
						validationMessage +=(pddTerm.equalsIgnoreCase(ciqValues.get("ciqPddValue"))) ? "" : 
							"PDD keyword is not matching Expecting "+ciqValues.get("ciqPddValue")
							+" but was " + pddTerm;
					}else{
						validationMessage +="CIQ dictionary "+pdd+" is not present in json";
					}
				}else if(pdd==null){
					String pdtTermCount =ciqPDT.get(pdt);

					if(checkKeyExistsInMap(ciqPDT, pdt)){
						validationMessage +=(pdtTermCount.equalsIgnoreCase("1")) ? "" : 
							"count for PDT is not matching Expecting 1 but was " + pdtTermCount;
					}else{
						validationMessage +="CIQ term "+pdt+" is not present in json";
					}
				}


			}else{
				validationMessage +="CIQ profile name:"+ ciqValues.get("ciqProfileName")+" is not found in content vulnerabilities json";
			}


		}else if (!Arrays.asList(risksActual).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}



	public String validationContentCheckContentTypeLogs(String sourceJson, String[] contentTypes, String mimeType) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");



		if (Arrays.asList(contentTypes).contains("audio")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String audioJson = getJSONValue(contentJson, "audio");

			validationMessage +=(mimetype.contains(mimeType)) ? "" : "mimetype is not Expecting "+mimeType+" but was " + mimetype;

			String reasonsJson = getJSONValue(audioJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("video")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String videoJson = getJSONValue(contentJson, "video");

			validationMessage +=(mimetype.contains(mimeType)) ? "" : "mimetype is not Expecting "+mimeType+" but was " + mimetype;

			String reasonsJson = getJSONValue(videoJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("image")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String imageJson = getJSONValue(contentJson, "image");

			validationMessage +=(mimetype.contains(mimeType)) ? "" : "mimetype is not Expecting "+mimeType+" but was " + mimetype;

			String reasonsJson = getJSONValue(imageJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("executable")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String executableJson = getJSONValue(contentJson, "executable");

			validationMessage +=(mimetype.contains(mimeType)) ? "" :
				"mimetype is not Expecting "+mimeType+" but was " + mimetype;

			String reasonsJson = getJSONValue(executableJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("cryptographic_keys")) {
			String cryptographicKeysJson = getJSONValue(contentJson, "cryptographic_keys");

			String expressionsJson="";
			try{expressionsJson = getJSONValue(cryptographicKeysJson, "expressions");}catch(Exception e){expressionsJson="";}

			String reasonsJson="";
			try{reasonsJson = getJSONValue(cryptographicKeysJson, "reasons");}catch(Exception e){reasonsJson="";}

			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}
			if(!reasonsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("legal")) {
			String legalJson="";
			try{legalJson = getJSONValue(contentJson, "legal");}catch(Exception e){legalJson="";}

			if(!legalJson.isEmpty()){
				String reasonsJson = getJSONValue(legalJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}



		}

		if (Arrays.asList(contentTypes).contains("health")) {
			String healthJson="";
			try{healthJson = getJSONValue(contentJson, "health");}catch(Exception e){healthJson="";}

			if(!healthJson.isEmpty()){
				String reasonsJson = getJSONValue(healthJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}


		if (Arrays.asList(contentTypes).contains("engineering")) {
			String engineeringJson="";
			try{engineeringJson = getJSONValue(contentJson, "engineering");}catch(Exception e){engineeringJson="";}

			if(!engineeringJson.isEmpty()){
				String reasonsJson = getJSONValue(engineeringJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("business")) {
			String businessJson="";
			try{businessJson = getJSONValue(contentJson, "business");}catch(Exception e){businessJson="";}


			if(!businessJson.isEmpty()){
				String reasonsJson = getJSONValue(businessJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("computing")) {
			String computingJson="";
			try{computingJson = getJSONValue(contentJson, "computing");}catch(Exception e){computingJson="";}


			if(!computingJson.isEmpty()){
				String reasonsJson = getJSONValue(computingJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("design")) {
			String designJson="";
			try{
				if(contentJson.contains("\"design doc\"")){
					designJson = getJSONValue(contentJson, "design doc");
				}else{
					designJson = getJSONValue(contentJson, "design");
				}
			}catch(Exception e){designJson="";}

			if(!designJson.isEmpty()){
				String reasonsJson = getJSONValue(designJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("encryption")) {
			String encryptionJson="";
			try{encryptionJson = getJSONValue(contentJson, "encryption");}catch(Exception e){encryptionJson="";}


			if(!encryptionJson.isEmpty()){
				String reasonsJson = getJSONValue(encryptionJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("source code")) {
			String sourceCodeJson="";
			try{sourceCodeJson = getJSONValue(contentJson, "source_code");}catch(Exception e){sourceCodeJson="";}


			if(!sourceCodeJson.isEmpty()){
				String reasonsJson = getJSONValue(sourceCodeJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		return validationMessage;
	}

	public String validationContentCheckContentTypeLog(String fileName, String sourceJson, String[] contentTypes) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");

		String mimetype = getJSONValue(contentJson, "mimetype");Logger.info(fileName+"="+mimetype);
		//validationMessage +=(mimetype.contains(mimeType)) ? "" : "mimetype is not Expecting "+mimeType+" but was " + mimetype;

		if (Arrays.asList(contentTypes).contains("audio")) {
			String audioJson = getJSONValue(contentJson, "audio");

			validationMessage +=(!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(audioJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("video")) {
			String videoJson = getJSONValue(contentJson, "video");

			validationMessage +=(!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(videoJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("image")) {
			String imageJson = getJSONValue(contentJson, "image");

			validationMessage +=(!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(imageJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("executable")) {
			String executableJson = getJSONValue(contentJson, "executable");

			validationMessage +=(!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(executableJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("cryptographic_keys")) {
			String cryptographicKeysJson = getJSONValue(contentJson, "cryptographic_keys");

			String expressionsJson="";
			try{expressionsJson = getJSONValue(cryptographicKeysJson, "expressions");}catch(Exception e){expressionsJson="";}

			String reasonsJson="";
			try{reasonsJson = getJSONValue(cryptographicKeysJson, "reasons");}catch(Exception e){reasonsJson="";}

			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}
			if(!reasonsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("legal")) {
			String legalJson="";
			try{legalJson = getJSONValue(contentJson, "legal");}catch(Exception e){legalJson="";}

			if(!legalJson.isEmpty()){
				String reasonsJson = getJSONValue(legalJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}



		}

		if (Arrays.asList(contentTypes).contains("health")) {
			String healthJson="";
			try{healthJson = getJSONValue(contentJson, "health");}catch(Exception e){healthJson="";}

			if(!healthJson.isEmpty()){
				String reasonsJson = getJSONValue(healthJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}


		if (Arrays.asList(contentTypes).contains("engineering")) {
			String engineeringJson="";
			try{engineeringJson = getJSONValue(contentJson, "engineering");}catch(Exception e){engineeringJson="";}

			if(!engineeringJson.isEmpty()){
				String reasonsJson = getJSONValue(engineeringJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("business")) {
			String businessJson="";
			try{businessJson = getJSONValue(contentJson, "business");}catch(Exception e){businessJson="";}


			if(!businessJson.isEmpty()){
				String reasonsJson = getJSONValue(businessJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("computing")) {
			String computingJson="";
			try{computingJson = getJSONValue(contentJson, "computing");}catch(Exception e){computingJson="";}


			if(!computingJson.isEmpty()){
				String reasonsJson = getJSONValue(computingJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}

		if (Arrays.asList(contentTypes).contains("design")) {
			String designJson="";
			try{
				if(contentJson.contains("\"design doc\"")){
					designJson = getJSONValue(contentJson, "design doc");
				}else{
					designJson = getJSONValue(contentJson, "design");
				}
			}catch(Exception e){designJson="";}

			if(!designJson.isEmpty()){
				String reasonsJson = getJSONValue(designJson, "Reasons");
				JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}



		return validationMessage;
	}



	public String validationContentCheckPCIRiskLogs(String sourceJson, String[] risks, String risksExpected,
			String pciJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}



		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			try{
				String piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				Logger.info(piiJson);

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}

		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			try{
				String pciJsonActual = getJSONValue(contentJson, "pci")
						.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");

				Logger.info("Expected Content Vulnerabilities Json: "+pciJsonExpected);
				Logger.info("Actual Content Vulnerabilities Json: "+pciJsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(pciJsonExpected, pciJsonActual);

			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");
			String keywordsJson = getJSONValue(hipaaJson, "keywords");
			String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

			jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckPIIRiskLogs(String sourceJson, String[] risks, 
			String risksExpected, String piiJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			try{
				String piiJsonActual = getJSONValue(contentJson, "pii")
						.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");

				Logger.info("Expected Content Vulnerabilities Json: "+piiJsonExpected);
				Logger.info("Actual Content Vulnerabilities Json: "+piiJsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(piiJsonExpected, piiJsonActual);

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			try{
				String pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson="";try {expressionsJson = getJSONValue(hipaaJson, "expressions");} catch (Exception e) {}
			String keywordsJson="";try {keywordsJson = getJSONValue(hipaaJson, "keywords");} catch (Exception e) {}
			String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;

			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}

			if(!keywordsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}

		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}



	public String validateContentVulnerabilitiesJson(String jsonExpected,
			String jsonActual) {
		String validationMessage="";
		try {
			Object expected = JSONParser.parseJSON(jsonExpected.replaceAll(", ", ","));
			Object actual = JSONParser.parseJSON(jsonActual.replaceAll(", ", ","));

			JSONCompareResult result = JSONCompare.compareJSON((JSONObject) expected, 
					(JSONObject) actual, JSONCompareMode.NON_EXTENSIBLE);
			if (result.failed()) {

				ContentVulnerabilities cvExpected = 
						unmarshallJSON(jsonExpected, ContentVulnerabilities.class);
				ContentVulnerabilities cvActual = 
						unmarshallJSON(jsonActual, ContentVulnerabilities.class);

				if(jsonActual.contains("\"expressions\"")){
					List<Expression> expExpected = cvExpected.getExpressions();
					List<Expression> expActual = cvActual.getExpressions();

					List<String> expNames = new ArrayList<String>();
					List<String> actualNames = new ArrayList<String>();

					Map<String,Integer> expectedKeyValue = new HashMap<String,Integer>(); 
					Map<String,Integer> actualKeyValue = new HashMap<String,Integer>();

					for(Expression e:expExpected){
						expNames.add(e.getName());

						List<Value> expValues= e.getValues();

						for(Value v:expValues){
							String key = v.getKey();
							Integer value = (Integer)v.getValue();

							if(key.contains(",")){
								String sortedKey="";
								String[] keyArray = arraySplitTrim(key,",");
								sortedKey = Arrays.toString(keyArray);
								expectedKeyValue.put(sortedKey, value);
							}else{
								expectedKeyValue.put(key, value);
							}
						}
					}
					for(Expression e:expActual){
						actualNames.add(e.getName());

						List<Value> actualValues= e.getValues();

						for(Value v:actualValues){
							String key = v.getKey();
							Integer value = (Integer)v.getValue();

							if(key.contains(",")){
								String sortedKey="";
								String[] keyArray = arraySplitTrim(key,",");
								sortedKey = Arrays.toString(keyArray);
								actualKeyValue.put(sortedKey, value);
							}else{
								actualKeyValue.put(key, value);
							}
						}
					}

					if(compareEqualList(expNames, actualNames)){
					}else{
						validationMessage+=
								"Expected Names in Expressions Section of Content Vulnerabilities Json:"+expNames+"\n"+
										"Actual Names in Expressions Section of Content Vulnerabilities Json:"+actualNames+"\n";
					}

					if(compareMap(expectedKeyValue, actualKeyValue)){
					}else{
						validationMessage+=
								"Expected Key & Values in Expressions Section of Content Vulnerabilities Json:"+Arrays.asList(expectedKeyValue)+"\n"+
										"Actual Key & Values in Expressions Section of Content Vulnerabilities Json:"+Arrays.asList(actualKeyValue)+"\n";
					}
				}
				if(jsonActual.contains("\"reasons\"")){
					List<Reason> expExpected = cvExpected.getReasons();
					List<Reason> expActual = cvActual.getReasons();

					List<String> expNames = new ArrayList<String>();
					List<String> actualNames = new ArrayList<String>();

					Map<String,String> expectedKeyValue = new HashMap<String,String>(); 
					Map<String,String> actualKeyValue = new HashMap<String,String>();

					for(Reason e:expExpected){
						expNames.add(e.getName());

						List<Value> expValues= e.getValues();

						for(Value v:expValues){
							String key = v.getKey();
							String value = (String)v.getValue();

							if(key.contains(",")){
								String sortedKey="";
								String[] keyArray = arraySplitTrim(key,",");
								sortedKey = Arrays.toString(keyArray);
								expectedKeyValue.put(sortedKey, value);
							}else{
								expectedKeyValue.put(key, value);
							}
						}
					}
					for(Reason e:expActual){
						actualNames.add(e.getName());

						List<Value> actualValues= e.getValues();

						for(Value v:actualValues){
							String key = v.getKey();
							String value = (String)v.getValue();

							if(key.contains(",")){
								String sortedKey="";
								String[] keyArray = arraySplitTrim(key,",");
								sortedKey = Arrays.toString(keyArray);
								actualKeyValue.put(sortedKey, value);
							}else{
								actualKeyValue.put(key, value);
							}
						}
					}

					if(compareEqualList(expNames, actualNames)){
					}else{
						validationMessage+=
								"Expected Names in Reasons Section of Content Vulnerabilities Json:"+expNames+"\n"+
										"Actual Names in Reasons Section of Content Vulnerabilities Json:"+actualNames+"\n";
					}

					if(compareStringMap(expectedKeyValue, actualKeyValue)){
					}else{
						validationMessage+=
								"Expected Key & Values in Reasons Section of Content Vulnerabilities Json:"+Arrays.asList(expectedKeyValue)+"\n"+
										"Actual Key & Values in Reasons Section of Content Vulnerabilities Json:"+Arrays.asList(actualKeyValue)+"\n";
					}
				}

			}
		} catch (JSONException e) {
		}
		return validationMessage;
	}

	private String[] arraySplitTrim(String splitString, String trim){
		String[] array = splitString.split(",");
		for (int i = 0; i < array.length; i++){
			array[i] = array[i].trim();
		}
		Arrays.sort(array);
		return array;
	}

	private boolean compareEqualList( List<?> list1, List<?> list2 ) {
		ArrayList<?> copy = new ArrayList<>( list1 );
		for ( Object o : list2 ) {
			if ( !copy.remove( o ) ) {
				return false;
			}
		}
		return copy.isEmpty();
	}

	private boolean compareSubList(List<?> list, List<?> sublist) {
		return Collections.indexOfSubList(list, sublist) != -1;
	}

	private boolean compareList( List<?> list1, List<?> list2 ) {
		if(list1.size()==list2.size()){
			return compareEqualList(list1, list2);
		}else if(list1.size()>list2.size()){
			return compareSubList(list1, list2);
		}else if(list2.size()>list1.size()){
			return compareSubList(list2, list1);
		}else{
			return compareEqualList(list1, list2);
		}
	}





	public boolean compareMap(Map<String,Integer> map1, Map<String,Integer> map2) {

		if (map1 == null || map2 == null)
			return false;

		for (String ch1 : map1.keySet()) {
			if (!(map1.get(ch1)==map2.get(ch1)))
				return false;

		}
		for (String ch2 : map2.keySet()) {
			if (!(map1.get(ch2)==map2.get(ch2)))
				return false;

		}

		return true;
	}

	public boolean compareStringMap(Map<String,String> map1, Map<String,String> map2) {

		if (map1 == null || map2 == null)
			return false;

		for (String ch1 : map1.keySet()) {
			if (!(map1.get(ch1)==map2.get(ch1)))
				return false;

		}
		for (String ch2 : map2.keySet()) {
			if (!(map1.get(ch2)==map2.get(ch2)))
				return false;

		}

		return true;
	}


	public String validationContentCheckHIPAARiskLogs(String sourceJson, String[] risks, String risksExpected, String hipaaJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			try{
				String piiJson = getJSONValue(contentJson, "pii");
				Logger.info("Actual PII Content Vulnerabilities Json: "+piiJson);
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				} 
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			try{  
				String pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}


			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}

		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}
		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJsonActual = getJSONValue(contentJson, "hipaa")
					.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");
			hipaaJsonActual=hipaaJsonActual.replaceAll(",\"updated_timestamp\":\"\\d*\"\\}","\\}");

			Logger.info("Expected Content Vulnerabilities Json: "+hipaaJsonExpected);
			Logger.info("Actual Content Vulnerabilities Json: "+hipaaJsonActual);

			validationMessage +=validateContentVulnerabilitiesJson(hipaaJsonExpected, hipaaJsonActual);



		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}


		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckGLBARiskLogs(String sourceJson, String[] risks,
			String risksExpected, String glbaJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJsonActual = getJSONValue(contentJson, "glba")
					.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");

			Logger.info("Expected Content Vulnerabilities Json: "+glbaJsonExpected);
			Logger.info("Actual Content Vulnerabilities Json: "+glbaJsonActual);

			validationMessage +=validateContentVulnerabilitiesJson(glbaJsonExpected, glbaJsonActual);


		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}



		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckVBARiskLogs(String sourceJson, String[] risks, 
			String risksExpected, String vbaJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}


		if (Arrays.asList(risks).contains("VBA Macros")) {

			int vba = Integer.parseInt(vk_vba_macros.replaceAll("\"", ""));
			validationMessage +=(vba>0) ? ""
					: "vk_vba_macros is not Expecting greater than zero but was " + vk_vba_macros;

			String vbaJsonActual = getJSONValue(contentJson, "vba_macros")
					.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");

			Logger.info("Expected Content Vulnerabilities Json: "+vbaJsonExpected);
			Logger.info("Actual Content Vulnerabilities Json: "+vbaJsonActual);

			validationMessage +=validateContentVulnerabilitiesJson(vbaJsonExpected, vbaJsonActual);

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}


	public String validationContentCheckVirusRiskLogs(String sourceJson, String[] risks, 
			String risksExpected, String virusJsonExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJsonActual = getJSONValue(contentJson, "virus")
					.replaceAll("\\{\"updated_timestamp\":\"\\d*\",","\\{");

			Logger.info("Expected Content Vulnerabilities Json: "+virusJsonExpected);
			Logger.info("Actual Content Vulnerabilities Json: "+virusJsonActual);

			validationMessage +=validateContentVulnerabilitiesJson(virusJsonExpected, virusJsonActual);

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckFERPARiskLogs(String sourceJson, String[] risks, String risksExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage +=(vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage +=(!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage +=(!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage +=(!name.isEmpty()) ? "" : "Expecting some name but was " + name;
			}

			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();

				validationMessage +=(!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage +=(!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage +=(cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}



	public boolean checkValueExistsInList(List<String> nameList, String name){
		boolean flag=false;

		for(String n:nameList){
			if(name.equalsIgnoreCase(n)){
				flag=true;break;
			}
		}

		return flag;
	}


	/**
	 * 
	 * @param sourceJson
	 * @param ciqProfileName
	 * @param dictionaries
	 * @param keywords
	 * @return
	 */
	public String validationContentCheckRiskLogs(String sourceJson,String ciqProfileName, String dictionaries, 
			String[] keywords, String filename) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = getJSONValue(contentJson, "vk_pii");
		String vk_pci = getJSONValue(contentJson, "vk_pci");
		String vk_hipaa = getJSONValue(contentJson, "vk_hipaa");
		String vk_virus = getJSONValue(contentJson, "vk_virus");
		String vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");
		String vk_glba = getJSONValue(contentJson, "vk_glba");
		String violations = getJSONValue(contentJson, "violations");

		String vk_content_iq_violations = "";
		try {
			vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");

			List<String> nameList = new ArrayList<String>();
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				nameList.add(name);
			}

			validationMessage +=(nameList.contains(ciqProfileName)) ? "" :
				"Expecting "+ciqProfileName+" but was " + nameList;


			String content_iq_violations = "";
			try {
				content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
			} catch (Exception e) {
				content_iq_violations = "";
			}

			List<String> keyList = new ArrayList<String>();
			List<String> keywordList = new ArrayList<String>();

			JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
			for (int i = 0; i < ciqArray.size(); i++) {
				String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
				String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
				String key = ciqArray.getJSONObject(i).get("key").toString();
				keyList.add(key);

				if(key.contains(ciqProfileName)){
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String keyword = violationsArray.getJSONObject(j).get("keyword").toString();
						String dictionary = violationsArray.getJSONObject(j).get("predefined_dictionary").toString();
						String count = violationsArray.getJSONObject(j).get("count").toString();

						keywordList.add(keyword);
						if(dictionary.contains(dictionaries)){
							validationMessage +=(dictionary.contains(dictionaries)) ? "" :
								"Expecting "+dictionaries+" but was " + dictionary;
						}else{
							validationMessage +=(!dictionary.isEmpty()) ? "" :
								"Expecting "+dictionaries+" but was " + dictionary;
						}

						if(keyword.equalsIgnoreCase("confidential")){
							int cCount = Integer.parseInt(count);
							validationMessage +=(cCount>0) ? "" :
								"For dictionary keyword:"+dictionary+" Expecting >0 but was " + count;
						}else if(keyword.equalsIgnoreCase("body")&&filename.contains("Mail With Body")){
							validationMessage +=(count.contains("2")) ? "" :
								"For dictionary keyword:"+dictionary+" Expecting 2 but was " + count;
						}else{
							validationMessage +=(count.contains("1")) ? "" :
								"For dictionary keyword:"+dictionary+"Expecting 1 but was " + count;
						}


					}
				}else{
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String keyword = violationsArray.getJSONObject(j).get("keyword").toString();
						String dictionary = violationsArray.getJSONObject(j).get("predefined_dictionary").toString();
						String count = violationsArray.getJSONObject(j).get("count").toString();

						validationMessage +=(!keyword.isEmpty()) ? "" :
							"Expecting "+Arrays.asList(keywords)+" but was " + keyword;
						validationMessage +=(!dictionary.isEmpty()) ? "" :
							"Expecting "+dictionaries+" but was " + dictionary;
						validationMessage +=(!count.isEmpty()) ? "" :
							"Expecting 1 but was " + count;
					}
				}



				validationMessage +=(!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
			}

			validationMessage +=(keyList.contains(ciqProfileName)) ? "" :
				"Expecting "+ciqProfileName+" but was " + keyList;
			if(keywordList.size()>0){
				String[] keywordActual = keywordList.toArray(new String[keywordList.size()]);Arrays.sort(keywordActual);
				String[] keywordExpected = keywords;Arrays.sort(keywordExpected);

				validationMessage +=(arrayComparison(keywordActual, keywordExpected)) ? "" :
					"Expecting keywords are"+Arrays.asList(keywordExpected)+" but was " + Arrays.asList(keywordActual);
			}


		} catch (Exception e) {
			vk_content_iq_violations = "";
		}



		validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		validationMessage +=(vk_vba_macros.contains("0")) ? "": "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	/**
	 * 
	 * @param sourceJson
	 * @param ciqProfileName
	 * @param dictionaries
	 * @param counts
	 * @return
	 */
	public String validationContentCheckRiskLogs(String sourceJson,String ciqProfileName, String terms, 
			String counts, String risksExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String violations = getJSONValue(contentJson, "violations");

		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}

		if (risksExpected.contains("ContentIQ Violations")) {
			String vk_content_iq_violations = "";
			try {
				vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");

				List<String> nameList = new ArrayList<String>();
				JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
				if(cArray.size()>1){
					for (int i = 0; i < cArray.size(); i++) {
						String name = cArray.getString(i).toString();
						if(name.contains("DCI_DIS")||name.contains("DCI_ENE")||name.contains("DCI_GAM")||
								name.contains("DCI_TIC")||name.contains("DCI_USG")||name.contains("DCI_PHA")||
								name.contains("DCI_DIS")||name.contains("DCI_DIS")||name.contains("DCI_USG")){
							Logger.info("Dictionary terms will not be added to the list of false positives");
						}else{
							nameList.add(name);
						}

					}
					if(ciqProfileName.contains("DCI_PHIDN")||ciqProfileName.contains("DCI_PCN")||ciqProfileName.contains("DCI_GRNID")){
						nameList = removeIndexFromNameList(nameList,"DCI_CANDLN",ciqProfileName);
						if(nameList.size()>1){
							validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

						}
					}else{
						if(checkRegexInNameList(nameList,"DCI_(.*)PN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)PN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)ID(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)ID(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)BA(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)BA(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)DLN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)DLN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
						else{
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;
							}


						}
					}

				}

				nameList = new ArrayList<String>();
				for (int i = 0; i < cArray.size(); i++) {
					String name = cArray.getString(i).toString();
					nameList.add(name);
				}

				validationMessage +=(nameList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + nameList;


				String content_iq_violations = "";
				try {
					content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
				} catch (Exception e) {
					content_iq_violations = "";
				}

				List<String> keyList = new ArrayList<String>();

				JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
				for (int i = 0; i < ciqArray.size(); i++) {
					String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
					String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
					String key = ciqArray.getJSONObject(i).get("key").toString();
					keyList.add(key);

					if(key.contains("DCI_CUS_")||key.contains("Titus_")){
						if(key.contains(ciqProfileName)){
							JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
							int actualCount = 0;
							for (int j = 0; j < violationsArray.size(); j++) {
								String term = violationsArray.getJSONObject(j).get("regex").toString();
								int count = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());
								if(term.contains(terms)){
									validationMessage +=(term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage +=(!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}
								int expectedCount = Integer.parseInt(counts);
								Logger.info("Expected count:"+expectedCount);
								Logger.info("Actual count:"+actualCount);
								validationMessage +=(actualCount==expectedCount) ? "" :
									"Expecting "+expectedCount+" but was " + actualCount;

							}
						}
					}else{
						if(key.contains(ciqProfileName)){
							JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
							int actualCount = 0;
							for (int j = 0; j < violationsArray.size(); j++) {
								String term = violationsArray.getJSONObject(j).get("predefined_term").toString();
								int count = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());
								if(term.contains(terms)){
									validationMessage +=(term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage +=(!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}	
							}

							int expectedCount = Integer.parseInt(counts);
							Logger.info("Expected count:"+expectedCount);
							Logger.info("Actual count:"+actualCount);
							validationMessage +=(actualCount==expectedCount) ? "" :
								"Expecting "+expectedCount+" but was " + actualCount;
						}
					}





					validationMessage +=(!updated_timestamp.isEmpty()) ? ""
							: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
				}

				validationMessage +=(keyList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + keyList;

			} catch (Exception e) {
				vk_content_iq_violations = "";
			}
		}




		if (risksExpected.contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
		}else{
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (risksExpected.contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
		}else{
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (risksExpected.contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}else{
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

		}

		if (risksExpected.contains("Virus")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;
		}else{
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (risksExpected.contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}else{
			validationMessage +=(vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (risksExpected.contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}else{
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (risksExpected.contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		}else{
			validationMessage +=(vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckRiskLogsHighSensitivity(String sourceJson,String ciqProfileName, String terms, 
			String counts, String risksExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String violations = getJSONValue(contentJson, "violations");

		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}

		if (risksExpected.contains("ContentIQ Violations")) {
			String vk_content_iq_violations = "";
			try {
				vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");

				List<String> nameList = new ArrayList<String>();
				JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
				if(cArray.size()>1){
					for (int i = 0; i < cArray.size(); i++) {
						String name = cArray.getString(i).toString();
						if(name.contains("DCI_DIS")||name.contains("DCI_ENE")||name.contains("DCI_GAM")||
								name.contains("DCI_TIC")||name.contains("DCI_USG")||name.contains("DCI_PHA")||
								name.contains("DCI_DIS")||name.contains("DCI_DIS")||name.contains("DCI_USG")){
							Logger.info("Dictionary terms will not be added to the list of false positives");
						}else{
							nameList.add(name);
						}

					}
					/*if(ciqProfileName.contains("DCI_PHIDN")||ciqProfileName.contains("DCI_PCN")||ciqProfileName.contains("DCI_GRNID")){
						nameList = removeIndexFromNameList(nameList,"DCI_CANDLN",ciqProfileName);
						if(nameList.size()>1){
							validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

						}
					}else{
						if(checkRegexInNameList(nameList,"DCI_(.*)PN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)PN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)ID(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)ID(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)BA(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)BA(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)DLN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)DLN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
						else{
							if(nameList.size()>1){
								validationMessage +="False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
					}*/

				}

				nameList = new ArrayList<String>();
				for (int i = 0; i < cArray.size(); i++) {
					String name = cArray.getString(i).toString();
					nameList.add(name);
				}

				validationMessage +=(nameList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + nameList;


				String content_iq_violations = "";
				try {
					content_iq_violations = getJSONValue(contentJson, "content_iq_violations");
				} catch (Exception e) {
					content_iq_violations = "";
				}

				List<String> keyList = new ArrayList<String>();

				JSONArray ciqArray = (JSONArray) new JSONTokener(content_iq_violations).nextValue();
				for (int i = 0; i < ciqArray.size(); i++) {
					String violationsJson = ciqArray.getJSONObject(i).get("violations").toString();
					String updated_timestamp = ciqArray.getJSONObject(i).get("updated_timestamp").toString();
					String key = ciqArray.getJSONObject(i).get("key").toString();
					keyList.add(key);

					if(key.contains("DCI_CUS_")){
						if(key.contains(ciqProfileName)){
							JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
							int actualCount = 0;

							for (int j = 0; j < violationsArray.size(); j++) {
								String term = violationsArray.getJSONObject(j).get("regex").toString();
								int count = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());
								if(term.contains(terms)){
									validationMessage +=(term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage +=(!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}
								int expectedCount = Integer.parseInt(counts);
								Logger.info("Expected count:"+expectedCount);
								Logger.info("Actual count:"+actualCount);
								validationMessage +=(actualCount==expectedCount) ? "" :
									"Expecting "+expectedCount+" but was " + actualCount;
							}
						}
					}else{
						if(key.contains(ciqProfileName)){
							JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();

							int actualCount = 0;

							for (int j = 0; j < violationsArray.size(); j++) {
								String term = violationsArray.getJSONObject(j).get("predefined_term").toString();
								int count = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());
								if(term.contains(terms)){
									validationMessage +=(term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage +=(!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}	
							}

							int expectedCount = Integer.parseInt(counts);
							validationMessage +=(actualCount==expectedCount) ? "" :
								"Expecting "+expectedCount+" but was " + actualCount;
						}
					}





					validationMessage +=(!updated_timestamp.isEmpty()) ? ""
							: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
				}

				validationMessage +=(keyList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + keyList;

			} catch (Exception e) {
				vk_content_iq_violations = "";
			}
		}




		if (risksExpected.contains("PII")) {
			validationMessage +=(vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
		}else{
			validationMessage +=(vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (risksExpected.contains("PCI")) {
			validationMessage +=(vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
		}else{
			validationMessage +=(vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (risksExpected.contains("HIPAA")) {
			validationMessage +=(vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}else{
			validationMessage +=(vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

		}

		if (risksExpected.contains("Virus")) {
			validationMessage +=(vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;
		}else{
			validationMessage +=(vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (risksExpected.contains("VBA Macros")) {
			validationMessage +=(vk_vba_macros.contains("1")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}else{
			validationMessage +=(vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (risksExpected.contains("GLBA")) {
			validationMessage +=(vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}else{
			validationMessage +=(vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (risksExpected.contains("FERPA")) {
			validationMessage +=(vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		}else{
			validationMessage +=(vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		validationMessage +=(violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	private List<String> removeIndexFromNameList(List<String> nameList, String regex, String ciqProfileName) {
		List<String> removeList = new ArrayList<String>();
		for(String n:nameList){
			Pattern r = Pattern.compile(regex);
			Matcher m = r.matcher(n);
			if (m.find()) {
				if(n.equalsIgnoreCase(ciqProfileName)){ 
				}else{
					removeList.add(n);
				}
			}
		}
		nameList.removeAll(removeList);

		return nameList;
	}


	private boolean checkRegexInNameList(List<String> nameList, String regex) {
		boolean flag = false; int count=0;

		for(String n:nameList){
			Pattern r = Pattern.compile(regex);
			Matcher m = r.matcher(n);
			if (m.find()) {
				count++;
			}
		}

		if(count>1){
			flag=true;
		}


		return flag;
	}


	/**
	 * Validation of document classification from content checks
	 * @param sourceJson
	 * @param docClass
	 * @return
	 */
	public String validationContentCheckDocClassLogs(String sourceJson, String[] docClass) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String doc_class = getJSONValue(contentJson, "doc_class");

		try {
			if (docClass != null || !Arrays.asList(docClass).isEmpty()) {
				for (String d : docClass) {
					validationMessage +=(doc_class.contains(d)) ? ""
							: "message does not contain doc class type Expected:" + d + " but was" + doc_class;
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return validationMessage;
	}

	/**
	 * Get risk types from properties file
	 * @return
	 */
	public Map<String, String> getRiskTypesFromPropertiesFile() {
		Map<String, String> riskTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/risk.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				riskTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return riskTypes;
	}

	/**
	 * get mime types for files
	 * @return
	 */
	public Map<String, String> getMimeTypesFromPropertiesFile() {
		Map<String, String> mimeTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/mimetype.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				mimeTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mimeTypes;
	}

	/**
	 * Get risk types for a file
	 * @param fileName
	 * @return
	 */


	public String[] riskTypesForAFile(String[] fileName) {
		String[] riskArray = new String[fileName.length];
		try {
			Map<String, String> riskTypes = getRiskTypesFromPropertiesFile();

			for(int i=0;i<fileName.length;i++){
				riskArray[i] = riskTypes.get(fileName[i]);
			}
		} catch (Exception ex) {

		}
		return riskArray;
	}

	public String[] docClassTypesForAFile(String[] fileName) {
		String[] docClassArray = new String[fileName.length];
		try {
			Map<String, String> docClassTypes = getDocClassTypesFromPropertiesFile();

			for(int i=0;i<fileName.length;i++){
				docClassArray[i] = docClassTypes.get(fileName[i]);
			}
		} catch (Exception ex) {

		}
		return docClassArray;
	}


	/**
	 * get document classification from properties file
	 * @return
	 */
	public Map<String, String> getDocClassTypesFromPropertiesFile() {
		Map<String, String> docClassTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/docclass.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				docClassTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return docClassTypes;
	}




	public Map<String, String> getPCIRisksFromPropertiesFile() {
		Map<String, String> pciRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/pciRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				pciRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return pciRisksTypes;
	}

	public Map<String, String> getHIPAARisksFromPropertiesFile() {
		Map<String, String> hipaaRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/hipaaRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				hipaaRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hipaaRisksTypes;
	}

	public Map<String, String> getGLBARisksFromPropertiesFile() {
		Map<String, String> hipaaRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/glbaRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				hipaaRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hipaaRisksTypes;
	}

	public Map<String, String> getVBARisksFromPropertiesFile() {
		Map<String, String> hipaaRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/vbaRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				hipaaRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hipaaRisksTypes;
	}


	public Map<String, String> getVirusRisksFromPropertiesFile() {
		Map<String, String> hipaaRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/virusRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				hipaaRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return hipaaRisksTypes;
	}



	public synchronized UserAccount getUserAccount(TestSuiteDTO suiteData) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
		UserAccount account = null;
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
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
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				account = new UserAccount(suiteData.getSaasAppUsername(), 
						suiteData.getSaasAppPassword(), suiteData.getSaasAppUserRole(), suiteData.getEnvironmentName(), null, suiteData.getDomainName());
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case SalesForce: {
				HashMap<String, String> instanceParams = new HashMap<String, String>();
				instanceParams.put("apiHost", suiteData.getSaasAppLoginHost());
				instanceParams.put("clientId", suiteData.getSaasAppClientId());
				instanceParams.put("clientSecret", suiteData.getSaasAppClientSecret());
				instanceParams.put("token", suiteData.getSaasAppToken());

				account = new UserAccount(suiteData.getSaasAppUsername(), suiteData.getSaasAppPassword(), 
						suiteData.getSaasAppUserRole(), instanceParams);
				break;
			}
			default: {

				break;
			}
			}
			waitForSeconds(2);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}
		return account;
	}

	public synchronized UniversalApi getUniversalApi(TestSuiteDTO suiteData,UserAccount account) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
		UniversalApi universalApi = null;

		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("BOX", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
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
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi = new UniversalApi("ONEDRIVEBUSINESS", account);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case SalesForce: {
				universalApi = new UniversalApi("SALESFORCE", account);
				break;
			}
			default: {

				break;
			}
			}
			waitForSeconds(2);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}
		return universalApi;
	}	


	/**
	 * create folder for saastype in saas app
	 * @param saasType
	 * @param folderName
	 * @return
	 */
	public Map<String,String> createFolder(UniversalApi universalApi, TestSuiteDTO suiteData, String folderName) {
		Logger.info("Creating new folder "+folderName+" in saas app is in progress");


		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);
		String folderId = null;String folderEtag = null;String folderType = null;
		Map<String,String> folderInfo = new HashMap<String,String>();
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				BoxFolder folderObj = universalApi.createFolder(folderName);
				folderId = folderObj.getId();
				folderEtag = folderObj.getEtag();
				folderType = folderObj.getType();
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
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
			case OneDrive:
			case Office365:
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

		folderInfo.put("folderName", folderName);
		folderInfo.put("folderId", folderId);
		folderInfo.put("folderEtag", folderEtag);
		folderInfo.put("folderType", folderType); 

		return folderInfo;
	}


	public Folder createFolderInOneDrive(UniversalApi universalApi, TestSuiteDTO suiteData, String folderName) {
		Logger.info("Creating new folder "+folderName+" in saas app is in progress");

		Folder folderObj=null;
		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				folderObj = universalApi.createFolderV2(folderName);
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

		return folderObj;
	}

	/**
	 * delete folder for saastype in saas app
	 * @param saasType
	 * @param folderId
	 */
	public void deleteFolder(UniversalApi universalApi, TestSuiteDTO suiteData, Map<String,String> folderInfo) {
		Logger.info("Deleting folder "+folderInfo.get("folderId")+" in saas app is in progress");

		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFolder(folderInfo.get("folderId"), true, null);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
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
			case OneDrive:
			case Office365:
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
	 * Delete file
	 * @param universalApi
	 * @param saasType
	 * @param fileInfo
	 */
	public void deleteFile(UniversalApi universalApi, TestSuiteDTO suiteData, Map<String,String> fileInfo) {
		Logger.info("Deleting file "+fileInfo.get("fileId")+" in saas app is in progress");
		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);
		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFile(fileInfo.get("fileId"), fileInfo.get("fileEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case Dropbox: {
				universalApi.deleteFile(fileInfo.get("fileName"), null);
				break;
			}
			case GoogleDrive: {
				universalApi.deleteFile(fileInfo.get("fileId"), null);
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				universalApi.deleteFile(fileInfo.get("fileId"), fileInfo.get("fileEtag"));
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				break;
			}
			case SalesForce:{
				Salesforce sfapi = universalApi.getSalesforce();
				sfapi.deleteFile(fileInfo.get("fileId"));
				break;
			}
			default: {

				break;
			}
			}
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		Logger.info("Deleting file "+fileInfo.get("fileId")+" in saas app is completed");

	}

	/**
	 * upload a file into folder for saastype in saas app
	 * @param saasType
	 * @param folderId
	 * @param fileName
	 */
	public Map<String,String> uploadFile(UniversalApi universalApi, TestSuiteDTO suiteData, String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);

		String fileId = null;String fileEtag = null;
		Map<String,String> fileInfo = new HashMap<String,String>();

		try {
			switch (stype) {
			case Box: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				FileUploadResponse fResponse = universalApi.uploadFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
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
			waitForSeconds(2);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

		}

		fileInfo.put("fileName", fileName);
		fileInfo.put("fileId", fileId);
		fileInfo.put("fileEtag", fileEtag);

		Logger.info("Uploading file "+fileName+" in saas app is completed");

		return fileInfo;
	}

	public ItemResource uploadFileInOneDrive(UniversalApi universalApi, TestSuiteDTO suiteData, String folderId, String fileName) {
		Logger.info("Uploading file "+fileName+" in saas app is in progress");
		String saasType = suiteData.getSaasApp();
		SaasType stype = SaasType.getSaasType(saasType);
		ItemResource itemResource=null;
		try {
			switch (stype) {

			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				itemResource = universalApi.uploadSimpleFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
			waitForSeconds(2);
		} catch (Exception ex) {
			try {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				itemResource = universalApi.uploadSimpleFile(folderId,
						DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName, fileName);
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			} catch (Exception e) {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			}

		}

		Logger.info("Uploading file "+fileName+" in saas app is completed");

		return itemResource;
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
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");

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
					String fileName=fileInfo.get("fileName");String fileId=fileInfo.get("fileId");

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
					String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");

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
				String fileName=fileInfo.get("fileName");
				String folderName=folderInfo.get("folderName");String folderId=folderInfo.get("folderId");String folderEtag=folderInfo.get("folderEtag");

				DropBox dropbox = universalApi.getDropbox();

				if(shareType.equalsIgnoreCase("Public")){
					dropbox.createSharedLinkForFolderORFile(folderId + "/" + fileName);
				}else if(shareType.equalsIgnoreCase("Internal")){
					fileInfo = uploadFile(universalApi,suiteData, folderId, fileName.replace(File.separator+getDropboxInternalFolderName(suiteData)+File.separator, ""));
					fileInfo.put("fileName", fileName);
					fileInfo.put("fileEtag", "");
				}else if(shareType.equalsIgnoreCase("External")){
					String externalUserEmailId=suiteData.getSaasAppExternalUserName();
					String externalUserAccessToken=suiteData.getSaasAppExternalUserToken();
					fileInfo = dropbox.shareAndMountFolder(folderId, externalUserEmailId, 
							DbxSharing.AccessLevel.viewer, externalUserAccessToken);
					fileInfo.put("folderId", folderId);
					fileInfo.put("folderName", folderName);
					fileInfo.put("folderEtag", folderEtag);
				}
				break;
			}
			case GoogleDrive: {
				String fileId=fileInfo.get("fileId");
				GDrive gDrive = universalApi.getgDrive();
				if(shareType.equalsIgnoreCase("External")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, "theautomationtester87@gmail.com", "user", "reader");
					/*fileInfo.put("folderId", permission.getId());
					fileInfo.put("folderEtag", permission.getEtag());
					fileInfo.put("folderName", folderName);*/
				}
				if(shareType.equalsIgnoreCase("Internal")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, suiteData.getDomainName(), "domain", "reader");
					/*fileInfo.put("fileId", permission.getId());
					fileInfo.put("fileEtag", permission.getEtag());
					fileInfo.put("fileName", fileName);*/
				}
				if(shareType.equalsIgnoreCase("Public")){
					gDrive.insertPermission(gDrive.getDriveService(), fileId, null, "anyone", "reader");
					/*fileInfo.put("fileId", permission.getId());
					fileInfo.put("fileEtag", permission.getEtag());
					fileInfo.put("fileName", fileName);*/
				}
				break;
			}
			case OneDrive:
			case Office365:
			case OneDriveBusiness: {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
				ItemResource fileResource = null;Folder folderResource = null;
				if(shareType.equalsIgnoreCase("Public")){
					shareFileOnOneDrive(fileResource, universalApi, shareType);
				}else if(shareType.equalsIgnoreCase("Internal")){
					shareFileOnOneDrive(fileResource, universalApi, shareType);
				}else if(shareType.equalsIgnoreCase("External")){
					shareFolderOnOneDrive(folderResource, universalApi, shareType);
				}
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);

				break;
			}
			default: {

				break;
			}
			}
			waitForSeconds(2);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			ex.printStackTrace();
		}


		return fileInfo;
	}

	public DocumentSharingResult shareFileOnOneDrive(ItemResource itemResource, UniversalApi universalApi, String exposureType) throws Exception{
		//share the file with Everyone
		SharingUserRoleAssignment shareObject = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> alist = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);

		if(exposureType.equalsIgnoreCase("Public")){
			userRoleAssignment.setRole(2);
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(true);
		} else if(exposureType.equalsIgnoreCase("Internal")){
			userRoleAssignment.setRole(2);
			userRoleAssignment.setUserId("Everyone except external users");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(false);
		}else if(exposureType.equalsIgnoreCase("External")){
			userRoleAssignment.setRole(1);
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			alist.add(userRoleAssignment);

			shareObject.setUserRoleAssignments(alist);
			shareObject.setResourceAddress(itemResource.getWebUrl());
			shareObject.setValidateExistingPermissions(false);
			shareObject.setAdditiveMode(true);
			shareObject.setSendServerManagedNotification(false);
			shareObject.setCustomMessage("Hi Pls. look at the following document");
			shareObject.setIncludeAnonymousLinksInNotification(false);
		}

		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(shareObject);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}


	public DocumentSharingResult shareFolderOnOneDrive(Folder itemResource, UniversalApi universalApi, String exposureType) throws Exception{
		SharingUserRoleAssignment sharingUserRoleAssignment = new SharingUserRoleAssignment();
		ArrayList<UserRoleAssignment> userRoleAssignmentList = new ArrayList<UserRoleAssignment>();
		UserRoleAssignment userRoleAssignment = new UserRoleAssignment();
		Metadata metadata = new Metadata();
		metadata.setType("SP.Sharing.UserRoleAssignment");
		userRoleAssignment.setMetadata(metadata);
		if(exposureType.equalsIgnoreCase("Public")){
			userRoleAssignment.setUserId("Everyone");
			userRoleAssignment.setRole(1);
		} else if(exposureType.equalsIgnoreCase("Internal")){
			userRoleAssignment.setUserId("Everyone except external users");
			userRoleAssignment.setRole(2);
		}else if(exposureType.equalsIgnoreCase("External")){
			userRoleAssignment.setUserId("theautomationtester87@gmail.com");
			userRoleAssignment.setRole(2);
		}
		userRoleAssignmentList.add(userRoleAssignment);
		sharingUserRoleAssignment.setUserRoleAssignments(userRoleAssignmentList);
		sharingUserRoleAssignment.setResourceAddress(itemResource.getWebUrl());
		sharingUserRoleAssignment.setValidateExistingPermissions(false);
		sharingUserRoleAssignment.setAdditiveMode(true);
		sharingUserRoleAssignment.setSendServerManagedNotification(false);
		sharingUserRoleAssignment.setCustomMessage("This is a custom message");
		if(exposureType.equalsIgnoreCase("External")){
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(true);
		}else{
			sharingUserRoleAssignment.setIncludeAnonymousLinksInNotification(false);
		}
		DocumentSharingResult documentSharingResult = universalApi.shareWithCollaborators(sharingUserRoleAssignment);
		Logger.info(documentSharingResult);
		return documentSharingResult;
	}




	public Map<String,String> sendMailWithAttachmentBody(TestSuiteDTO suiteData, Office365MailActivities objMail,
			String fileName, String bodyType) {

		String saasType = suiteData.getSaasApp();
		suiteData.getSaasAppUsername();
		suiteData.getSaasAppPassword();
		String recipientEmail = suiteData.getSaasAppUsername();

		Map<String,String> messageInfo = new HashMap<String,String>();

		SaasType stype = SaasType.getSaasType(saasType);

		String fileLocation = DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName;
		File attachmentFile = new File(fileLocation);
		ArrayList<String> attachment = new ArrayList<String>();
		attachment.add(attachmentFile.toString());

		try {

			switch (stype) {
			case Office365MailAttachment: {
				Logger.info("Sending email "+fileName+" as attachment in saas app is in progress");

				objMail.sendMail(recipientEmail, fileName+" Mail With Attachment", "This is test mail", bodyType, attachment, false);

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			case Office365MailBody: {
				Logger.info("Sending email "+fileName+" as body in saas app is in progress");

				String mailBody = readFile(fileLocation,bodyType);
				objMail.sendMail(recipientEmail, fileName+" Mail With Body", mailBody, bodyType, null, false);

				Logger.info("Sending email "+fileName+" as body in saas app is completed");
				break;
			}
			case Office365MailAttachmentBody: {
				Logger.info("Sending email "+fileName+" as attachment and body in saas app is in progress");

				String mailBody = readFile(fileLocation,bodyType);
				objMail.sendMail(recipientEmail, fileName+" Mail With Attachment and Body", mailBody, bodyType, attachment, false);


				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			default: {
				Logger.info("Saas App not supported");
				break;
			}
			}
			waitForSeconds(10);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
		}

		return messageInfo;
	}

	public Map<String,String> sendMailWithAttachmentBody(TestSuiteDTO suiteData, GoogleMailServices gobjMail,
			String fileName, String bodyType) {

		String saasType = suiteData.getSaasApp();
		String username = suiteData.getSaasAppUsername();

		Map<String,String> messageInfo = new HashMap<String,String>();

		SaasType stype = SaasType.getSaasType(saasType);

		String fileLocation = DCIConstants.DCI_FILE_TEMP_PATH + File.separator + fileName;
		File attachmentFile = new File(fileLocation);
		ArrayList<String> attachment = new ArrayList<String>();
		attachment.add(attachmentFile.toString());

		try {

			switch (stype) {
			case GmailAttachment: {
				Logger.info("Sending email "+fileName+" as attachment in saas app is in progress");

				List<String> to=new ArrayList<String>();to.add("box-"+username);
				Message message = gobjMail.sendMailWithAttachment(to, null, null, fileName+" Mail With Attachment", 
						"This is test mail", fileLocation, fileName);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			case GmailBody: {
				Logger.info("Sending email "+fileName+" as body in saas app is in progress");

				List<String> to=new ArrayList<String>();to.add("box-"+username);
				String mailBody = readFile(fileLocation,bodyType);
				Message message = gobjMail.sendMailWithBody(to, null, null, fileName+" Mail With Body", 
						mailBody);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as body in saas app is completed");
				break;
			}
			case GmailAttachmentBody: {
				Logger.info("Sending email "+fileName+" as attachment and body in saas app is in progress");

				List<String> to=new ArrayList<String>();to.add("box-"+username);
				String mailBody = readFile(fileLocation,bodyType);
				Message message = gobjMail.sendMailWithAttachment(to, null, null, fileName+" Mail With Attachment and Body", 
						mailBody, fileLocation, fileName);

				messageInfo.put("messageId", message.getId());

				Logger.info("Sending email "+fileName+" as attachment in saas app is completed");
				break;
			}
			default: {
				Logger.info("Saas App not supported");
				break;
			}
			}
			waitForSeconds(10);
		} catch (Exception ex) {
			org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
			Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
		}

		return messageInfo;
	}



	public void deleteEmails(TestSuiteDTO suiteData,List<String> mailId) {
		try {


			SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
			try {

				switch (stype) {
				case GmailAttachment:
				case GmailBody:
				case GmailAttachmentBody: {
					for(String s:mailId){
						GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
								suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken());
						gobjMail.deleteThread(s);
					}
					break;
				}
				case Office365MailAttachment:
				case Office365MailBody:
				case Office365MailAttachmentBody: {
					for(String s:mailId){
						Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),suiteData.getSaasAppPassword()); 
						DeleteMode delMode = DeleteMode.HardDelete;	
						objMail.deleteMail(s,delMode);
					}
					break;
				}
				default: {
					Logger.info("Saas App not supported");
					break;
				}
				}
				waitForSeconds(2);
			} catch (Exception ex) {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
			}


		} catch (Exception ex) {
			Logger.info("Issue with Delete Mail Operation " + ex.getLocalizedMessage());
		}
	}


	public void deleteAllEmailsFromInbox(TestSuiteDTO suiteData) {
		try {


			SaasType stype = SaasType.getSaasType(suiteData.getSaasApp());
			try {

				switch (stype) {
				case GmailAttachment:
				case GmailBody:
				case GmailAttachmentBody: {
					GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
							suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken());
					List<String> labelIds=new ArrayList<String>();labelIds.add("INBOX");
					List<Message> messages = gobjMail.listMessagesWithLabels(labelIds);
					for(Message message:messages){
						gobjMail.deleteThread(message.getThreadId());
					}
					break;
				}
				case Office365MailAttachment:
				case Office365MailBody:
				case Office365MailAttachmentBody: {
					Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),suiteData.getSaasAppPassword()); 
					objMail.emptyFolder("Inbox");
					objMail.emptyFolder("Drafts");
					break;
				}
				default: {
					Logger.info("Saas App not supported");
					break;
				}
				}
				waitForSeconds(2);
			} catch (Exception ex) {
				org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
				Logger.info("Issue in Sending Mail/Message :"+ex.getLocalizedMessage()); 
			}


		} catch (Exception ex) {
			Logger.info("Issue with Delete Mail Operation " + ex.getLocalizedMessage());
		}
	}



	public String readFile(String filePath, String bodyType) {
		String body="";

		if(filePath.contains(".pdf")){
			try{
				PDDocument document = null; 
				document = PDDocument.load(new File(filePath));
				document.getClass();
				if( !document.isEncrypted() ){
					PDFTextStripperByArea stripper = new PDFTextStripperByArea();
					stripper.setSortByPosition( true );
					PDFTextStripper Tstripper = new PDFTextStripper();
					String st = Tstripper.getText(document);
					body+=st;body+=System.getProperty("line.separator");

					document.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(filePath.contains(".xls")){
			try {
				FileInputStream file = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				XSSFSheet sheet = workbook.getSheetAt(0);
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()){
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()){
						Cell cell = cellIterator.next();
						switch (cell.getCellType()){
						case Cell.CELL_TYPE_NUMERIC:
							body+=cell.getNumericCellValue();body+=System.getProperty("line.separator");
							break;
						case Cell.CELL_TYPE_STRING:
							body+=cell.getStringCellValue();body+=System.getProperty("line.separator");
							break;
						}
					}
				}
				workbook.close();
				file.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}else if(filePath.contains(".docx")||filePath.contains(".doc")){
			try{

				File file = new File(filePath);
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				HWPFDocument document = new HWPFDocument(fis);
				WordExtractor extractor = new WordExtractor(document);
				String[] fileData = extractor.getParagraphText();
				for (int i = 0; i < fileData.length; i++){
					if (fileData[i] != null)
						body+=fileData[i];
				}
				extractor.close();
				fis.close();
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
		else{
			StringBuffer stringBuffer = new StringBuffer();
			try{
				BufferedReader br = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(filePath), "UTF8"));

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					stringBuffer.append(sCurrentLine);
					//stringBuffer.append(System.getProperty("line.separator"));
					if(bodyType.equalsIgnoreCase("html")){
						stringBuffer.append("<br>");
					}else{
						stringBuffer.append("\r\n");
					}

				}
				br.close();


			} catch (Exception e) {
				e.printStackTrace();
			}
			body = stringBuffer.toString().replaceAll("�", "");

			/*try {
				byte[] encoded = Files.readAllBytes(Paths.get(filePath));
				body = new String(encoded, Charset.defaultCharset());
			} catch (Exception e) {}*/

		}
		System.out.println("##############Email Body:"+body);
		return body;
	}



	/**
	 * generate authorization header
	 * @param username
	 * @param password
	 * @return
	 */
	public Map<String, String> getAuthorizationHeader(String username, String password) {
		String authorizationHeader = getAuthorizationHeaderValue(username, password);
		Map<String, String> HashMap_ReqHeaders = new HashMap<>();
		HashMap_ReqHeaders.put("Authorization", authorizationHeader);
		return HashMap_ReqHeaders;
	}

	/**
	 * get authorization header value
	 * @param username
	 * @param password
	 * @return
	 */
	public String getAuthorizationHeaderValue(String username, String password) {
		String str = username + ':' + password;
		String encodedAuthorization = new String(Base64.encodeBase64(str.getBytes()));
		String authorizationHeader = "Basic " + encodedAuthorization;
		return authorizationHeader;
	}








	public String getSearchQueryForCIQ(String name, String description, String severity, 
			int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms,
			List<String> valuesRiskTypes, List<String> valuesContentTypes, 
			List<String> valuesFileFormat, List<String> valuesMLProfile) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("threshold", threshold).
				field("api_enabled", apiEnabled).
				field("severity", severity);
		if(apiEnabled){
			builder.field("appliesToSecurlets", appliesToSecurlets);
		}else{
			builder.field("appliesToSecurlets", false);
		}
		builder.startArray("domains").
		value("__ALL_EL__").
		endArray().
		startArray("org_unit").
		value("__ALL_EL__").
		endArray().
		startArray("groups").
		startObject().
		field("operator", "AND").
		startArray("operands");
		if(valuesPreDefDict != null){buildValues(builder,valuesPreDefDict,"PREDEFINED_DICTIONARY",highSensitivityFlag);}
		if(valuesPreDefTerms != null){buildValues(builder,valuesPreDefTerms,"PREDEFINED_TERMS",highSensitivityFlag);}
		if(valuesCustomDict != null){buildValues(builder,valuesCustomDict,"DICTIONARY",highSensitivityFlag);}
		if(valuesCustomTerms != null){buildValues(builder,valuesCustomTerms,"CUSTOM_CONTENT",highSensitivityFlag);}	
		if(valuesRiskTypes != null){buildValues(builder,valuesRiskTypes,"DCI_RISK",false);}
		if(valuesContentTypes != null){buildValues(builder,valuesContentTypes,"DCI_CONTENT",false);}
		if(valuesFileFormat != null){buildValues(builder,valuesFileFormat,"DCI_FILE_FORMAT",false);}
		if(valuesMLProfile != null){buildValues(builder,valuesMLProfile,"ML_PROFILE",false);}
		builder.endArray().
		endObject().
		endArray().
		endObject(); 
		return builder.string();
	}
	
	public String getSearchQueryForCIQ(String profileId, String name, String description, String severity, 
			int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms,
			List<String> valuesRiskTypes, List<String> valuesContentTypes, 
			List<String> valuesFileFormat, List<String> valuesMLProfile) throws Exception {

		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("threshold", threshold).
				field("api_enabled", apiEnabled).
				field("severity", severity).
				field("profileId", profileId);
		if(apiEnabled){
			builder.field("appliesToSecurlets", appliesToSecurlets);
		}else{
			builder.field("appliesToSecurlets", false);
		}
		builder.startArray("domains").
		value("__ALL_EL__").
		endArray().
		startArray("org_unit").
		value("__ALL_EL__").
		endArray().
		startArray("groups").
		startObject().
		field("operator", "AND").
		startArray("operands");
		if(valuesPreDefDict != null){buildValues(builder,valuesPreDefDict,"PREDEFINED_DICTIONARY",highSensitivityFlag);}
		if(valuesPreDefTerms != null){buildValues(builder,valuesPreDefTerms,"PREDEFINED_TERMS",highSensitivityFlag);}
		if(valuesCustomDict != null){buildValues(builder,valuesCustomDict,"DICTIONARY",highSensitivityFlag);}
		if(valuesCustomTerms != null){buildValues(builder,valuesCustomTerms,"CUSTOM_CONTENT",highSensitivityFlag);}	
		if(valuesRiskTypes != null){buildValues(builder,valuesRiskTypes,"DCI_RISK",false);}
		if(valuesContentTypes != null){buildValues(builder,valuesContentTypes,"DCI_CONTENT",false);}
		if(valuesFileFormat != null){buildValues(builder,valuesFileFormat,"DCI_FILE_FORMAT",false);}
		if(valuesMLProfile != null){buildValues(builder,valuesMLProfile,"ML_PROFILE",false);}
		builder.endArray().
		endObject().
		endArray().
		endObject(); 
		return builder.string();
	}

	/**
	 * Build values for create CIQ operands
	 * @param builder
	 * @param values
	 * @param type
	 * @throws IOException
	 */
	public void buildValues(XContentBuilder builder,List<String> values, String type, boolean highSensitivityFlag) throws IOException {
			builder.
			startObject().
			field("name", "").
			field("is_not", false).
			field("source", type).
			field("min_count", 1);
			
			builder.startArray("value");
			for (String value : values) {
				builder.startObject().
				field("min_count", 1).
				field("key", value);
				if(type.equalsIgnoreCase("PREDEFINED_DICTIONARY")||type.equalsIgnoreCase("PREDEFINED_TERMS")||
						type.equalsIgnoreCase("DICTIONARY")||type.equalsIgnoreCase("CUSTOM_CONTENT")){
					builder.field("is_high_sensitivity", highSensitivityFlag);
				}
				builder.endObject();
			}
			builder.endArray();
			builder.endObject();
	}

	

	public String createCIQCustomTerms(Client restClient, TestSuiteDTO suiteData, String ciqCustomTerms, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" in progress");
		String contentIQProfileId=null;

		if(this.checkIfCIQProfileNameExistsAlready(restClient, suiteData, ciqProfileName)){
			Logger.info(ciqProfileName+" exists already and will not be created again");
		}else{
			try {
				List<String> valuesPreDefDict= null;
				List<String> valuesPreDefTerms=null;
				List<String> valuesCustomDict=null;
				List<String> valuesCustomTerms=new ArrayList<String>();
				valuesCustomTerms.add(ciqCustomTerms);
				List<String> valuesRiskTypes=null;
				List<String> valuesContentTypes=null;
				List<String> valuesFileFormat=null;
				List<String> valuesMLProfile=null;

				String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, severity,
						threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
						valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);
				createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
						new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

				Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" is completed");
			} catch (Exception e) {

			}	

		}

		try {
			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId);
		} catch (Exception e) {}

		return contentIQProfileId;
	}

	public String createCIQPredefinedDictionariesAndTerms(Client restClient, TestSuiteDTO suiteData, String ciqDictionary, String ciqTerm, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Predefined Dictionary:"+ciqDictionary+" with Predefined Term:"+ciqTerm+" in progress");
		String contentIQProfileId=null;

		try {
			List<String> valuesPreDefDict= new ArrayList<String>();
			valuesPreDefDict.add(ciqDictionary);
			List<String> valuesPreDefTerms= new ArrayList<String>();
			valuesPreDefTerms.add(ciqTerm);
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;
			List<String> valuesContentTypes=null;
			List<String> valuesFileFormat=null;
			List<String> valuesMLProfile=null;

			String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, severity,
					threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
					valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
					valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);
			Logger.info("CIQ Profile Creation Payload:"+payload);

			createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
					new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId );
			Logger.info("Creating CIQ Predefined Dictionary:"+ciqDictionary+" with Predefined Term:"+ciqTerm+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String createCIQPredefinedDictionaries(Client restClient, TestSuiteDTO suiteData, String ciqDictionary, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Predefined Dictionaries:"+ciqDictionary+" in progress");
		String contentIQProfileId=null;

		try {
			List<String> valuesPreDefDict= new ArrayList<String>();
			valuesPreDefDict.add(ciqDictionary);
			List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;
			List<String> valuesContentTypes=null;
			List<String> valuesFileFormat=null;
			List<String> valuesMLProfile=null;

			String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, severity, 
					threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
					valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
					valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);
			createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
					new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId );
			Logger.info("Creating CIQ Predefined Dictionaries:"+ciqDictionary+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String createCIQPredefinedTerms(Client restClient, TestSuiteDTO suiteData, String ciqTerm, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Predefined Terms:"+ciqTerm+" in progress");

		String contentIQProfileId=null;
		try {
			List<String> valuesPreDefDict= null;
			List<String> valuesPreDefTerms= new ArrayList<String>();
			valuesPreDefTerms.add(ciqTerm);
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;
			List<String> valuesContentTypes=null;
			List<String> valuesFileFormat=null;
			List<String> valuesMLProfile=null;

			String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, severity, 
					threshold, apiEnabled ,appliesToSecurlets, highSensitivityFlag, 
					valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
					valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);
			createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
					new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId);
			Logger.info("Creating CIQ Predefined Terms:"+ciqTerm+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}


	public String updateCIQPredefinedDictionary(Client restClient, TestSuiteDTO suiteData, String ciqId, String ciqDictionary, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Updating CIQ Predefined Dictionaries:"+ciqDictionary+" in progress");
		String contentIQProfileId=null;

		try {
			List<String> valuesPreDefDict= new ArrayList<String>();
			valuesPreDefDict.add(ciqDictionary);
			List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;
			List<String> valuesContentTypes=null;
			List<String> valuesFileFormat=null;
			List<String> valuesMLProfile=null;

			String payload = getSearchQueryForCIQ(ciqId, ciqProfileName, ciqProfileDescription, severity,
					threshold, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, 
					valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, valuesRiskTypes, 
					valuesContentTypes, valuesFileFormat, valuesMLProfile);
			createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
					new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId );
			Logger.info("Updating CIQ Predefined Dictionaries:"+ciqDictionary+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String updateCIQPredefinedTerm(Client restClient, TestSuiteDTO suiteData, String ciqId, String ciqTerm, String ciqProfileName,
			String ciqProfileDescription, String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Updating CIQ Predefined Terms:"+ciqTerm+" in progress");

		String contentIQProfileId=null;
		try {
			List<String> valuesPreDefDict= null;
			List<String> valuesPreDefTerms= new ArrayList<String>();
			valuesPreDefTerms.add(ciqTerm);
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;
			List<String> valuesRiskTypes=null;
			List<String> valuesContentTypes=null;
			List<String> valuesFileFormat=null;
			List<String> valuesMLProfile=null;
			
			String payload = getSearchQueryForCIQ(ciqId, ciqProfileName, ciqProfileDescription, severity,
					threshold, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, 
					valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, valuesRiskTypes, 
					valuesContentTypes, valuesFileFormat, valuesMLProfile);
			createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
					new StringEntity(payload,StandardCharsets.UTF_8), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId);
			Logger.info("Updating CIQ Predefined Terms:"+ciqTerm+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	/**
	 * Create content iq profiles
	 * @param restClient
	 * @param requestHeader
	 * @param entity
	 * @param scheme
	 * @param host
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public HttpResponse createContentIqProfile(Client restClient, String ciqProfileName, List<NameValuePair> requestHeader, 
			StringEntity entity, String scheme, String host){

		Logger.info("Creating of CIQ profile with profileName:"+ciqProfileName+" in progress");
		HttpResponse createContentIQResponse = null;
		String restAPI = "/controls/add_ciq_profile";

		try{	
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
				createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			this.waitForSeconds(1);
			try{
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}
		
		String ciqProfileId = "";
		try{
			HttpResponse listContentIQResponse = listContentIQProfile(restClient, requestHeader, 
					scheme, host);
			ciqProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(ciqProfileId);
		}catch(Exception e){
			this.waitForSeconds(1);
			try{	
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){
				try{
					URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
					createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
						createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					}
				}catch(Exception e2){}
			}
		}


		if(StringUtils.isBlank(ciqProfileId)){
			this.waitForSeconds(1);
			try{	
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e){
				try{
					URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
					createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					if(createContentIQResponse.getStatusLine().getStatusCode()==200){}else{
						createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					}
				}catch(Exception e1){}
			}
		}

		Logger.info("ContentIQ Create Response:"+ClientUtil.getResponseBody(createContentIQResponse));

		Logger.info("Creating of CIQ profile with profileName:"+ciqProfileName+" is completed");


		return createContentIQResponse;
	}


	public HttpResponse deleteContentIQProfileById(Client restClient, TestSuiteDTO suiteData, String id) {
		Logger.info("Deleting of CIQ profile:"+id+" in progress");

		HttpResponse delResponse = null;
		String restAPI = "/controls/make_api_request";

		try{	
			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);	
			waitForSeconds(1);

			delResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
			if(delResponse.getStatusLine().getStatusCode()==200){}else{
				delResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
			}
		}catch(Exception e){
			try{
				StringEntity entity = new StringEntity(
						"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);	
				waitForSeconds(1);

				delResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					delResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
				}
			}catch(Exception e1){}
		}

		Logger.info("Deleting of CIQ profile:"+id+" is completed");

		return delResponse;	
	}


	public HttpResponse createContentIqProfile(Client restClient, List<NameValuePair> requestHeader, 
			StringEntity entity, String scheme, String host) throws Exception{

		String restAPI = "/controls/add_ciq_profile";
		URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
		HttpResponse createContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);

		return createContentIQResponse;
	}		

	/**
	 * list content iq profiles
	 * @param restClient
	 * @param requestHeader
	 * @param scheme
	 * @param host
	 * @return
	 * @throws Exception
	 */
	public HttpResponse listContentIQProfile(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host){
		String restAPI = "/controls/make_api_request";
		HttpResponse listContentIQResponse =null;

		try{	
			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\"\",\"action\":\"list\",\"params\":{\"limit\":0,\"format\":\"json\"}}");	
			URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
			listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			try{
				StringEntity entity = new StringEntity(
						"{\"url\":\"contentprofiles\",\"id\":\"\",\"action\":\"list\",\"params\":{\"limit\":0,\"format\":\"json\"}}");	
				URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);	
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}


		return listContentIQResponse;	
	}

	/**
	 * 
	 * @param restClient
	 * @param requestHeader
	 * @param entity
	 * @param scheme
	 * @param host
	 * @return
	 * @throws Exception
	 */
	public HttpResponse disableContentInspection(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host){
		Logger.info("Disabling Content Inspection in progress");
		String restAPI = "/controls/make_api_request";
		HttpResponse ciqResponse = null;
		try {
			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles/granular\",\"id\":null,\"action\":\"post\",\"params\":null,\"data\":{\"risks\":[],\"contents\":[],\"ci_enabled\":false}}");	
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);		
			ciqResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.info("Disabling Content Inspection is completed");
		return ciqResponse;
	}


	/**
	 * 
	 * @param restClient
	 * @param requestHeader
	 * @param entity
	 * @param scheme
	 * @param host
	 * @return
	 * @throws Exception
	 */
	public HttpResponse enableContentInspection(Client restClient, List<NameValuePair> requestHeader, 
			StringEntity entity, String scheme, String host) {
		Logger.info("Enabling Content Inspection in progress");
		String restAPI = "/controls/make_api_request";
		HttpResponse ciqResponse = null;
		try {
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);		
			ciqResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Logger.info("Enabling Content Inspection is completed");
		return ciqResponse;
	}


	public HttpResponse enableScanNow(Client restClient, List<NameValuePair> requestHeader, 
			String saasApp, String scheme, String host) {
		HttpResponse enableScanNowResponse = null;

		try {
			String restAPI = "/admin/application/scan_content_iq/";
			StringEntity entity = new StringEntity("{\"queryObj\":{\"app_name\":\""+saasApp+"\"}}");
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			enableScanNowResponse= restClient.doPost(dataUri, requestHeader, null, entity);

			Logger.info("Response Message:"+ClientUtil.getResponseBody(enableScanNowResponse));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return enableScanNowResponse;
	}


	/**
	 * Get Content IQ Profile
	 * @param response
	 * @param profileName
	 * @return
	 */
	public String getContentIQProfileID(HttpResponse response, String profileName){
		String ciqResponse = ClientUtil.getResponseBody(response);
		String ciqProfileID = "";
		String data = getJSONValue(ciqResponse, "data");
		String ciqProfiles = getJSONValue(data, "objects");
		JSONArray ciqArray = (JSONArray) new JSONTokener(ciqProfiles).nextValue();
		for (int i = 0; i < ciqArray.size(); i++) {
			String policyNameByID = getJSONValue(ciqArray.getJSONObject(i).toString(), ProtectTestConstants.PROFILE_NAME);
			policyNameByID = policyNameByID.substring(1, policyNameByID.length() - 1);
			if (policyNameByID.equals(profileName)) {
				ciqProfileID = getJSONValue(ciqArray.getJSONObject(i).toString(), "id");
				ciqProfileID = ciqProfileID.substring(1, ciqProfileID.length() - 1);
				break;
			}
		}
		return ciqProfileID;
	}

	public void deleteAllContentIQProfiles(Client restClient, HttpResponse response, List<NameValuePair> requestHeader, 
			String scheme, String host ) throws Exception{
		Logger.info("Deletion of all CIQ profiles in progress");

		String restAPI = "/controls/make_api_request";

		String ciqResponse = ClientUtil.getResponseBody(response);
		String ciqProfileID = null;
		String data = getJSONValue(ciqResponse, "data");
		String ciqProfiles = getJSONValue(data, "objects");
		JSONArray ciqArray = (JSONArray) new JSONTokener(ciqProfiles).nextValue();
		for (int i = 0; i < ciqArray.size(); i++) {
			String ciqProfileNameByID = getJSONValue(ciqArray.getJSONObject(i).toString(), ProtectTestConstants.PROFILE_NAME);
			ciqProfileNameByID = ciqProfileNameByID.substring(1, ciqProfileNameByID.length() - 1);
			ciqProfileID = getJSONValue(ciqArray.getJSONObject(i).toString(), "id");
			ciqProfileID = ciqProfileID.substring(1, ciqProfileID.length() - 1);

			Logger.info("Deleting of CIQ profile:"+ciqProfileNameByID+" is in progress");

			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\""+ciqProfileID+"\",\"action\":\"delete\"}");	
			
			try {
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				HttpResponse delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					waitForSeconds(1);
					delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			} catch (Exception e) {
				waitForSeconds(1);
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				HttpResponse delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					waitForSeconds(1);
					delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}
			Logger.info("Deleting of CIQ profile:"+ciqProfileNameByID+" is completed");
		}

		Logger.info("Deletion of all CIQ profiles is completed");
	}


	public HttpResponse listPolicies(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host){
		String restAPI = "/controls/list";
		HttpResponse listPoliciesResponse =null;

		try{	
			URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
			listPoliciesResponse = restClient.doGet(dataUri, requestHeader);
			if(listPoliciesResponse.getStatusLine().getStatusCode()==200){}else{
				listPoliciesResponse = restClient.doGet(dataUri, requestHeader);
			}
		}catch(Exception e){
			try{
				URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
				listPoliciesResponse = restClient.doGet(dataUri, requestHeader);
				if(listPoliciesResponse.getStatusLine().getStatusCode()==200){}else{
					listPoliciesResponse = restClient.doGet(dataUri, requestHeader);
				}
			}catch(Exception e1){}
		}


		return listPoliciesResponse;	
	}

	public void deletePolicy(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host, String policyName, String policyId, String policyType) throws Exception{
		Logger.info("Deleting of Policy:"+policyName+" is in progress");

		String restAPI = "/controls/archive";
		StringEntity entity = new StringEntity(
				"{\"policy_type\":\""+policyType+"\",\"action\":true,\"sub_id\":\""+policyId+"\"}");	
		URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
		waitForSeconds(1);
		HttpResponse delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		if(delResponse.getStatusLine().getStatusCode()==200){}else{
			delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
		}

		Logger.info("Deleting of Policy:"+policyName+" is completed");
	}



	public void deleteAllPolicies(Client restClient, HttpResponse response, List<NameValuePair> requestHeader, 
			String scheme, String host ) throws Exception{
		Logger.info("Deletion of all policies in progress");

		String policiesResponse = ClientUtil.getResponseBody(response);
		String policies = getJSONValue(policiesResponse, "policies");
		JSONArray policiesArray = (JSONArray) new JSONTokener(policies).nextValue();
		for (int i = 0; i < policiesArray.size(); i++) {
			String policyName = getJSONValue(policiesArray.getJSONObject(i).toString(), "policy_name");
			String policyId = getJSONValue(policiesArray.getJSONObject(i).toString(), "sub_id");
			String policyType = getJSONValue(policiesArray.getJSONObject(i).toString(), "policy_type");
			deletePolicy(restClient, requestHeader, scheme, host, policyName, policyId, policyType);
		}

		Logger.info("Deletion of all policies is completed");
	}


	@SuppressWarnings({ "resource" })
	public String[] getKeywordsFromFile(String fileName) throws Exception{
		String filePath=DCIConstants.DCI_FILE_TEMP_PATH+File.separator+fileName;
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String str=null;
		ArrayList<String> lines = new ArrayList<String>();
		while((str = in.readLine()) != null){
			lines.add(str);
		}
		String[] linesArray = lines.toArray(new String[lines.size()]);

		/*Multiset<String> wordsMultiset = HashMultiset.create();
		Scanner scanner = new Scanner(fileName);
		while (scanner.hasNextLine()) {
			wordsMultiset.add(scanner.nextLine());
		}

		Map<String, Integer> wordMap = getWordCount(filePath);
		List<Entry<String, Integer>> list = sortByKey(wordMap);
		for(Map.Entry<String, Integer> entry:list){
			Logger.info(entry.getKey()+" ==== "+entry.getValue());
		}*/

		return linesArray;
	}


	public Map<String, Integer> getWordCount(String fileName){

		FileInputStream fis = null;
		DataInputStream dis = null;
		BufferedReader br = null;
		Map<String, Integer> wordMap = new HashMap<String, Integer>();
		try {
			fis = new FileInputStream(fileName);
			dis = new DataInputStream(fis);
			br = new BufferedReader(new InputStreamReader(dis));
			String line = null;
			while((line = br.readLine()) != null){
				StringTokenizer st = new StringTokenizer(line, " ");
				while(st.hasMoreTokens()){
					String tmp = st.nextToken().toLowerCase();
					if(wordMap.containsKey(tmp)){
						wordMap.put(tmp, wordMap.get(tmp)+1);
					} else {
						wordMap.put(tmp, 1);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try{if(br != null){ br.close();}}catch(Exception ex){}
		}
		return wordMap;
	}

	public List<Entry<String, Integer>> sortByKey(Map<String, Integer> wordMap){

		Set<Entry<String, Integer>> set = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return (o2.getKey()).compareTo( o1.getKey() );
			}
		} );
		return list;
	}

	public List<Entry<String, Integer>> sortByValue(Map<String, Integer> wordMap){

		Set<Entry<String, Integer>> set = wordMap.entrySet();
		List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(set);
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
		} );
		return list;
	}

	public <T> T unmarshall(String data, final Class<T> klass) {		
		return unmarshallJSON(data, klass);		
	}

	protected <T> T unmarshallJSON(final String json, final Class<T> klass) {
		final ObjectMapper mapper = new ObjectMapper();
		final AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		// make deserializer use JAXB annotations (only)
		mapper.setAnnotationIntrospector(introspector);
		// make serializer use JAXB annotations (only)		
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);


		try {
			return mapper.readValue(json, klass);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public String marshall(final Object object) throws JAXBException {

		try {
			return marshallJSON(object);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public String marshallJSON(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
		mapper.setAnnotationIntrospector(introspector);
		mapper.setAnnotationIntrospector(introspector);
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, object);
		return writer.toString();
	}


	public Map<String, String> getPIIRisksFromPropertiesFile() {
		Map<String, String> piiRisksTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/piiRisks.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				piiRisksTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return piiRisksTypes;
	}


	public String getEnableContentInspectionLog() {

		String payload = null;

		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().
					startObject().
					field("url", "contentprofiles/granular").
					nullField("id").
					field("action", "post").
					nullField("params").
					startObject("data").
					startArray("risks").
					value("DLP").
					value("FERPA").
					value("GLBA").
					value("HIPAA").
					value("PCI").
					value("PII").
					value("VBA Macros").
					value("Virus").
					endArray().
					startArray("contents").
					value("BUSINESS").
					value("COMPUTING").
					value("CRYPTOGRAPHIC KEYS").
					value("DESIGN DOC").
					value("ENCRYPTION").
					value("ENGINEERING").
					value("HEALTH").
					value("LEGAL").
					value("SOURCE CODE").
					endArray().
					field("ci_enabled", true).
					endObject().
					endObject();
			payload = builder.string();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return payload;
	}

	public List<String> getCIQKeysTerms() {
		List<String> ciqTermsDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesTerms.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ciqTermsDictionaries.add(key);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTermsDictionaries;
	}

	public List<String> getCIQValuesTerms() {
		List<String> ciqTerms = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesTerms.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqTerms.add(value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTerms;
	}


	public Map<String,String> getCIQKeyValueTerms() {
		Map<String,String> ciqTerms = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesTerms.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqTerms.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTerms;
	}

	public List<String> getCIQKeysTermsHighSensitivity() {
		List<String> ciqTermsDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesTermsHighSensitivity.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ciqTermsDictionaries.add(key);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTermsDictionaries;
	}

	public List<String> getCIQValuesTermsHighSensitivity() {
		List<String> ciqTermsDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesTermsHighSensitivity.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqTermsDictionaries.add(value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTermsDictionaries;
	}

	public List<String> getCIQKeysDictionaries() {
		List<String> ciqTermsDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ciqTermsDictionaries.add(key);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTermsDictionaries;
	}

	public List<String> getCIQValuesDictionaries() {
		List<String> ciqTermsDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqTermsDictionaries.add(value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqTermsDictionaries;
	}

	public String[] predefinedTermsCIQProfileName() {
		String[] array = null;
		try {
			List<String> keys = getCIQKeysTerms();
			array = new String[keys.size()];

			for(int i=0;i<keys.size();i++){
				array[i] = keys.get(i);
			}

		} catch (Exception ex) {

		}
		return array;
	}


	public String[] predefinedTermsCIQProfileText() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTerms();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileDescription() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTerms();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0]+" Description";
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileFileName() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTerms();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[1];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileCount() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTerms();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[2];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileNameHighSensitivity() {
		String[] array = null;
		try {
			List<String> keys = getCIQKeysTermsHighSensitivity();
			array = new String[keys.size()];

			for(int i=0;i<keys.size();i++){
				array[i] = keys.get(i);
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileTextHighSensitivity() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTermsHighSensitivity();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileDescriptionHighSensitivity() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTermsHighSensitivity();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0]+" Description";
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileFileNameHighSensitivity() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTermsHighSensitivity();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[1];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedTermsCIQProfileCountHighSensitivity() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesTermsHighSensitivity();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[2];
			}

		} catch (Exception ex) {

		}
		return array;
	}


	public String[] predefinedDictionariesCIQProfileName() {
		String[] array = null;
		try {
			List<String> keys = getCIQKeysDictionaries();
			array = new String[keys.size()];

			for(int i=0;i<keys.size();i++){
				array[i] = keys.get(i);
			}

		} catch (Exception ex) {

		}
		return array;
	}


	public String[] predefinedDictionariesCIQProfileText() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedDictionariesCIQProfileDescription() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0]+" Description";
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedDictionariesCIQProfileFileName() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[1];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public String[] predefinedDictionariesCIQProfileCount() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[2];
			}

		} catch (Exception ex) {

		}
		return array;
	}

	public Map<String, String> generateHeadersUI(String riskType, String appType, String isInternal, String requestType, String objectType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("requestType", requestType);
		additionalParams.put("app", appType);
		additionalParams.put("vlTypes", riskType);
		if(StringUtils.isNotBlank(objectType)){
			additionalParams.put("objectType", objectType);
		}
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, String isInternal, String requestType, String objectType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("requestType", requestType);
		additionalParams.put("app", appType);
		additionalParams.put("top", "100");
		if(StringUtils.isNotBlank(objectType)){
			additionalParams.put("objectType", objectType);
		}
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, String isInternal, String objectType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("app", appType);
		additionalParams.put("top", "100");
		if(StringUtils.isNotBlank(objectType)){
			additionalParams.put("objectType", objectType);
		}
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, boolean isExternal, String objectType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isExternal",  Boolean.toString(isExternal));
		additionalParams.put("app", appType);
		additionalParams.put("top", "100");
		if(StringUtils.isNotBlank(objectType)){
			additionalParams.put("objectType", objectType);
		}
		return additionalParams;
	}

	public Map<String, String> generateHeadersAPI(String riskType,String exposed, String isInternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		additionalParams.put("vl_types", 	riskType);
		additionalParams.put("exposed", 	exposed);
		return additionalParams;
	}

	public Map<String, String> generateHeadersAPI(String exposed, String isInternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("is_internal", isInternal);
		additionalParams.put("exposed", 	exposed);
		additionalParams.put("top", 	"100");
		return additionalParams;
	}

	public Map<String, String> generateHeadersAPI(String exposed, boolean isExternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isExternal",  Boolean.toString(isExternal));
		additionalParams.put("exposed", 	exposed);
		return additionalParams;
	}


	public List<NameValuePair> getBasicHeaders(TestSuiteDTO suiteData) throws IOException {
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler
				.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));

		return requestHeader;
	}


	public List<NameValuePair> getHeaders(TestSuiteDTO suiteData) throws IOException {
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCSRFToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		return headers;
	}


	public List<NameValuePair> getCookieHeaders(TestSuiteDTO suiteData) throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken",suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken",suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		return requestHeader;
	}

	public void waitForSeconds(long time){
		Logger.info("Waiting for " + (time) + " second(s)");
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
	}

	public void waitForMinutes(int time){
		Logger.info("Waiting for " + (time) + " min(s)");
		try {Thread.sleep(time * 60 * 1000);} catch (Exception e) {}
	}

	public void waitForOneMinute(int time){
		Logger.info("Waiting for " + (time) + " min(s)");
		try {Thread.sleep(60 * 1000);} catch (Exception e) {}
	}

	public VulnerabilityTypes getVulnerabilityTypes(TestSuiteDTO suiteData, Client restClient, String elappname, Map<String, String> additionalParams) throws Exception {

		List<NameValuePair> headers = getHeaders(suiteData);

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
		if(responseBody.contains("500 Internal Server Error")
				||responseBody.contains("\"api_response\": \"Error on url")){
			waitForSeconds(5);
			response =  restClient.doGet(uri, headers);
			responseBody = ClientUtil.getResponseBody(response);
		}

		Logger.info("Response body:"+ responseBody);
		VulnerabilityTypes vulnerabilityTypes = MarshallingUtils.unmarshall(responseBody, VulnerabilityTypes.class);
		return vulnerabilityTypes;
	}

	public Vulnerability getVulnerability(TestSuiteDTO suiteData, Client restClient, Map<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders(suiteData);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}


		String path = suiteData.getAPIMap().get("getUIVulnerabilityTypes");

		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		if(responseBody.contains("500 Internal Server Error")
				||responseBody.contains("\"api_response\": \"Error on url")){
			waitForSeconds(5);
			response =  restClient.doGet(uri, headers);
			responseBody = ClientUtil.getResponseBody(response);
		}
		responseBody = responseBody.replace(", \"api_response\": \"\", \"meta\": null, \"action_status\": \"success\", \"key\": \"results\", \"exceptions\": \"\"",
				"");

		Logger.info("Response body:"+ responseBody);
		Vulnerability vulnerability = unmarshall(responseBody, Vulnerability.class);
		return vulnerability;
	}

	public ContentTypes getContentTypes(TestSuiteDTO suiteData, Client restClient, String elappname, Map<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders(suiteData);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}


		String path = suiteData.getAPIMap().get("getDocumentClass")
				.replace("{elappname}", elappname)
				.replace("{tenant}", suiteData.getTenantName())
				.replace("{version}", suiteData.getBaseVersion());


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		if(responseBody.contains("500 Internal Server Error")
				||responseBody.contains("\"api_response\": \"Error on url")){
			waitForSeconds(5);
			response =  restClient.doGet(uri, headers);
			responseBody = ClientUtil.getResponseBody(response);
		}
		Logger.info("Response body:"+ responseBody);
		ContentTypes contentTypes = MarshallingUtils.unmarshall(responseBody, ContentTypes.class);
		return contentTypes;
	}

	public ContentType getContentType(TestSuiteDTO suiteData, Client restClient, Map<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders(suiteData);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}


		String path = suiteData.getAPIMap().get("getUIContentTypes");


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		if(responseBody.contains("500 Internal Server Error")
				||responseBody.contains("\"api_response\": \"Error on url")){
			waitForSeconds(5);
			response =  restClient.doGet(uri, headers);
			responseBody = ClientUtil.getResponseBody(response);
		}
		responseBody = responseBody.replace(", \"api_response\": \"\", \"meta\": null, \"action_status\": \"success\", \"key\": \"results\", \"exceptions\": \"\"",
				"");

		Logger.info("Response body:"+ responseBody);
		ContentType contentType = unmarshall(responseBody, ContentType.class);
		return contentType;
	}

	public FileType getFileType(TestSuiteDTO suiteData, Client restClient, Map<String, String> additionalParams) throws Exception {
		List<NameValuePair> headers = getHeaders(suiteData);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//Add all the keys
		for(String key : additionalParams.keySet()) {
			qparams.add(new BasicNameValuePair(key,  additionalParams.get(key)));
		}


		String path = suiteData.getAPIMap().get("getUIFileTypesTotal");


		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path, qparams);
		HttpResponse response =  restClient.doGet(uri, headers);
		String responseBody = ClientUtil.getResponseBody(response);
		if(responseBody.contains("500 Internal Server Error")
				||responseBody.contains("\"api_response\": \"Error on url")){
			waitForSeconds(5);
			response =  restClient.doGet(uri, headers);
			responseBody = ClientUtil.getResponseBody(response);
		}
		responseBody = responseBody.replace(", \"api_response\": \"\", \"meta\": null, \"action_status\": \"success\", \"key\": \"results\", \"exceptions\": \"\"",
				"");

		Logger.info("Response body:"+ responseBody);
		FileType fileType = unmarshall(responseBody, FileType.class);
		return fileType;
	}


	public String[] getFileName(String folderPath) {
		File dir = new File(folderPath);
		File[] fileList = dir.listFiles();
		String[] fileName = new String[fileList.length];
		for (int i = 0; i < fileName.length; i++) {
			fileName[i] = fileList[i].getName();
		}
		Arrays.sort(fileName);
		return fileName;
	}

	public List<String> getFileNames(File folder) {
		List<String> fileName = new ArrayList<String>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				fileName.addAll(getFileNames(fileEntry));
			} else {
				fileName.add(fileEntry.getName());
			}
		}
		return fileName;
	}


	public String[] getFileNameNoExtension(String folderPath) {
		File dir = new File(folderPath);
		File[] fileList = dir.listFiles();
		String[] fileName = new String[fileList.length];
		for (int i = 0; i < fileName.length; i++) {
			fileName[i] = FilenameUtils.removeExtension(fileList[i].getName());
		}
		Arrays.sort(fileName);
		return fileName;
	}

	public String[] getFileNameNoExtension(String[] fileNames) {
		String[] fileNameNoExtn = new String[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			fileNameNoExtn[i] = fileNames[i].substring(0, fileNames[i].lastIndexOf("."));
		}
		return fileNameNoExtn;
	}


	public String predefinedTermsCIQProfileName(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysTerms();
			for(int i=0;i<keys.size();i++){
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=keys.get(i);
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}


	public String predefinedTermsCIQProfileText(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysTerms();
			List<String> values = getCIQValuesTerms();
			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[0];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedTermsCIQProfileDescription(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysTerms();
			List<String> values = getCIQValuesTerms();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[0]+" Description";
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedTermsCIQProfileFileName(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysTerms();
			List<String> values = getCIQValuesTerms();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[1];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedTermsCIQProfileCount(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysTerms();
			List<String> values = getCIQValuesTerms();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[2];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedDictionariesCIQProfileName(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysDictionaries();

			for(int i=0;i<keys.size();i++){
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=keys.get(i);
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}


	public String predefinedDictionariesCIQProfileText(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysDictionaries();
			List<String> values = getCIQValuesDictionaries();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[0];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedDictionariesCIQProfileDescription(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysDictionaries();
			List<String> values = getCIQValuesDictionaries();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[0]+" Description";
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedDictionariesCIQProfileFileName(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysDictionaries();
			List<String> values = getCIQValuesDictionaries();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[1];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}

	public String predefinedDictionariesCIQProfileCount(String ciq) {
		String output = null;
		try {
			List<String> keys = getCIQKeysDictionaries();
			List<String> values = getCIQValuesDictionaries();

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				if(ciq.equalsIgnoreCase(keys.get(i))){
					output=temp[2];
					break;
				}
			}

		} catch (Exception ex) {

		}
		return output;
	}


	public int getExpectedCount(String fileName) {
		int count = 1;

		if(fileName.contains("pii.rtf")){
			count=2;
		}

		return count;
	}

	public String[] getRisksTypes(String fileName){
		String[] risks = null;
		if(fileName.contains("US_Social_Security_Number.txt")){
			risks = new String[2];
			risks[0]="PII,ContentIQ Violations";
			risks[1]="PII";
		}else if(fileName.contains("Credit_Card_Number.txt")){
			risks = new String[2];
			risks[0]="PII,ContentIQ Violations,PCI";
			risks[1]="PII,PCI";
		}else{
			risks = new String[1];
			risks[0]="ContentIQ Violations";
		}

		return risks;
	}

	public List<String> getCIQRisksTypes(String fileName){
		List<String> risks = new ArrayList<String>();
		if(fileName.contains("US_Social_Security_Number")){
			risks.add("PII,ContentIQ Violations");
		}else if(fileName.contains("Credit_Card_Number")){
			risks.add("ContentIQ Violations,PCI");
		}else{
			risks.add("ContentIQ Violations");
		}

		return risks;
	}

	public String[] merge(String[]... arrays){
		// Count the number of arrays passed for merging and the total size of resulting array
		int count = 0;
		for (String[] array: arrays){
			count += array.length;
		}
		// Create new array and copy all array contents
		String[] mergedArray = (String[]) java.lang.reflect.Array.newInstance(arrays[0][0].getClass(), count);
		int start = 0;
		for (String[] array: arrays){
			System.arraycopy(array, 0, mergedArray, start, array.length);
			start += array.length;
		}
		return mergedArray;
	}



	public String createContentIqProfile(Client restClient, TestSuiteDTO suiteData,
			String ciqType, String ciqProfileName, String ciqProfileDescription, String ciqProfileType, boolean highSensitivity){
		CIQType ciq = CIQType.getCIQType(ciqType);
		String contentIQProfileId = null;

		if(this.checkIfCIQProfileNameExistsAlready(restClient, suiteData, ciqProfileName)){
			Logger.info(ciqProfileName+" exists already and will not be created again");
		}else{
			switch (ciq) {
			case PreDefDict: {

				try {
					Logger.info("Creating CIQ Predefined Dictionaries:"+ciqProfileName+" in progress");
					List<String> valuesPreDefDict=new ArrayList<String>();
					valuesPreDefDict.add(ciqProfileType);
					List<String> valuesPreDefTerms=null;
					List<String> valuesCustomDict=null;
					List<String> valuesCustomTerms=null;
					List<String> valuesFileFormat=null;
					List<String> valuesRisks=null;
					List<String> valuesContentTypes = null;
					List<String> valuesMLProfile=null;

					createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
							valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat, valuesMLProfile, "high", 1, true, 1, highSensitivity);

					Logger.info("Creating CIQ Predefined Dictionaries:"+ciqProfileName+" is completed");
				} catch (Exception ex) {
					Logger.info("Issue with Create Content Iq Profiles:"+ciqProfileName+" with Predefined Dictionaries" + ex.getLocalizedMessage());
				}

				break;
			}
			case PreDefTerms: {

				try {
					Logger.info("Creating CIQ Predefined Terms:"+ciqProfileName+" in progress");
					List<String> valuesPreDefDict=null;
					List<String> valuesPreDefTerms=new ArrayList<String>();
					valuesPreDefTerms.add(ciqProfileType);
					List<String> valuesCustomDict=null;
					List<String> valuesCustomTerms=null;
					List<String> valuesFileFormat=null;
					List<String> valuesRisks=null;
					List<String> valuesContentTypes = null;
					List<String> valuesMLProfile=null;

					createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
							valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat, valuesMLProfile, "high", 1, true, 1, highSensitivity);

					Logger.info("Creating CIQ Predefined Terms:"+ciqProfileName+" is completed");
				} catch (Exception ex) {
					Logger.info("Issue with Create Content Iq Profiles:"+ciqProfileName+" with Predfined Terms" + ex.getLocalizedMessage());
				}

				break;
			}
			case CustomDict: {

				try {
					Logger.info("Creating CIQ Custom Dictionaries:"+ciqProfileName+" in progress");
					List<String> valuesPreDefDict=null;
					List<String> valuesPreDefTerms=null;
					List<String> valuesCustomDict=new ArrayList<String>();
					valuesCustomDict.add(ciqProfileType);
					List<String> valuesCustomTerms=null;
					List<String> valuesFileFormat=null;
					List<String> valuesRisks=null;
					List<String> valuesContentTypes = null;
					List<String> valuesMLProfile=null;

					createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
							valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat, valuesMLProfile, "high", 1, true, 1, highSensitivity);

					Logger.info("Creating CIQ Custom Dictionaries:"+ciqProfileName+" is completed");
				} catch (Exception ex) {
					Logger.info("Issue with Create Content Iq Profiles:"+ciqProfileName+" with Custom Dictionaries" + ex.getLocalizedMessage());
				}

				break;
			}
			case CustomTerms:{

				try {
					Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" in progress");
					List<String> valuesPreDefDict=null;
					List<String> valuesPreDefTerms=null;
					List<String> valuesCustomDict=null;
					List<String> valuesCustomTerms=new ArrayList<String>();
					valuesCustomTerms.add(ciqProfileType);
					List<String> valuesFileFormat=null;
					List<String> valuesRisks=null;
					List<String> valuesContentTypes = null;
					List<String> valuesMLProfile=null;

					createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
							valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat, valuesMLProfile, "high", 1, true, 1, highSensitivity);

					Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" is completed");
				} catch (Exception ex) {
					Logger.info("Issue with Create Content Iq Profiles:"+ciqProfileName+" with Custom Terms" + ex.getLocalizedMessage());
				}

				break;
			}
			case MLProfile:{

				try {
					Logger.info("Creating CIQ ML Profile:"+ciqProfileName+" in progress");
					List<String> valuesPreDefDict=null;
					List<String> valuesPreDefTerms=null;
					List<String> valuesCustomDict=null;
					List<String> valuesCustomTerms=null;
					List<String> valuesFileFormat=null;
					List<String> valuesRisks=null;
					List<String> valuesContentTypes=null;
					List<String> valuesMLProfile=new ArrayList<String>();
					valuesMLProfile.add(ciqProfileType);

					createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
							valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, valuesFileFormat, valuesMLProfile, "high", 0, true, 1, highSensitivity);

					Logger.info("Creating CIQ ML Profile:"+ciqProfileName+" is completed");
				} catch (Exception ex) {
					Logger.info("Issue with Create Content Iq Profiles:"+ciqProfileName+" with ML Profile" + ex.getLocalizedMessage());
				}

				break;
			}
			default: {

				break;
			}
			}
		}

		try {
			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId);
		} catch (Exception e) {}


		return contentIQProfileId;
	}

	public boolean checkIfCIQProfileNameExistsAlready(Client restClient,TestSuiteDTO suiteData, String ciqProfileName) {

		HttpResponse response = null;
		try {
			response = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
		} catch (Exception e) {}

		String responseBody =  ClientUtil.getResponseBody(response);
		List<String> ciqTermProfileNames = getAllCIQProfileNames(responseBody);
		if(ciqTermProfileNames.size()==0){
			return false;
		}else{
			return checkValueExistsInList(ciqTermProfileNames, ciqProfileName);
		}
	}



	/**
	 * This utility function adds appliance for DLP risk
	 * 
	 * @param suiteData
	 * @param restClient
	 * @param applianceName
	 * @return
	 * @throws Exception
	 */
	public String addDLPAppliance(TestSuiteDTO suiteData, Client restClient, String applianceName) throws Exception {

		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		HttpEntity entity = reqEntity
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addBinaryBody  ("file", new File(ProtectTestConstants.PROTECT_RESOURCE_PATH + File.separator + "icaps.pem"))
				.addTextBody("name", applianceName)
				.addTextBody("vendor", "Symantec")
				.addTextBody("host", "50.207.165.70")
				.addTextBody("port", "13444")
				.addTextBody("fileSize", "10485760")
				.addTextBody("timeout", "240")
				.addTextBody("certificate", "").build();
		String applianceId = null;

		String restApiAddAppliance = "/admin/application/add_appliance";
		URI addApplianceUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restApiAddAppliance);
		List<NameValuePair> requestHeader = ClientUtil.buildCookieHeaders(suiteData);
		HttpResponse addAppliancesResponse = restClient.doPost(addApplianceUri, requestHeader, null, entity);
		String addAppliancesResponseBody = ClientUtil.getResponseBody(addAppliancesResponse);
		Assert.assertEquals(ClientUtil.getJSONValue(addAppliancesResponseBody, "action_success"), "true");

		String fetchAppliancesResponseBody = getApplianceList(suiteData, restClient);

		String appliances = ClientUtil.getJSONValue(fetchAppliancesResponseBody, "appliances");
		JSONArray appliancesArray = (JSONArray) new JSONTokener(appliances).nextValue();
		Logger.info("Number of Appliances added: "+appliancesArray.size());
		for (int i = 0; i < appliancesArray.size(); i++) {
			String name = ClientUtil.getJSONValue(appliancesArray.getJSONObject(i).toString(), "name");
			if (name.substring(1, name.length() - 1).equals(applianceName))
				applianceId = ClientUtil.getJSONValue(appliancesArray.getJSONObject(i).toString(), "id");
		}

		String restApiSetStatus = "/admin/application/set_appliance_status/";
		URI setStatusUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restApiSetStatus);
		Logger.info("{\"id\":\""+applianceId.substring(1, applianceId.length() - 1)+"\",\"status\":{\"is_active\":true}}");
		StringEntity setStatusEntity = new StringEntity( "{\"id\":\""+applianceId.substring(1, applianceId.length() - 1)+"\",\"status\":{\"is_active\":true}}");
		HttpResponse setStatusResponse = restClient.doPost(setStatusUri, requestHeader, null, setStatusEntity);
		String setStatusResponseBody = ClientUtil.getResponseBody(setStatusResponse);  
		Logger.info("Activate Appliance Response: "+setStatusResponseBody);


		return applianceId;
	}


	/**
	 * This function fetches the appliances list
	 * 
	 * @param suiteData
	 * @param restClient
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public String getApplianceList(TestSuiteDTO suiteData, Client restClient) throws Exception,
	UnsupportedEncodingException {
		List<NameValuePair> requestHeader = ClientUtil.buildCookieHeaders(suiteData);
		String restApiGetApplicane = "/admin/application/list/get_appliance_list";
		URI getApplianceUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restApiGetApplicane);
		StringEntity entity = new StringEntity("{\"source\":{\"limit\":20,\"offset\":0}}");
		HttpResponse fetchAppliancesResponse = restClient.doPost(getApplianceUri, requestHeader, null, entity);
		String fetchAppliancesResponseBody = ClientUtil.getResponseBody(fetchAppliancesResponse);
		return fetchAppliancesResponseBody;
	}


	/**
	 * This utility function removes an existing appliance
	 * 
	 * @param id
	 * @param suiteData
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public HttpResponse removeDLPAppliance(String id, TestSuiteDTO suiteData, Client restClient) throws Exception {
		String restAPI = "/admin/application/remove_appliance";
		Logger.info("Removing Applicance Id: "+id);
		id = id.substring(1, id.length() - 1);
		StringEntity entity = new StringEntity("{\"id\":\""+id+"\"}");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
		List<NameValuePair> requestHeader = ClientUtil.buildCookieHeaders(suiteData);
		HttpResponse response = restClient.doPost(dataUri, requestHeader, null, entity);
		return response;
	}

	public int[] getIndex(String[] primaryFiles,String[] secondaryFiles){
		int[] index = new int[secondaryFiles.length];

		for(int i=0;i<secondaryFiles.length;i++){
			for(int j=0;j<primaryFiles.length;j++){
				if(secondaryFiles[i].contains(primaryFiles[j])){
					index[i]=j;
					break;
				}

			}
		}


		return index;
	}

	public Map<String,String> getCIQKeyValuesForeignLanguage() {
		Map<String,String> ciq = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/foreign_language.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciq.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciq;
	}



	public String getCIQProfileName(Map<String, String> flMap, String fileName) {
		for (Map.Entry<String, String> entry : flMap.entrySet()) {
			String key = entry.getKey();
			if(fileName.contains(key.replace("DCI_CUS_", ""))){
				return key;
			}
		}
		return null;
	}


	public List<String> fileUploadOrSendEmail(TestSuiteDTO suiteData, Map<String,String> folderInfo, String[] fileName) {

		List<String> uploadId = new ArrayList<String>();

		String bodyType="text";

		String[] fileNames = getFileExceptions();


		if(suiteData.getSaasApp().equalsIgnoreCase("Office365MailAttachment")){
			Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),
					suiteData.getSaasAppPassword()); 
			for (int i = 0; i < fileName.length; i++) {
				sendMailWithAttachmentBody(suiteData, objMail, fileName[i], bodyType);
				String uniqueId = fileName[i];
				uploadId.add(uniqueId+" Mail With Attachment");
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Office365MailBody")){
			Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),
					suiteData.getSaasAppPassword());
			for (int i = 0; i < fileName.length; i++) {
				if(checkStringInArray(fileNames,fileName[i])){bodyType="html";}
				sendMailWithAttachmentBody(suiteData, objMail, fileName[i], bodyType);
				String uniqueId = fileName[i];
				uploadId.add(uniqueId+" Mail With Body");
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Office365MailAttachmentBody")){
			Office365MailActivities objMail = new Office365MailActivities(suiteData.getSaasAppUsername(),
					suiteData.getSaasAppPassword());
			for (int i = 0; i < fileName.length; i++) {
				if(checkStringInArray(fileNames,fileName[i])){bodyType="html";}
				sendMailWithAttachmentBody(suiteData, objMail, fileName[i], bodyType);
				String uniqueId = fileName[i];
				uploadId.add(uniqueId+" Mail With Attachment and Body");
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("GmailAttachment")){
			GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
					suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
			for (int i = 0; i < fileName.length; i++) {
				Map<String,String> messageInfo = sendMailWithAttachmentBody(suiteData, gobjMail, fileName[i], bodyType);
				uploadId.add(messageInfo.get("messageId"));
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("GmailBody")){
			GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
					suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
			for (int i = 0; i < fileName.length; i++) {
				Map<String,String> messageInfo = sendMailWithAttachmentBody(suiteData, gobjMail, fileName[i], bodyType);
				uploadId.add(messageInfo.get("messageId"));
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("GmailAttachmentBody")){
			GoogleMailServices gobjMail = new GoogleMailServices(suiteData.getSaasAppClientId(), 
					suiteData.getSaasAppClientSecret(), suiteData.getSaasAppToken()); 
			for (int i = 0; i < fileName.length; i++) {
				Map<String,String> messageInfo = sendMailWithAttachmentBody(suiteData, gobjMail, fileName[i], bodyType);
				uploadId.add(messageInfo.get("messageId"));
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Salesforce")){
			UserAccount account = getUserAccount(suiteData);
			UniversalApi universalApi = getUniversalApi(suiteData, account);

			for (int i = 0; i < fileName.length; i++) {
				Map<String, String> fileInfo = uploadFile(universalApi, suiteData, null, fileName[i]);
				uploadId.add(fileInfo.get("fileId"));
			}
		}else if(suiteData.getSaasApp().equalsIgnoreCase("Box")){
			UserAccount account = getUserAccount(suiteData);
			UniversalApi universalApi = getUniversalApi(suiteData, account);

			ExecutorService executor = Executors.newFixedThreadPool(1);
			for (int i = 0; i < fileName.length; i++) {
				FileUploadThread fileUpload = new FileUploadThread(universalApi, suiteData, folderInfo.get("folderId"), fileName[i]);
				executor.execute(fileUpload);
				Map<String, String> fileInfo = fileUpload.getFileInfo();
				uploadId.add(fileInfo.get("fileId"));
			}
			executor.shutdown();
			while (!executor.isTerminated()) {
			}

			/*for (int i = 0; i < fileName.length; i++) {
				Map<String, String> fileInfo = uploadFile(universalApi, suiteData, folderInfo.get("folderId"), fileName[i]);
				uploadId.add(fileInfo.get("fileId"));
			}*/

		}else{
			UserAccount account = getUserAccount(suiteData);
			UniversalApi universalApi = getUniversalApi(suiteData, account);

			for (int i = 0; i < fileName.length; i++) {
				Map<String, String> fileInfo = uploadFile(universalApi, suiteData, folderInfo.get("folderId"), fileName[i]);
				uploadId.add(fileInfo.get("fileId"));
			}

		}

		return uploadId;
	}

	private String[] getFileExceptions() {
		List<String> fileNames = new ArrayList<String>();

		String filePath = DCIConstants.DCI_FILE_EXCEPTION_PATH; 
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filePath), "UTF8"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fileNames.add(sCurrentLine);
			}
			br.close();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames.toArray(new String[fileNames.size()]);
	}



	public Map<String,String> getTitusValues() {
		Map<String,String> titus = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/titus.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				titus.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return titus;
	}

	public String[] getCustomTermsTitus(String[] fileNames) {
		Map<String,String> titusValues = getTitusValues();
		String terms[] = new String[fileNames.length];		

		for(int i=0;i<fileNames.length;i++){
			for (String key : titusValues.keySet()) {
				if(fileNames[i].contains(key)){
					terms[i]=key;break;
				}
			}

		}	
		return terms;
	}

	public String[] getCustomTagsTitus(String[] fileNames) {
		Map<String,String> titusValues = getTitusValues();
		String terms[] = new String[fileNames.length];		

		for(int i=0;i<fileNames.length;i++){
			for (String key : titusValues.keySet()) {
				if(fileNames[i].contains(key)){
					terms[i]=titusValues.get(key);break;
				}
			}

		}	
		return terms;
	}

	public Map<String,String> getCompanyConfidentialValues() {
		Map<String,String> companyConfidential = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/company_confidential.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				companyConfidential.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return companyConfidential;
	}

	public String getCustomTermsCompanyConfidential(String fileName) {
		Map<String,String> companyConfidential = getCompanyConfidentialValues();
		String terms=null;		

		for (String key : companyConfidential.keySet()) {
			if(fileName.contains(key)){
				terms=key;break;
			}

		}	
		return terms;
	}

	public String getCustomTagsCompanyConfidential(String fileName) {
		Map<String,String> companyConfidential = getCompanyConfidentialValues();
		String terms=null;		

		for (String key : companyConfidential.keySet()) {
			if(fileName.contains(key)){
				terms=companyConfidential.get(key).toLowerCase();break;
			}

		}	
		return terms;
	}


	public void createDictionary(Client restClient, TestSuiteDTO suiteData, String dictName, String dictDescription, 
			Map<String,String> fileDetails, List<String> keywords, List<NameValuePair> headers) throws Exception{

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary("----WebKitFormBoundaryCLQHpDNmcjWZz7aR");
		builder.setCharset(Charset.forName("UTF-8"));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		try{
			if(fileDetails.size()>0){
				String fileName = fileDetails.get("fileName");
				String filePath = fileDetails.get("filePath");
				File fileUpload = new java.io.File(filePath);
				FileBody fileBody = new FileBody(fileUpload, org.apache.http.entity.ContentType.create("text/plain"), fileName);
				builder.addPart("file", fileBody);
			}else{
				builder.addTextBody("file", "");
			}
		}catch(NullPointerException nux){
			builder.addTextBody("file", "");
		}

		builder.addTextBody("name", dictName, org.apache.http.entity.ContentType.create("text/plain", Charset.forName("UTF-8")))
		.addTextBody("description", dictDescription)
		.addTextBody("operator", "AND");

		String operands="[";
		for(int i=0;i<keywords.size();i++){
			operands +="{\"desc\":\"\",\"expr\":\""+keywords.get(i)+"\",\"min_count\":1,\"weight\":1,\"is_not\":false}";
			if(keywords.size()>1&&i<(keywords.size()-1)){
				operands +=",";
			}
		}
		operands+="]";

		builder.addTextBody("operands", operands)
		.addTextBody("threshold", "1")
		.addTextBody("dictionary_type", "Custom");

		HttpEntity multipart = builder.build();

		//This is to print the entire entity
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
				(int) multipart.getContentLength());
		multipart.writeTo(out);
		String entityContentAsString = new String(out.toByteArray());
		Logger.info("multipartEntity:" + entityContentAsString);


		String path = "/controls/add_dictionary";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getReferer().replace("https://", ""), path);
		Logger.info("URI ::"+dataUri.toString());		
		HttpResponse response = restClient.doPost(dataUri, headers, null, multipart);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Response body: "+responseBody);
	}
	
	public void updateDictionary(Client restClient, TestSuiteDTO suiteData, String dictId, String dictName, String dictDescription, 
			Map<String,String> fileDetails, List<String> keywords, List<NameValuePair> headers) throws Exception{

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setBoundary("----WebKitFormBoundaryCLQHpDNmcjWZz7aR");
		builder.setCharset(Charset.forName("UTF-8"));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		try{
			if(fileDetails.size()>0){
				String fileName = fileDetails.get("fileName");
				String filePath = fileDetails.get("filePath");
				File fileUpload = new java.io.File(filePath);
				FileBody fileBody = new FileBody(fileUpload, org.apache.http.entity.ContentType.create("text/plain"), fileName);
				builder.addPart("file", fileBody);
			}else{
				builder.addTextBody("file", "");
			}
		}catch(NullPointerException nux){
			builder.addTextBody("file", "");
		}

		builder.addTextBody("name", dictName, org.apache.http.entity.ContentType.create("text/plain", Charset.forName("UTF-8")))
		.addTextBody("description", dictDescription)
		.addTextBody("id", dictId)
		.addTextBody("operator", "AND");

		String operands="[";
		for(int i=0;i<keywords.size();i++){
			operands +="{\"desc\":\"\",\"expr\":\""+keywords.get(i)+"\",\"min_count\":1,\"weight\":1,\"is_not\":false}";
			if(keywords.size()>1&&i<(keywords.size()-1)){
				operands +=",";
			}
		}
		operands+="]";

		builder.addTextBody("operands", operands)
		.addTextBody("threshold", "1")
		.addTextBody("dictionary_type", "Custom");

		HttpEntity multipart = builder.build();

		//This is to print the entire entity
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
				(int) multipart.getContentLength());
		multipart.writeTo(out);
		String entityContentAsString = new String(out.toByteArray());
		Logger.info("multipartEntity:" + entityContentAsString);


		String path = "/controls/add_dictionary";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getReferer().replace("https://", ""), path);
		Logger.info("URI ::"+dataUri.toString());		
		HttpResponse response = restClient.doPost(dataUri, headers, null, multipart);
		String responseBody = ClientUtil.getResponseBody(response);
		Logger.info("Response body: "+responseBody);
	}

	public HttpResponse deleteDictionary(Client restClient, TestSuiteDTO suiteData, String dictId) throws Exception {
		HttpResponse delResponse =deleteDictionaryById(restClient, getCookieHeaders(suiteData), dictId, suiteData.getScheme(), suiteData.getHost());
		return delResponse;	
	}

	
	public HttpResponse deleteDictionaryById(Client restClient, List<NameValuePair> requestHeader, 
			String dictId, String scheme, String host) throws Exception {
		Logger.info("Deleting of dictionary:"+dictId+" in progress");

		HttpResponse delResponse = null;
		String restAPI = "/controls/remove_dictionary";
		StringEntity entity = new StringEntity(
				"{\"id\":\""+dictId+"\"}");

		try{	

			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(delResponse.getStatusLine().getStatusCode()==200){}else{
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			try{
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}

		Logger.info("Deleting of dictionary:"+dictId+" is completed");

		return delResponse;	
	}

	public List<NameValuePair> getBrowserHeaders(TestSuiteDTO suiteData){
		String csrfToken="";String sessionId="";
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		WebDriver driver = getHtmlUnitDriver(suiteData);


		try{

			Set<Cookie> cookies = driver.manage().getCookies();
			for(Cookie c:cookies){
				if(c.getName().equalsIgnoreCase("csrfToken")){
					csrfToken=c.getValue();
				}
				if(c.getName().equalsIgnoreCase("sessionId")){
					sessionId=c.getValue();
				}
				/*if(c.getName().equalsIgnoreCase("__rid__")){
				String rid=c.getValue();
			}*/
			}

			if(csrfToken.isEmpty()||csrfToken.equals(null)){
				csrfToken = suiteData.getCSRFToken();
			}if(sessionId.isEmpty()||sessionId.equals(null)){
				sessionId = suiteData.getSessionID();
			}

			String cookie = cookies.toString().replace("[", "").replace("]", "");
			if(cookie.contains("csrf")&&cookie.contains("session")){}
			else{
				cookie = "csrftoken="+csrfToken+";sessionid="+sessionId+";"+cookies.toString().replace("[", "").replace("]", "");
			}


			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "*/*"));
			headers.add(new BasicNameValuePair("Content-Type", MediaType.MULTIPART_FORM_DATA + "; boundary=----WebKitFormBoundaryCLQHpDNmcjWZz7aR"));
			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
			headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()+"/static/ng/appControls/index.html"));
			headers.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
			headers.add(new BasicNameValuePair("X-Session", sessionId));
			headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
			headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
			headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36"));
			headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, cookie));
			//headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, getAuthParam(suiteData.getUsername(), suiteData.getPassword())));

			//Logger.info(headers.toString());

		}finally{
			driver.quit();
		}

		return headers;
	}

	public List<NameValuePair> getMultiHeaders(TestSuiteDTO suiteData){
		String csrfToken="";String sessionId="";String rid="";
		String mf_authenticated="";
		String el_auth_param="RU5DOkjgwB11lANZJq9CWSRhZ33PzPRVf%2Bm9u9UgUEhLvSMuI20BHahd7CdAwPUgqLbldKRmDNpPIZHj7OXSClk4VK6LS7YcUX/Uy%2BVWYda0WpexuAktHZTIMMTqFffvmI7WgA%3D%3D";
		String x_elastica_gw="v2.60.0";
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		WebDriver driver = getHtmlUnitDriver(suiteData);


		try{

			Set<Cookie> cookies = driver.manage().getCookies();
			for(Cookie c:cookies){
				if(c.getName().equalsIgnoreCase("csrfToken")){
					csrfToken=c.getValue();
				}
				if(c.getName().equalsIgnoreCase("sessionId")){
					sessionId=c.getValue();
				}
				if(c.getName().equalsIgnoreCase("__rid__")){
					rid=c.getValue();
				}
			}

			if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe"))
				mf_authenticated = FrameworkConstants.EOE_COOKIE;
			else if (suiteData.getEnvironmentName().equalsIgnoreCase("friends"))
				mf_authenticated = FrameworkConstants.OTHERS_COOKIE;
			else if (suiteData.getEnvironmentName().contains("qa"))
				mf_authenticated = FrameworkConstants.QAVPC_COOKIE;

			if(csrfToken.isEmpty()||csrfToken.equals(null)){
				csrfToken = suiteData.getCSRFToken();
			}if(sessionId.isEmpty()||sessionId.equals(null)){
				sessionId = suiteData.getSessionID();
			}

			String cookie = "csrftoken="+csrfToken+"; mf_authenticated="+mf_authenticated+
					"; __rid__="+rid+"; sessionid="+sessionId;


			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
			headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.5"));
			headers.add(new BasicNameValuePair("Connection", "keep-alive"));
			headers.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=---------------------------10172886071452304082575574372"));
			headers.add(new BasicNameValuePair(HttpHeaders.HOST, suiteData.getApiserverHostName()));
			headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()+"/static/ng/appControls/index.html"));
			headers.add(new BasicNameValuePair("Origin", suiteData.getReferer()));
			headers.add(new BasicNameValuePair("X-CSRFToken", csrfToken+", "+csrfToken));
			headers.add(new BasicNameValuePair("X-Session", sessionId));
			headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
			headers.add(new BasicNameValuePair("X-User", suiteData.getUsername().toLowerCase()));
			headers.add(new BasicNameValuePair("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:46.0) Gecko/20100101 Firefox/46.0"));
			headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, cookie));
			headers.add(new BasicNameValuePair("el_auth_param", el_auth_param));
			headers.add(new BasicNameValuePair("x-elastica_gw", x_elastica_gw));
			headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
					AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			driver.quit();
		}

		return headers;
	}

	private WebDriver getHtmlUnitDriver(TestSuiteDTO suiteData){
		DesiredCapabilities capability= DesiredCapabilities.firefox();
		capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);
		//WebDriver driver = new FirefoxDriver();

		driver.get(suiteData.getReferer()+"/static/ng/appLogin/index.html#/?redirect=false");
		try {Thread.sleep(10000);} catch (InterruptedException e) {}

		if (suiteData.getEnvironmentName().equalsIgnoreCase("eoe"))
			driver.manage().addCookie(new Cookie("mf_authenticated",FrameworkConstants.EOE_COOKIE));
		else if (suiteData.getEnvironmentName().equalsIgnoreCase("friends"))
			driver.manage().addCookie(new Cookie("mf_authenticated",FrameworkConstants.OTHERS_COOKIE));
		else if (suiteData.getEnvironmentName().contains("qa"))
			driver.manage().addCookie(new Cookie("mf_authenticated",FrameworkConstants.QAVPC_COOKIE));


		try {Thread.sleep(10000);} catch (InterruptedException e) {}
		driver.navigate().refresh();
		try {Thread.sleep(15000);} catch (InterruptedException e) {}

		WebElement login = driver.findElement(By.name("email"));
		login.sendKeys(suiteData.getUsername());
		WebElement pwd = driver.findElement(By.name("password"));
		pwd.sendKeys(suiteData.getPassword());
		WebElement loginbutton = driver.findElement(By.cssSelector(".btn.btn-default.btn-lg.btn-block.ng-scope"));
		loginbutton.click();

		try {Thread.sleep(10000);} catch (InterruptedException e) {}
		return driver;
	}

	public HttpResponse listTrainingProfiles(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host) throws Exception{
		String restAPI = "/controls/make_api_request";
		HttpResponse listContentIQResponse =null;
		StringEntity entity = new StringEntity(
				"{\"url\":\"mlprofiles\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":0}}");	
		try{	
			URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
			listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			waitForSeconds(10);
			try{

				URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);	
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}
		
		if(StringUtils.isBlank(listContentIQResponse.toString())){
			try{	
				URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e){
				waitForSeconds(10);
				try{

					URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);	
					listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
						listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
					}
				}catch(Exception e1){}
			}
		}
		
		return listContentIQResponse;
	}

	public HttpResponse listDictionary(Client restClient, List<NameValuePair> requestHeader, 
			String scheme, String host) throws Exception{
		String restAPI = "/controls/get_dictionary_list";
		HttpResponse listContentIQResponse =null;
		StringEntity entity = new StringEntity("{}");	

		try{	
			URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);
			listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			try{
				URI dataUri = ClientUtil.BuidURI(scheme, host,restAPI);	
				listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(listContentIQResponse.getStatusLine().getStatusCode()==200){}else{
					listContentIQResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}


		return listContentIQResponse;	
	}

	public String getDictionaryId(HttpResponse response, String name) {
		String id="";

		String dictResponse = ClientUtil.getResponseBody(response);
		String objects = getJSONValue(dictResponse, "objects");
		JSONArray dictArray = (JSONArray) new JSONTokener(objects).nextValue();
		for (int i = 0; i < dictArray.size(); i++) {
			String dictName= getJSONValue(dictArray.getJSONObject(i).toString(), "name");
			String dictId= getJSONValue(dictArray.getJSONObject(i).toString(), "id");

			if(dictName.contains(name)){
				id=dictId.replace("\"", "");
				break;
			}

		}
		Logger.info("Dictionary id: "+ id);
		return id;	
	}

	public void deleteAllCIQProfiles(Client restClient, TestSuiteDTO suiteData) {
		try {

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			deleteAllContentIQProfiles(restClient, listContentIQResponse, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Deleting of all ContentIQ Profiles" + ex.getLocalizedMessage());
		}
	}

	public void deleteAllDictionaries(Client restClient, TestSuiteDTO suiteData) {
		try {

			HttpResponse listResponse = listDictionary(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			deleteAllDictionaries(restClient, listResponse, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Deleting of all ContentIQ Profiles" + ex.getLocalizedMessage());
		}
	}

	public void deleteAllTrainingProfiles(Client restClient, TestSuiteDTO suiteData) {
		try {

			HttpResponse listResponse = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			deleteAllTrainingProfiles(restClient, suiteData, listResponse, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Deleting of all ContentIQ Profiles" + ex.getLocalizedMessage());
		}
	}

	public void deleteAllTrainingProfiles(Client restClient, TestSuiteDTO suiteData, HttpResponse response, List<NameValuePair> requestHeader, 
			String scheme, String host ) throws Exception{
		Logger.info("Deletion of all training profiles in progress");

		String tpResponse = ClientUtil.getResponseBody(response);
		tpResponse = getJSONValue(tpResponse, "data");
		String trainingProfiles = getJSONValue(tpResponse, "objects");
		JSONArray tpArray = (JSONArray) new JSONTokener(trainingProfiles).nextValue();
		for (int i = 0; i < tpArray.size(); i++) {
			String tpId= getJSONValue(tpArray.getJSONObject(i).toString(), "id").replace("\"", "");
			String tpName= getJSONValue(tpArray.getJSONObject(i).toString(), "name").replace("\"", "");

			activateDeactivateMLProfile(suiteData, restClient, tpName, false);
			deleteTrainingProfileById(restClient, requestHeader, tpId, scheme, host);
		}

		Logger.info("Deletion of all training profiles is completed");
	}

	public HttpResponse deleteTrainingProfileById(Client restClient, List<NameValuePair> requestHeader, 
			String id, String scheme, String host ) throws Exception {
		Logger.info("Deleting of training profile:"+id+" in progress");

		HttpResponse delResponse = null;
		String restAPI = "/controls/make_api_request";
		StringEntity entity = new StringEntity(
				"{\"url\":\"mlprofiles\",\"id\":\""+id+"\",\"action\":\"delete\",\"params\":null,\"data\":null}");

		try{	

			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(delResponse.getStatusLine().getStatusCode()==200){}else{
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			try{
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}

		Logger.info("Deleting of training profile:"+id+" is completed");

		return delResponse;	
	}


	public void deleteAllDictionaries(Client restClient, HttpResponse response, List<NameValuePair> requestHeader, 
			String scheme, String host ) throws Exception{
		Logger.info("Deletion of all dictionaries in progress");

		String dictResponse = ClientUtil.getResponseBody(response);
		String dictionaries = getJSONValue(dictResponse, "objects");
		JSONArray dictArray = (JSONArray) new JSONTokener(dictionaries).nextValue();
		for (int i = 0; i < dictArray.size(); i++) {
			String dictId= getJSONValue(dictArray.getJSONObject(i).toString(), "id").replace("\"", "");
			String dictType= getJSONValue(dictArray.getJSONObject(i).toString(), "dictionary_type").replace("\"", "");
			if(dictType.equalsIgnoreCase("Custom")){
				deleteDictionaryById(restClient, requestHeader, dictId, scheme, host);
			}
		}

		Logger.info("Deletion of all dictionaries is completed");
	}


	public boolean arrayComparison(String[] arrayA,String[] arrayB){
		return Arrays.asList(arrayA).containsAll(Arrays.asList(arrayB));
	}


	public String numberComparison(Map<String,Integer> numberList){
		String validationMessage="";

		List<Integer> numbers = Arrays.asList(numberList.get("ExpAPICount"),numberList.get("ActAPICount"),
				numberList.get("ExpUICount"),numberList.get("ActUICount"));

		if(new HashSet<>(numbers).size() == 1){
			Logger.info("No mismatch is seen between UI and API counts");
		}else{
			validationMessage="Mismatch seen in numbers between: Actual API Count:"+numberList.get("ActAPICount")+" "
					+ "and Expected API Count:"+numberList.get("ExpAPICount")+" & "
					+ "Actual UI Count:"+numberList.get("ActUICount")+" and Expected UI Count:"+numberList.get("ExpUICount");
		}

		return validationMessage;

	}

	public String numberComparison(List<Integer> numbers){
		String validationMessage="";

		System.out.println(new HashSet<>(numbers).size());

		if(new HashSet<>(numbers).size() == 1){
			Logger.info("Mismatch not seen between "+Arrays.asList(numbers));
		}else{
			validationMessage="Mismatch seen between "+Arrays.asList(numbers);
		}

		return validationMessage;

	}


	public String getResourceId(Client restClient, ElasticSearchLogs esLogs,
			TestSuiteDTO suiteData, String fileName) throws Exception {
		String resourceId = "";

		List<NameValuePair> headers = getBasicHeaders(suiteData);
		String payload = 
				getSearchQueryForFileName(getMinusMinutesFromCurrentTime(1440) , getPlusMinutesFromCurrentTime(120), 
						SaasType.getSaasFilterType(suiteData.getSaasApp()), "API", fileName, "investigate", suiteData);
		Logger.info("****************Input Payload****************");
		Logger.info(payload);
		Logger.info("*********************************************");

		HttpResponse response = esLogs.getDisplayLogs(restClient, headers, suiteData.getApiserverHostName(), new StringEntity(payload));
		String responseBody = ClientUtil.getResponseBody(response);
		String hits = getJSONValue(getJSONValue(responseBody, "hits"), "hits");

		Logger.info("****************Output Response****************");
		Logger.info(hits);
		Logger.info("***********************************************");


		JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();
		if(jArray.size()==0){
			Assert.fail("No logs are not generated even after 30 minutes for file upload of " + fileName);
		}else{
			for (int i = 0; i < jArray.size();) {
				String sourceJson = getJSONValue(jArray.getJSONObject(i).toString(), "_source");
				resourceId = getJSONValue(sourceJson, "Resource_Id").replaceAll("\"", "");
				break;
			}
		}

		return resourceId;
	}	


	public Map<String, String> getFileClassTypesFromPropertiesFile() {
		Map<String, String> fileClassTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/fileClassType.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				fileClassTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileClassTypes;
	}


	public Map<String, String> getFileFormatTypesFromPropertiesFile() {
		Map<String, String> fileFormatTypes = new HashMap<String, String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/fileFormatType.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);

			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				fileFormatTypes.put(key, value);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileFormatTypes;
	}


	public String verifyFileInfoSplunk(String splunkJson, String fileName){
		String message = "";

		DCISplunkResults splunkResults = unmarshall(splunkJson, DCISplunkResults.class);

		String expectedFileContentType = mimeTypesForAFile(fileName);
		String expectedFileClass = fileClassTypesForAFile(fileName);
		String expectedFileFormat = fileFormatTypesForAFile(fileName);

		String actualFileContentType = splunkResults.getContentChecks().getMetadata().getContentType()
				.toString().replace("[", "").replace("]", "");
		String actualFileClass = "";
		String actualFileFormat = "";
		try {actualFileClass = splunkResults.getContentChecks().getMetadata().getFileClass();
		if(actualFileClass.equalsIgnoreCase("null")){
			actualFileClass = "No value to be verified";expectedFileClass = "No value to be verified";
		}
		} catch (Exception e) 
		{actualFileClass = "No value to be verified";expectedFileClass = "No value to be verified";}
		try {actualFileFormat = splunkResults.getContentChecks().getMetadata().getFileFormat();
		if(actualFileFormat.equalsIgnoreCase("null")){
			actualFileFormat = "No value to be verified";expectedFileFormat = "No value to be verified";
		}
		} catch (Exception e) 
		{actualFileFormat = "No value to be verified";expectedFileFormat = "No value to be verified";}

		Logger.info("Splunk Json: "+splunkJson);
		Logger.info("################Expected Values################");
		Logger.info("Expected File Content Type: "+expectedFileContentType);
		Logger.info("Expected File Class: "+expectedFileClass);
		Logger.info("Expected File Format: "+expectedFileFormat);
		Logger.info("###############################################");

		Logger.info("################Actual Values################");
		Logger.info("Actual File Content Type: "+actualFileContentType);
		Logger.info("Actual File Class: "+actualFileClass);
		Logger.info("Actual File Format: "+actualFileFormat);
		Logger.info("###############################################");


		message += (actualFileContentType.equalsIgnoreCase(expectedFileContentType)) 
				? "" : "Expected file content type "+expectedFileContentType+" but was " + actualFileContentType+"\n";
		message += (actualFileClass.equalsIgnoreCase(expectedFileClass)) 
				? "" : "Expected file class "+expectedFileClass+" but was " + actualFileClass+"\n";
		message += (actualFileFormat.equalsIgnoreCase(expectedFileFormat)) 
				? "" : "Expected file format "+expectedFileFormat+" but was " + actualFileFormat+"\n";


		return message;
	}

	public String piiRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getPIIRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String pciRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getPCIRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String hipaaRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getHIPAARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}


	public String glbaRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getGLBARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String vbaRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getVBARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String virusRisksForAFile(String fileName) {
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getVirusRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String mimeTypesForAFile(String fileName) {
		String mimeType = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> mimeTypes = getMimeTypesFromPropertiesFile();
			mimeType=mimeTypes.get(fileName);
		} catch (Exception ex) {

		}
		return mimeType;
	}

	public String fileClassTypesForAFile(String fileName) {
		String fileClassType = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> fileClassTypes = getFileClassTypesFromPropertiesFile();

			for (Map.Entry<String, String> f : fileClassTypes.entrySet()) {
				String key = f.getKey();
				String value = f.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					fileClassType = value;
				}
			}
		} catch (Exception ex) {

		}
		return fileClassType;
	}

	public String fileFormatTypesForAFile(String fileName) {
		String fileFormatType = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> fileFormatTypes = getFileFormatTypesFromPropertiesFile();

			for (Map.Entry<String, String> f : fileFormatTypes.entrySet()) {
				String key = f.getKey();
				String value = f.getValue();
				if (fileName.equalsIgnoreCase(key)) {
					fileFormatType = value;
				}
			}
		} catch (Exception ex) {

		}
		return fileFormatType;
	}

	public String[] riskTypesForAFile(String fileName) {
		String[] riskArray = null;
		String risks = null;
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> riskTypes = getRiskTypesFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.equals(key)) {
					risks = value;break;
				}
			}

			if (risks != null) {
				riskArray = risks.split(",");
			}

		} catch (Exception ex) {

		}
		return riskArray;
	}

	public String riskContentTypesForAFile(String[] riskArray) {
		System.out.println(Arrays.toString(riskArray));
		return Arrays.toString(riskArray).
				replaceAll("\\[", "").
				replaceAll("\\]", "").
				replaceAll(", ", ",");
	}


	public String[] docClassTypesForAFile(String fileName) {
		String[] docClassArray = null;
		String docClass = "";
		fileName = fileName.replaceAll("^(.*?)_", "");
		try {
			Map<String, String> docClassTypes = getDocClassTypesFromPropertiesFile();

			for (Map.Entry<String, String> d : docClassTypes.entrySet()) {
				String key = d.getKey();
				String value = d.getValue();
				if (fileName.equals(key)) {
					docClass = value;break;
				}
			}
			if (docClass.equalsIgnoreCase("")) {
				docClassArray = new String[0];
			}else{
				docClassArray = docClass.split(",");
			}

		} catch (Exception ex) {
			docClassArray = new String[0];
		}
		return docClassArray;
	}

	public String getDropboxInternalFolderName(TestSuiteDTO suiteData){
		if(suiteData.getDomainName().equalsIgnoreCase("securletautofeatle.com")){
			return "securletautofe Team Folder";
		}else if(suiteData.getDomainName().equalsIgnoreCase("securletfeatle.com")){
			return "securletfeatle Team Folder";
		}else{
			return "Elastica QA Team Folder";
		}
	}

	public Map<String,String> getCIQRisksContentValues() {
		Map<String,String> ciqRisksContent = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqRiskContent.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqRisksContent.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqRisksContent;
	}

	public Map<String,String> getCIQFileFormatValues() {
		Map<String,String> ciqRisksContent = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/fileFormat.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = (String) prop.getProperty(key);
				ciqRisksContent.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqRisksContent;
	}

	public Map<String,String> getCIQFileClassValues() {
		Map<String,String> ciqRisksContent = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/fileClass.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}
			Reader reader = new InputStreamReader(input, "UTF-8");
			prop.load(reader);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = (String) prop.getProperty(key);
				ciqRisksContent.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqRisksContent;
	}

	public String[] getCIQRiskContentProfileName(String[] fileNames) {
		Map<String,String> ciqRiskContentValues = getCIQRisksContentValues();
		String terms[] = new String[fileNames.length];		

		for(int i=0;i<fileNames.length;i++){
			for (String key : ciqRiskContentValues.keySet()) {
				if(fileNames[i].equalsIgnoreCase(key)){
					terms[i]=ciqRiskContentValues.get(key);break;
				}
			}

		}	
		return terms;
	}

	public String[] getCIQFileFormatValue() {
		Map<String,String> ciqValues = getCIQFileFormatValues();
		Set<String> ciqValue = new HashSet<String>();
		for(Entry<String,String> ciq:ciqValues.entrySet()){
			ciqValue.add((String) ciq.getValue());
		}

		return ciqValue.toArray(new String[ciqValue.size()]);
	}

	public String[] getCIQFileClassValue() {
		Map<String,String> ciqValues = getCIQFileClassValues();
		Set<String> ciqValue = new HashSet<String>();
		for(Entry<String,String> ciq:ciqValues.entrySet()){
			ciqValue.add((String) ciq.getValue());
		}

		return ciqValue.toArray(new String[ciqValue.size()]);
	}


	public String[] getCustomCIQTitle(String[] pdts, String pdd) {
		String[] ciqTitle = new String[pdts.length];
		for(int i=0;i<pdts.length;i++){
			ciqTitle[i] = pdts[i].replaceAll(" ", "_").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\/", "")
					+"_"+pdd.replaceAll(" ", "_").replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\/", "")+"_Title";
		}
		return ciqTitle;
	}



	public String[] getCustomCIQDescription(String[] pdts, String pdd) {
		String[] ciqDescription = new String[pdts.length];
		for(int i=0;i<pdts.length;i++){
			ciqDescription[i] = pdts[i]+" "+pdd+" Description";
		}
		return ciqDescription;
	}



	public String[] predefinedTermsCIQProfile(String[] fileNames) {
		String[] terms = new String[fileNames.length];

		for(int i=0;i<fileNames.length;i++){
			List<String> values = getCIQValuesTerms();
			for(String value:values){
				String fileName = value.split(":")[1].replace(".txt", "");
				String ciqTerm = value.split(":")[0];
				if(fileNames[i].contains(fileName)){
					terms[i]=ciqTerm;break;
				}
			}

		}

		return terms;
	}

	public String[] predefinedTermsCIQProfileName(String[] fileNames) {
		String[] terms = new String[fileNames.length];

		for(int i=0;i<fileNames.length;i++){
			Map<String,String> keyValueMap = getCIQKeyValueTerms();

			for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String fileName = value.split(":")[1].replace(".txt", "");
				if(fileNames[i].contains(fileName)){
					terms[i]=key;break;
				}
			}
		}

		return terms;
	}

	public boolean checkKeyExistsInMap(Map<String,String> map, String key){
		return map.containsKey(key);
	}


	public String validateDCIRiskContentLogs(TestSuiteDTO suiteData, String hitsJson, 
			Map<String,String> CIJson){
		String validationMessage = "";

		int riskLogCount= Integer.parseInt(CIJson.get("LogCount"));
		String fileName = CIJson.get("FileName");

		Hits hits = unmarshall(hitsJson, Hits.class);
		List<Hit> hit = hits.getHits();

		if(hit.size()==0){
			//getSplunkResults(suiteData, fileName);
			Assert.assertTrue(hit.size() == riskLogCount, "Risk logs are not generated even after 15 minutes for file upload of " + fileName);
		}else{
			Assert.assertTrue(hit.size() == riskLogCount, "Multiple risk logs are generated for file upload of " + fileName
					+"Expected: "+riskLogCount+" but was "+hit.size());
		}

		validationMessage +=(hits.getTotal()==riskLogCount) ? "" : 
			"Hits: Total field count mismatch Expected:"+riskLogCount+" and Actual:"+hits.getTotal()+"\n";
		validationMessage +=(hits.getMaxScore()>=0.0) ? "" : 
			"Hits: Max Score field mismatch Expected:some float value >=0 and Actual:"+hits.getMaxScore()+"\n";

		validationMessage +=(hit.size()==riskLogCount) ? "" : 
			"Hits: Array Json mismatch Expected:"+riskLogCount+" and Actual:"+hit.size()+"\n";

		for(Hit h:hit){
			String id = h.getId();
			String index = h.getIndex();
			String score = h.getScore();
			String type = h.getType();
			List<Long> sort = h.getSort();
			Source source = h.getSource();
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

			validationMessage +=(StringUtils.isNotBlank(id)) ? "" : 
				"Hits: Id field mismatch Expected:some string value and Actual:"+id+"\n";
			validationMessage +=(StringUtils.isBlank(score)) ? "" : 
				"Hits: Score field mismatch Expected:null value and Actual:"+score+"\n";
			validationMessage +=(StringUtils.isNotBlank(type)) ? "" : 
				"Hits: Type field mismatch Expected:some string value and Actual:"+type+"\n";
			validationMessage +=(sort.size()>0) ? "" : 
				"Hits: Sort field mismatch Expected:some values and Actual:"+id+"\n";
			validationMessage +=(index.contains("logs_"+suiteData.getTenantName()+"-"+year)) ? "" : 
				"Hits: Index field mismatch Expected:"+"logs_"+suiteData.getTenantName()+"-"+year+
				" and Actual:"+index+"\n";

			String expectedFacility = SaasType.getSaasFilterType(suiteData.getSaasApp());

			String createdTimestamp = source.getCreatedTimestamp();
			String insertedTimestamp = source.getInsertedTimestamp();
			String resourceId = source.getResourceId();
			String activityType = source.getActivityType();
			String facility = source.getFacility();
			ContentChecks contentChecks = source.getContentChecks();

			validationMessage +=(StringUtils.isNotBlank(insertedTimestamp)) ? "" : 
				"Source: Inserted timestamp field mismatch Expected:some string value and Actual:"+insertedTimestamp+"\n";
			validationMessage +=(StringUtils.isNotBlank(createdTimestamp)) ? "" : 
				"Source: Created timestamp field mismatch Expected:some string value and Actual:"+createdTimestamp+"\n";
			validationMessage +=(StringUtils.isNotBlank(resourceId)) ? "" : 
				"Source: Resource Id field mismatch Expected:some string value and Actual:"+resourceId+"\n";

			validationMessage +=(facility.equalsIgnoreCase(expectedFacility)) ? "" : 
				"Source: Facility field mismatch Expected:"+expectedFacility+" and Actual:"+facility+"\n";

			String expectedMessage = getMessage(source,CIJson);
			String expectedName = getFileName(source,CIJson);
			String expectedSeverity = CIJson.get("Severity");
			String expectedSource = CIJson.get("Source");
			String expectedUser = CIJson.get("User");

			String actualMessage = getMessage(source);
			String actualName = source.getName();
			String actualSeverity = source.getSeverity();
			String actualSource = source.getSource();
			String actualUser = source.getUser();

			validationMessage +=(actualMessage.equalsIgnoreCase(expectedMessage)) ? "" : 
				"Source: Message field mismatch Expected:"+expectedMessage+" and Actual:"+actualMessage+"\n";
			validationMessage +=(actualName.equalsIgnoreCase(expectedName)) ? "" : 
				"Source: Name field mismatch Expected:"+expectedName+" and Actual:"+actualName+"\n";
			validationMessage +=(actualSeverity.equalsIgnoreCase(expectedSeverity)) ? "" : 
				"Source: Severity field mismatch Expected:"+expectedSeverity+" and Actual:"+actualSeverity+"\n";

			validationMessage +=(actualUser.contains(expectedUser)) ? "" : 
				"Source: User field mismatch Expected:"+expectedUser+" and Actual:"+actualUser+"\n";

			if(actualSeverity.equalsIgnoreCase(DCIConstants.CICriticalSeverityType)){
				validationMessage +=(actualSource.equalsIgnoreCase(expectedSource)) ? "" : 
					"Source: Source field mismatch Expected:"+expectedSource+" and Actual:"+actualSource+"\n";
				validationMessage +=(activityType.equalsIgnoreCase(DCIConstants.CIActivityType)) ? "" : 
					"Source: Activity type field mismatch Expected:"+DCIConstants.CIActivityType+" and Actual:"+activityType+"\n";

				String[] aRisks = source.getRisks().replace(", ", ",").split(",");Arrays.sort(aRisks);
				String[] eRisks = CIJson.get("Risks").split(",");Arrays.sort(eRisks);
				String expectedRisks = Arrays.toString(eRisks).replace("[", "").replace("]", "");
				String actualRisks = Arrays.toString(aRisks).replace("[", "").replace("]", "");

				validationMessage +=(this.compareEqualList(Arrays.asList(expectedRisks), Arrays.asList(actualRisks))) ? "" : 
					"Source: Risks field mismatch Expected:"+Arrays.asList(expectedRisks)+" and Actual:"+ Arrays.asList(actualRisks)+"\n";

				validationMessage += verifyRiskLogs(suiteData, source, CIJson);
			}
			if(actualSeverity.equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
				validationMessage +=(actualSource.equalsIgnoreCase(expectedSource)) ? "" : 
					"Source: Source field mismatch Expected:"+expectedSource+" and Actual:"+actualSource+"\n";
				validationMessage +=(activityType.equalsIgnoreCase(DCIConstants.CIActivityType)) ? "" : 
					"Source: Activity type field mismatch Expected:"+DCIConstants.CIActivityType+" and Actual:"+activityType+"\n";

				List<String> actualContent = contentChecks.getDocClass();
				List<String> expectedContent = Arrays.asList(CIJson.get("Risks").split(","));

				validationMessage +=(compareEqualList(actualContent, expectedContent)) ? "" : 
					"Source: Content field mismatch Expected:"+expectedContent+" and Actual:"+actualContent+"\n";

				validationMessage += verifyContentLogs(suiteData, source, CIJson);
			}
			if(actualSeverity.equalsIgnoreCase(DCIConstants.CIWarningSeverityType)){
				validationMessage +=(activityType.equalsIgnoreCase(DCIConstants.CIBypassActivityType)) ? "" : 
					"Source: Activity type field mismatch Expected:"+DCIConstants.CIBypassActivityType+" and Actual:"+activityType+"\n";
			}
		}

		return validationMessage;
	}


	private String getMessage(Source source, Map<String,String> CIJson) {
		String expectedMessage="";

		String message = source.getMessage();
		String fileName = CIJson.get("FileName");
		String severity = CIJson.get("Severity");



		if(severity.equalsIgnoreCase(DCIConstants.CICriticalSeverityType)){
			String[] actualRisks = source.getRisks().replace(", ", ",").split(",");Arrays.sort(actualRisks);
			String[] expectedRisks = CIJson.get("Risks").split(",");Arrays.sort(expectedRisks);
			String risks = Arrays.toString(expectedRisks).replace("[", "").replace("]", "");

			if(message.contains("Email file attachment")){
				expectedMessage = "Email file attachment " + fileName + " has risk(s) - "+risks;
			}else if(message.contains("Email message")&&message.contains("Mail With Body")){
				expectedMessage = "Email message " + fileName + " Mail With Body has risk(s) - "+risks;
			}else if(message.contains("Email message")&&message.contains("Mail With Attachment and Body")){
				expectedMessage = "Email message " + fileName + " Mail With Attachment and Body has risk(s) - "+risks;
			}else if(message.contains("Mail With Body")){
				expectedMessage = "File " + fileName + " Mail With Body has risk(s) - "+risks;
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = "File " + fileName + " Mail With Attachment and Body has risk(s) - "+risks;
			}else{
				expectedMessage = "File " + fileName + " has risk(s) - "+risks; 
			}
		}else if(severity.equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
			String aRisks = source.getMessage().replaceAll(".* has been classified - ", "");
			String[] actualRisks = aRisks.replace(", ", ",").split(",");Arrays.sort(actualRisks);
			String[] expectedRisks = CIJson.get("Risks").split(",");Arrays.sort(expectedRisks);
			String risks = Arrays.toString(expectedRisks).replace("[", "").replace("]", "");

			if(message.contains("Mail With Body")){
				expectedMessage = "File " + fileName + " Mail With Body has been classified - "+risks;
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = "File " + fileName + " Mail With Attachment and Body has been classified - "+risks;
			}else{
				expectedMessage = "File " + fileName + " has been classified - "+risks;
			}
		}else if(severity.equalsIgnoreCase(DCIConstants.CIWarningSeverityType)){
			expectedMessage = "File: \""+fileName+"\" not downloaded for content inspection due to size limit: 100MB";
		}

		return expectedMessage;
	}

	private String getMessage(Source source) {
		String expectedMessage="";

		String message = source.getMessage();


		if(source.getSeverity().equalsIgnoreCase(DCIConstants.CICriticalSeverityType)){
			String[] actualRisks = source.getRisks().replace(", ", ",").split(",");Arrays.sort(actualRisks);
			String risks = Arrays.toString(actualRisks).replace("[", "").replace("]", "");
			if(message.contains("Email file attachment")){
				expectedMessage = "Email file attachment " + source.getName() + " has risk(s) - "+risks;
			}else if(message.contains("Email message")&&message.contains("Mail With Body")){
				expectedMessage = "Email message " + source.getName() + " has risk(s) - "+risks;
			}else if(message.contains("Email message")&&message.contains("Mail With Attachment and Body")){
				expectedMessage = "Email message " + source.getName() + " has risk(s) - "+risks;
			}else if(message.contains("Mail With Body")){
				expectedMessage = "File " + source.getName() + " has risk(s) - "+risks;
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = "File " + source.getName() + " has risk(s) - "+risks;
			}else{
				expectedMessage = "File " + source.getName() + " has risk(s) - "+risks; 
			}
		}else if(source.getSeverity().equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
			String aRisks = source.getMessage().replaceAll(".* has been classified - ", "");
			String[] actualRisks = aRisks.replace(", ", ",").split(",");Arrays.sort(actualRisks);
			String risks = Arrays.toString(actualRisks).replace("[", "").replace("]", "");

			if(message.contains("Mail With Body")){
				expectedMessage = "File " + source.getName() + " has been classified - "+risks;
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = "File " + source.getName() + " has been classified - "+risks;
			}else{
				expectedMessage = "File " + source.getName() + " has been classified - "+risks;
			}
		}else if(source.getSeverity().equalsIgnoreCase(DCIConstants.CIWarningSeverityType)){
			expectedMessage = "File: \""+source.getName()+"\" not downloaded for content inspection due to size limit: 100MB";
		}

		return expectedMessage;
	}

	private String getFileName(Source source, Map<String,String> CIJson) {
		String expectedMessage="";

		String message = source.getMessage();
		String fileName = CIJson.get("FileName");
		String severity = CIJson.get("Severity");

		if(severity.equalsIgnoreCase(DCIConstants.CICriticalSeverityType)){
			if(message.contains("Email file attachment")){
				expectedMessage = fileName;
			}else if(message.contains("Email message")&&message.contains("Mail With Body")){
				expectedMessage = fileName + " Mail With Body";
			}else if(message.contains("Email message")&&message.contains("Mail Attachment and Body")){
				expectedMessage = fileName + " Mail With Attachment and Body";
			}else if(message.contains("Mail With Body")){
				expectedMessage = fileName + " Mail With Body";
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = fileName + " Mail With Attachment and Body";
			}else{
				expectedMessage = fileName; 
			}
		}else if(severity.equalsIgnoreCase(DCIConstants.CIInformationalSeverityType)){
			if(message.contains("Mail With Body")){
				expectedMessage = fileName + " Mail With Body";
			}else if(message.contains("Mail With Attachment and Body")){
				expectedMessage = fileName + " Mail With Attachment and Body";
			}else{
				expectedMessage = fileName;
			}
		}else if(severity.equalsIgnoreCase(DCIConstants.CIWarningSeverityType)){
			expectedMessage = fileName;
		}

		return expectedMessage;
	}


	private String verifyRiskLogs(TestSuiteDTO suiteData, Source source, Map<String, String> CIJson) {
		String validationMessage = "";

		ContentChecks contentChecks=source.getContentChecks();

		boolean violations = contentChecks.getViolations();
		validationMessage +=(violations) ? "" : "ContentChecks-->violations flag is mismatching Expecting true but was " + violations;

		validationMessage += verifyMimeTypeLogs(suiteData, source, CIJson);
		validationMessage += verifyDocumentClassificationLogs(contentChecks, CIJson);

		int vkDlp = 0; try {vkDlp = contentChecks.getVkDlp();} catch (Exception e) {vkDlp = 0;}
		int vkFerpa = 0; try {vkFerpa = contentChecks.getVkFerpa();} catch (Exception e) {vkFerpa = 0;}
		int vkGlba = 0; try {vkGlba = contentChecks.getVkGlba();} catch (Exception e) {vkGlba = 0;}
		int vkHipaa = 0; try {vkHipaa = contentChecks.getVkHipaa();} catch (Exception e) {vkHipaa = 0;}
		int vkPci = 0; try {vkPci = contentChecks.getVkPci();} catch (Exception e) {vkPci = 0;}
		int vkPii = 0; try {vkPii = contentChecks.getVkPii();} catch (Exception e) {vkPii = 0;}
		int vkVbaMacros = 0; try {vkVbaMacros = contentChecks.getVkVbaMacros();} catch (Exception e) {vkVbaMacros = 0;}
		int vkVirus = 0; try {vkVirus = contentChecks.getVkVirus();} catch (Exception e) {vkVirus = 0;}

		String[] expectedRisks = CIJson.get("Risks").split(","); 


		if (Arrays.asList(expectedRisks).contains("DLP")) {
			validationMessage +=(vkDlp==1) ? "" : "vkDlp is mismatching Expecting 1 but was " + vkDlp+"\n";
		} else {
			validationMessage +=(vkDlp==0) ? "" : "vkDlp is mismatching Expecting 0 but was " + vkDlp+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("FERPA")) {
			validationMessage +=(vkFerpa==1) ? "" : "vkFerpa is mismatching Expecting 1 but was " + vkFerpa+"\n";
		} else {
			validationMessage +=(vkFerpa==0) ? "" : "vkFerpa is mismatching Expecting 0 but was " + vkFerpa+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("GLBA")) {
			validationMessage +=(vkGlba==1) ? "" : "vkGlba is mismatching Expecting 1 but was " + vkGlba+"\n";

			try {
				Glba glba = contentChecks.getGlba();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "GLBA", glba);

			} catch (Exception e) {
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkGlba==0) ? "" : "vkGlba is mismatching Expecting 0 but was " + vkGlba+"\n";
		}

		System.out.println(Arrays.asList(expectedRisks));

		if (Arrays.asList(expectedRisks).contains("HIPAA")) {
			validationMessage +=(vkHipaa==1) ? "" : "vkHipaa is mismatching Expecting 1 but was " + vkHipaa+"\n";

			try {
				Hipaa hipaa = contentChecks.getHipaa();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "HIPAA", hipaa);
			} catch (Exception e) {
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkHipaa==0) ? "" : "vkHipaa is mismatching Expecting 0 but was " + vkHipaa+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("PCI")) {
			validationMessage +=(vkPci==1) ? "" : "vkPci is mismatching Expecting 1 but was " + vkPci+"\n";

			try {
				Pci pci = contentChecks.getPci();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "PCI", pci);

			} catch (Exception e) {
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkPci==0) ? "" : "vkPci is mismatching Expecting 0 but was " + vkPci+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("PII")) {
			validationMessage +=(vkPii==1) ? "" : "vkPii is mismatching Expecting 1 but was " + vkPii+"\n";

			try {
				Pii pii = contentChecks.getPii();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "PII", pii);

			} catch (Exception e) {
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkPii==0) ? "" : "vkPii is mismatching Expecting 0 but was " + vkPii+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("VBA Macros")) {
			validationMessage +=(vkVbaMacros>=1) ? "" : "vkVbaMacros is mismatching Expecting 1 or more but was " + vkVbaMacros+"\n";

			try {
				VbaMacros vbaMacros = contentChecks.getVbaMacros();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "VBA Macros", vbaMacros);

			} catch (Exception e) {
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkVbaMacros==0) ? "" : "vkVbaMacros is mismatching Expecting 0 but was " + vkVbaMacros+"\n";
		}

		if (Arrays.asList(expectedRisks).contains("Virus / Malware")) {
			validationMessage +=(vkVirus==1) ? "" : "vkVirus is mismatching Expecting 1 but was " + vkVirus+"\n";

			try {
				Virus virus = contentChecks.getVirus();
				validationMessage += validateRiskContentVulnerabilities(CIJson, "Virus", virus);

			} catch (Exception e) {
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window"+"\n";
			}

		} else {
			validationMessage +=(vkVirus==0) ? "" : "vkVirus is mismatching Expecting 0 but was " + vkVirus+"\n";
		}

		List<String> vkContentIqViolations = contentChecks.getVkContentIqViolations();
		if (Arrays.asList(expectedRisks).contains("ContentIQ Violations")) {
			validationMessage +=(vkContentIqViolations.size()>0) ? "" : 
				"Mismatch for vkContentIqViolations Array Expecting some values but " + vkContentIqViolations.size()+" value is seen"+"\n";

			try {
				validationMessage += validateContentIQViolationsContentVulnerabilities(CIJson, contentChecks);
			} catch (Exception e) {}

		} else {
			validationMessage +=(vkContentIqViolations.size()==0) ? "" : 
				"Mismatch for vkContentIqViolations Array Expecting no values but " + vkContentIqViolations.size()+" value is seen"+"\n";
		}







		return validationMessage;
	}

	private String validateRiskContentVulnerabilities(Map<String, String> CIJson, String type,
			Object objectType) throws Exception {

		String validationMessage="";

		boolean cvFlag = Boolean.parseBoolean(CIJson.get("cvFlag"));
		boolean strictFlag = getStrictFlag(CIJson);

		if(objectType instanceof Glba){
			String updatedTimestamp = ((Glba) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";


			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("glbaJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((Glba) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}else if(objectType instanceof Hipaa){
			String updatedTimestamp = ((Hipaa) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";

			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("hipaaJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((Hipaa) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}else if(objectType instanceof Pci){
			String updatedTimestamp = ((Pci) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";

			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("pciJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((Pci) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}else if(objectType instanceof Pii){
			String updatedTimestamp = ((Pii) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";

			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("piiJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((Pii) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}else if(objectType instanceof VbaMacros){
			String updatedTimestamp = ((VbaMacros) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";

			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("vbaJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((VbaMacros) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}else if(objectType instanceof Virus){
			String updatedTimestamp = ((Virus) objectType).getUpdatedTimestamp();
			validationMessage +=(StringUtils.isNotBlank(updatedTimestamp)) ? "" : 
				type+" Content Checks: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimestamp+"\n";

			if(cvFlag&&strictFlag){
				String jsonActual = marshall(objectType);
				String jsonExpected = CIJson.get("virusJson");
				Logger.info("Expected "+type+" Content Vulnerabilities Json: "+jsonExpected);
				Logger.info("Actual "+type+" Content Vulnerabilities Json: "+jsonActual);

				validationMessage +=validateContentVulnerabilitiesJson(jsonExpected, jsonActual);
			}else{
				List<com.elastica.beatle.dci.dto.Expression> expressions = ((Virus) objectType).getExpressions();
				for(com.elastica.beatle.dci.dto.Expression expression:expressions){
					String name = expression.getName();
					validationMessage +=(StringUtils.isNotBlank(name)) ? "" : 
						type+" Content Checks: Name field mismatch Expected:some string value and Actual:"+name+"\n";

					List<com.elastica.beatle.dci.dto.Value> values = expression.getValues();
					for(com.elastica.beatle.dci.dto.Value v:values){
						String key = v.getKey();
						int value = (int)v.getValue();
						validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
							type+" Content Checks: Key field mismatch Expected:some string value and Actual:"+key+"\n";
						validationMessage +=(value>0) ? "" : 
							"Content Checks: Value field mismatch Expected:some value > 0 and Actual:"+value+"\n";
					}

				}

			}
		}



		return validationMessage;
	}


	private String validateContentIQViolationsContentVulnerabilities(Map<String, String> CIJson,
			ContentChecks contentChecks) throws Exception {

		String validationMessage="";
		boolean cvFlag = Boolean.parseBoolean(CIJson.get("cvFlag"));
		try {
			List<ContentIqViolation> contentIqViolations = contentChecks.getContentIqViolations();

			if(cvFlag){

				List<String> actualProfileNameList = contentChecks.getVkContentIqViolations();
				List<String> expectedProfileNameList = Arrays.asList(CIJson.get("ciqProfileName").split(","));

				List<String> actualProfileNameKeyList = new ArrayList<String>();

				if(getStrictFlag(CIJson)){
					validationMessage +=(compareEqualList(actualProfileNameList, expectedProfileNameList)) ? "" : 
						"Source-->Content Checks-->vkContentIqViolations: ContentIqViolations Array"
						+ " mismatch Expected:"+expectedProfileNameList+" and Actual:"+actualProfileNameList+"\n";

					for(ContentIqViolation contentIqViolation: contentIqViolations){
						String updatedTimeStamp = contentIqViolation.getUpdatedTimestamp();
						validationMessage +=(StringUtils.isNotBlank(updatedTimeStamp)) ? "" : 
							" ContentIq Violation: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimeStamp+"\n";

						String key = contentIqViolation.getKey();
						actualProfileNameKeyList.add(key);

						List<Violation> violations = contentIqViolation.getViolations();
						for(Violation violation: violations){
							int count = 0; try {count = violation.getCount();} catch (Exception e) {count = 0;}
							String keyword = "";
							String predefinedTerm = ""; try {predefinedTerm = violation.getPredefinedTerm();} catch (Exception e) {predefinedTerm = "";}
							String customTerm = ""; try {customTerm = violation.getRegex();} catch (Exception e) {customTerm = "";}
							String predefinedDictionary = ""; try {predefinedDictionary = violation.getPredefinedDictionary();keyword = violation.getKeyword();} catch (Exception e) {predefinedDictionary = "";}
							String customDictionary = ""; try {customDictionary = violation.getDictionary();keyword = violation.getKeyword();} catch (Exception e) {customDictionary = "";}
							List<String> trainingProfile = new ArrayList<String>(); try {trainingProfile = violation.getTrainingProfile();} catch (Exception e) {customDictionary = "";}


							if(StringUtils.isNotBlank(predefinedTerm)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:"+expectedNameList+" and Actual:"+predefinedTerm+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Predefined Term: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(customTerm)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:"+expectedNameList+" and Actual:"+customTerm+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Custom Term: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(predefinedDictionary)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<String> expectedKeywordList = Arrays.asList(CIJson.get("ciqProfileKeyword").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:"+expectedNameList+" and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(expectedKeywordList.contains(keyword)) ? "" :
									"ContentIq Violation-->Violation-->Predefined Dictionary: Keyword field mismatch Expected:"+expectedKeywordList+" and Actual:"+keyword+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Predefined Dictionary: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(customDictionary)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<String> expectedKeywordList = Arrays.asList(CIJson.get("ciqProfileKeyword").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:"+expectedNameList+" and Actual:"+customDictionary+"\n";
								validationMessage +=(expectedKeywordList.contains(keyword)) ? "" :
									"ContentIq Violation-->Violation-->Custom Dictionary: Keyword field mismatch Expected:"+expectedKeywordList+" and Actual:"+keyword+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Custom Dictionary: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";

							}

							try {
								if(StringUtils.isNotBlank(trainingProfile.toString())){
									List<String> expectedTrainingProfile = Arrays.asList(CIJson.get("ciqTrainingProfile").split(","));

									for(String tp:expectedTrainingProfile){
										validationMessage +=(checkValueExistsInList(trainingProfile, tp)) ? "" : 
											"ContentIq Violation-->Violation: Training profile field mismatch "
											+ "Expected:"+trainingProfile+" and Actual:"+tp+"\n";

									}
								}
							} catch (Exception e) {
								Logger.info("ContentIq Violation-->Violation: Training profile is not present");
							}


						}	
					}
					validationMessage +=(compareEqualList(actualProfileNameKeyList, expectedProfileNameList)) ? "" : 
						"Source-->Content Checks-->ContentIqViolations Key: ContentIqViolations Array"
						+ " mismatch Expected:"+expectedProfileNameList+" and Actual:"+actualProfileNameKeyList+"\n";


				}else{
					validationMessage +=(compareSubList(actualProfileNameList, expectedProfileNameList)) ? "" : 
						"Source-->Content Checks-->vkContentIqViolations: ContentIqViolations Array"
						+ " mismatch Expected:"+expectedProfileNameList+" and Actual:"+actualProfileNameList+"\n";

					for(ContentIqViolation contentIqViolation: contentIqViolations){
						String updatedTimeStamp = contentIqViolation.getUpdatedTimestamp();
						validationMessage +=(StringUtils.isNotBlank(updatedTimeStamp)) ? "" : 
							" ContentIq Violation: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimeStamp+"\n";

						String key = contentIqViolation.getKey();
						actualProfileNameKeyList.add(key);


						List<Violation> violations = contentIqViolation.getViolations();
						for(Violation violation: violations){
							int count = 0; try {count = violation.getCount();} catch (Exception e) {count = 0;}
							String keyword = "";
							String predefinedTerm = ""; try {predefinedTerm = violation.getPredefinedTerm();} catch (Exception e) {predefinedTerm = "";}
							String customTerm = ""; try {customTerm = violation.getRegex();} catch (Exception e) {customTerm = "";}
							String predefinedDictionary = ""; try {predefinedDictionary = violation.getPredefinedDictionary();keyword = violation.getKeyword();} catch (Exception e) {predefinedDictionary = "";}
							String customDictionary = ""; try {customDictionary = violation.getDictionary();keyword = violation.getKeyword();} catch (Exception e) {customDictionary = "";}
							List<String> trainingProfile = new ArrayList<String>(); try {trainingProfile = violation.getTrainingProfile();} catch (Exception e) {customDictionary = "";}

							if(StringUtils.isNotBlank(predefinedTerm)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:"+expectedNameList+" and Actual:"+predefinedTerm+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Predefined Term: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(customTerm)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:"+expectedNameList+" and Actual:"+customTerm+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Custom Term: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(predefinedDictionary)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<String> expectedKeywordList = Arrays.asList(CIJson.get("ciqProfileKeyword").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:"+expectedNameList+" and Actual:"+predefinedDictionary+"\n";
								validationMessage +=(expectedKeywordList.contains(keyword)) ? "" :
									"ContentIq Violation-->Violation-->Predefined Dictionary: Keyword field mismatch Expected:"+expectedKeywordList+" and Actual:"+keyword+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Predefined Dictionary: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

							}if(StringUtils.isNotBlank(customDictionary)){
								List<String> expectedNameList = Arrays.asList(CIJson.get("ciqProfileType").split(","));
								List<String> expectedKeywordList = Arrays.asList(CIJson.get("ciqProfileKeyword").split(","));
								List<Integer> expectedCountList = 
										Lists.transform(Arrays.asList(CIJson.get("ciqProfileCount").split(",")), new Function<String, Integer>() {
											public Integer apply(String e) {
												return Integer.parseInt(e);
											};
										});

								validationMessage +=(expectedNameList.contains(customDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:"+expectedNameList+" and Actual:"+customDictionary+"\n";
								validationMessage +=(expectedKeywordList.contains(keyword)) ? "" :
									"ContentIq Violation-->Violation-->Custom Dictionary: Keyword field mismatch Expected:"+expectedKeywordList+" and Actual:"+keyword+"\n";
								validationMessage +=(expectedCountList.contains(count)) ? "" : 
									"ContentIq Violation-->Violation-->Custom Dictionary: Count field mismatch Expected:"+expectedCountList+" and Actual:"+count+"\n";

								validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
								validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
									"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
								validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
									"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";

							}

							try {
								if(StringUtils.isNotBlank(trainingProfile.toString())){
									for(String tp:trainingProfile){
										validationMessage +=(StringUtils.isNotBlank(tp)) ? "" : 
											"ContentIq Violation-->Violation: Training profile field mismatch "
											+ "Expected:some value and Actual:"+tp+"\n";

									}
								}
							} catch (Exception e) {
								Logger.info("ContentIq Violation-->Violation: Training profile is not present");
							}
						}
					}
					validationMessage +=(compareSubList(actualProfileNameKeyList, expectedProfileNameList)) ? "" : 
						"Source-->Content Checks-->ContentIqViolations Key: ContentIqViolations Array"
						+ " mismatch Expected:"+expectedProfileNameList+" and Actual:"+actualProfileNameKeyList+"\n";
				}





			}else{
				for(ContentIqViolation contentIqViolation: contentIqViolations){
					String updatedTimeStamp = contentIqViolation.getUpdatedTimestamp();
					validationMessage +=(StringUtils.isNotBlank(updatedTimeStamp)) ? "" : 
						" ContentIq Violation: Updated timestamp field mismatch Expected:some string value and Actual:"+updatedTimeStamp+"\n";

					String key = contentIqViolation.getKey();
					validationMessage +=(StringUtils.isNotBlank(key)) ? "" : 
						" ContentIq Violation: key field mismatch Expected:some string value and Actual:"+key+"\n";

					List<Violation> violations = contentIqViolation.getViolations();
					for(Violation violation: violations){
						int count = 0; try {count = violation.getCount();} catch (Exception e) {count = 0;}
						String keyword = "";
						validationMessage +=(count>0) ? "" : 
							"ContentIq Violation-->Violation: Count field mismatch Expected:some value > 0 and Actual:"+count+"\n";

						String predefinedTerm = ""; try {predefinedTerm = violation.getPredefinedTerm();} catch (Exception e) {predefinedTerm = "";}
						String customTerm = ""; try {predefinedTerm = violation.getRegex();} catch (Exception e) {customTerm = "";}
						String predefinedDictionary = ""; try {predefinedDictionary = violation.getPredefinedDictionary();keyword = violation.getKeyword();} catch (Exception e) {predefinedDictionary = "";}
						String customDictionary = ""; try {predefinedTerm = violation.getDictionary();keyword = violation.getKeyword();} catch (Exception e) {customDictionary = "";}
						List<String> trainingProfile = new ArrayList<String>(); try {trainingProfile = violation.getTrainingProfile();} catch (Exception e) {customDictionary = "";}

						if(StringUtils.isNotBlank(predefinedTerm)){
							validationMessage +=(StringUtils.isNotBlank(predefinedTerm)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:some string value and Actual:"+predefinedTerm+"\n";
							validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
								"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
							validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

						}if(StringUtils.isNotBlank(customTerm)){
							validationMessage +=(StringUtils.isNotBlank(customTerm)) ? "" : 
								"ContentIq Violation-->Violation: Custom Term field mismatch Expected:some string valueand Actual:"+customTerm+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";
							validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

						}if(StringUtils.isNotBlank(predefinedDictionary)){
							validationMessage +=(StringUtils.isNotBlank(predefinedDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:some string value and Actual:"+predefinedDictionary+"\n";
							validationMessage +=(StringUtils.isNotBlank(keyword)) ? "" : 
								"ContentIq Violation-->Violation-->Predefined Dictionary: Keyword field mismatch Expected:some string value and Actual:"+keyword+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
							validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
								"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
							validationMessage +=(StringUtils.isBlank(customDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:empty string and Actual:"+customDictionary+"\n";

						}if(StringUtils.isNotBlank(customDictionary)){
							validationMessage +=(StringUtils.isNotBlank(customDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Custom Dictionary field mismatch Expected:some string value and Actual:"+customDictionary+"\n";
							validationMessage +=(StringUtils.isNotBlank(keyword)) ? "" : 
								"ContentIq Violation-->Violation-->Custom Dictionary: Keyword field mismatch Expected:some string value and Actual:"+keyword+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedTerm)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Term field mismatch Expected:empty string and Actual:"+predefinedTerm+"\n";
							validationMessage +=(StringUtils.isBlank(customTerm)) ? "" : 
								"ContentIq Violation-->Violation: Custom Term field mismatch Expected:empty string and Actual:"+customTerm+"\n";
							validationMessage +=(StringUtils.isBlank(predefinedDictionary)) ? "" : 
								"ContentIq Violation-->Violation: Predefined Dictionary field mismatch Expected:empty string and Actual:"+predefinedDictionary+"\n";

						}
						try {
							if(StringUtils.isNotBlank(trainingProfile.toString())){
								for(String tp:trainingProfile){
									validationMessage +=(StringUtils.isNotBlank(tp)) ? "" : 
										"ContentIq Violation-->Violation: Training profile field mismatch "
										+ "Expected:some value and Actual:"+tp+"\n";

								}
							}
						} catch (Exception e) {
							Logger.info("ContentIq Violation-->Violation: Training profile is not present");
						}
					}
				}
			}

		} catch (Exception e) {
			validationMessage +="ContentIQ Violations expressions is not present in Content Vulnerabilities detail window"+"\n";
		}

		return validationMessage;
	}

	private String verifyContentLogs(TestSuiteDTO suiteData, Source source, Map<String, String> CIJson) {
		String validationMessage = "";

		ContentChecks contentChecks=source.getContentChecks();
		boolean violations = contentChecks.getViolations();
		validationMessage +=(!violations) ? "" : "ContentChecks-->violations flag is mismatching Expecting false but was " + violations+"\n";
		validationMessage += verifyMimeTypeLogs(suiteData, source, CIJson);
		validationMessage += verifyDocumentClassificationLogs(contentChecks, CIJson);

		int vkDlp = 0; try {vkDlp = contentChecks.getVkDlp();} catch (Exception e) {vkDlp = 0;}
		int vkFerpa = 0; try {vkFerpa = contentChecks.getVkFerpa();} catch (Exception e) {vkFerpa = 0;}
		int vkGlba = 0; try {vkGlba = contentChecks.getVkGlba();} catch (Exception e) {vkGlba = 0;}
		int vkHipaa = 0; try {vkHipaa = contentChecks.getVkHipaa();} catch (Exception e) {vkHipaa = 0;}
		int vkPci = 0; try {vkPci = contentChecks.getVkPci();} catch (Exception e) {vkPci = 0;}
		int vkPii = 0; try {vkPii = contentChecks.getVkPii();} catch (Exception e) {vkPii = 0;}
		int vkVbaMacros = 0; try {vkVbaMacros = contentChecks.getVkVbaMacros();} catch (Exception e) {vkVbaMacros = 0;}
		int vkVirus = 0; try {vkVirus = contentChecks.getVkVirus();} catch (Exception e) {vkVirus = 0;}

		validationMessage +=(vkDlp==0) ? "" : "vkDlp is mismatching Expecting 0 but was " + vkDlp+"\n";
		validationMessage +=(vkFerpa==0) ? "" : "vkFerpa is mismatching Expecting 0 but was " + vkFerpa+"\n";
		validationMessage +=(vkGlba==0) ? "" : "vkGlba is mismatching Expecting 0 but was " + vkGlba+"\n";
		validationMessage +=(vkHipaa==0) ? "" : "vkHipaa is mismatching Expecting 0 but was " + vkHipaa+"\n";
		validationMessage +=(vkPci==0) ? "" : "vkPci is mismatching Expecting 0 but was " + vkPci+"\n";
		validationMessage +=(vkPii==0) ? "" : "vkPii is mismatching Expecting 0 but was " + vkPii+"\n";
		validationMessage +=(vkVbaMacros==0) ? "" : "vkVbaMacros is mismatching Expecting 0 but was " + vkVbaMacros+"\n";
		validationMessage +=(vkVirus==0) ? "" : "vkGlba is mismatching Expecting 0 but was " + vkVirus+"\n";

		String[] expectedContent = CIJson.get("Risks").split(","); 


		if (Arrays.asList(expectedContent).contains("audio")) {
			try {validateContentContentVulnerabilities(CIJson, contentChecks.getAudio());} catch (Exception e) {}
		}
		if (Arrays.asList(expectedContent).contains("video")) {
			try {validateContentContentVulnerabilities(CIJson, contentChecks.getVideo());} catch (Exception e) {}
		}
		if (Arrays.asList(expectedContent).contains("image")) {
			try {validateContentContentVulnerabilities(CIJson, contentChecks.getImage());} catch (Exception e) {}
		}
		if (Arrays.asList(expectedContent).contains("executable")) {
			try {validateContentContentVulnerabilities(CIJson, contentChecks.getExecutable());} catch (Exception e) {}
		}
		if (Arrays.asList(expectedContent).contains("cryptographic_keys")) {
			try {validateContentContentVulnerabilities(CIJson, contentChecks.getCryptographicKeys());} catch (Exception e) {}
		}

		return validationMessage;
	}

	private String validateContentContentVulnerabilities(Map<String, String> CIJson,
			Object objectType) throws Exception {

		String validationMessage="";

		if(objectType instanceof Audio){
			List<com.elastica.beatle.dci.dto.Reason> reasons = 
					((Audio) objectType).getReasons();
			for(com.elastica.beatle.dci.dto.Reason reason:reasons){
				String nameActual = reason.getName();
				String nameExpected = "Content analysis indicate an audio file";       		
				validationMessage +=(nameActual.equalsIgnoreCase(nameExpected)) ? "" : 
					"Source-->Content Checks-->Audio: Name field mismatch Expected:"+nameExpected+" and Actual:"+nameActual+"\n";

				List<com.elastica.beatle.dci.dto.Value> values = reason.getValues();
				for(com.elastica.beatle.dci.dto.Value value:values){
					String keyActual = value.getKey();
					String valueActual = (String)value.getValue();
					String keyExpected = "Format";
					String valueExpected = "Unrecognized audio.";

					validationMessage +=(keyActual.equalsIgnoreCase(keyExpected)) ? "" : 
						"Source-->Content Checks-->Audio: Key field mismatch Expected:"+keyExpected+" and Actual:"+keyActual+"\n";
					validationMessage +=(valueActual.equalsIgnoreCase(valueExpected)) ? "" : 
						"Source-->Content Checks-->Audio: Value field mismatch Expected:"+valueExpected+" and Actual:"+valueActual+"\n";
				}
			}
		}
		if(objectType instanceof Video){
			List<com.elastica.beatle.dci.dto.Reason> reasons = 
					((Video) objectType).getReasons();
			for(com.elastica.beatle.dci.dto.Reason reason:reasons){
				String nameActual = reason.getName();
				String nameExpected = "Content analysis indicate an video file";       		
				validationMessage +=(nameActual.equalsIgnoreCase(nameExpected)) ? "" : 
					"Source-->Content Checks-->Video: Name field mismatch Expected:"+nameExpected+" and Actual:"+nameActual+"\n";

				List<com.elastica.beatle.dci.dto.Value> values = reason.getValues();
				for(com.elastica.beatle.dci.dto.Value value:values){
					String keyActual = value.getKey();
					String valueActual = (String)value.getValue();
					String keyExpected = "Format";
					String valueExpected = "Unrecognized video.";

					validationMessage +=(keyActual.equalsIgnoreCase(keyExpected)) ? "" : 
						"Source-->Content Checks-->Video: Key field mismatch Expected:"+keyExpected+" and Actual:"+keyActual+"\n";
					validationMessage +=(valueActual.equalsIgnoreCase(valueExpected)) ? "" : 
						"Source-->Content Checks-->Video: Value field mismatch Expected:"+valueExpected+" and Actual:"+valueActual+"\n";
				}
			}
		}
		if(objectType instanceof Executable){
			List<com.elastica.beatle.dci.dto.Reason> reasons = 
					((Executable) objectType).getReasons();
			for(com.elastica.beatle.dci.dto.Reason reason:reasons){
				String nameActual = reason.getName();
				String nameExpected = "Content analysis indicates a binary executable file";       		
				validationMessage +=(nameActual.equalsIgnoreCase(nameExpected)) ? "" : 
					"Source-->Content Checks-->Executable: Name field mismatch Expected:"+nameExpected+" and Actual:"+nameActual+"\n";

				List<com.elastica.beatle.dci.dto.Value> values = reason.getValues();
				for(com.elastica.beatle.dci.dto.Value value:values){
					String keyActual = value.getKey();
					String valueActual = (String)value.getValue();
					String keyExpected = "Format";
					String valueExpected = "Binary executable.";

					validationMessage +=(keyActual.equalsIgnoreCase(keyExpected)) ? "" : 
						"Source-->Content Checks-->Executable: Key field mismatch Expected:"+keyExpected+" and Actual:"+keyActual+"\n";
					validationMessage +=(valueActual.equalsIgnoreCase(valueExpected)) ? "" : 
						"Source-->Content Checks-->Executable: Value field mismatch Expected:"+valueExpected+" and Actual:"+valueActual+"\n";
				}
			}
		}
		if(objectType instanceof CryptographicKeys){
			List<com.elastica.beatle.dci.dto.Reason> reasons = 
					((CryptographicKeys) objectType).getReasons();
			for(com.elastica.beatle.dci.dto.Reason reason:reasons){
				String nameActual = reason.getName();
				String nameExpected = "Cryptographic Keys";       		
				validationMessage +=(nameActual.equalsIgnoreCase(nameExpected)) ? "" : 
					"Source-->Content Checks-->CryptographicKeys: Name field mismatch Expected:"+nameExpected+" and Actual:"+nameActual+"\n";

				List<com.elastica.beatle.dci.dto.Value> values = reason.getValues();
				for(com.elastica.beatle.dci.dto.Value value:values){
					String keyActual = value.getKey();
					Object valueActual = value.getValue();

					validationMessage +=(StringUtils.isNotBlank(keyActual)) ? "" : 
						"Source-->Content Checks-->CryptographicKeys: Key field mismatch Expected: some value and Actual:"+keyActual+"\n";
					validationMessage +=(StringUtils.isNotBlank((String)valueActual)) ? "" : 
						"Source-->Content Checks-->CryptographicKeys: Value field mismatch Expected:some value and Actual:"+valueActual+"\n";
				}
			}
		}

		if(objectType instanceof Image){
			List<com.elastica.beatle.dci.dto.Reason> reasons = 
					((Image) objectType).getReasons();
			for(com.elastica.beatle.dci.dto.Reason reason:reasons){
				String nameActual = reason.getName();
				String nameExpected = "Content analysis indicate an image file";       		
				validationMessage +=(nameActual.equalsIgnoreCase(nameExpected)) ? "" : 
					"Source-->Content Checks-->Image: Name field mismatch Expected:"+nameExpected+" and Actual:"+nameActual+"\n";

				List<com.elastica.beatle.dci.dto.Value> values = reason.getValues();
				for(com.elastica.beatle.dci.dto.Value value:values){
					String keyActual = value.getKey();
					Object valueActual = value.getValue();

					validationMessage +=(StringUtils.isNotBlank(keyActual)) ? "" : 
						"Source-->Content Checks-->Image: Key field mismatch Expected: some value and Actual:"+keyActual+"\n";
					validationMessage +=(StringUtils.isNotBlank((String)valueActual)) ? "" : 
						"Source-->Content Checks-->Image: Value field mismatch Expected:some value and Actual:"+valueActual+"\n";
				}
			}
		}


		return validationMessage;
	}


	private String verifyDocumentClassificationLogs(ContentChecks contentChecks, Map<String, String> CIJson) {
		String validationMessage = "";

		String fileName = CIJson.get("FileName");
		List<String> docClassActual = contentChecks.getDocClass();
		List<String> docClassExpected = Arrays.asList(docClassTypesForAFile(fileName));

		if(docClassActual.isEmpty()&docClassExpected.isEmpty()){
			Logger.info("File has not been classified");
		}else{
			if(getDocClassFlag(CIJson)){
				Logger.info("File classification check is disabled");
			}else{
				validationMessage +=(compareEqualList(docClassActual, docClassExpected)) ? "" : 
					"Source-->Content Checks: Doc class mismatch Expected:"+docClassExpected+" and Actual:"+docClassActual+"\n";

			}
		}

		return validationMessage;
	}



	private String verifyMimeTypeLogs(TestSuiteDTO suiteData, Source source, Map<String, String> CIJson) {
		String validationMessage = "";

		ContentChecks contentChecks=source.getContentChecks();
		String mimeTypeActual = contentChecks.getMimetype();

		boolean cvFlag = Boolean.parseBoolean(CIJson.get("cvFlag"));

		if(cvFlag){
			if(getMimeFlag(CIJson)){
				validationMessage +=(StringUtils.isNotBlank(mimeTypeActual)) ? "" : 
					"Source-->Content Checks-->Image: Mime type mismatch Expected: some value and Actual:"+mimeTypeActual+"\n";
			}else{
				String mimeTypeExpected = getMimeType(suiteData, source, CIJson);
				validationMessage +=(mimeTypeActual.equalsIgnoreCase(mimeTypeExpected)) ? "" : 
					"Source-->Content Checks: Mime type mismatch Expected:"+mimeTypeExpected+" and Actual:"+mimeTypeActual+"\n";
			}

		}else{
			validationMessage +=(StringUtils.isNotBlank(mimeTypeActual)) ? "" : 
				"Source-->Content Checks-->Image: Mime type mismatch Expected: some value and Actual:"+mimeTypeActual+"\n";
		}

		return validationMessage;
	}

	private boolean getMimeFlag(Map<String, String> CIJson) {
		boolean flag=false;
		try{
			flag = Boolean.parseBoolean(CIJson.get("mimeFlag"));
		}catch(Exception e){}
		return flag;
	}

	private boolean getDocClassFlag(Map<String, String> CIJson) {
		boolean flag=false;
		try{
			flag = Boolean.parseBoolean(CIJson.get("docClassFlag"));
		}catch(Exception e){}
		return flag;
	}

	private boolean getStrictFlag(Map<String, String> CIJson) {
		boolean flag=true;
		try{
			flag = Boolean.parseBoolean(CIJson.get("strictFlag"));
		}catch(Exception e){}
		return flag;
	}

	private String getMimeType(TestSuiteDTO suiteData, Source source, Map<String,String> CIJson) {
		String mimeType="";

		String[] fileNames = {
				"hipaa.txt","glba.txt","source_code.txt","source_code.html",
				"design_short.txt","design_short.txt","design_short.txt",	
				"Custom_Dictionaries.txt","Custom_Terms.txt","Diseases.txt",
				"US_License_Plate_Number.txt"	
		};

		String fileName = getFileName(source, CIJson).replace(" Mail With Attachment and Body", "").
				replace(" Mail With Body", "");
		String message = source.getMessage();

		if(message.contains("Email message ")){
			if(checkStringInArray(fileNames,fileName)){
				mimeType = "text/html";
			}else{
				mimeType = mimeTypesForAFile(fileName);
			}
		}else{
			mimeType = mimeTypesForAFile(fileName);
		}

		return mimeType;
	}

	public Map<String, String> populateContentInspectionJson(TestSuiteDTO suiteData, 
			String fileName, int rCount, String severity, String sourceType, boolean cvFlag) {
		Map<String, String> CIJson = new HashMap<String, String>();
		CIJson.put("LogCount",Integer.toString(rCount));
		CIJson.put("FileName",fileName);
		CIJson.put("Severity",severity);
		CIJson.put("Source",sourceType);
		CIJson.put("User",suiteData.getUsername());

		if(severity.equalsIgnoreCase(DCIConstants.CIWarningSeverityType)){
			CIJson.put("Risks","");
		}else{
			CIJson.put("Risks",riskContentTypesForAFile(riskTypesForAFile(fileName)));
		}


		CIJson.put("cvFlag",Boolean.toString(cvFlag));
		if(cvFlag){
			String risks = riskContentTypesForAFile(riskTypesForAFile(fileName));
			if(risks.contains("PCI")){
				CIJson.put("pciJson",pciRisksForAFile(fileName));
			}
			if(risks.contains("PII")){
				CIJson.put("piiJson",piiRisksForAFile(fileName));
			}
			if(risks.contains("HIPAA")){
				CIJson.put("hipaaJson",hipaaRisksForAFile(fileName));
			}
			if(risks.contains("GLBA")){
				CIJson.put("glbaJson",glbaRisksForAFile(fileName));
			}
			if(risks.contains("VBA Macros")){
				CIJson.put("vbaJson",vbaRisksForAFile(fileName));
			}
			if(risks.contains("Virus")){
				CIJson.put("virusJson",virusRisksForAFile(fileName));
			}
		}
		return CIJson;
	}

	public Map<String, String> populateContentInspectionJson(TestSuiteDTO suiteData, 
			String fileName, int rCount, String severity, String sourceType, boolean cvFlag,
			List<String> ciqProfileName, List<String> ciqProfileType, List<String> ciqProfileKeyword, 
			List<String> ciqProfileCount, List<String> ciqType) {
		Map<String, String> CIJson = new HashMap<String, String>();
		CIJson.put("LogCount",Integer.toString(rCount));
		CIJson.put("FileName",fileName);
		CIJson.put("Severity",severity);
		CIJson.put("Source",sourceType);
		CIJson.put("User",suiteData.getUsername());
		CIJson.put("Risks",riskContentTypesForAFile(riskTypesForAFile(fileName)));
		CIJson.put("cvFlag",Boolean.toString(cvFlag));
		CIJson.put("ciqProfileName",ciqProfileName.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileType",ciqProfileType.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileKeyword",ciqProfileKeyword.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileCount",ciqProfileCount.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqType",ciqType.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

		return CIJson;
	}

	public Map<String, String> populateContentInspectionJson(TestSuiteDTO suiteData, 
			String fileName, int rCount, String severity, String sourceType, boolean cvFlag,
			List<String> ciqProfileName, List<String> ciqProfileType, List<String> ciqProfileKeyword, 
			List<String> ciqProfileCount, List<String> ciqType, List<String> ciqTrainingProfile) {
		Map<String, String> CIJson = new HashMap<String, String>();
		CIJson.put("LogCount",Integer.toString(rCount));
		CIJson.put("FileName",fileName);
		CIJson.put("Severity",severity);
		CIJson.put("Source",sourceType);
		CIJson.put("User",suiteData.getUsername());
		CIJson.put("Risks",riskContentTypesForAFile(riskTypesForAFile(fileName)));
		CIJson.put("cvFlag",Boolean.toString(cvFlag));
		CIJson.put("ciqProfileName",ciqProfileName.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileType",ciqProfileType.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileKeyword",ciqProfileKeyword.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileCount",ciqProfileCount.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqType",ciqType.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqTrainingProfile",ciqTrainingProfile.toString().replaceAll("\\[", "").replaceAll("\\]", ""));

		return CIJson;
	}


	public Map<String, String> populateContentInspectionJson(TestSuiteDTO suiteData, 
			String fileName, int rCount, String severity, String sourceType, boolean cvFlag,
			Map<String,String> ciq) {
		Map<String, String> CIJson = new HashMap<String, String>();
		CIJson.put("LogCount",Integer.toString(rCount));
		CIJson.put("FileName",fileName);
		CIJson.put("Severity",severity);
		CIJson.put("Source",sourceType);
		CIJson.put("User",suiteData.getUsername());
		CIJson.put("Risks",riskContentTypesForAFile(riskTypesForAFile(fileName)));
		CIJson.put("cvFlag",Boolean.toString(cvFlag));
		CIJson.put("ciqProfileName",ciq.get("ciqProfileName").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileType",ciq.get("ciqProfileType").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileKeyword",ciq.get("ciqProfileKeyword").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqProfileCount",ciq.get("ciqProfileCount").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqType",ciq.get("ciqType").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqPDT",ciq.get("ciqPDT").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqPDD",ciq.get("ciqPDD").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqCT",ciq.get("ciqCT").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqCD",ciq.get("ciqCD").toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		CIJson.put("ciqMLProfile",ciq.get("ciqMLProfile").toString().replaceAll("\\[", "").replaceAll("\\]", ""));

		return CIJson;
	}


	public void createCIQProfile(TestSuiteDTO suiteData, Client restClient, String ciqProfileName, String ciqProfileDescription, 
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms, 
			List<String> valuesCustomDict, List<String> valuesCustomTerms,
			List<String> valuesRiskTypes, List<String> valuesContentTypes, 
			List<String> valuesFileFormat, List<String> valuesMLProfile,
			String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag) 
					throws Exception {
		String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 
				severity, threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
				valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);	
		Logger.info(payload);
		createContentIqProfile(restClient, ciqProfileName, getCookieHeaders(suiteData), 
				new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

		HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		String contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
		Logger.info(contentIQProfileId);

	}

	public HttpResponse createContentIQProfile(TestSuiteDTO suiteData, Client restClient, String ciqProfileName, String ciqProfileDescription, 
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms, 
			List<String> valuesCustomDict, List<String> valuesCustomTerms,
			List<String> valuesRiskTypes, List<String> valuesContentTypes, 
			List<String> valuesFileFormat, List<String> valuesMLProfile,
			String severity, int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag) 
					throws Exception {
		String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 
				severity, threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,
				valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile);	
		Logger.info(payload);
		HttpResponse createContentIQResponse = createContentIqProfile(restClient, getCookieHeaders(suiteData), 
				new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

		return createContentIQResponse;
	}

	public void createAllCIQPredefinedTerms(Client restClient, TestSuiteDTO suiteData){


		String[] ciqTerms = predefinedTermsCIQProfileText();
		String[] ciqProfileNames = predefinedTermsCIQProfileName(); 
		String[] ciqProfileDescriptions = predefinedTermsCIQProfileDescription();


		for(int i=0;i<ciqProfileNames.length;i++){
			createCIQPredefinedTerms(restClient, suiteData, ciqTerms[i], ciqProfileNames[i],
					ciqProfileDescriptions[i], "high", 1, true, 1, false);
		}	
	}

	public void createAllCIQPredefinedDictionaries(Client restClient, TestSuiteDTO suiteData){

		String[] ciqDictionaries = predefinedDictionariesCIQProfileText();
		String[] ciqProfileNames = predefinedDictionariesCIQProfileName(); 
		String[] ciqProfileDescriptions = predefinedDictionariesCIQProfileDescription();


		for(int i=0;i<ciqProfileNames.length;i++){
			createCIQPredefinedDictionaries(restClient, suiteData, ciqDictionaries[i], ciqProfileNames[i],
					ciqProfileDescriptions[i], "high", 1, true, 1, false);
		}	
	}

	public String createCIQProfile(TestSuiteDTO suiteData, Client restClient, String ciqProfileName, 
			String ciqProfileDescription, String customDict, String customTerms) throws Exception{

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+
				" with description:"+ciqProfileDescription+" in progress");

		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesFileFormat=null;List<String> valuesMLProfile=null;

		if(StringUtils.isAnyBlank(customDict)){
			valuesCustomDict=new ArrayList<String>();
			List<String> values = Arrays.asList(customDict.split(","));
			for(String v:values){
				valuesCustomDict.add(v);
			}
		}

		if(StringUtils.isAnyBlank(customTerms)){
			valuesCustomTerms=new ArrayList<String>();
			List<String> values = Arrays.asList(customTerms.split(","));
			for(String v:values){
				valuesCustomTerms.add(v);
			}
		}

		HttpResponse response  = createContentIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat, valuesMLProfile,
				"high", 0, true, 1, false);

		String responseMessage = response.getStatusLine().toString();
		String responseBody = ClientUtil.getResponseBody(response);

		String validationMessage="";

		validationMessage +=(responseMessage.equalsIgnoreCase(DCIConstants.responseOKMessage)) ? "" : 
			"Create CIQ profile response message is mismatching Expected:"+DCIConstants.responseOKMessage+" and Actual:"+responseMessage+"\n";
		validationMessage +=(responseBody.equalsIgnoreCase(DCIConstants.ciqCreateSuccessMessage)) ? "" : 
			"Create CIQ profile response body is mismatching Expected:"+DCIConstants.ciqCreateSuccessMessage+" and Actual:"+responseMessage+"\n";

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+
				" with description:"+ciqProfileDescription+" is completed");


		return validationMessage;
	}


	public void getSplunkResults(TestSuiteDTO suiteData, String fileName){
		SplunkQueryResult splunkResult = SplunkQueries.lookForProcessedCILogsForAFile(
				suiteData.getEnvironmentName(), fileName, "-2h");
		if(splunkResult.getEventsCount()>=1){
			Logger.info("****************Splunk Output*****************");
			Logger.info("Splunk Query Output: File "+fileName+ " got processed by Content Inspection Engine successfully");

			try {
				org.json.JSONArray result = splunkResult.getQueryResult().getJSONArray("results"); 
				for(int i = 0;i<result.length();){
					org.json.JSONObject resultObject = result.getJSONObject(i);
					String rawJson = ((String) resultObject.get("_raw")).replaceAll("_raw(.*)DEBUG-CI results for file(.*) - ", "");
					rawJson = rawJson.replaceAll("(.*) Finished Job:",""); 
					rawJson = rawJson.replace("u'", "\"");rawJson = rawJson.replace("'", "\"");
					rawJson = rawJson.replace("\"_silent\": [None]", "\"_silent\": []");
					rawJson = rawJson.replace(": True", ": true");
					rawJson = rawJson.replace(": False", ": false");

					Logger.info("Splunk Query Output for File "+fileName
							+" ==>"+ rawJson);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			Logger.info("**********************************************");
		}else{
			Logger.info("****************Splunk Output*****************");
			Logger.info("Splunk Query Output for File "+fileName+ " did not get processed by Content Inspection Engine");
			Logger.info("**********************************************");
		}

	}

	public List<Map<String,String>> getAllMLProfiles
	(TestSuiteDTO suiteData, Client restClient) throws Exception{
		List<Map<String,String>> mlDetails = new ArrayList<Map<String,String>>();

		String restAPI = "/controls/make_api_request";
		StringEntity payload = new StringEntity(
				"{\"url\":\"mlprofiles\",\"id\":null,\"action\":\"list\",\"params\":{\"limit\":0}}");
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse mlProfilesList = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, payload);
		String mlJson = ClientUtil.getResponseBody(mlProfilesList);
		if(mlJson.contains("\"status\": 500")){
			waitForSeconds(1);
			dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			mlProfilesList = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, payload);
			mlJson = ClientUtil.getResponseBody(mlProfilesList);
		}
		//Logger.info("mlJson:"+mlJson);
		TrainingProfile tp = unmarshall(mlJson, TrainingProfile.class);
		Data data = tp.getData();Meta meta = data.getMeta();
		if(meta.getTotalCount()==0){
			Logger.info("No ML profile is present");
		}else{

			List<com.elastica.beatle.dci.dto.tp.Object> objects= data.getObjects();
			for(com.elastica.beatle.dci.dto.tp.Object object:objects){
				Double tpCLT = object.getConfidenceLevelThreshold();
				String tpName = object.getName();
				String tpDescription = object.getDescription();
				String tpId = object.getId();
				Boolean tpActive = object.getIsActive();
				String tpStatus = object.getStatus();
				//TrainingFiles tpTrainingFiles = object.getTrainingFiles();
				String tpCreatedBy = object.getCreatedBy();
				String tpCreatedOn = object.getCreatedOn();
				String tpModifiedBy = object.getModifiedBy();
				String tpModifiedOn = object.getModifiedOn();
				Map<String,String> mlDetail = new HashMap<String,String>();
				mlDetail.put("tpCLT", Double.toString(tpCLT));
				mlDetail.put("tpName", tpName);
				mlDetail.put("tpDescription", tpDescription);
				mlDetail.put("tpId", tpId);
				mlDetail.put("tpActive", Boolean.toString(tpActive));
				mlDetail.put("tpStatus", tpStatus);
				mlDetail.put("tpCreatedBy", tpCreatedBy);
				mlDetail.put("tpCreatedOn", tpCreatedOn);
				mlDetail.put("tpModifiedBy", tpModifiedBy);
				mlDetail.put("tpModifiedOn", tpModifiedOn);


				mlDetails.add(mlDetail);
			}
		}

		return mlDetails;
	}

	public Map<String,String> getMLProfileByName(TestSuiteDTO suiteData, Client restClient, String mlProfileName) throws Exception{
		Map<String,String> mlDetail = new HashMap<String,String>();

		List<Map<String,String>> mlDetails = getAllMLProfiles(suiteData, restClient);
		if(mlDetails.isEmpty()){
			Logger.info("No ML profiles listed with name:"+mlProfileName);
		}else{
			for(Map<String,String> ml:mlDetails){
				if(ml.get("tpName").equalsIgnoreCase(mlProfileName)){
					mlDetail=ml;break;
				}
			}
		}


		return mlDetail;
	}

	public boolean getMLProfileActive(TestSuiteDTO suiteData, Client restClient,String mlProfileName) throws Exception{
		Map<String,String> mlDetail = getMLProfileByName(suiteData, restClient, mlProfileName);

		if(mlDetail.isEmpty()){
			Logger.info("No ML profiles listed to get active status with name:"+mlProfileName);
		}else{
			return Boolean.parseBoolean(mlDetail.get("tpActive"));
		}

		return false;
	}

	public String getMLId(TestSuiteDTO suiteData, Client restClient,String mlProfileName) throws Exception{
		Map<String,String> mlDetail = getMLProfileByName(suiteData, restClient, mlProfileName);

		if(mlDetail.isEmpty()){
			Logger.info("No ML profiles listed to get ID with name:"+mlProfileName);
		}else{
			return mlDetail.get("tpId");
		}

		return null;
	}

	public String getMLCLT(TestSuiteDTO suiteData, Client restClient,String mlProfileName) throws Exception{
		Map<String,String> mlDetail = getMLProfileByName(suiteData, restClient, mlProfileName);

		if(mlDetail.isEmpty()){
			Logger.info("No ML profiles listed to get CLT with name:"+mlProfileName);
		}else{
			return mlDetail.get("tpCLT");
		}

		return null;
	}

	public String getMLDescription(TestSuiteDTO suiteData, Client restClient,String mlProfileName) throws Exception{
		Map<String,String> mlDetail = getMLProfileByName(suiteData, restClient, mlProfileName);

		if(mlDetail.isEmpty()){
			Logger.info("No ML profiles listed to get description with name:"+mlProfileName);
		}else{
			return mlDetail.get("tpDescription");
		}

		return null;
	}

	public void activateDeactivateMLProfile(TestSuiteDTO suiteData, Client restClient,String mlProfileName, boolean activateFlag) throws Exception{
		Map<String,String> mlDetail = getMLProfileByName(suiteData, restClient, mlProfileName);
		if(mlDetail.isEmpty()){
			Logger.info("No ML profiles listed to be activated or deactivated with name:"+mlProfileName);
		}else{
			if(activateFlag){
				Logger.info("Going to activate the ML profile name:"+mlProfileName);
			}else{
				Logger.info("Going to deactivate the ML profile name:"+mlProfileName);
			}

			if(Boolean.parseBoolean(mlDetail.get("tpActive"))==activateFlag){
				if(activateFlag){
					Logger.info("ML profile name:"+mlProfileName+" is already in activated state");
				}else{
					Logger.info("ML profile name:"+mlProfileName+" is already in deactivated state");
				}
			}else{
				String restAPI = "/controls/make_api_request";
				StringEntity payload = new StringEntity(
						"{\"url\":\"mlprofiles\",\"id\":\""+mlDetail.get("tpId")+"\","
								+ "\"action\":\"patch\",\"params\":null,"
								+ "\"data\":{\"name\":\""+mlDetail.get("tpName")+"\"\n,"
								+ "\"description\":\""+mlDetail.get("tpDescription")+"\","
								+ "\"is_active\":"+Boolean.toString(activateFlag)+","
								+ "\"confidence_level_threshold\":"+mlDetail.get("CLT")+"}}");	
				URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
				restClient.doPost(dataUri, getCookieHeaders(suiteData), null, payload);
				//String mlJson = ClientUtil.getResponseBody(mlResponse);
			}	
		}
	}

	public Map<String,String> createNewMLProfile(TestSuiteDTO suiteData, 
			Client restClient, String profileName, String profileDescription) 
					throws Exception{
		Map<String,String> mlDetail = new HashMap<String,String>();

		Logger.info("Creating new ML profile name:"+profileName+" in progress");

		String restAPI = "/controls/make_api_request";
		StringEntity payload = new StringEntity(
				"{\"url\":\"mlprofiles\",\"id\":null,\"action\":\"post\",\"params\":null,"
						+ "\"data\":{\"name\":\""+profileName+"\",\"description\":\""+profileDescription+"\","
						+ "\"is_active\":false,\"confidence_level_threshold\":0.9}}");	
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse mlProfilesList = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, payload);
		String mlJson = ClientUtil.getResponseBody(mlProfilesList);

		TrainingProfileDetailed tp = unmarshall(mlJson, TrainingProfileDetailed.class);
		DataDetailed data = tp.getData();
		if(tp.getStatus()!=201){
			Logger.info("ML profile creation with name "+profileName+" failed:"+tp.getStatus());
		}else{
			Double tpCLT = data.getConfidenceLevelThreshold();
			String tpName = data.getName();
			String tpDescription = data.getDescription();
			String tpId = data.getId();
			Boolean tpActive = data.getIsActive();
			String tpStatus = data.getStatus();
			//TrainingFiles tpTrainingFiles = data.getTrainingFiles();
			String tpCreatedBy = data.getCreatedBy();
			String tpCreatedOn = data.getCreatedOn();
			String tpModifiedBy = data.getModifiedBy();
			String tpModifiedOn = data.getModifiedOn();

			mlDetail.put("tpCLT", Double.toString(tpCLT));
			mlDetail.put("tpName", tpName);
			mlDetail.put("tpDescription", tpDescription);
			mlDetail.put("tpId", tpId);
			mlDetail.put("tpActive", Boolean.toString(tpActive));
			mlDetail.put("tpStatus", tpStatus);
			mlDetail.put("tpCreatedBy", tpCreatedBy);
			mlDetail.put("tpCreatedOn", tpCreatedOn);
			mlDetail.put("tpModifiedBy", tpModifiedBy);
			mlDetail.put("tpModifiedOn", tpModifiedOn);
		}

		Logger.info("Creating new ML profile name:"+profileName+" is completed");

		return mlDetail;
	}

	public boolean uploadFileIntoTrainingProfile(TestSuiteDTO suiteData, Client restClient, List<NameValuePair> headers, String mlProfileName, 
			String fileName, String uploadType, String filePath) throws Exception{
		Logger.info("Uploading filename:"+fileName+" into ML profile name:"+mlProfileName+" in progress");

		String id = getMLId(suiteData, restClient, mlProfileName);
		if(StringUtils.isNotBlank(id)){
			File uploadFile = new java.io.File(filePath);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();

			builder.setBoundary("-----------------------------10172886071452304082575574372");
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			FileBody fileBody = new FileBody(uploadFile, 
					org.apache.http.entity.ContentType.create("text/plain"), fileName);
			builder.addPart("training_file", fileBody)
			.addTextBody("profile_id", id)
			.addTextBody("training_set", uploadType);

			HttpEntity multipart = builder.build();

			//This is to print the entire entity
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(
					(int) multipart.getContentLength());
			multipart.writeTo(out);
			//String entityContentAsString = new String(out.toByteArray());
			//Logger.info("multipartEntity:" + entityContentAsString);

			String path = "/mlprofiles/upload/";
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
			Logger.info("URI ::"+dataUri.toString());		
			HttpResponse response = restClient.doPost(dataUri, headers, null, multipart);
			String responseBody = ClientUtil.getResponseBody(response);
			//Logger.info("Response body: "+responseBody);

			Logger.info("Uploading filename:"+fileName+" into ML profile name:"+mlProfileName+" is completed");

			return responseBody.equalsIgnoreCase("{status: \"success\"}");
		}


		return false;
	}

	public boolean processMLProfile(TestSuiteDTO suiteData, Client restClient,
			String mlProfileName) throws Exception{
		String mlId = getMLId(suiteData, restClient, mlProfileName);
		if(StringUtils.isBlank(mlId)){
			Logger.info("No ML profiles listed to be process with name:"+mlProfileName);
		}else{
			Logger.info("Going to process the ML profile name:"+mlProfileName);

			String restAPI = "/controls/make_api_request";
			StringEntity payload = new StringEntity(
					"{\"url\":\"mlprofiles/build\","
							+ "\"id\":\""+mlId+"\","
							+ "\"action\":\"get\",\"params\":null,\"data\":null}");	
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			HttpResponse mlResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, payload);
			String mlJson = ClientUtil.getResponseBody(mlResponse);

			Logger.info("Processing the ML profile name:"+mlProfileName+ " in progress");
			return mlJson.contains("{\"status\": \"success\"}");
		}

		return false;
	}

	public List<String> getMLProfileNames(TestSuiteDTO suiteData, Client restClient) throws Exception{
		List<String> mlProfileNames = new ArrayList<String>();

		HttpResponse response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		String tpJson = ClientUtil.getResponseBody(response);

		try {
			if(StringUtils.isBlank(tpJson)){
				waitForSeconds(5);
				response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
						suiteData.getScheme(), suiteData.getHost());
				tpJson = ClientUtil.getResponseBody(response);
			}
		} catch (Exception e) {
			waitForSeconds(5);
			response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			tpJson = ClientUtil.getResponseBody(response);
		}

		try {
			TrainingProfile tp = unmarshall(tpJson, 
					TrainingProfile.class);
			List<com.elastica.beatle.dci.dto.tp.Object> objects = tp.getData().getObjects();

			for(com.elastica.beatle.dci.dto.tp.Object object:objects){
				mlProfileNames.add(object.getName());

			}
		} catch (Exception e) {
			waitForSeconds(5);
			response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			tpJson = ClientUtil.getResponseBody(response);
			TrainingProfile tp = unmarshall(tpJson, 
					TrainingProfile.class);
			List<com.elastica.beatle.dci.dto.tp.Object> objects = tp.getData().getObjects();

			for(com.elastica.beatle.dci.dto.tp.Object object:objects){
				mlProfileNames.add(object.getName());

			}
		}
		return mlProfileNames;	

	}

	public Map<String,String> getMLProfileNameValues(TestSuiteDTO suiteData, Client restClient) throws Exception{
		Map<String,String> mlProfileNames = new HashMap<String,String>();

		HttpResponse response = null;
		String tpJson = null;
		try {
			response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			tpJson = ClientUtil.getResponseBody(response);
		} catch (Exception e) {
			response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			tpJson = ClientUtil.getResponseBody(response);
		}

		TrainingProfile tp = unmarshall(tpJson, 
				TrainingProfile.class);
		List<com.elastica.beatle.dci.dto.tp.Object> objects = tp.getData().getObjects();

		for(com.elastica.beatle.dci.dto.tp.Object object:objects){
			mlProfileNames.put(object.getName(), object.getStatus());

		}
		return mlProfileNames;	

	}


	public boolean checkIfMLProfilesAreProcessed(TestSuiteDTO suiteData, Client restClient,String[] mlProfileNames) throws Exception{
		List<String> mlProfileNamesActual = getMLProfileNames(suiteData, restClient);		
		List<String> mlProfileNamesExpected = Arrays.asList(mlProfileNames);

		Map<String,String> mlProfileNameValuesActual = getMLProfileNameValues(suiteData, restClient);

		List<String> mlFlag = new ArrayList<String>();
		boolean flag=false;
		if(this.compareList(mlProfileNamesActual, mlProfileNamesExpected)){

			for(String mlProfileNameExpected:mlProfileNamesExpected){
				mlFlag.add(mlProfileNameValuesActual.get(mlProfileNameExpected));
				Logger.info(mlProfileNameExpected+"="+mlProfileNameValuesActual.get(mlProfileNameExpected));
			}

			flag = !(mlFlag.contains("pending")|mlFlag.contains("processing")|mlFlag.contains("failed"));
		}else{
			flag=false;
		}

		return flag;
	}

	public boolean checkIfAllMLProfilesAreProcessed(TestSuiteDTO suiteData, 
			Client restClient,String[] mlProfileNames, int timeOut) throws Exception{
		int time = 0; boolean flag=false;
		
		for (int i = 0; i < timeOut; i++) {
			if(checkIfMLProfilesAreProcessed(suiteData, restClient, mlProfileNames)){
				Logger.info("**************************************************************");
				Logger.info("Training profiles got processed in "+(i+1)+" minutes"); flag=true;
				Logger.info("**************************************************************");
				break;
			} else {
				waitForOneMinute(i+1);time++;
			}
		}

		if(time==timeOut){
			Logger.info("Training profiles did not got processed within "+timeOut+" minutes");
			Logger.info("**************************************************************");
			printMLProfilesWhichAreNotProcessed(suiteData, restClient, mlProfileNames);
			Logger.info("**************************************************************");
		}

		return flag;
	}

	public void printMLProfilesWhichAreNotProcessed(TestSuiteDTO suiteData, Client restClient,String[] mlProfileNames) throws Exception{
		List<String> mlProfileNamesActual = getMLProfileNames(suiteData, restClient);		
		List<String> mlProfileNamesExpected = Arrays.asList(mlProfileNames);
		Map<String,String> mlProfileNameValuesActual = getMLProfileNameValues(suiteData, restClient);
		if(this.compareList(mlProfileNamesActual, mlProfileNamesExpected)){
			for(String mlProfileNameExpected:mlProfileNamesExpected){
				if(!mlProfileNameValuesActual.get(mlProfileNameExpected).equalsIgnoreCase("completed")){
					Logger.info(mlProfileNameExpected+"="+mlProfileNameValuesActual.get(mlProfileNameExpected));
				}

			}	
		}
	}

	public String[] appendArray(String[] fileNameNoExtn, String text) {
		String[] aArray = new String[fileNameNoExtn.length];
		for(int i=0;i<fileNameNoExtn.length;i++){
			aArray[i] = text.concat(fileNameNoExtn[i]);
		}
		return aArray;
	}



	public String[] readFiles(String folderPath) {
		String[] fileName = getFileName(folderPath);

		String[] body = new String[fileName.length];
		for(int i=0;i<fileName.length;i++){
			body[i] = readFileContent(folderPath + File.separator + fileName[i]);
		}

		return body;
	}

	public String readFileContent(String filePath){
		String body="";

		StringBuffer stringBuffer = new StringBuffer();
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filePath), "UTF8"));

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				stringBuffer.append(sCurrentLine);
			}
			br.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		body = stringBuffer.toString().replaceAll("�", "");

		System.out.println("##############File content:"+body);
		return body;
	}

	public void deleteAllPolicies(Client restClient, TestSuiteDTO suiteData){
		try {
			suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));
			String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("policyList"));
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
			HttpResponse policyListResponse = restClient.doGet(dataUri, getCookieHeaders(suiteData));
			String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
			String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
			JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
				name = name.substring(1, name.length()-1);
				deletePolicy(restClient, name, getCookieHeaders(suiteData), suiteData);
			}
		} catch (Exception e) {
		}		
	}

	public void deletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, 
			TestSuiteDTO suiteData)  throws Exception{
		Logger.info("Delete Policy - " + policyName);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		HttpResponse deleteResponse = this.deletePolicy(restClient, policyDetails.get(DCIConstants.POLICY_TYPE), policyDetails.get(DCIConstants.POLICY_SUB_ID), requestHeader, suiteData);
		String policyDeleteResponseBody = ClientUtil.getResponseBody(deleteResponse);
		String policyDeleteStatus = ClientUtil.getJSONValue(policyDeleteResponseBody, DCIConstants.ACTION_STATUS);
		policyDeleteStatus = policyDeleteStatus.substring(1, policyDeleteStatus.length() - 1);
		Assert.assertEquals(policyDeleteStatus, DCIConstants.SUCCESS);
		Logger.info("Policy Deleted - " + policyName);
	}

	public HttpResponse deletePolicy(Client restClient, String policyType, String policySubId, 
			List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("deletePolicy"));
		String entityString = "{\"policy_type\":\""+policyType+"\",\"action\":true,\"sub_id\":\""+policySubId+"\"}";
		Logger.info(entityString);
		StringEntity entity = new StringEntity(entityString);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}

	public Map<String, String> getPolicyDetailsByName(Client restClient, String policyName, 
			List<NameValuePair> requestHeader, TestSuiteDTO suiteData) throws Exception{
		Map<String, String> policyDetails = new HashMap<String, String>();
		String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("policyList"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			if(name.equalsIgnoreCase(policyName)){
				String policyType = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_type");
				policyType = policyType.substring(1, policyType.length()-1);
				String policyId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "id");
				policyId = policyId.substring(1, policyId.length()-1);
				String policySubId = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "sub_id");
				policySubId = policySubId.substring(1, policySubId.length()-1);
				String policyStatus = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "is_active");
				String createdBy = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "created_by");
				createdBy = createdBy.substring(1, createdBy.length()-1);
				String modifiedBy = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "modified_by");
				modifiedBy = modifiedBy.substring(1, modifiedBy.length()-1);
				policyDetails.put(DCIConstants.POLICY_TYPE, policyType);
				policyDetails.put(DCIConstants.POLICY_ID, policyId);
				policyDetails.put(DCIConstants.POLICY_SUB_ID, policySubId);
				policyDetails.put(DCIConstants.ACTION_STATUS, policyStatus);
				policyDetails.put(DCIConstants.CREATED_BY, createdBy);
				policyDetails.put(DCIConstants.MODIFIED_BY, modifiedBy);
				//policy_details
				String policyDetail = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_details");
				if(policyDetail.toString().contains("exposure_scope")){
					String exposureType = ClientUtil.getJSONValue(policyDetail.toString(), "exposure_scope");
					JSONArray fileArray = (JSONArray) new JSONTokener(exposureType).nextValue();
					if(fileArray.size()>0){
						exposureType = fileArray.get(0).toString();
						policyDetails.put(DCIConstants.EXPOSURE_TYPE, exposureType);
					}
				}

				break;
			}
		}
		return policyDetails;
	}

	public String replaceGenericParams(TestSuiteDTO suiteData, String url){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}



	public List<String> getAllDictionaryNames(String responseJson) {
		List<String> dictionaryNames = new ArrayList<String>();
		Dictionary dictionary = unmarshall(responseJson, 
				Dictionary.class);
		List<com.elastica.beatle.dci.dto.dictionary.Object> objects = dictionary.getObjects();
		for(com.elastica.beatle.dci.dto.dictionary.Object object:objects){
			dictionaryNames.add(object.getName());
		}

		return dictionaryNames;
	}


	public void createCIQProfileWithFileFormats(Client restClient,TestSuiteDTO suiteData) throws Exception{
		String ciqProfileName="CIQ File Format Name";
		String ciqProfileDescription="CIQ File Format Description";

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with all file formats in progress");

		String[] fileFormats={"ANIMATION","DATABASE","DESKTOPPUBLSH","ENCAPSULATION","EXECUTABLE",
				"FAXFORMAT","FONT","MISC","MIXED","MOVIE","PRESENTATION","RASTERIMAGE",
				"SOUND","SPREADSHEET","UNKNOWN","VECTORGRAPHIC","WORDPROCESSOR"};

		List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
		List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
		List<String> valuesRiskTypes=null;List<String> valuesContentTypes=null;
		List<String> valuesMLProfile=null;
		List<String> valuesFileFormat=new ArrayList<String>();
		for(String fileFormat:fileFormats){
			valuesFileFormat.add("class:"+fileFormat);
		}

		createCIQProfile(suiteData, restClient, ciqProfileName, ciqProfileDescription, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms, 
				valuesRiskTypes, valuesContentTypes, valuesFileFormat,valuesMLProfile,
				"high", 0, true, 1, false);

		Logger.info("Creating CIQ profile with name:"+ciqProfileName+" with all file formats is completed");
	}

	public void createTrainingProfile(Client restClient, TestSuiteDTO suiteData, String mlProfileName) throws Exception{
		List<NameValuePair> headers = getCookieHeaders(suiteData);

		Logger.info("Creating ML profile name:"+mlProfileName+" in progress");

		createNewMLProfile(suiteData, restClient, mlProfileName, mlProfileName+" Sample Description");

		String filePathPS = DCIConstants.DCI_TP_UPLOAD_PATH + File.separator + mlProfileName + File.separator + "Positive";
		String filePathNS = DCIConstants.DCI_TP_UPLOAD_PATH + File.separator + mlProfileName + File.separator + "Negative";

		String[] trainingFilesPositive = getFileName(filePathPS);
		String[] trainingFilesNegative = getFileName(filePathNS);

		for(String fileName: trainingFilesPositive){
			uploadFileIntoTrainingProfile(suiteData, restClient, headers, mlProfileName,fileName, 
					"PS", filePathPS+ File.separator +fileName);
		}
		for(String fileName: trainingFilesNegative){
			uploadFileIntoTrainingProfile(suiteData, restClient, headers, mlProfileName,fileName, 
					"NS", filePathNS+ File.separator +fileName);
		}

		processMLProfile(suiteData, restClient, mlProfileName);

		Logger.info("Creating ML profile name:"+mlProfileName+" is completed");
	}

	public List<String> getAllTrainingProfileNames(String reponseJson) {
		List<String> trainingProfileNames = new ArrayList<String>();

		TrainingProfile tp = unmarshall(reponseJson, 
				TrainingProfile.class);
		List<com.elastica.beatle.dci.dto.tp.Object> objects = tp.getData().getObjects();
		for(com.elastica.beatle.dci.dto.tp.Object object:objects){
			trainingProfileNames.add(object.getName());
		}

		return trainingProfileNames;
	}



	public void cleanupAllTenants(Client restClient, TestSuiteDTO suiteData) throws Exception {
		String[] tenants = {
				"dcibeatlecom","dciautobeatlecom","dciciqbeatlecom","dcimultibeatlecom",
				"dcio365beatlecom",/*"securletfeatlecom",*/"securletautofeatlecom","securleto365featlecom",
				"securletautoo365featlecom"
		};

		for(String tenant:tenants){
			suiteData.setTenantName(tenant);
			suiteData.setCloudSocUname("admin");

			Map<String, String> credentialsDBconfig = 
					FileHandlingUtils.readPropertyFile(
							FrameworkConstants.FRAMEWORK_CREDENTIALDB_FILEPATH);

			if(suiteData.getCloudSocUname()!= null && !suiteData.getCloudSocUname().isEmpty()){
				suiteData.setUsername(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat(suiteData.getEnvironmentName()).concat(".").concat(suiteData.getCloudSocUname()).concat(".").concat("userName")));
				suiteData.setPassword(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat(suiteData.getEnvironmentName()).concat(".").concat(suiteData.getCloudSocUname()).concat(".").concat("userPassword"))));
				suiteData.setTenantToken(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat("tenantToken")));
			}	

			HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
			suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
			suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));		
			suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(SecurletsConstants.SECURLETS_API_CONFIGURATION_FILEPATH));

			this.deleteAllPolicies(restClient, suiteData);
			this.deleteAllCIQProfiles(restClient, suiteData);
			this.deleteAllDictionaries(restClient, suiteData);
			this.deleteAllTrainingProfiles(restClient, suiteData);
		}




	}



	public int getCIQProfileCount(Client restClient, TestSuiteDTO suiteData) {
		try{
			HttpResponse response = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			CIQProfile ciqProfile = unmarshall(ClientUtil.getResponseBody(response), 
					CIQProfile.class);
			return ciqProfile.getData().getMeta().getTotalCount();
		}catch(Exception e){
			return 0;
		}
	}

	public int getDictionaryCount(Client restClient, TestSuiteDTO suiteData) {
		try{
			HttpResponse response = listDictionary(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			Dictionary dictionary = unmarshall(ClientUtil.getResponseBody(response), 
					Dictionary.class);
			return dictionary.getMeta().getTotalCount();
		}catch(Exception e){
			return 0;
		}
	}

	public int getTrainingProfileCount(Client restClient, TestSuiteDTO suiteData) {
		try{
			HttpResponse response = listTrainingProfiles(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			TrainingProfile trainingProfile = unmarshall(ClientUtil.getResponseBody(response), 
					TrainingProfile.class);
			return trainingProfile.getData().getObjects().size();
		}catch(Exception e){
			return 0;
		}
	}

	public List<String> getAllCIQProfileNames(String responseJson) {
		List<String> ciqProfileNames = new ArrayList<String>();
		CIQProfile ciqProfile = unmarshall(responseJson, 
				CIQProfile.class);
		List<com.elastica.beatle.dci.dto.ciq.Object> objects = ciqProfile.getData().getObjects();

		for(com.elastica.beatle.dci.dto.ciq.Object object:objects){
			ciqProfileNames.add(object.getProfileName());
		}

		return ciqProfileNames;
	}

	public HttpResponse createContentIqProfile(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String ciqProfileName, String entity){
		Logger.info("Creating of CIQ profile with profileName:"+ciqProfileName+" in progress");
		HttpResponse createResponse = null;
		String restAPI = "/controls/add_ciq_profile";
		Logger.info("Request Json:"+entity);
		try{	
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);	
			createResponse = restClient.doPost(dataUri, headers, null, new StringEntity(entity));
		}catch(Exception e){
			Logger.info("Creating of CIQ profile with profileName:"+ciqProfileName+" is failed");
			return null;
		}
		//Logger.info("ContentIQ Create Response:"+ClientUtil.getResponseBody(createResponse));

		Logger.info("Creating of CIQ profile with profileName:"+ciqProfileName+" is completed");
		return createResponse;
	}


	public HttpResponse deleteContentIQProfile(Client restClient, TestSuiteDTO suiteData, List<NameValuePair> headers, String ciqProfileName, String id) {
		Logger.info("Deleting of CIQ profile with profileName:"+ciqProfileName+" in progress");
		HttpResponse deleteResponse = null;
		String restAPI = "/controls/add_ciq_profile";


		try{	
			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);	
			deleteResponse = restClient.doPost(dataUri, headers, null, entity);
		}catch(Exception e){
			Logger.info("Deleting of CIQ profile with profileName:"+ciqProfileName+" is failed");
			return null;
		}
		//Logger.info("ContentIQ Delete Response:"+ClientUtil.getResponseBody(deleteResponse));

		Logger.info("Deleting of CIQ profile with profileName:"+ciqProfileName+" is completed");
		return deleteResponse;	
	}



	public HttpResponse clearNotificationsList(Client restClient, TestSuiteDTO suiteData) {
		Logger.info("Notification Clearing is in progress");

		HttpResponse notificationResponse = null;
		String restAPI = "/admin/notification/seen";
		try{	
			StringEntity entity = new StringEntity("{\"id\":\"all\"}");
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
			notificationResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
			if(notificationResponse.getStatusLine().getStatusCode()==200){}else{
				waitForSeconds(1);
				notificationResponse = restClient.doPost(dataUri, getCookieHeaders(suiteData), null, entity);
			}
		}catch(Exception e){
			Logger.info("Notification clearing failed");
		}

		Logger.info("Notification clearing is completed");

		return notificationResponse;	
	}

	public HttpResponse getNotificationsList(Client restClient, TestSuiteDTO suiteData){

		Logger.info("Getting Notification List is in progress");

		HttpResponse notificationResponse = null;
		String restAPI = "/admin/notification/list";
		try{	
			URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), restAPI);
			notificationResponse = restClient.doGet(dataUri, getCookieHeaders(suiteData));
			if(notificationResponse.getStatusLine().getStatusCode()==200){}else{
				waitForSeconds(1);
				notificationResponse = restClient.doGet(dataUri, getCookieHeaders(suiteData));
			}
		}catch(Exception e){
			Logger.info("Getting Notification List failed");
		}

		Logger.info("Getting Notification List is completed");

		return notificationResponse;
	}

	public int getNotificationsUnseen(String notificationResponseBody) {
		int count=0;
		Notification notification = unmarshall(notificationResponseBody, Notification.class);

		for(int i=0;i<notification.getMeta().getTotalCount();i++){
			boolean seen = notification.getObjects().get(i).getSeen();
			if(!seen) {
				count = count+1;
			}
		}
		return count;
	}

	public int getNotificationsSeen(String notificationResponseBody) {
		int count=0;
		Notification notification = unmarshall(notificationResponseBody, Notification.class);

		for(int i=0;i<notification.getMeta().getTotalCount();i++){
			boolean  seen = notification.getObjects().get(i).getSeen();
			if(seen) {
				count = count+1;
			}
		}
		return count;
	}



	public String verifyNotificationResponse(TestSuiteDTO suiteData, String notificationResponseBody) {
		String validationMessage="";
		Notification notification = unmarshall(notificationResponseBody, Notification.class);

		int i=0;
		for(com.elastica.beatle.dci.dto.notification.Object object:notification.getObjects()){
			String action = object.getAction();
			String id = object.getId();
			String subject = object.getSubject();
			String email = object.getEmail();
			String createdOn = object.getCreatedOn();
			String details = object.getDetails();
			String url = object.getUrl();
			boolean seen = object.getSeen();


			validationMessage +=(action.equalsIgnoreCase("DETAILS")) ? "" : 
				"For Notification#"+(i+1)+" action field is mismatching Expected:DETAILS and Actual:"+action+"\n";
			validationMessage +=(StringUtils.isNotBlank(id)) ? "" : 
				"For Notification#"+(i+1)+" id field is mismatching Expected:Some value and Actual:"+id+"\n";
			validationMessage +=(subject.equalsIgnoreCase("ContentIQ Configuration Changed")) ? "" : 
				"For Notification#"+(i+1)+" subject field is mismatching Expected:ContentIQ Configuration Changed and Actual:"+subject+"\n";
			validationMessage +=(email.equalsIgnoreCase(suiteData.getUsername())) ? "" : 
				"For Notification#"+(i+1)+" email field is mismatching Expected:"+suiteData.getUsername()+" and Actual:"+email+"\n";
			validationMessage +=(StringUtils.isNotBlank(createdOn)) ? "" : 
				"For Notification#"+(i+1)+" created_on field is mismatching Expected:Some value and Actual:"+createdOn+"\n";
			validationMessage +=(details.equalsIgnoreCase("<p>Configuration modified since the last full Securlet content inspection scan.<br>Use the 'Scan Now' option in the Securlets.</p>")) ? "" : 
				"For Notification#"+(i+1)+" details field is mismatching Expected:"
				+ "<p>Configuration modified since the last full Securlet content inspection scan.<br>Use the 'Scan Now' option in the Securlets.</p>"
				+ " and Actual:"+details+"\n";
			validationMessage +=(StringUtils.isBlank(url)) ? "" : 
				"For Notification#"+(i+1)+" url field is mismatching Expected:empty value and Actual:"+url+"\n";
			validationMessage +=(!seen||seen) ? "" : 
				"For Notification#"+(i+1)+" seen field is mismatching Expected:true/false and Actual:"+seen+"\n";

			i++;
		}


		return validationMessage;
	}

	
	public List<String> getKeysCustomDictionaries() {
		List<String> ciqCustomDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ciqCustomDictionaries.add(key);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}

	public List<String> getValuesCustomDictionaries() {
		List<String> ciqCustomDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqCustomDictionaries.add(value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}


	public Map<String,String> getKeyValueCustomDictionaries() {
		Map<String,String> ciqCustomDictionaries = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqCustomDictionaries.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}
	
	public List<String> getCIQKeysCustomDictionaries() {
		List<String> ciqCustomDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				ciqCustomDictionaries.add(key);
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}

	public List<String> getCIQValuesCustomDictionaries() {
		List<String> ciqCustomDictionaries = new ArrayList<String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqCustomDictionaries.add(value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}


	public Map<String,String> getCIQKeyValueCustomDictionaries() {
		Map<String,String> ciqCustomDictionaries = new HashMap<String,String>();
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = "dci/risks/ciqProfilesCustomDictionaries.properties";
			input = getClass().getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				Logger.info("Sorry, unable to find " + filename);
			}

			prop.load(input);

			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {

				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				ciqCustomDictionaries.put(key, value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ciqCustomDictionaries;
	}

	public String[] customDictionaryNames() {
		String[] array = null;
		try {
			List<String> keys = getKeysCustomDictionaries();
			array = new String[keys.size()];

			for(int i=0;i<keys.size();i++){
				array[i] = keys.get(i);
			}

		} catch (Exception ex) {

		}
		return array;
	}


	public String[] customDictionaryKeywords() {
		String[] array = null;
		try {
			List<String> values = getValuesCustomDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0];
			}

		} catch (Exception ex) {

		}
		return array;
	}
	
	public String[] customDictionaryFiles() {
		String[] array = null;
		try {
			List<String> values = getValuesCustomDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[1];
			}

		} catch (Exception ex) {

		}
		return array;
	}
	
	public String[] ciqCustomProfileNames() {
		String[] array = null;
		try {
			List<String> keys = getCIQKeysCustomDictionaries();
			array = new String[keys.size()];

			for(int i=0;i<keys.size();i++){
				array[i] = keys.get(i);
			}

		} catch (Exception ex) {

		}
		return array;
	}


	public String[] ciqCustomDictionaryNames() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesCustomDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[0];
			}

		} catch (Exception ex) {

		}
		return array;
	}
	
	public String[] ciqCustomDictionaryFileNames() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesCustomDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[1];
			}

		} catch (Exception ex) {

		}
		return array;
	}
	
	public String[] ciqCustomDictionaryCounts() {
		String[] array = null;
		try {
			List<String> values = getCIQValuesCustomDictionaries();
			array = new String[values.size()];

			for(int i=0;i<values.size();i++){
				String[] temp = values.get(i).split(":");
				array[i] = temp[2];
			}

		} catch (Exception ex) {

		}
		return array;
	}
	
	public String[] getKeywordsFromFile(FileInputStream fileInput) {
		List<String> fileNames = new ArrayList<String>();

		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(fileInput, "UTF8"));
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				fileNames.add(sCurrentLine);
			}
			br.close();


		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNames.toArray(new String[fileNames.size()]);
	}

}