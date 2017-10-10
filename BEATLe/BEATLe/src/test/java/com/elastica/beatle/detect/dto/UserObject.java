package com.elastica.beatle.detect.dto;

public class UserObject {
	//UserObject userdata1 = new UserObject(severity, ioi, userName, service, id, threatScore, 0,country, message, activityType, responsibleLogs);

	
	private String severity, ioiName,userName, service , id, threatScore,country, city, message, activityType, responsibleLogs, userCreatedTimeStamp, IOIActual;
	private int threatScore1;
	public UserObject(String severity, String ioiName, String userName, String  service, String id, String threatScore,int threatScore1,
			String country, String city, String message, String activityType, String responsibleLogs, String userCreatedTimeStamp, String IOIActual) {
		super();
		this.severity = severity;
		this.userName = userName;
		this.ioiName = ioiName;
		this.service = service;
		this.id = id;
		this.threatScore=threatScore;
		this.threatScore1=threatScore1;
		this.country=country;
		this.message=message;
		this.activityType=activityType;
		this.responsibleLogs=responsibleLogs;
		this.city=city;
		this.userCreatedTimeStamp=userCreatedTimeStamp;
		this.IOIActual=IOIActual;
		
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getSeverity(){
		return severity;
	}
	
	public void setseverity(String severity){
		this.severity = severity;
	}
	public String getIoiName(){
		return ioiName;
	}
	
	public void setIoiName(String ioiName){
		this.ioiName = ioiName;
	}
	
	public String getService(){
		 return service;
	}
	
	public void setService(String service){
		this.service = service;
	}
	
	public String getId(){
		 return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getThreatScore(){
		return threatScore;
	}
	
	public void setThreatScore(String threatScore){
		this.threatScore = threatScore;
	}
	
	public String getCountry(){
		return country;
	}
	
	public void setCountry(String country){
		this.country=country;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setMessage(String message){
		this.message=message;
	}

	public String getActivityType(){
		return activityType;
	}
	
	public void setActivityType(String activityType){
		this.activityType=activityType;
	}
	
	public String getResponsibleLogs(){
		return responsibleLogs;
	}
	
	public void setResponsibleLogs(String responsibleLogs){
		this.responsibleLogs=responsibleLogs;
	}
	
	public int getThreatScore1(){
		return threatScore1;
	}
	
	public void setThreatScore1(int threatScore1){
		this.threatScore1=threatScore1;
	}
	
	public String getCity(){
		return city;
	}
	
	public void setCity(String city){
		this.city=city;
	}
	
	public String getUserCreatedTimeStamp(){
		return userCreatedTimeStamp;
	}
	
	public void setUserCreatedTimeStamp(String userCreatedTimeStamp){
		this.userCreatedTimeStamp=userCreatedTimeStamp;
	}
	
	public String getIOIActual(){
		return IOIActual;
	}
	
	public void setIOIActual(String IOIActual){
		this.IOIActual=IOIActual;
	}
	
}
