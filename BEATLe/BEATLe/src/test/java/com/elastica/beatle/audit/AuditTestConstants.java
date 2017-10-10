/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anuvrath
 *
 */
public class AuditTestConstants {

	public static final String EOE_S3_REG_BUCKET="eoe_be_s3_regression";
	public static final String PROD_S3_REG_BUCKET="prod_be_s3_regression";
	public static final String CEP_S3_REG_BUCKET="cep_be_s3_regression";
	public static final String QAVPC_S3_REG_BUCKET="qavpc_be_s3_regression";
	public static final String envX_S3_REG_BUCKET="envX_be_s3_regression";
	
	public static final String AUDIT_S3_BUCKET = "elastica-qa-automation";
	public static final String AUDIT_FILE_TEMP_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"Audit"+File.separator+"LogFiles"+File.separator+"TempLogFiles";
	
	public static final String AUDIT_BACKUP_RESTORE_FILE_TEMP_PATH = System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+
			File.separator+"Audit"+File.separator+"Audit_TestData"+File.separator+"TempBRFile";
	

	
	/*
	 * Syslog transportation type constants
	 */
	public static final String SYSLOG_TRAFFIC_GENERATOR_SCRIPT_FILEPATH = "/src/test/resources/Audit/SysLogPythonScript/replay_syslogsnew.py";
	
	public static final String AUDIT_API_CONFIGURATION_FILEPATH = "/src/test/resources/Audit/AuditConfigurations/AuditAPIConfigurations.xml";
	public static final String AUDIT_FIREWALL_CONFIG_FILEPATH = "/src/test/resources/Audit/AuditConfigurations/LogFileAttribute.xml";
	public static final String AUDIT_S3_PROPERTIES_FILEPATH = "/src/test/resources/Audit/AuditConfigurations/S3Configurations.xml";	
	public static final String PREFERENCEJSON_FILEPATH = "/src/test/resources/Audit/Audit_TestData/Audit_Preference_JSON.json";
	public static final String RESTOREDEFAULTSPREFERENCEJSON_FILEPATH= "/src/test/resources/Audit/Audit_TestData/Audit_RestoreDefaultsPreference_JSON.json";
	
	public static final String AUDIT_CONFIGURED_AGENTS_PATH = "/src/test/resources/Audit/AuditSpanVAAgentsList/configuredagents.properties";

	public static final String FIREWALL_WALLMART_MCAFEE_DATA_FILE_PATH = "/src/test/resources/Audit/Audit_TestData/Audit_Wallmart_Mcaffe_Data.properties";
	public static final String FIREWALL_WALLMART_PANCSV_DATA_FILE_PATH = "/src/test/resources/Audit/Audit_TestData/Audit_Wallmart_PanCsv_Data.properties";
	public static final String FIREWALL_WALLMART_PANSYS_DATA_FILE_PATH = "/src/test/resources/Audit/Audit_TestData/Audit_Wallmart_PanSys_Data.properties";

	public static final String FIREWALL_WALLMART_MCAFEE_DENIED_DATA_PATH = "/src/test/resources/Audit/Audit_TestData/Wallmart_Mcaffe_Denied_Data.properties";
	public static final String FIREWALL_WALLMART_PANCSV_DENIED_DATA_PATH = "/src/test/resources/Audit/Audit_TestData/Audit_Wallmart_PanCsv_Denied_Data.properties";
	public static final String FIREWALL_WALLMART_PANSYS_DENIED_DATA_PATH = "/src/test/resources/Audit/Audit_TestData/Audit_Wallmart_PanSys_Denied_Data.properties";

	public static final String FIREWALL_SERVICENAMES = "/src/test/resources/Audit/Audit_TestData/servicenames.properties";

	public static final String SPANVA_PROPS="/src/test/resources/Audit/AuditSpanVAAgentsList/spanvaconfig.properties";
	
	public static final String SPANVA_FIREWALLSLIST="/src/test/resources/Audit/AuditSpanVAAgentsList/spavaupgradetestsfirewalls_zip.properties";
	public static final String SPANVA_FIREWALLSLIST_7Z="/src/test/resources/Audit/AuditSpanVAAgentsList/spavaupgradetestsfirewalls_7z.properties";
	public static final String SPANVA_FIREWALLSLIST_GZ="/src/test/resources/Audit/AuditSpanVAAgentsList/spavaupgradetestsfirewalls_gz.properties";
	public static final String SPANVA_FIREWALLSLIST_BZ2="/src/test/resources/Audit/AuditSpanVAAgentsList/spavaupgradetestsfirewalls_bz2.properties";
	public static final String SPANVA_UPDRADESTEST_FIREWALLSLIST="/src/test/resources/Audit/AuditSpanVAAgentsList/spanvaupgradetestsfirewalls.properties";
	public static final String AUDIT_FIREWALLS_LIST="/src/test/resources/Audit/AuditSpanVAAgentsList/beregressionfirewalls.properties";
	public static final String PREFERENCE_FIREWALLS_LISTS="/src/test/resources/Audit/AuditSpanVAAgentsList/preferenceFirewallsLists.properties";	
	
	
	public static final String SPANVA_SCP_PWDS="/src/test/resources/Audit/AuditSpanVAAgentsList/spanvatenantsscppwds.properties";

	public static final String AUDIT_GOLDENSET_DATA_SHEET="/src/test/resources/Audit/Audit_TestData/GoldensetData.xlsx";

	//public static final String AUDIT_GOLDENSET_DATA_NEW_SHEET="/src/test/resources/Audit/Audit_TestData/gsdata1.xlsx";
	
	public static final String AUDIT_GOLDENSET_DATA_NEW_SHEET="/src/test/resources/Audit/Audit_TestData/GoldenSetData/gsdata1_new.xlsx";
	//public static final String AUDIT_GOLDENSET_DATA_NEW_SHEET_2="/src/test/resources/Audit/Audit_TestData/GoldenSetData/gsdata2_new.xlsx";

	public static final String AuDIT_SANITY_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/SanityScpDss.txt";
	public static final String AuDIT_SANITY_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/SanityS3Dss.txt";
	
