/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.beatle.reporting;

import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RawJsonParser;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;


public class ThousandEyesUtils {

    String getTransactionTests = "https://api.thousandeyes.com/tests/transactions.json";
    String updateTransactionTests="https://api.thousandeyes.com/tests/transactions";// /817/update.json";
    String username = "mohammad.usman@elastica.co";
    String password = "**************";
    String authParam;
    Client restClient;
    List<NameValuePair> headers=new ArrayList();
    
    public ThousandEyesUtils() {  
        try {
            this.restClient=new Client();
            headers.add(new BasicNameValuePair("authorization", AuthorizationHandler.getAuthParam(username, password))); 
            headers.add(new BasicNameValuePair("Content-Type", "application/json"));  
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThousandEyesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateTest(String testId,String payLoad){
        
        String updateTransactionTestsURI=updateTransactionTests+"/"+testId+"/"+"update.json";
        try {
            HttpResponse doPost = restClient.doPost(new URI(updateTransactionTestsURI), headers, null, new StringEntity(payLoad));
            System.out.println("Update Test Response :"+ClientUtil.getResponseBody(doPost));
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(ThousandEyesUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ThousandEyesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public List<Long> getTestIds(){
        try {
            HttpResponse doGet = restClient.doGet(new URI(getTransactionTests), headers);
            String responseBody = ClientUtil.getResponseBody(doGet);
            System.out.println("Get Tests Info :"+responseBody);
            List<Long> fetchMultipleFields = (List<Long>) RawJsonParser.fetchMultipleFields(responseBody, "$.test[*].testId");
            //List<Long> fetchMultipleFields = (List<Long>) RawJsonParser.fetchMultipleFields(responseBody, "$.test[*].interval");
            
            return fetchMultipleFields;
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ThousandEyesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    
    
    public static void main(String[] args) {

        ThousandEyesUtils eyesUtils=new ThousandEyesUtils();
        List<Long> testIds = eyesUtils.getTestIds();//$.hits.hits[*].source
        System.out.println(testIds.size());
        for (long testId : testIds) {
            String strLong = Long.toString(testId);
            System.out.println("==>"+strLong); 
            String payLoad="{\"interval\":3600}";
            
           // eyesUtils.updateTest(Long.toString(testId), payLoad);
        }
        
       // String testId="88299";
       // String payLoad="{\"interval\":3600}";
       // eyesUtils.updateTest(testId, payLoad);
        
    }
}
