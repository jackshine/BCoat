package com.elastica.tests.sso;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.elastica.action.backend.BEAction.SSOName;
import com.elastica.common.CommonTest;
import com.elastica.constants.sso.SSOConstants;
import com.elastica.logger.Logger;
public class SSOTests extends CommonTest{
	/**
	 * Configure Elastica in OneLogin portal
	 * Downloading SAML File using API 
	 * @throws Exception 
	 */
	@Test(groups ={"regression"})
	public void testSSOLevel1() throws Exception {
		SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
		switch(identifier) {
			case OneLogin: {
				String oneLoginUrl = suiteData.getSaasAppBaseUrl() + suiteData.getSaasAppMetaData();
				String samlResponse = backend.getSAMLFile(client, null, oneLoginUrl);
				saml.createSAMLFile(samlResponse);
				break;
			}
			case Okta:{
				okta.loginPortal(getWebDriver(), suiteData);
				okta.deactiveAppIfFound(getWebDriver(), suiteData);
				okta.addApplication(getWebDriver(), suiteData);
				okta.signOut(getWebDriver(), suiteData);
				break;
			}
			case Centrify:{
				
				break;
			}
			case AzureAD:{
				//Authentication done through CloudSoc
				break;
			}
			case PingOne:{
				poa.loginPortal(getWebDriver(), suiteData);
				poa.getSamlDataUrl(getWebDriver(), suiteData);
				Thread.sleep(5000);
				break;
			}
			case CASite:{
				break;
			}
			case Bitium:{
				break;
			}
			default:{
				Assert.fail("No configured SSO present");
				break;
			}
		}
		Logger.info("Level 1 for " + identifier + " done");
	}

	
	/**
	 * Uploading One-Login SAML file into CloudSoc using API
	 */
	@Test(groups ={"regression"}, dependsOnMethods = {"testSSOLevel1"})
	public void testSSOLevel2() throws Exception {
		SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
		
		login.login(getWebDriver(), suiteData);
		
		switch(identifier){
			case OneLogin:{
				backend.configureSAMLMetadataFile(client, suiteData, getWebDriver());
				dashboard.clickSetting(getWebDriver());
				settings.clickSingleSignSidemenu(getWebDriver());
				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				break;
			}
			case Okta:{
				dashboard.clickSetting(getWebDriver());
				settings.clickSingleSignSidemenu(getWebDriver());
				settings.clickSSOProviderDropdownButton(getWebDriver());
				settings.selectDropdownValueFromSSOProviderDropdown(getWebDriver(), suiteData.getSaasAppName());
				settings.sendTokenAndBaseUrl(getWebDriver(), suiteData);
				settings.clickIDPConfigureButton(getWebDriver());
				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				break;
			}
			case Centrify:{
				dashboard.clickSetting(getWebDriver());
				settings.clickSingleSignSidemenu(getWebDriver());
				settings.clickSSOProviderDropdownButton(getWebDriver());
				settings.selectDropdownValueFromSSOProviderDropdown(getWebDriver(), suiteData.getSaasAppName());
				settings.clickIDPConfigureButton(getWebDriver());
				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				break;
			}
			case AzureAD:{
				dashboard.clickSetting(getWebDriver());
				settings.clickSingleSignSidemenu(getWebDriver());
				settings.clickSSOProviderDropdownButton(getWebDriver());
				settings.selectDropdownValueFromSSOProviderDropdown(getWebDriver(), suiteData.getSaasAppName());
				//aza.grantAccess(getWebDriver(), suiteData);
//				dashboard.clickSetting(getWebDriver());
//				settings.clickSingleSignSidemenu(getWebDriver());
//				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				settings.uploadFile(getWebDriver(), suiteData, SSOConstants.azureAdMetaFile);
				settings.clickIDPConfigureButton(getWebDriver());
				
				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				Thread.sleep(10000);
				break;
			}
			case PingOne:{
				dashboard.clickSetting(getWebDriver());
				settings.clickSingleSignSidemenu(getWebDriver());
				settings.clickSSOProviderDropdownButton(getWebDriver());
				settings.selectDropdownValueFromSSOProviderDropdown(getWebDriver(), suiteData.getSaasAppName());
				String fileName=SSOConstants.pingOneLoginMetaFile;
				settings.uploadFile(getWebDriver(), suiteData, fileName);
				settings.clickIDPConfigureButton(getWebDriver());
				
				Assert.assertEquals(settings.getIDPConfiguredButtonText(getWebDriver()), suiteData.getSaasAppName());
				break;
			}
			case CASite:{
				break;
			}
			case Bitium:{
				break;
			}
			default:{
				Assert.fail("No configured SSO present");
				break;
			}
		}
		Logger.info("Level 2 for " + identifier + " done");
	}
	
	/**
	 * Verify SSO: 
	 * Checking without one-login already logged-in and entering User Name and Password
	 * Checking with one-login already logged-in and verify the Dash board is rendered
	 * @throws Exception
	 */
	@Test(groups ={"regression"}, dependsOnMethods = {"testSSOLevel2"})
	public void testSSOLevel3() throws Exception {
		SSOName identifier = SSOName.valueOf(suiteData.getSaasAppName());
		dashboard.clickLogout(getWebDriver());
		Assert.assertEquals(login.getSignInLabelText(getWebDriver()), "Sign In");
		login.loginViaSSO(getWebDriver(), suiteData);	
		Assert.assertEquals(dashboard.getHeader(getWebDriver()), "Dashboard", "Login failed using SSO");
	
		dashboard.clickLogout(getWebDriver());
		login.loggedInViaSSO(getWebDriver(), suiteData);
		Assert.assertEquals(dashboard.getHeader(getWebDriver()), "Dashboard");
		Logger.info("Level 3 for " + identifier + " done");
	}
	
	@BeforeClass(groups ={"regression"})
	public void doBeforeClass() throws Exception {
		backend.deleteMetaDataFile(suiteData);
		backend.deleteSAMLFile(client, suiteData);
		
	}
	
	@AfterClass(groups ={"regression"})
	public void doAfterClass() throws Exception {
		backend.deleteSAMLFile(client, suiteData);
	}
}