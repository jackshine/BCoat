package com.elastica.beatle.tests.securlets.salesforce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.LogValidator;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.tests.securlets.CustomAssertion;
import com.universal.constants.CommonConstants;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.FileShares;
import com.universal.dtos.salesforce.FileSharesInput;
import com.universal.dtos.salesforce.InternalFileShare;
import com.universal.dtos.salesforce.SObject;
import com.universal.util.OAuth20Token;

public class SalesforceDashboardMetrics extends SecurletUtils{

	
	LogValidator logValidator;
	DocumentValidator docValidator;
	
	long maxWaitTime = 20;
	long minWaitTime = 10;

	String saasAppUsername;
	String saasAppPassword;
	String saasAppUserRole;
	String saasAppLoginHost;
	String createdTime;
	String destinationFile;
	String saasAppUser;
	String instanceUrl;
	String instanceId;
	
	SalesforceUtils sfutils;
	String internalUserId1;
	String externalUserId1;
	String externalUserId2;
	
	public SalesforceDashboardMetrics() throws Exception {
		
		logValidator = new LogValidator(); 
		sfutils = new SalesforceUtils();
		docValidator = new DocumentValidator();
	}

	@BeforeClass(alwaysRun=true)
	public void initSalesforce() throws Exception {
		this.saasAppUsername 	= getRegressionSpecificSuitParameters("saasAppUsername");
		this.saasAppPassword 	= getRegressionSpecificSuitParameters("saasAppPassword");
		this.saasAppUserRole 	= getRegressionSpecificSuitParameters("saasAppUserRole");
		this.saasAppLoginHost 	= getRegressionSpecificSuitParameters("saasAppLoginHost");
		this.internalUserId1	= getRegressionSpecificSuitParameters("saasAppEndUser1Name");
		this.externalUserId1	= getRegressionSpecificSuitParameters("saasAppExternalUser1Name");
		this.externalUserId2	= getRegressionSpecificSuitParameters("saasAppExternalUser2Name");
		
		if(saasAppUsername.toLowerCase().contains(".sandbox")) {
			saasAppUser = StringUtils.chop(saasAppUsername.toLowerCase()).replace(".sandbox", "");
		} else {
			saasAppUser = saasAppUsername;
		}
		
		OAuth20Token tokenObj = sfapi.getTokenObject();
		
		//https://test.salesforce.com/id/00D170000008i3DEAQ/005o0000000RTtEAAW  //4th param is the id
		instanceUrl = tokenObj.getInstanceUrl();
		Reporter.log("Token Id:" + tokenObj.getId(), true);
		instanceId = tokenObj.getId().split("/")[4];
		
	}
	
	
	/**
	 * Test 1
	 * Check the metrics as depicted in venn diagram
	 * Check with UI server call and API server call and check metrics are same
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER", "P1"})
	public void verifyVennDiagramMetrics() throws Exception {

		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram ",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		
		LogUtils.logStep("1. GET Internal exposure metrics totals with API server call");
		
		ExposureTotals apiCallTotals = getExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		long apiCountPublic 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternal 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternal 	= apiCallTotals.getExternalExposouresCount();

		Reporter.log("API call Public count  :"+apiCountPublic, true);
		Reporter.log("API call Internal count:"+apiCountInternal, true);
		Reporter.log("API call External count:"+apiCountExternal, true);
		
		LogUtils.logStep("2. GET Internal exposure metrics totals with UI server call");
		
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		
		ExposureTotals uiCallTotals = getUIExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		long uiCountPublic 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternal 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternal 	= uiCallTotals.getExternalExposouresCount();
		
		Reporter.log("UI Call Public count  :"+uiCountPublic, true);
		Reporter.log("UI Call Internal count:"+uiCountInternal, true);
		Reporter.log("UI Call External count:"+uiCountExternal, true);
		
		LogUtils.logStep("3. Verifying UI Server call metrics and API Server call metrics");
		
		CustomAssertion.assertEquals(apiCountPublic, uiCountPublic, 
							"API public count "+apiCountPublic+" is not matching with UI public count "+uiCountPublic);
		CustomAssertion.assertEquals(apiCountInternal, uiCountInternal, 
							"API internal count "+apiCountInternal+" is not matching with UI internal count "+uiCountInternal);
		CustomAssertion.assertEquals(apiCountExternal, uiCountExternal, 
							"API internal count "+apiCountExternal+" is not matching with UI internal count "+uiCountExternal);
		
		
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		
		LogUtils.logStep("4. Check the exposed files count..");
		
		SecurletDocument exposedDocuments = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		
		docparams.add(new BasicNameValuePair("exposures.types",  "ext_count,all_internal,public"));
		SecurletDocument allDocuments = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		
		CustomAssertion.assertEquals(allDocuments.getMeta().getTotalCount(), exposedDocuments.getMeta().getTotalCount(), 
															"Exposed documents count is not matching with exposed files count");	
		
		LogUtils.logStep("5. Check the internally, externally, publicly exposed files and filtered results");
		
		String exposureTypes[] = {"public","all_internal","ext_count"};
		long apiCounts[] = {apiCountPublic, apiCountInternal, apiCountExternal};
		long uiCounts[] =  {uiCountPublic,  uiCountInternal,  uiCountExternal};
		
		for (int i = 0; i< exposureTypes.length; i++) {
			docparams.clear();
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
			docparams.add(new BasicNameValuePair("exposures.types",  exposureTypes[i]));
			SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
			CustomAssertion.assertEquals(apiCounts[i], documents.getMeta().getTotalCount(),  
								"Exposed documents count is not matching with API Count" + exposureTypes[i] + " count");
			
			CustomAssertion.assertEquals(uiCounts[i], documents.getMeta().getTotalCount(),  
								"Exposed documents count is not matching with API Count" + exposureTypes[i] + " count");
		}
		
		
		
		
		
		
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
//		exposures.types=public
//				ext_count,all_internal
//				ext_count,all_internal,public
		
	}
	
	
	/**
	 * Test 2
	 * Check the metrics after a public exposure
	 * Check with UI server call and API server call and check metrics are same
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER", "P1"})
	public void verifyVennDiagramMetricsAfterPublicExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a public exposure and after removing the public exposire",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicBeforeExposure 	= uiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		LogUtils.logStep("2. Expose a file publicly and get the metrics");
		
		//Expose a file publicly and check the metrics 
		String fileId = exposeFilePublicly(null);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET Internal exposure metrics totals with API server call and UI Server call after exposure");
		ExposureTotals uiCallTotalsAfterExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying public exposure count");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure, apiCountPublicBeforeExposure+1, "API Public exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountPublicAfterExposure,  uiCountPublicBeforeExposure+1,  "UI Public exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI Public exposure count and API public exposure count should be equal");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure,  apiCountInternalBeforeExposure,  "Internal count should not change after public exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  apiCountExternalBeforeExposure,  "External count should not change after public exposure");
		
		
		LogUtils.logStep("5. Deleting the public link");
		//clean up the file
		sfapi.deleteFileShareLink(fileId);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET exposure metrics totals with API server and UI server call");
		
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying public exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure, apiCountPublicBeforeExposure, "API Public exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure, uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API external count and UI external count are not same");
		
		//clean up the file
		sfapi.deleteFile(fileId);
	}	
	
	
	/**
	 * Test 3
	 * 
	 */
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "sharingType", groups={"DASHBOARD", "P1", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsAfterInternalExposure(String sharingType) throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a internal exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountInternalBeforeExposure = uiCallTotalsBeforeExposure.getInternalExposouresCount();
		
		LogUtils.logStep("2. Expose a file internally and get the metrics");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeFileInternally(null, null, sharingType);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying internal exposure count");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure, apiCountInternalBeforeExposure+1, "API Internal exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountInternalAfterExposure, uiCountInternalBeforeExposure+1, "API Internal exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure,  uiCountInternalAfterExposure,  "UI Internal exposure count and API Internal exposure count should be equal");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  apiCountExternalBeforeExposure,  "External count should not change after Internal exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure,  "UI public exposure count and API Internal exposure count should be equal");
		
