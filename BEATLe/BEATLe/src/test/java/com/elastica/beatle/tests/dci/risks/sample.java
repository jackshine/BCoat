package com.elastica.beatle.tests.dci.risks;

import java.util.Arrays;

import com.elastica.beatle.dci.DCIConstants;
import com.elastica.beatle.dci.DCIFunctions;

public class sample {

	public static void main(String[] args) {
		DCIFunctions dciFunctions = new DCIFunctions();


		String[] fileName = dciFunctions.getFileName(DCIConstants.DCI_FILE_UPLOAD_PII_P2_FILES_PATH);
		String[] risks = dciFunctions.riskTypesForAFile(fileName);

		System.out.println(Arrays.asList(risks));
	}

}
