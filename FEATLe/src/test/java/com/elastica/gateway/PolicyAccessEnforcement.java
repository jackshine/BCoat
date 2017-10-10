package com.elastica.gateway;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import com.elastica.common.SuiteData;
import com.elastica.restClient.Client;
import com.elastica.restClient.ClientUtil;

import net.sf.json.JSONArray;

public class PolicyAccessEnforcement {
	 
	 
	/*public static void fileTransferpolicy(SuiteData suiteData, List<NameValuePair> requestHeader ) {
	Client restClient = new Client();
    String filename = "file.pdf";
    String policyName = "PolicyFT_FileType";
    ProtectFunctions protectFunctions = new ProtectFunctions();
    GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
   // protectFunctions.createFileTransferPolicy(restClient, policyName, filename, requestHeader, suiteData);
    try {
    	gwProtectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
	//Thread.sleep(90000);
		//protectFunctions.deActivatePolicyByName(restClient, policyName, requestHeader, suiteData);
   // protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}*/
	
	
	public static void fileTransferPolicyCreate(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createFileTransferPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void accessEnforcementPolicyCreate(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createAccessEnforcementPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void accessEnforcementPolicyCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	public static void policyAccessEnforcement(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    org.apache.http.HttpResponse httpr=gwProtectFunctions.createAccessEnforcementPolicyGeneric(restClient, requestHeader, suiteData, policyDataMap );
	  
	    //System.out.println(ClientUtil.getResponseBody(httpr));
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void accessEnforcementPolicyCreateEnablePlatform(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	public static void accessEnforcementPolicyCreateEnableBrowser(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	public static void accessEnforcementPolicyCreateEnableNoBlock(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	
	public static void accessEnforcementPolicyWithRiskCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	public static void fileTransferPolicyCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    HttpResponse response=  gwProtectFunctions.createFileTransferPolicy(restClient,requestHeader, suiteData, policyDataMap);
	    String responseBody=ClientUtil.getResponseBody(response);
	    System.out.println(responseBody);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileTransferPolicyCreateEnableEncryptionDecryption(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithContentEncryptionDecryption(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fileTransferPolicyCreateEnableEncryptionDecryptionCIQ(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    String action="[\"SEVERITY_LEVEL\",\"ENCRYPT\",\"BLOCK_ON_ENCRYPT_FAILURE\"]";
	    gwProtectFunctions.createFileTransferPolicyWithContentEncryptionDecryption(restClient,requestHeader, suiteData, policyDataMap,action);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void fileTransferPolicyWithFileSizeCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	public static void fileTransferPolicyWithRiskContentIqCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	public static void fileTransferPolicyWithRiskContentIqCreateEnableWhitelist(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithRiskWhitelist(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fileTransferPolicyWithRiskContentIqCreateEnableBlock(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions();
		ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    gwProtectFunctions.createFileTransferPolicyWithRiskBlock(restClient,requestHeader, suiteData, policyDataMap);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void fileSharingPolicyCreate(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap) {
		Client restClient = new Client();
		GWProtectFunctions protectFunctions = new GWProtectFunctions();
	    protectFunctions.createFileSharingPolicy(restClient, requestHeader, suiteData, policyDataMap );
	}
	
	public static void fileSharingPolicyCreateEnable(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
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
	
	public static void enablePolicy(SuiteData suiteData, List<NameValuePair> requestHeader,  Map<String, String > policyDataMap  ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
			protectFunctions.activatePolicyByName(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void disablePolicy(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap  ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
	    	protectFunctions.deActivatePolicy(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deletePolicy(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
			protectFunctions.deleteExistingPolicy(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deletePolicyIfAlreadyExist(SuiteData suiteData, List<NameValuePair> requestHeader, Map<String, String > policyDataMap ) {
		Client restClient = new Client();
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    String policyName=policyDataMap.get(GatewayTestConstants.POLICY_NAME);
	    try {
			protectFunctions.deletePolicyIfAlreadyExist(restClient, policyName, requestHeader, suiteData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteAllPolicy(SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception {
		Client restClient = new Client();
		GWProtectFunctions gwProtectFunctions = new GWProtectFunctions(); 
		String policyName;
		JSONArray jArray=gwProtectFunctions.getAllPoliciesNamesList(restClient, requestHeader , suiteData);
	    ProtectFunctions protectFunctions = new ProtectFunctions();
	    	for (int i = 0; i < jArray.size(); i++) {
	    		policyName = gwProtectFunctions.getJSONValue(jArray.getJSONObject(i).toString(), "policy_name");
	    		policyName = policyName.substring(1, policyName.length()-1);
				System.out.println("Name of policies: "+i+" : "+policyName);
				protectFunctions.deletePolicy(restClient, policyName, requestHeader, suiteData);
	    	}
	}
	
	  
}
