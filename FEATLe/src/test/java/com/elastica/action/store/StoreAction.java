package com.elastica.action.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.elastica.action.Action;
import com.elastica.action.universalapi.SaasType;
import com.elastica.action.universalapi.UniversalAPIFunctions;
import com.elastica.common.SuiteData;
import com.elastica.logger.Logger;
import com.elastica.pagefactory.AdvancedPageFactory;
import com.elastica.pageobjects.store.StorePage;
import com.elastica.webelements.Element;

/**
 * Store common actions
 * @author eldorajan
 *
 */
public class StoreAction extends Action{

	public String getStoreHeader(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText="";
		try {
			headerText = sp.header(driver).getText().trim();Logger.info("Actual Header Text:"+headerText);
		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			e.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return headerText;
	}

	public String verifySecurletSectionInStore(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		String[] securletTitle={
				"Box","Dropbox","Office 365","Salesforce","Yammer","Amazon Web Services",
				"DocuSign","Google Apps","Jive","ServiceNow"
		};
		String[] securletImageLink={
				suiteData.getBaseUrl()+"/static/images/store/logos/box.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/dropbox.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/office_365.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/salesforce.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/yammer.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/amazon_web_services.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/docusign.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/google_apps.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/jive.jpg",
				suiteData.getBaseUrl()+"/static/images/store/logos/servicenow.jpg"
		};

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String message="";
		try {

			message += (sp.securletSection(driver).isElementVisible()) ? "": 
				"Securlet section is not visible";
			message += (sp.securletSectionHeader(driver).isElementVisible()) ? "": 
				"Securlet section header is not visible";
			String text = sp.securletSectionHeader(driver).getText().trim();
			message += (text.equalsIgnoreCase("Securlets™")) ? "": 
				"Securlet section header is not matching Expected:Securlets™ but was "+text;

			message += (sp.securletSectionAllTiles(driver).isElementVisible()) ? "": 
				"Securlet section tiles is not visible";


			message += (sp.securletSectionSeeAllLink(driver).isElementVisible()) ? "": 
				"Securlet section see all link is not visible";
			text = sp.securletSectionSeeAllLink(driver).getText().trim();
			message += (text.equalsIgnoreCase("See all")) ? "": 
				"Securlet section see all link is not matching Expected:See all but was "+text;
			List<Element> securletSectionTileElement = sp.securletSectionTileElement(driver).getChildElements();
			message += (securletSectionTileElement.size()==4) ? "": 
				"Securlet section default tile count is not matching Expected:4 but was "+securletSectionTileElement.size();

			this.refresh(driver, 20);

			sp.securletSectionSeeAllLink(driver).click();hardWait(5);
			text = sp.securletSectionSeeAllLink(driver).getText().trim();
			message += (text.equalsIgnoreCase("See less")) ? "": 
				"Securlet section see all link is not matching Expected:See less but was "+text;
			securletSectionTileElement = sp.securletSectionTileElement(driver).getChildElements();
			message += (securletSectionTileElement.size()==10) ? "": 
				"Securlet section default tile count is not matching Expected:10 but was "+securletSectionTileElement.size();


			List<Element> securletSectionTileLinkElement = sp.securletSectionTileLinkElement(driver).getChildElements();
			List<Element> securletSectionTileImageElement = sp.securletSectionTileImageElement(driver).getChildElements();
			List<Element> securletSectionTileTitleElement = sp.securletSectionTileTitleElement(driver).getChildElements();

			for(int i=0;i<securletSectionTileLinkElement.size();i++){
				securletSectionTileLinkElement = sp.securletSectionTileLinkElement(driver).getChildElements();
				securletSectionTileImageElement = sp.securletSectionTileImageElement(driver).getChildElements();
				securletSectionTileTitleElement = sp.securletSectionTileTitleElement(driver).getChildElements();

				message += (securletSectionTileImageElement.get(i).isElementVisible()) ? "": 
					"Securlet Section Tile Image "+(i+1)+" is not visible";
				message += (securletSectionTileTitleElement.get(i).isElementVisible()) ? "": 
					"Securlet Section Tile Title "+(i+1)+" is not visible";
				securletSectionTileTitleElement.get(i).mouseOver(driver);hardWait(1);
				securletSectionTileTitleElement = sp.securletSectionTileTitleElement(driver).getChildElements();
				text = securletSectionTileLinkElement.get(i).getAttribute("title").trim();
				message += (text.equalsIgnoreCase(securletTitle[i])) ? "": 
					"Securlet section tile link title is not matching Expected:"+securletTitle[i]+" but was "+text;
				text = securletSectionTileImageElement.get(i).getAttribute("src").trim();
				message += (text.equalsIgnoreCase(securletImageLink[i])) ? "": 
					"Securlet section tile image source is not matching Expected:"+securletImageLink[i]+" but was "+text;
				text = securletSectionTileTitleElement.get(i).getText().trim(); 
				message += (text.equalsIgnoreCase(securletTitle[i])) ? "": 
					"Securlet section tiletitle is not matching Expected:"+securletTitle[i]+" but was "+text;


			}	

		} catch (Exception e) {
			Logger.info("Failed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName()+" "+e.toString());
			e.printStackTrace();
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		Logger.info(message);

		return message;
	}

	private void navigateToDetailedViewAppInStorePage(WebDriver driver,
			SuiteData suiteData) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());

		String appUrl = suiteData.getBaseUrl()+"/static/ng/appStore/index.html#/appdetails/APP_NAME";
		switch (stype) {
		case Box: {
			appUrl = appUrl.replace("APP_NAME", "box");
			break;
		}
		case Dropbox: {
			appUrl = appUrl.replace("APP_NAME", "dropbox");
			break;
		}
		case GoogleApps: {
			appUrl = appUrl.replace("APP_NAME", "google_apps");
			break;
		}
		case Office365: {
			appUrl = appUrl.replace("APP_NAME", "office_365");
			break;
		}
		case Salesforce: {
			appUrl = appUrl.replace("APP_NAME", "salesforce");
			break;
		}
		default: {
			Logger.info("Not applicable securlet");
			break;
		}
		}

		driver.get(appUrl);hardWait(10);
	}

	public boolean getSaasAppActivatedOrNot(WebDriver driver,
			SuiteData suiteData) {
		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		boolean flag = false;
		String currentUrl = getCurrentUrl(driver);

		clickSeeAllLessLink(driver, "See all");
		navigateToDetailedViewAppInStorePage(driver,suiteData);

		switch (stype) {
		case Box: {
			if(sp.ConfigureButton(driver).isElementVisible()){
				clickSecurletConfigureButton(driver, suiteData);
				flag = getAccountInfoDropdownList(driver).contains(suiteData.getTenantDomainName());
			}
			break;
		}
		case Dropbox: {
			flag = sp.DeactivateButton(driver).isElementVisible();	
			break;
		}
		case GoogleApps: {
			flag = sp.DeactivateButton(driver).isElementVisible();
			break;
		}
		case Office365: {
			if(sp.ConfigureButton(driver).isElementVisible()){
				clickSecurletConfigureButton(driver, suiteData);
				flag = getAccountInfoDropdownList(driver).contains(suiteData.getTenantDomainName());
			}
			break;
		}
		case Salesforce: {
			if(sp.ConfigureButton(driver).isElementVisible()){
				clickSecurletConfigureButton(driver, suiteData);
				flag = getAccountInfoDropdownList(driver).contains(suiteData.getTenantDomainName());
			}
			break;
		}
		default: {
			Logger.info("Not applicable securlet");
			break;
		}
		}
		driver.get(currentUrl);hardWait(10);
		return flag;
	}

	public void clickSaasAppLink(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		sp.AccountInformationButton(driver).mouseOver(driver);
		sp.AccountInformationButton(driver).click();
		hardWait(10);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public List<String> getAccountInfoDropdownList(WebDriver driver){
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		List<String> accountList = new ArrayList<String>();
		List<Element> AccountInformationDropdown = sp.AccountInformationDropdown(driver).getChildElements();
		for(Element accountInfo:AccountInformationDropdown){
			accountList.add(accountInfo.getInnerHtml().trim()
					.replace("<!-- ngIf: showIcon(item) -->", "")
					.replace("<!-- ngIf: config.showSvgIconWithListItem -->","")
					.replace("\n","").replace("\r", "").trim());
		}
		System.out.println(accountList.toString());
		return accountList;
	}

	public void clickSaasAppLink(WebDriver driver, String app){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		this.refresh(driver, 20);

		List<Element> securletSectionTileTitleElement = sp.securletSectionTileTitleElement(driver).getChildElements();
		List<Element> securletSectionTileImageElement = sp.securletSectionTileImageElement(driver).getChildElements();
		for(int i=0;i<securletSectionTileTitleElement.size();i++){
			securletSectionTileTitleElement = sp.securletSectionTileTitleElement(driver).getChildElements();
			securletSectionTileImageElement = sp.securletSectionTileImageElement(driver).getChildElements();
			String title = securletSectionTileTitleElement.get(i).getText().trim();
			if(title.equalsIgnoreCase(app)){
				securletSectionTileImageElement.get(i).mouseOver(driver);
				securletSectionTileImageElement.get(i).click();hardWait(20);
				break;
			}
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void clickSeeAllLessLink(WebDriver driver, String linkText){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		if(sp.securletSectionSeeAllLink(driver).getText().trim().equalsIgnoreCase(linkText)){
			sp.securletSectionSeeAllLink(driver).click();hardWait(5);
		}else{
			Logger.info("Link is already clicked");
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public String getSecurletStoreHeaderDetailedView(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText = sp.securletHeaderDetailedView(driver).getText().trim();
		Logger.info("Actual Header Text:"+headerText);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return headerText;
	}

	public String getSecurletConfigureButtonText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText = sp.securletConfigureButton(driver).getText().trim();
		Logger.info("Actual Header Text:"+headerText);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return headerText;
	}

	public String getSecurletDeactivateButtonText(WebDriver driver,SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText="";;
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			headerText = sp.securletDeactivateButton(driver).getText().trim();
			Logger.info("Actual Header Text:"+headerText);
		}else{
			headerText = sp.securletDeactivateButton1(driver).getText().trim();
			Logger.info("Actual Header Text:"+headerText);
		}


		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return headerText;
	}

	public void clickSecurletDeactivateButton(WebDriver driver,SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			if(getAccountInfoDropdownList(driver).size()>2){
				clickSecurletDeleteButton(driver);
			}else{
				sp.securletDeactivateButton(driver).mouseOver(driver);
				sp.securletDeactivateButton(driver).click();hardWait(5);
			}
		}else{
			sp.securletDeactivateButton1(driver).mouseOver(driver);
			sp.securletDeactivateButton1(driver).click();hardWait(5);
		}



		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletActivationButton(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
			if(getAccountInfoDropdownList(driver).size()>1){
				sp.SalesforceRegisterAccountButton(driver).mouseOver(driver);
				sp.SalesforceRegisterAccountButton(driver).click();hardWait(5);
			}else{
				sp.securletDeactivateButton(driver).mouseOver(driver);
				sp.securletDeactivateButton(driver).click();hardWait(5);
			}
			
		}else{
			sp.securletDeactivateButton(driver).mouseOver(driver);
			sp.securletDeactivateButton(driver).click();hardWait(5);
		}
		
		

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	
	public void clickSecurletDeleteButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.DeleteAccountButton(driver).mouseOver(driver);
		sp.DeleteAccountButton(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletConfigureButton(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			sp.securletConfigureButton(driver).mouseOver(driver);
			sp.securletConfigureButton(driver).click();hardWait(15);
			
			selectAccountFromDropdown(driver, suiteData);
		}
		
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	private void selectAccountFromDropdown(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		
		if(sp.AccountInformationDropdownButton(driver).isElementVisible()){
			sp.AccountInformationButton(driver).click();hardWait(5);
			
			List<Element> AccountInformationDropdown = sp.AccountInformationDropdown(driver).getChildElements();
			for(int i=0;i<AccountInformationDropdown.size();i++){
				AccountInformationDropdown = sp.AccountInformationDropdown(driver).getChildElements();
				if(AccountInformationDropdown.get(i).getText().trim().equalsIgnoreCase
						(suiteData.getTenantDomainName())){
					AccountInformationDropdown.get(i).mouseOver(driver);
					AccountInformationDropdown.get(i).click();hardWait(2);
					break;
				}
				
			}	
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletPurgeDataButton(WebDriver driver,SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			if(getAccountInfoDropdownList(driver).size()>2){
			}else{
				sp.securletPurgeDataButton(driver).check();hardWait(5);
			}
		}else{
			sp.securletPurgeDataButton1(driver).check();hardWait(5);
		}



		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String getSecurletPurgeDataText(WebDriver driver,SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText="";;
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			if(getAccountInfoDropdownList(driver).size()>2){
				headerText = "Purge Data";
			}else{
				headerText = sp.securletPurgeDataText(driver).getText().trim();
				Logger.info("Actual Header Text:"+headerText);
			}
			
			
		}else{
			headerText = sp.securletPurgeDataText1(driver).getText().trim();
			Logger.info("Actual Header Text:"+headerText);
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return headerText;
	}


	public String verifySecurletDeactivateDialogBox(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String message="";

		message += (sp.deactivateDialogBox(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box is not visible";
		message += (sp.deactivateDialogBoxTextOne(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box section one is not visible";
		message += (sp.deactivateDialogBoxTextTwo(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box section two is not visible";
		message += (sp.deactivateDialogBoxCancelButton(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box cancel button is not visible";
		message += (sp.deactivateDialogBoxRemoveButton(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box remove button is not visible";

		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Box)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)||
				SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Office365)){
			String text = sp.deactivateDialogBoxTextOne(driver).getText().trim();
			message += (text.equalsIgnoreCase("You are about to remove the "+suiteData.getTenantDomainName()+" Account.")) ? "": 
				"Securlet deactivate dialog box section one text is not matching "
				+ "Expected:You are about to remove the "+suiteData.getTenantDomainName()+" Account. but was "+text;
		}else{
			String text = sp.deactivateDialogBoxTextOne(driver).getText().trim();
			message += (text.equalsIgnoreCase("You are about to remove the "+suiteData.getSaasAppName()+" Securlet.")) ? "": 
				"Securlet deactivate dialog box section one text is not matching "
				+ "Expected:You are about to remove the "+suiteData.getSaasAppName()+" Securlet. but was "+text;
		}

		String text = sp.deactivateDialogBoxTextTwo(driver).getText().trim();
		message += (text.equalsIgnoreCase("Do you want to continue?")) ? "": 
			"Securlet deactivate dialog box section two text is not matching "
			+ "Expected:Do you want to continue? but was "+text;
		text = sp.deactivateDialogBoxCancelButton(driver).getText().trim();
		message += (text.equalsIgnoreCase("Cancel")) ? "": 
			"Securlet deactivate dialog box cancel button text is not matching "
			+ "Expected:Cancel but was "+text;
		text = sp.deactivateDialogBoxRemoveButton(driver).getText().trim();
		message += (text.equalsIgnoreCase("Remove")) ? "": 
			"Securlet deactivate dialog box remove button text is not matching "
			+ "Remove but was "+text;


		clickDeactivateDialogBoxCancelButton(driver);

		message += (!sp.deactivateDialogBox(driver).isElementVisible()) ? "": 
			"Securlet deactivate dialog box is visible";

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public void clickDeactivateDialogBoxCancelButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.deactivateDialogBoxCancelButton(driver).mouseOver(driver);
		sp.deactivateDialogBoxCancelButton(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickDeactivateDialogBoxRemoveButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.deactivateDialogBoxRemoveButton(driver).mouseOver(driver);
		sp.deactivateDialogBoxRemoveButton(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String verifySecurletDeactivateAlertBox(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String message="";

		message += (sp.deactivateAlertBox(driver).isElementVisible()) ? "": 
			"Securlet alert dialog box is not visible";
		message += (sp.deactivateAlertBoxCloseButton(driver).isElementVisible()) ? "": 
			"Securlet alert close dialog box section one is not visible";
		message += (sp.deactivateAlertBoxMessage(driver).isElementVisible()) ? "": 
			"Securlet alert dialog box message is not visible";

		String text = sp.deactivateAlertBoxMessage(driver).getText().trim();
		Logger.info(text);
		
		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
			message += (text.contains("has been removed successfully for "+suiteData.getSaasAppName()+" Securlet.")) ? "": 
				"Securlet alert dialog box text is not matching "
				+ "Expected:"+suiteData.getSaasAppName()+" Securlet has been removed"+" but was "+text;
		}else{
			message += (text.equalsIgnoreCase(suiteData.getSaasAppName()+" Securlet has been removed")) ? "": 
				"Securlet alert dialog box text is not matching "
				+ "Expected:"+suiteData.getSaasAppName()+" Securlet has been removed"+" but was "+text;
		}
		
		

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public String getSecurletActivateButtonText(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String headerText = sp.securletActivateButton(driver).getText().trim();
		Logger.info("Actual Header Text:"+headerText);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		return headerText;
	}

	public void clickSecurletActivateButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletActivateButton(driver).mouseOver(driver);
		sp.securletActivateButton(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletScanContainerFullScanOption(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletScanContainerFullScanOption(driver).mouseOver(driver);
		sp.securletScanContainerFullScanOption(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletScanContainerSelectiveScanOption(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletScanContainerSelectiveScanOption(driver).mouseOver(driver);
		sp.securletScanContainerSelectiveScanOption(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletScanContainerCancelButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletScanContainerCancelButton(driver).mouseOver(driver);
		sp.securletScanContainerCancelButton(driver).click();hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void clickSecurletScanContainerActivateButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletScanContainerActivateButton(driver).mouseOver(driver);
		sp.securletScanContainerActivateButton(driver).click();hardWait(30);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public void typeInSecurletDomainNameTextBox(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.securletDomainNameTextBox(driver).clear();
		sp.securletDomainNameTextBox(driver).type(suiteData.getTenantDomainName());hardWait(10);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


	public void clickStoreAuthorizationSaveButton(WebDriver driver){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.storeAuthorizationSaveButton(driver).mouseOver(driver);
		sp.storeAuthorizationSaveButton(driver).click();hardWait(30);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}

	public String verifySecurletActivationContainerBox(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		String message="";

		if(SaasType.getSaasType(suiteData.getSaasAppName()).equals(SaasType.Salesforce)){
			
			clickRegisterAccountLink(driver);
			
			message += (sp.SalesforceEditionSelectionDropdownButton(driver).isElementVisible()) ? "": 
				"Salesforce Edition Selection Dropdown Button is not visible";
			String text = sp.SalesforceEditionSelectionDropdownButton(driver).getText().trim();
			message += (text.equalsIgnoreCase("Select Edition")) ? "": 
				"Salesforce Edition Selection Dropdown Button text is not matching "
				+ "Expected:Select Edition"+" and Actual:"+text;
			
			sp.SalesforceEditionSelectionDropdownButton(driver).click();hardWait(5);
			
			String[] expectedList = {"Group Edition","Professional Edition","Enterprise Edition",
					"Unlimited Edition","Performance Edition"};
			List<Element> SalesforceEditionSelectionDropdownList = sp.SalesforceEditionSelectionDropdownList(driver).getChildElements();
			for(int i=0;i<SalesforceEditionSelectionDropdownList.size();i++){
				SalesforceEditionSelectionDropdownList = sp.SalesforceEditionSelectionDropdownList(driver).getChildElements();
				message += (SalesforceEditionSelectionDropdownList.get(i).isElementVisible()) ? "": 
					"Salesforce Edition Selection DropdownList "+(i+1)+" is not visible";
				String actualNameList = SalesforceEditionSelectionDropdownList.get(i).getText().trim();
				message += (actualNameList
						.equalsIgnoreCase(expectedList[i])) ? "": 
					"Salesforce Edition Selection DropdownList "+(i+1)+
					" text is mismatching Expected:"+expectedList[i]+" and Actual:"+actualNameList;
			}	
			
			message += (sp.SalesforceAccountName(driver).isElementVisible()) ? "": 
				"Salesforce Edition Account Name Textbox is not visible";
			message += (sp.SalesforceSandboxName(driver).isElementVisible()) ? "": 
				"Salesforce Edition Sandbox Name Textbox is not visible";
			
			String[] expectedLabel = {"Account Name","Salesforce Sandbox Name",};
			List<Element> SalesforceTabPanelDetailsLabel = sp.SalesforceTabPanelDetailsLabel(driver).getChildElements();
			for(int i=0;i<SalesforceTabPanelDetailsLabel.size();i++){
				SalesforceTabPanelDetailsLabel = sp.SalesforceTabPanelDetailsLabel(driver).getChildElements();
				message += (SalesforceTabPanelDetailsLabel.get(i).isElementVisible()) ? "": 
					"Salesforce Tab Panel Details Label "+(i+1)+" is not visible";
				String actualNameList = SalesforceTabPanelDetailsLabel.get(i).getText().trim();
				message += (actualNameList
						.equalsIgnoreCase(expectedLabel[i])) ? "": 
					"Salesforce Tab Panel Details Label "+(i+1)+
					" text is mismatching Expected:"+expectedLabel[i]+" and Actual:"+actualNameList;
			}
			
			String[] expectedDescription = {"A unique Identifier for Securlet Instance of CloudSOC.",
					"Please enter the name of the sandbox if you wish to activate the Securlet against a sandbox. Leave it blank for your production instance."};
			List<Element> SalesforceTabPanelDetailsDescription = sp.SalesforceTabPanelDetailsDescription(driver).getChildElements();
			for(int i=0;i<SalesforceTabPanelDetailsDescription.size();i++){
				SalesforceTabPanelDetailsDescription = sp.SalesforceTabPanelDetailsDescription(driver).getChildElements();
				message += (SalesforceTabPanelDetailsDescription.get(i).isElementVisible()) ? "": 
					"Salesforce Tab Panel Details Description DropdownList "+(i+1)+" is not visible";
				String actualNameList = SalesforceTabPanelDetailsDescription.get(i).getText().trim();
				message += (actualNameList
						.equalsIgnoreCase(expectedDescription[i])) ? "": 
							"Salesforce Tab Panel Details Description  DropdownList "+(i+1)+
					" text is mismatching Expected:"+expectedDescription[i]+" and Actual:"+actualNameList;
			}
			System.out.println(message);
		}else{
			message += (sp.securletScanContainerImage(driver).isElementVisible()) ? "": 
				"Securlet scan container image is not visible";
			message += (sp.securletScanContainerHeader(driver).isElementVisible()) ? "": 
				"Securlet scan container header is not visible";
			message += (sp.securletScanContainerFullScanOption(driver).isElementVisible()) ? "": 
				"Securlet scan container full scan option radio button is not visible";
			message += (sp.securletScanContainerSelectiveScanOption(driver).isElementVisible()) ? "": 
				"Securlet scan container selective scan option radio button is not visible";
			message += (sp.securletScanContainerFullScanOptionText(driver).isElementVisible()) ? "": 
				"Securlet scan container full scan option radio button text is not visible";
			message += (sp.securletScanContainerSelectiveScanOptionText(driver).isElementVisible()) ? "": 
				"Securlet scan container selective scan option radio button text is not visible";
			message += (sp.securletScanContainerSelectiveScanOptionMessageText(driver).isElementVisible()) ? "": 
				"Securlet scan container selective scan option radio button text is not visible";
			message += (sp.securletScanContainerMessage(driver).isElementVisible()) ? "": 
				"Securlet scan container message is not visible";
			message += (sp.securletScanContainerCancelButton(driver).isElementVisible()) ? "": 
				"Securlet scan container cancel button is not visible";
			message += (sp.securletScanContainerActivateButton(driver).isElementVisible()) ? "": 
				"Securlet scan container activate button is not visible";

			String text = sp.securletScanContainerHeader(driver).getText().trim();
			message += (text.equalsIgnoreCase("Select the type of scan to perform")) ? "": 
				"Securlet scan container header text is not matching "
				+ "Expected:Select the type of scan to perform but was "+text;
			text = sp.securletScanContainerFullScanOptionText(driver).getText().trim();
			message += (text.equalsIgnoreCase("Full scan of all users and folders")) ? "": 
				"Securlet scan container full scan option radio button text is not matching "
				+ "Expected:Full scan of all users and folders but was "+text;
			text = sp.securletScanContainerSelectiveScanOptionText(driver).getText().trim();
			message += (text.equalsIgnoreCase("Selective scan of some users and/or folders")) ? "": 
				"Securlet scan container selective scan option radio button text is not matching "
				+ "Expected:Selective scan of some users and/or folders but was "+text;
			text = sp.securletScanContainerSelectiveScanOptionMessageText(driver).getText().trim();
			message += (text.equalsIgnoreCase("This option will allow you to include or exclude some users, groups and/or folders from the scan.")) ? "": 
				"Securlet scan container selective scan option message text is not matching "
				+ "Expected:This option will allow you to include or exclude some users, groups and/or folders from the scan. but was "+text;
			text = sp.securletScanContainerMessage(driver).getText().trim();
			String expectedText="If your "+suiteData.getSaasAppName()+" account has internal users from multiple domains, please work with your Elastica representative before activating the Securlet. Otherwise, they will be treated as external users, and rescan will be required to add them as secondary domains.";
			message += (text.equalsIgnoreCase(expectedText)) ? "": 
				"Securlet scan container message text is not matching "
				+ "Expected:"+expectedText+" but was "+text ;
			text = sp.securletScanContainerCancelButton(driver).getText().trim();
			message += (text.equalsIgnoreCase("Cancel")) ? "": 
				"Securlet scan container cancel button text is not matching "
				+ "Expected:Cancel but was "+text;
			text = sp.securletScanContainerActivateButton(driver).getText().trim();
			message += (text.equalsIgnoreCase("Activate Securlet")) ? "": 
				"Securlet scan container activate button text is not matching "
				+ "Expected:Activate Securletbut was "+text;

		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		return message;
	}

	public void performBoxAuthorization(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		sp.boxUsernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(5);
		sp.boxPasswordTextBox(driver).type(suiteData.getSaasAppPassword());hardWait(5);
		sp.boxAuthorizeButton(driver).click();hardWait(10);
		sp.boxGrantAccessButton(driver).click();hardWait(30);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public void performDropboxAuthorization(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		if(sp.dropboxUsernameTextBox(driver).isElementPresent(driver)){
			sp.dropboxUsernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(5);
			sp.dropboxPasswordTextBox(driver).type(suiteData.getSaasAppPassword());hardWait(5);
			sp.dropboxAuthorizeButton(driver).mouseOver(driver);
			sp.dropboxAuthorizeButton(driver).click();hardWait(30);
		}

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public void performGoogleAppsAuthorization(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);

		String winHandleBefore = getWindowHandle(driver);

		WebElement iframe = driver.findElement(By.cssSelector("div[role=\"dialog\"] div:nth-child(3) iframe"));
		switchFrame(iframe, driver);
		sp.googleInstallAppButton(driver).mouseOver(driver);
		sp.googleInstallAppButton(driver).click();hardWait(10);

		if(sp.googleIframe2(driver).isElementPresent(driver)){
			WebElement iframe2 = driver.findElement(By.cssSelector("#glass-content>iframe"));
			switchFrame(iframe2, driver);
			sp.googleInstallAppSaveButton(driver).mouseOver(driver);
			sp.googleInstallAppSaveButton(driver).click();hardWait(30);
		}

		switchTopWindow(driver);

		Set<String> winHandlesAfter = getWindowHandles(driver);
		for(String winHandle : winHandlesAfter){
			if(winHandle.equalsIgnoreCase(winHandleBefore)){
				Logger.info("Control is in current window, switching of windows will not be enforced");
			}else{
				Logger.info("Control is in new window, switching of windows will be enforced");
				switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));

				sp.googleUsernameTextBox(driver).type(suiteData.getSaasAppUsername());hardWait(5);
				sp.googleNextButton(driver).mouseOver(driver);sp.googleNextButton(driver).click();hardWait(10);

				sp.googlePasswordTextBox(driver).type(suiteData.getSaasAppPassword());hardWait(5);
				sp.googleSignInButton(driver).mouseOver(driver);sp.googleSignInButton(driver).click();hardWait(30);

				break;
			}
		}


		switchToWindow(driver,winHandleBefore);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public void performSalesforceAuthorization(WebDriver driver, SuiteData suiteData) throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		
		if(sp.SalesforceLoginUsername(driver).isElementVisible()){
			sp.SalesforceLoginUsername(driver).type(suiteData.getSaasAppUsername());hardWait(5);
			sp.SalesforceLoginPassword(driver).type(suiteData.getSaasAppPassword());hardWait(5);
			sp.SalesforceLoginButton(driver).mouseOver(driver);
			sp.SalesforceLoginButton(driver).click();hardWait(45);
			
			if(driver.getTitle().contains("Verify Your Identity")){
				UniversalAPIFunctions universalAPI = new UniversalAPIFunctions();
				String verificationCode = universalAPI.getVerificationCode(suiteData);
				sp.SalesforceVerificationCodeTextbox(driver).type(verificationCode);hardWait(2);
				sp.SalesforceVerificationCodeVerifyButton(driver).click();hardWait(30);
			}
		}
		
		if(sp.SalesforceUpgradeButton(driver).isElementVisible()){
			sp.SalesforceUpgradeButton(driver).click();hardWait(45);
		}
		if(sp.SalesforceDoneButton(driver).isElementVisible()){
			sp.SalesforceDoneButton(driver).click();hardWait(45);
		}
		
		String namePrefix = sp.SalesforceNamePrefix(driver).getText().trim();
		
		sp.SalesforceAllTabs(driver).click();hardWait(20);
		
		List<Element> SalesforceElasticaSecurletSetup = sp.SalesforceElasticaSecurletSetup(driver).getChildElements();
		for(int i=0;i<SalesforceElasticaSecurletSetup.size();i++){
			SalesforceElasticaSecurletSetup = sp.SalesforceElasticaSecurletSetup(driver).getChildElements();
			String currentUrl = getCurrentUrl(driver);
			SalesforceElasticaSecurletSetup.get(i).click();hardWait(20);
			String url = getCurrentUrl(driver);
			if(url.contains(namePrefix)){
				String winHandleBefore = getWindowHandle(driver);
				sp.SalesforceConfigureLink(driver).click();hardWait(20);
				Set<String> winHandlesAfter = getWindowHandles(driver);
				for(String winHandle : winHandlesAfter){
					if(winHandle.equalsIgnoreCase(winHandleBefore)){
						Logger.info("Control is in current window, switching of windows will not be enforced");
					}else{
						Logger.info("Control is in new window, switching of windows will be enforced");
						switchToWindow(driver,winHandle);Logger.info(getCurrentUrl(driver));
						sp.SalesforceLoginUsername(driver).type(suiteData.getSaasAppUsername());hardWait(2);
						sp.SalesforceLoginPassword(driver).type(suiteData.getSaasAppPassword());hardWait(2);
						sp.SalesforceLoginButton(driver).mouseOver(driver);
						sp.SalesforceLoginButton(driver).click();hardWait(30);
						break;
					}
				}	
				
				
				
				break;
			}else{
				driver.get(currentUrl);hardWait(20);
			}
		
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}
	
	public void saasAppAuthorization(WebDriver driver, SuiteData suiteData) throws Exception {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		Logger.info("Logging into Saas App with "+suiteData.getSaasAppName()+ " with credentials: "
				+suiteData.getSaasAppUsername()+"=="+suiteData.getSaasAppPassword());

		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		switch (stype) {
		case Box: {
			typeInSecurletDomainNameTextBox(driver,suiteData);
			clickSecurletActivationButton(driver, suiteData);
			performBoxAuthorization(driver, suiteData); 
			break;
		}
		case Dropbox: {
			performDropboxAuthorization(driver, suiteData);
			break;
		}
		case GoogleApps: {
			clickStoreAuthorizationSaveButton(driver);
			performGoogleAppsAuthorization(driver, suiteData);
			break;
		}
		case Office365: {

			break;
		}
		case Salesforce: {
			clickRegisterAccountLink(driver);
			selectDropdownOptionsSalesforceMenu(driver, "Enterprise Edition");
			typeInSalesforceAccountNameTextBox(driver, suiteData);
			typeInSalesforceSandboxNameTextBox(driver, suiteData);
			clickSecurletActivationButton(driver, suiteData);
			performSalesforceAuthorization(driver, suiteData);
			break;
		}
		default: {

			break;
		}
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public void clickRegisterAccountLink(WebDriver driver) {
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		if(sp.AccountInformationDropdownButton(driver).isElementVisible()){
			sp.AccountInformationButton(driver).click();hardWait(5);
			sp.AccountInformationRegisterAccountLink(driver).click();hardWait(5);
		}
	}


	public void navigationInSecurletDeactivation(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		switch (stype) {
		case Box: {

			break;
		}
		case Dropbox: {

			break;
		}
		case GoogleApps: {

			break;
		}
		case Office365: {

			break;
		}
		default: {

			break;
		}
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}


	public void navigationInSecurletActivation(WebDriver driver, SuiteData suiteData) {
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		SaasType stype = SaasType.getSaasType(suiteData.getSaasAppName());
		switch (stype) {
		case Box: {

			break;
		}
		case Dropbox: {

			break;
		}
		case GoogleApps: {

			break;
		}
		case Office365: {

			break;
		}
		default: {

			break;
		}
		}
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

	}

	public void typeInSalesforceAccountNameTextBox(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.SalesforceAccountName(driver).clear();
		sp.SalesforceAccountName(driver).type(suiteData.getTenantDomainName());hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void typeInSalesforceSandboxNameTextBox(WebDriver driver, SuiteData suiteData){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());

		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.SalesforceSandboxName(driver).clear();
		sp.SalesforceSandboxName(driver).type(suiteData.getSaasAppUsername());hardWait(5);

		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	public void selectDropdownOptionsSalesforceMenu(WebDriver driver, String editionName){
		Logger.info("Started the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
		
		StorePage sp =  AdvancedPageFactory.getPageObject(driver, StorePage.class);
		sp.SalesforceEditionSelectionDropdownButton(driver).click();hardWait(5);
		
		List<Element> SalesforceEditionSelectionDropdownList = sp.SalesforceEditionSelectionDropdownList(driver).getChildElements();
		for(int i=0;i<SalesforceEditionSelectionDropdownList.size();i++){
			SalesforceEditionSelectionDropdownList = sp.SalesforceEditionSelectionDropdownList(driver).getChildElements();
			if(SalesforceEditionSelectionDropdownList.get(i).getText().trim().equalsIgnoreCase(editionName)){
				SalesforceEditionSelectionDropdownList.get(i).mouseOver(driver);
				SalesforceEditionSelectionDropdownList.get(i).click();hardWait(5);
				break;
			}
			
		}	
	
		Logger.info("Completed the method:"+Thread.currentThread().getStackTrace()[1].getMethodName());
	}


}
