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
public class AuditS3Upload7zFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
			    new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_7Z),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_7Z),
		
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_7Z),
			
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_7Z),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_7Z),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_7Z)
				
				
				
				
		};
	}
}
