package com.elastica.beatle.gateway;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.Assert;

import com.elastica.beatle.securlets.dto.ForensicSearchResults;
import com.elastica.beatle.securlets.dto.Hit;
import com.elastica.beatle.securlets.dto.Source;
import com.elastica.beatle.tests.securlets.CustomAssertion;

public class GatewayLogsValidator {
	ForensicSearchResults fsr;
	
	public GatewayLogsValidator() {
		
	}
	
	public GatewayLogsValidator(ForensicSearchResults fsr) {
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
		for (Hit hit : fsr.getHits().getHits()) {
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
		
		for (Hit hit : fsr.getHits().getHits()) {
			Source source  = hit.getSource();
			alist.add(source.getMessage());
		}
		CustomAssertion.assertTrue(alist.contains(message), "Expected activity not available in the log");
	}
	
	
	public void verifyLog(ArrayList<String> logs,  String message) {
		CustomAssertion.assertTrue(logs.contains(message), "Expected activity not available in the log");
	}
	
	public void verifySource(Source source) {
		if (source.getSource().equals("__API")) { 
			
			Assert.assertNotNull(source.getFacility(), "Facility is null");
			Assert.assertNotNull(source.getFileSize(), "Filesize is null");
			Assert.assertNotNull(source.getCreatedTimestamp(), "Created timestamp is null");
			Assert.assertNotNull(source.getInsertedTimestamp(), "Inserted timestamp is null");

			Assert.assertNotNull(source.getMessage(), "Message is null");
			Assert.assertNotNull(source.getActivityType(), "ActivityType is null");
			Assert.assertNotNull(source.getSeverity(), "Severity is null");
			Assert.assertNotNull(source.getObjectType(), "ObjectType is null");
			Assert.assertNotNull(source.getUserName(), "Username is null");
			Assert.assertNotNull(source.getParent(), "Parent is null");
			Assert.assertNotNull(source.getResourceId(), "Resource Id is null");
			Assert.assertNotNull(source.getHost(), "Host is null");
			Assert.assertNotNull(source.getUser(), "User is null");
			Assert.assertNotNull(source.getObjectName(), "Object name is null");
			Assert.assertNotNull(source.getSource(), "Source is null");

			Assert.assertNotNull(source.getCountry(), "City is null");
			Assert.assertNotNull(source.getCity(), "City is null");
			Assert.assertNotNull(source.getLatitude(), "Latitude is null");
			Assert.assertNotNull(source.getLongitude(), "Longitude is null");
			Assert.assertNotNull(source.getLocation(), "Location is null");

		}
	}
}
