/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.ScpWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionScpUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditScpUploadgzFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ),
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ),				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ),

				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ),
				
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ),
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ), 
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ), 
				new AuditWeeklyRegressionScpUploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ)
				
		};
	}
}
