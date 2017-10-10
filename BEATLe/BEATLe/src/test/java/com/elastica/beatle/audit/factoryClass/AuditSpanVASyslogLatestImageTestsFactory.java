/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.spanvatests.AuditSpanVASyslogLatestImageTests;

/**
 * @author Anuvrath
 *
 */
public class AuditSpanVASyslogLatestImageTestsFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] DeviceLogsUploadFactoryMethod() {
		return new Object[] { 
				new AuditSpanVASyslogLatestImageTests(AuditTestConstants.PAN_SYSLOG),
//				new AuditSpanVASyslogLatestImageTests(AuditTestConstants.WSA_SYSLOG),// Need to fix this file
				new AuditSpanVASyslogLatestImageTests(AuditTestConstants.CISCO_ASA_SYSLOG) };
	}
}