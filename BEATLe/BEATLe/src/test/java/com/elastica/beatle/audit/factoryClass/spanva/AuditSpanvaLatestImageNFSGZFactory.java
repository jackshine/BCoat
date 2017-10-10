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
public class AuditSpanvaLatestImageNFSGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageNFSGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
