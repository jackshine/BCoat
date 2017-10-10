package com.elastica.action.onelogin;

import org.openqa.selenium.WebDriver;
import com.elastica.action.Action;
import com.elastica.common.SuiteData;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.OneLoginPage;

public class OneLoginAction extends Action{

	public void login(WebDriver driver, SuiteData suiteData) throws InterruptedException{
		OneLoginPage ollp =  AdvancedPageFactory.getPageObject(driver, OneLoginPage.class);
		ollp.username(driver).clear();ollp.username(driver).type(suiteData.getSaasAppUsername());
		ollp.password(driver).clear();ollp.password(driver).type(suiteData.getSaasAppPassword());
		ollp.loginButton(driver).click();
		hardWait(5);
	}
	

}