		LogUtils.logStep("5. Deleting the internal share");
		//remove the internal share
		sfapi.removeInternalShare(hmap.get("sharedId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET Internal exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying internal exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure, apiCountInternalBeforeExposure, "API Internal exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API external count and UI external count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
		
		//clean up the file
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	
	/**
	 * Test 4
	 * 
	 */
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "sharingType", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsAfterExternalExposure(String sharingType) throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a external exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountExternalBeforeExposure = uiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Expose a file externally and get the metrics");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeFileExternally(null, null, sharingType);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		long uiCountExternalAfterExposure 	= uiCallTotalsAfterExposure.getExternalExposouresCount();
		
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying external exposure count");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure, apiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountExternalAfterExposure, uiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  uiCountExternalAfterExposure,  "UI External exposure count and API External exposure count should be equal");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure,  apiCountInternalBeforeExposure,  "Internal count should not change after External exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure,  "UI public exposure count and API public exposure count should be equal");
		
		LogUtils.logStep("5. Deleting the external share");
		//remove the external share
		sfapi.removeCollaborator(hmap.get("fileId"), sharingType);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET Exnternal exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying External exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure, apiCountExternalBeforeExposure, "API External exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API external count and UI external count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
		
		//clean up the file
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	
	/**
	 * Test 5
	 * 
	 */
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "sharingType", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsAfterPublicExternalExposure(String sharingType) throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a external exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountExternalBeforeExposure = uiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Expose a file publicly and externally and get the metrics");
		String fileId = this.exposeFilePublicly(null);
		
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeFileExternally(fileId, null, sharingType);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		long uiCountExternalAfterExposure 	= uiCallTotalsAfterExposure.getExternalExposouresCount();
		
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying external exposure count");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure, apiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountExternalAfterExposure, uiCountExternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  uiCountExternalAfterExposure,  "UI External exposure count and API External exposure count should be equal");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure,  apiCountInternalBeforeExposure,  "Internal count should not change after External exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure+1,  "API public exposure count should increment by 1 after public exposure");
		
