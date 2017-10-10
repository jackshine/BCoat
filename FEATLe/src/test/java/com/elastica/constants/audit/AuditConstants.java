package com.elastica.constants.audit;

import java.io.File;

/**
 * Audit Constants
 * @author Eldo Rajan
 *
 */
public class AuditConstants  {

	
	public static String cwsScriptLocation=
			System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+
			"resources"+File.separator+"data"+File.separator+"audit"+File.separator+"traffic_generator.sh";
	public static String AUDIT_FIREWALL_CONFIG_FILEPATH = "/src/test/resources/audit/AuditConfigurations/LogFileAttribute.xml";
	public static String AUDIT_WEBUPLOAD_FILEFORMAT = "File";
	public static String AUDIT_API_CONFIGURATION_FILEPATH = "/src/test/resources/Audit/AuditConfigurations/AuditAPIConfigurations.xml";
	public static String AUDIT_WU_DS_NAME="wu";
	

}