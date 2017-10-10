/**
 * 
 */
package com.elastica.beatle.FileCompressionUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.testng.Reporter;

import com.elastica.beatle.audit.TiaFirewallDto;
import com.elastica.beatle.fileHandler.FileHandlingUtils;

/**
 * @author anuvrath
 *
 */


public class CompressionUtils {
	
	 /*static String tempDir=FileHandlingUtils.getFileAbsolutePath("/src/test/resources/Audit/LogFiles/TIACompressedFiles/");
	 static String filename="McAfee_LowRepURL_01Apr16_UserTIA01_";
	 static String fileFormat=".log";
	 static String actualFileName = FileHandlingUtils.getFileAbsolutePath("/src/test/resources/Audit/LogFiles/TIALogs/McAfee_LowRepURL_01Apr16_UserTIA01.log");
	 
	 static String tempRenamedActualFile = FileHandlingUtils.getFileAbsolutePath("/src/test/resources/Audit/LogFiles/TIACompressedFiles/McAfee_LowRepURL_01Apr16_UserTIA01.log");
	 static String zipFileName = FileHandlingUtils.getFileAbsolutePath("/src/test/resources/Audit/LogFiles/TIACompressedFiles/McAfee_LowRepURL_01Apr16_UserTIA01.zip");
	 */   
	
	/**
	 * @param inputFilePath
	 * @param zipFileName
	 * @return
	 * @throws IOException
	 */
	public String createzipFile(String inputFilePath,String zipFileName) throws IOException {
		byte[] buffer = new byte[1024];
		zipFileName = zipFileName + ".zip";
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
		zos.putNextEntry(new ZipEntry(inputFilePath));
		FileInputStream in = new FileInputStream(inputFilePath);
		int len;
		while ((len = in.read(buffer)) > 0) {
			zos.write(buffer, 0, len);
		}
		in.close();
		zos.closeEntry();
		zos.close();
		return zipFileName;
	}	
	
	/**
	 * @param inputFilePath
	 * @param gzipFileName
	 * @return
	 * @throws Exception
	 */
	public String creategzipFile(String inputFilePath, String gzipFileName) throws Exception {
		byte[] buffer = new byte[1024];			 
		gzipFileName = gzipFileName +".gz";
		GZIPOutputStream gzipOStream = new GZIPOutputStream(new FileOutputStream(gzipFileName));	 
		FileInputStream fileInStream = new FileInputStream(inputFilePath);	 
	    int len;
	    while ((len = fileInStream.read(buffer)) > 0) {
	    	gzipOStream.write(buffer, 0, len);
	    }
	    fileInStream.close();
	    gzipOStream.finish();
	    gzipOStream.close();
		return gzipFileName;	
	}
	
	public static String editTheFirewallLogAndMakeItCompressionFormat(TiaFirewallDto tiafirewallDto) throws Exception
	{
		
		String tmpFileName= createTemporayFileinSpecificDir(FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getTempDirPath()),tiafirewallDto.getTiaFileName(),tiafirewallDto.getTiaFileFormat());
		replace(FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getActualFileName()),tmpFileName,FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getTempRenamedActualFile()),tiafirewallDto.getTiaUser(),tiafirewallDto.getRandomId());
		zipSingleFile(new File(FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getTempRenamedActualFile())), FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getZipFileName()));
		new File(FileHandlingUtils.getFileAbsolutePath(tiafirewallDto.getTempRenamedActualFile())).delete();
		
		/*String tmpFileName= createTemporayFileinSpecificDir(tempDir,filename,fileFormat);
	    replace(actualFileName,tmpFileName,tempRenamedActualFile);
		zipSingleFile(new File(tempRenamedActualFile), zipFileName);
		new File(tempRenamedActualFile).delete();
		*/
		return null;
	}
	public static void replace(String oldFileName, String tmpFileName, String renamedFile,String user,long randomNumber ) {
		//  /src/test/java/com/elastica/beatle/McAfee_LowRepURL_01Apr16_UserTIA01.log
	     
	      //String user="UserTIA03";
	     // long randomNumber = Math.round(Math.random()*1000);
	     

	      BufferedReader br = null;
	      BufferedWriter bw = null;
	      try {
	         br = new BufferedReader(new FileReader(oldFileName));
	         bw = new BufferedWriter(new FileWriter(tmpFileName));
	         String line;
	         while ((line = br.readLine()) != null) {
	            if (line.contains(user))
	               line = line.replace(user, user+"_"+randomNumber);
	            bw.write(line+"\n");
	         }
	      } catch (Exception e) {
	         return;
	      } finally {
	         try {
	            if(br != null)
	               br.close();
	         } catch (IOException e) {
	            //
	         }
	         try {
	            if(bw != null)
	               bw.close();
	         } catch (IOException e) {
	            //
	         }
	      }
	      // Once everything is complete, delete old file..
	      File oldFile = new File(renamedFile);
	      //oldFile.delete();

	      // And rename tmp file's name to old file name
	      File newFile = new File(tmpFileName);
	      newFile.renameTo(oldFile);
	      Reporter.log("new file written...",true);

	   }
	private static void zipSingleFile(File file, String zipFileName) {
        try {
            //create ZipOutputStream to write to the zip file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            //add a new Zip Entry to the ZipOutputStream
            ZipEntry ze = new ZipEntry(file.getName());
            zos.putNextEntry(ze);
            //read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
             
            //Close the zip entry to write to zip file
            zos.closeEntry();
            //Close resources
            zos.close();
            fis.close();
            fos.close();
            Reporter.log(file.getCanonicalPath()+" is zipped to "+zipFileName,true);
             
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
	
	public static String createTemporayFileinSpecificDir(String tempDir,String filename, String fileFormat) throws Exception
	{
		File file = null;
	    File dir = new File(tempDir);
	    
	    try
	    {
	      file = File.createTempFile(filename, fileFormat, dir);
	    }
	    catch(IOException ioe)
	    {
	    System.out.println("Exception creating temporary file : " + ioe);
	    }
	 
	    /*
	     * Please note that if the directory does not exists, IOException will be
	     * thrown and temporary file will not be created.
	     */
	    System.out.println("Temporary file created at : " + file.getPath());
	    return  file.getPath();
	  }
	
	
}
