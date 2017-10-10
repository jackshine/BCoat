package com.elastica.beatle.dci;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;

public class TestParameters {
	private String testName = null;
	private String testDescription = null;

	private String fileName = null;
	private String saasType = null;
	private String riskType = null;
	private String fileType = null;
	private List<String> risks = null;
	private List<String> content = null;
	private String contentIQProfileTerm = null;
	private String contentIQProfileName = null;
	private String contentIQProfileDescription = null;
	private String dictionaries = null;
	private String terms = null;
	private String count = null;
	private String riskPrimaryJson = null;
	private String riskSecondaryJson = null;
	private int countUI = 0;
	private int countAPI = 0;
	private Map<String, String> headersUI = null;
	private Map<String, String> headersAPI = null;
	private String ciqProfileName = null;
	private String ciqProfileType = null;
	private int countCiq = 0;
	private Map<String, String> ciq = null;
	private boolean deleteFlag = false;
	private HttpResponse response = null;
	


	public TestParameters(String name,
			String description) {
		this.testName = name;
		this.testDescription = description;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
	}
	
	public TestParameters(String name, String description, String fileName,
			String saasType, boolean deleteFlag) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.deleteFlag = deleteFlag;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, String riskType, List<String> risks, int countUI, int countAPI) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.riskType = riskType;
		this.risks = risks;
		this.countUI = countUI;
		this.countAPI = countAPI;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, String riskType, List<String> risks, int countUI, int countAPI,
			Map<String, String> headersUI, Map<String, String> headersAPI) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.riskType = riskType;
		this.risks = risks;
		this.countUI = countUI;
		this.countAPI = countAPI;
		this.headersUI = headersUI;
		this.headersAPI = headersAPI;
	}
	
	public TestParameters(String name, String description, String fileName,
			String saasType, String fileType, List<String> risks, int countUI,
			Map<String, String> headersUI) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.fileType = fileType;
		this.risks = risks;
		this.countUI = countUI;
		this.headersUI = headersUI;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
	}
	
	
	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks, Map<String,String> ciq, boolean deleteFlag) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
		this.ciq = ciq;
		this.deleteFlag = deleteFlag;
	}
	
	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks, Map<String,String> ciq) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
		this.ciq = ciq;
	}
	
	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks, String ciqProfileName, String ciqProfileType, int countCiq) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
		this.ciqProfileName = ciqProfileName;
		this.ciqProfileType = ciqProfileType;
		this.countCiq = countCiq;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks, String primaryJson) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
		this.riskPrimaryJson = primaryJson;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, List<String> risks, String primaryJson, String secondaryJson) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.risks = risks;
		this.riskPrimaryJson = primaryJson;
		this.riskSecondaryJson = secondaryJson;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, String contentIQProfileName, String dictionaries) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.contentIQProfileName = contentIQProfileName;
		this.dictionaries = dictionaries;
	}

	public TestParameters(String name, String description, String fileName,
			String saasType, String contentIQProfileName, String terms, String count) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.contentIQProfileName = contentIQProfileName;
		this.terms = terms;
		this.count = count;
	}
	
	public TestParameters(String name, String description, String fileName, String saasType,
			String contentIQProfileTerm, String contentIQProfileName, String contentIQProfileDescription, String count) {
		this.testName = name;
		this.testDescription = description;
		this.fileName = fileName;
		this.saasType = saasType;
		this.contentIQProfileTerm=contentIQProfileTerm;
		this.contentIQProfileName = contentIQProfileName;
		this.contentIQProfileDescription=contentIQProfileDescription;
		this.count = count;
	}
	

	public TestParameters(String name, String description, String contentIQProfileName, HttpResponse response) {
		this.testName = name;
		this.testDescription = description;
		this.contentIQProfileName = contentIQProfileName;
		this.response = response;
		
	}

	public String getTestName() {
		return testName;
	}
	public String getTestDescription() {
		return testDescription;
	}
	public String getFileName() {
		return fileName;
	}
	public String getSaasType() {
		return saasType;
	}
	public String getFileType() {
		return fileType;
	}
	public List<String> getRisks() {
		return risks;
	}
	public List<String> getContent() {
		return content;
	}
	public String getContentIQProfileName() {
		return contentIQProfileName;
	}
	public String getContentIQProfileTerm() {
		return contentIQProfileTerm;
	}
	public String getContentIQProfileDescription() {
		return contentIQProfileDescription;
	}
	public String getDictionaries() {
		return dictionaries;
	}
	public String getTerms() {
		return terms;
	}
	public String getCount() {
		return count;
	}
	public String getPrimaryJson() {
		return riskPrimaryJson;
	}
	public String getSecondaryJson() {
		return riskSecondaryJson;
	}
	public String getRiskType() {
		return riskType;
	}
	public int getCountUI() {
		return countUI;
	}
	public int getCountAPI() {
		return countAPI;
	}
	public Map<String, String> getHeadersUI() {
		return headersUI;
	}
	public Map<String, String> getHeadersAPI() {
		return headersAPI;
	}
	public String getCiqProfileName() {
		return ciqProfileName;
	}
	public String getCiqProfileType() {
		return ciqProfileType;
	}
	public int getCountCiq() {
		return countCiq;
	}
	public Map<String, String> getCiq() {
		return ciq;
	}
	public boolean getDeleteFlag() {
		return deleteFlag;
	}
	public HttpResponse getResponse() {
		return response;
	}	

}