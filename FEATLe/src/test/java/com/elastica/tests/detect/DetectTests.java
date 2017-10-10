package com.elastica.tests.detect;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.elastica.common.CommonTest;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;



/**
 * Detect Test Suite
 * @author Eldo Rajan
 *
 */
public class DetectTests extends CommonTest{

	SoftAssert sAssert = null;

	@Priority(1)
	@Test(groups = { "sanity","smoke","regression" })
	public void testDetectThreatTreeRedirectionToInvestigatePage() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify Detect Threat Tree redirection to investigate page");
		Logger.info("1. Login into cloudsoc and navigate to Detect page");
		Logger.info("2. Verify atleast event is present and click on it");
		Logger.info("3. Verify detailed view appears and threat tree is loading");
		Logger.info("4. Click on any leaf from threat tree and subsequently verify it opens new window");
		Logger.info("5. Validate detailed page loads and verify it matches event from threat tree leaf");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		try{
			dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);

			Logger.info("****Expected header text:Detect****");
			sAssert.assertEquals(detect.getPageHeader(getWebDriver()),"Detect",
					"Detect App-Users Tab: Detect header mismatch is seen");
			Logger.info("****Expected active tab Text:Users****");
			sAssert.assertEquals(detect.getActiveTabText(getWebDriver()),"Users",
					"Detect App-Users Tab: Active tab header text mismatch is seen");

			int count=detect.getTableCountUsersTab(getWebDriver());
			if(count>0){
				sAssert.assertTrue(count>0,
						"Detect App-Users Tab: Expected some rows in table of users but is seen empty");
				detect.clickARowInUsersTab(getWebDriver(), 1);
				detect.clickRedirectionLinkFromSlidingPanelInUsersTab(getWebDriver());

				Logger.info("****Expected header text:Investigate****");
				sAssert.assertEquals(detect.getPageHeader(getWebDriver()),"Investigate",
						"Investigate App: Investigate header mismatch is seen");

				Logger.info("****Expected date dropdown text:3 Years****");
				sAssert.assertEquals(investigate.getDateDropdownButton(getWebDriver()),"3 Years",
						"Investigate App: Investigate date dropdown text mismatch is seen");

				String dateText = investigate.getDateTextBoxText(getWebDriver());
				sAssert.assertEquals(investigate.getDateTextBoxTextDifference(dateText),"",
						"Investigate App: Date textbox difference is seen");

				int activities = investigate.getTableCountActivities(getWebDriver());
				sAssert.assertTrue(activities>0,
						"Investigate App: Expected some rows in activities  but is seen empty");

			}else{
				Assert.fail("Detect App-Users Tab: Expected some rows in table of users but is seen empty");
			}

