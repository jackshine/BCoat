package com.elastica.pageobjects.securlets;

import org.openqa.selenium.WebDriver;
import com.elastica.webelements.BasePage;
import com.elastica.webelements.Button;
import com.elastica.webelements.Element;
import com.elastica.webelements.ElementList;
import com.elastica.webelements.HyperLink;
import com.elastica.webelements.TextBox;

/**
 * Securlet page
 * @author Eldo Rajan
 *
 */
public class SecurletDashboardPage extends BasePage{

	public HyperLink header(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> securletTabs(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink exposedFilesExposuresWidget(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesTopRisksTypesWidget(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesTopContentTypesWidget(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesExposuresWidgetHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesTopRisksTypesWidgetHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesTopContentTypesWidgetHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}

	public ElementList<Element> exposedFilesExposuresWidgetCircles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesExposuresWidgetNumbers(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesExposuresWidgetText(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopRisksTypesWidgetList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopRisksTypesWidgetListIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopRisksTypesWidgetListInfoHeader(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopRisksTypesWidgetListInfoFiles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopRisksTypesWidgetListUsageBar(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopContentTypesWidgetList(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopContentTypesWidgetListIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopContentTypesWidgetListInfoHeader(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopContentTypesWidgetListInfoFiles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesTopContentTypesWidgetListUsageBar(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesFilesTableHeaders(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public Button exposedFilesExportCSVButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	
	public HyperLink exposedFilesFilesTableListCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesFilesTableSearchField(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesFilesTableSearchButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresColumns(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresColumnsName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresColumnsAscDescIndicator(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresColumnsHandle(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresColumnsSubHeading(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRows(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowTitles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowOwner(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowSize(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowRisks(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesFilesTableInternalExposuresRowTypes(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTopRiskTypeRowsCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTopContentTypeRowsCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink topRiskTypesPiiIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesPciIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesSourceCodeIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesHipaaIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesGlbaIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topRiskTypesHealthIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesBusinessIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesComputingIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesDesignDocIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topRiskTypesLegalIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> topRiskTypeRowsCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink topRiskTypeRowsTopFileTypesIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink topRiskTypeRowsTopFileTypesHeadle(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink topRiskTypeRowsTopFileTypesFilesCounts(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink topRiskTypeRowsTopFileTypesBarIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopRiskTypeRowsTopFileTypesIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopRiskTypeRowsTopFileTypesHeadle(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopRiskTypeRowsTopFileTypesFilesCounts(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink otherRisksTopRiskTypeRowsTopFileTypesBarIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopContentTypeRowsTopFileTypesIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopContentTypeRowsTopFileTypesHeadle(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink otherRisksTopContentTypeRowsTopFileTypesFilesCounts(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	public HyperLink otherRisksTopContentTypeRowsTopFileTypesBarIcon(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public HyperLink topOtherRiskExportCsv(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskExportCsvAlertMessage(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskShowOverviewVideo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskVideo(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowPrivateTxtBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> otherRisksTableInternalExposuresColumns(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresColumnsName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public ElementList<Element> otherRisksTableInternalExposuresRows(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowTitles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowOwner(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowSize(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowRisks(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> otherRisksTableInternalExposuresRowTypes(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink otherRisksPopupRowTitles(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksPopupRowOwner(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksPopupRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksPopupRowSize(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowTitles(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowOwner(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowSize(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksPopupClose(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupClose(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedFilesPopupRowPrivateLink(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink signInButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink ExternalClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskLearnMore(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskLearnMoreHandle(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherOptions(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherScanNow(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherViewScanPolicies(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherViewScanPoliciesHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherViewScanPoliciesBody(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherViewScanPoliciesPopupClose(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherViewScanPoliciesFilterTestBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}	
	public HyperLink topOtherViewScanPoliciesFilterButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherToggleTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherSearchBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topOtherSearchBoxClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topOtherClearFiltersButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherAppTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskTypeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherRiskTypeTabClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topOtherContentIQProfileTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherContentTypeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherFileTypesTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherSizeSortAsc(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherSizeSortDesc(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherTableSize(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherTableOwner(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherTableDocument(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherSearchIcon(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherSearchIconInHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topOtherSearchIconClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> topOtherTableDocumentCategory(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink topOtherTapContentTypeTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherContentTypeTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherFileTypesTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> topOtherTableEFileTypeIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink topOtherContentIQProfileTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> topOtherTableContentIQProfile(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink topOtherRiskTypeTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink topOtherRiskTypeTabItemNameClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public ElementList<Element> topOtherTableRiskType(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink topOtherTapRiskTypesTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink topOtherTapFileTypesTabItemName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersDoughnutChartTotalUserCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersDoughnutChartInternalUserCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersDoughnutChartExternalUserCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> exposedUsersTableInternalUserCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedUsersTableExternalUserCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public HyperLink exposedUsersTabExternalCollaborators(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersTabInternalCollaborators(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersTabExternalAppTableFirstRowCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesSearchHeader(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabUserTableFirstRowCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabUserTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabSeverity(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabSeverityTableFirstRowCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabSeverityTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabObjectTableFirstRowCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabObjectTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink activitiesTabUserTableFirstRowName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public ElementList<Element> activitiesTabUserTableCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink activitiesTabUserTableRowCount(WebDriver driver, int count){
		return new HyperLink(driver,getLocator().replace("SCOUNT", Integer.toString(count)));
	}
	
	public ElementList<Element> exposedFilesTableInternalExposuresRows(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowTitles(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowIcon(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowName(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowOwner(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowCount(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowSize(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowRisks(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	public ElementList<Element> exposedFilesTableInternalExposuresRowTypes(WebDriver driver){
		return new ElementList<Element>(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabExternalAppTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink otherRisksaContentTypeTabUserTableFirstRowCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksaContentTypeTabUserTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksaContentTypeTabUserTableFirstRowName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink otherRisksaTopContentTypeTabUserTableFirstRow(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksaTopContentTypeTabUserTableFirstRowCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink otherRisksaTopContentTypeTabUserTableFirstRowName(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink scanPolicyPopup(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public TextBox scanPolicyPopupExcludeFolderTextBox(WebDriver driver){
		return new TextBox(driver,getLocator());
	}
	public Button scanPolicyStartScanButton(WebDriver driver){
		return new Button(driver,getLocator());
	}
	public HyperLink exposedUsersTabFileExposureTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabFileExposureTabClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabFileExposureTabInternalCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabFileExposureTabInternalCheckBoxButton(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabFileExposureTabInternalCheckBoxClick(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabFileExposureTabExternalCheckBox(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersTabFileExposureTabInternalCheckBoxIsVerifyed(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabRiskTypeTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	public HyperLink exposedUsersTabRiskTypeTabFirstRowLabel(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabRiskTypeTabFirstRowLabelSecondtProriotyVerifyed(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedUsersTabRiskTypeTabFirstRowLabelFirstProriotyVerifyed(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink exposedFilesTabPopupExposuresTab(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	public HyperLink activitiesAllLogCount(WebDriver driver){
		return new HyperLink(driver,getLocator());
	}
	
	
	
}
