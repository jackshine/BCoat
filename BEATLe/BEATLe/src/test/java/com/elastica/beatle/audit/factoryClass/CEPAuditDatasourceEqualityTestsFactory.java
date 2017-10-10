/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSEqualityTests;
import com.elastica.beatle.tests.audit.AuditPreferenceTests;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;

/**
 * @author Mallesh
 *
 */
public class CEPAuditDatasourceEqualityTestsFactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] DataSourceEqualityTestsFactoryMethod(){
		return new Object[]{
				new AuditDSEqualityTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG)
		};
	}
}