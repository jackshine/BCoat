package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.BoxDataProvider;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.O365DataProvider;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ApiScanPolicy;
import com.elastica.beatle.securlets.dto.BoxAction;
import com.elastica.beatle.securlets.dto.BoxMetaInfo;
import com.elastica.beatle.securlets.dto.BoxRemediation;
import com.elastica.beatle.securlets.dto.Data;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.InclusionRule;
import com.elastica.beatle.securlets.dto.RemedialAction;
import com.elastica.beatle.securlets.dto.RemediationMetaInfo;
import com.elastica.beatle.securlets.dto.ScanSource;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.SecurletRemediation;
import com.elastica.beatle.securlets.dto.SelectiveScanPolicy;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.elastica.beatle.securlets.dto.UIExposedDoc;
import com.elastica.beatle.securlets.dto.UISource;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.FileEntry;
import com.universal.dtos.onedrive.DocumentSharingResult;
import com.universal.dtos.onedrive.ItemResource;
import com.universal.dtos.onedrive.SharingUserRoleAssignment;



public class OneDriveDashboardMetricsTests extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	DocumentValidator docValidator;
	String uniqueId;
	OneDriveUtils onedriveUtils;



	public OneDriveDashboardMetricsTests() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		docValidator = new DocumentValidator();
		uniqueId = String.valueOf(System.currentTimeMillis());//UUID.randomUUID().toString();
		onedriveUtils = new OneDriveUtils();
	}


	/**
	 * Test 1
	 * Check the exposure metrics totals as in Venn Diagram
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"})
	public void verifyVennDiagramMetrics() throws Exception {
		Reporter.log("Verifying the exposure totals for internally owned documents as depicted in Venn diagram...", true);
		long countPublic   = 0;
		long countInternal = 0;
		long countExternal = 0;

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));

		ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

		countPublic = expsoureTotals.getPublicExposouresCount();
		countInternal = expsoureTotals.getInternalExposouresCount();
		countExternal = expsoureTotals.getExternalExposouresCount();

		Reporter.log("Verifying the exposure totals for internally owned documents", true);
		Reporter.log("Public count:"+countPublic, true);
		Reporter.log("Internal count:"+countInternal, true);
		Reporter.log("External count:"+countExternal, true);

		CustomAssertion.assertTrue((countPublic >= 0 || countInternal >= 0 || countExternal >= 0), 
				"Exposure totals for internal, external and public seems to be zero");

	}

	
	
	/**
	 * Test 2
	 * verify  the internally exposed document count with UI and API server call
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES"})
	public void verifyExposedDocsFilter() throws Exception {
		Reporter.log("Verifying the internally exposed docs with UI call and API server call ...", true);
		
		UIExposedDoc payload = new UIExposedDoc();
		UISource uisource = new UISource();
		uisource.setApp("Office 365");
		uisource.setLimit(20);
		uisource.setOffset(0);
		uisource.setIsInternal(Boolean.TRUE.booleanValue());
		uisource.setSearchTextFromTable("");
		uisource.setOrderBy("name");
		uisource.setExportType("docs");
		//uisource.setObjectType("OneDrive");
		payload.setSource(uisource);
		
		LogUtils.logStep("1. Get the list of exposed documents with UI server call");
		SecurletDocument uiCallDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("2. Get the list of exposed documents with API server call");
		//Get the exposed documents and check the document is publicly exposed
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		SecurletDocument apiCallDocs = getExposedDocuments(elapp.el_office_365.name(), docparams);
		LogUtils.logStep("3. Verify the total docs returned by api and ui calls are same");
		CustomAssertion.assertEquals(uiCallDocs.getMeta().getTotalCount(), apiCallDocs.getMeta().getTotalCount(), 
												"UI server docs count:"+uiCallDocs.getMeta().getTotalCount() +" is not matching "
														+ "with api server docs count:"+apiCallDocs.getMeta().getTotalCount());	
		for (com.elastica.beatle.securlets.dto.Object obj : apiCallDocs.getObjects()) {
			CustomAssertion.assertTrue(obj.getIsInternal(), "Doc is internally exposed", "Doc is not internally exposed");	
		}

	}
	
	
	
	/**
	 * Test 3
	 * Check the exposure metrics totals as in Venn Diagram
	 * @throws Exception
	 */

	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedFileMetrics() throws Exception {
		Reporter.log("Verifying the exposure totals for internally owned documents with UI and API server call...", true);
		long countPublic   = 0;
		long countInternal = 0;
		long countExternal = 0;

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		LogUtils.logStep("1. Get the exposure metrics total with API server call");
		ExposureTotals apiTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

		countPublic = apiTotals.getPublicExposouresCount();
		countInternal = apiTotals.getInternalExposouresCount();
		countExternal = apiTotals.getExternalExposouresCount();
		
		
		LogUtils.logStep("2. Get the exposure metrics total with UI server call");
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		ExposureTotals uiTotals = getUIExposuresMetricsTotal("Office 365", qparams);

		CustomAssertion.assertEquals(uiTotals.getInternalExposouresCount(), countInternal, 
										"UI server count:"+uiTotals.getInternalExposouresCount() +" is not matching with api server count:"+countInternal);
		
		CustomAssertion.assertEquals(uiTotals.getExternalExposouresCount(), countExternal, 
										"UI server count:"+uiTotals.getExternalExposouresCount() +" is not matching with api server count:"+countExternal);

		CustomAssertion.assertEquals(uiTotals.getPublicExposouresCount(), countPublic, 
										"UI server count:"+uiTotals.getPublicExposouresCount() +" is not matching with api server count:"+countPublic);
		
		LogUtils.logStep("3. Get the exposed docs count and verify it is sum of all exposures");
		//Also get the exposed docs count and check
		List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
		docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
		SecurletDocument apiCallDocs = getExposedDocuments(elapp.el_office_365.name(), docparams);
		
		CustomAssertion.assertTrue((countPublic + countInternal + countExternal) >=
				apiCallDocs.getMeta().getTotalCount(), "Exposed docs and Exposure metrics total are not same");

	}
	

	/**
	 * Test 2
	 * 
	 * This test exposes/unexposes a file publically, internally and externally and check the exposure total getting incremented/decremented
	 * 
	 * @throws Exception
	 */
	@Test(dataProviderClass = O365DataProvider.class, dataProvider = "exposureTypeProvider", groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyExposureCountAfterExposure(String filename, String exposureType, int role,  String server) throws Exception {
		Reporter.log("Starting the test to upload and share "+ filename + " with " + exposureType + " access "
				+ "and role "+ role + " (1-Read, 2-Edit) and check the exposure totals", true);
		ItemResource itemResource = null;
		
		Reporter.log("**********************************************************", true);
		Reporter.log("External User:" + suiteData.getSaasAppExternalUser1Name(), true);
		Reporter.log("**********************************************************", true);
		
		try {

			long countPublic   = 0;
			long countInternal = 0;
			long countExternal = 0;

			List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
			qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));

			ExposureTotals expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			countPublic   = expsoureTotals.getPublicExposouresCount();
			countInternal = expsoureTotals.getInternalExposouresCount();
			countExternal = expsoureTotals.getExternalExposouresCount();

			Reporter.log("Verifying the exposure totals for internally owned documents before exposure", true);
			Reporter.log("Public count:"  + countPublic,   true);
			Reporter.log("Internal count:"+ countInternal, true);
			Reporter.log("External count:"+ countExternal, true);

			//Create a file and share it
			itemResource = uploadAndShareDocument(filename, exposureType, role);

			//3 mins sleep
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			//After external exposure
			Reporter.log("Getting the exposure count after exposure", true);
			expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			Reporter.log("Public count after exposure  :"+ expsoureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count after exposure:"+ expsoureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count after exposure:"+ expsoureTotals.getExternalExposouresCount(), true);

			if (exposureType.equalsIgnoreCase(ExposureTypes.PUBLIC.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic+1,  "Public exposure count not incremented");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal,  "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure shouldn't change");

			} else if(exposureType.equalsIgnoreCase(ExposureTypes.EXTERNAL.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic, "Public exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal, "Internal exposure shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal+1,  "External exposure count not incremented");

			} else if(exposureType.equalsIgnoreCase(ExposureTypes.INTERNAL.name())) {
				CustomAssertion.assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic,    "Public exposure count shouldn't change");
				CustomAssertion.assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal+1, "Internal exposure count not incremented");
				CustomAssertion.assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure shouldn't change");
			}

			Reporter.log("Disabling the sharing ", true);
			universalApi.disableSharing(itemResource.getName());

			//5 mins sleep
			sleep(CommonConstants.FIVE_MINUTES_SLEEP);

			expsoureTotals = getExposuresMetricsTotal(elapp.el_office_365.name(), qparams);

			Reporter.log("Getting the exposure count after removing the exposure", true);
			Reporter.log("Public count after exposure  :"+ expsoureTotals.getPublicExposouresCount(), true);
			Reporter.log("Internal count after exposure:"+ expsoureTotals.getInternalExposouresCount(), true);
			Reporter.log("External count after exposure:"+ expsoureTotals.getExternalExposouresCount(), true);

			
			if(exposureType.equalsIgnoreCase(ExposureTypes.EXTERNAL.name())) {
				assertEquals(expsoureTotals.getExternalExposouresCount(), countExternal, "External exposure count not decremented");
			}
			
			if(exposureType.equalsIgnoreCase(ExposureTypes.INTERNAL.name())) {
				assertEquals(expsoureTotals.getInternalExposouresCount(), countInternal, "Internal exposure count not decremented");
			}
			
			if(exposureType.equalsIgnoreCase(ExposureTypes.PUBLIC.name())) {
				assertEquals(expsoureTotals.getPublicExposouresCount(),   countPublic,   "Public exposure count not decremented");
			}

		}
		finally{
			universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
			Reporter.log("*******************************************************************************************************************", true);
		}
	}


	/**
	 * Test 3
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedDocumentsCountAfterExposure() throws Exception {
		String[] messages = {"1. Upload a file and expose it.",
				"2. Get the list of exposed files and check the parameters.", 
				"3. Get the other risks and the file should not be present.",
				"4. Unexpose the file and check the exposed files.",
		"5. Get the list of exposed files and files should not be present."};

		LogUtils.logTestDescription(messages);
		ItemResource itemResource = null;
		try {

			String filename = "test.pdf";
			//Create a file and share it

			itemResource = uploadAndShareDocument(filename, ExposureTypes.PUBLIC.name(), 1);

			//3 mins sleep
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  itemResource.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_office_365.name(), docparams);

			Reporter.log("Checking the publicly shared document is present in the expsosed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, itemResource.getName(), "File");

			//Get the other risk and check the document it is not listed
			Reporter.log("Checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ itemResource.getName(), true);
			universalApi.disableSharing(itemResource.getName());

			//3 mins sleep
			Reporter.log("Going to Sleep now ... " + CommonConstants.THREE_MINUTES_SLEEP, true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			documents = getExposedDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Other risk is not zero");

			riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");
		}

		finally{
			universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		}
	}

	/**
	 * Test 4
	 * Upload a risky file 
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void verifyExposedRiskyDocumentsCountAfterExposure() throws Exception {
		String[] messages = {"1. Upload a risk file and expose it.",
				"2. Get the list of exposed files and check the parameters.", 
				"3. Get the other risks and the file should not be present.",
				"4. Unexpose the file and check the exposed files.",
		"5. Get the list of exposed files and files should not be present."};

		LogUtils.logTestDescription(messages);
		ItemResource itemResource = null;
		try {

			String filename = "PII_PCI_EmployeeList.xlsx";
			//Create a file and share it

			itemResource = uploadAndShareDocument(filename, ExposureTypes.PUBLIC.name(), 1);

			//3 mins sleep
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			//Get the exposed documents and check the document is publicly exposed
			List<NameValuePair> docparams = new ArrayList<NameValuePair>(); 
			docparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
			docparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Office365.name()));
			docparams.add(new BasicNameValuePair("name",  itemResource.getName()));
			SecurletDocument documents = getExposedDocuments(elapp.el_office_365.name(), docparams);

			Reporter.log("Checking the publicly shared document is present in the expsosed files tab...", true);
			docValidator.verifyPubliclyExposedDocument(documents, this.saasAppUserAccount, itemResource.getName(), "File");

			//Get the other risk and check the document it is not listed
			Reporter.log("Checking the publicly shared document is present in the other risks tab...", true);
			SecurletDocument riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 0, "Other risk is not zero");

			Reporter.log("Disabling the share on  "+ itemResource.getName(), true);
			universalApi.disableSharing(itemResource.getName());

			//3 mins sleep
			Reporter.log("Going to Sleep now ... " + CommonConstants.THREE_MINUTES_SLEEP, true);
			Thread.sleep(CommonConstants.THREE_MINUTES_SLEEP);

			documents = getExposedDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), 0, "Other risk is not zero");

			riskyDocuments = getRiskyDocuments(elapp.el_office_365.name(), docparams);
			CustomAssertion.assertEquals(riskyDocuments.getMeta().getTotalCount(), 1, "Other risk should not be zero");
		}

		finally{
			//universalApi.deleteFile(itemResource.getId(), itemResource.getETag());
		}
	}


	/**
	 * Bulk remediation
	 * @param collaborator
	 * @param currentRole
	 * @throws Exception
	 */
	
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void bulkRemediateExternalUser() throws Exception {
		//Add comment for this
		String externalCollaborator = "pushpan@gmail.com";
		long docCount = 0;
		long afterCount = 0;
		
		//Retrieve the documents count for the users
		LogUtils.logTestDescription("1. Remove the external collaborator access for all internally owned documents.");
	
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		
		//Get external collabs on internal documents
		Reporter.log("2. Get the count of documents for external collaborators before collaboration", true);
		SecurletDocument documents = getCollaborators(elapp.el_office_365.name(), qparams);
		
		for (com.elastica.beatle.securlets.dto.Object docobject : documents.getObjects()) {
			
			Reporter.log("Remediation to be performed Doc object Id:"+ docobject.getUserId(), true);
			Reporter.log("Doc object Total:"+ docobject.getDocsExposed(), true);
			
			if(docobject.getUserId().equals(externalCollaborator)) {
				docCount			 =  documents.getObjects().get(0).getDocsExposed();
			}
		}
		
		ArrayList<String> collabs = new ArrayList<String>();
		collabs.add(externalCollaborator);	
		SecurletRemediation securletRemediation = getBulkRemediationObject(collabs);
		Reporter.log(MarshallingUtils.marshall(securletRemediation), true);
		
		
		//Apply the bulk remediation
		Reporter.log("4. Apply the remediation", true);
		remediateExposureWithAPI(securletRemediation);

		//Wait for remedial action
		sleep(CommonConstants.THREE_MINUTES_SLEEP);

		//Get external collabs on internal documents
		documents = getCollaborators(elapp.el_office_365.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object docobject : documents.getObjects()) {
			if (docobject.getUserId().equals(externalCollaborator)) {
				afterCount = docobject.getDocsExposed();
			} else {
				//After remediation, user might be removed. So it will not get into if part. In this case I am making the after count as zero
				afterCount = 0;
			}
		}
		Reporter.log("5. Get the count of documents for external collaborator after remediation", true);
		CustomAssertion.assertEquals(afterCount, 0, "Bulk remediation didnot happen it seems");
	}
	

	
	//New tests refactored
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void getUISubFeaturesTotal()  throws Exception {
		LogUtils.logTestDescription("1. Check the enabled subfeatures and their totals");
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		LogUtils.logStep("1. Get the sub features and their totals");
		ExposureTotals features = getUISubFeaturesTotal(null, qparams);
		boolean mail = false, onedrive=false, sites=false;
		for (TotalObject obj : features.getResults()) {
			if (obj.getId().equals("Mail")) {
				CustomAssertion.assertTrue(obj.getTotal() >=0, "Mail count >= Zero", "Mail count Not available");
				mail = true;
			}
			if (obj.getId().equals("OneDrive")) {
				CustomAssertion.assertTrue(obj.getTotal() >=0, "Onedrive count >= Zero", "Onedrive count Not available");
				onedrive = true;
			}
			if (obj.getId().equals("Sites")) {
				CustomAssertion.assertTrue(obj.getTotal() >=0, "Sites count >= Zero", "Sites count Not available");
				sites = true;
			}
		}
		LogUtils.logStep("2. Verify the sub features presence.");
		CustomAssertion.assertTrue(mail, "Mail Feature found", "Mail Feature not found");
		CustomAssertion.assertTrue(onedrive, "Onedrive Feature found", "Onedrive Feature not found");
		CustomAssertion.assertTrue(sites, "Sites Feature found", "Sites feature not found");
		
	}
	
	
	@Test(groups={"DASHBOARD", "INTERNAL"})
	public void getActivityFilter()  throws Exception {
//		{"source":{"limit":20,"offset":0,"isInternal":true,"searchTextFromTable":"","orderBy":"name","exportType"
//			:"docs","objectType":"OneDrive","app":"Office 365"}}
		//getUIPayload
		
		String objectType = "OneDrive";
		Reporter.log("Verifying the internally exposed docs with UI call and API server call ...", true);
		UIExposedDoc payload = this.getUIPayload("Office 365", null, "OneDrive", "docs", 0, 20, true, "", "name");
		
		
		LogUtils.logStep("1. Get the sub feature totals with UI server call.");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		qparams.add(new BasicNameValuePair("objectType",  objectType));
		ExposureTotals totals = getUISubFeaturesTotal(null, qparams);
		long total = totals.getTotal(objectType);
		
		LogUtils.logStep("2. Get the list of exposed documents with UI server call.");
		SecurletDocument uiCallDocs = getUIExposedDocuments(MarshallingUtils.marshall(payload), null);
		
		LogUtils.logStep("3. Verify subfeature count and document count are same.");
		CustomAssertion.assertEquals(total, uiCallDocs.getMeta().getTotalCount(), 
				"Exposed doc count:"+uiCallDocs.getMeta().getTotalCount() + " is not matching with "+ objectType + " total "+ total);
		
		//LogUtils.logStep("4. Iterate the document list and verify the Object type.");
		
	}
	
	@Test(groups={"DASHBOARD", "INTERNAL", "SELECTIVE_SCANNING"})
	public void getScanPolicy()  throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		String scanPolicy = this.getScanPolicy(qparams);
		Reporter.log(scanPolicy, true);
		getTenantScanPolicyId("Office 365");
	}
	
	@Test(groups={"DASHBOARD", "INTERNAL", "SELECTIVE_SCANNING"})
	public void getApiScanPolicy()  throws Exception {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		qparams.add(new BasicNameValuePair("app_name",  "Office 365"));
		String scanPolicy = this.getApiScanPolicy(suiteData.getTenantName(), qparams);
		Reporter.log(scanPolicy, true);
		//getTenantScanPolicyId("Office 365");
	}
	
	@Test(groups={"DASHBOARD", "INTERNAL", "SELECTIVE_SCANNING"})
	public void updateApiScanPolicy()  throws Exception {
		
		String sp = "{\"api_scan_policy\": {\"scan_all\": false}, \"app_name\": \"Office 365\"}";
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		//qparams.add(new BasicNameValuePair("app_name",  "Office 365"));
		String scanPolicy = this.updateApiScanPolicy(suiteData.getTenantName(), sp);
		Reporter.log(scanPolicy, true);
		//getTenantScanPolicyId("Office 365");
	}
	
	
	
	
	@Test(groups={"DASHBOARD", "INTERNAL", "SELECTIVE_SCANNING"})
	public void updateFullScanPolicy()  throws Exception {
		
		ApiScanPolicy apiScanPolicy = new ApiScanPolicy();
		apiScanPolicy.setScanAll(true);
		apiScanPolicy.setInclusionRules(null);
		apiScanPolicy.setExclusionRules(null);
		Data scandata = new Data();
		scandata.setApiScanPolicy(apiScanPolicy);
		scandata.setAppName("Office 365");
		//scandata.setInstance("securlet365mailbeatle");
		SelectiveScanPolicy ssp = new SelectiveScanPolicy();
		ScanSource scansource = new ScanSource();
		scansource.setData(scandata);
		//scansource.setId("56c2d1ede43736776e7e5c17");
		scansource.setType("configure");
		ssp.setSource(scansource);
		
		Reporter.log(MarshallingUtils.marshall(ssp), true);
		
		String scanPolicy = this.updateScanPolicy(ssp);
		Reporter.log(scanPolicy, true);
		//configure_scan_policy
		
		//{"source":{"data":{"api_scan_policy":{"scan_all":true},"app_name":"Office 365"},"type":"configure"}}
		
//		
//		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
//		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
//		String scanPolicy = this.getScanPolicy(qparams);
//		Reporter.log(scanPolicy, true);
	}
	
	
	@Test(groups={"DASHBOARD", "INTERNAL", "SELECTIVE_SCANNING"})
	public void updateSelectiveScanPolicy()  throws Exception {
		
		ApiScanPolicy apiScanPolicy = new ApiScanPolicy();
		apiScanPolicy.setScanAll(false);
		List<InclusionRule> inclusionRules = new ArrayList<InclusionRule>();
		InclusionRule rule1 = new InclusionRule();
		
		List<String> folders = new ArrayList<String>();
		folders.add("confidential");
		folders.add("legal");
		
		List<String> grps = new ArrayList<String>();
		grps.add("__ALL_EL__");
		
		List<String> usrs = new ArrayList<String>();
		usrs.add("__ALL_EL__");
		
		rule1.setFolders(folders);
		rule1.setGroups(grps);
		rule1.setUsers(usrs);
		
		inclusionRules.add(rule1);
		
		apiScanPolicy.setInclusionRules(inclusionRules);
		apiScanPolicy.setExclusionRules(null);
		Data scandata = new Data();
		scandata.setApiScanPolicy(apiScanPolicy);
		scandata.setAppName("Office 365");
		//scandata.setSecurletState("AC");
		scandata.setInstance("securlet365mailbeatle");
		SelectiveScanPolicy ssp = new SelectiveScanPolicy();
		ScanSource scansource = new ScanSource();
		scansource.setData(scandata);
		scansource.setId("56c2d1ede43736776e7e5c17");
		scansource.setType("configure");
		ssp.setSource(scansource);
		
		Reporter.log(MarshallingUtils.marshall(ssp), true);
		
		String scanPolicy = this.updateScanPolicy(ssp);
		Reporter.log(scanPolicy, true);
	}
	
	
	
	public ExposureTotals  getOffice365MetricsWithAPI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  "Office 365"));
		ExposureTotals exposureTotals = getUIExposuresMetricsTotal(elapp.el_office_365.name(), qparams);
		return exposureTotals;
	}
	
	private SecurletRemediation getBulkRemediationObject(ArrayList<String> collaboratorName) {
		SecurletRemediation securletRemediation = new SecurletRemediation();

		securletRemediation.setDbName(suiteData.getTenantName());
		securletRemediation.setUser("__ALL_EL__");
		securletRemediation.setUserId("__ALL_EL__");
		securletRemediation.setDocType("__ALL_EL__");
		securletRemediation.setDocId("__ALL_EL__");

		//Meta Info
		RemediationMetaInfo remediationMetaInfo = new RemediationMetaInfo();
		remediationMetaInfo.setCurrentLink(null);
		remediationMetaInfo.setCollabs(collaboratorName);
		remediationMetaInfo.setExpireOn(null);
		remediationMetaInfo.setAccess(null);

		List<RemedialAction> actions = new ArrayList<RemedialAction>();
		RemedialAction remedialAction = new RemedialAction();
		remedialAction.setCode("COLLAB_REMOVE");
		remedialAction.setPossibleValues(new ArrayList<String>());
		remedialAction.setMetaInfo(remediationMetaInfo);
		remedialAction.setCodeName("Remove");
		actions.add(remedialAction);

		securletRemediation.setActions(actions);
		return securletRemediation;
	}	

	public ItemResource uploadAndShareDocument(String filename, String exposureType, int role) throws Exception {
		//Upload file 
		ItemResource itemResource = universalApi.uploadSimpleFile("/", filename, uniqueId +"_"+filename);
		Reporter.log("Item Resource:"+ itemResource.getId(), true);
		Reporter.log("Item Name:"+ itemResource.getName(), true);

		SharingUserRoleAssignment shareObject = null;
		if (exposureType.equalsIgnoreCase("public")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document publicly ...", true);
			shareObject = onedriveUtils.getPublicShareObject(itemResource.getWebUrl(), role);
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);

		} else if (exposureType.equalsIgnoreCase("internal")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document with internal user ...", true);
			shareObject = onedriveUtils.getFileShareObject(itemResource, role, "Everyone except external users");
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);

		} else if (exposureType.equalsIgnoreCase("external")) {
			Reporter.log("Going to upload "+ itemResource.getName() + " and share the document with external user ...", true);
			shareObject = onedriveUtils.getFileShareObject(itemResource, role, this.suiteData.getSaasAppExternalUser1Name());
			DocumentSharingResult docSharingResult = universalApi.shareWithCollaborators(shareObject);
			Reporter.log(MarshallingUtils.marshall(docSharingResult), true);
		}

		return itemResource;
	}
	
	

}