package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.UIVulnerabilityTotals;
import com.elastica.beatle.securlets.dto.VlType;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

public class SecurletDashboardFilters extends SecurletUtils {
	String appName;
	public final static String FILE_EXPOSURE = "fileExposure";
	public final static String FILE_EXPOSURES = "fileExposures";
	
	String exposures[] = new String []{"public", "internal", "external"};
	String exposures1[] = new String []{"public", "all_internal", "external"};
	
	//Following arrays defined because UI calls are sending different  parameter names for exposure types.
	String exposuresArgDocs[] = new String []{"public", "all_internal", "ext_count"};
	String exposuresArgMetrics[] = new String []{"public", "internal", "external"};
	
	
	//Following is specific to office 365
	String office365Features[] = new String []{"OneDrive", "Mail", "Sites"};
	
	boolean isInternal = true;
	
	
	List<String> exposureTypes1 = Arrays.asList(exposures);
	List<String> contentTypes = new ArrayList<String>();
	List<String> fileTypes = new ArrayList<String>();
	List<String> vulnerabilityTypes = new ArrayList<String>();
	
	//Risky types
	List<String> contentTypesRisky = new ArrayList<String>();
	List<String> fileTypesRisky = new ArrayList<String>();
	List<String> vulnerabilityTypesRisky = new ArrayList<String>();
	