	public static final String AUDIT_SANITY_VPC_WU_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/qavpc_sanityswuds.txt";
	public static final String AUDIT_SANITY_VPC_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/qavpc_sanityscpds.txt";
	public static final String AUDIT_SANITY_VPC_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/qavpc_sanitys3ds.txt";
	public static final String AUDIT_SANITY_VPC_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/qavpc_sanitys3prevtopreds.txt";
	public static final String AUDIT_SANITY_VPC_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/qavpc_sanityscpprevtopreds.txt";
	
	
	public static final String AUDIT_SANITY_EOE_WU_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanitywuds.txt";
	public static final String AUDIT_SANITY_EOE_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanityscpds.txt";
	public static final String AUDIT_SANITY_EOE_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanitys3ds.txt";
	public static final String AUDIT_SANITY_EOE_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanitys3prevtopreds.txt";
	public static final String AUDIT_SANITY_EOE_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanityscpprevtopreds.txt";
	
	
	public static final String AUDIT_SANITY_CEP_WU_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/cep_sanitywuds.txt";
	public static final String AUDIT_SANITY_CEP_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/cep_sanityscpds.txt";
	public static final String AUDIT_SANITY_CEP_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/cep_sanityscpprevtopreds.txt";
	public static final String AUDIT_SANITY_CEP_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/cep_sanitys3ds.txt";
	public static final String AUDIT_SANITY_CEP_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/cep_sanitys3prevtopreds.txt";
	
	public static final String AUDIT_SANITY_PROD_WU_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/prod_sanitywuds.txt";
	public static final String AUDIT_SANITY_PROD_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/prod_sanityscpds.txt";
	public static final String AUDIT_SANITY_PROD_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/prod_sanityscpprevtopreds.txt";
	public static final String AUDIT_SANITY_PROD_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/prod_sanitys3ds.txt";
	public static final String AUDIT_SANITY_PROD_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/prod_sanitys3prevtopreds.txt";
	
	
	public static final String AUDIT_SANITY_ENVX_WU_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/envX_sanitywuds.txt";
	public static final String AUDIT_SANITY_ENVX_SCP_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/envX_sanityscpds.txt";
	public static final String AUDIT_SANITY_ENVX_SCP_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/envX_sanityscpprevtopreds.txt";
	public static final String AUDIT_SANITY_ENVX_S3_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/envX_sanitys3ds.txt";
	public static final String AUDIT_SANITY_ENVX_S3_PREVIOUS_TO_PREV_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/envX_sanitys3prevtopreds.txt";
	
	public static final String AUDIT_EOE_OLD_REGKEY_DSS_BR_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/brolddata/eoe_old_regkey_dss_br_data.txt";
	public static final String AUDIT_PROD_OLD_REGKEY_DSS_BR_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/brolddata/prod_old_regkey_dss_br_data.txt";
	public static final String AUDIT_CHA_OLD_REGKEY_DSS_BR_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/brolddata/cha_old_regkey_dss_br_data.txt";
	public static final String AUDIT_QAVPC_OLD_REGKEY_DSS_BR_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/brolddata/qavpc_old_regkey_dss_br_data.txt";
	public static final String AUDIT_EU_OLD_REGKEY_DSS_BR_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/brolddata/eu_old_regkey_dss_br_data.txt";
	
	
	
	public static final String FIREWALL_LOG_REPORT_DATA_DATERANGE="/src/test/resources/Audit/Audit_TestData/firewallsdatadaterange/logdaterange.properties";
	

	public static final String AUDIT_SANITY_EOE_SCP_DUMMY_PREVIOUS_RUN_DSID_LOCATION_PATH = "/src/test/resources/Audit/Audit_TestData/previousrunds/eoe_sanityscpds_dummy.txt";
		
	public static final String AUDIT_DASHBOARD_REPORT_SOURCES_CONFIG_FIELDS_PATH="/src/test/resources/Audit/AuditReportDashboard/auditreportconfigfields.properties";
	
	

	public static final long AUDIT_PROCESSING_MAX_WAITTIME = 25200000 ; // Audit process max wait time 7 hours

	public static final long AUDIT_PROCESSING_SANITY_MAX_WAITTIME = 1200000 ; // Audit Sanity data source process wait time 30 min
	
	//public static final long AUDIT_PROCESSING_SANITY_MAX_WAITTIME_TEMP = 180000 ; // Audit Sanity data source process wait time 30 min



	public static final long AUDIT_FILE_PROCESSING_MAX_WAITTIME = 10800000 ; // For web Upload wait time - 3 hours
	public static final long AUDIT_S3_FILE_PROCESSING_MAX_WAITTIME = 14400000 ; // For S3 Upload- 4 hours
	public static final long AUDIT_SCP_FILE_PROCESSING_MAX_WAITTIME=18000000;// scp total log process waiting time 5 hrs
	public static final long AUDIT_THREAD_WAITTIME = 120000;
	public static final String AUDIT_WEBUPLOAD_FILEFORMAT = "File";	
	public static final String AUDIT_SCP_FILEFORMAT = "SCP";
	//public static final String AUDIT_SCP_SERVER="eoeupload.elastica-inc.com";
	public static final String AUDIT_SCP_SERVER="10.0.61.210";
	//public static final String AUDIT_SCP_SERVER="10.0.0.48";
	
	public static final String AUDIT_QAVPC_SCP_SERVER="10.0.48.118";
	public static final String AUDIT_SCP_SERVER_PROD="upload.elastica.net";
	public static final String AUDIT_SCP_SERVER_CEP="upload.eu.elastica.net";
	public static final String AUDIT_SCP_SERVER_CWSINTG="upload-cwsintg.elastica-inc.com";
	
	public static final String AUDIT_CHA_SCP_SERVER="10.0.52.140";
	public static final String AUDIT_CHA_TENANT_SCP_PWD="ih6KJdbw";
	
	



