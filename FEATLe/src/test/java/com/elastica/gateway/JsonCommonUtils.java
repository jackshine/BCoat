/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.gateway;

import com.google.common.collect.Maps;
import java.util.Locale;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Reporter;

public class JsonCommonUtils {

    
    public boolean filterFieldsFromResponse(String httpjsonResponse, String parentQuery, final Map<String, String> expectedResult) {
        Reporter.log("## Expected Response :"+expectedResult.toString(),true);
        if(httpjsonResponse==null){
            return false;
        }
        boolean resultFound = false;
        final Set<String> keySet = expectedResult.keySet();
        int count = 0;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        try {
            // Null Check...
            boolean validateNull = validateNull(jsonResponse,expectedResult);
            if(validateNull == false){
               // return false;
            }
        } catch (IOException ex) {
            Reporter.log("HTTP RESPONSE :"+httpjsonResponse,true);
            Reporter.log("Issue found in Null Check Validation:"+ex.getLocalizedMessage(),true);
        }
        final List<Map<String, String>> total_result = new ArrayList<Map<String, String>>();
        JsonPathListener print = new JsonPathListener() {
            JsonSurfer surfer = JsonSurfer.simple();
            @Override
            public void onValue(Object value, ParsingContext context) {
                Map<String, String> result = new HashMap<String, String>();
                for (String alChildElementspath : keySet) {
                    String key=StringUtils.replace(alChildElementspath, ".", "");
                    try{
                    value=(String) surfer.collectOne(jsonResponse, context.getJsonPath() + "." + alChildElementspath).toString().trim(); 
                    String val=(String) value;
                    val=val.replaceAll("\"", "");
                    if(key.equals("message")){
                        val=processUserMessage(val); 
                        result.put(key, (String) val);
                    }
                    else{
                        result.put(key, (String) val);
                    }
                    }
                    catch(Exception e){
                        Reporter.log(" ## Exception :"+e.getLocalizedMessage());
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
        if (GatewayTestConstants.LOG_MESSAGE != null) {
        	GatewayTestConstants.LOG_MESSAGE.clear();
        }
        GatewayTestConstants.LOG_MESSAGE = total_result;
        for (Map<String, String> result : total_result) {
            Reporter.log("Filtered Response :"+result.toString(),true);
                if(Maps.difference(result, expectedResult).areEqual()){
                  Reporter.log("## Expected Response Found :"+result.toString(),true);
                 
                return true;
            }
        }
        return resultFound;
    }

    public boolean validateNull(String jsonResponse, Map<String, String> expectedResult) throws IOException {
        Reporter.log("Performing Null Check....",true);
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
                if(!contains){
                    // Integer Validations....
                    if(name.equals("java.lang.Integer")){
                        if((Integer)value==0){
                            Reporter.log("Zero entry Found in Field : KEY :"+ key +"## Value :"+value,true);
                            return false;
                        }
                    }
                    // String Validations...
                    if(value == null || value.toString().length()==0 || value.toString().equalsIgnoreCase("null")){
                            Reporter.log("null entry Found in Field : KEY :"+ key +"## Value :"+value,true);
                            return false;
                    }  
                }
               // System.out.println("Key :" + key + " ## Value :" + value);
            }
        }
        Reporter.log("!!! No Null Found !!! ",true); 
        return true; 
    }
    
    public String processUserMessage(String msg) {
        String[] split = msg.split("Platform");
        System.out.println("Length :"+split.length);
        if(split.length>1){
            String tobeRemoved=split[1];
            msg=msg.replaceAll(tobeRemoved,"");
        }
        System.out.println("Msg :"+msg);
        return msg;
    }
    
    
    public  static Map<String,Object> getLocationPerametrs(){
    	Map<Object, Object > dataMap=new HashMap<Object, Object >();
    	
    	ProcessBuilder pb = new ProcessBuilder(
	            "curl",
	            "http://ipinfo.io/");
	 Process p = null;
	try {
		p = pb.start();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 InputStream is = p.getInputStream();
	 
	 String outputString;
	 String output = "";
	 DataInputStream curlIn = new DataInputStream(is);
	 try {
		while ((outputString = curlIn.readLine()) != null) {
		      output+=outputString;
		 }
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	// System.out.println(output);
	 
	 
	 
	 Map<String,Object> fetchSingleField = (Map<String,Object>) RawJsonParser.fetchSingleField(output, "$");
	 RawJsonParser.printMap(fetchSingleField);
	 Map<String,Object> resultMap= new HashMap<String,Object>();
	 resultMap.clear();
	 String ip = (String) fetchSingleField.get("ip");
     String city = (String) fetchSingleField.get("city");
     String region = (String) fetchSingleField.get("region");
     String country = (String) fetchSingleField.get("country");
     String loc = (String) fetchSingleField.get("loc");
     
     
     Locale obj = new Locale("", country);
	 country=obj.getDisplayCountry();
     
     String [] longlat=loc.split(",");
     String latl=longlat[0];
     String longl=longlat[1];
    
     String latitude= latl.substring(0, latl.indexOf('.')+3);
     String longitude= longl.substring(0, longl.indexOf('.')+3);
     resultMap.put(GatewayTestConstants.LONGITUDE, longitude);
     resultMap.put(GatewayTestConstants.LATITUDE, latitude);
     resultMap.put(GatewayTestConstants.COUNTRY, country);
     resultMap.put(GatewayTestConstants.HOST, ip);
     resultMap.put(GatewayTestConstants.CITY, city);
     resultMap.put(GatewayTestConstants.REGION, region);
    return resultMap;
    }
    
    
    
    
}
