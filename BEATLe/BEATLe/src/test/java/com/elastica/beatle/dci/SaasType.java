package com.elastica.beatle.dci;

/**
 * Saas App Types
 * @author Eldo Rajan
 *
 */
public enum SaasType {
	Dropbox("dropbox"),
	Box("box"),
	GoogleDrive("googledrive"),
	GmailAttachment("gmailattachment"),
	GmailBody("gmailbody"),
	GmailAttachmentBody("gmailattachmentbody"),
	OneDrive("onedrive"),
	Office365("office365"),
	Office365MailAttachment("office365mailattachment"),
	Office365MailBody("office365mailbody"),
	Office365MailAttachmentBody("office365mailattachmentbody"),
	OneDriveBusiness("onedrivebusiness"),
	SalesForce("salesforce"),
	;

	public static SaasType getSaasType(String saasType) {
		SaasType sType=null;
		try{
			if(saasType.equalsIgnoreCase("dropbox")) {
				sType=SaasType.Dropbox;
			}else if (saasType.equalsIgnoreCase("box")) {
				sType=SaasType.Box;
			}else if (saasType.equalsIgnoreCase("googledrive")||saasType.equalsIgnoreCase("google drive")) {
				sType=SaasType.GoogleDrive;
			}else if (saasType.equalsIgnoreCase("gmailattachment")
					|| saasType.equalsIgnoreCase("gmail attachment")) {
				sType=SaasType.GmailAttachment;
			}else if (saasType.equalsIgnoreCase("gmailbody")
					|| saasType.equalsIgnoreCase("gmail body")) {
				sType=SaasType.GmailBody;
			}else if (saasType.equalsIgnoreCase("gmailattachmentbody")
					|| saasType.equalsIgnoreCase("gmail attachment body")) {
				sType=SaasType.GmailAttachmentBody;
			}else if (saasType.equalsIgnoreCase("onedrive")) {
				sType=SaasType.OneDrive;
			}else if (saasType.equalsIgnoreCase("office365")|| saasType.equalsIgnoreCase("office 365")) {
				sType=SaasType.Office365;
			}else if (saasType.equalsIgnoreCase("office365mailattachment")
					|| saasType.equalsIgnoreCase("office 365 mail attachment")) {
				sType=SaasType.Office365MailAttachment;
			}else if (saasType.equalsIgnoreCase("office365mailbody")
					|| saasType.equalsIgnoreCase("office 365 mail body")) {
				sType=SaasType.Office365MailBody;
			}else if (saasType.equalsIgnoreCase("office365mailattachmentbody")
					|| saasType.equalsIgnoreCase("office 365 mail attachment body")) {
				sType=SaasType.Office365MailAttachmentBody;
			}else if (saasType.equalsIgnoreCase("onedrivebusiness")|| saasType.equalsIgnoreCase("onedrive business")) {
				sType=SaasType.OneDriveBusiness;
			}else if (saasType.equalsIgnoreCase("salesforce")) {
				sType=SaasType.SalesForce;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}

		return sType;
	}

	public static String getSaasFilterType(String saasType) {
		String sType=null;
		try{
			if (saasType.equalsIgnoreCase("dropbox")) {
				sType="Dropbox";
			} else if (saasType.equalsIgnoreCase("box")) {
				sType="Box";
			} else if (saasType.equalsIgnoreCase("googledrive")||saasType.equalsIgnoreCase("google drive")) {
				sType="Google Drive";
			} else if (saasType.equalsIgnoreCase("gmailattachment")||saasType.equalsIgnoreCase("gmail attachment")||
					saasType.equalsIgnoreCase("gmailbody")||saasType.equalsIgnoreCase("gmail body")||
					saasType.equalsIgnoreCase("gmailattachmentbody")||saasType.equalsIgnoreCase("gmail attachment body")) {
				sType="Gmail";
			} else if (saasType.equalsIgnoreCase("onedrive")) {
				sType="One Drive";
			} else if (saasType.equalsIgnoreCase("office365") || saasType.equalsIgnoreCase("office 365")||
					saasType.equalsIgnoreCase("onedrivebusiness") || saasType.equalsIgnoreCase("onedrive business")||
					saasType.equalsIgnoreCase("office365mailattachment") || saasType.equalsIgnoreCase("office 365 mail attachment")||
					saasType.equalsIgnoreCase("office365mailbody") || saasType.equalsIgnoreCase("office 365 mail body")||
					saasType.equalsIgnoreCase("office365mailattachmentbody") || saasType.equalsIgnoreCase("office 365 mail attachment body")) {
				sType="Office 365";
			} else if(saasType.equalsIgnoreCase("Salesforce")){
				sType="Salesforce";
			}


		} catch (Exception e) {
			//e.printStackTrace();
		}
		return sType;
	}

	public static String getAppType(String saasType,String env) {
		String aType=null;
		try{
			if (saasType.equalsIgnoreCase("dropbox")) {
				aType="el_dropbox";
			} else if (saasType.equalsIgnoreCase("box")) {
				aType="el_box";
			} else if (saasType.equalsIgnoreCase("googledrive")||saasType.equalsIgnoreCase("google drive")) {
				aType="el_google_apps";
			} else if (saasType.equalsIgnoreCase("onedrive")) {
				aType="el_onedrive";
			}  else if (saasType.equalsIgnoreCase("office365") || saasType.equalsIgnoreCase("office 365")||
					saasType.equalsIgnoreCase("onedrivebusiness") || saasType.equalsIgnoreCase("onedrive business")||
					saasType.equalsIgnoreCase("office365mailattachment") || saasType.equalsIgnoreCase("office 365 mail attachment")||
					saasType.equalsIgnoreCase("office365mailbody") || saasType.equalsIgnoreCase("office 365 mail body")) {
				aType="el_office_365";
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return aType;
	}

	public static String getVlType(String saasType) {
		String vType=null;
		try{
			if (saasType.equalsIgnoreCase("dropbox")) {
				vType="Dropbox";
			} else if (saasType.equalsIgnoreCase("box")) {
				vType="Box";
			} else if (saasType.equalsIgnoreCase("googledrive")||saasType.equalsIgnoreCase("google drive")) {
				vType="Google Apps";
			} else if (saasType.equalsIgnoreCase("onedrive")) {
				vType="One Drive";
			} else if (saasType.equalsIgnoreCase("office365") || saasType.equalsIgnoreCase("office 365")||
					saasType.equalsIgnoreCase("onedrivebusiness") || saasType.equalsIgnoreCase("onedrive business")||
					saasType.equalsIgnoreCase("office365mailattachment") || saasType.equalsIgnoreCase("office 365 mail attachment")||
					saasType.equalsIgnoreCase("office365mailbody") || saasType.equalsIgnoreCase("office 365 mail body")) {
				vType="Office 365";
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return vType;
	}

	public static String getObjectType(String saasType) {
		String oType="";
		try{
			if (saasType.equalsIgnoreCase("dropbox")) {
				oType="";
			} else if (saasType.equalsIgnoreCase("box")) {
				oType="";
			} else if (saasType.equalsIgnoreCase("googledrive")||saasType.equalsIgnoreCase("google drive")) {
				oType="Drive";
			} else if (saasType.toLowerCase().contains("gmail")) {
				oType="Mail";
			} else if (saasType.equalsIgnoreCase("office365") || saasType.equalsIgnoreCase("office 365")||
					saasType.equalsIgnoreCase("onedrivebusiness") || saasType.equalsIgnoreCase("onedrive business")||
					saasType.equalsIgnoreCase("office365mailattachment") || saasType.equalsIgnoreCase("office 365 mail attachment")||
					saasType.equalsIgnoreCase("office365mailbody") || saasType.equalsIgnoreCase("office 365 mail body")) {
				if(saasType.toLowerCase().contains("mail")){
					oType="Mail";
				}else if(saasType.toLowerCase().contains("sites")){
					oType="Sites";
				}else{
					oType="OneDrive";
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return oType;
	}
	
	
	private String saasType;

	SaasType(final String type) {
		this.saasType = type;
	}

	public String getSaasType() {
		return this.saasType;
	}

}