	public static final String AUDIT_SCP_PORT= "22";
	//This password is used for the scp authentication for all the datasources for the tenant
	public static final String AUDIT_TENANT_PWD="8kkZKkAK";//  datasources password for tenant ds_auditbe1com    RsXetuZY
	public static final String AUDIT_TENANT_VPC_PWD="i2j5m5Dl";//  scp pwd for user@vpcauditscp.co
	public static final String AUDIT_TENANT_VPC_MULTIDEVICEID_SCP_PWD="Uh4fvIdg";//  datasources password for tenant ds_auditbe1com
	public static final String AUDIT_TENANT_VPC_NEGATIVETESTS_SCP_PWD="S71iqi9V";//  datasources password for tenant ds_auditbe1com
	public static final String AUDIT_SPANVA_TENANT_PWD="f0yMP9cB";//  password for tenant auditspanva1
	public static final String AUDIT_SPANVA_VPC_TENANT_PWD="5GXBoFad";//  datasources password for tenant ds_auditbe1com
	public static final String AUDIT_TENANT_PROD_SCP_PWD="wiV09yO0";// scp password for the prod tenant  4EsZOcyB
	public static final String AUDIT_TENANT_PROD_SCP_PWD1="4EsZOcyB";// scp password for the prod tenant  4EsZOcyB
	//public static final String AUDIT_TENANT_CEP_SCP_PWD="hAmm1c6W"; //scp password for the cep tenant  utlCPgOe 
	//public static final String AUDIT_TENANT_CEP_SCP_PWD="QMIOXJ5e";
	public static final String AUDIT_TENANT_CEP_SCP_PWD="JpWkSL0h";
	
	public static final String AUDIT_TENANT_EOE_ANONY_SCP_PWD="uWEC2EYb";
	
	
	public static final String AUDIT_TENANT_VPC_PWD1="b6hKsFyX";//  scp pwd for muzaffar tenant
	
	
	
	public static final String AUDIT_TENANT_EOE_WEEKLY_SCP_PWD="tBCt3u6U"; //scp password for the EOE Weekly based tenant
	public static final String AUDIT_SPANVA_LOG_TRANSPORT = "Agent";
	public static final String AUDIT_SPANVA_AGENT_NAME1="EOE_auditbecom_SpanVA";
	public static final String AUDIT_SFTP_FILEFORMAT = "SFTP";
	public static final String AUDIT_S3_LOGTRANSPORT = "S3";	
	public static final String AUDIT_LOG_PROCESS_SUCESS_MSG="Logs processed successfully";
	public static final String AUDIT_SCP_DEVICEID_TENANT_PWD="cr5tFRYp";//  scp password for deviceid
	public static final String AUDIT_TENANT_CWS_SCP_PWD="DHzF8tsL";//  scp password for cws integrated tenant
	public static final String AUDIT_TENANT_EOE_NEGATIVETESTS_TENANT_SCP_PWD="2YiJexuU"; //scp password for the EOE Weekly based tenant
	
	public static final String AUDIT_PROD_SCP_SANITY_PWD="Nxiyz1g2"; //scp password for the prod sanity
	
	public static final String AUDIT_CEP_SCP_SANITY_PWD="HzynreXa"; //scp password for the cep sanity
	
	public static final String AUDIT_EOE_SCP_SANITY_PWD="Pmcsy16v"; //scp password for the eoe sanity user: user@eoesanitytia.co
	
	public static final String AUDIT_QAVPC_SCP_SANITY_PWD="gKRtsHSn"; //scp password for the eoe sanity user: user@eoesanitytia.co
	
	
	
	
	
	
	

	//Weekly based Tenants Regresson scp passwords
	public static final String AUDIT_EOE_TENANT_WEEKLY_SCP_PWD="vualWNg1";//  scp password for weeklyscptenant
	
	public static final String AUDIT_EOE_SPANVA_ING_SCP_PWD="s5RwadzZ";//  scp password for weeklyscptenant  s5RwadzZ   yM0kjNih
	
	

	


	//DatasourceName constants
	public static final String AUDIT_WU_DS_NAME="wu";
	public static final String AUDIT_WU_DEVICE_ID_DS_NAME="wu_deviceId"; 
	public static final String AUDIT_SCP_DS_NAME="scp";
	public static final String AUDIT_SCP_DEVICE_ID_DS_NAME="scp_deviceId";
	public static final String AUDIT_S3_DS_NAME="s3"; 
	public static final String AUDIT_S3_DEVICE_ID_DS_NAME="s3_deviceId";
	public static final String AUDIT_SPANVA_DS_NAME="SPANVA";


	//DatasourceNames for Weekly Regression
	public static final String AUDIT_WEEKLY_WU_DS_NAME="weekly_wu";
	public static final String AUDIT_WEEKLY_WU_DEVICE_ID_DS_NAME="weekly_wu_deviceId"; 
	public static final String AUDIT_WEEKLY_SCP_DS_NAME="weekly_scp";
	public static final String AUDIT_WEEKLY_SCP_DEVICE_ID_DS_NAME="weekly_scp_deviceId";
	public static final String AUDIT_WEEKLY_S3_DS_NAME="weekly_s3"; 
	public static final String AUDIT_WEEKLY_S3_DEVICE_ID_DS_NAME="weekly_s3_deviceId";




	// Firewalls supported
	//web upload firewalls
	public static final String FIREWALL_BLUECOAT_PROXYSG = "blueCoatProxySG";
	public static final String FIREWALL_CISCO_ASA_SERIES = "CiscoASASeries";
	public static final String FIREWALL_WALLMART_PAN_CSV = "wallmartPanCSV";
	public static final String FIREWALL_WALLMART_PAN_CSV_SHEET = "WALLMART-PAN-CSV";
	public static final String FIREWALL_WALLMART_PAN_SYS = "wallmartPanSYS";
	public static final String FIREWALL_WALLMART_PAN_SYS_SHEET = "WALLMART-PAN-SYS";
	public static final String FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY = "wallmartWebGateWayLog";

	//scp upload firewalls
	public static final String FIREWALL_SCANSAFE = "scanSafe";
	public static final String FIREWALL_SCANSAFE_SHEET = "ScanSafe";


