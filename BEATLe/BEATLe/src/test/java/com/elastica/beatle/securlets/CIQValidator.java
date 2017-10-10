package com.elastica.beatle.securlets;

import static org.testng.Assert.*;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.elastica.beatle.ciq.dto.ContentIqViolation;
import com.elastica.beatle.ciq.dto.ESResults;
import com.elastica.beatle.ciq.dto.Source;
import org.unitils.reflectionassert.ReflectionAssert;

public class CIQValidator {
	ESResults esr;

	public CIQValidator() {

	}

	public CIQValidator(ESResults esr) {
		this.esr = esr;
	}

	public void validateAll(Source expectedSource) {
		
		Source actualSource = esr.getHits().getHits().get(0).getSource();

		//String excludedFields[] ={"_ObjectName", "Resource_Id", "message", "name", "inserted_timestamp", "created_timestamp", "updated_timestamp", "filename"};
		
		//System.out.println("Actual Source:" + actualSource.toString());
		//System.out.println("Expected Source:" + expectedSource.toString());
		
		actualSource.setObjectName(null);
		expectedSource.setObjectName(null);
		
		actualSource.setResourceId(null);
		expectedSource.setResourceId(null);
		
		actualSource.setMessage(null);
		expectedSource.setMessage(null);
		
		actualSource.setName(null);
		expectedSource.setName(null);
		
		actualSource.setInsertedTimestamp(null);
		expectedSource.setInsertedTimestamp(null);

		actualSource.setCreatedTimestamp(null);
		expectedSource.setCreatedTimestamp(null);
		
		actualSource.getContentChecks().getPii().setUpdatedTimestamp(null);
		expectedSource.getContentChecks().getPii().setUpdatedTimestamp(null);
		
		actualSource.getContentChecks().setFilename(null);
		expectedSource.getContentChecks().setFilename(null);
		
		for (ContentIqViolation cv : actualSource.getContentChecks().getContentIqViolations()) {
			cv.setUpdatedTimestamp(null);
		}
		
		for (ContentIqViolation cv : expectedSource.getContentChecks().getContentIqViolations()) {
			cv.setUpdatedTimestamp(null);
		}
		
		actualSource.getContentChecks().getHipaa().setUpdatedTimestamp(null);
		expectedSource.getContentChecks().getHipaa().setUpdatedTimestamp(null);
		
		ReflectionAssert.assertReflectionEquals(actualSource, expectedSource);
		//assertTrue(EqualsBuilder.reflectionEquals(actualSource, expectedSource, excludedFields));
	}
}
