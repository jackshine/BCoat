package com.elastica.beatle.gateway;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.amazonaws.http.HttpResponse;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.protect.GWProtectFunctions;
import com.elastica.beatle.protect.ProtectFunctions;
import net.sf.json.JSONArray;
import net.sf.json.util.JSONTokener;

public class PolicyAccessEnforcement {
	 
	 
	public static void fileTransferpolicy(TestSuiteDTO suiteData, List<NameValuePair> requestHeader ) {
	Client restClient = new Client();
    String filename = "file.pdf";
    String policyName = "PolicyFT_FileType";
    ProtectFunctions protectFunctions = new ProtectFunctions();
    GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
   // protectFunctions.createFileTransferPolicy(restClient, policyName, filename, requestHeader, suiteData);
    try {
		protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
	//Thread.sleep(90000);
		//protectFunctions.deActivatePolicyByName(restClient, policyName, requestHeader, suiteData);
   // protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	public static void fileTransferPolicyCreate(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createFileTransferPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void accessEnforcementPolicyCreate(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createAccessEnforcementPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void accessEnforcementPolicyCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicy(restClient, requestHeader, suiteData, policyDataMap );
	  
	    System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void policyAccessEnforcement(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicyGeneric(restClient, requestHeader, suiteData, policyDataMap );
	  
	    System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void accessEnforcementPolicyCreateEnablePlatform(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicyPlatform(restClient, requestHeader, suiteData, policyDataMap );
	  
	    System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void accessEnforcementPolicyCreateEnableBrowser(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicyBrowser(restClient, requestHeader, suiteData, policyDataMap );
	  
	    System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void accessEnforcementPolicyCreateEnableNoBlock(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicyNoBlock(restClient, requestHeader, suiteData, policyDataMap );
	  
	    System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void accessEnforcementPolicyWithRiskCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithRisk(restClient, requestHeader, suiteData, policyDataMap );
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileTransferPolicyCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicy(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileTransferPolicyWithFileSizeCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithFileSize(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileTransferPolicyWithRiskContentIqCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithRisk(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileSharingPolicyCreate(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createFileSharingPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void fileSharingPolicyCreateEnable(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileSharingPolicy(restClient, requestHeader, suiteData, policyDataMap );
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void enablePolicy(TestSuiteDTO suiteData, List<NameValuePair> requestHeader,  Map<String, String > policyDataMap  ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void disablePolicy(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap  ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
	    	protectFunctions.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deletePolicy(TestSuiteDTO suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
			protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllPolicy(TestSuiteDTO suiteData, List<NameValuePair> requestHeader) throws Exception {
		/*Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions(); 
		String policyName;
		JSONArray jArray=gwProtectFunctions.getAllPoliciesNamesList(restClient, requestHeader , suiteData);
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    	for (int i = 0; i < jArray.size(); i++) {
	    		policyName = gwProtectFunctions.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
	    		policyName = policyName.substring(1, policyName.length()-1);
				System.out.println("Name of policies: "+i+" : "+policyName);
				protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
	    	}*/
	}
	
	  
}
