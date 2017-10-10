package com.elastica.action.audit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.action.dashboard.DashboardAction;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.audit.AuditPage;
import com.elastica.pageobjects.CiscoPage;
import com.elastica.webelements.Element;

/**
 * Sample google search action class
 * @author Eldo Rajan
 *
 */
public class AuditAction extends Action{

	public enum ServiceType {
		Twitter,Dropbox,Salesforce,LogMeIn,Box
	}

	public ServiceType getServiceName(String service){
		ServiceType sName= null;

		if(service.equalsIgnoreCase("Twitter")){
			sName = ServiceType.Twitter;
		}else if(service.equalsIgnoreCase("Dropbox")){
			sName = ServiceType.Dropbox;
		}else if(service.equalsIgnoreCase("Salesforce")){
			sName = ServiceType.Salesforce;
		}else if(service.equalsIgnoreCase("LogMeIn")){
			sName = ServiceType.LogMeIn;
		}else if(service.equalsIgnoreCase("Box")){
			sName = ServiceType.Box;
		}

		return sName;

	}

	public String getServiceDescription(ServiceType service){
		String sDescription= null;

		if(service.equals(ServiceType.Twitter)){
			sDescription = "Social Network";
		}else if(service.equals(ServiceType.Dropbox)){
			sDescription = "Storage, File Sharing";
		}else if(service.equals(ServiceType.Salesforce)){
			sDescription = "CRM";
		}else if(service.equals(ServiceType.LogMeIn)){
			sDescription = "Remote Access";
		}else if(service.equals(ServiceType.Box)){
			sDescription = "Storage, File Sharing";
		}

		return sDescription;

	}


	/**
	 * login into cisco page
	 * @param driver
	 * @param username
	 * @param password
	 */
	public void loginCiscoPage(WebDriver driver,String username,String password){

		if(driver.getTitle().trim().equalsIgnoreCase("Cisco")){
			Logger.info("Login into cisco page is successful");
		}else{
			try {
				CiscoPage cp =  AdvancedPageFactory.getPageObject(driver,CiscoPage.class);

				Assert.assertTrue(cp.username(driver).isElementVisible(), "Cisco Username textbox is not visible");
				Assert.assertTrue(cp.password(driver).isElementVisible(), "Cisco Password textbox is not visible");
				Assert.assertTrue(cp.loginButton(driver).isElementVisible(), "Cisco login button is not visible");

				cp.username(driver).clear();cp.username(driver).type(username);
				cp.password(driver).clear();cp.password(driver).type(password);
				cp.loginButton(driver).click();

				cp.auditRedirectLink(driver).waitForElementPresent(driver);
				cp.auditRedirectLink(driver).waitForElementToBeVisible(driver);

				Assert.assertTrue(cp.auditRedirectLink(driver).isElementVisible(), "Audit redirect link is not visible");
				Assert.assertEquals(driver.getTitle().trim(),"Cisco", "Cisco page title is not matching");
				Assert.assertEquals(cp.auditRedirectLink(driver).getText(),"Launch Cisco Cloud Access Security by Elastica.", "Cisco Audit Redirect Link text is not matching");
				Logger.info(cp.auditRedirectLink(driver).getText());

			} catch (Exception ex) {
				Assert.fail("Issue with Cisco Login Page Operation " + ex.getLocalizedMessage());
			}
		}
	}

	public void clickOnAuditRedirectLink(WebDriver driver) {
		try {
			CiscoPage cp =  AdvancedPageFactory.getPageObject(driver,CiscoPage.class);

			int windowCountBefore = getWindowCount(driver);
			String winHandleBefore = getWindowHandle(driver);

			cp.auditRedirectLink(driver).waitForElementPresent(driver);
			cp.auditRedirectLink(driver).waitForElementToBeVisible(driver);
			cp.auditRedirectLink(driver).click();

			hardWait(30);

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

			Assert.assertEquals(windowCountAfter,windowCountBefore+1, "New window is not getting opened after clicking audit redirect link");

		} catch (Exception ex) {
			Assert.fail("Issue with Redirect to Elastica CEP portal from Scan center Operation " + ex.getLocalizedMessage());
		}

	}

