package com.elastica.beatle.tests.gateway;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import com.elastica.beatle.gateway.CommonConfiguration;
import com.elastica.beatle.gateway.LogValidator;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;


/*******************Author**************
 * 
 * @author mohd afjal
 *
 */

public class O365EpdTest extends CommonConfiguration {

	String currentTimeInJodaFormat;
	GWForensicSearchResults fsr;
	ArrayList<String> messages = new ArrayList<String>();
	ArrayList<String> objectTypeList = new ArrayList<String>();
	ArrayList<String> objectNameList = new ArrayList<String>();
	ArrayList<String> severityList = new ArrayList<String>();
	LogValidator logValidator;
	String userLitral="User";
	Map <String, String> data = new HashMap<String, String>();
	
	/******************************************************************
	 * Validate the login,logout and invalid login functionality in Box
	 * @param activityType
	 * @param objectType
	 * @param severity
	 * @param expectedMsg
	 * @throws IOException
	 * @throws Exception
	 */
	
	
	          //@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_676")
				public void verify_User_HomeButton_676(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User browsed Home at Microsoft Office 365 Portal ", true);
					String expectedMsg="User browsed Home at Microsoft Office 365 Portal ";
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _676() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Logout", "Session","informational", ""},
						
					};
				}
				
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "677_678") 
				public void verify_User_DirectLogin_677_678(String activityType, String objectType, String severity, String expectedMsg, String logFile) throws Exception{
					Reporter.log("Validate User logged in " , true);
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _677_678() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Login", "Session", "informational","User logged in", ""},
						{ "InvalidLogin", "Session", "informational","User Login Failed", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "679") 
				public void verify_User_OutlookCalendarview1_679(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Outlook Calendar " , true);
					String expectedMsg="User viewed Outlook Calendar";
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _679() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "680") 
				public void verify_User_OutlookCalendarViewSSO_680(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Outlook " , true);
					String expectedMsg="User viewed Outlook";
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _680() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "681") 
				public void verify_User_OutlookCalendarView_681(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Outlook " , true);
					String expectedMsg="User viewed Outlook";
					replayLogsDebug(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _681() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "682") 
				public void verify_User_OutlookCalCreateEvent_682(String activityType, String objectType, String event_name, String start, String end, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Created an event named "+event_name+" " , true);
					String expectedMsg="User Created an event named "+event_name+". Event Start Time: "+start+" and End Time: "+end;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _682() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Event","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "683") 
				public void verify_User_OutlookCalCancelEvent_683(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User canceled an event named "+event_name+" " , true);
					String expectedMsg="User canceled an event named "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _683() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Cancel", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "684") 
				public void verify_User_OutlookCalEditEventName_684(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited an event named "+event_name+" " , true);
					String expectedMsg="User Edited an event named "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _684() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "685") 
				public void verify_User_OutlookCalEditEventPersonStatus_685(String activityType, String objectType, String event_name, String status, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited an event named "+event_name+" and updated the status as "+status+" " , true);
					String expectedMsg="User Edited an event named "+event_name+" and updated the status as "+status;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _685() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_686") 
				public void verify_User_OutlookCalEditLocation_686(String activityType, String objectType, String event_name, String location, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited an event named "+event_name+" and updated the location as "+location+" " , true);
					String expectedMsg="User Edited an event named "+event_name+" and updated the location as "+location;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _686() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_687") 
				public void verify_User_OutlookCalEditTime_687(String activityType, String objectType, String event_name, String start, String end, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited an event named "+event_name+" and updated the time " , true);
					String expectedMsg="User Edited an event named "+event_name+" and updated the time. Start: "+start+" - End: "+end;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _687() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_688") 
				public void verify_User_OutlookCalEventForward_688(String activityType, String objectType, String event_name, String recipient, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Forwarded details of event "+event_name+" to "+recipient+" " , true);
					String expectedMsg="User Forwarded details of event "+event_name+" to "+recipient;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _688() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Forward", "Event","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_689") 
				public void verify_User_OutlookCalEventReply_689(String activityType, String objectType, String event_name, String recipient, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Replied about event "+event_name+" to "+recipient+" " , true);
					String expectedMsg="User Replied about event "+event_name+" to "+recipient;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _689() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Reply", "Event","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_690") 
				public void verify_User_OutlookCalViewEvent_690(String activityType, String objectType, String event_name, String start, String end, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed an event named "+event_name+" ", true);
					String expectedMsg="User viewed an event named "+event_name+". Event Start Time: "+start+" and End Time: "+end;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _690() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Event","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_691") 
				public void verify_User_OutlookCalDeleteEvent_691(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Deleted an event named "+event_name+" ", true);
					String expectedMsg="User Deleted an event named "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _691() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_692") 
				public void verify_User_OutlookCalDayWeekWorkWeekMonthView_692(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Month View of the Outlook Calendar ", true);
					String expectedMsg="User switched to Month View of the Outlook Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _692() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_693") 
				public void verify_User_OutlookCalDayWeekWorkWeekMonthView_693(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Month View of the Outlook Calendar ", true);
					String expectedMsg="User switched to WorkWeek View of the Outlook Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _693() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_694") 
				public void verify_User_OutlookCalDayWeekWorkWeekMonthView_694(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Week View of the Outlook Calendar ", true);
					String expectedMsg="User switched to Week View of the Outlook Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _694() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_695") 
				public void verify_User_OutlookCalDayWeekWorkWeekMonthView_695(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Day View of the Outlook Calendar ", true);
					String expectedMsg="User switched to Day View of the Outlook Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				}
				
				@DataProvider
				public Object[][] _695() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_696") 
				public void verify_User_OutlookShareCalendar_696(String activityType, String objectType, String email, String severity, String logFile) throws Exception{
					Reporter.log("Validate User shared calendar with "+email+" ", true);
					String expectedMsg="User shared calendar with "+email;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _696() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Calendar","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_697") 
				public void verify_User_LogOut_697(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User logged out ", true);
					String expectedMsg="User logged out";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _697() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Logout", "Session", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_698") 
				public void verify_User_OutlookLoggedin_698(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User opened Outlook Exchange ", true);
					String expectedMsg="User opened Outlook Exchange";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _698() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Login", "Session", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_699") 
				public void verify_User_OutlookEmailRead_699(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User has read an email ", true);
					String expectedMsg="User has read an email";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _699() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Read", "Email", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_700") 
				public void verify_User_OutlookEmailReadSingle_700(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User has marked unread an email ", true);
					String expectedMsg="User has marked unread an email";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _700() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Read", "Email", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_701") 
				public void verify_User_OutlookEmailSendWithAttachment_701(String activityType, String objectType, String recipients, String subject, String attachment_name, String attachment_id, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent an email to "+recipients+" with subject "+subject+" and attachment "+attachment_name+" and attachment id "+attachment_id+"  ", true);
					String expectedMsg="User sent an email to "+recipients+" with subject "+subject+" and attachment "+attachment_name+" and attachment id "+attachment_id;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _701() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Email","","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_702") 
				public void verify_User_OutlookEmailSendWithAttachment_702(String activityType, String objectType, String recipients, String attachment_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent an email to "+recipients+" with no subject with attachment "+attachment_name+"  ", true);
					String expectedMsg="User sent an email to "+recipients+" with no subject with attachment "+attachment_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _702() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_703") 
				public void verify_User_OutlookEmailSend_703(String activityType, String objectType, String recipients, String subject, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent an email to "+recipients+" with subject "+subject+"  ", true);
					String expectedMsg="User sent an email to "+recipients+" with subject "+subject;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _703() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_704") 
				public void verify_User_OutlookEmailSend_704(String activityType, String objectType, String recipients, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sent an email to "+recipients+" with no subject  ", true);
					String expectedMsg="User sent an email to "+recipients+" with no subject";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _704() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Email","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_705") 
				public void verify_User_OutlookEmailFlag_705(String activityType, String objectType, String subjects, String sender, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sets flag to email(s) with subject(s) "+subjects+" and sender(s) "+sender+" ", true);
					String expectedMsg="User sets flag to email(s) with subject(s) "+subjects+" and sender(s) "+sender;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _705() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Flag", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_706") 
				public void verify_User_OutlookEmailDelete_706(String activityType, String objectType, String subjects, String senders, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted email(s) with subject(s) "+subjects+" and sender(s) "+senders+" ", true);
					String expectedMsg="User deleted email(s) with subject(s) "+subjects+" and sender(s) "+senders;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _706() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_707") 
				public void verify_User_OutlookEmailAttachment_707(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User attached file "+file_name+" to an email ", true);
					String expectedMsg="User attached file "+file_name+" to an email";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _707() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Upload", "File","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_708") 
				public void verify_User_OutlookDownloadAttachment_708(String activityType, String objectType, String file_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded attached file "+file_name+" ", true);
					String expectedMsg="User downloaded attached file "+file_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _708() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "File","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_709") 
				public void verify_User_OutlookEmailSearch_709(String activityType, String objectType, String search_item, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched for item "+search_item+" ", true);
					String expectedMsg="User searched for item "+search_item;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _709() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Email","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_710") 
				public void verify_User_OutlookContactSearch_710(String activityType, String objectType, String search_item, String severity, String logFile) throws Exception{
					Reporter.log("Validate User searched for contact "+search_item+" ", true);
					String expectedMsg="User searched for contact "+search_item;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _710() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Contact","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_711") 
				public void verify_User_OutlookAddContact_711(String activityType, String objectType, String first_name, String last_name, String emails, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added contact with name "+first_name+" "+last_name+" and email address(es) "+emails+" ", true);
					String expectedMsg="User added contact with name "+first_name+" "+last_name+" and email address(es) "+emails;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _711() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Add", "Contact","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_712") 
				public void verify_User_OutlookDeleteContact_712(String activityType, String objectType, String contact_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted contact named "+contact_name+" ", true);
					String expectedMsg="User deleted contact named "+contact_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _712() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Contact","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_713") 
				public void verify_User_OutlookEditContact_713(String activityType, String objectType, String new_first_name, String new_last_name, String emails, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited contact with new values "+new_first_name+" "+new_last_name+" "+emails+" ", true);
					String expectedMsg="User edited contact with new values "+new_first_name+" "+new_last_name+" "+emails;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _713() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Contact","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_714") 
				public void verify_User_OutlookIgnoreConversation_714(String activityType, String objectType, String subjects, String sender, String severity, String logFile) throws Exception{
					Reporter.log("Validate User ignored conversation of email(s) with subject(s) "+subjects+" and sender(s) "+sender+" ", true);
					String expectedMsg="User ignored conversation of email(s) with subject(s) "+subjects+" and sender(s) "+sender;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _714() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "IgnoreConversation", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_715") 
				public void verify_User_OutlookMarkAsRead_715(String activityType, String objectType, String subjects, String senders, String severity, String logFile) throws Exception{
					Reporter.log("Validate User marked as read email(s) with subject(s) "+subjects+" and sender(s) "+senders+" ", true);
					String expectedMsg="User marked as read email(s) with subject(s) "+subjects+" and sender(s) "+senders;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _715() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "MarkRead", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_716") 
				public void verify_User_OutlookMarkAsUnRead_716(String activityType, String objectType, String subjects, String senders, String severity, String logFile) throws Exception{
					Reporter.log("Validate User marked as unread email(s) with subject(s) "+subjects+" and sender(s) "+senders+" ", true);
					String expectedMsg="User marked as unread email(s) with subject(s) "+subjects+" and sender(s) "+senders;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _716() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "MarkUnread", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_717") 
				public void verify_User_OutlookEmailMove_717(String activityType, String objectType, String subjects, String senders, String severity, String logFile) throws Exception{
					Reporter.log("Validate User moved email(s) with subject(s) "+subjects+" and sender(s) "+senders+" ", true);
					String expectedMsg="User moved email(s) with subject(s) "+subjects+" and sender(s) "+senders;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _717() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Move", "Email","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_718") 
				public void verify_User_OutlookEmailSaveAsDraft_718(String activityType, String objectType, String subject, String severity, String logFile) throws Exception{
					Reporter.log("Validate User saved email as draft with subject "+subject+" ", true);
					String expectedMsg="User saved email as draft with subject "+subject;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _718() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Save", "Email","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_719") 
				public void verify_User_OutlookEmailDeleteDraft_719(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted draft(s) or deleted email(s) ", true);
					String expectedMsg="User deleted draft(s) or deleted email(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _719() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Email", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_720") 
				public void verify_User_OutlookMyTaskView_720(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed My Task(s) ", true);
					String expectedMsg="User viewed My Task(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _720() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task", "informational", ""},
						
					};
				}
				

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_721") 
				public void verify_User_OutlookCreateTask_721(String activityType, String objectType, String task_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User create a task named "+task_name+"", true);
					String expectedMsg="User create a task named "+task_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _721() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_722") 
				public void verify_User_OutlookDeleteTask_722(String activityType, String objectType, String task_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Deleted a task named "+task_name+" ", true);
					String expectedMsg="User Deleted a task named "+task_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _722() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Task","", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_723") 
				public void verify_User_OutlookEditTask_723(String activityType, String objectType, String old_name, String new_taskname, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited a task named "+old_name+" to "+new_taskname+" ", true);
					String expectedMsg="User Edited a task named "+old_name+" to "+new_taskname;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _723() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_724") 
				public void verify_User_OutlookTaskComplete_724(String activityType, String objectType, String task_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Marked a task named "+task_name+" completed ", true);
					String expectedMsg="User Marked a task named "+task_name+" completed";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _724() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task","", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_725") 
				public void verify_User_OutlookTaskComplete_725(String activityType, String objectType, String task_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Marked a task named "+task_name+" flagged ", true);
					String expectedMsg="User Marked a task named "+task_name+" flagged";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _725() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_726") 
				public void verify_User_OutlookRemoveAllTaskCategory_726(String activityType, String objectType, String task_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Removed a task named "+task_name+" from all categories ", true);
					String expectedMsg="User Removed a task named "+task_name+" from all categories";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _726() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Remove", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_727") 
				public void verify_User_OutlookAddTaskCategory_727(String activityType, String objectType, String task_name, String cat_str, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Added a task named "+task_name+" in categories "+cat_str+" ", true);
					String expectedMsg="User Added a task named "+task_name+" in categories "+cat_str;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _727() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Add", "Task","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_728") 
				public void verify_User_OutlookCreateTaskFolder_728(String activityType, String objectType, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created folder named "+folder_name+" ", true);
					String expectedMsg="User created folder named "+folder_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _728() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Folder","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_729") 
				public void verify_User_OutlookRenameTaskFolder_729(String activityType, String objectType, String old_folder_name, String new_folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Renamed folder "+old_folder_name+" to "+new_folder_name+" ", true);
					String expectedMsg="User Renamed folder "+old_folder_name+" to "+new_folder_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _729() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Rename", "Folder","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_730") 
				public void verify_User_OutlookDeleteTaskFolder_730(String activityType, String objectType, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Deleted a folder named "+folder_name+" ", true);
					String expectedMsg="User Deleted a folder named "+folder_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _730() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Folder","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_731") 
				public void verify_User_OutlookMoveTask_731(String activityType, String objectType, String task_name, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Moved email or task "+task_name+" to folder "+folder_name+" ", true);
					String expectedMsg="User Moved email or task "+task_name+" to folder "+folder_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _731() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Move", "Email/Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_732") 
				public void verify_User_SharepointTaskCalAppCreate_732(String activityType, String objectType, String app_name, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a Task Application named "+app_name+" ", true);
					String expectedMsg="User created a Calendar Application named "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _732() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Task_Application","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_733") 
				public void verify_User_SharepointTaskCalAppCreate_733(String activityType, String objectType, String app_name, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a Calendar Application named "+app_name+" ", true);
					String expectedMsg="User created a Calendar Application named "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _733() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Calendar_Application","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_734") 
				public void verify_User_SharepointTaskCreate_734(String activityType, String objectType, String item_name, String folder_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a Task "+item_name+" ", true);
					String expectedMsg="User created a Task "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _734() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_735") 
				public void verify_User_SharepointMyTaskView_735(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed My Task(s)", true);
					String expectedMsg="User viewed My Task(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _735() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_736") 
				public void verify_User_SharepointViewCompletedTask_736(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Completed task(s)", true);
					String expectedMsg="User viewed Completed task(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _736() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_737") 
				public void verify_User_SharepointViewTaskandLinks_737(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Task "+item_name+" ", true);
					String expectedMsg="User viewed Task "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _737() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task","", "informational", ""},
						
					};
				}
				
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_738") 
				public void verify_User_SharepointViewTaskandLinks_738(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Link Item "+item_name+" ", true);
					String expectedMsg="User viewed Link Item "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _738() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Link","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_739") 
				public void verify_User_SharepointDueDateFilter_739(String activityType, String objectType, String object_name,String Empty, String severity, String logFile) throws Exception{
					Reporter.log("Validate User applied filter field "+object_name+" and filter value "+Empty+" on Tasks ", true);
					String expectedMsg="User applied filter field "+object_name+" and filter value "+Empty+" on Tasks";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _739() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Task","Due date","Empty", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_740") 
				public void verify_User_SharepointDueDateFilter_740(String activityType, String objectType, String object_name,String filter_value, String severity, String logFile) throws Exception{
					Reporter.log("Validate User applied filter field "+object_name+" and filter value "+filter_value+" on Tasks ", true);
					String expectedMsg="User applied filter field "+object_name+" and filter value "+filter_value+" on Tasks";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _740() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Task","Due date","filter_value", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_741") 
				public void verify_User_SharepointSortChkMarkAsc_741(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Active/Inactive Tasks in Ascending order ", true);
					String expectedMsg="User sort Active/Inactive Tasks in Ascending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _741() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_742") 
				public void verify_User_SharepointSortChkMarkDesc_742(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Active/Inactive Tasks in Descending order ", true);
					String expectedMsg="User sort Active/Inactive Tasks in Descending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _742() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_743") 
				public void verify_User_SharepointSortDueDateAsc_743(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Tasks by Due date in Ascending order ", true);
					String expectedMsg="User sort Tasks by Due date in Ascending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _743() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_744") 
				public void verify_User_SharepointSortDueDateDesc_744(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Tasks by Due date in Descending order ", true);
					String expectedMsg="User sort Tasks by Due date in Descending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _744() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_745") 
				public void verify_User_SharepointAssignedToFilter_745(String activityType, String objectType, String Assigned_To, String Empty, String severity, String logFile) throws Exception{
					Reporter.log("Validate User applied filter field "+Assigned_To+" and filter value "+Empty+" on Tasks ", true);
					String expectedMsg="User applied filter field "+Assigned_To+" and filter value "+Empty+" on Tasks";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _745() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Task","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_746") 
				public void verify_User_SharepointAssignedToFilter_746(String activityType, String objectType, String Assigned_To, String filter_value, String severity, String logFile) throws Exception{
					Reporter.log("Validate User applied filter field "+Assigned_To+" and filter value "+filter_value+" on Tasks ", true);
					String expectedMsg="User applied filter field "+Assigned_To+" and filter value "+filter_value+" on Tasks";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _746() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Task","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_747") 
				public void verify_User_SharepointTitleFilter_747(String activityType, String objectType, String Title, String filter_value, String severity, String logFile) throws Exception{
					Reporter.log("Validate User applied filter field "+Title+" and filter value "+filter_value+" on Task Items ", true);
					String expectedMsg="User applied filter field "+Title+" and filter value "+filter_value+" on Task Items";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _747() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Filter", "Task","","", "informational", ""},
						
					};
				}


				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_748") 
				public void verify_User_SharepointSortTitleAsc_748(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Tasks by Title in Ascending order ", true);
					String expectedMsg="User sort Tasks by Title in Ascending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _748() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_749") 
				public void verify_User_SharepointSortTitleDesc_749(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort Tasks by Title in Descending order ", true);
					String expectedMsg="User sort Tasks by Title in Descending order";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _749() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_750") 
				public void verify_User_SharepointViewLateTask_750(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed late task(s) ", true);
					String expectedMsg="User User viewed late task(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _750() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_751") 
				public void verify_User_SharepointViewUpcomingTask_751(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Upcoming task(s) ", true);
					String expectedMsg="User viewed Upcoming task(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _751() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_752") 
				public void verify_User_SharepointEditTask_752(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited a Task named "+item_name+" ", true);
					String expectedMsg="User Edited a Task named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _752() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_753") 
				public void verify_User_SharepointEditTaskList_753(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited Task list ", true);
					String expectedMsg="User Edited Task list";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _753() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_754") 
				public void verify_User_SharepointDeleteTaskandLink_754(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted item named "+item_name+" ", true);
					String expectedMsg="User deleted item named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _754() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Item","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_755") 
				public void verify_User_SharepointDeleteTaskInside_755(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Task "+item_name+" ", true);
					String expectedMsg="User deleted a Task "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _755() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_756") 
				public void verify_User_SharepointDeleteTaskInside_756(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Link "+item_name+" ", true);
					String expectedMsg="User deleted a Link "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _756() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Link","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_757") 
				public void verify_User_SharepointDeleteTaskTab_757(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Task ", true);
					String expectedMsg="User deleted a Task";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _757() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_758") 
				public void verify_User_SharepointDeleteTaskTab_758(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Link  ", true);
					String expectedMsg="User deleted a Link";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _758() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Link", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_759") 
				public void verify_User_SharepointTaskAlert_759(String activityType, String objectType, String alert_item, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an Alert "+alert_item+" on Task named "+item_name+"  ", true);
					String expectedMsg="User created an Alert "+alert_item+" on Task named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _759() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_760") 
				public void verify_User_SharepointTaskAlert_760(String activityType, String objectType, String alert_item, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an Alert "+alert_item+" on Link named "+item_name+"  ", true);
					String expectedMsg="User created an Alert "+alert_item+" on Link named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _760() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_761") 
				public void verify_User_SharepointTaskViewSharedWith_761(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Sharing information of item named "+item_name+"  ", true);
					String expectedMsg="User viewed Sharing information of item named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _761() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Item","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_762") 
				public void verify_User_SharepointTaskViewSharedWith_762(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Sharing information of Link  ", true);
					String expectedMsg="User viewed Sharing information of Link";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _762() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Link", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_763") 
				public void verify_User_SharepointTaskCalLinkWorkflow_763(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Workflow of item named "+item_name+"  ", true);
					String expectedMsg="User viewed Workflow of item named "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _763() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Workflow","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_764") 
				public void verify_User_SharepointTaskCalLinkWorkflow_764(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Workflow of Link "+item_name+" ", true);
					String expectedMsg="User viewed Workflow of Link "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _764() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Workflow","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_766") 
				public void verify_User_SharepointEditView_766(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited a view and named "+view_name+" ", true);
					String expectedMsg="User Edited a view and named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _766() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_765") 
				public void verify_User_SharepointCreateView_765(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a view named "+view_name+" ", true);
					String expectedMsg="User created a view named "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _765() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_767") 
				public void verify_User_SharepointEditDelete_767(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Deleted a view ", true);
					String expectedMsg="User Deleted a view";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _767() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_768") 
				public void verify_User_SharepointCreateDiscussionBoard_768(String activityType, String objectType, String discussion_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a discussion board named "+discussion_name+" ", true);
					String expectedMsg="User created a discussion board named "+discussion_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _768() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Discussion_Board","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_769") 
				public void verify_User_SharepointCreateDiscussion_769(String activityType, String objectType, String new_post, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created a discussion named "+new_post+" ", true);
					String expectedMsg="User created a discussion named "+new_post;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _769() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Discussion","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_770") 
				public void verify_User_SharepointViewDiscussion_770(String activityType, String objectType, String post_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed a discussion named "+post_name+" ", true);
					String expectedMsg="User viewed a discussion named "+post_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _770() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_771") 
				public void verify_User_SharepointViewMyDiscussion_771(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed My Discussion(s) ", true);
					String expectedMsg="User viewed My Discussion(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _771() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_772") 
				public void verify_User_SharepointViewUnansDiscussion_772(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Unanswered Discussion(s) ", true);
					String expectedMsg="User viewed Unanswered Discussion(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _772() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_773") 
				public void verify_User_SharepointViewAnsDiscussion_773(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Answered Discussion(s) ", true);
					String expectedMsg="User viewed Answered Discussion(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _773() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_774") 
				public void verify_User_SharepointViewFeatured_774(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Featured Discussion(s) ", true);
					String expectedMsg="User viewed Featured Discussion(s)";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _774() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_775") 
				public void verify_User_SharepointNewDisReply1_775(String activityType, String objectType, String reply, String severity, String logFile) throws Exception{
					Reporter.log("Validate User replied "+reply+" in discussion ", true);
					String expectedMsg="User replied "+reply+" in discussion";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _775() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Discussion","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_776") 
				public void verify_User_SharepointEditDiscussion_776(String activityType, String objectType, String post_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited a discussion named "+post_name+" ", true);
					String expectedMsg="User Edited a discussion named "+post_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _776() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Discussion","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_777") 
				public void verify_User_SharepointEditReply_777(String activityType, String objectType, String reply, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited reply to "+reply+" in discussion ", true);
					String expectedMsg="User Edited reply to "+reply+" in discussion";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _777() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Post","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_778") 
				public void verify_User_SharepointBestAnwser_778(String activityType, String objectType, String disc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Selected a post as best answer in the discussion named "+disc_name+" ", true);
					String expectedMsg="User Selected a post as best answer in the discussion named "+disc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _778() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Select", "Post","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_779") 
				public void verify_User_SharepointDiscussAlert_779(String activityType, String objectType, String alert_post, String disc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an Alert entitled "+alert_post+" on discussion named "+disc_name+" ", true);
					String expectedMsg="User created an Alert entitled "+alert_post+" on discussion named "+disc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _779() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_780") 
				public void verify_User_SharepointDiscussDel_780(String activityType, String objectType, String disc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Discussion named "+disc_name+" ", true);
					String expectedMsg="User deleted a Discussion named "+disc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _780() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_781") 
				public void verify_User_SharepointDiscussReplyDel_781(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a reply from Discussion ", true);
					String expectedMsg="User deleted a reply from Discussion";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _781() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Reply", "Delete", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_782") 
				public void verify_User_SharepointUnmarkBestAnswer_782(String activityType, String objectType, String disc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Unselected a post as best answer in the discussion named "+disc_name+" ", true);
					String expectedMsg="User  Unselected a post as best answer in the discussion named "+disc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _782() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Select", "Post","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_783") 
				public void verify_User_SharepointTaskItemSearch_783(String activityType, String objectType, String search_key, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Searched for Task item "+search_key+" ", true);
					String expectedMsg="User Searched for Task item "+search_key;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _783() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Search", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_784") 
				public void verify_User_SharepointUpdateTimeline_784(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User updated Task on the timeline display ", true);
					String expectedMsg="User updated Task on the timeline display";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _784() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Timeline_Display", "Task", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_785") 
				public void verify_User_SharepointFollowDocument_785(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Started following Document entitled: "+doc_name+" ", true);
					String expectedMsg="User Started following Document entitled: "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _785() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Follow", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_786") 
				public void verify_User_SharepointTeamAppView_786(String activityType, String objectType, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed a Team Site app named "+app_name+" ", true);
					String expectedMsg="User viewed a Team Site app named \\"+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _786() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "App","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_787") 
				public void verify_User_SharepointCalendarView_787(String activityType, String objectType, String cal_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Calendar App named "+cal_name+" for site "+site_name+" ", true);
					String expectedMsg="User viewed Calendar App named \\"+cal_name+" for site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _787() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_788") 
				public void verify_User_SharepointTeamCalendarView_788(String activityType, String objectType, String cal_name, String Team, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Calendar App named "+cal_name+" for site "+Team+" ", true);
					String expectedMsg="User viewed Calendar App named "+cal_name+" for site "+Team;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _788() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Calendar","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_789") 
				public void verify_User_SharepointCalendarEventCreate_789(String activityType, String objectType, String event, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an event named "+event+" beginning from {begin_date} at {begin_hour}:{begin_min} and ending on {end_date} at {end_hour}:{end_min} ", true);
					String expectedMsg="User created an event named "+event+" beginning from {begin_date} at {begin_hour}:{begin_min} and ending on {end_date} at {end_hour}:{end_min}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _789() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_790") 
				public void verify_User_SharepointViewCalendarEvent_790(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Viewed Calendar Event "+event_name+" ", true);
					String expectedMsg="User Viewed Calendar Event "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _790() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_791") 
				public void verify_User_SharepointTeamViewCalendarEvent_791(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Viewed Calendar Event "+event_name+" ", true);
					String expectedMsg="User Viewed Calendar Event "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _791() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_792") 
				public void verify_User_SharepointEditCalendarEvent_792(String activityType, String objectType, String event_name, String event, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited Calendar Event "+event_name+" ", true);
					String expectedMsg="User Edited Calendar Event "+event_name+". Event details are now: Event name is "+event+" beginning from {begin_date} at {begin_hour}:{begin_min} and ending on {end_date} at {end_hour}:{end_min}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _792() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Event","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_793") 
				public void verify_User_SharepointDeleteEvent_793(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted an Event named "+event_name+" ", true);
					String expectedMsg="User deleted an Event named "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _793() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_794") 
				public void verify_User_SharepointDeleteEvent2_794(String activityType, String objectType, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted an Event "+event_name+" ", true);
					String expectedMsg="User deleted an Event "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _794() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Event","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_795") 
				public void verify_User_SharepointEventAlert_795(String activityType, String objectType, String alert_event, String event_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created an Alert entitled "+alert_event+" on Calendar event "+event_name+" ", true);
					String expectedMsg="User created an Alert entitled "+alert_event+" on Calendar event "+event_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _795() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_796") 
				public void verify_User_SharepointCalendarDayView_796(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Day View of Calendar ", true);
					String expectedMsg="User switched to Day View of Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _796() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Configure", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_797") 
				public void verify_User_SharepointCalendarWeekView_797(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Week View of Calendar ", true);
					String expectedMsg="User switched to Week View of Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _797() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Configure", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_798") 
				public void verify_User_SharepointCalendarWeekView_798(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User switched to Week View of Calendar ", true);
					String expectedMsg="User switched to Week View of Calendar";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _798() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Configure", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_799") 
				public void verify_User_SharepointCalendarOverlaySettings_799(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Calendar Overlay Settings ", true);
					String expectedMsg="User viewed Calendar Overlay Settings";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _799() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Settings", "Calendar", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_800") 
				public void verify_User_SharepointViewTaskandLinks1_800(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Task "+item_name+" ", true);
					String expectedMsg="User viewed Task "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _800() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Task","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_801") 
				public void verify_User_SharepointViewTaskandLinks1_801(String activityType, String objectType, String item_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Link Item "+item_name+" ", true);
					String expectedMsg="User viewed Link Item "+item_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _801() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Link","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_802") 
				public void verify_User_SharepointPopularityTrends_802(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User is downloading popularity trends of document "+doc_name+" ", true);
					String expectedMsg="User is downloading popularity trends of document "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _802() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Alert","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_803") 
				public void verify_User_SharepointDeleteDocInFileTabPersonal_803(String activityType, String objectType, String doc_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted document "+doc_name+" from site "+site_name+" ", true);
					String expectedMsg="User deleted document "+doc_name+" from site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _803() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "App","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_804") 
				public void verify_User_SharepointDeleteDocInFileTab_804(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted document "+doc_name+" ", true);
					String expectedMsg="User deleted document "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _804() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "App","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_805") 
				public void verify_User_SharepointTeamAppDel_805(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted a Team Site App ", true);
					String expectedMsg="User deleted a Team Site App";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _805() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "App", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_806") 
				public void verify_User_SharepointDocDownload_806(String activityType, String objectType, String doc_name, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded "+doc_name+" from app "+app_name+" ", true);
					String expectedMsg="User downloaded "+doc_name+" from app "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _806() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_807") 
				public void verify_User_SharepointDocDownloadWhenOpen_807(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User downloaded document "+doc_name+" ", true);
					String expectedMsg="User downloaded document "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _807() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Download", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_808") 
				public void verify_User_SharepointCopyDocInApp_808(String activityType, String objectType, String doc_name, String source_url, String dest_url, String severity, String logFile) throws Exception{
					Reporter.log("Validate User is copying document "+doc_name+" from location "+source_url+" to destination "+dest_url+" ", true);
					String expectedMsg="User is copying document "+doc_name+" from location "+source_url+" to destination "+dest_url;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _808() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Copy", "Document","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_809") 
				public void verify_User_SharepointFollowDocInLib_809(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User is following document "+doc_name+" ", true);
					String expectedMsg="User is following document "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _809() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Follow", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_810") 
				public void verify_User_SharepointFollowDocInLibPersonal_810(String activityType, String objectType, String doc_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User is following document "+doc_name+" in site "+site_name+" ", true);
					String expectedMsg="User is following document "+doc_name+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _810() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Follow", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_811") 
				public void verify_User_SharepointEditPropertyInFileTab_811(String activityType, String objectType, String doc_name_new, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited properties of document "+doc_name_new+" ", true);
					String expectedMsg="User edited properties of document "+doc_name_new;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _811() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_812") 
				public void verify_User_SharepointEditPropertyInFileTabPersonal_812(String activityType, String objectType, String doc_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited properties of document "+doc_name+" in site "+site_name+" ", true);
					String expectedMsg="User edited properties of document "+doc_name+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
				}
				
				@DataProvider
				public Object[][] _812() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","","", "informational", ""},
						
					};
				}

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_813") 
				public void verify_User_SharepointQuickEditPersonal_813(String activityType, String objectType, String app_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited content of app "+app_name+" of site "+site_name+" in quick edit mode ", true);
					String expectedMsg="User edited content of app "+app_name+" of site "+site_name+" in quick edit mode";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _813() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","","", "informational", ""},
						
					};
				}

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_814") 
				public void verify_User_SharepointQuickEdit_814(String activityType, String objectType, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User edited content of app "+app_name+" in quick edit mode ", true);
					String expectedMsg="User edited content of app "+app_name+" in quick edit mode";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _814() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit", "Document","", "informational", ""},
						
					};
				}

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_815") 
				public void verify_User_SharepointAddColumnInView_815(String activityType, String objectType, String column_name, String description, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added new column "+column_name+" with description "+description+" ", true);
					String expectedMsg="User added new column "+column_name+" with description "+description;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _815() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Add", "Column","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_816") 
				public void verify_User_SharepointEditPropertyInAppPersonal_816(String activityType, String objectType, String doc_name, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited properties of "+doc_name+" in app "+app_name+" ", true);
					String expectedMsg="User Edited properties of "+doc_name+" in app "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _816() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Add", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_817") 
				public void verify_User_SharepointEditPropertyInApp_817(String activityType, String objectType, String doc_name, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Edited properties of "+doc_name+" in app "+app_name+" ", true);
					String expectedMsg="User Edited properties of "+doc_name+" in app "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _817() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Edit Properties", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_818") 
				public void verify_User_SharepointViewWorkFlowInApp_818(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Workflow of file "+doc_name+" ", true);
					String expectedMsg="User viewed Workflow of file "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _818() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_819") 
				public void verify_User_SharepointComplianceDetails_819(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed Compliance Details of file "+doc_name+" ", true);
					String expectedMsg="User viewed Compliance Details of file "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _819() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_820") 
				public void verify_User_SharepointViewAppPage_820(String activityType, String objectType, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User viewed "+app_name+" page ", true);
					String expectedMsg="User viewed "+app_name+" page";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _820() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "View", "Page","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_821") 
				public void verify_User_SharepointAddApp_821(String activityType, String objectType, String type, String name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User added app of type "+type+", named "+name+" ", true);
					String expectedMsg="User added app of type "+type+", named "+name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _821() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Add", "App","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_822") 
				public void verify_User_SharepointAddItemInAssetLib_822(String activityType, String objectType, String file_name, String lib_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User uploaded file "+file_name+" in library "+lib_name+" ", true);
					String expectedMsg="User uploaded file "+file_name+" in library "+lib_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _822() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Upload", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_823") 
				public void verify_User_SharepointCreateWordDocInLib_823(String activityType, String objectType, String doc_name, String app_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new word document "+doc_name+" in app "+app_name+" ", true);
					String expectedMsg="User created new word document "+doc_name+" in app "+app_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _823() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_824") 
				public void verify_User_SharepointCreateWordDocInLibPersonal_824(String activityType, String objectType, String doc_name, String app_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created new word document "+doc_name+" in app "+app_name+" of site "+site_name+" ", true);
					String expectedMsg="User created new word document "+doc_name+" in app "+app_name+" of site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _824() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Document","","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_825") 
				public void verify_User_SharepointCreatingViewPersonal_825(String activityType, String objectType, String view_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User modified view "+view_name+" in site "+site_name+" ", true);
					String expectedMsg="User modified view "+view_name+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _825() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Modify", "View","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_826") 
				public void verify_User_SharepointCreatingViewPersonal_826(String activityType, String objectType, String view_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User created new view "+view_name+" in site "+site_name+" ", true);
					String expectedMsg="User created new view "+view_name+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _826() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_827") 
				public void verify_User_SharepointCreatingView_827(String activityType, String objectType, String view_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User modified view "+view_name+" ", true);
					String expectedMsg="User modified view "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _827() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Modify", "View","", "informational", ""},
						
					};
				}

				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_828") 
				public void verify_User_SharepointCreatingView_828(String activityType, String objectType, String view_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User created new view "+view_name+" ", true);
					String expectedMsg="User created new view "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _828() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_829") 
				public void verify_User_SharepointSortName_829(String activityType, String objectType, String order, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Name in order "+order+" ", true);
					String expectedMsg="User sort field Name in order "+order;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _829() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_830") 
				public void verify_User_SharepointSortNamePersonal_830(String activityType, String objectType, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Name in order {order} in site "+site_name+" ", true);
					String expectedMsg="User sort field Name in order {order} in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _830() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_831") 
				public void verify_User_SharepointSortModified_831(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Modified in order {order} ", true);
					String expectedMsg="User sort field Modified in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _831() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_832") 
				public void verify_User_SharepointSortModifiedPersonal_832(String activityType, String objectType, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Modified in order {order} in site "+site_name+" ", true);
					String expectedMsg="User sort field Modified in order {order} in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _832() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_833") 
				public void verify_User_SharepointSortPictureSize_833(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field File Size in order {order} ", true);
					String expectedMsg="User sort field File Size in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _833() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_834") 
				public void verify_User_SharepointSortModifiedBy_834(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Modified By in order {order} ", true);
					String expectedMsg="User sort field Modified By in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _834() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_835") 
				public void verify_User_SharepointSortModifiedByPersonal_835(String activityType, String objectType, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Modified By in order {order} in site "+site_name+" ", true);
					String expectedMsg="User sort field Modified By in order {order} in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _835() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_836") 
				public void verify_User_SharepointSortNameAssets_836(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Name in order {order} ", true);
					String expectedMsg="User sort field Name in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _836() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_837") 
				public void verify_User_SharepointSortNameLength_837(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate  User sort field Length in order {order} ", true);
					String expectedMsg="User sort field Length in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _837() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_838") 
				public void verify_User_SharepointSortCheckoutTo_838(String activityType, String objectType, String severity, String logFile) throws Exception{
					Reporter.log("Validate User sort field Check Out To in order {order} ", true);
					String expectedMsg="User sort field Check Out To in order {order}";
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _838() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Sort", "View", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_839") 
				public void verify_User_SharepointDocSharePersonal_839(String activityType, String objectType, String obj_name, String share_with, String severity, String logFile) throws Exception{
					Reporter.log("Validate User is sharing document "+obj_name+" with "+share_with+" ", true);
					String expectedMsg="User is sharing document "+obj_name+" with "+share_with;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _839() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Share", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_840") 
				public void verify_User_SharepointCreateFilter_840(String activityType, String objectType, String filter_value, String filter_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User created filter of "+filter_value+" on filed "+filter_name+" ", true);
					String expectedMsg="User created filter of "+filter_value+" on filed "+filter_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _840() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Create", "Filter","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_841") 
				public void verify_User_SharepointFindItem_841(String activityType, String objectType, String search_item, String severity, String logFile) throws Exception{
					Reporter.log("Validate User search item "+search_item+" ", true);
					String expectedMsg="User search item "+search_item;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _841() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Find", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_842") 
				public void verify_User_SharepointFindItem2_842(String activityType, String objectType, String search_item, String severity, String logFile) throws Exception{
					Reporter.log("Validate User search item "+search_item+" ", true);
					String expectedMsg="User search item "+search_item;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _842() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Find", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_843") 
				public void verify_User_SharepointFindItemPersonal_843(String activityType, String objectType, String search_item, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User search item "+search_item+" in site "+site_name+" ", true);
					String expectedMsg="User search item "+search_item+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _843() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Find", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_844") 
				public void verify_User_SharepointFindItem2Personal_844(String activityType, String objectType, String search_item, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User search item "+search_item+" in site "+site_name+" ", true);
					String expectedMsg="User search item "+search_item+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _844() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Find", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_845") 
				public void verify_User_SharepointDeleteView_845(String activityType, String objectType, String view_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted view "+view_name+" ", true);
					String expectedMsg="User deleted view "+view_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _845() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "View","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_846") 
				public void verify_User_SharepointDeleteFileInApp_846(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User deleted Document/Folder "+doc_name+" ", true);
					String expectedMsg="User deleted Document/Folder "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _846() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "Delete", "Document/Folder","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_847") 
				public void verify_User_SharepointFileCheckOutInAppPersonal_847(String activityType, String objectType, String doc_name, String site_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Checked Out file "+doc_name+" in site "+site_name+" ", true);
					String expectedMsg="User Checked Out file "+doc_name+" in site "+site_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _847() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "CheckOut", "Document","","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_848") 
				public void verify_User_SharepointFileCheckOutInApp_848(String activityType, String objectType, String doc_name, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Checked Out file "+doc_name+" ", true);
					String expectedMsg="User Checked Out file "+doc_name;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _848() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "CheckOut", "Document","", "informational", ""},
						
					};
				}
				
				//@Test(groups ={"TEST","BOX", "UPLOAD", "QAVPC","P1"},dataProvider = "_849") 
				public void verify_User_SharepointFileCheckInAppPersonal_849(String activityType, String objectType, String doc_name, String description, String severity, String logFile) throws Exception{
					Reporter.log("Validate User Checked In file "+doc_name+" with description "+description+" ", true);
					String expectedMsg="User Checked In file \\"+doc_name+" with description "+description;
					replayLogs(logFile);
					data.put("message", expectedMsg);
					data.put("account_type", "Internal");
					assertTrue((validateLogsFields(fetchElasticSearchLogsUniversal(), data)), activityType+" Logs does not match" ); 
					
				}
				
				@DataProvider
				public Object[][] _849() {
					return new Object[][]{
						//Activity type  Object Type  Object Name  Severity  message
						{ "CheckOut", "Document","", "informational", ""},
						
					};
				}
}