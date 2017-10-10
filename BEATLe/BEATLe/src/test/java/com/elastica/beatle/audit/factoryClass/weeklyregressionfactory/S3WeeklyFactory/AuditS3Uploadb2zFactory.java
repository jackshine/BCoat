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
public class AuditS3Uploadb2zFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_BZ2),				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_BZ2),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_BZ2),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_BZ2),
				
			    new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_BZ2),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_BZ2),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_BZ2),
				
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_BZ2), 
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_BZ2), 
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_BZ2)
				
				
				
		};
	}
}
