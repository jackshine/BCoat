/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.weeklyregressionfactory.S3WeeklyFactory;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionS3UploadTests;


public class AuditS3UploadzipFactory{

	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
			
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BARRACUDA_SYS_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOAT_PROXY_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CHECKPOINT_CSV_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW_ZIP),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_JUNIPER_SCREENOS_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_MCAFEE_SEF_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SQUID_PROXY_ZIP),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSAW3C_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_WSA_ACCESS_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_BE_ZSCALAR_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_JUNIPER_SRX_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SCANSAFE_ZIP),
				
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_CISCO_ASA_SERIES_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_ARC_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_SONICWALL_ZIP),
				new AuditWeeklyRegressionS3UploadTests(AuditWeeklyRegressionConstants.FIREWALL_WEBSENSE_HOSTED_ZIP)
				
				
		};
	}
}
