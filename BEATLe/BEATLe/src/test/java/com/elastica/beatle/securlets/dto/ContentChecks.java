package com.elastica.beatle.securlets.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;


public class ContentChecks {

	@JsonProperty("content_iq_violations")
	private java.lang.Object contentIqViolations;
	@JsonProperty("dlp")
	private java.lang.Object dlp;
	@JsonProperty("doc_class")
	private List<String> docClass;
	@JsonProperty("encryption")
	private java.lang.Object encryption;
	@JsonProperty("glba")
	private java.lang.Object glba;
	@JsonProperty("hipaa")
	private java.lang.Object hipaa;
	@JsonProperty("id")
	private String id;
	@JsonProperty("pci")
	private java.lang.Object pci;
	@JsonProperty("pii")
	private java.lang.Object pii;
	@JsonProperty("vba_macros")
	private java.lang.Object vbaMacros;
	@JsonProperty("violations")
	private Boolean violations;
	@JsonProperty("virus")
	private java.lang.Object virus;
	@JsonProperty("vk_content_iq_violations")
	private List<java.lang.Object> vkContentIqViolations = new ArrayList<java.lang.Object>();
	@JsonProperty("vk_dlp")
	private Integer vkDlp;
	@JsonProperty("vk_encryption")
	private Integer vkEncryption;
	@JsonProperty("vk_glba")
	private Integer vkGlba;
	@JsonProperty("vk_hipaa")
	private Integer vkHipaa;
	@JsonProperty("vk_pci")
	private Integer vkPci;
	@JsonProperty("vk_pii")
	private Integer vkPii;
	@JsonProperty("vk_source_code")
	private Integer vkSourceCode;
	@JsonProperty("vk_vba_macros")
	private Integer vkVbaMacros;
	@JsonProperty("vk_virus")
	private Integer vkVirus;
	@JsonProperty("source_code")
	private SourceCode sourceCode;
	@JsonProperty("ferpa")
	private java.lang.Object ferpa;
	
	@JsonProperty("vk_ferpa")
	private Integer vkFerpa;
	
	@JsonProperty("metadata")
	private Metadata metadata;

	/**
	 * @return the metadata
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the ferpa
	 */
	public java.lang.Object getFerpa() {
		return ferpa;
	}

	/**
	 * @param ferpa the ferpa to set
	 */
	public void setFerpa(java.lang.Object ferpa) {
		this.ferpa = ferpa;
	}

	/**
	 * @return the vkFerpa
	 */
	public Integer getVkFerpa() {
		return vkFerpa;
	}

	/**
	 * @param vkFerpa the vkFerpa to set
	 */
	public void setVkFerpa(Integer vkFerpa) {
		this.vkFerpa = vkFerpa;
	}

	/**
	 *
	 * @return
	 * The contentIqViolations
	 */
	@JsonProperty("content_iq_violations")
	public java.lang.Object getContentIqViolations() {
		return contentIqViolations;
	}

	/**
	 *
	 * @param contentIqViolations
	 * The content_iq_violations
	 */
	@JsonProperty("content_iq_violations")
	public void setContentIqViolations(java.lang.Object contentIqViolations) {
		this.contentIqViolations = contentIqViolations;
	}

	/**
	 *
	 * @return
	 * The dlp
	 */
	@JsonProperty("dlp")
	public java.lang.Object getDlp() {
		return dlp;
	}

	/**
	 *
	 * @param dlp
	 * The dlp
	 */
	@JsonProperty("dlp")
	public void setDlp(java.lang.Object dlp) {
		this.dlp = dlp;
	}

	/**
	 *
	 * @return
	 * The docClass
	 */
	@JsonProperty("doc_class")
	public List<String> getDocClass() {
		return docClass;
	}

	/**
	 *
	 * @param docClass
	 * The doc_class
	 */
	@JsonProperty("doc_class")
	public void setDocClass(List<String> docClass) {
		this.docClass = docClass;
	}

	/**
	 *
	 * @return
	 * The encryption
	 */
	@JsonProperty("encryption")
	public java.lang.Object getEncryption() {
		return encryption;
	}

	/**
	 *
	 * @param encryption
	 * The encryption
	 */
	@JsonProperty("encryption")
	public void setEncryption(java.lang.Object encryption) {
		this.encryption = encryption;
	}

	/**
	 *
	 * @return
	 * The glba
	 */
	@JsonProperty("glba")
	public java.lang.Object getGlba() {
		return glba;
	}

	/**
	 *
	 * @param glba
	 * The glba
	 */
	@JsonProperty("glba")
	public void setGlba(java.lang.Object glba) {
		this.glba = glba;
	}

	/**
	 *
	 * @return
	 * The hipaa
	 */
	@JsonProperty("hipaa")
	public java.lang.Object getHipaa() {
		return hipaa;
	}

	/**
	 *
	 * @param hipaa
	 * The hipaa
	 */
	@JsonProperty("hipaa")
	public void setHipaa(java.lang.Object hipaa) {
		this.hipaa = hipaa;
	}

	/**
	 *
	 * @return
	 * The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 * The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	/**
	 *
	 * @return
	 * The pci
	 */
	@JsonProperty("pci")
	public java.lang.Object getPci() {
		return pci;
	}

	/**
	 *
	 * @param pci
	 * The pci
	 */
	@JsonProperty("pci")
	public void setPci(java.lang.Object pci) {
		this.pci = pci;
	}

