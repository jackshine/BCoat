/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSUploadWithS3Tests;
import com.elastica.beatle.tests.audit.AuditScpTests;

/**
 * @author anuvrath
 *
 */
public class AuditS3UploadFactory{
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
		        new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
		        new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
		        new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2),
		        new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
		        new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_JUNIPER_SRX)
					
		};
	}
}