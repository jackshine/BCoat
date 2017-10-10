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
public class AuditSpanvaLatestImageNFSZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageNFSBZ2FactoryMethod(){
		return new Object[]{
		
				new AuditSpanVANetworkFileTests(AuditTestConstants.FIREWALL_WEBSENSE_HOSTED)
		};
	}
}
