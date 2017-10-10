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

public class DetectBBIProfileTests extends DetectUtils {
	
	private static final String OBJECTS = "detect_attributes";
	String user = null;
	
	
	
	@DataProvider()
    public Object[][] profilesDp()  {
	 Object[][] atrributesDP = new Object[8][];
		
			atrributesDP[0] = new Object[] { "largeUploadProfile", IOI_Code.ANOMALOUSLY_LARGE_UPLOAD.toString() };
			atrributesDP[1] = new Object[] { "largeDownloadProfile", IOI_Code.ANOMALOUSLY_LARGE_DOWNLOAD.toString() };
			atrributesDP[2] = new Object[] { "largeShareProfile", IOI_Code.ANOMALOUSLY_LARGE_SHARING.toString() };
			atrributesDP[3] = new Object[] { "largeUploadASProfile", IOI_Code.ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC.toString() };
			atrributesDP[4] = new Object[] { "frequentDeleteProfile", IOI_Code.ANOMALOUSLY_FREQUENT_DELETES.toString() };
			atrributesDP[5] = new Object[] { "frequentShareProfile", IOI_Code.ANOMALOUSLY_FREQUENT_SHARING.toString() };
			atrributesDP[6] = new Object[] { "frequentUserActionsProfile", IOI_Code.ANOMALOUSLY_FREQUENT_SESSIONS.toString() };
			atrributesDP[7] = new Object[] { "largeDeleteProfile", IOI_Code.ANOMALOUSLY_LARGE_DELETES.toString() };
			
		return atrributesDP;
	}
 
	
	@DataProvider()
	    public Object[][] AttributesDp()  {
		 List<AttributeBean> list = 	getAttributeList();
		 Object[][] atrributesDP = new Object[list.size()][];
			
			
			for(int i=0; i<atrributesDP.length;i++){
				atrributesDP[i] = new Object[] {list.get(i)};
			}
				
			return atrributesDP;
		}
	 
	
	public static Object[][] combine(Object[][] a1, Object[][] a2){
        List<Object[]> objectCodesList = new LinkedList<Object[]>();
        for(Object[] o : a1){
            for(Object[] o2 : a2){
            	objectCodesList.add(concatAll(o, o2));
            }
        }
         return objectCodesList.toArray(new Object[0][0]);
    }
	
	 @SafeVarargs
	    public static <T> T[] concatAll(T[] first, T[]... rest) {
	     //calculate the total length of the final object array after the concat    
	      int totalLength = first.length;
	      for (T[] array : rest) {
	        totalLength += array.length;
	      }
	      //copy the first array to result array and then copy each array completely to result
	      T[] result = Arrays.copyOf(first, totalLength);
	      int offset = first.length;
	      for (T[] array : rest) {
	        System.arraycopy(array, 0, result, offset, array.length);
	        offset += array.length;
	      }

	      return result;
	    }


	@DataProvider()
    public Object[][] DataProvider()  {
			
		return combine(profilesDp(), AttributesDp());
	}
	
	 
	 protected static List<AttributeBean> getAttributeList() {
			
			List<AttributeBean> list = new ArrayList<>();
			
			AttributeBean  ab1 = new  AttributeBean(10, 1,true);
			AttributeBean  ab2 = new AttributeBean(20, 2,true);
			AttributeBean  ab3 = new AttributeBean(30, 3,true);
			AttributeBean  ab4 = new AttributeBean(40, 3,true);
			AttributeBean  ab5 = new AttributeBean(50, 4,true);
			AttributeBean  ab6 = new AttributeBean(60, 2,true);
			AttributeBean  ab7 = new AttributeBean(70, 3,true);
			AttributeBean  ab8 = new AttributeBean(80, 3,true);
			AttributeBean  ab9 = new  AttributeBean(90, 4,true);
			AttributeBean  ab14 = new AttributeBean(10, 4,false);
			
				list.add(ab1);
				list.add(ab2);
				list.add(ab3);
				list.add(ab4);
				list.add(ab5);
				list.add(ab6);
				list.add(ab7);
				list.add(ab8);
				list.add(ab9);
				list.add(ab14);
			
			
			System.out.println("list of attributes :::: "+list);
			
			return list;
		}

	
	
	
	@Test(dataProvider = "DataProvider",  description = "This test operates on real API data, and generates BBI large upload incidents.")
	public void ProfileCreationTests(Method method, String fileName,String ioi_Code, AttributeBean attributeBean) throws Exception {
		Reporter.log("       #####################################");
		Reporter.log(" ");
		Reporter.log("Execution Started - Test Case Name:::::  Confidence"+attributeBean.getConfidence()+"  ::::: IOI Code:::::: "+ioi_Code , true);
		Reporter.log(" ");
		Reporter.log("       #####################################");
		InputBean inputBean = createFileUpdateDataforLargeBBI1(fileName, attributeBean);
		
		Log(method, attributeBean,inputBean, ioi_Code);
		
		HttpResponse resp = getDetectAttributes();
		String responseBody = getResponseBody(resp);
		org.json.JSONArray getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
		
		DetectAttributeDto detectAttributeDto = new DetectAttributeDto();
		boolean enabled = attributeBean.isEnabled();
		updateBBIAttributes(enabled, attributeBean, getResponseArray, detectAttributeDto, ioi_Code);
		Reporter.log("updated preferences ::::::     " + attributeBean.toString(), true);
		
		user = inputBean.getUser();
		scpActivityLogsAndValidate(method, inputBean);
		
		HashSet<String> expIncidents = new HashSet<String>();
		expIncidents.add(ioi_Code);

		if(enabled){
			boolean abortTest = false;
			resp = getDetectAttributes();
			responseBody = getResponseBody(resp);
			getResponseArray = new JSONObject(responseBody).getJSONArray(OBJECTS);
			for (int index = 0; index < getResponseArray.length(); index++) {
				JSONObject attributeObject = getResponseArray.getJSONObject(index);
				if (ioi_Code.toString().equals((String) attributeObject.get(NAME))) {
					if(attributeObject.get("enabled").toString().equals("false")){
						Reporter.log("");
						Reporter.log("preference got changed in between test excetuion so terminating validation::::::", true);
						Reporter.log("");
						abortTest=true;
						throw new SkipException("preference got changed in between test excetuion so skipping test");
					}
				}
			}
		if (!abortTest) {
			verifyProfile(inputBean.getUser(), ioi_Code);
		}
	}else{
		verifyNoProfile(inputBean.getUser(), ioi_Code);
		}
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

	
	
	
	@AfterMethod
	public void tearDown(ITestResult results) throws Exception {
		System.out.println("Cleaning up Resources");
		
		HttpResponse response;
		String responseBody;
		ElasticSearchLogs esLogs = new ElasticSearchLogs();
		
		if (results.isSuccess() && user != null) {
			
			response = esLogs.clearThreatscore(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearState(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);

			response = esLogs.clearDetectProfile(restClient, user, suiteData.getTenantName());
			responseBody = getResponseBody(response);
			System.out.println(responseBody);
			
			user = null;
		} else {
			System.out.println("No Cleanup Required - I am not cleaning up data to debug if needed!");
		}
		
		System.out.println("Cleaning up Resources - COMPLETED");
	}

}
