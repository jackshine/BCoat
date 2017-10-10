package com.elastica.tests.store;

import java.lang.reflect.Method;
import org.openqa.selenium.Cookie;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.action.universalapi.SaasType;
import com.elastica.common.CommonTest;
import com.elastica.constants.store.StoreConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

/**
 * Store Test Suite
 * @author Eldo Rajan
 *
 */
public class StoreTests extends CommonTest{

	SoftAssert sAssert = null;


	@Priority(1)
	@Test(groups = { "regression" })
	public void testSecurletsInStorePage() throws Exception {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for store page and verify securlet options are appearing");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify securlets are appearing");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");
		sAssert.assertEquals(store.verifySecurletSectionInStore(getWebDriver(), suiteData),"");



		sAssert.assertAll();
	}

	@Priority(2)
	@Test(groups = { "regression" })
	public void testSecurletsDeactivationDialogBoxInStorePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for deactivation with purge option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify deactivation dialog box with purge option");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());
			sAssert.assertEquals(store.getSecurletStoreHeaderDetailedView(getWebDriver()),
					"Securlet™ for "+suiteData.getSaasAppName(),
					"Securlet header in detailed view mismatch is seen");

			if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
					SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
					SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
				sAssert.assertEquals(store.getSecurletConfigureButtonText(getWebDriver()),
						StoreConstants.Configure,"Securlet deactivation button text mismatch is seen");
			}

			store.clickSecurletConfigureButton(getWebDriver(), suiteData);

			sAssert.assertEquals(store.getSecurletDeactivateButtonText(getWebDriver(), suiteData),
					StoreConstants.Deactivate,"Securlet deactivation button text mismatch is seen");
			sAssert.assertEquals(store.getSecurletPurgeDataText(getWebDriver(), suiteData),
					StoreConstants.PurgeData,"Securlet purge button text mismatch is seen");


			store.clickSecurletPurgeDataButton(getWebDriver(), suiteData);
			store.clickSecurletDeactivateButton(getWebDriver(), suiteData);

			sAssert.assertEquals(store.verifySecurletDeactivateDialogBox(getWebDriver()
					,suiteData),
					"","Securlet deactivate dialog mismatch is seen");


		}else{
			Logger.info(suiteData.getSaasAppName()+" is not in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is not in activated state");
		}

		sAssert.assertAll();
	}

	@Priority(3)
	@Test(groups = { "regression","smoke" })
	public void testSecurletsDeactivationInStorePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for deactivation with purge option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify deactivation of securlet with purge option");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());
			sAssert.assertEquals(store.getSecurletStoreHeaderDetailedView(getWebDriver()),
					"Securlet™ for "+suiteData.getSaasAppName(),
					"Securlet header in detailed view mismatch is seen");

			store.clickSecurletConfigureButton(getWebDriver(), suiteData);
			sAssert.assertEquals(store.getSecurletDeactivateButtonText(getWebDriver(), suiteData),
					StoreConstants.Deactivate,"Securlet deactivation button text mismatch is seen");
			sAssert.assertEquals(store.getSecurletPurgeDataText(getWebDriver(), suiteData),
					StoreConstants.PurgeData,"Securlet purge button text mismatch is seen");

			store.clickSecurletPurgeDataButton(getWebDriver(), suiteData);
			store.clickSecurletDeactivateButton(getWebDriver(), suiteData);
			store.clickDeactivateDialogBoxRemoveButton(getWebDriver());

			sAssert.assertEquals(store.verifySecurletDeactivateAlertBox(getWebDriver()
					,suiteData),
					"","Securlet alert dialog mismatch is seen");

			boolean removedFlag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
			sAssert.assertEquals(removedFlag,
					false,"Securlet deactivation is not successful");
		}else{
			Logger.info(suiteData.getSaasAppName()+" is not in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is not in activated state");
		}


		sAssert.assertAll();
	}

	@Priority(4)
	@Test(groups = { "regression" })
	public void testSecurletsActivationOptionsInStorePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for activation options from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify activation of securlet with options");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = !store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());

			/*sAssert.assertEquals(store.getSecurletActivateButtonText(getWebDriver()),
					StoreConstants.Activate,"Securlet activation button text mismatch is seen");*/
			store.clickSecurletActivateButton(getWebDriver());

			sAssert.assertEquals(store.verifySecurletActivationContainerBox(getWebDriver(), suiteData),
					"","Securlet activation container mismatch is seen");
		}else{
			Logger.info(suiteData.getSaasAppName()+" is already in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is already in activated state");
		}


		sAssert.assertAll();
	}

	@Priority(5)
	@Test(groups = { "regression","smoke" })
	public void testSecurletsActivationWithFullScanOptionInStorePage() throws Exception {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for activation with full scan option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify activation of securlet with options");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		try{
			dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
			sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
					"Store header mismatch is seen");

			boolean flag = !store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
			if(flag){
				store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());
				store.clickSecurletActivateButton(getWebDriver());
				if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
					Logger.info("No full/partial scan option to be clicked");
				}else{
					store.clickSecurletScanContainerFullScanOption(getWebDriver());
					store.clickSecurletScanContainerActivateButton(getWebDriver());
				}

				store.saasAppAuthorization(getWebDriver(),suiteData);

				dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
				boolean addedFlag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
				sAssert.assertEquals(addedFlag,
						true,"Securlet activation is not successful");
			}else{
				Logger.info(suiteData.getSaasAppName()+" is already in activated state");
				sAssert.fail(suiteData.getSaasAppName()+" is already in activated state");
			}

		}finally{
			Object[] winHandles = store.getWindowHandles(getWebDriver()).toArray();
			for(int i=0;i<winHandles.length-1;i++){
				store.switchToWindow(getWebDriver(), (String)winHandles[i]);
				getWebDriver().close();
			}
			store.switchToWindow(getWebDriver(), (String)winHandles[winHandles.length-1]);
		}

		sAssert.assertAll();
	}

	@Priority(6)
	@Test(groups = { "regression" })
	public void testSecurletsViewScanPoliciesFromSecurletDashboard() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for activation with full scan option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify activation of securlet with options");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			dashboard.refresh(getWebDriver(),30);
			int appCount = dashboard.hoverOnSidebarLinks(getWebDriver(), StoreConstants.Securlet);
			dashboard.clickOnSidebarSubMenuLinks(getWebDriver(), suiteData.getSaasAppName(), appCount);
			sAssert.assertEquals(securlet.getSecurletHeader(getWebDriver()),"Securlet for\n"+suiteData.getSaasAppName(),
					"Securlet header mismatch is seen");
			sAssert.assertEquals(securlet.getActiveTabText(getWebDriver()),StoreConstants.ExposedFiles,
					"Active tab header text mismatch is seen");

			securlet.clickOptionsSecurletDashboard(getWebDriver());
			securlet.clickViewScanPoliciesSecurletDashboard(getWebDriver());

			String actualText = securlet.getViewScanPoliciesText(getWebDriver());

			String expectedText=""; 
			if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)){
				expectedText="Account Name:\n"+suiteData.getTenantDomainName()+"\n"+StoreConstants.FullScan;
			}else{
				expectedText=StoreConstants.FullScan;
			}
			Logger.info("Actual Text:"+actualText);
			Logger.info("Expected Text:"+expectedText);

			sAssert.assertEquals(actualText,expectedText,
					"View Scan policy header text mismatch is seen");
			securlet.clickCloseScanPoliciesSecurletDashboard(getWebDriver());
		}else{
			Logger.info(suiteData.getSaasAppName()+" is already in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is already in activated state");
		}


		sAssert.assertAll();
	}

	@Priority(7)
	@Test(groups = { "regression" })
	public void testSecurletsDeactivationWithoutPurgeInStorePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for deactivation without purge option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify deactivation of securlet without purge option");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());
			sAssert.assertEquals(store.getSecurletStoreHeaderDetailedView(getWebDriver()),
					"Securlet™ for "+suiteData.getSaasAppName(),
					"Securlet header in detailed view mismatch is seen");

			store.clickSecurletConfigureButton(getWebDriver(), suiteData);
			store.clickSecurletDeactivateButton(getWebDriver(), suiteData);
			store.clickDeactivateDialogBoxRemoveButton(getWebDriver());

			sAssert.assertEquals(store.verifySecurletDeactivateAlertBox(getWebDriver()
					,suiteData),
					"","Securlet alert dialog mismatch is seen");

			boolean removedFlag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
			sAssert.assertEquals(removedFlag,
					false,"Securlet deactivation is not successful");
		}else{
			Logger.info(suiteData.getSaasAppName()+" is not in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is not in activated state");
		}


		sAssert.assertAll();
	}

	@Priority(8)
	@Test(groups = { "regression" })
	public void testSecurletsActivationWithSelectiveScanOptionInStorePage() throws Exception {
		addCookie();
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Check for activation with selective scan option from store page");
		Logger.info("1. Login into cloudsoc home page");
		Logger.info("2. Go to store page and verify activation of securlet with selective scan option");
		Logger.info("***********************************");

		login.login(getWebDriver(), suiteData);
		dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
		sAssert.assertEquals(store.getStoreHeader(getWebDriver()),StoreConstants.Store,
				"Store header mismatch is seen");

		boolean flag = !store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
		if(flag){
			store.clickSaasAppLink(getWebDriver(), suiteData.getSaasAppName());
			store.clickSecurletActivateButton(getWebDriver());

			if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
				Logger.info("No full/partial scan option to be clicked");
			}else{
				store.clickSecurletScanContainerSelectiveScanOption(getWebDriver());
				store.clickSecurletScanContainerActivateButton(getWebDriver());
			}

			store.saasAppAuthorization(getWebDriver(),suiteData);

			securlet.enterOptionsSecurletScanPolicyPopup(getWebDriver(),suiteData);

			dashboard.clickOnSidebarLinks(getWebDriver(), StoreConstants.Store);
			boolean addedFlag = store.getSaasAppActivatedOrNot(getWebDriver(),suiteData);
			sAssert.assertEquals(addedFlag,
					true,"Securlet activation is not successful");
		}else{
			Logger.info(suiteData.getSaasAppName()+" is already in activated state");
			sAssert.fail(suiteData.getSaasAppName()+" is already in activated state");
		}


		sAssert.assertAll();
	}

	/*********************************************************************/
	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}

	public void addCookie() {
		getWebDriver().manage().deleteAllCookies();
		getWebDriver().manage().addCookie(new Cookie("mf_authenticated",suiteData.getAccessToken()));
	}

	/*********************************************************************/

}
