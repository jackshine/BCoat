/**
 * 
 */
package com.elastica.beatle.tests.securlets.aws;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.awsUtils.AWSUser;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.AWSSecurletConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;

/**
 * @author anuvrath
 *
 */
public class AWSUserTest extends CommonTest{	
	
	private static final long TOTAL_USER_OPERATION = 2;
	private AWSUser awsUserObject;
	private Regions awsRegion;
	private Client restClient;
	private String userName; 
	
	@BeforeClass(alwaysRun=true)
	public void initClass() throws Exception {
		restClient = new Client();
		awsRegion = Regions.valueOf(getAWSRegion());		
		awsUserObject = new AWSUser(getAccessKey(), getAccessKeySecret());
	}
	
	private boolean performUserOperations() throws InterruptedException{
		try{
			Logger.info("Starting AWS Group Operations");
			
			Logger.info(" ");			
			userName = AWSTestUtils.generateAWSRandomNames();
			Logger.info("Creating new user with username: "+userName);
			awsUserObject.createAWSUser(userName);
			
			Logger.info(" ");
			Logger.info("Waiting for "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+" Seconds");
			AWSTestUtils.waitForIterationTime();
			Logger.info(" ");
			Logger.info("Deleting user: "+ userName);
			awsUserObject.deleteAWSUser(userName);
			Logger.info("All AWS User related operations completed");
			Logger.info("**********************************************************");
			Logger.info("");
			
			return true;
		}catch(AmazonServiceException exception){
			Logger.info("#########################################################################");
			Logger.info("############ Exception occured while doing the AWS Operation ############");
			Logger.info("Request ID: "+exception.getRequestId());		
			Logger.info("Service Name: "+exception.getServiceName());
			Logger.info("Message: "+exception.getMessage());
			Logger.info("Status Code: "+exception.getStatusCode());		
			Logger.info("Error Type: "+exception.getErrorType());
			Logger.info("Error Code: "+exception.getErrorCode());
			Logger.info("#########################################################################");
			return false;
		}
	}
	
	@Test(priority=1)
	public void testAWSUserActionsTest() throws Exception{
		boolean operationStatus = performUserOperations();
		if(operationStatus){
			Logger.info("");
			Logger.info("**********************************************************");
			Logger.info("Starting AWS User Actions test");
			Logger.info("Description: This is the main test which will initiate all the AWS User actions. If everything went smooth here the remaining tests will run. Other wise this and the dependendant tests will be skipped.");		
			Logger.info("Expected: ");
			Logger.info("1) Perform all the AWS User related operations");
			Logger.info("2) Wait for 15 minutes");
			Logger.info("3)	Verify if the logs appeared for all 3 User operations appeared or not.");
			Logger.info("4)	If found validate the response, else fails the test case");
			Logger.info("**********************************************************");
			Logger.info("Waiting for 7 minutes before we check for investigate logs");
			AWSTestUtils.waitForMinimumWaitTimeToCheckESLog();
			Logger.info("**********************************************************");
			Logger.info("");
			
			String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousHoursDateFromCurrentTime(1), DateUtils.getCurrentTime(),
					userName, suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
			for(int i= (int)AWSSecurletConstants.MINIMUM_WAITIME; i<= (int)AWSSecurletConstants.MAX_WAITTIME;i+=AWSSecurletConstants.WAIT_CLOCK){
				Logger.info("Post body:"+ payload);
				HttpResponse response = getInvestigateLog(new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
				String investigateReponse = ClientUtil.getResponseBody(response);
				Logger.info("Investigate Response: "+investigateReponse);
				Logger.info("");
				JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits"));
				if(hitsObject.getInt("total") == TOTAL_USER_OPERATION && hitsObject.getJSONArray("hits").length() ==TOTAL_USER_OPERATION){
					Logger.info("The number of logs appeared match the number of logs expected. So passing the test case");
					Logger.info("Expected: ");
					Logger.info("Expecting "+TOTAL_USER_OPERATION+" investigate logs for "+TOTAL_USER_OPERATION+" AWS User related operation. Found "+hitsObject.getInt("total"));
					Assert.assertTrue(true);	
					break;
				}
				else if(hitsObject.getInt("total") != TOTAL_USER_OPERATION && hitsObject.getJSONArray("hits").length() != TOTAL_USER_OPERATION){
					if(i+AWSSecurletConstants.WAIT_CLOCK > AWSSecurletConstants.MAX_WAITTIME){						
						Logger.info("**********************************************************");
						Logger.info("Expected: ");
						Logger.info("Waited for "+ TimeUnit.MILLISECONDS.toMinutes(i)+" minutes to get Investigate logs");
						Logger.info("Expecting "+TOTAL_USER_OPERATION+" investigate logs for "+TOTAL_USER_OPERATION+" AWS User related operation. But found "+hitsObject.getInt("total"));
						Assert.assertTrue(false, "Logs didn't come after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" Minutes so failing the test case");						
						break;
					}					
					else if(hitsObject.getInt("total") < TOTAL_USER_OPERATION && hitsObject.getJSONArray("hits").length() < TOTAL_USER_OPERATION){
						Logger.info("Number of Logs found are less than expected at " + TimeUnit.MILLISECONDS.toMinutes(i)+"th minutes. So waiting for another "+TimeUnit.MILLISECONDS.toMinutes(AWSSecurletConstants.WAIT_CLOCK) +" minutes to check if all logs appear");
						AWSTestUtils.waitForTwoMinBeforeRetry();						
						Logger.info("Waiting in "+ TimeUnit.MILLISECONDS.toMinutes(i+AWSSecurletConstants.WAIT_CLOCK)+"th minute");
						Logger.info("**********************************************************");
					}
				}
			}
		}else{
			throw new SkipException("Skipping this test as AWS operations were not successful");
		}
	}
	
	@Test(priority=2)
	public void testUserCreateOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS User Update event in investigate logs");
		Logger.info("Description: This test looks for User Create logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the User:"+userName);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				userName, "Amazon Web Services","User","Create",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForUserCreateLog(hitsObject,userName, suiteData.getUsername());
	}
	
	@Test(priority=2)
	public void testUserDeleteOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS User Delete event in investigate logs");
		Logger.info("Description: This test looks for User Delete logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the User:"+userName);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				userName, "Amazon Web Services","User","Delete",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForUserDeleteLog(hitsObject,userName, suiteData.getUsername());
	}
	
	/**
	 * @param entity
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public synchronized HttpResponse getInvestigateLog(StringEntity entity) throws IOException, Exception{
		Logger.info("Looking for investigate logs");
		return new ElasticSearchLogs().getDisplayLogs(restClient, getHeaders(), suiteData.getApiserverHostName(), entity);
	}
}
