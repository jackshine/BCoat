package com.elastica.beatle.htmlunit;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.beatle.htmlunit.Element;

/**
 * Element List
 * @author Eldo Rajan
 *
 * @param <T>
 */
public class ElementList <T extends Element>{

	/**
	 * Enum for locator types
	 * @author Eldo Rajan
	 *
	 */
	public enum LocatorType {
		CSS, XPATH, ID, NAME, CLASS, DOM, LINK, LINKP
	}
	
	protected List<WebElement> elementList;
	
	public ElementList(List<WebElement> elementList){
		this.elementList = elementList;
	}
	
	public ElementList(WebDriver driver,By by){
		this.elementList = findElements(driver, by);
	}
	
	public ElementList(WebDriver driver, String locator) {
		this.elementList = findElements(driver, getBy(locator));
	}

	@SuppressWarnings("unchecked")
	public List<T> getChildElements() {
		List<T> ElementList = new ArrayList<T>();
		
		for (WebElement anElement : elementList){
			try {
				ElementList.add((T) new Element(anElement));
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return ElementList;
	}

	public List<WebElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<WebElement> elementList) {
		this.elementList = elementList;
	}
	
	public List<WebElement> findElements(WebDriver driver,By by) {
		try {
			return driver.findElements(by);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("can't findElements " + by + " in WebElement[" + toString() + "]", e);
		} catch (StaleElementReferenceException e) {
			throw new StaleElementReferenceException("WebElement[" + toString() + "] has been removed (unable to find " + by + ")", e);
		}		
	}

	public By getBy(String locator) {
		LocatorType identifier = LocatorType.valueOf(locator.toUpperCase().substring(0, locator.indexOf("=")));
		locator = locator.substring(locator.indexOf("=")+1);

		By locatorIdentifiedBy=null;
		switch (identifier) {
		case XPATH:
			locatorIdentifiedBy = By.xpath(locator);
			break;
		case CSS:
			locatorIdentifiedBy = By.cssSelector(locator);
			break;
		case ID:
			locatorIdentifiedBy = By.id(locator);
			break;
		case NAME:
			locatorIdentifiedBy = By.name(locator);
			break;
		case CLASS:
			locatorIdentifiedBy = By.className(locator);
			break;
		case LINK:
			locatorIdentifiedBy = By.linkText(locator);
			break;
		case LINKP:
			locatorIdentifiedBy = By.partialLinkText(locator);
			break;
		default:
			locatorIdentifiedBy = By.cssSelector(locator);
			break;
		}
		return locatorIdentifiedBy;
	}
}
