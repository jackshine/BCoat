package com.elastica.beatle.tests.bop;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import com.elastica.beatle.CommonTest;
import java.io.IOException;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.tests.infra.InfraConstants;
import com.elastica.beatle.tests.infra.InfraUtils;

public class BopTest extends CommonTest {

	
	List<NameValuePair> headers;
	InfraUtils Infrautils;
	String tenant=null;
	String bopuser=null;
	/*Author: Vijay Gangwar
	 *todo: revoke invenstigate request 
	 *params none
	 */
	@BeforeClass(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		


		System.setProperty("jsse.enableSNIExtension", "false");
		Infrautils= new InfraUtils();
		 bopuser=suiteData.getUsername();
		bopuser=bopuser.replace("@", "%40");
		 tenant=suiteData.getTenantName();
		System.out.println("** revokeDetect** Response: "+suiteData.getReferer());
	}
	@Test(priority = 1, dataProvider = "revokeData",groups = { "EOE"} )
	public void revokeGrantTest(String Modulename,String category,String action,String user,String priDomain ) throws Exception {

		Reporter.log("exeuting testcase ****revokeDetect() ***** ...Test Rail id:C1052425",true);
		Reporter.log("====================Description==============",true);
		Reporter.log("1.BOP admin to change module :"+Modulename,true);
		Reporter.log("2.BOP admin should take action :"+action,true);		
		String QueryParams ="name="+Modulename+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
		Reporter.log("Get request url "+uri,true);
		URI dataUri=new URI(uri);	
		String responseBody = Infrautils.getdigestheader( uri,suiteData.getUsername(),suiteData.getPassword());
		assertEquals( getKeyResponseBody(responseBody,"action_status"),InfraConstants.BOP_SUCCESS,"App status change failed");		
		Reporter.log(action+"  action is sucessfull",true);


	}


	@Test(priority = 1, dataProvider = "revokeDataQAVPC",groups = { "QAVPC"} )
	public void revokeGrantTestQAVPC(String Modulename,String category,String action,String user,String priDomain ) throws Exception {
		//@Test(priority = 1)
		//public void revokeDetect( ) throws Exception {

		Reporter.log("exeuting testcase ****revokeDetect() ***** ...Test Rail id:C1052425",true);
		Reporter.log("====================Description==============",true);
		Reporter.log("1.user has acesss to module"+Modulename,true);
		Reporter.log("1.BOP admin should take action"+action,true);

		String QueryParams ="name="+Modulename+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		//   String uri =suiteData.getReferer()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;//"name=Audit&category=Elastica&action=grant&user=vijay.gangwar%40elastica.co&priDomain=testdel.com";
		String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
		Reporter.log("Get request url "+uri,true);
		URI dataUri=new URI(uri);	
		String responseBody = Infrautils.getdigestheader( uri,suiteData.getUsername(),suiteData.getPassword());
		assertEquals( getKeyResponseBody(responseBody,"action_status"),InfraConstants.BOP_SUCCESS,"App status change failed");		
		Reporter.log(action+"  action is sucessfull",true);



	}

