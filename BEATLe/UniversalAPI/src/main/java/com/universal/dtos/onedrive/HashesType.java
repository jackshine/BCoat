package com.universal.dtos.onedrive;

public class HashesType {
	private String crc32Hash;
	public String getCrc32Hash() {
		return crc32Hash;
	}
	public void setCrc32Hash(String crc32Hash) {
		this.crc32Hash = crc32Hash;
	}
	public String getSha1Hash() {
		return sha1Hash;
	}
	public void setSha1Hash(String sha1Hash) {
		this.sha1Hash = sha1Hash;
	}
	private String sha1Hash;
}
