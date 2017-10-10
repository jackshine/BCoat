package com.elastica.beatle.audit;

import java.util.Comparator;
import java.util.List;

public class AuditSummaryTopRiskyServices {
	
	private String servicename;
	private String service_brr;
	private int service_user_count;
	private List<AuditSummaryUserObject> summaryUserObjList;
	private long userLocations;
	
	
	@Override
	public String toString() {
		return "AuditSummaryTopRiskyServices [servicename=" + servicename + ", service_brr=" + service_brr
				+ ", service_user_count=" + service_user_count + ", summaryUserObjList=" + summaryUserObjList
				+ ", userLocations=" + userLocations + "]";
	}
	public String getServicename() {
		return servicename;
	}
	public void setServicename(String servicename) {
		this.servicename = servicename;
	}
	public String getService_brr() {
		return service_brr;
	}
	public void setService_brr(String service_brr) {
		this.service_brr = service_brr;
	}
	public int getService_user_count() {
		return service_user_count;
	}
	public void setService_user_count(int service_user_count) {
		this.service_user_count = service_user_count;
	}
	public List<AuditSummaryUserObject> getSummaryUserObjList() {
		return summaryUserObjList;
	}
	public void setSummaryUserObjList(List<AuditSummaryUserObject> summaryUserObjList) {
		this.summaryUserObjList = summaryUserObjList;
	}
	public long getUserLocations() {
		return userLocations;
	}
	public void setUserLocations(long userLocations) {
		this.userLocations = userLocations;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((service_brr == null) ? 0 : service_brr.hashCode());
		result = prime * result + service_user_count;
		result = prime * result + ((servicename == null) ? 0 : servicename.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditSummaryTopRiskyServices other = (AuditSummaryTopRiskyServices) obj;
		if (service_brr == null) {
			if (other.service_brr != null)
				return false;
		} else if (!service_brr.equals(other.service_brr))
			return false;
		if (service_user_count != other.service_user_count)
			return false;
		if (servicename == null) {
			if (other.servicename != null)
				return false;
		} else if (!servicename.equals(other.servicename))
			return false;
		return true;
	}
	public static final Comparator<AuditSummaryTopRiskyServices> serviceNameComparator = new Comparator<AuditSummaryTopRiskyServices>(){

        @Override
        public int compare(AuditSummaryTopRiskyServices o1, AuditSummaryTopRiskyServices o2) {
            return o1.servicename.compareTo(o2.servicename);
        }
      
    };


	

}
