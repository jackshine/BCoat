package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.logger.Logger;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.TextBox;

public class GooglePage extends BasePage{
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver, getLocator());
	}

	public Button nextbutton(WebDriver driver){
		return new Button(driver, getLocator());
	}
	
	public TextBox password(WebDriver driver){
		return new TextBox(driver, getLocator());
	}
	
	public Button signin(WebDriver driver){
		return new Button(driver, getLocator());
	}

	public Element adminUser(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminAddButton(WebDriver driver) {
		return new Element(driver,getLocator());
	}
	
	public TextBox adminCreateFirstName(WebDriver driver){
		return new TextBox(driver, getLocator());
	}
	
	public TextBox adminCreateLastName(WebDriver driver){
		return new TextBox(driver, getLocator());
	}
	
	public Element adminCreateemail(WebDriver driver){
		return new Element(driver, getLocator());
	}
	
	public Element adminCreateButton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminEditsupportmessage(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminEditByName(WebDriver driver, String name) {
		return new Element(driver, getLocator().replaceAll("NAME", name));
	}
	
	public Element adminSaveSettingButtton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String adminCommunicationPreferenceCheckbox(WebDriver driver, String name) {
		return getLocator().replaceAll("NAME", name);
	}
	
	public Element adminresetpassword(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminresetbutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminuserselect(WebDriver driver, String userName) {
		return new Element(driver, getLocator().replaceAll("USER", userName));
	}
	
	public Element admininputbyname(WebDriver driver, String name) {
		return new Element(driver, getLocator().replaceAll("NAME", name));
	}
	
	public Element adminusermore(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public Element adminusersubmenu(WebDriver driver, String menu) {
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	}
	
	public Element adminrenamebutton(WebDriver driver) {
		return new Element(driver, getLocator());
	}
	
	public String adminuserchangelogostr(WebDriver driver, String name) {
		return getLocator().replaceAll("NAME", name);
	}
	
	public Element adminuserchangelogo(WebDriver driver, String name) {
		return new Element(driver, getLocator().replaceAll("NAME", name));
	}
	
	public Element adminAppSelectMenu(WebDriver driver,String menu ) {
		return new Element(driver, getLocator().replaceAll("MENU", menu));
	}
	
	public String adminGdriveSharingOn(WebDriver driver) {
		return getLocator();
	}
	
	public String adminGdriveSharingOff(WebDriver driver) {
		return getLocator();
	}
	
	public String adminGdriveLinkSharingOff(WebDriver driver) {
		return getLocator();
	}
	
	public String adminGdriveLinkSharingCompanyOn(WebDriver driver) {
		return getLocator();
	}
	
	public String adminGdriveLinkSharingEveryoneOn(WebDriver driver) {
		return getLocator();
	}
	
}