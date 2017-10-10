package com.elastica.action.investigate;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.action.Action;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.investigate.InvestigatePage;
import com.elastica.webelements.Element;

/**
 * Investigate common actions
 * @author eldorajan
 *
 */
public class InvestigateAction extends Action{

	public String getDateDropdownButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String text="";
		try {
			text = ip.dateDropdownButton(driver).getText().trim();
			Logger.info("****Actual Date Dropdown Button Text:"+text+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return text;
	}


	public String getDateTextBoxText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String text="";
		try {
			text = ip.dateTextbox(driver).getAttribute("value");
			Logger.info("****Actual Date Textbox Text:"+text+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return text;
	}

	public int getTableCountActivities(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		int count=0;
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		try {
			List<Element> activityList  = ip.activityElement(driver).getChildElements();		
			count = activityList.size();
			Logger.info("****No of activities in investigate:"+count+"****");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}

	public String getDateTextBoxTextDifference(String dateText){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";
		try {
			String[] date = dateText.split(" - ");
			int dateDifference=(int)getTimeDifference(date[1], date[0], "MMM dd, yyyy","GMT-7")/(24*60*60*365);

			Logger.info("****Expected Date Textbox Difference: 3 years****");
			Logger.info("****Actual Date Textbox Difference:"+dateDifference+" years****");

			message += (dateDifference==3) ? "": 
				"Datebox Text Difference Expected:3 and Actual:"+dateDifference;
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public void clickFilter(WebDriver driver) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		ip.filterButton(driver).waitForLoading(driver);
		ip.filterButton(driver).click();
	}

	public boolean verifyFilterServiceTab(WebDriver driver, String type,  String value) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		if (ip.serviceAndUserFiltertab(driver, type).isElementVisible()) {
			ip.serviceAndUserFiltertab(driver, type).click();
		}
		if (ip.serviceanduserfilterdatatab(driver, value).isElementPresent(driver)) {
			return true;
		}
		return false;
	}


	public void choiceApp(WebDriver driver, String value) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		//hardWait(5);
		ip.selectApp(driver, value).isElementPresent(driver);
		ip.selectApp(driver, value).click();
		hardWait(10); // Loading same page, this wait is needed
	}

	public void checkGateway(WebDriver driver) throws InterruptedException{
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		Thread.sleep(5000);
		ip.gatewayCheckbox(driver).waitForLoading(driver);
		ip.gatewayCheckbox(driver).click();
	}

	public void selectObjects(WebDriver driver, List<String> objectList) throws InterruptedException{
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		//ip.objectList(driver).waitForLoading(driver);
		//ip.objectList(driver).click();
		for(int i=0;i<objectList.size();i++){
			Thread.sleep(5000);
			ip.filterValues(driver, objectList.get(i)).click();
		}
	}

	public void selectActive(WebDriver driver, String activeName) throws InterruptedException{
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		Thread.sleep(8000);
		ip.filterItem(driver, activeName).click();
	}

	public void selectSeverity(WebDriver driver, String activeName) throws InterruptedException{
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		Thread.sleep(5000);
		ip.filterItem(driver, activeName).click();
	}

	public void selectActivitiess(WebDriver driver, List<String> activityList){
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		ip.activityList(driver).waitForLoading(driver);
		ip.activityList(driver).click();
		for(int i=0;i>activityList.size();i++){
			ip.filterValues(driver, activityList.get(i));
		}
	}

	public int getLogCount(WebDriver driver, String value, String application) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		ip.dateDropdownButton(driver).isElementPresent(driver);
		ip.dateDropdownButton(driver).click();
		hardWait(5);// Loading same page, this wait is needed
		ip.selectActivityRange(driver, value).click();  // Select Duration 1: for 1 Day; 2: for Week
		hardWait(5);// Loading same page, this wait is needed
		int count = Integer.valueOf(ip.logCount(driver, application).getText());
		Logger.info("Count is " + count);
		return count;
	}


	public int getGWLogCount(WebDriver driver, String application) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		int count = Integer.valueOf(ip.downFilterServicecount(driver, application).getText());
		Logger.info("Count is " + count);
		return count;
	}

	public int getServiceNameCount(WebDriver driver, String application) {
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		int count =  ip.logTableServiceName(driver, application).getElementList().size();
		Logger.info("Service GW Count is " + count);
		return count;
	}

	public void gotoInvestigatePage(WebDriver driver){
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		ip.investigateIcon(driver).waitForLoading(driver);
		ip.investigateIcon(driver).click();
	}

	public String clickActivityLog(WebDriver driver, String message){
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		List<Element> activityList  = ip.activityElement(driver).getChildElements();
		String investigateLog = null;
		int count = activityList.size();
		Reporter.log("Expected Message: "+message, true);
		Reporter.log("-----------------------------------------------------------");
		for(int i=0;i<count;i++){
			investigateLog = ip.activityElementMessage(driver, i+1).getText();
			Reporter.log(i+1+". Investigate Log: "+investigateLog, true);
			if(message.equals(investigateLog)){
				//ip.activityElementMessage(driver, i+1).click();
				System.out.println("Investigate Log: "+investigateLog);
				break;
			}
		}
		Reporter.log("-----------------------------------------------------------");
		return investigateLog;
	}


	public String getAllActivitiesLogsText(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
		return ip.activityLogCount(driver).getText().trim();
	}

