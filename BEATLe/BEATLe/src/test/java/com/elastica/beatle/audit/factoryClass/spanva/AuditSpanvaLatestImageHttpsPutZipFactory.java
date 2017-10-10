/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPutTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageHttpsPutZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPutZipFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAHttpsPutTests(AuditTestConstants.FIREWALL_BE_ZSCALAR)
				
				
		};
	}
}
