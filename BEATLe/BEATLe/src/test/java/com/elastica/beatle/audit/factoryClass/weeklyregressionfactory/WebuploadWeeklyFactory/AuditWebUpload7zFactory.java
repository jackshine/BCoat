/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.WebuploadWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditWebUpload7zFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				
				
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
			    new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z),
		
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z),
			
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z),

		};
	}
}
