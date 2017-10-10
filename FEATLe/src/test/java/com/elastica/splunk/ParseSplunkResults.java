package com.elastica.splunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.splunk.Job;
import com.splunk.ResultsReaderXml;

/**
 * 
 * @author rocky
 *
 */
public class ParseSplunkResults {
	public List<String> getHosts(Job job) {
		List<String > hosts = new ArrayList<String>();
		Set<String> hostset = new HashSet<String>();
		try {
			ResultsReaderXml resultsReader = new ResultsReaderXml(job.getResults());
		    HashMap<String, String> event;
		    while ((event = resultsReader.getNextEvent()) != null) {
		        hostset.add(event.get("host"));
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		hosts.addAll(hostset);
		return hosts;
	}
}
