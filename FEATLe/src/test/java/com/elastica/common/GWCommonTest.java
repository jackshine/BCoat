package com.elastica.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.elastica.gateway.GatewayTestConstants;
import com.elastica.logger.Logger;
import com.jacob.com.LibraryLoader;

import autoitx4java.AutoItX;

public class GWCommonTest extends CommonTest {
	
	@BeforeClass(groups ={"grid","cloud","local"})
	public void getUpdatedBrowser(ITestContext suiteConfigurations){
		suiteData.setBrowser(suiteConfigurations.getCurrentXmlTest().getParameter("browser"));
		browser = System.getProperty("browser");
		if (browser!=null){
			suiteData.setBrowser(browser);
		}
		
	}
	
	
	//@BeforeMethod(alwaysRun=true)
	public void printCredentials(){
		Logger.info("***********************************************************************************");
		Logger.info("CloudSOC Admin: "+suiteData.getUsername()+"/"+suiteData.getPassword());
		Logger.info("CloudSOC Test user: "+suiteData.getTestUsername()+"/"+suiteData.getTestPassword());
		Logger.info("Saas App Test user: "+suiteData.getSaasAppUsername()+"/"+suiteData.getSaasAppPassword());
		Logger.info("***********************************************************************************");
		//Logger.info("Password stored on link: https://docs.google.com/a/elastica.co/spreadsheets/d/1V7lgaaAqm-pCF6Hzs1DPVsIeNqNIMsClU7K00x6SJ0Y/edit?usp=sharing");
		//Logger.info("***********************************************************************************");
	}

	public Map <String, Object> setCommonFieldsInExpectedDataMap(Map <String, Object> expectedDataMap){

		if (suiteData.getTenantDomainName()!=null){
			expectedDataMap.put(GatewayTestConstants.DOMAIN, suiteData.getTenantDomainName());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.DOMAIN, "NOT_EMPTY");
		}
		if (suiteData.getAccountType()!=null){
			expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,suiteData.getAccountType());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.ACCOUNT_TYPE,"NOT_EMPTY");
		}
		if (suiteData.getBrowser()!=null){
			expectedDataMap.put(GatewayTestConstants.BROWSER, suiteData.getBrowser());
			if(suiteData.getBrowser().equalsIgnoreCase("ie")){
				expectedDataMap.put(GatewayTestConstants.BROWSER, "Internet Explorer");
			}
		}
		else{
			expectedDataMap.put(GatewayTestConstants.BROWSER, "NOT_EMPTY");
		}
		if(getDeviceName()!=null){
			expectedDataMap.put(GatewayTestConstants.DEVICE, getDeviceName());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.DEVICE, "NOT_EMPTY");
		}
		if(suiteData.getTestUsername()!=null){
			expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, suiteData.getTestUsername());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.ELASTICA_USER, "NOT_EMPTY");
		}
		if(suiteData.getSaasAppName()!=null){
			expectedDataMap.put(GatewayTestConstants.FACILITY, suiteData.getSaasAppName());
		}
		else{
			expectedDataMap.put(GatewayTestConstants.FACILITY,"NOT_EMPTY" );
		}
		if(suiteData.getTestRunHost()!=null){
			expectedDataMap.put(GatewayTestConstants.HOST, suiteData.getTestRunHost());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.HOST, "NOT_EMPTY");
		}

		if(suiteData.getTimeZone()!=null){
			expectedDataMap.put(GatewayTestConstants.TIME_ZONE, suiteData.getTimeZone());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.TIME_ZONE,"NOT_EMPTY");
		}
		if(suiteData.getSaasAppUsername()!=null){
			expectedDataMap.put(GatewayTestConstants.USER, suiteData.getSaasAppUsername());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.USER, "NOT_EMPTY" );
		}
		if(getUserAgent(getWebDriver())!=null){
			String userAgent=getUserAgent(getWebDriver());
			expectedDataMap.put(GatewayTestConstants.USER_AGENT, userAgent);
			if(getUserAgent(getWebDriver()).contains(".NET")){
				expectedDataMap.put(GatewayTestConstants.USER_AGENT, userAgent.substring(0, userAgent.length()/2));
			}
			
		}
		else {
			expectedDataMap.put(GatewayTestConstants.USER_AGENT, "NOT_EMPTY");
		}
		expectedDataMap.put(GatewayTestConstants.CREATED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.INSERTED_TIME_STAMP, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.IS_ANONYMOUS_PROXY, GatewayTestConstants.IS_ANONYMOUS_PROXY_FALSE);
		expectedDataMap.put(GatewayTestConstants.REQ_SIZE, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.REQ_URI, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.USER_NAME, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.VERSION, "NOT_EMPTY");
		expectedDataMap.put(GatewayTestConstants.SOURCE, "GW");
		return expectedDataMap;

	}

	public Map <String, Object> setLocationFieldsInExpectedDataMap(Map <String, Object> expectedDataMap){
		if(suiteData.getCity()!=null){
			expectedDataMap.put(GatewayTestConstants.CITY, suiteData.getCity());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.CITY, "NOT_EMPTY");
		}
		if(suiteData.getCountry()!=null){
			expectedDataMap.put(GatewayTestConstants.COUNTRY, suiteData.getCountry());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.COUNTRY, "NOT_EMPTY");
		}
