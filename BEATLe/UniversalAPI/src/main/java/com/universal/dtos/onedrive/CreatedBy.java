package com.universal.dtos.onedrive;

import org.codehaus.jackson.annotate.JsonProperty;


public class CreatedBy {

	@JsonProperty("application")
	private Application application;
	@JsonProperty("user")
	private User user;
	

	/**
	 * 
	 * @return
	 * The application
	 */
	@JsonProperty("application")
	public Application getApplication() {
		return application;
	}

	/**
	 * 
	 * @param application
	 * The application
	 */
	@JsonProperty("application")
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * 
	 * @return
	 * The user
	 */
	@JsonProperty("user")
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 * The user
	 */
	@JsonProperty("user")
	public void setUser(User user) {
		this.user = user;
	}

	

}
