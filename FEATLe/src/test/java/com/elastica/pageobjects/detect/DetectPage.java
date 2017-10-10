package com.elastica.pageobjects.detect;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;

/**
 * Detect page
 * @author Eldo Rajan
 *
 */
public class DetectPage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> detectTabs(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListThreatScoreTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListUserNameTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListUserEmailTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListServicesTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListIncidentsTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> elementListLastUpdatedTable(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink elementThreatScoreTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink elementUserNameTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink elementUserEmailTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink elementServicesTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink elementIncidentsTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink elementLastUpdatedTable(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("ACOUNT", Integer.toString(count)));
	}
	
	public HyperLink userSlidingPanel(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelRedirectionLink(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelTabs(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink userSlidingPanelActiveTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink userSlidingPanelViewAllIncidents(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink userSlidingPanelIncidentCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelIncidentElementList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelIncidentTimeStampElementList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelIncidentServiceElementList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> userSlidingPanelIncidentDescriptionElementList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
		
	public ElementList<Element> incidentListIncidentsTab(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> incidentSeverityListIncidentsTab(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> incidentServicesListIncidentsTab(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> incidentServicesTypeListIncidentsTab(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> incidentUserListIncidentsTab(WebDriver driver, int count){
		return new ElementList<Element>(driver,getLocator().replace("ROW_COUNT", String.valueOf(count)));
	}
	
	public ElementList<Element> incidentDateTimeListIncidentsTab(WebDriver driver, int count){
		return new ElementList<Element>(driver,getLocator().replace("ROW_COUNT", String.valueOf(count)));
	}
	
	public ElementList<Element> incidentTypeListIncidentsTab(WebDriver driver, int count){
		return new ElementList<Element>(driver,getLocator().replace("ROW_COUNT", String.valueOf(count)));
	}
	
	public ElementList<Element> incidentOptionsListIncidentsTab(WebDriver driver, int count){
		return new ElementList<Element>(driver,getLocator().replace("ROW_COUNT", String.valueOf(count)));
	}
	
	public ElementList<Element> detectWidgets(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink detectClearFiltersBtn(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink detectFiltersSelectTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink detectFiltersTag(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink detectFiltersLowTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink detectFiltersMediumTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersHighTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersLowButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersMediumButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersHighButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink otherLowRowsName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectUsersDonutValue(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink detectUsersChartRowElementRiskScore(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	public HyperLink detectUsersChartRowElementName(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	public HyperLink detectUsersChartRowElementEmail(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	public HyperLink detectUsersChartRowElementServices(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	public HyperLink detectUsersChartRowElementIncidents(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	public HyperLink detectUsersChartRowElementTimeStamp(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", String.valueOf(count)));
	}
	
	public ElementList<Element> detectUsersChartRowElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink detectFiltersLowTabSize(WebDriver driver) {
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersMediumTabSize(WebDriver driver) {
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink detectFiltersHighTabSize(WebDriver driver) {
		return new HyperLink(driver,getLocator());
	}
	
}