	/**
	 *
	 * @return
	 * The pii
	 */
	@JsonProperty("pii")
	public java.lang.Object getPii() {
		return pii;
	}

	/**
	 *
	 * @param pii
	 * The pii
	 */
	@JsonProperty("pii")
	public void setPii(java.lang.Object pii) {
		this.pii = pii;
	}

	/**
	 *
	 * @return
	 * The vbaMacros
	 */
	@JsonProperty("vba_macros")
	public java.lang.Object getVbaMacros() {
		return vbaMacros;
	}

	/**
	 *
	 * @param vbaMacros
	 * The vba_macros
	 */
	@JsonProperty("vba_macros")
	public void setVbaMacros(java.lang.Object vbaMacros) {
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
	 * The virus
	 */
	@JsonProperty("virus")
	public java.lang.Object getVirus() {
		return virus;
	}

	/**
	 *
	 * @param virus
	 * The virus
	 */
	@JsonProperty("virus")
	public void setVirus(java.lang.Object virus) {
		this.virus = virus;
	}

	/**
	 *
	 * @return
	 * The vkContentIqViolations
	 */
	@JsonProperty("vk_content_iq_violations")
	public List<java.lang.Object> getVkContentIqViolations() {
		return vkContentIqViolations;
	}

	/**
	 *
	 * @param vkContentIqViolations
	 * The vk_content_iq_violations
	 */
	@JsonProperty("vk_content_iq_violations")
	public void setVkContentIqViolations(List<java.lang.Object> vkContentIqViolations) {
		this.vkContentIqViolations = vkContentIqViolations;
	}

	/**
	 *
	 * @return
	 * The vkDlp
	 */
	@JsonProperty("vk_dlp")
	public Integer getVkDlp() {
		return vkDlp;
	}

	/**
	 *
	 * @param vkDlp
	 * The vk_dlp
	 */
	@JsonProperty("vk_dlp")
	public void setVkDlp(Integer vkDlp) {
		this.vkDlp = vkDlp;
	}

	/**
	 *
	 * @return
	 * The vkEncryption
	 */
	@JsonProperty("vk_encryption")
	public Integer getVkEncryption() {
		return vkEncryption;
	}

	/**
	 *
	 * @param vkEncryption
	 * The vk_encryption
	 */
	@JsonProperty("vk_encryption")
	public void setVkEncryption(Integer vkEncryption) {
		this.vkEncryption = vkEncryption;
	}

	/**
	 *
	 * @return
	 * The vkGlba
	 */
	@JsonProperty("vk_glba")
	public Integer getVkGlba() {
		return vkGlba;
	}

	/**
	 *
	 * @param vkGlba
	 * The vk_glba
	 */
	@JsonProperty("vk_glba")
	public void setVkGlba(Integer vkGlba) {
		this.vkGlba = vkGlba;
	}

	/**
	 *
	 * @return
	 * The vkHipaa
	 */
	@JsonProperty("vk_hipaa")
	public Integer getVkHipaa() {
		return vkHipaa;
	}

	/**
	 *
	 * @param vkHipaa
	 * The vk_hipaa
	 */
	@JsonProperty("vk_hipaa")
	public void setVkHipaa(Integer vkHipaa) {
		this.vkHipaa = vkHipaa;
	}

	/**
	 *
	 * @return
	 * The vkPci
	 */
	@JsonProperty("vk_pci")
	public Integer getVkPci() {
		return vkPci;
	}

	/**
	 *
	 * @param vkPci
	 * The vk_pci
	 */
	@JsonProperty("vk_pci")
	public void setVkPci(Integer vkPci) {
		this.vkPci = vkPci;
	}

	/**
	 *
	 * @return
	 * The vkPii
	 */
	@JsonProperty("vk_pii")
	public Integer getVkPii() {
		return vkPii;
	}

	/**
	 *
	 * @param vkPii
	 * The vk_pii
	 */
	@JsonProperty("vk_pii")
	public void setVkPii(Integer vkPii) {
		this.vkPii = vkPii;
	}

	/**
	 *
	 * @return
	 * The vkSourceCode
	 */
	@JsonProperty("vk_source_code")
	public Integer getVkSourceCode() {
		return vkSourceCode;
	}

	/**
	 *
	 * @param vkSourceCode
	 * The vk_source_code
	 */
	@JsonProperty("vk_source_code")
	public void setVkSourceCode(Integer vkSourceCode) {
		this.vkSourceCode = vkSourceCode;
	}

	/**
	 *
	 * @return
	 * The vkVbaMacros
	 */
	@JsonProperty("vk_vba_macros")
	public Integer getVkVbaMacros() {
		return vkVbaMacros;
	}

	/**
	 *
	 * @param vkVbaMacros
	 * The vk_vba_macros
	 */
	@JsonProperty("vk_vba_macros")
	public void setVkVbaMacros(Integer vkVbaMacros) {
		this.vkVbaMacros = vkVbaMacros;
	}

	/**
	 *
	 * @return
	 * The vkVirus
	 */
	@JsonProperty("vk_virus")
	public Integer getVkVirus() {
		return vkVirus;
	}

	/**
	 *
	 * @param vkVirus
	 * The vk_virus
	 */
	@JsonProperty("vk_virus")
	public void setVkVirus(Integer vkVirus) {
		this.vkVirus = vkVirus;
	}

	

}