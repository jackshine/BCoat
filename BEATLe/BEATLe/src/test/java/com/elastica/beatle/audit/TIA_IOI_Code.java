package com.elastica.beatle.audit;

public enum TIA_IOI_Code {
	
	

	SESSIONS_PER_USER("sessions per user"),
	DOWNLOAD_PER_USER("download per user"),
	UPLOAD_PER_USER("upload per user"),
	ANOMALOUSLY_FREQUENT_USER_SESSIONS("Anomalously frequent user sessions"),
	ANOMALOUSLY_LARGE_DOWNLOAD_DATA("Anomalously large download data"),
	ANOMALOUSLY_LARGE_UPLOAD_DATA("Anomalously large upload data"),
	TOO_MANY_LOW_REPUTATION_DESTINATIONS("access to low reputation destinations"),
	DEVICES_PER_USER("devices per user"),
	BROWSERS_PER_USER("browsers per user");
	
	
	private  String message;

	private TIA_IOI_Code(String message) {
		this.message = message;
	}
	
	
	public String getMessage(String threshold, String window, String voilations, String threshold2){
		message = message.replaceFirst("9", threshold);
		message = message.replaceFirst("60", window);
		message = message.replaceFirst("41", voilations);
		message = message.replaceFirst("1.777", threshold2);
		return message;
	}
	
	public String getMessage(){
		return message;
	}

}