	@DataProvider
	public Object[][] revokeData() {
		return new Object[][]{
			//String name,String category,String action,String user,String priDomain   
			//"name=Detect&category=Elastica&action=revoke&user=vijay.gangwar%40elastica.co&priDomain=testdel.com";
			{ "Audit",  "Elastica", "grant",bopuser,tenant},
			{ "Audit",  "Elastica", "revoke",bopuser,tenant},
			{ "Protect",  "Elastica", "grant",bopuser,tenant},
			{ "Protect",  "Elastica", "revoke",bopuser,tenant},
			{ "Detect",  "Elastica", "grant",bopuser,tenant},
			{ "Detect",  "Elastica", "revoke",bopuser,tenant},
			{ "ALL",  "Gateway", "grant",bopuser,tenant},
			{ "ALL",  "Gateway", "revoke",bopuser,tenant},
			{ "Investigate",  "Elastica", "grant",bopuser,tenant},
			{ "Investigate",  "Elastica", "revoke",bopuser,tenant},
			{ "Box",  "API", "grant",bopuser,tenant},
			{ "Box",  "API", "revoke",bopuser,tenant,},
			{ "Salesforce",  "API", "grant",bopuser,tenant},
			{ "Salesforce",  "API", "revoke",bopuser,tenant,},
			{ "Asana",  "API", "grant",bopuser,tenant},
			{ "Asana",  "API", "revoke",bopuser,tenant,},
			{ "ServiceNow",  "API", "grant",bopuser,tenant},
			{ "ServiceNow",  "API", "revoke",bopuser,tenant,},
			{ "NetSuite",  "API", "grant",bopuser,tenant},
			{ "NetSuite",  "API", "revoke",bopuser,tenant,},
			{ "DocuSign",  "API", "grant",bopuser,tenant},
			{ "DocuSign",  "API", "revoke",bopuser,tenant,},
			{ "Zendesk",  "API", "grant",bopuser,tenant},
			{ "Zendesk",  "API", "revoke",bopuser,tenant,},
			{ "GitHub",  "API", "grant",bopuser,tenant},
			{ "GitHub",  "API", "revoke",bopuser,tenant,},
			{ "Google+Apps",  "API", "grant",bopuser,tenant},
			{ "Google+Apps",  "API", "revoke",bopuser,tenant,},
			{ "Jive",  "API", "grant",bopuser,tenant},
			{ "Jive",  "API", "revoke",bopuser,tenant,},
			{ "Dropbox",  "API", "grant",bopuser,tenant},
			{ "Dropbox",  "API", "revoke",bopuser,tenant,},
			{ "Yammer",  "API", "grant",bopuser,tenant},
			{ "Yammer",  "API", "revoke",bopuser,tenant,},
			{ "Assembla",  "API", "grant",bopuser,tenant},
			{ "Assembla",  "API", "revoke",bopuser,tenant,},
			{ "Amazon+Web+Services",  "API", "grant",bopuser,tenant},
			{ "Amazon+Web+Services",  "API", "revoke",bopuser,tenant,},
			{ "Office+365",  "API", "grant",bopuser,tenant},
			{ "Office+365",  "API", "revoke",bopuser,tenant,},
		};
	}

