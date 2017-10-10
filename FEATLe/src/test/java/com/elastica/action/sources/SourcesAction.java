package com.elastica.action.sources;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.elastica.action.Action;
import com.elastica.action.audit.AuditAction;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.audit.AuditPage;
import com.elastica.pageobjects.investigate.InvestigatePage;
import com.elastica.pageobjects.sources.SourcesPage;
import com.elastica.webelements.Element;

/**
 * Sources action class
 * @author Eldo Rajan
 *
 */
public class SourcesAction extends Action{


	public String verifySourcesPage(WebDriver driver) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SourcesPage sp =  AdvancedPageFactory.getPageObject(driver,SourcesPage.class);
		String  validationMessage = ""; 

		sp.SourcesLink(driver).click();
		hardWait(30); 

		Assert.assertTrue(sp.SourcesPageDevicelogsBox(driver).isElementVisible(), 
				"Sources Page is not loading with Device Logs/Securlets/Gatelets");


		Assert.assertTrue(sp.SourcesPageGateletsBox(driver).isElementVisible(), "Sources Page Gatelets Box is not visible");
		Assert.assertTrue(sp.SourcesPageSecurletsBox(driver).isElementVisible(), "Sources Page Securlets Box is not visible");
		Assert.assertTrue(sp.SourcesPageDevicelogsBox(driver).isElementVisible(), "Sources Page Device logs Box is not visible");

		Assert.assertEquals(sp.SourcesPageGateletsHeader(driver).getText().trim(),"Gatelets", "Gatelets page title is not matching");
		Assert.assertEquals(sp.SourcesPageSecurletsHeader(driver).getText().trim(),"Securlets", "Securlets page title is not matching");
		Assert.assertEquals(sp.SourcesPageDevicelogsHeader(driver).getText().trim(),"Device Logs", "Device logs page title is not matching");

		Assert.assertEquals(sp.SourcesPageGateletsSources(driver).getText().trim(),"Sources", "Gatelets Sources page title is not matching");
		Assert.assertEquals(sp.SourcesPageSecurletsSources(driver).getText().trim(),"Sources", "Securlets Sources page title is not matching");
		Assert.assertEquals(sp.SourcesPageDevicelogsSources(driver).getText().trim(),"Sources", "Device logs Sources page title is not matching");

		Assert.assertEquals(sp.SourcesPageGateletsRecords(driver).getText().trim(),"Total Records", "Gatelets Records page title is not matching");
		Assert.assertEquals(sp.SourcesPageSecurletsRecords(driver).getText().trim(),"Total Records", "Securlets Records page title is not matching");
		Assert.assertEquals(sp.SourcesPageDevicelogsRecords(driver).getText().trim(),"Total Records", "Device logs Records page title is not matching");

		selectSourcesFromPage(driver,"Device Logs");

		String sourceCount = sp.SourcesPageDevicelogsSourcesCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		
		String noRecordsCount = sp.SourcesPageDevicelogsBox(driver).getText().trim();
		noRecordsCount = noRecordsCount.substring(noRecordsCount.indexOf("With No Records: "), noRecordsCount.indexOf("Total Records:"));
		noRecordsCount = noRecordsCount.replaceAll("With No Records: ", "").trim();
		
