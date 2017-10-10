/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.action.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsfr.json.JsonSurfer;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

public class RawJsonParser {

    private static final JsonSurfer surfer = JsonSurfer.simple();
    static String NotEmpty = "NOT_EMPTY";// not null...
    static String NotZero = "NON_ZERO";// not null...
    
    
    public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
      //  Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        List<Map<String, Object>> fetchAllKeys = fetchAllKeys(httpjsonResponse, parentNode);
        
         for (Map<String, Object> fetchAllKey : fetchAllKeys) {
             for (Map.Entry<String, Object> entrySet : fetchAllKey.entrySet()) {
                 String key = entrySet.getKey();
                 Object value = entrySet.getValue();
                 fetchAllKey.put(key, value.toString().replaceAll("\"", ""));
             }
             fetchAllKey.put("transit_hosts", "");
         }
        
       
         
        if (matchMapFromMapList(fetchAllKeys, expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }
    
   /* public static boolean findExpectedKeysAndPartialValues(String httpjsonResponse, String parentNode, final Map<String, Object> expectedResult,String uniqueKey) {
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,true,true)) {
            return true;
        }
        return resultFound;
    }*/
    
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
        Reporter.log("Expected Result :"+expectedResult.toString(),true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,false,false)) {
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
        if (matchMapFromMapList(fetchAllKeys(httpjsonResponse, parentNode), expectedResult,uniqueKey,true,false)) {
            return true;
        }
        return resultFound;
    }
    
    

    /**
     * @param total_result ...List of Map
     * @param expectedResult .. Expected Map...
     * @param uniqueKey
     * @param nullCheck
     * @return if Expected Map Found then true, else return false..
     */
    public static boolean matchMapFromMapList(List<Map<String, Object>> total_result, final Map<String, Object> expectedResult, String uniqueKey,boolean nullCheck,boolean partialSearch) {
    	SoftAssert softAssert = new SoftAssert();
    	Reporter.log("==================================================================================",true);
        Reporter.log("Expected log fields to validate  :-", true);
        printMap(expectedResult);
        Reporter.log("==================================================================================",true);
        boolean validationResult=true;
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
            Reporter.log("!! Log Found, Checking other detail fields !! Actual Result with Null & Value Check :---",true);
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
                
                if (value == null && expectedResult.get(key) == null) {
                    Reporter.log(count++ + ")  " + key + " : " + value + " ## <Expected NULL> Passed", true);
                }
                
                else if(keyList.contains(key)){
                    if(expectedResult.get(key).equals(NotEmpty)){
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
                            Reporter.log(count++ + ">>  " + key + ": " + value.toString() +  "  ## Expected :"+expectedResult.get(key).toString()+" < ### Field Value Check #### > !! PASSED !!", true);
                        } else {
                        	
                        	softAssert.assertEquals(value.toString(), expectedResult.get(key).toString(), 
                        			"Actual value:"+value.toString()+" is not matching with the expected value:"+expectedResult.get(key).toString());
                            Reporter.log("===========================================================================", true);
                            Reporter.log(count++ + ")  " + key + ": " + value.toString() +  "  ## Expected :"+expectedResult.get(key).toString()+ " <### Field Value Check ####> $$ FAILED $$", true);
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
                       Reporter.log(count++ +">>  "+key +": "+value.toString() +" <Value Check> $$ FAILED $$",true); 
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
          //  Reporter.log("==================================================================================",true);
           // Reporter.log("## Expected Result Not Found....",true);
            Reporter.log("Actual logs found in responce:- ", true);
           // Reporter.log("==================================================================================",true);
            printMapList(getRequiredKeys(total_result,arrayList));
           // Reporter.log("!!!! Expected Result Not Found !!!!", true);
            Reporter.log("==================================================================================",true);
            Reporter.log("Expected log: $$$ "+expectedResult.get("message") +" $$$ does not exist in returned log above", true);
            Reporter.log("==================================================================================",true);
            
            validationResult=false;
        }
        
       // Reporter.log("================================================================================================================================",true);
      //  Reporter.log("================================================================================================================================",true);
        softAssert.assertAll();
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
        if (total_result.size()<=0){
        	Reporter.log(" None ", true);
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
            Reporter.log(count++ +")  "+key + " : "+value.toString(),true);
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
        } catch (Exception e) {
            return null;
        }
        return singleResult.toString();
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
        Reporter.log("==================================================================================",true);
        Reporter.log("### Parsing Json Response .....#### ", true);
        List<Map<String, Object>> filteredResults = new ArrayList<>();
        JsonSurfer jsonSurfer = JsonSurfer.simple();
        Collection<Object> multipleResults = jsonSurfer.collectAll(jsonResponse, parentNode);
        Reporter.log("### Total logs found in responce :" + multipleResults.size(),true);
        Reporter.log("==================================================================================",true);
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
                    	 if (key.equals("File_Size")){
                    		 if(value.toString().length()>0){
                    		long valueInt= Long.parseLong(value.toString());
                    		valueInt=valueInt/1024;
                    		value=Long.toString(valueInt);
                    		 }
                    	 }
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

}