	//s3 upload firewalls
	public static final String FIREWALL_JUNIPER_SCREENOS = "juniperScreenOS";
	public static final String FIREWALL_CISCO_WSA_W3C = "ciscoWSAW3C";
	public static final String FIREWALL_PALOALTO_CSV = "paloAltoNet";
	public static final String FIREWALL_CISCO_CWS = "ciscoCWS";
	public static final String FIREWALL_BARRACUDA_ACTIVITYLOG = "barracudaActivityLog";
	public static final String FIREWALL_BARRACUDA_SYSLOG = "barracudaActivitySysLog";

	public static final String FIREWALL_CHECKPOINT_CSV = "checkpointCSV";
	public static final String FIREWALL_CHECKPOINT_CSV_SHEET = "CheckPoint-CSV";

	public static final String FIREWALL_CHECKPOINT_SMARTVIEW = "checkpointSmartView";
	public static final String FIREWALL_CHECKPOINT_SMARTVVIEW_SHEET = "CheckPoint-SmartView";

	public static final String FIREWALL_JUNIPER_SRX = "juniperSRX";
	public static final String FIREWALL_JUNIPER_SRX_SHEET = "JuniperSrx";

	//Audit pencentage score constants	
	public static final Integer AUDIT_PERCENTAGE_05=5;
	public static final Integer AUDIT_PERCENTAGE_10=10;
	public static final Integer AUDIT_PERCENTAGE_20=50;


	//Audit Goldenset data sheet names- sheet name points to one firewall log datasource

	//CWS DeviceIds
	public static final String FIREWALL_CWS_MULTIPLE_DEVICE_IDS = "cwsMultipleDeviceIds";
	public static final String CWS_DEVICEID_TSV_SHEET="CWS-DeviceID-TSV";
	public static final String CWS_DEVICEID_TSV_1234567890_SHEET="CWS-DeviceID-1234567890";
	public static final String CWS_DEVICEID_TSV_1234567880_SHEET="CWS-DeviceID-1234567880";
	public static final String CWS_DEVICEID_TSV_1234567860_SHEET="CWS-DeviceID_1234567860";
	public static final String CWS_DEVICEID_TSV_1234567870_SHEET="CWS-DeviceID_1234567870";

	//Cisco WSA Access DeviceIds
	public static final String FIREWALL_WSA_ACCESS_DEVICE_IDS = "wsaaccessDeviceIds";
	public static final String WSA_ACCESS_DEVICE_ID_SHEET="WSA_ACCESS_DeviceID";
	public static final String WSA_ACCESS_DEVICE_ID_192_168_1_0_SHEET="192.168.1.0";
	public static final String WSA_ACCESS_DEVICE_ID_192_168_1_1_SHEET="192.168.1.1";

	//Cisco WSA W3c DeviceIds
	public static final String FIREWALL_WSA_W3C_DEVICE_IDS = "wsaw3cDeviceIds";
	public static final String WSA_W3C_DEVICE_ID_SHEET="WSA_W3C_DeviceID";
	public static final String WSA_W3C_DEVICE_ID_10_0_0_1_SHEET="10.0.0.1";
	public static final String WSA_W3C_DEVICE_ID_10_0_0_2_SHEET="10.0.0.2";
	public static final String WSA_W3C_DEVICE_ID_192_168_1_100_SHEET="192.168.1.100";
	public static final String WSA_W3C_DEVICE_ID_192_168_1_101_SHEET="192.168.1.101";


	//PAN CSV Deviceids
	public static final String FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS = "panCSVMultipleDeviceIds";
	public static final String PAN_CSV_DEVICE_ID_SHEET="PAN-CSV-DeviceID";
	public static final String PAN_CSV_DEVICE_ID_1606015701_SHEET="PAN-CSV-DeviceID-1606015701";
	public static final String PAN_CSV_DEVICE_ID_1606015301_SHEET="PAN-CSV-DeviceID-1606015301";
	public static final String PAN_CSV_DEVICE_ID_1606015501_SHEET="PAN-CSV-DeviceID-1606015501";
	public static final String PAN_CSV_DEVICE_ID_1606015901_SHEET="PAN-CSV-DeviceID-1606015901";
	public static final String PAN_CSV_DEVICE_ID_1606015019_SHEET="PAN-CSV-DeviceID-1606015019";
	public static final String PAN_CSV_DEVICE_ID_1606015401_SHEET="PAN-CSV-DeviceID-1606015401";
	public static final String PAN_CSV_DEVICE_ID_1606015801_SHEET="PAN-CSV-DeviceID-1606015801";
	public static final String PAN_CSV_DEVICE_ID_1606015601_SHEET="PAN-CSV-DeviceID-1606015601";
	public static final String PAN_CSV_DEVICE_ID_1606015201_SHEET="PAN-CSV-DeviceID-1606015201";

	//PAN SYS Deviceids
	public static final String FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS = "panSYSMultipleDeviceIds";
	public static final String PAN_SYS_DEVICE_ID_SHEET="PAN_SYS_DeviceID";
	public static final String PAN_SYS_DEVICE_ID_001901000402_SHEET="PAN-SYS-DeviceID-001901000402";
	public static final String PAN_SYS_DEVICE_ID_001901123456_SHEET="PAN-SYS-DeviceID-001901123456";
	public static final String PAN_SYS_DEVICE_ID_881901123456_SHEET="PAN-SYS-DeviceID-881901123456";
	public static final String PAN_SYS_DEVICE_ID_991901123456_SHEET="PAN-SYS-DeviceID-991901123456";

	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS = "blueCoatProxySGDeviceIds";
	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_SHEET = "BlueCoatDeviceId";
	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_10_0_0_1_SHEET= "AXXA - 10.0.0.1";
	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_1_100_SHEET = "AXXA - 192.168.1.100";
	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_2_100_SHEET= "AXXA - 192.168.2.100";
	public static final String FIREWALL_BLUECOAT_PROXYSG_DEVICE_192_168_3_100_SHEET = "AXXA - 192.168.3.100";



