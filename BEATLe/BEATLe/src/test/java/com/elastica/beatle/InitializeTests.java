 /**
 * 
 */
package com.elastica.beatle;

import java.util.Map;
import org.apache.commons.lang3.StringEscapeUtils;
import org.testng.ITestContext;
import com.elastica.beatle.constants.FrameworkConstants;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

/**
 * @author anuvrath
 *
 */
public abstract class InitializeTests {
	
	private Map<String, String> frameworkConfigurations;
	private Map<String, String> credentialsDBconfig;
	/**
	 * @param suiteConfigurations
	 * @throws Exception 
	 */
	public void initSuiteConfigurations(ITestContext suiteConfigurations, TestSuiteDTO suiteData) throws Exception {
		/*
		 * Initializing the suite mandatory fields
		 */
		Logger.info("Initializing suite mandatory fields");
		Logger.info("======================================================================");
		suiteData.setEnvironmentName(suiteConfigurations.getCurrentXmlTest().getParameter("environmentName"));
		suiteData.setGwHostname(suiteConfigurations.getCurrentXmlTest().getParameter("gwHostname"));
		suiteData.setTenantName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantName"));		
		suiteData.setCloudSocUname(suiteConfigurations.getCurrentXmlTest().getParameter("cloudSocUName"));
		suiteData.setBaseVersion(suiteConfigurations.getCurrentXmlTest().getParameter("apiBaseVersion"));		
		suiteData.setSaasApp(suiteConfigurations.getCurrentXmlTest().getParameter("saasApp"));
		suiteData.setLoggingOn(Boolean.valueOf(suiteConfigurations.getCurrentXmlTest().getParameter("isLoggingOn")));				
		suiteData.setDomainName(suiteConfigurations.getCurrentXmlTest().getParameter("domainName"));
		suiteData.setSaasAppGroupMailId(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppGroupMailId"));
		suiteData.setSocUserName(suiteConfigurations.getCurrentXmlTest().getParameter("socUserName"));
		suiteData.setInternal(Boolean.valueOf(suiteConfigurations.getCurrentXmlTest().getParameter("isInternal")));
		suiteData.setCustDomain(suiteConfigurations.getCurrentXmlTest().getParameter("custDomain"));
		
		
		// Looks like redundant one. 
		suiteData.setTenantDomainName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantDomainName"));
		/*
		 * Need to centralize these still
		 */
		suiteData.setSaasAppLoginHost(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppLoginHost"));
	
		
		Logger.info("");
		Logger.info("======================================================================");
		Logger.info("Initializing Environment mandatory params");
		Logger.info("======================================================================");
		try{
			frameworkConfigurations = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_CONFIGURATION_FILEPATH);
			if(suiteData.getEnvironmentName()!= null || !suiteData.getEnvironmentName().isEmpty()){
				suiteData.setReferer(frameworkConfigurations.get(suiteData.getEnvironmentName().concat(".referer")));
				suiteData.setLoginURL(frameworkConfigurations.get(suiteData.getEnvironmentName().concat(".loginURL")));
				suiteData.setScheme(frameworkConfigurations.get(suiteData.getEnvironmentName().concat(".scheme")));
				suiteData.setHost(frameworkConfigurations.get(suiteData.getEnvironmentName().concat(".hostname")));
				suiteData.setApiserverHostName(frameworkConfigurations.get(suiteData.getEnvironmentName().concat(".apiServerhostname")));
				String port = frameworkConfigurations.get(suiteData.getEnvironmentName().concat("port"));
				if(port != null && !port.isEmpty())
					suiteData.setPort(Integer.parseInt(port));
			}
		}catch(Exception e){
			Logger.error("Failed to initialize framework parameters. Aborting the build");
		}
		suiteData.setSpanvaUpdatedVersion(suiteConfigurations.getCurrentXmlTest().getParameter("spanvaUpgradedVersion"));
		suiteData.setFirewallSet(suiteConfigurations.getCurrentXmlTest().getParameter("firewallset"));
		suiteData.setDpoUsername(suiteConfigurations.getCurrentXmlTest().getParameter("dpoUserName"));
		suiteData.setDpoPassword(suiteConfigurations.getCurrentXmlTest().getParameter("dpoUserPassword"));
		suiteData.setSpanvaIp(suiteConfigurations.getCurrentXmlTest().getParameter("spanvaip"));
		suiteData.setLoadApparraziServicesCheck(suiteConfigurations.getCurrentXmlTest().getParameter("loadApparraziServicesCheck"));
		
		//Audit tenant acl checks for customerseperation tickets.
		suiteData.setUser2Name(suiteConfigurations.getCurrentXmlTest().getParameter("userName2"));
		suiteData.setUser2Password(suiteConfigurations.getCurrentXmlTest().getParameter("userPassword2"));
		suiteData.setUser2TenantName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantName2"));
		suiteData.setUser2TenantToken(suiteConfigurations.getCurrentXmlTest().getParameter("tenantToken2"));
		
		
		suiteData.setAdminUser(suiteConfigurations.getCurrentXmlTest().getParameter("adminUser"));
		suiteData.setAdminUserPwd(suiteConfigurations.getCurrentXmlTest().getParameter("adminUserPwd"));
		suiteData.setAdminUserTen(suiteConfigurations.getCurrentXmlTest().getParameter("adminUserTen"));
		suiteData.setAdminUserTenToken(suiteConfigurations.getCurrentXmlTest().getParameter("adminUserTenantToken"));
		suiteData.setDpoUserTen(suiteConfigurations.getCurrentXmlTest().getParameter("dpoUserTen"));
		suiteData.setTenant2Aclcheck(suiteConfigurations.getCurrentXmlTest().getParameter("tenant2Aclcheck"));
		
	
		Logger.info("");
		Logger.info("======================================================================");
		Logger.info("Initializing Credential related params. ");
		credentialsDBconfig = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_CREDENTIALDB_FILEPATH);
		
		if(suiteData.getCloudSocUname()!= null && !suiteData.getCloudSocUname().isEmpty()){
			
			suiteData.setUsername(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat(suiteData.getEnvironmentName()).concat(".").concat(suiteData.getCloudSocUname()).concat(".").concat("userName")));
			suiteData.setPassword(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat(suiteData.getEnvironmentName()).concat(".").concat(suiteData.getCloudSocUname()).concat(".").concat("userPassword"))));
			suiteData.setTenantToken(credentialsDBconfig.get(suiteData.getTenantName().toLowerCase().concat(".").concat("tenantToken")));
			
			if(suiteData.getSaasApp() != null){
				suiteData.setSaasAppUsername(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppUsername")));
				suiteData.setSaasAppPassword(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppPassword"))));
				suiteData.setSaasAppUserRole(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppUserRole")));
				
				suiteData.setSaasAppToken(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppToken")));		
				suiteData.setSaasAppClientId(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppClientId")));
				suiteData.setSaasAppClientSecret(credentialsDBconfig.get(suiteData.getSaasApp().toLowerCase().concat(".").concat(suiteData.getTenantName().toLowerCase()).concat(".").concat("saasAppClientSecret")));
				
			}
			
			suiteData.setSaasAppEndUser1Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser1Name"));
			if(suiteData.getSaasAppEndUser1Name()!= null){
				String[] saasAppEndUser1NameDetails =  suiteData.getSaasAppEndUser1Name().split("@|\\.");	 
				suiteData.setSaasAppEndUser1Password(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(saasAppEndUser1NameDetails[1]+saasAppEndUser1NameDetails[2].concat(".").concat(saasAppEndUser1NameDetails[0]).concat(".").concat("userPassword"))));
			}
			
			suiteData.setSaasAppEndUser2Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser2Name"));
			if(suiteData.getSaasAppEndUser2Name()!= null){
				String[] saasAppEndUser2NameDetails =  suiteData.getSaasAppEndUser2Name().split("@|\\.");
				suiteData.setSaasAppEndUser2Password(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(saasAppEndUser2NameDetails[1]+saasAppEndUser2NameDetails[2].concat(".").concat(saasAppEndUser2NameDetails[0]).concat(".").concat("userPassword"))));
			}
			
			suiteData.setSaasAppExternalUser1Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser1Name"));
			if(suiteData.getSaasAppExternalUser1Name()!= null){
				String[] saasAppExternalUser1NameDetails =  suiteData.getSaasAppExternalUser1Name().split("@|\\.");
				suiteData.setSaasAppExternalUser1Password(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(saasAppExternalUser1NameDetails[1]+saasAppExternalUser1NameDetails[2].concat(".").concat(saasAppExternalUser1NameDetails[0]).concat(".").concat("userPassword"))));
			}
			
			suiteData.setSaasAppExternalUser2Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser2Name"));
			if(suiteData.getSaasAppExternalUser2Name()!= null){
				String[] saasAppExternalUser2NameDetails =  suiteData.getSaasAppExternalUser2Name().split("@|\\.");
				suiteData.setSaasAppExternalUser2Password(StringEscapeUtils.unescapeXml(credentialsDBconfig.get(saasAppExternalUser2NameDetails[1]+saasAppExternalUser2NameDetails[2].concat(".").concat(saasAppExternalUser2NameDetails[0]).concat(".").concat("userPassword"))));
			}		
			
			suiteData.setSaasAppExternalUserName(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUserName"));
			if(suiteData.getSaasAppExternalUserName()!= null){
				suiteData.setSaasAppExternalUserToken(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUserToken"));
			}
			
			
		}else{
			/*
			 * All this is for backward compitability. After couple of weeks this will be removed. 
			 */
			suiteData.setSaasAppExternalUser2Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser2Name"));
			suiteData.setSaasAppEndUser2Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser2Name"));
			suiteData.setSaasAppExternalUser1Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser1Name"));
			suiteData.setSaasAppEndUser1Name(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser1Name"));
			suiteData.setSaasAppExternalUserName(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUserName"));
			suiteData.setSaasAppExternalUserToken(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUserToken"));
			
			if(suiteData.getUsername() == null || suiteData.getUsername().isEmpty()){
				suiteData.setUsername(suiteConfigurations.getCurrentXmlTest().getParameter("userName"));
			}
			if(suiteData.getPassword() == null || suiteData.getPassword().isEmpty() ){
				suiteData.setPassword(StringEscapeUtils.unescapeXml(suiteConfigurations.getCurrentXmlTest().getParameter("userPassword")));
			}
			if( suiteData.getTenantToken() == null || suiteData.getTenantToken().isEmpty()){
				suiteData.setTenantToken(suiteConfigurations.getCurrentXmlTest().getParameter("tenantToken"));
			}
			if(suiteData.getSaasAppUsername() == null || suiteData.getSaasAppUsername().isEmpty() ){
				suiteData.setSaasAppUsername(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppUsername"));
			}
			if(suiteData.getSaasAppPassword() == null || suiteData.getSaasAppPassword().isEmpty()){
				suiteData.setSaasAppPassword(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppPassword"));
			}
			if( suiteData.getSaasAppUserRole() == null || suiteData.getSaasAppUserRole().isEmpty()){
				suiteData.setSaasAppUserRole(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppUserRole"));
			}
			if(suiteData.getSaasAppToken() == null || suiteData.getSaasAppToken().isEmpty()){
				suiteData.setSaasAppToken(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppToken"));
			}
			if(suiteData.getSaasAppClientId() == null || suiteData.getSaasAppClientId().isEmpty()){
				suiteData.setSaasAppClientId(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppClientId"));
			}
			if(suiteData.getSaasAppClientSecret() == null || suiteData.getSaasAppClientSecret().isEmpty()){
				suiteData.setSaasAppClientSecret(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppClientSecret"));
			}
			if(suiteData.getSaasAppEndUser1Password() == null || suiteData.getSaasAppEndUser1Password().isEmpty()){
				suiteData.setSaasAppEndUser1Password(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser1Password"));
			}
			if(suiteData.getSaasAppEndUser2Password()== null || suiteData.getSaasAppEndUser2Password().isEmpty()){
				suiteData.setSaasAppEndUser2Password(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppEndUser2Password"));
			}						
			if(suiteData.getSaasAppExternalUser1Password()== null || suiteData.getSaasAppExternalUser1Password().isEmpty()){
				suiteData.setSaasAppExternalUser1Password(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser1Password"));
			}		
			if(suiteData.getSaasAppExternalUser2Password()== null || suiteData.getSaasAppExternalUser2Password().isEmpty()){
				suiteData.setSaasAppExternalUser2Password(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser2Password"));
			}			
			if(suiteData.getSaasAppExternalUser() == null || suiteData.getSaasAppExternalUser().isEmpty()){
				suiteData.setSaasAppExternalUser(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppExternalUser"));
			}	
		}
		
			
	}
}
