package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.ciq.dto.ContentChecks;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.InvestigateDataProvider;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.Source;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.AccessibleBy;
import com.universal.dtos.box.BoxCollaboration;
import com.universal.dtos.box.BoxFolder;
import com.universal.dtos.box.BoxGroup;
import com.universal.dtos.box.BoxMembership;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.BoxWeblink;
import com.universal.dtos.box.CollaborationInput;
import com.universal.dtos.box.Entry;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.box.FileUploadResponse;
import com.universal.dtos.box.GroupInput;
import com.universal.dtos.box.GroupList;
import com.universal.dtos.box.Item;
import com.universal.dtos.box.MembershipInput;
import com.universal.dtos.box.Parent;
import com.universal.dtos.box.SharedLink;
import com.universal.dtos.box.UserCollection;
import com.universal.dtos.box.WeblinkInput;

import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;



public class InvestigateDataExportTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	
	HashMap<String, String> accountType = new HashMap<String, String>();
	HashMap<String, String> activityMapQuick = new HashMap<String, String>();
	HashMap<String, String> filters = new HashMap<String, String>();
	HashMap<String, String> facets = new HashMap<String, String>();
	ArrayList<String> missing = new ArrayList<String>();
	String apiHost ; 
	
	
	static long saleforceCount ;
	int recordMaxSize = 500;
	

	public InvestigateDataExportTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		//Populate facets
		facets.put("histoGreen",  "informational");
		facets.put("histoOrange", "warning");
		facets.put("histoRed",    "critical");
		facets.put("histoYellow", "error");
		facets.put("histoLow",    "low");
		facets.put("histoMedium", "medium");
		facets.put("histoHigh",   "high");
		
		
		//Populate filters
		filters.put("user",  "1000");
		filters.put("facility",  "1000");
		filters.put("location",  "1000");
		filters.put("Object_type",  "1000");
		filters.put("Activity_type",  "1000");
		filters.put("account_type",  "1000");
		filters.put("browser",  "1000");
		filters.put("severity",  "1000");
		filters.put("device",  "1000");
		filters.put("device_mgmt",  "1000");
		filters.put("device_owner",  "1000");
		filters.put("device_compliance",  "1000");
		filters.put("__source",  "1000");
		filters.put("instance",  "1000");
			
	}	
	
	
	
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "ExportFilter", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardServiceTypeFiltersDataExport(String tsfrom, String tsto, String facility, String servicename, String exportFormat) throws Exception {
		LogUtils.logTestDescription("Export the investigate logs filtered by service type:"+ servicename +" and verify the same.");
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("facility", servicename);
		apiHost = "https://"+suiteData.getApiserverHostName();
		
		List<NameValuePair> headers = getHeaders();
		String payload = "";
		payload = esQueryBuilder.getInvestigateQueryWithFacets(tsfrom, tsto, facility, termmap, missing, facets,  "minute", "+5:30", 
				filters,  suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 5000, 
				"investigate",  tsfrom, tsto, exportFormat);

		LogUtils.logTestDescription("Export the "+ exportFormat + " logs to user email and check");
		
		String path = suiteData.getAPIMap().get("getActivityLogExport") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);
		
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("Response body:"+ responseBody, true);
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		CustomAssertion.assertTrue(responseBody.contains("true"), "Filelink sent to email", "File not sent");
		CustomAssertion.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK, "Not exported");
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		String query = exportFormat.toUpperCase() +" Log Export Request";
		String link = getEmailDownloadLink(query, null, null, null);
		
		Reporter.log("Link:"+ link, true );
		String source = System.getProperty("user.dir") + File.separator + "activityLog."+exportFormat.toLowerCase()+".gz";
		FileUtils.copyURLToFile(new URL(link), new File(source));
		String destination = System.getProperty("user.dir") + File.separator + "activityLog."+exportFormat.toLowerCase();
		uncompressgz(source, destination);
		
		int rows = getLineCountOfAFile(destination);
		
		Reporter.log("All rows size:" + rows, true);
		CustomAssertion.assertTrue(rows > 1, "Exported "+ exportFormat +" file has "+ rows + " rows  which is greater than zero", "Exported "+ exportFormat+ " has zero rows");
	}
	
	
	
	public HashMap<String, HashMap<String, String>> getInvestigateLogs(String starttime, String endtime, HashMap<String, String> termmap) throws Exception {
		
		starttime = (starttime != null) ? starttime : "7D";
		endtime   = (endtime   != null) ? endtime   : "0D";
		HashMap<String, String> emptyTerms =  new HashMap<String, String>();
		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		String results = getInvestigateLogsWithFacets(starttime, endtime, facility.Elastica.name(), emptyTerms, missing,
							facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 0, "investigate"); 
		//System.out.println("Results:"+results);
		
		JSONObject investigateLogs = new JSONObject(results);
		JSONObject allFacets = investigateLogs.getJSONObject("facets");
		HashMap<String, HashMap<String, String>> allFilters= new HashMap<String, HashMap<String, String>>();
		
		ArrayList<String> filterNames = new ArrayList<String>(termmap.keySet());
		
		for(String filterName : filterNames) {
			HashMap<String, String> filterValues = new HashMap<String, String>();
			JSONObject accountType = allFacets.getJSONObject(filterName);
			
			//If missing field is available, add it
			if (accountType.getString("missing") != null) {
				filterValues.put("missing", accountType.getString("missing") );
				Reporter.log("missing", true);
				Reporter.log(accountType.getString("missing"), true);
			}
			
			
			org.json.JSONArray jArray = accountType.getJSONArray("terms");
			//JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();

			for (int i = 0; i < jArray.length(); i++) {
				filterValues.put(jArray.getJSONObject(i).getString("term"), jArray.getJSONObject(i).getString("count"));
				Reporter.log(jArray.getJSONObject(i).getString("term"), true);
				Reporter.log(jArray.getJSONObject(i).getString("count"), true);
			}
			allFilters.put(filterName, filterValues);
		}
		
		return allFilters;
	}
	
	
	
	public void getInvestigateLogs(String starttime, String endtime) throws Exception {
		
		starttime = (starttime != null) ? starttime : "7D";
		endtime   = (endtime   != null) ? endtime   : "0D";
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		String results = getInvestigateLogsWithFacets(starttime, endtime, facility.Elastica.name(), termmap, missing,
				facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 0, "investigate"); 
		Thread.sleep(1000);		
		System.out.println("Results:"+results);
		
		JSONObject investigateLogs = new JSONObject(results);
		JSONObject allFacets = investigateLogs.getJSONObject("facets");
		
		
		HashMap<String, HashMap<String, String>> allFilters= new HashMap<String, HashMap<String, String>>();
		
		
		String[] filterNames = {"account_type", "severity", "facility", "user"};
		
		for(String filterName : filterNames) {
			JSONObject accountType = allFacets.getJSONObject(filterName);

			org.json.JSONArray jArray = accountType.getJSONArray("terms");
			//JSONArray jArray = (JSONArray) new JSONTokener(hits).nextValue();

			for (int i = 0; i < jArray.length(); i++) {
				System.out.println(jArray.getJSONObject(i).getString("term"));
				System.out.println(jArray.getJSONObject(i).getString("count"));
			}
		}
	}
	
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
		try {
			return rootNode.get(key).toString();
		} catch (NullPointerException e) {
			return null;
		}
	}
		
	
	@Test
	public void getSession() throws Exception {
		List<NameValuePair> headers = getHeaders();
		String path = suiteData.getAPIMap().get("getAdminSession") ;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(), path);
		Reporter.log("URI ::"+dataUri.toString(), true);

		HttpResponse response =  restClient.doPost(dataUri, headers, null, null);

		String responseBody = ClientUtil.getResponseBody(response);
		Reporter.log("ResponseBody:"+responseBody, true);
		Reporter.log("==============================================================================");
	}
}
	
	
	