	//Audit Goldenset data sheet columns	
	public static final String GOLDENSETDATA_AUDIT_SCORE="AUDITSCORE";
	public static final String GOLDENSETDATA_AUDIT_SERVICES="SERVICES";
	public static final String GOLDENSETDATA_AUDIT_USERS="USERS";
	public static final String GOLDENSETDATA_AUDIT_DESTINATIONS="DESTINATIONS";
	public static final String GOLDENSETDATA_AUDIT_TOP_RISKY_SERVICES="TOP_RISKY_SERVICES";
	public static final String GOLDENSETDATA_AUDIT_TOP_USED_SERVICES="TOP_USED_SERVICES";
	public static final String GOLDENSETDATA_AUDIT_HIGH_RISKY_SERVICES="HIGH_RISKY_SERVICES";
	public static final String GOLDENSETDATA_AUDIT_MED_RISKY_SERVICES="MED_RISKY_SERVICES";
	public static final String GOLDENSETDATA_AUDIT_LOW_RISKYSERVICES="LOW_RISKY_SERVICES";
	public static final String GOLDENSETDATA_AUDIT_DESTINATION_SESSIONS="DESTINATION_SESSIONS";

	public static final String FIREWALL_BLUECOAT_PROXYSG_INVALID = "blueCoatProxySGInvalid";
	public static final String FIREWALL_ZSCALAR="zscalar";
	public static final String ZSCALAR_DATA_SHEET="ZSCALAR";
	public static final String BARRACUDA_CLI_DATA_SHEET="Barracuda_CLI";
	public static final String BLUECOATPROXY_DATA_SHEET="BlueCoat";
	public static final String BARRACUDA_SYS_DATA_SHEET="Barracuda_Sys";


	public static final String FIREWALL_BE_BARRACUDA_CLI = "be_barracuda_cli";
	public static final String BE_BARRACUDA_CLI_DATA_SHEET="be_barracuda_cli";
	
	public static final String FIREWALL_BE_BARRACUDA_SYS = "be_barracuda_sys";
	public static final String BE_BARRACUDA_SYS_DATA_SHEET="be_barracuda_sys";
	
	public static final String FIREWALL_BE_BLUECOAT_PROXY = "be_bluecoat_proxy";
	public static final String BE_BLUECOAT_PROXY_DATA_SHEET="be_bluecoat_proxy";
	
	
	public static final String FIREWALL_BE_BLUECOATPROXY_CU_HE_SP_ESC = "be_bluecoat_proxy_custom_header_space_escape";
	public static final String FIREWALL_BE_BLUECOATPROXY_CU_HE_QU_HE = "be_bluecoat_proxy_custom_header_quote_header";

	public static final String FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH = "be_bluecoatProxy_splunk_wo_ch";
	public static final String BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET = "be_bluecoatProxy_splunk_wo_ch";
	
	public static final String FIREWALL_BE_BLUECOATPROXY_SPLUNK_MANDATORY_HEADERS = "be_bluecoatProxy_splunk_mandatory_headers";
	//public static final String BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET = "be_bluecoatProxy_splunk_wo_ch";
	
	
	
	
	public static final String FIREWALL_BE_CHECKPOINT_CSV = "be_checkpoint_csv";
	public static final String BE_CHECKPOINT_CSV_DATA_SHEET="be_checkpoint_csv";
	
	
	public static final String FIREWALL_BE_CHECKPOINT_SMARTVIEW = "be_checkpoint_smartview";
	public static final String BE_CHECKPOINT_SMARTVIEW_DATA_SHEET="be_checkpoint_smartview";
	
	public static final String FIREWALL_BE_JUNIPER_SCREENOS = "be_juniperscreenos";
	public static final String BE_JUNIPER_SCREENOS_DATA_SHEET = "be_juniperscreenos";
	
	public static final String FIREWALL_MCAFEE_SEF = "mcafeesef";
	public static final String MCAFEE_SEF_SHEET = "mcafee_sef";

	public static final String FIREWALL_SQUID_PROXY = "squidrpoxy";
	public static final String SQUID_PROXY_SHEET = "squid_proxy";
	
	
	public static final String FIREWALL_BE_PANCSV = "be_pancsv";
	public static final String BE_PANCSV_DATA_SHEET = "be_pancsv";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_WO_CH = "be_pancsv_splunk_wo_ch";
	public static final String BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET = "be_pancsv_splunk_wo_ch";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_CH = "be_pancsv_splunk_ch";
	public static final String BE_PANCSV_SPLUNK_CH_DATA_SHEET = "be_pancsv_splunk_ch";
	
	
	public static final String FIREWALL_BE_WSAW3C = "be_wsaw3c";
	public static final String BE_WSAW3C_DATA_SHEET = "be_wsaw3c";
	
	public static final String FIREWALL_BE_WSA_ACCESS = "be_wsaaccess";
	public static final String BE_WSA_ACCESS_DATA_SHEET = "be_wsaaccess";
	
	public static final String FIREWALL_BE_ZSCALAR = "be_zscalar";
	public static final String BE_ZSCALAR_DATA_SHEET = "be_zscalar";


	//empty log files
	public static final String FIREWALL_WSA_ACCESS_ZIP = "wsaAccessEmpty_zip";
	public static final String FIREWALL_WSA_ACCESS_GZ = "wsaAccessEmpty_gz";
	public static final String FIREWALL_WSA_ACCESS_BZ2 = "wsaAccessEmpty_bz2";

	//headeronly log files
	public static final String FIREWALL_CWS_HEADERONLY_ZIP = "cwsHeaderOnly_zip";
	public static final String FIREWALL_CWS_HEADERONLY_BZ2 = "cwsHeaderOnly_gz";
	public static final String FIREWALL_CWS_HEADERONLY_GZ = "cwsHeaderOnly_bz2";

	//image log files
	public static final String FIREWALL_CISCO_ASA_IMG_ZIP = "ciscoasaJpeg_zip";
	public static final String FIREWALL_CISCO_ASA_IMG_BZ2 = "ciscoasaJpeg_gz";
	public static final String FIREWALL_CISCO_ASA_IMG_GZ = "ciscoasaJpeg_bz2";

