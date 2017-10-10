/**
 * 
 */
package com.elastica.beatle.tests.securlets.aws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.awsUtils.AWSInstance;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.AWSSecurletConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;

/**
 * @author anuvrath
 *
 */
public class AWSInstanceTests extends CommonTest{
	
	private AWSInstance awsInstanceObject;
	private static final long TOTAL_INSTANCE_OPERATIONS = 5;
	private Client restClient;
	private Regions awsRegion;
	private String instanceID;
	List<String> instanceidsList;
	RunInstancesResult runInstancesResult;
	/**
	 * @throws Exception
	 */
	@BeforeClass
	public void initClass() throws Exception {
		awsRegion = Regions.valueOf(getAWSRegion());
		awsInstanceObject = new AWSInstance(getAccessKey(), getAccessKeySecret());
		restClient = new Client();
		instanceidsList = new ArrayList<>();
	}
		
	public boolean performOneInstanceOperations() throws InterruptedException{
		try{
			Logger.info("Starting AWS Instance Operations");
			runInstancesResult = awsInstanceObject.launchAWSInstance("t2.micro", AWSTestUtils.getAMIID(awsRegion), null,null,awsRegion);			
			instanceID = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();		
			instanceidsList.add(instanceID);

			Logger.info("");
			StopInstancesResult stopResult = awsInstanceObject.stopAWSInstance(instanceidsList, awsRegion);
			String stoppedInstanceCurrentState = awsInstanceObject.getInstanceState(stopResult.getStoppingInstances().get(0).getInstanceId(), awsRegion);
			while(!"stopped".equalsIgnoreCase(stoppedInstanceCurrentState)){
				AWSTestUtils.waitForIterationTime();
				stoppedInstanceCurrentState = awsInstanceObject.getInstanceState(stopResult.getStoppingInstances().get(0).getInstanceId(), awsRegion);
			}		
			
			Logger.info("");
			StartInstancesResult startResult = awsInstanceObject.startAWSInstance(instanceidsList, awsRegion);
			String startInstanceCurrentState = awsInstanceObject.getInstanceState(startResult.getStartingInstances().get(0).getInstanceId(), awsRegion);
			while(!"running".equalsIgnoreCase(startInstanceCurrentState)){
				AWSTestUtils.waitForIterationTime();
				startInstanceCurrentState = awsInstanceObject.getInstanceState(startResult.getStartingInstances().get(0).getInstanceId(), awsRegion);
			}
			
			Logger.info("");
			awsInstanceObject.rebootAWSInstance(instanceidsList,awsRegion);
			String rebootInstanceCurrentState = awsInstanceObject.getInstanceState(stopResult.getStoppingInstances().get(0).getInstanceId(), awsRegion);
			if("running".equalsIgnoreCase(rebootInstanceCurrentState)){
				
				Logger.info("");
				TerminateInstancesResult terminatestatus = awsInstanceObject.terminateAWSInstance(instanceidsList, awsRegion);
				String terminatRequestStatus = awsInstanceObject.getInstanceState(terminatestatus.getTerminatingInstances().get(0).getInstanceId(), awsRegion);
				while(!"terminated".equalsIgnoreCase(terminatRequestStatus)){
					Logger.info("Terminate request status is: "+terminatRequestStatus+" So waiting for one 30 seconds till the instance terminates");
					AWSTestUtils.waitForIterationTime();
					terminatRequestStatus = awsInstanceObject.getInstanceState(terminatestatus.getTerminatingInstances().get(0).getInstanceId(), awsRegion);
				}
			}	
			Logger.info("All AWS Instance related operations completed");
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
	public void testAWSInstanceActions() throws Exception{		
		
		
		boolean operationStatus = performOneInstanceOperations();
		if(operationStatus){
			Logger.info("**********************************************************");
			Logger.info("Starting AWS Instance Actions test");
			Logger.info("Description: This is the main test which will initiate all the AWS Instance actions. If everything went smooth here the remaining tests will run. Other wise this and the dependendant tests will be skipped.");		
			Logger.info("Expected: ");
			Logger.info("1) Perform all the AWS Instance related operations");
			Logger.info("2) Wait for 15 minutes");
			Logger.info("3)	Verify if the logs appeared for all 5 instance operations appeared or not.");
			Logger.info("4)	If found validate the response, else fails the test case");
			Logger.info("**********************************************************");
			Logger.info("Waiting for 7 minutes before we check for investigate logs");
			AWSTestUtils.waitForMinimumWaitTimeToCheckESLog();
			Logger.info("**********************************************************");
			Logger.info("");
			String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousHoursDateFromCurrentTime(1), DateUtils.getCurrentTime(),
					instanceID, suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());									
			for(int i= (int)AWSSecurletConstants.MINIMUM_WAITIME; i<= (int)AWSSecurletConstants.MAX_WAITTIME;i+=AWSSecurletConstants.WAIT_CLOCK){
				Logger.info("Post body:"+ payload);
				HttpResponse response = getInvestigateLog(new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
				String investigateReponse = ClientUtil.getResponseBody(response);
				Logger.info("Investigate Response: "+investigateReponse);
				Logger.info("");
				JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits"));
				if(hitsObject.getInt("total") == TOTAL_INSTANCE_OPERATIONS && hitsObject.getJSONArray("hits").length() ==TOTAL_INSTANCE_OPERATIONS){
					Logger.info("The number logs appeared match the number of logs expected. So passing the test case");
					Logger.info("Expected: ");
					Logger.info("Expecting 5 investigate logs for 5 AWS Instance related operation. Found "+hitsObject.getInt("total"));
					Assert.assertTrue(true);	
					break;
				}
				else if(hitsObject.getInt("total") != TOTAL_INSTANCE_OPERATIONS && hitsObject.getJSONArray("hits").length() != TOTAL_INSTANCE_OPERATIONS){
					if(i+AWSSecurletConstants.WAIT_CLOCK > AWSSecurletConstants.MAX_WAITTIME){						
						Logger.info("**********************************************************");
						Logger.info("Expected: ");
						Logger.info("Waited for "+ TimeUnit.MILLISECONDS.toMinutes(i)+" minutes to get Investigate logs");
						Logger.info("Expecting 5 investigate logs for 5 AWS Instance related operation. But found "+hitsObject.getInt("total"));
						Assert.assertTrue(false, "Logs didn't come after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" Minutes so failing the test case");						
						break;
					}					
					else if(hitsObject.getInt("total") < TOTAL_INSTANCE_OPERATIONS && hitsObject.getJSONArray("hits").length() < TOTAL_INSTANCE_OPERATIONS){
						Logger.info("Number of Logs found are less than expected at " + TimeUnit.MILLISECONDS.toMinutes(i)+"th minutes. So waiting for another "+TimeUnit.MILLISECONDS.toMinutes(AWSSecurletConstants.WAIT_CLOCK) +" minutes to check if all logs appear");
						AWSTestUtils.waitForTwoMinBeforeRetry();
						Logger.info("Waiting in "+ TimeUnit.MILLISECONDS.toMinutes(i+AWSSecurletConstants.WAIT_CLOCK)+"th minute");
						Logger.info("**********************************************************");
					}
				}													
			}
		}
		else{
			throw new SkipException("Skipping this test as AWS operations were not successful");
		}		
	}
	
	@Test(priority=2)
	public void testAWSInstanceLaunchAction() throws Exception{		
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Instance Launch event in investigate logs");
		Logger.info("Description: This test looks for instance Launch logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the instance:"+instanceID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				instanceID, "Amazon Web Services","Instance","Launch",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		Assert.assertTrue(new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").length()==1,"Expecting one Investigate log. But found none.");
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInstanceLaunchRelatedLogs(hitsObject,runInstancesResult,suiteData.getUsername());
	}
	
	@Test(priority=2)	
	public void testAWSInstanceStopAction() throws Exception{
		
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Instance Stop event in investigate logs");
		Logger.info("Description: This test looks for instance Stop logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the instance:"+instanceID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				instanceID, "Amazon Web Services","Instance","Stop",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		Assert.assertTrue(new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").length()==1,"Expecting one Investigate log. But found none.");
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInstanceStopRelatedLogs(hitsObject,instanceID,suiteData.getUsername());
	}
	
	@Test(priority=2)
	public void testAWSInstanceStartAction() throws Exception{
		
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Instance Start event in investigate logs");
		Logger.info("Description: This test looks for instance Start logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the instance:"+instanceID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				instanceID, "Amazon Web Services","Instance","Start",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		Assert.assertTrue(new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").length()==1,"Expecting one Investigate log. But found none.");
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInstanceStartRelatedLogs(hitsObject,instanceID,suiteData.getUsername());
	}
	
	@Test(priority=2)	
	public void testAWSInstanceRebootAction() throws Exception{				
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Instance Reboot event in investigate logs");
		Logger.info("Description: This test looks for instance Reboot logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the instance:"+instanceID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				instanceID, "Amazon Web Services","Instance","Reboot",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		Assert.assertTrue(new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").length()==1,"Expecting one Investigate log. But found none.");
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInstanceRebootRelatedLogs(hitsObject,instanceID,suiteData.getUsername());
	}
	
	/**
	 * @throws Exception
	 */
	@Test(priority=2)
	public void testAWSInstanceTerminateAction() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Instance Terminate event in investigate logs");
		Logger.info("Description: This test looks for instance termination logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the instance:"+instanceID);
		Logger.info("**********************************************************");
		
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				instanceID, "Amazon Web Services","Instance","Terminate",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API Payload: ");
		Logger.info(payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		Assert.assertTrue(new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").length()==1,"Expecting one Investigate log. But found none.");
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());		
		AWSTestUtils.validateInstanceTerminateRelatedLogs(hitsObject,instanceID,suiteData.getUsername());
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