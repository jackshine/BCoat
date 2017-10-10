package com.elastica.beatle.detect;

import com.elastica.beatle.TestSuiteDTO;

public class DetectSuiteParams extends TestSuiteDTO {

	private String goldenInputTmplPath;
	private String goldenInputFilePath;
	
	private String esScriptsHostName;
	private String esScriptsUserName;
	
	
	
	
	public String getEsScriptsHostName() {
		return esScriptsHostName;
	}

	public void setEsScriptsHostName(String esScriptsHostName) {
		this.esScriptsHostName = esScriptsHostName;
	}

	public String getEsScriptsUserName() {
		return esScriptsUserName;
	}

	public void setEsScriptsUserName(String esScriptsUserName) {
		this.esScriptsUserName = esScriptsUserName;
	}

	public String getGoldenInputTmplPath() {
		return goldenInputTmplPath;
	}

	public void setGoldenInputTmplPath(String goldenInputTmplPath) {
		this.goldenInputTmplPath = goldenInputTmplPath;
	}

	public String getGoldenInputFilePath() {
		return goldenInputFilePath;
	}

	public void setGoldenInputFilePath(String goldenInputFilePath) {
		this.goldenInputFilePath = goldenInputFilePath;
	}
}
