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
public class AuditSpanvaLatestImageScpGZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpGZFactoryMethod(){
		return new Object[]{
		
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAUploadTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
		};
	}
}
