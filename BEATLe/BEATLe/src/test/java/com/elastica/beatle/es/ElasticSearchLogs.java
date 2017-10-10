package com.elastica.beatle.es;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;

import com.elastica.beatle.detect.DetectSuiteParams;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;

public class ElasticSearchLogs {
	
	private String esUserName = "eoeadmin";
	private String esPassword = "3akzXOJ2K6SptRwHQraPTte5ax67xN2D";


	public HttpResponse getDisplayLogs(Client restClient, List<NameValuePair> requestHeader, String apiServer, StringEntity se) throws Exception {

		String requestUri = "/eslogs/displaylogs/";

		URI dataUri = ClientUtil.BuidURI("https", apiServer, requestUri);

		return restClient.doPost(dataUri, requestHeader, null, se);
	}

	public HttpResponse getUserScoreAnomalies(Client restClient, List<NameValuePair> requestCookieHeader, String apiServer, List<NameValuePair> queryParams) throws Exception {

		String requestUri = "/admin/application/list/user_score_anomalies/";

		URI dataUri = ClientUtil.BuidURI("https", apiServer, requestUri, queryParams);

		return restClient.doGet(dataUri, requestCookieHeader);
	}

	public HttpResponse getUserScoreAnomalies(Client restClient, List<NameValuePair> requestCookieHeader, List<NameValuePair> queryParams,DetectSuiteParams suiteData) throws Exception {

		String requestUri = "/admin/application/list/user_score_anomalies/";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri, queryParams);

