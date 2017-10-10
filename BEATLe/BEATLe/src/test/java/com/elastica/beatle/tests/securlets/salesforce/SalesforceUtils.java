package com.elastica.beatle.tests.securlets.salesforce;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.elastica.beatle.securlets.SecurletUtils;
import com.elastica.beatle.securlets.SecurletsConstants;
import com.elastica.beatle.securlets.SecurletUtils.elapp;
import com.elastica.beatle.securlets.SecurletUtils.facility;
import com.elastica.beatle.securlets.dto.ExposureTotals;

public class SalesforceUtils extends SecurletUtils{

	public SalesforceUtils() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	
	public ExposureTotals  getSalesforceExposureMetricsWithAPI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>(); 
		qparams.add(new BasicNameValuePair(SecurletsConstants.IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		ExposureTotals exposureTotals = getExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		return exposureTotals;
	}
	
	public ExposureTotals  getSalesforceExposureMetricsWithUI(boolean isInternal, List<NameValuePair> queryParams) throws Exception  {
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.clear();
		qparams.add(new BasicNameValuePair(SecurletsConstants.UI_PARAM_IS_INTERNAL,  String.valueOf(isInternal)));
		qparams.add(new BasicNameValuePair(SecurletsConstants.APP,  facility.Salesforce.name()));
		ExposureTotals exposureTotals = getUIExposuresMetricsTotal(elapp.el_salesforce.name(), qparams);
		return exposureTotals;
	}
}
