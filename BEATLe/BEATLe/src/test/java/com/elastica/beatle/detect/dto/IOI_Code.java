package com.elastica.beatle.detect.dto;

public enum IOI_Code {
	
	
	TOO_MANY_INFEASIBLE_LOCATIONS("Too many suspicious location changes: Observed user actions from physical locations that are too far apart for the intervening time"),
	TOO_MANY_SUM_LARGE_TOTAL_TRAFFIC("Upload/download data threshold exceeded: The user is allowed at most 1.777 MB in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_INVALID_LOGINS("Brute force login attack: The user is allowed at most 9 invalid logins in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_SUM_LARGE_DOWNLOADS("Excessive data downloads: The user is allowed at most 1.777 MB in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_SUM_LARGE_UPLOADS("Excessive data uploads: The user is allowed at most 1.777 MB in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_POLICY_VIOLATIONS("Too many policy matches: The user is allowed at most 9 policy matches in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_DEVICES("Too many devices in use: The user is allowed at most 9 devices in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_BROWSERS("Too many browsers in use: The user is allowed at most 9 browsers in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_ENCRYPTED_FILES("Too many encrypted files: The user is allowed at most 9 encrypted files in any 60-sec window. Detected 41 violations of this rule."),
	TOO_MANY_INVALID_LOGINS_GROUP("Distributed brute force login attack: No group of users is allowed more than 1 invalid logins in any 600-sec window. Detected 26 violations of this rule."),
	TOO_MANY_SUSPICIOUS_LOGINS("Too many suspicious logins: The user is allowed at most 9 suspicious logins in any 60-sec window. Detected 41 violations of this rule."),
	ANOMALOUSLY_FREQUENT_SESSIONS("Anomalously frequent View activity: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_FREQUENT_DELETES("Anomalously frequent Delete activity: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_LARGE_SHARING("Anomalously large shared data: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_FREQUENT_SHARING("Anomalously frequent Share activity: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_LARGE_DELETES("Anomalously large deleted data: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_LARGE_UPDOWNLOAD(""),
	ANOMALOUSLY_LARGE_UPLOAD("Anomalously large upload data: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_LARGE_DOWNLOAD("Anomalously large download data: Please use the Investigate app to examine the logs at and around the times shown below."),
	ANOMALOUSLY_LARGE_UPLOAD_ACROSS_SVC("Anomalously large upload data across services: Please use the Investigate app to examine the logs at and around the times shown below.");
	
	private  String message;

	private IOI_Code(String message) {
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
