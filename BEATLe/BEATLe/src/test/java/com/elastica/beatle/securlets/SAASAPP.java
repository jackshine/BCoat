package com.elastica.beatle.securlets;

public enum SAASAPP {
	BOX("Box"), 
	ONEDRIVEBUSINESS("Office 365"), 
	AMAZON_WEB_SERVICES("Amazon web services");

	private String value;

	/**
	 * Construct a new SAASAPP enumeration.
	 * 
	 * @param value
	 *            value of the key
	 */
	private SAASAPP(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return this.value;
	}
}
