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
public class AuditSpanvaLatestImageScpClient7ZFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageScpClient7ZFactoryMethod(){
		return new Object[]{
		
				
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
				new AuditSpanVAScpClientTests(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z)
				
				
		};
	}
}
