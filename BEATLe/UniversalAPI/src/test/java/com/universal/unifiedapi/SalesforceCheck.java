package com.universal.unifiedapi;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Reporter;
import org.testng.annotations.Test;


import com.universal.common.Salesforce;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.salesforce.APIVersion;
import com.universal.dtos.salesforce.Body;
import com.universal.dtos.salesforce.ChatterFile;
import com.universal.dtos.salesforce.FeedItemInput;
import com.universal.dtos.salesforce.FileShares;
import com.universal.dtos.salesforce.GroupDetail;
import com.universal.dtos.salesforce.GroupInput;
import com.universal.dtos.salesforce.Lead;
import com.universal.dtos.salesforce.LeadInput;
import com.universal.dtos.salesforce.MessageSegment;
import com.universal.dtos.salesforce.Resources;
import com.universal.dtos.salesforce.SObject;
import com.universal.dtos.salesforce.SObjectInput;


public class SalesforceCheck {
	UniversalApi universalApi;
	Salesforce sfapi;


	@Test
	public void test1() {
		try{
			UserAccount ua = new UserAccount("admin@securletbeatle.com.Sandbox6", "NBB(6GCxGlArr47", "ADMIN");
			HashMap<String, String> instanceParams = new HashMap<String, String>();
			instanceParams.put("clientId", 		"3MVG9jfQT7vUue.Eo_PTDL3Iqm_St1W8HwjV8o3HLsSiAQ_lXZwEA1fz8xMCL82sAe3YdJ1OY.CGcqCus4ehC");
	    	instanceParams.put("clientSecret",  "6136402971934082815");
	    	instanceParams.put("token", 		"6136402971934082815");
			
			
			ua.setInstanceParams(instanceParams);
			universalApi = new UniversalApi("SALESFORCE", ua);
			sfapi = universalApi.getSalesforce();
			System.out.println(sfapi.getToken());
			
						
			GroupInput gi = new GroupInput();
			gi.setName("Group Of Groups");
			gi.setVisibility("PublicAccess");
			
			GroupDetail gd = sfapi.createGroup(gi);
			Reporter.log("Group Id:" + gd.getId());
			
			FeedItemInput fin = new FeedItemInput();
			
			Body body = new Body();
			MessageSegment ms = new MessageSegment();
			ms.setText("Hello World");
			ms.setType("Text");
			ArrayList<MessageSegment> als = new ArrayList<MessageSegment>();
			als.add(ms);
			body.setMessageSegments(als);
			
			fin.setBody(body);
			fin.setFeedElementType("FeedItem");
			fin.setSubjectId(gd.getId());
			
			sfapi.createFeedElement(fin);
			
			//sfapi.deleteGroup(gd.getId());
			
//			
//			String profileId = sfapi.getChatterFreeUserProfileId();
//			Reporter.log("ProfileId:" + profileId);
			
			/*
			APIVersion[] versions = sfapi.getAvailableVersions();
			
			for( APIVersion version : versions) {
				System.out.println(version.getVersion());
			}
			
			Resources resource = sfapi.getResourcesByVersion("v34.0");
			System.out.println(resource.getChatter());
			
			sfapi.uploadFileToChatter("Hello.java", "pushh_Hello.java");
			
			GroupInput ginput = new GroupInput();
			ginput.setName("AutomatedGroup");
			ginput.setVisibility("PublicAccess");
			
			GroupDetail groupDetails = sfapi.createGroup(ginput);
			groupDetails = sfapi.getGroup(groupDetails.getId());
			System.out.println(sfapi.deleteGroup(groupDetails.getId()));
			*/
//			
//			LeadInput li = new LeadInput();
//			li.setCompany("Elastica Data Sciences India pvt ltd");
//			li.setLastName("Pushparaj - EDS India");
//			
//			Lead response = sfapi.createLead(li);
//			
//			li.setId(response.getId());
//			li.setLastName("Updated Lead Lastname");
//			
//			response = sfapi.updateLead(li);
//			
			//Lead lead = sfapi.getLead("00Q17000006iZ9SEAU");
			
			//LeadInput li = new LeadInput();
			//li.setTitle("Mr.");
			//sfapi.updateLead(li, "00Q17000006iZ9SEAU");
//			li.setLastName("Pushparaj - EDS India");
			//sfapi.deleteLead("00Q17000006iZ9SEAU");
			
			
			
//			SObjectInput sInput = new SObjectInput();
//			sInput.setName("New Account Object");
//			sfapi.createSObject("Account", sInput);
//			//System.out.println(marshall(response));
			
			/*
			//Oppurtunity creation
			SObjectInput sInput = new SObjectInput();
			sInput.setName("New Sales Opportunity");
			sInput.setStageName("Review");
			sInput.setCloseDate("2016-02-25");
			sfapi.createSObject("Opportunity", sInput);	
			*/
			
/*			
			ChatterFile cf = sfapi.uploadFileToChatter("Hello.java", "pushh_Hello2.java");
			FileShares shares = sfapi.getFileShares(cf.getId());
			
			Reporter.log("Sharing type:"+ shares.getShares().get(0).getSharingType(), true);
*/			
			//  user.setField("Alias", "abcd");
           
			/*
			SObjectInput sInput = new SObjectInput();
			sInput.setName("Sample User Profile");
			sfapi.createSObject("Profile", sInput);
			
			*/
            //Fields for user creation
            //[Username, Alias, CommunityNickname, TimeZoneSidKey, LocaleSidKey, EmailEncodingKey, ProfileId, LanguageLocaleKey]
			
//			
//			SObjectInput sInput = new SObjectInput();
//			sInput.setUsername("pushpan@gmail.com");
//			sInput.setAlias("Push");
//			sInput.setCommunityNickname("Push");
//			sInput.setTimeZoneSidKey("America/Los_Angeles");
//			sInput.setLocaleSidKey("en_GB");
//			sInput.setEmailEncodingKey("ISO-8859-1");
//			sInput.setLanguageLocaleKey("en_US");
//			sInput.setEmail("pushpan@gmail.com");
//			sInput.setProfileId("00eo0000000mCveAAE");
//			sInput.setLastName("Pushparaj");
//			sfapi.createSObject("User", sInput);
//				
			
			//ChatterFile cf = sfapi.uploadFileToChatterGroup("Hello.java", "GroupPost.java", "0D55B00000073CXSAY");
			//FileShares shares = sfapi.getFileShares(cf.getId());
			
			//Reporter.log("Sharing type:"+ shares.getShares().get(0).getSharingType(), true);
			/*
			
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setAccountId("0035B000001zzzRQAQ");
			sobjectInput.setStartDate("2016-06-01");
			
			sobjectInput.setContractTerm(6);
			
			sobjectInput.setDescription("Naya order22");
			
			//sobjectInput.setEmail("securletuser@securletbeatle.com");
			SObject sobjectResponse = sfapi.createSObject("Contract", sobjectInput);
			
			
			
			SObject contractObj = sfapi.getSObject("Contract", sobjectResponse.getId());
			SObjectInput updateInput = new SObjectInput();
			updateInput.setContractTerm(16);
			//updateInput.setStatusCode("InApproval");
			sfapi.updateSObject("Contract", updateInput, sobjectResponse.getId());
			*/
			
			/*
			String markup = "<apex:page><b>Hello World!</b></apex:page>";
			
			SObjectInput sobjectInput = new SObjectInput();
			sobjectInput.setName("QAAutomationPage");
			sobjectInput.setMasterLabel("SampleLable");
			sobjectInput.setMarkup(markup);

			
			SObject sobjectResponse = sfapi.createSObject("ApexPage", sobjectInput);
			Thread.sleep(180000);
			SObjectInput updateInput = new SObjectInput();
			updateInput.setDescription("Report for sampling in Tabular Format");

			sfapi.updateSObject("ApexPage", updateInput, sobjectResponse.getId());
			Thread.sleep(180000);
			
			sfapi.deleteSObject("ApexPage", sobjectResponse.getId());
			 */
			
			//SObjectInput sobjectInput = new SObjectInput();
			//sobjectInput.setAccountId("0015B000002Moy2");
			//sobjectInput.setStartDate("2016-06-01");
			//sobjectInput.setBody("Feed Item Body");
			//sobjectInput.setName("Chatter Group");
			//sobjectInput.setParentId("00561000000Dydt");
			
			/*
			
			SObjectInput sobjectInput = new SObjectInput();
			//sobjectInput.setAccountId("0015B000002Moy2");
			//sobjectInput.setStartDate("2016-06-01");
			sobjectInput.setBody("Feed Item Body");
			sobjectInput.setTitle("Feed Item Title");
			sobjectInput.setParentId("00561000000Dydt");*/
			//sobjectInput.setDurationInMinutes(60);
			//sobjectInput.setActivityDateTime("2016-06-07T12:00:00");
			
			//ActivityDate
			
			//sobjectInput.setEmail("securletuser@securletbeatle.com");
			//SObject sobjectResponse = sfapi.createSObject("Group", sobjectInput);
			/*
			Thread.sleep(180000);
			
			SObject contractObj = sfapi.getSObject("Event", sobjectResponse.getId());
			
			SObjectInput updateInput = new SObjectInput();
			updateInput.setDescription("All hands for QA automation group.");
			//updateInput.setStatusCode("InApproval");
			sfapi.updateSObject("Event", updateInput, sobjectResponse.getId());
			
			Thread.sleep(180000);
			
			sfapi.deleteSObject("Event", sobjectResponse.getId());*/
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
