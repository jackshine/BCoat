package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.TextBox;

public class OneLoginPage extends BasePage {
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button loginButton(WebDriver driver){
		return new Button(driver,getLocator());
	}

}
