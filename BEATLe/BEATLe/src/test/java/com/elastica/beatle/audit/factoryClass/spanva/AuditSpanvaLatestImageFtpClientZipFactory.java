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
public class AuditSpanvaLatestImageFtpClientZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageFtpClientBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
		};
	}
}
