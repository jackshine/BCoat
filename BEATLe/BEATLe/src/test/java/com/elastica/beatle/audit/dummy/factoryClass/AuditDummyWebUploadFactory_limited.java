/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;


/**
 * @author Mallesh
 *
 */
public class AuditDummyWebUploadFactory_limited{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY),
				new AuditWebUploadTests_2(AuditTestConstants.FIREWALL_BE_BLUECOATPROXY_SPLUNK_WO_CH),
				
				
				
		};
	}
}
