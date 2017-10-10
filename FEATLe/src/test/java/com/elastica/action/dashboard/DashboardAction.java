package com.elastica.action.dashboard;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.dashboard.DashboardPage;
import com.elastica.pageobjects.LoginPage;
import com.elastica.pageobjects.SettingsPage;
import com.elastica.webelements.Element;

/**
 * Sample google search action class
 * @author Eldo Rajan
 *
 */
public class DashboardAction extends Action{


	public void clickSetting(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		try {
			hardWait(5);
			dp.profileName(driver).waitForLoading(driver);
			dp.profileName(driver).click();
			dp.setting(driver).waitForLoading(driver);
			dp.setting(driver).click();
			hardWait(5);
			Assert.assertTrue(sp.singleSignOnSideMenu(driver).isElementVisible(), "Setting Page: Sign sign on side menu is not visible");
		}catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickLogout(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		LoginPage lp =  AdvancedPageFactory.getPageObject(driver, LoginPage.class);

		try {
			if(dp.profileName(driver).isElementPresent(driver)){
				if(dp.profileName(driver).isElementVisible()){
					dp.profileName(driver).click();
					hardWait(5);
					dp.logout(driver).click();
					hardWait(5);
					Assert.assertTrue(lp.signInLabel(driver).isElementVisible(), "Login Page: Sign In Label is not visible");
				}
			} 
		}catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String getHeader(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		dp.header(driver).waitForLoading(driver);
		Logger.info("Value of get Header " +  dp.header(driver).getText());
		return dp.header(driver).getText();
	}
	
	public void navigateToSecurletDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appSecurlets/index.html#/SECURLET_NAME?deeplink=exposed_files";
		
		if(suiteData.getSaasAppName().equalsIgnoreCase("Box")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "box");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Dropbox")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "dropbox");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Google Apps")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "googleapps");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Office 365")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "office365");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Salesforce")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "salesforce");
		}else{
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "box");
		}
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToInvestigateDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appForensics/index.html#/";
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToAuditDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appAudit/index.html#/?deeplink=summary";
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToDetectDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appThreats/index.html#/?deeplink=users";
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToProtectDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appControls/index.html#/?deeplink=Policies";
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToStoreDashboard(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appStore/index.html#/";
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigateToSecurletDashboardTabs(WebDriver driver, SuiteData suiteData,
			String tabType){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String expectedUrl = suiteData.getBaseUrl()+"/static/ng/appSecurlets/index.html#/SECURLET_NAME?deeplink=TAB_TYPE";
		
		if(suiteData.getSaasAppName().equalsIgnoreCase("Box")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "box");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Dropbox")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "dropbox");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Google Apps")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "googleapps");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Office 365")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "office365");
		}else if(suiteData.getSaasAppName().equalsIgnoreCase("Salesforce")){
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "salesforce");
		}else{
			expectedUrl = expectedUrl.replace("SECURLET_NAME", "box");
		}
		
		if(tabType.equalsIgnoreCase("Exposed Files")){
			expectedUrl = expectedUrl.replace("TAB_TYPE", "exposed_files");
		}else if(tabType.equalsIgnoreCase("Exposed Users")){
			expectedUrl = expectedUrl.replace("TAB_TYPE", "exposed_users");
		}else if(tabType.equalsIgnoreCase("Other Risks")){
			expectedUrl = expectedUrl.replace("TAB_TYPE", "other_risks");
		}else if(tabType.equalsIgnoreCase("Activities")){
			expectedUrl = expectedUrl.replace("TAB_TYPE", "activities");
		}else{
			expectedUrl = expectedUrl.replace("TAB_TYPE", "exposed_files");
		}
		navigationAppPage(driver, expectedUrl);
			
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void navigationAppPage(WebDriver driver, String expectedUrl) {
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		
		driver.get(expectedUrl);hardWait(20);refresh(driver, 20);
		String actualUrl=getCurrentUrl(driver);
		Logger.info("Expected url:"+expectedUrl);
		Logger.info("Actual url:"  +actualUrl);
		
		if(actualUrl.contains(expectedUrl)){
			if (dp.loadingIcon(driver).isElementPresent(driver)){
				dp.loadingIcon(driver).waitForElementToDisappear(driver);
			}
			Logger.info("Navigation successfully completed");
		}else{
			Logger.info("Retry #1 is in progress");
			driver.get(expectedUrl);hardWait(20);refresh(driver, 20);
			actualUrl=getCurrentUrl(driver);
			Logger.info("Retry #1 Expected url:"+expectedUrl);
			Logger.info("Retry #1 Actual url:"  +actualUrl);
			
			if(actualUrl.contains(expectedUrl)){
				if (dp.loadingIcon(driver).isElementPresent(driver)){
					dp.loadingIcon(driver).waitForElementToDisappear(driver);
				}
				Logger.info("Retry #1  Navigation successfully completed");
			}else{
				Logger.info("Retry #1  Navigation failed");
			}
		}
	}
	
	public int clickOnSidebarLinks(WebDriver driver, String type){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarLinks  = dp.sidebarLinks(driver).getChildElements();		
			for(int i=0;i<sidebarLinks.size();i++){
				sidebarLinks  = dp.sidebarLinks(driver).getChildElements();
				if(sidebarLinks.get(i).getInnerHtml().trim().contains(type)){
					sidebarLinks.get(i).click();
					dp.loadingIcon(driver).waitForElementToDisappear(driver);
					refresh(driver, 20);
					count=i+1;
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public int hoverOnSidebarLinks(WebDriver driver, String type){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarLinks  = dp.sidebarLinks(driver).getChildElements();
			for(int i=0;i<sidebarLinks.size();i++){
				sidebarLinks  = dp.sidebarLinks(driver).getChildElements();
				String text=sidebarLinks.get(i).getInnerHtml();
				Logger.debug(text);
				if(text.contains(type)){
					Logger.info("Hovering over "+text);
					sidebarLinks.get(i).mouseOver(driver);
					hardWait(1);
					count=i+1;
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public int clickOnSidebarSubMenuLinks(WebDriver driver, String type, int aCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarSubMenuLinks  = dp.sidebarSubMenuLinks(driver,aCount).getChildElements();		
			for(int i=0;i<sidebarSubMenuLinks.size();i++){
				sidebarSubMenuLinks  = dp.sidebarSubMenuLinks(driver,aCount).getChildElements();
				String text = sidebarSubMenuLinks.get(i).getText();
				Logger.debug(text);
				if(text.contains(type)){
					count=i+2;
					sidebarSubMenuLinks.get(i).click();
					dp.loadingIcon(driver).waitForElementToDisappear(driver);
					refresh(driver, 20);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public int hoverOnSidebarSubMenuLinks(WebDriver driver, String type, int aCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarSubMenuLinks  = dp.sidebarSubMenuLinks(driver,aCount).getChildElements();		
			for(int i=0;i<sidebarSubMenuLinks.size();i++){
				sidebarSubMenuLinks  = dp.sidebarSubMenuLinks(driver,aCount).getChildElements();
				if(sidebarSubMenuLinks.get(i).getText().contains(type)){
					count=i+2;
					sidebarSubMenuLinks.get(i).mouseOver(driver);
					hardWait(1);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public int clickOnSidebarDeepMenuLinks(WebDriver driver, String type, int aCount, int bCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarDeepMenuLinks  = dp.sidebarDeepMenuLinks(driver,aCount,bCount).getChildElements();		
			for(int i=0;i<sidebarDeepMenuLinks.size();i++){
				//Logger.info(sidebarDeepMenuLinks.get(i).getText());
				if(sidebarDeepMenuLinks.get(i).getText().contains(type)){
					count=i+1;
					sidebarDeepMenuLinks.get(i).click();
					dp.loadingIcon(driver).waitForElementToDisappear(driver);
					refresh(driver, 20);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public int hoverOnSidebarDeepMenuLinks(WebDriver driver, String type, int aCount, int bCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		int count=0;
		try {
			List<Element> sidebarDeepMenuLinks  = dp.sidebarDeepMenuLinks(driver,aCount,bCount).getChildElements();		
			for(int i=0;i<sidebarDeepMenuLinks.size();i++){
				if(sidebarDeepMenuLinks.get(i).getText().contains(type)){
					count=i+1;
					sidebarDeepMenuLinks.get(i).mouseOver(driver);
					hardWait(1);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return count;
	}

	public void hoverOnSidebarLink(WebDriver driver, int count){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		try {
			dp.sidebarLink(driver, count).mouseOver(driver);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void hoverOnSidebarSubMenuLink(WebDriver driver, int count, int appCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		try {
			dp.sidebarSubMenuLink(driver, count, appCount).mouseOver(driver);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickOnSidebarDeepMenuLink(WebDriver driver, int count, int appCount, int typeCount){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		try {
			dp.sidebarDeepMenuLink(driver, count, appCount, typeCount).click();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public boolean arrayComparison(String[] arrayA,String[] arrayB){
		return Arrays.asList(arrayA).containsAll(Arrays.asList(arrayB));
	}
	
	public boolean arrayComparison(Integer[] arrayA,Integer[] arrayB){
		return Arrays.asList(arrayA).containsAll(Arrays.asList(arrayB));
	}
	
	public void goToInvestigatePage(WebDriver driver, SuiteData suiteData) {
		driver.get(suiteData.getReferer()+ "/static/ng/appForensics/index.html#/");	
	}
	
	public int getDashboardWidgetCount(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		DashboardPage dp =  AdvancedPageFactory.getPageObject(driver, DashboardPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return dp.dashboardWidgets(driver).getChildElements().size();
	}
	
}


