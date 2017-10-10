package com.elastica.beatle.RestClient;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class GatewayClient extends Client {
	
	private HttpHost gatewayAsProxy;
	
	public GatewayClient() {
		super();
		gatewayAsProxy = new HttpHost("10.0.54.16", 443, "http");
//		HttpHost proxy = new HttpHost("10.0.54.115", 443, "http");
	}
	
	public GatewayClient(String gatewayHost) {
		super();
		gatewayAsProxy = new HttpHost(gatewayHost, 443, "http");
	}
	
	public GatewayClient(HttpHost proxyGateway) {
		super();
		gatewayAsProxy = proxyGateway;
	}
	

	/**
	 * @return the gatewayAsProxy
	 */
	public HttpHost getGatewayAsProxy() {
		return gatewayAsProxy;
	}

	/**
	 * @param gatewayAsProxy the gatewayAsProxy to set
	 */
	public void setGatewayAsProxy(HttpHost gatewayAsProxy) {
		this.gatewayAsProxy = gatewayAsProxy;
	}
	

	@Override
	protected CloseableHttpClient getRestClient() {
		
		try {
			HttpClientBuilder hcBuilder = HttpClients.custom();
			
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial((KeyStore)null, new TrustStrategy() {
	    		        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			            return true;
			        }
			    }).build();
		    hcBuilder.setSslcontext( sslContext);
			
			HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
		            .register("http", PlainConnectionSocketFactory.getSocketFactory())
		            .register("https", sslSocketFactory)
		            .build();
			
		    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        cm.setMaxTotal(2000);	//max connection
	        hcBuilder.setConnectionManager(cm);
	        
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(gatewayAsProxy);
			hcBuilder.setRoutePlanner(routePlanner);
			
			CloseableHttpClient httpClient = hcBuilder.build();
			
		    return httpClient;			
		}
		
		
		catch(Exception e) {
			System.out.print(e);
			return null;
		}
		
	}
}
