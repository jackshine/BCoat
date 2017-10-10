/**
 * 
 */
package com.elastica.beatle.splunk;

/**
 * @author anuvrath, Rocky
 *
 */
public class SplunkConstants {
	public static enum SplunkHosts{
		PROD("host=prd*"), QAVPC("host=qa-vpc*"), EOE("host=eoe*"), CEP("host=cep-*"),EMAILPROD("host=prd*");
		
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
		GW("source=/var/log/elastica/gateway/*"),
		GW_ICAPServer("source=/var/log/elastica/gateway/icap-server.log"),
		DGF("source=/var/log/elastica/dgf_*.log");
		private String service;
		 
		private ServiceLogs(String serviceName) {
			service = serviceName;
		}
 
		public String getServiceLogPath() {
			return service;
		}
	}
	
	public static final String SPLUNK_BASIC_AUTH_KEY = "Basic cWEtdGVzdGluZzpTQU50YW5hUm93Iw==";
	public static final String PROD_SPLUNK_HOST = "54.215.185.43";
	public static final String EU_SPLUNK_HOST = "52.16.46.233";
	public static final String EOE_SPLUNK_HOST = "10.0.50.235";
	public static final int SPLUNK_CONNECTION_PORT = 8089; 
}
