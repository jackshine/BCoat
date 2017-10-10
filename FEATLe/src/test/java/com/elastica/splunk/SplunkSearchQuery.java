package com.elastica.splunk;

import java.util.ArrayList;
import java.util.List;

import com.elastica.splunk.SqlunkConstants.ServiceLogs;
import com.elastica.splunk.SqlunkConstants.SplunkHosts;

/**
 * 
 * @author rocky
 *
 */
public class SplunkSearchQuery {
	
	private String query;
	private List<String> options;
	private int limit;
	
	public SplunkSearchQuery() {
		query = "search";
		options = new ArrayList<String>();
		limit = 0;
	}
	
	public void addRule(String rule) {
		options.add(rule);
	}

	public void addRule(SplunkHosts host) {
		options.add(host.getHost());
	}
	
	public void addRule(ServiceLogs log) {
		options.add(log.toString());
	}
	
	public void addLimit(int num) {
		limit = num;
	}
	
	public String getQuery() {
		
		StringBuffer sb = new StringBuffer(query);
		for (String rule : options) {
			sb.append(" " + rule);
		}
		
		if (limit > 0) {
			sb.append(" | head ").append(limit);
		}
		
		return sb.toString();
	}
}
