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
public class AuditSpanvaLatestImageFtpClientGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageFtpClientGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
