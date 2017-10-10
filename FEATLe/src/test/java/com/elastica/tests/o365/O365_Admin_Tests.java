package com.elastica.tests.o365;
import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.elastica.common.GWCommonTest;
import com.elastica.gateway.GatewayTestConstants;
import com.elastica.listeners.Priority;
import com.elastica.logger.Logger;

public class O365_Admin_Tests extends GWCommonTest{
	Map <String, Object> expectedDataMap = new HashMap<String, Object>();
	Map<String, String>policyDataMap= new HashMap<String, String>(); 
	String fromTime=backend.getCurrentTime();
	
	@BeforeMethod()
	public void clearDataMap(){
		fromTime=backend.getCurrentTime();
		expectedDataMap.clear();
	}

	@Priority(1)
	@Test(groups ={"Regression5"})
	public void loginToCloudSocAppAndSAASApp() throws Exception {
		fromTime=backend.getCurrentTime();
		printCredentials();
		Logger.info("Started performing activities on saas app");
		login.loginCloudSocPortal(getWebDriver(), suiteData);
		o365Login.login(getWebDriver(), suiteData);
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(1)
	@Test(groups ={"REACH"})
	public void loginAppAndSAASApp() throws Exception {
		o365Login.login(getWebDriver(), suiteData);
		printCredentials();
		Logger.info("Finished login activities on cloudSoc");
	}
	
	@Priority(2)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_01_Add_User() throws Exception {
		Logger.info("Add User");
		printCredentials();
		o365HomeAction.deleteUser(getWebDriver(),GatewayTestConstants.O365_ADMIN_USER,  suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadAdminApp(getWebDriver());
		o365HomeAction.changeToOldStyle(getWebDriver());
		o365HomeAction.addUser(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER) ;

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added user " +GatewayTestConstants.O365_ADMIN_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Add User Completed");
	}
	
	@Priority(3)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_02_Change_Role() throws Exception {
		Logger.info("Role Changed");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.changeRole(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData, "Global administrator");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.changeRole(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData, "Customized administrator");
		//o365HomeAction.changeRole(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData, "User (no administrator access)");
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User modified role of user " +GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com to Global Admin"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Role Changed Completed");
	}
	
	@Priority(4)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_03_Edit_User() throws Exception {
		Logger.info("Edit User");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadAdminApp(getWebDriver());
		o365HomeAction.editUser(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User edited user " +GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Edit User Completed");
	}
	
	@Priority(4)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_03_Add_Group_To_User() throws Exception {
		Logger.info("Add Group For User");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadAdminApp(getWebDriver());
		o365HomeAction.addGroup(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, GatewayTestConstants.O365_TEST_GROUP, suiteData);

		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User shared group(s) "+GatewayTestConstants.O365_TEST_GROUP +" with " + GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com"); 
		expectedDataMap.put(GatewayTestConstants.OBJECT_NAME, "");
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Add Group For User Completed");
	}
	
	
	@Priority(5)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_04_Edit_Company_User() throws Exception {
		Logger.info("Edit Company User Profile");
		printCredentials();
		o365HomeAction.editCompany(getWebDriver(),GatewayTestConstants.O365_ADMIN_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User edited company profile"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Edit Company Completed");
	}
	
	@Priority(6)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_05_Reset_Password() throws Exception {
		Logger.info("Add User");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.loadAdminApp(getWebDriver());
		o365HomeAction.resetPassword(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User reset password for " + GatewayTestConstants.O365_ADMIN_USER + "@securletautoo365featle.com"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Add User Completed");
	}
	
	@Priority(7)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_06_Delete_User() throws Exception {
		Logger.info("Delete User");
		printCredentials();
		o365HomeAction.deleteUser(getWebDriver(),GatewayTestConstants.O365_ADMIN_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted user " +GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete User Completed");
	}
	
