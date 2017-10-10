package com.elastica.beatle.dci.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;


public class ContentChecks {

	@JsonProperty("_silent")
	private List<Object> Silent = new ArrayList<Object>();
	@JsonProperty("partial")
	private Boolean partial;
	@JsonProperty("filename")
	private String filename;
	@JsonProperty("vk_dlp")
	private int vkDlp;
	@JsonProperty("vk_ferpa")
	private int vkFerpa;
	@JsonProperty("vk_glba")
	private int vkGlba;
	@JsonProperty("vk_hipaa")
	private int vkHipaa;
	@JsonProperty("vk_pci")
	private int vkPci;
	@JsonProperty("vk_pii")
	private int vkPii;
	@JsonProperty("vk_vba_macros")
	private int vkVbaMacros;
	@JsonProperty("vk_virus")
	private int vkVirus;
	@JsonProperty("mimetype")
	private String mimetype;
	@JsonProperty("violations")
	private boolean violations;
	@JsonProperty("vk_content_iq_violations")
	private List<String> vkContentIqViolations = new ArrayList<String>();
	@JsonProperty("training_profiles")
	private List<String> trainingProfiles = new ArrayList<String>();
	@JsonProperty("doc_class")
	private List<String> docClass = new ArrayList<String>();
	@JsonProperty("glba")
	private Glba glba;
	@JsonProperty("hipaa")
	private Hipaa hipaa;
	@JsonProperty("vba_macros")
	private VbaMacros vbaMacros;
	@JsonProperty("virus")
	private Virus virus;
	@JsonProperty("pci")
	private Pci pci;
	@JsonProperty("pii")
	private Pii pii;
	@JsonProperty("content_iq_violations")
	private List<ContentIqViolation> contentIqViolations = new ArrayList<ContentIqViolation>();
	@JsonProperty("audio")
	private Audio audio;
	@JsonProperty("cryptographic_keys")
	private CryptographicKeys cryptographicKeys;
	@JsonProperty("executable")
	private Executable executable;
	@JsonProperty("image")
	private Image image;
	@JsonProperty("video")
	private Video video;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	/**
	 * 
	 * @return
	 * The vkGlba
	 */
	@JsonProperty("vk_glba")
	public int getVkGlba() {
		return vkGlba;
	}

	/**
	 * 
	 * @param vkGlba
	 * The vk_glba
	 */
	@JsonProperty("vk_glba")
	public void setVkGlba(int vkGlba) {
		this.vkGlba = vkGlba;
	}

	/**
	 * 
	 * @return
	 * The vkDlp
	 */
	@JsonProperty("vk_dlp")
	public int getVkDlp() {
		return vkDlp;
	}

	/**
	 * 
	 * @param vkDlp
	 * The vk_dlp
	 */
	@JsonProperty("vk_dlp")
	public void setVkDlp(int vkDlp) {
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
	 * The violations
	 */
	@JsonProperty("violations")
	public boolean getViolations() {
		return violations;
	}

	/**
	 * 
	 * @param violations
	 * The violations
	 */
	@JsonProperty("violations")
	public void setViolations(boolean violations) {
		this.violations = violations;
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
	 * The vkVbaMacros
	 */
	@JsonProperty("vk_vba_macros")
	public int getVkVbaMacros() {
		return vkVbaMacros;
	}

	/**
	 * 
	 * @param vkVbaMacros
	 * The vk_vba_macros
	 */
	@JsonProperty("vk_vba_macros")
	public void setVkVbaMacros(int vkVbaMacros) {
		this.vkVbaMacros = vkVbaMacros;
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
	 * The vkVirus
	 */
	@JsonProperty("vk_virus")
	public int getVkVirus() {
		return vkVirus;
	}

	/**
	 * 
	 * @param vkVirus
	 * The vk_virus
	 */
	@JsonProperty("vk_virus")
	public void setVkVirus(int vkVirus) {
		this.vkVirus = vkVirus;
	}

	/**
	 * 
	 * @return
	 * The vkPci
	 */
	@JsonProperty("vk_pci")
	public int getVkPci() {
		return vkPci;
	}

	/**
	 * 
	 * @param vkPci
	 * The vk_pci
	 */
	@JsonProperty("vk_pci")
	public void setVkPci(int vkPci) {
		this.vkPci = vkPci;
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
	 * The vkPii
	 */
	@JsonProperty("vk_pii")
	public int getVkPii() {
		return vkPii;
	}

	/**
	 * 
	 * @param vkPii
	 * The vk_pii
	 */
	@JsonProperty("vk_pii")
	public void setVkPii(int vkPii) {
		this.vkPii = vkPii;
	}

	/**
	 * 
	 * @return
	 * The vkFerpa
	 */
	@JsonProperty("vk_ferpa")
	public int getVkFerpa() {
		return vkFerpa;
	}

	/**
	 * 
	 * @param vkHipaa
	 * The vk_hipaa
	 */
	@JsonProperty("vk_ferpa")
	public void setVkFerpa(int vkFerpa) {
		this.vkFerpa = vkFerpa;
	}
	
	/**
	 * 
	 * @return
	 * The vkHipaa
	 */
	@JsonProperty("vk_hipaa")
	public int getVkHipaa() {
		return vkHipaa;
	}

	/**
	 * 
	 * @param vkHipaa
	 * The vk_hipaa
	 */
	@JsonProperty("vk_hipaa")
	public void setVkHipaa(int vkHipaa) {
		this.vkHipaa = vkHipaa;
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
	 * @return the audio
	 */
	@JsonProperty("audio")
	public Audio getAudio() {
		return audio;
	}

	/**
	 * @param audio the audio to set
	 */
	@JsonProperty("audio")
	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	/**
	 * @return the executable
	 */
	@JsonProperty("executable")
	public Executable getExecutable() {
		return executable;
	}

	/**
	 * @param executable the executable to set
	 */
	@JsonProperty("executable")
	public void setExecutable(Executable executable) {
		this.executable = executable;
	}

	/**
	 * @return the image
	 */
	@JsonProperty("image")
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	@JsonProperty("image")
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the video
	 */
	@JsonProperty("video")
	public Video getVideo() {
		return video;
	}

	/**
	 * @param video the video to set
	 */
	@JsonProperty("video")
	public void setVideo(Video video) {
		this.video = video;
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
	 * @return the trainingProfiles
	 */
	public List<String> getTrainingProfiles() {
		return trainingProfiles;
	}

	/**
	 * @param trainingProfiles the trainingProfiles to set
	 */
	public void setTrainingProfiles(List<String> trainingProfiles) {
		this.trainingProfiles = trainingProfiles;
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