	public void testShowing_50_of(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		hardWait(10); 
		String strHeaderString = ip.investigateFoterText(driver).getText();
		Logger.info("strHeaderString   : "+ strHeaderString);
		String s = strHeaderString;
		s = s.substring(s.indexOf(" ") + 1);
		s = s.substring(0, s.indexOf(" "));
		System.out.println("Showing - "+s);
		if(Integer.parseInt(s)>=50){
			Assert.assertTrue(strHeaderString.contains("Showing 50 of"), "Header string not missmatch #" + strHeaderString);
		}else
		{ 
			Assert.assertTrue(strHeaderString.contains("Showing "+s+" of "+s), "Header string not missmatch #" + strHeaderString);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public String verifyInvestigateFilterOnService(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeviceTabs(driver).click();
			Assert.assertEquals(ip.investigateSelectSevice(driver).getText().trim(),"Service", "Service title is not matching");
			ip.investigateSelectSevice(driver).click();
			ip.investigateSeviceFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateSeviceName=ip.investigateSeviceFirstSeviceName(driver).getText().trim();
			String strInvestigateSeviceCount=ip.investigateSeviceFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceName   : "+ strInvestigateSeviceName);
			Logger.info("strInvestigateSeviceCount   : "+ strInvestigateSeviceCount);
			String strInvestigateSeviceMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceMatchLogCount   : "+ strInvestigateSeviceMatchLogCount);
			String strInvestigateSeviceMatchLogCountAfterRemove =strInvestigateSeviceMatchLogCount.replace(",", "");
			Logger.info("strInvestigateSeviceMatchLogCountAfterRemove   : "+ strInvestigateSeviceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateSeviceCount), "Foter count after filter is not missmatch");
			Assert.assertTrue(strInvestigateSeviceMatchLogCountAfterRemove.contains(strInvestigateSeviceCount), "Header count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateSeviceCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {

			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyInvestigateFilterOnInstance(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeviceTabs(driver).click();
			ip.investigateSelectInstance(driver).click();
			Assert.assertEquals(ip.investigateSeviceTabs(driver).getText().trim(),"Instance", "Instance title is not matching");
			ip.investigateInstanceFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateInstanceName=ip.investigateInstanceFirstSeviceName(driver).getText().trim();
			String strInvestigateInstanceCount=ip.investigateInstanceFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceName   : "+ strInvestigateInstanceName);
			Logger.info("strInvestigateSeviceCount   : "+ strInvestigateInstanceCount);
			String strInvestigateInstanceMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateInstanceMatchLogCount   : "+ strInvestigateInstanceMatchLogCount);
			String strInvestigateInstanceMatchLogCountAfterRemove =strInvestigateInstanceMatchLogCount.replace(",", "");
			Logger.info("strInvestigateInstanceMatchLogCountAfterRemove   : "+ strInvestigateInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strInvestigateInstanceMatchLogCountAfterRemove.contains(strInvestigateInstanceCount), "Instance Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateInstanceCount), "Instance Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateInstanceCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyInvestigateFilterOnUser(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateUserTabs(driver).click();
			ip.investigateSelectUser(driver).click();
			Assert.assertEquals(ip.investigateUserTabs(driver).getText().trim(),"User", "Instance title is not matching");
			ip.investigateUserFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateUserName=ip.investigateUserFirstSeviceName(driver).getText().trim();
			String strInvestigateUserCount=ip.investigateUserFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateUserName   : "+ strInvestigateUserName);
			Logger.info("strInvestigateUserCount   : "+ strInvestigateUserCount);
			String strInvestigateUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateUserMatchLogCount   : "+ strInvestigateUserMatchLogCount);
			String strInvestigateInstanceMatchLogCountAfterRemove =strInvestigateUserMatchLogCount.replace(",", "");
			Logger.info("strInvestigateUserMatchLogCountAfterRemove   : "+ strInvestigateInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strInvestigateInstanceMatchLogCountAfterRemove.contains(strInvestigateUserCount), "User Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateUserCount), "User Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateUserCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyInvestigateFilterOnAccountType(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateUserTabs(driver).click();
			ip.investigateSelectAccontType(driver).click();
			Assert.assertEquals(ip.investigateUserTabs(driver).getText().trim(),"Account Type", "Instance title is not matching");
			ip.investigateAccountTypeFirstCheckBox(driver).click();
			hardWait(10);
			String strAccountTypeUserName=ip.investigateAccountTypeFirstSeviceName(driver).getText().trim();
			String strAccountTypeCount=ip.investigateAccountTypeFirstSeviceCount(driver).getText().trim();
			Logger.info("strAccountTypeUserName   : "+ strAccountTypeUserName);
			Logger.info("strInvestigateUserCount   : "+ strAccountTypeCount);
			String strAccountTypeUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strAccountTypeUserMatchLogCount   : "+ strAccountTypeUserMatchLogCount);
			String strAccountTypeInstanceMatchLogCountAfterRemove =strAccountTypeUserMatchLogCount.replace(",", "");
			Logger.info("strAccountTypeMatchLogCountAfterRemove   : "+ strAccountTypeInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strAccountTypeInstanceMatchLogCountAfterRemove.contains(strAccountTypeCount), "Account Type Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strAccountTypeCount), "Account Type Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strAccountTypeCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnObject(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			ip.investigateObjectFirstCheckBox(driver).click();
			hardWait(10);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjecteUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strObjectUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strObjectUserMatchLogCount   : "+ strObjectUserMatchLogCount);
			String strObjectInstanceMatchLogCountAfterRemove =strObjectUserMatchLogCount.replace(",", "");
			Logger.info("strObjectMatchLogCountAfterRemove   : "+ strObjectInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strObjectInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Object Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Object Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnSeverity(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeverityTabs(driver).click();
			Assert.assertEquals(ip.investigateSeverityTabs(driver).getText().trim(),"Severity", "Instance title is not matching");
			ip.investigateSeverityFirstCheckBox(driver).click();
			hardWait(10);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			String strSeverityUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strSeverityUserMatchLogCount   : "+ strSeverityUserMatchLogCount);
			String strSeverityInstanceMatchLogCountAfterRemove =strSeverityUserMatchLogCount.replace(",", "");
			Logger.info("strSeverityMatchLogCountAfterRemove   : "+ strSeverityInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strSeverityInstanceMatchLogCountAfterRemove.contains(strSeverityCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strSeverityCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strSeverityCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyInvestigateFilterOnActivity(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			ip.investigateActiveFirstCheckBox(driver).click();
			hardWait(10);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnSourceLocation(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSourceLocationTabs(driver).click();
			Assert.assertEquals(ip.investigateSourceLocationTabs(driver).getText().trim(),"Source Location", "Instance title is not matching");
			ip.investigateSourceLocationFirstCheckBox(driver).click();
			hardWait(10);
			String strSourceLocationUserName=ip.investigateSourceLocationFirstSeviceName(driver).getText().trim();
			String strSourceLocationCount=ip.investigateSourceLocationFirstSeviceCount(driver).getText().trim();
			Logger.info("strSourceLocationUserName   : "+ strSourceLocationUserName);
			Logger.info("strSourceLocationCount   : "+ strSourceLocationCount);
			String strSourceLocationUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strSourceLocationUserMatchLogCount   : "+ strSourceLocationUserMatchLogCount);
			String strSourceLocationInstanceMatchLogCountAfterRemove =strSourceLocationUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strSourceLocationInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strSourceLocationInstanceMatchLogCountAfterRemove.contains(strSourceLocationCount), "SourceLocation Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strSourceLocationCount), "SourceLocation Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strSourceLocationCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnBrowser(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateBrowserTabs(driver).click();
			Assert.assertEquals(ip.investigateBrowserTabs(driver).getText().trim(),"Browser", "Instance title is not matching");
			ip.investigateBrowserFirstCheckBox(driver).click();
			hardWait(10);
			String strBrowserUserName=ip.investigateBrowserFirstSeviceName(driver).getText().trim();
			String strBrowserCount=ip.investigateBrowserFirstSeviceCount(driver).getText().trim();
			Logger.info("strBrowserUserName   : "+ strBrowserUserName);
			Logger.info("strBrowserCount   : "+ strBrowserCount);
			String strBrowserUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strBrowserUserMatchLogCount   : "+ strBrowserUserMatchLogCount);
			String strBrowserInstanceMatchLogCountAfterRemove =strBrowserUserMatchLogCount.replace(",", "");
			Logger.info("strBrowserMatchLogCountAfterRemove   : "+ strBrowserInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strBrowserInstanceMatchLogCountAfterRemove.contains(strBrowserCount), "Browser Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strBrowserCount), "Browser Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strBrowserCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnPlatform(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigatePlatformTabs(driver).click();
			ip.investigateSelectPlatform(driver).click();
			Assert.assertEquals(ip.investigatePlatformTabs(driver).getText().trim(),"Platform", "Instance title is not matching");
			ip.investigatePlatformFirstCheckBox(driver).click();
			hardWait(10);
			String strPlatformUserName=ip.investigatePlatformFirstSeviceName(driver).getText().trim();
			String strPlatformCount=ip.investigatePlatformFirstSeviceCount(driver).getText().trim();
			Logger.info("strPlatformUserName   : "+ strPlatformUserName);
			Logger.info("strPlatformUserCount   : "+ strPlatformCount);
			String strPlatformUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strPlatformUserMatchLogCount   : "+ strPlatformUserMatchLogCount);
			String strPlatformInstanceMatchLogCountAfterRemove =strPlatformUserMatchLogCount.replace(",", "");
			Logger.info("strPlatformMatchLogCountAfterRemove   : "+ strPlatformInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strPlatformInstanceMatchLogCountAfterRemove.contains(strPlatformCount), "Platform Type Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strPlatformCount), "Platform Type Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strPlatformCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyExportas_CEF_Beforefilter(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			Assert.assertTrue(ip.investigateExportasCEF(driver).isElementVisible(), "investigate Exportas CEF not visible");
			ip.investigateExportasCEF(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyInvestigateFilterOnDriver(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigatePlatformTabs(driver).click();
			ip.investigateSelectDevice(driver).click();
			Assert.assertEquals(ip.investigatePlatformTabs(driver).getText().trim(),"Device", "Instance title is not matching");
			ip.investigateDeviceFirstCheckBox(driver).click();
			hardWait(10);
			String strDriverUserName=ip.investigateDeviceFirstSeviceName(driver).getText().trim();
			String strDriverCount=ip.investigateDeviceFirstSeviceCount(driver).getText().trim();
			Logger.info("strDriverUserName   : "+ strDriverUserName);
			Logger.info("strDriverUserCount   : "+ strDriverCount);
			String strDriverUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strDriverUserMatchLogCount   : "+ strDriverUserMatchLogCount);
			String strDriverInstanceMatchLogCountAfterRemove =strDriverUserMatchLogCount.replace(",", "");
			Logger.info("strDriverMatchLogCountAfterRemove   : "+ strDriverInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strDriverInstanceMatchLogCountAfterRemove.contains(strDriverCount), "Driver Type Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strDriverCount), "Driver Type Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strDriverCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyExportas_CSV_Beforefilter(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			Assert.assertTrue(ip.investigateExportasCSV(driver).isElementVisible(), "investigate Exportas CSV not visible");
			ip.investigateExportasCSV(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyExportas_LEEF_Beforefilter(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			Assert.assertTrue(ip.investigateExportasLEEF(driver).isElementVisible(), "investigate Exportas CEF not visible");
			ip.investigateExportasLEEF(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyExportas_CEF_AfterFilterOnService(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeviceTabs(driver).click();
			Assert.assertEquals(ip.investigateSelectSevice(driver).getText().trim(),"Service", "Service title is not matching");
			ip.investigateSelectSevice(driver).click();
			ip.investigateSeviceFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateSeviceName=ip.investigateSeviceFirstSeviceName(driver).getText().trim();
			String strInvestigateSeviceCount=ip.investigateSeviceFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceName   : "+ strInvestigateSeviceName);
			Logger.info("strInvestigateSeviceCount   : "+ strInvestigateSeviceCount);
			String strInvestigateSeviceMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceMatchLogCount   : "+ strInvestigateSeviceMatchLogCount);
			String strInvestigateSeviceMatchLogCountAfterRemove =strInvestigateSeviceMatchLogCount.replace(",", "");
			Logger.info("strInvestigateSeviceMatchLogCountAfterRemove   : "+ strInvestigateSeviceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateSeviceCount), "Foter count after filter is not missmatch");
			Assert.assertTrue(strInvestigateSeviceMatchLogCountAfterRemove.contains(strInvestigateSeviceCount), "Header count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateSeviceCount)>0)


			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}
			Assert.assertTrue(ip.investigateExportasCEF(driver).isElementVisible(), "investigate Exportas CEF not visible");
			ip.investigateExportasCEF(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {

			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String changeLogResultTo_3_Years(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		Assert.assertTrue(ip.investigateDropdownClick(driver).isElementVisible(), "Dropdown Box not visible");
		ip.investigateDropdownClick(driver).click();
		hardWait(20);
		Assert.assertTrue(ip.investigateSelect3Years(driver).isElementVisible(), "3 Years not visible");
		ip.investigateSelect3Years(driver).click();
		hardWait(30);
		try {

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyExportas_CSV_AfterFilterOnService(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeviceTabs(driver).click();
			Assert.assertEquals(ip.investigateSelectSevice(driver).getText().trim(),"Service", "Service title is not matching");
			ip.investigateSelectSevice(driver).click();
			ip.investigateSeviceFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateSeviceName=ip.investigateSeviceFirstSeviceName(driver).getText().trim();
			String strInvestigateSeviceCount=ip.investigateSeviceFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceName   : "+ strInvestigateSeviceName);
			Logger.info("strInvestigateSeviceCount   : "+ strInvestigateSeviceCount);
			String strInvestigateSeviceMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceMatchLogCount   : "+ strInvestigateSeviceMatchLogCount);
			String strInvestigateSeviceMatchLogCountAfterRemove =strInvestigateSeviceMatchLogCount.replace(",", "");
			Logger.info("strInvestigateSeviceMatchLogCountAfterRemove   : "+ strInvestigateSeviceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateSeviceCount), "Foter count after filter is not missmatch");
			Assert.assertTrue(strInvestigateSeviceMatchLogCountAfterRemove.contains(strInvestigateSeviceCount), "Header count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateSeviceCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}
			Assert.assertTrue(ip.investigateExportasCSV(driver).isElementVisible(), "investigate Exportas CEF not visible");
			ip.investigateExportasCSV(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {

			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyExportas_LEEF_AfterFilterOnService(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateSeviceTabs(driver).click();
			Assert.assertEquals(ip.investigateSelectSevice(driver).getText().trim(),"Service", "Service title is not matching");
			ip.investigateSelectSevice(driver).click();
			ip.investigateSeviceFirstCheckBox(driver).click();
			hardWait(10);
			String strInvestigateSeviceName=ip.investigateSeviceFirstSeviceName(driver).getText().trim();
			String strInvestigateSeviceCount=ip.investigateSeviceFirstSeviceCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceName   : "+ strInvestigateSeviceName);
			Logger.info("strInvestigateSeviceCount   : "+ strInvestigateSeviceCount);
			String strInvestigateSeviceMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strInvestigateSeviceMatchLogCount   : "+ strInvestigateSeviceMatchLogCount);
			String strInvestigateSeviceMatchLogCountAfterRemove =strInvestigateSeviceMatchLogCount.replace(",", "");
			Logger.info("strInvestigateSeviceMatchLogCountAfterRemove   : "+ strInvestigateSeviceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strFoterStringAfterRemove.contains(strInvestigateSeviceCount), "Foter count after filter is not missmatch");
			Assert.assertTrue(strInvestigateSeviceMatchLogCountAfterRemove.contains(strInvestigateSeviceCount), "Header count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strInvestigateSeviceCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}
			Assert.assertTrue(ip.investigateExportasLEEF(driver).isElementVisible(), "investigate Exportas CEF not visible");
			ip.investigateExportasLEEF(driver).click();
			hardWait(1);
			Assert.assertTrue(ip.investigateAlertMessage(driver).isElementVisible(), "investigateAlertMessage not visible");
			String strInvestigateAlertMessage = ip.investigateAlertMessage(driver).getText();
			Logger.info("strInvestigateAlertMessage   : "+ strInvestigateAlertMessage);
			Assert.assertEquals(strInvestigateAlertMessage.trim(),"Your request has been received. Download link will be sent to you via email.", "Message title is not matching");

		} catch (Exception e) {

			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyLearnMore(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateLearnMore(driver).click();
			hardWait(30);
			Assert.assertTrue(ip.investigateLearnMoreIcon(driver).isElementVisible(), "investigateLearnMoreIcon not visible");
			Assert.assertEquals(ip.investigateLearnMoreH1(driver).getText().trim(),"Investigate", "Investigate is not matching");
			Assert.assertEquals(ip.investigateLearnMoreH2(driver).getText().trim(),"Historical transactions", "Historical transactions is not matching");

		} catch (Exception e) {

			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public void clickInvestigatetTimePeriodSelectorButton(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		try {
			ip.investigatetimeperiodselectorbutton(driver).waitForElementPresent(driver);
			ip.investigatetimeperiodselectorbutton(driver).waitForElementToBeVisible(driver);
			ip.investigatetimeperiodselectorbutton(driver).mouseOverClick(driver);

			hardWait(5);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickOptionFromInvestigatetTimePeriodDropdown(WebDriver driver, String optionType) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		clickInvestigatetTimePeriodSelectorButton(driver);

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		try {
			if(optionType.contains("1 Day")){
				ip.investigatetimeperiodselectordaydropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectordaydropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectordaydropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectordaydropdown(driver).click();
			}else if(optionType.contains("1 Week")){
				ip.investigatetimeperiodselectorweekdropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectorweekdropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectorweekdropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectorweekdropdown(driver).click();
			}else if(optionType.contains("1 Month")){
				ip.investigatetimeperiodselectormonthdropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectormonthdropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectormonthdropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectormonthdropdown(driver).click();
			}else if(optionType.contains("1 Year")){
				ip.investigatetimeperiodselectoryeardropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectoryeardropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectoryeardropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectoryeardropdown(driver).click();
			}else if(optionType.contains("3 Months")){
				ip.investigatetimeperiodselectorthreemonthdropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectorthreemonthdropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectorthreemonthdropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectorthreemonthdropdown(driver).click();
			}else if(optionType.contains("6 Months")){
				ip.investigatetimeperiodselectorsixmonthdropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectorsixmonthdropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectorsixmonthdropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectorsixmonthdropdown(driver).click();
			}else if(optionType.contains("2 Years")){
				ip.investigatetimeperiodselectortwoyeardropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectortwoyeardropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectortwoyeardropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectortwoyeardropdown(driver).click();
			}else if(optionType.contains("3 Years")){
				ip.investigatetimeperiodselectorthreeyeardropdown(driver).waitForElementPresent(driver);
				ip.investigatetimeperiodselectorthreeyeardropdown(driver).waitForElementToBeVisible(driver);
				ip.investigatetimeperiodselectorthreeyeardropdown(driver).mouseOver(driver);
				ip.investigatetimeperiodselectorthreeyeardropdown(driver).click();
			}
			hardWait(15);

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	public String verifyFilterActivity_Download_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_ContentInspection_critical(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			hardWait(10);
			selectActive(driver,selectActive);
			hardWait(10);
			selectSeverity(driver,selectSeverity);
			hardWait(10);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterActivity_ContentInspection_informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterActivity_Delete_Warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_Trash_Warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}




	public String verifyFilterActivity_Delete_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			hardWait(5);
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_Trash_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Unshare_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Share_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Create_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Authorize_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_InvalidLogin_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}



	public String verifyFilterActivity_Login_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_Allow_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Send_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_Edit_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Preview_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterActivity_Move_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_File_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_Unknown_Device_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterObject_Folder_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterObject_App_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_NotAvailable_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			ip.ObjectNotAvailable(driver).click();
			//selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0){
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.fail("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_NotAvailable_critical(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			ip.ObjectNotAvailable(driver).click();
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_Authentication_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterObject_folder_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_Application_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterObject_EmailMessage_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterObject_Folder_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_folder_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyFilterObject_Session_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_file_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterObject_File_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterObject_file_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifyFilterObject_Session_warning(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateObjectTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateObjectTabs(driver).getText().trim(),"Object", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strObjectUserName=ip.investigateObjectFirstSeviceName(driver).getText().trim();
			String strObjectCount=ip.investigateObjectFirstSeviceCount(driver).getText().trim();
			Logger.info("strObjectUserName   : "+ strObjectUserName);
			Logger.info("strObjectCount   : "+ strObjectCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strObjectCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strObjectCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strObjectCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strObjectCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}



	public String verifyFilterActivity_InvalidLogin_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}

	public String verifyFilterActivity_Receive_Informational(WebDriver driver,String selectActive,String selectSeverity) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);

		try {
			testShowing_50_of(driver);
			ip.investigateToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(ip.investigateSearchBoxTabs(driver).isElementVisible(), "Search Box not visible");
			ip.investigateActiveTabs(driver).click();
			Assert.assertTrue(ip.investigateAPIClickCheckBox(driver).isElementVisible(), "Search Box not visible");
			ip.investigateAPIClickCheckBox(driver).click();
			Assert.assertEquals(ip.investigateActiveTabs(driver).getText().trim(),"Activity", "Instance title is not matching");
			selectActive(driver,selectActive);
			hardWait(5);
			selectSeverity(driver,selectSeverity);
			hardWait(5);
			String strActiveUserName=ip.investigateActiveFirstSeviceName(driver).getText().trim();
			String strActiveCount=ip.investigateActiveFirstSeviceCount(driver).getText().trim();
			Logger.info("strActiveUserName   : "+ strActiveUserName);
			Logger.info("strActiveCount   : "+ strActiveCount);
			String strSeverityUserName=ip.investigateSeverityFirstSeviceName(driver).getText().trim();
			String strSeverityCount=ip.investigateSeverityFirstSeviceCount(driver).getText().trim();
			Logger.info("strSeverityUserName   : "+ strSeverityUserName);
			Logger.info("strSeverityCount   : "+ strSeverityCount);
			Assert.assertEquals(strActiveCount,strSeverityCount, "Severity Count title is not matching");
			String strActiveUserMatchLogCount=ip.investigateSeviceMatchLogCount(driver).getText().trim();
			Logger.info("strActiveUserMatchLogCount   : "+ strActiveUserMatchLogCount);
			String strActiveInstanceMatchLogCountAfterRemove =strActiveUserMatchLogCount.replace(",", "");
			Logger.info("strActiveMatchLogCountAfterRemove   : "+ strActiveInstanceMatchLogCountAfterRemove);
			String strFoterString = ip.investigateFoterText(driver).getText();
			Logger.info("strFoterString   : "+ strFoterString);
			String strFoterStringAfterRemove =strFoterString.replace(",", "");
			Logger.info("strFoterStringAfterRemove   : "+ strFoterStringAfterRemove);
			Assert.assertTrue(strActiveInstanceMatchLogCountAfterRemove.contains(strActiveCount), "Active Header count after filter is not missmatch");
			Assert.assertTrue(strFoterStringAfterRemove.contains(strActiveCount), "Active Foter count after filter is not missmatch");
			List<Element> activityTableRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();
			if (Integer.parseInt(strActiveCount)>0)
			{
				Assert.assertTrue(activityTableRowCount.size()>0, "Table Row after filter is not missmatch");
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			Assert.assertTrue(false);
			e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyDetails_Delete_InformationalSourcesP(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);

			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();

			String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			String ipLocation = ip.ipLocation(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipParen = ip.ipParen(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();

			Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Number but was " + ipUserName;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Number but was " + ipDocumentType;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Number but was " + ipcity;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Number but was " + ipinstance;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLocation+
					" - Expecting some Number but was " + ipLocation;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipParen.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParen+
					" - Expecting some Number but was " + ipParen;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipSourceLocation;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Download_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String Service = ip.ipService(driver).getText().trim();
			System.out.println("Service Type is :" +Service);
			if(Service.contains("Box"))
			{
				String ObjectType = ip.ipObjectType(driver).getText().trim();
				System.out.println("Object Type is :" +ObjectType);
				if(ObjectType.contains("File"))
				{String ipService = ip.ipService(driver).getText().trim();
				String ipUserName = ip.ipUserName(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipHost = ip.ipHost(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipLongitude = ip.ipLongitude(driver).getText().trim();
				String ipLatitude = ip.ipLatitude(driver).getText().trim();
				String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();

				String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
				String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipcity = ip.ipcity(driver).getText().trim();
				String ipcountry = ip.ipcountry(driver).getText().trim();
				String ipinstance = ip.ipinstance(driver).getText().trim();
				//String ipLocation = ip.ipLocation(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipParen = ip.ipParen(driver).getText().trim();
				String ipParent_id = ip.ipParent_id(driver).getText().trim();

				Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);
				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
						" - Expecting some Number but was " + ipUserName;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
						" - Expecting some Number but was " + ipHost;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
						" - Expecting some Name but was " + ipLongitude;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
						" - Expecting some Name but was " + ipLatitude;
				validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
						" - Expecting some Number but was " + ipDocumentType;
				validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
						" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
						" - Expecting some Number but was " + ipcity;
				validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
						" - Expecting some Name but was " + ipcountry;
				validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
						" - Expecting some Number but was " + ipinstance;
				validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
						" - Expecting some Name but was " + ipLatitude;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipParen.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParen+
						" - Expecting some Number but was " + ipParen;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipParent_id;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipSourceLocation;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);


				}else
				{
					String ipService = ip.ipService(driver).getText().trim();
					String ipUserName = ip.ipUserName(driver).getText().trim();
					String ipUser = ip.ipUser(driver).getText().trim();
					String ipSeverity = ip.ipSeverity(driver).getText().trim();
					String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
					String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
					String ipMessage = ip.ipMessage(driver).getText().trim();
					String ipHost = ip.ipHost(driver).getText().trim();
					String ipObjectType = ip.ipObjectType(driver).getText().trim();
					String ipActivityType = ip.ipActivityType(driver).getText().trim();
					String ipLongitude = ip.ipLongitude(driver).getText().trim();
					String ipLatitude = ip.ipLatitude(driver).getText().trim();
					String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();

					String ipFileSize = ip.ipFileSize(driver).getText().trim();
					String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
					String ipcity = ip.ipcity(driver).getText().trim();
					String ipcountry = ip.ipcountry(driver).getText().trim();
					String ipinstance = ip.ipinstance(driver).getText().trim();
					//String ipLocation = ip.ipLocation(driver).getText().trim();
					String ipName = ip.ipName(driver).getText().trim();
					String ipParen = ip.ipParen(driver).getText().trim();
					String ipParent_id = ip.ipParent_id(driver).getText().trim();

					Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);
					validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
							" - Expecting some Name but was " + ipService;
					validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
							" - Expecting some Number but was " + ipUserName;
					validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
							" - Expecting some Name but was " + ipUser;
					validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
							" - Expecting some Number but was " + ipSeverity;
					validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
							" - Expecting some Name but was " + ipHappenedAt;
					validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
							" - Expecting some Number but was " + ipRecordedAt;
					validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
							" - Expecting some Name but was " + ipMessage;
					validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
							" - Expecting some Number but was " + ipHost;
					validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
							" - Expecting some Name but was " + Name;
					validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
							" - Expecting some Number but was " + Badge;
					validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
							" - Expecting some Name but was " + ipObjectType;
					validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
							" - Expecting some Number but was " + ipActivityType;
					validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
							" - Expecting some Name but was " + ipLongitude;
					validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
							" - Expecting some Number but was " + Badge;
					validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
							" - Expecting some Name but was " + FileName;
					validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
							" - Expecting some Number but was " + Fileinfo;
					validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
							" - Expecting some Name but was " + ipLatitude;
					validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
							" - Expecting some Name but was " + ipFileSize;
					validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
							" - Expecting some Number but was " + ipResource_Id;
					validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
							" - Expecting some Number but was " + ipcity;
					validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
							" - Expecting some Name but was " + ipcountry;
					validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
							" - Expecting some Number but was " + ipinstance;
					validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
							" - Expecting some Name but was " + ipLatitude;
					validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
							" - Expecting some Name but was " + ipName;
					validationMessage += (!ipParen.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParen+
							" - Expecting some Number but was " + ipParen;
					validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
							" - Expecting some Number but was " + ipParent_id;
					validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
							" - Expecting some Number but was " + ipSourceLocation;

					Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
					Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
					Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

					ip.investgatepopupclose(driver).click();	
					hardWait(5);


				}


			}
			else
			{
				String ipService = ip.ipService(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipParent_id = ip.ipParent_id(driver).getText().trim();

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
						" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipParent_id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);


			}
		}

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_ContentInspection_criticalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("critical"),"critical title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item# Name:"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item# Badge:"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item# FileName:"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item# FileInfo:"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);		
			String iService = ip.ipService(driver).getText().trim();
			if(iService.contains("Google Drive"))
			{
				String ipService = ip.ipUser(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipRisks = ip.ipRisks(driver).getText().trim();
				String ipContentVulnerabilities = ip.ipContentVulnerabilities(driver).getText().trim();
				String ipService1 = ip.ipService(driver).getText().trim();

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipService1.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService1+
						" - Expecting some Name but was " + ipService1;
				validationMessage += (!ipRisks.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRisks+
						" - Expecting some Number but was " + ipRisks;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!ipContentVulnerabilities.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipContentVulnerabilities+
						" - Expecting some Number but was " + ipContentVulnerabilities;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);
			}

			else
			{
				String ipService1 = ip.ipService(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipRisks = ip.ipRisks(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipContentVulnerabilities = ip.ipContentVulnerabilities(driver).getText().trim();

				validationMessage += (!ipContentVulnerabilities.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipContentVulnerabilities+
						" - Expecting some Name but was " + ipContentVulnerabilities;
				validationMessage += (!ipService1.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService1+
						" - Expecting some Name but was " + ipService1;
				validationMessage += (!ipRisks.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRisks+
						" - Expecting some Number but was " + ipRisks;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);
			}

		}
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_ContentInspection_informationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);

			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_Delete_Warning(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);

			Boolean boolean1 = ip.FileType(driver).isElementPresent(driver);
			if ( boolean1) 
			{

				String ipService = ip.ipService(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();			
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipParent_id = ip.ipParent_id(driver).getText().trim();
				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
						" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipParent_id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);
			}

			else

			{

				String ipService = ip.ipService(driver).getText().trim();
				//String ipUserName = ip.ipUserName(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				//String ipHost = ip.ipHost(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				//String ipLongitude = ip.ipLongitude(driver).getText().trim();
				//String ipLatitude = ip.ipLatitude(driver).getText().trim();
				//String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();

				//String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
				//String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				//String ipcity = ip.ipcity(driver).getText().trim();
				//String ipcountry = ip.ipcountry(driver).getText().trim();
				//String ipinstance = ip.ipinstance(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipParent_id = ip.ipParent_id(driver).getText().trim();

				//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				//validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
				//	" - Expecting some Number but was " + ipUserName;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				//validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
				//	" - Expecting some Number but was " + ipHost;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				//	validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
				//		" - Expecting some Name but was " + ipLongitude;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				//validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
				//	" - Expecting some Name but was " + ipLatitude;
				//validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
				//	" - Expecting some Number but was " + ipDocumentType;
				//validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
				//	" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				//validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
				//	" - Expecting some Number but was " + ipcity;
				//validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
				//	" - Expecting some Name but was " + ipcountry;
				//validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
				//	" - Expecting some Number but was " + ipinstance;
				//validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
				//	" - Expecting some Name but was " + ipLatitude;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				//validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
				//	" - Expecting some Number but was " + ipSourceLocation;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipParent_id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);


			}

		}

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Trash_Warning(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);

			Boolean boolean1 = ip.FileType(driver).isElementPresent(driver);
			if ( boolean1) 
			{

				String ipService = ip.ipService(driver).getText().trim();
				//	String ipUserName = ip.ipUserName(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				//String ipHost = ip.ipHost(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				//String ipLongitude = ip.ipLongitude(driver).getText().trim();
				//String ipLatitude = ip.ipLatitude(driver).getText().trim();
				//String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();

				//String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
				String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				//String ipcity = ip.ipcity(driver).getText().trim();
				//String ipcountry = ip.ipcountry(driver).getText().trim();
				//String ipinstance = ip.ipinstance(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				String ipParent_id = ip.ipParent_id(driver).getText().trim();


				//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
						" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
						" - Expecting some Number but was " + ipParent_id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);
			}

			else

			{

				String ipService = ip.ipService(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();
				//String ipParent_id = ip.ipParent_id(driver).getText().trim();

				//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				//validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
				//	" - Expecting some Number but was " + ipHost;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				//	validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
				//		" - Expecting some Number but was " + ipParent_id;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);


			}

		}

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}



	public String verifyDetails_Delete_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ObjectType = ip.ipObjectType(driver).getText().trim();
			if (ObjectType.contains("Folder")) {

				String ipService = ip.ipService(driver).getText().trim();
				String ipUserName = ip.ipUserName(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipHost = ip.ipHost(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipLongitude = ip.ipLongitude(driver).getText().trim();
				String ipLatitude = ip.ipLatitude(driver).getText().trim();
				String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
				//String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
				String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipcity = ip.ipcity(driver).getText().trim();
				String ipcountry = ip.ipcountry(driver).getText().trim();
				String ipinstance = ip.ipinstance(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();


				Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
						" - Expecting some Number but was " + ipUserName;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
						" - Expecting some Number but was " + ipHost;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
						" - Expecting some Name but was " + ipLongitude;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
						" - Expecting some Name but was " + ipLatitude;
				//			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
				//				" - Expecting some Number but was " + ipDocumentType;
				validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
						" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
						" - Expecting some Number but was " + ipcity;
				validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
						" - Expecting some Name but was " + ipcountry;
				validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
						" - Expecting some Number but was " + ipinstance;
				validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
						" - Expecting some Name but was " + ipLatitude;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
						" - Expecting some Number but was " + ipSourceLocation;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);
			}

			else

			{

				String ipService = ip.ipService(driver).getText().trim();
				String ipUserName = ip.ipUserName(driver).getText().trim();
				String ipUser = ip.ipUser(driver).getText().trim();
				String ipSeverity = ip.ipSeverity(driver).getText().trim();
				String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
				String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
				String ipMessage = ip.ipMessage(driver).getText().trim();
				String ipHost = ip.ipHost(driver).getText().trim();
				String ipObjectType = ip.ipObjectType(driver).getText().trim();
				String ipActivityType = ip.ipActivityType(driver).getText().trim();
				String ipLongitude = ip.ipLongitude(driver).getText().trim();
				String ipLatitude = ip.ipLatitude(driver).getText().trim();
				String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();	
				String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
				//String ipFileSize = ip.ipFileSize(driver).getText().trim();
				String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
				String ipcity = ip.ipcity(driver).getText().trim();
				String ipcountry = ip.ipcountry(driver).getText().trim();
				String ipinstance = ip.ipinstance(driver).getText().trim();
				String ipName = ip.ipName(driver).getText().trim();


				Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

				validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
						" - Expecting some Name but was " + ipLongitude;
				validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
						" - Expecting some Name but was " + ipLatitude;

				validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
						" - Expecting some Name but was " + ipDocumentType;
				validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
						" - Expecting some Name but was " + ipService;
				validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
						" - Expecting some Number but was " + ipUserName;
				validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
						" - Expecting some Name but was " + ipUser;
				validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
						" - Expecting some Number but was " + ipSeverity;
				validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
						" - Expecting some Name but was " + ipHappenedAt;
				validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
						" - Expecting some Number but was " + ipRecordedAt;
				validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
						" - Expecting some Name but was " + ipMessage;
				validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
						" - Expecting some Number but was " + ipHost;
				validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
						" - Expecting some Name but was " + Name;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
						" - Expecting some Name but was " + ipObjectType;
				validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
						" - Expecting some Number but was " + ipActivityType;
				//validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
				//	" - Expecting some Name but was " + ipLongitude;
				validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
						" - Expecting some Number but was " + Badge;
				validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
						" - Expecting some Name but was " + FileName;
				validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
						" - Expecting some Number but was " + Fileinfo;
				//validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
				//	" - Expecting some Name but was " + ipLatitude;
				//validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
				//	" - Expecting some Number but was " + ipDocumentType;
				//validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
				//	" - Expecting some Name but was " + ipFileSize;
				validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
						" - Expecting some Number but was " + ipResource_Id;
				validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
						" - Expecting some Number but was " + ipcity;
				validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
						" - Expecting some Name but was " + ipcountry;
				validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
						" - Expecting some Number but was " + ipinstance;
				//	validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
				//		" - Expecting some Name but was " + ipLatitude;
				validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
						" - Expecting some Name but was " + ipName;
				validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
						" - Expecting some Number but was " + ipSourceLocation;

				Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
				Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
				Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

				ip.investgatepopupclose(driver).click();	
				hardWait(5);


			}

		}

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Trash_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipsubject = ip.ipsubject(driver).getText().trim();
			String ipInFolder = ip.ipInFolder(driver).getText().trim();
			String ipInternalrecipients = ip.ipInternalrecipients(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipsubject.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsubject+
					" - Expecting some Name but was " + ipsubject;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipInFolder.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInFolder+
					" - Expecting some Number but was " + ipInFolder;
			validationMessage += (!ipInternalrecipients.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInternalrecipients+
					" - Expecting some Name but was " + ipInternalrecipients;
			//validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
			//	" - Expecting some Number but was " + ipinstance;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_Unshare_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipSharedWith = ip.ipSharedWith(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipAccountType = ip.ipAccountType(driver).getText().trim();
			String iptarget_account_type = ip.iptarget_account_type(driver).getText().trim();

			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipSharedWith.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSharedWith+
					" - Expecting some Name but was " + ipSharedWith;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Number but was " + ipFileSize;
			validationMessage += (!ipAccountType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipAccountType+
					" - Expecting some Name but was " + ipAccountType;
			validationMessage += (!iptarget_account_type.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+iptarget_account_type+
					" - Expecting some Number but was " + iptarget_account_type;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Name but was " + ipParent_id;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Share_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Name Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Badge Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List FileName Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Fileinfo Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			
			String ipName = ip.ipName(driver).getText().trim();
			//String ipSharedWith = ip.ipSharedWith(driver).getText().trim();
			//String ipFileSize = ip.ipFileSize(driver).getText().trim();
			//String ipParent_id = ip.ipParent_id(driver).getText().trim();
			//String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			//String ipAccountType = ip.ipAccountType(driver).getText().trim();
			//String iptarget_account_type = ip.iptarget_account_type(driver).getText().trim();
			//String ipRole = ip.ipRole(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List ipService Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List ipUser Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List ipSeverity Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List ipHappenedAt Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List ipRecordedAt Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List ipMessage Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List ipObjectType Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List ipActivityType Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
		/*	validationMessage += (!ipSharedWith.isEmpty()) ? "" : "Investigate Logs List ipSharedWith Item#"+(i+1)+":"+ipSharedWith+
					" - Expecting some Name but was " + ipSharedWith;*/
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List ipName Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			
			/*validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Number but was " + ipFileSize;
			validationMessage += (!ipAccountType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipAccountType+
					" - Expecting some Name but was " + ipAccountType;
			validationMessage += (!ipRole.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRole+
					" - Expecting some Name but was " + ipRole;
			validationMessage += (!iptarget_account_type.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+iptarget_account_type+
					" - Expecting some Number but was " + iptarget_account_type;*/
			/*validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Name but was " + ipParent_id;*/

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Create_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Name Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Badge Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List FileName Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Fileinfo Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			/*String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();*/
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List ipService Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List ipUser Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List ipSeverity Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List ipHappenedAt Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List ipRecordedAt Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List ipMessage Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List ipObjectType Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List ipActivityType Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List ipName Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			/*validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Name but was " + ipParent_id;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			*/
			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_InvalidLogin_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();	
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			//String ipHasAccessTo =ip.ipHasAccessTo(driver).getText().trim();
			//String ipclient_id=ip.ipclient_id(driver).getText().trim();
			//String ipapplication =ip.ipapplication(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Number but was " + ipSourceLocation;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Authorize_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipHasAccessTo = ip.ipHasAccessTo(driver).getText().trim();
			String ipclient_id = ip.ipclient_id(driver).getText().trim();
			String ipapplication = ip.ipapplication(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipHasAccessTo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHasAccessTo+
					" - Expecting some Number but was " + ipHasAccessTo;
			validationMessage += (!ipclient_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipclient_id+
					" - Expecting some Name but was " + ipclient_id;
			validationMessage += (!ipapplication.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipapplication+
					" - Expecting some Name but was " + ipapplication;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}	
		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}



	public String verifyDetails_Login_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();	
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Number but was " + ipUserName;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Number but was " + ipcity;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Number but was " + ipinstance;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Number but was " + ipSourceLocation;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Allow_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Number but was " + ipSourceLocation;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Number but was " + ipcity;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_InvalidLogin_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Number but was " + ipSourceLocation;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Number but was " + ipcity;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Send_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();		
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipInFolder = ip.ipInFolder(driver).getText().trim();
			String ipInternalrecipients = ip.ipInternalrecipients(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipAccountType = ip.ipAccountType(driver).getText().trim();
			//String ipattachments = ip.ipattachments(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipsubject = ip.ipsubject(driver).getText().trim();	
			String iptarget_account_type = ip.iptarget_account_type(driver).getText().trim();


			//String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();	
			//String ipcity = ip.ipcity(driver).getText().trim();
			//String ipcountry = ip.ipcountry(driver).getText().trim();
			//String ipinstance = ip.ipinstance(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipInFolder.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInFolder+
					" - Expecting some Number but was " + ipInFolder;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipAccountType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipAccountType+
					" - Expecting some Number but was " + ipAccountType;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipInternalrecipients.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInternalrecipients+
					" - Expecting some Name but was " + ipInternalrecipients;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			//	validationMessage += (!ipattachments.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipattachments+
			//		" - Expecting some Name but was " + ipattachments;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipsubject.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsubject+
					" - Expecting some Name but was " + ipsubject;
			validationMessage += (!iptarget_account_type.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+iptarget_account_type+
					" - Expecting some Name but was " + iptarget_account_type;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Edit_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();		
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Number but was " + ipUserName;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Name but was " + ipDocumentType;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Name but was " + ipDocumentType;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Preview_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();		
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
			//String ipName = ip.ipName(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Number but was " + ipUserName;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Number but was " + ipHost;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			//validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
			//	" - Expecting some Name but was " + ipName;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Name but was " + ipDocumentType;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Name but was " + ipDocumentType;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Move_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();		
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Number but was " + ipName;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_File_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();

			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();

			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();

			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipDocumentType = ip.ipDocumentType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();

			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Name but was " + ipUserName;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipDocumentType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipDocumentType+
					" - Expecting some Name but was " + ipDocumentType;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Number but was " + ipName;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Unknown_Device_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Name but was " + ipUserName;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Folder_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Name but was " + ipUserName;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_App_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_NotAvailable_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			//Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_NotAvailable_criticalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("critical"),"critical title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipRisks = ip.ipRisks(driver).getText().trim();
			String ipContentVulnerabilities = ip.ipContentVulnerabilities(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);
			validationMessage += (!ipRisks.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRisks+
					" - Expecting some Name but was " + ipRisks;
			validationMessage += (!ipContentVulnerabilities.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipContentVulnerabilities+
					" - Expecting some Name but was " + ipContentVulnerabilities;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			//Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Authentication_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_folder_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Name but was " + ipParent_id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_Application_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipHasAccessTo =ip.ipHasAccessTo(driver).getText().trim();
			String ipclient_id=ip.ipclient_id(driver).getText().trim();
			String ipapplication =ip.ipapplication(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipHasAccessTo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHasAccessTo+
					" - Expecting some Name but was " + ipHasAccessTo;
			validationMessage += (!ipclient_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipclient_id+
					" - Expecting some Name but was " + ipclient_id;
			validationMessage += (!ipapplication.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipapplication+
					" - Expecting some Name but was " + ipapplication;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}




	public String verifyDetails_EmailMessage_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipInFolder = ip.ipInFolder(driver).getText().trim();
			String ipInternalrecipients = ip.ipInternalrecipients(driver).getText().trim();
			String ipAccountType = ip.ipAccountType(driver).getText().trim();
			String ipsender = ip.ipsender(driver).getText().trim();	
			String ipsubject = ip.ipsubject(driver).getText().trim();
			String iptarget_account_type = ip.iptarget_account_type(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();

			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipInFolder;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInFolder+
					" - Expecting some Name but was " + ipInFolder;
			validationMessage += (!ipInternalrecipients.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInternalrecipients+
					" - Expecting some Name but was " + ipInternalrecipients;
			validationMessage += (!ipsender.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsender+
					" - Expecting some Name but was " + ipsender;
			validationMessage += (!ipAccountType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipAccountType+
					" - Expecting some Name but was " + ipAccountType;
			validationMessage += (!ipsubject.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsubject+
					" - Expecting some Name but was " + ipsubject;
			validationMessage += (!iptarget_account_type.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+iptarget_account_type+
					" - Expecting some Name but was " + iptarget_account_type;

			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}
	public String verifyDetails_Folder_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);


			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}



	public String verifyDetails_folder_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Name but was " + ipParent_id;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;


			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_Session_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);


			validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
					" - Expecting some Name but was " + ipUserName;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			//validationMessage += (!ipParen.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParen+
			//	" - Expecting some Name but was " + ipParen;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			//validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
			//" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_file_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Number but was " + ipService;	
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Number but was " + ipName;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public String verifyDetails_File_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Number but was " + ipName;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_file_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipFileSize = ip.ipFileSize(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipParent_id = ip.ipParent_id(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
					" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Number but was " + ipName;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;
			validationMessage += (!ipFileSize.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipFileSize+
					" - Expecting some Name but was " + ipFileSize;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_Session_warningSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateObjectFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("warning"),"warning title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);
			String ipService = ip.ipService(driver).getText().trim();
			//String ipUserName = ip.ipUserName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();	
			String ipHost = ip.ipHost(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipLongitude = ip.ipLongitude(driver).getText().trim();
			String ipLatitude = ip.ipLatitude(driver).getText().trim();
			String ipSourceLocation = ip.ipSourceLocation(driver).getText().trim();
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipcity = ip.ipcity(driver).getText().trim();
			String ipcountry = ip.ipcountry(driver).getText().trim();
			String ipinstance = ip.ipinstance(driver).getText().trim();
			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);


			//	validationMessage += (!ipUserName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUserName+
			//		" - Expecting some Name but was " + ipUserName;
			validationMessage += (!ipcity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcity+
					" - Expecting some Name but was " + ipcity;
			validationMessage += (!ipHost.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHost+
					" - Expecting some Name but was " + ipHost;
			validationMessage += (!ipLongitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLongitude+
					" - Expecting some Name but was " + ipLongitude;
			validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
					" - Expecting some Name but was " + ipLatitude;
			validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
					" - Expecting some Name but was " + ipSourceLocation;
			//validationMessage += (!ipParen.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParen+
			//	" - Expecting some Name but was " + ipParen;
			validationMessage += (!ipcountry.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipcountry+
					" - Expecting some Name but was " + ipcountry;
			validationMessage += (!ipinstance.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipinstance+
					" - Expecting some Name but was " + ipinstance;
			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			//validationMessage += (!ipParent_id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipParent_id+
			//" - Expecting some Number but was " + ipParent_id;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Name but was " + ipUser;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Name but was " + ipResource_Id;

			Assert.assertTrue(ipObjectType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public String verifyDetails_Receive_InformationalSourcesPage(WebDriver driver,String selectActive,String selectSeverity) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		String  validationMessage = ""; 

		String sourceCount = ip.investigateActiveFirstSeviceCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		List<Element> SourcesPageSourcesRowCount  = ip.investigateTableRowCountCount(driver).getChildElements();		
		hardWait(5);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount.trim()); int count=0;
		if(intGateletsSourcesCount>10){
			count=5;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Details view started");

		for(int i=0;i<count;i++){

			Assert.assertTrue(ip.investigatePageTableIcon(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableServiceBadge(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileName(driver,i+1).isElementVisible(), "Icon is not visible");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).isElementVisible(), "Icon is not visible");
			String strApi=ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			Logger.info("strApi : "+strApi);
			String strBadge=ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();
			Logger.info("strBadge : "+strBadge);
			Assert.assertEquals(ip.investigatePageTableServiceBadge(driver,i+1).getText().trim(),"API", "API title is not matching");
			Assert.assertTrue(ip.investigatePageTableFiileInfo(driver,i+1).getText().trim().contains("informational"),"informational title is not matching");

			String Name = ip.investigatePageTableServiceName(driver,i+1).getText().trim();
			String Badge = ip.investigatePageTableServiceBadge(driver,i+1).getText().trim();
			String FileName = ip.investigatePageTableFiileName(driver,i+1).getText().trim();
			String Fileinfo = ip.investigatePageTableFiileInfo(driver,i+1).getText().trim();

			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;

			ip.investigatePageTableIcon(driver,i+1).click();	
			hardWait(5);


			String ipService = ip.ipService(driver).getText().trim();
			String ipName = ip.ipName(driver).getText().trim();
			String ipUser = ip.ipUser(driver).getText().trim();
			String ipSeverity = ip.ipSeverity(driver).getText().trim();
			String ipHappenedAt = ip.ipHappenedAt(driver).getText().trim();
			String ipRecordedAt = ip.ipRecordedAt(driver).getText().trim();
			String ipMessage = ip.ipMessage(driver).getText().trim();
			String ipInFolder = ip.ipInFolder(driver).getText().trim();
			String ipObjectType = ip.ipObjectType(driver).getText().trim();
			String ipActivityType = ip.ipActivityType(driver).getText().trim();
			String ipInternalrecipients = ip.ipInternalrecipients(driver).getText().trim();
			String ipAccountType = ip.ipAccountType(driver).getText().trim();
			String ipsender = ip.ipsender(driver).getText().trim();	
			String ipResource_Id = ip.ipResource_Id(driver).getText().trim();
			String ipsubject = ip.ipsubject(driver).getText().trim();
			String iptarget_account_type = ip.iptarget_account_type(driver).getText().trim();
			//String ipinstance = ip.ipinstance(driver).getText().trim();


			//Logger.info(ipService+ "|"+ipUserName + "|"+ipUser + "|" + ipSeverity + "|"+ipName);

			validationMessage += (!ipService.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipService+
					" - Expecting some Name but was " + ipService;
			validationMessage += (!ipAccountType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipAccountType+
					" - Expecting some Number but was " + ipAccountType;
			validationMessage += (!ipName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipName+
					" - Expecting some Name but was " + ipName;
			validationMessage += (!ipSeverity.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSeverity+
					" - Expecting some Number but was " + ipSeverity;
			validationMessage += (!ipHappenedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipHappenedAt+
					" - Expecting some Name but was " + ipHappenedAt;
			validationMessage += (!ipRecordedAt.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipRecordedAt+
					" - Expecting some Number but was " + ipRecordedAt;
			validationMessage += (!ipMessage.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipMessage+
					" - Expecting some Name but was " + ipMessage;
			validationMessage += (!ipInFolder.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInFolder+
					" - Expecting some Number but was " + ipInFolder;
			validationMessage += (!Name.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!ipObjectType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipObjectType+
					" - Expecting some Name but was " + ipObjectType;
			validationMessage += (!ipActivityType.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipActivityType+
					" - Expecting some Number but was " + ipActivityType;
			validationMessage += (!ipInternalrecipients.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipInternalrecipients+
					" - Expecting some Name but was " + ipInternalrecipients;
			validationMessage += (!Badge.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Badge+
					" - Expecting some Number but was " + Badge;
			validationMessage += (!FileName.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+FileName+
					" - Expecting some Name but was " + FileName;
			validationMessage += (!Fileinfo.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+Fileinfo+
					" - Expecting some Number but was " + Fileinfo;
			validationMessage += (!ipsender.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsender+
					" - Expecting some Name but was " + ipsender;
			validationMessage += (!ipResource_Id.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipResource_Id+
					" - Expecting some Number but was " + ipResource_Id;
			validationMessage += (!ipsubject.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipsubject+
					" - Expecting some Number but was " + ipsubject;
			validationMessage += (!iptarget_account_type.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+iptarget_account_type+
					" - Expecting some Name but was " + iptarget_account_type;
			validationMessage += (!ipUser.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipUser+
					" - Expecting some Number but was " + ipUser;
			//validationMessage += (!ipLatitude.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipLatitude+
			//	" - Expecting some Name but was " + ipLatitude;
			//validationMessage += (!ipSourceLocation.isEmpty()) ? "" : "Investigate Logs List Item#"+(i+1)+":"+ipSourceLocation+
			//	" - Expecting some Number but was " + ipSourceLocation;

			Assert.assertTrue(ipActivityType.trim().contains(selectActive),"Download title is not matching");
			Assert.assertTrue(ipSeverity.trim().contains(selectSeverity),"Severity title is not matching");
			Assert.assertEquals(ipMessage,FileName,"File Name is not matching");

			ip.investgatepopupclose(driver).click();	
			hardWait(5);
		}			

		Logger.info("Validation of Details view completed");
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}


	public void deleteFiltersOnInvestigetePage(WebDriver driver) throws InterruptedException{
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		if(ip.investigateFilter(driver).isDisplayed()){
			int filterCount = ip.investigateFilters(driver).getChildElements().size();
			for(int i=0;i<filterCount;i++){
				ip.investigateFiltersRemove(driver).waitForLoading(driver);
				ip.investigateFiltersRemove(driver).click();
				Thread.sleep(5000);
			}
		}
	}

	public String getValueFromDetailedViewOfActivity(WebDriver driver, String fieldName){
		String fieldValue="";

		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver, InvestigatePage.class);
		List<Element> investigateDetailedViewFieldName  = ip.investigateDetailedViewFieldName(driver).getChildElements();
		List<Element> investigateDetailedViewFieldValue  = ip.investigateDetailedViewFieldValue(driver).getChildElements();

		for(int i=0;i<investigateDetailedViewFieldName.size();i++){
			investigateDetailedViewFieldName  = ip.investigateDetailedViewFieldName(driver).getChildElements();
			investigateDetailedViewFieldValue  = ip.investigateDetailedViewFieldValue(driver).getChildElements();
			if(investigateDetailedViewFieldName.get(i).getText().trim().equalsIgnoreCase(fieldName)){
				fieldValue = investigateDetailedViewFieldValue.get(i).getText().trim();
				break;
			}
		}

		return fieldValue;

	}

}


