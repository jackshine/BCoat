package com.elastica.beatle.tests.dci.temp;

import java.io.File;
import org.testng.annotations.Test;

import com.elastica.beatle.S3Utils.S3ActionHandler;
import com.elastica.beatle.dci.DCIConstants;

public class S3Download {

	@Test
	public void Download() throws Exception {
		S3ActionHandler s3 = new S3ActionHandler();
		s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/CIQ", 
				new File(DCIConstants.DCI_FILE_TEMP_PATH));
		s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/Content", 
				new File(DCIConstants.DCI_FILE_TEMP_PATH));
		s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/FileFormat", 
				new File(DCIConstants.DCI_FILE_TEMP_PATH));
		s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/GoldenSet", 
				new File(DCIConstants.DCI_FILE_TEMP_PATH));
		s3.downloadWholeFolder(DCIConstants.DCI_S3_BUCKET, "DCI/TrainingProfiles", 
				new File(DCIConstants.DCI_FILE_TEMP_PATH));
	}

}
