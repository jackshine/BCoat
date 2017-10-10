package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.TextBox;

public class O365LoginPage extends BasePage {
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button userProfileButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button logoutButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element loginuser(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element loginuserinactive(WebDriver driver, String email){
		return new Element(driver, getLocator().replaceAll("EMAIL", email));
	}
	
	public Element personalSignin(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalPopupemail(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalPopupnext(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalLoginEmail(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalLoginPassword(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
	public Element personalLoginSubmit(WebDriver driver) {
		return new Element(driver, getLocator());
	} 
	
}