/* Removed temporary
		if(suiteData.getLatitude()!=null){
			expectedDataMap.put(GatewayTestConstants.LATITUDE, suiteData.getLatitude());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.LATITUDE, "NOT_EMPTY");
		}

		if(suiteData.getLongitude()!=null){
			expectedDataMap.put(GatewayTestConstants.LONGITUDE, suiteData.getLongitude());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.LONGITUDE, "NOT_EMPTY");
		}
		*/

		if(suiteData.getCity()!=null){
			expectedDataMap.put(GatewayTestConstants.LOCATION, suiteData.getCity());
		}
		else {
			expectedDataMap.put(GatewayTestConstants.LOCATION, "NOT_EMPTY");
		}

		if(suiteData.getRunRegion()!=null){
			expectedDataMap.put(GatewayTestConstants.REGION, suiteData.getRunRegion());
			if (suiteData.getRunRegion().equals("01")) expectedDataMap.put(GatewayTestConstants.REGION, "00");
		}
		else {
			expectedDataMap.put(GatewayTestConstants.REGION, "NOT_EMPTY");
		}
		return expectedDataMap;
	}

	public boolean isFileDownloadSuccess(String fileName){
		File file = new File(System.getProperty("user.home")+"/Downloads/"+fileName); 
		Boolean status=file.exists();
		if (status){
			Logger.info("File "+fileName+" exist in download folder");
		}
		else {
			Logger.info("File "+fileName+" doen not exist in download folder");
		}
		return status;
	}

	public void deleteFileInDownloadFolder(String fileName){
		System.out.println(fileName);
		String filename=fileName.substring(0, fileName.indexOf('.'));
		File file = new File(System.getProperty("user.home")+"/Downloads"); 
		File[] listFiles = file.listFiles();
		for (File listFile : listFiles) {
			String fileNameInList=listFile.getName();
			// Reporter.log("File list in Download folder :"+fileNameInList, true);
			if(fileNameInList.contains(filename)){
				listFile.delete();
				Logger.info("File found in download folder and deleted successfully");
			}
		}

	}

	public boolean clickOkInPopup() throws InterruptedException{
		Logger.info("Started popup clicking");
		if (suiteData.getProxyExtension()==null) {
			Logger.info("Reach Agent Popup inprogress");
			Thread.sleep(15000);
			return true;
		} else {
			String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
			File file = new File(dllPath);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			AutoItX x = new AutoItX();
			//x.controlGetFocus(title);
			Thread.sleep(8000);
			System.out.println(x.winExists("Elastica CloudSOC"));
			x.winActivate("Elastica CloudSOC");
			x.winWaitActive("Elastica CloudSOC", "", 15);
			if (x.winExists("Elastica CloudSOC")){
				//x.winWaitActive("Elastica CloudSOC", "", 15);
				x.controlGetFocus("Elastica CloudSOC");
				Thread.sleep(5000);
				x.winClose("Elastica CloudSOC");
				Logger.info("Blocker Popup Closed");
				return true;
			} else {
				Logger.info("Blocker Popup Not Found");
				return false;
			}
		}
		
	}
	
	
	public boolean ieDownloadFile() throws InterruptedException{
		Logger.info("Started popup clicking");
			String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
			File file = new File(dllPath);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			AutoItX x = new AutoItX();
			//x.controlGetFocus(title);
			Thread.sleep(8000);
			System.out.println(x.winExists("[CLASS:DirectUIHWND]"));
			x.winActivate("[CLASS:DirectUIHWND]");
			x.winWaitActive("[CLASS:DirectUIHWND]", "", 15);
			if (x.winExists("[CLASS:DirectUIHWND]")){
				//x.winWaitActive("Elastica CloudSOC", "", 15);
				x.controlGetFocus("[CLASS:DirectUIHWND]");
				Thread.sleep(5000);
				x.send("{!n}");
				Thread.sleep(3000);
				x.send("{!s}");
				Thread.sleep(3000);
				x.send("{^q}");
				Logger.info("Blocker Popup Closed");
				return true;
			} else {
				Logger.info("Blocker Popup Not Found");
				return false;
			}
		
	}
	
	
	public boolean clickOkInPopup(SuiteData suiteData) throws InterruptedException{
		Logger.info("Started popup clicking");
		if (suiteData.getTestUsername().toLowerCase().contains("reach")) {
			Logger.info("Reach Agent Popup inprogress");
			Thread.sleep(15000);
			return true;
		} else {
			String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
			File file = new File(dllPath);
			System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
			AutoItX x = new AutoItX();
			//x.controlGetFocus(title);
			Thread.sleep(8000);
			System.out.println(x.winExists("Elastica CloudSOC"));
			x.winActivate("Elastica CloudSOC");
			x.winWaitActive("Elastica CloudSOC", "", 15);
			if (x.winExists("Elastica CloudSOC")){
				//x.winWaitActive("Elastica CloudSOC", "", 15);
				x.controlGetFocus("Elastica CloudSOC");
				Thread.sleep(5000);
				x.winClose("Elastica CloudSOC");
				Logger.info("Blocker Popup Closed");
				return true;
			} else {
				Logger.info("Blocker Popup Not Found");
				return false;
			}
		}
		
	}	
	public boolean clickOkInPopupSalesforce() throws InterruptedException{
		Logger.info("Started popup clicking");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
		//x.controlGetFocus(title);
		Thread.sleep(15000);
		System.out.println(x.winExists("[JavaScript Application]"));
		x.winActivate("[JavaScript Application]");
		x.winWaitActive("[JavaScript Application]", "", 15);
		if (x.winExists("[JavaScript Application]")){
			//x.winWaitActive("Elastica CloudSOC", "", 15);
			x.controlGetFocus("[JavaScript Application]");
			Thread.sleep(5000);
			x.winClose("[JavaScript Application]");
			Logger.info("Blocker Popup Closed");
			return true;
		} else {
			Logger.info("Blocker Popup Not Found");
			return false;
		}
	}
	
	public boolean uploadMultipleFiles_Firefox(String path) throws InterruptedException {
		Logger.info("Uploading Multi files inprogress");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
        x.winActivate("File Upload");
        x.winWaitActive("File Upload");
        Thread.sleep(5000);
	        x.controlFocus("[CLASS:#32770]","","Edit1");
	        Thread.sleep(5000);
	        x.ControlSetText("[CLASS:#32770]", "", "Edit1", path);
	        Thread.sleep(5000);
	
	        x.controlClick("[CLASS:#32770]", "","Button1");
	        Thread.sleep(5000);
	        x.send("\"multi1\" \"multi2\" \"multi3.pdf\"");
	        //x.send("^a");
	        Thread.sleep(5000);
	        x.controlClick("[CLASS:#32770]", "","Button1");
	        Thread.sleep(5000);
	        Logger.info("Uploading Multi files completed");
        return true;
	}
	
	
	public boolean uploadSingleFile_Firefox1(String path) throws InterruptedException {
		Logger.info("Started popup clicking");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
        x.winActivate("File Upload");
        x.winWaitActive("File Upload");
        Thread.sleep(5000);
	        x.controlFocus("[CLASS:#32770]","","Edit1");
	        Thread.sleep(5000);
	        x.ControlSetText("[CLASS:#32770]", "", "Edit1", path);
	        Thread.sleep(5000);
	
	        x.controlClick("[CLASS:#32770]", "","Button1");
//	        Thread.sleep(5000);
//	        x.send("\"multi1\"");
	        //x.send("^a");
//	        Thread.sleep(5000);
//	        x.controlClick("[CLASS:#32770]", "","Button1");
	        Thread.sleep(50000);
        return true;
	}
	
	public boolean uploadSingleFile(String path, SuiteData suiteData) throws InterruptedException {
		Logger.info("Started popup clicking");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
		Logger.info("Inside the AutoIT handler block");
		if (suiteData.getBrowser().equals("Firefox") || suiteData.getBrowser().equals("firefox")) {
			Logger.info("Firefox Upload in Progress..");
			Logger.info("File upload dialog is appeared: "+x.winExists("File Upload"));
			Logger.info("Selecting the File Upload window");
	        x.winActivate("File Upload");
	        Logger.info("Windows activated: "+ x.winWaitActive("File Upload"));
	        if (!x.winWaitActive("File Upload")){
	        	x.winActivate("File Upload");
	        }
	        Logger.info("Waiting for Max 20 seconds for popup window get active, else it will timeout");
	        x.winWaitActive("File Upload", "", 20);
		} 
		else if (suiteData.getBrowser().equals("ie") || suiteData.getBrowser().equals("IE")||suiteData.getBrowser().equals("Internet Explorer")) {
			Logger.info("IE Upload in Progress..");
			Logger.info("File upload dialog is appeared: "+x.winExists("Choose File to Upload"));
			Logger.info("Selecting the File Upload window");
	        x.winActivate("Choose File to Upload");
	        Logger.info("Windows activated: "+ x.winWaitActive("Choose File to Upload"));
	        if (!x.winWaitActive("Choose File to Upload")){
	        	x.winActivate("Choose File to Upload");
	        }
	        Logger.info("Waiting for Max 20 seconds for popup window get active, else it will timeout");
	        x.winWaitActive("Choose File to Upload", "", 20);
		}
		else {
			Logger.info("Chrome Upload in Progress..");
			x.winActivate("Open");
	        x.winWaitActive("Open");
	        Thread.sleep(5000);
		}
        
        Logger.info("Popup Window get activated");
        Thread.sleep(5000);
        x.controlFocus("[CLASS:#32770]","","Edit1");
        Logger.info("Typing in editbox");
        Thread.sleep(5000);
        x.ControlSetText("[CLASS:#32770]", "", "Edit1", path);
        Thread.sleep(5000);
	
        x.controlClick("[CLASS:#32770]", "","Button1");
        Logger.info("Clicked the upload button");
//	        Thread.sleep(5000);
//	        x.send("\"multi1\"");
	        //x.send("^a");
//	        Thread.sleep(5000);
//	        x.controlClick("[CLASS:#32770]", "","Button1");
        Thread.sleep(50000);
        return true;
	}

	
	public boolean uploadMultipleFiles_Chrome(String path) throws InterruptedException {
		Logger.info("Started popup clicking");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
		x.winActivate("Open");
        x.winWaitActive("Open");
        Thread.sleep(5000);
        x.controlFocus("[CLASS:#32770]","","Edit1");
        Thread.sleep(5000);
        x.ControlSetText("[CLASS:#32770]", "", "Edit1", path);
        Thread.sleep(5000);

        x.controlClick("[CLASS:#32770]", "","Button1");
        Thread.sleep(5000);
        x.send("\"test\" \"test1\" \"test2\" \"test4\"");
        Thread.sleep(5000);
        x.controlClick("[CLASS:#32770]", "","Button1");
        Thread.sleep(5000);
        return true;
	}

	public boolean uploadSingleFiles_Chrome(String path, String fileName) throws InterruptedException {
		Logger.info("Started uploading " + fileName + " in Progress");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
		x.winActivate("Open");
        x.winWaitActive("Open");
        Thread.sleep(5000);
        x.controlFocus("[CLASS:#32770]","","Edit1");
        Thread.sleep(5000);
        x.ControlSetText("[CLASS:#32770]", "", "Edit1", path + fileName);
        Thread.sleep(5000);
        Logger.info("The sent file name " + fileName);
        x.controlClick("[CLASS:#32770]", "","Button1");
        Thread.sleep(50000);
    	Logger.info("Started uploading " + fileName + " completed");
        return true;
	}
	
	public boolean uploadFolder_Chrome(String path, String folderwithPath) throws InterruptedException {
		Logger.info("Started uploading Folder" +  path + folderwithPath + " in Progress");
		String dllPath = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"lib"+File.separator+"jacob-1.18-x64.dll";
		File file = new File(dllPath);
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
		AutoItX x = new AutoItX();
		x.winActivate("Browse For Folder");
		x.winWaitActive("Browse For Folder");
		Thread.sleep(5000);
		x.controlFocus("[CLASS:#32770]","","Edit1");
		Thread.sleep(5000);
		x.ControlSetText("[CLASS:#32770]", "", "Edit1", "");
		Thread.sleep(5000);
		x.ControlSetText("[CLASS:#32770]", "", "Edit1", path + folderwithPath);
		Thread.sleep(5000);
		
		x.controlClick("[CLASS:#32770]", "","Button2");
		Thread.sleep(50000);
		return true;
	}
	
	
	
	 public List<String> getStringInMultipleLanguage(){
	        List<String> multipleLanguage=new ArrayList();
	        multipleLanguage.add("Hindi:"+"नमस्ते");
	        multipleLanguage.add("English:"+"Hello");
	        multipleLanguage.add("Spanish:"+"Hola");
	        multipleLanguage.add("Bangala:"+"হ্যালো");
	        multipleLanguage.add("Kannada:"+"ಹಲೋ");  
	        multipleLanguage.add("Tamil:"+"வணக்கம்");
	        multipleLanguage.add("Malyalam:"+"ഹലോ");
	        multipleLanguage.add("Bulgarian:"+"Здравей");
	        multipleLanguage.add("French:"+"Bonjour");
	        multipleLanguage.add("Gujrati:"+"હેલો");
	        multipleLanguage.add("Chinese:"+"你好");
	        multipleLanguage.add("Greek:"+"Χαίρετε");
	        multipleLanguage.add("Arabic:"+"مرحبا");
	        multipleLanguage.add("Burmese:"+"ဟယ်လို");
	        multipleLanguage.add("Georgian:"+"გაუმარჯოს");
	        multipleLanguage.add("Hungarian:"+"Helló");
	        multipleLanguage.add("Japanese:"+"こんにちは");
	        multipleLanguage.add("Kazakh:"+"Сәлеметсіз бе");
	        multipleLanguage.add("Khmer:"+"ជំរាបសួរ");
	        multipleLanguage.add("Korean:"+"안녕하세요");
	        multipleLanguage.add("Lao:"+"ສະບາຍດີ");
	        multipleLanguage.add("Macdonian:"+"Здраво");
	        multipleLanguage.add("Mangolian:"+"Сайн уу");
	        multipleLanguage.add("Telgu:"+"హలో");
	        multipleLanguage.add("Sindhi:"+"سلام");
	        multipleLanguage.add("Yidish:"+"העלא");
	        multipleLanguage.add("Yorkuba:"+"Pẹlẹ o");
	        multipleLanguage.add("Thai:"+"สวัสดี");
	        multipleLanguage.add("Tajik:"+"Салом");
	        return multipleLanguage;
	    }
	 
	 public String getSaasAppUserName(){
			return suiteData.getSaasAppUsername().replaceAll("@", "_");
		}
	 
	 public String getOneDriveBusinessUrl(){
		 System.out.println(suiteData.getTenantDomainName());
		 String tenatDomainName=suiteData.getTenantDomainName().substring(0, suiteData.getTenantDomainName().indexOf("."));
		 System.out.println(tenatDomainName);
		 String emailLink=tenatDomainName+"-my.sharepoint.com";
		 System.out.println(emailLink);	
		 return emailLink;
		}
	 
	 public String getO365EMailUrl(){
		 return "https://outlook.office.com/owa/";
		}
	
	




	
	
	

}
