/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.S3WeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionS3UploadTests;



/**
 * @author anuvrath
 *
 */
public class AuditS3UploadgzFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_GZ),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_GZ),				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_GZ),

				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_GZ),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_GZ),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_GZ), 
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_GZ), 
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_GZ)
				
				
		};
	}
}
