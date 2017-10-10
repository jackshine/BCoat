/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elastica.gateway;

import java.io.File;

public class GWCommonUtils {

    public static String getFileSize(String fileName){
    	File file=new File (GatewayTestConstants.UPLOAD_FILE_PATH+fileName );
		String size=Long.toString(file.length()/1024);
		return size;
    }
    
    public static String getFileSizeBig(String fileName){
    	File file=new File (GatewayTestConstants.UPLOAD_BIG_FILE_PATH+fileName );
		String size=Long.toString(file.length()/1024);
		return size;
    }
    
    public static String getMultiFile(String fileName){
    	File file=new File (GatewayTestConstants.MULTIPLE_FILE_PATH +fileName );
		String size=Long.toString(file.length()/1024);
		return size;
    }
    
    
    
    
}
