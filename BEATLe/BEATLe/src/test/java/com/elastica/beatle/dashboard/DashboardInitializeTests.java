package com.elastica.beatle.dashboard;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;

import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class DashboardInitializeTests  extends InitializeTests {


	public TestSuiteDTO suiteData;
	public List<NameValuePair> requestHeader;
	
	/**
	 * @param suiteConfigurations
	 * @throws Exception 
	 */
	@BeforeTest(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		
		suiteData = new TestSuiteDTO();
		super.initSuiteConfigurations(suiteConfigurations, suiteData);		
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(DashboardTestConstants.DASHBOARD_API_CONFIGURATION_FILEPATH));		
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));
		requestHeader = buildCookieHeaders();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected List<NameValuePair> buildCookieHeaders() throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();
		requestHeader.add(new BasicNameValuePair("X-CSRFToken",suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + suiteData.getSessionID() + "; csrftoken=" + suiteData.getCSRFToken() + ";"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		requestHeader.add(new BasicNameValuePair("Referer", suiteData.getReferer()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT, "​*/*​"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
		requestHeader.add(new BasicNameValuePair(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
		return requestHeader;
	}	
}