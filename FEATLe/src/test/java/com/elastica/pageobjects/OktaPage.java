package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

public class OktaPage extends BasePage {
	
	public TextBox username(WebDriver driver){
		return new TextBox(driver,getLocator());
	}

	public TextBox password(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button signIn(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element appName(WebDriver driver){
		return new Element(driver,getLocator());
	}
		
	public HyperLink apiSubmenu(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink createTokenButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
		
	public TextBox tokenNameTexbox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
			
	public TextBox tokenValue(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button okgotitButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink logout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button createtokensubmit(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element deleteTokenLink(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element deleteButton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element adminactivebutton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element admindeactivebutton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
	public Element admindeactivedialogbutton(WebDriver driver){
		return new Element(driver,getLocator());
	}
	
}