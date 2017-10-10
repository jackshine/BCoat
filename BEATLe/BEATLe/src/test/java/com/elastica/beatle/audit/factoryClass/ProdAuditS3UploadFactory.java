/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSUploadWithS3Tests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
/**
 * @author anuvrath
 *
 */
public class ProdAuditS3UploadFactory extends AuditInitializeTests {
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CISCO_WSA_W3C)
						
		};
	}
}