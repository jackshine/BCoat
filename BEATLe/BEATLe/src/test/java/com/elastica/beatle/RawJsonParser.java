/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsfr.json.JsonSurfer;
import org.testng.Assert;
import org.testng.Reporter;

public class RawJsonParser {

    private static final JsonSurfer surfer = JsonSurfer.simple();
    static String NotEmpty = "NOT_EMPTY";// not null...
    static String NotZero = "NON_ZERO";// not null...
    
    
    public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        if (httpjsonResponse == null) {
            return false;
        }
        
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey,List<String> ignoredKeyList) {
        if (httpjsonResponse == null) {
            return false;
        }   
        boolean resultFound = false;
        if (matchMapFromMapList(removeIgonoreKeys(fetchAllKeys(httpjsonResponse, parentNode),ignoredKeyList), removeIgnoredValues(expectedResult),uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndPartialValuesIgnoredNullCheck(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("### Note : NULL Validation is Ignored in Assertion.....###",true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(removeNullFromListOfMap(fetchAllKeys(httpjsonResponse, parentNode)), expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }
    
    /**
     * @param httpjsonResponse
     * @param parentNode Ex. $.hits.hits[*].source , it will look into all the
     * hits..
     * @param expectedResult Map where we put expected Json keys/fields and
     * values..
     * @return true if all keys and values are present in the node list else
     * false...
     */
    
    public static boolean findExpectedKeysAndValuesIgnoreNullCheck(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("### Note : NULL Validation is Ignored in Assertion.....###",true);
        if (httpjsonResponse == null) {
            return false;
        } 
        boolean resultFound = false;
        if (matchMapFromMapList(removeNullFromListOfMap(fetchAllKeys(httpjsonResponse, parentNode)), expectedResult,uniqueKey,true,false)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,"message",true,false)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        Map<String, Object> removeIgnoredValues = removeIgnoredValues(expectedResult);
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), removeIgnoredValues,uniqueKey,true,false)) {
            return true;
        }
        return resultFound;
    }
    
    public static boolean findExpectedKeysAndValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey,List<String> ignoredKeyList) {
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(removeIgonoreKeys(fetchAllKeys(httpjsonResponse, parentNode),ignoredKeyList), removeIgnoredValues(expectedResult),uniqueKey,true,false)) {
            return true;
        }
        return resultFound;
    }
    
    private static List <Map<String, Object>> removeIgonoreKeys(List <Map<String, Object>> mapList,List<String> ignoredKeyList){
        List <Map<String, Object>> resultMapList=new ArrayList<>();
        for (Map<String, Object> resultMapList1 : mapList) {  
            for (String ignoredKey : ignoredKeyList) {
                resultMapList1.remove(ignoredKey);
            } 
            resultMapList.add(resultMapList1);    
        } 
         return resultMapList;
    }
    
    private static List <Map<String, Object>> removeNullFromListOfMap(List <Map<String, Object>> mapList){
        List <Map<String, Object>> resultMapList=new ArrayList<>();
        for (Map<String, Object> resultMapList1 : mapList) {
            resultMapList.add(removeNullValues(resultMapList1));
        }
        return resultMapList;
    }
    private static Map<String, Object> removeNullValues(final Map<String, Object> expectedResult) {
        Map<String, Object> expectedResultRemovedIgnoreItems = new HashMap<>();
        Reporter.log("####  ------------------  NULL Found in the Actual Result  ---------------------------   ####",true);
        for (Map.Entry<String, Object> entrySet : expectedResult.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (value == null) {        
                Reporter.log("Key :"+key + "Value :"+value,true);
            }
            else{
               expectedResultRemovedIgnoreItems.put(key, value.toString().trim()); 
            }
        }
        Reporter.log("####  ----------------------------------------------------------------------------  #####",true);
        return expectedResultRemovedIgnoreItems;
    }

    private static Map<String, Object> removeIgnoredValues(final Map<String, Object> expectedResult) {
        Map<String, Object> expectedResultRemovedIgnoreItems = new HashMap<>();
        for (Map.Entry<String, Object> entrySet : expectedResult.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if(value == null){
                expectedResultRemovedIgnoreItems.put(key, value);
            }
            else if (value != null) {
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                if (name.contains("String")) {
                    if (!value.toString().equalsIgnoreCase("Ignore")) {
                        expectedResultRemovedIgnoreItems.put(key, value.toString().trim());
                    }
                }
            }
        }
        return expectedResultRemovedIgnoreItems;
    }
    
    

    /**
     * @param total_result ...List of Map
     * @param expectedResult .. Expected Map...
     * @param uniqueKey
     * @param nullCheck
     * @return if Expected Map Found then true, else return false..
     */
    public static boolean matchMapFromMapList(List<Map<String, Object>> total_result, final Map<String, Object> expectedResult, String uniqueKey,boolean nullCheck,boolean partialSearch) {
        Reporter.log("==================================================================================",true);
        Reporter.log("Expected Result :---", true);
        printMap(expectedResult);
        Reporter.log("==================================================================================",true);
        boolean validationResult=true;
        Map<String, Object> foundResult = null;
        List<String> keyList = Arrays.asList(expectedResult.keySet().toArray(new String[expectedResult.size()])); 
        Map<String, Object> findMatchingMapInMapList = null;
        if (partialSearch) {
            findMatchingMapInMapList = findPartialMatchingMapInMapList(total_result, uniqueKey, expectedResult);
        } else {
            findMatchingMapInMapList = findMatchingMapInMapList(total_result, uniqueKey, expectedResult);
        }
        if(findMatchingMapInMapList!=null){
            int count=1;
            Reporter.log("==================================================================================",true);
            Reporter.log("!! Result Found !! Actual Result with Null & Value Check :---",true);
            for (String key_ExpectedResult : keyList) {  
                if(!findMatchingMapInMapList.containsKey(key_ExpectedResult)){
                  Reporter.log("Key : "+key_ExpectedResult +" $$$$ NOT FOUND IN ACTUAL RESULT $$$ ",true); 
                  validationResult=false;
                }   
            }
            Reporter.log("==================================================================================",true);
            for (Map.Entry<String, Object> entrySet : findMatchingMapInMapList.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                if (keyList.contains(key)) {
                    if (value == null && expectedResult.get(key) == null) {
                        Reporter.log(count++ + ")  " + key + " : " + value + " ## <Expected NULL> Passed", true);
                    } else if (value == null && expectedResult.get(key) != null) {
                        Reporter.log("===========================================================================", true);
                        Reporter.log(count++ + ")  " + key + " : " + value + " ## <Expected NULL> FAILED..., Expected NULL", true);
                        Reporter.log("===========================================================================", true);
                        validationResult=false;
                    } else if (expectedResult.get(key) == null && value != null) {
                        Reporter.log("===========================================================================", true);
                        Reporter.log(count++ + ")  " + key + " : " + value + " ## <Expected NULL> FAILED..., Expected NULL", true);
                        Reporter.log("===========================================================================", true);
                        validationResult=false;
                    }
                   else if(expectedResult.get(key).equals(NotEmpty)){
                        if(nullValidate(value)){
                           Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true); 
                        }
                        else{
                          Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true); 
                          if(nullCheck){
                          validationResult=false;
                          }
                        }
                    } 
                    else if (expectedResult.get(key).equals(NotZero)) {
                        if (nonZero(value)) {
                            Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);
                        } else {
                            Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true);
                            if (nullCheck) {
                                validationResult = false;
                            }
                        }
                    }
                    else if (partialSearch) {
                        if (value.toString().contains(expectedResult.get(key).toString())) {
                            if(value.equals(expectedResult.get(key)))  {
                               Reporter.log(count++ + ">>  " + key + ": " + value.toString() + "## Expected :"+expectedResult.get(key).toString()+" < ### Value Check #### > !! PASSED !!", true); 
                            } 
                            else{
                            Reporter.log(count++ + ">>  " + key + ": " + value.toString() + " ## Expected :"+expectedResult.get(key).toString()+" < ## Partial Value Check ## > !! PASSED !!", true);
                            }
                        } else {
                            Reporter.log("===========================================================================", true);
                            Reporter.log(count++ + ")  " + key + ": " + value.toString() +  " ## Expected :"+expectedResult.get(key).toString()+ " <### Partial Value Check ####> $$ FAILED $$", true);
                            Reporter.log("===========================================================================", true);
                            if (nullCheck) {
                                validationResult = false;
                            }
                        }
                    } else if (value.equals(expectedResult.get(key))) {
                        Reporter.log(count++ + ">>  " + key + ": " + value.toString() + " <Value Check> !! PASSED !!", true);
                    }
                    else{
                       Reporter.log("===========================================================================",true);
                       Reporter.log(count++ +">>  "+key +": "+value.toString() +"Expected :"+expectedResult.get(key) +" <Value Check> $$ FAILED $$",true); 
                       Reporter.log("===========================================================================",true);
                       if(nullCheck){
                       validationResult=false;
                       }
                    }
                }
                else{
                    if (nullValidate(value)) {
                        Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);
                    } else {
                        Reporter.log("===========================================================================",true);
                        Reporter.log(count++ + ">>  " + key + " : " + value + " ## <Null Check> FAILED", true); 
                        Reporter.log("===========================================================================",true);
                        if(nullCheck){
                        validationResult=false;
                            }
                        }
                    }
                }
            }
        else{
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(uniqueKey);
            Reporter.log("==================================================================================",true);
            Reporter.log("## Expected Result Not Found....",true);
            Reporter.log("Actual Json Parsed Result for Unique Json Field <"+uniqueKey+ "> From Response:--", true);
            Reporter.log("==================================================================================",true);
            printMapList(getRequiredKeys(total_result,arrayList));
            Reporter.log("!!!! Expected Result Not Found !!!!", true);
            validationResult=false;
        }
        return validationResult;
    }

    private static boolean nullValidate(Object value) {
        // Null Check For Other Fields....=============================================
        boolean IsNotNull=true;
        if(value==null){
            return false;
        }
        Class<? extends Object> aClass = value.getClass();
        String name = aClass.getName();
        // Integer Null Validations....
        if (name.equals("java.lang.Integer")) {
            if ((Integer) value == null) {     
                IsNotNull=false;
                } else {
                IsNotNull=true;
                
                }
        } // String Null Validations...
        else if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {  
           
            IsNotNull=false;
        } else {
            IsNotNull=true;    
        }
        return IsNotNull;
    }
    
    private static boolean nonZero(Object value) {
        // Null Check For Other Fields....=============================================
        boolean IsNotNull=true;
        if(value==null){
            return false;
        }
        Class<? extends Object> aClass = value.getClass();
        String name = aClass.getName();
        // Integer Null Validations....
        if (name.equals("java.lang.Integer")) {
                if ((Integer) value == null || (Integer) value == 0) {
                IsNotNull = false;
            } else {
                IsNotNull = true;
            }
        } // String Null Validations...
        else if (value.toString().equals("0") || value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {  
            IsNotNull=false;
        } else {
            IsNotNull=true;    
        }
        return IsNotNull;
    }
    
    public static Map<String, Object> findPartialMatchingMapInMapList(List<Map<String, Object>> total_result, String uniqueKey, final Map<String, Object> expectedResult) {
        for (Map<String, Object> actualResult : total_result){
            if(actualResult.get(uniqueKey).toString().contains(expectedResult.get(uniqueKey).toString())){
                return actualResult;
            }
        }
        return null;
    }

    public static Map<String, Object> findMatchingMapInMapList(List<Map<String, Object>> total_result, String uniqueKey, final Map<String, Object> expectedResult) {
        for (Map<String, Object> actualResult : total_result){
            if(actualResult.get(uniqueKey).equals(expectedResult.get(uniqueKey))){
                return actualResult;
            }
        }
        return null;
    }
    
    public static void printMapList(List<Map<String, Object>> total_result){
        int count=1;
        for (Map<String, Object> total_result1 : total_result) {
            Reporter.log(count++ +" ) "+total_result1.toString(), true);
        }
    }
    
    public static boolean printMatchResultAndValidateNull(List<String> keys, Map<String, Object> matchedMap) {
        Reporter.log("==================================================================================",true);
        Reporter.log("Actual Result with NULL Check Validation :---",true);
        Reporter.log("==================================================================================",true);
        int count = 0;
        boolean nullFound=true;
        for (Map.Entry<String, Object> entrySet : matchedMap.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            if (keys.contains(key)) {
                Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Value Check> Passed", true);
            }
            else{
                
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                // Integer Null Validations....
                if (name.equals("java.lang.Integer")) {
                    if ((Integer) value == null) {
                        Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true); 
                        nullFound=false;
                    }
                    else{
                       Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true);  
                    }
                }
                // String Null Validations...
                else if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
                    Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> FAILED", true);
                    nullFound=false;
                }  
                else{
                   Reporter.log(count++ + ")  " + key + " : " + value.toString() + " ## <Null Check> Passed", true); 
                }
            }  
            }
        return nullFound;
        }
    
    
    
    public static void printMap(final Map<String, Object> expectedResult) {
        int count=1;
        for (Map.Entry<String, Object> entrySet : expectedResult.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            Reporter.log(count++ +")  "+key + " : "+value,true);
        }
    }
    
    /**
     * @param httpjsonResponse
     * @param parentNode Ex. $.hits.hits[*].source , it will look into all the
     * hits..
     * @param keys Ex. Activity_Type
     * @return
     */
    public static List<Map<String, Object>> getRequiredKeys(String httpjsonResponse, String parentNode, List<String> keys) {
        List<Map<String, Object>> total_result = new ArrayList<>();
        List<Map<String, Object>> fetchAllKeys = fetchAllKeys(httpjsonResponse,parentNode);
        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             Map<String, Object> map=new HashMap<>();
             for (String key : keys) {
                if(fetchAllKey.containsKey(key)){
                map.put(key, fetchAllKey.get(key).toString());
                }
                else{
                  map.put(key,"NOT FOUND");  
                }
            }  
            total_result.add(map);
        }
        return total_result;
    }
    
    public static List<Map<String, Object>> getRequiredKeys(List<Map<String, Object>> fetchAllKeys, List<String> keys) {
        List<Map<String, Object>> total_result = new ArrayList<>();
        for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             Map<String, Object> map=new HashMap<>();
             for (String key : keys) {
                if(fetchAllKey.containsKey(key)){
                map.put(key, fetchAllKey.get(key));
                }
                else{
                  map.put(key,"NOT FOUND");  
                }
            }  
            total_result.add(map);
        }
        return total_result;
    }
    
    /**
     *
     * @param httpjsonResponse
     * @param query Ex. "$.hits.total"
     * @return it will return first occurrence for matched parameter..
     */
    public static String getSingleKey(String httpjsonResponse, String query) {
        Object singleResult = null;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            singleResult = surfer.collectOne(httpjsonResponse, query);
            return singleResult.toString();
        } catch (Exception e) {
            System.out.println("Exception found in raw Json parser : http response :"+httpjsonResponse);
            System.out.println("Exception found in raw Json parser : query :"+query);
            return null;
        }
    }

    /**
     * @param httpjsonResponse
     * @param query
     * @return it return all the matched keys..
     */
    public static String getAllKeys(String httpjsonResponse, String query) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Object singleResult = surfer.collectAll(httpjsonResponse, query);
        return singleResult.toString();
    }
    

    public static Object fetchSingleField(String httpjsonResponse, String query) {
        Object singleResult = null;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            singleResult = surfer.collectOne(httpjsonResponse, query);
        } catch (Exception e) {
            return null;
        }
        return singleResult;
    }

    /**
     * @param httpjsonResponse
     * @param query
     * @return it return all the matched keys..
     */
    public static Object fetchMultipleFields(String httpjsonResponse, String query) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Object allResult = surfer.collectAll(httpjsonResponse, query);
        return allResult;
    }
    
    public static boolean validateNullValues(List<Map<String, Object>> multipleResults) {
        boolean nullFound = true;
        for (Map<String, Object> result : multipleResults) {
            for (Map.Entry<String, Object> entrySet : result.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                // Integer Null Validations....
                if (name.equals("java.lang.Integer")) {
                    if ((Integer) value == null) {
                        Reporter.log("======================== NULL FOUND =========================================",true);
                        Reporter.log("Map Result :"+result.toString(),true);
                        Reporter.log("## NULL entry Found in Field : KEY :" + key + "## Value :" + value, true);
                        nullFound = false;//fail Scenarios
                    }
                }
                // String Null Validations...
                if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
                    Reporter.log("======================= NULL FOUND ============================================",true);
                    Reporter.log("Map Result :"+result.toString(),true);
                    Reporter.log("## null entry Found in Field : KEY :" + key + "## Value :" + value, true);
                    nullFound = false;//fail Scenarios
                }
            }
        }
        Reporter.log("Null Found :"+nullFound + " !! Null Not Found..!!",true);
        return nullFound;
    }
    
    public static List<Map<String, Object>> fetchAllKeys(String jsonResponse, String parentNode){
        if (jsonResponse == null) {
            Reporter.log("## Invalid HTTP Response/Result :"+jsonResponse,true);
            return null;
        }
        jsonResponse = jsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        Reporter.log("### Filtering the Results from Json Response .....#### ", true);
        List<Map<String, Object>> filteredResults = new ArrayList<>();
        JsonSurfer jsonSurfer = JsonSurfer.simple();
        Collection<Object> multipleResults = jsonSurfer.collectAll(jsonResponse, parentNode);
        Reporter.log("### Results Count :" + multipleResults.size(),true);
        LinkedHashMap<String, Object> result = null;
        for (Object multipleResult : multipleResults) {
            String toString = multipleResult.toString();
            try {
                result = new ObjectMapper().readValue(toString, LinkedHashMap.class);
                for (Map.Entry<String, Object> entrySet : result.entrySet()) {
                    String key = entrySet.getKey();
                    Object value = entrySet.getValue();
                    if(value!=null){
                    Class<? extends Object> aClass = value.getClass();
                     String name = aClass.getName();
                     if(name.contains("String")){
                         result.put(key, value.toString().trim());
                     }
                     }
                    else{
                       result.put(key, value); 
                    }
                }
            } catch (IOException ex) {
                Reporter.log("### Issue Found with Object Mapper"+ex.getLocalizedMessage(),true);
            }
            filteredResults.add(result);
        }
        return filteredResults;
    }
    
     public static Void LogValidator(String json_Response,Map expectedResult,String parentNode,String uniqueKey){
        boolean matchResult = RawJsonParser.findExpectedKeysAndValues(json_Response, parentNode, expectedResult, uniqueKey);
        Reporter.log(" ### Assertion :" + matchResult, true);
        Assert.assertTrue(matchResult, "Assertion :" + matchResult);
        return null;
    }
     
     public static String getDisplayLogResponse(TestSuiteDTO suiteData,List<NameValuePair> headers,String AppName,String from_jodaTime) throws Exception {
        ESQueryBuilder eSQueryBuilder = new ESQueryBuilder();
        HashMap<String, String> termmap = new HashMap<String, String>();
        termmap.put("facility", AppName);
        String apiHost = suiteData.getScheme() + "://" + suiteData.getApiserverHostName();
        String payload = "";
        payload = eSQueryBuilder.getESQuery(from_jodaTime, RawJsonParser.getCurrentJodaTime(),
                AppName, termmap, suiteData.getUsername().toLowerCase(), apiHost, suiteData.getCSRFToken(), suiteData.getSessionID(), 0, 500, AppName);
        Reporter.log("Request body:" + payload, true);
        String path = suiteData.getAPIMap().get("getInvestigateLogs");
        URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), path);
        Reporter.log("URI ::" + dataUri.toString(), true);
        com.elastica.beatle.RestClient.Client restClient=new Client();
        HttpResponse response = restClient.doPost(dataUri, headers, null, new StringEntity(payload));
        String jsonResponse=ClientUtil.getResponseBody(response);
        Reporter.log("Display Log Response : "+jsonResponse,true);
        return jsonResponse;
    }
    
    public static String getURI(String url, Map<String, String> params) throws URISyntaxException {
        URIBuilder uRIBuilder = new URIBuilder(url);
        if (params == null) {
            return uRIBuilder.toString();
        }
        for (Map.Entry<String, String> entrySet : params.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            uRIBuilder.addParameter(key, value);
            Reporter.log("Appending Query Params to the URL ... KEY :" + key + "  VALUE :" + value);
        }
        String toString = uRIBuilder.toString();
        return toString;
    }
     
    public static void wait(long millisecs, String msg) {
        Reporter.log("------------------------------------------------------------------",true);
        Reporter.log("### SLEEP : "+msg.toUpperCase() + " >> ..Waiting for " + millisecs + " millisecs...", true);
        Reporter.log("------------------------------------------------------------------",true);
        try {
            Thread.sleep(millisecs);
        } catch (InterruptedException ex) {
            Reporter.log("Exception Found in thread sleep:"+ex.getLocalizedMessage(),true);
        }
    }
    
    public static String getCurrentJodaTime(){
       return new org.joda.time.DateTime(org.joda.time.DateTimeZone.UTC).toString();
    }
    
    public static boolean matchMap(Map<String, Object> expected,Map<String, Object> actual){
        boolean areEqual = Maps.difference(expected, actual).areEqual();
        MapDifference<String, Object> difference = Maps.difference(expected, actual);
        System.out.println("Difference :"+difference.toString());
        
        Map<String, Object> entriesInCommon = difference.entriesInCommon();
        System.out.println("Common Entries :"+entriesInCommon);
        return areEqual;    
    }
    
     public static Void LogValidatorPartialCheck(String json_Response,Map expectedResult,String parentNode,String uniqueKey) throws Exception {
        boolean matchResult = RawJsonParser.findExpectedKeysAndPartialValues(json_Response, parentNode, expectedResult, uniqueKey);
        Reporter.log(" ### Assertion :" + matchResult, true);
        Assert.assertTrue(matchResult, "Assertion :" + expectedResult);
        return null;
    }
     
    public static void reportDescription(String description){
        String[] split = description.split("'");
        Reporter.log("==============================================================================",true);
        Reporter.log("Detailed Test Steps description :-",true);
        Reporter.log("==============================================================================",true);
        int i=0;
        for (String split1 : split) {
            Reporter.log(++i+") "+split1);
        }
        Reporter.log("==============================================================================",true);    
    }
    
    
}
