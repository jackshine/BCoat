package com.elastica.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.elastica.webelements.BasePage;

/**
 * Page factory for webelements
 * @author Eldo Rajan
 *
 */
public class AdvancedPageFactory {
	
	/**
	 * 
	 * @param driver
	 * @param klass
	 * @return
	 */
	public static <T extends BasePage> T getPageObject(WebDriver driver,Class<T> klass) {
		return PageFactory.initElements(driver, klass);
	}
}

