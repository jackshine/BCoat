package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class PingOnePage extends BasePage {
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element application(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public HyperLink downloadlink(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public Element adminusername(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element adminpassword(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element adminsubmitbutton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	

}
