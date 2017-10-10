package com.elastica.action.settings;

import java.util.List;

import org.openqa.selenium.WebDriver;

import com.elastica.action.Action;
import com.elastica.action.backend.BEAction.SSOName;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.SettingsPage;
import com.elastica.webelements.Element;

public class SettingsAction extends Action{

	public void clickSingleSignSidemenu(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.singleSignOnSideMenu(driver).waitForLoading(driver);
		sp.singleSignOnSideMenu(driver).click();
		hardWait(5);
	}
	
	public void clickSSOProviderDropdownButton(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.ssoProviderButton(driver).click();
		hardWait(5);
	}
	
	public void selectDropdownValueFromSSOProviderDropdown(WebDriver driver, String ssoName) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		
		try {
			List<Element> ssoProviderDropdownLinks  = sp.ssoProviderDropdownLinks(driver).getChildElements();		
			for(int i=0;i<ssoProviderDropdownLinks.size();i++){
				ssoProviderDropdownLinks  = sp.ssoProviderDropdownLinks(driver).getChildElements();
				if(ssoProviderDropdownLinks.get(i).getText().trim().contains(ssoName)){
					ssoProviderDropdownLinks.get(i).mouseOverClick(driver);
					hardWait(5);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		
	}
	
	public void clickIDPUploadFileButton(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.idpUploadFileButton(driver).click();
		hardWait(2);
	}
	
	public void clickIDPConfigureButton(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.idpConfigureButton(driver).click();
		hardWait(10);
	}
	
	public void clickIDPConfiguredButton(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.idpConfiguredButton(driver).click();
		hardWait(5);
	}
	
	public String getIDPConfiguredButtonText(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		return sp.idpConfiguredButton(driver).getText().trim();
	}
	
	public void clickIDPRemoveButton(WebDriver driver) throws Exception{
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.idpRemoveButton(driver).mouseOverClick(driver);
		hardWait(5);
	}
	
	public void uploadFile(WebDriver driver, SuiteData suiteData, String filePath) throws Exception {
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		sp.idpUploadFileButton(driver).type(filePath);
		Logger.info("The File Upload Path " + filePath +  " SSO" + SSOName.valueOf(suiteData.getSaasAppName()));
	}
	
	public void sendTokenAndBaseUrl(WebDriver driver, SuiteData suiteData) throws Exception {
		SettingsPage sp =  AdvancedPageFactory.getPageObject(driver, SettingsPage.class);
		Logger.info("The SSO Token " + suiteData.getSaasAppToken());
		sp.accessToken(driver).type(suiteData.getSaasAppToken());
		String value = suiteData.getSaasAppBaseDomain().replace("-admin", "");
		Logger.info(" The  Base Domain Value " + value);
		sp.baseDomain(driver).type(value);
	}

}
