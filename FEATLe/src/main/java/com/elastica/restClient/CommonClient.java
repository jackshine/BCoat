/**
 * 
 */
package com.elastica.restClient;

import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import com.elastica.logger.Logger;
/**
 * @author anuvrath
 *
 */
@SuppressWarnings("deprecation")
public abstract class CommonClient {	
	
	public CommonClient(){
	}		
	
	/**
	 * @return
	 */
	protected CloseableHttpClient getRestClient() {
		try{
			/*
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{tm}, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);								

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
			schemeRegistry.register(new Scheme("https", 443, ssf));
			ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
			cm.setMaxTotal(1000);
			cm.setDefaultMaxPerRoute(1000);
			*/
			
			HttpClientBuilder hcBuilder = HttpClients.custom();
			//@SuppressWarnings("deprecation")
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial((KeyStore)null, new TrustStrategy() {
	    		        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			            return true;
			        }
	    		        
			    })
					.useProtocol("TLSv1.2")
					.build();
		    hcBuilder.setSslcontext( sslContext);
			HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;			
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
		            .register("http", PlainConnectionSocketFactory.getSocketFactory())
		            .register("https", sslSocketFactory)
		            .build();
			
			System.setProperty("java.net.preferIPv4Stack" , "true");
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        cm.setMaxTotal(2000);	//max connection
	        hcBuilder.setConnectionManager(cm);
	        hcBuilder.setServiceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy() {				
	        	@Override
	            public boolean retryRequest(
	                final HttpResponse response, final int executionCount, final HttpContext context) {
	                int statusCode = response.getStatusLine().getStatusCode();
	                // Changing it to 403 because, there is no meaning in retrying if the credentails are wrong. 
	                return statusCode > 403 && executionCount < 5;
	            }
				
				@Override
				public long getRetryInterval() {
					return 10000;
				}
			});
			return hcBuilder.build();
		}
		catch(Exception e) {
			System.out.print(e);
			return null;
		}		
	}
	
	/**
	 * @param request
	 * @param headers
	 * @return
	 */
	protected HttpResponse executeRestRequest(HttpUriRequest request, List<NameValuePair> headers){
		
		if(headers != null) {
			Iterator<NameValuePair> iterator1 = headers.iterator();

			while(iterator1.hasNext()) {
				NameValuePair header = iterator1.next();
				request.setHeader(header.getName(), header.getValue());
			}
		}
		HttpResponse response = null;
		try {
			response = getRestClient().execute(request);
			if(!isValidReponse(response)){
				Logger.info("Status Code: "+ response.getStatusLine().getStatusCode());
				Logger.info("Request Headers: ");
				for(NameValuePair headerPair: headers){
					Logger.info("\t\t"+headerPair.getName() +":"+headerPair.getValue());
				}			
				Logger.info("Reason Phrase: "+ response.getStatusLine().getReasonPhrase());							
			}
			return response;	
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * @param response
	 * @return
	 */
	private boolean isValidReponse(HttpResponse response){
		switch(response.getStatusLine().getStatusCode()){
		case HttpStatus.SC_OK:
		case HttpStatus.SC_CREATED:			
		case HttpStatus.SC_ACCEPTED:	
		case HttpStatus.SC_NO_CONTENT:
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			return true;
		}
		return false;
	}
}