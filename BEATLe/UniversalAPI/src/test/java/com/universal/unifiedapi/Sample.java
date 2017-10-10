package com.universal.unifiedapi;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

public class Sample {

	public static void main(String args[]) {
		long millis = System.currentTimeMillis();
		
		System.out.println(millis);
		
		
		System.out.println(TimeUnit.MILLISECONDS.toSeconds(millis));
		
		String fs = String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
			);
		System.out.println(fs);
		
		String email = "qa-admin@elasticaqa.net";
		
		System.out.println(StringUtils.split(email, "@")[1].replace(".", ""));
		
	}
	
}
