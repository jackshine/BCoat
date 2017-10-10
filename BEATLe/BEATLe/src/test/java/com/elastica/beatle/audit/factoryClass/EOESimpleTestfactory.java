/**
 * 
 */
package com.elastica.beatle.audit.factoryClass;
import org.testng.annotations.Factory;

import com.elastica.beatle.audit.AuditTestConstants;
import com.elastica.beatle.tests.audit.AuditWebUploadTests;
import com.elastica.beatle.tests.audit.AuditWebuploadDeviceLogsTests;
import com.elastica.beatle.tests.audit.EoeSimpleTests;

public class EOESimpleTestfactory{
	
	/**
	 * @return
	 */
	@Factory
	public Object[] WebUploadFactoryMethod(){
		return new Object[]{
			
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_PAN_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_PAN_SYS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WALLMART_MCAFEE_WEB_GATEWAY),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_ZSCALAR),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CHECKPOINT_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CHECKPOINT_SMARTVIEW),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_JUNIPER_SRX),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_SCANSAFE),
				//GS results are not available for the below logs
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CISCO_ASA_SERIES),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_JUNIPER_SCREENOS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CISCO_WSA_W3C),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_PALOALTO_CSV),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CISCO_CWS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_CWS_MULTIPLE_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_PAN_CSV_MULTIPLE_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_PAN_SYS_MULTIPLE_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_WSA_ACCESS_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BLUECOAT_PROXYSG_DEVICE_IDS),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_ZSCALAR),
				new AuditWebUploadTests(AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG)
				
		};
	}
}
