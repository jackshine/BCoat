package com.elastica.common;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.elastica.action.audit.AuditAction;
import com.elastica.action.aws.AwsAction;
import com.elastica.action.azure.AzureADAction;
import com.elastica.action.azure.AzureAction;
import com.elastica.action.backend.BEAction;
import com.elastica.action.backend.sauce.SauceLabsHelper;
import com.elastica.action.box.BoxAction;
import com.elastica.action.centrify.CentrifyAction;
import com.elastica.action.dashboard.DashboardAction;
import com.elastica.action.dci.DCIAction;
import com.elastica.action.detect.DetectAction;
import com.elastica.action.dropbox.DropboxAction;
import com.elastica.action.fileupload.FileUploadAction;
import com.elastica.action.google.GDriveAction;
import com.elastica.action.google.GoogleAction;
import com.elastica.action.infra.InfraActions;
import com.elastica.action.investigate.InvestigateAction;
import com.elastica.action.login.LoginAction;
import com.elastica.action.o365.O365HomeAction;
import com.elastica.action.o365.O365LoginAction;
import com.elastica.action.okta.OktaAction;
import com.elastica.action.onelogin.OneLoginAction;
import com.elastica.action.pingone.PingOneAction;
import com.elastica.action.protect.ProtectAction;
import com.elastica.action.salesforce.SalesforceHomeAction;
import com.elastica.action.salesforce.SalesforceLoginAction;
import com.elastica.action.saml.SAMLAction;
import com.elastica.action.securlet.SecurletAction;
import com.elastica.action.securlet.o365.O365SecurletAction;
import com.elastica.action.settings.SettingsAction;
import com.elastica.action.sources.SourcesAction;
import com.elastica.action.store.StoreAction;
import com.elastica.action.universalapi.UniversalAPIFunctions;
import com.elastica.constants.FrameworkConstants;
import com.elastica.dataHandler.FileHandlingUtils;
import com.elastica.driver.DriverType;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.gateway.PolicyActions;
import com.elastica.gateway.ProtectTestConstants;
import com.elastica.logger.Logger;
import com.elastica.restClient.AuthorizationHandler;
import com.elastica.restClient.Client;
import com.saucelabs.saucerest.SecurityUtils;
import com.universal.common.GoogleMailServices;

/**
 * Common Method in Test Suite Level
 * @author Eldo Rajan
 *
 */
public class CommonTest extends DriverRunner{

	protected FileUploadAction fu = new FileUploadAction();
	protected AuditAction audit = new AuditAction();
	protected DetectAction detect = new DetectAction();
	protected DashboardAction dashboard = new DashboardAction();
	protected LoginAction login = new LoginAction();
	protected O365LoginAction o365Login = new O365LoginAction();
	protected O365HomeAction o365HomeAction = new O365HomeAction();
	protected SalesforceLoginAction salesforceLogin = new SalesforceLoginAction();
	protected SalesforceHomeAction salesforceHomeAction = new SalesforceHomeAction();
	protected AwsAction awsAction = new AwsAction();
	protected GDriveAction gda = new GDriveAction();
	protected DropboxAction dba = new DropboxAction();
	protected GoogleAction ga = new GoogleAction();
	protected BEAction backend = new BEAction();
	protected InfraActions infra = new InfraActions();
	protected SettingsAction settings = new SettingsAction();
	protected SAMLAction saml = new SAMLAction();
	protected OneLoginAction ola = new OneLoginAction();
	protected OktaAction okta = new OktaAction();
	protected AzureADAction aza = new AzureADAction();
	protected PingOneAction poa = new PingOneAction();
	protected CentrifyAction ca = new CentrifyAction();
	protected SecurletAction securlet = new SecurletAction();
	protected ProtectAction protect = new ProtectAction();
	protected O365SecurletAction o365Securlet = new O365SecurletAction();
	protected StoreAction store = new StoreAction();
	protected InvestigateAction investigate = new InvestigateAction();
	protected SourcesAction sources = new SourcesAction();
	protected PolicyActions policy= new PolicyActions();
	protected DCIAction dci= new DCIAction();
	protected BoxAction box= new BoxAction();
	protected AzureAction azure= new AzureAction();
	protected UniversalAPIFunctions universalAPI = new UniversalAPIFunctions();
	protected SuiteData suiteData;
	protected Client client;
	protected HttpResponse CSRFHeader ;
	protected String csrfToken;
	protected String sessionID;
	protected SauceLabsHelper sh;
	protected AuthorizationHandler auth;
	protected String sslMailFlag;
	private ITestContext localSuiteConfigurations;

