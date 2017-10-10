/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAScpClientTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageScpClientGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpClientGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
