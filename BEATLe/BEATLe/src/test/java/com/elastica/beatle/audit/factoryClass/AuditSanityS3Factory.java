/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;


import com.elastica.beatle.audit.AuditTestConstants;

import com.elastica.beatle.tests.audit.AuditS3SanityTests2;

/**
 * @author Mallesh
 *
 */
public class AuditSanityS3Factory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] S3FactoryMethod(){
		return new Object[]{
														
				new AuditS3SanityTests2(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY)
				
		};
	}
}