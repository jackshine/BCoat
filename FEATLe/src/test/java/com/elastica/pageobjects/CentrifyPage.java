package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class CentrifyPage extends BasePage {
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink usernextbutton(WebDriver driver){
		return new HyperLink(driver, getLocator()); 
	}
	
	public HyperLink passwordnextbutton(WebDriver driver){
		return new HyperLink(driver, getLocator()); 
	}
	
}