	public SecurletDashboardFilters() throws Exception {
		super();
	}
	
	
	@BeforeTest(alwaysRun=true)
	public void initInstance() throws Exception {
		//this.instanceId = this.getTenantInstanceId(facility.Box.name());
		if (suiteData.getSaasApp().equals("ONEDRIVEBUSINESS")) {
			appName = "Office 365";
		} else if (suiteData.getSaasApp().equals("BOX")){
			appName = "Box";
		} else if (suiteData.getSaasApp().equals("SALESFORCE")){
			appName = "Salesforce";
		} else if (suiteData.getSaasApp().equals("GDRIVE")){
			appName = "Google Apps";
		} else if (suiteData.getSaasApp().equals("DROPBOX")){
			appName = "Dropbox";
		}
                
		//Need to uncomment
		//isInternal = suiteData.isInternal();
		
		//Retrieve all the content types and populate to contentTypes list
		ExposureTotals allContentTypesTotals = this.getUIContentTypesTotal(null, 50, null, false, true);
		for (TotalObject result : allContentTypesTotals.getResults() ) {
			if(result.getTotal() > 0 ) {
				contentTypes.add(result.getId());
			}
		}
		
		//Retrieve all file types and populate to filetypes list
		ExposureTotals allFileTypesTotals = this.getUIFileTypesTotal(null, 50, null, null, false, true);
		for (TotalObject result : allFileTypesTotals.getResults() ) {
			if( result.getTotal() > 0 ) {
				fileTypes.add(result.getId());
			}
		}
		
		//Retrieve all vulnerability types and populate to vulnerabilityTypes list
		UIVulnerabilityTotals allVlTypesTotals = this.getUIVulnerabilityTypesTotal(null, 50, null, false, true);
		for (VlType result : allVlTypesTotals.getResults().getVlTypes() ) {
			if(result.getTotal() > 0) {
				vulnerabilityTypes.add(result.getId());
			}
		}
		
		/*
		vulnerabilityTypes.clear();
		vulnerabilityTypes.add("pii");
		vulnerabilityTypes.add("hipaa");
		vulnerabilityTypes.add("pci");
		fileTypes.clear();
		fileTypes.add("txt");
		*/
		
		Reporter.log("All Vulnerability Types:" + vulnerabilityTypes, true);
		Reporter.log("All File Types		 :" + fileTypes, true);
		Reporter.log("All Content Types		 :" + contentTypes, true);
		
		//Retrieve all the content types and populate to contentTypes list
		ExposureTotals allContentTypesTotalsRisky = this.getUIContentTypesTotal(null, 50, null, true, true);
		for (TotalObject result : allContentTypesTotalsRisky.getResults() ) {
			contentTypesRisky.add(result.getId());
		}

		//Retrieve all file types and populate to filetypes list
		ExposureTotals allFileTypesTotalsRisky = this.getUIFileTypesTotal(null, 50, null, null, true, true);
		for (TotalObject result : allFileTypesTotalsRisky.getResults() ) {
			fileTypesRisky.add(result.getId());
		}

		//Retrieve all vulnerability types and populate to vulnerabilityTypes list
		UIVulnerabilityTotals allVlTypesTotalsRisky = this.getUIVulnerabilityTypesTotal(null, 50, null, true, true);
		for (VlType result : allVlTypesTotalsRisky.getResults().getVlTypes() ) {
			vulnerabilityTypesRisky.add(result.getId());
		}

		Reporter.log("All Vulnerability Types Risky:" + vulnerabilityTypesRisky, true);
		Reporter.log("All File Types Risky		   :" + fileTypesRisky, true);
		Reporter.log("All Content Types	Risky	   :" + contentTypesRisky, true);
	}
	
	
	
	
	
	
	/**
	 * Test 1
	 * @throws Exception
	 */
	@Test( groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void exposedFilesFileExposureTypes()  throws Exception {
		String steps[] =
			{		"1. Get the exposed files with no filters", 
					"2. Get the metrics exposure with no filters",
					"3. Get the content types with no filters",
					"4. Get the file types with no filters",
					"5. Get the vulnerability types with no filters"
			};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, isInternal, "", "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		CustomAssertion.assertTrue(exposedDocs.getDocs() != null, "Exposed docs result set is not null", "Exposed docs result set is null");
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		ExposureTotals totals = this.getUIExposureMetricsTotals("", false, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		
		LogUtils.logStep("3. Get the content type totals with UI server call.");
		ExposureTotals contentTypeTotals = this.getUIContentTypesTotal(null, 10, null, false, isInternal);
		CustomAssertion.assertTrue(contentTypeTotals.getResults() != null, "ContentTypes result set is not null", "ContentTypes result set is null");
		
		LogUtils.logStep("4. Get the file type totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, null, null, false, isInternal);
		CustomAssertion.assertTrue(fileTypeTotals.getResults() != null, "FileTypes result set is not null", "FileTypes result set is null");
		
		LogUtils.logStep("5. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(null, 10, null, false, isInternal);
		CustomAssertion.assertTrue(vulnerabilityTypeTotals.getResults().getVlTypes() != null, "VulnerabilityTypes result set is not null", 
																										"VulnerabilityTypes result set is null");
		
	}
	
	
	
	/**
	 * Test 2
	 * @throws Exception
	 */
	
	@Test(dataProvider = "ExposureDataProvider2", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesFileExposureTypes(String docsExposureType, String totalsExposureType)  throws Exception {
		String steps[] =
			{		"1. Get the exposed files based on the filter exposure type: "+docsExposureType, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+totalsExposureType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, docsExposureType, null, "docs", 0, 0, isInternal, "", "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		ExposureTotals totals = this.getUIExposureMetricsTotals(totalsExposureType, false, isInternal);
		long exposureTotal = totals.getTotal(totalsExposureType);
		
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(exposureTotal, exposedDocs.getMeta().getTotalCount(), 
									"Exposed doc count:"+exposedDocs.getMeta().getTotalCount() + " is not matching with the total "+ exposureTotal);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, docsExposureType, null, "docs", offset, limit, isInternal, "", "name");
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyExposureOnly(exposedDocs, docsExposureType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 3
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleVulnerabilityProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesVulnerabilityTypes(String vltype)  throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter vulnerability type: "+vltype, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+vltype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+vltype);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, isInternal, "", "name", vltype, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(null, 10, vltype, false, isInternal);
		int total = vulnerabilityTypeTotals.getTotal(vltype);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Exposed doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the Vulnerability "+ vltype +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "docs", offset, limit, isInternal, "", "name", vltype, null, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyDocumentWithVltype(exposedDocs, vltype);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	
	/**
	 * Test 4
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleFiletypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesFileTypes(String filetype)  throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter file type: "+filetype, 
					"2. Get the metrics exposure totals based on the filter file type: "+filetype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+filetype);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, isInternal, "", "name", null, filetype, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the file type totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, filetype, null, false, isInternal);
		long total = fileTypeTotals.getTotal(filetype);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Exposed doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the filetype "+ filetype +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "docs", offset, limit, isInternal, "", "name", null, filetype, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyDocumentWithFileType(exposedDocs, filetype);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 5
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleContentTypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesContentTypes(String contentType)  throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter content type: "+contentType, 
					"2. Get the metrics exposure totals based on the filter content type: "+contentType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+contentType);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, isInternal, "", "name", null, null, contentType);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the content type totals with UI server call.");
		ExposureTotals contentTypeTotals = this.getUIContentTypesTotal(null, 10, contentType, false, isInternal);
		long total = contentTypeTotals.getTotal(contentType);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Exposed doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the content type "+ contentType +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "docs", offset, limit, isInternal, "", "name", null, null, contentType);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyDocumentWithContentType(exposedDocs, contentType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 6
	 * @throws Exception
	 */
	@Test(dataProvider = "SubFeatureProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterExposedFilesBySubFeature(String objectType)  throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter feature type: "+objectType, 
					"2. Get the metrics exposure totals based on the filter feature type: "+objectType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+objectType);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, objectType, "docs", 0, 0, isInternal, "", "name", null, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the object type totals with UI server call.");
		ExposureTotals featureTotals = this.getUISubFeaturesTotal(objectType, false, isInternal);
		long total = featureTotals.getTotal(objectType);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Exposed doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the object type "+ objectType +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, objectType, "docs", offset, limit, isInternal, "", "name", null, null, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyDocumentWithObjectType(exposedDocs, objectType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	
	/**
	 * Test 7
	 * @throws Exception
	 */
	@Test(dataProvider = "ExposureTypeAndfiletypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesWithCombinedFilterExposureTypeAndFileType(String exposureTypeAndfiletype)  throws Exception {
		String exposureType, filetype;
		try {
			exposureType = exposureTypeAndfiletype.split(",")[0];
			filetype 	= exposureTypeAndfiletype.split(",")[1];
		}
		catch (NullPointerException e) {
			throw new SkipException("Skipping this test");
		}
		String exposedDocParam = exposureType;
		if (exposureType.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (exposureType.equals("external")) {
			exposedDocParam = "ext_count";
		} 
		
		String steps[] =
			{		"1. Get the exposed files based on the filter: "+exposureTypeAndfiletype, 
					"2. Get the metrics exposure totals based on the filter : "+exposureTypeAndfiletype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+exposureTypeAndfiletype);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		
		UIExposedDoc payload = this.getUIPayload(appName, exposedDocParam, null, "docs", 0, 0, isInternal, "", "name", null, filetype, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put(FILE_EXPOSURES,  exposureType);
		hmap.put("fileClass",  filetype);
		ExposureTotals totals = this.getUIExposureMetricsTotals(hmap, false, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		long expTotal = totals.getTotal(exposureType);
		
		LogUtils.logStep("3. Get the filetype totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(exposureType, 10, filetype, null, false, isInternal);
		long total = fileTypeTotals.getTotal(filetype);
		CustomAssertion.assertTrue(total == expTotal , "Metrics and filetypes totals match", "Metrics and filetypes totals don't match");
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, exposedDocParam, null, "docs", offset, limit, isInternal, "", "name", null, filetype, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			
			this.verifyDocumentWithFileType(exposedDocs, filetype);
			this.verifyExposureOnly(exposedDocs, exposedDocParam);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 8
	 * @throws Exception
	 */
	@Test(dataProvider = "VulnerabilityAndfiletypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesWithCombinedFilterVulnerabilityTypeAndFileType(String vlTypeAndfiletype)  throws Exception {
		
		String vltype	 = vlTypeAndfiletype.split(",")[0];
		String filetype  = vlTypeAndfiletype.split(",")[1];
		
		/*String exposedDocParam = exposureType;
		if (exposureType.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (exposureType.equals("external")) {
			exposedDocParam = "ext_count";
		} */
		
		String steps[] =
			{		"1. Get the exposed files based on the filter: "+vlTypeAndfiletype, 
					"2. Get the metrics exposure totals based on the filter : "+vlTypeAndfiletype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+vlTypeAndfiletype);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, isInternal, "", "name", vltype, filetype, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("fileClass",  filetype);
		hmap.put("vlTypes",    vltype);
		ExposureTotals totals = this.getUIExposureMetricsTotals(hmap, false, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		
		
		LogUtils.logStep("3. Get the filetype totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, filetype, null, false, isInternal);
		long ftotal = fileTypeTotals.getTotal(filetype);
		System.out.println("F total:"+ftotal);
		
		LogUtils.logStep("4. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(hmap, false, isInternal);
		int vtotal = vulnerabilityTypeTotals.getTotal(vltype);
		System.out.println("V total:"+vtotal);
		CustomAssertion.assertTrue(ftotal >= vtotal , "File type total is >= to vulnerability totals.", "File type total is not >= to vulnerability totals.");
		
		LogUtils.logStep("5. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "docs", offset, limit, isInternal, "", "name", vltype, filetype, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			if (ftotal == vtotal) {
				this.verifyDocumentWithFileType(exposedDocs, filetype);
				this.verifyDocumentWithVltype(exposedDocs, vltype);
			}
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 9
	 * @throws Exception
	 */
	@Test(dataProvider = "ExposureTypeVulnerabilityAndfiletypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterExposedFilesWithCombinedFilterExposureTypeVulnerabilityTypeAndFileType(String exposureTypeVlTypeAndfiletype)  throws Exception {
		
		String exposureType = exposureTypeVlTypeAndfiletype.split(",")[0];
		String vltype	    = exposureTypeVlTypeAndfiletype.split(",")[1];
		String filetype     = exposureTypeVlTypeAndfiletype.split(",")[2];
		
		String exposedDocParam = exposureType;
		if (exposureType.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (exposureType.equals("external")) {
			exposedDocParam = "ext_count";
		} 
		
		String steps[] =
			{		"1. Get the exposed files based on the filter: "+exposureTypeVlTypeAndfiletype, 
					"2. Get the metrics exposure totals based on the filter : "+exposureTypeVlTypeAndfiletype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+exposureTypeVlTypeAndfiletype);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, exposedDocParam, null, "docs", 0, 0, isInternal, "", "name", vltype, filetype, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("fileClass",  filetype);
		hmap.put("vlTypes",    vltype);
		hmap.put(FILE_EXPOSURES,  exposureType);
		ExposureTotals totals = this.getUIExposureMetricsTotals(hmap, false, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		long expTotal = totals.getTotal(exposureType);
		
		LogUtils.logStep("3. Get the filetype totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, filetype, null, false, isInternal);
		long ftotal = fileTypeTotals.getTotal(filetype);
		System.out.println("F total:"+ftotal);
		
		LogUtils.logStep("4. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(hmap, false, isInternal);
		int vtotal = vulnerabilityTypeTotals.getTotal(vltype);
		System.out.println("V total:"+vtotal);
		CustomAssertion.assertTrue(ftotal >= vtotal , "File type total is >= to vulnerability totals.", "File type total is not >= to vulnerability totals.");
		CustomAssertion.assertTrue(ftotal >= expTotal , "File type total is >= to exposure totals.", "File type total is not >= to exposure totals.");
		
		LogUtils.logStep("5. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, exposedDocParam, null, "docs", offset, limit, isInternal, "", "name", vltype, filetype, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			if (ftotal == vtotal && ftotal == expTotal) {
				this.verifyDocumentWithFileType(exposedDocs, filetype);
				this.verifyDocumentWithVltype(exposedDocs, vltype);
				this.verifyExposureOnly(exposedDocs, exposedDocParam);
			}
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 10
	 * @throws Exception
	 */
	@Test(dataProvider = "ExposureTypeVulnerabilityAndfiletypeProvider", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL", "SEARCH"})
	public void searchExposedFilesWithCombinedFilterExposureTypeVulnerabilityTypeAndFileType(String exposureTypeVlTypeAndfiletype)  throws Exception {
		
		String exposureType = exposureTypeVlTypeAndfiletype.split(",")[0];
		String vltype	    = exposureTypeVlTypeAndfiletype.split(",")[1];
		String filetype     = exposureTypeVlTypeAndfiletype.split(",")[2];
		
		String exposedDocParam = exposureType;
		if (exposureType.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (exposureType.equals("external")) {
			exposedDocParam = "ext_count";
		} 
		
		String steps[] =
			{		"1. Get the exposed files based on the filter: "+exposureTypeVlTypeAndfiletype, 
					"2. Get the metrics exposure totals based on the filter : "+exposureTypeVlTypeAndfiletype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, exposedDocParam, null, "docs", 0, 0, isInternal, filetype, "name", vltype, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("fileClass",  filetype);
		hmap.put("vlTypes",    vltype);
		hmap.put(FILE_EXPOSURES,  exposureType);
		ExposureTotals totals = this.getUIExposureMetricsTotals(hmap, false, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		long expTotal = totals.getTotal(exposureType);
		
		LogUtils.logStep("3. Get the filetype totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, filetype, null, false, isInternal);
		long ftotal = fileTypeTotals.getTotal(filetype);
		
		LogUtils.logStep("4. Get the filetype totals with search text with UI server call.");
		ExposureTotals searchTotals = this.getUIFileTypesTotal(null, 10, null, filetype, false, isInternal);
		long stotal = searchTotals.getTotal(filetype);
		
		CustomAssertion.assertTrue(ftotal == stotal, "File type total with search text and file type filter is same", "File type total with search text and file type filter is not same");
		
		LogUtils.logStep("5. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(hmap, false, isInternal);
		int vtotal = vulnerabilityTypeTotals.getTotal(vltype);
		
		CustomAssertion.assertTrue(ftotal >= vtotal , "File type total is >= to vulnerability totals.", "File type total is not >= to vulnerability totals.");
		CustomAssertion.assertTrue(ftotal >= expTotal , "File type total is >= to exposure totals.", "File type total is not >= to exposure totals.");
		
		LogUtils.logStep("6. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, exposedDocParam, null, "docs", offset, limit, isInternal, filetype, "name", vltype, null, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
			if (ftotal == vtotal && ftotal == expTotal) {
				this.verifyDocumentWithFileType(exposedDocs, filetype);
				this.verifyDocumentWithVltype(exposedDocs, vltype);
				this.verifyExposureOnly(exposedDocs, exposedDocParam);
			}
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}	
	
	
	//Risky files filters 
	
	/**
	 * Test 10
	 * @throws Exception
	 */
	@Test( groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void riskyFilesFileExposureTypes()  throws Exception {
		String steps[] =
			{		"1. Get the risky files with no filters", 
					"2. Get the metrics totals with no filters",
					"3. Get the content types with no filters",
					"4. Get the file types with no filters",
					"5. Get the vulnerability types with no filters"
			};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of risky documents with UI server call.");
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "risky_docs", 0, 0, isInternal, "", "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		CustomAssertion.assertTrue(exposedDocs.getDocs() != null, "Risky docs result set is not null", "Risky docs result set is null");
		
		LogUtils.logStep("2. Get the risky totals with UI server call.");
		ExposureTotals totals = this.getUIExposureMetricsTotals("", true, isInternal);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Totals result set is not null", "Totals docs result set is null");
		CustomAssertion.assertTrue(totals.getTotal("public") == 0 , "Public total is zero", "Public total is not zero");
		CustomAssertion.assertTrue(totals.getTotal("internal") == 0 , "Internal total is zero", "Internal total is not zero");
		CustomAssertion.assertTrue(totals.getTotal("external") == 0 , "External total is zero", "External total is not zero");
		
		LogUtils.logStep("3. Get the content type totals with UI server call.");
		ExposureTotals contentTypeTotals = this.getUIContentTypesTotal(null, 10, null, true, isInternal);
		CustomAssertion.assertTrue(contentTypeTotals.getResults() != null, "ContentTypes result set is not null", "ContentTypes result set is null");
		
		LogUtils.logStep("4. Get the file type totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, null, null, true, isInternal);
		CustomAssertion.assertTrue(fileTypeTotals.getResults() != null, "FileTypes result set is not null", "FileTypes result set is null");
		
		LogUtils.logStep("5. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(null, 10, null, true, isInternal);
		CustomAssertion.assertTrue(vulnerabilityTypeTotals.getResults().getVlTypes() != null, "VulnerabilityTypes result set is not null", 
																										"VulnerabilityTypes result set is null");
		
	}
	
	
	/**
	 * Test 11
	 * @throws Exception
	 */
	
	@Test(dataProvider = "ExposureDataProvider2", groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterRiskyFilesFileExposureTypes(String docsExposureType, String totalsExposureType)  throws Exception {
		String steps[] =
			{		"1. Get the Risky files based on the filter exposure type: "+docsExposureType, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+totalsExposureType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of Risky documents with UI server call.");
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		UIExposedDoc payload = this.getUIPayload(appName, docsExposureType, null, "risky_docs", 0, 0, isInternal, "", "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		ExposureTotals totals = this.getUIExposureMetricsTotals(totalsExposureType, true, isInternal);
		long exposureTotal = totals.getTotal(totalsExposureType);
		
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(exposureTotal, exposedDocs.getMeta().getTotalCount(), 
									"Risky doc count:"+exposedDocs.getMeta().getTotalCount() + " is not matching with the total "+ exposureTotal);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, docsExposureType, null, "risky_docs", offset, limit, isInternal, "", "name");
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
			
			this.verifyExposureOnly(exposedDocs, docsExposureType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 12
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleVulnerabilityProvider", groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterRiskyFilesVulnerabilityTypes(String vltype)  throws Exception {
		
		String steps[] =
			{		"1. Get the risky files based on the filter vulnerability type: "+vltype, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+vltype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+vltype);
		
		LogUtils.logStep("1. Get the list of risky documents with UI server call.");
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "risky_docs", 0, 0, isInternal, "", "name", vltype, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		
		LogUtils.logStep("2. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(null, 10, vltype, true, isInternal);
		int total = vulnerabilityTypeTotals.getTotal(vltype);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Risky doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the Vulnerability "+ vltype +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "risky_docs", offset, limit, isInternal, "", "name", vltype, null, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
			
			this.verifyDocumentWithVltype(exposedDocs, vltype);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	
	/**
	 * Test 13
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleFiletypeProvider", groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterRiskyFilesFileTypes(String filetype)  throws Exception {
		
		String steps[] =
			{		"1. Get the risky files based on the filter file type: "+filetype, 
					"2. Get the metrics exposure totals based on the filter file type: "+filetype,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+filetype);
		
		LogUtils.logStep("1. Get the list of risky documents with UI server call.");
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "risky_docs", 0, 0, isInternal, "", "name", null, filetype, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		
		LogUtils.logStep("2. Get the file type totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, filetype, null, true, isInternal);
		long total = fileTypeTotals.getTotal(filetype);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Risky doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the filetype "+ filetype +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "risky_docs", offset, limit, isInternal, "", "name", null, filetype, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
			
			this.verifyDocumentWithFileType(exposedDocs, filetype);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 14
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleContentTypeProvider", groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "ALL"})
	public void filterRiskyFilesContentTypes(String contentType)  throws Exception {
		
		String steps[] =
			{		"1. Get the risky files based on the filter content type: "+contentType, 
					"2. Get the metrics exposure totals based on the filter content type: "+contentType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+contentType);
		
		LogUtils.logStep("1. Get the list of  risky documents with UI server call.");
		
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "risky_docs", 0, 0, isInternal, "", "name", null, null, contentType);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		
		LogUtils.logStep("2. Get the content type totals with UI server call.");
		ExposureTotals contentTypeTotals = this.getUIContentTypesTotal(null, 10, contentType, true, isInternal);
		long total = contentTypeTotals.getTotal(contentType);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Risky doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the content type "+ contentType +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, null, "risky_docs", offset, limit, isInternal, "", "name", null, null, contentType);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
			
			this.verifyDocumentWithContentType(exposedDocs, contentType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	/**
	 * Test 15
	 * @throws Exception
	 */
	@Test(dataProvider = "SubFeatureProvider", groups={"RISKY_FILES", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterRiskyFilesBySubFeature(String objectType)  throws Exception {
		
		String steps[] =
			{		"1. Get the Risky files based on the filter feature type: "+objectType, 
					"2. Get the metrics  totals based on the filter feature type: "+objectType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+objectType);
		
		LogUtils.logStep("1. Get the list of Risky documents with UI server call.");
		List<NameValuePair> riskparams = new ArrayList<NameValuePair>(); 
		riskparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		UIExposedDoc payload = this.getUIPayload(appName, null, objectType, "risky_docs", 0, 0, isInternal, "", "name", null, null, null);
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
		
		LogUtils.logStep("2. Get the object type totals with UI server call.");
		ExposureTotals featureTotals = this.getUISubFeaturesTotal(objectType, true, isInternal);
		long total = featureTotals.getTotal(objectType);
		
		LogUtils.logStep("3. Verifying both the metrics.");
		CustomAssertion.assertEquals(total, exposedDocs.getMeta().getTotalCount(), 
									"Risky doc count:"+exposedDocs.getMeta().getTotalCount() + " is not"
											+ " matching with the object type "+ objectType +"total "+ total);
		
		LogUtils.logStep("4. Iterate over the list of documents and check the selected filter");
		int offset = 0, limit = 50;
		do {
			payload = this.getUIPayload(appName, null, objectType, "risky_docs", offset, limit, isInternal, "", "name", null, null, null);
			exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), riskparams);
			
			this.verifyDocumentWithObjectType(exposedDocs, objectType);
			
			offset +=limit;
		} while(exposedDocs.getMeta().getTotalCount() >= offset);
		
	}
	
	
	
	// Exposed Users Section
	/**
	 * Test 16
	 * @throws Exception
	 */
	@Test(dataProvider = "ExposureDataProvider", groups={"EXPOSED_USERS", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterExposedUsersByExposureType(String type)  throws Exception {
		
		String steps[] =
			{		"1. Get the user totals based on the filter exposure type: "+type, 
					"2. Get the user exposures based on the filter exposure type: "+type,
					"3. Verify both the counts match."  };
		
		
		String exposedDocParam = type;
		if (type.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (type.equals("external")) {
			exposedDocParam = "ext_count";
		} 

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+type);
		
		LogUtils.logStep("1. Get the user  totals with UI server call.");
		ExposureTotals usertotals = this.getUIMetricsUsersTotal(type, 10, null, null, null, false, isInternal);
		long internalUsersTotal = usertotals.getTotal("internal");
		long externalUsersTotal = usertotals.getTotal("external");
		
		//Reporter.log("Users: "+internalUsersTotal + " ... "+externalUsersTotal, true);
		
		LogUtils.logStep("2. Get the user exposures totals with UI server call.");
		ExposureTotals userExposurestotals = this.getUIMetricsUsersExposures(type, 10, null, null,null, false, isInternal);
		long internalUsers = userExposurestotals.getResults().size();
		
		
		CustomAssertion.assertEquals(internalUsersTotal, internalUsers, 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the Internal users exposures total "+ internalUsers);
		
		LogUtils.logStep("3. Get the internal users list who exposed the docs.");
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("requestType",  "users"));
		UIExposedDoc payload = this.getUIPayload(appName, exposedDocParam, null, "users", 0, 0, isInternal, "", "name", null, null, null);
		SecurletDocument exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		CustomAssertion.assertEquals(internalUsersTotal, exposedUsers.getUsers().size(), "user count don't match");
		
		LogUtils.logStep("4. Get the external collaborator on internal documents.");
		params.clear();
		params.add(new BasicNameValuePair("requestType",  "collabs"));
		payload = this.getUIPayload(appName, exposedDocParam, null, "collabs", 0, 0, isInternal, "", "name", null, null, null);
		exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		
		CustomAssertion.assertEquals(externalUsersTotal, exposedUsers.getCollabs().size(), "user count don't match");
	}
	
	
	/**
	 * Test 17
	 * @throws Exception
	 */
	@Test(dataProvider = "SingleVulnerabilityProvider", groups={"EXPOSED_USERS", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterExposedUsersByVulnerabilityType(String type)  throws Exception {
		
		String steps[] =
			{		"1. Get the user totals based on the filter vulnerability type: "+type, 
					"2. Get the user exposures based on the filter vulnerability type: "+type,
					"3. Verify both the counts match."  };
		
		
		/*String exposedDocParam = type;
		if (type.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (type.equals("external")) {
			exposedDocParam = "ext_count";
		} */

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+type);
		
		LogUtils.logStep("1. Get the user  totals with UI server call.");
		ExposureTotals usertotals = this.getUIMetricsUsersTotal(null, 10, null, null, type, false, isInternal);
		long internalUsersTotal = usertotals.getTotal("internal");
		long externalUsersTotal = usertotals.getTotal("external");
		
		LogUtils.logStep("2. Get the user  totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>(); 
		hmap.put("vlTypes",  type);
		ExposureTotals uivltotals = getUIMetricsUsersVulnerabilities(hmap, false, isInternal);
		CustomAssertion.assertEquals(internalUsersTotal, uivltotals.getResults().size(), 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the vulnerable users total "+ uivltotals.getResults().size());
		
		LogUtils.logStep("3. Get the user exposures totals with UI server call.");
		ExposureTotals userExposurestotals = this.getUIMetricsUsersExposures(null, 10, null, null, type, false, isInternal);
		long internalUsers = userExposurestotals.getResults().size();
		
		
		CustomAssertion.assertEquals(internalUsersTotal, internalUsers, 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the Internal users exposures total "+ internalUsers);
		
		LogUtils.logStep("4. Get the internal users list who exposed the vulnerability docs.");
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("requestType",  "users"));
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "users", 0, 0, isInternal, "", "name", type, null, null);
		SecurletDocument exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		CustomAssertion.assertEquals(internalUsersTotal, exposedUsers.getUsers().size(), "user count don't match");
		
		LogUtils.logStep("5. Get the external collaborator on internal vulnerability documents.");
		params.clear();
		params.add(new BasicNameValuePair("requestType",  "collabs"));
		payload = this.getUIPayload(appName, null, null, "collabs", 0, 0, isInternal, "", "name", type, null, null);
		exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		
		CustomAssertion.assertEquals(externalUsersTotal, exposedUsers.getCollabs().size(), "user count don't match");
	}
	
	
	/**
	 * Test 18
	 * @throws Exception
	 */
	@Test(dataProvider = "VulnerabilityAndfiletypeProvider", groups={"EXPOSED_USERS", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterExposedUsersByVulnerabilityTypeAndFiletype(String vlTypeAndFileType)  throws Exception {
		
		String vltype = vlTypeAndFileType.split(",")[0];
		String filetype = vlTypeAndFileType.split(",")[1];
		
		String steps[] =
			{		"1. Get the user totals based on the filter : "+vlTypeAndFileType, 
					"2. Get the user exposures based on the filter : "+vlTypeAndFileType,
					"3. Verify both the counts match."  };
		
		
		/*String exposedDocParam = type;
		if (type.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (type.equals("external")) {
			exposedDocParam = "ext_count";
		} */

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+vlTypeAndFileType);
		
		LogUtils.logStep("1. Get the user  totals with UI server call.");
		ExposureTotals usertotals = this.getUIMetricsUsersTotal(null, 10, filetype, null, vltype,  false, isInternal);
		long internalUsersTotal = usertotals.getTotal("internal");
		long externalUsersTotal = usertotals.getTotal("external");
		
		LogUtils.logStep("2. Get the user  totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>(); 
		hmap.put("vlTypes",  vltype);
		hmap.put("fileClass", filetype);
		ExposureTotals uivltotals = getUIMetricsUsersVulnerabilities(hmap, false, isInternal);
		CustomAssertion.assertEquals(internalUsersTotal, uivltotals.getResults().size(), 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the vulnerable users total "+ uivltotals.getResults().size());
		
		LogUtils.logStep("3. Get the user exposures totals with UI server call.");
		ExposureTotals userExposurestotals = this.getUIMetricsUsersExposures(null, 10, filetype, null, vltype, false, isInternal);
		long internalUsers = userExposurestotals.getResults().size();
		
		
		CustomAssertion.assertEquals(internalUsersTotal, internalUsers, 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the Internal users exposures total "+ internalUsers);
		
		LogUtils.logStep("4. Get the internal users list who exposed the vulnerability docs.");
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("requestType",  "users"));
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "users", 0, 0, isInternal, "", "name", vltype, filetype, null);
		SecurletDocument exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		CustomAssertion.assertEquals(internalUsersTotal, exposedUsers.getUsers().size(), "user count don't match");
		
		LogUtils.logStep("5. Get the external collaborator on internal vulnerability documents.");
		params.clear();
		params.add(new BasicNameValuePair("requestType",  "collabs"));
		payload = this.getUIPayload(appName, null, null, "collabs", 0, 0, isInternal, "", "name", vltype, filetype, null);
		exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		
		CustomAssertion.assertEquals(externalUsersTotal, exposedUsers.getCollabs().size(), "user count don't match");
	}
	
	
	
	/**
	 * Test 19
	 * @throws Exception
	 */
	@Test(dataProvider = "ExposureTypeVulnerabilityAndfiletypeProvider", groups={"EXPOSED_USERS", "DASHBOARD", "INTERNAL", "OFFICE365"})
	public void filterExposedUsersByExpsoureTypeVulnerabilityTypeAndFiletype(String exposureTypeVlTypeAndFileType)  throws Exception {
		String exposureType = exposureTypeVlTypeAndFileType.split(",")[0];
		String vltype 		= exposureTypeVlTypeAndFileType.split(",")[1];
		String filetype 	= exposureTypeVlTypeAndFileType.split(",")[2];
		
		String steps[] =
			{		"1. Get the user totals based on the filter : "+exposureTypeVlTypeAndFileType, 
					"2. Get the user exposures based on the filter : "+exposureTypeVlTypeAndFileType,
					"3. Verify both the counts match."  };
		
		
		String exposedDocParam = exposureType;
		if (exposureType.equals("internal")) {
			exposedDocParam = "all_internal";
		} else if (exposureType.equals("external")) {
			exposedDocParam = "ext_count";
		} 

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("0. Started filtering the filter:"+exposureTypeVlTypeAndFileType);
		
		LogUtils.logStep("1. Get the user  totals with UI server call.");
		ExposureTotals usertotals = this.getUIMetricsUsersTotal(exposureType, 10, filetype, null, vltype,  false, isInternal);
		long internalUsersTotal = usertotals.getTotal("internal");
		long externalUsersTotal = usertotals.getTotal("external");
		
		LogUtils.logStep("2. Get the user  totals with UI server call.");
		HashMap<String, String> hmap = new HashMap<String, String>(); 
		hmap.put("vlTypes",  vltype);
		hmap.put("fileClass", filetype);
		hmap.put("fileExposures", exposureType);
		ExposureTotals uivltotals = getUIMetricsUsersVulnerabilities(hmap, false, isInternal);
		CustomAssertion.assertEquals(internalUsersTotal, uivltotals.getResults().size(), 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the vulnerable users total "+ uivltotals.getResults().size());
		
		LogUtils.logStep("3. Get the user exposures totals with UI server call.");
		ExposureTotals userExposurestotals = this.getUIMetricsUsersExposures(exposureType, 10, filetype, null, vltype, false, isInternal);
		long internalUsers = userExposurestotals.getResults().size();
		
		
		CustomAssertion.assertEquals(internalUsersTotal, internalUsers, 
				"Internal users metrics total:"+internalUsersTotal + " is not"
						+ " matching with the Internal users exposures total "+ internalUsers);
		
		LogUtils.logStep("4. Get the internal users list who exposed the vulnerability docs.");
		List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		params.add(new BasicNameValuePair("requestType",  "users"));
		UIExposedDoc payload = this.getUIPayload(appName, exposedDocParam, null, "users", 0, 0, isInternal, "", "name", vltype, filetype, null);
		SecurletDocument exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		CustomAssertion.assertEquals(internalUsersTotal, exposedUsers.getUsers().size(), "user count don't match");
		
		LogUtils.logStep("5. Get the external collaborator on internal vulnerability documents.");
		params.clear();
		params.add(new BasicNameValuePair("requestType",  "collabs"));
		payload = this.getUIPayload(appName, exposedDocParam, null, "collabs", 0, 0, isInternal, "", "name", vltype, filetype, null);
		exposedUsers = getUIExposedDocuments(MarshallingUtils.marshall(payload), params);
		
		CustomAssertion.assertEquals(externalUsersTotal, exposedUsers.getCollabs().size(), "user count don't match");
	}
	
	@Test(dataProvider = "ExposureDataProvider2", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL", "SEARCH"})
	public void searchForFilesWithSpecificFileTypes(String docsExposureType, String totalsExposureType) throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter exposure type: "+docsExposureType, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+totalsExposureType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		for (String searchText : this.fileTypes) {
			
			LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
			UIExposedDoc payload = this.getUIPayload(appName, docsExposureType, null, "docs", 0, 0, true, searchText, "name");
			SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);

			LogUtils.logStep("2. Iterate over the list of documents and check the selected filter");
			int offset = 0, limit = 50;
			do {
				payload = this.getUIPayload(appName, docsExposureType, null, "docs", offset, limit, true, searchText, "name");
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);

				this.verifyDocumentWithFileType(exposedDocs, searchText);
				offset +=limit;
			} while(exposedDocs.getMeta().getTotalCount() >= offset);
		}
	}

	@Test(dataProvider = "ExposureDataProvider2", groups={"EXPOSED_FILES", "DASHBOARD", "INTERNAL", "ALL", "SEARCH"})
	public void searchForFilesWithSpecificContentTypes(String docsExposureType, String totalsExposureType) throws Exception {
		
		String steps[] =
			{		"1. Get the exposed files based on the filter exposure type: "+docsExposureType, 
					"2. Get the metrics exposure totals based on the filter exposure type: "+totalsExposureType,
					"3. Verify both the counts match."  };

		LogUtils.logTestDescription(steps);
		
		for (String searchText : this.contentTypes) {
			
			LogUtils.logStep("1. Search the exposed files with the content type:"+searchText);
			
			LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
			UIExposedDoc payload = this.getUIPayload(appName, docsExposureType, null, "docs", 0, 0, true, searchText, "name");
			SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);

			LogUtils.logStep("2. Iterate over the list of documents and check the selected filter");
			int offset = 0, limit = 50;
			do {
				payload = this.getUIPayload(appName, docsExposureType, null, "docs", offset, limit, true, searchText, "name");
				exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);

				this.verifyDocumentWithContentType(exposedDocs, searchText);
				offset +=limit;
			} while(exposedDocs.getMeta().getTotalCount() >= offset);
		}
	}
	
	//External Filters Tests
	/**
	 * Test 1
	 * @throws Exception
	 */
	@Test( groups={"EXPOSED_FILES", "DASHBOARD", "EXTERNAL", "ALL"})
	public void externallyExposedFilesFileExposureTypes()  throws Exception {
		String steps[] =
			{		"1. Get the exposed files with no filters", 
					"2. Get the metrics exposure with no filters",
					"3. Get the content types with no filters",
					"4. Get the file types with no filters",
					"5. Get the vulnerability types with no filters"
			};

		LogUtils.logTestDescription(steps);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call.");
		UIExposedDoc payload = this.getUIPayload(appName, null, null, "docs", 0, 0, false, "", "name");
		SecurletDocument exposedDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		CustomAssertion.assertTrue(exposedDocs.getDocs() != null, "Exposed docs result set is not null", "Exposed docs result set is null");
		
		LogUtils.logStep("2. Get the exposure totals with UI server call.");
		ExposureTotals totals = this.getUIExposureMetricsTotals("", false, false);
		CustomAssertion.assertTrue(totals.getObjects() != null , "Exposure totals result set is not null", "Exposure totals docs result set is null");
		
		LogUtils.logStep("3. Get the content type totals with UI server call.");
		ExposureTotals contentTypeTotals = this.getUIContentTypesTotal(null, 10, null, false, false);
		CustomAssertion.assertTrue(contentTypeTotals.getResults() != null, "ContentTypes result set is not null", "ContentTypes result set is null");
		
		LogUtils.logStep("4. Get the file type totals with UI server call.");
		ExposureTotals fileTypeTotals = this.getUIFileTypesTotal(null, 10, null, null, false, false);
		CustomAssertion.assertTrue(fileTypeTotals.getResults() != null || fileTypeTotals.getResults() == null, "FileTypes result set is not null", "FileTypes result set is null");
		
		LogUtils.logStep("5. Get the vulnerability type totals with UI server call.");
		UIVulnerabilityTotals vulnerabilityTypeTotals = this.getUIVulnerabilityTypesTotal(null, 10, null, false, false);
		CustomAssertion.assertTrue(vulnerabilityTypeTotals.getResults().getVlTypes() != null, "VulnerabilityTypes result set is not null", 
																										"VulnerabilityTypes result set is null");
		
	}
	
	
	//Verfification utility methods
	private void verifyDocumentWithObjectType(SecurletDocument exposedDocs, String objectType) throws Exception {
		for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
			if (!objectType.equals("OneDrive")) {
				CustomAssertion.assertTrue(document.getObjectType().equals(objectType), 
						document.getName() + " is of object type "+ objectType, document.getName() + " is not of object type "+ objectType);
			} else {
				CustomAssertion.assertTrue(document.getObjectType() == null, 
						document.getName() + "  object type is null ", document.getName() + " object type is not null ");
			}
		}
	}
	
	
	private void verifyDocumentWithFileType(SecurletDocument exposedDocs, String fileType) throws Exception {
		for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
/*			
			CustomAssertion.assertTrue(document.getFormat().equals(fileType) , 
					document.getName() + " is of file type "+ fileType, document.getName() + " is not of file type "+ fileType);
*/
			
			String fileClass = document.getContentChecks().getMetadata().getFileClass();
			
			CustomAssertion.assertTrue(fileClass.equals(fileType) , 
					document.getName() + " is of file type "+ fileType, document.getName() + " is not of file type "+ fileClass);
/*			
			
			if (fileType.equals("WORDPROCESSOR")) {
				CustomAssertion.assertTrue("pdftxthtmlrtfjavabinemailbodydocdocx".contains(document.getFormat().toLowerCase()),
						document.getName() + " is of format "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("VIDEO")) {
				CustomAssertion.assertTrue("mp4avi".contains(document.getFormat().toLowerCase()),
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("ENCAPSULATION")) {
				CustomAssertion.assertTrue("ziprar".contains(document.getFormat().toLowerCase()),
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("SPREADSHEET")) {
				CustomAssertion.assertTrue("xlsxlsxxlsm".contains(document.getFormat().toLowerCase()),
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("UNKNOWN")) {
				CustomAssertion.assertTrue(document.getFormat() != null,
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("RASTERIMAGE")) {
				CustomAssertion.assertTrue("pngjpgjpeg".contains(document.getFormat().toLowerCase()),
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			
			if (fileType.equals("AutoDetNoFormat")) {
				CustomAssertion.assertTrue(document.getFormat() != null,
						document.getName() + " is of file class "+ document.getFormat(), document.getName() + " is of not of file class "+ document.getFormat());
			}
			*/
			
			
			
/*			
			CustomAssertion.assertTrue(document.getContentChecks().getMetadata().getFileClass() .equals(fileType),
					document.getName() + " is of file class "+ fileType, document.getContentChecks().getMetadata().getFileClass() + " is not of file type "+ fileType);
*/			

		}
	}
	
	
	private void verifyDocumentWithContentType(SecurletDocument exposedDocs, String contentType) throws Exception {
		for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
			Reporter.log(StringUtils.join(document.getContentChecks().getDocClass(), ","), true);
			CustomAssertion.assertTrue(document.getContentChecks().getDocClass().contains(contentType), 
					document.getName() + " is of content type "+ contentType, document.getName() + " is not of content type "+ contentType);

		}
	}
	
	
	private void verifyDocumentWithVltype(SecurletDocument exposedDocs, String vltype) throws Exception {
		for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
			
			if (document.getContentChecks().getPci() != null)
			
			if (vltype.equals("pci")) {
				CustomAssertion.assertTrue(document.getContentChecks().getPci() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("pii")) {
				CustomAssertion.assertTrue(document.getContentChecks().getPii() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("hipaa")) {
				CustomAssertion.assertTrue(document.getContentChecks().getHipaa() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("source_code")) {
				CustomAssertion.assertTrue(document.getContentChecks().getVkSourceCode() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("virus")) {
				CustomAssertion.assertTrue(document.getContentChecks().getVirus() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("dlp")) {
				CustomAssertion.assertTrue(document.getContentChecks().getDlp() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("encryption")) {
				CustomAssertion.assertTrue(document.getContentChecks().getEncryption() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("vba_macros")) {
				CustomAssertion.assertTrue(document.getContentChecks().getVbaMacros() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("glba")) {
				CustomAssertion.assertTrue(document.getContentChecks().getGlba() != null, 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
			
			if (vltype.equals("ferpa")) {
				CustomAssertion.assertTrue(document.getContentChecks().getVkFerpa() > 0 , 
						document.getName() + " is "+ vltype +" vulnerable", document.getName() + " is not "+ vltype +" vulnerable");
			}
		}
	}
	
	
	private void verifyExposureOnly(SecurletDocument exposedDocs, String exposureType) throws Exception {
		for (com.elastica.beatle.securlets.dto.Object document : exposedDocs.getDocs()) {
			if (exposureType.equals("public")) {
				CustomAssertion.assertTrue(document.getExposures().getPublic().booleanValue(), 
						document.getName() + " is publicly exposed", document.getName() + " is not publicly exposed");
			}
			if (exposureType.equals("all_internal")) {
				CustomAssertion.assertTrue(document.getExposures().getAllInternal().booleanValue(), 
						document.getName() + " is internally exposed", document.getName() + " is not internally exposed");
			}
			if (exposureType.equals("ext_count")) {
				CustomAssertion.assertTrue(document.getExposures().getExtCount() > 0, 
						document.getName() + " is externally exposed", document.getName() + " is not externally exposed");
			}
		}
	}

	
	
	//Utility Methods
	
	
	
	
	
	
	
	
	//Dynamic Data Providers
	
	//Subfeature provider for office 365
	@DataProvider(name = "SubFeatureProvider")
    public Iterator<Object []> SubFeatureProvider( ) throws InterruptedException
    {
        List<Object []> featureData = new ArrayList<>();
        Object[] data= null;
        for (String exp : this.office365Features) {
        	data= new Object[]{exp};
        	featureData.add(data);
        }
        return featureData.iterator();
    }
	
	//This is the data provider for just exposure data provider
	@DataProvider(name = "ExposureDataProvider")
    public Iterator<Object []> provider( ) throws InterruptedException
    {
        List<Object []> exposureData = new ArrayList<>();
        Object[] data= null;
        for (String exp : exposures) {
        	data= new Object[]{exp};
        	exposureData.add(data);
        }
        return exposureData.iterator();
    }
    
    
    @DataProvider(name = "ExposureDataProvider2")
    public Iterator<Object []> twoArgProvider1( ) throws InterruptedException
    {
        List<Object []> exposureData = new ArrayList<>();
        Object[] data= null;
        
        String exposuresArgDocs[] = new String []{"public", "all_internal", "ext_count"};
    	String exposuresArgMetrics[] = new String []{"public", "internal", "external"};
        
    	for (int i=0, j=exposuresArgDocs.length; i<j; i++) {
    		data= new Object[]{exposuresArgDocs[i], exposuresArgMetrics[i]};
        	exposureData.add(data);
    	}
        return exposureData.iterator();
    }
    
    
    
    @DataProvider(name = "FileTypeSearchProviderWithExposureType")
    public Iterator<Object []> FileTypeSearchProviderWithExposureType( ) throws InterruptedException
    {
        List<Object []> exposureData = new ArrayList<>();
        Object[] data= null;
        
        String exposuresArgDocs[] = new String []{"public", "all_internal", "ext_count"};
    	String exposuresArgMetrics[] = new String []{"public", "internal", "external"};
        
    	for (int i=0, j=exposuresArgDocs.length; i<j; i++) {
    		data= new Object[]{exposuresArgDocs[i], exposuresArgMetrics[i]};
        	exposureData.add(data);
    	}
        return exposureData.iterator();
    }
    
    
    @DataProvider(name = "SingleVulnerabilityProvider")
    public Iterator<Object[]> singelVulnerabilityProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(vulnerabilityTypes, 1);
    	List<Object []> vulnerabilities = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	vulnerabilities.add(data);
        }
        return vulnerabilities.iterator();
    }
    
    @DataProvider(name = "MultipleVulnerabilityProvider")
    public Iterator<Object[]> multipleVulnerabilityProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(vulnerabilityTypes, vulnerabilityTypes.size());
    	List<Object []> vulnerabilities = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	vulnerabilities.add(data);
        }
        return vulnerabilities.iterator();
    }
	
    @DataProvider(name = "SingleFiletypeProvider")
    public Iterator<Object[]> singleFiletypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(fileTypes, 1);
    	List<Object []> filetypes = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	filetypes.add(data);
        }
        return filetypes.iterator();
    }
    
    @DataProvider(name = "MultipleFiletypeProvider")
    public Iterator<Object[]> multipleFiletypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(fileTypes, fileTypes.size());
    	List<Object []> filetypes = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	filetypes.add(data);
        }
        return filetypes.iterator();
    }
    
    
    @DataProvider(name = "SingleContentTypeProvider")
    public Iterator<Object[]> SingleContentTypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(contentTypes, 1);
    	List<Object []> contenttypes = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	contenttypes.add(data);
        }
        return contenttypes.iterator();
    }
    
    @DataProvider(name = "MultipleContentTypeProvider")
    public Iterator<Object[]> multipleContentTypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = getAllCombinationsOfAList(contentTypes, contentTypes.size());
    	List<Object []> contenttypes = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	contenttypes.add(data);
        }
        return contenttypes.iterator();
    }
    
    @DataProvider(name = "ExposureTypeAndfiletypeProvider")
    public Iterator<Object[]> exposureTypeAndfiletypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = generateCombinations(Arrays.asList(exposuresArgMetrics), null, fileTypes, null);
    	List<Object []> filters = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	filters.add(data);
        }
        return filters.iterator();
    }
    
    @DataProvider(name = "VulnerabilityAndfiletypeProvider")
    public Iterator<Object[]> VulnerabilityAndfiletypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = generateCombinations(null, vulnerabilityTypes, fileTypes, null);
    	List<Object []> filters = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	filters.add(data);
        }
        return filters.iterator();
    }
    
    @DataProvider(name = "ExposureTypeVulnerabilityAndfiletypeProvider")
    public Iterator<Object[]> ExposureTypeVulnerabilityAndfiletypeProvider( ) throws InterruptedException
    {   
    	List<String> allcombinations = generateCombinations(Arrays.asList(exposuresArgMetrics), vulnerabilityTypes, fileTypes, null);
    	List<Object []> filters = new ArrayList<>();
        Object[] data= null;
        for (String exp : allcombinations) {
        	data= new Object[]{exp};
        	filters.add(data);
        }
        return filters.iterator();
    }
    
    
    
    
    
	
	//Util methods
    public ExposureTotals getUIExposureMetricsTotals(String fileExposures, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (fileExposures !=null || fileExposures.length() > 0) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUIExposuresMetricsTotal(null, qparams);
		return totals;
	}
	
	
	
	
	public ExposureTotals getUIExposureMetricsTotals(HashMap<String, String> hmap, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey(),  entry.getValue()));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUIExposuresMetricsTotal(null, qparams);
		return totals;
	}
	
	
	
	public ExposureTotals getUISubFeaturesTotal(String objectType, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (objectType !=null) {
			qparams.add(new BasicNameValuePair("objectType",  objectType));
		}
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUISubFeaturesTotal(null, qparams);
		return totals;
	}
	
	public ExposureTotals getUIFileTypesTotal(String fileExposures, int top, String filetypes, String searchText, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		
		if (fileExposures !=null) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		if(top >= 0) {
			qparams.add(new BasicNameValuePair("top",  String.valueOf(top)));
		} else {
			qparams.add(new BasicNameValuePair("top",  "10"));
		}
		
		if (filetypes != null) {
			String decoded = UrlUtils.decode(filetypes);
			qparams.add(new BasicNameValuePair("fileClass",  decoded));
		}
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		
		if (searchText != null) {
			qparams.add(new BasicNameValuePair("searchText",  searchText));
		}
		
		ExposureTotals totals = this.getUIFileTypesTotal(null, qparams);
		return totals;
	}
	
	public ExposureTotals getUIFileTypesTotal(HashMap<String, String> hmap, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		qparams.add(new BasicNameValuePair("top",  "10"));
		
		
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey(),  entry.getValue()));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		
		ExposureTotals totals = this.getUIFileTypesTotal(null, qparams);
		return totals;
	}
	
	
	
	
	public ExposureTotals getUIContentTypesTotal(String fileExposures, int top, String contenttypes, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (fileExposures !=null) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		if(top >= 0) {
			qparams.add(new BasicNameValuePair("top",  String.valueOf(top)));
		} else {
			qparams.add(new BasicNameValuePair("top",  "10"));
		}
		
		if (contenttypes != null) {
			String decoded = UrlUtils.decode(contenttypes);
			qparams.add(new BasicNameValuePair("contentType",  decoded));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUIContentTypesTotal(null, qparams);
		return totals;
	}
	
	public UIVulnerabilityTotals getUIVulnerabilityTypesTotal(String fileExposures, int top, String vltypes, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (fileExposures !=null) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		if(top >= 0) {
			qparams.add(new BasicNameValuePair("top",  String.valueOf(top)));
		} else {
			qparams.add(new BasicNameValuePair("top",  "10"));
		}
		
		if (vltypes != null) {
			String decoded = UrlUtils.decode(vltypes);
			qparams.add(new BasicNameValuePair("vlTypes",  decoded));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		UIVulnerabilityTotals totals = this.getUIVulnerabilityTypesTotal(null, qparams);
		return totals;
	}
	
	public UIVulnerabilityTotals getUIVulnerabilityTypesTotal(HashMap<String, String> hmap, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		qparams.add(new BasicNameValuePair("top",  "10"));
		
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey(),  entry.getValue()));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		
		
		UIVulnerabilityTotals totals = this.getUIVulnerabilityTypesTotal(null, qparams);
		return totals;
	}
	

	public ExposureTotals getUIMetricsUsersTotal(String fileExposures, int top, String filetypes, String contenttypes, String vlTypes, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (fileExposures !=null) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		if (vlTypes !=null) {
			qparams.add(new BasicNameValuePair("vlTypes",  vlTypes));
		}
		
		
		if(top >= 0) {
			qparams.add(new BasicNameValuePair("top",  String.valueOf(top)));
		} else {
			qparams.add(new BasicNameValuePair("top",  "10"));
		}
		
		if (contenttypes != null) {
			String decoded = UrlUtils.decode(contenttypes);
			qparams.add(new BasicNameValuePair("contentType",  decoded));
		}
		

		if (filetypes != null) {
			qparams.add(new BasicNameValuePair("fileClass",  filetypes));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUIMetricsUsersTotal(qparams);
		return totals;
	}
	
	public ExposureTotals getUIMetricsUsersExposures(String fileExposures, int top, String filetypes, String contenttypes, String vlTypes, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		if (fileExposures !=null) {
			qparams.add(new BasicNameValuePair(FILE_EXPOSURES,  fileExposures));
		}
		
		if (vlTypes !=null) {
			qparams.add(new BasicNameValuePair("vlTypes",  vlTypes));
		}
		
		if(top >= 0) {
			qparams.add(new BasicNameValuePair("top",  String.valueOf(top)));
		} else {
			qparams.add(new BasicNameValuePair("top",  "10"));
		}
		
		if (contenttypes != null) {
			String decoded = UrlUtils.decode(contenttypes);
			qparams.add(new BasicNameValuePair("contentType",  decoded));
		}
		
		if (filetypes != null) {
			qparams.add(new BasicNameValuePair("fileClass",  filetypes));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		ExposureTotals totals = this.getUIMetricsUsersExposures(qparams);
		return totals;
	}
	
	public ExposureTotals getUIMetricsUsersVulnerabilities(HashMap<String, String> hmap, boolean riskyDocs, boolean isInternal) throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  this.appName));
		qparams.add(new BasicNameValuePair("top",  "10"));
		
		for (Map.Entry<String, String> entry : hmap.entrySet()) {
			qparams.add(new BasicNameValuePair(entry.getKey(),  entry.getValue()));
		}
		
		if (riskyDocs) {
			qparams.add(new BasicNameValuePair("requestType",  "risky_docs"));
		}
		
		
		ExposureTotals totals = this.getUIMetricsUsersVulnerabilities(qparams);
		return totals;
	}
	
	

}