	@DataProvider
	public Object[][] revokeDataQAVPC() {
		return new Object[][]{
			//String name,String category,String action,String user,String priDomain   
			//"name=Detect&category=Elastica&action=revoke&user=vijay.gangwar%40elastica.co&priDomain=testdel.com";
			{ "Audit",  "Elastica", "grant",bopuser,tenant},
			{ "Audit",  "Elastica", "revoke",bopuser,tenant},
			{ "Protect",  "Elastica", "grant",bopuser,tenant},
			{ "Protect",  "Elastica", "revoke",bopuser,tenant},
			{ "Detect",  "Elastica", "grant",bopuser,tenant},
			{ "Detect",  "Elastica", "revoke",bopuser,tenant},
			{ "ALL",  "Gateway", "grant",bopuser,tenant},
			{ "ALL",  "Gateway", "revoke",bopuser,tenant},
			{ "Investigate",  "Elastica", "grant",bopuser,tenant},
			{ "Investigate",  "Elastica", "revoke",bopuser,tenant},
			{ "Box",  "API", "grant",bopuser,tenant},
			{ "Box",  "API", "revoke",bopuser,tenant,},
			{ "Salesforce",  "API", "grant",bopuser,tenant},
			{ "Salesforce",  "API", "revoke",bopuser,tenant,},
			//			{ "Asana",  "API", "grant",bopuser,tenant},
			//			{ "Asana",  "API", "revoke",bopuser,tenant,},
			//			{ "ServiceNow",  "API", "grant",bopuser,tenant},
			//			{ "ServiceNow",  "API", "revoke",bopuser,tenant,},
			//			{ "NetSuite",  "API", "grant",bopuser,tenant},
			//			{ "NetSuite",  "API", "revoke",bopuser,tenant,},
			//			{ "DocuSign",  "API", "grant",bopuser,tenant},
			//			{ "DocuSign",  "API", "revoke",bopuser,tenant,},
			//			{ "Zendesk",  "API", "grant",bopuser,tenant},
			//			{ "Zendesk",  "API", "revoke",bopuser,tenant,},
			//			{ "GitHub",  "API", "grant",bopuser,tenant},
			//			{ "GitHub",  "API", "revoke",bopuser,tenant,},
			{ "Google+Apps",  "API", "grant",bopuser,tenant},
			{ "Google+Apps",  "API", "revoke",bopuser,tenant,},
			//			{ "Jive",  "API", "grant",bopuser,tenant},
			//			{ "Jive",  "API", "revoke",bopuser,tenant,},
			{ "Dropbox",  "API", "grant",bopuser,tenant},
			{ "Dropbox",  "API", "revoke",bopuser,tenant,},
			{ "Yammer",  "API", "grant",bopuser,tenant},
			{ "Yammer",  "API", "revoke",bopuser,tenant,},
			//			{ "Assembla",  "API", "grant",bopuser,tenant},
			//			{ "Assembla",  "API", "revoke",bopuser,tenant,},
			{ "Amazon+Web+Services",  "API", "grant",bopuser,tenant},
			{ "Amazon+Web+Services",  "API", "revoke",bopuser,tenant,},
			{ "Office+365",  "API", "grant",bopuser,tenant},
			{ "Office+365",  "API", "revoke",bopuser,tenant,},
		};
	}


	/*Author: Vijay Gangwar
	 *todo: revoke invenstigate request 
	 *params none
	 */
	//@Test(priority=1)
	public void revokeInvestigate() throws Exception {
		Reporter.log("exeuting revoke ****revokeInvestigate()****Test Rail:C415920",true);
		Reporter.log("====================Description==============",true);
		Reporter.log("1.user has acesss to  Investigate module",true);
		Reporter.log("1.BOP admin should revoke Investigate access from user",true);


		//Get headers
		//Map<String,String> headers = getRequestHeaders();
		String uri ="https://api-eoe.elastica-inc.com/bop/editSubscription/?";
		String QueryParams ="name=Audit&category=Elastica&action=revoke&user=vijay.gangwar%40elastica.co&priDomain=testdel.com";
		String url =uri+QueryParams;//"https://eoeapi.elastica-inc.com/bop/editSubscription/?name=Investigate&category=Elastica&action=revoke&user=vijay.gangwar%40elastica.co&priDomain=newblr.com";	
		URI dataUri=new URI(url);	
		//String Response = sentGet(uri,headers);
		HttpResponse Response = restClient.doGet(dataUri,headers);
		String responseBody = ClientUtil.getResponseBody(Response);
		//System.out.println("** revokeDetect** Response: "+Response.toString());
		Reporter.log("** revokeDetect** Response: "+Response.getStatusLine().getStatusCode(),true);
		Reporter.log(" responseBody : "+responseBody,true);
		Reporter.log(" Asserting the api response  : ",true);
		assertEquals(Response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
		Reporter.log(" API request is sucessfull : ",true);
		//String status=getJSONValue( responseBody, "action_status").toString();
		//String Stat[]=status.split("\"");
		assertEquals( getKeyResponseBody(responseBody,"action_status"),InfraConstants.BOP_SUCCESS,"App status verification failed");
		Reporter.log(" Revoke action status  is sucessfull : ",true);


	}

	String getKeyResponseBody(String json, String key) throws JsonProcessingException, IOException
	{
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		JsonNode rootNode = mapper.readTree(json);

		String status=rootNode.get(key).toString();
		String Stat[]=status.split("\"");
		return Stat[1];
	}



}
