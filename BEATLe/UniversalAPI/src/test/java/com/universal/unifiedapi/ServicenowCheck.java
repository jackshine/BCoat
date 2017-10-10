package com.universal.unifiedapi;

import org.testng.annotations.Test;

import com.universal.common.Salesforce;
import com.universal.common.ServiceNow;
import com.universal.common.UniversalApi;
import com.universal.dtos.UserAccount;
import com.universal.dtos.salesforce.APIVersion;
import com.universal.dtos.salesforce.GroupDetail;
import com.universal.dtos.salesforce.GroupInput;
import com.universal.dtos.salesforce.Resources;
import com.universal.dtos.servicenow.Incident;
import com.universal.dtos.servicenow.IncidentInput;

public class ServicenowCheck {
	UniversalApi universalApi;
	ServiceNow snapi;


	@Test
	public void test1() {
		try{
			UserAccount ua = new UserAccount("admin@elasticaqa.net", "Elastica@123", "ADMIN");
			universalApi = new UniversalApi("SERVICENOW", ua);
			snapi = universalApi.getServiceNow();
			System.out.println(snapi.getToken());
			
			
			IncidentInput input = new IncidentInput();
			input.setComments("Automation Testing of ServiceNow");
			input.setShortDescription("This is the short description of incident-1");
			
			Incident incident = snapi.createIncident(input);
			System.out.println(incident.getResult().getSysId());
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
