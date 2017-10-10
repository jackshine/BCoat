
package com.elastica.action.securlet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.action.universalapi.SaasType;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.securlets.SecurletDashboardPage;
import com.elastica.webelements.Element;

/**
 * Securlet common actions
 * @author eldorajan
 *
 */
public class SecurletAction extends Action{

	public String getSecurletHeader(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		String headerText="";
		try {
			headerText = sp.header(driver).getText().trim();Logger.info("Actual Header Text:"+headerText);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public void testShowing_20_of(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		hardWait(10); 
		sp.activitiesSearchHeader(driver).waitForLoading(driver, 10);
		String strHeaderString = sp.activitiesSearchHeader(driver).getText();
		Logger.info("strHeaderString   : "+ strHeaderString);
		String s = strHeaderString;
		s = s.substring(s.indexOf(" ") + 1);
		s = s.substring(0, s.indexOf(" "));
		System.out.println("Showing - "+s);
		if(Integer.parseInt(s)>=20){
			Assert.assertTrue(strHeaderString.contains("Showing 20 of"), "Header string mismatch #" + strHeaderString);
		}else
		{ 
			Assert.assertTrue(strHeaderString.contains("Showing "+s+" of "+s), "Header string is mismatching #" + strHeaderString);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}
	public void testShowing_30_of(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		hardWait(10); 
		sp.activitiesSearchHeader(driver).waitForLoading(driver, 10);
		String strHeaderString = sp.activitiesSearchHeader(driver).getText();
		Logger.info("strHeaderString   : "+ strHeaderString);
		String s = strHeaderString;
		s = s.substring(s.indexOf(" ") + 1);
		s = s.substring(0, s.indexOf(" "));
		System.out.println("Showing - "+s);
		if(Integer.parseInt(s)>=30){
			Assert.assertTrue(strHeaderString.contains("Showing 30 of"), "Header string is mismatching #" + strHeaderString);
		}else
		{ 
			Assert.assertTrue(strHeaderString.contains("Showing "+s+" of "+s), "Header string is mismatching #" + strHeaderString);
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}
	public String getActiveTabText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		hardWait(20);
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		String headerText="";
		try {
			headerText = sp.activeTab(driver).getText().trim();Logger.info("Actual Active Tab Text:"+headerText);

			if (sp.topOtherRiskShowOverviewVideo(driver).isElementPresent(driver)){
				if (sp.topOtherRiskShowOverviewVideo(driver).getText().contains("Hide overview video")){
					sp.topOtherRiskShowOverviewVideo(driver).click();
				}
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public void clickTabSecurletDashboard(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName() + " - "+ type);

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> securletTabs  = sp.securletTabs(driver).getChildElements();		
			for(int i=0;i<securletTabs.size();i++){
				if(securletTabs.get(i).getText().trim().contains(type)){
					securletTabs.get(i).click();

					hardWait(10);
					break;
				}
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName() + " - "+ type+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName() + " - "+ type);


	}

	public int countSecurletTabs(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		int count=0;
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> securletTabs  = sp.securletTabs(driver).getChildElements();		
			count = securletTabs.size();
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return count;
	}


	public String verifyExposuresWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			message += (sp.exposedFilesExposuresWidget(driver).isElementVisible()) ? "": "Exposures widget is not visible";
			message += (sp.exposedFilesTopRisksTypesWidget(driver).isElementVisible()) ? "": "Top risks widget is not visible";
			message += (sp.exposedFilesTopContentTypesWidget(driver).isElementVisible()) ? "": "Top content types widget is not visible";

			message += (sp.exposedFilesExposuresWidgetHeader(driver).getText().trim().equalsIgnoreCase("Exposures")) ? "": 
				"Exposures widget text not matching Expected:Exposures and Actual:"+sp.exposedFilesExposuresWidgetHeader(driver).getText().trim();
			message += (sp.exposedFilesTopRisksTypesWidgetHeader(driver).getText().trim().equalsIgnoreCase("Top Risk Types")) ? "": 
				"Top risks widget text not matching Expected:Top Risk Types and Actual:"+sp.exposedFilesTopRisksTypesWidgetHeader(driver).getText().trim();
			message += (sp.exposedFilesTopContentTypesWidgetHeader(driver).getText().trim().equalsIgnoreCase("Top Content Types")) ? "": 
				"Top content types widget text not matching Expected:Top Content Types and Actual:"+sp.exposedFilesTopContentTypesWidgetHeader(driver).getText().trim();

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String[] getTextFromVennCircle(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		List<String> circleText = new ArrayList<String>();

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesExposuresWidgetCircles = sp.exposedFilesExposuresWidgetCircles(driver).getChildElements();

			for(int i=0;i<exposedFilesExposuresWidgetCircles.size();i++){
				String[] temp = exposedFilesExposuresWidgetCircles.get(i).getAttribute("tooltip-title").trim().split(":");
				circleText.add(temp[0]);
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return circleText.toArray(new String[circleText.size()]);
	}


	public Integer[] getNumberFromVennCircle(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		List<Integer> circleNumber = new ArrayList<Integer>();

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesExposuresWidgetCircles = sp.exposedFilesExposuresWidgetCircles(driver).getChildElements();

			for(int i=0;i<exposedFilesExposuresWidgetCircles.size();i++){
				String[] temp = exposedFilesExposuresWidgetCircles.get(i).getAttribute("tooltip-title").trim().split(":");
				circleNumber.add(Integer.parseInt(temp[1].replaceAll(" ","").replaceAll(",","")));
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return circleNumber.toArray(new Integer[circleNumber.size()]);
	}

	public String[] getTextFromWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		List<String> circleText = new ArrayList<String>();

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesExposuresWidgetText = sp.exposedFilesExposuresWidgetText(driver).getChildElements();

			for(int i=0;i<exposedFilesExposuresWidgetText.size();i++){
				circleText.add(exposedFilesExposuresWidgetText.get(i).getText().trim());
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return circleText.toArray(new String[circleText.size()]);
	}


	public Integer[] getNumberFromWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		List<Integer> circleNumber = new ArrayList<Integer>();

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesExposuresWidgetNumbers = sp.exposedFilesExposuresWidgetNumbers(driver).getChildElements();

			for(int i=0;i<exposedFilesExposuresWidgetNumbers.size();i++){
				circleNumber.add(Integer.parseInt(exposedFilesExposuresWidgetNumbers.get(i).getText().trim().replaceAll(" ","").replaceAll(",","")));
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return circleNumber.toArray(new Integer[circleNumber.size()]);
	}



	public String verifyTopRiskTypesWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesTopRisksTypesWidgetList = sp.exposedFilesTopRisksTypesWidgetList(driver).getChildElements();
			List<Element> exposedFilesTopRisksTypesWidgetListIcon = sp.exposedFilesTopRisksTypesWidgetListIcon(driver).getChildElements();
			List<Element> exposedFilesTopRisksTypesWidgetListInfoHeader = sp.exposedFilesTopRisksTypesWidgetListInfoHeader(driver).getChildElements();
			List<Element> exposedFilesTopRisksTypesWidgetListInfoFiles = sp.exposedFilesTopRisksTypesWidgetListInfoFiles(driver).getChildElements();
			List<Element> exposedFilesTopRisksTypesWidgetListUsageBar = sp.exposedFilesTopRisksTypesWidgetListUsageBar(driver).getChildElements();

			for(int i=0;i<exposedFilesTopRisksTypesWidgetList.size();i++){
				message += (exposedFilesTopRisksTypesWidgetList.get(i).isElementVisible()) ? "": "Top risk types list element "+(i+1)+" is not visible";
				message += (exposedFilesTopRisksTypesWidgetListIcon.get(i).isElementVisible()) ? "": "Top risk types list icon element "+(i+1)+" is not visible";
				message += (exposedFilesTopRisksTypesWidgetListInfoHeader.get(i).isElementVisible()) ? "": "Top risk types list header element "+(i+1)+" is not visible";
				message += (exposedFilesTopRisksTypesWidgetListInfoFiles.get(i).isElementVisible()) ? "": "Top risk types list number of files element "+(i+1)+" is not visible";
				message += (exposedFilesTopRisksTypesWidgetListUsageBar.get(i).isElementVisible()) ? "": "Top risk types list usage bar element "+(i+1)+" is not visible";

				String text = exposedFilesTopRisksTypesWidgetListInfoHeader.get(i).getText().trim();
				message += (!text.isEmpty()) ? "": "Top risk types list header element "+(i+1)+" is empty";

				text=exposedFilesTopRisksTypesWidgetListInfoFiles.get(i).getText().trim();
				int count = Integer.parseInt(text.replace("Files", "").replaceAll(" ","").replaceAll(",",""));
				message += (count>0) ? "": "Top risk types number of files"+(i+1)+" is zero";
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyTopContentTypesWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesTopContentTypesWidgetList = sp.exposedFilesTopContentTypesWidgetList(driver).getChildElements();
			List<Element> exposedFilesTopContentTypesWidgetListIcon = sp.exposedFilesTopContentTypesWidgetListIcon(driver).getChildElements();
			List<Element> exposedFilesTopContentTypesWidgetListInfoHeader = sp.exposedFilesTopContentTypesWidgetListInfoHeader(driver).getChildElements();
			List<Element> exposedFilesTopContentTypesWidgetListInfoFiles = sp.exposedFilesTopContentTypesWidgetListInfoFiles(driver).getChildElements();
			List<Element> exposedFilesTopContentTypesWidgetListUsageBar = sp.exposedFilesTopContentTypesWidgetListUsageBar(driver).getChildElements();

			for(int i=0;i<exposedFilesTopContentTypesWidgetList.size();i++){
				message += (exposedFilesTopContentTypesWidgetList.get(i).isElementVisible()) ? "": "Top content types list element "+(i+1)+" is not visible";
				message += (exposedFilesTopContentTypesWidgetListIcon.get(i).isElementVisible()) ? "": "Top content types list icon element "+(i+1)+" is not visible";
				message += (exposedFilesTopContentTypesWidgetListInfoHeader.get(i).isElementVisible()) ? "": "Top content types list header element "+(i+1)+" is not visible";
				message += (exposedFilesTopContentTypesWidgetListInfoFiles.get(i).isElementVisible()) ? "": "Top content types list number of files element "+(i+1)+" is not visible";
				message += (exposedFilesTopContentTypesWidgetListUsageBar.get(i).isElementVisible()) ? "": "Top content types list usage bar element "+(i+1)+" is not visible";

				String text = exposedFilesTopContentTypesWidgetListInfoHeader.get(i).getText().trim();
				message += (!text.isEmpty()) ? "": "Top content types list header element "+(i+1)+" is empty";

				text=exposedFilesTopContentTypesWidgetListInfoFiles.get(i).getText().trim();
				int count = Integer.parseInt(text.replace(" Files", "").replace(" Docs", ""));
				message += (count>0) ? "": "Top content types number of files"+(i+1)+" is zero";
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}


	public String verifyExposedFilesListTableHeader(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesFilesTableInternalExposuresColumns = 
					sp.exposedFilesFilesTableInternalExposuresColumns(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresColumnsName = 
					sp.exposedFilesFilesTableInternalExposuresColumnsName(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresColumnsSubHeading = 
					sp.exposedFilesFilesTableInternalExposuresColumnsSubHeading(driver).getChildElements();

			for(int i=0;i<exposedFilesFilesTableInternalExposuresColumns.size();i++){
				message += (exposedFilesFilesTableInternalExposuresColumns.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures column list element "+(i+1)+" is not visible";
			}
			for(int i=0;i<exposedFilesFilesTableInternalExposuresColumnsName.size();i++){
				if(i==0){ message += (!exposedFilesFilesTableInternalExposuresColumnsName.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures column list element name "+(i+1)+" is visible";}
				else{ message += (exposedFilesFilesTableInternalExposuresColumnsName.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures column list element name "+(i+1)+" is not visible";}
				Logger.info(exposedFilesFilesTableInternalExposuresColumnsName.get(i).getText().trim());
			}
			for(int i=0;i<exposedFilesFilesTableInternalExposuresColumnsSubHeading.size();i++){
				message += (exposedFilesFilesTableInternalExposuresColumnsSubHeading.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures sub heading column list element subheading"+(i+1)+" is not visible";
				Logger.info(exposedFilesFilesTableInternalExposuresColumnsSubHeading.get(i).getText().trim());
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedFilesListTableBody(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> exposedFilesFilesTableInternalExposuresRows = 
					sp.exposedFilesFilesTableInternalExposuresRows(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowTitles = 
					sp.exposedFilesFilesTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowIcon = 
					sp.exposedFilesFilesTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowName = 
					sp.exposedFilesFilesTableInternalExposuresRowName(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowOwner = 
					sp.exposedFilesFilesTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowCount = 
					sp.exposedFilesFilesTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowSize = 
					sp.exposedFilesFilesTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowRisks = 
					sp.exposedFilesFilesTableInternalExposuresRowRisks(driver).getChildElements();
			List<Element> exposedFilesFilesTableInternalExposuresRowTypes = 
					sp.exposedFilesFilesTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<exposedFilesFilesTableInternalExposuresRows.size();i++){
				message += (exposedFilesFilesTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row file list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row list icon element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row name list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row owner list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row file size list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row risks list element "+(i+1)+" is not visible";
				message += (exposedFilesFilesTableInternalExposuresRowTypes.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures row types list element "+(i+1)+" is not visible";


				Logger.info(exposedFilesFilesTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						exposedFilesFilesTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						exposedFilesFilesTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						exposedFilesFilesTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						exposedFilesFilesTableInternalExposuresRowSize.get(i).getText().trim());
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyOtherRisks_TopRiskTypesWidget(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		List<Element> otherRisksTopRiskTypeRowsCount = sp.topRiskTypeRowsCount(driver).getChildElements();

		for(int i=0;i<otherRisksTopRiskTypeRowsCount.size();i++){
			message += (otherRisksTopRiskTypeRowsCount.get(i).isElementVisible()) ? "": 
				"Top Risk Type Rows list element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopRiskTypeRowsTopFileTypesIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Icon element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopRiskTypeRowsTopFileTypesHeadle(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Headle element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopRiskTypeRowsTopFileTypesFilesCounts(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files Counts element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopRiskTypeRowsTopFileTypesBarIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files BarIcon element "+(i+1)+" is not visible";

			Logger.info(otherRisksTopRiskTypeRowsCount.get(i).getText().trim() +"\t"+
					sp.otherRisksTopRiskTypeRowsTopFileTypesIcon(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopRiskTypeRowsTopFileTypesHeadle(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopRiskTypeRowsTopFileTypesFilesCounts(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopRiskTypeRowsTopFileTypesBarIcon(driver,i+1).getText().trim());

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifyOtherRisks_TopFileTypesWidget(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		List<Element> topRiskTypeRowsCount = sp.topRiskTypeRowsCount(driver).getChildElements();


		for(int i=0;i<topRiskTypeRowsCount.size();i++){
			message += (topRiskTypeRowsCount.get(i).isElementVisible()) ? "": 
				"Top Risk Type Rows list element "+(i+1)+" is not visible";
			message += (sp.topRiskTypeRowsTopFileTypesIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Icon element "+(i+1)+" is not visible";
			message += (sp.topRiskTypeRowsTopFileTypesHeadle(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Headle element "+(i+1)+" is not visible";
			message += (sp.topRiskTypeRowsTopFileTypesFilesCounts(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files Counts element "+(i+1)+" is not visible";
			message += (sp.topRiskTypeRowsTopFileTypesBarIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files BarIcon element "+(i+1)+" is not visible";

			Logger.info(topRiskTypeRowsCount.get(i).getText().trim() +"\t"+
					sp.topRiskTypeRowsTopFileTypesIcon(driver,i+1).getText().trim() +"\t"+
					sp.topRiskTypeRowsTopFileTypesHeadle(driver,i+1).getText().trim() +"\t"+
					sp.topRiskTypeRowsTopFileTypesFilesCounts(driver,i+1).getText().trim() +"\t"+
					sp.topRiskTypeRowsTopFileTypesBarIcon(driver,i+1).getText().trim());

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}

	public String verifyOtherRisks_TopContentTypesWidget(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		List<Element> otherRisksTopContentTypeRowsCount = sp.otherRisksTopContentTypeRowsCount(driver).getChildElements();


		for(int i=0;i<otherRisksTopContentTypeRowsCount.size();i++){
			message += (otherRisksTopContentTypeRowsCount.get(i).isElementVisible()) ? "": 
				"Top Risk Type Rows list element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopContentTypeRowsTopFileTypesIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Icon element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopContentTypeRowsTopFileTypesHeadle(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Headle element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopContentTypeRowsTopFileTypesFilesCounts(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files Counts element "+(i+1)+" is not visible";
			message += (sp.otherRisksTopContentTypeRowsTopFileTypesBarIcon(driver,i+1).isElementVisible()) ? "": 
				"Top Risk Type Rows Top File Types Files BarIcon element "+(i+1)+" is not visible";

			Logger.info(otherRisksTopContentTypeRowsCount.get(i).getText().trim() +"\t"+
					sp.otherRisksTopContentTypeRowsTopFileTypesIcon(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopContentTypeRowsTopFileTypesHeadle(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopContentTypeRowsTopFileTypesFilesCounts(driver,i+1).getText().trim() +"\t"+
					sp.otherRisksTopContentTypeRowsTopFileTypesBarIcon(driver,i+1).getText().trim());

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}

	public String verifyOtherRisksTab_ExportCSV_AlertMessage(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		Assert.assertTrue(sp.topOtherRiskExportCsv(driver).isElementVisible(), "Export CSV Button is not visible");
		hardWait(10);
		sp.topOtherRiskExportCsv(driver).mouseOverClick(driver);
		sp.topOtherRiskExportCsvAlertMessage(driver).waitForLoading(driver, 5);
		if (sp.topOtherRiskExportCsvAlertMessage(driver).isElementPresent(driver)){
			if (sp.topOtherRiskExportCsvAlertMessage(driver).isElementVisible()){
				String strErrorMsg = sp.topOtherRiskExportCsvAlertMessage(driver).getText().trim();
				Logger.info("Error msg : "+ strErrorMsg);
				Assert.assertEquals(strErrorMsg,"Your request has been received. Exported file will be sent to you via email.", "Export CSV Alert Message not matching");
			}else{
				sp.topOtherRiskExportCsv(driver).mouseOverClick(driver);
				sp.topOtherRiskExportCsvAlertMessage(driver).waitForLoading(driver, 5);
				Assert.assertTrue(sp.topOtherRiskExportCsvAlertMessage(driver).isElementVisible(), "Export CSV Alert Message not visible");
			}
		}else{
			sp.topOtherRiskExportCsv(driver).mouseOverClick(driver);
			sp.topOtherRiskExportCsvAlertMessage(driver).waitForLoading(driver, 5);
			Assert.assertTrue(sp.topOtherRiskExportCsvAlertMessage(driver).isElementVisible(), "Export CSV Alert Message not visible");
		}
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	public String verifyMoveToExteralTab(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			WebElement source = driver.findElement(By.cssSelector(".slider-track>.slider-handle:nth-child(2)"));
			WebElement target = driver.findElement(By.cssSelector(".e-slider-label.ng-scope:last-child"));
			Actions builder = new Actions(driver);
			org.openqa.selenium.interactions.Action dragAndDrop = builder.clickAndHold(source)
					.moveToElement(target)
					.release(target)
					.build();
			dragAndDrop.perform();
			Logger.info("Now we are in External page");
		} catch (Exception ex) {
			Assert.fail("Issue with Cisco Login Page Operation " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}

	public String verifyOtherRisks_ShowOverviewVideo(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			Assert.assertTrue(sp.topOtherRiskShowOverviewVideo(driver).isElementVisible(), "Show Overview Video link is not visible");
			sp.topOtherRiskShowOverviewVideo(driver).click();
			Assert.assertTrue(sp.topOtherRiskVideo(driver).isElementVisible(), "Video not visible");
			Assert.assertTrue(sp.topOtherRiskShowOverviewVideo(driver).isElementVisible(), "Show Overview Video link is not visible");
			sp.topOtherRiskShowOverviewVideo(driver).click();
			//Assert.assertTrue(sp.topOtherRiskShowOverviewVideo(driver).isElementVisible(), "Video is visible");

		} catch (Exception ex) {
			Assert.fail("Issue with Cisco Login Page Operation " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}
	public String verifyOtherRisksTableHeader(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> otherRisksTableInternalExposuresColumns = 
					sp.otherRisksTableInternalExposuresColumns(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresColumnsName = 
					sp.otherRisksTableInternalExposuresColumnsName(driver).getChildElements();


			for(int i=0;i<otherRisksTableInternalExposuresColumns.size();i++){
				message += (otherRisksTableInternalExposuresColumns.get(i).isElementVisible()) ? "": 
					"Other Risks internal exposures column list element "+(i+1)+" is not visible";
			}
			for(int i=0;i<otherRisksTableInternalExposuresColumnsName.size();i++){
				if(i==0){ message += (!otherRisksTableInternalExposuresColumnsName.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures column list element name "+(i+1)+" is visible";}
				else{ message += (otherRisksTableInternalExposuresColumnsName.get(i).isElementVisible()) ? "": 
					"Exposed Files internal exposures column list element name "+(i+1)+" is not visible";}
				Logger.info(otherRisksTableInternalExposuresColumnsName.get(i).getText().trim());
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowTitles = 
					sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowIcon = 
					sp.otherRisksTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowName = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowOwner = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowCount = 
					sp.otherRisksTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowRisks = 
					sp.otherRisksTableInternalExposuresRowRisks(driver).getChildElements();
			sp.otherRisksTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<otherRisksTableInternalExposuresRows.size();i++){
				message += (otherRisksTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row file list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row list icon element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row name list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row owner list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row file size list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row risks list element "+(i+1)+" is not visible";

				Logger.info(otherRisksTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowSize.get(i).getText().trim());

			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyOtherRisksTableDetailPage(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowTitles = 
					sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowIcon = 
					sp.otherRisksTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowName = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowOwner = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowCount = 
					sp.otherRisksTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowRisks = 
					sp.otherRisksTableInternalExposuresRowRisks(driver).getChildElements();
			sp.otherRisksTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<otherRisksTableInternalExposuresRows.size();i++){
				message += (otherRisksTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row file list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row list icon element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row name list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row owner list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row file size list element "+(i+1)+" is not visible";
				message += (otherRisksTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"otherRisks internal exposures row risks list element "+(i+1)+" is not visible";

				String strRowName =otherRisksTableInternalExposuresRowName.get(i).getText().trim();
				String strRowOwner =otherRisksTableInternalExposuresRowOwner.get(i).getText().trim();
				otherRisksTableInternalExposuresRowCount.get(i).getText().trim();
				String strRowSize =otherRisksTableInternalExposuresRowSize.get(i).getText().trim();

				Logger.info(otherRisksTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowSize.get(i).getText().trim());

				otherRisksTableInternalExposuresRows.get(i).click();
				hardWait(5);

				Logger.info("otherRisksPopupRowTitles : "+sp.otherRisksPopupRowTitles(driver).getInnerHtml());
				Logger.info("otherRisksPopupRowOwner  : "+sp.otherRisksPopupRowOwner(driver).getInnerHtml());
				Logger.info("otherRisksPopupRowCount  : "+sp.otherRisksPopupRowCount(driver).getText());
				Logger.info("otherRisksPopupRowSize   : "+sp.otherRisksPopupRowSize(driver).getInnerHtml());


				Assert.assertTrue(strRowName.contains(sp.otherRisksPopupRowTitles(driver).getInnerHtml().trim()), "Row Name Not maching");
				Assert.assertEquals(sp.otherRisksPopupRowOwner(driver).getInnerHtml().trim(),strRowOwner, "Row Owner Not maching");
				//Assert.assertEquals(sp.otherRisksPopupRowCount(driver).getInnerHtml().trim(),strRowCount, "Row Count Not maching");
				Assert.assertEquals(sp.otherRisksPopupRowSize(driver).getInnerHtml().trim(),strRowSize, "Row Size Not maching");
				sp.otherRisksPopupClose(driver).click();
				hardWait(5);
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyOtherRisks_LearnMore(WebDriver driver,String type) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			Assert.assertTrue(sp.topOtherRiskLearnMore(driver).isElementVisible(), "Show Learn More link is not visible");
			sp.topOtherRiskLearnMore(driver).click();
			hardWait(30);
			Assert.assertTrue(sp.topOtherRiskLearnMoreHandle(driver).isElementVisible(), "Learn More not visible");
			Assert.assertEquals(sp.topOtherRiskLearnMoreHandle(driver).getText().trim(),"Securlet™ for "+type, "Securlet Message not matching");

		} catch (Exception ex) {
			Assert.fail("Issue with Cisco Login Page Operation " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}

	public String verifyOtherRisks_Options(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {

			Assert.assertTrue(sp.topOtherOptions(driver).isElementVisible(), "Show Options link is not visible");
			sp.topOtherOptions(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherScanNow(driver).isElementVisible(), "Scan Now not visible");
			Assert.assertEquals(sp.topOtherScanNow(driver).getText().trim(),"Scan Now", "Scan Now not matching");

			Assert.assertTrue(sp.topOtherViewScanPolicies(driver).isElementVisible(), "View Scan Policies not visible");
			Assert.assertEquals(sp.topOtherViewScanPolicies(driver).getText().trim(),"View Scan Policies", "Scan Now not matching");

			sp.topOtherViewScanPolicies(driver).click();

			Assert.assertTrue(sp.topOtherViewScanPoliciesHeader(driver).isElementVisible(), "View Scan Policies Header not visible");
			Assert.assertEquals(sp.topOtherViewScanPoliciesHeader(driver).getText().trim(),"Scan Policies", "Scan Now not matching");

			Assert.assertTrue(sp.topOtherViewScanPoliciesPopupClose(driver).isElementVisible(), "View Scan Policies Header Close button not visible");
			sp.topOtherViewScanPoliciesPopupClose(driver).click();

		} catch (Exception ex) {
			Assert.fail("Issue with Cisco Login Page Operation " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;

	}

	public String verifyExposedOtherRisksTableBody_FilterDocument(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowName = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();
			String strFilterValue=otherRisksTableInternalExposuresRowName.get(1).getText().trim();
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterTestBox(driver).type(strFilterValue);
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterButton(driver).click();
			hardWait(30);
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowTitles = 
					sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();

			int rowCount = otherRisksTableInternalExposuresRows.size();
			Logger.info("rowCount : "+rowCount);
			for(int i=0;i<rowCount;i++){

				String strRowName =otherRisksTableInternalExposuresRowName.get(i+1).getText().trim();		

				Logger.info("strFilterValue : "+strFilterValue);
				Logger.info("strRowName     : "+strRowName);

				Logger.info(otherRisksTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowName.get(i+1).getText().trim() +"\t");
				Assert.assertEquals(strFilterValue,strRowName, "Row Doument Not maching");

			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_FilterOwner(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowOwner = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();
			String strFilterValue=otherRisksTableInternalExposuresRowOwner.get(1).getText().trim();
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterTestBox(driver).type(strFilterValue);
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterButton(driver).click();
			hardWait(30);
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowTitles = 
					sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();

			int rowCount = otherRisksTableInternalExposuresRows.size();
			Logger.info("rowCount : "+rowCount);

			for(int i=0;i<rowCount;i++){

				String strOwer =otherRisksTableInternalExposuresRowOwner.get(i+1).getText().trim();	

				Logger.info("strFilterValue : "+strFilterValue);
				Logger.info("strOwer        : "+strOwer);

				Logger.info(otherRisksTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowOwner.get(i+1).getText().trim() +"\t");
				Assert.assertEquals(strFilterValue,strOwer, "Owner Doument Not maching");

			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_FilterSize(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();
			String strFilterValue=otherRisksTableInternalExposuresRowSize.get(1).getText().trim();
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterTestBox(driver).type(strFilterValue);
			//hardWait(30);
			sp.topOtherViewScanPoliciesFilterButton(driver).click();
			hardWait(30);
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRowTitles = 
					sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();

			int rowCount = otherRisksTableInternalExposuresRows.size();
			Logger.info("rowCount : "+rowCount);

			for(int i=0;i<rowCount;i++){

				String strSize =otherRisksTableInternalExposuresRowSize.get(i+1).getText().trim();

				Logger.info("strFilterValue : "+strFilterValue);
				Logger.info("strSize        : "+strSize);


				Logger.info(otherRisksTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						otherRisksTableInternalExposuresRowSize.get(i+1).getText().trim() +"\t");
				Assert.assertEquals(strFilterValue,strSize, "Size Doument Not maching");

			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_FilterToggleTab(WebDriver driver,String type) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			Assert.assertTrue(sp.topOtherClearFiltersButton(driver).isElementVisible(), "Clear Filters Button not visible");

			if(type.contains("Office 365")||type.contains("Google Apps")){
				Assert.assertTrue(sp.topOtherAppTab(driver).isElementVisible(), "App Tab not visible");
				Assert.assertEquals(sp.topOtherAppTab(driver).getText().trim(),"App", "App Tab not matching");
			} 

			//Assert.assertTrue(sp.topOtherRiskTypeTab(driver).isElementVisible(), "RiskType Tab not visible");
			//Assert.assertEquals(sp.topOtherRiskTypeTab(driver).getText().trim(),"Risk Type", "RiskType Tab not matching");
			Assert.assertTrue(sp.topOtherContentIQProfileTab(driver).isElementVisible(), "ContentIQProfile Tab not visible");
			Assert.assertEquals(sp.topOtherContentIQProfileTab(driver).getText().trim(),"ContentIQ Profile", "ContentIQProfile Tab not matching");
			Assert.assertTrue(sp.topOtherContentTypeTab(driver).isElementVisible(), "ContentType Tab not visible");
			Assert.assertEquals(sp.topOtherContentTypeTab(driver).getText().trim(),"Content Type", "Content Type Tab not matching");
			Assert.assertTrue(sp.topOtherFileTypesTab(driver).isElementVisible(), "FileTypes Tab not visible");
			Assert.assertEquals(sp.topOtherFileTypesTab(driver).getText().trim(),"File Classes", "FileTypes Tab not matching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_SizeSort(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();
			int rowCount = otherRisksTableInternalExposuresRows.size();
			for(int i=0;i<rowCount;i++){
				String strSize =otherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("Without sort - : " + i +" : "+ strSize);
			}

			sp.topOtherTableSize(driver).click();
			sp.topOtherSizeSortAsc(driver).click();

			hardWait(30);

			List<Element> sortedAscOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strSize =sortedAscOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Asc -   : " + i +" : "+ strSize);
			}

			sp.topOtherSizeSortDesc(driver).click();
			hardWait(30);

			List<Element> sortedDescOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowSize(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strSize =sortedDescOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Desc   : " + i +" : "+ strSize);
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_OwnerSort(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowOwner = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();
			int rowCount = otherRisksTableInternalExposuresRows.size();
			for(int i=0;i<rowCount;i++){
				String strOwner =otherRisksTableInternalExposuresRowOwner.get(i).getText().trim();
				Logger.info("Without sort - : " + i +" : "+ strOwner);
			}

			sp.topOtherTableOwner(driver).click();
			sp.topOtherSizeSortAsc(driver).click();

			hardWait(30);

			List<Element> sortedAscOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strOwner =sortedAscOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Asc -   : " + i +" : "+ strOwner);
			}

			sp.topOtherSizeSortDesc(driver).click();
			hardWait(30);

			List<Element> sortedDescOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowOwner(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strOwner =sortedDescOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Desc   : " + i +" : "+ strOwner);
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_DocumentSort(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			List<Element> otherRisksTableInternalExposuresRowName = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();
			List<Element> otherRisksTableInternalExposuresRows = 
					sp.otherRisksTableInternalExposuresRows(driver).getChildElements();
			sp.otherRisksTableInternalExposuresRowTitles(driver).getChildElements();
			int rowCount = otherRisksTableInternalExposuresRows.size();
			for(int i=0;i<rowCount;i++){
				String strDocument =otherRisksTableInternalExposuresRowName.get(i).getText().trim();
				Logger.info("Without sort - : " + i +" : "+ strDocument);
			}

			sp.topOtherTableDocument(driver).click();
			sp.topOtherSizeSortAsc(driver).click();

			hardWait(30);

			List<Element> sortedAscOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strDocument =sortedAscOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Asc -   : " + i +" : "+ strDocument);
			}

			sp.topOtherSizeSortDesc(driver).click();
			hardWait(30);

			List<Element> sortedDescOtherRisksTableInternalExposuresRowSize = 
					sp.otherRisksTableInternalExposuresRowName(driver).getChildElements();

			for(int i=0;i<rowCount;i++){
				String strDocument =sortedDescOtherRisksTableInternalExposuresRowSize.get(i).getText().trim();
				Logger.info("After Desc   : " + i +" : "+ strDocument);
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_TapContentType(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.otherRisksaTopContentTypeTabUserTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);
			sp.otherRisksaTopContentTypeTabUserTableFirstRow(driver).click();
			String streCategory = sp.otherRisksaTopContentTypeTabUserTableFirstRowName(driver).getText();
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			String strReplaceTabUserTableFirstRowCount = strTabUserTableFirstRowCount.replace(",", "").replace("Docs","");
			String strReplaceAfterFilterHeaderString = strAfterFilterHeaderString.replace(",", "");
			Assert.assertTrue(strReplaceAfterFilterHeaderString.contains(strReplaceTabUserTableFirstRowCount.trim()), "Header count after filter is mismatching");
			List<Element> topOtherTableDocumentCategory = 
					sp.topOtherTableDocumentCategory(driver).getChildElements();
			int rowCount = topOtherTableDocumentCategory.size();
			for(int i=0;i<rowCount;i++){
				String strTableeCategory =topOtherTableDocumentCategory.get(i).getText().trim();
				Logger.info("strECategory   : " + i +" : "+ strTableeCategory);
				Assert.assertTrue(strTableeCategory.contains(streCategory), "ECategory not matching");			
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedUsersTableBody_TapInternalAndExternal(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.exposedUsersTabInternalCollaborators(driver).click();

			String strChatTotalUserCount=sp.exposedUsersDoughnutChartTotalUserCount(driver).getText().trim();
			Logger.info("strChatTotalUserCount   : "+ strChatTotalUserCount);
			String strChatInternalUserCount=sp.exposedUsersDoughnutChartInternalUserCount(driver).getText().trim();
			Logger.info("strInternalUserCount   : "+ strChatInternalUserCount);
			String strChatExternalUserCount=sp.exposedUsersDoughnutChartExternalUserCount(driver).getText().trim();
			Logger.info("strChatExternalUserCount   : "+ strChatExternalUserCount);
			List<Element> exposedUsersTableInternalUserCount = 
					sp.exposedUsersTableInternalUserCount(driver).getChildElements();
			int rowTableInterUserCount = exposedUsersTableInternalUserCount.size();
			Logger.info("rowTableInterUserCount   : "+ rowTableInterUserCount);

			sp.exposedUsersTabExternalCollaborators(driver).click();

			List<Element> exposedUsersTableExternalUserCount = 
					sp.exposedUsersTableExternalUserCount(driver).getChildElements();
			int rowTableExternalUserCount = exposedUsersTableExternalUserCount.size() - rowTableInterUserCount ;
			Logger.info("rowTableExternalUserCount   : "+ rowTableExternalUserCount);

			Assert.assertEquals(strChatInternalUserCount,Integer.toString(rowTableInterUserCount), "Internal User Count not matching");		
			Assert.assertEquals(strChatExternalUserCount,Integer.toString(rowTableExternalUserCount), "External User Count not matching");		

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.exposedUsersTabExternalAppTableFirstRowCheckBox(driver).click();
			hardWait(10);
			sp.exposedUsersTabInternalCollaborators(driver).click();

			String strAfterFilterChatTotalUserCount=sp.exposedUsersDoughnutChartTotalUserCount(driver).getText().trim();
			Logger.info("strAfterFilterChatTotalUserCount   : "+ strAfterFilterChatTotalUserCount);
			String strAfterFilterChatInternalUserCount=sp.exposedUsersDoughnutChartInternalUserCount(driver).getText().trim();
			Logger.info("strAfterFilterChatInternalUserCount   : "+ strAfterFilterChatInternalUserCount);
			String strAfterFilterChatExternalUserCount=sp.exposedUsersDoughnutChartExternalUserCount(driver).getText().trim();
			Logger.info("strAfterFilterChatExternalUserCount   : "+ strAfterFilterChatExternalUserCount);
			List<Element> exposedUsersTableAfterFilterInternalUserCount = 
					sp.exposedUsersTableInternalUserCount(driver).getChildElements();
			int rowTableAfterFilterInterUserCount = exposedUsersTableAfterFilterInternalUserCount.size() ;
			Logger.info("rowTableAfterFilterInterUserCount   : "+ rowTableAfterFilterInterUserCount);

			sp.exposedUsersTabExternalCollaborators(driver).click();

			List<Element> exposedUsersTableAfterFilterExternalUserCount = 
					sp.exposedUsersTableExternalUserCount(driver).getChildElements();
			int rowTableAfterFilterExternalUserCount = exposedUsersTableAfterFilterExternalUserCount.size() - rowTableAfterFilterInterUserCount;
			Logger.info("rowTableAfterFilterExternalUserCount   : "+ rowTableAfterFilterExternalUserCount);

			Assert.assertEquals(strAfterFilterChatInternalUserCount,Integer.toString(rowTableAfterFilterInterUserCount), "Internal User  After Filter Count not matching");		
			Assert.assertEquals(strAfterFilterChatExternalUserCount,Integer.toString(rowTableAfterFilterExternalUserCount), "External User After Filter Count not matching");		

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_SearchBox_ContentType(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.otherRisksaContentTypeTabUserTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);
			sp.otherRisksaContentTypeTabUserTableFirstRowCheckBox(driver).click();
			String streCategory = sp.otherRisksaContentTypeTabUserTableFirstRowName(driver).getText();
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			String strReplaceTabUserTableFirstRowCount = strTabUserTableFirstRowCount.replace(",", "");
			String strReplaceAfterFilterHeaderString = strAfterFilterHeaderString.replace(",", "");
			Assert.assertTrue(strReplaceAfterFilterHeaderString.contains(strReplaceTabUserTableFirstRowCount), 
					"Header count after filter is mismatching Expected:"+strReplaceTabUserTableFirstRowCount+
					" and Actual:"+strReplaceAfterFilterHeaderString);
			List<Element> topOtherTableDocumentCategory = 
					sp.topOtherTableDocumentCategory(driver).getChildElements();
			int rowCount = topOtherTableDocumentCategory.size();
			for(int i=0;i<rowCount;i++){
				String strTableeCategory =topOtherTableDocumentCategory.get(i).getText().trim();
				Logger.info("strECategory   : " + i +" : "+ strTableeCategory);
				Assert.assertTrue(strTableeCategory.contains(streCategory), 
						"ECategory not matching Expected:"+streCategory+" and Actual:"+strTableeCategory);			
			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_SearchBox_FileTypes(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strFileType=sp.topOtherFileTypesTabItemName(driver).getText().trim();
			sp.topOtherSearchBox(driver).type(strFileType);
			sp.topOtherSearchIcon(driver).click();
			hardWait(10);
			List<Element> topOtherTableEFileTypeIcon = 
					sp.topOtherTableEFileTypeIcon(driver).getChildElements();
			int rowCount = topOtherTableEFileTypeIcon.size();
			for(int i=0;i<rowCount;i++){
				String strTableEFileTypeIcon =topOtherTableEFileTypeIcon.get(i).getText().trim();
				Logger.info("strTableEFileTypeIcon   : " + i +" : "+ strTableEFileTypeIcon);
				Assert.assertTrue(strTableEFileTypeIcon.contains(strFileType), "File Icon Type not matching");
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_SearchBox_ContentIQProfile(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strContentIQProfile=sp.topOtherContentIQProfileTabItemName(driver).getText().trim();
			sp.topOtherSearchBox(driver).type(strContentIQProfile);
			sp.topOtherSearchIcon(driver).click();
			hardWait(10);
			List<Element> topOtherTableContentIQProfile = 
					sp.topOtherTableContentIQProfile(driver).getChildElements();
			int rowCount = topOtherTableContentIQProfile.size();
			for(int i=0;i<rowCount;i++){
				String strTableContentIQProfile =topOtherTableContentIQProfile.get(i).getAttribute("tooltip-title").trim();
				Logger.info("strTableContentIQProfile   : " + i +" : "+ strTableContentIQProfile);
				Assert.assertEquals(strTableContentIQProfile,"ContentIQ Profile", "File ContentIQ Profile not matching");			
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_SearchBox_RiskType(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.topOtherRiskTypeTabClick(driver).click();
			sp.topOtherRiskTypeTabItemNameClick(driver).click();

			//sp.topOtherSearchBoxClick(driver).type(strRiskType);
			String strRiskType=sp.topOtherSearchIconInHeader(driver).getText();
			hardWait(10);
			List<Element> topOtherTableRiskType = 
					sp.topOtherTableRiskType(driver).getChildElements();
			int rowCount = topOtherTableRiskType.size();
			for(int i=0;i<rowCount;i++){
				String strTableRiskType =topOtherTableRiskType.get(i).getAttribute("tooltip-title").trim();
				Logger.info("strTableRiskType   : " + i +" : "+ strTableRiskType);
				//Assert.assertEquals(strTableRiskType,strRiskType, "File Risk Type not matching");			
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedOtherRisksTableBody_SearchBoxTopRiskTypes(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strFileTypes=sp.topOtherTapFileTypesTabItemName(driver).getText().trim();
			sp.topOtherSearchBox(driver).type(strFileTypes);
			sp.topOtherSearchIcon(driver).click();
			hardWait(10);
			List<Element> topOtherTableEFileTypeIcon = 
					sp.topOtherTableEFileTypeIcon(driver).getChildElements();
			int rowCount = topOtherTableEFileTypeIcon.size();
			for(int i=0;i<rowCount;i++){
				String strTableEFileTypeIcon =topOtherTableEFileTypeIcon.get(i).getText().trim();
				Logger.info("strRiskTypes   : " + i +" : "+ strTableEFileTypeIcon);
				Assert.assertTrue(strTableEFileTypeIcon.contains(strFileTypes), "File Icon Type not matching");
				}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyExposedOtherRisksTableBody_SearchBoxTopFileTypes(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {

			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strRiskTypes=sp.topOtherTapFileTypesTabItemName(driver).getText().trim();
			sp.topOtherSearchBox(driver).type(strRiskTypes);
			sp.topOtherSearchIcon(driver).click();
			hardWait(10);
			List<Element> topOtherTableEFileTypeIcon = 
					sp.topOtherTableEFileTypeIcon(driver).getChildElements();
			int rowCount = topOtherTableEFileTypeIcon.size();
			for(int i=0;i<rowCount;i++){
				String strTableEFileTypeIcon =topOtherTableEFileTypeIcon.get(i).getText().trim();
				Logger.info("strTableEFileTypeIcon   : " + i +" : "+ strTableEFileTypeIcon);
				Assert.assertTrue(strTableEFileTypeIcon.contains(strRiskTypes), "File Icon Type not matching");
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyActivitiesTab_FiltersUsersBox(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		testShowing_30_of(driver);
		sp.topOtherToggleTab(driver).click();
		hardWait(10);
		Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
		String strTabUserTableFirstRowCount = sp.activitiesTabUserTableFirstRowCount(driver).getText();

		sp.activitiesTabUserTableFirstRowCheckBox(driver).click();
		hardWait(15);
		strTabUserTableFirstRowCount = sp.activitiesTabUserTableFirstRowCount(driver).getText();
		String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
		/*String strReplaceTabUserTableFirstRowCount = strTabUserTableFirstRowCount.replace(",", "");
			String strReplaceAfterFilterHeaderString = strAfterFilterHeaderString.replace(",", "");*/

		int filterCount = Integer.parseInt(strTabUserTableFirstRowCount.replace(",", "").trim());
		int headerCount = Integer.parseInt(strAfterFilterHeaderString.trim().
				replaceFirst("Showing .* of ", "").replace(",", "")); 

		Logger.info("User Filter Event Count: "+ filterCount);
		Logger.info("Header Count after filter is selected: "+ headerCount);

		Assert.assertTrue((headerCount-filterCount)<10, "Header count after filter is mismatching"
				+ "Expected:"+filterCount+" and Actual:"+headerCount);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifyActivitiesTab_FiltersObjectBox(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			testShowing_30_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabObjectTableFirstRowCount = sp.activitiesTabObjectTableFirstRowCount(driver).getText();
			Logger.info("strTabObjectTableFirstRowCount   : "+ strTabObjectTableFirstRowCount);
			sp.activitiesTabObjectTableFirstRowCheckBox(driver).click();
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			Logger.info("strAfterFilterHeaderString   : "+ strAfterFilterHeaderString);
			Assert.assertTrue(strAfterFilterHeaderString.contains(strTabObjectTableFirstRowCount), "Header count after filter is mismatching");
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifySecurletDashboardActivitiesTab_UIFiltersAndUnFilters(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			testShowing_30_of(driver);
			String strHeaderString = sp.activitiesSearchHeader(driver).getText();
			Logger.info("strHeaderString   : "+ strHeaderString);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabObjectTableFirstRowCount = sp.activitiesTabObjectTableFirstRowCount(driver).getText();
			Logger.info("strTabObjectTableFirstRowCount   : "+ strTabObjectTableFirstRowCount);
			sp.activitiesTabObjectTableFirstRowCheckBox(driver).click();
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			Logger.info("strAfterFilterHeaderString   : "+ strAfterFilterHeaderString);
			Assert.assertTrue(strAfterFilterHeaderString.contains(strTabObjectTableFirstRowCount), "Header count after filter is mismatching");
			sp.activitiesTabObjectTableFirstRowCheckBox(driver).click();
			hardWait(10);
			String strHeaderStringAfterUncheck = sp.activitiesSearchHeader(driver).getText();
			Logger.info("strHeaderStringAfterUncheck   : "+ strHeaderStringAfterUncheck);
			Assert.assertEquals(strHeaderString,strHeaderStringAfterUncheck, "File strHeaderString not matching");	
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifySecurletDashboardActivitiesTab_FiltersSpecificUsersBox(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			testShowing_30_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");

			String strTabObjectTableFirstRowName = sp.activitiesTabUserTableFirstRowName(driver).getText();
			Logger.info("strTabObjectTableFirstRowName   : "+ strTabObjectTableFirstRowName);

			sp.topOtherSearchBox(driver).type(strTabObjectTableFirstRowName);
			System.out.println(sp.topOtherSearchIcon(driver).getTagName());
			sp.topOtherSearchIcon(driver).click();
			hardWait(10);

			List<Element> activitiesTabUserTableCount = 
					sp.activitiesTabUserTableCount(driver).getChildElements();
			int rowCount = activitiesTabUserTableCount.size();

			int intTotalCount = 0;

			for(int i=0;i<rowCount;i++){
				String strTableCount =sp.activitiesTabUserTableRowCount(driver,i+1).getText().trim();
				Logger.info("strTableCount   : " + i +" : "+ strTableCount.replaceAll(" ","").replaceAll(",",""));
				String test=strTableCount.replaceAll(" ","").replaceAll(",","");
				intTotalCount=Integer.parseInt(test)+intTotalCount;
				//Assert.assertEquals(strTableEFileTypeIcon,strRiskTypes, "File Icon Type not matching");			
			}
			Logger.info("strTableCount   : "+ intTotalCount);
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			String strAfterReplaceFilterHeaderString =strAfterFilterHeaderString.replace(",", "");
			Logger.info("strAfterFilterHeaderString   : "+ strAfterFilterHeaderString);
			Assert.assertTrue(strAfterReplaceFilterHeaderString.contains(Integer.toString(intTotalCount)), "Header count after filter is mismatching");

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifySecurletDashboardActivitiesTab_FiltersUsersBoxAndSeverityBoxAccordingly(WebDriver driver) {

		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		String message="";
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		try {
			testShowing_30_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.activitiesTabUserTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);
			sp.activitiesTabUserTableFirstRowCheckBox(driver).click();
			hardWait(10);
			sp.activitiesTabSeverity(driver).click();
			sp.activitiesTabSeverityTableFirstRowCheckBox(driver).click();hardWait(5);
			String strTabSeverityTableFirstRowCount = sp.activitiesTabSeverityTableFirstRowCount(driver).getText();
			Logger.info("strTabSeverityTableFirstRowCount   : "+ strTabSeverityTableFirstRowCount);
			hardWait(10);
			String strAfterFilterHeaderString = sp.activitiesSearchHeader(driver).getText();
			Assert.assertTrue(strAfterFilterHeaderString.contains(strTabSeverityTableFirstRowCount), 
					"Header count after filter is mismatching");
			Logger.info("strAfterFilterHeaderString   : "+ strAfterFilterHeaderString);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifySecurletDashboardExposedFilesTab_PopUpTable_ViewFileLink(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.exposedUsersTabExternalAppTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);
			sp.exposedUsersTabExternalAppTableFirstRowCheckBox(driver).click();
			hardWait(10);
			List<Element> exposedFilesTableInternalExposuresRows = 
					sp.exposedFilesTableInternalExposuresRows(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowTitles = 
					sp.exposedFilesTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowIcon = 
					sp.exposedFilesTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowName = 
					sp.exposedFilesTableInternalExposuresRowName(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowOwner = 
					sp.exposedFilesTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowCount = 
					sp.exposedFilesTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowSize = 
					sp.exposedFilesTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowRisks = 
					sp.exposedFilesTableInternalExposuresRowRisks(driver).getChildElements();
			sp.exposedFilesTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<exposedFilesTableInternalExposuresRows.size();i++){
				message += (exposedFilesTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list icon element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row name list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file size list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row risks list element "+(i+1)+" is not visible";

				String strRowName =exposedFilesTableInternalExposuresRowName.get(i).getText().trim();
				String strRowOwner =exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim();
				String strRowCount =exposedFilesTableInternalExposuresRowCount.get(i).getText().trim();
				String strRowSize =exposedFilesTableInternalExposuresRowSize.get(i).getText().trim();

				Logger.info(exposedFilesTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowSize.get(i).getText().trim());

				exposedFilesTableInternalExposuresRows.get(i).mouseOver(driver);
				exposedFilesTableInternalExposuresRows.get(i).click();
				hardWait(5);

				Logger.info("exposedFilesPopupRowTitles : "+sp.exposedFilesPopupRowTitles(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowOwner  : "+sp.exposedFilesPopupRowOwner(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowCount  : "+sp.exposedFilesPopupRowCount(driver).getText());
				Logger.info("exposedFilesPopupRowSize   : "+sp.exposedFilesPopupRowSize(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowFileView   : "+sp.exposedFilesPopupRowPrivateLink(driver).getInnerHtml());

				Assert.assertTrue(strRowName.contains(sp.exposedFilesPopupRowTitles(driver).getInnerHtml().trim()), "Row Name Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowOwner(driver).getInnerHtml().trim(),strRowOwner, "Row Owner Not maching");
				Assert.assertTrue((sp.exposedFilesPopupRowCount(driver).getText().trim()).contains(strRowCount), "Row Count Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowSize(driver).getInnerHtml().trim(),strRowSize, "Row Size Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowPrivateLink(driver).getInnerHtml().trim(),"View File", "Row View File Not maching");


				sp.exposedFilesPopupClose(driver).click();
				hardWait(5);
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}


	public String verifySecurletDashboardExposedFilesTab_PopupDetailsTab(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.exposedUsersTabExternalAppTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);			
			sp.exposedUsersTabFileExposureTab(driver).click();   
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.exposedUsersTabFileExposureTabExternalCheckBox(driver).click();
			hardWait(10);
			List<Element> exposedFilesTableInternalExposuresRows = 
					sp.exposedFilesTableInternalExposuresRows(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowTitles = 
					sp.exposedFilesTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowIcon = 
					sp.exposedFilesTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowName = 
					sp.exposedFilesTableInternalExposuresRowName(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowOwner = 
					sp.exposedFilesTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowCount = 
					sp.exposedFilesTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowSize = 
					sp.exposedFilesTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowRisks = 
					sp.exposedFilesTableInternalExposuresRowRisks(driver).getChildElements();
			sp.exposedFilesTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<exposedFilesTableInternalExposuresRows.size();i++){
				message += (exposedFilesTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list icon element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row name list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file size list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row risks list element "+(i+1)+" is not visible";

				String strRowName =exposedFilesTableInternalExposuresRowName.get(i).getText().trim();
				String strRowOwner =exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim();
				String strRowCount =exposedFilesTableInternalExposuresRowCount.get(i).getText().trim();
				String strRowSize =exposedFilesTableInternalExposuresRowSize.get(i).getText().trim();

				Logger.info(exposedFilesTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowSize.get(i).getText().trim());

				exposedFilesTableInternalExposuresRows.get(i).click();

				hardWait(5);

				Logger.info("exposedFilesPopupRowTitles : "+sp.exposedFilesPopupRowTitles(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowOwner  : "+sp.exposedFilesPopupRowOwner(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowCount  : "+sp.exposedFilesPopupRowCount(driver).getText());
				Logger.info("exposedFilesPopupRowSize   : "+sp.exposedFilesPopupRowSize(driver).getInnerHtml());

				Assert.assertTrue(strRowName.contains(sp.exposedFilesPopupRowTitles(driver).getInnerHtml().trim()), "Row Name Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowOwner(driver).getInnerHtml().trim(),strRowOwner, "Row Owner Not maching");
				Assert.assertTrue((sp.exposedFilesPopupRowCount(driver).getText().trim()).contains(strRowCount), "Row Count Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowSize(driver).getInnerHtml().trim(),strRowSize, "Row Size Not maching");

				sp.exposedFilesPopupClose(driver).click();
				hardWait(5);
			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifySecurletDashboardExposedFilesTab_PopupExposureTab(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.exposedUsersTabExternalAppTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);			
			sp.exposedUsersTabFileExposureTab(driver).click();   
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.exposedUsersTabFileExposureTabExternalCheckBox(driver).click();
			hardWait(10);
			List<Element> exposedFilesTableInternalExposuresRows = 
					sp.exposedFilesTableInternalExposuresRows(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowIcon = 
					sp.exposedFilesTableInternalExposuresRowIcon(driver).getChildElements();

			for(int i=0;i<exposedFilesTableInternalExposuresRows.size();i++){
				String strRowIcontype = exposedFilesTableInternalExposuresRowIcon.get(i).getAttribute("title").trim();
				exposedFilesTableInternalExposuresRows.get(i).click();
				hardWait(5);
				Assert.assertTrue(sp.exposedFilesTabPopupExposuresTab(driver).isElementVisible(), "Exposures Tab not visible");
				sp.exposedFilesTabPopupExposuresTab(driver).click();
				hardWait(5);
				if(strRowIcontype.contains("emailbody"))
				{
					Assert.assertTrue(sp.exposedFilesPopupRowPrivateTxtBox(driver).isElementVisible(), "Private Text Box not visible");
				}
				sp.exposedFilesPopupClose(driver).click();
				hardWait(5);
			}

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}
	public String verifyExposedFilesTab_PopUpTable_ViewFileOpenExactFiles(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			String strTabUserTableFirstRowCount = sp.exposedUsersTabExternalAppTableFirstRowCount(driver).getText();
			Logger.info("strTabUserTableFirstRowCount   : "+ strTabUserTableFirstRowCount);
			sp.exposedUsersTabExternalAppTableFirstRowCheckBox(driver).click();
			hardWait(10);
			List<Element> exposedFilesTableInternalExposuresRows = 
					sp.exposedFilesTableInternalExposuresRows(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowTitles = 
					sp.exposedFilesTableInternalExposuresRowTitles(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowIcon = 
					sp.exposedFilesTableInternalExposuresRowIcon(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowName = 
					sp.exposedFilesTableInternalExposuresRowName(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowOwner = 
					sp.exposedFilesTableInternalExposuresRowOwner(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowCount = 
					sp.exposedFilesTableInternalExposuresRowCount(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowSize = 
					sp.exposedFilesTableInternalExposuresRowSize(driver).getChildElements();
			List<Element> exposedFilesTableInternalExposuresRowRisks = 
					sp.exposedFilesTableInternalExposuresRowRisks(driver).getChildElements();
			sp.exposedFilesTableInternalExposuresRowTypes(driver).getChildElements();

			for(int i=0;i<exposedFilesTableInternalExposuresRows.size();i++){
				message += (exposedFilesTableInternalExposuresRows.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowTitles.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowIcon.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row list icon element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowName.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row name list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowOwner.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowCount.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row owner change count element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowSize.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row file size list element "+(i+1)+" is not visible";
				message += (exposedFilesTableInternalExposuresRowRisks.get(i).isElementVisible()) ? "": 
					"exposedFiles internal exposures row risks list element "+(i+1)+" is not visible";

				String strRowName =exposedFilesTableInternalExposuresRowName.get(i).getText().trim();
				String strRowOwner =exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim();
				String strRowCount =exposedFilesTableInternalExposuresRowCount.get(i).getText().trim();
				String strRowSize =exposedFilesTableInternalExposuresRowSize.get(i).getText().trim();

				Logger.info(exposedFilesTableInternalExposuresRowTitles.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowName.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowOwner.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowCount.get(i).getText().trim() +"\t"+
						exposedFilesTableInternalExposuresRowSize.get(i).getText().trim());

				exposedFilesTableInternalExposuresRows.get(i).click();
				hardWait(5);

				Logger.info("exposedFilesPopupRowTitles : "+sp.exposedFilesPopupRowTitles(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowOwner  : "+sp.exposedFilesPopupRowOwner(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowCount  : "+sp.exposedFilesPopupRowCount(driver).getText());
				Logger.info("exposedFilesPopupRowSize   : "+sp.exposedFilesPopupRowSize(driver).getInnerHtml());
				Logger.info("exposedFilesPopupRowFileView   : "+sp.exposedFilesPopupRowPrivateLink(driver).getInnerHtml());

				Assert.assertTrue(strRowName.contains(sp.exposedFilesPopupRowTitles(driver).getInnerHtml().trim()), "Row Name Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowOwner(driver).getInnerHtml().trim(),strRowOwner, "Row Owner Not maching");
				Assert.assertTrue((sp.exposedFilesPopupRowCount(driver).getText().trim()).contains(strRowCount), "Row Count Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowSize(driver).getInnerHtml().trim(),strRowSize, "Row Size Not maching");
				Assert.assertEquals(sp.exposedFilesPopupRowPrivateLink(driver).getInnerHtml().trim(),"View File", "Row View File Not maching");

				sp.exposedFilesPopupRowPrivateLink(driver).click();
				hardWait(50);

				System.out.println("First Window Title : "+ driver.getTitle());
				Set <String> set1=driver.getWindowHandles();
				Iterator <String> win1=set1.iterator();
				win1.next();  
				String child=win1.next();
				driver.switchTo().window(child);
				System.out.println("Second Window Title : "+ driver.getTitle());
				//Assert.assertTrue(sp.signInButton(driver).isElementVisible(), "sign In Button not visible");
				//driver.close();
				//sp.exposedFilesPopupClose(driver).click();
				hardWait(5);
				break;

			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String verifySecurletDashboardExposedUsersTab_TapFileExposure_Internal(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			if(type.contains("Google Apps")||type.contains("Office 365")){
				Assert.assertTrue(sp.topOtherAppTab(driver).isElementVisible(), "App Tab not visible");
				Assert.assertEquals(sp.topOtherAppTab(driver).getText().trim(),"App", "App Tab not matching");
				sp.exposedUsersTabFileExposureTab(driver).click();   
				Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
				sp.exposedUsersTabFileExposureTabInternalCheckBoxClick(driver).click();
				String strInternal = sp.exposedUsersTabFileExposureTabInternalCheckBoxIsVerifyed(driver).getText();
				Assert.assertEquals(strInternal,"Internal", "Internal box Not maching");
			}else
			{
				Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
				sp.exposedUsersTabFileExposureTabInternalCheckBoxButton(driver).click();
				String strInternal = sp.exposedUsersTabFileExposureTabInternalCheckBoxIsVerifyed(driver).getText();
				Assert.assertEquals(strInternal,"Internal", "Internal box Not maching");

			}
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();

		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}
	public String verifySecurletDashboardExposedUsersTab_TapFileExposure_RiskType(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.exposedUsersTabRiskTypeTab(driver).click();   
			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
			sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).click();
			String strFirstRowLabel = sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).getText();
			String strFirstRowLabelVerified = sp.exposedUsersTabRiskTypeTabFirstRowLabelFirstProriotyVerifyed(driver).getText();
			Assert.assertEquals(strFirstRowLabel,strFirstRowLabelVerified, "Tabs Are Not maching");
		} catch (Exception e) 
		{
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return message;
	}


	public String verifySecurletDashboardExposedUsersTab_TapFileExposure_RiskTypeAnsInternal(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String message="";

		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		try {
			testShowing_20_of(driver);
			sp.topOtherToggleTab(driver).click();
			hardWait(10);

			Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");

			if(type.contains("Google Apps")||type.contains("Office 365")){
				sp.exposedUsersTabFileExposureTabClick(driver).click();   			
				sp.exposedUsersTabFileExposureTabInternalCheckBoxClick(driver).click();
				String strInternala = sp.exposedUsersTabFileExposureTabInternalCheckBoxIsVerifyed(driver).getText();
				Assert.assertEquals(strInternala,"Internal", "Internal box Not maching");
				sp.exposedUsersTabRiskTypeTab(driver).click();
				sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).click();
				String strFirstRowLabel = sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).getText();
				String strFirstRowLabelVerified = sp.exposedUsersTabRiskTypeTabFirstRowLabelSecondtProriotyVerifyed(driver).getText();
				Assert.assertEquals(strFirstRowLabel,strFirstRowLabelVerified, "Tabs Are Not maching");
			}else
			{
				Assert.assertTrue(sp.topOtherSearchBox(driver).isElementVisible(), "Search Box not visible");
				sp.exposedUsersTabFileExposureTabInternalCheckBoxButton(driver).click();
				String strInternal = sp.exposedUsersTabFileExposureTabInternalCheckBoxIsVerifyed(driver).getText();
				Assert.assertEquals(strInternal,"Internal", "Internal box Not maching");
				sp.exposedUsersTabRiskTypeTab(driver).click();
				sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).click();
				String strFirstRowLabel = sp.exposedUsersTabRiskTypeTabFirstRowLabel(driver).getText();
				String strFirstRowLabelVerified = sp.exposedUsersTabRiskTypeTabFirstRowLabelSecondtProriotyVerifyed(driver).getText();
				Assert.assertEquals(strFirstRowLabel,strFirstRowLabelVerified, "Tabs Are Not maching");

			}


		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			//e.printStackTrace(); return e.toString();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}


	public void clickOptionsSecurletDashboard(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		sp.topOtherOptions(driver).click();
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
	}

	public void clickViewScanPoliciesSecurletDashboard(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		sp.topOtherViewScanPolicies(driver).click();
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
	}

	public void clickCloseScanPoliciesSecurletDashboard(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		sp.topOtherViewScanPoliciesPopupClose(driver).click();
		hardWait(10);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
	}

	public String getViewScanPoliciesText(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	

		return sp.topOtherViewScanPoliciesBody(driver).getText().trim();		
	}

	public void enterOptionsSecurletScanPolicyPopup(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);

		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
			Logger.info("No partial scan option to be entered");
		}else{
			boolean flag = sp.scanPolicyPopup(driver).isElementPresent(driver);
			if(flag){
				sp.scanPolicyPopupExcludeFolderTextBox(driver).type("Sample_Exclude");
				sp.scanPolicyPopupExcludeFolderTextBox(driver).type(String.valueOf(Keys.ENTER));

				sp.scanPolicyStartScanButton(driver).mouseOver(driver);
				sp.scanPolicyStartScanButton(driver).click();hardWait(20);
			}else{
				Assert.fail("Scan policy configure options popup not appearing");
			}
		}
		
		
		

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());	
	}

	public String getAllActivitiesLogsText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SecurletDashboardPage sp =  AdvancedPageFactory.getPageObject(driver, SecurletDashboardPage.class);
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return sp.activitiesAllLogCount(driver).getText().trim();
	}
}
