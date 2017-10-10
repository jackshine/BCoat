package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

/**
 * Cisco page
 * @author Eldo Rajan
 *
 */
public class CiscoPage extends BasePage{

	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink auditRedirectLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
}