		LogUtils.logStep("5. Deleting the external share");
		//remove the external share
		sfapi.removeCollaborator(hmap.get("fileId"), sharingType);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET External exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying External exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure, apiCountExternalBeforeExposure, "API External exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API external count and UI external count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    apiCountPublicAfterExposure, "API Public exposure count  and UI public count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
		
		//clean up the file
		sfapi.deleteFileShareLink(fileId);
				
		//clean up the file
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	
	/**
	 * Test 6
	 * 
	 */
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "sharingType", groups={"DASHBOARD", "P1", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsAfterPublicInternalExposure(String sharingType) throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a internal exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountExternalBeforeExposure = uiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Expose a file publicly and externally and get the metrics");
		String fileId = this.exposeFilePublicly(null);
		
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeFileInternally(fileId, null, sharingType);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		long uiCountExternalAfterExposure 	= uiCallTotalsAfterExposure.getExternalExposouresCount();
		
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying external exposure count");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure, apiCountInternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(uiCountInternalAfterExposure, apiCountInternalAfterExposure, "API Internal exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  apiCountExternalBeforeExposure,  "External count should not change after External exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure+1,  "API public exposure count should increment by 1 after public exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		
		LogUtils.logStep("5. Deleting the internal share");
		//remove the internal share
		sfapi.removeInternalShare(hmap.get("sharedId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET Exnternal exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying External exposure after cleaning up the exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  apiCountInternalBeforeExposure, "API External exposure count not decremented by 1 after cleaning exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure,  uiCountInternalAfterCleaningExposure,  "API external count and UI external count are not same");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  uiCountExternalAfterCleaningExposure,  "API internal count and UI internal count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    apiCountPublicAfterExposure, "API Public exposure count  and UI public count are not same");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,    uiCountPublicAfterCleaningExposure, "API Public exposure count  and UI public count are not same");
		
		//clean up the file
		sfapi.deleteFileShareLink(fileId);
				
		//clean up the file
		sfapi.deleteFile(hmap.get("fileId"));
	}
	
	
	
	/**
	 * Test 7
	 * 
	 */
	
	@Test(dataProviderClass = SalesforceDataProvider.class, dataProvider = "sharingType", groups={"DASHBOARD", "P1", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsAfterPublicInternalExternalExposure(String sharingType) throws Exception {
		
		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram after a public, internal, external exposure and after removing it",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. GET exposure metrics totals with API server call and UI Server call before exposure");
		
		
		ExposureTotals apiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountInternalBeforeExposure = apiCallTotalsBeforeExposure.getInternalExposouresCount();
		long apiCountExternalBeforeExposure = apiCallTotalsBeforeExposure.getExternalExposouresCount();
		long apiCountPublicBeforeExposure 	= apiCallTotalsBeforeExposure.getPublicExposouresCount();
		
		ExposureTotals uiCallTotalsBeforeExposure = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountExternalBeforeExposure = uiCallTotalsBeforeExposure.getExternalExposouresCount();
		
		LogUtils.logStep("2. Expose a file publicly , internally and externally and get the metrics");
		String fileId = this.exposeFilePublicly(null);
		
		//Expose a file externally 
		HashMap<String, String> hmapEshare = exposeFileExternally(fileId, null, sharingType);
				
		//Expose a file internally
		HashMap<String, String> hmapIshare = exposeFileInternally(fileId, null, sharingType);
		
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		LogUtils.logStep("3. GET exposure metrics totals with API server call and UI Server call after exposure");
		
		ExposureTotals uiCallTotalsAfterExposure =  getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterExposure 	= uiCallTotalsAfterExposure.getPublicExposouresCount();
		long uiCountInternalAfterExposure 	= uiCallTotalsAfterExposure.getInternalExposouresCount();
		long uiCountExternalAfterExposure 	= uiCallTotalsAfterExposure.getExternalExposouresCount();
		
		
		ExposureTotals apiCallTotalsAfterExposure = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterExposure 	= apiCallTotalsAfterExposure.getPublicExposouresCount();
		long apiCountInternalAfterExposure 	= apiCallTotalsAfterExposure.getInternalExposouresCount();
		long apiCountExternalAfterExposure 	= apiCallTotalsAfterExposure.getExternalExposouresCount();
		
		LogUtils.logStep("4. Verifying  exposure count");
		CustomAssertion.assertEquals(apiCountInternalAfterExposure, apiCountInternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  apiCountPublicBeforeExposure+1,  "API public exposure count should increment by 1 after public exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  apiCountExternalBeforeExposure+1,  "External count should increment by 1 after External exposure");
		
		CustomAssertion.assertEquals(uiCountInternalAfterExposure, apiCountInternalAfterExposure, "API Internal exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterExposure,  uiCountExternalAfterExposure,  "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterExposure,  uiCountPublicAfterExposure,  "UI public exposure count and API public exposure count should be equal");
		
		LogUtils.logStep("5. Deleting the internal share");
		
		//remove all the share
		sfapi.removeInternalShare(hmapIshare.get("sharedId"));
		//public link removal
		sfapi.deleteFileShareLink(fileId);
		//external link removal
		sfapi.removeCollaborator(hmapEshare.get("fileId"), sharingType);
		
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("6. GET  exposure metrics totals with API server and UI server call");
		
		ExposureTotals apiCallTotals = getSalesforceExposureMetricsWithAPI(true, null);
		long apiCountPublicAfterCleaningExposure 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternalAfterCleaningExposure 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternalAfterCleaningExposure 	= apiCallTotals.getExternalExposouresCount();
		
		ExposureTotals uiCallTotals = getSalesforceExposureMetricsWithUI(true, null);
		long uiCountPublicAfterCleaningExposure 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternalAfterCleaningExposure 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternalAfterCleaningExposure 	= uiCallTotals.getExternalExposouresCount();
		
		LogUtils.logStep("7. Verifying  exposure after cleaning up the exposure");CustomAssertion.assertEquals(apiCountInternalAfterExposure, apiCountInternalBeforeExposure+1, "API External exposure count not incremented by 1 after exposure");
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure,  apiCountPublicBeforeExposure,  "API public exposure count should decrement by 1 after public exposure");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure,  apiCountExternalBeforeExposure,  "External count should decrement by 1 after External exposure");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure, apiCountInternalBeforeExposure, "API Internal exposure count not decremented by 1 after exposure");
		
		CustomAssertion.assertEquals(apiCountPublicAfterCleaningExposure, uiCountPublicAfterCleaningExposure, "API & UI metrics should be same.");
		CustomAssertion.assertEquals(apiCountInternalAfterCleaningExposure, uiCountInternalAfterCleaningExposure, "API & UI metrics should be same.");
		CustomAssertion.assertEquals(apiCountExternalAfterCleaningExposure, uiCountExternalAfterCleaningExposure, "API & UI metrics should be same.");
				
		//clean up the file
		sfapi.deleteFile(hmapEshare.get("fileId"));
	}
	
	
	