	// syslog log files
	public static final String CISCO_ASA_SYSLOG = "CiscoASASeries_syslog"; 
	public static final String PAN_SYSLOG = "pan_syslog";
	public static final String WSA_SYSLOG = "wsa_syslog";
	
	
	//exe log file
	public static final String FIREWALL_PAN_CSV_EXE_ZIP = "pancsvExe_zip";
	public static final String FIREWALL_PAN_CSV_EXE_GZ = "pancsvExe_gz";
	public static final String FIREWALL_PAN_CSV_EXE_BZ2 = "pancsvExe_bz2";

	//pancsv invalid files
	public static final String FIREWALL_PAN_TRAFFIC_FILE = "panTrafficOnlyZip";
	public static final String FIREWALL_PAN_URL_FILE = "panUrlOnlyZip";

	//fileformats
	public static final String FILE_FORMAT_ZIP="Zip";
	public static final String FILE_FORMAT_GZ="Gz";
	public static final String FILE_FORMAT_BZ2="Bz2";

	//
	public static final String FIREWALL_FILE_WITH_SLASH="blueCoatFileWithSlash";

	//TIA Logs
	public static final String FIREWALL_TIA_MCAFEE_NORMAL_LOWIP="tiaMcaNormalLow";

	// constants for multifiles scp
	public static final String FIREWALL_WSA_ACCESS_HEADER_ONLY = "wsaAccessHeaderOnly";

	//
	public static final String FIREWALL_MCA_WEBGATEWAY_NEGATIVE = "mcawebgatewaynegative";

	public static final String TIA_FIREWALL_MCA = "tiamca";
	public static final String TIA_FIREWALL_MCA2 = "tiamca2";
	public static final String TIA_FIREWALL_MCA2_USER="UserTIA03 Hilton";
	
	public static final String TIA_FIREWALL_BC_ULDL_BBI = "bc_upload_download_BBI";
	public static final String TIA_FIREWALL_BC_ULDL_TBI = "bc_upload_download_TBI";
	
	public static final String TIA_FIREWALL_BC_ULDL_TBI_BBI = "bc_upload_download_TBI_BBI";
	public static final String TIA_FIREWALL_BC_ULDL_TBI_BBI_USER="BC26";
	
	
	public static final String TIA_FIREWALL_BC_NO_SESSIONS_TBI = "bc_nosessions_TBI_BBI";
	public static final String TIA_FIREWALL_BC_NO_SESSIONS_TBI_USER="BC03";
	
	public static final String TIA_COMPRESSED_FILES_PATH ="/src/test/resources/Audit/LogFiles/TIACompressedFiles/";
	public static final String TIA_LOG_FILES_PATH ="/src/test/resources/Audit/LogFiles/TIALogs/";
	
	public static final String TBI_INCIDENT_DOWNLOADS="TOO_MANY_SUM_LARGE_DOWNLOADS";
	public static final String TBI_INCIDENT_UPLOADS="TOO_MANY_SUM_LARGE_UPLOADS";
	public static final String BBI_INCIDENT_ANAMALOUSLY_DOWNLOADS="ANOMALOUSLY_LARGE_DOWNLOAD";
	public static final String BBI_INCIDENT_ANAMALOUSLY_UPLOADS="ANOMALOUSLY_LARGE_UPLOAD";
	
	
	
	public static final String SCP_COMPLETED = "scpuploadcompleted";
	

	
	
	//7z format constants
	public static final String FIREWALL_BE_BARRACUDA_CLI_7Z = "be_barracuda_cli_7z";
	public static final String BE_BARRACUDA_CLI_DATA_SHEET_7Z="be_barracuda_cli";
	
	public static final String FIREWALL_BE_BARRACUDA_SYS_7Z = "be_barracuda_sys_7z";
	public static final String BE_BARRACUDA_SYS_DATA_SHEET_7Z="be_barracuda_sys";
	
	public static final String FIREWALL_BE_BLUECOAT_PROXY_7Z = "be_bluecoat_proxy_7z";
	public static final String FIREWALL_BE_BLUECOAT_PROXY_BZ2 = "be_bluecoat_proxy_bz2";
	public static final String FIREWALL_BE_BLUECOAT_PROXY_GZ = "be_bluecoat_proxy_gz";
	public static final String BE_BLUECOAT_PROXY_DATA_SHEET_7Z="be_bluecoat_proxy";
	
	public static final String FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z = "be_bluecoatProxy_splunk_wo_ch_7z";
	public static final String BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET_7Z = "be_bluecoatProxy_splunk_wo_ch";
	
	
	public static final String FIREWALL_BE_CHECKPOINT_CSV_7Z = "be_checkpoint_csv_7z";
	public static final String BE_CHECKPOINT_CSV_DATA_SHEET_7Z="be_checkpoint_csv";
	
	
	public static final String FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z = "be_checkpoint_smartview_7z";
	public static final String BE_CHECKPOINT_SMARTVIEW_DATA_SHEET_7Z="be_checkpoint_smartview";
	
	public static final String FIREWALL_BE_JUNIPER_SCREENOS_7Z = "be_juniperscreenos_7z";
	public static final String BE_JUNIPER_SCREENOS_DATA_SHEET_7Z = "be_juniperscreenos";
	
	public static final String FIREWALL_MCAFEE_SEF_7Z = "mcafeesef_7z";
	public static final String MCAFEE_SEF_SHEET_7Z = "mcafee_sef";

	public static final String FIREWALL_SQUID_PROXY_7Z = "squidrpoxy_7z";
	public static final String SQUID_PROXY_SHEET_7Z = "squid_proxy";
	
	public static final String FIREWALL_BE_PANCSV_7Z = "be_pancsv_7z";
	public static final String FIREWALL_BE_PANCSV_BZ2 = "be_pancsv_bz2";
	public static final String FIREWALL_BE_PANCSV_GZ = "be_pancsv_gz";
	
