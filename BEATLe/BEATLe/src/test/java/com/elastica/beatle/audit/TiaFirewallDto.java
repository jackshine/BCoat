package com.elastica.beatle.audit;

public class TiaFirewallDto {

	String tempDirPath;
	String tiaFileName;
	String tiaFileFormat;
	String actualFileName;
	String tempRenamedActualFile;
	String zipFileName;
	String tiaUser;
	long randomId;
	
	

	public long getRandomId() {
		return randomId;
	}
	public void setRandomId(long randomId) {
		this.randomId = randomId;
	}
	public String getTiaUser() {
		return tiaUser;
	}
	public void setTiaUser(String tiaUser) {
		this.tiaUser = tiaUser;
	}
	public String getTempDirPath() {
		return tempDirPath;
	}
	public void setTempDirPath(String tempDirPath) {
		this.tempDirPath = tempDirPath;
	}
	public String getTiaFileName() {
		return tiaFileName;
	}
	public void setTiaFileName(String tiaFileName) {
		this.tiaFileName = tiaFileName;
	}
	public String getTiaFileFormat() {
		return tiaFileFormat;
	}
	public void setTiaFileFormat(String tiaFileFormat) {
		this.tiaFileFormat = tiaFileFormat;
	}
	public String getActualFileName() {
		return actualFileName;
	}
	public void setActualFileName(String actualFileName) {
		this.actualFileName = actualFileName;
	}
	public String getTempRenamedActualFile() {
		return tempRenamedActualFile;
	}
	public void setTempRenamedActualFile(String tempRenamedActualFile) {
		this.tempRenamedActualFile = tempRenamedActualFile;
	}
	public String getZipFileName() {
		return zipFileName;
	}
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}
}
