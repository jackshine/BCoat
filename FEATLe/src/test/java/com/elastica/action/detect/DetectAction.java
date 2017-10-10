package com.elastica.action.detect;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.detect.DetectPage;
import com.elastica.webelements.Element;

/**
 * Detect common actions
 * @author eldorajan
 *
 */
public class DetectAction extends Action{

	public String getPageHeader(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		String headerText="";
		try {
			headerText = dp.header(driver).getText().trim();
			Logger.info("****Actual Header Text:"+headerText+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public String getActiveTabText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		String headerText="";
		try {
			headerText = dp.activeTab(driver).getText().trim();
			Logger.info("****Actual Active Tab Text:"+headerText+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public void clickTabDetectDashboard(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> detectTabs  = dp.detectTabs(driver).getChildElements();		
			for(int i=0;i<detectTabs.size();i++){
				if(detectTabs.get(i).getText().trim().contains(type)){
					detectTabs.get(i).mouseOver(driver);
					detectTabs.get(i).click();
					hardWait(10);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());


	}

	public int getTableCountUsersTab(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		int count=0;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> userList  = dp.elementListIncidentsTable(driver).getChildElements();		
			count = userList.size();
			Logger.info("****No of users in table:"+count+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}

	public void clickARowInUsersTab(WebDriver driver, int count){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		dp.elementThreatScoreTable(driver, count).mouseOver(driver);
		dp.elementThreatScoreTable(driver, count).click();
		hardWait(10);
		Assert.assertTrue(dp.userSlidingPanel(driver).isElementVisible(),
				"Detect App-Users Tab: Sliding panel is not getting opened");

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickRedirectionLinkFromSlidingPanelInUsersTab(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> userSlidingPanelRedirectionLink  = dp.userSlidingPanelRedirectionLink(driver).getChildElements();
			
			Assert.assertTrue(userSlidingPanelRedirectionLink.get(0).isElementVisible(),
					"Detect App-Users Tab: Sliding panel redirection link is not visible");

			int windowCountBefore = getWindowCount(driver);
			String winHandleBefore = getWindowHandle(driver);

			userSlidingPanelRedirectionLink  = dp.userSlidingPanelRedirectionLink(driver).getChildElements();
			userSlidingPanelRedirectionLink.get(0).click();hardWait(30);

			Set<String> winHandlesAfter = getWindowHandles(driver);
			int windowCountAfter = getWindowCount(driver);

			for(String winHandle : winHandlesAfter){
				if(winHandle.equalsIgnoreCase(winHandleBefore)){
					Logger.info("Control is in current window, switching of windows will not be enforced");
				}else{
					Logger.info("Control is in new window, switching of windows will be enforced");
					switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));
					break;
				}
			}

			Assert.assertEquals(windowCountAfter,windowCountBefore+1, "New window is not getting opened after clicking redirect link from sliding panel");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void selectTabFromUsersDetailedView(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {

			List<Element> userSlidingPanelTabs  = dp.userSlidingPanelTabs(driver).getChildElements();		
			for(int i=0;i<userSlidingPanelTabs.size();i++){
				userSlidingPanelTabs  = dp.userSlidingPanelTabs(driver).getChildElements();	
				Logger.info("Going to click tab:"+userSlidingPanelTabs.get(i).getInnerHtml().trim());
				if(userSlidingPanelTabs.get(i).getInnerHtml().trim().contains(type)){
					userSlidingPanelTabs.get(i).mouseOver(driver);
					userSlidingPanelTabs.get(i).click();
					hardWait(20);
					break;
				}
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String getActiveTabFromUsersDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		String headerText="";
		try {
			headerText = dp.userSlidingPanelActiveTab(driver).getText().trim();
			Logger.info("****Actual active tab text in user detailed view:"+headerText+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public void clickViewAllIncidentsLink(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			dp.userSlidingPanelViewAllIncidents(driver).click();hardWait(20);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public int getIncidentsCountDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		String text="";
		try {
			text = dp.userSlidingPanelIncidentCount(driver).getText().trim();
			Logger.info("****Actual incidents count in user detailed view:"+text+"****");
			if(StringUtils.isBlank(text)){
				Assert.fail("No count is seen in incidents tabs of detect incident detailed view");
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(text);
	}

	public int getIncidentsListCountDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		int count=0;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> incidentList  = dp.userSlidingPanelIncidentElementList(driver).getChildElements();		
			count = incidentList.size();
			Logger.info("****No of incidents in table for incidents tabs of user detailed view:"+count+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}

	public String[] getIncidentsTimeStampFromDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] timestamp=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> incidentList  = dp.userSlidingPanelIncidentTimeStampElementList(driver).getChildElements();		
			if(incidentList.size()>0){
				timestamp = new String[incidentList.size()];
				for(int i=0;i<incidentList.size();i++){
					timestamp[i]=incidentList.get(i).getText().trim();
					timestamp[i] = getDateInFormat(timestamp[i], "MMM dd, yyyy, HH:mm:ss aa","MMM dd, yyyy, HH:mm:ss aa");
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return timestamp;
	}

	public String[] getIncidentsServiceFromDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] service=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> serviceList  = dp.userSlidingPanelIncidentServiceElementList(driver).getChildElements();		
			if(serviceList.size()>0){
				service = new String[serviceList.size()];
				for(int i=0;i<serviceList.size();i++){
					service[i]=serviceList.get(i).getText().trim();
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return service;
	}

	public String[] getIncidentsDescriptionFromDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] description=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> descriptionList  = dp.userSlidingPanelIncidentDescriptionElementList(driver).getChildElements();		
			if(descriptionList.size()>0){
				description = new String[descriptionList.size()];
				for(int i=0;i<descriptionList.size();i++){
					description[i]=descriptionList.get(i).getText().trim();
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return description;
	}

	public int getElementListInIncidentsTab(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		int count=0;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> list  = dp.incidentListIncidentsTab(driver).getChildElements();		
			count = list.size();
			Logger.info("****Detect App:Incident Tab - No of incidents:"+count+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}

	public String[] getIncidentsTimeStampFromIncidentsTab(WebDriver driver, int count){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] timestamp=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> incidentList  = dp.incidentDateTimeListIncidentsTab(driver, count).getChildElements();		
			if(incidentList.size()>0){
				timestamp = new String[incidentList.size()];
				for(int i=0;i<incidentList.size();i++){
					timestamp[i]=incidentList.get(i).getText().trim();
					timestamp[i] = getDateInFormat(timestamp[i], "MMM dd, HH:mm:ss aa","MMM dd, yyyy, HH:mm:ss aa");

				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return timestamp;
	}

	public String[] getIncidentsServiceFromIncidentsTab(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] service=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> serviceList  = dp.incidentServicesListIncidentsTab(driver).getChildElements();		
			if(serviceList.size()>0){
				service = new String[serviceList.size()];
				for(int i=0;i<serviceList.size();i++){
					service[i]=serviceList.get(i).getText().trim();
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return service;
	}

	public String[] getIncidentsDescriptionFromIncidentsTab(WebDriver driver, int count){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] description=null;
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		try {
			List<Element> descriptionList  = dp.incidentTypeListIncidentsTab(driver, count).getChildElements();		
			if(descriptionList.size()>0){
				description = new String[descriptionList.size()];
				for(int i=0;i<descriptionList.size();i++){
					description[i]=descriptionList.get(i).getText().trim();
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return description;
	}

	public String verifyIncidentDescription(String[] expectedDescription, String[] actualDescription) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String validationMessage="";

		for(int i=0;i<expectedDescription.length;i++){
			if(!(actualDescription[i].contains(expectedDescription[i]))){
				validationMessage += (!actualDescription[i].isEmpty()) ? "" :
					"Expecting Description was "+expectedDescription[i]+" but is "+actualDescription[i];
			}else{
				validationMessage += (actualDescription[i].contains(expectedDescription[i])) ? "" :
					"Expecting Description was "+expectedDescription[i]+" but is "+actualDescription[i];
			}

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}


	public int getDashboardWidgetCount(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver, DetectPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return dp.detectWidgets(driver).getChildElements().size();
	}


	public void validateDetectPage(WebDriver driver) {
		try {
			DetectPage ap =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);

			ap.header(driver).waitForElementPresent(driver);
			ap.header(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.header(driver).isElementVisible(), "Detect page header is not visible");

		} catch (Exception ex) {
			Assert.fail("Issue with Redirect to Elastica CEP portal from Scan center Operation " + ex.getLocalizedMessage());
		}
	}

	public void validateDetectUsersTabFilterValidation(WebDriver driver, String filterType) {
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);
		String message = "";
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			

			clickDetectFilterDropdownButton(driver);
			clickDetectFilterButtonFromDropdown(driver, filterType);
			
			int usersCountFromChart = getDetectUsersCountFromChart(driver, filterType);
			int userCountFromChartLegend = getDetectUsersCountFromChartLegend(driver, filterType);
			List<Element> detectUsersRowElements = dp.detectUsersChartRowElement(driver).getChildElements();
			int userCountFromChartTable = detectUsersRowElements.size();
			
			message += allEqual(usersCountFromChart,userCountFromChartLegend,userCountFromChartTable) 
					? "" : "Number count mismatching for values from chart:"+usersCountFromChart+
							" legends:"+userCountFromChartLegend+" row count:"+userCountFromChartTable+"\n";
			
			for(int i=0;i<userCountFromChartTable;i++){
				String riskScore = dp.detectUsersChartRowElementRiskScore(driver, i+1).getText().trim();
				String name = dp.detectUsersChartRowElementName(driver, i+1).getText().trim();
				String email = dp.detectUsersChartRowElementEmail(driver, i+1).getText().trim();
				String services = dp.detectUsersChartRowElementServices(driver, i+1).getText().trim();
				String incidents = dp.detectUsersChartRowElementIncidents(driver, i+1).getText().trim();
				String timeStamp = dp.detectUsersChartRowElementTimeStamp(driver, i+1).getText().trim();
				
				message += (StringUtils.isNotBlank(riskScore)) 
						? "" : "Risk Score in table not present for "+(i+1)+" but was"+riskScore+"\n";
				message += (StringUtils.isNotBlank(name)) 
						? "" : "Name in table not present for "+(i+1)+" but was"+name+"\n";
				message += (StringUtils.isNotBlank(email)) 
						? "" : "Email in table not present for "+(i+1)+" but was"+email+"\n";
				message += (StringUtils.isNotBlank(services)) 
						? "" : "Services in table not present for "+(i+1)+" but was"+services+"\n";
				message += (StringUtils.isNotBlank(incidents)) 
						? "" : "Incidents in table not present for "+(i+1)+" but was"+incidents+"\n";
				message += (StringUtils.isNotBlank(timeStamp)) 
						? "" : "Time Stamp in table not present for "+(i+1)+" but was"+timeStamp+"\n";
				
			}	
			
			Assert.assertEquals(message, "", "Validation failing after selection of "+filterType+" filter and errors are"+message);
			
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ ex.getLocalizedMessage());
			Assert.fail("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	private int getDetectUsersCountFromChart(WebDriver driver, String filterType) {
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);
		return (Integer.parseInt(dp.detectUsersDonutValue(driver).getText()));
	}

	private int getDetectUsersCountFromChartLegend(WebDriver driver, String filterType) {
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);
		
		if(filterType.equalsIgnoreCase("Low")){
			return (Integer.parseInt(dp.detectFiltersLowTabSize(driver).getText()));
		}else if(filterType.equalsIgnoreCase("Medium")){
			return (Integer.parseInt(dp.detectFiltersMediumTabSize(driver).getText()));
		}else if(filterType.equalsIgnoreCase("High")){
			return (Integer.parseInt(dp.detectFiltersHighTabSize(driver).getText()));
		}
		
		return 0;
	}

	public void clickDetectFilterDropdownButton(WebDriver driver) {
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);
		
		dp.detectFiltersTag(driver).waitForElementPresent(driver);
		dp.detectFiltersTag(driver).waitForElementToBeVisible(driver);
		dp.detectFiltersTag(driver).click(); 
		hardWait(20);
	}
	
	public void clickDetectFilterButtonFromDropdown(WebDriver driver, String filterType) {
		DetectPage dp =  AdvancedPageFactory.getPageObject(driver,DetectPage.class);
		
		Logger.info("Clicking filter:"+filterType);
		
		if(filterType.equalsIgnoreCase("Low")){
			dp.detectFiltersLowButton(driver).waitForElementPresent(driver);
			dp.detectFiltersLowButton(driver).waitForElementToBeVisible(driver);
			dp.detectFiltersLowButton(driver).click(); 
		}else if(filterType.equalsIgnoreCase("Medium")){
			dp.detectFiltersMediumButton(driver).waitForElementPresent(driver);
			dp.detectFiltersMediumButton(driver).waitForElementToBeVisible(driver);
			dp.detectFiltersMediumButton(driver).click(); 
		}else if(filterType.equalsIgnoreCase("High")){
			dp.detectFiltersHighButton(driver).waitForElementPresent(driver);
			dp.detectFiltersHighButton(driver).waitForElementToBeVisible(driver);
			dp.detectFiltersHighButton(driver).click(); 
		}
		
		hardWait(30);
		
		Logger.info("Clicked filter:"+filterType);
	}

	public boolean allEqual(Object key, Object... objs) {
	    for(Object o : objs) if(!o.equals(key)) return false;
	    return true;
	}

	
}