		Logger.info("Device Logs Sources Count is - "+(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount)));

		List<Element> SourcesPageSourcesRowCount  = sp.SourcesPageSourcesRowCount(driver).getChildElements();		
		hardWait(10);
		int intGateletsSourcesCount=Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount); int count=0;
		if(intGateletsSourcesCount>10){
			count=10;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount),SourcesPageSourcesRowCount.size(), "Device Logs:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Device Logs List started");

		List<Element> SourcesPageGateletsSourcesRowName  = sp.SourcesPageGateletsSourcesRowName(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowRecord  = sp.SourcesPageGateletsSourcesRowRecord(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowRecordCount  = sp.SourcesPageGateletsSourcesRowRecordCount(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowUser  = sp.SourcesPageGateletsSourcesRowUser(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowUserCount  = sp.SourcesPageGateletsSourcesRowUserCount(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowViolations  = sp.SourcesPageGateletsSourcesRowViolations(driver).getChildElements();
		List<Element> SourcesPageGateletsSourcesRowViolationsCount  = sp.SourcesPageGateletsSourcesRowViolationsCount(driver).getChildElements();

		for(int i=0;i<count;i++){


			String Name = SourcesPageGateletsSourcesRowName.get(i).getText().trim();
			String RowRecord = SourcesPageGateletsSourcesRowRecord.get(i).getText().trim();
			String RowRecordCount = SourcesPageGateletsSourcesRowRecordCount.get(i).getText().trim();
			String RowUser = SourcesPageGateletsSourcesRowUser.get(i).getText().trim();
			String RowUserCount = SourcesPageGateletsSourcesRowUserCount.get(i).getText().trim();
			String Rowviolations = SourcesPageGateletsSourcesRowViolations.get(i).getText().trim();
			String RowviolationsCount = SourcesPageGateletsSourcesRowViolationsCount.get(i).getText().trim();

			validationMessage += (StringUtils.isNoneEmpty(Name)) ? "" : "Device Logs List Name "+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (StringUtils.isNoneEmpty(RowRecord)) ? "" : "Device Logs List RowRecord "+(i+1)+":"+RowRecord+
					" - Expecting some Number but was " + RowRecord;
			validationMessage += (StringUtils.isNoneEmpty(RowRecordCount)) ? "" : "Device Logs List RowRecordCount "+(i+1)+":"+RowRecordCount+
					" - Expecting some Number but was " + RowRecordCount;
			validationMessage += (StringUtils.isNoneEmpty(RowUser)) ? "" : "Device Logs List RowUser "+(i+1)+":"+RowUser+
					" - Expecting some Number but was " + RowUser;
			validationMessage += (StringUtils.isNoneEmpty(RowUserCount)) ? "" : "Device Logs List RowUserCount "+(i+1)+":"+RowUserCount+
					" - Expecting some Number but was " + RowUserCount;
			validationMessage += (StringUtils.isEmpty(Rowviolations)) ? "" : "Device Logs List Rowviolations "+(i+1)+":"+Rowviolations+
					" - Expecting some Number but was " + Rowviolations;
			validationMessage += (StringUtils.isEmpty(RowviolationsCount)) ? "" : "Device Logs List RowviolationsCount "+(i+1)+":"+RowviolationsCount+
					" - Expecting some Number but was " + RowviolationsCount;

		}
		Logger.info("Validation of Device Logs List completed");



		selectSourcesFromPage(driver,"Securlets");

		sourceCount = sp.SourcesPageSecurletsSourcesCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		
		noRecordsCount = sp.SourcesPageSecurletsBox(driver).getText().trim();
		noRecordsCount = noRecordsCount.substring(noRecordsCount.indexOf("With No Records: "), noRecordsCount.indexOf("Total Records:"));
		noRecordsCount = noRecordsCount.replaceAll("With No Records: ", "").trim();
		
		Logger.info("Securlets Sources Count is - "+(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount)));

		SourcesPageSourcesRowCount  = sp.SourcesPageSourcesRowCount(driver).getChildElements();		
		hardWait(10);
		intGateletsSourcesCount=Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount); count=0;
		if(intGateletsSourcesCount>10){
			count=10;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount),SourcesPageSourcesRowCount.size(), 
					"Securlets:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Securlets List started");

		SourcesPageGateletsSourcesRowName  = sp.SourcesPageGateletsSourcesRowName(driver).getChildElements();
		SourcesPageGateletsSourcesRowRecord  = sp.SourcesPageGateletsSourcesRowRecord(driver).getChildElements();
		SourcesPageGateletsSourcesRowRecordCount  = sp.SourcesPageGateletsSourcesRowRecordCount(driver).getChildElements();
		SourcesPageGateletsSourcesRowUser  = sp.SourcesPageGateletsSourcesRowUser(driver).getChildElements();
		SourcesPageGateletsSourcesRowUserCount  = sp.SourcesPageGateletsSourcesRowUserCount(driver).getChildElements();
		SourcesPageGateletsSourcesRowViolations  = sp.SourcesPageGateletsSourcesRowViolations(driver).getChildElements();
		SourcesPageGateletsSourcesRowViolationsCount  = sp.SourcesPageGateletsSourcesRowViolationsCount(driver).getChildElements();

		for(int i=0;i<count;i++){


			String Name = SourcesPageGateletsSourcesRowName.get(i).getText().trim();
			String RowRecord = SourcesPageGateletsSourcesRowRecord.get(i).getText().trim();
			String RowRecordCount = SourcesPageGateletsSourcesRowRecordCount.get(i).getText().trim();
			String RowUser = SourcesPageGateletsSourcesRowUser.get(i).getText().trim();
			String RowUserCount = SourcesPageGateletsSourcesRowUserCount.get(i).getText().trim();
			String Rowviolations = SourcesPageGateletsSourcesRowViolations.get(i).getText().trim();
			String RowviolationsCount = SourcesPageGateletsSourcesRowViolationsCount.get(i).getText().trim();

			validationMessage += (StringUtils.isNoneEmpty(Name)) ? "" : "Securlets List Name "+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (StringUtils.isNoneEmpty(RowRecord)) ? "" : "Securlets List RowRecord "+(i+1)+":"+RowRecord+
					" - Expecting some Number but was " + RowRecord;
			validationMessage += (StringUtils.isNoneEmpty(RowRecordCount)) ? "" : "Securlets List RowRecordCount "+(i+1)+":"+RowRecordCount+
					" - Expecting some Number but was " + RowRecordCount;
			validationMessage += (StringUtils.isNoneEmpty(RowUser)) ? "" : "Securlets List RowUser "+(i+1)+":"+RowUser+
					" - Expecting some Number but was " + RowUser;
			validationMessage += (StringUtils.isNoneEmpty(RowUserCount)) ? "" : "Securlets List RowUserCount "+(i+1)+":"+RowUserCount+
					" - Expecting some Number but was " + RowUserCount;
			validationMessage += (StringUtils.isNoneEmpty(Rowviolations)) ? "" : "Securlets List Rowviolations "+(i+1)+":"+Rowviolations+
					" - Expecting some Number but was " + Rowviolations;
			validationMessage += (StringUtils.isNoneEmpty(RowviolationsCount)) ? "" : "Securlets List RowviolationsCount "+(i+1)+":"+RowviolationsCount+
					" - Expecting some Number but was " + RowviolationsCount;

		}
		Logger.info("Validation of Securlets List completed");

		selectSourcesFromPage(driver,"Gatelets");

		sourceCount = sp.SourcesPageGateletsSourcesCount(driver).getText().trim();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		
		noRecordsCount = sp.SourcesPageGateletsBox(driver).getText().trim();
		noRecordsCount = noRecordsCount.substring(noRecordsCount.indexOf("With No Records: "), noRecordsCount.indexOf("Total Records:"));
		noRecordsCount = noRecordsCount.replaceAll("With No Records: ", "").trim();
		
		Logger.info("Gatelets Sources Count is - "+(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount)));

		SourcesPageSourcesRowCount  = sp.SourcesPageSourcesRowCount(driver).getChildElements();		
		hardWait(10);
		intGateletsSourcesCount=Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount); count=0;
		if(intGateletsSourcesCount>10){
			count=10;
		}else{
			Assert.assertEquals(Integer.parseInt(sourceCount)-Integer.parseInt(noRecordsCount),SourcesPageSourcesRowCount.size(), 
					"Gatelets:Table and chat row count is not matching");
			count=SourcesPageSourcesRowCount.size();
		}

		Logger.info("Validation of Gatelets List started");
		SourcesPageGateletsSourcesRowName  = sp.SourcesPageGateletsSourcesRowName(driver).getChildElements();
		SourcesPageGateletsSourcesRowRecord  = sp.SourcesPageGateletsSourcesRowRecord(driver).getChildElements();
		SourcesPageGateletsSourcesRowRecordCount  = sp.SourcesPageGateletsSourcesRowRecordCount(driver).getChildElements();
		SourcesPageGateletsSourcesRowUser  = sp.SourcesPageGateletsSourcesRowUser(driver).getChildElements();
		SourcesPageGateletsSourcesRowUserCount  = sp.SourcesPageGateletsSourcesRowUserCount(driver).getChildElements();
		SourcesPageGateletsSourcesRowViolations  = sp.SourcesPageGateletsSourcesRowViolations(driver).getChildElements();
		SourcesPageGateletsSourcesRowViolationsCount  = sp.SourcesPageGateletsSourcesRowViolationsCount(driver).getChildElements();

		for(int i=0;i<count;i++){

			String Name = SourcesPageGateletsSourcesRowName.get(i).getText().trim();
			String RowRecord = SourcesPageGateletsSourcesRowRecord.get(i).getText().trim();
			String RowRecordCount = SourcesPageGateletsSourcesRowRecordCount.get(i).getText().trim();
			String RowUser = SourcesPageGateletsSourcesRowUser.get(i).getText().trim();
			String RowUserCount = SourcesPageGateletsSourcesRowUserCount.get(i).getText().trim();
			String Rowviolations = SourcesPageGateletsSourcesRowViolations.get(i).getText().trim();
			String RowviolationsCount = SourcesPageGateletsSourcesRowViolationsCount.get(i).getText().trim();

			validationMessage += (StringUtils.isNoneEmpty(Name)) ? "" : "Gatelets List Name "+(i+1)+":"+Name+
					" - Expecting some Name but was " + Name;
			validationMessage += (StringUtils.isNoneEmpty(RowRecord)) ? "" : "Gatelets List RowRecord "+(i+1)+":"+RowRecord+
					" - Expecting some Number but was " + RowRecord;
			validationMessage += (StringUtils.isNoneEmpty(RowRecordCount)) ? "" : "Gatelets List RowRecordCount "+(i+1)+":"+RowRecordCount+
					" - Expecting some Number but was " + RowRecordCount;
			validationMessage += (StringUtils.isNoneEmpty(RowUser)) ? "" : "Gatelets List RowUser "+(i+1)+":"+RowUser+
					" - Expecting some Number but was " + RowUser;
			validationMessage += (StringUtils.isNoneEmpty(RowUserCount)) ? "" : "Gatelets List RowUserCount "+(i+1)+":"+RowUserCount+
					" - Expecting some Number but was " + RowUserCount;
			validationMessage += (StringUtils.isNoneEmpty(Rowviolations)) ? "" : "Gatelets List Rowviolations "+(i+1)+":"+Rowviolations+
					" - Expecting some Number but was " + Rowviolations;
			validationMessage += (StringUtils.isNoneEmpty(RowviolationsCount)) ? "" : "Gatelets List RowviolationsCount "+(i+1)+":"+RowviolationsCount+
					" - Expecting some Number but was " + RowviolationsCount;

		}
		Logger.info("Validation of Gatelets List completed");

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	private void selectSourcesFromPage(WebDriver driver, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SourcesPage sp =  AdvancedPageFactory.getPageObject(driver,SourcesPage.class);
		if(type.equalsIgnoreCase("Device Logs")){
			sp.SourcesPageDevicelogsBox(driver).click();	
		}else if(type.equalsIgnoreCase("Securlets")){
			sp.SourcesPageSecurletsBox(driver).click();	
		}else if(type.equalsIgnoreCase("Gatelets")){
			sp.SourcesPageGateletsBox(driver).click();	
		}
		hardWait(20);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public String verifyDeviceLogsInSourcesPage(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		AuditAction audit = new AuditAction();
		SourcesPage sp =  AdvancedPageFactory.getPageObject(driver,SourcesPage.class);
		AuditPage ap =  AdvancedPageFactory.getPageObject(driver,AuditPage.class);

		String validationMessage = ""; 

		sp.SourcesLink(driver).click();
		hardWait(30); 

		Assert.assertTrue(sp.SourcesPageDevicelogsBox(driver).isElementVisible(), 
				"Sources Page is not loading with Device Logs/Securlets/Gatelets");

		selectSourcesFromPage(driver,"Device Logs");

		String sourceCount = sp.SourcesPageDevicelogsSourcesCount(driver).getText();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info("Device Logs Sources Count is - "+sourceCount);

		if(Integer.parseInt(sourceCount)>0){

			List<Element> serviceNameList  = sp.SourcesPageGateletsSourcesRowName(driver).getChildElements();
			List<Element> serviceNameHyperlinkList  = sp.SourcesPageGateletsSourcesRowNameLink(driver).getChildElements();
			List<Element> recordsList  = sp.SourcesPageGateletsSourcesRowRecordCount(driver).getChildElements();
			List<Element> usersList  = sp.SourcesPageGateletsSourcesRowUserCount(driver).getChildElements();
			List<Element> servicesList  = sp.SourcesPageGateletsSourcesRowservicesCount(driver).getChildElements();

			String serviceName = serviceNameList.get(0).getText().trim();
			String serviceNameHyperlink = serviceNameHyperlinkList.get(0).getAttribute("href").trim();
			String serviceNameId = serviceNameHyperlink.replaceAll(".*=", "");
			String records = recordsList.get(0).getText().trim().toLowerCase().replace("records", "").replace("\n", "");
			int users = Integer.parseInt(usersList.get(0).getText().trim().toLowerCase().replace("users", "").replace("\n", ""));
			int services = Integer.parseInt(servicesList.get(0).getText().trim().toLowerCase().replace("services", "").replace("\n", ""));

			validationMessage += (serviceNameHyperlink.contains(suiteData.getBaseUrl()+"/static/ng/appAudit/index.html#/")) ? "" : 
				"The link not redirected to audit page expected:"+suiteData.getBaseUrl()+"/static/ng/appAudit/index.html#/"
				+" but is "+serviceNameHyperlink;

			
			String winHandleBefore = getWindowHandle(driver);
			clickServiceNameFromSourcePage(driver,1);
			Set<String> winHandlesAfter = getWindowHandles(driver);
			for(String winHandle : winHandlesAfter){
				if(winHandle.equalsIgnoreCase(winHandleBefore)){
					Logger.info("Control is in current window, switching of windows will not be enforced");
				}else{
					Logger.info("Control is in new window, switching of windows will be enforced");
					switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));
					String auditUrl = getCurrentUrl(driver); 
					String header = ap.auditheader(driver).getText().trim();

					String dataSourceName = auditUrl.replaceAll(".*=", "");
					String sourceName=ap.auditDataSourcesSourceSelected(driver).getText().trim();
					int sourceUsers=audit.getUserCount(driver);
					int sourceServices=audit.getSaasServicesCount(driver);
					
					double recordNumbers=0;String roundOff="";
					if(records.contains("k")){
						recordNumbers = Float.parseFloat(records.replace("k", ""))*1000;roundOff="k";
					}else if(records.contains("m")){
						recordNumbers = Float.parseFloat(records.replace("m", ""))*1000000;roundOff="m";
					}else{
						recordNumbers = Float.parseFloat(records);roundOff="";
					}

					validationMessage += (auditUrl.contains(serviceNameId)) ? "" : 
						"The link does not contain service id Expected:"+serviceNameId+" but was "+auditUrl;
					validationMessage += (header.equalsIgnoreCase("Audit")) ? "" : 
						"Expected header:"+header+" but was "+auditUrl;
					validationMessage += (dataSourceName.equalsIgnoreCase(serviceNameId)) ? "" : 
						"Data Source id mismatch Expected:"+serviceNameId+" but was "+dataSourceName;
					validationMessage += (sourceName.equalsIgnoreCase(serviceName)) ? "" : 
						"Data Source name mismatch Expected:"+serviceName+" but was "+sourceName;
					validationMessage += (recordNumbers>0) ? "" : 
						"Data Source records mismatch Expected:some count but was "+recordNumbers;
					validationMessage += (sourceUsers==users) ? "" : 
						"Data Source users mismatch Expected:"+users+" but was "+sourceUsers;
					validationMessage += (sourceServices==services) ? "" : 
						"Data Source services mismatch Expected:"+services+" but was "+sourceServices;

					break;
				}
			}	
			
			switchToParentWindow(driver);
			

		}else{
			Assert.fail("No device logs to be verified, populate data to verify the tests");
		}



		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	private void clickServiceNameFromSourcePage(WebDriver driver, int sCount) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		SourcesPage sp =  AdvancedPageFactory.getPageObject(driver,SourcesPage.class);

		List<Element> serviceNameLinkList  = sp.SourcesPageGateletsSourcesRowNameLink(driver).getChildElements();
		serviceNameLinkList.get(sCount-1).mouseOver(driver);
		serviceNameLinkList.get(sCount-1).click();
		hardWait(80); 

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public String verifySecurletsGateletsInSourcesPage(WebDriver driver, SuiteData suiteData, String type) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String apiType="";if(type.equalsIgnoreCase("Gatelets")){apiType="GW";}else{apiType="API";}

		SourcesPage sp =  AdvancedPageFactory.getPageObject(driver,SourcesPage.class);
		InvestigatePage ip =  AdvancedPageFactory.getPageObject(driver,InvestigatePage.class);

		String  validationMessage = ""; 

		sp.SourcesLink(driver).click();
		hardWait(30); 

		Assert.assertTrue(sp.SourcesPageDevicelogsBox(driver).isElementVisible(), 
				"Sources Page is not loading with Device Logs/Securlets/Gatelets");

		selectSourcesFromPage(driver,type);

		String sourceCount = sp.SourcesPageDevicelogsSourcesCount(driver).getText();
		sourceCount = sourceCount.substring(sourceCount.indexOf(": ") + 1).trim();
		Logger.info(type+" Sources Count is - "+sourceCount);

		if(Integer.parseInt(sourceCount)>0){

			List<Element> serviceNameList  = sp.SourcesPageGateletsSourcesRowName(driver).getChildElements();
			List<Element> recordsList  = sp.SourcesPageGateletsSourcesRowRecordCount(driver).getChildElements();
			List<Element> usersList  = sp.SourcesPageGateletsSourcesRowUserCount(driver).getChildElements();
			List<Element> violationsList  = sp.SourcesPageGateletsSourcesRowViolationsCount(driver).getChildElements();

			String serviceName = serviceNameList.get(0).getText().trim();
			String records = recordsList.get(0).getText().trim().toLowerCase().replace("records", "").replace("\n", "");
			int users = Integer.parseInt(usersList.get(0).getText().trim().toLowerCase().replace("users", "").replace("\n", ""));
			int violations = Integer.parseInt(violationsList.get(0).getText().trim().toLowerCase().replace("violations", "").replace("\n", "")
					.replace(",", ""));

			

			String winHandleBefore = getWindowHandle(driver);
			clickServiceNameFromSourcePage(driver,1);
			Set<String> winHandlesAfter = getWindowHandles(driver);
			for(String winHandle : winHandlesAfter){
				if(winHandle.equalsIgnoreCase(winHandleBefore)){
					Logger.info("Control is in current window, switching of windows will not be enforced");
				}else{
					Logger.info("Control is in new window, switching of windows will be enforced");
					switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));

					
					
					String investigateUrl = getCurrentUrl(driver); 
					String header = ip.header(driver).getText().trim();

					validationMessage += (header.equalsIgnoreCase("Investigate")) ? "" : 
						"Expected header:Investigate but was "+header;

					validationMessage += (investigateUrl.equalsIgnoreCase
							(suiteData.getBaseUrl()+"/static/ng/appForensics/index.html#/?app="+serviceName.replace(" ", "%20")+"&__source="+apiType)) ? "" : 
								"The link does not contain service id Expected:"+serviceName+":::"+apiType+" but was "+investigateUrl;

					double recordNumbers=0;String roundOff="";
					if(records.contains("k")){
						recordNumbers = Float.parseFloat(records.replace("k", ""))*1000;roundOff="k";
					}else if(records.contains("m")){
						recordNumbers = Float.parseFloat(records.replace("m", ""))*1000000;roundOff="m";
					}else{
						recordNumbers = Float.parseFloat(records);roundOff="";
					}

					validationMessage += (recordNumbers>0) ? "" : 
						"Data Source records mismatch Expected:some count but was "+recordNumbers;
					validationMessage += (users>0) ? "" : 
						"Data Source users mismatch Expected:some count but was "+users;
					validationMessage += (violations>=0) ? "" : 
						"Data Source violations mismatch Expected:some count but was "+violations;

					double investigateLogCount = Float.parseFloat(ip.activityLogCount(driver).getText().trim().replace(",", "").replace(" matching logs", ""));


					recordNumbers = this.getValueInmOrk(recordNumbers, roundOff);
					investigateLogCount = this.getValueInmOrk(investigateLogCount, roundOff);

					Logger.info(recordNumbers+" "+investigateLogCount);

					validationMessage += (recordNumbers==investigateLogCount) ? "" : 
						"Data Source records mismatch between sources page:"+recordNumbers+roundOff+
						" and investigate page:"+investigateLogCount+roundOff;

					break;
				}
			}	
			
			switchToParentWindow(driver);
			
			

		}else{
			Assert.fail("No device logs to be verified, populate data to verify the tests");
		}



		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return validationMessage;
	}

	public boolean almostEqual(double a, double b, double eps){
		return Math.abs(a-b)<eps;
	}

	public double getValueInmOrk(double a, String type){
		double t=1;
		if(type.equalsIgnoreCase("m")){t=1000000;}else if(type.equalsIgnoreCase("k")){t=1000;}
		 DecimalFormat dForm = new DecimalFormat("#.#");
		return Double.valueOf(dForm.format(a/t));
	}

}


