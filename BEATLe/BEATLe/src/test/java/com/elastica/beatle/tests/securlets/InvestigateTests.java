package com.elastica.beatle.tests.securlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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



public class InvestigateTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	
	HashMap<String, String> accountType = new HashMap<String, String>();
	HashMap<String, String> activityMapQuick = new HashMap<String, String>();
	HashMap<String, String> filters = new HashMap<String, String>();
	HashMap<String, String> facets = new HashMap<String, String>();
	ArrayList<String> missing = new ArrayList<String>();
	String apiHost ; 
	
	
	static long saleforceCount ;
	int recordMaxSize = 500;
	

	public InvestigateTests() throws Exception {
		super();
		esQueryBuilder = new ESQueryBuilder();
		//Populate facets
		facets.put("histoGreen",  "informational");
		facets.put("histoOrange", "warning");
		facets.put("histoRed",    "critical");
		facets.put("histoYellow", "error");
		
		
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
	
	

	
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "AccountTypeFilter", groups={"FILTERS", "REGRESSION", "P1"})
	public void dashboardAccountTypeFilters(String tsfrom, String tsto, String accountType, int from, int size) throws Exception {
		LogUtils.logTestDescription("Retrieve the investigate logs filtered by account type:"+ accountType +" and verify the same.");
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("account_type", accountType);
		
		if (accountType.equals("account_type")) {
			missing.clear();
			missing.add(accountType);
			//If missing object is added, clear the term map
			termmap.clear();
		}

		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
														facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
														apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, size, "investigate"); 
		
		String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
		JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
		if(allHits.size() > 0) {
			for (int i = 0; i < allHits.size(); i++) {
				String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
				String actualAccountType = getJSONValue(source, "account_type");
				
				if (!accountType.equals("account_type")) {
					String actualValue = actualAccountType.replaceAll("^\"|\"$", "");
					CustomAssertion.assertEquals(actualValue, accountType, "Actual account type :"+ actualAccountType + 
							" is not matching with the expected account type :" +accountType );
				} else {
					CustomAssertion.assertTrue(actualAccountType == null, "account_type is null",  "account_type is not null" );
				}
			}
		}
		
		
	}
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "SeverityFilter", groups={"FILTERS", "REGRESSION", "P1"})
	public void dashboardSeverityTypeFilters(String tsfrom, String tsto, String severityType, int from, int size) throws Exception {
		LogUtils.logTestDescription("Retrieve the investigate logs filtered by severity type:"+ severityType +" and verify the same.");
		
		HashMap<String, String> termmap = new HashMap<String, String>();
		termmap.put("severity", severityType);
		
		if (accountType.equals("severity")) {
			missing.clear();
			missing.add(severityType);
			//If missing object is added, clear the term map
			termmap.clear();
		}

		String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
														facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
														apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, size, "investigate"); 

		String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
		JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
		
		//All logs should have severity. If missing then bug. So add assert
		if (severityType.equals("severity")) {
			CustomAssertion.assertTrue(allHits.size() == 0, "severity missing activities are zero",  "severity missing activities are not zero" );
		}
		
		if(allHits.size() > 0) {
			for (int i = 0; i < allHits.size(); i++) {
				String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
				String actualType = getJSONValue(source, "severity");
				
				if (!severityType.equals("severity")) {
					String actualValue = actualType.replaceAll("^\"|\"$", "");
					CustomAssertion.assertEquals(actualValue, severityType, "Actual severity type :"+ actualValue + 
																				" is not matching with the expected severity type :" + severityType );
				} 
			}
		}
	}
	
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "ServiceFilter", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardServiceTypeFilters(String tsfrom, String tsto, String servicename) throws Exception {
		LogUtils.logTestDescription("Retrieve the investigate logs filtered by service type:"+ servicename +" and verify the same.");

		try {
			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("facility", servicename);
			apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();

			Reporter.log("Get the total number of activity logs for the service "+ servicename, true);

			// Get only the total count of records returned for the specific service
			HashMap<String, HashMap<String, String>> serviceLogCountMap =  getInvestigateLogs(tsfrom, tsto, termmap);
			long totalCount = Long.parseLong(serviceLogCountMap.get("facility").get(servicename));
			Reporter.log("Total number of activity logs for the service "+ servicename +"is :"+totalCount, true);

			// Fetch all the activity logs from 0 incremented by the record count and verify
			Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


			for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

				try {
					String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
							facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

					String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
					JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
					if(allHits.size() > 0) {
						for (int i = 0; i < allHits.size(); i++) {
							String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
							String actualfacility = getJSONValue(source, "facility");
							String actualValue = actualfacility.replaceAll("^\"|\"$", "");
							CustomAssertion.assertEquals(actualValue, servicename, "Actual facility :"+ actualValue + 
									" is not matching with the expected facility :" +servicename );
						}
					}
				} catch(Exception e) {

				}

				//Sleep for some time before fetching again
				sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			}
		}
		catch(NumberFormatException e) {

		}
	}
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "InstanceFilter", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardInstanceFilters(String tsfrom, String tsto)  throws Exception {
		HashMap<String, String> notermmap = new HashMap<String, String>();
		notermmap.put("instance", "");

		// Get only the total count of records returned for all the instances
		// As we don't know the instance, get the available instances and their counts
		HashMap<String, HashMap<String, String>> logCountMap =  getInvestigateLogs(tsfrom, tsto, notermmap);
		HashMap<String, String> instanceMap = logCountMap.get("instance");

		apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Iterate over the instance and its count
		for (Map.Entry<String, String> instance : instanceMap.entrySet()) {

			long totalCount = Long.parseLong(instance.getValue());
			Reporter.log("Total number of activity logs for the instance "+ instance.getKey() +"is :"+totalCount, true);
			LogUtils.logTestDescription("Retrieve the investigate logs filtered by instance:"+ instance.getKey() +" and verify the same.");
			missing.clear();
			if (!instance.getKey().equals("missing")) {
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("instance", instance.getKey());
				Reporter.log("Get the total number of activity logs for the instance "+ instance.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, "instance");
								String actualValue = actualInstance.replaceAll("^\"|\"$", "");
								CustomAssertion.assertEquals(actualValue, instance.getKey(), "Actual instance :"+ actualValue + 
										" is not matching with the expected instance :" +instance.getKey() );
							}
						}
					} catch(Exception e) {

					}
				}
			} else {

				HashMap<String, String> termmap = new HashMap<String, String>();				
				missing.add("instance");

				Reporter.log("Get the total number of activity logs with instance "+ instance.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, "instance");
								CustomAssertion.assertTrue(actualInstance == null, "Instance is null",  "Instance is not null" );
							}
						}
					} catch(Exception e) {

					}
				}
			}
		}
	}
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "UserFilter", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardUserFilters(String tsfrom, String tsto)  throws Exception {
		HashMap<String, String> nousermap = new HashMap<String, String>();
		nousermap.put("user", "");

		// Get only the total count of records returned for all the users
		// As we don't know the users, get the available users and their counts
		HashMap<String, HashMap<String, String>> logCountMap =  getInvestigateLogs(tsfrom, tsto, nousermap);
		HashMap<String, String> userMap = logCountMap.get("user");


		sleep(CommonConstants.THIRTY_SECONDS_SLEEP);

		apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Iterate over the instance and its count
		for (Map.Entry<String, String> user : userMap.entrySet()) {

			long totalCount = Long.parseLong(user.getValue());
			Reporter.log("Total number of activity logs for the user "+ user.getKey() +"is :"+totalCount, true);
			LogUtils.logTestDescription("Retrieve the investigate logs filtered by user:"+ user.getKey() +" and verify the same.");
			missing.clear();

			HashMap<String, String> termmap = new HashMap<String, String>();
			termmap.put("user", user.getKey());
			Reporter.log("Get the total number of activity logs for the user "+ user.getKey(), true);

			// Fetch all the activity logs from 0 incremented by the record count and verify
			Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);

			for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

				try {
					String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
							facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
							apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

					String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
					JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
					if(allHits.size() > 0) {
						for (int i = 0; i < allHits.size(); i++) {
							String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
							String actualUser = getJSONValue(source, "user");
							String actualValue = actualUser.replaceAll("^\"|\"$", "");
							CustomAssertion.assertEquals(actualValue, user.getKey(), "Actual user :"+ actualValue + 
									" is not matching with the expected user :" +user.getKey() );
						}
					}
				} catch(Exception e) {

				}
			}
		}
	}
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "AccountType", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardAccountTypeFilters(String tsfrom, String tsto)  throws Exception {
		HashMap<String, String> notermmap = new HashMap<String, String>();
		notermmap.put("account_type", "");

		// Get only the total count of records returned for all the account types
		// As we don't know the account type, get the available account types and their counts
		HashMap<String, HashMap<String, String>> logCountMap =  getInvestigateLogs(tsfrom, tsto, notermmap);
		HashMap<String, String> accountTypeMap = logCountMap.get("account_type");

		apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Iterate over the instance and its count
		for (Map.Entry<String, String> accountType : accountTypeMap.entrySet()) {

			long totalCount = Long.parseLong(accountType.getValue());
			Reporter.log("Total number of activity logs for the acoount type "+ accountType.getKey() +"is :"+totalCount, true);
			LogUtils.logTestDescription("Retrieve the investigate logs filtered by account type:"+ accountType.getKey() +" and verify the same.");
			missing.clear();
			if (!accountType.getKey().equals("missing")) {
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put("account_type", accountType.getKey());
				Reporter.log("Get the total number of activity logs for the accountType "+ accountType.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, "account_type");
								String actualValue = actualInstance.replaceAll("^\"|\"$", "");
								CustomAssertion.assertEquals(actualValue, accountType.getKey(), "Actual account type :"+ actualValue + 
										" is not matching with the expected account type :" +accountType.getKey() );
							}
						}
					} catch(Exception e) {

					}
				}
			} else {

				HashMap<String, String> termmap = new HashMap<String, String>();				
				missing.add("account_type");

				Reporter.log("Get the total number of activity logs with instance "+ accountType.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, "account_type");
								CustomAssertion.assertTrue(actualInstance == null, "Account type is null",  "Account type is not null" );
							}
						}
					} catch(Exception e) {

					}
				}
			}
		}
	}
	
	
	
	@Test(dataProviderClass = InvestigateDataProvider.class, dataProvider = "DashboardFilter", groups={"DASHBOARD_FILTER", "FILTERS", "REGRESSION", "P1"})
	public void dashboardFilters(String tsfrom, String tsto, String filterName)  throws Exception {
		HashMap<String, String> notermmap = new HashMap<String, String>();
		notermmap.put(filterName, "");

		// Get only the total count of records returned for all the account types
		// As we don't know the account type, get the available account types and their counts
		HashMap<String, HashMap<String, String>> logCountMap =  getInvestigateLogs(tsfrom, tsto, notermmap);
		HashMap<String, String> filterTypeMap = logCountMap.get(filterName);

		apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
		//Iterate over the instance and its count
		for (Map.Entry<String, String> filterType : filterTypeMap.entrySet()) {

			long totalCount = Long.parseLong(filterType.getValue());
			Reporter.log("Total number of activity logs for the filter name "+ filterType.getKey() +"is :"+totalCount, true);
			LogUtils.logTestDescription("Retrieve the investigate logs filtered by "+ filterName +" filter name:"+ filterType.getKey() +" and verify the same.");
			missing.clear();
			if (!filterType.getKey().equals("missing")) {
				HashMap<String, String> termmap = new HashMap<String, String>();
				termmap.put(filterName, filterType.getKey());
				Reporter.log("Get the total number of activity logs for the filter type "+ filterType.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, filterName);
								String actualValue = actualInstance.replaceAll("^\"|\"$", "");
								CustomAssertion.assertEquals(actualValue, filterType.getKey(), "Actual filter type :"+ actualValue + 
										" is not matching with the expected filter type :" +filterType.getKey() );
							}
						}
					} catch(Exception e) {

					}
				}
			} else {

				HashMap<String, String> termmap = new HashMap<String, String>();				
				missing.add(filterName);

				Reporter.log("Get the total number of activity logs with filter type "+ filterType.getKey(), true);


				// Fetch all the activity logs from 0 incremented by the record count and verify
				Reporter.log("Fetch all the records from 0 to "+totalCount +"in a batch of "+ recordMaxSize + " and verifying" , true);


				for (long from = 0, to = totalCount; from < to;  from+=recordMaxSize) {
					// Fetch all the activity logs from 0 incremented by the record count and verify
					Reporter.log("Fetch all the records from "+ from + " to " + (from + recordMaxSize) + " and verifying" , true);

					try {
						String results = getInvestigateLogsWithFacets(tsfrom, tsto, facility.Elastica.name(), termmap, missing,
								facets, "minute", "+5:30", filters, suiteData.getUsername().toLowerCase(), 
								apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), from, recordMaxSize, "investigate"); 

						String hits = getJSONValue(getJSONValue(results, "hits"), "hits");
						JSONArray allHits = (JSONArray) new JSONTokener(hits).nextValue();
						if(allHits.size() > 0) {
							for (int i = 0; i < allHits.size(); i++) {
								String source = getJSONValue(allHits.getJSONObject(i).toString(), "_source");
								String actualInstance = getJSONValue(source, filterName);
								CustomAssertion.assertTrue(actualInstance == null, filterName +" is null",  filterName+" is not null" );
							}
						}
					} catch(Exception e) {

					}
				}
			}
		}
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
	
	
	