	@BeforeClass(alwaysRun=true)
	public synchronized void initializeTests(ITestContext suiteConfigurations){
		Map<String, String> config = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_CONFIGURATION_FILEPATH);
		Map<String, String> dbConfig = FileHandlingUtils.readPropertyFile(FrameworkConstants.FRAMEWORK_DBCREDENTIAL_FILEPATH);

		Map<String,String> locationMap=backend.getGeoIPData();

		this.localSuiteConfigurations=suiteConfigurations;
		suiteData=new SuiteData();
		client = new Client();

		/**** Browser related ****/
		suiteData.setBrowser(suiteConfigurations.getCurrentXmlTest().getParameter("browser"));
		suiteData.setMode(suiteConfigurations.getCurrentXmlTest().getParameter("mode"));
		suiteData.setProxyUrl(suiteConfigurations.getCurrentXmlTest().getParameter("proxyUrl"));
		suiteData.setProxyExtension(suiteConfigurations.getCurrentXmlTest().getParameter("proxyExtension"));
		suiteData.setProxyExtensionVersion(suiteConfigurations.getCurrentXmlTest().getParameter("proxyExtensionVersion"));
		suiteData.setSSlCertificate(suiteConfigurations.getCurrentXmlTest().getParameter("sslCertificate"));
		suiteData.setExportHar(suiteConfigurations.getCurrentXmlTest().getParameter("exportHar"));
		suiteData.setExportHarFolder(suiteConfigurations.getCurrentXmlTest().getParameter("exportHarFolder"));
		suiteData.setFirefoxProfile(suiteConfigurations.getCurrentXmlTest().getParameter("firefoxProfile"));

		/**** Screenshot Listener related ****/
		suiteData.setSSlMailFlag(System.getProperty("sslMailFlag"));
		suiteData.setSSlMailClientId(dbConfig.get("sslMailClientId"));
		suiteData.setSSlMailClientSecret(dbConfig.get("sslMailClientSecret"));
		suiteData.setSSlMailRefreshToken(dbConfig.get("sslMailRefreshToken"));

		/**** Environment/Test method related ****/
		suiteData.setEnvName(getEnv(suiteConfigurations));

		if(suiteData.getEnvName().equalsIgnoreCase("EoE")){
			suiteData.setAccessToken(FrameworkConstants.accessTokenEoE);
		}else if(suiteData.getEnvName().equalsIgnoreCase("QAVPC")){
			suiteData.setAccessToken(FrameworkConstants.accessTokenQAVPC);
		}else if(suiteData.getEnvName().equalsIgnoreCase("CHA")){
			suiteData.setAccessToken(FrameworkConstants.accessTokenCHA);
		}else{
			suiteData.setAccessToken(FrameworkConstants.accessTokenOthers);
		}