		return restClient.doGet(dataUri, requestCookieHeader);
	}

	public HttpResponse getThreatscore(Client restClient, String user, String tenantName) throws Exception {

		String requestUri = "/alias_logs_" + tenantName + "/elastica_threatscore/_search";		
		URI dataUri = ClientUtil.BuidURI("http", "internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200", requestUri);

		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(esUserName,esPassword)));
		
		StringEntity se = new StringEntity("{ \"size\":12000, query:{term:{\"user\":\"" + user + "\" }}}");
		Reporter.log("input payload for getThreatscore  ::: "+se.getContent());
		return restClient.doPost(dataUri, requestHeader, null, se);
	}

	public HttpResponse getState(Client restClient, String user, String tenantName) throws Exception {

		String requestUri = "/alias_logs_" + tenantName + "-2016/elastica_state/_search";

		URI dataUri = ClientUtil.BuidURI("http", "internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200", requestUri);

		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(esUserName,esPassword)));
		
		StringEntity se = new StringEntity("{ \"size\":12000, query:{term:{\"user\":\"" + user + "\" }}}");

		return restClient.doPost(dataUri, requestHeader, null, se);
	}


	public HttpResponse detectsequences(Client restClient, List<NameValuePair> requestCookieHeader,  StringEntity se, TestSuiteDTO suiteData) throws Exception {

		String requestUri = "/admin/analytics/detectsequences";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);

		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}

	public HttpResponse get_es_logs_threatscore_tree(Client restClient, List<NameValuePair> requestCookieHeader, String apiServer, String user) throws Exception {

		String requestUri = "/admin/application/list/get_es_logs_threatscore_tree";

		URI dataUri = ClientUtil.BuidURI("https", apiServer, requestUri);
		String s = "{\"threatscore_userid\":\""+user +"\",\"sourceName\":\"DETECT\"}";
		StringEntity se = new StringEntity(s);
		Reporter.log("get_es_logs_threatscore_tree payload:::  "+s, true);

		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}



	public HttpResponse clearThreatscore(Client restClient, String user, String tenantName) throws Exception {

		//		curl -XGET internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200/alias_logs_elasticaqanet/elastica_threatscore/_query -d '{term:{\"user\":\"$user\"}}}'
		String requestUri = "/alias_logs_" + tenantName + "/elastica_threatscore/_query";

		URI dataUri = ClientUtil.BuidURI("http", "internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200", requestUri);

		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(esUserName,esPassword)));
		
		StringEntity se = new StringEntity("{term:{\"user\":\"" + user + "\"}}}");

		return restClient.doDelete(dataUri, requestHeader, se);
	}

	public HttpResponse clearState(Client restClient, String user, String tenantName) throws Exception {

		//		curl -XGET internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200/alias_logs_elasticaqanet-2016/elastica_state/_query -d '{term:{\"user\":\"$user\"}}}'
		String requestUri = "/alias_logs_" + tenantName + "-2016/elastica_state/_query";

		URI dataUri = ClientUtil.BuidURI("http", "internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200", requestUri);

		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(esUserName,esPassword)));
		
		StringEntity se = new StringEntity("{term:{\"user\":\"" + user + "\"}}}");

		return restClient.doDelete(dataUri, requestHeader, se);
	}

	public HttpResponse clearDetectProfile(Client restClient, String user, String tenantName) throws Exception {

		//		curl -XGET internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200/alias_logs_elasticaqanet-2016/elastica_state/_query -d '{ \"bool\" : { \"must\" :[ { term:{\"event_type\":\"ProfileReport\"} }, {term:{\"user\":\"$user\"}} ] } } \''
		String requestUri = "/alias_logs_" + tenantName + "-2016/elastica_state/_query";

		URI dataUri = ClientUtil.BuidURI("http", "internal-eoe-es-1923969177.us-west-2.elb.amazonaws.com:9200", requestUri);

		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(esUserName,esPassword)));
		
		StringEntity se = new StringEntity("{ \"bool\" : { \"must\" :[ { term:{\"event_type\":\"ProfileReport\"} }, {term:{\"user\":\"" + user + "\"}} ] } }");

		return restClient.doDelete(dataUri, requestHeader, se);
	}

	public HttpResponse getCloudServiceAnomalies(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se, TestSuiteDTO suiteData ) throws Exception {

		//		POST
		//		String requestUrl = "https://eoe.elastica-inc.com/admin/application/list/user_score_anomalies";
		String requestUri = "/admin/application/list/cloud_service_anomalies/";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);

		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}


	public HttpResponse unblockUser(Client restClient, List<NameValuePair> requestCookieHeader, List<NameValuePair> queryParams,TestSuiteDTO suiteData) throws Exception {

		//		GET
		//https://eoe.elastica-inc.com/admin/analytics/unblock?app_action=NOTHREAT&blocked_apps=Across+Services&email=detect_9EysQGwKBK8U@detectautomation.com&report_id=OWDOt6-aRzqvVr2MmouO-Q
		String requestUri = "/admin/analytics/unblock";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri, queryParams);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		return restClient.doPost(dataUri, requestCookieHeader, queryParams, null);
	}

	public HttpResponse updateLog(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se,TestSuiteDTO suiteData) throws Exception {

		//		POST
		//https://eoe.elastica-inc.com/admin/analytics/updateLog
		String requestUri = "/admin/analytics/updateLog";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		return restClient.doPost(dataUri, requestCookieHeader, null,se);
	}

	public HttpResponse postanomalyvalues(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se,TestSuiteDTO suiteData) throws Exception {

		//		POST
		//https://eoe.elastica-inc.com/admin/analytics/postanomalyvalues
		String requestUri = "/admin/analytics/postanomalyvalues";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		return restClient.doPost(dataUri, requestCookieHeader, null,se);
	}

	public HttpResponse getanomalyvalues(Client restClient, List<NameValuePair> requestCookieHeader,TestSuiteDTO suiteData) throws Exception {

		//		POST
		//https://eoe.elastica-inc.com/admin/analytics/getanomalyvalues
		String requestUri = "/admin/analytics/getanomalyvalues";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		System.out.println("unblockUser dataUri:::::::: "+dataUri);
		return restClient.doGet(dataUri, requestCookieHeader);
	}


	public HttpResponse getUserThreatsData(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se,TestSuiteDTO suiteData) throws Exception {

		//		POST
		//https://api-eoe.elastica-inc.com/eslogs/getUserThreatsData/
		String requestUri = "/eslogs/getUserThreatsData/";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getApiserverHostName(), requestUri);
		System.out.println("getUserThreatsData dataUri:::::::: "+dataUri);
		return restClient.doPost(dataUri, requestCookieHeader, null,se);
	}

	public HttpResponse getNotes(Client restClient, List<NameValuePair> requestCookieHeader, List<NameValuePair> queryParams,TestSuiteDTO suiteData) throws Exception {

		//		GET
		//		String requestUrl = "https://eoe.elastica-inc.com/admin/analytics/getNotes?source=%7B%22id%22:%228qAIAnbcR1usJL0dZrS_4g%22,%22type%22:%22elastica_state%22,%22index%22:%22logs_detectbackendautomationcom-2016%22,%22event_type%22:%22AnomalyReport%22%7D&sourceName=DETECT";
		String requestUri = "/admin/analytics/getNotes";

		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri, queryParams);

		return restClient.doGet(dataUri, requestCookieHeader);
	}

	public HttpResponse updateNotes(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se, TestSuiteDTO suiteData) throws Exception {

		String requestUri = "/admin/analytics/updateNotes";
		
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);

		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}
	
	public HttpResponse getCloudServiceAnomalies(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se, String host) throws Exception {
		String requestUri = "/admin/application/list/cloud_service_anomalies/";
		URI dataUri = ClientUtil.BuidURI("https", host, requestUri);
		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}

	public HttpResponse bulkIncidentUpdate(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se, TestSuiteDTO suiteData) throws Exception {
		String requestUri = "/admin/analytics/bulkincidentsupdate";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getHost(), requestUri);
		return restClient.doPost(dataUri, requestCookieHeader, null, se);
	}
	
	
	public HttpResponse updateLog(Client restClient, List<NameValuePair> requestCookieHeader, StringEntity se,String host) throws Exception {

		String requestUri = "/admin/analytics/updateLog";
		URI dataUri = ClientUtil.BuidURI("https", host, requestUri);
		return restClient.doPost(dataUri, requestCookieHeader, null,se);
	}

}
