/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVANetworkFileTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageNFS7ZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageNFS7ZFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z)
				
				
		};
	}
}
