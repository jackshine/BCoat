/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditSpanVAUploadTests;


/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageScpZipFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpZipFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_ZSCALAR)
				
				
		};
	}
}
