/**
 * 
 */
package com.elastica.beatle.tests.splunkTests;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.protect.ProtectTestConstants;

/**
 * @author anuvrath
 *
 */
public class SplunkInitializer extends InitializeTests {
	
	protected String auditDailyReportName;
	protected String auditWeeklyReportName;
	protected String auditMonthlyReportName;
	protected TestSuiteDTO suiteData;
	protected String emailRecipientName;
	protected Client restClient;
	protected List<NameValuePair> requestHeaders;
	protected static final int invocationCount = 1;
	
	@BeforeTest(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception{
		suiteData = new TestSuiteDTO();
		restClient = new Client();
		super.initSuiteConfigurations(suiteConfigurations, suiteData);			
		auditDailyReportName = suiteConfigurations.getCurrentXmlTest().getParameter("auditDailyReportName");
		auditWeeklyReportName = suiteConfigurations.getCurrentXmlTest().getParameter("auditWeeklyReportName");
		auditMonthlyReportName = suiteConfigurations.getCurrentXmlTest().getParameter("auditMonthlyReportName");
		emailRecipientName = suiteConfigurations.getCurrentXmlTest().getParameter("recipientName");			
		if(suiteData.getUsername() != null && suiteData.getPassword() != null ){
			HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
			suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
			suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));	
			suiteData.setAuthParam(AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword()));			
		}else{
			Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			Logger.info("No User credentails are passed for cloudSoc Portal.");
			Logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}			
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));
		suiteData.setProxyUrl(suiteConfigurations.getCurrentXmlTest().getParameter("proxyUrl"));
		suiteData.setProxyExtension(suiteConfigurations.getCurrentXmlTest().getParameter("proxyExtension"));
		suiteData.setProxyExtensionVersion(suiteConfigurations.getCurrentXmlTest().getParameter("proxyExtensionVersion"));
		requestHeaders = buildRequestHeaders();
	}
	
	/**
	 * @return
	 */
	private List<NameValuePair> buildRequestHeaders() {
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.COOKIE, "sessionid="+suiteData.getSessionID()+"; csrftoken="+suiteData.getCSRFToken()+";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));	
		return requestHeader;
	}
}
