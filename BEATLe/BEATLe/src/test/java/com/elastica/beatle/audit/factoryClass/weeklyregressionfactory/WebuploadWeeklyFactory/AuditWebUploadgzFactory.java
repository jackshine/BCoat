/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.WebuploadWeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionWebUploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditWebUploadgzFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ),				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ),

				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ),
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ),
				
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ), //  due to data
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ), //  due to data
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ), //  due to data
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_GZ), //  due to data
				new AuditWeeklyRegressionWebUploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ), //  due to data
				
		};
	}
}
