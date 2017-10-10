package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class SettingsPage extends BasePage{
	
	public HyperLink singleSignOnSideMenu(WebDriver driver) {
		return new HyperLink(driver, getLocator());
	}
	
	public Button ssoProviderButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}

	public ElementList<Element> ssoProviderDropdownLinks(WebDriver driver) {
		return new ElementList<Element>(driver, getLocator());
	}
	
	public Button idpUploadFileButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}
	
	public Button idpConfigureButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}
	
	public Button idpCancelButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}
	
	public Button idpConfiguredButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}
	
	public Button idpRemoveButton(WebDriver driver) {
		return new Button(driver, getLocator());
	}
	
	public Element ssoProvderDropdownActive(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public TextBox accessToken(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox baseDomain(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox uploadMetaFileButton(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	
	
}