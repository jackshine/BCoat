package com.elastica.beatle.ciq.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
public class ContentChecks {

	@JsonProperty("mimetype")
	private String mimetype;
	@JsonProperty("pii")
	private Pii pii;
	@JsonProperty("content_iq_violations")
	private List<ContentIqViolation> contentIqViolations = new ArrayList<ContentIqViolation>();
	@JsonProperty("vk_virus")
	private Long vkVirus;
	@JsonProperty("vk_pci")
	private Long vkPci;
	@JsonProperty("vk_content_iq_violations")
	private List<String> vkContentIqViolations = new ArrayList<String>();
	@JsonProperty("virus")
	private Virus virus;
	@JsonProperty("doc_class")
	private List<Object> docClass = new ArrayList<Object>();
	@JsonProperty("filename")
	private String filename;
	@JsonProperty("vk_vba_macros")
	private Long vkVbaMacros;
	@JsonProperty("vk_encryption")
	private Long vkEncryption;
	@JsonProperty("vk_pii")
	private Long vkPii;
	@JsonProperty("vba_macros")
	private VbaMacros vbaMacros;
	@JsonProperty("violations")
	private Boolean violations;
	@JsonProperty("vk_source_code")
	private Long vkSourceCode;
	@JsonProperty("hipaa")
	private Hipaa hipaa;
	@JsonProperty("vk_dlp")
	private Long vkDlp;
	@JsonProperty("vk_hipaa")
	private Long vkHipaa;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return
	 * The mimetype
	 */
	@JsonProperty("mimetype")
	public String getMimetype() {
		return mimetype;
	}

	/**
	 * 
	 * @param mimetype
	 * The mimetype
	 */
	@JsonProperty("mimetype")
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	/**
	 * 
	 * @return
	 * The pii
	 */
	@JsonProperty("pii")
	public Pii getPii() {
		return pii;
	}

	/**
	 * 
	 * @param pii
	 * The pii
	 */
	@JsonProperty("pii")
	public void setPii(Pii pii) {
		this.pii = pii;
	}

	/**
	 * 
	 * @return
	 * The contentIqViolations
	 */
	@JsonProperty("content_iq_violations")
	public List<ContentIqViolation> getContentIqViolations() {
		return contentIqViolations;
	}

	/**
	 * 
	 * @param contentIqViolations
	 * The content_iq_violations
	 */
	@JsonProperty("content_iq_violations")
	public void setContentIqViolations(List<ContentIqViolation> contentIqViolations) {
		this.contentIqViolations = contentIqViolations;
	}

	/**
	 * 
	 * @return
	 * The vkVirus
	 */
	@JsonProperty("vk_virus")
	public Long getVkVirus() {
		return vkVirus;
	}

	/**
	 * 
	 * @param vkVirus
	 * The vk_virus
	 */
	@JsonProperty("vk_virus")
	public void setVkVirus(Long vkVirus) {
		this.vkVirus = vkVirus;
	}

	/**
	 * 
	 * @return
	 * The vkPci
	 */
	@JsonProperty("vk_pci")
	public Long getVkPci() {
		return vkPci;
	}

	/**
	 * 
	 * @param vkPci
	 * The vk_pci
	 */
	@JsonProperty("vk_pci")
	public void setVkPci(Long vkPci) {
		this.vkPci = vkPci;
	}

	/**
	 * 
	 * @return
	 * The vkContentIqViolations
	 */
	@JsonProperty("vk_content_iq_violations")
	public List<String> getVkContentIqViolations() {
		return vkContentIqViolations;
	}

	/**
	 * 
	 * @param vkContentIqViolations
	 * The vk_content_iq_violations
	 */
	@JsonProperty("vk_content_iq_violations")
	public void setVkContentIqViolations(List<String> vkContentIqViolations) {
		this.vkContentIqViolations = vkContentIqViolations;
	}

	/**
	 * 
	 * @return
	 * The virus
	 */
	@JsonProperty("virus")
	public Virus getVirus() {
		return virus;
	}

	/**
	 * 
	 * @param virus
	 * The virus
	 */
	@JsonProperty("virus")
	public void setVirus(Virus virus) {
		this.virus = virus;
	}

