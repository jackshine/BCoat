package com.elastica.action.centrify;

import org.openqa.selenium.WebDriver;

import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.CentrifyPage;

public class CentrifyAction extends Action{

	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		CentrifyPage cp =  AdvancedPageFactory.getPageObject(driver, CentrifyPage.class);
		cp.username(driver).clear();cp.username(driver).type(suiteData.getSaasAppUsername());
		cp.usernextbutton(driver).click();
		cp.password(driver).clear();cp.password(driver).type(suiteData.getSaasAppPassword());
		cp.passwordnextbutton(driver).click();
		Thread.sleep(5000);
	}

}
