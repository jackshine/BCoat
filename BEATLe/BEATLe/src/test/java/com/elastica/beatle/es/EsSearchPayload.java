package com.elastica.beatle.es;

public class EsSearchPayload {
	public static class source {
		public static class query {
			public static class filtered {
				public static class filterQuery {
					public static class bool {
						public static class must {
							public static class range {
								public static class created_timestamp {
									private String from;
									private String to;
									/**
									 * @return the from
									 */
									public String getFrom() {
										return from;
									}
									/**
									 * @param from the from to set
									 */
									public void setFrom(String from) {
										this.from = from;
									}
									/**
									 * @return the to
									 */
									public String getTo() {
										return to;
									}
									/**
									 * @param to the to to set
									 */
									public void setTo(String to) {
										this.to = to;
									}
								}
							}
						}
						public static class must_not {
							public static class term {
								private String facility;

								/**
								 * @return the facility
								 */
								public String getFacility() {
									return facility;
								}

								/**
								 * @param facility the facility to set
								 */
								public void setFacility(String facility) {
									this.facility = facility;
								}
							}
						}
					}
				}
				public static class filter {
					
				}
			}
		}
		private int from;
		private int size;
		/**
		 * @return the from
		 */
		public int getFrom() {
			return from;
		}
		/**
		 * @param from the from to set
		 */
		public void setFrom(int from) {
			this.from = from;
		}
		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}
		/**
		 * @param size the size to set
		 */
		public void setSize(int size) {
			this.size = size;
		}
		public static class sort {
			public static class created_timestamp {
				private String order;
				private String ignore_unmapped;
				/**
				 * @return the order
				 */
				public String getOrder() {
					return order;
				}
				/**
				 * @param order the order to set
				 */
				public void setOrder(String order) {
					this.order = order;
				}
				/**
				 * @return the ignore_unmapped
				 */
				public String getIgnore_unmapped() {
					return ignore_unmapped;
				}
				/**
				 * @param ignore_unmapped the ignore_unmapped to set
				 */
				public void setIgnore_unmapped(String ignore_unmapped) {
					this.ignore_unmapped = ignore_unmapped;
				}
			}
		}
	}
	private source _source;
	private String sourceName;
	private String apiServerUrl;
	private String csrftoken;
	private String sessionid;
	private String userid;
	/**
	 * @return the _source
	 */
	public source get_source() {
		return _source;
	}
	/**
	 * @param _source the _source to set
	 */
	public void set_source(source _source) {
		this._source = _source;
	}
	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}
	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * @return the apiServerUrl
	 */
	public String getApiServerUrl() {
		return apiServerUrl;
	}
	/**
	 * @param apiServerUrl the apiServerUrl to set
	 */
	public void setApiServerUrl(String apiServerUrl) {
		this.apiServerUrl = apiServerUrl;
	}
	/**
	 * @return the csrftoken
	 */
	public String getCsrftoken() {
		return csrftoken;
	}
	/**
	 * @param csrftoken the csrftoken to set
	 */
	public void setCsrftoken(String csrftoken) {
		this.csrftoken = csrftoken;
	}
	/**
	 * @return the sessionid
	 */
	public String getSessionid() {
		return sessionid;
	}
	/**
	 * @param sessionid the sessionid to set
	 */
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
