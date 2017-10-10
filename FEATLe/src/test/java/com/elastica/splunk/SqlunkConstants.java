/**
 * 
 */
package com.elastica.splunk;

/**
 * @author anuvrath
 *
 */
public class SqlunkConstants {
	public static enum SplunkHosts{
		PROD("host=prd*"), QAVPC("host=qa-vpc*"), EOE("host=eoe*"), CEP("host=cep-*"),EMAILPROD("host=p-*");
		
		private String envHost;
		 
		private SplunkHosts(String env) {
			envHost = env;
		}
 
		public String getHost() {
			return envHost;
		}
	}
	
	public static enum ServiceLogs{
		CI("source=/var/log/elastica/contentinspec*"),
		EMAIL("source=/var/log/elastica/sendmail_service.log"),
		CELERY("source=/var/log/elastica/celery/*"),
		REPORTERWORKER("source=/var/log/elastica/celery/report-workers.log"),
		GW("source=/var/log/elastica/gateway/*");
		
		private String service;
		 
		private ServiceLogs(String serviceName) {
			service = serviceName;
		}
 
		public String getServiceLogPath() {
			return service;
		}
	}
}
