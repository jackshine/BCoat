package com.elastica.beatle.tests.detect;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.detect.DetectCommonutils;
import com.elastica.beatle.detect.DetectFunctions;
import com.elastica.beatle.detect.DetectSequenceDetector;
import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.DetectSequenceDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.Objects;
import com.elastica.beatle.detect.dto.SDInput;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectInitializeTests;
import com.elastica.beatle.protect.ProtectTestConstants;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;

public class DetectDataSeparationTests  extends  DetectUtils  {
	
	AttributeBean attributeBean ;
	TestSuiteDTO suiteDataB = new TestSuiteDTO();
	DetectSequenceDetector dsd = new DetectSequenceDetector();
	
	@BeforeClass
	public void setSuitdata() throws Exception {
		
		suiteDataB.setHost(suiteData.getHost());
		suiteDataB.setScheme(suiteData.getScheme());
		suiteDataB.setEnvironmentName(suiteData.getEnvironmentName());
		suiteDataB.setAPIMap(suiteData.getAPIMap());
		suiteDataB.setBaseVersion(suiteData.getBaseVersion());
		suiteDataB.setTenantName(suiteData.getUser2TenantName());
		suiteDataB.setApiserverHostName(suiteData.getApiserverHostName());
		suiteDataB.setSaasApp(ProtectTestConstants.BOX);
		suiteDataB.setUsername(suiteData.getUser2Name());
		suiteDataB.setUser2Name(suiteData.getUser2Name());
		suiteDataB.setUser2Password(suiteData.getUser2Password());
		suiteDataB.setUser2TenantName(suiteData.getUser2TenantName());
		suiteDataB.setUser2TenantToken(suiteData.getUser2TenantToken());
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteDataB.getUser2Name(),suiteDataB.getUser2Password(), suiteDataB);
		suiteDataB.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteDataB.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));	
		
	}

	@Test
	public void DetectTBIAttributeTests() throws Exception {
		
		HttpResponse resp = getDetectAttributes(suiteData);
		String  responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		String ioi_Code = "TOO_MANY_SUM_LARGE_UPLOADS";
		 attributeBean = new AttributeBean(10, 5, 4, null);
			attributeBean.setEnabled(true);
		boolean enabled = attributeBean.isEnabled();
		updateDetectAttributes(enabled, attributeBean, getResponseArray, ioi_Code);
		//TDOD::Verify Attibutes
		
		HttpResponse resp1 = getDetectAttributes(suiteDataB);
		  responseBody = getResponseBody(resp1);
		 getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		 for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					
					String uri = (String) attributeObject.get(DetectUtils.RESOURCE_URI);
					Integer	threshold = (Integer) attributeObject.get("threshold");
					Integer	importance = (Integer) attributeObject.get("importance");
					Integer	window = (Integer) attributeObject.get("window");
				Logger.info("Assert::: ");
				Assert.assertFalse(attributeBean.getThreshold().equals(threshold)&&
						attributeBean.getImportance().equals(importance)&&
						attributeBean.getWindow().equals(window), "TBI values got updated in 2 tenants");
				
				break;
				}
			}
		 System.out.println("getResponseArray :::::: "+getResponseArray);
		 
		
	}
	
	@Test
	public void DetectBBIAttributeTests() throws Exception {
		
		HttpResponse resp = getDetectAttributes(suiteData);
		String  responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		attributeBean = new AttributeBean(20, 2,true);
		String ioi_Code = IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString();
		boolean enabled = attributeBean.isEnabled();
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		//TDOD::Verify Attibutes
		
		HttpResponse resp1 = getDetectAttributes(suiteDataB);
		  responseBody = getResponseBody(resp1);
		 getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		 for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					
					String uri = (String) attributeObject.get(DetectUtils.RESOURCE_URI);
					Integer	confidence = (Integer) attributeObject.get("confidence");
					Integer	importance = (Integer) attributeObject.get("importance");
				Logger.info("Assert::: ");
				Assert.assertFalse(attributeBean.getConfidence().equals(confidence)&&
						attributeBean.getImportance().equals(importance) ,
						 "BBI values got updated in 2 tenants");
				
				break;
				}
			}
		 System.out.println("getResponseArray :::::: "+getResponseArray);
		 
		
	}
	
	
	@Test
	public void DetectSequenceTests() throws Exception {
		
		
		String name = UUID.randomUUID().toString();

	String[] activities = new String[]{"Download", "Upload"};
	String[] failities = new String[]{"__any", "__any"} ;
	String[] sources = new String[]{"__any", "__any"} ;
	String[] users = new String[]{"__any", "__any"} ;
	String[] objects = new String[]{"__any", "__any"} ;
	
	SDInput sdInput = dsd.createSDInput(name, 3,36, false   ,false, true,
			activities, failities, sources, users, objects);
	HttpResponse response = dsd.createSequenceDetector(sdInput, suiteData);
	
	
	String responseBody = utils.getResponseBody(response);
	
	JsonNode detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	//TODO:::add validation here
	Assert.assertEquals(response.getStatusLine().getStatusCode(), 200, "Response code is not equal");

	
	//to get the list of sequence detector
	DetectSequenceDto dsdto = new DetectSequenceDto();
	dsdto.setLimit(20);
	dsdto.setOffset(0);
	dsdto.setRequestType("list");
	Reporter.log("Sequence detector :::::: "+utils.marshall(dsdto));
	ElasticSearchLogs esLogs = new ElasticSearchLogs();
	StringEntity se =new StringEntity(utils.marshall(dsdto));
	response = esLogs.detectsequences(restClient, buildCookieHeaders(suiteDataB.getCSRFToken(),suiteDataB.getSessionID()), se, suiteDataB);
	responseBody = getResponseBody(response);
	
	detect_sequences = new ObjectMapper().readTree(getJSONValue(responseBody.toString(), "detect_sequences"));
	
	
	 String id=null;
	 if (detect_sequences.isArray()) {
		 for (final JsonNode objNode : detect_sequences) {
			String sequenceName =	getJSONValue(objNode.toString(), "name");
			Assert.assertNotEquals(sequenceName,name, "SEQUENCE NAME IS SAME, sequence got created in 2 tenants");
			}
		 
	 }
		}

}
