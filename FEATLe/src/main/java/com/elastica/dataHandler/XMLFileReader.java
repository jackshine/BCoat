package com.elastica.dataHandler;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLFileReader {

	DocumentBuilderFactory factory = null;
	DocumentBuilder builder = null;
	Document document = null;
	Element element, subElement, nodeValueElement = null;
	NodeList nodelist, subNodelist = null;
	
	/**
	 * This function is used to 
	 * @param xmlFile
	 * @param environmentname
	 * @param url
	 * @return
	 */
	public String getXMLValues(String xmlFile, String environmentname, String url){
		String values = null;
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(xmlFile));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.getDocumentElement().normalize();
		element = document.getDocumentElement();
		nodelist = element.getElementsByTagName(environmentname);
		if(nodelist != null){
			for(int i=0; i < nodelist.getLength(); i++){
				if(nodelist.item(i).getNodeName().equalsIgnoreCase(environmentname)){
					subElement = (Element)nodelist.item(i);
					subNodelist = subElement.getElementsByTagName(url);
					if(subNodelist != null){
						nodeValueElement = (Element)subNodelist.item(0);
						values = nodeValueElement.getFirstChild().getNodeValue();
					}
					
				}
			}
		}
		return values;
	}
}
