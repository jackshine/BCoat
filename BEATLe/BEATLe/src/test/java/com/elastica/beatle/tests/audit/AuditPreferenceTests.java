package com.elastica.beatle.tests.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.audit.AuditFunctions;
import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestUtils;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

/**
 * @author anuvrath
 *
 */
public class AuditPreferenceTests extends  AuditInitializeTests  {
	
	private Client restClient;
	String preferenceRespString;
	
	public AuditPreferenceTests() {
		restClient = new Client();
	}		
	
	@Test()
	public void testChangeExistingValidPreferenceWeights() throws Exception {
		
		// Get existing preferences
		HttpResponse existingPreferences = AuditFunctions.getAuditPreferences(restClient, null);
		Assert.assertEquals(existingPreferences.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject preferenceMetaObjects = new JSONObject(ClientUtil.getResponseBody(existingPreferences)).getJSONObject("meta");								
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("limit", String.valueOf(preferenceMetaObjects.getInt("total_count"))));
		queryParams.add(new BasicNameValuePair("offset", String.valueOf(0)));
		existingPreferences = AuditFunctions.getAuditPreferences(restClient, queryParams);
		preferenceRespString = ClientUtil.getResponseBody(existingPreferences);		
		
		// change the attribute_weight for all the preferences
		Integer[] attr_weight = {0,1,3,8};		
		JSONArray existingPreferenceObject  = new JSONObject(preferenceRespString).getJSONArray("objects");
		JSONArray postBodyJSON = new JSONArray();
		for(int index = 0; index < existingPreferenceObject.length();index++){
			JSONObject object = existingPreferenceObject.getJSONObject(index);
			object.remove("attribute_weight");
			object.put("attribute_weight", attr_weight[new Random().nextInt(attr_weight.length)]);			
			postBodyJSON.put(object);			
		}		
		String postBody = new JSONObject().put("objects",postBodyJSON).toString();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(postBody));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED);			
		
		// Get the preferences again to validate
		HttpResponse preferenceGetResponse = AuditFunctions.getAuditPreferences(restClient, queryParams);
		Assert.assertEquals(preferenceGetResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);						
		String getreponseString = ClientUtil.getResponseBody(preferenceGetResponse);
		JSONArray getResponseArray = new JSONObject(getreponseString).getJSONArray("objects");		
		assertJsonEquals(getResponseArray.toString(), new JSONObject(postBody).getJSONArray("objects").toString());				
		
		for(int index = 0; index< getResponseArray.length();index++){
			JSONObject attributeObject = getResponseArray.getJSONObject(index);
			Assert.assertFalse(((String)attributeObject.get("attribute_category")).isEmpty());
			Assert.assertFalse(((String)attributeObject.getString("attribute_risk_bucket")).isEmpty(),"Risk Bucket is empty");
			Assert.assertFalse(((String)attributeObject.get("attribute_name")).isEmpty(),"Attribute Name is empty");
			Assert.assertFalse(((String)attributeObject.get("id")).isEmpty(),"id is empty");
			Assert.assertFalse(((String)attributeObject.get("resource_uri")).isEmpty(),"resource_uri is empty");			
			Assert.assertNotNull(attributeObject.get("attribute_category"), "attribute category is null");
			Assert.assertNotNull(attributeObject.get("attribute_id"),"attribute id is null");
			Assert.assertNotNull(attributeObject.get("attribute_name"),"name is null");
			Assert.assertNotNull(attributeObject.get("id"),"id is null");
			Assert.assertNotNull(attributeObject.get("resource_uri"),"resource_uri is null");						
		}		
		String temp = AuditTestUtils.getAuditPreferenceJSON().replace("{replaceIgnoreParamInt}", "\"${json-unit.ignore}\"").replace("{replaceIgnoreParam}", "${json-unit.ignore}");
		//commented temporarly
		//assertThatJson(getreponseString).when(IGNORING_ARRAY_ORDER).isEqualTo(temp);
	}
	
	@Test
	public void testSettingInvalidAttribute_ID() throws Exception {
		// Get existing preferences
		HttpResponse existingPreferences = AuditFunctions.getAuditPreferences(restClient, null);
		Assert.assertEquals(existingPreferences.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject preferenceMetaObjects = new JSONObject(ClientUtil.getResponseBody(existingPreferences)).getJSONObject("meta");								
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("limit", String.valueOf(preferenceMetaObjects.getInt("total_count"))));
		queryParams.add(new BasicNameValuePair("offset", String.valueOf(0)));
		existingPreferences = AuditFunctions.getAuditPreferences(restClient, queryParams);
		String preferenceRespString = ClientUtil.getResponseBody(existingPreferences);
		
		JSONObject existingPreferenceObject  = new JSONObject(preferenceRespString).getJSONArray("objects").getJSONObject(0);
		existingPreferenceObject.remove("attribute_id");
		existingPreferenceObject.put("attribute_id", 999);
		String postBody = new JSONObject().put("objects", new JSONArray().put(existingPreferenceObject)).toString();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(postBody));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_BAD_REQUEST);		
	}
	
	@DataProvider(name="attribute_weight")
	public Object[][] AttributeWeightProvider(){
		Object[][] data = { { -1 },{ 10 }};
		return data;
		
	}
	
	@Test(dataProvider="attribute_weight")
	public void testingSettingInvalidAttributeWeight(int attribute_weight) throws Exception { 
		// Get existing preferences
		HttpResponse existingPreferences = AuditFunctions.getAuditPreferences(restClient, null);
		Assert.assertEquals(existingPreferences.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject preferenceMetaObjects = new JSONObject(ClientUtil.getResponseBody(existingPreferences)).getJSONObject("meta");								
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("limit", String.valueOf(preferenceMetaObjects.getInt("total_count"))));
		queryParams.add(new BasicNameValuePair("offset", String.valueOf(0)));
		existingPreferences = AuditFunctions.getAuditPreferences(restClient, queryParams);
		String preferenceRespString = ClientUtil.getResponseBody(existingPreferences);
		
		JSONObject existingPreferenceObject  = new JSONObject(preferenceRespString).getJSONArray("objects").getJSONObject(0);
		existingPreferenceObject.remove("attribute_weight");
		existingPreferenceObject.put("attribute_weight", attribute_weight);
		String postBody = new JSONObject().put("objects", new JSONArray().put(existingPreferenceObject)).toString();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(postBody));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	public void testingSettingInvalidID() throws Exception {
		// Get existing preferences
		HttpResponse existingPreferences = AuditFunctions.getAuditPreferences(restClient, null);
		Assert.assertEquals(existingPreferences.getStatusLine().getStatusCode(), HttpStatus.SC_OK);		
		JSONObject preferenceMetaObjects = new JSONObject(ClientUtil.getResponseBody(existingPreferences)).getJSONObject("meta");								
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();		
		queryParams.add(new BasicNameValuePair("limit", String.valueOf(preferenceMetaObjects.getInt("total_count"))));
		queryParams.add(new BasicNameValuePair("offset", String.valueOf(0)));
		existingPreferences = AuditFunctions.getAuditPreferences(restClient, queryParams);
		String preferenceRespString = ClientUtil.getResponseBody(existingPreferences);
				
		JSONObject existingPreferenceObject  = new JSONObject(preferenceRespString).getJSONArray("objects").getJSONObject(0);
		existingPreferenceObject.remove("id");
		existingPreferenceObject.put("id", "InvalidID");
		String postBody = new JSONObject().put("objects", new JSONArray().put(existingPreferenceObject)).toString();
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(postBody));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	
	@AfterClass(alwaysRun = true)
	public void resetPreferences() throws Exception {
		HttpResponse preferenceChangeResponse = AuditFunctions.setAuditPreferences(restClient, new StringEntity(preferenceRespString));
		Assert.assertEquals(preferenceChangeResponse.getStatusLine().getStatusCode(), HttpStatus.SC_ACCEPTED);
	}
}
