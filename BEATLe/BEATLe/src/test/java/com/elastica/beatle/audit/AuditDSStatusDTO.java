package com.elastica.beatle.audit;

public class AuditDSStatusDTO {
	
	private String dsId=null;
	private String dsName=null;
	private String dsType=null;
	private String dsLogTransport=null;
	private String dsLogFormat=null;
	private String dsSetUpBy=null;
	private String dsLastDetectStatus=null;
	private String dsLastStatus=null;
	private String dsLastStatusMsg=null;
	
	
	
	
	
	public void setDsLastStatus(String dsLastStatus) {
		this.dsLastStatus = dsLastStatus;
	}




	public void setDsId(String dsId) {
		this.dsId = dsId;
	}




	public void setDsName(String dsName) {
		this.dsName = dsName;
	}




	public void setDsType(String dsType) {
		this.dsType = dsType;
	}




	public void setDsLogTransport(String dsLogTransport) {
		this.dsLogTransport = dsLogTransport;
	}




	public void setDsLogFormat(String dsLogFormat) {
		this.dsLogFormat = dsLogFormat;
	}




	public void setDsSetUpBy(String dsSetUpBy) {
		this.dsSetUpBy = dsSetUpBy;
	}




	public void setDsLastDetectStatus(String dsLastDetectStatus) {
		this.dsLastDetectStatus = dsLastDetectStatus;
	}




	public void setDsLastStatusMsg(String dsLastStatusMsg) {
		this.dsLastStatusMsg = dsLastStatusMsg;
	}


	@Override
	public String toString() {
		return "AuditDSStatusDTO [dsId= " + dsId + ", dsName= " + dsName
				+ ", dsType= " + dsType + ", dsLogTransport= " + dsLogTransport
				+ ", dsLogFormat= " + dsLogFormat + ", dsSetUpBy= " + dsSetUpBy
				+ ", dsLastDetectStatus= " + dsLastDetectStatus
				+ ", dsLastStatus= " + dsLastStatus + ", dsLastStatusMsg= "
				+ dsLastStatusMsg + "]";
	}



}
