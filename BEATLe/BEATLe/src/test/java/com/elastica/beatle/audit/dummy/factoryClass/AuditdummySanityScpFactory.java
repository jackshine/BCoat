/**
 * 
 */
package com.elastica.beatle.audit.dummy.factoryClass;

import org.testng.annotations.Factory;


import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpSanityTests2;

/**
 * @author Mallesh
 *
 */
public class AuditdummySanityScpFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPFactoryMethod(){
		return new Object[]{
														
				new AuditdummyScpSanityTests(AuditTestConstants.FIREWALL_BE_BARRACUDA_CLI)
				
		};
	}
}