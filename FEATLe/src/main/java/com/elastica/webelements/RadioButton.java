package com.elastica.webelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * RadioButton Element
 * @author Eldo Rajan
 *
 */
public class RadioButton extends Element {

	public RadioButton(WebElement element) {
		super(element);
	}

	public RadioButton(WebDriver driver, By by) {
		super(driver, by);
	}
	
	public RadioButton(WebDriver driver, String locator) {
		super(driver, locator);
	}
	
	
	
}