	@Priority(8)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_07_Restore_User() throws Exception {
		Logger.info("Delete User");
		printCredentials();
		o365HomeAction.restoreUser(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com" , suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User restored " +GatewayTestConstants.O365_ADMIN_USER  +"@securletautoo365featle.com"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete User Completed");
	}
	
	@Priority(9)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_08_Add_Contact() throws Exception {
		Logger.info("Add Contact");
		printCredentials();
		o365HomeAction.deleteContact(getWebDriver(),GatewayTestConstants.O365_ADMIN_USER, suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.addContact(getWebDriver(),GatewayTestConstants.O365_ADMIN_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added user "+ GatewayTestConstants.O365_ADMIN_USER + " " + GatewayTestConstants.O365_ADMIN_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Add Contact Completed");
	}
	
	@Priority(10)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_09_Delete_Contact() throws Exception {
		Logger.info("Delete Contact");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteContact(getWebDriver(), GatewayTestConstants.O365_ADMIN_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted " + GatewayTestConstants.O365_ADMIN_USER + " " + GatewayTestConstants.O365_ADMIN_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete User Completed");
	}
	
	@Priority(11)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_10_Create_Sharedmailbox() throws Exception {
		Logger.info("Share MailBox");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteShareMailBox(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.addSharedMailBox(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a shared mailbox " +GatewayTestConstants.O365_SHARED_MAIL_BOX_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Share MailBox Completed");
	}
	
	@Priority(12)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_11_Edit_Sharedmailbox() throws Exception {
		Logger.info("Edit Share Email Member");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.editShareMailBox(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User edited shared mail box " +GatewayTestConstants.O365_SHARED_MAIL_BOX_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Edit Share Email Completed");
	}
	
	@Priority(13)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_12_Delete_Sharedmailbox() throws Exception {
		Logger.info("Delete Shared Mail Box");
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteShareMailBox(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted shared mailbox(es) " +GatewayTestConstants.O365_SHARED_MAIL_BOX_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Shared Mail Box Completed");
	}
	
	@Priority(14)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_13_Meeting_Room() throws Exception {
		Logger.info("Create Meeting Room");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteMeetingRooms(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.addMeetingRooms(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User created a meeting room " +GatewayTestConstants.O365_SHARED_MAIL_BOX_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Create Meeting Room Completed");
	}
	
	@Priority(15)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_14_Delete_Meeting_Room() throws Exception {
		Logger.info("Delete Meeting Room");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteMeetingRooms(getWebDriver(), GatewayTestConstants.O365_SHARED_MAIL_BOX_USER, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User deleted meeting room(s) " +GatewayTestConstants.O365_SHARED_MAIL_BOX_USER); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Meeting Room Completed");
	}
	
	
	@Priority(16)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_15_External_Sharing_Skype() throws Exception {
		Logger.info("External Sharing");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.sharePublicSkype(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User allowed for using Skype with external users"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("External Sharing Completed");
	}
	
	@Priority(17)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_16_External_Share_Site() throws Exception {
		Logger.info("External Sharing");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.externalSharingSiteAllow(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User started sharing sites with anonymous external users"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("External Sharing Completed");
	}
	
	@Priority(18)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_17_No_Share_Site() throws Exception {
		Logger.info("External Sharing");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.externalSharingSiteNotAllow(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User stopped sharing sites with external users"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("External Sharing Completed");
	}
	
	@Priority(19)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_18_Calender_Share() throws Exception {
		Logger.info("Calender Sharing");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.externalSharingCalender(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User enabled public URL sharing of calendar and allowed anonymous users as well"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Calender Sharing Completed");
	}
	
	@Priority(20)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_19_Add_Domain() throws Exception {
		Logger.info("Add Domain");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteDomain(getWebDriver(), GatewayTestConstants.O365_TEST_DOMAIN, suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.addDomain(getWebDriver(), GatewayTestConstants.O365_TEST_DOMAIN, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User is adding a new domain " + GatewayTestConstants.O365_TEST_DOMAIN); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Add Domain Completed");
	}
	
	@Priority(21)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_20_Delete_Domain() throws Exception {
		Logger.info("Delete Domain");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.deleteDomain(getWebDriver(), GatewayTestConstants.O365_TEST_DOMAIN, suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User removed domain " + GatewayTestConstants.O365_TEST_DOMAIN); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Delete Domain Completed");
	}
	
	@Priority(22)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_21_SwaypublicSharing() throws Exception {
		Logger.info("Public Share");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.publicSharingPublicShare(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on Sway for organization"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Public Share Completed");
	}
	
	@Priority(23)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_22_SwayExternalShare() throws Exception {
		Logger.info("External Share");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.publicExternalShare(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on External Sway sharing"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("External Share Completed");
	}
	
	@Priority(24)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_23_Service_Settings_Mobile() throws Exception {
		Logger.info("Service Settings Mobile");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.mobileEnableService(getWebDriver(), suiteData);
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.mobileDeleteService(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User enabled Blackberry cloud services"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Service Settings Mobile Completed");
	}
	
	@Priority(25)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_24_Service_Settings_User_Software_Office() throws Exception {
		Logger.info("Service Settings User Software Office");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.userSoftwareForOfficeEnableAndDisable(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added software Office for Window"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Service Settings User Software Office Completed");
	}
	
	@Priority(26)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_25_Service_Settings_User_Software_Lync() throws Exception {
		Logger.info("Service Settings User Software Lync");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.userSoftwareForLyncEnableAndDisable(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User added software Lync for Mac"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Service Settings User Software Lync Completed");
	}
	
	@Priority(27)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_26_Service_Settings_Set_Password_Expiry() throws Exception {
		Logger.info("Set Never Password Expiry");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.setPasswordExpiry(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User set passwords to never expire"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Set Never Password Expiry Completed");
	}
	
	@Priority(28)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_27_Service_Settings_Set_Community() throws Exception {
		Logger.info("Community");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.setCommunity(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on community participation for Office 365"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap),"Logs does not match");
		Logger.info("Community Completed");
	}
	
	@Priority(29)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_28_Service_Setting_Cortana() throws Exception {
		Logger.info("Cortana");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.setCortana(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User turned on access for Cortana"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Cortana Completed");
	}
	
	@Priority(30)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_29_Service_Setting_Site_On_Overview() throws Exception {
		Logger.info("Cortana");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.siteOnOverview(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User enabled external sharing for sites"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Cortana Completed");
	}
	
	@Priority(31)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_29_Service_Setting_SkypeForBusiness_Overview() throws Exception {
		Logger.info("Cortana");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.skypeForBusinessOverview(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);;
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User enabled external sharing for skype"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Cortana Completed");
	}
	
	@Priority(32)
	@Test(groups ={"Regression5", "REACH"})
	public void o365_Test_30_Service_Setting_IntegratedApps_Overview() throws Exception {
		Logger.info("Cortana");
		printCredentials();
		o365HomeAction.loadhomePage(getWebDriver(), suiteData.getSaasAppBaseUrl());
		o365HomeAction.integratedAppOverview(getWebDriver(), suiteData);
		
		expectedDataMap.clear();
		setCommonFieldsInExpectedDataMap(expectedDataMap);
		setLocationFieldsInExpectedDataMap(expectedDataMap);
		expectedDataMap.put(GatewayTestConstants.MESSAGE, "User enabled external sharing for integrated apps"); 
		Assert.assertTrue(backend.validateLog(client, suiteData, fromTime, expectedDataMap), "Logs does not match");
		Logger.info("Cortana Completed");
	}
	
	
}