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
public class AuditSpanvaLatestImageScpClientZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpClientBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
		};
	}
}
