/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;


/**
 * @author anuvrath
 *
 */
public class AuditFunctions extends AuditInitializeTests {

	/**
	 * @param url
	 * @return
	 */
	private static String replaceGenericParams(String url){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", suiteData.getTenantName());
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}
	
	private static String replaceGenericParams(String url,String tenantName){
		if(url.contains("tenantName"))
			url = url.replace("{tenantName}", tenantName);
		if(url.contains("version"))
			url = url.replace("{version}", suiteData.getBaseVersion());
		return url;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static List<NameValuePair> buildBasicHeaders() throws IOException{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.COOKIE, "sessionid="+suiteData.getSessionID()+"; csrftoken="+suiteData.getCSRFToken()+";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));	
		Reporter.log("requestHeader Mallesh..."+requestHeader,true);
		return requestHeader;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private static List<NameValuePair> buildServiceHeaders() throws IOException{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));	
		requestHeader.add(new BasicNameValuePair("X-Session",  suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		
		Reporter.log("serviceheaers:"+requestHeader,true);
		
		return requestHeader;
	}

	/**
	 * @param restClient
	 * @param dataSourceDTO
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse createDataSource(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createDataSource"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);								
	}
	
	public static HttpResponse getSummary(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditSummary"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);								
	}
	public static HttpResponse getAuditReport(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditReport"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);								
	}

	/**
	 * @param restClient
	 * @param queryParams
	 * @param sourceID
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getSignedDataResourceURL(Client restClient, List<NameValuePair> queryParams, String sourceID) throws Exception{
		String signedURL = replaceGenericParams(suiteData.getAPIMap().get("getSignedURL"));
		signedURL = signedURL.replace("{sourceID}", sourceID);
		URI signedURI = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),signedURL,queryParams);
		Logger.info(" Getting signed URL for dataSource: "+sourceID);
		return restClient.doGet(signedURI, buildBasicHeaders());		
	}

	/**
	 * @param restClient
	 * @param signedURL
	 * @param filePath
	 * @return
	 * @throws Exception 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static HttpResponse uploadFirewallLogFile(Client restClient,String signedURL, String filePath) throws URISyntaxException, IOException, Exception {			
		HttpEntity putBodyEntity = new FileEntity(new File(FileHandlingUtils.getFileAbsolutePath(filePath))); 
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("x-amz-acl", "private"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/zip"));
		requestHeader.add(new BasicNameValuePair("x-amz-server-side-encryption", "AES256"));
		Logger.info("******Request payload for Uploading file using S3 signed url*******:"+putBodyEntity);
		Logger.info("Upload Firewall Log: ");
		return restClient.doPut(ClientUtil.BuidURI(signedURL), requestHeader, null, putBodyEntity);
	}

	/**
	 * @param restClient
	 * @param id
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse notifyFileUploadStatus(Client restClient, String id) throws IOException, Exception{
		String notifyURL = replaceGenericParams(suiteData.getAPIMap().get("notifyFileUpload"));
		notifyURL = notifyURL.replace("{sourceID}", id);
		String postBody =  "{\"datasource_id\":\""+id+"\", \"last_status\":\"success\"}";
		Logger.info("*******Request payload for Notifying the upload status********: "+postBody);
		StringEntity entity = new StringEntity(postBody);
		Logger.info("Notifying the upload status");
		return restClient.doPost(ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), notifyURL), buildBasicHeaders(), null, entity);			
	}

	/**
	 * This method is throwing 301 (MOVED PERMANENTLY SO NEED TO FIX THIS)
	 * @param restClient
	 * @param sourceId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse deleteDataSource(Client restClient, String sourceId) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("deleteDataSource"));
		restAPI = restAPI.replace("{sourceID}", sourceId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("Deleting the data resource: "+sourceId);
		return restClient.doDelete(uri, buildBasicHeaders());	
	}

	/**
	 * @param restClient
	 * @param sourceID
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse pollForDataSourceStatus(Client restClient, String sourceID) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("pollDataSourceStatus"));
		restAPI = restAPI.replace("{sourceID}", sourceID);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}
	


	/**
	 * @param restClient
	 * @param sourceID
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse pollForSpanvaDataSourceStatus(Client restClient, String sourceID) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAgentDS"));
		restAPI = restAPI.replace("{sourceID}", sourceID);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @param queryParams 	 
	 * @return
	 * @throws Exception  
	 */	
	public static HttpResponse getAuditSummary(Client restClient, List<NameValuePair> queryParams) throws Exception{
		String summaryAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditSummary"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),summaryAPI, queryParams);
		Logger.info("Getting Audit Summary");
		return restClient.doGet(uri, buildBasicHeaders());
	}
	

	/**
	 * @param restClient
	 * @param queryParams 	 
	 * @return
	 * @throws Exception  
	 */	
	public static HttpResponse getAuditReport(Client restClient, List<NameValuePair> queryParams) throws Exception{
		String summaryAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditReport"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),summaryAPI, queryParams);
		Logger.info("Getting Audit Report");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAuditPreferences(Client restClient, List<NameValuePair> queryParams) throws Exception{
		String preferenceAPI = replaceGenericParams(suiteData.getAPIMap().get("auditPreferences"));
		URI uri;
		if(queryParams == null)
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), preferenceAPI);
		else
			uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), preferenceAPI,queryParams);
		Logger.info("Getting Preference");
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		return restClient.doGet(uri, header);
	}

	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse setAuditPreferences(Client restClient, HttpEntity entity) throws Exception {
		String preferenceAPI = replaceGenericParams(suiteData.getAPIMap().get("auditPreferences"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), preferenceAPI);
		Logger.info("Setting Preference");
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		header.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		return restClient.doPatch(uri, header, null, entity);
	}	

	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse checkS3Credentials(Client restClient, HttpEntity entity) throws Exception {
		String s3CredentialsAPI = replaceGenericParams(suiteData.getAPIMap().get("checkS3Credentials"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),s3CredentialsAPI);
		Logger.info("Checking Credentials for S3");
		return restClient.doPost(uri, buildBasicHeaders(), null, entity);
	}
	/**
	 * @param restClient
	 * @param queryParams
	 * @param csfrToken
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getDataSourceList(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createDataSource"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}
	
	public static HttpResponse getSpanvaUrl(Client restClient) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getSpanvaDownloadImageurl"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}

	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getDataSourceListAndInfo(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getDSList"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}

	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse createSpanVADataSource(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createAgentDS"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);								
	}

	/**
	 * @param restClientO
	 * @param sourceID
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getCredentials(Client restClient) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getScpCredentials"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAllAgentsSpanVADetails(Client restClient) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAllAuditAgentAppliances"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAgentsSpanVADetails(Client restClient, String agentId) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditAgentAppliance"));
		restAPI = restAPI.replace("{sourceID}", agentId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAllAgentsList(Client restClient) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listAgents"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
		Logger.info("Getting all agents list: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAgent(Client restClient, String agentId) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAgent"));
		restAPI = restAPI.replace("{sourceID}", agentId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(), restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeaders());
	}

	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse generateAgentRegistrationKey(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createAgent"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);
		Logger.info("generate key :"+dataUri.toString());
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);	

	}
	
	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse generateAgentRegistrationKey(Client restClient) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("createAgent"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);
		Logger.info("generate key :"+dataUri.toString());
		return restClient.doGet(dataUri, buildBasicHeaders());	

	}
	
	/**
	 * @param restClient
	 * @param agentId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse deleteAgent(Client restClient, String agentId) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("deleteAgent"));
		restAPI = restAPI.replace("{sourceID}", agentId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("Deleting the data resource: "+agentId);
		return restClient.doDelete(uri, buildBasicHeaders());	
	}


	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getServiceDetails(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getServiceDetails"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}

	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getServicesBrrRate(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("serviceBRRScores"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}

	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getUsers(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getUserDetails"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildServiceHeaders());		
	}

	/**
	 * @param restClient
	 * @param signedURL
	 * @param users_uri_headersObj
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse getUsersInfoFromS3(Client restClient,String signedURL, JSONObject users_uri_headersObj) throws URISyntaxException, IOException, Exception {	
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("Authorization", users_uri_headersObj.getString("Authorization")));
		requestHeader.add(new BasicNameValuePair("x-amz-date", users_uri_headersObj.getString("x-amz-date")));
		Logger.info("Upload Firewall Log: ");
		return restClient.doGet(ClientUtil.BuidURI(signedURL), requestHeader);	
	}

	/**
	 * @param restClient
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse getTIAAuditPreferences(Client restClient) throws URISyntaxException, IOException, Exception {	
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("tiaGetPreferences"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,null);	
		List<NameValuePair> headers=buildBasicHeaders();
		return restClient.doGet(uri,headers);	

	}

	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse setTIAAuditPreferences(Client restClient, HttpEntity entity) throws URISyntaxException, IOException, Exception {	
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("tiaUpdatePreferences"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI);		
		return restClient.doPost(uri, buildBasicHeaders(), null, entity);	
	}
	
	/**
	 * @param restClient
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse getDashBoardReportSources(Client restClient) throws URISyntaxException, IOException, Exception {	
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("db_reportsources"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,null);	
		List<NameValuePair> headers=buildServiceHeaders();
		return restClient.doGet(uri,headers);	
	}
	
	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse reportSourceData(Client restClient,HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("reportSourceData"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		
		return restClient.doPost(dataUri, buildServiceHeaders(), null, entity);			
	}
   
	
	/**
	 * @param restClient
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws Exception
	 */
	public static HttpResponse getPredefinedWidgets(Client restClient) throws URISyntaxException, IOException, Exception {	
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("allPredefinedWidgets"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,null);	
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		return restClient.doGet(uri,requestHeader);	

	}
	
	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getAuditReportSourceConfig(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("db_auditReportSourceConfig"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);	
		List<NameValuePair> headers=buildServiceHeaders();
		return restClient.doGet(uri, headers);		
	}
	
	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getServiceTabDataApi(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("serviceUsersTabDataApi"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);	
		List<NameValuePair> headers=buildServiceHeaders();
		Logger.info("headers::"+headers);
		return restClient.doGet(uri, headers);		
	}

	public static HttpResponse getServiceTabDataApiThroughPost(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("serviceUsersTabDataApi"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildServiceHeaders(), null, entity);								
	}
	
	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getTenantServices(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("tenantServices"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}
	

	//Tags Apis
	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse createTag(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("addTag_or_getTags"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);								
	}
	
	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse listAllTags(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("addTag_or_getTags"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}
	
	/**
	 * @param restClient
	 * @param tagid
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getTag(Client restClient, String tagid) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("delete_Or_get_Or_edit_Tag"));
		restAPI = restAPI.replace("{tagid}", tagid);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("get tag: "+tagid);
		return restClient.doGet(uri, buildBasicHeaders());	
	}
	
	/**
	 * @param restClient
	 * @param tagid
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse deleteTag(Client restClient, String tagid) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("delete_Or_get_Or_edit_Tag"));
		restAPI = restAPI.replace("{tagid}", tagid);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("Deleting the tag: "+tagid);
		return restClient.doDelete(uri, buildBasicHeaders());	
	}
	
	/**
	 * @param restClient
	 * @param entity
	 * @param tagid
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse editTag(Client restClient, HttpEntity entity,String tagid) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("delete_Or_get_Or_edit_Tag"));
		restAPI = restAPI.replace("{tagid}", tagid);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		Logger.info("editing the tag: "+tagid);
		return restClient.doPatch(dataUri, buildBasicHeaders(), null, entity);								
	}
	
	/**
	 * @param restClient
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getServieTags(Client restClient) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("add_Or_getServiceTags"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		return restClient.doGet(uri, buildBasicHeaders());	
	}
	
	/**
	 * @param restClient
	 * @param entity
	 * @param serviceTagObjId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getServieTagInfo(Client restClient,HttpEntity entity,String serviceTagObjId) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("change_serviceTags"));
		restAPI = restAPI.replace("{service_tagid}", serviceTagObjId);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		Logger.info("editing the tag: "+serviceTagObjId);
		return restClient.doPatch(dataUri, buildBasicHeaders(), null, entity);			
	}
	
	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse changeTagToService(Client restClient,HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("add_Or_getServiceTags"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		return restClient.doPatch(dataUri, buildBasicHeaders(), null, entity);			
	}
	
	/**
	 * @param restClient
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse addCommentToService(Client restClient,HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("addT_Comment"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		return restClient.doPost(dataUri, buildBasicHeaders(), null, entity);			
	}
	

	
	public static HttpResponse editOrDeleteCommentToService(Client restClient,HttpEntity entity,String commentId) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("edit_Or_delete_Comments"));
		restAPI = restAPI.replace("{commentId}", commentId);
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);	
		Logger.info("editing the comemnt: "+commentId);
		return restClient.doPatch(dataUri, buildBasicHeaders(), null, entity);			
	}
	
	
	/**
	 * @param restClient
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse getComment(Client restClient, List<NameValuePair> queryParams) throws Exception{		
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("addT_Comment"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI,queryParams);														
		return restClient.doGet(uri, buildBasicHeaders());		
	}
	
	/**
	 * @param restClient
	 * @param dsId
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse listSpanvaAgentDSs(Client restClient) throws Exception {
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("listAgentDS"));
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);														
		return restClient.doGet(uri, buildBasicHeaders());
	}
	
	public static HttpResponse deleteSpanaAgentDataSource(Client restClient, String sourceId) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("deleteAgentDS"));
		restAPI = restAPI.replace("{sourceID}", sourceId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("Deleting the data resource: "+sourceId);
		return restClient.doDelete(uri, buildBasicHeaders());	
	}
	
	public static HttpResponse getDSAnonyUsers(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getDSAnonyUsers"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildServiceHeaders(), null, entity);								
	}
	
	public static HttpResponse getRevealUserFromAnonyUser(Client restClient, HttpEntity entity) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("useranonymize"));
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, anonyHeaders(), null, entity);								
	}
	
	public static List<NameValuePair> anonyHeaders() throws Exception
	{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.COOKIE, "sessionid="+suiteData.getSessionID()+"; csrftoken="+suiteData.getCSRFToken()+";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));	
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-DPO-USERNAME", suiteData.getDpoUsername()));
		requestHeader.add(new BasicNameValuePair("X-DPO-PASSWORD", suiteData.getDpoPassword()));	
	
		return requestHeader;
	}
	
//Audit customer seperation tickets
	
	public static HttpResponse pollForDataSourceStatusAclCheck(Client restClient, String sourceID, String tenant, String sessionId, String csrfToken, String authParam) throws Exception {
		Reporter.log("pollForDataSourceStatusAclCheck:: tenant details:"+tenant+"sessionid: "+sessionId+" csrftoken:"+csrfToken+" authParam: "+authParam);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("pollDataSourceStatus"),tenant);
		restAPI = restAPI.replace("{sourceID}", sourceID);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);
		Logger.info("Looking for Data Source Creating Status: ");
		return restClient.doGet(uri, buildBasicHeadersAclCheck(sessionId,csrfToken,authParam));
	}
	public static HttpResponse getSummaryACLCheck(Client restClient, HttpEntity entity,String tenant, String sessionId, String csrfToken, String authParam) throws Exception{
		Reporter.log("getSummaryACLCheck:: tenant details:"+tenant+"sessionid: "+sessionId+" csrftoken:"+csrfToken+" authParam: "+authParam);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditSummary"),suiteData.getUser2TenantName());
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeadersAclCheck(sessionId,csrfToken,authParam), null, entity);								
	}
	public static HttpResponse getAuditReportACLCheck(Client restClient, HttpEntity entity,String tenant,String sessionId, String csrfToken, String authParam) throws Exception{
		Reporter.log("getAuditReportACLCheck :: tenant details:"+tenant+"sessionid: "+sessionId+" csrftoken:"+csrfToken+" authParam: "+authParam);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getAuditReport"),suiteData.getUser2TenantName());
		URI dataUri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);				
		return restClient.doPost(dataUri, buildBasicHeadersAclCheck(sessionId,csrfToken,authParam), null, entity);								
	}
	public static HttpResponse deleteDataSourceACLCheck(Client restClient, String sourceId,String tenant,String sessionId, String csrfToken, String authParam) throws Exception{
		Reporter.log("deleteDataSourceACLCheck:: tenant details:"+tenant+"sessionid: "+sessionId+" csrftoken:"+csrfToken+" authParam: "+authParam);
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("deleteDataSource"),suiteData.getUser2TenantName());
		restAPI = restAPI.replace("{sourceID}", sourceId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getApiserverHostName(),restAPI);												
		Logger.info("Deleting the data resource: "+sourceId);
		return restClient.doDelete(uri, buildBasicHeadersAclCheck(sessionId,csrfToken,authParam));	
	}

	public static List<NameValuePair> buildBasicHeadersAclCheck(String sessionId, String csrfToken, String authParam) throws IOException{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.COOKIE, "sessionid="+sessionId+"; csrftoken="+csrfToken+";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, authParam));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", csrfToken));
		Reporter.log("requestHeader::"+requestHeader,true);
		return requestHeader;
	}

	public static void DownloadFileFormS3(String fireWallType) {
		S3ActionHandler s3 = new S3ActionHandler();
		try {
			
			String LogFilePath = AuditTestUtils.getFirewallLogFilePath(fireWallType);
			String result[] = LogFilePath.split("/");
			String FileName = result[result.length - 1]; 
			Reporter.log("Started downloading the file for S3 ::"+FileName,true);
			s3.downloadFileFromS3Bucket(AuditTestConstants.AUDIT_S3_BUCKET, "Audit/be_firewall_logs"+File.separator+FileName, 
					AuditTestConstants.AUDIT_FILE_TEMP_PATH+File.separator+FileName);
			Reporter.log("Complited downloading the file for S3 ::"+FileName,true);													   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static HttpResponse getAgentRegistrationToken(Client restClient, List<NameValuePair> queryParams) throws Exception{
		String restAPI = replaceGenericParams(suiteData.getAPIMap().get("getRegKey"));
		//restAPI = restAPI.replace("{agentId}", agentId);
		URI uri = ClientUtil.BuidURI(suiteData.getScheme(), suiteData.getHost(),restAPI,queryParams);	
		return restClient.doGet(uri, buildBasicHeaders());	
	}

		
}