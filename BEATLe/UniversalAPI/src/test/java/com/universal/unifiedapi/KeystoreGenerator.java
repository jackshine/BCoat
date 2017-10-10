package com.universal.unifiedapi;


import java.io.*;
import java.util.*;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.security.*;
import java.security.cert.*;
import java.security.spec.*;

public class KeystoreGenerator {
	@SuppressWarnings("deprecation")
	public static void main (String args[]) throws Exception {
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		FileInputStream instream = new FileInputStream(new File("/Users/pushparajt/Documents/store/elastica.pkcs12"));
		try {
			keyStore.load(instream, "changeit".toCharArray());
		} finally {
			instream.close();
		}
		
		HttpHost proxy = new HttpHost("eoegw.elastica-inc.com", 443);
		
		// Trust own CA and all self-signed certs
		SSLContext sslcontext = SSLContexts.custom()
				.loadKeyMaterial(keyStore, "changeit".toCharArray())
				//.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
				.build();
		// Allow TLSv1 protocol only
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext,
				new String[] { "TLSv1" },
				null,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); //TODO
		CloseableHttpClient httpclient = HttpClients.custom()
				.setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) //TODO
				.setSSLSocketFactory(sslsf)
				.setProxy(proxy)
				.build();
		try {

			HttpGet httpget = new HttpGet("https://box.com");

			System.out.println("executing request" + httpget.getRequestLine());

			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();

				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				if (entity != null) {
					System.out.println("Response content length: " + entity.getContentLength());
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}
	}

}

