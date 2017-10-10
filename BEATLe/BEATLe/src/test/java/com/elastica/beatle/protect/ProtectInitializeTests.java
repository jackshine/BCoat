package com.elastica.beatle.protect;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.universal.common.Salesforce;

public class ProtectInitializeTests  extends InitializeTests {


	public TestSuiteDTO suiteData;
	public List<NameValuePair> requestHeader;
	static ITestContext suiteConfigurations;
	protected Salesforce sfapi;
	
	/**
	 * @param suiteConfigurations
	 * @throws Exception 
	 */
	@BeforeTest(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {		
		this.suiteConfigurations = suiteConfigurations;
		suiteData = new TestSuiteDTO();
		super.initSuiteConfigurations(suiteConfigurations, suiteData);		
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));
		requestHeader = buildCookieHeaders();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	protected List<NameValuePair> buildCookieHeaders() throws Exception{
		List<NameValuePair> reqHeader = new ArrayList<NameValuePair>();
		try{
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		String csrfToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		reqHeader.add(new BasicNameValuePair("X-CSRFToken",csrfToken));
		reqHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csrfToken + ";"));
		reqHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUsername(),suiteData.getPassword())));
		reqHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		reqHeader.add(new BasicNameValuePair("X-Session", sessionID));
		reqHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getTenantToken()));
		reqHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		}
		catch(Exception e) {
			Reporter.log("Exception occured on test case initialization and so the test cases will be skipped", true);
			e.printStackTrace();
		}
		return reqHeader;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<NameValuePair> buildCookieHeadersForUser2(TestSuiteDTO suiteData) throws Exception{
		
		List<NameValuePair> reqHeader = new ArrayList<NameValuePair>();
		try{
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUser2Name(),suiteData.getUser2Password(), suiteData);
		String csrfToken = AuthorizationHandler.getCSRFToken(CSRFHeader);
		String sessionID = AuthorizationHandler.getUserSessionID(CSRFHeader);
		reqHeader.add(new BasicNameValuePair("X-CSRFToken",csrfToken));
		reqHeader.add(new BasicNameValuePair("Cookie", "sessionid=" + sessionID + "; csrftoken=" + csrfToken + ";"));
		reqHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, AuthorizationHandler.getAuthParam(suiteData.getUser2Name(),suiteData.getUser2Password())));
		reqHeader.add(new BasicNameValuePair("Referer", "https://"+suiteData.getHost()));
		reqHeader.add(new BasicNameValuePair("X-Session", sessionID));
		reqHeader.add(new BasicNameValuePair("X-TenantToken", suiteData.getUser2TenantToken()));
		reqHeader.add(new BasicNameValuePair("X-User", suiteData.getUser2Name()));
		}
		catch(Exception e) {
			Reporter.log("Exception occured on test case initialization and so the test cases will be skipped", true);
			e.printStackTrace();
		}
		return reqHeader;
	}
	

	public static String getRegressionSpecificSuitParameters(String parameter){
        return suiteConfigurations.getCurrentXmlTest().getParameter(parameter);
    }
}
