package com.elastica.pageobjects.sources;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;

/**
 * Audit page
 * @author Eldo Rajan
 *
 */
public class SourcesPage extends BasePage{

	public ElementList<Element> SourcesPageSourcesRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> SourcesPageGateletsSourcesRowName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowNameLink(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowRecord(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowRecordCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowUser(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowUserCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowViolations(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowViolationsCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> SourcesPageGateletsSourcesRowservicesCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink SourcesLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageGateletsBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageSecurletsBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageDevicelogsBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SourcesPageGateletsHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageSecurletsHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageDevicelogsHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SourcesPageGateletsSources(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageSecurletsSources(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageDevicelogsSources(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SourcesPageGateletsRecords(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageSecurletsRecords(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageDevicelogsRecords(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SourcesPageGateletsSourcesCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink SourcesPageSecurletsSourcesCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink SourcesPageDevicelogsSourcesCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
	
}

