package com.elastica.beatle.tests.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.detect.dto.DetectAttributeDto;
import com.elastica.beatle.detect.dto.IOI_Code;
import com.elastica.beatle.detect.dto.InputBean;
import com.elastica.beatle.es.ElasticSearchLogs;

public class DetectKafkaPollerTests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	String user = null;

	@DataProvider()
	public Object[][] DataProvider()  {
		Object[][] files = new Object[1][];

		files[0] = new Object[] { "largeUploadActivities_25K", IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString() };
		files[0] = new Object[] { "largeUploadActivities_50K", IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString() };

		return files;
	}
 
	@Test(dataProvider = "DataProvider",  description = "This test operates on real API data, and generates BBI large upload incidents.")
	public void kafkaPollerTests(Method method, String fileName,String ioi_Code) throws Exception {
		
		AttributeBean  attributeBean = new AttributeBean(20, 2,true);
		
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name:::::  Confidence"+attributeBean.getConfidence()+"  ::::: IOI Code:::::: "+ioi_Code , true);
		Reporter.log(" ");
		Reporter.log("       #####################################");
		
		InputBean inputBean = createFileUpdateDataforLargeBBI2(fileName, attributeBean);
		
		Log(method, attributeBean,inputBean, ioi_Code);
		
		user = inputBean.getUser();
		scpAndInjectActivities(method, inputBean);
		
		//TODO: Add validation of Injected Activities and Count from Investigate Page.
		
		Reporter.log("Execution Completed - Test Case Name: " + method.getName(), true);
	}
	
	private void Log(Method method, AttributeBean attributeBean, InputBean inputBean, String ioi_Code) {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("This Test is to validate " + method.getName().replace("_Tests", "")+",   And preferences for this test are  preference enabled???:::::: "+attributeBean.isEnabled() +" confidence:: "
				+ ""+attributeBean.getConfidence()+" importance:::: "+attributeBean.getImportance()+" ::::::::: ioi code :: "+ioi_Code, true);
		Reporter.log(" ");
		Reporter.log("Test details :::::::  userName::  " + inputBean.getUserName()+"  user::::: "+inputBean.getUser()+" testId:::  "+inputBean.getTestId(), true);
		Reporter.log(" ");
		Reporter.log("To verify manually login to "+suiteData.getEnvironmentName()+"  with user:: "+suiteData.getUsername()+" and pass word for this user is :: "+suiteData.getPassword(),true);
		Reporter.log(" ");
		Reporter.log("        #####################################");
	}
}
