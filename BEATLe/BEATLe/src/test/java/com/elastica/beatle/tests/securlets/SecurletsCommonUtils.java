/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.tests.securlets;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsfr.json.BuilderFactory;
import org.jsfr.json.JsonPathListener;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.ParsingContext;
import org.jsfr.json.SurfingConfiguration;
import org.testng.Reporter;

public class SecurletsCommonUtils {

    static String NotEmpty = "NOT_EMPTY";

    public boolean filterFieldsFromResponse(String httpjsonResponse, String parentQuery, final Map<String, String> expectedResult) {
        System.out.println("===>" + httpjsonResponse);
        Reporter.log("## Expected Response :" + expectedResult.toString(), true);
        if (httpjsonResponse == null) {
            return false;
        }
        boolean resultFound = false;
        final Set<String> keySet = expectedResult.keySet();
        int count = 0;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            // Null Check...
            boolean validateNull = validateNull(jsonResponse, expectedResult);
            if (validateNull == false) {
                // return false;
            }
        } catch (IOException ex) {
            Reporter.log("HTTP RESPONSE :" + httpjsonResponse, true);
            Reporter.log("Issue found in Null Check Validation:" + ex.getLocalizedMessage(), true);
        }
        final List<Map<String, String>> total_result = new ArrayList<>();
        JsonPathListener print = new JsonPathListener() {
            JsonSurfer surfer = JsonSurfer.simple();

            @Override
            public void onValue(Object value, ParsingContext context) {
                Map<String, String> result = new HashMap<>();
                for (String alChildElementspath : keySet) {
                    String key = StringUtils.replace(alChildElementspath, ".", "");
                    try {
                        value = (String) surfer.collectOne(jsonResponse, context.getJsonPath() + "." + alChildElementspath).toString().trim();
                        String val = (String) value;
                        val = val.replaceAll("\"", "");
                        if (key.equals("message")) {
                            val = processUserMessage(val);
                            result.put(key, (String) val);
                        } else {
                            result.put(key, (String) val);
                        }
                    } catch (Exception e) {
                        Reporter.log(" ## Exception :" + e.getLocalizedMessage());
                        result.put(key, null);
                    }
                }
                total_result.add(result);
            }
        };
        JsonSurfer surfer = JsonSurfer.gson();
        SurfingConfiguration.Builder builder = BuilderFactory.config();
        builder.bind(parentQuery, print);
        surfer.surf(jsonResponse, builder.build());
        List<String> keysForNonEmpty = getKeysForNonEmpty(expectedResult);
        for (Map<String, String> result : total_result) {
            Reporter.log("Filtered Response :" + result.toString(), true);
            Map<String, String> filteredResult = new HashMap();
            filteredResult.putAll(result);
            for (String key : keysForNonEmpty) {
                if (result.get(key) != null) {
                    if (result.get(key).length() != 0) {
                        result.put(key, SecurletsCommonUtils.NotEmpty);
                    }
                }
            }
            System.out.println("Result :" + result);
            if (Maps.difference(result, expectedResult).areEqual()) {
                Reporter.log("## Expected Response Found :" + result.toString(), true);
                Reporter.log("## Actual Values From Json :" + filteredResult.toString(), true);
                return true;
            }
        }
        return resultFound;
    }

    public List<String> getKeysForNonEmpty(Map<String, String> expectedResults) {
        List<String> getKeysForNonEmpty = new ArrayList<>();
        for (Map.Entry<String, String> entrySet : expectedResults.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if (value.equals(SecurletsCommonUtils.NotEmpty)) {
                getKeysForNonEmpty.add(key);
            }
        }
        System.out.println(SecurletsCommonUtils.NotEmpty + " ## Validation Keys :" + getKeysForNonEmpty);
        return getKeysForNonEmpty;
    }

    public boolean validateNull(String jsonResponse, Map<String, String> expectedResult) throws IOException {
        Reporter.log("Performing Null Check....", true);
        JsonSurfer jsonSurfer = JsonSurfer.simple();
        Collection<Object> multipleResults = jsonSurfer.collectAll(jsonResponse, "$.hits.hits[*].source");
        LinkedHashMap<String, Object> result = null;
        for (Object multipleResult : multipleResults) {
            String toString = multipleResult.toString();
            System.out.println("==>" + toString);
            result = new ObjectMapper().readValue(toString, LinkedHashMap.class);
            for (Map.Entry<String, Object> entrySet : result.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                Class<? extends Object> aClass = value.getClass();
                String name = aClass.getName();
                boolean contains = expectedResult.keySet().contains(key);
                if (!contains) {
                    // Integer Validations....
                    if (name.equals("java.lang.Integer")) {
                        if ((Integer) value == 0) {
                            Reporter.log("Zero entry Found in Field : KEY :" + key + "## Value :" + value, true);
                            return false;
                        }
                    }
                    // String Validations...
                    if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
                        Reporter.log("null entry Found in Field : KEY :" + key + "## Value :" + value, true);
                        return false;
                    }
                }
                // System.out.println("Key :" + key + " ## Value :" + value);
            }
        }
        Reporter.log("!!! No Null Found !!! ", true);
        return true;
    }

    public static void main(String[] args) {

        String msg = "[ALERT] testuser1@gatewaybeatle.com attempted Activity: Share on Object type: Folder using Platform: Mac OS X, Version: 10.10 and Browser : Firefox and Version : 40.0 violating policy:\"ACCESS_ENFORCE_FOLDER13\"";

      //  processUserMessage(msg);
    }

    public String processUserMessage(String msg) {
        String[] split = msg.split("Platform");
        System.out.println("Length :" + split.length);
        if (split.length > 1) {
            String tobeRemoved = split[1];
            msg = msg.replaceAll(tobeRemoved, "");
        }
        System.out.println("Msg :" + msg);
        return msg;
    }
}
