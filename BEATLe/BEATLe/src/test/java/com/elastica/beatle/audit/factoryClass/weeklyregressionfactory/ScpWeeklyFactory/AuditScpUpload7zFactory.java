/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.ScpWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionScpUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditScpUpload7zFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
			    new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z),
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z),
		
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z),
			
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z),
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z)
				
				
		};
	}
}
