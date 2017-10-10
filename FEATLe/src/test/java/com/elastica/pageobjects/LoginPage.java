package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

/**
 * Cisco page
 * @author Eldo Rajan
 *
 */
public class LoginPage extends BasePage{

	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element signInLabel(WebDriver driver){
		return new Element(driver, getLocator()); 
	}
	
	public HyperLink singleSignOnLink(WebDriver driver){
		return new HyperLink(driver, getLocator()); 
	}
	
	public HyperLink partnerLoginLink(WebDriver driver){
		return new HyperLink(driver, getLocator()); 
	}
	
	public Element selectTenantlabel(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element dropDownBoxSelectTenant(WebDriver driver, String tenant) {
		return new Element(driver, getLocator().replaceAll("TENANT", tenant));
	} 
	
}
