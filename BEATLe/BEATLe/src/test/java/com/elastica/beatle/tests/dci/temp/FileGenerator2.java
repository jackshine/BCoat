package com.elastica.beatle.tests.dci.temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class FileGenerator2 {


	/*Driver function to check for above function*/
	public static void main (String[] args) throws IOException {
		String filePathRisk = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"Risks";
		String filePathContent = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"Contents";
		String filePathTP = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"TP";
		String filePathPDD = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"PDD.txt";
		String filePathPDT = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"PDT.txt";
		String filePathCD = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"CD.txt";
		String filePathCT = File.separator+"Users"+File.separator+"eldorajan"
				+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"CT.txt";

		String[] fileRisks = getFileName(filePathRisk);
		String[] fileContent = getFileName(filePathContent);
		String[] fileTP = getFileName(filePathTP);

		for(int i=0;i<fileRisks.length;i++){
			for(int j=0;j<fileContent.length;j++){
				for(int k=0;k<fileTP.length;k++){
					File fileC = new File( File.separator+"Users"+File.separator+"eldorajan"+File.separator+
							"Desktop"+File.separator+"Combinations"+File.separator+"Files"+File.separator+
							fileRisks[i].replace(".txt", "")+"_"+fileContent[j].replace(".txt", "")+"_"+fileTP[k].replace(".txt", "")
							+"_"+"PDD"+"_"+"PDT"+"_"+"CD"+"_"+"CT.txt");

					File fileW = new File( File.separator+"Users"+File.separator+"eldorajan"+File.separator+
							"Desktop"+File.separator+"Combinations"+File.separator+"Files"+File.separator+
							fileRisks[i].replace(".txt", "")+"_"+fileContent[j].replace(".txt", "")+"_"+fileTP[k].replace(".txt", "")
							+"_"+"PDD"+"_"+"PDT"+"_"+"CD"+"_"+"CT.docx");

					String file1Str = FileUtils.readFileToString(new File(filePathPDD));
					String file2Str = FileUtils.readFileToString(new File(filePathPDT));
					String file3Str = FileUtils.readFileToString(new File(filePathCD));
					String file4Str = FileUtils.readFileToString(new File(filePathCT));
					
					String file5Str = FileUtils.readFileToString(new File(filePathRisk+File.separator+fileRisks[i]));
					String file6Str = FileUtils.readFileToString(new File(filePathContent+File.separator+fileContent[j]));
					String file7Str = FileUtils.readFileToString(new File(filePathTP+File.separator+fileTP[k]));
					
					FileUtils.write(fileC, file1Str);
					FileUtils.write(fileC, file2Str, true);
					FileUtils.write(fileC, file3Str, true);
					FileUtils.write(fileC, file4Str, true);
					FileUtils.write(fileC, file5Str, true);
					FileUtils.write(fileC, file6Str, true);
					FileUtils.write(fileC, file7Str, true);

					
					XWPFDocument document= new XWPFDocument(); 
					//Write the Document in file system
					Scanner input = new Scanner(fileC);
					FileOutputStream out = new FileOutputStream(fileW,true);
					
		            while (input.hasNextLine()) {
		            	
		                XWPFParagraph paragraph = document.createParagraph();
						XWPFRun run=paragraph.createRun();
						run.setText(input.nextLine());
		                
		            }
		            document.write(out);
	            	out.close();
		            input.close();
				
				}	
			}
		}	

	}

	public static String[] getFileName(String folderPath) {
		File dir = new File(folderPath);
		File[] fileList = dir.listFiles();
		String[] fileName = new String[fileList.length];
		for (int i = 0; i < fileName.length; i++) {
			fileName[i] = fileList[i].getName();
		}
		Arrays.sort(fileName);
		return fileName;
	}

	public static String readFileContent(String filePath){
		String body="";

		StringBuffer stringBuffer = new StringBuffer();
		try{
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filePath), "UTF8"));

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				stringBuffer.append(sCurrentLine);
			}
			br.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		body = stringBuffer.toString();

		//System.out.println("##############File content:"+body);
		return body;
	}

	public static String[] getFileNameNoExtension(String folderPath) {
		File dir = new File(folderPath);
		File[] fileList = dir.listFiles();
		String[] fileName = new String[fileList.length];
		for (int i = 0; i < fileName.length; i++) {
			fileName[i] = FilenameUtils.removeExtension(fileList[i].getName());
		}
		Arrays.sort(fileName);
		return fileName;
	}

}



