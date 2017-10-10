package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DetectPreferencesTests extends DetectUtils{
	
	private static final String APPLICATION_JSON = "application/json";
	
	@DataProvider
    public Object[][] getData()  {
	
	try {
		HttpResponse	resp = getDetectAttributes();
	
	String responseBody = getResponseBody(resp);
	JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray("detect_attributes");	
	Object[][] atrributesDP = new Object[getResponseArray.length()][2];
	for(int index = 0; index< getResponseArray.length();index++){
		JSONObject attributeObject = getResponseArray.getJSONObject(index);
		String resourceUri = (String) attributeObject.get("resource_uri");
		atrributesDP[index][0] = resourceUri;
		atrributesDP[index][1] = attributeObject;
		 
	}
	return atrributesDP;
	}
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("Exception in getting data provider:::"+e.getMessage());
	}
	 return null;
    }
	

	
		@Test(description="This test is for get the list of the dtetct attributes")
	public void detectAttributes(Method method) throws Exception {
		 System.out.println("Test Exceution started :::detectAttributes");
		 HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		System.out.println("Response Body Printing....... : " + responseBody);
		
		Assert.assertNotNull(responseBody, "Responce body is not null");
		Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
		System.out.println("Test Exceution ended :::detectAttributes");
	}
	
	
		@Test(dataProvider="getData", threadPoolSize =10,
			description="this test is for update detect attributes (enable/disable attribute).")
	public void updateDetectAttributesEnabledDisabledTest(String resourceUri, JSONObject attributeObject) throws Exception {
		
		 System.out.println("Test Exceution started :::updateDetectAttributesDurationAndConfidence");
		 Assert.assertNotNull(resourceUri, "Responce body is null");
		

		String[] enabledValues = {"true", "fasle"};
			for (String enabled : enabledValues) {
				boolean value = Boolean.parseBoolean(enabled);

				attributeObject.put("enabled", value);
				System.out.println("enabled ::: :::"+value);
				URI getAttributesURI = postDetectattributes();
				StringEntity se = new StringEntity(attributeObject.toString());
				se.setContentType(APPLICATION_JSON);
				HttpResponse resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
			}

		}
		
		@Test(dataProvider="getData",threadPoolSize =10,
				description="this test is for update detect attributes (importance).")
		public void updateDetectAttributesImportance(String resourceUri, JSONObject attributeObject) throws Exception {
			
			 System.out.println("Test Exceution started :::updateDetectAttributesDurationAndConfidence");
			 Assert.assertNotNull(resourceUri, "Responce body is null");
			

			int[] importanceValues = {1,2,3,4};
				for (int importance : importanceValues) {

					attributeObject.put("importance", importance);
					System.out.println("importance ::: :::"+importance);
					URI getAttributesURI = postDetectattributes();
					StringEntity se = new StringEntity(attributeObject.toString());
					se.setContentType(APPLICATION_JSON);
					HttpResponse resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
					Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				}

			}

		
	
		
		
		
		@Test(dataProvider="getData",threadPoolSize =10,
				description="this test is for update detect attributes (duration for threshold attribute and confidence for behaviour attributs).")
		public void updateDetectAttributesDurationAndConfidence(String resourceUri, JSONObject attributeObject) throws Exception {
			HttpResponse resp =null;
			 System.out.println("Test Exceution started :::updateDetectAttributesDurationAndConfidence");
			 Assert.assertNotNull(resourceUri, "Responce body is null");
			
			String category = (String) attributeObject.get("category");
			if(category.equalsIgnoreCase("Threshold")){

				int[] windowValues = {1, 3, 5, 10};

				for (int window : windowValues) {


					attributeObject.put("window", window);
					System.out.println("window ::: :::"+window);
					URI getAttributesURI = postDetectattributes();
					StringEntity se = new StringEntity(attributeObject.toString());
					se.setContentType(APPLICATION_JSON);
					resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
					Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
					//Assert
					/*resp =  restClient.doGet(getAttributesURI, buildBasicHeaders(csfrToken, sessionID));
					Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
					String responseBody = getResponseBody(resp);
					JSONObject getResponseObject = new JSONObject(responseBody);
					int actual = (Integer) getResponseObject.get("window");
					System.out.println("actulal :::  "+ actual +"   expected :::  "+window);
					Assert.assertEquals(actual, window, "Response  is not equal");*/
				}

			}else if(category.equalsIgnoreCase("Behavior")){
				int[] confidenceValues = {1, 3, 5, 10};

				for (int confidence : confidenceValues) {
				attributeObject.put("confidence", confidence);
				System.out.println("confidence ::: :::"+confidence);
				URI getAttributesURI = postDetectattributes();
				StringEntity se = new StringEntity(attributeObject.toString());
				se.setContentType(APPLICATION_JSON);
				resp = restClient.doPost(getAttributesURI, buildCookieHeaders(), null, se);
				Assert.assertEquals(resp.getStatusLine().getStatusCode(), 200, "Response code is not equal");
				}
				

			}
			 System.out.println("Test Exceution ended :::updateDetectAttributesDurationAndConfidence");
		}
		
	
	
	
	
	}
