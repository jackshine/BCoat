/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;
import org.testng.annotations.Factory;
import com.elastica.beatle.audit.AuditTestConstants;


/**
 * @author Mallesh
 *
 */
public class AuditDummyS3UploadFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3FactoryMethod(){
		return new Object[]{
				
					
				/*new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_MCAFEE_SEF),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_PANCSV),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditDSUploadWithS3Tests_2(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES)*/
				
				
		};
	}
}
