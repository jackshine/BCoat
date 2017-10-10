package com.elastica.beatle.dci;

public enum CIQType {
		PreDefDict("predefdict"),
		PreDefTerms("predefterms"),
		CustomDict("customdict"),
		CustomTerms("customterms,foreignlanguage"),
		MLProfile("mlprofile"),
		;
	

	public static CIQType getCIQType(String ciq) {
		CIQType ciqType=null;
		try{
			if (ciq.equalsIgnoreCase("predefdict")) {
				ciqType=CIQType.PreDefDict;
			} else if (ciq.equalsIgnoreCase("predefterms")) {
				ciqType=CIQType.PreDefTerms;
			} else if (ciq.equalsIgnoreCase("customdict")){
				ciqType=CIQType.CustomDict;
			} else if (ciq.equalsIgnoreCase("customterms")||
					ciq.equalsIgnoreCase("foreignlanguage")) {
				ciqType=CIQType.CustomTerms;
			} else if (ciq.equalsIgnoreCase("mlprofile")){
				ciqType=CIQType.MLProfile;
			} 
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return ciqType;
	}

	private String ciqType;

	CIQType(final String type) {
		this.ciqType = type;
	}

	public String getCIQType() {
		return this.ciqType;
	}
	
}	