		suiteData.setUsername(suiteConfigurations.getCurrentXmlTest().getParameter("username"));
		suiteData.setPassword(suiteConfigurations.getCurrentXmlTest().getParameter("password"));
		suiteData.setTenantName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantName"));
		suiteData.setTenantDomainName(suiteConfigurations.getCurrentXmlTest().getParameter("tenantDomainName"));
		suiteData.setTenantToken(suiteConfigurations.getCurrentXmlTest().getParameter("tenantToken"));
		suiteData.setBaseVersion(config.get("apiBaseVersion"));
		suiteData.setBaseUrl(config.get(suiteData.getEnvName().concat(".baseUrl")));
		suiteData.setDashboardUrl(config.get(suiteData.getEnvName().concat(".dashboardUrl")));
		suiteData.setLoginUrl(config.get(suiteData.getEnvName().concat(".loginURL")));
		suiteData.setApiServer(config.get(suiteData.getEnvName().concat(".apiServerHost")));
		suiteData.setHost(config.get(suiteData.getEnvName().concat(".hostname")));
		suiteData.setScheme(config.get(suiteData.getEnvName().concat(".scheme")));
		suiteData.setReferer(config.get(suiteData.getEnvName().concat(".referer")));
		suiteData.setAdminUsername(suiteConfigurations.getCurrentXmlTest().getParameter("adminUsername"));
		suiteData.setAdminPassword(suiteConfigurations.getCurrentXmlTest().getParameter("adminPassword"));
		suiteData.setDpoUsername(suiteConfigurations.getCurrentXmlTest().getParameter("dpoUsername"));
		suiteData.setDpoPassword(suiteConfigurations.getCurrentXmlTest().getParameter("dpoPassword"));
		suiteData.setEndUsername(suiteConfigurations.getCurrentXmlTest().getParameter("endUsername"));
		suiteData.setEndUserPassword(suiteConfigurations.getCurrentXmlTest().getParameter("endUserPassword"));
		suiteData.setTestUsername(suiteConfigurations.getCurrentXmlTest().getParameter("testUsername"));
		suiteData.setTestPassword(suiteConfigurations.getCurrentXmlTest().getParameter("testPassword"));

