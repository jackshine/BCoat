package com.elastica.beatle.dci;

import org.apache.http.entity.StringEntity;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.logger.Logger;

public class DCICommonTest extends CommonTest {
	DCIFunctions dciFunctions = null;
	
	/**
	 * Delete files from temp folders
	 */
	//@BeforeSuite(alwaysRun=true)
	public void deleteFolderBeforeSuite() {
		dciFunctions = new DCIFunctions();
		try {
			dciFunctions.cleanupTempFolder();
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	/**
	 * Enable content inspection
	 */
	@BeforeClass(alwaysRun=true)
	public void enableContentInspection() {
		dciFunctions = new DCIFunctions();
		try {
			String payload = dciFunctions.getEnableContentInspectionLog();
			dciFunctions.enableContentInspection(restClient, 
					dciFunctions.getCookieHeaders(suiteData), new StringEntity(payload), suiteData.getScheme(), suiteData.getHost());
			
		} catch (Exception ex) {
			Logger.info("Issue with Enabling of Content Inspection" + ex.getLocalizedMessage());
		}
	}
	
	/**
	 * Delete files from temp folders
	 */
	@AfterSuite(alwaysRun=true)
	public void deleteFolderAfterSuite() {
		dciFunctions = new DCIFunctions();
		try {
			dciFunctions.cleanupTempFolder();
		} catch (Exception ex) {
			Logger.info("Issue with Delete Folder Operation " + ex.getLocalizedMessage());
		}
	}
	
	
	
	
}