	public void validateAuditPage(WebDriver driver) {
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			ap.auditheader(driver).waitForElementPresent(driver);
			ap.auditheader(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.auditheader(driver).isElementVisible(), "Audit page header is not visible");

		} catch (Exception ex) {
			Assert.fail("Audit Page Validation is failing " + ex.getLocalizedMessage());
		}
	}

	public void clickOnAuditSideBarLinks(WebDriver driver,String auditText) {
		try {
			clickAuditIconSidebar(driver);
			hoverOverClickOptionFromAuditSidebar(driver,auditText);

		} catch (Exception ex) {
			Assert.fail("Clicking on audit side bar links is failing " + ex.getLocalizedMessage());
		}

	}

	private void clickAuditIconSidebar(WebDriver driver) throws InterruptedException {
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		ap.auditicon(driver).waitForElementPresent(driver);
		ap.auditicon(driver).waitForElementToBeVisible(driver);
		ap.auditicon(driver).mouseOver(driver);
		hardWait(20);
	}
	public void hoverOverClickOptionFromAuditSidebar(WebDriver driver, String optionType) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditsidebaroptionlist  = ap.auditsidebaroptionlist(driver).getChildElements();		
			for(int i=0;i<auditsidebaroptionlist.size();i++){
				auditsidebaroptionlist  = ap.auditsidebaroptionlist(driver).getChildElements();
				if(auditsidebaroptionlist.get(i).getText().contains(optionType)){
					//auditsidebaroptionlist.get(i).mouseOver(driver);
					auditsidebaroptionlist.get(i).click();
					hardWait(20);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void clickOptionFromAuditTab(WebDriver driver, String optionType) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> audittab  = ap.audittab(driver).getChildElements();		
			for(int i=0;i<audittab.size();i++){
				audittab  = ap.audittab(driver).getChildElements();
				if(audittab.get(i).getText().contains(optionType)){
					audittab.get(i).mouseOver(driver);
					audittab.get(i).click();
					hardWait(15);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateSourcesPageInAudit(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.auditheader(driver).waitForElementPresent(driver);
			ap.auditheader(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.auditheader(driver).isElementVisible(), "Device logs page header is not visible");
			Assert.assertEquals(ap.auditheader(driver).getText(),"Device logs", "Header for Device logs page in audit is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validatePreferencesPageInAudit(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.auditheader(driver).waitForElementPresent(driver);
			ap.auditheader(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.auditheader(driver).isElementVisible(), "Preferences page header is not visible");
			Assert.assertEquals(ap.auditheader(driver).getText(),"Preferences", "Header for Preferences page in audit is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateScheduledReportsPageInAudit(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.auditheader(driver).waitForElementPresent(driver);
			ap.auditheader(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.auditheader(driver).isElementVisible(), "Scheduled Reports page header is not visible");
			Assert.assertEquals(ap.auditheader(driver).getText(),"Scheduled Reports", "Header for Scheduled Reports page in audit is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateFindServicesPageInAudit(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.auditheader(driver).waitForElementPresent(driver);
			ap.auditheader(driver).waitForElementToBeVisible(driver);

			Assert.assertTrue(ap.auditheader(driver).isElementVisible(), "Compare Services page header is not visible");
			Assert.assertEquals(ap.auditheader(driver).getText(),"Compare Services", "Header for Compare Services page in audit is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void generateCWSTraffic(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(com.elastica.constants.audit.AuditConstants.cwsScriptLocation);
			process.waitFor();
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String processStream = "";

			while((processStream = bufReader.readLine()) != null){
				Logger.info(processStream);
			}

			for(int i=0;i<15;i++){
				driver.navigate().refresh();
				hardWait(60);
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickCloseOverviewVideo(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			if(ap.hideoverviewvideo(driver).isElementVisible()){
				ap.hideoverviewvideo(driver).mouseOverClick(driver);
				ap.showoverviewvideo(driver).waitForElementToBeVisible(driver);
			}
		} catch (Exception e) {
			//Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickAuditTimePeriodSelectorButton(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.audittimeperiodselectorbutton(driver).waitForElementPresent(driver);
			ap.audittimeperiodselectorbutton(driver).waitForElementToBeVisible(driver);
			ap.audittimeperiodselectorbutton(driver).mouseOverClick(driver);

			hardWait(5);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void clickOptionFromAuditTimePeriodDropdown(WebDriver driver, String optionType) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		clickAuditTimePeriodSelectorButton(driver);
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			if(optionType.contains("Last Day")){
				ap.audittimeperiodselectorLastDaydropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLastDaydropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLastDaydropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLastDaydropdown(driver).click();
			}else if(optionType.contains("Last 7 Days")){
				ap.audittimeperiodselectorLast7Daysdropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLast7Daysdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLast7Daysdropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLast7Daysdropdown(driver).click();
			}else if(optionType.contains("Last Calendar Week")){
				ap.audittimeperiodselectorLastCalendarWeekdropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLastCalendarWeekdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLastCalendarWeekdropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLastCalendarWeekdropdown(driver).click();
			}else if(optionType.contains("Last 30 Days")){
				ap.audittimeperiodselectorLast30Daysdropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLast30Daysdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLast30Daysdropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLast30Daysdropdown(driver).click();
			}else if(optionType.contains("Last Calendar Month")){
				ap.audittimeperiodselectorLastCalendarMonthdropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLastCalendarMonthdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLastCalendarMonthdropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLastCalendarMonthdropdown(driver).click();
			}else if(optionType.contains("Last 12 Months")){
				ap.audittimeperiodselectorLast12Monthsdropdown(driver).waitForElementPresent(driver);
				ap.audittimeperiodselectorLast12Monthsdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittimeperiodselectorLast12Monthsdropdown(driver).mouseOver(driver);
				ap.audittimeperiodselectorLast12Monthsdropdown(driver).click();
			}


			hardWait(15);

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String getDateRangeFilterValue(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String value="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			value = ap.audittimeperiodselectorbuttonvalue(driver).getText();

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.fail("Not able to retrive date range filter value");
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return value;
	}

	public String getOtherTabsDateRangeFilterValue(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String value="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			value = ap.audittimeperiodselectorbuttonvalueothertabs(driver).getText();

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.fail("Not able to retrive date range filter value");
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return value;
	}


	public int getScoreCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String scoreCount="";
		try {
			scoreCount = ap.auditscorecount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(scoreCount);

	}

	public int getSaasServicesCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String saasServicesCount="";
		try {
			saasServicesCount = ap.auditsaasservicescount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(saasServicesCount);

	}


	public int getRiskPercentageCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String riskPercentageCount="";
		try {
			riskPercentageCount = ap.auditriskpercentagecount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(riskPercentageCount.replace("%", ""));

	}

	public int getRiskServicesCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String riskServicesCount="";
		try {
			riskServicesCount = ap.auditriskservicescount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(riskServicesCount.replace(" Services", ""));

	}

	public int getUserCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String userCount="";
		try {
			userCount = ap.auditusercount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(userCount);

	}

	public int getDestinationCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String destinationCount="";
		try {
			destinationCount = ap.auditdestinationcount(driver).getText();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return Integer.parseInt(destinationCount);

	}

	public String getSummaryTableDetails(WebDriver driver, String type, String[] expectedTitle, String[] expectedDescription) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		DashboardAction db = new DashboardAction();
		try {
			ArrayList<String> actualTitle = new ArrayList<String>();
			ArrayList<String> actualDescription = new ArrayList<String>();

			List<Element> auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();		
			for(int i=0;i<auditsummarytabservicestableelement.size();i++){
				List<Element> auditsummarytabservicesnumbertableelement  = ap.auditsummarytabservicesnumbertableelement(driver).getChildElements();
				List<Element> auditsummarytabservicestitlenametableelement  = ap.auditsummarytabservicestitlenametableelement(driver).getChildElements();
				List<Element> auditsummarytabservicestitledescriptiontableelement  = ap.auditsummarytabservicestitledescriptiontableelement(driver).getChildElements();
				List<Element> auditsummarytabservicesriskscoretableelement  = ap.auditsummarytabservicesriskscoretableelement(driver).getChildElements();
				List<Element> auditsummarytabservicesuserstableelement  = ap.auditsummarytabservicesuserstableelement(driver).getChildElements();

				String number = auditsummarytabservicesnumbertableelement.get(i).getText();
				String title = auditsummarytabservicestitlenametableelement.get(i).getText();actualTitle.add(title);
				String description = auditsummarytabservicestitledescriptiontableelement.get(i).getText();actualDescription.add(description);
				String riskScore = auditsummarytabservicesriskscoretableelement.get(i).getText();
				String users = auditsummarytabservicesuserstableelement.get(i).getText().replace("Users", "").trim();

				validationMessage += (Integer.parseInt(number)==(i+1)) ? "" : "Summary Tab:"+type+
						" - Expecting row count:"+(i+1)+" but was " + Integer.parseInt(number);
				validationMessage += (!title.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some title but was " + title;
				validationMessage += (!description.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some description but was " + description;
				validationMessage += (Integer.parseInt(riskScore)>0) ? "" : "Summary Tab:"+type+
						" - Expecting some risk score but was " + Integer.parseInt(riskScore);
				validationMessage += (Integer.parseInt(users)>0) ? "" : "Summary Tab:"+type+
						" - Expecting some user count but was " + Integer.parseInt(users.replace(" Users", ""));
			}

			Logger.info("Expected Title:"+Arrays.asList(expectedTitle));
			Logger.info("Actual Title:"+actualTitle);
			Logger.info("Expected Description:"+Arrays.asList(expectedDescription));
			Logger.info("Actual Description:"+actualDescription);

			validationMessage += (db.arrayComparison(expectedTitle,actualTitle.toArray(new String[actualTitle.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Title was "+Arrays.asList(expectedTitle)+" but is "+actualTitle;
			validationMessage += (db.arrayComparison(expectedDescription, actualDescription.toArray(new String[actualDescription.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Description was "+Arrays.asList(expectedDescription)+" but is "+actualDescription;


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getSummaryTableDetails(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		try {
			ArrayList<String> actualTitle = new ArrayList<String>();
			ArrayList<String> actualDescription = new ArrayList<String>();

			List<Element> auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();		
			for(int i=0;i<auditsummarytabservicestableelement.size();i++){
				List<Element> auditsummarytabservicesnumbertableelement  = ap.auditsummarytabservicesnumbertableelement(driver).getChildElements();
				List<Element> auditsummarytabservicestitlenametableelement  = ap.auditsummarytabservicestitlenametableelement(driver).getChildElements();
				List<Element> auditsummarytabservicestitledescriptiontableelement  = ap.auditsummarytabservicestitledescriptiontableelement(driver).getChildElements();
				List<Element> auditsummarytabservicesriskscoretableelement  = ap.auditsummarytabservicesriskscoretableelement(driver).getChildElements();
				List<Element> auditsummarytabservicesuserstableelement  = ap.auditsummarytabservicesuserstableelement(driver).getChildElements();

				String number = auditsummarytabservicesnumbertableelement.get(i).getText();
				String title = auditsummarytabservicestitlenametableelement.get(i).getText();actualTitle.add(title);
				String description = auditsummarytabservicestitledescriptiontableelement.get(i).getText();actualDescription.add(description);
				String riskScore = auditsummarytabservicesriskscoretableelement.get(i).getText();
				String users = auditsummarytabservicesuserstableelement.get(i).getText().replace("Users", "").trim();

				validationMessage += (Integer.parseInt(number)==(i+1)) ? "" : "Summary Tab:"+type+
						" - Expecting row count:"+(i+1)+" but was " + Integer.parseInt(number);
				validationMessage += (!title.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some title but was " + title;
				validationMessage += (!description.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some description but was " + description;
				validationMessage += (Integer.parseInt(riskScore)>0) ? "" : "Summary Tab:"+type+
						" - Expecting some risk score but was " + Integer.parseInt(riskScore);
				validationMessage += (Integer.parseInt(users)>0) ? "" : "Summary Tab:"+type+
						" - Expecting some user count but was " + Integer.parseInt(users.replace(" Users", ""));
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}


	public int getSummaryTableDetailsCount(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		int count=0;

		try {
			List<Element> auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();		
			count = auditsummarytabservicestableelement.size();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}

	public void clickAuditTopOptionsSelectorButton(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			ap.audittopoptionselectorbutton(driver).waitForElementPresent(driver);
			ap.audittopoptionselectorbutton(driver).waitForElementToBeVisible(driver);
			ap.audittopoptionselectorbutton(driver).mouseOverClick(driver);

			hardWait(5);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void clickAuditTopOptionsSelectorButton(WebDriver driver, String optionType) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		clickAuditTopOptionsSelectorButton(driver);
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			if(optionType.contains("Top Risky Services")){
				ap.audittopoptionselectorriskyservicesdropdown(driver).waitForElementPresent(driver);
				ap.audittopoptionselectorriskyservicesdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittopoptionselectorriskyservicesdropdown(driver).mouseOver(driver);
				ap.audittopoptionselectorriskyservicesdropdown(driver).click();
			}else if(optionType.contains("Top Used Services")){
				ap.audittopoptionselectorusedservicesdropdown(driver).waitForElementPresent(driver);
				ap.audittopoptionselectorusedservicesdropdown(driver).waitForElementToBeVisible(driver);
				ap.audittopoptionselectorusedservicesdropdown(driver).mouseOver(driver);
				ap.audittopoptionselectorusedservicesdropdown(driver).click();
			}

			hardWait(5);

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String getSummaryPanelDetails(WebDriver driver,String type,String[] expectedTitle,String[] expectedDescription) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();		
			for(int i=0;i<auditsummarytabservicestableelement.size();i++){
				auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();
				auditsummarytabservicestableelement.get(i).mouseOver(driver);
				auditsummarytabservicestableelement.get(i).click();

				validationMessage+=getAuditSummaryDetailedPanel(driver,type,expectedTitle,expectedDescription);

			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getSummaryPanelDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();		
			for(int i=0;i<auditsummarytabservicestableelement.size();i++){
				auditsummarytabservicestableelement  = ap.auditsummarytabservicestableelement(driver).getChildElements();
				auditsummarytabservicestableelement.get(i).mouseOver(driver);
				auditsummarytabservicestableelement.get(i).click();

				validationMessage+=getAuditSummaryDetailedPanel(driver,type);

			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getAuditSummaryDetailedPanel(WebDriver driver,String type,String[] expectedTitle,String[] expectedDescription) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		DashboardAction db = new DashboardAction();
		String validationMessage="";

		ArrayList<String> actualTitle = new ArrayList<String>();
		ArrayList<String> actualDescription = new ArrayList<String>();

		try{

			String appName = ap.auditsummarydetailpanelappname(driver).getText();actualTitle.add(appName);
			String appSubHeading = ap.auditsummarydetailpanelappsubheading(driver).getText();actualDescription.add(appSubHeading);
			String destinationHeader = ap.auditsummarydetailpaneldestinationheader(driver).getText();
			int destinationHeaderCount = Integer.parseInt(destinationHeader.replace("Destinations (", "").replace(")",""));
			String destinationCountryCount = ap.auditsummarydetailpaneldestinationcountrycount(driver).getInnerHtml().trim();
			String destinationCityCount = ap.auditsummarydetailpaneldestinationcitycount(driver).getInnerHtml().trim();

			validationMessage += (!appName.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some App Name but was " + appName;
			validationMessage += (!appSubHeading.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some App Sub Heading but was " + appSubHeading;
			validationMessage += (!destinationHeader.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Header but was " + destinationHeader;
			validationMessage += (destinationHeaderCount>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Header Count but was " + destinationHeaderCount;
			validationMessage += (Integer.parseInt(destinationCountryCount)>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Countries but was " + Integer.parseInt(destinationCountryCount);
			validationMessage += (Integer.parseInt(destinationCityCount)>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Cities but was " + Integer.parseInt(destinationCityCount);

			validationMessage += (db.arrayComparison(expectedTitle,actualTitle.toArray(new String[actualTitle.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Title was "+Arrays.asList(expectedTitle)+" but is "+actualTitle;
			validationMessage += (db.arrayComparison(expectedDescription, actualDescription.toArray(new String[actualDescription.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Description was "+Arrays.asList(expectedDescription)+" but is "+actualDescription;

			List<Element> auditsummarydetailpaneltopusers  = ap.auditsummarydetailpaneltopusers(driver).getChildElements();		
			for(int j=0;j<auditsummarydetailpaneltopusers.size();j++){
				List<Element> auditsummarydetailpaneltopusersip  = ap.auditsummarydetailpaneltopusersip(driver).getChildElements();	
				List<Element> auditsummarydetailpaneltopuserssession  = ap.auditsummarydetailpaneltopuserssession(driver).getChildElements();	
				List<Element> auditsummarydetailpaneltopusersdataconsumed  = ap.auditsummarydetailpaneltopusersdataconsumed(driver).getChildElements();	

				String UserIP = auditsummarydetailpaneltopusersip.get(j).getText();
				String UserSession = auditsummarydetailpaneltopuserssession.get(j).getText();
				String DataConsumed = auditsummarydetailpaneltopusersdataconsumed.get(j).getText();

				validationMessage += (!UserIP.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some User IP but was " + UserIP;
				validationMessage += (!UserSession.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some User Session but was " + UserSession;
				validationMessage += (!DataConsumed.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some Data Consumed but was " + DataConsumed;



			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getAuditSummaryDetailedPanel(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		String validationMessage="";

		ArrayList<String> actualTitle = new ArrayList<String>();
		ArrayList<String> actualDescription = new ArrayList<String>();

		try{

			String appName = ap.auditsummarydetailpanelappname(driver).getText();actualTitle.add(appName);
			String appSubHeading = ap.auditsummarydetailpanelappsubheading(driver).getText();actualDescription.add(appSubHeading);
			String destinationHeader = ap.auditsummarydetailpaneldestinationheader(driver).getText();
			int destinationHeaderCount = Integer.parseInt(destinationHeader.replace("Destinations (", "").replace(")",""));
			String destinationCountryCount = ap.auditsummarydetailpaneldestinationcountrycount(driver).getInnerHtml().trim();
			String destinationCityCount = ap.auditsummarydetailpaneldestinationcitycount(driver).getInnerHtml().trim();

			validationMessage += (!appName.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some App Name but was " + appName;
			validationMessage += (!appSubHeading.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some App Sub Heading but was " + appSubHeading;
			validationMessage += (!destinationHeader.isEmpty()) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Header but was " + destinationHeader;
			validationMessage += (destinationHeaderCount>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Header Count but was " + destinationHeaderCount;
			validationMessage += (Integer.parseInt(destinationCountryCount)>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Countries but was " + Integer.parseInt(destinationCountryCount);
			validationMessage += (Integer.parseInt(destinationCityCount)>0) ? "" : "Summary Tab:"+type+
					" - Expecting some Destination Cities but was " + Integer.parseInt(destinationCityCount);


			List<Element> auditsummarydetailpaneltopusers  = ap.auditsummarydetailpaneltopusers(driver).getChildElements();		
			for(int j=0;j<auditsummarydetailpaneltopusers.size();j++){
				List<Element> auditsummarydetailpaneltopusersip  = ap.auditsummarydetailpaneltopusersip(driver).getChildElements();	
				List<Element> auditsummarydetailpaneltopuserssession  = ap.auditsummarydetailpaneltopuserssession(driver).getChildElements();	
				List<Element> auditsummarydetailpaneltopusersdataconsumed  = ap.auditsummarydetailpaneltopusersdataconsumed(driver).getChildElements();	

				String UserIP = auditsummarydetailpaneltopusersip.get(j).getText();
				String UserSession = auditsummarydetailpaneltopuserssession.get(j).getText();
				String DataConsumed = auditsummarydetailpaneltopusersdataconsumed.get(j).getText();

				validationMessage += (!UserIP.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some User IP but was " + UserIP;
				validationMessage += (!UserSession.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some User Session but was " + UserSession;
				validationMessage += (!DataConsumed.isEmpty()) ? "" : "Summary Tab:"+type+
						" - Expecting some Data Consumed but was " + DataConsumed;



			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}


	public String getServicesHeaderDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();		
			for(int i=0;i<auditservicetabtopelement.size();i++){
				auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();

				int count = Integer.parseInt(auditservicetabtopelement.get(i).getText());
				validationMessage += (count>=0) ? "" : "Services Tab:"+type+
						" - Expecting some Service Tab Header Count but was " + count;
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getServicesTableDetails(WebDriver driver, String type, String[] expectedTitle, String[] expectedDescription) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		DashboardAction db = new DashboardAction();
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String validationMessage="";

		ArrayList<String> actualTitle = new ArrayList<String>();
		ArrayList<String> actualDescription = new ArrayList<String>();

		try {
			List<Element> auditservicetabtablerowelement  = ap.auditservicetabtablerowelement(driver).getChildElements();		
			for(int i=0;i<auditservicetabtablerowelement.size();i++){
				auditservicetabtablerowelement  = ap.auditservicetabtablerowelement(driver).getChildElements();


				List<Element> auditservicetabtableratingrowelement  = ap.auditservicetabtableratingrowelement(driver).getChildElements();
				List<Element> auditservicetabtablenamerowelement  = ap.auditservicetabtablenamerowelement(driver).getChildElements();
				List<Element> auditservicetabtablenametitlerowelement  = ap.auditservicetabtablenametitlerowelement(driver).getChildElements();
				List<Element> auditservicetabtablenamedescriptionrowelement  = ap.auditservicetabtablenamedescriptionrowelement(driver).getChildElements();
				List<Element> auditservicetabtablesessionsrowelement  = ap.auditservicetabtablesessionsrowelement(driver).getChildElements();
				List<Element> auditservicetabtableuploadrowelement  = ap.auditservicetabtableuploadrowelement(driver).getChildElements();
				List<Element> auditservicetabtabledownloadrowelement  = ap.auditservicetabtabledownloadrowelement(driver).getChildElements();
				List<Element> auditservicetabtableusersrowelement  = ap.auditservicetabtableusersrowelement(driver).getChildElements();
				List<Element> auditservicetabtabledestinationrowelement  = ap.auditservicetabtabledestinationrowelement(driver).getChildElements();
				List<Element> auditservicetabtableplatformrowelement  = ap.auditservicetabtableplatformrowelement(driver).getChildElements();
				List<Element> auditservicetabtableavgdurationrowelement  = ap.auditservicetabtableavgdurationrowelement(driver).getChildElements();

				int rating = Integer.parseInt(auditservicetabtableratingrowelement.get(i).getText());
				String name = auditservicetabtablenamerowelement.get(i).getText();
				String title = auditservicetabtablenametitlerowelement.get(i).getText();actualTitle.add(title);
				String description = auditservicetabtablenamedescriptionrowelement.get(i).getText();actualDescription.add(description);

				String sessions = auditservicetabtablesessionsrowelement.get(i).getText();
				String upload = auditservicetabtableuploadrowelement.get(i).getText();
				String download = auditservicetabtabledownloadrowelement.get(i).getText();
				int users = Integer.parseInt(auditservicetabtableusersrowelement.get(i).getText());
				int destination = Integer.parseInt(auditservicetabtabledestinationrowelement.get(i).getText());
				int platform = Integer.parseInt(auditservicetabtableplatformrowelement.get(i).getText());
				String avgduration = auditservicetabtableavgdurationrowelement.get(i).getText(); 

				validationMessage += (rating>0) ? "" : "Services Tab:"+type+
						" - Expecting some Rating Count but was " + rating;
				validationMessage += (!name.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Name but was " + name;
				validationMessage += (!sessions.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Sessions but was " + sessions;
				validationMessage += (!upload.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Upload but was " + upload;
				validationMessage += (!download.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Download but was " + download;
				validationMessage += (users>0) ? "" : "Services Tab:"+type+
						" - Expecting some User Count but was " + users;
				validationMessage += (destination>0) ? "" : "Services Tab:"+type+
						" - Expecting some Destination Count but was " + destination;
				validationMessage += (platform>0) ? "" : "Services Tab:"+type+
						" - Expecting some Platform Count but was " + platform;
				validationMessage += (!avgduration.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Average Duration but was " + avgduration;

			}

			Logger.info("Expected Title:"+Arrays.asList(expectedTitle));Logger.info("Actual Title:"+actualTitle);
			Logger.info("Expected Description:"+Arrays.asList(expectedDescription));Logger.info("Actual Description:"+actualDescription);

			validationMessage += (db.arrayComparison(expectedTitle,actualTitle.toArray(new String[actualTitle.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Title was "+Arrays.asList(expectedTitle)+" but is "+actualTitle;
			validationMessage += (db.arrayComparison(expectedDescription, actualDescription.toArray(new String[actualDescription.size()]))) ? "" : "Summary Tab:"+type+
					" - Expecting Description was "+Arrays.asList(expectedDescription)+" but is "+actualDescription;


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getServicesTableDetails(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String validationMessage="";

		ArrayList<String> actualTitle = new ArrayList<String>();
		ArrayList<String> actualDescription = new ArrayList<String>();

		try {
			List<Element> auditservicetabtablerowelement  = ap.auditservicetabtablerowelement(driver).getChildElements();		
			for(int i=0;i<auditservicetabtablerowelement.size();i++){
				auditservicetabtablerowelement  = ap.auditservicetabtablerowelement(driver).getChildElements();


				List<Element> auditservicetabtableratingrowelement  = ap.auditservicetabtableratingrowelement(driver).getChildElements();
				List<Element> auditservicetabtablenamerowelement  = ap.auditservicetabtablenamerowelement(driver).getChildElements();
				List<Element> auditservicetabtablenametitlerowelement  = ap.auditservicetabtablenametitlerowelement(driver).getChildElements();
				List<Element> auditservicetabtablenamedescriptionrowelement  = ap.auditservicetabtablenamedescriptionrowelement(driver).getChildElements();
				List<Element> auditservicetabtablesessionsrowelement  = ap.auditservicetabtablesessionsrowelement(driver).getChildElements();
				List<Element> auditservicetabtableuploadrowelement  = ap.auditservicetabtableuploadrowelement(driver).getChildElements();
				List<Element> auditservicetabtabledownloadrowelement  = ap.auditservicetabtabledownloadrowelement(driver).getChildElements();
				List<Element> auditservicetabtableusersrowelement  = ap.auditservicetabtableusersrowelement(driver).getChildElements();
				List<Element> auditservicetabtabledestinationrowelement  = ap.auditservicetabtabledestinationrowelement(driver).getChildElements();
				List<Element> auditservicetabtableplatformrowelement  = ap.auditservicetabtableplatformrowelement(driver).getChildElements();
				List<Element> auditservicetabtableavgdurationrowelement  = ap.auditservicetabtableavgdurationrowelement(driver).getChildElements();

				int rating = Integer.parseInt(auditservicetabtableratingrowelement.get(i).getText());
				String name = auditservicetabtablenamerowelement.get(i).getText();
				String title = auditservicetabtablenametitlerowelement.get(i).getText();actualTitle.add(title);
				String description = auditservicetabtablenamedescriptionrowelement.get(i).getText();actualDescription.add(description);

				String sessions = auditservicetabtablesessionsrowelement.get(i).getText();
				String upload = auditservicetabtableuploadrowelement.get(i).getText();
				String download = auditservicetabtabledownloadrowelement.get(i).getText();
				int users = Integer.parseInt(auditservicetabtableusersrowelement.get(i).getText());
				int destination = Integer.parseInt(auditservicetabtabledestinationrowelement.get(i).getText());
				int platform = Integer.parseInt(auditservicetabtableplatformrowelement.get(i).getText());
				String avgduration = auditservicetabtableavgdurationrowelement.get(i).getText(); 

				validationMessage += (rating>0) ? "" : "Services Tab:"+type+
						" - Expecting some Rating Count but was " + rating;
				validationMessage += (!name.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Name but was " + name;
				validationMessage += (!sessions.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Sessions but was " + sessions;
				validationMessage += (!upload.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Upload but was " + upload;
				validationMessage += (!download.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Download but was " + download;
				validationMessage += (users>0) ? "" : "Services Tab:"+type+
						" - Expecting some User Count but was " + users;
				validationMessage += (destination>0) ? "" : "Services Tab:"+type+
						" - Expecting some Destination Count but was " + destination;
				validationMessage += (platform>0) ? "" : "Services Tab:"+type+
						" - Expecting some Platform Count but was " + platform;
				validationMessage += (!avgduration.isEmpty()) ? "" : "Services Tab:"+type+
						" - Expecting some Average Duration but was " + avgduration;

			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getUsersHeaderDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();		
			for(int i=0;i<auditservicetabtopelement.size();i++){
				auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();
				int count = Integer.parseInt(auditservicetabtopelement.get(i).getText());
				validationMessage += (count>=0) ? "" : "Users Tab:"+type+
						" - Expecting some Service Tab Header Count but was " + count;
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getUsersTableDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String validationMessage="";

		try {
			List<Element> auditusertabtablerowelement  = ap.auditusertabtablerowelement(driver).getChildElements();
			int size = auditusertabtablerowelement.size();
			for(int i=0;i<size;i++){
				auditusertabtablerowelement  = ap.auditusertabtablerowelement(driver).getChildElements();

				List<Element> auditusertabtableratingrowelement  = ap.auditusertabtableratingrowelement(driver).getChildElements();
				List<Element> auditusertabtablenamerowelement  = ap.auditusertabtablenamerowelement(driver).getChildElements();
				List<Element> auditusertabtablesessionsrowelement  = ap.auditusertabtablesessionsrowelement(driver).getChildElements();
				List<Element> auditusertabtableuploadrowelement  = ap.auditusertabtableuploadrowelement(driver).getChildElements();
				List<Element> auditusertabtabledownloadrowelement  = ap.auditusertabtabledownloadrowelement(driver).getChildElements();
				List<Element> auditusertabtableusersrowelement  = ap.auditusertabtableusersrowelement(driver).getChildElements();
				List<Element> auditusertabtabledestinationrowelement  = ap.auditusertabtabledestinationrowelement(driver).getChildElements();

				String name = auditusertabtableratingrowelement.get(i).getText();
				int rating = Integer.parseInt(auditusertabtablenamerowelement.get(i).getText());
				String upload = auditusertabtablesessionsrowelement.get(i).getText();
				String download = auditusertabtableuploadrowelement.get(i).getText();
				int users = Integer.parseInt(auditusertabtabledownloadrowelement.get(i).getText());
				int destination = Integer.parseInt(auditusertabtableusersrowelement.get(i).getText());
				int platform = Integer.parseInt(auditusertabtabledestinationrowelement.get(i).getText());

				validationMessage += (!name.isEmpty()) ? "" : "Users Tab:"+type+
						" - Expecting some Name but was " + name;
				validationMessage += (rating>0) ? "" : "Users Tab:"+type+
						" - Expecting some Rating Count but was " + rating;
				validationMessage += (!upload.isEmpty()) ? "" : "Users Tab:"+type+
						" - Expecting some Upload but was " + upload;
				validationMessage += (!download.isEmpty()) ? "" : "Users Tab:"+type+
						" - Expecting some Download but was " + download;
				validationMessage += (users>0) ? "" : "Users Tab:"+type+
						" - Expecting some User Count but was " + users;
				validationMessage += (destination>0) ? "" : "Users Tab:"+type+
						" - Expecting some Destination Count but was " + destination;
				validationMessage += (platform>0) ? "" : "Users Tab:"+type+
						" - Expecting some Platform Count but was " + platform;

			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getDestinationsHeaderDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String validationMessage="";
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		try {
			List<Element> auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();		
			for(int i=0;i<auditservicetabtopelement.size();i++){
				auditservicetabtopelement  = ap.auditservicetabtopelement(driver).getChildElements();
				int count = Integer.parseInt(auditservicetabtopelement.get(i).getText());
				validationMessage += (count>=0) ? "" : "Destinations Tab:"+type+
						" - Expecting some Service Tab Header Count but was " + count;
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getDestinationsTableDetails(WebDriver driver,String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String validationMessage="";

		try {
			List<Element> auditlocationtabtablerowelement  = ap.auditlocationtabtablerowelement(driver).getChildElements();		
			for(int i=0;i<auditlocationtabtablerowelement.size();i++){
				auditlocationtabtablerowelement  = ap.auditlocationtabtablerowelement(driver).getChildElements();


				List<Element> auditlocationtabtableratingrowelement  = ap.auditlocationtabtableratingrowelement(driver).getChildElements();
				List<Element> auditlocationtabtablenamerowelement  = ap.auditlocationtabtablenamerowelement(driver).getChildElements();
				List<Element> auditlocationtabtablesessionsrowelement  = ap.auditlocationtabtablesessionsrowelement(driver).getChildElements();
				List<Element> auditlocationtabtableuploadrowelement  = ap.auditlocationtabtableuploadrowelement(driver).getChildElements();
				List<Element> auditlocationtabtabledownloadrowelement  = ap.auditlocationtabtabledownloadrowelement(driver).getChildElements();
				List<Element> auditlocationtabtableusersrowelement  = ap.auditlocationtabtableusersrowelement(driver).getChildElements();
				List<Element> auditlocationtabtabledestinationrowelement  = ap.auditlocationtabtabledestinationrowelement(driver).getChildElements();
				List<Element> auditlocationtabtableplatformrowelement  = ap.auditlocationtabtableplatformrowelement(driver).getChildElements();

				String city = auditlocationtabtableratingrowelement.get(i).getText();
				String country = auditlocationtabtablenamerowelement.get(i).getText();
				int services = Integer.parseInt(auditlocationtabtablesessionsrowelement.get(i).getText());
				int sessions = Integer.parseInt(auditlocationtabtableuploadrowelement.get(i).getText());
				String upload = auditlocationtabtabledownloadrowelement.get(i).getText();
				String download = auditlocationtabtableusersrowelement.get(i).getText();
				int users = Integer.parseInt(auditlocationtabtabledestinationrowelement.get(i).getText());
				int platform = Integer.parseInt(auditlocationtabtableplatformrowelement.get(i).getText());


				validationMessage += (!city.isEmpty()) ? "" : "Destinations Tab:"+type+
						" - Expecting some City but was " + city;
				validationMessage += (!country.isEmpty()) ? "" : "Destinations Tab:"+type+
						" - Expecting some Country but was " + country;
				validationMessage += (services>0) ? "" : "Destinations Tab:"+type+
						" - Expecting some Services Count but was " + services;
				validationMessage += (sessions>0) ? "" : "Destinations Tab:"+type+
						" - Expecting some Sessions Count but was " + sessions;
				validationMessage += (!upload.isEmpty()) ? "" : "Destinations Tab:"+type+
						" - Expecting some Upload but was " + upload;
				validationMessage += (!download.isEmpty()) ? "" : "Destinations Tab:"+type+
						" - Expecting some Download but was " + download;
				validationMessage += (users>0) ? "" : "Destinations Tab:"+type+
						" - Expecting some Users Count but was " + users;
				validationMessage += (platform>0) ? "" : "Destinations Tab:"+type+
						" - Expecting some Platform Count but was " + platform;



			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return validationMessage;
	}

	public String getSpiderChartDetails(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		String validationMessage="";
		try {

			Assert.assertTrue(ap.auditsummarySpiderChart(driver).isElementVisible(), "Spider Chart not visible");
			Assert.assertTrue(ap.auditsummarySpiderChartTable(driver).isElementVisible(), "Spider Chart table not visible");
			int intSpiderChartLineCount=ap.auditsummarySpiderChartLineCount(driver).getChildElements().size();
			Logger.info("Spider Chart Line Count :"+intSpiderChartLineCount);
			int intSpiderCharTableLineCount=ap.auditsummarySpiderChartTableLineCount(driver).getChildElements().size();
			Logger.info("Spider Chart Line Table Count :"+intSpiderCharTableLineCount);
			List<Element> auditsummarySpiderChartTableLineCount  = ap.auditsummarySpiderChartTableLineCount(driver).getChildElements();		
			Assert.assertEquals(intSpiderCharTableLineCount,intSpiderChartLineCount, "Table and chat row count is not matching");
			for(int i=0;i<auditsummarySpiderChartTableLineCount.size();i++){

				List<Element> auditsummarySpiderChartTableLineName  = ap.auditsummarySpiderChartTableLineName(driver).getChildElements();
				List<Element> auditsummarySpiderChartTableLineNamber  = ap.auditsummarySpiderChartTableLineNumber(driver).getChildElements();

				String Name = auditsummarySpiderChartTableLineName.get(i).getText();
				String Number = auditsummarySpiderChartTableLineNamber.get(i).getText();

				Logger.info("Risk Name : "+Name+ " | Number : " +Number);
				validationMessage += (!Name.isEmpty()) ? "" : "Destinations Tab:"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Number.isEmpty()) ? "" : "Destinations Tab:"+Number+
						" - Expecting some Number but was " + Number;
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public boolean verifySummaryPagesServicesSort(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		boolean isSorted = false; 
		try {

			List<Integer> originalList=new ArrayList<Integer>();
			List<Element> auditsummaryTopServiceCount  = ap.auditsummaryTopServiceCount(driver).getChildElements();		
			Logger.info("audit summary Top Service Count : "+auditsummaryTopServiceCount.size());
			for(int i=0;i<auditsummaryTopServiceCount.size();i++){

				String Number = ap.auditsummaryTopServiceNumber(driver,i+1).getInnerHtml().trim();
				String Name = ap.auditsummaryTopServiceName(driver,i+1).getInnerHtml().trim();
				String Risk = ap.auditsummaryTopServiceRiskScore(driver,i+1).getInnerHtml().trim();
				String UserCount = ap.auditsummaryTopServiceUserCount(driver,i+1).getInnerHtml().trim();
				Logger.info("Number : "+Number+ " | Name : " +Name + " | Risk : "+Risk+" | UserCount : "+ UserCount);
				originalList.add(Integer.parseInt(UserCount));	
			}
			Logger.info("Original List: "+originalList);

			Set<Integer> originalListAfterRemoveDuplicate = new HashSet<Integer>(originalList);
			Logger.info("Original List After Remove Duplicate Values: "+originalListAfterRemoveDuplicate);

			TreeSet<Integer> treeset=new TreeSet<Integer>(originalListAfterRemoveDuplicate);
			List<Integer> sortedlList=new ArrayList<Integer>(treeset);

			Logger.info("Sorted List using TreeSet Expected Values: "+sortedlList);
			List<Integer> originalsortedlList=new ArrayList<Integer>(originalListAfterRemoveDuplicate);
			//Logger.info("originalsortedlList: "+originalsortedlList);

			if(sortedlList.equals(originalsortedlList))
				isSorted=true;


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			return isSorted;
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return isSorted;
	}

	public void validateBarChart(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditBarChart(driver).waitForElementPresent(driver);
			ap.auditBarChart(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditBarChart(driver).isElementVisible(), "Audit page header is not visible");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditExportCSVButton(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditExportButton(driver).waitForElementPresent(driver);
			ap.auditExportButton(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditExportButton(driver).isElementVisible(), "Audit Export CSV Button is not visible");
			hardWait(20); 
			ap.auditExportButton(driver).click();
			hardWait(50); 
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateauditServiceTableHeadingActions(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditServiceTableHeadingActions(driver).waitForElementPresent(driver);
			ap.auditServiceTableHeadingActions(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditServiceTableHeadingActions(driver).isElementVisible(), "audit Service Table Heading Actions is not visible");
			Assert.assertEquals(ap.auditServiceTableHeadingActions(driver).getText(),"Actions", "Actions Lable: not matching");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateauditServiceLink(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditServiceLink(driver).waitForElementPresent(driver);
			ap.auditServiceLink(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditServiceLink(driver).isElementVisible(), "audit Service Link is not visible");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditMapChart(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditmapChart(driver).waitForElementPresent(driver);
			ap.auditmapChart(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditmapChart(driver).isElementVisible(), "Audit page header is not visible");
			Assert.assertEquals(ap.auditServiceVisibilityLable(driver).getText(),"Service Visibility:", "Service Visibility: not matching");
			ap.Configure(driver).click();
			Assert.assertEquals(ap.auditServiceVisibilityIgnoreLable(driver).getText(),"Ignore", "audit Service Visibility Ignore Lable: not matching");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateSpiderChart(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			ap.auditsummarySpiderChart(driver).waitForElementPresent(driver);
			ap.auditsummarySpiderChart(driver).waitForElementToBeVisible(driver);
			Assert.assertTrue(ap.auditsummarySpiderChart(driver).isElementVisible(), "audit summary Spider Chart is not visible");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public Integer getFielsCounts(){

		String OS = System.getProperty("os.name").toLowerCase();
		Integer Filecount=0;
		Logger.info("Operating Systen Name:"+OS);
		if(OS.contains("mac")){
			Filecount = countFiles(new File("/Users/laxman/Downloads"), Integer.valueOf(0));}
		else{
			Filecount = countFiles(new File("C:/Users/Administrator/Downloads"), Integer.valueOf(0));
		}

		return Filecount;
	}
	public Integer countFiles(File folder, Integer count) {
		File[] files = folder.listFiles();
		for (File file: files) {
			if (file.isFile()) {
				count++;
			} else {
				countFiles(file, count);
			}
		}

		return count;
	}

	public void validateAuditServicesFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");
			ap.auditFiltersServiceTab(driver).click();
			String strText=ap.auditFiltersServiceTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersServiceTab(driver).getText(),"Services", "audit Clear Filters Btn is not matching");
			List<Element> auditFiltersServiceRowCount  = ap.auditFiltersServiceRowCount(driver).getChildElements();
			int intServiceRowCount  = ap.auditFiltersServiceRowCount(driver).getChildElements().size();	
			Logger.info("intServiceRowCount : "+intServiceRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intServiceRowCount>1)
			{
				Logger.info("Service Row Count is more then 1 : " + intServiceRowCount);
				ap.auditFiltersServiceRowFirstCheckBox(driver).click();
				hardWait(50);
				String strServiceRowFirstTxt=ap.auditFiltersServiceRowFirstCheckBoxText(driver).getText();
				Logger.info("strServiceRowFirstTxt : " + strServiceRowFirstTxt);
				Logger.info("intServiceRowCount : "+intServiceRowCount);
				String strSaaSSERVICESCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSSERVICESCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSSERVICESCountAfterFilter : "+strSaaSSERVICESCountAfterFilter);
				Logger.info("strSaaSSERVICESCountInTableHeaderAfterFilter : "+strSaaSSERVICESCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSSERVICESCountInTableHeaderAfterFilter.contains(strSaaSSERVICESCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Service Row Count is Less then 1 : " + intServiceRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditCategoriesFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}

				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersCategoriesTab(driver).click();
			String strText=ap.auditFiltersCategoriesTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersCategoriesTab(driver).getText(),"Categories", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersCategoriesRowCount  = ap.auditFiltersCategoriesRowCount(driver).getChildElements();
			int intCategoriesRowCount  = ap.auditFiltersCategoriesRowCount(driver).getChildElements().size();	
			Logger.info("intCategoriesRowCount : "+intCategoriesRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intCategoriesRowCount>1)
			{
				Logger.info("Categories Row Count is more then 1 : " + intCategoriesRowCount);
				ap.auditFiltersCategoriesRowFirstCheckBox(driver).click();
				hardWait(50);
				String strCategoriesRowFirstTxt=ap.auditFiltersCategoriesRowFirstCheckBoxText(driver).getText();
				Logger.info("strCategoriesRowFirstTxt : " + strCategoriesRowFirstTxt);
				Logger.info("intCategoriesRowCount : "+intCategoriesRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Categories Row Count is Less then 1 : " + intCategoriesRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateAuditDefaultTagsFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}

				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			hardWait(10);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersDefaultTagsTab(driver).click();
			String strText=ap.auditFiltersDefaultTagsTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersDefaultTagsTab(driver).getText(),"Default Tags", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersDefaultRowCount  = ap.auditFiltersDefaultTagsRowCount(driver).getChildElements();
			int intDefaultTagsRowCount  = ap.auditFiltersDefaultTagsRowCount(driver).getChildElements().size();	
			Logger.info("intDefaultTagsRowCount : "+intDefaultTagsRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intDefaultTagsRowCount>1)
			{
				Logger.info("Default Tags Row Count is more then 1 : " + intDefaultTagsRowCount);
				ap.auditFiltersDefaultTagsRowFirstCheckBox(driver).click();
				hardWait(50);
				String strDefaultTagsRowFirstTxt=ap.auditFiltersDefaulTagsRowFirstCheckBoxText(driver).getText();
				Logger.info("strDefaultTagsRowFirstTxt : " + strDefaultTagsRowFirstTxt);
				Logger.info("intDefaultTagsRowCount : "+intDefaultTagsRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Default Tags Row Count is Less then 1 : " + intDefaultTagsRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateAuditCustomTagsFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersCustomTagsTab(driver).click();
			String strText=ap.auditFiltersCustomTagsTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersCustomTagsTab(driver).getText(),"Custom Tags", "audit Clear Services Btn is not matching");
			List<Element> auditFilterCustomTagsRowCount  = ap.auditFiltersCustomTagsRowCount(driver).getChildElements();
			int intCustomTagsRowCount  = ap.auditFiltersCustomTagsRowCount(driver).getChildElements().size();	
			Logger.info("intCustomTagssRowCount : "+intCustomTagsRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intCustomTagsRowCount>1)
			{
				Logger.info("Custom Tags Row Count is more then 1 : " + intCustomTagsRowCount);
				ap.auditFiltersCustomTagsRowFirstCheckBox(driver).click();
				hardWait(50);
				String strCustomTagsRowFirstTxt=ap.auditFiltersCustomTagsRowFirstCheckBoxText(driver).getText();
				Logger.info("strCustomTagsRowFirstTxt : " + strCustomTagsRowFirstTxt);
				Logger.info("intCustomTagsRowCount : "+intCustomTagsRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Custom Tags Row Count is Less then 1 : " + intCustomTagsRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}




	public void validateAuditRiskTabFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersRiskTab(driver).click();
			String strText=ap.auditFiltersRiskTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersRiskTab(driver).getText(),"Risk", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersRiskRowCount  = ap.auditRiskTabRowCount(driver).getChildElements();
			int intRiskTabRowCount  = ap.auditRiskTabRowCount(driver).getChildElements().size();	
			Logger.info("intRiskRowCount : "+intRiskTabRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intRiskTabRowCount>1)
			{
				Logger.info("Risk Row Count is more then 1 : " + intRiskTabRowCount);
				ap.auditFiltersRiskTabFirstCheckBox(driver).click();
				hardWait(50);
				String strRiskRowFirstTxt=ap.auditFiltersRiskTabFirstCheckBoxText(driver).getText();
				Logger.info("strRiskRowFirstTxt : " + strRiskRowFirstTxt);
				Logger.info("intRiskRowCount : "+intRiskTabRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Risk Row Count is Less then 1 : " + intRiskTabRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}



	public void validateAuditUsersTabFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersUsersTab(driver).click();
			String strText=ap.auditFiltersUsersTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersUsersTab(driver).getText(),"Users", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersUsersRowCount  = ap.auditUsersTabRowCount(driver).getChildElements();
			int intUsersTabRowCount  = ap.auditUsersTabRowCount(driver).getChildElements().size();	
			Logger.info("intUsersTabRowCount : "+intUsersTabRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intUsersTabRowCount>1)
			{
				Logger.info("Users Tab Row Count is more then 1 : " + intUsersTabRowCount);
				ap.auditFiltersUsersTabFirstCheckBox(driver).click();
				hardWait(50);
				String strUsersRowFirstTxt=ap.auditFiltersUsersTabFirstCheckBoxText(driver).getText();
				Logger.info("strUsersRowFirstTxt : " + strUsersRowFirstTxt);
				Logger.info("intUsersRowCount : "+intUsersTabRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Users Row Count is Less then 1 : " + intUsersTabRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditCountriesTabFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");


			ap.auditFiltersDropDown(driver).click();
			ap.auditFiltersCountriesTab(driver).click();
			String strText=ap.auditFiltersCountriesTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersCountriesTab(driver).getText(),"Countries", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersCountriesRowCount  = ap.auditCountriesTabRowCount(driver).getChildElements();
			int intCountriesTabRowCount  = ap.auditCountriesTabRowCount(driver).getChildElements().size();	
			Logger.info("intCountriesTabRowCount : "+intCountriesTabRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intCountriesTabRowCount>1)
			{
				Logger.info("Countries Tab Row Count is more then 1 : " + intCountriesTabRowCount);
				ap.auditFiltersCountriesTabFirstCheckBox(driver).click();
				hardWait(50);
				String strCountriesRowFirstTxt=ap.auditFiltersCountriesTabFirstCheckBoxText(driver).getText();
				Logger.info("strCountriesRowFirstTxt : " + strCountriesRowFirstTxt);
				Logger.info("intCountriesRowCount : "+intCountriesTabRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Countries Row Count is Less then 1 : " + intCountriesTabRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateAuditCitiesTabFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersCitiesTab(driver).click();
			String strText=ap.auditFiltersCitiesTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersCitiesTab(driver).getText(),"Cities", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersServicesRowCount  = ap.auditCitiesTabRowCount(driver).getChildElements();
			int intCitiesTabRowCount  = ap.auditCitiesTabRowCount(driver).getChildElements().size();	
			Logger.info("intCitiesTabRowCount : "+intCitiesTabRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intCitiesTabRowCount>1)
			{
				Logger.info("Cities Tab Row Count is more then 1 : " + intCitiesTabRowCount);
				ap.auditFiltersCitiesTabFirstCheckBox(driver).click();
				hardWait(50);
				String strCitiesRowFirstTxt=ap.auditFiltersCitiesTabFirstCheckBoxText(driver).getText();
				Logger.info("strCitiesRowFirstTxt : " + strCitiesRowFirstTxt);
				Logger.info("intCitiesRowCount : "+intCitiesTabRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Cities Row Count is Less then 1 : " + intCitiesTabRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditPlatformsabFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");

			ap.auditFiltersPlatformsTab(driver).click();
			String strText=ap.auditFiltersPlatformsTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersPlatformsTab(driver).getText(),"Platforms", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersServicesRowCount  = ap.auditPlatformsRowCount(driver).getChildElements();
			int intPlatformsTabRowCount  = ap.auditPlatformsRowCount(driver).getChildElements().size();	
			Logger.info("intPlatformsTabRowCount : "+intPlatformsTabRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intPlatformsTabRowCount>1)
			{
				Logger.info("Platforms Tab Row Count is more then 1 : " + intPlatformsTabRowCount);
				ap.auditFiltersPlatformsTabFirstCheckBox(driver).click();
				hardWait(50);
				String strPlatformsRowFirstTxt=ap.auditFiltersPlatformsTabFirstCheckBoxText(driver).getText();
				Logger.info("strPlatformsRowFirstTxt : " + strPlatformsRowFirstTxt);
				Logger.info("intPlatformsRowCount : "+intPlatformsTabRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Platforms Row Count is Less then 1 : " + intPlatformsTabRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateAuditFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			if(ap.auditClearFiltersBtn(driver).isElementVisible())
			{
				String status = ap.auditClearFilters(driver).getInnerHtml();
				Logger.info("status : "+status);
				String checkString="disabled=\"disabled\"";
				Logger.info("checkString : "+checkString);
				if(status.contains("disabled=\"disabled\""))
				{
					ap.auditFiltersSelectTag(driver).click();
				}else
				{
					ap.auditClearFiltersBtn(driver).click();
					ap.auditFiltersSelectTag(driver).click();
				}
				hardWait(20);
			}

			ap.auditFiltersTag(driver).waitForElementPresent(driver);
			ap.auditFiltersTag(driver).waitForElementToBeVisible(driver);
			ap.auditFiltersTag(driver).click(); hardWait(20);
			Assert.assertTrue(ap.auditClearFiltersBtn(driver).isElementVisible(), "audit Clear Filters Btn is not visible");
			Assert.assertEquals(ap.auditClearFiltersValue(driver).getText(),"Clear Filters", "audit Clear Filters Btn is not matching");
			ap.auditFiltersCategoriesTab(driver).click();
			String strText=ap.auditFiltersCategoriesTab(driver).getText();
			Assert.assertEquals(ap.auditFiltersCategoriesTab(driver).getText(),"Categories", "audit Clear Services Btn is not matching");
			List<Element> auditFiltersServicesRowCount  = ap.auditFiltersCategoriesRowCount(driver).getChildElements();
			int intCategoriesRowCount  = ap.auditFiltersServiceRowCount(driver).getChildElements().size();	
			Logger.info("intCategoriesRowCount : "+intCategoriesRowCount);
			String strSaaSSERVICESCount=ap.auditSaaSSERVICESCount(driver).getText();
			String strSaaSSERVICESCountInTableHeader=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
			Logger.info("strSaaSSERVICESCount : "+strSaaSSERVICESCount);
			Logger.info("strSaaSSERVICESCountInTableHeader : "+strSaaSSERVICESCountInTableHeader);
			Assert.assertTrue(strSaaSSERVICESCountInTableHeader.contains(strSaaSSERVICESCount), "Audit page header count is missmatch");
			if (intCategoriesRowCount>1)
			{
				Logger.info("Categories Row Count is more then 1 : " + intCategoriesRowCount);
				ap.auditFiltersCategoriesRowFirstCheckBox(driver).click();
				hardWait(50);
				String strCategoriesRowFirstTxt=ap.auditFiltersCategoriesRowFirstCheckBoxText(driver).getText();
				Logger.info("strCategoriesRowFirstTxt : " + strCategoriesRowFirstTxt);
				Logger.info("intCategoriesRowCount : "+intCategoriesRowCount);
				String strSaaSCategoriesCountAfterFilter=ap.auditSaaSSERVICESCount(driver).getText();
				String strSaaSCategoriesCountInTableHeaderAfterFilter=ap.auditSaaSSERVICESCountInTableHeader(driver).getText();
				Logger.info("strSaaSCategoriesCountAfterFilter : "+strSaaSCategoriesCountAfterFilter);
				Logger.info("strSaaSCategoriesCountInTableHeaderAfterFilter : "+strSaaSCategoriesCountInTableHeaderAfterFilter);
				Assert.assertTrue(strSaaSCategoriesCountInTableHeaderAfterFilter.contains(strSaaSCategoriesCountAfterFilter), "Audit page header count after filter is missmatch");
			}else
			{
				Logger.info("Service Row Count is Less then 1 : " + intCategoriesRowCount);
			}

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void validateUserCanAddComments(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

			ap.auditServicesTableActionSelect(driver).click();
			hardWait(10);
			ap.auditServicesTableActionComments(driver).click();
			hardWait(10);
			Date date = new Date();
			String strRowSubject="FE Automation Comments : "+date.toString();
			System.out.println(date.toString());
			ap.auditServicesTableCommentsTextBox(driver).type(strRowSubject);
			ap.auditServicesTableAddCommentBtn(driver).click();
			hardWait(10);
			String strFirstRowSubject=ap.auditServicesTableAddCommentSubject(driver).getText();
			Logger.info("strFirstRowSubject : "+strFirstRowSubject);
			Assert.assertEquals(strRowSubject.trim(),strFirstRowSubject.trim(), "audit Services Table AddComment is not matching");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateServiceTabSearchUserBox(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		String srtServicesTableFirstServicesName =ap.auditServicesTableFirstServicesName(driver).getText();
		Logger.info("srtServicesTableFirstServicesName : "+srtServicesTableFirstServicesName);
		ap.auditServicesTableFirstServicesName(driver).mouseOverClick(driver);
		hardWait(30);
		ap.auditServicesServiceDetailsTab(driver).click();
		hardWait(30);
		String srtauditServicesServiceDetailsH1 =ap.auditServicesServiceDetailsH1(driver).getText();
		Logger.info("srtauditServicesServiceDetailsH1 : "+srtauditServicesServiceDetailsH1);
		Assert.assertEquals(srtServicesTableFirstServicesName.trim(),srtauditServicesServiceDetailsH1.trim(), "audit Services Name is not matching in header");
		String srtServicesServiceDetailsTableFirstUserName =ap.auditServicesServiceDetailsTableFirstUserName(driver).getText();
		Logger.info("srtServicesServiceDetailsTableFirstUserName : "+srtServicesServiceDetailsTableFirstUserName);
		String srtauditServicesServiceDetailsH2 =ap.auditServicesServiceDetailsH2(driver).getText();
		Logger.info("srtauditServicesServiceDetailsH2 : "+srtauditServicesServiceDetailsH2);
		Assert.assertTrue(srtauditServicesServiceDetailsH2.contains(srtServicesTableFirstServicesName), "Audit page header count after filter is missmatch");
		ap.auditServicesServiceDetailsSearchBox(driver).type(srtServicesServiceDetailsTableFirstUserName);
		ap.auditServicesServiceDetailsTableFirstUserNameSelect(driver).click();
		hardWait(10);
		String srtAfterSearchServicesServiceDetailsTableFirstUserName =ap.auditServicesServiceDetailsTableFirstUserName(driver).getText();
		Logger.info("srtAfterSearchServicesServiceDetailsTableFirstUserName : "+srtAfterSearchServicesServiceDetailsTableFirstUserName);
		Assert.assertEquals(srtServicesServiceDetailsTableFirstUserName.trim(),srtAfterSearchServicesServiceDetailsTableFirstUserName.trim(), "audit Services Name is not matching");

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void testSettingsTag(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			hardWait(10);
			Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
			ap.clickAdminDCIAdmin(driver).click();
			Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
			ap.clickSettings(driver).click();
			Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
			ap.clickTags(driver).click();
			Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
			Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");

		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void validateSettingsAddTag(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			hardWait(10);
			Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
			ap.clickAdminDCIAdmin(driver).click();
			Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
			ap.clickSettings(driver).click();
			Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
			ap.clickTags(driver).click();
			Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
			Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");
			ap.SettingsNewTag(driver).click();
			Date date = new Date();
			Random rand = new Random(); 
			int a = rand.nextInt(500) + 1;
			String strRowSubject="FE Automation Tag  "+a;
			System.out.println(strRowSubject);
			ap.SettingsTagText(driver).type(strRowSubject);
			ap.SettingsSaveTag(driver).click();
			Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
			String srtmessageBox =ap.messageBox(driver).getText();
			Logger.info("messageBox : "+srtmessageBox);
			Assert.assertEquals(srtmessageBox.trim(),"Successfully added the tag.", "Successfully added the tag.is not matching");
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void validateSettingsTagCreated_LastModified(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			hardWait(10);
			Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
			ap.clickAdminDCIAdmin(driver).click();
			Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
			ap.clickSettings(driver).click();
			Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
			ap.clickTags(driver).click();
			Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
			Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");
			ap.SettingsNewTag(driver).click();
			Date date = new Date();
			Random rand = new Random(); 
			int a = rand.nextInt(500) + 1;
			String strRowSubject="FE"+a;
			System.out.println(strRowSubject);
			ap.SettingsTagText(driver).type(strRowSubject+"\n");
			ap.SettingsSaveTag(driver).click();
			Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
			String srtmessageBox =ap.messageBox(driver).getText();
			Logger.info("messageBox : "+srtmessageBox);
			Assert.assertEquals(srtmessageBox.trim(),"Successfully added the tag.", "Successfully added the tag.is not matching");
			String srtFirstTagName =ap.SettingsTagFirstName(driver).getText();
			Logger.info("srtFirstTagName : "+srtFirstTagName);
			Assert.assertEquals(srtFirstTagName.trim(),strRowSubject.trim(), "Successfully added the tag.is not matching");
			String srtSettingsTagFirstCreated =ap.SettingsTagFirstCreated(driver).getText();
			Logger.info("srtSettingsTagFirstCreated : "+srtSettingsTagFirstCreated);
			String srtSettingsTagFirstModify =ap.SettingsTagFirstModify(driver).getText();
			Logger.info("srtSettingsTagFirstModify : "+srtSettingsTagFirstModify);
			String validationTagFirstCreated ="";
			validationTagFirstCreated += (!srtSettingsTagFirstCreated.isEmpty()) ? "" : "error in srtSettingsTagFirstCreated";
			Assert.assertEquals(validationTagFirstCreated, "", "Output Response Validation "+validationTagFirstCreated);
			String validationTagFirstModify ="";
			validationTagFirstModify += (!srtSettingsTagFirstModify.isEmpty()) ? "" : "error in srtSettingsTagFirstModify";
			Assert.assertEquals(validationTagFirstModify, "", "Output Response Validation "+validationTagFirstModify);
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void validateSettingsTagCountIncreasedByOne(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			hardWait(10);
			Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
			ap.clickAdminDCIAdmin(driver).click();
			Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
			ap.clickSettings(driver).click();
			Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
			ap.clickTags(driver).click();
			Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
			Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");
			int intBeforeCount;
			if(ap.TagCounntNumber(driver).isElementVisible()){
				String BeforeTagCount =ap.TagCounntNumber(driver).getText();
				BeforeTagCount = BeforeTagCount.replace("t", "");
				BeforeTagCount = BeforeTagCount.replace("a", "");
				BeforeTagCount = BeforeTagCount.replace("g", "");
				BeforeTagCount = BeforeTagCount.replace("s", "");
				System.out.println("BeforeTagCount : "+BeforeTagCount);
				intBeforeCount = Integer.parseInt(BeforeTagCount.trim());
			}
			else
			{ 	
				intBeforeCount = 0;
				System.out.println("BeforeTagCount : "+intBeforeCount);
			}
			ap.SettingsNewTag(driver).click();
			Date date = new Date();
			Random rand = new Random(); 
			int a = rand.nextInt(500) + 1;
			String strRowSubject="FE"+a;
			System.out.println(strRowSubject);
			ap.SettingsTagText(driver).type(strRowSubject);
			ap.SettingsSaveTag(driver).click();
			Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
			String srtmessageBox =ap.messageBox(driver).getText();
			Logger.info("messageBox : "+srtmessageBox);
			Assert.assertEquals(srtmessageBox.trim(),"Successfully added the tag.", "Successfully added the tag.is not matching");
			String srtFirstTagName =ap.SettingsTagFirstName(driver).getText();
			Logger.info("srtFirstTagName : "+srtFirstTagName);
			Assert.assertEquals(srtFirstTagName.trim(),strRowSubject.trim(), "Successfully added the tag.is not matching");
			String srtSettingsTagFirstCreated =ap.SettingsTagFirstCreated(driver).getText();
			Logger.info("srtSettingsTagFirstCreated : "+srtSettingsTagFirstCreated);
			String srtSettingsTagFirstModify =ap.SettingsTagFirstModify(driver).getText();
			Logger.info("srtSettingsTagFirstModify : "+srtSettingsTagFirstModify);
			String validationTagFirstCreated ="";
			validationTagFirstCreated += (!srtSettingsTagFirstCreated.isEmpty()) ? "" : "error in srtSettingsTagFirstCreated";
			Assert.assertEquals(validationTagFirstCreated, "", "Output Response Validation "+validationTagFirstCreated);
			String validationTagFirstModify ="";
			validationTagFirstModify += (!srtSettingsTagFirstModify.isEmpty()) ? "" : "error in srtSettingsTagFirstModify";
			Assert.assertEquals(validationTagFirstModify, "", "Output Response Validation "+validationTagFirstModify);
			String AfterTagCount =ap.TagCounntNumber(driver).getText();
			AfterTagCount = AfterTagCount.replace("tags", "");
			System.out.println("AfterTagCount : " + AfterTagCount);
			int intAfterCount = Integer.parseInt(AfterTagCount.trim());
			Assert.assertEquals(intBeforeCount+1,intAfterCount, "Successfully Tag is not Incremented By 1");
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public void validateSettingsEditAddedTag(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		try {
			AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
			hardWait(10);
			Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
			ap.clickAdminDCIAdmin(driver).click();
			Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
			ap.clickSettings(driver).click();
			Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
			ap.clickTags(driver).click();
			Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
			Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");
			int intBeforeCount;
			if(ap.TagCounntNumber(driver).isElementVisible()){
				String BeforeTagCount =ap.TagCounntNumber(driver).getText();
				BeforeTagCount = BeforeTagCount.replace("t", "");
				BeforeTagCount = BeforeTagCount.replace("a", "");
				BeforeTagCount = BeforeTagCount.replace("g", "");
				BeforeTagCount = BeforeTagCount.replace("s", "");
				System.out.println("BeforeTagCount : "+BeforeTagCount);
				intBeforeCount = Integer.parseInt(BeforeTagCount.trim());
			}
			else
			{ 	
				intBeforeCount = 0;
				System.out.println("BeforeTagCount : "+intBeforeCount);
			}
			ap.SettingsNewTag(driver).click();
			String strRowSubject=((intBeforeCount+1)+"Auto");
			System.out.println("strRowSubject "+ strRowSubject);
			ap.SettingsTagText(driver).type(strRowSubject);
			ap.SettingsSaveTag(driver).click();
			Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
			String srtmessageBox =ap.messageBox(driver).getText();
			Logger.info("messageBox : "+srtmessageBox);
			Assert.assertEquals(srtmessageBox.trim(),"Successfully added the tag.", "Successfully added the tag.is not matching");
			String srtFirstTagName =ap.SettingsTagFirstName(driver).getText();
			Logger.info("srtFirstTagName : "+srtFirstTagName);
			Assert.assertEquals(srtFirstTagName.trim(),strRowSubject.trim(), "Successfully added the tag.is not matching");
			String srtSettingsTagFirstCreated =ap.SettingsTagFirstCreated(driver).getText();
			Logger.info("srtSettingsTagFirstCreated : "+srtSettingsTagFirstCreated);
			String srtSettingsTagFirstModify =ap.SettingsTagFirstModify(driver).getText();
			Logger.info("srtSettingsTagFirstModify : "+srtSettingsTagFirstModify);
			String validationTagFirstCreated ="";
			validationTagFirstCreated += (!srtSettingsTagFirstCreated.isEmpty()) ? "" : "error in srtSettingsTagFirstCreated";
			Assert.assertEquals(validationTagFirstCreated, "", "Output Response Validation "+validationTagFirstCreated);
			String validationTagFirstModify ="";
			validationTagFirstModify += (!srtSettingsTagFirstModify.isEmpty()) ? "" : "error in srtSettingsTagFirstModify";
			Assert.assertEquals(validationTagFirstModify, "", "Output Response Validation "+validationTagFirstModify);
			String AfterTagCount =ap.TagCounntNumber(driver).getText();
			AfterTagCount = AfterTagCount.replace("tags", "");
			AfterTagCount = AfterTagCount.replace("t", "");
			AfterTagCount = AfterTagCount.replace("a", "");
			AfterTagCount = AfterTagCount.replace("g", "");
			AfterTagCount = AfterTagCount.replace("s", "");
			System.out.println("AfterTagCount : " + AfterTagCount);
			int intAfterCount = Integer.parseInt(AfterTagCount.trim());
			Assert.assertEquals(intBeforeCount+1,intAfterCount, "Successfully Tag is not Incremented By 1");
			ap.SelectDropDownTabClicked(driver).click();
			hardWait(5);
			ap.EditTabClicked(driver).click();
			String GetLabelData =ap.BeforeEditLabel(driver).getText();
			System.out.println("BeforeLabelText : " + GetLabelData);
			String AfterEditLabelText ="Edit"+GetLabelData;
			System.out.println("AfterLabelTextEdited : " + AfterEditLabelText);
			ap.EditLabelBox(driver).type(AfterEditLabelText);
			System.out.println("AfterLabelTextEdited : " + AfterEditLabelText);
			ap.SettingsSaveTag(driver).click();
			Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
			String srtEditmessageBox =ap.messageBox(driver).getText();
			Logger.info("srtEditmessageBox : "+srtEditmessageBox);
			Assert.assertEquals(srtEditmessageBox.trim(),"Successfully edited the tag.", "Successfully edited the tag is not matching");
		} catch (Exception ex) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+ex.toString());
			Assert.fail("validateBarChart : " + ex.getLocalizedMessage());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void validateTagColumnIsSortByAsendingAndDesending(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);
		hardWait(10);
		Assert.assertTrue(ap.clickAdminDCIAdmin(driver).isElementVisible(), "audit clickAdminDCIAdmin Btn is not visible");
		ap.clickAdminDCIAdmin(driver).click();
		Assert.assertTrue(ap.clickSettings(driver).isElementVisible(), "audit clickSettings Btn is not visible");
		ap.clickSettings(driver).click();
		Assert.assertTrue(ap.clickTags(driver).isElementVisible(), "audit clickTags Btn is not visible");
		ap.clickTags(driver).click();
		Assert.assertTrue(ap.SettingsNewTag(driver).isElementVisible(), "audit clickTags Btn is not visible");
		Assert.assertEquals(ap.SettingsNewTag(driver).getText(),"New Tag", "New Tag is not matching");
		int intBeforeCount;
		if(ap.TagCounntNumber(driver).isElementVisible()){
			String BeforeTagCount =ap.TagCounntNumber(driver).getText();
			BeforeTagCount = BeforeTagCount.replaceAll("tag.*", "").trim();
			System.out.println("BeforeTagCount : "+BeforeTagCount);
			intBeforeCount = Integer.parseInt(BeforeTagCount.trim());
		}
		else
		{ 	
			intBeforeCount = 0;
			System.out.println("BeforeTagCount : "+intBeforeCount);
		}
		ap.SettingsNewTag(driver).click();
		Random rand = new Random(); 
		int a = rand.nextInt(500) + 1;
		String strRowSubject="FE"+a;
		System.out.println(strRowSubject);
		ap.SettingsTagText(driver).type(strRowSubject);
		ap.SettingsSaveTag(driver).click();
		Assert.assertTrue(ap.messageBox(driver).isElementVisible(), "audit messageBox is not visible");
		String srtmessageBox =ap.messageBox(driver).getText();
		Logger.info("messageBox : "+srtmessageBox);
		Assert.assertEquals(srtmessageBox.trim(),"Successfully added the tag.", "Successfully added the tag.is not matching");
		String srtFirstTagName =ap.SettingsTagFirstName(driver).getText();
		Logger.info("srtFirstTagName : "+srtFirstTagName);
		Assert.assertEquals(srtFirstTagName.trim(),strRowSubject.trim(), "Successfully added the tag.is not matching");
		String srtSettingsTagFirstCreated =ap.SettingsTagFirstCreated(driver).getText();
		Logger.info("srtSettingsTagFirstCreated : "+srtSettingsTagFirstCreated);
		String srtSettingsTagFirstModify =ap.SettingsTagFirstModify(driver).getText();
		Logger.info("srtSettingsTagFirstModify : "+srtSettingsTagFirstModify);
		String validationTagFirstCreated ="";
		validationTagFirstCreated += (!srtSettingsTagFirstCreated.isEmpty()) ? "" : "error in srtSettingsTagFirstCreated";
		Assert.assertEquals(validationTagFirstCreated, "", "Output Response Validation "+validationTagFirstCreated);
		String validationTagFirstModify ="";
		validationTagFirstModify += (!srtSettingsTagFirstModify.isEmpty()) ? "" : "error in srtSettingsTagFirstModify";
		Assert.assertEquals(validationTagFirstModify, "", "Output Response Validation "+validationTagFirstModify);
		String AfterTagCount =ap.TagCounntNumber(driver).getText();
		AfterTagCount = AfterTagCount.replaceAll("tag.*", "").trim();
		System.out.println("AfterTagCount : " + AfterTagCount);
		int intAfterCount = Integer.parseInt(AfterTagCount.trim());
		Assert.assertEquals(intBeforeCount+1,intAfterCount, "Successfully Tag is not Incremented By 1");

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	
}