	public static final String BE_PANCSV_DATA_SHEET_7Z = "be_pancsv";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z = "be_pancsv_splunk_wo_ch_7z";
	public static final String BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET_7Z = "be_pancsv_splunk_wo_ch";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_CH_7Z = "be_pancsv_splunk_ch_7z";
	public static final String BE_PANCSV_SPLUNK_CH_DATA_SHEET_7Z = "be_pancsv_splunk_ch";
	
	
	public static final String FIREWALL_BE_WSAW3C_7Z = "be_wsaw3c_7z";
	public static final String FIREWALL_BE_WSAW3C_BZ2 = "be_wsaw3c_bz2";
	public static final String FIREWALL_BE_WSAW3C_GZ = "be_wsaw3c_gz";
	
	public static final String BE_WSAW3C_DATA_SHEET_7Z = "be_wsaw3c";
	
	public static final String FIREWALL_BE_WSA_ACCESS_7Z = "be_wsaaccess_7z";
	public static final String BE_WSA_ACCESS_DATA_SHEET_7Z = "be_wsaaccess";
	
	public static final String FIREWALL_BE_ZSCALAR_7Z = "be_zscalar_7z";
	public static final String FIREWALL_BE_ZSCALAR_BZ2 = "be_zscalar_bz2";
	public static final String FIREWALL_BE_ZSCALAR_GZ = "be_zscalar_gz";
	
	public static final String BE_ZSCALAR_DATA_SHEET_7Z = "be_zscalar";
	
	public static final String FIREWALL_WALLMART_PAN_CSV_7Z = "wallmartPanCSV_7z";
	public static final String FIREWALL_WALLMART_PAN_CSV_SHEET_7Z = "WALLMART-PAN-CSV";
	
	public static final String FIREWALL_WALLMART_PAN_SYS_7Z = "wallmartPanSYS_7z";
	public static final String FIREWALL_WALLMART_PAN_SYS_SHEET_7Z = "WALLMART-PAN-SYS";
	
	public static final String FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7Z = "wallmartWebGateWayLog_7z";
	public static final String FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2 = "wallmartWebGateWayLog_bz2";
	public static final String FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_GZ = "wallmartWebGateWayLog_gz";
	
	public static final String FIREWALL_JUNIPER_SRX_7Z = "juniperSRX_7z";
	public static final String FIREWALL_JUNIPER_SRX_SHEET_7Z = "JuniperSrx";
	
	public static final String FIREWALL_SCANSAFE_7Z = "scanSafe_7z";
	public static final String FIREWALL_SCANSAFE_SHEET_7Z = "ScanSafe";
	
	public static final String FIREWALL_CISCO_ASA_SERIES_7Z = "CiscoASASeries_7z";
	public static final String FIREWALL_CISCO_ASA_SERIES_BZ2 = "CiscoASASeries_bz2";
	public static final String FIREWALL_CISCO_ASA_SERIES_GZ = "CiscoASASeries_gz";
	public static final String FIREWALL_CISCO_ASA_SERIES_SHEET = "CiscoASASeries";
	

	//7za formats
	public static final String FIREWALL_BE_BARRACUDA_CLI_7ZA = "be_barracuda_cli_7za";
	public static final String BE_BARRACUDA_CLI_DATA_SHEET_7ZA="be_barracuda_cli";
	
	public static final String FIREWALL_BE_BARRACUDA_SYS_7ZA = "be_barracuda_sys_7za";
	public static final String BE_BARRACUDA_SYS_DATA_SHEET_7ZA="be_barracuda_sys";
	
	public static final String FIREWALL_BE_BLUECOAT_PROXY_7ZA = "be_bluecoat_proxy_7za";
	public static final String BE_BLUECOAT_PROXY_DATA_SHEET_7ZA="be_bluecoat_proxy";
	
	public static final String FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7ZA = "be_bluecoatProxy_splunk_wo_ch_7za";
	public static final String BE_BLUECOATPROXY_SPLUNK_WO_CH_DATA_SHEET_7ZA = "be_bluecoatProxy_splunk_wo_ch";
	
	
	public static final String FIREWALL_BE_CHECKPOINT_CSV_7ZA = "be_checkpoint_csv_7za";
	public static final String BE_CHECKPOINT_CSV_DATA_SHEET_7ZA="be_checkpoint_csv";
	
	
	public static final String FIREWALL_BE_CHECKPOINT_SMARTVIEW_7ZA = "be_checkpoint_smartview_7za";
	public static final String BE_CHECKPOINT_SMARTVIEW_DATA_SHEET_7ZA="be_checkpoint_smartview";
	
	public static final String FIREWALL_BE_JUNIPER_SCREENOS_7ZA = "be_juniperscreenos_7za";
	public static final String BE_JUNIPER_SCREENOS_DATA_SHEET_7ZA = "be_juniperscreenos";
	
	public static final String FIREWALL_MCAFEE_SEF_7ZA = "mcafeesef_7za";
	public static final String MCAFEE_SEF_SHEET_7ZA = "mcafee_sef";

	public static final String FIREWALL_SQUID_PROXY_7ZA = "squidrpoxy_7za";
	public static final String SQUID_PROXY_SHEET_7ZA = "squid_proxy";
	
	public static final String FIREWALL_BE_PANCSV_7ZA = "be_pancsv_7za";
	public static final String BE_PANCSV_DATA_SHEET_7ZA = "be_pancsv";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7ZA = "be_pancsv_splunk_wo_ch_7za";
	public static final String BE_PANCSV_SPLUNK_WO_CH_DATA_SHEET_7ZA = "be_pancsv_splunk_wo_ch";
	
	public static final String FIREWALL_BE_PANCSV_SPLUNK_CH_7ZA = "be_pancsv_splunk_ch_7za";
	public static final String BE_PANCSV_SPLUNK_CH_DATA_SHEET_7ZA = "be_pancsv_splunk_ch";
	
	
	public static final String FIREWALL_BE_WSAW3C_7ZA = "be_wsaw3c_7za";
	public static final String BE_WSAW3C_DATA_SHEET_7ZA = "be_wsaw3c";
	
