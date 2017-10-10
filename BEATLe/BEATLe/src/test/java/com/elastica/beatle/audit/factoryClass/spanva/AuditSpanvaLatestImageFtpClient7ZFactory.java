/**
 * 
 */
package com.elastica.beatle.audit.factoryClass.spanva;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.audit.AuditWeeklyRegressionConstants;
import com.elastica.beatle.tests.audit.WeeklyRegressionTests.AuditWeeklyRegressionScpUploadTests;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVAFtpClientTests;

/**
 * @author Mallesh
 *
 */
public class AuditSpanvaLatestImageFtpClient7ZFactory {

	/**
	 * @return
	 */
	@Factory
	public Object[] SpanvaLatestImageFtpClient7ZFactoryMethod() {
		return new Object[] { 
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_BLUECOAT_PROXY_7Z),
				new AuditSpanVAFtpClientTests(AuditTestConstants.FIREWALL_BE_ZSCALAR_7Z),
		        new AuditSpanVAFtpClientTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_QUOTE_HEADER_7Z),
		        new AuditSpanVAFtpClientTests(AuditWeeklyRegressionConstants.BLUECOAT_PROXY_CUSTOM_HEADER_SPACE_HEADER_7Z)
		
		};
	}
}
