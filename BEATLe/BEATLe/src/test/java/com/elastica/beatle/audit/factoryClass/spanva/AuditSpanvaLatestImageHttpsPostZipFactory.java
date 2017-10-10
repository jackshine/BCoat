/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAHttpsPostTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageHttpsPostZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageHttpsPostZipFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAHttpsPostTests(AuditTestConstants.FIREWALL_BE_ZSCALAR)
				
				
		};
	}
}
