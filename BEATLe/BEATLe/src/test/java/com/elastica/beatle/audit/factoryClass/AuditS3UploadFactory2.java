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
public class AuditS3UploadFactory2{
	/**
	 * @return
	 */
	@Factory
	public Object[] S3UploadFactoryMethod(){
		return new Object[]{
				
				    new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_SCANSAFE),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WEBSENSE_ARC),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_SONICWALL),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
					
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_SP_ESC),
					new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_CU_HE_QU_HE),
				
					
		};
	}
}