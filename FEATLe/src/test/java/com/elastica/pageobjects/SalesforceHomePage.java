package com.elastica.pageobjects;

import org.openqa.selenium.WebDriver;

import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextArea;
import com.elastica.webelements.TextBox;

public class SalesforceHomePage extends BasePage {
	
	public HyperLink salesforceHomeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceLeadsTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
	public HyperLink salesforceContactTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceMoreTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceOpportunityTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesTab(WebDriver driver){
		System.out.println(getLocator());
		return new HyperLink(driver,getLocator());
	}
			
	public HyperLink salesforceMoreOption(WebDriver driver, String option){
		return new HyperLink(driver,getLocator().replaceAll("option", option));
	}
	
	public TextBox salesforceAccountName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextArea salesforceAccountDescription(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public TextBox salesforceAccountWebsite(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceAccountName1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextArea salesforceAccountDescription1(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public TextBox salesforceAccountWebsite1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public Button salesforceCreateAccountButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink salesForceAccountNameLink(WebDriver driver, String accountName){
		return new HyperLink(driver,getLocator().replaceAll("accountname", accountName));
	}
	
	public Button salesforceAccountEditButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public TextArea salesforceAccountEditDescription(WebDriver driver){
		return new TextArea(driver,getLocator());
	}
	
	public Button salesforceAccountSaveButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button salesforceAccountViewButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Button salesforceAccountDeleteButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink salesforceAccountTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceChatterTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public Button salesforceAccountNewButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityAccountName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityAmount(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityStage(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityDescription(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityCloseDate(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityName1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityAccountName1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityAmount1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityStage1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceOpportunityCloseDate1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactSalutation(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactFirstname(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactEmail(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactMiddlename(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactLastname(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactAccountname(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactSuffix(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactPhone(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactTitle(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactSalutation1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactFirstname1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactEmail1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactMiddlename1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactLastname1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactAccountname1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactSuffix1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactPhone1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactTitle1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceContactMobile(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink salesforceContactLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("contactname", value));
	}
	
	public TextBox salesforceLeadCompany(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceLeadCompany1(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink salesforceReportNewReportButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox salesforceQuickFindInput(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink salesforceReportType(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("reporttype", value));
	}
	
	public HyperLink salesforceCreateReportButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceSaveReportButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox salesforceReportName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceReportUniqueName(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public TextBox salesforceReportDescription(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink salesforceSaveReportDlgButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportCloseButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportSaveAndCloseDlgButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportNameLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("reportname", value));
	}
	
	public HyperLink salesforceReportEditSave(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox salesforceReportEditDescription(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public HyperLink salesforceReportExportDetails(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportExportDone(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportDelete(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceReportRun(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceLeadHomeLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("leadname", value));
	}
	
	public HyperLink salesforceHomeChatterFileLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceHomeChatterUploadButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceHomeChatterBrowserInput(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceHomeChatterShareButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceHomeChatterFilenameLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceHomeChatterDownloadFileLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceHomeChatterDownloadFileExtension(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceHomeChatterDeleteDropdown(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceHomeChatterDelete(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceFilesUpload(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesFilenameLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceFilesDownloadLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesDeleteLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesShareDropdown(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceFilesShareViaLink(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceFilesShareWithPoeple(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("filename", value));
	}
	
	public HyperLink salesforceFilesShareEmail(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesShareMessage(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesShareOk(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceFilesShareClose(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountCreateNewDropdown(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountCreateNewFileMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountUploadFile(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountShareDropdownButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountShareViaLinkMenu(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink salesforceAccountUploadButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public HyperLink salesforceChatterCommentButton(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("accountname", value));
	}
	
	public HyperLink salesforceChatterCommentTextarea(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("accountname", value));
	}
	
	public HyperLink salesforceChatterCommentSubmit(WebDriver driver, String value){
		return new HyperLink(driver,getLocator().replaceAll("accountname", value));
	}
	
	public HyperLink salesforceUploadButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
}