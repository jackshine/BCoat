package com.elastica.beatle.gateway;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.Assert;

import com.elastica.beatle.gateway.dto.BoxGWForensicSearchResults;
import com.elastica.beatle.gateway.dto.BoxGWHit;
import com.elastica.beatle.gateway.dto.BoxGWSource;
import com.elastica.beatle.gateway.dto.GWForensicSearchResults;
import com.elastica.beatle.gateway.dto.GWHit;
import com.elastica.beatle.gateway.dto.GWSource;
import com.elastica.beatle.tests.securlets.CustomAssertion;

public class LogValidator {
	GWForensicSearchResults fsr;
	
	public LogValidator() {
		
	}
	
	public LogValidator(GWForensicSearchResults fsr) {
		this.fsr = fsr;
	}
	
	public void verifyHitCountGreater(int count) {
		CustomAssertion.assertTrue(count > 0, "Hits count do not match");
	}
	
	public void validateAll() {
		verifyHitCountGreater(this.fsr.getHits().getHits().size());
		verifyMetadata();
	}
	
	
	public void verifyMetadata() {
		for (GWHit hit : fsr.getHits().getHits()) {
			Assert.assertTrue(hit.getSort().size() > 0, "Sort is not present");
			Assert.assertTrue(hit.getSort().get(0).longValue() > 0, "Sort value is not present");
			Assert.assertNotNull(hit.getId(), "_id is null");
			Assert.assertNotNull(hit.getIndex(), "_index is null");
			Assert.assertNull(hit.getScore(), "_score is null");
			Assert.assertNotNull(hit.getType(), "_type is null");
			Assert.assertNotNull(hit.getSource(), "_source is null");	
			this.verifySource(hit.getSource());
		}
	}
	
	public void verifyActivityLog(String message) {
		ArrayList<String> alist = new ArrayList<String>();
		
		for (GWHit hit : fsr.getHits().getHits()) {
			GWSource source  = hit.getSource();
			alist.add(source.getMessage());
		}
		CustomAssertion.assertTrue(alist.contains(message), "Expected activity not available in the log");
	}
	
	
	public void verifyLog(ArrayList<String> logs,  String message) {
		assertTrue(logs.contains(message), "Expected activity:"+message+" not available in the log");
		//CustomAssertion.assertTrue(logs.contains(message), "Expected activity not available in the log");
	}
	
	public void verifySource(GWSource source) {
		if (source.getSource().equals("__GW")) { 
			Assert.assertNotNull(source.getFacility(), "Facility is null");
			//Assert.assertNotNull(source.getFileSize(), "Filesize is null");
			Assert.assertNotNull(source.getCreatedTimestamp(), "Created timestamp is null");
			Assert.assertNotNull(source.getInsertedTimestamp(), "Inserted timestamp is null");
			Assert.assertNotNull(source.getMessage(), "Message is null");
			Assert.assertNotNull(source.getActivityType(), "ActivityType is null");
			Assert.assertNotNull(source.getSeverity(), "Severity is null");
			Assert.assertNotNull(source.getObjectType(), "ObjectType is null");
			Assert.assertNotNull(source.getUserName(), "Username is null");
			Assert.assertNotNull(source.getHost(), "Host is null");
			Assert.assertNotNull(source.getUser(), "User is null");
			Assert.assertNotNull(source.getObjectName(), "Object name is null");
			Assert.assertNotNull(source.getSource(), "Source is null");
			Assert.assertNotNull(source.getCountry(), "City is null");
			Assert.assertNotNull(source.getCity(), "City is null");
			//Assert.assertNotNull(source.getLatitude(), "Latitude is null");
			//Assert.assertNotNull(source.getLongitude(), "Longitude is null");
			Assert.assertNotNull(source.getLocation(), "Location is null");

		}
	}
}
