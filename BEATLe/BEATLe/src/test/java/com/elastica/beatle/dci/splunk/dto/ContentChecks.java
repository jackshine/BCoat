package com.elastica.beatle.dci.splunk.dto;

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
	@JsonProperty("vk_ferpa")
	private Integer vkFerpa;
	@JsonProperty("_silent")
	private List<Object> Silent = new ArrayList<Object>();
	@JsonProperty("partial")
	private Boolean partial;
	@JsonProperty("vk_glba")
	private Integer vkGlba;
	@JsonProperty("vk_pci")
	private Integer vkPci;
	@JsonProperty("vk_content_iq_violations")
	private List<Object> vkContentIqViolations = new ArrayList<Object>();
	@JsonProperty("doc_class")
	private List<Object> docClass = new ArrayList<Object>();
	@JsonProperty("filename")
	private String filename;
	@JsonProperty("vk_vba_macros")
	private Integer vkVbaMacros;
	@JsonProperty("vk_dlp")
	private Integer vkDlp;
	@JsonProperty("cryptographic_keys")
	private CryptographicKeys cryptographicKeys;
	@JsonProperty("vk_pii")
	private Integer vkPii;
	@JsonProperty("violations")
	private Boolean violations;
	@JsonProperty("vk_virus")
	private Integer vkVirus;
	@JsonProperty("vk_hipaa")
	private Integer vkHipaa;
	@JsonProperty("metadata")
	private Metadata metadata;
	@JsonProperty("pii")
	private Pii pii;
	@JsonProperty("pci")
	private Pci pci;
	@JsonProperty("hipaa")
	private Hipaa hipaa;
	@JsonProperty("glba")
	private Glba glba;
	@JsonProperty("vba_macros")
	private VbaMacros vbaMacros;
	@JsonProperty("virus")
	private Virus virus;


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
	 * The vkFerpa
	 */
	@JsonProperty("vk_ferpa")
	public Integer getVkFerpa() {
		return vkFerpa;
	}

	/**
	 * 
	 * @param vkFerpa
	 * The vk_ferpa
	 */
	@JsonProperty("vk_ferpa")
	public void setVkFerpa(Integer vkFerpa) {
		this.vkFerpa = vkFerpa;
	}

	/**
	 * 
	 * @return
	 * The Silent
	 */
	@JsonProperty("_silent")
	public List<Object> getSilent() {
		return Silent;
	}

	/**
	 * 
	 * @param Silent
	 * The _silent
	 */
	@JsonProperty("_silent")
	public void setSilent(List<Object> Silent) {
		this.Silent = Silent;
	}

	/**
	 * 
	 * @return
	 * The partial
	 */
	@JsonProperty("partial")
	public Boolean getPartial() {
		return partial;
	}

	/**
	 * 
	 * @param partial
	 * The partial
	 */
	@JsonProperty("partial")
	public void setPartial(Boolean partial) {
		this.partial = partial;
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
	 * The vkContentIqViolations
	 */
	@JsonProperty("vk_content_iq_violations")
	public List<Object> getVkContentIqViolations() {
		return vkContentIqViolations;
	}

	/**
	 * 
	 * @param vkContentIqViolations
	 * The vk_content_iq_violations
	 */
	@JsonProperty("vk_content_iq_violations")
	public void setVkContentIqViolations(List<Object> vkContentIqViolations) {
		this.vkContentIqViolations = vkContentIqViolations;
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
	 * The cryptographicKeys
	 */
	@JsonProperty("cryptographic_keys")
	public CryptographicKeys getCryptographicKeys() {
		return cryptographicKeys;
	}

	/**
	 * 
	 * @param cryptographicKeys
	 * The cryptographic_keys
	 */
	@JsonProperty("cryptographic_keys")
	public void setCryptographicKeys(CryptographicKeys cryptographicKeys) {
		this.cryptographicKeys = cryptographicKeys;
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
	 * The metadata
	 */
	@JsonProperty("metadata")
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * 
	 * @param metadata
	 * The metadata
	 */
	@JsonProperty("metadata")
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
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
	 * The pci
	 */
	@JsonProperty("pci")
	public Pci getPci() {
		return pci;
	}

	/**
	 * 
	 * @param pci
	 * The pci
	 */
	@JsonProperty("pci")
	public void setPci(Pci pci) {
		this.pci = pci;
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
	 * The glba
	 */
	@JsonProperty("glba")
	public Glba getGlba() {
		return glba;
	}

	/**
	 * 
	 * @param glba
	 * The glba
	 */
	@JsonProperty("glba")
	public void setGlba(Glba glba) {
		this.glba = glba;
	}

	/**
	 * 
	 * @return
	 * The vba_macros
	 */
	@JsonProperty("vba_macros")
	public VbaMacros getVbaMacros() {
		return vbaMacros;
	}

	/**
	 * 
	 * @param vba_macros
	 * The vba_macros
	 */
	@JsonProperty("vba_macros")
	public void setVbaMacros(VbaMacros vbaMacros) {
		this.vbaMacros = vbaMacros;
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


	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}

