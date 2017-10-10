package com.universal.common;

public interface UniversalCore {
	
	public <T> T getFile(String fileName);
	
	
	public <T> T getFolder(String folderId);
	
	
	public <T> T uploadFile(String folderId, String filename) throws Exception;
	
	
	public <T> T getDefaultDrive() throws Exception;
	
}
