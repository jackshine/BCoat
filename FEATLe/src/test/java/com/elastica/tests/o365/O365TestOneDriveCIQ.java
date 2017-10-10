package com.elastica.tests.o365;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365TestOneDriveCIQ extends GWCommonTest {
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=null;
	CiqUtils utils=new CiqUtils();
	@BeforeMethod()
	public void clearDataMap(){
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
		
	}

	@Priority(1)
	@Test(groups ={"PERSONAL","ONEDRIVE","OUTLOOK"})
	public void loginToCloudSocAppAndSetupSSO() throws Exception {
		fromTime=backend.getCurrentTime();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	@Priority(2)
	@Test(groups ={"PERSONAL","ONEDRIVE","OUTLOOK"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365ValidateLoginActivityEvent() throws Exception {
		Logger.info("Verifying the login event");
		o365Login.login(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.ACTIVITY_TYPE, "Login");
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User logged in"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_TYPE, "Session");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "https://login.microsoftonline.com/common/login");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Login Event Successfull");
	}
	
	@Priority(3)
	@Test(groups ={"MAIL_CIQ", "REACH"})  //,dependsOnMethods = { "loginToCloudSocAppAndSetupSSO" }
	public void o365EmailAttachedWithFile() throws Exception {
		Logger.info("Email Attached via One Drive");
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "CIQ_FE_GW_DontDelete","CIQ_FE_GW_DontDelete",CIQConstants.SOURCE_DCI,CIQConstants.DEFAUTRISKVALUE);
		utils.createCIQProfile( CIQConstants.ZERO,suiteData, "GW_CIQ_DictionaryProfile","GW_CIQ_DictionaryProfile",CIQConstants.SOURCE_RISK,CIQConstants.RISKVALUE);

		fromTime=backend.getCurrentTime();
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.createCIQPolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData),"GW_CIQ_DictionaryProfile","upload");

		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadEmailApp(getWebDriver());
		// upload risk files
		String [] ciqfiles={"pci.txt",  "pii.txt", "glba.txt", "hipaa.txt", "vba_macro.xls","Illegal_Drugs.txt","Gambling.txt","FERPA_BaileyDoxed.txt","pii_risks.rtf"}; //source_code.xls
		for(int i=0; i<ciqfiles.length;i++){
			try{

				String filepath=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
						"resources"+File.separator+"ciq"+File.separator + ciqfiles[i];
				//o365HomeAction.loadEmailApp(getWebDriver());
				o365HomeAction.sendEmailWithAttach(getWebDriver(), ciqfiles[i]);
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}

			catch(Exception e) {

				Reporter.log("error in uploading  the file "+ciqfiles[i], true);
			}
		}
		String [] contentfiles={"divorce.txt","US_License_Plate_Number.txt","Java.txt","hdfc.txt","us_social_security_number.txt","design.pdf"};
		String	absLocation=System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
				"resources"+File.separator+"ciq"+File.separator+"contentType"+File.separator;// + ciqfiles[i]

		for(int i=0; i<contentfiles.length;i++){
			try{
				o365HomeAction.loadEmailApp(getWebDriver());
				o365HomeAction.sendEmailWithAttachAsCopy(getWebDriver(), contentfiles[i]);
				Thread.sleep(5000);
				o365HomeAction.refresh(getWebDriver(), 5);
			}
			catch(Exception e) {

				Reporter.log("error in uploading  the file "+contentfiles[i], true);
			}
		}
		Thread.sleep(150000);
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+getSaasAppUserName(), suiteData, backend.getHeaders(suiteData));
		policy.deletePolicy(GatewayTestConstants.CIQ_UPLOAD_POLICY_NAME+"DictionaryProfile", suiteData, backend.getHeaders(suiteData));

		//		Logger.info("Email Attached via One Drive Successful");
	}
	public String getSaasAppUserName(){
		return suiteData.getSaasAppUsername().replaceAll("@", "_");
	}
	
}
