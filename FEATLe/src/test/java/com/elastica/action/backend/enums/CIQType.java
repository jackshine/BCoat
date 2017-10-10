package com.elastica.action.backend.enums;

public enum CIQType {
		PreDefDict("predefdict"),
		PreDefTerms("predefterms"),
		CustomDict("customdict"),
		CustomTerms("customterms"),
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
			} else if (ciq.equalsIgnoreCase("customterms")) {
				ciqType=CIQType.CustomTerms;
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