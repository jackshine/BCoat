package com.elastica.tests.o365;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;

import com.elastica.action.dci.DCIAction;
import com.elastica.action.infra.TestSuiteDTO;

import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;

public class CiqUtils {
	protected DCIAction dci= new DCIAction();
	protected Client restClient = new Client();;

	public HttpResponse createProfile(SuiteData suiteDatadto,List<NameValuePair> headers,String payload) throws Exception
	 {
		String uri ="/controls/add_ciq_profile post";
		Reporter.log(" Creating new CIQ profile  ",true);
		URI dataUri = ClientUtil.BuidURI(suiteDatadto.getScheme(),suiteDatadto.getHost(),uri);
		Reporter.log("create API :"+dataUri,true);
		HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
		return response;
	 }
	public void createCIQProfile( String threshold, SuiteData suiteData,String ciqProfileName,String ciqProfileDescription,String source,String value){
		
		try {
			String payload ="{\"threshold\":"+threshold+",\"description\":\""+ciqProfileDescription+"\",\"name\":\""+ciqProfileName+"\",\"groups\":[{\"operator\":\"AND\",\"operands\":[{\"name\":\"\",\"weight\":1,\"is_not\":false,\"value\":[\""+value+"\"],\"source\":\""+source+"\",\"min_count\":1}]}],\"appliesToSecurlets\":false,\"api_enabled\":false,\"domains\":[\"__ALL_EL__\"]}";
			
			HttpResponse response=createProfile(suiteData, dci.getCookieHeaders(suiteData), payload);
			String responseBody = ClientUtil.getResponseBody(response);
			//Logger.info("completed"+responseBody);
			Logger.info("Creating CIQ profile  is completed");
			Thread.sleep(10000);

		} catch (Exception ex) {
			Logger.info("Issue with Create Content Iq Profiles" + ex.getLocalizedMessage());
		}
		//return ciq;
	}


}
