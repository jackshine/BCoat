/**
 * 
 */

package com.elastica.action.dci;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
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
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.backend.BEAction;
import com.elastica.action.backend.enums.CIQType;
import com.elastica.common.SuiteData;
import com.elastica.constants.dci.DCIConstants;
import com.elastica.dci.dto.Expression;
import com.elastica.dci.dto.HIPAA;
import com.elastica.dci.dto.Keyword;
import com.elastica.dci.dto.PCI;
import com.elastica.dci.dto.PII;
import com.elastica.dci.dto.Value;
import com.elastica.gateway.GWProtectFunctions;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.PolicyAccessEnforcement;
import com.elastica.gateway.ProtectFunctions;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;

/**
 * DCI Actions
 * @author eldorajan
 *
 */
public class DCIAction extends BEAction{

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
	public String getSearchQueryForDCI(String tsFrom, String tsTo, String facility,
			String sourceType, String severity, String query, String sourceName, SuiteData suiteData) throws Exception {


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
				endObject().
				startObject().
				startObject("term").
				field("__source", sourceType).
				endObject().
				endObject().
				startObject().
				startObject("term").
				field("Activity_type", "Content Inspection").
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
				field("apiServerUrl", "https://"+suiteData.getApiServer()+"/").
				field("csrftoken", suiteData.getCsrfToken()).
				field("sessionid", suiteData.getSessionID()).
				field("userid", suiteData.getUsername()).
				endObject(); 
		return builder.string();
	}