		/**** Saas App related ****/
		suiteData.setSaasAppName(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppName"));
		suiteData.setSaasAppUsername(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppUsername"));
		suiteData.setSaasAppPassword(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppPassword"));
		suiteData.setSaasAppMetaData(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppMetaData"));
		suiteData.setSaasAppBaseUrl(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppBaseUrl"));
		suiteData.setSaasAppBaseDomain(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppBaseDomain"));
		suiteData.setSaasAppToken(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppToken"));
		suiteData.setSaasAppTestUsername(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppTestUsername"));
		suiteData.setSaasAppTestPassword(suiteConfigurations.getCurrentXmlTest().getParameter("saasAppTestPassword"));
		suiteData.setAccountType(suiteConfigurations.getCurrentXmlTest().getParameter("accountType"));

		/**** Gateway Related related ****/
		suiteData.setCity((String)locationMap.get(GatewayTestConstants.CITY));
		suiteData.setCountry((String)locationMap.get(GatewayTestConstants.COUNTRY));
		suiteData.setLatitude(locationMap.get(GatewayTestConstants.LATITUDE));
		suiteData.setLongitude((String)locationMap.get(GatewayTestConstants.LONGITUDE));
		suiteData.setTestRunHost((String)locationMap.get(GatewayTestConstants.HOST));
		suiteData.setRunRegion((String)locationMap.get(GatewayTestConstants.REGION));
		suiteData.setTimeZone((String)locationMap.get(GatewayTestConstants.TIME_ZONE));



		try{

			if(suiteData.getSSlMailFlag()==null){
				if (suiteConfigurations.getCurrentXmlTest().getParameter("sslMailFlag")==null){
					suiteData.setSSlMailFlag("false");
				}else{
					suiteData.setSSlMailFlag(suiteConfigurations.getCurrentXmlTest().getParameter("sslMailFlag"));
				}
			}

			if (StringUtils.isBlank(suiteData.getPassword()))
				suiteData.setPassword(dbConfig.get(suiteData.getEnvName().concat(".").concat(suiteData.getUsername())));

			if (StringUtils.isBlank(suiteData.getTenantToken()))
				suiteData.setTenantToken(dbConfig.get(suiteData.getTenantDomainName()));

			if (StringUtils.isBlank(suiteData.getProxyUrl())) 
				suiteData.setProxyUrl(dbConfig.get(suiteData.getEnvName().toLowerCase().concat(".").concat(suiteData.getTenantDomainName())));

			if (StringUtils.isBlank(suiteData.getSaasAppUsername())) {
				suiteData.setSaasAppUsername(suiteData.getUsername());
				suiteData.setSaasAppPassword(dbConfig.get(suiteData.getSaasAppName().toLowerCase().concat(".").concat(suiteData.getUsername().toLowerCase())));
			}

			if (StringUtils.isBlank(suiteData.getSaasAppPassword())) 
				suiteData.setSaasAppPassword(dbConfig.get(suiteData.getSaasAppName().toLowerCase().concat(".").concat(suiteData.getSaasAppUsername().toLowerCase())));

			if (StringUtils.isBlank(suiteData.getTestPassword())) 
				suiteData.setTestPassword(dbConfig.get(suiteData.getEnvName().toLowerCase().concat(".").concat(suiteData.getTestUsername().toLowerCase())));
		}
		catch (Exception e){}

		auth = new AuthorizationHandler();
		CSRFHeader = auth.getCSRFHeaders(suiteData.getUsername(),suiteData.getPassword(),
				suiteData.getScheme(), suiteData.getHost());
		csrfToken = auth.getCSRFToken(CSRFHeader);
		sessionID = auth.getUserSessionID(CSRFHeader);
		suiteData.setCsrfToken(csrfToken);
		suiteData.setSessionID(sessionID);	

		suiteData.setAPIMap(FileHandlingUtils.readPropertyFile(ProtectTestConstants.PROTECT_API_CONFIGURATION_FILEPATH));

	}


	/*@BeforeMethod(groups ={"grid","cloud"})
	public void initializeBrowser(ITestContext theTestContext){
		startBrowser(theTestContext,suiteData);
		if(suiteData.getMode().equalsIgnoreCase("cloud")){
			suiteData.setSauceLabUrl("https://saucelabs.com/tests/"+sessionId.get());
			Logger.info("**************** Sauce Labs Session Link: "+suiteData.getSauceLabUrl()+" ****************");
		}
	}

	@AfterMethod(groups ={"grid","cloud"})
	public void closeBrowser(){
		stopBrowser();
	}*/

	@BeforeClass(dependsOnMethods="initializeTests",groups ={"local","grid","cloud"})
	public synchronized void initializeBrowser(ITestContext theTestContext){
		theTestContext.setAttribute("EnvironmentName", suiteData.getEnvName());
		if(suiteData.getEnvName().equalsIgnoreCase("EoE")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenEoE);
		}else if(suiteData.getEnvName().equalsIgnoreCase("QAVPC")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenQAVPC);
		}else if(suiteData.getEnvName().equalsIgnoreCase("CHA")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenCHA);
		}else{
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenOthers);
		}

		startBrowser(theTestContext);
		if(!browserInstanceExist(getWebDriver())){
			Logger.info("Browser initialization failed, reinitialization is in progress");
			try {Thread.sleep(30000);} catch (InterruptedException e) {}
			startBrowser(theTestContext);
		}


		suiteData.setHubUrl(testconfig.getHuburl());
		if(suiteData.getMode().equalsIgnoreCase(DriverType.Cloud.toString())){
			sh=new SauceLabsHelper();
			suiteData.setSauceUsername(suiteData.getHubUrl().split(":")[0]);
			suiteData.setSaucePassword(suiteData.getHubUrl().split(":")[1]);
		}

		if(suiteData.getMode().equalsIgnoreCase("cloud")){
			String sauceLabUrl = getPublicJobLink(suiteData.getHubUrl().split(":")[0],
					suiteData.getHubUrl().split(":")[1], sessionId.get());
			suiteData.setSauceLabUrl(sauceLabUrl);
			Logger.info("**************** Sauce Labs Session Link: "+suiteData.getSauceLabUrl()+" ****************");
		}
		suiteData.setUserAgent(getUserAgent(getWebDriver()));
		suiteData.setDeviceName(getDeviceName());
		theTestContext.setAttribute("WebDriverInstance", getWebDriver());
		theTestContext.setAttribute("sslMailFlag", suiteData.getSSlMailFlag());

		if (suiteData.getSSlMailFlag().equalsIgnoreCase("true")){
			GoogleMailServices gmailServices = 
					new GoogleMailServices(suiteData.getSSlMailClientId(),suiteData.getSSlMailClientSecret(),suiteData.getSSlMailRefreshToken());
			theTestContext.setAttribute("sslMailInstance", gmailServices);
		}
	}

	@AfterClass(groups ={"local","grid","cloud"})
	public synchronized void closeBrowser(ITestContext theTestContext){
		stopBrowser();
		writeSauceLabUrl(theTestContext.getName()+"======"+suiteData.getSauceLabUrl());
	}

	@BeforeMethod(groups ={"granular"})
	public synchronized void initializeBrowserGranular(ITestContext theTestContext){
		theTestContext.setAttribute("EnvironmentName", suiteData.getEnvName());
		if(suiteData.getEnvName().equalsIgnoreCase("EoE")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenEoE);
		}else if(suiteData.getEnvName().equalsIgnoreCase("QAVPC")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenQAVPC);
		}else if(suiteData.getEnvName().equalsIgnoreCase("CHA")){
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenCHA);
		}else{
			theTestContext.setAttribute("authToken",FrameworkConstants.accessTokenOthers);
		}

		startBrowser(theTestContext);
		if(!browserInstanceExist(getWebDriver())){
			Logger.info("Browser initialization failed, reinitialization is in progress");
			try {Thread.sleep(30000);} catch (InterruptedException e) {}
			startBrowser(theTestContext);
		}

		suiteData.setHubUrl(testconfig.getHuburl());

		suiteData.setUserAgent(getUserAgent(getWebDriver()));
		suiteData.setDeviceName(getDeviceName());
		theTestContext.setAttribute("WebDriverInstance", getWebDriver());
		theTestContext.setAttribute("sslMailFlag", suiteData.getSSlMailFlag());

		if (suiteData.getSSlMailFlag().equalsIgnoreCase("true")){
			GoogleMailServices gmailServices = 
					new GoogleMailServices(suiteData.getSSlMailClientId(),suiteData.getSSlMailClientSecret(),suiteData.getSSlMailRefreshToken());
			theTestContext.setAttribute("sslMailInstance", gmailServices);
		}
	}

