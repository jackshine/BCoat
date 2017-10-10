package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class SalesforceLoginPage extends BasePage {
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button personalDeviceButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink continueWithoutInstallLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button userProfileButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button logoutButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink verifyCode(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink verifyButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

}
