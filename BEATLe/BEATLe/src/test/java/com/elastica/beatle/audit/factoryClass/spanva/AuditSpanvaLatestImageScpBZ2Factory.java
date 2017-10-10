/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageScpBZ2Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2)
		};
	}
}
