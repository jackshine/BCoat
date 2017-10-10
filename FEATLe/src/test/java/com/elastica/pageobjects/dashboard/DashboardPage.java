package com.elastica.pageobjects.dashboard;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;

/**
 * Dashboard page
 * @author Eldo Rajan
 *
 */
public class DashboardPage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink profileName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink setting(WebDriver driver) {
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink logout(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> sidebarLinks(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}

	public ElementList<Element> sidebarLinksText(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> sidebarSubMenuLinks(WebDriver driver, int count){
		return new ElementList<Element>(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public ElementList<Element> sidebarDeepMenuLinks(WebDriver driver, int count, int appCount){
		return new ElementList<Element>(driver,getLocator().replace("SCOUNT",
				Integer.toString(count)).replace("ACOUNT", Integer.toString(appCount)));
	}
	
	public HyperLink sidebarLink(WebDriver driver, int count) {
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink sidebarSubMenuLink(WebDriver driver, int count, int appCount) {
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)).replace("ACOUNT", Integer.toString(appCount)));
	}
	
	public HyperLink sidebarDeepMenuLink(WebDriver driver, int count, int appCount, int typeCount){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count))
				.replace("ACOUNT", Integer.toString(appCount)).replace("MCOUNT", Integer.toString(typeCount)));
	}

	public ElementList<Element> dashboardWidgets(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink loadingIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink storeLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink dashboardLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink auditLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink detectLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink protectLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink investigateLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
}
