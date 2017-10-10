package com.universal.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;

public class HttpClient {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String sendGET(String url, Map<String, String> headers) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            for (Map.Entry<String, String> entrySet : headers.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                httpGet.addHeader(key, value);
            }
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            httpClient.close();
            return response.toString();
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
     public static HttpResponse doGet(String url, Map<String, String> headers) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            if(headers!=null){
            for (Map.Entry<String, String> entrySet : headers.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                httpGet.addHeader(key, value);
            }
            }
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            return httpResponse;
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String sendPOST(String url, Map<String, String> headers, Map<String, String> postParameters) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            for (Map.Entry<String, String> entrySet : headers.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                httpPost.addHeader(key, value);
            }
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entrySet : postParameters.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                urlParameters.add(new BasicNameValuePair(key, value));
            }
            HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
            httpPost.setEntity(postParams);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            System.out.println("POST Response Status:: "
                    + httpResponse.getStatusLine().getStatusCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpResponse.getEntity().getContent()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            // print result
            // System.out.println(response.toString());
            httpClient.close();
            return response.toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String multipartRequest(String url, Map<String, String> headers, String localFileLocation, String fileName) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url);
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            uploadFile.addHeader(key, value);
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody(fileName, "yes", ContentType.TEXT_PLAIN);
        builder.addBinaryBody("attachment", new File(localFileLocation), ContentType.APPLICATION_OCTET_STREAM, fileName);
        HttpEntity multipart = builder.build();

        uploadFile.setEntity(multipart);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(uploadFile);
        } catch (IOException ex) {
            Reporter.log("Multipart Execution Exception :"+ex.getLocalizedMessage(),true);
        }
        
        return CommonTest.getResponseBody(response);
        

    }

}
