/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class CepAuditScpUploadFactory {
	
	/**
	 * @return
	 */
	@Factory
	public Object[] ScpUploadFactoryMethod(){
		return new Object[]{
				
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditScpTests(AuditTestConstants.FIREWALL_SQUID_PROXY),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_WSA_ACCESS),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_ZSCALAR)
			
		};
	}
}