package com.elastica.pageobjects.securlets;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.HyperLink;

/**
 * Office 365 Securlet Dashboard page objects
 * @author eldorajan
 *
 */
public class O365SecurletDashboardPage extends BasePage{

	
	public HyperLink username(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
}
