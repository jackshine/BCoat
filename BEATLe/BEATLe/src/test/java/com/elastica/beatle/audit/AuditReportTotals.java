package com.elastica.beatle.audit;

public class AuditReportTotals {
	
	  private long users;
	     private long sessions;
	     private long services;
	     private long traffic;
	     private long locations;
	     private long categories;
	     private long uploads;
	     private long downloads;
	     private long new_services;
	     
		@Override
		public String toString() {
			return "AuditReportTotals [users=" + users + ", sessions=" + sessions + ", services=" + services
					+ ", traffic=" + traffic + ", locations=" + locations + ", categories=" + categories + ", uploads="
					+ uploads + ", downloads=" + downloads + ", new_services=" + new_services + "]";
		}
		public long getUsers() {
			return users;
		}
		public void setUsers(long users) {
			this.users = users;
		}
		public long getSessions() {
			return sessions;
		}
		public void setSessions(long sessions) {
			this.sessions = sessions;
		}
		public long getServices() {
			return services;
		}
		public void setServices(long services) {
			this.services = services;
		}
		public long getTraffic() {
			return traffic;
		}
		public void setTraffic(long traffic) {
			this.traffic = traffic;
		}
		public long getLocations() {
			return locations;
		}
		public void setLocations(long locations) {
			this.locations = locations;
		}
		public long getCategories() {
			return categories;
		}
		public void setCategories(long categories) {
			this.categories = categories;
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
		public long getNew_services() {
			return new_services;
		}
		public void setNew_services(long new_services) {
			this.new_services = new_services;
		}
	 	

}
