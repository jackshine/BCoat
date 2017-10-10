package com.elastica.action.backend.sauce;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.common.SuiteData;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;


public class SauceLabsHelper {

	String host="saucelabs.com/rest/v1";
	
	public HttpResponse getJobs(Client restClient, SuiteData suiteData) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse getNumberOfJobs(Client restClient, SuiteData suiteData, int number) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("limit", Integer.toString(number)));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, queryParams);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse getJobsDetails(Client restClient, SuiteData suiteData) throws Exception {
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("full", "true"));
		
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, queryParams);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse getSkippedJobs(Client restClient, SuiteData suiteData, int number) throws Exception {
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("skip", Integer.toString(number)));
		
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, queryParams);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse getJobsFromTime(Client restClient, SuiteData suiteData, String time) throws Exception {
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("from", time));
		
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, queryParams);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse getJobsInCSV(Client restClient, SuiteData suiteData) throws Exception {
		List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
		queryParams.add(new BasicNameValuePair("format", "csv"));
		
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, queryParams);
		
		return restClient.doGet(dataUri, requestHeaders);
	}

	public HttpResponse updateJob(Client restClient, SuiteData suiteData, 
			String jobId, StringEntity stringEntity) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs/"+jobId;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, null);
		
		return restClient.doPut(dataUri, requestHeaders, null, stringEntity);
	}
	
	public HttpResponse deleteJob(Client restClient, SuiteData suiteData, String jobId) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs/"+jobId;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, null);
		
		return restClient.doDelete(dataUri, requestHeaders);
	}
	
	public HttpResponse stopJob(Client restClient, SuiteData suiteData, 
			String jobId, StringEntity stringEntity) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs/"+jobId+"/stop";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, null);
		
		return restClient.doPut(dataUri, requestHeaders, null, stringEntity);
	}
	
	public HttpResponse deleteJobAssets(Client restClient, SuiteData suiteData, String jobId) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs/"+jobId+"/assets";
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, null);
		
		return restClient.doDelete(dataUri, requestHeaders);
	}
	
	public HttpResponse getJobAssetFile(Client restClient, SuiteData suiteData, 
			String jobId,String assetName) throws Exception {
		List<NameValuePair> requestHeaders = new ArrayList<NameValuePair>();
		requestHeaders.add(new BasicNameValuePair("u",suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()));
		requestHeaders.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, 
				ClientUtil.getAuthParam(suiteData.getSauceUsername(), suiteData.getSaucePassword())));
		
		String requestUri = "/"+suiteData.getSauceUsername()+"/jobs/"+jobId+"/assets/"+assetName;
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), 
				suiteData.getSauceUsername()+":"+suiteData.getSaucePassword()+"@"+host, requestUri, null);
		
		return restClient.doGet(dataUri, requestHeaders);
	}
	


	
}
