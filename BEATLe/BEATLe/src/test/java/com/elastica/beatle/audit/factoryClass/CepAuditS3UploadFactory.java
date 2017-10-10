/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSUploadWithS3Tests;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class CepAuditS3UploadFactory {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_SCANSAFE),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES)
				
		};
	}
}