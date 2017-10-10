/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class ProdAuditWebUploadFactory extends AuditInitializeTests {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				//new AuditPreferenceTests(),												
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_BZ2),
		        new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_GZ),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_JUNIPER_SCREENOS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_JUNIPER_SRX)
				
				
		};
	}
}