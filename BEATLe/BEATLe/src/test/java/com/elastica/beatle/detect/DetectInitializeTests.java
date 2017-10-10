package com.elastica.beatle.detect;

import org.apache.http.HttpResponse;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;

import com.elastica.beatle.CommonTest;
import com.elastica.beatle.InitializeTests;
import com.elastica.beatle.Authorization.AuthorizationHandler;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.es.ESQueryBuilder;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.protect.ProtectTestConstants;

public class DetectInitializeTests extends CommonTest {
	
	protected Client restClient;
	protected ESQueryBuilder esQueryBuilder;
	protected String apiServer;
	
	/**
	 * @param suiteConfigurations
	 * @throws Exception 
	 */
	@BeforeTest(alwaysRun=true)
	public void initTests(ITestContext suiteConfigurations) throws Exception{	
		//super.initSuiteConfigurations(suiteConfigurations, suiteData);
		
		restClient = new Client();
		esQueryBuilder = new ESQueryBuilder();
		System.out.println("================ GETTING CSFR TOKEN AND SESSIONID ================");
		System.out.println(suiteData.getUsername());
		System.out.println(suiteData.getPassword());
		 apiServer = suiteData.getApiserverHostName();
		Reporter.log("==================================================================",true);
		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile("/src/test/resources/detect/configurations/APIConfigurations.xml"));
		
		
		
		
	}

}
