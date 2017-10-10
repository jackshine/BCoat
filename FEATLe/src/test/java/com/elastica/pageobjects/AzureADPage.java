package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class AzureADPage extends BasePage {
	public TextBox emailId(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox usernameBusiness(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox passwordBusiness(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button signinButtonBusiness(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element  microsoftAccount(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element grantAccessIframe(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox signinbutton(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public Element grantaccessbutton(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public String grantaccessiframe(WebDriver driver){
		return getLocator();
	}
	
	public HyperLink adTitle(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink profileName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink profileSingout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Element existingAccount(WebDriver driver){
		return new Element(driver, getLocator());
	}

}
