/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAFtpClientTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAScpClientTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageFtpCientBZ2Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageFtpClientBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY_BZ2),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES_BZ2)
				
		};
	}
}
