package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.HyperLink;

/**
 * Securlet O365 page
 * @author Eldo Rajan
 *
 */
public class SecurletO365Page extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
}
