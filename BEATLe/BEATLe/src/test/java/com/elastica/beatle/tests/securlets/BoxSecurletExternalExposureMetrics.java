package com.elastica.beatle.tests.securlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.elastica.beatle.DateUtils;
import com.elastica.beatle.MarshallingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.CIQValidator;
import com.elastica.beatle.securlets.DocumentValidator;
import com.elastica.beatle.securlets.ESQueryBuilder;
import com.elastica.beatle.securlets.LogUtils;
import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.dto.BoxDocument;
import com.elastica.beatle.securlets.dto.ExposureTotals;
import com.elastica.beatle.securlets.dto.SecurletDocument;
import com.elastica.beatle.securlets.dto.TotalObject;
import com.universal.constants.CommonConstants;
import com.universal.dtos.box.BoxUserInfo;
import com.universal.dtos.box.FileEntry;



public class BoxSecurletExternalExposureMetrics extends SecurletUtils {
	ESQueryBuilder esQueryBuilder = null;
	DocumentValidator docValidator;
	CIQValidator ciqValidator;
	String resourceId;
	BoxUserInfo userInfo;
	String shareExpiry ;
	

	public BoxSecurletExternalExposureMetrics() throws Exception {
		esQueryBuilder = new ESQueryBuilder();
		docValidator = new DocumentValidator();
		shareExpiry = DateUtils.getDaysFromCurrentTime(1);
	}
	
	
	/**
	 * Test 1
	 * Check the metrics as depicted in venn diagram
	 * Check with UI server call and API server call and check metrics are same
	 * 
	 */
	@Test(groups={"DASHBOARD", "EXTERNAL", "EXPOSED_FILES", "FILTER"})
	public void verifyVennDiagramMetricsForExposedFiles() throws Exception {

		String steps[] = {
				"1. This test check the metrics as depicted in venn diagram ",
				"2. Check the metrics as depicted in Venn Diagram with API server call.",
				"3. Check the metrics as depicted in Venn Diagram with UI  server call.",
				"4. Verify both metrics are same."
				};

		LogUtils.logTestDescription(steps);

		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));

		Reporter.log("External exposure metrics totals with API server call", true);
		ExposureTotals apiCallTotals = getExposuresMetricsTotal(elapp.el_box.name(), qparams);
		long apiCountPublic 	= apiCallTotals.getPublicExposouresCount();
		long apiCountInternal 	= apiCallTotals.getInternalExposouresCount();
		long apiCountExternal 	= apiCallTotals.getExternalExposouresCount();

		Reporter.log("API call Public count  :"+apiCountPublic, true);
		Reporter.log("API call Internal count:"+apiCountInternal, true);
		Reporter.log("API call External count:"+apiCountExternal, true);

		Reporter.log("External exposure metrics totals with UI server call", true);
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  Boolean.FALSE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		ExposureTotals uiCallTotals = getUIExposuresMetricsTotal(elapp.el_box.name(), qparams);
		long uiCountPublic 	= uiCallTotals.getPublicExposouresCount();
		long uiCountInternal 	= uiCallTotals.getInternalExposouresCount();
		long uiCountExternal 	= uiCallTotals.getExternalExposouresCount();
		
		Reporter.log("UI Call Public count  :"+uiCountPublic, true);
		Reporter.log("UI Call Internal count:"+uiCountInternal, true);
		Reporter.log("UI Call External count:"+uiCountExternal, true);
		
		CustomAssertion.assertEquals(apiCountPublic, uiCountPublic, 
							"API public count "+apiCountPublic+" is not matching with UI public count "+uiCountPublic);
		CustomAssertion.assertEquals(apiCountInternal, uiCountInternal, 
							"API internal count "+apiCountInternal+" is not matching with UI internal count "+uiCountInternal);
		CustomAssertion.assertEquals(apiCountExternal, uiCountExternal, 
							"API internal count "+apiCountExternal+" is not matching with UI internal count "+uiCountExternal);
		
	}
	
	
	
	
	
	/**
	 * Test 5
	 * 
	 * List all internally exposed files and validate all documents
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void listAllInternallyExposedFiles() throws Exception {
		LogUtils.logTestDescription("Retrieve all internally exposed files and verify each and every field as per schema for all documents.");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposures.types", "all_internal");
		
		Reporter.log("Fetching the box exposed documents to find the total count.", true);
		BoxDocument documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		
		//Fetch all the documents in one query
		int limit = documents.getMeta().getTotalCount();
		additionalParams.put("offset", "0");
		additionalParams.put("limit",  String.valueOf(limit));
		Reporter.log("Fetching all the box exposed documents", true);
		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		
		//Verify all internally owned documents
		Reporter.log("Validating all the box internally owned exposed documents...", true);
		docValidator.validateExposedDocuments(documents, true);
	}
	
	
	/**
	 * Test 6
	 * 
	 * List all internally exposed files after internal exposure
	 * @throws Exception
	 */
	@Test(groups={"DASHBOARD", "INTERNAL", "EXPOSED_FILES", "FILTER"})
	public void listAllInternallyExposedFilesAfterInternalExposure() throws Exception {
		
		LogUtils.logTestDescription("Retrieve all internally exposed files and verify only the exposed document and its fields.");
		
		HashMap<String, String> additionalParams = new HashMap<String, String>();
		additionalParams.put("exposures.types", "all_internal");
		additionalParams.put("offset", "0");
		additionalParams.put("limit", "1000");
		
		Reporter.log("Getting all the documents ...", true);
		BoxDocument documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		int countBefore = documents.getMeta().getTotalCount();
		
		Reporter.log("Upload a file and share it ...", true);
		//Expose a document internally and check the count
		FileEntry sharedFile = uploadFileAndShareit("/", "Hello.java", "company");
		
		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		int countAfter = documents.getMeta().getTotalCount();
		
		//validate the newly added record count
		Reporter.log("Verifying the count before and after exposure ...", true);
		CustomAssertion.assertEquals(countAfter, countBefore+1, "Internally exposed document not returned");
		CustomAssertion.assertTrue(MarshallingUtils.marshall(documents).contains(sharedFile.getId()), "Internally exposed document not returned in the records");
		
		
		
		//validate the newly added document
		//Pass the additional params
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("createdBy", this.saasAppUserAccount.getUsername());
		hmap.put("ownedBy", this.saasAppUserAccount.getUsername());
		hmap.put("name", sharedFile.getName());
		hmap.put("format", "java");
		hmap.put("ownerId", sharedFile.getOwnedBy().getId());
		hmap.put("size", String.valueOf(sharedFile.getSize()));
		hmap.put("parentName", "All Files");
		hmap.put("docType", "file");
		
		docValidator.verifyDocument(documents, sharedFile.getId(), "ALL_INTERNAL", "company", hmap);
		
		Reporter.log("Disable the sharing on the file ...", true);
		//remove the exposure
		sharedFile = universalApi.disableSharedLink(sharedFile.getId());
		
		//Wait for three mins
		sleep(CommonConstants.THREE_MINUTES_SLEEP);
		
		documents = getBoxDocuments("true", elapp.el_box.name(), additionalParams);
		CustomAssertion.assertEquals(documents.getMeta().getTotalCount(), countBefore, "Internally exposed document count not decremented");
		CustomAssertion.assertFalse(MarshallingUtils.marshall(documents).contains(sharedFile.getId()), "Internally exposed document not returned in the records");
		
		//clean up file
		universalApi.deleteFile(sharedFile.getId(), sharedFile.getEtag());
		
	}
	
	
	
	/**
	 * Test 17
	 * @throws Exception
	 */
	
	//@Test
	public void verifyUserTotalsAsInVennDiagram() throws Exception {
		LogUtils.logTestDescription("This test verify the user totals as in venn diagram");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		//qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		ExposureTotals exposureTotals = getUserTotals(elapp.el_box.name(), qparams);
		
		Reporter.log("Verifying the internal user totals is greater than zero", true);
		for(TotalObject totalObject : exposureTotals.getObjects()) {
			CustomAssertion.assertTrue(totalObject.getTotal() >=0 , totalObject.getId()  + " user total >= 0" , totalObject.getId()  + " user total < 0");
		}
	}
	
	
	/**
	 * Test 18
	 * @throws Exception
	 */
	//@Test 
	public void verifyUserDocumentExposures() throws Exception {
		LogUtils.logTestDescription("This test verify the user documemt exposures");
		Logger.info( "Getting user totals ...");
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument documents = getUserExposures(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User document exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}
	
	/**
	 * Test 19
	 * @throws Exception
	 */
	//@Test 
	public void verifyUserVulnerabilities() throws Exception {
		LogUtils.logTestDescription("This test verify the user vulnerabilities");
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  Boolean.TRUE.toString()));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Box.name()));
		
		SecurletDocument documents = getUserVulnerabilities(elapp.el_box.name(), qparams);
		for (com.elastica.beatle.securlets.dto.Object object : documents.getObjects()) {
			CustomAssertion.assertTrue(object.getTotal() >=0, "User vulnerability exposure total can't be null");
			Reporter.log(String.valueOf(object.getTotal()), true);
		}
	}

}