	/**
	 * 
	 * @return
	 * The docClass
	 */
	@JsonProperty("doc_class")
	public List<Object> getDocClass() {
		return docClass;
	}

	/**
	 * 
	 * @param docClass
	 * The doc_class
	 */
	@JsonProperty("doc_class")
	public void setDocClass(List<Object> docClass) {
		this.docClass = docClass;
	}

	/**
	 * 
	 * @return
	 * The filename
	 */
	@JsonProperty("filename")
	public String getFilename() {
		return filename;
	}

	/**
	 * 
	 * @param filename
	 * The filename
	 */
	@JsonProperty("filename")
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * 
	 * @return
	 * The vkVbaMacros
	 */
	@JsonProperty("vk_vba_macros")
	public Long getVkVbaMacros() {
		return vkVbaMacros;
	}

	/**
	 * 
	 * @param vkVbaMacros
	 * The vk_vba_macros
	 */
	@JsonProperty("vk_vba_macros")
	public void setVkVbaMacros(Long vkVbaMacros) {
		this.vkVbaMacros = vkVbaMacros;
	}

	/**
	 * 
	 * @return
	 * The vkEncryption
	 */
	@JsonProperty("vk_encryption")
	public Long getVkEncryption() {
		return vkEncryption;
	}

	/**
	 * 
	 * @param vkEncryption
	 * The vk_encryption
	 */
	@JsonProperty("vk_encryption")
	public void setVkEncryption(Long vkEncryption) {
		this.vkEncryption = vkEncryption;
	}

	/**
	 * 
	 * @return
	 * The vkPii
	 */
	@JsonProperty("vk_pii")
	public Long getVkPii() {
		return vkPii;
	}

	/**
	 * 
	 * @param vkPii
	 * The vk_pii
	 */
	@JsonProperty("vk_pii")
	public void setVkPii(Long vkPii) {
		this.vkPii = vkPii;
	}

	/**
	 * 
	 * @return
	 * The vbaMacros
	 */
	@JsonProperty("vba_macros")
	public VbaMacros getVbaMacros() {
		return vbaMacros;
	}

	/**
	 * 
	 * @param vbaMacros
	 * The vba_macros
	 */
	@JsonProperty("vba_macros")
	public void setVbaMacros(VbaMacros vbaMacros) {
		this.vbaMacros = vbaMacros;
	}

	/**
	 * 
	 * @return
	 * The violations
	 */
	@JsonProperty("violations")
	public Boolean getViolations() {
		return violations;
	}

	/**
	 * 
	 * @param violations
	 * The violations
	 */
	@JsonProperty("violations")
	public void setViolations(Boolean violations) {
		this.violations = violations;
	}

	/**
	 * 
	 * @return
	 * The vkSourceCode
	 */
	@JsonProperty("vk_source_code")
	public Long getVkSourceCode() {
		return vkSourceCode;
	}

	/**
	 * 
	 * @param vkSourceCode
	 * The vk_source_code
	 */
	@JsonProperty("vk_source_code")
	public void setVkSourceCode(Long vkSourceCode) {
		this.vkSourceCode = vkSourceCode;
	}

	/**
	 * 
	 * @return
	 * The hipaa
	 */
	@JsonProperty("hipaa")
	public Hipaa getHipaa() {
		return hipaa;
	}

	/**
	 * 
	 * @param hipaa
	 * The hipaa
	 */
	@JsonProperty("hipaa")
	public void setHipaa(Hipaa hipaa) {
		this.hipaa = hipaa;
	}

	/**
	 * 
	 * @return
	 * The vkDlp
	 */
	@JsonProperty("vk_dlp")
	public Long getVkDlp() {
		return vkDlp;
	}

	/**
	 * 
	 * @param vkDlp
	 * The vk_dlp
	 */
	@JsonProperty("vk_dlp")
	public void setVkDlp(Long vkDlp) {
		this.vkDlp = vkDlp;
	}

	/**
	 * 
	 * @return
	 * The vkHipaa
	 */
	@JsonProperty("vk_hipaa")
	public Long getVkHipaa() {
		return vkHipaa;
	}

	/**
	 * 
	 * @param vkHipaa
	 * The vk_hipaa
	 */
	@JsonProperty("vk_hipaa")
	public void setVkHipaa(Long vkHipaa) {
		this.vkHipaa = vkHipaa;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}