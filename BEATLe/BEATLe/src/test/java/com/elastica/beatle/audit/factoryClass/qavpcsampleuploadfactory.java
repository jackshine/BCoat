/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;

import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditInitializeTests;
import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditDSUploadWithS3Tests;
import com.elastica.beatle.tests.audit.AuditS3DeviceLogsTests;
import com.elastica.beatle.tests.audit.AuditScpDeviceLogsTests;
import com.elastica.beatle.tests.audit.AuditScpTests;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
import com.elastica.beatle.tests.audit.AuditWebuploadDeviceLogsTests;
/**
 * @author mallesh
 *
 */
public class qavpcsampleuploadfactory {
	/**
	 * @return
	 */
	@Factory
	public Object[] QavpcsampleuploadfactoryMethod(){
		return new Object[]{
				
				
				new AuditWebuploadDeviceLogsTests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditWebuploadDeviceLogsTests(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BE_ZSCALAR),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_JUNIPER_SRX),

				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS),
				new AuditScpDeviceLogsTests(AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_CHECKPOINT_SMARTVIEW),
				new AuditScpTests(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS),

				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_WSAW3C),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditDSUploadWithS3Tests(AuditTestConstants.FIREWALL_BE_PANCSV_SPLUNK_WO_CH),
				new AuditS3DeviceLogsTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS)
			
		
		};
	}
}