package com.elastica.pageobjects.investigate;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.CheckBox;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

/**
 * Investigate page
 * @author Eldo Rajan
 *
 */
public class InvestigatePage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activityLogCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink dateDropdownButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public TextBox dateTextbox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	
	public ElementList<Element> activityElement(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public Element serviceanduserfilterdatatab(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("DATA", value));
	}
	
	public HyperLink serviceAndUserFiltertab(WebDriver driver, String tabname){
		return new HyperLink(driver, getLocator().replaceAll("FIELD", tabname));
	}
	
	public Button filterButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public Element selectApp(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("APP", value));
	}
	
	public HyperLink selectActivityRange(WebDriver driver, String index){
		return new HyperLink(driver,  getLocator().replaceAll("INDEX", index));
	}
	
	public Element logCount(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("APP", value));
	}
	
	public Element downFilterCount(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("APP", value));
	}
	
	public Element downFilterServicecount(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("APP", value));
	}
	
	public ElementList<Element> logTableServiceName(WebDriver driver, String value){
		return new ElementList<Element>(driver,  getLocator().replaceAll("APP", value));
	}
	
	
	public HyperLink investigateIcon(WebDriver driver){
		return new HyperLink(driver,  getLocator());
	}
	
	public HyperLink objectList(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink activityList(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public Element filterValues(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("values", value));
	}
	
	public HyperLink activityElementMessage(WebDriver driver, int value){
		return new HyperLink(driver, getLocator().replaceAll("value", String.valueOf(value)));
	}
	
	public CheckBox gatewayCheckbox(WebDriver driver){
		return new CheckBox(driver, getLocator());
	}
	
	public HyperLink investigateFoterText(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateToggleTab(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSearchBoxTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeviceTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectSevice(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeviceFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeviceFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeviceFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeviceMatchLogCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectInstance(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateInstanceFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateInstanceFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateInstanceFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateUserTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectUser(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateUserFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateUserFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateUserFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectAccontType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateAccountTypeFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateAccountTypeFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateAccountTypeFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateObjectTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateObjectFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateObjectFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateObjectFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateActiveTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateActiveFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateActiveFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateActiveFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeverityTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeverityFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeverityFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSeverityFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSourceLocationTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSourceLocationFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSourceLocationFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSourceLocationFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateBrowserTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateBrowserFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateBrowserFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateBrowserFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatePlatformTabs(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectPlatform(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatePlatformFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatePlatformFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatePlatformFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelectDevice(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateDeviceFirstCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateDeviceFirstSeviceName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateDeviceFirstSeviceCount(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public ElementList<Element> investigateTableRowCountCount(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	public HyperLink investigateExportasCEF(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateExportasCSV(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateExportasLEEF(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateAlertMessage(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateLearnMore(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateLearnMoreIcon(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateLearnMoreH1(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateLearnMoreH2(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectorbutton(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectordaydropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectorweekdropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectormonthdropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectorthreemonthdropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectorsixmonthdropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectoryeardropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectortwoyeardropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatetimeperiodselectorthreeyeardropdown(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public Element filterItem(WebDriver driver, String value){
		return new Element(driver,  getLocator().replaceAll("values", value));
	}
	public HyperLink investigateAPIClickCheckBox(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigatePageTableIcon(WebDriver driver, int value){
		return new HyperLink(driver,  getLocator().replaceAll("values", Integer.toString(value)));
	}
	public HyperLink investigatePageTableServiceName(WebDriver driver, int value){
		return new HyperLink(driver,  getLocator().replaceAll("values", Integer.toString(value)));
	}
	public HyperLink investigatePageTableServiceBadge(WebDriver driver, int value){
		return new HyperLink(driver,  getLocator().replaceAll("values", Integer.toString(value)));
	}
	public HyperLink investigatePageTableFiileName(WebDriver driver, int value){
		return new HyperLink(driver,  getLocator().replaceAll("values", Integer.toString(value)));
	}
	public HyperLink investigatePageTableFiileInfo(WebDriver driver, int value){
		return new HyperLink(driver,  getLocator().replaceAll("values", Integer.toString(value)));
	}
	public HyperLink ipService(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipUserName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipUser(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipSeverity(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipHappenedAt(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipRecordedAt(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipMessage(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipHost(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipObjectType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipInFolder(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ObjectType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink FileType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipInternalrecipients(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipActivityType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipHasAccessTo(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipapplication(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipclient_id(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipSharedWith(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipAccountType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipsender(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	
	public HyperLink ipLongitude(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipDocumentType(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipLatitude(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipSourceLocation(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipFileSize(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipResource_Id(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipattachments(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipsubject(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink iptarget_account_type(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipcity(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipcountry(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipinstance(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipLocation(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipName(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipRole(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipParen(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink ipRisks(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipContentVulnerabilities(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink ipParent_id(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investgatepopupclose(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink investigateFilter(WebDriver driver){
		return new HyperLink(driver,  getLocator());
	}
	
	public ElementList<Element> investigateFilters(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}

	public HyperLink investigateFiltersRemove(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public HyperLink investigateDropdownClick(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	public HyperLink investigateSelect3Years(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
	public ElementList<Element> investigateDetailedViewFieldName(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	
	public ElementList<Element> investigateDetailedViewFieldValue(WebDriver driver){
		return new ElementList<Element>(driver,  getLocator());
	}
	public HyperLink ObjectNotAvailable(WebDriver driver){
		return new HyperLink(driver, getLocator());
	}
	
}