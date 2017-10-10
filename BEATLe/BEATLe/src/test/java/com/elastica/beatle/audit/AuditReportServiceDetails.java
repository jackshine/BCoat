package com.elastica.beatle.audit;

import java.util.Comparator;

public class AuditReportServiceDetails {
	
	
	private String serviceId;
	private String is_new;
	private String is_Most_Used;
	private String cat1;
	private String cat2;
	private String cat3;
	private String cat4;
	private long users_Count;
	private long uploads;
	private long downloads;
	private long sessions;
	private long locations_Count;
	private String service_Brr;
	private String service_Url;
	
	
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getIs_new() {
		return is_new;
	}
	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}
	public String getIs_Most_Used() {
		return is_Most_Used;
	}
	public void setIs_Most_Used(String is_Most_Used) {
		this.is_Most_Used = is_Most_Used;
	}
	public String getCat1() {
		return cat1;
	}
	public void setCat1(String cat1) {
		this.cat1 = cat1;
	}
	public String getCat2() {
		return cat2;
	}
	public void setCat2(String cat2) {
		this.cat2 = cat2;
	}
	public String getCat3() {
		return cat3;
	}
	public void setCat3(String cat3) {
		this.cat3 = cat3;
	}
	public String getCat4() {
		return cat4;
	}
	public void setCat4(String cat4) {
		this.cat4 = cat4;
	}
	public long getUsers_Count() {
		return users_Count;
	}
	public void setUsers_Count(long users_Count) {
		this.users_Count = users_Count;
	}
	public long getUploads() {
		return uploads;
	}
	public void setUploads(long uploads) {
		this.uploads = uploads;
	}
	public long getDownloads() {
		return downloads;
	}
	public void setDownloads(long downloads) {
		this.downloads = downloads;
	}
	public long getSessions() {
		return sessions;
	}
	public void setSessions(long sessions) {
		this.sessions = sessions;
	}
	public long getLocations_Count() {
		return locations_Count;
	}
	public void setLocations_Count(long locations_Count) {
		this.locations_Count = locations_Count;
	}
	public String getService_Brr() {
		return service_Brr;
	}
	public void setService_Brr(String service_Brr) {
		this.service_Brr = service_Brr;
	}
	public String getService_Url() {
		return service_Url;
	}
	public void setService_Url(String service_Url) {
		this.service_Url = service_Url;
	}
	@Override
	public String toString() {
		return "AuditReportServiceDetails [serviceId=" + serviceId + ", is_new=" + is_new + ", is_Most_Used="
				+ is_Most_Used + ", cat1=" + cat1 + ", cat2=" + cat2 + ", cat3=" + cat3 + ", cat4=" + cat4
				+ ", users_Count=" + users_Count + ", uploads=" + uploads + ", downloads=" + downloads + ", sessions="
				+ sessions + ", locations_Count=" + locations_Count + ", service_Brr=" + service_Brr + ", service_Url="
				+ service_Url + "]";
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cat1 == null) ? 0 : cat1.hashCode());
		result = prime * result + ((cat2 == null) ? 0 : cat2.hashCode());
		result = prime * result + ((cat3 == null) ? 0 : cat3.hashCode());
		result = prime * result + ((cat4 == null) ? 0 : cat4.hashCode());
		result = prime * result + (int) (downloads ^ (downloads >>> 32));
		result = prime * result + ((is_Most_Used == null) ? 0 : is_Most_Used.hashCode());
		result = prime * result + ((is_new == null) ? 0 : is_new.hashCode());
		result = prime * result + (int) (locations_Count ^ (locations_Count >>> 32));
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		result = prime * result + ((service_Brr == null) ? 0 : service_Brr.hashCode());
		result = prime * result + ((service_Url == null) ? 0 : service_Url.hashCode());
		result = prime * result + (int) (sessions ^ (sessions >>> 32));
		result = prime * result + (int) (uploads ^ (uploads >>> 32));
		result = prime * result + (int) (users_Count ^ (users_Count >>> 32));
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
		AuditReportServiceDetails other = (AuditReportServiceDetails) obj;
		if (cat1 == null) {
			if (other.cat1 != null)
				return false;
		} else if (!cat1.equals(other.cat1))
			return false;
		if (cat2 == null) {
			if (other.cat2 != null)
				return false;
		} else if (!cat2.equals(other.cat2))
			return false;
		if (cat3 == null) {
			if (other.cat3 != null)
				return false;
		} else if (!cat3.equals(other.cat3))
			return false;
		if (cat4 == null) {
			if (other.cat4 != null)
				return false;
		} else if (!cat4.equals(other.cat4))
			return false;
		if (downloads != other.downloads)
			return false;
		if (is_Most_Used == null) {
			if (other.is_Most_Used != null)
				return false;
		} else if (!is_Most_Used.equals(other.is_Most_Used))
			return false;
		if (is_new == null) {
			if (other.is_new != null)
				return false;
		} else if (!is_new.equals(other.is_new))
			return false;
		if (locations_Count != other.locations_Count)
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		if (service_Brr == null) {
			if (other.service_Brr != null)
				return false;
		} else if (!service_Brr.equals(other.service_Brr))
			return false;
		if (service_Url == null) {
			if (other.service_Url != null)
				return false;
		} else if (!service_Url.equals(other.service_Url))
			return false;
		if (sessions != other.sessions)
			return false;
		if (uploads != other.uploads)
			return false;
		if (users_Count != other.users_Count)
			return false;
		return true;
	}




	public static final Comparator<AuditReportServiceDetails> serviceIDComparator = new Comparator<AuditReportServiceDetails>(){

        @Override
        public int compare(AuditReportServiceDetails o1, AuditReportServiceDetails o2) {
            return o1.serviceId.compareTo(o2.serviceId);
        }
      
    };

	

}
