/**
 * 
 */
package co.elastica.EMailHandler;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import co.elastica.DataHelper.DataReader;

/**
 * @author anuvrath
 *
 */
public class EmailUtils {
	
	DataReader dataReader;
	
	/**
	 * 
	 */
	public EmailUtils() {
		super();
		dataReader = new DataReader();
	}

	/**
	 * @param filePath
	 * @param envName
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws AddressException
	 */
	public List<InternetAddress> getToMailingList(String filePath, String envName) throws SAXException, IOException, ParserConfigurationException, AddressException {

		List<InternetAddress> toList = new ArrayList<InternetAddress>();
		File fXmlFile = new java.io.File(FilenameUtils.separatorsToSystem(filePath));
		Reader input = new FileReader(fXmlFile);
		StringWriter output = new StringWriter();
		try {
			IOUtils.copy(input, output);
		} finally {
			input.close();
		}

		NodeList nList = dataReader.getFileContents(output.toString());
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getAttributes().getNamedItem("key").getNodeValue().equalsIgnoreCase("Subject")) {
				continue;
			} else if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String mailId = ((Element) nNode).getTextContent();
				if (envName.contains("_AUTO") && mailId.equals("vijay.mummaneni@elastica.co")) {
				} else
					toList.add(new InternetAddress(mailId));
			}
		}
		return toList;
	}
	
	/**
	 * @param environment
	 * @return
	 */
	public String getEmailSubject(String environment) {
		if ("EOE".equalsIgnoreCase(environment) || "EOE_AUTO".equalsIgnoreCase(environment))
			return "EoE CloudSoC BE Regression Report";
		if ("qavpc".equalsIgnoreCase(environment) || "QAVPC_AUTO".equalsIgnoreCase(environment))
			return "QA-VPC CloudSoC BE Regression Report";
		if (("Prod".equalsIgnoreCase(environment) && !"Sanity".equalsIgnoreCase(environment))
				|| ("PROD_AUTO".equalsIgnoreCase(environment)))
			return "PROD CloudSoC BE Regression Report";
		if ("CEP".equalsIgnoreCase(environment) || "CEP_AUTO".equalsIgnoreCase(environment))
			return "EU CloudSoC BE Regression Report";
		if ("ProdSanity".equalsIgnoreCase(environment))
			return "PROD BE Sanity Failures";
		if ("CEPSanity".equalsIgnoreCase(environment))
			return "EU BE Sanity Failures";
		if ("EoESanity".equalsIgnoreCase(environment))
			return "EoE BE Sanity Failures";
		if ("QAVPCSanity".equalsIgnoreCase(environment))
			return "QAVPC BE Sanity Failures";
		if ("FriendsSanity".equalsIgnoreCase(environment))
			return "Friends BE Sanity Failures";		
		if ("EOE_FE".equalsIgnoreCase(environment))
			return "EoE CloudSoC FE Regression Report";
		if ("QAVPC_FE".equalsIgnoreCase(environment))
			return "QAVPC CloudSoC FE Regression Report";
		if ("PROD_FE".equalsIgnoreCase(environment))
			return "PROD CloudSoC FE Regression Report";
		if ("CEP_FE".equalsIgnoreCase(environment))
			return "EU CloudSoC FE Regression Report";
		if ("EOE_FE_SANITY".equalsIgnoreCase(environment))
			return "EoE UI Sanity Failures";
		if ("QAVPC_FE_SANITY".equalsIgnoreCase(environment))
			return "QAVPC UI Sanity Failures";
		if ("PROD_FE_SANITY".equalsIgnoreCase(environment))
			return "PROD UI Sanity Failures";
		if ("CEP_FE_SANITY".equalsIgnoreCase(environment))
			return "EU UI Sanity Failures";
		if ("PRODGWSANITY".equalsIgnoreCase(environment))
			return "PROD Regional Cloud Config change Tests";
		if ("EOE_AUDIT_WEEKLY".equalsIgnoreCase(environment))
			return "EoE Weekly Audit Regression Tests";
		else
			return "UNKNOWN ENVIRONMENT PASSED";
	}
}
