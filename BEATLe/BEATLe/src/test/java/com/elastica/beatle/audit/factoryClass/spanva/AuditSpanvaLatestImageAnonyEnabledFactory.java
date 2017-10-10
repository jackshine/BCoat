 /**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAAnonymizeTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAFtpClientTests;

/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageAnonyEnabledFactory {

	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageAnomalyEnabledFactoryMethod() {
		return new Object[] { 
				new AuditSpanVAAnonymizeTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
				new AuditSpanVAAnonymizeTests(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z),
				new AuditSpanVAAnonymizeTests(AuditTestConstants.FIREWALL_BE_PANCSV_GZ),
				new AuditSpanVAAnonymizeTests(AuditTestConstants.FIREWALL_BE_WSAW3C_GZ)
				};
	}
}
