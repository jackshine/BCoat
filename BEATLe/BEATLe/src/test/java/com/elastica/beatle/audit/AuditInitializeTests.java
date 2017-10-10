package com.elastica.beatle.audit;

import org.apache.http.HttpResponse;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.TestSuiteDTO;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;

public class AuditInitializeTests  extends InitializeTests {

	public static TestSuiteDTO suiteData;	
	/**
	 * @param suiteConfigurations
	 * @throws Exception 
	 */
	@BeforeTest(alwaysRun= true)
	public void initTests(ITestContext suiteConfigurations) throws Exception {
		Logger.info("==========================================================================================");
		Logger.info("Initializing Framework related configurations");
		
		suiteData = new TestSuiteDTO();
		super.initSuiteConfigurations(suiteConfigurations, suiteData);
		
		Logger.info("==========================================================================================");
		Logger.info("Initializing audit related configurations");
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(AuditTestConstants.AUDIT_API_CONFIGURATION_FILEPATH));
		HttpResponse CSRFHeader = AuthorizationHandler.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(), suiteData);
		suiteData.setCSRFToken(AuthorizationHandler.getCSRFToken(CSRFHeader));
		suiteData.setSessionID(AuthorizationHandler.getUserSessionID(CSRFHeader));	
		suiteData.setAuthParam(AuthorizationHandler.getAuthParam(suiteData.getUsername(), suiteData.getPassword()));
		/*if(AuditTestConstants.tenantsListForAparajiServicesCheck().contains(suiteData.getTenantName()) && "yes".equals(suiteData.getLoadApparraziServicesCheck()))
			suiteData.setAuditGoldenSetTestDataSetup(new AuditGoldenSetTestDataSetup());*/
		
		if("yes".equals(suiteData.getTenant2Aclcheck()))
		{
			//tenant 2
			HttpResponse CSRFHeaderTen2 = AuthorizationHandler.getCSRFHeaders(suiteData.getUser2Name(),suiteData.getUser2Name(), suiteData);
			suiteData.setUser2CsrfToken(AuthorizationHandler.getCSRFToken(CSRFHeaderTen2));
			suiteData.setUser2SessionID(AuthorizationHandler.getUserSessionID(CSRFHeaderTen2));	
			suiteData.setUser2AuthParam(AuthorizationHandler.getAuthParam(suiteData.getUser2Name(),suiteData.getUser2Password()));
			
			//admin
			HttpResponse CSRFHeaderAdmin = AuthorizationHandler.getCSRFHeaders(suiteData.getAdminUser(),suiteData.getAdminUserPwd(), suiteData);
			suiteData.setAdminUserCsrfToken(AuthorizationHandler.getCSRFToken(CSRFHeaderAdmin));
			suiteData.setAdminUserSessionID(AuthorizationHandler.getUserSessionID(CSRFHeaderAdmin));	
			suiteData.setAdminUserAuthParam(AuthorizationHandler.getAuthParam(suiteData.getAdminUser(),suiteData.getAdminUserPwd()));
			
		}
		
		
		//Spanva ip and version loading part
		suiteData.setSpanvaIp(suiteConfigurations.getCurrentXmlTest().getParameter("spanvaip"));
		suiteData.setSpanvaVersion(suiteConfigurations.getCurrentXmlTest().getParameter("spanvaversion"));
		
		//spanva ip authentication details
		suiteData.setSpanvausername(suiteConfigurations.getCurrentXmlTest().getParameter("spanvausername"));
		suiteData.setSpanvapwd(suiteConfigurations.getCurrentXmlTest().getParameter("spanvapwd"));
		suiteData.setSpanvaAgentName(suiteConfigurations.getCurrentXmlTest().getParameter("spanvaAgentName"));
		suiteData.setLogCompressionFormat(suiteConfigurations.getCurrentXmlTest().getParameter("logCompressionFormat"));
		
		Logger.info("==========================================================================================");
	}
}
