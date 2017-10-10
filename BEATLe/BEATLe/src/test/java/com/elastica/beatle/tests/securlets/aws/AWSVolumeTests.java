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
import com.amazonaws.services.ec2.model.AttachVolumeResult;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DetachVolumeResult;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.elastica.beatle.CommonTest;
import com.elastica.beatle.DateUtils;
import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;
import com.elastica.beatle.awsUtils.AWSInstance;
import com.elastica.beatle.awsUtils.AWSVolume;
import com.elastica.beatle.es.ElasticSearchLogs;
import com.elastica.beatle.logger.Logger;
import com.elastica.beatle.securlets.AWSSecurletConstants;
import com.elastica.beatle.securlets.ESQueryBuilder;

/**
 * @author anuvrath
 *
 */
public class AWSVolumeTests extends CommonTest{
	
	private AWSVolume awsVolumeObject;
	private AWSInstance awsInstanceObject;		
	private Client restClient;
	private Regions awsRegion;
	private String volumeID;
	private static final long TOTAL_VOLUME_OPERATION = 4;
	
	/**
	 * @throws Exception
	 */
	@BeforeClass
	public void initClass() throws Exception {
		awsRegion = Regions.valueOf(getAWSRegion());
		awsInstanceObject = new AWSInstance(getAccessKey(), getAccessKeySecret());
		awsVolumeObject = new AWSVolume(getAccessKey(), getAccessKeySecret());
		restClient = new Client();
	}
	
