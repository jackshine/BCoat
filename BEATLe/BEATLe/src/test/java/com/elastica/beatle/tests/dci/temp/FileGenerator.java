package com.elastica.beatle.tests.dci.temp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class FileGenerator {

	private static void creatingWritingSavingFiles(String FileName) {
		try {


			Map<String, String> foreignPIIValues = new HashMap<>();				

			foreignPIIValues.put("PDT", "053.314.328-40 Identificação");
			foreignPIIValues.put("PDD", "anadrol");
			foreignPIIValues.put("CT", "custom_terms_only");
			foreignPIIValues.put("CD", "custom_dictionaries_only");
			foreignPIIValues.put("FileFormat", "");

			foreignPIIValues.put("Risk", File.separator+"Users"+File.separator+"eldorajan"
					+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"Risks");
			foreignPIIValues.put("Content", File.separator+"Users"+File.separator+"eldorajan"
					+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"Contents");
			foreignPIIValues.put("TrainingProfile", File.separator+"Users"+File.separator+"eldorajan"
					+File.separator+"Desktop"+File.separator+"Combinations"+File.separator+"TP");

			String[] rCount = getFileName(foreignPIIValues.get("Risk"));
			String[] cCount = getFileName(foreignPIIValues.get("Content")); 
			String[] tCount = getFileName(foreignPIIValues.get("TrainingProfile")); 


			if(StringUtils.contains(FileName, "Risk")||StringUtils.contains(FileName, "Content")||
					StringUtils.contains(FileName, "TrainingProfile")){
				for(int i=0;i<rCount.length;i++){
					for(int j=0;j<cCount.length;j++){
						for(int k=0;k<tCount.length;k++){

							String rContent = readFileContent(foreignPIIValues.get("Risk")+File.separator+rCount[i]);
							String cContent = readFileContent(foreignPIIValues.get("Content")+File.separator+cCount[j]);
							String tContent = readFileContent(foreignPIIValues.get("TrainingProfile")+File.separator+tCount[k]);



							FileName = FileName.replace("Risk", rCount[i].replace(".txt", "")).
									replace("Content", cCount[j].replace(".txt", "")).replace("TrainingProfile", tCount[k].replace(".txt", ""));

							System.out.println(FileName);

							File fileDir = new File(File.separator+"Users"+File.separator+"eldorajan"+File.separator+
									"Desktop"+File.separator+"Combinations"+File.separator+"Files"+File.separator+FileName);
							Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8"));

							FileName = FileName.substring(0, FileName.length()-4);
							for (String fileNames_Split: FileName.split("_")){
								String fileData = "";
								if(fileNames_Split.equalsIgnoreCase("Risk")){
									fileData = rContent;
								}else if(fileNames_Split.equalsIgnoreCase("Content")){
									fileData = cContent;
								}else if(fileNames_Split.equalsIgnoreCase("TrainingProfile")){
									fileData = tContent;
								}else{
									fileData = foreignPIIValues.get(fileNames_Split);	
								}

								if(fileData!=null){
									out.append(fileData).append("\r\n");
								}

							}

							out.flush();
							out.close();

						}

					}
				}
			}

			
		}






		catch(Exception e){
			//System.out.println(e.getMessage());
		}
	}

	/* arr[]  ---> Input Array ; data[] ---> Temporary array to store current combination start & end ---> Staring and Ending indexes in arr[] index  ---> Current index in data[] r ---> Size of a combination to be printed */
	static void combinationUtil(String arr[], int n, int r, int index,
			String data[], int i)
	{
		// Current combination is ready to be printed, print it
		if (index == r)
		{
			String FileName="";
			for (int j=0; j<r; j++)

				FileName = FileName+data[j]+"_";
			FileName = FileName.substring(0, FileName.length()-1);
			FileName = FileName+".txt";
			creatingWritingSavingFiles(FileName); 
			return;
		}

		// When no more elements are there to put in data[]
		if (i >= n)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		combinationUtil(arr, n, r, index+1, data, i+1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		combinationUtil(arr, n, r, index, data, i+1);
	}



	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	static void printCombination(String arr[], int n, int r)
	{
		// A temporary array to store all combination one by one

		String data[]=new String[r];
		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, n, r, 0, data, 0);
	}

	public static void combinationGenerator(String[] terms) {
		int n = terms.length;
		for(int i=1;i<terms.length+1;i++){
			printCombination(terms, n, i);
		}
	}

	/*Driver function to check for above function*/
	public static void main (String[] args) {
		String terms[] = {"TrainingProfile","PDT","PDD","CT","CD","Risk","Content","FileFormat"};



		combinationGenerator(terms);

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



