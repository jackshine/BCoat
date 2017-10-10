package com.elastica.beatle.tests.securlets;


import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.Reporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsfr.json.BuilderFactory;
import org.jsfr.json.JsonPathListener;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.ParsingContext;
import org.jsfr.json.SurfingConfiguration;
import org.json.simple.JSONObject;
import org.yaml.snakeyaml.Yaml;

public class SecurletsUtils {

    private CloseableHttpAsyncClient client;
    private String csrfToken;
    private String user;
    private String sessionId;
    private String appServerUrl;
    private String tenantToken;
    private String referrer = "";
    private String authorizationHeader = "";
    private String cookie = "";
    private String jsonObject;

    public void setAuthorizationHeader(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAppServerUrl(String appServerUrl) {
        this.appServerUrl = appServerUrl;
    }

    public void setTenantToken(String tenantToken) {
        this.tenantToken = tenantToken;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public SecurletsUtils() {
        //initializeEnvironmentalVariable();
    }

    public void setYamlFileLocation(String yamlFileLocation) {
        this.jsonObject = yamlToJson(yamlFileLocation);
    }

    public void updateYamlExpectedResponse(String oldString, String newString) {
        this.jsonObject = this.jsonObject.replaceAll(oldString, newString);
    }

    public Map getYamlMap(String nodePath) {
        try {
            return getYamlMapFromJsonObject(this.jsonObject, nodePath);
        } catch (IOException ex) {
            Logger.getLogger(SecurletsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getYamlString(String nodePath) {
        return getYamlStringFromJsonObject(this.jsonObject, nodePath);
    }

    public static String generatePayLoadForDisplayLog(String to_time, String from_time, String SaaSApp,String csrfToken,String sessionId,String user,String appServerUrl) {
        String Sample_displayLogJonPayload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"FROM\",\"to\":\"TO\"}}},{\"term\":{\"facility\":\"SAASAPP\"}}]}}}},\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"from\":0,\"size\":1000,\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}},\"Resource_Id\":{\"terms\":{\"field\":\"Resource_Id\",\"size\":1000}}}},\"sourceName\":\"SAASAPP\",\"apiServerUrl\":\"APISERVERURL\",\"csrftoken\":\"CSRF\",\"sessionid\":\"SESSIONID\",\"userid\":\"USERID\"}";
        Sample_displayLogJonPayload = Sample_displayLogJonPayload
                .replaceAll("TO", to_time)
                .replaceAll("FROM", from_time)
                .replaceAll("CSRF", csrfToken)
                .replaceAll("SESSIONID", sessionId)
                .replaceAll("USERID", user)
                .replaceAll("SAASAPP", SaaSApp)
                .replaceAll("APISERVERURL", appServerUrl);
        System.out.println("#### Generated PayLoad :" + Sample_displayLogJonPayload);
        return Sample_displayLogJonPayload;
    }

    public String generatePayLoad(String to_time, String from_time, String SaaSApp, String ActivityType) {
        String Sample_displayLogJonPayload = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\"FROM\",\"to\":\"TO\"}}},{\"term\":{\"facility\":\"SAASAPP\"}},{\"term\":{\"Activity_type\":\"ACTIVITYTYPE\"}}]}}}},\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"from\":0,\"size\":1000,\"facets\":{\"histoGreen\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"informational\"}}},\"histoOrange\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"warning\"}}},\"histoRed\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"critical\"}}},\"histoYellow\":{\"date_histogram\":{\"field\":\"created_timestamp\",\"interval\":\"minute\"},\"facet_filter\":{\"term\":{\"severity\":\"error\"}}},\"user\":{\"terms\":{\"field\":\"user\",\"size\":1000}},\"severity\":{\"terms\":{\"field\":\"severity\",\"size\":1000}},\"location\":{\"terms\":{\"field\":\"location\",\"size\":1000}},\"Object_type\":{\"terms\":{\"field\":\"Object_type\",\"size\":1000}},\"Activity_type\":{\"terms\":{\"field\":\"Activity_type\",\"size\":1000}},\"__source\":{\"terms\":{\"field\":\"__source\",\"size\":1000}},\"Resource_Id\":{\"terms\":{\"field\":\"Resource_Id\",\"size\":1000}}}},\"sourceName\":\"SAASAPP\",\"apiServerUrl\":\"APISERVERURL\",\"csrftoken\":\"CSRF\",\"sessionid\":\"SESSIONID\",\"userid\":\"USERID\"}";
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("TO", to_time);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("FROM", from_time);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("ACTIVITYTYPE", ActivityType);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("CSRF", this.csrfToken);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("SESSIONID", this.sessionId);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("USERID", this.user);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("SAASAPP", SaaSApp);
        Sample_displayLogJonPayload = Sample_displayLogJonPayload.replaceAll("APISERVERURL", this.appServerUrl);
        System.out.println("#### Generated PayLoad :" + Sample_displayLogJonPayload);
        return Sample_displayLogJonPayload;
    }

    public String generatePayLoadGDriveRemediationRemoveLink(String tenant, String user,
            String docId, String docType, String accessType) {

        String samplePayloadRemoveLink = "{\"objects\":[{\"db_name\":\""
                + tenant
                + "\",\"user\":\""
                + user
                + "\",\"user_id\":null,\"doc_id\":\""
                + docId
                + "\",\""
                + "doc_type\":\""
                + docType
                + "\",\"actions\":[{\"code\":\""
                + "UNSHARE\",\"possible_values\":[],\"meta_info\":{\"current_link\":\"open\"}}]}]}";

        return samplePayloadRemoveLink;
    }

    public String generatePayLoadForGDriveRemediation(String tenant, String user,
            String docId, String docType, String accessType, String newValue) {
        String Sample_displayLogJonPayload = "{\"objects\":[{\"db_name\":\""
                + tenant
                + "\",\"user\":\""
                + user
                + "\",\"user_id\":null,\""
                + "doc_id"
                + "\":\""
                + docId
                + "\",\""
                + "doc_type"
                + "\":\""
                + docType
                + "\",\"actions\":[{\"code\":\""
                + accessType
                + "\",\"possible_values\":[\"open-writer\",\"open-reader\",\"open-commenter\",\"open-withlink-writer\",\"open-withlink-reader\",\"open-withlink-commenter\",\"company-writer\",\"company-reader\",\"company-commenter\",\"company-withlink-writer\",\"company-withlink-reader\",\"company-withlink-commenter\"],\"meta_info\":{\"access\":\""
                + newValue
                + "\",\"collabs\":[],\"current_link\":\"open\"}}]}]}";

        System.out.println("#### Generated PayLoad :" + Sample_displayLogJonPayload);
        return Sample_displayLogJonPayload;
    }

    // Update Headers....
    public void updateHeaders(List<NameValuePair> headers) {
        for (NameValuePair header : headers) {
            String key = header.getName();
            String value = header.getValue();
            Reporter.log("Key :" + key + " ## Value :" + value, true);
            if (key.equalsIgnoreCase("X-CSRFToken")) {
                setCsrfToken(value);
            }
            if (key.equalsIgnoreCase("X-Session")) {
                setSessionId(value);
            }
            if (key.equalsIgnoreCase("Referer")) {
                setSessionId(value);
            }
            if (key.equalsIgnoreCase("X-User")) {
                setUser(value);
            }
            if (key.equalsIgnoreCase("Authorization")) {
                setAuthorizationHeader(value);
            }
            if (key.equalsIgnoreCase("Cookie")) {
                setCookie(value);
            }
        }
    }

    // Generic Request Headers.....
    public Map<String, String> getRequestHeaders() {
        Map<String, String> HashMap_ReqHeaders = new HashMap<String, String>();
        HashMap_ReqHeaders.put("CSP", "active");
        HashMap_ReqHeaders.put("Content-Type", "application/json;charset=UTF-8");
        HashMap_ReqHeaders.put("X-User", this.user);
        HashMap_ReqHeaders.put("X-Session", this.sessionId);
        HashMap_ReqHeaders.put("X-CSRFToken", this.csrfToken);
        HashMap_ReqHeaders.put("X-TenantToken", this.tenantToken);
        return HashMap_ReqHeaders;
    }

    // HTTP GET ... Sent
    public String sentGet(String url, Map<String, String> headers) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        System.out.println("--- Elastic Rest Call ---");
        HttpGet request = new HttpGet(url);
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            request.setHeader(key, value);
        }
        HttpResponse response = client.execute(request);
        int responseCode = response.getStatusLine().getStatusCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        Header[] allHeaders = request.getAllHeaders();
        for (Header header : allHeaders) {
            Reporter.log(" Header Name : " + header.getName() + " ## Header Element Value :" + header.getValue(), true);
            HeaderElement[] elements = header.getElements();
            for (HeaderElement element : elements) {
                Reporter.log(" Header Element Name : " + element.getName() + " ## Header Element Value :" + element.getValue(), true);
            }
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String result_response = result.toString();
        System.out.println("Result Response :" + result_response);
        return result_response;
    }

    public StringBuffer getStringResponseFromHttpResponse(HttpResponse doGet) throws IOException, UnsupportedOperationException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(doGet.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result;
    }

    public String getURI(String url, Map<String, String> params) throws URISyntaxException {
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

    // HTTP POST ... Sent
    public String sentPost(String url, Map<String, String> headers, String payLoad) throws IOException, InterruptedException, ExecutionException, KeyManagementException, NoSuchAlgorithmException, IOReactorException, KeyStoreException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("jsse.enableSNIExtension", "false");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        Reporter.log("Client Started .....", true);
        Reporter.log("--- Elastic Rest Call For DisplayLog... ---", true);
        HttpPost request = new HttpPost(url);
        Reporter.log(" ", true);
        HttpEntity httpEntity = new StringEntity(payLoad);
        request.setEntity(httpEntity);
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            request.setHeader(key, value);
        }
        Reporter.log(" ## Host " + request.getURI().getHost() + " ## POST : Rest URL :" + request.getURI().toString(), true);
        Header[] allHeaders = request.getAllHeaders();
        for (Header header : allHeaders) {
            Reporter.log(" Header Name : " + header.getName() + " ## Header Element Value :" + header.getValue(), true);
            HeaderElement[] elements = header.getElements();
            for (HeaderElement element : elements) {
                Reporter.log(" Header Element Name : " + element.getName() + " ## Header Element Value :" + element.getValue(), true);
            }
        }
        request.getRequestLine();
        request.getURI();
        HttpResponse response = client.execute(request);
        Reporter.log(request.getRequestLine() + "->" + response.getStatusLine(), true);
        String postResponse = EntityUtils.toString(response.getEntity(), "utf-8");
        Reporter.log(" ## POST RESPONSE :" + postResponse, true);
        client.close();
        return postResponse;
    }

    public List<String> filterRequiredFieldsFromResponse(String httpjsonResponse, String parentQuery, final String childElement) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        final List<String> total_result = new ArrayList<>();
        JsonPathListener print = new JsonPathListener() {
            JsonSurfer surfer = JsonSurfer.simple();

            @Override
            public void onValue(Object value, ParsingContext context) {
                value = surfer.collectOne(jsonResponse, context.getJsonPath() + childElement);
                total_result.add((String) value);
            }
        };
        JsonSurfer surfer = JsonSurfer.gson();
        SurfingConfiguration.Builder builder = BuilderFactory.config();
        builder.bind(parentQuery, print);
        surfer.surf(jsonResponse, builder.build());
        return total_result;
    }

    public void wait(int secs) {
        Reporter.log("Waiting for " + secs + " secs........", true);
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SecurletsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void wait(int secs, String msg) {
        Reporter.log(msg + " >> ..Waiting for " + secs + " secs........", true);
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SecurletsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String filterSingleResultFromResponse(String httpjsonResponse, String query) {
        Object singleResult=null;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        JsonSurfer surfer = JsonSurfer.simple();
        try{
        singleResult = surfer.collectOne(httpjsonResponse, query);
        }
        catch(Exception e){
           return null; 
        }
        return singleResult.toString();
    }

    public List filterFields(String response, String query) {
        final List<Object> result = new ArrayList<>();
        response = response.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        JsonPathListener print = new JsonPathListener() {
            @Override
            public void onValue(Object value, ParsingContext context) {
                result.add(value.toString().trim().replaceAll("\"", ""));
                System.out.println(value);
            }
        };
        JsonSurfer surfer = JsonSurfer.gson();
        SurfingConfiguration.Builder builder = BuilderFactory.config();
        builder.bind("$.hits.hits[*].source.message", print);
        surfer.surf(response, builder.build());
        return result;
    }

    public String filterAllResultsFromResponse(String httpjsonResponse, String query) {
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        JsonSurfer surfer = JsonSurfer.simple();
        Object singleResult = surfer.collectAll(httpjsonResponse, query);
        return singleResult.toString();
    }

    public List<Map<String, String>> filterRequiredFieldsFromResponse(String httpjsonResponse, String parentQuery, final List<String> alChildElementspaths) {
         if(httpjsonResponse==null){
            return null;
        }
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        final List<Map<String, String>> total_result = new ArrayList<>();
        JsonPathListener print = new JsonPathListener() {
            JsonSurfer surfer = JsonSurfer.simple();
            @Override
            public void onValue(Object value, ParsingContext context) {
                Map<String, String> result = new HashMap<>();
                for (String alChildElementspath : alChildElementspaths) {
                    String key=StringUtils.replace(alChildElementspath, ".", "");   
                    try{
                    value=(String) surfer.collectOne(jsonResponse, context.getJsonPath() + "." + alChildElementspath).toString().trim(); 
                    result.put(key, (String) value);
                    }
                    catch(Exception e){
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
        return total_result;
    }

    public boolean filterFieldsFromResponse(String httpjsonResponse, String parentQuery, final Map<String, String> expectedResult) {
        if(httpjsonResponse==null){
            return false;
        }
        boolean resultFound = false;
        final Set<String> keySet = expectedResult.keySet();
        int count = 0;
        final String jsonResponse = httpjsonResponse.replaceAll("\"_", "\"").replaceAll("\"_", "\"").replaceAll("\"_", "\"");
        final List<Map<String, String>> total_result = new ArrayList<>();
        JsonPathListener print = new JsonPathListener() {
            JsonSurfer surfer = JsonSurfer.simple();
            @Override
            public void onValue(Object value, ParsingContext context) {
                Map<String, String> result = new HashMap<>();
                for (String alChildElementspath : keySet) {
                    String key=StringUtils.replace(alChildElementspath, ".", "");
                    try{
                    value=(String) surfer.collectOne(jsonResponse, context.getJsonPath() + "." + alChildElementspath).toString().trim(); 
                    result.put(key, (String) value);
                    }
                    catch(Exception e){
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
        for (Map<String, String> result : total_result) {
            Reporter.log("Filtered Response :"+result.toString(),true);
                if(Maps.difference(result, expectedResult).areEqual()){
                  Reporter.log("## Expected Response Found :"+result.toString(),true);
                  return true;
                }
        }
        return resultFound;
    }

    private JsonSurfer jsonSurfer = JsonSurfer.simple();

    private String yamlToJson(String fileLocation) {
        fileLocation = System.getProperty("user.dir") + File.separator + fileLocation;
        Reporter.log(" ## Loading the YAML file :" + fileLocation, true);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File(fileLocation));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SecurletsUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(fileReader);
        JSONObject jsonObject = new JSONObject(map);
        //Reporter.log("\n ## Expected Response List from YAML : " + jsonObject + "\n", true);
        Reporter.log(" ## Loading the YAML file  COMPLETED ## ", true);
        return jsonObject.toString();
    }

    private Map getYamlMapFromJsonObject(String jsonString, String nodePath) throws IOException {
        HashMap<String, String> finalresult = new HashMap<>();
        Object singleResult = jsonSurfer.collectOne(jsonString, nodePath);
        LinkedHashMap<String, Object> result = null;
        result = new ObjectMapper().readValue(singleResult.toString(), LinkedHashMap.class);
        for (Map.Entry<String, Object> entrySet : result.entrySet()) {
            String key = entrySet.getKey();
            Object value = entrySet.getValue();
            String simpleName = value.getClass().getSimpleName();
            if (simpleName.equals("ArrayList")) {
                List<String> al = (List<String>) value;
                String[] arr = al.toArray(new String[al.size()]);
                String listString = StringUtils.join(arr, " ").trim();
                result.put(entrySet.getKey(), listString);
            }
            
        }
        for (Map.Entry<String, Object> entrySet : result.entrySet()) {
            String key = entrySet.getKey().trim();
            String value = entrySet.getValue().toString().trim();
            finalresult.put(key, value);
        }
        return finalresult;
    }

    private String getYamlStringFromJsonObject(String jsonString, String nodePath) {
        Object singleResult = jsonSurfer.collectOne(jsonString, nodePath);
        return singleResult.toString();
    }

    public Map<String, String> compareAndNullCheckInMap(Map<String, String> expected, List<Map<String, String>> listMap) {
        boolean found = true;
        Map<String, String> result = new HashMap<>();
        Object[] toArray = expected.keySet().toArray();
        for (Map<String, String> actualmap : listMap) {
            found = true;
            for (Map.Entry<String, String> entrySet : expected.entrySet()) {
                if (found) {
                    String key = entrySet.getKey();
                    String value = entrySet.getValue();
                    if (value != null) {
                        String get = actualmap.get(key);
                        if (!get.equals(value)) {
                            found = false;
                        }
                    }
                }
            }
            if (found) {
                result = actualmap;
                return result;
            }
        }
        return result;
    }

    public Map<String, String> toMap(String s) {
        Map<String, String> map = new HashMap<String, String>();
        for (final String entry : s.split(",")) {
            final String[] parts = entry.split("=");
            map.put(parts[0], parts[1]);
        }
        return map;
    }

    public String generatePayLoadForDropboxRemediation(String tenant, String user, String userId, String docId, String code) {
        String payload = "{\"objects\":[{\"db_name\":\""
                + tenant
                + "\",\"user\":\""
                + user
                + "\",\"user_id\":\""
                + userId
                + "\",\"doc_id\":\""
                + docId
                + "\",\"doc_type\":\"file\",\"actions\":[{\"code\":\""
                + code
                + "\",\"possible_values\":[],\"meta_info\":{}}]}]}";

        return payload;
    }

    public String generateUniqueKeyUsingUUID() {
        // Static factory to retrieve a type 4 (pseudo randomly generated) UUID
        String crunchifyUUID = UUID.randomUUID().toString();
        return crunchifyUUID;
    }

    public boolean matchPermission(PermissionList retrievePermissionList, List<String> expectedPermissionValues) throws IOException {
        boolean permissionFound = true;
        List<Permission> permissionList = retrievePermissionList.getItems();
        for (Permission permission : permissionList) {
            permissionFound = true;
            for (String expectedPermissionValue : expectedPermissionValues) {
                boolean containsValue = permission.containsValue(expectedPermissionValue);
                if (containsValue == false) {
                    permissionFound = false;
                }
            }
            if (permissionFound) {
                return permissionFound;

            }
        }// External For Loop...
        return false;
    }
    
    public String getExportPayloadForGDrive(String format, String SaaSApp, String csrftoken,
            String sessionid, String apiServerUrl, String userid, String startDate, String endDate) {
        String payLoad = "{\"source\":{\"query\":{\"filtered\":{\"query\":{\"bool\":{\"must\":[{\"range\":{\"created_timestamp\":{\"from\":\""
                + startDate
                + "\",\"to\":\""
                + endDate
                + "\"}}},{\"term\":{\"facility\":\""
                + SaaSApp
                + "\"}}],\"must_not\":[{\"term\":{\"facility\":\"Elastica\"}}]}},\"filter\":{}}},\"from\":0,\"size\":93772,\"sort\":{\"created_timestamp\":{\"order\":\"desc\",\"ignore_unmapped\":\"true\"}},\"facets\":{}},\"startDate\":\""
                + startDate
                + "\",\"endDate\":\""
                + endDate
                + "\",\"format\":\""
                + format
                + "\",\"app\":\""
                + SaaSApp
                + "\",\"sourceName\":\"investigate\",\"apiServerUrl\":\""
                + apiServerUrl
                + "\",\"csrftoken\":\""
                + csrftoken
                + "\",\"sessionid\":\""
                + sessionid
                + "\",\"userid\":\""
                + userid
                + "\"}";
        return payLoad;
    }
    
    public String getDropboxSharedLinkRevocationPayload(String userid,String docid){
        String payload="{\"source\":{\"objects\":{\"objects\":[{\"db_name\":\"securletbeatlecom\",\"user\":\""
                + "admin@securletbeatle.com"
                + "\",\"user_id\":\""
                + userid
                + "\",\"doc_id\":\""
                + docid
                + "\",\"instance\":null,\"doc_type\":\"file\",\"actions\":[{\"code\":\""
                + "SHARED_LINK_REVOKE"
                + "\",\"possible_values\":[],\"meta_info\":{}}]}]},\"app\":\"Dropbox\"}}";
        return payload;
    }

}