	/**
	 * @return
	 * @throws InterruptedException
	 */
	public boolean performVolumeOperations() throws InterruptedException{
		try{
			Logger.info("Starting AWS Volume Operations");
			
			Logger.info(" ");
			Logger.info("Creating instance for attaching to volume once volume created ");
			RunInstancesResult runInstancesResult = awsInstanceObject.launchAWSInstance("t2.micro", AWSTestUtils.getAMIID(awsRegion), null,null,awsRegion);
			String instanceAvailabilityZone = runInstancesResult.getReservation().getInstances().get(0).getPlacement().getAvailabilityZone();
			String instanceID = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();
			
			CreateVolumeResult result = awsVolumeObject.createVolume(new Integer(100), awsRegion, instanceAvailabilityZone);
			volumeID = result.getVolume().getVolumeId();
			if(result != null){
				String volumeState = awsVolumeObject.getVolumeState(result.getVolume().getVolumeId(), awsRegion);
				while(!"available".equalsIgnoreCase(volumeState)){
					Logger.info("Waiting for another "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+ " Seconds. So that the volume becomes available");
					AWSTestUtils.waitForIterationTime();					
					volumeState = awsVolumeObject.getVolumeState(result.getVolume().getVolumeId(), awsRegion);
				}
				
				Logger.info(" ");
				Logger.info("Performing attach operation");
				AttachVolumeResult attachResult = awsVolumeObject.attachVolume(result.getVolume().getVolumeId(), instanceID, "/dev/xvdf", awsRegion);
				String attachStatus = attachResult.getAttachment().getState();				
				while(!"in-use".equals(attachStatus)){
					Logger.info("Still volume is not attached");
					Logger.info("Waiting for another "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+" Seconds");
					AWSTestUtils.waitForIterationTime();
					attachStatus = awsVolumeObject.getVolumeState(result.getVolume().getVolumeId(), awsRegion);					
				}
				
				Logger.info(" ");
				Logger.info("Detaching instance "+instanceID+ "from volume "+result.getVolume().getVolumeId());
				DetachVolumeResult detachResult = awsVolumeObject.detachVolume(result.getVolume().getVolumeId(), awsRegion);
				String detachStatus = detachResult.getAttachment().getState();				
				while(!"available".equalsIgnoreCase(detachStatus)){
					Logger.info("Still volume is getting detached");
					Logger.info("Waiting for another "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+" Seconds");
					AWSTestUtils.waitForIterationTime();
					detachStatus = awsVolumeObject.getVolumeState(result.getVolume().getVolumeId(), awsRegion);
				}
				
				Logger.info("");
				List<String> deleteVolumeList = new ArrayList<>();
				deleteVolumeList.add(result.getVolume().getVolumeId());
				awsVolumeObject.deleteVolume(deleteVolumeList, awsRegion);
				String deleteStatus = detachResult.getAttachment().getState();
				while("deleting".equalsIgnoreCase(deleteStatus)){
					Logger.info("Still volume is getting deleted");
					Logger.info("Waiting for another "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+" Seconds");
					AWSTestUtils.waitForIterationTime();
					deleteStatus = awsVolumeObject.getVolumeState(result.getVolume().getVolumeId(), awsRegion);
				}
				
				Logger.info(" ");
				Logger.info("Terminating the instance "+ instanceID+" that was created before");
				List<String> instanceList = new ArrayList<>();
				instanceList.add(instanceID);
				TerminateInstancesResult terminatestatus = awsInstanceObject.terminateAWSInstance(instanceList, awsRegion);
				String terminatRequestStatus = awsInstanceObject.getInstanceState(terminatestatus.getTerminatingInstances().get(0).getInstanceId(), awsRegion);
				while(!"terminated".equalsIgnoreCase(terminatRequestStatus)){
					Logger.info("Terminate request status is: "+terminatRequestStatus+" So waiting for one 30 seconds till the instance terminates");
					Logger.info("Waiting for another "+TimeUnit.MILLISECONDS.toSeconds(AWSSecurletConstants.ITERATION_WAIT_TIME)+" Seconds");
					AWSTestUtils.waitForIterationTime();
					terminatRequestStatus = awsInstanceObject.getInstanceState(terminatestatus.getTerminatingInstances().get(0).getInstanceId(), awsRegion);
				}
			}
			Logger.info("All AWS Volume related operations completed");
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
	
	/**
	 * @throws Exception
	 */
	@Test(priority=1)
	public void testAWSVolumeActions() throws Exception{	
		boolean operationStatus = performVolumeOperations();
		if(operationStatus){
			Logger.info("**********************************************************");
			Logger.info("Starting AWS Volume Actions test");
			Logger.info("Description: This is the main test which will initiate all the AWS Volume actions. If everything went smooth here the remaining tests will run. Other wise this and the dependendant tests will be skipped.");		
			Logger.info("Expected: ");
			Logger.info("1) Perform all the AWS Volume related operations");
			Logger.info("2) Wait for 15 minutes");
			Logger.info("3)	Verify if the logs appeared for all 4 volume operations appeared or not.");
			Logger.info("4)	If found validate the response, else fails the test case");
			Logger.info("**********************************************************");
			Logger.info("Waiting for 7 minutes before we check for investigate logs");
			AWSTestUtils.waitForMinimumWaitTimeToCheckESLog();			
			Logger.info("**********************************************************");
			Logger.info("");
			//volumeID = "vol-9509e679";
			String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousHoursDateFromCurrentTime(1), DateUtils.getCurrentTime(),
					volumeID, suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
			for(int i= (int)AWSSecurletConstants.MINIMUM_WAITIME; i<= (int)AWSSecurletConstants.MAX_WAITTIME;i+=AWSSecurletConstants.WAIT_CLOCK){
				Logger.info("Post body:"+ payload);
				HttpResponse response = getInvestigateLog(new StringEntity(payload));
				Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
				String investigateReponse = ClientUtil.getResponseBody(response);
				Logger.info("Investigate Response: "+investigateReponse);
				Logger.info("");
				JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits"));
				if(hitsObject.getInt("total") == TOTAL_VOLUME_OPERATION && hitsObject.getJSONArray("hits").length() ==TOTAL_VOLUME_OPERATION){
					Logger.info("The number logs appeared match the number of logs expected. So passing the test case");
					Logger.info("Expected: ");
					Logger.info("Expecting "+TOTAL_VOLUME_OPERATION+" investigate logs for "+TOTAL_VOLUME_OPERATION+" AWS Volume related operation. Found "+hitsObject.getInt("total"));
					Assert.assertTrue(true);	
					break;
				}
				else if(hitsObject.getInt("total") != TOTAL_VOLUME_OPERATION && hitsObject.getJSONArray("hits").length() != TOTAL_VOLUME_OPERATION){
					if(i+AWSSecurletConstants.WAIT_CLOCK > AWSSecurletConstants.MAX_WAITTIME){						
						Logger.info("**********************************************************");
						Logger.info("Expected: ");
						Logger.info("Waited for "+ TimeUnit.MILLISECONDS.toMinutes(i)+" minutes to get Investigate logs");
						Logger.info("Expecting "+TOTAL_VOLUME_OPERATION+" investigate logs for "+TOTAL_VOLUME_OPERATION+" AWS Volume related operation. But found "+hitsObject.getInt("total"));
						Assert.assertTrue(false, "Logs didn't come after waiting for "+TimeUnit.MILLISECONDS.toMinutes(i)+" Minutes so failing the test case");						
						break;
					}					
					else if(hitsObject.getInt("total") < TOTAL_VOLUME_OPERATION && hitsObject.getJSONArray("hits").length() < TOTAL_VOLUME_OPERATION){
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
	public void testVolumeCreateOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Volume Launch event in investigate logs");
		Logger.info("Description: This test looks for Volume Launch logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the Volume:"+volumeID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				volumeID, "Amazon Web Services","Volume","Create",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForVolumeCreateLog(hitsObject,volumeID, suiteData.getUsername());
	}
	
	@Test(priority=2)
	public void testVolumeAttachOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Volume Attach event in investigate logs");
		Logger.info("Description: This test looks for Volume Attach logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the Volume:"+volumeID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				volumeID, "Amazon Web Services","Volume","Attach",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForVolumeAttachLog(hitsObject,volumeID, suiteData.getUsername());
	}
	
	@Test(priority=2)
	public void testVolumeDetachOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Volume Detach event in investigate logs");
		Logger.info("Description: This test looks for Volume Detach logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the Volume:"+volumeID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				volumeID, "Amazon Web Services","Volume","Detach",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");		
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForVolumeDetachLog(hitsObject,volumeID, suiteData.getUsername());
	}
	
	@Test(priority=2)
	public void testVolumeDeleteOperationLogs() throws Exception{
		Logger.info("**********************************************************");
		Logger.info("Testing AWS Volume Delete event in investigate logs");
		Logger.info("Description: This test looks for Volume Delete logs in investigate");
		Logger.info("NOTE: Before starting this test we have already waited for 15 mins once the operations are done in AWS");
		Logger.info("Expected: Looking for one investigate log in investigate for the Volume:"+volumeID);
		Logger.info("**********************************************************");
		String payload = new ESQueryBuilder().getSearchQueryForInvestigateDisplayLogs(DateUtils.getPreviousMinuteDateFromCurrentTime(120), DateUtils.getCurrentTime(),
				volumeID, "Amazon Web Services","Volume","Delete",suiteData.getUsername(), ClientUtil.BuidURI(suiteData.getScheme(),suiteData.getApiserverHostName(), "").toString(), suiteData.getCSRFToken(), suiteData.getSessionID());
		Logger.info("Investigate Display log API payload: "+payload);
		HttpResponse response = getInvestigateLog(new StringEntity(payload));
		Assert.assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK,"Status code doesn't match");
		String investigateReponse = ClientUtil.getResponseBody(response);
		Logger.info("Investigate Response: "+investigateReponse);
		JSONObject hitsObject = new JSONObject(ClientUtil.getJSONValue(investigateReponse, "hits")).getJSONArray("hits").getJSONObject(0).getJSONObject("_source");
		AWSTestUtils.validateInvestigateLogGenericField(hitsObject,suiteData.getUsername(),Region.getRegion(awsRegion).getName());
		AWSTestUtils.validateInvestigateLogForVolumeDeleteLog(hitsObject,volumeID, suiteData.getUsername());
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