	/**
	 * Test 8
	 * Check the exposed files after a public exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterPublicExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after a public exposure and after removing the public exposure",
				"2. Check the exposed files after a public exposure.",
				"3. Check the exposed files after removing public exposure."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. Expose a file publicly.");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeAFilePublicly(null);
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		
		LogUtils.logStep("2. After exposure, checking the publicly shared document is present in the exposed files tab...");
		docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the public link");
		sfapi.deleteFileShareLink(hmap.get("fileId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	/**
	 * Test 9
	 * Check the exposed files after a internal exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterInternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after a internal exposure and after removing the internal exposure",
				"2. Check the exposed files after a internal exposure.",
				"3. Check the exposed files after removing internal exposure."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. Expose a file internally with collaborator permission.");
		
		HashMap<String, String> hmap = uploadFile(null);
		
		//Expose a file internally and check the metrics 
		HashMap<String, String> hmapIshare = this.exposeFileInternally(hmap.get("fileId"), null, "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		
		LogUtils.logStep("2. After exposure, checking the internally shared document is present in the exposed files tab...");
		docValidator.verifyInternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the internal share");
		sfapi.removeInternalShare(hmapIshare.get("sharedId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	/**
	 * Test 10
	 * Check the exposed files after a external exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterExternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after a external exposure and after removing the external exposure",
				"2. Check the exposed files after a external exposure.",
				"3. Check the exposed files after removing external exposure."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. Expose a file externally with collaborator permission.");
		
		HashMap<String, String> hmap = uploadFile(null);
		
		//Expose a file internally and check the metrics 
		HashMap<String, String> hmapEshare = this.exposeFileExternally(hmap.get("fileId"), null, "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		LogUtils.logStep("2. After exposure, checking the externally shared document is present in the exposed files tab...");
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		docValidator.verifyExternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the external share");
		sfapi.removeCollaborator(hmap.get("fileId"), "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}	
	
	
	
	/**
	 * Test 11
	 * Check the exposed files after a public and external exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterPublicExternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after a external exposure and after removing the external exposure",
				"2. Check the exposed files after a external exposure.",
				"3. Check the exposed files after removing external exposure."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. Expose a file externally with collaborator permission.");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeAFilePublicly(null);
		
		//Expose a file internally and check the metrics 
		HashMap<String, String> hmapEshare = this.exposeFileExternally(hmap.get("fileId"), null, "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		LogUtils.logStep("2. After exposure, checking the publicly,externally shared document is present in the exposed files tab...");
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		docValidator.verifyExternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the public,external share");
		sfapi.removeCollaborator(hmap.get("fileId"), "C");
		sfapi.deleteFileShareLink(hmap.get("fileId"));
		
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}
	
	
	/**
	 * Test 12
	 * Check the exposed files after a public and internal exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterExternalInternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after a public and internal exposure and after removing the external exposure",
				"2. Check the exposed files after a external exposure.",
				"3. Check the exposed files after removing external exposure."
				};

		LogUtils.logTestDescription(steps);
		LogUtils.logStep("1. Expose a file externally with collaborator permission.");
		
		HashMap<String, String> hmap = uploadFile(null);
		
		HashMap<String, String> hmapIshare =  this.exposeFileInternally(hmap.get("fileId"), null, "C");
		
		//Expose a file internally and check the metrics 
		HashMap<String, String> hmapEshare = this.exposeFileExternally(hmap.get("fileId"), null, "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		LogUtils.logStep("2. After exposure, checking the publicly,externally shared document is present in the exposed files tab...");
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		docValidator.verifyExternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		docValidator.verifyInternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the internal,external share");
		sfapi.removeCollaborator(hmap.get("fileId"), "C");
		sfapi.removeInternalShare(hmapIshare.get("sharedId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}
	
	
	/**
	 * Test 13
	 * Check the exposed files after a public and internal exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "P1", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposedFilesAfterPublicInternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after an public and internal exposure and after removing the exposure",
				"2. Check the exposed files after a external exposure.",
				"3. Check the exposed files after removing external exposure."
				};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Expose a file publicly with collaborator permission.");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeAFilePublicly(null);
		
		HashMap<String, String> hmapIshare =  this.exposeFileInternally(hmap.get("fileId"), null, "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		LogUtils.logStep("2. After exposure, checking the publicly,externally shared document is present in the exposed files tab...");
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		docValidator.verifyInternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the internal,external share");
		sfapi.deleteFileShareLink(hmap.get("fileId"));
		sfapi.removeInternalShare(hmapIshare.get("sharedId"));
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}
	
	
	/**
	 * Test 14
	 * Check the exposed files after a public, external and internal exposure
	 * 
	 * 
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER", "P1"})
	public void verifyExposedFilesAfterPublicInternalExternalExposure() throws Exception {
		
		String steps[] = {
				"1. This test check the exposed files after an public, external and internal exposure and after removing the exposure",
				"2. Check the exposed files after a external exposure.",
				"3. Check the exposed files after removing external exposure."
				};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Expose a file publicly with collaborator permission.");
		
		//Expose a file publicly and check the metrics 
		HashMap<String, String> hmap = exposeAFilePublicly(null);
		HashMap<String, String> hmapIshare =  this.exposeFileInternally(hmap.get("fileId"), null, "C");
		HashMap<String, String> hmapEshare = this.exposeFileExternally(hmap.get("fileId"), null, "C");
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		
		
		docparams.add(new BasicNameValuePair("name",  hmap.get("name")));
		LogUtils.logStep("2. After exposure, checking the publicly,externally shared document is present in the exposed files tab...");
		SecurletDocument documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		docValidator.verifyInternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		docValidator.verifyExternallyExposedDocument(documents, this.saasAppUserAccount,  hmap.get("name"), "File");
		
		LogUtils.logStep("3. Deleting the internal,external share");
		sfapi.deleteFileShareLink(hmap.get("fileId"));
		sfapi.removeInternalShare(hmapIshare.get("sharedId"));
		sfapi.removeCollaborator(hmap.get("fileId"), "C");
		
		sleep(CommonConstants.FIVE_MINUTES_SLEEP);
		LogUtils.logStep("4. After removing exposure, checking the file is removed from the exposed files tab...");
		documents = getExposedDocuments(elapp.el_salesforce.name(), docparams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Exposed Files tab still showing the deleted file it seems.");
		
		LogUtils.logStep("5. Clean up the file.");
		sfapi.deleteFile(hmap.get("fileId"));
	}
	
	
	//Utility methods  - Need to be moved to Salesforce utils
	public String exposeFilePublicly(String filename) throws Exception {
		
		String sourceFile = filename == null ? "test.pdf": filename;
		
		//Upload the file
		String randomId = String.valueOf(System.currentTimeMillis());
		String destinationFile = randomId + "_" + sourceFile;
		ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
		String fileId = chatterfile.getId();
		Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
		Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
		
		LogUtils.logStep("Sharing the file publicly.");
		sfapi.createFileShareLink(fileId);
		LogUtils.logStep("File shared publicly.");		
		
		return fileId;
	}
	
	
	public HashMap<String, String> exposeAFilePublicly(String filename) throws Exception {
		HashMap<String, String> hmap = new HashMap<String,String>();
		String sourceFile = filename == null ? "test.pdf": filename;
		
		//Upload the file
		String randomId = String.valueOf(System.currentTimeMillis());
		String destinationFile = randomId + "_" + sourceFile;
		ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
		String fileId = chatterfile.getId();
		Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
		Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
		
		LogUtils.logStep("Sharing the file publicly.");
		sfapi.createFileShareLink(fileId);
		LogUtils.logStep("File shared publicly.");		
		hmap.put("fileId", fileId);
		hmap.put("name", destinationFile);
		return hmap;
	}
	
	
	public HashMap<String, String> exposeFileInternally(String fileid, String filename, String permission) throws Exception {
		HashMap<String, String> hmap = new HashMap<String,String>();
		
		String fileId = (fileid != null) ? fileid : uploadAFile(filename);
		
		LogUtils.logStep("Sharing the file externally.");
		
		LogUtils.logStep("Sharing the file internally.");
		InternalFileShare ifs = new InternalFileShare(permission, fileId, this.instanceId);
		SObject sharedId = sfapi.shareFileInternally(ifs);
		
		hmap.put("fileId", fileId);
		hmap.put("sharedId", sharedId.getId());
		
		LogUtils.logStep("File shared internally.");		
		
		return hmap;
	}
	
	public HashMap<String, String> exposeFileExternally(String fileid, String filename, String permission) throws Exception {
		HashMap<String, String> hmap = new HashMap<String,String>();
		String fileId = (fileid != null) ? fileid : uploadAFile(filename);
		
		LogUtils.logStep("Sharing the file externally.");
		
		//Get the user id for fileshare
		String userId1 = sfapi.getExternalUserId(externalUserId1, "Securletuser11", "SUser11", "ExtUser11");
		
		String ids[] = {userId1};
		String sharingTypes[] = {permission};
		FileSharesInput fsi = frameFileShareInputObject(ids, sharingTypes, "I have shared a file with "+permission+" permission.");
		sfapi.shareFilewithCollaborator(fileId, fsi);
		
		hmap.put("fileId", fileId);
		LogUtils.logStep("File shared externally.");		
		
		return hmap;
	}
	
	
	public String uploadAFile(String filename) throws Exception {
		String sourceFile = filename == null ? "test.pdf": filename;
		
		//Upload the file
		String randomId = String.valueOf(System.currentTimeMillis());
		String destinationFile = randomId + "_" + sourceFile;
		ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
		String fileId = chatterfile.getId();
		Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
		Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
		return fileId;
	}
	
	public HashMap<String,String> uploadFile(String filename) throws Exception {
		HashMap<String, String> hmap = new HashMap<String,String>();
		String sourceFile = filename == null ? "test.pdf": filename;
		
		//Upload the file
		String randomId = String.valueOf(System.currentTimeMillis());
		String destinationFile = randomId + "_" + sourceFile;
		ChatterFile chatterfile = sfapi.uploadFileToChatter(sourceFile, destinationFile);
		String fileId = chatterfile.getId();
		Reporter.log("Chatter file contents:"+ MarshallingUtils.marshall(chatterfile), true);
		Reporter.log("File "+ chatterfile.getName() +" uploaded to salesforce chatter."+ destinationFile, true);
		hmap.put("fileId", fileId);
		hmap.put("name", destinationFile);
		return hmap;
	}
	
	
//	
//	@Test
//	public void removeCollab() throws Exception{
//		sfapi.removeCollaborator("0693B0000000DsMQAU", "C");
//	}
//	
	
	public ExposureTotals  getSalesforceExposureMetricsWithAPI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		return exposureTotals;
	}
	
	public ExposureTotals  getSalesforceExposureMetricsWithUI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		ExposureTotals exposureTotals = getUIExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		return exposureTotals;
	}
	
}
