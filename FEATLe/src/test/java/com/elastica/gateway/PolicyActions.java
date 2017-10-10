package com.elastica.gateway;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;

public class PolicyActions {

	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	
	public void createEnableEncryptionDecryptionPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("______________________________________________________________________",true);
		Reporter.log("Creating a new policy", true);
		Reporter.log("______________________________________________________________________",true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		PolicyAccessEnforcement.fileTransferPolicyCreateEnableEncryptionDecryption(suiteData, requestHeader, policyDataMap);
	}
	
	//@Depricate
	public void deleteEncryptionDecryptionPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader ) throws Exception{
		Reporter.log("______________________________________________________________________",true);
		Reporter.log("Deleting a policy if already exist", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		Reporter.log("Policy Delete in Progress...", true);
		PolicyAccessEnforcement.deletePolicyIfAlreadyExist(suiteData, requestHeader, policyDataMap);
		Reporter.log("______________________________________________________________________",true);
		//Reporter.log("Policy Deleted sucessfully", true);
		Reporter.log("______________________________________________________________________",true);
	}
	
	public void deletePolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader ) throws Exception{
		Logger.info("Going to check if policy exist with name: "+policyName);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		PolicyAccessEnforcement.deletePolicyIfAlreadyExist(suiteData, requestHeader, policyDataMap);
	}
	
	public void deactivateEncryptionDecryptionPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader ) throws Exception{
		Reporter.log("Create and enable a file encryption/decryption policy", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		//policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		PolicyAccessEnforcement.disablePolicy(suiteData, requestHeader, policyDataMap);
		Reporter.log("Policy creation in Progress...", true);
		//PolicyAccessEnforcement.fileTransferPolicyCreateEnableEncryptionDecryption(suiteData, requestHeader, policyDataMap);
		Reporter.log("Policy created sucessfully", true);
	}
	
	public void activateEncryptionDecryptionPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader ) throws Exception{
		Reporter.log("Create and enable a file encryption/decryption policy", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		Reporter.log("Policy Active in Progress...", true);
		PolicyAccessEnforcement.enablePolicy(suiteData, requestHeader, policyDataMap);
		Reporter.log("Policy Active sucessfully", true);
	}
	public void activateEncryptionDecryptionCIQPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader,String contentIq,String transferType) throws Exception{
		Reporter.log("Create and enable a file encryption/decryption policy", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		Reporter.log("Policy Active in Progress...", true);
		PolicyAccessEnforcement.fileTransferPolicyCreateEnableEncryptionDecryptionCIQ(suiteData, requestHeader, policyDataMap);
	//	PolicyAccessEnforcement.enablePolicy(suiteData, requestHeader, policyDataMap);
		Reporter.log("Policy Active sucessfully", true);
	}
	
	public void createCIQPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, "CIQ_FE_GW_DontDelete");
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, "upload");
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
	}
	
	public void createCIQPolicy(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader,String contentIq,String transferType) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
	}
	public void createCIQPolicyWhitelist(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader,String contentIq,String transferType,String whitelist) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		policyDataMap.put(GatewayTestConstants.CIQ_WHITELIST, whitelist);
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnableWhitelist(suiteData, requestHeader, policyDataMap);
	}
	
	public void createCIQPolicyBlock(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader,String contentIq,String transferType) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, contentIq);
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, transferType);
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnableBlock(suiteData, requestHeader, policyDataMap);//fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
	}
	
	public void createCIQPolicyDownload(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, "CIQ_FE_GW_DontDelete");
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, "download");
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
	}
	
	public void createCIQPolicy2(String policyName, SuiteData suiteData, List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		policyDataMap.put(GatewayTestConstants.CONTENT_IQ, "GW_CIQ_DictionaryProfile,GW_CIQ_RiskTypeProfile");
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, "upload");
		
		
		PolicyAccessEnforcement.fileTransferPolicyWithRiskContentIqCreateEnable(suiteData, requestHeader, policyDataMap);
	}
	
	
	public void createAndEnableFileTransferPolicyUpload(String policyName,  SuiteData suiteData,  List<NameValuePair> requestHeader) throws Exception{
		Reporter.log("Validate User Upload the file/folder to folder with Policy Enabled", true);
		policyDataMap.clear();
		policyDataMap.put(GatewayTestConstants.POLICY_NAME, policyName);
		policyDataMap.put(GatewayTestConstants.APPLICATIONS, suiteData.getSaasAppName());
		policyDataMap.put(GatewayTestConstants.TARGET_USER, suiteData.getTestUsername());
		policyDataMap.put(GatewayTestConstants.NOTIFY_EMAILID, suiteData.getUsername());
		policyDataMap.put(GatewayTestConstants.TRANSFER_TYPE, "upload");
		policyDataMap.put(GatewayTestConstants.FILE_TYPE, "__ALL_EL__");
		policyDataMap.put(GatewayTestConstants.FILE_NAME, "ANY");
		
		PolicyAccessEnforcement.fileTransferPolicyCreateEnable(suiteData, requestHeader, policyDataMap);
		
		

	}
	
}
