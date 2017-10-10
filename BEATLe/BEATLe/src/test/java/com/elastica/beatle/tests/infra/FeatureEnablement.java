package com.elastica.beatle.tests.infra;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.RestClient.ClientUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.elastica.beatle.tests.infra.BoxSeleniumTest;
import com.universal.constants.CommonConstants;
public class FeatureEnablement extends CommonTest {
	private final static CloseableHttpClient client = HttpClientBuilder.create().build();
	List<NameValuePair> headers;
	String tenantDB;
	InfraUtils InfraUtils;
	String BOPAdmin;
	String BOPpwd;
	 @BeforeClass(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		
		headers =getHeaders();
		 InfraUtils= new InfraUtils();
		 
		//InfraUtils.AppSubscriptionStatus(Responsebody, userlist, field, name)
		BOPAdmin=getRegressionSpecificSuitParameters("bopUser");
		BOPpwd=getRegressionSpecificSuitParameters("bopPassword");
		
	}
	 	/*Author: Vijay Gangwar
		*todo: retrive new tenant app information
		*params: dataProvider
		*/
		
		@Test ( priority = 1,dataProvider="AppData",groups = { "CEP","EOE" } )
		public void AppStatusNewTenant(String test,String id,String module,String caseType) throws Exception {
			Reporter.log("exeuting testcase ****AppStatusNewTenant() ***** Assembl id:"+id,true);
			Reporter.log(" View   :*** "+test,true);
			Reporter.log(" Description : 1 Elastica app should be disabled on new tenant",true);
			Reporter.log(" 2.check for Elastica app status on new tenant :",true);
			//prepare http request
			String uri =suiteData.getReferer()+InfraConstants.SUBSCRIPTION;//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
			Reporter.log("search user uri ."+uri,true);
			URI dataUri1 = ClientUtil.BuidURI(uri);
			Reporter.log("Send the get request for app subscription "+uri,true);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			//
			assertEquals( response1.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			String responseBody1 = ClientUtil.getResponseBody(response1);
			System.out.println(" ****responseBody ***** ..."+responseBody1);
			//Reporter.log("search user responseBody: ."+responseBody1,true);
			String Activeapp =InfraUtils.AppSubscriptionStatus(responseBody1,"subscriptions",InfraConstants.APP_NAME,module) ;
			if((null == Activeapp)||(Activeapp.contains("REVOKED")))
			{
				Activeapp=null;
				Reporter.log("Expected result from the search :null  ",true);
				Reporter.log("Actual result from the search :  "+Activeapp,true);
				assertEquals( null,Activeapp, "Response code verification failed");
				Reporter.log("Test case execution is completed  ",true);
			}
			else
			{
				Reporter.log("Access request is alreay send for ",true);
				Reporter.log("Test case execution is completed  ",true);
			}
			
			
		}
		
		@DataProvider
		public Object[][] AppData() {
			return new Object[][]{
				// Action    		id	   		App name         testcasetype
				{ " Gateway apps status","C23351",  "ALL", "normal"},
				{ "Securlet apps status","C23351",  "API", "normal"},
				{ "Detect apps status","C29741",  "Detect", "normal"},
				{ "Protect apps status","C23355",  "Protect", "normal"},
				{ "Investigate apps status","TBM",  "Investigate", "normal"},
				{ "Audit apps status","C23365",  "Audit", "normal"}
				
			};
		}

		   /*Author: Vijay Gangwar
			*todo: enable  new app information
			*params: dataProvider
			*this Testcase should be executed once only for a tenant
			*/

		
		@Test ( priority = 1,dataProvider="enableApp", groups = { "CEP","EOE" } )
		public void SendEnableRequest(String test,String id,String module,String payload,String caseType) throws Exception {
			Reporter.log("exeuting testcase sendEnableRequest() , Assembl id:"+id,true);
			Reporter.log(" SaaS App  :*** "+test,true);
			Reporter.log(" Description : 1 Gateway app should be disabled on new app. Send the enable Request. ",true);
			// check for available apps
			String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
			URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);
			String responseBody1 = ClientUtil.getResponseBody(response1);
			String Id=null;
			
			Id=InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			System.out.println("e ****responseBody ***** ..."+responseBody1);
			//Check if request is already pending or App is active.
			// check for gateway subscription gateway_subscription_id
			String gateway_subscription_ID= InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			boolean is_api_subscribed =InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscribed");
			boolean subscription = InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscription_requested");
			if((is_api_subscribed)||(subscription))//||(!Id.isEmpty()))
				{
					Reporter.log("App / Module is already active or Request is pending",true);
				}
			else
				{
				String uri =suiteData.getReferer()+InfraConstants.REQUESTED_ACCESS;//"https://eoe.elastica-inc.com/admin/application/requestaccess/";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
				//Reporter.log("search user uri ."+uri,true);
				//prepare request to enable app
				URI dataUri = ClientUtil.BuidURI(uri);
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				//
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				String responseBody = ClientUtil.getResponseBody(response);
				System.out.println("e ****responseBody ***** ..."+responseBody);
				Reporter.log("search user responseBody: ."+responseBody,true);
				if(!responseBody.contains("Invalid request"))
				{
					String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
					Reporter.log(" Expected Result:  api action status:success",true);
					Reporter.log(" Actual Result: api action status:"+action_status,true);
					assertEquals(action_status,"success", "Response code verification failed");
					Reporter.log(" Test execution is completed",true);
					//validate app status after Requesting access
					String Status=getAppSubscripionsStatus(module);
					assertEquals(Status,InfraConstants.REQUESTED, "App status verification failed");
					Reporter.log(" Expected Result  app status: REQUESTED",true);
					Reporter.log(" Actual Result app  status:"+Status,true);
					Reporter.log(" Test execution is completed",true);
			
				
				}
				else
				{
					Reporter.log(" Subscription Request is alredy sent",true);
					Reporter.log(" Test execution is completed",true);
				}
			}
	}
		
