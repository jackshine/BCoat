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
public class AuditWebUploadzipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP),

		};
	}
}
