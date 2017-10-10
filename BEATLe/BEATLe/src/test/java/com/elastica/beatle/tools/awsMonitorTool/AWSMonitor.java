/**
 * 
 */
package com.elastica.beatle.tools.awsMonitorTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.elastica.beatle.awsUtils.AWSInstance;
import com.elastica.beatle.awsUtils.AWSLoadBalancer;
import com.elastica.beatle.awsUtils.AWSVolume;
import com.elastica.beatle.fileHandler.FileHandlingUtils;
import com.elastica.beatle.logger.Logger;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;

/**
 * @author anuvrath
 *
 */
public class AWSMonitor {

	private JSONArray awsAccountData;
	private String report;
	private boolean anythingFound = false;
	private List<String> instanceSkipList;
	/**
	 * @throws Exception
	 */
	@BeforeClass(alwaysRun=true)
	public void initAWSMonitor() throws Exception{
		Logger.info("*********************************************************");
		Logger.info("Initiating the AWS Monitoring suite");
		Logger.info("*********************************************************");
		awsAccountData = new JSONObject(FileHandlingUtils.readDataFromFile("/src/test/resources/MonitoringSuites/AWSAccountData/AWSAccountData.json")).getJSONArray("Accounts");
		Logger.info("Script will look for Running Instances, Volumes and Load Balancers in "+awsAccountData.length()+" accounts");
		report = "<html><head><style type=\"text/css\"> table { width: 100%; border-collapse: collapse; } .BreakWord { word-break: break-all; }"
				+ " .instruction {border-width:2px; border-style:solid; padding: 10px} tr.pass{background: #FFFFFF} td {word-wrap:break-word; padding: .3em; border: 1px #ccc solid;overflow: hidden;} "
				+ "td.fail{background: #FF0000} th { height: 50px; border: 2px #ccc solid; } thead { background: #fc9; } </style></head><body><table><thead><tr><th>TenantName</th><th>Region</th>"
				+ "<th>Resource ID</th><th>Resource Previous State</th><th>Resource Current State</th><th>Type Of Resource</th><th>Resource Launch Date</th><th>Resource Up Time(days)</th></tr></thead>";
		Logger.info("*********************************************************");
		Logger.info("Completed Initialization");
		Logger.info("*********************************************************");
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void monitorAWS() throws Exception {
		Logger.info("Starting looking into each accounts under all regions");
		Logger.info("*********************************************************");
		instanceSkipList = new ArrayList<>();
		instanceSkipList.add("i-32be05e3");
		instanceSkipList.add("i-33be05e2");
		instanceSkipList.add("i-34be05e5");
		instanceSkipList.add("i-b9bd0668");
		instanceSkipList.add("vol-876c6a94");
		instanceSkipList.add("vol-826c6a91");
		instanceSkipList.add("vol-496c6a5a");
		instanceSkipList.add("vol-ed6f69fe");
		
		for(int accountCounter = 0; accountCounter < awsAccountData.length(); accountCounter++){
			JSONObject accObject = awsAccountData.getJSONObject(accountCounter);
			AWSInstance Ec2Instance = new AWSInstance(accObject.getString("AccountAccessKey"), accObject.getString("AccountSecreat"));
			AWSVolume Ec2Volume = new AWSVolume(accObject.getString("AccountAccessKey"), accObject.getString("AccountSecreat"));
			for (Regions region : Regions.values()){			
				if(!region.getName().equalsIgnoreCase("us-gov-west-1") && !region.getName().equalsIgnoreCase("cn-north-1")){			
					Logger.info("");
					Logger.info("Looking for Running Instances for :"+accObject.getString("AccountName")+" in "+ region.getName());					
					report = lookForInstanceAndTerminate(Ec2Instance,region,accObject, report);					
					Logger.info("");
					Logger.info("Looking for Running Volumes for :"+accObject.getString("AccountName")+" in "+ region.getName());
					report = lookForVolumeAndTerminate(Ec2Volume,region,accObject, report);
					Logger.info("*********************************************************");
				}
			}
			Logger.info("*********************************************************");			
			Logger.info("");
			Logger.info("Looking for Running load balancers in :"+accObject.getString("AccountName")+" Account");
			AWSLoadBalancer ec2LoadBalancer = new AWSLoadBalancer(accObject.getString("AccountAccessKey"), accObject.getString("AccountSecreat"));
			report = lookForRunningLoadBalancers(ec2LoadBalancer,accObject, report);
			
		}		
	}
	
	@AfterMethod(alwaysRun=true)
	public void finalizeReport() throws IOException, AddressException, MessagingException, SAXException, ParserConfigurationException{
		report = report+"<table></body></html>";
		FileWriter writer = new FileWriter(new File("MonitoringReport.html"));
		writer.write(report);
		writer.close();
		if(anythingFound)
			sendEmail(report);
	}
	
	private String lookForRunningLoadBalancers(AWSLoadBalancer elasticaQACOM_LBBalancer2, JSONObject accObject, String report) throws JSONException {
		List<LoadBalancerDescription> descriptions = elasticaQACOM_LBBalancer2.listAllLoadBalancers();
		Logger.info("No of load balancers found for: "+accObject.getString("AccountName")+" is: "+descriptions.size());
		List<AWSMonitorDTO> LBData = new ArrayList<>();
		for(LoadBalancerDescription lbDescription: descriptions){
			Logger.info("LB Name: "+lbDescription.getLoadBalancerName());
			Logger.info("LB created at: "+lbDescription.getCreatedTime());
			AWSMonitorDTO data = new AWSMonitorDTO();			
			data.setresourceID(lbDescription.getLoadBalancerName());
			data.setAvailabilityZone("NA");
			data.setresourceLaunchDate(lbDescription.getCreatedTime());
			data.setresourceState("NA");
			data.setresourceType("Load Balancer");
			data.setresourceUpTime(elasticaQACOM_LBBalancer2.getLBUpTime(lbDescription.getCreatedTime()));
			if(data.getresourceUpTime()>=1)
				elasticaQACOM_LBBalancer2.deleteLoadBalancer(lbDescription.getLoadBalancerName());
			LBData.add(data);
		}
		return BuildReport(report,LBData,null, accObject, null);
	}
	
	/**
	 * @param awsVolume
	 * @param region
	 * @param accObject
	 * @param report
	 * @return
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	private String lookForVolumeAndTerminate(AWSVolume awsVolume,Regions region,JSONObject accObject,String report) throws InterruptedException, JSONException{
		List<AWSMonitorDTO> runningVolumeDTO = awsVolume.getRunningVolumeDetails(region);
		Map<String,String> afterTerminateStatus = null;
		if(!runningVolumeDTO.isEmpty()){
			Logger.info("Running Volume found... Looking if they can be terminated");
			afterTerminateStatus = terminateOldVolumes(awsVolume, runningVolumeDTO,region);
		}
		else
			Logger.info("None of the running Volumes found in "+region.getName() +" for "+accObject.getString("AccountName"));
		report  = BuildReport(report, runningVolumeDTO, region,accObject,afterTerminateStatus);
		return report;
	}
	
	/**
	 * @param awsInstance
	 * @param region
	 * @param accObject
	 * @param report
	 * @return
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	private String lookForInstanceAndTerminate(AWSInstance awsInstance,Regions region,JSONObject accObject,String report) throws InterruptedException, JSONException{
		List<AWSMonitorDTO> runningInstanceDTO = awsInstance.getRunningInstanceDetails(region);
		Map<String,String> afterTerminateStatus = new HashMap<String,String>();
		if(!runningInstanceDTO.isEmpty()){
			Logger.info("Running instances found... Looking if they can be terminated");
			TerminateInstancesResult results = terminateOldInstances(awsInstance, runningInstanceDTO,region);
			if(results!= null){
				List<InstanceStateChange> change = results.getTerminatingInstances();
				for(InstanceStateChange ichange : change){
					afterTerminateStatus.put(ichange.getInstanceId(), ichange.getCurrentState().getName());
				}
			}			
			else{				
				for(AWSMonitorDTO awsdata: runningInstanceDTO)
					afterTerminateStatus.put(awsdata.getresourceID(), awsdata.getresourceState());
			}
				
		}		
		else{
			Logger.info("None of the running instances found in "+region.getName() +" for "+accObject.getString("AccountName"));
			for(AWSMonitorDTO awsdata: runningInstanceDTO)
				afterTerminateStatus.put(awsdata.getresourceID(), awsdata.getresourceState());			
		}								
		report  = BuildReport(report, runningInstanceDTO, region, accObject,afterTerminateStatus);
		return report;
	}
	
	/**
	 * @param awsVolume
	 * @param runningVolumeDTO
	 * @param region
	 */
	private Map<String,String> terminateOldVolumes(AWSVolume awsVolume, List<AWSMonitorDTO> runningVolumeDTO, Regions region) {
		List<String> terminateInstanceIDs = new ArrayList<>();
		Map<String,String> afterTerminateStatus = new HashMap<String,String>();
		for(AWSMonitorDTO volume: runningVolumeDTO){
			if(!instanceSkipList.contains(volume.getresourceID())){
				if(volume.getresourceUpTime()>=1){
					Logger.info("Volume "+volume.getresourceID()+" is running from "+volume.getresourceUpTime()+" days");
					Logger.info("Adding to terminate List");
					terminateInstanceIDs.add(volume.getresourceID());
				}
				else
					Logger.info("Instance "+ volume.getresourceID()+" found in the skip list. So not terminating this instance.");
			}						
		}
		if(!terminateInstanceIDs.isEmpty()){
			Logger.info("Terminating All the volumes from the list");
			awsVolume.deleteVolume(terminateInstanceIDs, region);								
		}
		else{
			Logger.info("Terminate volume list is empty as all the running volumes are less than a day old... So no deletion");
		}
		for(AWSMonitorDTO volume: runningVolumeDTO){
			afterTerminateStatus.put(volume.getresourceID(), awsVolume.getVolumeState(volume.getresourceID(), region));
		}
		return afterTerminateStatus;
	}
	
	/**
	 * @param awsInstanceHandler
	 * @param runningInstanceDTO
	 * @param region
	 * @return
	 * @throws InterruptedException
	 */
	private TerminateInstancesResult terminateOldInstances(AWSInstance awsInstanceHandler,List<AWSMonitorDTO> runningInstanceDTO,Regions region) throws InterruptedException {
		List<String> terminateInstanceIDs = new ArrayList<String>();
		for(AWSMonitorDTO instance: runningInstanceDTO){
			if(!instanceSkipList.contains(instance.getresourceID())){
				if(instance.getresourceUpTime()>=1){
					Logger.info("Instance "+instance.getresourceID()+" is running from "+instance.getresourceUpTime()+" days");
					Logger.info("Adding to terminate List");				
					terminateInstanceIDs.add(instance.getresourceID());
				}
			}	
			else
				Logger.info("Instance "+ instance.getresourceID()+" found in the skip list. So not terminating this instance.");
		}		
		if(!terminateInstanceIDs.isEmpty()){
			Logger.info("Terminating instance");
			return awsInstanceHandler.terminateAWSInstance(terminateInstanceIDs, region);
		}			
		else{
			Logger.info("Terminate instance list is empty as all the running instances are less than a day old... So no deletion");
			return null;
		}			
	}
	
	
	/**
	 * @param report
	 * @param runningResourceList
	 * @param region
	 * @param accObject
	 * @param afterTerminateStatus
	 * @return
	 * @throws JSONException
	 */
	private String BuildReport(String report, List<AWSMonitorDTO> runningResourceList,Regions region,JSONObject accObject,Map<String,String> afterTerminateStatus) throws JSONException{		
		for(AWSMonitorDTO resource: runningResourceList){
			if(!instanceSkipList.contains(resource.getresourceID())){
				anythingFound = true;
		    	report = report+"<tr><td>"+accObject.getString("AccountName")+"</td>";
		    	report = report +"<td>"+resource.getAvailabilityZone()+"</td>";	    	
		    	report = report +"<td>"+resource.getresourceID()+"</td>";
		    	report = report +"<td>"+resource.getresourceState()+"</td>";
		    	
		    	if(region!= null && afterTerminateStatus != null)
		    		report = report +"<td>"+afterTerminateStatus.get(resource.getresourceID())+"</td>";
		    	else if(region == null && afterTerminateStatus == null)
		    		report = report +"<td>NA</td>";
		    	report = report +"<td>"+resource.getresourceType()+"</td>";
		    	report = report +"<td>"+resource.getresourceLaunchDate()+"</td>";
		    	report = report +"<td>"+resource.getresourceUpTime()+"</td><tr>";
			}			
	    }
		return report;		
	}
	
	/**
	 * @param content
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static void sendEmail(String content) throws AddressException, MessagingException, SAXException, IOException, ParserConfigurationException {
		Logger.info("Initiating email sender");
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.user", "zookeeper@elastica.co");
		props.put("mail.smtp.password", "TaQa&y98");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		Logger.info("EmailSender: Properties Set");
		
		Session session = Session.getDefaultInstance(props, null);		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("zookeeper@elastica.co"));		
		message.setRecipient(Message.RecipientType.TO, new InternetAddress("team-qa@elastica.co"));
		//message.setRecipient(Message.RecipientType.TO, new InternetAddress("anuvrath.joshi@elastica.co"));
		message.setSubject("QA_AWS_Alerts!!!.. Please Terminate these instances if you are not using");							
		message.setContent(content, "text/html");
		Logger.info("EmailSender: Session set");
		
		Transport transport = session.getTransport("smtp");		
		transport.connect("smtp.gmail.com", "zookeeper@elastica.co", "TaQa&y98");
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();		
		Logger.info("Email Sent to QA team");
	}	
}
