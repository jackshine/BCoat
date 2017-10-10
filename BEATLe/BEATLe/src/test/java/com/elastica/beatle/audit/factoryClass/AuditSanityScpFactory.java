/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;


import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditScpSanityTests2;

/**
 * @author Mallesh
 *
 */
public class AuditSanityScpFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] SCPFactoryMethod(){
		return new Object[]{
														
				new AuditScpSanityTests2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY)
				
		};
	}
}