		@DataProvider
		public Object[][] enableApp() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ " Gateway apps status","C23370",  "OneDrive Personal","{\"app_name\":\"ALL\",\"app_category\":\"Gateway\",\"app_id\":\"\"}", "normal"},
				{ "Securlet Box  status","C23351",  "Dropbox","{\"app_name\":\"Dropbox\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Detect apps status","C23351",  "Detect","{\"app_name\":\"Detect\",\"app_category\":\"Elastica\",\"app_id\":\"\"}", "normal"},
				{ "Protect apps status","C23351",  "Protect","{\"app_name\":\"Protect\",\"app_category\":\"Elastica\",\"app_id\":\"\"}", "normal"},
				{ "Investigate apps status","C23351",  "Investigate","{\"app_name\":\"Investigate\",\"app_category\":\"Elastica\",\"app_id\":\"\"}", "normal"},
				{ "Audit apps status","C23351",  "Audit","{\"app_name\":\"Audit\",\"app_category\":\"Elastica\",\"app_id\":\"\"}", "normal"}
				
			};
		}
		/*Author: Vijay Gangwar
		*todo: enable  securlet app
		*params: dataProvider
		*this Testcase should be executed once only for a tenant
		*/
		
		@Test ( priority = 1,dataProvider="SecureletData" , groups = { "EOE" } )
		public void enableSecurletRequest(String test,String id,String module,String payload,String caseType) throws Exception {
			Reporter.log("exeuting testcase ****enableSecurletRequest() ***** Test Rail id: "+id,true);
			Reporter.log(" Securlet SaaS App  *** "+test,true);
			Reporter.log(" Description : 1 Send enable request for Securlet  app : ",true);
			// check for available apps
			String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
			URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);
			String responseBody1 = ClientUtil.getResponseBody(response1);
			//String Id="";
			String Id=InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			if(caseType.contains("normal"))
			{
				//assertEquals(Id,null, InfraConstants.APP_NAME+"  Saas app should not be active");
			//}
			System.out.println("e ****responseBody ***** ..."+responseBody1);
			//Check if request is already pending or App is active.
			// check for gateway subscription gateway_subscription_id
			String gateway_subscription_ID= InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			boolean is_api_subscribed =InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscribed");
			boolean subscription = InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscription_requested");
			if((is_api_subscribed)||(subscription)||(!Id.isEmpty()))
				{
					Reporter.log("App / Module is already active or Request is pending",true);
				}
			else
				{
				
				String uri =suiteData.getReferer()+InfraConstants.REQUESTED_ACCESS;//"https://eoe.elastica-inc.com/admin/application/requestaccess/";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
				Reporter.log("search user uri ."+uri,true);
				URI dataUri = ClientUtil.BuidURI(uri);
				
				// prepare the enable request 
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				//
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				String responseBody = ClientUtil.getResponseBody(response);
				System.out.println(" ****responseBody ***** ..."+responseBody);
				Reporter.log("search user responseBody: ."+responseBody,true);
				// subscription should be sent only once.
				//Repeated request will give invalid request.
				if(!responseBody.contains("Invalid request"))
				{
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				Reporter.log(" Expected Result:  api action status:success",true);
				Reporter.log(" Actual Result: api action status:"+action_status,true);
				assertEquals(action_status,"success", "Response code verification failed");
				
				String Status=getAppSubscripionsStatus(module);
				assertEquals(Status,InfraConstants.REQUESTED, "App status verification failed");
				Reporter.log(" Expected Result  api action status:REQUESTED",true);
				Reporter.log(" Actual Result api action status: "+Status,true);
				Reporter.log(" Test execution is completed",true);
				}
				else
				{
					Reporter.log(" Request is alredy sent",true);
					Reporter.log(" Test execution is completed",true);
				}
			}
			}
			else
			{
				assertEquals(Id,null, InfraConstants.APP_NAME+"  Saas app should not be active");
				Reporter.log(" Expected Result  Saas app  status: null",true);
				Reporter.log(" Actual Result api action status: "+Id,true);
				Reporter.log(" Test execution is completed",true);
			}
	}
		
		@DataProvider
		public Object[][] SecureletData() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "Box  enable request","C23351",  "Box","{\"app_name\":\"Box\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Office 365 enable  request","TBM",  "Office 365","{\"app_name\":\"Office 365\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Salesforce enable request","TBM",  "Salesforce","{\"app_name\":\"Salesforce\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "yammer enable  request","TBM",  "Yammer","{\"app_name\":\"Yammer\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "AWS enable  request","TBM",  "Amazon Web Services","{\"app_name\":\"Amazon Web Services\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},			
				{ "DocuSign enable  request","TBM",  "DocuSign","{\"app_name\":\"DocuSign\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},				
				{ "Jive enable  request","TBM",  "Jive","{\"app_name\":\"Jive\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},			
				{ "ServiceNow enable  request","TBM",  "ServiceNow","{\"app_name\":\"ServiceNow\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Asana enable  request","TBM",  "Asana","{\"app_name\":\"Asana\",\"app_category\":\"API\",\"app_id\":\"\"}", "error"},
				{ "GitHub enable  request","TBM",  "GitHub","{\"app_name\":\"GitHub\",\"app_category\":\"API\",\"app_id\":\"\"}", "error"},
				{ "NetSuite enable  request","TBM",  "NetSuite","{\"app_name\":\"NetSuite\",\"app_category\":\"API\",\"app_id\":\"\"}", "error"},
				{ "Zendesk enable  request","TBM",  "Zendesk","{\"app_name\":\"Zendesk\",\"app_category\":\"API\",\"app_id\":\"\"}", "error"},
		    	{ "Assembla enable  request","TBM",  "Assembla","{\"app_name\":\"Assembla\",\"app_category\":\"API\",\"app_id\":\"\"}", "error"},
			};
		}
		
		/*Author: Vijay Gangwar
		*todo: enable  securlet app
		*params: dataProvider
		*this Testcase should be executed once only for a tenant
		*/
		
		@Test ( priority = 1,dataProvider="SecureletDataCEP" , groups = { "CEP" } )
		public void enableSecurletRequestCEP(String test,String id,String module,String payload,String caseType) throws Exception {
			Reporter.log("exeuting testcase ****enableSecurletRequest() ***** Test Rail id: "+id,true);
			Reporter.log(" Securlet SaaS App  *** "+test,true);
			Reporter.log(" Description : 1 Send enable request for Securlet  app : ",true);
			// check for available apps
			String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
			URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);
			String responseBody1 = ClientUtil.getResponseBody(response1);
			String Id="";
			Id=InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			System.out.println("e ****responseBody ***** ..."+responseBody1);
			//Check if request is already pending or App is active.
			// check for gateway subscription gateway_subscription_id
			String gateway_subscription_ID= InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			boolean is_api_subscribed =InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscribed");
			boolean subscription = InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscription_requested");
			if((is_api_subscribed)||(subscription)||(!Id.isEmpty()))
				{
					Reporter.log("App / Module is already active or Request is pending",true);
				}
			else
				{
				
				String uri =suiteData.getReferer()+InfraConstants.REQUESTED_ACCESS;//"https://eoe.elastica-inc.com/admin/application/requestaccess/";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
				Reporter.log("search user uri ."+uri,true);
				URI dataUri = ClientUtil.BuidURI(uri);
				
				// prepare the enable request 
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				//
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				String responseBody = ClientUtil.getResponseBody(response);
				System.out.println("e ****responseBody ***** ..."+responseBody);
				Reporter.log("search user responseBody: ."+responseBody,true);
				// subscription should be sent only once.
				//Repeated request will give invalid request.
				if(!responseBody.contains("Invalid request"))
				{
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
				Reporter.log(" Expected Result:  api action status:success",true);
				Reporter.log(" Actual Result: api action status:"+action_status,true);
				assertEquals(action_status,"success", "Response code verification failed");
				
				String Status=getAppSubscripionsStatus(module);
				assertEquals(Status,InfraConstants.REQUESTED, "App status verification failed");
				Reporter.log(" Expected Result  api action status:REQUESTED",true);
				Reporter.log(" Actual Result api action status: "+Status,true);
				Reporter.log(" Test execution is completed",true);
				}
				else
				{
					Reporter.log(" Request is alredy sent",true);
					Reporter.log(" Test execution is completed",true);
				}
			}
	}
		
		@DataProvider
		public Object[][] SecureletDataCEP() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "Box  enable request","C23351",  "Box","{\"app_name\":\"Box\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Office 365 enable  request","TBM",  "Office 365","{\"app_name\":\"Office 365\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				{ "Salesforce enable request","TBM",  "Salesforce","{\"app_name\":\"Salesforce\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "yammer enable  request","TBM",  "Yammer","{\"app_name\":\"Yammer\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "AWS enable  request","TBM",  "Amazon Web Services","{\"app_name\":\"Amazon Web Services\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "Asana enable  request","TBM",  "Asana","{\"app_name\":\"Asana\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//			//	{ "Assembla enable  request","TBM",  "Assembla","{\"app_name\":\"Assembla\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "DocuSign enable  request","TBM",  "DocuSign","{\"app_name\":\"DocuSign\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "GitHub enable  request","TBM",  "GitHub","{\"app_name\":\"GitHub\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "Jive enable  request","TBM",  "Jive","{\"app_name\":\"Jive\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "NetSuite enable  request","TBM",  "NetSuite","{\"app_name\":\"NetSuite\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "ServiceNow enable  request","TBM",  "ServiceNow","{\"app_name\":\"ServiceNow\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				{ "Zendesk enable  request","TBM",  "Zendesk","{\"app_name\":\"Zendesk\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
//				//{ "NetSuite enable  request","TBM",  "NetSuite","{\"app_name\":\"NetSuite\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				
			};
		}
		
		/*Author: Vijay Gangwar
		*todo: Disable  securlet app
		*params: dataProvider
		*this Testcase should be executed for new tenant
		*/
		@Test ( priority = 2,dataProvider="SecureletAppData", groups = { "EOE" }  )
		public void disableSecurletApps(String test,String id,String module,String category,String action,String user,String priDomain) throws Exception {
			Reporter.log("exeuting testcase ****disableSecurletApps() ***** Test Rail id:"+id+"  for : "+module,true);
			Reporter.log(" Disable request for Securlet SaaS App  :*** "+test,true);
			Reporter.log(" Description : 1 Securlet app is enabled. 2. disable the already enabled seculet ",true);
			// Grant permission to the appp
			priDomain=suiteData.getTenantName();
			String QueryParams ="name="+module+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		    String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
			URI dataUri=new URI(uri);	
		   // ClientUtil.BuidURI(uri);
		   // URI dataUri=ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(),"/bop/editSubscription/\"?"+QueryParams);
			//HttpResponse Response = restClient.doGet(dataUri, headers);
			//String responseBody = ClientUtil.getResponseBody(Response);
			String responseBody = InfraUtils.getdigestheader( uri,BOPAdmin,BOPpwd);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			Reporter.log(" Expected Result:  api action status:success",true);
			Reporter.log(" Actual Result: api action status:"+action_status,true);
			assertEquals(action_status,"success", "Response code verification failed");
			// validate the status after api request
			
			if(module.contains("Office+365"))
			{
				module = "Office 365";
				
			}
			else if(module.contains("Amazon+Web+Services"))
			{
				module = "Amazon Web Services";
			}
			else
			{
				
			}
			String Status=getAppSubscripionsStatus(module);
			assertEquals(Status,InfraConstants.REVOKED, "App status verification failed");
			Reporter.log(" Expected Result  app status : REVOKED",true);
			Reporter.log(" Actual Result app  status:"+Status,true);
			Reporter.log(" Test execution is completed",true);
			
			
		
	}
		
		@DataProvider
		public Object[][] SecureletAppData() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "dropBox  disable request","C23351",  "Dropbox",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Office 365 disable  request","TBM",  "Office+365",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Salesforce disable request","TBM",  "Salesforce",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "yammer disable  request","TBM",  "Yammer",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "AWS disable  request","TBM",  "Amazon+Web+Services",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Asana disable  request","TBM",  "Asana",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
			//	{ "Assembla disable  request","TBM",  "Assembla",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "DocuSign disable  request","TBM",  "DocuSign",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "GitHub disable  request","TBM",  "GitHub",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Jive disable  request","TBM",  "Jive",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "NetSuite disable  request","TBM",  "NetSuite",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "ServiceNow disable  request","TBM",  "ServiceNow",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Zendesk disable  request","TBM",  "Zendesk",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				//{ "NetSuite enable  request","TBM",  "NetSuite","{\"app_name\":\"NetSuite\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				
			};
		}
		
		/*Author: Vijay Gangwar
		*todo: Disable  securlet app
		*params: dataProvider
		*this Testcase should be executed for new tenant
		*/
		@Test ( priority = 2,dataProvider="SecureletAppDataCEP", groups = { "CEP" }  )
		public void disableSecurletAppsCEP(String test,String id,String module,String category,String action,String user,String priDomain) throws Exception {
			Reporter.log("exeuting testcase ****disableSecurletApps() ***** Test Rail id:"+id+"  for : "+module,true);
			Reporter.log(" Disable request for Securlet SaaS App  :*** "+test,true);
			Reporter.log(" Description : 1 Securlet app is enabled. 2. disable the already enabled seculet ",true);
			// Grant permission to the appp
			priDomain=suiteData.getTenantName();
			String QueryParams ="name="+module+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		    String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
			URI dataUri=new URI(uri);	
		   
			String responseBody = InfraUtils.getdigestheader( uri,BOPAdmin,BOPpwd);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			Reporter.log(" Expected Result:  api action status:success",true);
			Reporter.log(" Actual Result: api action status:"+action_status,true);
			assertEquals(action_status,"success", "Response code verification failed");
			// validate the status after api request
			
			if(module.contains("Office+365"))
			{
				module = "Office 365";
				
			}
			else if(module.contains("Amazon+Web+Services"))
			{
				module = "Amazon Web Services";
			}
			else
			{
				
			}
			String Status=getAppSubscripionsStatus(module);
			assertEquals(Status,InfraConstants.REVOKED, "App status verification failed");
			Reporter.log(" Expected Result  app status : REVOKED",true);
			Reporter.log(" Actual Result app  status:"+Status,true);
			Reporter.log(" Test execution is completed",true);
			
			
		
	}
		
		@DataProvider
		public Object[][] SecureletAppDataCEP() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "dropBox  disable request","C23351",  "Dropbox",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Office 365 disable  request","TBM",  "Office+365",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "Salesforce disable request","TBM",  "Salesforce",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "yammer disable  request","TBM",  "Yammer",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "AWS disable  request","TBM",  "Amazon+Web+Services",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "Asana disable  request","TBM",  "Asana",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//			//	{ "Assembla disable  request","TBM",  "Assembla",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "DocuSign disable  request","TBM",  "DocuSign",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "GitHub disable  request","TBM",  "GitHub",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "Jive disable  request","TBM",  "Jive",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "NetSuite disable  request","TBM",  "NetSuite",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "ServiceNow disable  request","TBM",  "ServiceNow",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				{ "Zendesk disable  request","TBM",  "Zendesk",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
//				//{ "NetSuite enable  request","TBM",  "NetSuite","{\"app_name\":\"NetSuite\",\"app_category\":\"API\",\"app_id\":\"\"}", "normal"},
				
			};
		}
		
		/*Author: Vijay Gangwar
		*todo: Enable  Elastica app
		*params: dataProvider
		*this Testcase should be executed for new tenant
		*/
		
		@Test ( priority = 1,dataProvider="EnableData", groups = { "CEP","EOE" }  )
		public void enableApp(String test,String id,String module,String category,String action,String user,String priDomain) throws Exception {
			Reporter.log("**Executing testcase enableApp , Assembl id:"+id,true);
			Reporter.log(" send Enable request for Elastica  App  :*** "+test,true);
			Reporter.log(" Description : 1 Elastica app  is blocked on new Tenant.  2. enable Elastica app on new tenant. ",true);
			// Grant permission to the appp
			priDomain=suiteData.getTenantName();
			String QueryParams ="name="+module+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		    String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
			//URI dataUri=new URI(uri);	
		   // ClientUtil.BuidURI(uri);
		   // URI dataUri=ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(),"/bop/editSubscription/\"?"+QueryParams);
			//HttpResponse Response = restClient.doGet(dataUri, headers);
			String responseBody = InfraUtils.getdigestheader( uri,BOPAdmin,BOPpwd);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			Reporter.log(" Expected Result:  api action status:success",true);
			Reporter.log(" Actual Result: api action status:"+action_status,true);
			assertEquals(action_status,"success", "Response code verification failed");
			//validate app status after enabling
			String Status=getAppSubscripionsStatus(module);
			assertEquals(Status,InfraConstants.GRANTED, "App status verification failed");
			Reporter.log(" Expected Result  app status :GRANTED",true);
			Reporter.log(" Actual Result app  status:"+Status,true);
			Reporter.log(" Test execution is completed",true);
		
			}
	
		
		@DataProvider
		public Object[][] EnableData() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "enable Protect","C23357",  "Protect",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "enable Detect","C23352",  "Detect",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "enable Investigate","TBM",  "Investigate",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "enable Audit","C23364",  "Audit",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "enable Gateway","C23370",  "ALL",InfraConstants.CATEGORY_GATEWAY,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "enable Securlet","C23385",  "Box",InfraConstants.CATEGORY_API,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				
			};
		}
		/*Author: Vijay Gangwar
		*todo: disable  Elastica app
		*params: dataProvider
		*this Testcase should be executed for new tenant
		*/
		
		@Test ( priority = 2,dataProvider="disableData" , groups = { "CEP","EOE" } )
		public void disableApp(String test,String id,String module,String category,String action,String user,String priDomain) throws Exception {
			Reporter.log("Start testcase **disableApp** , Assembl id:"+id,true);
			Reporter.log(" send Enable request for Elastica  App  :*** "+module,true);
			Reporter.log(" Description : 1 Disable an  enabled Elastica app. 2.  check for Elastica app details *** ",true);
			// Grant permission to the appp
			priDomain=suiteData.getTenantName();
			String QueryParams ="name="+module+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		    String uri =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
		//	URI dataUri=new URI(uri);
			String responseBody = InfraUtils.getdigestheader( uri,BOPAdmin,BOPpwd);
		//	Reporter.log(action+"  action is sucessfull",true);

			//HttpResponse Response = restClient.doGet(dataUri, headers);
		//	String responseBody = ClientUtil.getResponseBody(Response);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			Reporter.log(" Expected Result: api action status:success",true);
			Reporter.log(" Actual Result: api action status:"+action_status,true);
			assertEquals(action_status,"success", "Response code verification failed");
			//Reporter.log("Test case execution is completed:  ",true);
			//enable app will be available on dashboard
			//validate app status after Disabling
			String Status=getAppSubscripionsStatus(module);
			assertEquals(Status,InfraConstants.REVOKED, "App status verification failed");
			Reporter.log(" Expected Result  app status : REVOKED",true);
			Reporter.log(" Actual Result app  status:"+Status,true);
			Reporter.log(" Test execution is completed",true);
			}
	
		
		@DataProvider
		public Object[][] disableData() {
			return new Object[][]{
				// Action    		id	   		App name    Payload     testcasetype
				{ "disable Protect","C23351",  "Protect",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "disable Detect","C23351",  "Detect",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "disable Investigate","C23351",  "Investigate",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "disable Audit","C23351",  "Audit",InfraConstants.CATEGORY_ELASTICA,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "disable Gateway","C23351",  "ALL",InfraConstants.CATEGORY_GATEWAY,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				{ "disable Securlet","C23351",  "Box",InfraConstants.CATEGORY_API,InfraConstants.BOP_REVOKE,InfraConstants.BOP_ADMIN,"BOP_ADMIN"},
				
			};
		}	
		
		/*Author: Vijay Gangwar
		*todo: enable  securlet app
		*params: dataProvider
		*this Testcase should be executed once only for a tenant
		*/
		
		@Test ( priority = 1 ,groups = {"CEP", "EOE" },dataProvider="SaasAppActivationData")
		public void ActivateBoxSecurlet(String test,String id,String module,String payload,String caseType,String category,String action,String user) throws Exception {
			Reporter.log("exeuting testcase ****enableSecurletRequest() ***** Test Rail id: ",true);
			Reporter.log(" Securlet SaaS App  *** ",true);
			Reporter.log(" Description : 1 Send enable request for Securlet  app : ",true);
			// send subscription request for available apps
			Reporter.log(" Description : 1 Send enable request for Securlet  app : ",true);
			// check for available apps
			String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
			URI dataUri1 = ClientUtil.BuidURI(uri1);
			HttpResponse response1 =  restClient.doGet(dataUri1, headers);
			String responseBody1 = ClientUtil.getResponseBody(response1);
			//String Id="";
			//Id=InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			System.out.println(" ****responseBody ***** ..."+responseBody1);
			//Check if request is already pending or App is active.
			// check for gateway subscription gateway_subscription_id
			String gateway_subscription_ID= InfraUtils.getUserid(test,responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module);
			boolean is_api_subscribed =InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscribed");
			boolean subscription = InfraUtils.getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, module,"is_api_subscription_requested");
			if((is_api_subscribed)||(subscription))
				{
					Reporter.log("App / Module is already active or Request is pending",true);
				}
			else
				{
				
				String uri =suiteData.getReferer()+InfraConstants.REQUESTED_ACCESS;//"https://eoe.elastica-inc.com/admin/application/requestaccess/";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
				Reporter.log("search user uri ."+uri,true);
				URI dataUri = ClientUtil.BuidURI(uri);
				
				// prepare the enable request 
				HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
				//
				assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
				String responseBody = ClientUtil.getResponseBody(response);
				}
			// grant permission from bop.
			String	priDomain=suiteData.getTenantName();
			String QueryParams ="name="+module+"&category="+category+"&action="+action+"&user="+user+"&priDomain="+priDomain;
		    String uriBop =suiteData.getScheme()+"://"+suiteData.getApiserverHostName()+InfraConstants.BOP_EDIT_SUBSCRIPTION+QueryParams;
			URI dataUriBop=new URI(uriBop);	
		  
			Reporter.log(" Expected url"+uriBop,true);// ClientUtil.BuidURI(uri);
		   // URI dataUri=ClientUtil.BuidURI(suiteData.getScheme(),s
			//HttpResponse Response = restClient.doGet(dataUriBop, headers);
			sleep(CommonConstants.SIXTY_SECONDS_SLEEP);
			//String responseBody = ClientUtil.getResponseBody(Response);
			String responseBody = InfraUtils.getdigestheader( uriBop,BOPAdmin,BOPpwd);
			String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
			Reporter.log(" Expected Result:  api action status:success",true);
			Reporter.log(" Actual Result: api action status:"+action_status,true);
			assertEquals(action_status,"success", "Response code verification failed");
			// activate request
			
			BoxSeleniumTest.getboxActivated(suiteData.getReferer(),UIConstants.EOE_USER_ID,suiteData.getPassword(),UIConstants.BOX_USER_ID,UIConstants.BOX_PWD);
			
	}
		
		@DataProvider
		public	Object[][] SaasAppActivationData()
		{
			return new Object[][]{
				{ "Activate Box","C23357",  "Box","{\"app_name\":\"Box\",\"app_category\":\"API\",\"app_id\":\"\"}","normal",InfraConstants.CATEGORY_API,InfraConstants.BOP_GRANT,InfraConstants.BOP_ADMIN},
				
			};
		}	
		
		
		@Test ( priority = 2, groups = { "CEP","EOE" })
		public void deActivateBoxSecurlet() throws Exception {
			Reporter.log("exeuting testcase ****enableSecurletRequest() ***** Test Rail id: ",true);
			Reporter.log(" Securlet SaaS App  *** ",true);
			Reporter.log(" Description : 1 Send enable request for Securlet  app : ",true);
			// check for available apps
//			String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
//			URI dataUri1 = ClientUtil.BuidURI(uri1);
//			HttpResponse response1 =  restClient.doGet(dataUri1, headers);
//			String responseBody1 = ClientUtil.getResponseBody(response1);
//			String Id="";
			BoxSeleniumTest.removBoxActivation(suiteData.getReferer());
			
	}		
	/*	
		@Test ( priority = 1)
		public void gDriveProtectEnablement() throws Exception {
			Reporter.log("Enabling google drive will enable protect automatically, Assembl id:C23381",true);
			Reporter.log(" 1 Enable Google drive app .  check for proctect status app details *** ",true);
			// Check for subscription of  Google drive 	
			 
			String Status=getAppSubscripionsStatus("Google Drive");
			if(Status.contains(InfraConstants.GRANTED))
			{
				//Check for the protect status since gDrive is already active
			}
			else
			{
				if(Status.isEmpty())
				{
					Reporter.log(" Google drive status is "+Status,true); 
				}
				else 
				{
					Reporter.log(" Google drive status is "+Status,true); 
					// do nothing
				}
				//grant google drive access
			}
			
			//check for protect accees
			//Activate google Drive.
			String uri =suiteData.getReferer()+"/admin/user/subscription";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
			Reporter.log("search user uri ."+uri,true);
			URI dataUri = ClientUtil.BuidURI(uri);
			HttpResponse response =  restClient.doGet(dataUri, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			//
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			String responseBody = ClientUtil.getResponseBody(response);
			//System.out.println("e ****responseBody ***** ..."+responseBody1);
			//Reporter.log("search user responseBody: ."+responseBody1,true);
			String Activeapp =AppSubscriptionStatus(responseBody,"subscriptions","app_name","Google Drive") ;
			if((null == Activeapp)||(Activeapp.contains("REVOKED")))
			{
				Activeapp=null;
				System.out.println("Activeapp : "+Activeapp);
				assertEquals( null,Activeapp, "Response code verification failed");
			}
			else
			{
				Reporter.log("Access request Google Drive is alreay send for ",true);
				System.out.println("Access request Google Drive is alreay send for ");
			}
			// send enablement request
			// check for available apps
						String uri1=suiteData.getReferer()+InfraConstants.LIST_TENANTS;
						URI dataUri1 = ClientUtil.BuidURI(uri1);
						HttpResponse response1 =  restClient.doGet(dataUri1, headers);
						String responseBody1 = ClientUtil.getResponseBody(response1);
						String Id=null;
						
				//		Id=getUserid(test,responseBody1, "applications", "app_name", module);
						System.out.println("e ****responseBody ***** ..."+responseBody1);
						//Check if request is already pending or App is active.
						// check for gateway subscription gateway_subscription_id
//						String gateway_subscription_ID= getUserid(test,responseBody1, "applications", "app_name", module);
//						boolean is_api_subscribed =getSubscriptionStatus(responseBody1, "applications", "app_name", module,"is_api_subscribed");
//						boolean subscription = getSubscriptionStatus(responseBody1, "applications", "app_name", module,"is_api_subscription_requested");
//						if((is_api_subscribed)||(subscription)||(!Id.isEmpty()))
//							{
//								Reporter.log("App / Module is already active or Request is pending",true);
//							}
//						else
//							{
//							String uri =suiteData.getReferer()+InfraConstants.REQUESTED_ACCESS;//"https://eoe.elastica-inc.com/admin/application/requestaccess/";//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
//							Reporter.log("search user uri ."+uri,true);
//							URI dataUri = ClientUtil.BuidURI(uri);
//							//payload=payload.replace("55f175b8bf831218269dc84c", "Id");
//							//  /newdscom/api/admin/v1/subscriptions/55f122b49dfa514470f677f7/
//							// suiteData.getScheme()+"://"+ suiteData.getApiserverHostName()+"/"+tenantDB+InfraConstants.API_CALL_USERS+userId+"/";
//							HttpResponse response =  restClient.doPost(dataUri, headers, null, new StringEntity(payload));
//							//
//							assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
//							String responseBody = ClientUtil.getResponseBody(response);
//							System.out.println("e ****responseBody ***** ..."+responseBody);
//							Reporter.log("search user responseBody: ."+responseBody,true);
//							if(!responseBody.contains("Invalid request"))
//							{
//							String action_status =(String) new JSONObject(responseBody).get("action_status").toString();
//
//							assertEquals(action_status,"success", "Response code verification failed");
//							Reporter.log(" user filter api action status."+action_status,true);
//							}
//							else
//							{
//								Reporter.log(" Request is alredy sent",true);
//							}			
//			
			
			
		//	System.out.println(" get subscribed Requested status ...");
				
			 
				}
//		@Test ( priority = 1)
		public void viewThreatAlertDetect() throws Exception {
				
			 String to_jodaTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).toString(); 	
			 System.out.println("exeuting testcase ****check get user list ***** ..."+to_jodaTime);
			 ///abccom/api/admin/v1/tenantappparams/
			 
			 String currentTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).toString(); 	
				String uri1="https://eoe.elastica-inc.com/dashboardapi/getThreatAlertsData";//-us&modified="+currentTime;
				URI dataUri1 = ClientUtil.BuidURI(uri1);
				String payload="{\"from_date\":\""+currentTime+"\",\"to_date\":\""+currentTime+"\"}";
				HttpResponse response1 =  restClient.doPost(dataUri1, headers,null, new StringEntity(payload));
				String responseBody1 = ClientUtil.getResponseBody(response1);
				String Id=getUserid("description",responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, InfraConstants.AUDIT);
				System.out.println("get subscription id ..."+Id);
			//		 String uri ="https://eoe.elastica-inc.com/api/admin/v1/apps/?format=json";
//			    Reporter.log("search user uri ."+uri,true);
//				URI dataUri1 = ClientUtil.BuidURI(uri);
//				HttpResponse response1 =  restClient.doGet(dataUri1, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			//	String responseBody1 = ClientUtil.getResponseBody(response1);
				//System.out.println("e ****responseBody ***** ..."+responseBody1);
				System.out.println("\n get subscription id ..."+Id);
				// checck for api subscrition status
				System.out.println("\n get subscription  status ..."+getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, InfraConstants.AUDIT,"is_api_subscribed"));
				System.out.println("\n get subscribed Requested status ..."+getSubscriptionStatus(responseBody1, InfraConstants.APPLICATIONS, InfraConstants.APP_NAME, InfraConstants.AUDIT,"is_api_subscription_requested"));
				
			 
				}
		*/
	//	@Test ( priority = 1)
		public void getDetectAttribute() throws Exception {
				
			 String to_jodaTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).toString(); 	
			 System.out.println("exeuting testcase ****check get user list ***** ..."+to_jodaTime);
			 ///abccom/api/admin/v1/tenantappparams/
			 
			 String currentTime=new org.joda.time.DateTime( org.joda.time.DateTimeZone.UTC ).toString(); 	
				String uri1="https://eoe.elastica-inc.com/admin/analytics/getdetectattributes";//-us&modified="+currentTime;
				URI dataUri1 = ClientUtil.BuidURI(uri1);
				//String payload="{\"from_date\":\""+currentTime+"\",\"to_date\":\""+currentTime+"\"}";
				HttpResponse response =  restClient.doGet(dataUri1, headers);//doPost(dataUri1, headers,null, new StringEntity(payload));
				String responseBody = ClientUtil.getResponseBody(response);
				String action_status =(String) new JSONObject(responseBody).get("action_status").toString();

				assertEquals(action_status,"success", "Response code verification failed");
				Reporter.log(" user filter api action status."+action_status,true);
				
				
			 
				}
		
/**********************************************************************************************/		
		String getAppSubscripionsStatus(String module)throws Exception
		{
			String uri =suiteData.getReferer()+InfraConstants.SUBSCRIPTION;//suiteData.getReferer()+InfraConstants.SEARCH_USER+"AugDpo";
			Reporter.log("search user uri ."+uri,true);
			URI dataUri = ClientUtil.BuidURI(uri);
			Reporter.log("Send the get request for app subscription "+uri,true);
			Thread.sleep(10000);
			HttpResponse response =  restClient.doGet(dataUri, headers);//.doPost(dataUri, headers, null, new StringEntity(payload));
			//
			assertEquals( response.getStatusLine().getStatusCode(),HttpStatus.SC_OK, "Response code verification failed");
			String responseBody = ClientUtil.getResponseBody(response);
			System.out.println(" ****responseBody ***** ..."+responseBody);
			//Reporter.log("search user responseBody: ."+responseBody1,true);
			String Activeapp =InfraUtils.AppSubscriptionStatus(responseBody,"subscriptions",InfraConstants.APP_NAME,module) ;
			return Activeapp;
		}
}
