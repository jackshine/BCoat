/**
 * 
 */
package com.elastica.beatle.fileHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author anuvrath
 *
 */
public class FileHandlingUtils {
	
	/**
	 * @param dataLocationConstant
	 * @return
	 */
	public static String getFileAbsolutePath(String dataLocationConstant){
		return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(dataLocationConstant); 
	}
	
	

	/**
	 * @param relativeFilePath
	 * @return
	 */
	public static Map<String,String> readPropertyFile(String relativeFilePath){
		
		Map<String, String> ApiMap = new HashMap<String, String>();
		try {
			//Reporter.log("Config file location path:"+getFileAbsolutePath(relativeFilePath), true);
			//File fXmlFile = new File(getFileAbsolutePath(relativeFilePath), File.separator);
			File fXmlFile = new java.io.File(FilenameUtils.separatorsToSystem(getFileAbsolutePath(relativeFilePath)).trim());
			Reader input = new FileReader(fXmlFile);
			StringWriter output = new StringWriter();
			try {
			  IOUtils.copy(input, output);
			} finally {
			  input.close();
			}
			String fileContents = output.toString();
			//Reporter.log("File contents:"+fileContents , true);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
		    dbf.setNamespaceAware(true);
		    dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		    dbf.setFeature("http://xml.org/sax/features/validation", false);
		    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		    dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		    DocumentBuilder dBuilder = dbf.newDocumentBuilder();
		    InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(fileContents));
			Document doc = dBuilder.parse(inputSource);	
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("entry");
			for (int temp = 0; temp < nList.getLength(); temp++) {			 
				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){
					ApiMap.put(nNode.getAttributes().getNamedItem("key").getNodeValue(), ((Element) nNode).getTextContent());
					nNode.getTextContent();
				}					
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ApiMap;
	}
	
	
//	
//	/**
//	 * @param relativeFilePath
//	 * @return
//	 */
//	public static Map<String,String> readPropertyFile(String relativeFilePath){
//		
//		Map<String, String> ApiMap = new HashMap<String, String>();
//		try {
//			Reporter.log("Config file location path:"+getFileAbsolutePath(relativeFilePath), true);
//			//File fXmlFile = new File(getFileAbsolutePath(relativeFilePath), File.separator);
//			File fXmlFile = new java.io.File(FilenameUtils.separatorsToSystem(getFileAbsolutePath(relativeFilePath)).trim());
//			
//			Reader input = new FileReader(fXmlFile);
//			StringWriter output = new StringWriter();
//			try {
//			  IOUtils.copy(input, output);
//			} finally {
//			  input.close();
//			}
//			String fileContents = output.toString();
//			
//			Reporter.log("File contents:"+fileContents , true);
//			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			
//			InputSource inputSource = new InputSource();
//			inputSource.setCharacterStream(new StringReader(fileContents));
//			
//			//Document doc = dBuilder.parse(new FileInputStream(fXmlFile));
//			Document doc = dBuilder.parse(inputSource);	
//			//Document doc = dBuilder.parse(fileContents.getBytes());	
//			//new ByteArrayInputStream( new String( "<doc><name>Steve</name></doc>" ).getBytes() )
//			doc.getDocumentElement().normalize();
//		 
//			NodeList nList = doc.getElementsByTagName("entry");
//			for (int temp = 0; temp < nList.getLength(); temp++) {			 
//				Node nNode = nList.item(temp);
//				if(nNode.getNodeType() == Node.ELEMENT_NODE){
//					ApiMap.put(nNode.getAttributes().getNamedItem("key").getNodeValue(), ((Element) nNode).getTextContent());
//					nNode.getTextContent();
//				}					
//			}			
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ApiMap;
//	}

	public static String readDataFromFile(String filePath) throws FileNotFoundException, IOException {
		return IOUtils.toString(new FileInputStream(new File(getFileAbsolutePath(filePath))),"UTF-8");
	}
	
	public static void writeDatasourceIntoFile(String dsID,String filePath) throws Exception{

		try {
			File file = new File(FileHandlingUtils.getFileAbsolutePath(filePath));

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(dsID);
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static String readDatasourceFromTheFile(String filePath) throws Exception{

		
		BufferedReader br = null;
		String dsId=null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FileHandlingUtils.getFileAbsolutePath(filePath)));

			while ((sCurrentLine = br.readLine()) != null) {

				System.out.println(sCurrentLine);
				dsId=sCurrentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return dsId;

	}
}