	@AfterMethod(groups ={"granular"})
	public synchronized void closeBrowserGranular(ITestContext theTestContext){
		stopBrowser();
	}

	@BeforeSuite(alwaysRun=true)
	public synchronized void cleanupSauceLabsUrlFile(){
		String filePath=System.getProperty("user.dir") +File.separator+"target"+File.separator+"sauceLabsUrl.txt";
		try{
			File f = new File(filePath); f.delete();
		}catch(Exception e){}
	}

	public synchronized String getPublicJobLink(String userName, String accessKey, String jobId) {
		try {
			String key = userName + ":" + accessKey;
			String auth_token = SecurityUtils.hmacEncode("HmacMD5", jobId, key);
			return "https://saucelabs.com/jobs/" + jobId + "?auth=" + auth_token;
		} catch (IllegalArgumentException ex) {
			Logger.info("Unable to create an authenticated public link to job:", ex);
			return "";
		}
	}

	public synchronized void writeSauceLabUrl(String text) {
		try {			
			File fileWrite = new File(System.getProperty("user.dir") +File.separator+"target"+File.separator+"sauceLabsUrl.txt");
			BufferedWriter bw= new BufferedWriter(new FileWriter(fileWrite,true));
			bw.write(text+"\n");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized String getDeviceName(){
		String [] deviceName;
		String device=System.getProperty("os.name");
		deviceName =device.split(" ");
		return deviceName[0];
	}

	public synchronized String getUserAgent(WebDriver driver){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String useragent = (String)js.executeScript("return navigator.userAgent;");
		return useragent;
	}
	

	public synchronized String getEnv(ITestContext suiteConfigurations){
		if (System.getProperty("env")!=null){
			return System.getProperty("env").toString();
		}else {
			return suiteConfigurations.getCurrentXmlTest().getParameter("env").toString();
		}
	}

	public synchronized boolean browserInstanceExist(WebDriver driver){
		try {
			if (driver.getWindowHandles().isEmpty()){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		
	}
	
	/**
	 * @param parameter
	 * @return
	 */
	public String getSuiteParameter(String parameter){
		return localSuiteConfigurations.getCurrentXmlTest().getParameter(parameter);
	}
	

}

