package com.universal.dtos.onedrive;

public class Drive {

	private String id;
	private String driveType;
	private Owner owner;
	private Quota quota;

	public String getDriveType() {
		return driveType;
	}

	public String getId() {
		return id;
	}

	public Owner getOwner() {
		return owner;
	}

	public Quota getQuota() {
		return quota;
	}

	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public void setQuota(Quota quota) {
		this.quota = quota;
	}
}
