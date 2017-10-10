package com.elastica.beatle.tests.dci.temp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssemblaQuery {

	public static void main(String[] args) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(
	            "curl",
	            "-H \"X-Api-Key: 987ad11b18a5dcbe70fe\"",
	            "-H \"X-Api-Secret: 88deffa6d095d06e76df8108b793f5ce3b6104f8\"",
	            "https://api.assembla.com/v1/spaces/elastica/tickets/27898.json");

		
		
	    Process p = pb.start();
	    InputStream is = p.getInputStream();
	    
	    String line;
	    BufferedInputStream bis = new BufferedInputStream(is);
	}

}
