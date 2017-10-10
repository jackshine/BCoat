/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditS3DeviceLogsTests;
import com.elastica.beatle.tests.audit.AuditScpDeviceLogsTests;
import com.elastica.beatle.tests.audit.AuditWebuploadDeviceLogsTests;
/**
 * @author mallesh
 *
 */
public class CEPAuditDeviceLogsUploadFactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] DeviceLogsUploadFactoryMethod(){
		return new Object[]{
				
				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS),
				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS),
				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS),
				new AuditS3DeviceLogsTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS)
				
		
		};
	}
}