			sAssert.assertAll();
		}finally{
			/*int count = login.getWindowHandles(getWebDriver()).size();
			if(count>1){
				getWebDriver().close();
			}
			login.switchToParentWindow(getWebDriver());*/
		}

	}

	@Priority(2)
	@Test(groups = { "sanity","smoke","regression" })
	public void testDetectAllIncidentsRedirectionToIncidentsTab() {
		Logger.info("************Test Case Steps************");
		Logger.info("Description: Verify view all incidents links redirect to incidents tab");
		Logger.info("1. Login into cloudsoc and navigate to Detect page");
		Logger.info("2. Verify atleast event is present and click on it");
		Logger.info("3. Verify detailed view appears and incident tabs loads");
		Logger.info("4. Navigate to detailed view and verify incidents loads with view all incidents link");
		Logger.info("5. Click on view all incidents link and verify detailed page loads & it matches the events from incidents tab");
		Logger.info("***************************************");
		
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);	

		Logger.info("****Expected active tab Text:Users****");
		sAssert.assertEquals(detect.getActiveTabText(getWebDriver()),"Users",
				"Detect App-Users Tab: Active tab header text mismatch is seen");

		int count=detect.getTableCountUsersTab(getWebDriver());
		if(count>0){
			sAssert.assertTrue(count>0,
					"Detect App-Users Tab: Expected some rows in table of users but is seen empty");
			detect.clickARowInUsersTab(getWebDriver(), 1);

			Logger.info("****Expected active tab text in user detailed view:Threat Tree****");
			sAssert.assertEquals(detect.getActiveTabFromUsersDetailedView(getWebDriver()),"Threat Tree",
					"Detect App-Users Tab: Active tab header text mismatch is seen in users detailed view");

			detect.selectTabFromUsersDetailedView(getWebDriver(),"Incidents");
			Logger.info("****Expected active tab text in user detailed view:Incidents****");
			Assert.assertEquals(detect.getActiveTabFromUsersDetailedView(getWebDriver()),"Incidents",
					"Detect App-Users Tab: Incident tab is not getting selected");

			int incidentCount= detect.getIncidentsCountDetailedView(getWebDriver());
			int incidentListCount= detect.getIncidentsListCountDetailedView(getWebDriver());
			Logger.info("****Incident count:"+incidentCount+" and incident list count:"+incidentListCount+"****");
			sAssert.assertEquals(incidentCount,incidentListCount,
					"Detect App-Users Tab:Incident count mismatch is seen on incident tab of users detailed view");

			Assert.assertTrue(incidentCount>0&&incidentListCount>0, "Incident count must be greater than zero but was "+incidentCount);

			String[] incidentTimestamp = detect.getIncidentsTimeStampFromDetailedView(getWebDriver());
			String[] incidentService = detect.getIncidentsServiceFromDetailedView(getWebDriver());Arrays.sort(incidentService);
			String[] incidentDescription = detect.getIncidentsDescriptionFromDetailedView(getWebDriver());


			detect.clickViewAllIncidentsLink(getWebDriver());

			Logger.info("****Expected active tab Text:Incidents****");
			sAssert.assertEquals(detect.getActiveTabText(getWebDriver()),"Incidents",
					"Detect App-Incidents Tab: Active tab header text mismatch is seen after redirection");

			Assert.assertEquals(incidentCount,detect.getElementListInIncidentsTab(getWebDriver()),
					"Detect App-Incidents Tab:Incident list count mismatch is seen");

			int rCount=5;

			String[] incidentTimestampFromIncidentTab = detect.getIncidentsTimeStampFromIncidentsTab(getWebDriver(),rCount);
			String[] incidentServiceFromIncidentTab = detect.getIncidentsServiceFromIncidentsTab(getWebDriver());Arrays.sort(incidentServiceFromIncidentTab);
			String[] incidentDescriptionFromIncidentTab = detect.getIncidentsDescriptionFromIncidentsTab(getWebDriver(),rCount+1);

			Logger.info("**** Incident Details from User Detailed View ****");
			Logger.info(Arrays.asList(incidentTimestamp));Logger.info(Arrays.asList(incidentService));
			Logger.info(Arrays.asList(incidentDescription));
			Logger.info("***************************************************");

			Logger.info("**** Incident Details from Incidents Tab ****");
			Logger.info(Arrays.asList(incidentTimestampFromIncidentTab));Logger.info(Arrays.asList(incidentServiceFromIncidentTab));
			Logger.info(Arrays.asList(incidentDescriptionFromIncidentTab));
			Logger.info("***************************************************");

			sAssert.assertEquals(Arrays.asList(incidentTimestamp),Arrays.asList(incidentTimestampFromIncidentTab),
					"Detect App-Incidents Tab:Incident timestamp mismatch is seen");
			sAssert.assertEquals(Arrays.asList(incidentService),Arrays.asList(incidentServiceFromIncidentTab),
					"Detect App-Incidents Tab:Service type mismatch is seen");
			sAssert.assertEquals(detect.verifyIncidentDescription(incidentDescriptionFromIncidentTab, incidentDescription),"",
					"Detect App-Incidents Tab:Incident description mismatch is seen");



		}else{
			Assert.fail("Detect App-Users Tab: Expected some rows in table of users but is seen empty");
		}

		sAssert.assertAll();	
	}



	@Priority(3)
	@Test(groups = { "smoke","regression" })
	public void testUserFilterLowTabCounts() {
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);
		detect.validateDetectPage(getWebDriver());
		detect.validateDetectUsersTabFilterValidation(getWebDriver(),"Low");


	}
	@Priority(4)
	@Test(groups = { "smoke","regression" })
	public void testUserFilterMediunTabCounts() {
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);
		detect.validateDetectPage(getWebDriver());
		detect.validateDetectUsersTabFilterValidation(getWebDriver(),"Medium");	
	}
	@Priority(5)
	@Test(groups = { "smoke","regression" })
	public void testUserFilterHighTabCounts() {
		login.login(getWebDriver(), suiteData);
		dashboard.navigateToDetectDashboard(getWebDriver(), suiteData);
		detect.validateDetectPage(getWebDriver());
		detect.validateDetectUsersTabFilterValidation(getWebDriver(),"High");
	}


	@BeforeMethod(alwaysRun=true)
	public void testData(Method method, Object[] testData) {
		sAssert = new SoftAssert();
	}



}



