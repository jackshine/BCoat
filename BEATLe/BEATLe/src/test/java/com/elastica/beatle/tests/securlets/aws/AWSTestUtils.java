/**
 * 
 */
package com.elastica.beatle.tests.securlets.aws;

import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.elastica.beatle.securlets.AWSSecurletConstants;

/**
 * @author anuvrath
 *
 */
public class AWSTestUtils {
		
	/**
	 * @param hitsObject
	 * @throws JSONException
	 */
	public synchronized static void validateInvestigateLogGenericField(JSONObject investigateLogObject,String userName,String region) throws JSONException {				
		Assert.assertNotNull(investigateLogObject.get("City"), "City is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("facility"),"Facility is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("Country"),"Country is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("user_type"),"user_type is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("host"),"host is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("user"),"user is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("created_timestamp"),"created_timestamp is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("message"),"message is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("Activity_type"),"Activity_type is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("event_service"),"event_service is null but it shouldn't be null");			
		Assert.assertNotNull(investigateLogObject.get("severity"),"severity is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("region"),"region is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("Object_type"),"Object_type is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("longitude"),"longitude is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("user-agent"),"user-agent is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("__source"),"__source is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("location"),"location is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("latitude"),"latitude is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("inserted_timestamp"),"inserted_timestamp is null but it shouldn't be null");
		Assert.assertNotNull(investigateLogObject.get("user_name"),"user_name is null but it shouldn't be null");			
		Assert.assertFalse(investigateLogObject.getString("City").isEmpty(),"City is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("facility").isEmpty(),"facility is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("Country").isEmpty(),"Country is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("user_type").isEmpty(),"user_type is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("host").isEmpty(),"host is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("user").isEmpty(),"user is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("created_timestamp").isEmpty(),"created_timestamp is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("message").isEmpty(),"message is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("Activity_type").isEmpty(),"Activity_type is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("event_service").isEmpty(),"event_service is empty but it shouldn't be empty");			
		Assert.assertFalse(investigateLogObject.getString("severity").isEmpty(),"severity is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("region").isEmpty(),"region is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("Object_type").isEmpty(),"Object_type is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("longitude").isEmpty(),"longitude is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("user-agent").isEmpty(),"user_agent is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("__source").isEmpty(),"__source is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("location").isEmpty(),"location is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("latitude").isEmpty(),"latitude is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("inserted_timestamp").isEmpty(),"inserted_timestamp is empty but it shouldn't be empty");
		Assert.assertFalse(investigateLogObject.getString("user_name").isEmpty(),"user_name is empty but it shouldn't be empty");
		Assert.assertEquals(investigateLogObject.getString("facility"), "Amazon Web Services","Facility name doesn't match");
		Assert.assertEquals(investigateLogObject.getString("user_type"), "IAMUser", "User type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("user"), "testAccount@"+userName.split("@")[1],"user email id doesn't match");
		Assert.assertEquals(investigateLogObject.getString("__source"), "API","__source doesn't match");
		Assert.assertEquals(investigateLogObject.getString("user_name"), "testAccount","userName doesn't match");
		Assert.assertEquals(investigateLogObject.getString("severity"),"informational","Severity doesn't match");
	}
	
	/**
	 * @param investigateLogObject
	 * @throws JSONException 
	 */
	public static void validateInstanceTerminateRelatedLogs(JSONObject investigateLogObject, String instanceID,String userName) throws JSONException {
		Assert.assertEquals(investigateLogObject.getString("message"), "User 'testAccount@"+userName.split("@")[1]+"' terminated Instance","Message doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Activity_type"), "Terminate", "activity type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("event_service"), "ec2", "eventService doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Object_type"),"Instance","Object type doesn't match");
		JSONObject requestItem = new JSONObject(investigateLogObject.getString("request").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(requestItem.getString("instanceId"), instanceID,"Instance Request id doesn't match");
		JSONObject responseItem = new JSONObject(investigateLogObject.getString("response").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(responseItem.getString("instanceId"), instanceID,"Instance Response id doesn't match");
		JSONObject currentStateObj = responseItem.getJSONObject("currentState");
		Assert.assertEquals(currentStateObj.getInt("code"), 32,"Current state code doesn't match");
		Assert.assertEquals(currentStateObj.getString("name"), "shutting-down","Current state name doesn't match");
		JSONObject previousStateObj = responseItem.getJSONObject("previousState");
		Assert.assertEquals(previousStateObj.getInt("code"), 16,"Previous state code doesn't match");
		Assert.assertEquals(previousStateObj.getString("name"), "running","Previous state name doesn't match");		
	}

	/**
	 * @param jsonObject
	 * @param instanceID
	 * @throws JSONException 
	 */
	public static void validateInstanceRebootRelatedLogs(JSONObject investigateLogObject,String instanceID,String userName) throws JSONException {
		Assert.assertEquals(investigateLogObject.getString("message"), "User 'testAccount@"+userName.split("@")[1]+"' rebooted Instance");
		Assert.assertEquals(investigateLogObject.getString("Activity_type"), "Reboot", "activity type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Object_type"),"Instance","Object type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("event_service"), "ec2", "eventService doesn't match");
		JSONObject requestItem = new JSONObject(investigateLogObject.getString("request").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(requestItem.getString("instanceId"), instanceID,"Instance Request id doesn't match");
		JSONObject responseItem = new JSONObject((investigateLogObject.getString("response").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getBoolean("_return"), true,"Response doesn't match");
	}

	/**
	 * @param jsonObject
	 * @param instanceID
	 * @throws JSONException 
	 */
	public static void validateInstanceStartRelatedLogs(JSONObject investigateLogObject,String instanceID,String userName) throws JSONException {
		Assert.assertEquals(investigateLogObject.getString("message"), "User 'testAccount@"+userName.split("@")[1]+"' started Instance");
		Assert.assertEquals(investigateLogObject.getString("Activity_type"), "Start", "activity type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("event_service"), "ec2", "eventService doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Object_type"),"Instance","Object type doesn't match");
		JSONObject requestItem = new JSONObject(investigateLogObject.getString("request").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(requestItem.getString("instanceId"), instanceID,"Instance Request id doesn't match");
		JSONObject responseItem = new JSONObject(investigateLogObject.getString("response").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(responseItem.getString("instanceId"), instanceID,"Instance Response id doesn't match");
		JSONObject currentStateObj = responseItem.getJSONObject("currentState");
		Assert.assertEquals(currentStateObj.getInt("code"), 0,"Current state code doesn't match");
		Assert.assertEquals(currentStateObj.getString("name"), "pending","Current state name doesn't match");
		JSONObject previousStateObj = responseItem.getJSONObject("previousState");
		Assert.assertEquals(previousStateObj.getInt("code"), 80,"Previous state code doesn't match");
		Assert.assertEquals(previousStateObj.getString("name"), "stopped","Previous state name doesn't match");
	}

	/**
	 * @param jsonObject
	 * @param instanceID
	 * @throws JSONException 
	 */
	public static void validateInstanceStopRelatedLogs(JSONObject investigateLogObject,String instanceID,String userName) throws JSONException {
		Assert.assertEquals(investigateLogObject.getString("message"), "User 'testAccount@"+userName.split("@")[1]+"' stopped Instance");
		Assert.assertEquals(investigateLogObject.getString("Activity_type"), "Stop", "activity type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("event_service"), "ec2", "eventService doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Object_type"),"Instance","Object type doesn't match");
		JSONObject requestItem = new JSONObject(investigateLogObject.getString("request").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(requestItem.getString("instanceId"), instanceID,"Instance Request id doesn't match");
		JSONObject responseItem = new JSONObject(investigateLogObject.getString("response").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Assert.assertEquals(responseItem.getString("instanceId"), instanceID,"Instance Response id doesn't match");
		JSONObject currentStateObj = responseItem.getJSONObject("currentState");
		Assert.assertEquals(currentStateObj.getInt("code"), 64,"Current state code doesn't match");
		Assert.assertEquals(currentStateObj.getString("name"), "stopping","Current state name doesn't match");
		JSONObject previousStateObj = responseItem.getJSONObject("previousState");
		Assert.assertEquals(previousStateObj.getInt("code"), 16,"Previous state code doesn't match");
		Assert.assertEquals(previousStateObj.getString("name"), "running","Previous state name doesn't match");		
	}

	/**
	 * @param jsonObject
	 * @param runInstancesResult
	 * @throws JSONException
	 * 
	 */
	public static void validateInstanceLaunchRelatedLogs(JSONObject investigateLogObject,RunInstancesResult runInstancesResult,String userName) throws JSONException {		
		Assert.assertEquals(investigateLogObject.getString("message"), "User 'testAccount@"+userName.split("@")[1]+"' ran Instance");
		Assert.assertEquals(investigateLogObject.getString("Activity_type"), "Launch", "activity type doesn't match");
		Assert.assertEquals(investigateLogObject.getString("event_service"), "ec2", "eventService doesn't match");
		Assert.assertEquals(investigateLogObject.getString("Object_type"),"Instance","Object type doesn't match");		
		JSONObject responseItemObj = new JSONObject(investigateLogObject.getString("response").replaceAll("\"", "\\\"")).getJSONObject("instancesSet").getJSONArray("items").getJSONObject(0);
		Instance instance = runInstancesResult.getReservation().getInstances().get(0);
		Assert.assertEquals(responseItemObj.getString("vpcId"), instance.getVpcId(),"vpcId doesn't match");		
		Assert.assertEquals(responseItemObj.getString("instanceId"), instance.getInstanceId(),"Instance ID doesn't match");
		Assert.assertEquals(responseItemObj.getString("imageId"), instance.getImageId(),"image id doesn't match");
		Assert.assertEquals(responseItemObj.getString("clientToken"), instance.getClientToken(),"client token doesn't match");
		Assert.assertEquals(responseItemObj.getString("subnetId"), instance.getSubnetId(),"subnetId doesn't match");
		Assert.assertEquals(Integer.valueOf(responseItemObj.getInt("amiLaunchIndex")), instance.getAmiLaunchIndex(),"ami launch index doesn't match");		
		Assert.assertEquals(responseItemObj.getString("instanceType"), instance.getInstanceType(),"instanceType doesn't match");
		Assert.assertEquals(responseItemObj.getString("privateIpAddress"), instance.getPrivateIpAddress(),"private ip address doesn't match");
		Assert.assertEquals(responseItemObj.getString("virtualizationType"), instance.getVirtualizationType(),"virtualization type doesn't match");
		Assert.assertEquals(responseItemObj.getString("privateDnsName"), instance.getPrivateDnsName(),"Private DNS name doesn't match");
		Assert.assertEquals(Boolean.valueOf(responseItemObj.getBoolean("sourceDestCheck")), instance.getSourceDestCheck(),"source dest check doesn't match");		
		Assert.assertEquals(Boolean.valueOf(responseItemObj.getBoolean("ebsOptimized")),instance.getEbsOptimized(),"ebs optimized doesn't match");				
		Assert.assertEquals(responseItemObj.getString("architecture"),instance.getArchitecture(),"architecture doesn't match");
		Assert.assertEquals(responseItemObj.getString("hypervisor"),instance.getHypervisor(),"hypervisor doesn't match");
		Assert.assertEquals(responseItemObj.getString("rootDeviceType"),instance.getRootDeviceType(),"root device type doesn't match");
		Assert.assertEquals(responseItemObj.getString("rootDeviceName"),instance.getRootDeviceName(),"root deviceName doesn't match");		
		JSONObject monitoring = responseItemObj.getJSONObject("monitoring");
		Assert.assertEquals(monitoring.getString("state"),instance.getMonitoring().getState(),"Monitoring state doesn't match");
		JSONObject stateReason = responseItemObj.getJSONObject("stateReason");
		Assert.assertEquals(stateReason.getString("code"),instance.getStateReason().getCode(),"stateReason code doesn't match");
		Assert.assertEquals(stateReason.getString("message"),instance.getStateReason().getMessage(),"stateReason message doesn't match");
		JSONObject placement = responseItemObj.getJSONObject("placement");
		Assert.assertEquals(placement.getString("tenancy"), instance.getPlacement().getTenancy(),"Tenancy doesn't match");
		Assert.assertEquals(placement.getString("availabilityZone"), instance.getPlacement().getAvailabilityZone(),"Availability zone doesn't match");		
		JSONObject instantState = responseItemObj.getJSONObject("instanceState");
		Assert.assertEquals(Integer.valueOf(instantState.getInt("code")), instance.getState().getCode(),"Instant Code doesn't match");		
		Assert.assertEquals(instantState.getString("name"), instance.getState().getName(),"Instant state name doesn't match");
	}
	
	/**
	 * @param hitsObject
	 * @param volumeID
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForVolumeCreateLog(JSONObject hitsObject, String volumeID, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' created Volume","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Create", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Volume","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("response").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("volumeId"),volumeID,"Volume id doesn't match");				
	}
	
	/**
	 * @param hitsObject
	 * @param volumeID
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForVolumeAttachLog(JSONObject hitsObject, String volumeID, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' attached Volume","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Attach", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Volume","Object type doesn't match");
		JSONObject requestItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(requestItem.getString("volumeId"),volumeID,"Volume id doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("response").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("volumeId"),volumeID,"Volume id doesn't match");				
	}
	
	/**
	 * @param hitsObject
	 * @param volumeID
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForVolumeDetachLog(JSONObject hitsObject, String volumeID, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' detached Volume","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Detach", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Volume","Object type doesn't match");
		JSONObject requestItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(requestItem.getString("volumeId"),volumeID,"Volume id doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("response").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("volumeId"),volumeID,"Volume id doesn't match");				
	}
	
	/**
	 * @param hitsObject
	 * @param volumeID
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForVolumeDeleteLog(JSONObject hitsObject, String volumeID, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' deleted Volume","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Delete", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "ec2", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Volume","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("volumeId"),volumeID,"Volume id doesn't match");
	}

	/**
	 * @param awsRegion
	 * @return
	 */
	public synchronized static String getAMIID(Regions awsRegion) {
		String regionName = Region.getRegion(awsRegion).getName();
		if("us-east-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.US_EAST_1.getAMIId();
		if("us-west-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.US_WEST_1.getAMIId();
		if("us-west-2".equals(regionName))
			return AWSSecurletConstants.AMIEnum.US_WEST_2.getAMIId();
		if("eu-west-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.EU_WEST_1.getAMIId();
		if("eu-central-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.EU_CENTRAL_1.getAMIId();
		if("ap-southeast-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.AP_SOUTHEAST_1.getAMIId();
		if("ap-southeast-2".equals(regionName))
			return AWSSecurletConstants.AMIEnum.AP_SOUTHEAST_2.getAMIId();
		if("ap-northeast-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.AP_NORTHEAST_1.getAMIId();
		if("sa-east-1".equals(regionName))
			return AWSSecurletConstants.AMIEnum.SA_EAST_1.getAMIId();
		else
			return null;
	}

	/**
	 * @return
	 */
	public static String generateAWSRandomNames() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @param hitsObject
	 * @param userName
	 * @param username2
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForUserDeleteLog(JSONObject hitsObject, String userName, String tenantUserName) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+tenantUserName.split("@")[1]+"' deleted User","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Delete", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"User","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("userName"),userName,"User name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param userName
	 * @param username2
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForUserCreateLog(JSONObject hitsObject, String userName, String tenantUserName) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+tenantUserName.split("@")[1]+"' created User","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Create", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"User","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("userName"),userName,"User name id doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param roleName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForRoleCreateLog(JSONObject hitsObject, String roleName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' created Role","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Create", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Role","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("roleName"),roleName,"Role name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param roleName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForRoleDeleteLog(JSONObject hitsObject, String roleName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' deleted Role","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Delete", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Role","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("roleName"),roleName,"Role name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param groupName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupDeleteLog(JSONObject hitsObject, String groupName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' deleted Group","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Delete", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("groupName"),groupName,"Group name doesn't match");
		
	}

	/**
	 * @param hitsObject
	 * @param groupName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupUpdateLog(JSONObject hitsObject, String groupName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' updated Group","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Update", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group","Object type doesn't match");
		JSONObject responseItem = new JSONObject((hitsObject.getString("request").replaceAll("\"", "\\\"")));
		Assert.assertEquals(responseItem.getString("groupName"),groupName,"Group name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param groupName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupCreateLog(JSONObject hitsObject, String groupName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' created Group","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Create", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group","Object type doesn't match");
		String responseItemObj = new JSONObject(hitsObject.getString("request").replaceAll("\"", "\\\"")).getString("groupName");
		Assert.assertEquals(responseItemObj,groupName,"Group name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param policyName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupPolicyDeleteLog(JSONObject hitsObject, String policyName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' deleted Group Policy","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Delete", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group Policy","Object type doesn't match");
		String responseItemObj = new JSONObject(hitsObject.getString("request").replaceAll("\"", "\\\"")).getString("groupName");
		Assert.assertEquals(responseItemObj,policyName,"Group policy name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param policyName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupPolicyAttachLog(JSONObject hitsObject, String policyName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' attached Group Policy","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Attach", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group Policy","Object type doesn't match");
		String responseItemObj = new JSONObject(hitsObject.getString("request").replaceAll("\"", "\\\"")).getString("groupName");
		Assert.assertEquals(responseItemObj,policyName,"Group policy name doesn't match");
	}

	/**
	 * @param hitsObject
	 * @param policyName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupPolicyDetachLog(JSONObject hitsObject, String policyName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' detached Group Policy","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Detach", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group Policy","Object type doesn't match");
		String responseItemObj = new JSONObject(hitsObject.getString("request").replaceAll("\"", "\\\"")).getString("groupName");
		Assert.assertEquals(responseItemObj,policyName,"Group policy name doesn't match");	
	}

	/**
	 * @param hitsObject
	 * @param policyName
	 * @param username
	 * @throws JSONException 
	 */
	public static void validateInvestigateLogForGroupPolicyPutLog(JSONObject hitsObject, String policyName, String username) throws JSONException {
		Assert.assertEquals(hitsObject.getString("message"), "User 'testAccount@"+username.split("@")[1]+"' put Group Policy","Message doesn't match");
		Assert.assertEquals(hitsObject.getString("Activity_type"), "Put", "activity type doesn't match");
		Assert.assertEquals(hitsObject.getString("event_service"), "iam", "eventService doesn't match");
		Assert.assertEquals(hitsObject.getString("Object_type"),"Group Policy","Object type doesn't match");
		String responseItemObj = new JSONObject(hitsObject.getString("request").replaceAll("\"", "\\\"")).getString("groupName");
		Assert.assertEquals(responseItemObj,policyName,"Group policy name doesn't match");
	}
	
	/**
	 * @throws InterruptedException 
	 * 
	 */
	public static void waitForIterationTime() throws InterruptedException{
		Thread.sleep(AWSSecurletConstants.ITERATION_WAIT_TIME);
	}
	
	/**
	 * @throws InterruptedException
	 */
	public static void waitForMinimumWaitTimeToCheckESLog() throws InterruptedException{
		Thread.sleep(AWSSecurletConstants.MINIMUM_WAITIME);
	}
	
	/**
	 * @throws InterruptedException
	 */
	public static void waitForTwoMinBeforeRetry() throws InterruptedException{
		Thread.sleep(AWSSecurletConstants.WAIT_CLOCK);
	}
}