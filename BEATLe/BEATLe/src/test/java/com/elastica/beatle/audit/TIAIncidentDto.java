package com.elastica.beatle.audit;

import java.util.Comparator;

public class TIAIncidentDto  implements Comparable<TIAIncidentDto> {
	
	String incidentSeverity;
	String service;
	String userOfIncident;
	String incidentDate;
	String incidentType;
	String incidentMsg;
	
	
	@Override
	public int compareTo(TIAIncidentDto o) {
		if (getIncidentType() == null || o.getIncidentType() == null)
		      return 0;
		  return getIncidentType().toString().compareTo(o.getIncidentType().toString());
	}
	
	public static Comparator<TIAIncidentDto> NameComparator = new Comparator<TIAIncidentDto>() {
		 
        @Override
        public int compare(TIAIncidentDto e1, TIAIncidentDto e2) {
            return e1.getIncidentType().compareTo(e2.getIncidentType());
        }
    };
	
	@Override
	public String toString() {
		return "TIAIncidentDto [incidentSeverity=" + incidentSeverity + ", service=" + service + ", userOfIncident="
				+ userOfIncident + ", incidentDate=" + incidentDate + ", incidentType=" + incidentType
				+ ", incidentMsg=" + incidentMsg + "]";
	}
	public String getIncidentSeverity() {
		return incidentSeverity;
	}
	public void setIncidentSeverity(String incidentSeverity) {
		this.incidentSeverity = incidentSeverity;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getUserOfIncident() {
		return userOfIncident;
	}
	public void setUserOfIncident(String userOfIncident) {
		this.userOfIncident = userOfIncident;
	}
	public String getIncidentDate() {
		return incidentDate;
	}
	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}
	public String getIncidentType() {
		return incidentType;
	}
	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}
	public String getIncidentMsg() {
		return incidentMsg;
	}
	public void setIncidentMsg(String incidentMsg) {
		this.incidentMsg = incidentMsg;
	}
	
	
	
	
	

}