	public static final String FIREWALL_BE_WSA_ACCESS_7ZA = "be_wsaaccess_7za";
	public static final String BE_WSA_ACCESS_DATA_SHEET_7ZA = "be_wsaaccess";
	
	public static final String FIREWALL_BE_ZSCALAR_7ZA = "be_zscalar_7za";
	public static final String BE_ZSCALAR_DATA_SHEET_7ZA = "be_zscalar";
	
	public static final String FIREWALL_WALLMART_PAN_CSV_7ZA = "wallmartPanCSV_7za";
	public static final String FIREWALL_WALLMART_PAN_CSV_SHEET_7ZA = "WALLMART-PAN-CSV";
	
	public static final String FIREWALL_WALLMART_PAN_SYS_7ZA = "wallmartPanSYS_7za";
	public static final String FIREWALL_WALLMART_PAN_SYS_SHEET_7ZA = "WALLMART-PAN-SYS";
	
	public static final String FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_7ZA = "wallmartWebGateWayLog_7za";
	
	public static final String FIREWALL_JUNIPER_SRX_7ZA = "juniperSRX_7za";
	public static final String FIREWALL_JUNIPER_SRX_SHEET_7ZA = "JuniperSrx";
	
	public static final String FIREWALL_SCANSAFE_7ZA = "scanSafe_7za";
	public static final String FIREWALL_SCANSAFE_SHEET_7ZA = "ScanSafe";
	
	public static final String FIREWALL_CISCO_ASA_SERIES_7ZA = "CiscoASASeries_7za";
	
	
	public static final String FIREWALL_WEBSENSE_ARC = "websense_arc";
	public static final String FIREWALL_WEBSENSE_ARC_SHEET = "websense_arc";
	
	public static final String FIREWALL_WEBSENSE_HOSTED = "websense_hosted";
	public static final String FIREWALL_WEBSENSE_HOSTED_7Z = "websense_hosted_7z";
	public static final String FIREWALL_WEBSENSE_HOSTED_BZ2 = "websense_hosted_bz2";
	public static final String FIREWALL_WEBSENSE_HOSTED_GZ = "websense_hosted_gz";
	public static final String FIREWALL_WEBSENSE_HOSTED_SHEET = "websense_hosted";
	
	public static final String FIREWALL_WEBSENSE_ARC_TAR = "websense_arc_tar";
	public static final String FIREWALL_WEBSENSE_ARC_TAR_SHEET = "websense_arc_tar";
	
	
	
	
	
	public static final String FIREWALL_SONICWALL = "sonicwall";
	public static final String FIREWALL_SONICWALL_SHEET = "sonicwall";
	
	
	//Reporting Dashboard constants
	public static final String REPORT_SOURCE_DATA_PAYLOAD_REQUEST_FILE_NAMES="/src/test/resources/Audit/reportsourcedatapayloadrequests/reportsourcedataconfiguration.properties";
	public static final String REPORT_SOURCE_DATA_SERVICE_COUNT = "services_count";
	public static final String REPORT_SOURCE_DATA_TOP5_SERVICES_GROUPBY_BRR = "top5_services_groupby_brr";
	
	
	//7za format constants
	
	

	public static List<String> tenantsListForAparajiServicesCheck(){
		
		List<String> tenantsList=new ArrayList<String>();
		//eoe tenants
		tenantsList.add("auditwebuploadcom");
		tenantsList.add("auditbe1com");
		tenantsList.add("auditbecom");
		tenantsList.add("auditdeviceidco");
		tenantsList.add("auditspanva1com");
		tenantsList.add("auditweeklyscpco");
		tenantsList.add("auditbescpco");
		tenantsList.add("s32elasticaco");
		tenantsList.add("eoeauditanonyenabledco");
		tenantsList.add("spanvatestanyenacom");
		
		
		//for cha tenants
		tenantsList.add("auditddd2com");
		
		
		//for upgrades tests
		tenantsList.add("spanonydis1co");
		tenantsList.add("spanonydis2co");
		tenantsList.add("spanonydis3co");
		tenantsList.add("spanonydis4co");
		tenantsList.add("spanonydis5co");
		
		
	
		//cep tenants
		tenantsList.add("gatewayo365beatlecom");
		tenantsList.add("securletbeatlecom");
		
		//prod tenant
		tenantsList.add("protecto365autobeatlecom");
		tenantsList.add("elasticaqanet");
		tenantsList.add("auditregbeatlecom");
		tenantsList.add("protectbeatlecom");
		
		
		
		//qavpc tenants:
		tenantsList.add("vpcauditwuco");
		tenantsList.add("vpcaudits3co");
		
		
		
		return tenantsList;

	}

	public static List<String> getFirewallsList()
	{
		List<String> firewallLogsList=new ArrayList<String>();

		firewallLogsList.add(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV);
		firewallLogsList.add(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS);
		firewallLogsList.add(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY);
		firewallLogsList.add(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG);
		firewallLogsList.add(AuditTestConstants.FIREWALL_ZSCALAR);
		firewallLogsList.add(AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG);
		firewallLogsList.add(AuditTestConstants.FIREWALL_CHECKPOINT_CSV);
		firewallLogsList.add(AuditTestConstants.FIREWALL_CHECKPOINT_SMARTVIEW);
		firewallLogsList.add(AuditTestConstants.FIREWALL_JUNIPER_SRX);
		firewallLogsList.add(AuditTestConstants.FIREWALL_SCANSAFE);
		//GS results are not available for the below logs
		firewallLogsList.add(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES);
		firewallLogsList.add(AuditTestConstants.FIREWALL_JUNIPER_SCREENOS);
		firewallLogsList.add(AuditTestConstants.FIREWALL_CISCO_WSA_W3C);
		firewallLogsList.add(AuditTestConstants.FIREWALL_PALOALTO_CSV);
		firewallLogsList.add(AuditTestConstants.FIREWALL_CISCO_CWS);

		return firewallLogsList;
	}


	public static final String FIREWALL_BE_ZSCALER_TEST = "be_zscler_test";


}
