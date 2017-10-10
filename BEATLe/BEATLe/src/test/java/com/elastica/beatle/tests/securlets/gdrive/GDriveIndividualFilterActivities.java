/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets.gdrive;

import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.assertTrue;
import org.testng.Reporter;
import org.testng.annotations.Test;

/**
 *
 * @author rahulkumar
 */
public class GDriveIndividualFilterActivities extends GDriveUtils{
    
    String parentNode = "$.hits.hits[*].source";
    // Activity Filters...
    @Test(groups={"FILTERS", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "ActivityTypeFilter")
    public void dashboardActivityTypeFilters(String activityType) throws Exception {
        ForensicSearchResults logs;
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + activityType);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        termmap.put("Activity_type", activityType);

        for (int retry = 0; retry < 1; retry++) {

            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(),
                        apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
                List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log(" Total Number of Results : " + fetchAllKeys.size(), true);
                for (Map<String, Object> map : fetchAllKeys) {
                    String value = map.get("Activity_type").toString();
                    CustomAssertion.assertTrue(value.equals(activityType), "ActivityType is " + activityType, "ActivityType is not " + activityType);
                }
                boolean validateNullValues = RawJsonParser.validateNullValues(fetchAllKeys);
                assertTrue(validateNullValues, "Null Values Found in the Response");
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "ActivityType " + activityType + " related messages are not present");
            } catch (Exception e) {
            }
        }
    }

    @Test(groups={"FILTERS", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "SeverityFilter")
    public void dashboardSeverityTypeFilters(String severityType) throws Exception {
        ForensicSearchResults logs;
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + severityType);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        termmap.put("severity", severityType);
        termmap.put("__source", "API");
        for (int retry = 0; retry < 1; retry++) {
            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
                List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log(" Total Number of Results : " + fetchAllKeys.size(), true);
                for (Map<String, Object> map : fetchAllKeys) {
                    String value = map.get("severity").toString();
                    CustomAssertion.assertTrue(value.equals(severityType), "Severity Type is " + severityType, "Severity Type is not " + severityType);
                }
                boolean validateNullValues = RawJsonParser.validateNullValues(fetchAllKeys);
                assertTrue(validateNullValues, "Null Values Found in the Response");
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "SeverityType " + severityType + " related messages are not present");
            } catch (Exception e) {
                Reporter.log("Exception Found :" + e.getLocalizedMessage(), true);
            }
        }
    }

    @Test(groups={"FILTERS", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "LocationFilter")
    public void dashboardLocationTypeFilters(String severityType, String location) throws Exception {
        LogUtils.logTestDescription("Retrieve the activities and filter them by name:" + severityType + " and location:" + location);
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        termmap.put("severity", severityType);
        termmap.put("__source", "API");
        termmap.put("location", location);
        termmap.put("user", getRegressionSpecificSuitParameters("userName"));
        for (int retry = 0; retry < 1; retry++) {
            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
                List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                Reporter.log("Total Number of Results : " + fetchAllKeys.size(), true);
                for (Map<String, Object> map : fetchAllKeys) {
                    String value = map.get("severity").toString();
                    CustomAssertion.assertTrue(value.equals(severityType), "Severity Type is " + severityType, "Severity Type is not " + severityType);
                }
                boolean validateNullValues = RawJsonParser.validateNullValues(fetchAllKeys);
                assertTrue(validateNullValues, "Null Values Found in the Response");
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "SeverityType " + severityType + " related messages are not present");
            } catch (Exception e) {
            }
        }
    }

    @Test(groups={"FILTERS", "P2"},dataProviderClass = GDriveDataProvider.class, dataProvider = "ObjectTypeFilter")
    public void dashboardObjectTypeFilters(String objType) throws Exception {
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", "Google Drive");
        termmap.put("Object_type", objType);
        LogUtils.logTestDescription("Retrieve the objecttype and filter them by name:" + objType);
        List<String> keyList = new ArrayList();
        keyList.add("Object_type");
        for (int retry = 0; retry < 1; retry++) {
            try {
                String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
                //Fetch the activity logs from yesterday to tomorrow and limited to 500
                //Get file related logs
                String responseBody = getInvestigateLogs(-18000, 10, SaasApp_GoogleApps, termmap, suiteData.getUsername().toLowerCase(),
                        apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, SaasApp_GoogleApps);
                System.out.println("### Response Body :" + responseBody);
                List<Map<String, Object>> fetchAllKeys = RawJsonParser.fetchAllKeys(responseBody, parentNode);
                System.out.println("Total Size :" + fetchAllKeys.size());
                for (Map<String, Object> map : fetchAllKeys) {
                    String value = map.get("Object_type").toString();
                    CustomAssertion.assertTrue(value.equals(objType), "Object Type is " + objType, "Object Type is not " + objType);
                }
                boolean validateNullValues = RawJsonParser.validateNullValues(fetchAllKeys);
                assertTrue(validateNullValues, "Null Values Found in the Response");
                int totalCount = Integer.parseInt(RawJsonParser.getSingleKey(responseBody, "$.hits.total"));
                assertTrue(totalCount > 0, "ObjectType " + objType + " related messages are not present");
            } catch (Exception e) {
                Reporter.log("Expection Found " + e.getLocalizedMessage(), true);
                sleep(20);
            }

        }

    }

   
    
}