	public String getSearchQueryForDCI(String tsFrom, String tsTo, String facility,
			String sourceType, String query, String sourceName,
			SuiteData suiteData) throws Exception {
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
				field("Activity_type", "Content Inspection").
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
				field("apiServerUrl", "https://"+suiteData.getApiServer()+"/").
				field("csrftoken", suiteData.getCsrfToken()).
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



	/**
	 * Cleanup temp folder
	 */
	public void cleanupTempFolder() {
		try {
			File dir = new File(DCIConstants.DCI_FILE_TEMP_PATH);
			if (dir.exists()) {
				if (dir.isDirectory()) {
					if (dir.listFiles().length == 0) {
						// dir.delete();
					} else {
						for (File f : dir.listFiles()){
							f.delete();
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String contentTypesExpected = null;



					if(message.contains("Mail With Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Body has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has been classified but was " + message;
						contentTypesExpected = message.replace("File " + fileName + " Mail With Body has been classified - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Attachment and Body has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has been classified but was " + message;
						contentTypesExpected = message.replace("File " + fileName + " Mail With Attachment and Body has been classified - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
						contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
								"");
					}


					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (contentTypes != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, contentTypes);
					}
					if (docClass != null) {
						validationMessage += validationContentCheckDocClassLogs(sourceJson, docClass);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (contentTypes != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, contentTypes);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (contentTypes != null) {
						for (String c : contentTypes) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (contentTypes != null) {
						validationMessage += validationContentCheckContentTypeLog(sourceJson, contentTypes);
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

			validationMessage += (!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage += (!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage += (!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage += (!score.isEmpty()) ? ""
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (activityType.contains("Content Inspection")) ? ""
							: "Activity Type is not matching Expected:Content Inspection but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage += (message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksExpected = risksExpected.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}
					if (docClass != null) {
						validationMessage += validationContentCheckDocClassLogs(sourceJson, docClass);
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

			validationMessage += (!index.isEmpty()) ? ""
					: "index is not matching Expected:some value but was " + index;
			validationMessage += (!type.isEmpty()) ? ""
					: "type is not matching Expected:some value but was " + type;
			validationMessage += (!id.isEmpty()) ? ""
					: "id is not matching Expected:some value but was " + id;
			validationMessage += (!score.isEmpty()) ? ""
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (activityType.contains("Content Inspection")) ? ""
							: "Activity Type is not matching Expected:Content Inspection but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Email message")){
						validationMessage += (message.contains("Email message " + fileName + " Mail With Body has risk(s) - ")||
								message.contains("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
										: "message is not matching Expected:Email message " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("Email message " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
						risksExpected = risksExpected.replace("Email message " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else if(message.contains("Mail With Attachment and Body")){
						validationMessage += (message.contains("File " + fileName + " Mail With Attachment and Body has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " Mail With Body has risk(s) but was " + message;
						risksExpected = message.replace("File " + fileName + " Mail With Attachment and Body has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
								: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message; 
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}


					if (risks != null) {
						for (String r : risks) {

							validationMessage += (risksExpected.contains(r)) ? ""
									: "message does not contain risk type Expected:" + r + " but was"
									+ risksExpected;

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckRiskLogs(sourceJson, risks, risksExpected, ciqValues);
					}
					if (docClass != null) {
						validationMessage += validationContentCheckDocClassLogs(sourceJson, docClass);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (activityType.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (activityType.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + activityType;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, risks);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationPCIPIIRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String pciJson, String piiJson) {
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;

					//String file = fileName.replaceFirst("[0-9]*_", "");
					if (risksExpected.contains("PCI")) {
						validationMessage += validationContentCheckPCIRiskLogs(sourceJson, risks, risksExpected, pciJson);
					}
					if (risksExpected.contains("PII")) {
						validationMessage += validationContentCheckPIIRiskLogs(sourceJson, risks, risksExpected, piiJson);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null && pciJson != null) {
						validationMessage += validationContentCheckPCIRiskLogs(sourceJson, risks, risksExpected, pciJson);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckPIIRiskLogs(sourceJson, risks, risksExpected, piiJson);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationHIPAARiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String hipaaJson) {
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckHIPAARiskLogs(sourceJson, risks, risksExpected, hipaaJson);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, risks);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationGLBARiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String glbaJson) {
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckGLBARiskLogs(sourceJson, risks, risksExpected, glbaJson);
					}
				}
			}
		}
		return validationMessage;
	}

	public String validationVBAMacrosRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String vbaJson) {
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckVBARiskLogs(sourceJson, risks, risksExpected, vbaJson);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckRiskLogs(sourceJson, risks, risksExpected);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, risks);
					}

				}
			}

		}
		return validationMessage;
	}

	public String validationVirusRiskLogs(String hits, String fileName, String userName, String fullName, String saasType,
			String sourceType, String[] risks, String virusJson) {
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckVirusRiskLogs(sourceJson, risks, risksExpected, virusJson);
					}

				}
			}
			if (severity.equalsIgnoreCase("\"informational\"")) {
				if (Activity_type.equalsIgnoreCase("\"Content Inspection\"")) {
					String contentChecksJson = getJSONValue(sourceJson, "content_checks");

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("informational")) ? ""
							: "Severity is not matching informational but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has been classified - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has been classified - but was " + message;
					String contentTypesExpected = message.replace("File " + fileName + " has been classified - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String c : risks) {
							validationMessage += (contentTypesExpected.contains(c)) ? ""
									: "message does not contain content type Expected:" + c + " but was"
									+ contentTypesExpected;
						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;


					if (risks != null) {
						validationMessage += validationContentCheckContentTypeLogs(sourceJson, risks);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")) ? ""
							: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");
					if (risks != null) {
						for (String r : risks) {
							if (r.contains("ContentIQ Violations")) {
								if (risksExpected.contains(r)) {
									validationMessage += (risksExpected.contains(r)) ? ""
											: "message does not contain risk type Expected:" + r + " but was"
											+ risksExpected;

								}
							} else {
								validationMessage += (risksExpected.contains(r)) ? ""
										: "message does not contain risk type Expected:" + r + " but was"
										+ risksExpected;
							}

						}
					}
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;
					// validationMessage+= (_ObjectName.contains(fileName)) ? ""
					// : "ObjectName is not matching Expected:"+fileName+" but
					// was "+_ObjectName;
					validationMessage += (Risks.contains(risksExpected)) ? ""
							: "Risks is not matching Expected:" + risksExpected + " but was " + Risks;
					if (risks != null) {
						validationMessage += validationContentCheckFERPARiskLogs(sourceJson, risks, risksExpected);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
					}
					validationMessage += (Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage += validationContentCheckRiskLogs
							(sourceJson,contentIQProfileName,dictionaries,keywords);
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}

					validationMessage += (Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage += validationContentCheckRiskLogs
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")||
								message.contains(risk)) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
										+ "but was " + message;
						risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}

					validationMessage += (Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage += validationContentCheckRiskLogsHighSensitivity
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;

					String risksExpected="";
					if(message.contains("Email file attachment")){
						validationMessage += (message.contains("Email file attachment " + fileName + " has risk(s) - ")
								||message.contains(risk[i])) ? ""
										: "message is not matching Expected:File " + fileName + " has risk(s) but was " + message;
						risksExpected = message.replace("Email file attachment " + fileName + " has risk(s) - ", "").replaceAll("\"",
								"");
					}else{
						validationMessage += (message.contains("File " + fileName + " has risk(s) - ")||
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

					validationMessage += (!flag.contains("false")) ? ""
							: "message does not contain risk type Expected:"+risk[i]+" but was"+ Risks;
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					validationMessage += validationContentCheckRiskLogs
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

					validationMessage += (!user.isEmpty()) ? ""
							: "Username is not matching Expected:" + userName + " but was " + user;
					validationMessage += (severity.contains("critical")) ? ""
							: "Severity is not matching Expected:critical but was " + severity;
					validationMessage += (facility.contains(saasType)) ? ""
							: "Facility is not matching Expected:" + saasType + " but was " + facility;
					validationMessage += (__source.contains(sourceType)) ? ""
							: "Source is not matching Expected:" + sourceType + " but was " + __source;
					validationMessage += (!created_timestamp.isEmpty()) ? ""
							: "Created time stamp is not present but was " + created_timestamp;
					validationMessage += (!inserted_timestamp.isEmpty()) ? ""
							: "Inserted time stamp is not present but was " + inserted_timestamp;
					validationMessage += (message.contains("File " + fileName + " has risk(s) - ")||
							message.contains(risk)) ? ""
									: "message is not matching Expected:File " + fileName + " has risk(s) - "+risk
									+ "but was " + message;
					validationMessage += (Risks.contains(risk)) ? ""
							: "message does not contain risk type Expected:"+risk+" but was"+ Risks;
					validationMessage += (name.contains(fileName)) ? ""
							: "Filename is not matching Expected:" + fileName + " but was " + name;
					validationMessage += (Activity_type.contains("Content Inspection")) ? ""
							: "Activity type is not matching Expected:Content Inspection but was " + Activity_type;
					validationMessage += (!contentChecksJson.isEmpty()) ? ""
							: "Risk content checks is not present but was " + contentChecksJson;

					String risksExpected = message.replace("File " + fileName + " has risk(s) - ", "").replaceAll("\"",
							"");

					validationMessage += validationContentCheckRiskLogs
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
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}


		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

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

				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage += (vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage += (vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}



		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
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
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}
		String vk_ferpa = "";try {vk_ferpa = getJSONValue(contentJson, "vk_ferpa");} catch (Exception e) {vk_ferpa = "0";}


		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}
		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

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

				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;


				if(!expressionsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

				if(!keywordsJson.isEmpty()){
					JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				}

			}catch(NullPointerException nux){
				validationMessage +="HIPAA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = "";
			try{
				virusJson = getJSONValue(contentJson, "virus");
				String expressionsJson = getJSONValue(virusJson, "expressions");

				try {
					JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
					for (int i = 0; i < jArray.size(); i++) {
						String valuesJson = jArray.getJSONObject(i).get("values").toString();
						String name = jArray.getJSONObject(i).get("name").toString();
						validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

						JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
						for (int j = 0; j < valuesArray.size(); j++) {
							String key = valuesArray.getJSONObject(j).get("key").toString();
							String value = valuesArray.getJSONObject(j).get("value").toString();
							validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
							validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
						}
					}
				} catch (Exception e) {
					String valuesJson = getJSONValue(expressionsJson, "values");
					String name = getJSONValue(expressionsJson, "name");
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="Virus expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = "";
			try{
				vbaJson = getJSONValue(contentJson, "vba_macros");
				String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(vbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="VBA Macros expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = "";
			try{
				glbaJson = getJSONValue(contentJson, "glba");
				String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				String expressionsJson = getJSONValue(glbaJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="GLBA expressions is not present in Content Vulnerabilities detail window";
			}




		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage += (vk_ferpa.contains("1")) ? ""
					: "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage += (vk_ferpa.contains("0")) ? ""
					: "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		//String[] ciqTerms = ciqValues.get("ciqProfileName").split(",");

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (ciqValues.get("ciqProfileName").contains(name)) ? "" : 
					"Expecting CIQ Profile name:"+ciqValues.get("ciqProfileName")+" but was " + name;
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

				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;
				validationMessage += (ciqValues.get("ciqProfileName").contains(key)) ? "" : 
					"Expecting CIQ Profile name:"+ciqValues.get("ciqProfileName")+" but was " + key;

				if(ciqValues.get("ciqType").equalsIgnoreCase("OnlyRisks")){
					Logger.info("No violationsJson to verify for only risks ciq profile:"+violationsJson);
				}
				if(ciqValues.get("ciqType").equalsIgnoreCase("PDT")){
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String actualTerm = violationsArray.getJSONObject(j).get("predefined_term").toString();
						int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

						validationMessage += (actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
							"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
						validationMessage += (actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
							"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;

					}
				}
				if(ciqValues.get("ciqType").equalsIgnoreCase("PDD")){
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String actualTerm = violationsArray.getJSONObject(j).get("dictionary").toString();
						String actualKeyword = violationsArray.getJSONObject(j).get("keyword").toString();
						int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

						validationMessage += (actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
							"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
						validationMessage += (actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
							"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;
						validationMessage += (actualKeyword.contains(ciqValues.get("ciqProfileKeyword"))) ? "" : 
							"Expecting CIQ Profile Term Keyword:"+ciqValues.get("ciqProfileKeyword")+" but was " + actualKeyword;

					}
				}
				if(ciqValues.get("ciqType").equalsIgnoreCase("CT")){
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String actualTerm = violationsArray.getJSONObject(j).get("regex").toString();
						int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

						validationMessage += (actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
							"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
						validationMessage += (actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
							"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;

					}
				}
				if(ciqValues.get("ciqType").equalsIgnoreCase("CD")){
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String actualTerm = violationsArray.getJSONObject(j).get("dictionary").toString();
						String actualKeyword = violationsArray.getJSONObject(j).get("keyword").toString();
						int actualCount = Integer.parseInt(violationsArray.getJSONObject(j).get("count").toString());

						validationMessage += (actualTerm.contains(ciqValues.get("ciqProfileType"))) ? "" : 
							"Expecting CIQ Profile Term Value:"+ciqValues.get("ciqProfileType")+" but was " + actualTerm;
						validationMessage += (actualCount==Integer.parseInt((ciqValues.get("ciqProfileCount")))) ? "" : 
							"Expecting CIQ Profile Term Count:"+ciqValues.get("ciqProfileCount")+" but was " + actualCount;
						validationMessage += (actualKeyword.contains(ciqValues.get("ciqProfileKeyword"))) ? "" : 
							"Expecting CIQ Profile Term Keyword:"+ciqValues.get("ciqProfileKeyword")+" but was " + actualKeyword;

					}
				}


			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}



	public String validationContentCheckContentTypeLogs(String sourceJson, String[] contentTypes) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");



		if (Arrays.asList(contentTypes).contains("audio")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String audioJson = getJSONValue(contentJson, "audio");

			validationMessage += (mimetype.contains("audio/mpeg")) ? "" : "mimetype is not Expecting audio/mpeg but was " + mimetype;

			String reasonsJson = getJSONValue(audioJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("video")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String videoJson = getJSONValue(contentJson, "video");

			validationMessage += (mimetype.contains("video/mp4")) ? "" : "mimetype is not Expecting video/mp4 but was " + mimetype;

			String reasonsJson = getJSONValue(videoJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("image")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String imageJson = getJSONValue(contentJson, "image");

			validationMessage += (mimetype.contains("image/jpeg")) ? "" : "mimetype is not Expecting image/jpeg but was " + mimetype;

			String reasonsJson = getJSONValue(imageJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("executable")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String executableJson = getJSONValue(contentJson, "executable");

			validationMessage += (mimetype.contains("application/x-dosexec")||mimetype.contains("application/mswindows-exe")) ? "" :
				"mimetype is not Expecting application/x-dosexec / application/mswindows-exe but was " + mimetype;

			String reasonsJson = getJSONValue(executableJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("cryptographic_keys")) {
			String cryptographicKeysJson = getJSONValue(contentJson, "cryptographic_keys");

			String expressionsJson = getJSONValue(cryptographicKeysJson, "expressions");
			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}



		return validationMessage;
	}

	public String validationContentCheckContentTypeLog(String sourceJson, String[] contentTypes) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");



		if (Arrays.asList(contentTypes).contains("audio")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String audioJson = getJSONValue(contentJson, "audio");

			validationMessage += (!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(audioJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("video")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String videoJson = getJSONValue(contentJson, "video");

			validationMessage += (!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(videoJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("image")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String imageJson = getJSONValue(contentJson, "image");

			validationMessage += (!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(imageJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("executable")) {
			String mimetype = getJSONValue(contentJson, "mimetype");
			String executableJson = getJSONValue(contentJson, "executable");

			validationMessage += (!mimetype.isEmpty()) ? "" : "mimetype is not Expecting some mimetype but was " + mimetype;

			String reasonsJson = getJSONValue(executableJson, "reasons");
			JSONArray jArray = (JSONArray) new JSONTokener(reasonsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		}

		if (Arrays.asList(contentTypes).contains("cryptographic_keys")) {
			String cryptographicKeysJson = getJSONValue(contentJson, "cryptographic_keys");

			String expressionsJson = getJSONValue(cryptographicKeysJson, "expressions");
			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
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
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}


		}



		return validationMessage;
	}



	public String validationContentCheckPCIRiskLogs(String sourceJson, String[] risks, String risksExpected,
			String jsonPci) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}



		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			try{
				String piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				Logger.info(piiJson);

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (name.contains("Personally Identifiable Information")) ? ""
							: "Expecting Personally Identifiable Information but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}

		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			try{
				String pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				Logger.info("Expected Content Vulnerabilities Json: "+jsonPci);
				Logger.info("Actual Content Vulnerabilities Json: "+pciJson);

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonPci,name,"PCI");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonPci, name, key, value,"PCI");
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");
			String keywordsJson = getJSONValue(hipaaJson, "keywords");
			String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

			jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

			String sourceCodeJson = getJSONValue(contentJson, "source_code");
			String updated_timestamp = getJSONValue(sourceCodeJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckPIIRiskLogs(String sourceJson, String[] risks, String risksExpected, String jsonPii) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			try{
				String piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				Logger.info("Expected Content Vulnerabilities Json: "+jsonPii);
				Logger.info("Actual Content Vulnerabilities Json: "+piiJson);

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonPii,name,"PII");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonPii, name, key, value,"PII");
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			try{
				String pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}


		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson="";try {expressionsJson = getJSONValue(hipaaJson, "expressions");} catch (Exception e) {}
			String keywordsJson="";try {keywordsJson = getJSONValue(hipaaJson, "keywords");} catch (Exception e) {}
			String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;

			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}

			if(!keywordsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}

		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckHIPAARiskLogs(String sourceJson, String[] risks, String risksExpected, String jsonHipaa) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			try{
				String piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				} 
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			try{  
				String pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}


			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}

		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}
		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

			String hipaaJson = getJSONValue(contentJson, "hipaa");

			String expressionsJson="";
			try {
				expressionsJson = getJSONValue(hipaaJson, "expressions");
			} catch (Exception e) {}
			String keywordsJson="";
			try {
				keywordsJson = getJSONValue(hipaaJson, "keywords");
			} catch (Exception e) {}

			String updated_timestamp = getJSONValue(hipaaJson, "updated_timestamp");

			Logger.info("Expected Content Vulnerabilities Json: "+jsonHipaa);
			Logger.info("Actual Content Vulnerabilities Json: "+hipaaJson);

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;


			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonHipaa,name,"HIPAA","expressions");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonHipaa, name, key, value, "HIPAA", "expressions");
					}
				}
			}

			if(!keywordsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(keywordsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonHipaa,name,"HIPAA","keywords");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonHipaa, name, key, value, "HIPAA", "keywords");
					}
				}
			}


		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

			String sourceCodeJson = getJSONValue(contentJson, "source_code");
			String updated_timestamp = getJSONValue(sourceCodeJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckGLBARiskLogs(String sourceJson, String[] risks, String risksExpected, String jsonGlba) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");

			String expressionsJson="";
			try {
				expressionsJson = getJSONValue(glbaJson, "expressions");
			} catch (Exception e) {}
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");

			Logger.info("Expected Content Vulnerabilities Json: "+jsonGlba);
			Logger.info("Actual Content Vulnerabilities Json: "+glbaJson);

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;


			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonGlba,name,"GLBA","expressions");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonGlba, name, key, value, "GLBA", "expressions");
					}
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

			String sourceCodeJson = getJSONValue(contentJson, "source_code");
			String updated_timestamp = getJSONValue(sourceCodeJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}



		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckVBARiskLogs(String sourceJson, String[] risks, String risksExpected, String jsonVba) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;

			String piiJson = "";
			try{
				piiJson = getJSONValue(contentJson, "pii");
				String expressionsJson = getJSONValue(piiJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PII expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;

			String pciJson = "";
			try{
				pciJson = getJSONValue(contentJson, "pci");
				String expressionsJson = getJSONValue(pciJson, "expressions");

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			}catch(NullPointerException nux){
				validationMessage +="PCI expressions is not present in Content Vulnerabilities detail window";
			}



		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}


		if (Arrays.asList(risks).contains("VBA Macros")) {

			int vba = Integer.parseInt(vk_vba_macros.replaceAll("\"", ""));
			validationMessage += (vba>0) ? ""
					: "vk_vba_macros is not Expecting greater than zero but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");

			String expressionsJson="";
			try {
				expressionsJson = getJSONValue(vbaJson, "expressions");
			} catch (Exception e) {}
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");

			Logger.info("Expected Content Vulnerabilities Json: "+jsonVba);
			Logger.info("Actual Content Vulnerabilities Json: "+vbaJson);

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;


			if(!expressionsJson.isEmpty()){
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonVba,name,"VBA MACROS","expressions");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonVba, name, key, value, "VBA MACROS", "expressions");
					}
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}





		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}


	public String validationContentCheckVirusRiskLogs(String sourceJson, String[] risks, String risksExpected, String jsonVirus) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_vba_macros = "";try {vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");} catch (Exception e) {vk_vba_macros = "0";}
		String vk_glba = "";try {vk_glba = getJSONValue(contentJson, "vk_glba");} catch (Exception e) {vk_glba = "0";}
		String violations = "";try {violations = getJSONValue(contentJson, "violations");} catch (Exception e) {violations = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");

			Logger.info("Expected Content Vulnerabilities Json: "+jsonVirus);
			Logger.info("Actual Content Vulnerabilities Json: "+virusJson);

			if(virusJson.contains("\"expressions\":{\"values\":")){
				virusJson = virusJson.replace("\"expressions\":{\"values\":", "\"expressions\":[{\"values\":");
				virusJson = virusJson.replace("\"name\":\"virus\"}}", "\"name\":\"virus\"}]}");
			}

			String expressionsJson="";
			try {
				expressionsJson = getJSONValue(virusJson, "expressions");
			} catch (Exception e) {}
			String updated_timestamp = getJSONValue(virusJson, "updated_timestamp");

			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;

			if(!expressionsJson.isEmpty()){

				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();

					validationMessage += verifyNameExistsJson(jsonVirus,name,"virus","expressions");

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();

						validationMessage += verifyKeyValueExistsJson(jsonVirus, name, key, value, "virus", "expressions");
					}
				}
			}



		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

			String sourceCodeJson = getJSONValue(contentJson, "source_code");
			String updated_timestamp = getJSONValue(sourceCodeJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
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
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_content_iq_violations = "";try {vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");} catch (Exception e) {vk_content_iq_violations = "0";}

		if (Arrays.asList(risks).contains("FERPA")) {
			validationMessage += (vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		} else {
			validationMessage += (vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		if (Arrays.asList(risks).contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
			String piiJson = getJSONValue(contentJson, "pii");
			String expressionsJson = getJSONValue(piiJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();

					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (Arrays.asList(risks).contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
			String pciJson = getJSONValue(contentJson, "pci");
			String expressionsJson = getJSONValue(pciJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (Arrays.asList(risks).contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_pci;
			String hipaaJson = getJSONValue(contentJson, "hipaa");
			String expressionsJson = getJSONValue(hipaaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}
		} else {
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}

		if (Arrays.asList(risks).contains("Virus / Malware")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;

			String virusJson = getJSONValue(contentJson, "virus");
			String expressionsJson = getJSONValue(virusJson, "expressions");

			try {
				JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
				for (int i = 0; i < jArray.size(); i++) {
					String valuesJson = jArray.getJSONObject(i).get("values").toString();
					String name = jArray.getJSONObject(i).get("name").toString();
					validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

					JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
					for (int j = 0; j < valuesArray.size(); j++) {
						String key = valuesArray.getJSONObject(j).get("key").toString();
						String value = valuesArray.getJSONObject(j).get("value").toString();
						validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
						validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
					}
				}
			} catch (Exception e) {
				String valuesJson = getJSONValue(expressionsJson, "values");
				String name = getJSONValue(expressionsJson, "name");
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (Arrays.asList(risks).contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? ""
					: "vk_encryption is not Expecting 1 but was " + vk_encryption;
		} else {
			validationMessage += (vk_encryption.contains("0")) ? ""
					: "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (Arrays.asList(risks).contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? ""
					: "vk_source_code is not Expecting 1 but was " + vk_source_code;

			String sourceCodeJson = getJSONValue(contentJson, "source_code");
			String updated_timestamp = getJSONValue(sourceCodeJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
		} else {
			validationMessage += (vk_source_code.contains("0")) ? ""
					: "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (Arrays.asList(risks).contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1") || vk_vba_macros.contains("2")) ? ""
					: "vk_vba_macros is not Expecting 1/2 but was " + vk_vba_macros;

			String vbaJson = getJSONValue(contentJson, "vba_macros");
			String updated_timestamp = getJSONValue(vbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(vbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_vba_macros.contains("0")) ? ""
					: "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (Arrays.asList(risks).contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;

			String glbaJson = getJSONValue(contentJson, "glba");
			String updated_timestamp = getJSONValue(glbaJson, "updated_timestamp");
			validationMessage += (!updated_timestamp.isEmpty()) ? ""
					: "Expecting some updated_timestamp but was " + updated_timestamp;
			String expressionsJson = getJSONValue(glbaJson, "expressions");

			JSONArray jArray = (JSONArray) new JSONTokener(expressionsJson).nextValue();
			for (int i = 0; i < jArray.size(); i++) {
				String valuesJson = jArray.getJSONObject(i).get("values").toString();
				String name = jArray.getJSONObject(i).get("name").toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;

				JSONArray valuesArray = (JSONArray) new JSONTokener(valuesJson).nextValue();
				for (int j = 0; j < valuesArray.size(); j++) {
					String key = valuesArray.getJSONObject(j).get("key").toString();
					String value = valuesArray.getJSONObject(j).get("value").toString();
					validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
					validationMessage += (!value.isEmpty()) ? "" : "Expecting some value but was " + value;
				}
			}

		} else {
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				validationMessage += (!name.isEmpty()) ? "" : "Expecting some name but was " + name;
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

				validationMessage += (!key.isEmpty()) ? "" : "Expecting some key but was " + key;
				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp;

				JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
				for (int j = 0; j < violationsArray.size(); j++) {
					validationMessage += (!violationsArray.get(j).toString().isEmpty()) ? ""
							: "Expecting some violations but was " + violationsArray.get(j).toString();
				}
			}

		}else if (!Arrays.asList(risks).contains("ContentIQ Violations")&&risksExpected.contains("ContentIQ Violations")) {

		}else {
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			validationMessage += (cArray.size() == 0) ? "" : "Expecting carray as 0 but was " + cArray.size();
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}


	private String verifyNameExistsJson(String json, String name, String type) {
		String validationMessage = "";
		List<String> nameList = new ArrayList<String>();

		if(type.equalsIgnoreCase("PCI")){
			PCI pci = unmarshall(json, PCI.class);
			List<Expression> expressions = pci.getExpressions();
			for(Expression e:expressions){
				nameList.add(e.getName());
			}
			validationMessage += (nameList.contains(name)) ? ""
					: "Name expected in PCI is not seen Expected:"+name+" but was "+nameList.toString();

		}else if(type.equalsIgnoreCase("PII")){
			PII pii = unmarshall(json, PII.class);
			List<Expression> expressions = pii.getExpressions();
			for(Expression e:expressions){
				nameList.add(e.getName());
			}
			validationMessage += (nameList.contains(name)) ? ""
					: "Name expected in PII is not seen Expected:"+name+" but was "+nameList.toString();

		}


		return validationMessage;
	}


	private String verifyKeyValueExistsJson(String json, String name, String key, String value, String type) {
		String validationMessage = "";

		if(type.equalsIgnoreCase("PCI")){
			PCI pci = unmarshall(json, PCI.class);
			List<Expression> expressions = pci.getExpressions();
			List<String> keyList = new ArrayList<String>();
			List<Integer> valueList = new ArrayList<Integer>();
			for(Expression e:expressions){
				if(e.getName().contains(name)){
					List<Value> values = e.getValues();
					for(Value v:values){
						keyList.add(v.getKey());
						valueList.add(v.getValue());
					}
				}else{
					validationMessage += "Name "+name+" is not present under PCI";	
				}
			}

			if(key.contains(",")){
				String[] keyTemp = key.split(",");
				validationMessage += (keyTemp.length>0) ? "" : "Expecting some key for PCI but was " + key;
			}else{
				validationMessage += (keyList.contains(key)) ? ""
						: "Key expected in PCI is not seen Expected:"+key+" but was "+keyList.toString();

			}

			validationMessage += (valueList.toString().contains(value)) ? ""
					: "Value expected in PCI is not seen Expected:"+valueList.toString()+" but was "+value;

		}else if(type.equalsIgnoreCase("PII")){
			PII pii = unmarshall(json, PII.class);
			List<Expression> expressions = pii.getExpressions();
			List<String> keyList = new ArrayList<String>();
			List<Integer> valueList = new ArrayList<Integer>();
			for(Expression e:expressions){
				if(e.getName().contains(name)){
					List<Value> values = e.getValues();
					for(Value v:values){
						keyList.add(v.getKey());
						valueList.add(v.getValue());
					}
				}else{
					validationMessage += "Name "+name+" is not present under PII";	
				}
			}

			if(key.contains(",")){
				String[] keyTemp = key.split(",");
				validationMessage += (keyTemp.length>0) ? "" : "Expecting some key for PII but was " + key;
			}else{
				validationMessage += (keyList.contains(key)) ? ""
						: "Key expected in PII is not seen Expected:"+key+" but was "+keyList.toString();

			}

			validationMessage += (valueList.toString().contains(value)) ? ""
					: "Value expected in PII is not seen Expected:"+valueList.toString()+" but was "+value;

		}


		return validationMessage;
	}

	private String verifyNameExistsJson(String json, String name, String riskType, String type) {
		String validationMessage = "";
		List<String> nameList = new ArrayList<String>();

		if(type.equalsIgnoreCase("expressions")){
			HIPAA hipaa = unmarshall(json, HIPAA.class);
			List<Expression> expressions = hipaa.getExpressions();
			for(Expression e:expressions){
				nameList.add(e.getName());
			}
			validationMessage += (nameList.contains(name)) ? ""
					: "Name expected in Json is not seen Expected:"+name+" but was "+nameList.toString();

		}else if(type.equalsIgnoreCase("keywords")){
			HIPAA hipaa = unmarshall(json, HIPAA.class);
			List<Keyword> keywords = hipaa.getKeywords();
			for(Keyword k:keywords){
				nameList.add(k.getName());
			}
			validationMessage += (nameList.contains(name)) ? ""
					: "Name expected in Json is not seen Expected:"+name+" but was "+nameList.toString();

		}


		return validationMessage;
	}


	private String verifyKeyValueExistsJson(String json, String name, String key, String value, String riskType, String type) {
		String validationMessage = "";

		if(type.equalsIgnoreCase("expressions")){
			HIPAA hipaa = unmarshall(json, HIPAA.class);
			List<Expression> expressions = hipaa.getExpressions();
			List<String> keyList = new ArrayList<String>();
			List<Integer> valueList = new ArrayList<Integer>();
			for(Expression e:expressions){
				if(e.getName().contains(name)){
					List<Value> values = e.getValues();
					for(Value v:values){
						keyList.add(v.getKey());
						valueList.add(v.getValue());
					}
				}else{
					//validationMessage += "Name "+name+" is not present under HIPAA";	
				}
			}

			if(key.contains(",")){
				String[] keyTemp = key.split(",");
				validationMessage += (keyTemp.length>0) ? "" : "Expecting some key for Json but was " + key;
			}else{
				validationMessage += (keyList.contains(key)) ? ""
						: "Key expected in Json is not seen Expected:"+key+" but was "+keyList.toString();

			}

			validationMessage += (valueList.toString().contains(value)) ? ""
					: "Value expected in Json is not seen Expected:"+valueList.toString()+" but was "+value;

		}else if(type.equalsIgnoreCase("keywords")){
			HIPAA hipaa = unmarshall(json, HIPAA.class);
			List<Keyword> keywords = hipaa.getKeywords();
			List<String> keyList = new ArrayList<String>();
			List<Integer> valueList = new ArrayList<Integer>();
			for(Keyword k:keywords){
				if(k.getName().contains(name)){
					List<Value> values = k.getValues();
					for(Value v:values){
						keyList.add(v.getKey());
						valueList.add(v.getValue());
					}
				}else{
					//validationMessage += "Name "+name+" is not present under HIPAA";	
				}
			}

			if(key.contains(",")){
				String[] keyTemp = key.split(",");
				validationMessage += (keyTemp.length>0) ? "" : "Expecting some key for Json but was " + key;
			}else{
				validationMessage += (keyList.contains(key)) ? ""
						: "Key expected in Json is not seen Expected:"+key+" but was "+keyList.toString();

			}

			validationMessage += (valueList.toString().contains(value)) ? ""
					: "Value expected in Json is not seen Expected:"+valueList.toString()+" but was "+value;

		}


		return validationMessage;
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
			String[] keywords) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String vk_pii = getJSONValue(contentJson, "vk_pii");
		String vk_pci = getJSONValue(contentJson, "vk_pci");
		String vk_hipaa = getJSONValue(contentJson, "vk_hipaa");
		String vk_virus = getJSONValue(contentJson, "vk_virus");
		String vk_encryption = getJSONValue(contentJson, "vk_encryption");
		String vk_vba_macros = getJSONValue(contentJson, "vk_vba_macros");
		String vk_glba = getJSONValue(contentJson, "vk_glba");
		String violations = getJSONValue(contentJson, "violations");

		String vk_source_code = "";
		try {
			vk_source_code = getJSONValue(contentJson, "vk_source_code");
		} catch (Exception e) {
			vk_source_code = "0";
		}
		String vk_content_iq_violations = "";
		try {
			vk_content_iq_violations = getJSONValue(contentJson, "vk_content_iq_violations");

			List<String> nameList = new ArrayList<String>();
			JSONArray cArray = (JSONArray) new JSONTokener(vk_content_iq_violations).nextValue();
			for (int i = 0; i < cArray.size(); i++) {
				String name = cArray.getString(i).toString();
				nameList.add(name);
			}

			validationMessage += (nameList.contains(ciqProfileName)) ? "" :
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
						String dictionary = violationsArray.getJSONObject(j).get("dictionary").toString();
						String count = violationsArray.getJSONObject(j).get("count").toString();

						keywordList.add(keyword);
						if(dictionary.contains(dictionaries)){
							validationMessage += (dictionary.contains(dictionaries)) ? "" :
								"Expecting "+dictionaries+" but was " + dictionary;
						}else{
							validationMessage += (!dictionary.isEmpty()) ? "" :
								"Expecting "+dictionaries+" but was " + dictionary;
						}

						if(keyword.equalsIgnoreCase("confidential")){
							int cCount = Integer.parseInt(count);
							validationMessage += (cCount>0) ? "" :
								"Expecting >0 but was " + count;
						}else{
							validationMessage += (count.contains("1")) ? "" :
								"Expecting 1 but was " + count;
						}


					}
				}else{
					JSONArray violationsArray = (JSONArray) new JSONTokener(violationsJson).nextValue();
					for (int j = 0; j < violationsArray.size(); j++) {
						String keyword = violationsArray.getJSONObject(j).get("keyword").toString();
						String dictionary = violationsArray.getJSONObject(j).get("dictionary").toString();
						String count = violationsArray.getJSONObject(j).get("count").toString();

						validationMessage += (!keyword.isEmpty()) ? "" :
							"Expecting "+Arrays.asList(keywords)+" but was " + keyword;
						validationMessage += (!dictionary.isEmpty()) ? "" :
							"Expecting "+dictionaries+" but was " + dictionary;
						validationMessage += (!count.isEmpty()) ? "" :
							"Expecting 1 but was " + count;
					}
				}



				validationMessage += (!updated_timestamp.isEmpty()) ? ""
						: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
			}

			validationMessage += (keyList.contains(ciqProfileName)) ? "" :
				"Expecting "+ciqProfileName+" but was " + keyList;
			if(keywordList.size()>0){
				String[] keywordActual = keywordList.toArray(new String[keywordList.size()]);Arrays.sort(keywordActual);
				String[] keywordExpected = keywords;Arrays.sort(keywordExpected);

				validationMessage += (arrayComparison(keywordActual, keywordExpected)) ? "" :
					"Expecting keywords are"+Arrays.asList(keywordExpected)+" but was " + Arrays.asList(keywordActual);
			}


		} catch (Exception e) {
			vk_content_iq_violations = "";
		}



		validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		validationMessage += (vk_encryption.contains("0")) ? "": "vk_encryption is not Expecting 0 but was " + vk_encryption;
		validationMessage += (vk_source_code.contains("0")) ? "": "vk_source_code is not Expecting 0 but was " + vk_source_code;
		validationMessage += (vk_vba_macros.contains("0")) ? "": "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;

		validationMessage += (violations.contains("true")) ? ""
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

		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
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
							validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

						}
					}else{
						if(checkRegexInNameList(nameList,"DCI_(.*)PN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)PN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)ID(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)ID(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)BA(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)BA(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)DLN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)DLN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
						else{
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;
							}


						}
					}

				}

				nameList = new ArrayList<String>();
				for (int i = 0; i < cArray.size(); i++) {
					String name = cArray.getString(i).toString();
					nameList.add(name);
				}

				validationMessage += (nameList.contains(ciqProfileName)) ? "" :
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
									validationMessage += (term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage += (!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}
								int expectedCount = Integer.parseInt(counts);
								Logger.info("Expected count:"+expectedCount);
								Logger.info("Actual count:"+actualCount);
								validationMessage += (actualCount==expectedCount) ? "" :
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
									validationMessage += (term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage += (!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}	
							}

							int expectedCount = Integer.parseInt(counts);
							Logger.info("Expected count:"+expectedCount);
							Logger.info("Actual count:"+actualCount);
							validationMessage += (actualCount==expectedCount) ? "" :
								"Expecting "+expectedCount+" but was " + actualCount;
						}
					}





					validationMessage += (!updated_timestamp.isEmpty()) ? ""
							: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
				}

				validationMessage += (keyList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + keyList;

			} catch (Exception e) {
				vk_content_iq_violations = "";
			}
		}




		if (risksExpected.contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
		}else{
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (risksExpected.contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
		}else{
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (risksExpected.contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}else{
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

		}

		if (risksExpected.contains("Virus")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;
		}else{
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (risksExpected.contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? "" : "vk_encryption is not Expecting 1 but was " + vk_encryption;
		}else{
			validationMessage += (vk_encryption.contains("0")) ? "" : "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (risksExpected.contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? "" : "vk_source_code is not Expecting 1 but was " + vk_source_code;
		}else{
			validationMessage += (vk_source_code.contains("0")) ? "" : "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (risksExpected.contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}else{
			validationMessage += (vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (risksExpected.contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}else{
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (risksExpected.contains("FERPA")) {
			validationMessage += (vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		}else{
			validationMessage += (vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		validationMessage += (violations.contains("true")) ? ""
				: "violations is not Expecting true but was " + violations;

		return validationMessage;
	}

	public String validationContentCheckRiskLogsHighSensitivity(String sourceJson,String ciqProfileName, String terms, 
			String counts, String risksExpected) {
		String validationMessage = "";
		String contentJson = getJSONValue(sourceJson, "content_checks");
		String violations = getJSONValue(contentJson, "violations");

		String vk_source_code = "";try {vk_source_code = getJSONValue(contentJson, "vk_source_code");} catch (Exception e) {vk_source_code = "0";}
		String vk_pii = "";try {vk_pii = getJSONValue(contentJson, "vk_pii");} catch (Exception e) {vk_pii = "0";}
		String vk_pci = "";try {vk_pci = getJSONValue(contentJson, "vk_pci");} catch (Exception e) {vk_pci = "0";}
		String vk_hipaa = "";try {vk_hipaa = getJSONValue(contentJson, "vk_hipaa");} catch (Exception e) {vk_hipaa = "0";}
		String vk_virus = "";try {vk_virus = getJSONValue(contentJson, "vk_virus");} catch (Exception e) {vk_virus = "0";}
		String vk_encryption = "";try {vk_encryption = getJSONValue(contentJson, "vk_encryption");} catch (Exception e) {vk_encryption = "0";}
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
							validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

						}
					}else{
						if(checkRegexInNameList(nameList,"DCI_(.*)PN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)PN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)ID(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)ID(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)BA(.*)N")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)BA(.*)N",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}else if(checkRegexInNameList(nameList,"DCI_(.*)DLN")){
							nameList = removeIndexFromNameList(nameList,"DCI_(.*)DLN",ciqProfileName);
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
						else{
							if(nameList.size()>1){
								validationMessage += "False positives for terms are seen for the file Expected was:"+ciqProfileName+" but is "+nameList;

							}
						}
					}*/

				}

				nameList = new ArrayList<String>();
				for (int i = 0; i < cArray.size(); i++) {
					String name = cArray.getString(i).toString();
					nameList.add(name);
				}

				validationMessage += (nameList.contains(ciqProfileName)) ? "" :
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
									validationMessage += (term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage += (!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}
								int expectedCount = Integer.parseInt(counts);
								Logger.info("Expected count:"+expectedCount);
								Logger.info("Actual count:"+actualCount);
								validationMessage += (actualCount==expectedCount) ? "" :
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
									validationMessage += (term.contains(terms)) ? "" :
										"Expecting "+terms+" but was " + term;
									actualCount = actualCount+count;
								}else{
									validationMessage += (!term.isEmpty()) ? "" :
										"Expecting "+terms+" but was " + term;
								}	
							}

							int expectedCount = Integer.parseInt(counts);
							validationMessage += (actualCount==expectedCount) ? "" :
								"Expecting "+expectedCount+" but was " + actualCount;
						}
					}





					validationMessage += (!updated_timestamp.isEmpty()) ? ""
							: "Expecting some updated_timestamp but was " + updated_timestamp + " for key "+key;
				}

				validationMessage += (keyList.contains(ciqProfileName)) ? "" :
					"Expecting "+ciqProfileName+" but was " + keyList;

			} catch (Exception e) {
				vk_content_iq_violations = "";
			}
		}




		if (risksExpected.contains("PII")) {
			validationMessage += (vk_pii.contains("1")) ? "" : "vk_pii is not Expecting 1 but was " + vk_pii;
		}else{
			validationMessage += (vk_pii.contains("0")) ? "" : "vk_pii is not Expecting 0 but was " + vk_pii;
		}

		if (risksExpected.contains("PCI")) {
			validationMessage += (vk_pci.contains("1")) ? "" : "vk_pci is not Expecting 1 but was " + vk_pci;
		}else{
			validationMessage += (vk_pci.contains("0")) ? "" : "vk_pci is not Expecting 0 but was " + vk_pci;
		}

		if (risksExpected.contains("HIPAA")) {
			validationMessage += (vk_hipaa.contains("1")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;
		}else{
			validationMessage += (vk_hipaa.contains("0")) ? "" : "vk_hipaa is not Expecting 1 but was " + vk_hipaa;

		}

		if (risksExpected.contains("Virus")) {
			validationMessage += (vk_virus.contains("1")) ? "" : "vk_virus is not Expecting 1 but was " + vk_virus;
		}else{
			validationMessage += (vk_virus.contains("0")) ? "" : "vk_virus is not Expecting 0 but was " + vk_virus;
		}

		if (risksExpected.contains("ENCRYPTION")) {
			validationMessage += (vk_encryption.contains("1")) ? "" : "vk_encryption is not Expecting 1 but was " + vk_encryption;
		}else{
			validationMessage += (vk_encryption.contains("0")) ? "" : "vk_encryption is not Expecting 0 but was " + vk_encryption;
		}

		if (risksExpected.contains("Source Code")) {
			validationMessage += (vk_source_code.contains("1")) ? "" : "vk_source_code is not Expecting 1 but was " + vk_source_code;
		}else{
			validationMessage += (vk_source_code.contains("0")) ? "" : "vk_source_code is not Expecting 0 but was " + vk_source_code;
		}

		if (risksExpected.contains("VBA Macros")) {
			validationMessage += (vk_vba_macros.contains("1")) ? "" : "vk_vba_macros is not Expecting 1 but was " + vk_vba_macros;
		}else{
			validationMessage += (vk_vba_macros.contains("0")) ? "" : "vk_vba_macros is not Expecting 0 but was " + vk_vba_macros;
		}

		if (risksExpected.contains("GLBA")) {
			validationMessage += (vk_glba.contains("1")) ? "" : "vk_glba is not Expecting 1 but was " + vk_glba;
		}else{
			validationMessage += (vk_glba.contains("0")) ? "" : "vk_glba is not Expecting 0 but was " + vk_glba;
		}

		if (risksExpected.contains("FERPA")) {
			validationMessage += (vk_ferpa.contains("1")) ? "" : "vk_ferpa is not Expecting 1 but was " + vk_ferpa;
		}else{
			validationMessage += (vk_ferpa.contains("0")) ? "" : "vk_ferpa is not Expecting 0 but was " + vk_ferpa;
		}

		validationMessage += (violations.contains("true")) ? ""
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
					validationMessage += (doc_class.contains(d)) ? ""
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
	 * Get risk types for a file
	 * @param fileName
	 * @return
	 */
	public String[] riskTypesForAFile(String fileName) {
		String[] riskArray = null;
		String risks = null;
		try {
			Map<String, String> riskTypes = getRiskTypesFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

			if (risks != null) {
				riskArray = risks.split(",");
			}

		} catch (Exception ex) {

		}
		return riskArray;
	}

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

	/**
	 * get document classification for a file
	 * @param fileName
	 * @return
	 */
	public String pciRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getPCIRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String hipaaRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getHIPAARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}


	public String glbaRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getGLBARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String vbaRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getVBARisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
	}

	public String virusRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getVirusRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
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

	/**
	 * get document classification for a file
	 * @param fileName
	 * @return
	 */
	public String[] docClassTypesForAFile(String fileName) {
		String[] docClassArray = null;
		String docClass = null;
		try {
			Map<String, String> docClassTypes = getDocClassTypesFromPropertiesFile();

			for (Map.Entry<String, String> d : docClassTypes.entrySet()) {
				String key = d.getKey();
				String value = d.getValue();
				if (fileName.contains(key)) {
					docClass = value;
				}
			}

			if (docClass != null) {
				docClassArray = docClass.split(",");
			}

		} catch (Exception ex) {

		}
		return docClassArray;
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


	/**
	 * Get generic create search query
	 * @param name
	 * @param description
	 * @param threshold
	 * @param valuesPreDefDict
	 * @param valuesPreDefTerms
	 * @param valuesCustomDict
	 * @param valuesCustomTerms
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForCIQ(String name, String description, int threshold, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("threshold", threshold).
				field("api_enabled", true).
				field("appliesToSecurlets", true).
				startArray("domains").
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
		builder.endArray().
		endObject().
		endArray().	
		endObject(); 
		return builder.string();
	}

	/**
	 * Get generic create search query
	 * @param name
	 * @param description
	 * @param threshold
	 * @param valuesPreDefDict
	 * @param valuesPreDefTerms
	 * @param valuesCustomDict
	 * @param valuesCustomTerms
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForCIQ(String name, String description, int threshold, boolean apiEnabled, 
			boolean appliesToSecurlets, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("threshold", threshold).
				field("api_enabled", apiEnabled).
				field("appliesToSecurlets", appliesToSecurlets).
				startArray("domains").
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
		builder.endArray().
		endObject().
		endArray().	
		endObject(); 
		return builder.string();
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param threshold
	 * @param apiEnabled
	 * @param appliesToSecurlets
	 * @param highSensitivityFlag
	 * @param valuesPreDefDict
	 * @param valuesPreDefTerms
	 * @param valuesCustomDict
	 * @param valuesCustomTerms
	 * @param riskTypes
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForCIQ(String name, String description, int threshold, boolean apiEnabled, 
			int appliesToSecurlets, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms,
			List<String> riskTypes, List<String> contentTypes) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("threshold", threshold).
				field("api_enabled", apiEnabled).
				field("appliesToSecurlets", appliesToSecurlets).
				startArray("domains").
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
		if(riskTypes != null){buildValuesRisks(builder,riskTypes,"DCI_RISK");}
		if(contentTypes != null){buildValuesContentTypes(builder,contentTypes,"DCI_CONTENT");}
		builder.endArray().
		endObject().
		endArray();
		if(riskTypes == null) {
			builder.startArray("riskTypeSelected").endArray();
		}
		if(contentTypes == null) {
			builder.startArray("contentTypeSelected").endArray();
		}
		builder.endObject(); 
		return builder.string();
	}


	/**
	 * Get generic create search query
	 * @param name
	 * @param description
	 * @param threshold
	 * @param valuesPreDefDict
	 * @param valuesPreDefTerms
	 * @param valuesCustomDict
	 * @param valuesCustomTerms
	 * @return
	 * @throws Exception
	 */
	public String getSearchQueryForCIQ(String profileId, String name, String description, 
			int threshold, boolean apiEnabled, boolean appliesToSecurlets, boolean highSensitivityFlag,
			List<String> valuesPreDefDict, List<String> valuesPreDefTerms,
			List<String> valuesCustomDict, List<String> valuesCustomTerms) throws Exception {
		XContentBuilder builder = XContentFactory.jsonBuilder().
				startObject().
				field("name", name).
				field("description", description).
				field("profileId", profileId).
				field("threshold", threshold).
				field("api_enabled", apiEnabled).
				field("appliesToSecurlets", appliesToSecurlets).
				startArray("domains").
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
		for (String value : values) {
			builder.
			startObject().
			field("name", "").
			field("weight", 1).
			field("is_not", false).
			field("source", type).
			field("min_count", 1);
			if(type.equalsIgnoreCase("PREDEFINED_TERMS")){
				builder.field("is_high_sensitivity", highSensitivityFlag);
			}
			builder.
			startArray("value").
			value(value).
			endArray().
			endObject();
		}
	}

	public void buildValuesRisks(XContentBuilder builder,List<String> values, String type) throws IOException {
		builder.
		startObject().
		field("name", "").
		field("weight", 1).
		field("is_not", false).
		field("source", type).
		field("min_count", 1).
		startArray("value");

		for (String value : values) {
			builder.value(value);
		}
		builder.endArray().
		endObject();
	}

	public void buildValuesContentTypes(XContentBuilder builder,List<String> values, String type) throws IOException {
		builder.
		startObject().
		field("name", "").
		field("weight", 1).
		field("is_not", false).
		field("source", type).
		field("min_count", 1).
		startArray("value");

		for (String value : values) {
			builder.value(value);
		}
		builder.endArray().
		endObject();
	}

	public String createCIQPredefinedDictionary(Client restClient, SuiteData suiteData, String ciqDictionary, String ciqProfileName,
			String ciqProfileDescription, boolean apiEnabled, boolean appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Predefined Dictionaries:"+ciqDictionary+" in progress");
		String contentIQProfileId=null;

		try {
			List<String> valuesPreDefDict= new ArrayList<String>();
			valuesPreDefDict.add(ciqDictionary);
			List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;

			String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 
					1, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);
			createContentIqProfile(restClient, getCookieHeaders(suiteData), 
					new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId );
			Logger.info("Creating CIQ Predefined Dictionaries:"+ciqDictionary+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String createCIQPredefinedTerm(Client restClient, SuiteData suiteData, String ciqTerm, String ciqProfileName,
			String ciqProfileDescription, boolean apiEnabled, boolean appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Creating CIQ Predefined Terms:"+ciqTerm+" in progress");

		String contentIQProfileId=null;
		try {
			List<String> valuesPreDefDict= null;
			List<String> valuesPreDefTerms= new ArrayList<String>();
			valuesPreDefTerms.add(ciqTerm);
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;

			String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 
					1, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);
			createContentIqProfile(restClient, getCookieHeaders(suiteData), 
					new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId);
			Logger.info("Creating CIQ Predefined Terms:"+ciqTerm+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String updateCIQPredefinedDictionary(Client restClient, SuiteData suiteData, String ciqId, String ciqDictionary, String ciqProfileName,
			String ciqProfileDescription, boolean apiEnabled, boolean appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Updating CIQ Predefined Dictionaries:"+ciqDictionary+" in progress");
		String contentIQProfileId=null;

		try {
			List<String> valuesPreDefDict= new ArrayList<String>();
			valuesPreDefDict.add(ciqDictionary);
			List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;

			String payload = getSearchQueryForCIQ(ciqId, ciqProfileName, ciqProfileDescription, 
					1, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);
			createContentIqProfile(restClient, getCookieHeaders(suiteData), 
					new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
			Logger.info(contentIQProfileId );
			Logger.info("Updating CIQ Predefined Dictionaries:"+ciqDictionary+" is completed");
		} catch (Exception e) {

		}	
		return contentIQProfileId;
	}

	public String updateCIQPredefinedTerm(Client restClient, SuiteData suiteData, String ciqId, String ciqTerm, String ciqProfileName,
			String ciqProfileDescription, boolean apiEnabled, boolean appliesToSecurlets, boolean highSensitivityFlag){
		Logger.info("Updating CIQ Predefined Terms:"+ciqTerm+" in progress");

		String contentIQProfileId=null;
		try {
			List<String> valuesPreDefDict= null;
			List<String> valuesPreDefTerms= new ArrayList<String>();
			valuesPreDefTerms.add(ciqTerm);
			List<String> valuesCustomDict=null;
			List<String> valuesCustomTerms=null;

			String payload = getSearchQueryForCIQ(ciqId, ciqProfileName, ciqProfileDescription, 
					1, apiEnabled ,appliesToSecurlets, highSensitivityFlag, valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);
			createContentIqProfile(restClient, getCookieHeaders(suiteData), 
					new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

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
	public HttpResponse createContentIqProfile(Client restClient, List<NameValuePair> requestHeader, 
			StringEntity entity, String scheme, String host){

		Logger.info("Creating of CIQ profile in progress");
		HttpResponse createContentIQResponse = null;
		String restAPI = "/controls/add_ciq_profile";

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

		Logger.info("Creating of CIQ profile is completed");


		return createContentIQResponse;
	}


	public HttpResponse deleteContentIQProfileById(Client restClient, List<NameValuePair> requestHeader, 
			String id, String scheme, String host ) {
		Logger.info("Deleting of CIQ profile:"+id+" in progress");

		HttpResponse delResponse = null;
		String restAPI = "/controls/add_ciq_profile";


		try{	
			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			waitForSeconds(1);

			delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(delResponse.getStatusLine().getStatusCode()==200){}else{
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
		}catch(Exception e){
			try{
				StringEntity entity = new StringEntity(
						"{\"url\":\"contentprofiles\",\"id\":\""+id+"\",\"action\":\"delete\"}");
				URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
				waitForSeconds(1);

				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				if(delResponse.getStatusLine().getStatusCode()==200){}else{
					delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
				}
			}catch(Exception e1){}
		}

		Logger.info("Deleting of CIQ profile:"+id+" is completed");

		return delResponse;	
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
		String ciqProfileID = null;
		String data = getJSONValue(ciqResponse, "data");
		String ciqProfiles = getJSONValue(data, "objects");
		JSONArray ciqArray = (JSONArray) new JSONTokener(ciqProfiles).nextValue();
		for (int i = 0; i < ciqArray.size(); i++) {
			String policyNameByID = getJSONValue(ciqArray.getJSONObject(i).toString(), DCIConstants.PROFILE_NAME);
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
			String policyNameByID = getJSONValue(ciqArray.getJSONObject(i).toString(), DCIConstants.PROFILE_NAME);
			policyNameByID = policyNameByID.substring(1, policyNameByID.length() - 1);
			ciqProfileID = getJSONValue(ciqArray.getJSONObject(i).toString(), "id");
			ciqProfileID = ciqProfileID.substring(1, ciqProfileID.length() - 1);

			Logger.info("Deleting of CIQ profile:"+policyNameByID+" is in progress");

			StringEntity entity = new StringEntity(
					"{\"url\":\"contentprofiles\",\"id\":\""+ciqProfileID+"\",\"action\":\"delete\"}");	
			URI dataUri = ClientUtil.BuidURI(scheme, host, restAPI);	
			waitForSeconds(1);
			HttpResponse delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			if(delResponse.getStatusLine().getStatusCode()==200){}else{
				delResponse = restClient.doPost(dataUri, requestHeader, null, entity);
			}
			Logger.info("Deleting of CIQ profile:"+policyNameByID+" is completed");
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

	public List<String> createPredefTerms() {
		List<String> valuesPreDefTerms=new ArrayList<String>();
		valuesPreDefTerms.add("Australia Company Number, ACN");valuesPreDefTerms.add("Australia Medicare Card Number");
		valuesPreDefTerms.add("Australia Tax File Number");valuesPreDefTerms.add("Brazil CPF Number");
		valuesPreDefTerms.add("Belgium National ID");valuesPreDefTerms.add("Belgium National Register");
		valuesPreDefTerms.add("Canada Social Insurance Number");valuesPreDefTerms.add("Credit Card Number");
		valuesPreDefTerms.add("France National Identification Number, INSEE");valuesPreDefTerms.add("Germany Driver License");
		valuesPreDefTerms.add("Mexico RFC Number");valuesPreDefTerms.add("Mexico CURP Number");
		valuesPreDefTerms.add("Netherlands BSN");valuesPreDefTerms.add("Sweden National ID");
		valuesPreDefTerms.add("SWIFT code");valuesPreDefTerms.add("UK National Health Service Number");
		valuesPreDefTerms.add("UK National Insurance Number");valuesPreDefTerms.add("US ITIN");
		valuesPreDefTerms.add("US Social Security Number");valuesPreDefTerms.add("UK Driving License Number");
		return valuesPreDefTerms;
	}


	public List<String> createPredefDictionaries() {
		List<String> valuesPreDefDict= new ArrayList<String>();
		valuesPreDefDict.add("Diseases");valuesPreDefDict.add("Energy");valuesPreDefDict.add("Gambling");
		valuesPreDefDict.add("Illegal Drugs");valuesPreDefDict.add("Obscenities");valuesPreDefDict.add("Pharmaceutical Drugs");
		valuesPreDefDict.add("Ticker Symbols");valuesPreDefDict.add("USG Export Controlled Items");valuesPreDefDict.add("Violence");
		return valuesPreDefDict;
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
	 * get document classification for a file
	 * @param fileName
	 * @return
	 */
	public String piiRisksForAFile(String fileName) {
		String risks = null;
		try {
			Map<String, String> riskTypes = getPIIRisksFromPropertiesFile();

			for (Map.Entry<String, String> r : riskTypes.entrySet()) {
				String key = r.getKey();
				String value = r.getValue();
				if (fileName.contains(key)) {
					risks = value;
				}
			}

		} catch (Exception ex) {

		}
		return risks;
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
					value("AUDIO").
					value("BUSINESS").
					value("COMPUTING").
					value("CRYPTOGRAPHIC KEYS").
					value("DESIGN DOC").
					value("ENCRYPTION").
					value("ENGINEERING").
					value("EXECUTABLE").
					value("HEALTH").
					value("IMAGE").
					value("LEGAL").
					value("SOURCE CODE").
					value("VIDEO").
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

	public Map<String, String> generateHeadersUI(String riskType, String appType, String isInternal, String requestType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("requestType", requestType);
		additionalParams.put("app", appType);
		additionalParams.put("vlTypes", riskType);
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, String isInternal, String requestType) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("requestType", requestType);
		additionalParams.put("app", appType);
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, String isInternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isInternal",  isInternal);
		additionalParams.put("app", appType);
		return additionalParams;
	}

	public Map<String, String> generateHeadersUI(String appType, boolean isExternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isExternal",  Boolean.toString(isExternal));
		additionalParams.put("app", appType);
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
		return additionalParams;
	}

	public Map<String, String> generateHeadersAPI(String exposed, boolean isExternal) {
		Map<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("isExternal",  Boolean.toString(isExternal));
		additionalParams.put("exposed", 	exposed);
		return additionalParams;
	}


	public List<NameValuePair> getBasicHeaders(SuiteData suiteData) {
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, ClientUtil
				.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCsrfToken()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));

		return requestHeader;
	}


	public List<NameValuePair> getHeaders(SuiteData suiteData) {
		List<NameValuePair> headers = new ArrayList<NameValuePair>(); 
		headers.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		headers.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		headers.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCsrfToken()));
		headers.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		headers.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		headers.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		headers.add(new BasicNameValuePair(HttpHeaders.COOKIE, "csrftoken="+suiteData.getCsrfToken()+";sessionid="+suiteData.getSessionID()+";"));
		headers.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, ClientUtil.getAuthParam(suiteData.getUsername(), suiteData.getPassword())));
		return headers;
	}


	public List<NameValuePair> getCookieHeaders(SuiteData suiteData){
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken",suiteData.getCsrfToken()));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCsrfToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, ClientUtil.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken",suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		return requestHeader;
	}

	public void waitForSeconds(int time){
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

	public String[] createSampleFileType(String folderName, String[] fileName) {
		for (int i = 0; i < fileName.length; i++) {	
			fileName[i] = createSampleFileType(folderName,
					fileName[i]);
		}

		return fileName;
	}



	public String createContentIqProfile(Client restClient, SuiteData suiteData,
			String ciqType, String ciqProfileName, String ciqProfileDescription, String ciqProfileType){
		CIQType ciq = CIQType.getCIQType(ciqType);
		String contentIQProfileId = null;
		switch (ciq) {
		case PreDefDict: {

			try {
				Logger.info("Creating CIQ Predefined Dictionaries:"+ciqProfileName+" in progress");
				List<String> valuesPreDefDict=new ArrayList<String>();
				valuesPreDefDict.add(ciqProfileType);
				List<String> valuesPreDefTerms=null;
				List<String> valuesCustomDict=null;
				List<String> valuesCustomTerms=null;

				String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 1, false,
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);	
				createContentIqProfile(restClient, getCookieHeaders(suiteData), 
						new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

				HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
						suiteData.getScheme(), suiteData.getHost());
				contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
				Logger.info(contentIQProfileId);

				Logger.info("Creating CIQ Predefined Dictionaries:"+ciqProfileName+" is completed");
			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles with Predefined Dictionaries" + ex.getLocalizedMessage());
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

				String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 1, false,
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);	
				createContentIqProfile(restClient, getCookieHeaders(suiteData), 
						new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

				HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
						suiteData.getScheme(), suiteData.getHost());
				contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
				Logger.info(contentIQProfileId);

				Logger.info("Creating CIQ Predefined Terms:"+ciqProfileName+" is completed");
			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles with Predfined Terms" + ex.getLocalizedMessage());
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

				String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 1, false,
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);	
				createContentIqProfile(restClient, getCookieHeaders(suiteData), 
						new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

				HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
						suiteData.getScheme(), suiteData.getHost());
				contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
				Logger.info(contentIQProfileId);

				Logger.info("Creating CIQ Custom Dictionaries:"+ciqProfileName+" is completed");
			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles with Custom Dictionaries" + ex.getLocalizedMessage());
			}

			break;
		}
		case CustomTerms: {

			try {
				Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" in progress");
				List<String> valuesPreDefDict=null;
				List<String> valuesPreDefTerms=null;
				List<String> valuesCustomDict=null;
				List<String> valuesCustomTerms=new ArrayList<String>();
				valuesCustomTerms.add(ciqProfileType);

				String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 1, false,
						valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms);	
				createContentIqProfile(restClient, getCookieHeaders(suiteData), 
						new StringEntity(payload,"UTF-8"), suiteData.getScheme(), suiteData.getHost());

				HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
						suiteData.getScheme(), suiteData.getHost());
				contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
				Logger.info(contentIQProfileId);

				Logger.info("Creating CIQ Custom Terms:"+ciqProfileName+" is completed");
			} catch (Exception ex) {
				Logger.info("Issue with Create Content Iq Profiles with Custom Terms" + ex.getLocalizedMessage());
			}

			break;
		}
		default: {

			break;
		}
		}

		return contentIQProfileId;
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


	public void createDictionary(SuiteData suiteData, Client restClient, String dictName, String dictDescription, 
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

		builder.addTextBody("name", dictName)
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


	public List<NameValuePair> getBrowserHeaders(SuiteData suiteData){
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
				csrfToken = suiteData.getCsrfToken();
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

	private WebDriver getHtmlUnitDriver(SuiteData suiteData){
		DesiredCapabilities capability= DesiredCapabilities.htmlUnit();
		capability.setJavascriptEnabled(true);
		capability.setBrowserName("htmlunit");
		capability.setVersion("internet explorer");
		capability.setPlatform(org.openqa.selenium.Platform.ANY);
		WebDriver driver = new HtmlUnitDriver(capability);

		driver.get(suiteData.getReferer()+"/static/ng/appLogin/index.html#/?redirect=false");
		try {Thread.sleep(10000);} catch (InterruptedException e) {}
		WebElement login = driver.findElement(By.name("email"));
		login.sendKeys(suiteData.getUsername());
		WebElement pwd = driver.findElement(By.name("password"));
		pwd.sendKeys(suiteData.getPassword());
		WebElement loginbutton = driver.findElement(By.cssSelector(".btn.btn-default.btn-lg.btn-block.ng-scope"));
		loginbutton.click();

		try {Thread.sleep(10000);} catch (InterruptedException e) {}
		return driver;
	}

	public HttpResponse deleteDictionaryById(Client restClient, List<NameValuePair> requestHeader, 
			String id, String scheme, String host ) throws Exception {
		Logger.info("Deleting of dictionary:"+id+" in progress");

		HttpResponse delResponse = null;
		String restAPI = "/controls/remove_dictionary";
		StringEntity entity = new StringEntity(
				"{\"id\":\""+id+"\"}");

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

		Logger.info("Deleting of dictionary:"+id+" is completed");

		return delResponse;	
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

	public void deleteAllCIQProfile(Client restClient, SuiteData suiteData) {
		try {

			HttpResponse listContentIQResponse = listContentIQProfile(restClient, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());
			deleteAllContentIQProfiles(restClient, listContentIQResponse, getCookieHeaders(suiteData), 
					suiteData.getScheme(), suiteData.getHost());

		} catch (Exception ex) {
			Logger.info("Issue with Deleting of all ContentIQ Profiles" + ex.getLocalizedMessage());
		}
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

	public void deleteAllPolicies(Client restClient, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("policyList"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);
		HttpResponse policyListResponse = restClient.doGet(dataUri, requestHeader);
		String policyListResponseBody = ClientUtil.getResponseBody(policyListResponse);
		String policiesList = ClientUtil.getJSONValue(policyListResponseBody, "policies"); 
		JSONArray jArray = (JSONArray) new JSONTokener(policiesList).nextValue();
		for (int i = 0; i < jArray.size(); i++) {
			String name = ClientUtil.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
			name = name.substring(1, name.length()-1);
			deletePolicy(restClient, name, requestHeader, suiteData);
		}		
	}

	public void deletePolicy(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		Logger.info("Delete Policy - " + policyName);
		Map<String, String> policyDetails = this.getPolicyDetailsByName(restClient, policyName, requestHeader, suiteData);
		HttpResponse deleteResponse = this.deletePolicy(restClient, policyDetails.get(DCIConstants.POLICY_TYPE), policyDetails.get(DCIConstants.POLICY_SUB_ID), requestHeader, suiteData);
		String policyDeleteResponseBody = ClientUtil.getResponseBody(deleteResponse);
		String policyDeleteStatus = ClientUtil.getJSONValue(policyDeleteResponseBody, DCIConstants.ACTION_STATUS);
		policyDeleteStatus = policyDeleteStatus.substring(1, policyDeleteStatus.length() - 1);
		Assert.assertEquals(policyDeleteStatus, DCIConstants.SUCCESS);
		Logger.info("Policy Deleted - " + policyName);
	}

	public HttpResponse deletePolicy(Client restClient, String policyType, String policySubId, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
		String restAPI = replaceGenericParams(suiteData, suiteData.getAPIMap().get("deletePolicy"));
		String entityString = "{\"policy_type\":\""+policyType+"\",\"action\":true,\"sub_id\":\""+policySubId+"\"}";
		Logger.info(entityString);
		StringEntity entity = new StringEntity(entityString);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(dataUri, requestHeader, null, entity);	
	}

	public Map<String, String> getPolicyDetailsByName(Client restClient, String policyName, List<NameValuePair> requestHeader, SuiteData suiteData) throws Exception{
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

	public Map<String, String> createCIQProfileWithRiskAndContentTypes(Client client, SuiteData suiteData){
		String ciqProfileName = "DCI_CIQ_RISK_CONTENT";String ciqProfileDescription = "DCI CIQ Description";
		String ciqType = "RiskContentTypes";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only risks and content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRisks= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
					"pii","ferpa","glba");
			List<String> valuesContentTypes=Arrays.asList(
					"audio","business","computing","cryptographic_keys","design doc",
					"encryption","engineering","executable","health","image","legal","source_code","video"
					);
			createCIQProfile(client, suiteData, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks and content is completed");
			waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}
		return ciq;
	}
	
	public Map<String, String> createCIQProfileWithRiskTypes(Client client, SuiteData suiteData){
		String ciqProfileName = "DCI_CIQ_RISK";String ciqProfileDescription = "DCI CIQ Description";
		String ciqType = "RiskTypes";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only risks in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRisks= Arrays.asList("dlp","hipaa","vba_macros","virus","pci",
					"pii","ferpa","glba");
			List<String> valuesContentTypes=null;
			createCIQProfile(client, suiteData, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, 0, true, 1, false);
			Logger.info("Creating CIQ profile with only risks is completed");
			waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}
		return ciq;
	}
	
	public Map<String, String> createCIQProfileWithContentTypes(Client client, SuiteData suiteData){
		String ciqProfileName = "DCI_CIQ_CONTENT";String ciqProfileDescription = "DCI CIQ Description";
		String ciqType = "ContentTypes";

		Map<String,String> ciq = new HashMap<String,String>();
		ciq.put("ciqProfileName", ciqProfileName);ciq.put("ciqProfileDescription", ciqProfileDescription);
		ciq.put("ciqType", ciqType);

		try {
			Logger.info("Creating CIQ profile with only content in progress");
			List<String> valuesPreDefDict=null;List<String> valuesPreDefTerms=null;
			List<String> valuesCustomDict=null;List<String> valuesCustomTerms=null;
			List<String> valuesRisks=null;
			List<String> valuesContentTypes=Arrays.asList(
					"audio","business","computing","cryptographic_keys","design doc",
					"encryption","engineering","executable","health","image","legal","source_code","video"
					);
			createCIQProfile(client, suiteData, ciqProfileName, ciqProfileDescription, valuesPreDefDict, valuesPreDefTerms,
					valuesCustomDict, valuesCustomTerms, valuesRisks, valuesContentTypes, 0, true, 1, false);
			Logger.info("Creating CIQ profile with only content is completed");
			waitForSeconds(10);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}
		return ciq;
	}
	
	private void createCIQProfile(Client client, SuiteData suiteData, String ciqProfileName, String ciqProfileDescription, List<String> valuesPreDefDict,
			List<String> valuesPreDefTerms, List<String> valuesCustomDict, List<String> valuesCustomTerms,List<String> valuesRisks,
			List<String> valuesContentTypes,int threshold, boolean apiEnabled, int appliesToSecurlets, boolean highSensitivityFlag) 
					throws Exception {
		String payload = getSearchQueryForCIQ(ciqProfileName, ciqProfileDescription, 
				threshold, apiEnabled, appliesToSecurlets, highSensitivityFlag, 
				valuesPreDefDict, valuesPreDefTerms, valuesCustomDict, valuesCustomTerms,valuesRisks,valuesContentTypes);	
		Logger.info(payload);
		createContentIqProfile(client, getCookieHeaders(suiteData), 
				new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());

		HttpResponse listContentIQResponse = listContentIQProfile(client, getCookieHeaders(suiteData), 
				suiteData.getScheme(), suiteData.getHost());
		String contentIQProfileId = getContentIQProfileID(listContentIQResponse, ciqProfileName);
		Logger.info(contentIQProfileId);
	}
	
	public void createCIQPolicy(String policyName, Map<String, String> ciq,
			SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		Map<String, String>policyDataMap= new HashMap<String, String>(); 
		
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, ciq.get("ciqProfileName"));
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, "upload");
		
		
		fileTransferPolicyWithRiskContentIqCreateEnable
										(suiteData, requestHeader, policyDataMap);
	}
	
	public void fileTransferPolicyWithRiskContentIqCreateEnable(SuiteData suiteData, 
			List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithRisk(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}