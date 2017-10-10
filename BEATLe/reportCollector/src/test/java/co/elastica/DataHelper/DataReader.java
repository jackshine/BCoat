package co.elastica.DataHelper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import co.elastica.Constants.Constants;

/**
 * @author anuvrath
 *
 */
public class DataReader {

	/**
	 * @param environment
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public List<String> getSlaveURL(String environment) throws SAXException, IOException, ParserConfigurationException {
		List<String> slaveList = new ArrayList<String>();
		Reader input = new FileReader(new java.io.File(FilenameUtils.separatorsToSystem(getConfigFilePath(environment)).trim()));
		StringWriter output = new StringWriter();
		try {
			IOUtils.copy(input, output);
		} finally {
			input.close();
		}
		
		NodeList nList = getFileContents(output.toString());
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				String slaveLink = ((Element) nNode).getTextContent();
				slaveList.add(slaveLink);
			}
		}
		return slaveList;
	}

	/**
	 * @param environment
	 * @return
	 */
	private String getConfigFilePath(String environment) {
		if ("EOE".equalsIgnoreCase(environment) || "EOE_AUTO".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.EOE_SLAVE_MACHINE_LINKS);
		else if ("QAVPC".equalsIgnoreCase(environment) || "QAVPC_AUTO".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.QAVPC_SLAVE_MACHINE_LINKS);
		else if (("PROD".equalsIgnoreCase(environment) && !"Sanity".equalsIgnoreCase(environment)) || ("PROD_AUTO".equalsIgnoreCase(environment)))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PROD_SLAVE_MACHINE_LINKS);
		else if ("CEP".equalsIgnoreCase(environment) || "CEP_AUTO".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.CEP_SLAVE_MACHINE_LINKS);
		else if ("PRODSANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PROD_SANITY_SLAVE_MACHINE_LINKS);
		else if ("CEPSANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.CEP_SANITY_SLAVE_MACHINE_LINKS);
		else if ("QAVPCSANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.QAVPC_SANITY_SLAVE_MACHINE_LINKS);
		else if ("EOESANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.EOE_SANITY_SLAVE_MACHINE_LINKS);
		else if ("FRIENDSSANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.FRIENDS_SANITY_SLAVE_MACHINE_LINKS);		
		else if ("EOE_FE".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.EOE_FE_SLAVE_MACHINE_LINKS);
		else if ("CEP_FE".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.CEP_FE_SLAVE_MACHINE_LINKS);
		else if ("PROD_FE".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PROD_FE_SLAVE_MACHINE_LINKS);
		else if ("QAVPC_FE".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.QAVPC_FE_SLAVE_MACHINE_LINKS);
		else if ("EOE_FE_SANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.EOE_FE_SANITY_SLAVE_MACHINE_LINKS);
		else if ("CEP_FE_SANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.CEP_FE_SANITY_SLAVE_MACHINE_LINKS);
		else if ("PROD_FE_SANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PROD_FE_SANITY_SLAVE_MACHINE_LINKS);
		else if ("QAVPC_FE_SANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.QAVPC_FE_SANITY_SLAVE_MACHINE_LINKS);
		else if ("PRODGWSANITY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PROD_REGIONALGW_TESTS_LINKS);
		else if("EOE_AUDIT_WEEKLY".equalsIgnoreCase(environment))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.EOE_WEEKLY_REGRESSION_LINKs);
			return "UNKNOWN ENVIRONMENT PASSED, SO SLAVE LINK CONFIG NOT FOUND";
	}

	/**
	 * @param testEnvironment
	 * @return
	 */
	public String getEmailListFilePath(String testEnvironment) {
		if (testEnvironment.equals("ProdGWSanity"))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.GATEWAY_MAILINGLIST);
		else if (testEnvironment.contains("Sanity")) {
			if (testEnvironment.equals("ProdSanity"))
				return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.PRODUCTION_SANITY_MAILINGLIST);
			else if (testEnvironment.equals("FriendsSanity"))
				return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.FRIENDS_SANITY_MAILINGLIST);
			else
				return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.SANITY_MAILINGLIST);
		} else if(testEnvironment.contains("_AUDIT_WEEKLY"))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.AUDIT_MAILINGLIST);
		else if (testEnvironment.contains("_AUTO"))
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.OWNER_MAILINGLIST);
		else
			return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.ENG_AND_QA_MAILINGLIST);
	}

	/**
	 * @return
	 */
	public String getOwnersEmailListFilePath() {
		return System.getProperty("user.dir") + org.apache.commons.io.FilenameUtils.separatorsToSystem(Constants.OWNER_MAILINGLIST);
	}
	
	/**
	 * @param fileContents
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public NodeList getFileContents(String fileContents) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		dbf.setFeature("http://xml.org/sax/features/validation", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(new StringReader(fileContents));
		Document doc = dbf.newDocumentBuilder().parse(inputSource);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("entry");
		return nList;
	}

	/**
	 * @param envName
	 * @return
	 */
	public String getEnvironmentBuildVersionURL(String envName) {
		String versionUrl = "";
		if(envName.toLowerCase().contains("eoe")) {
			versionUrl = Constants.EOE_BASE_URI + Constants.ENVIRONMENT_VERSION_URI;
		} else if(envName.toLowerCase().contains("prod")) {
			versionUrl = Constants.PROD_BASE_URI  + Constants.ENVIRONMENT_VERSION_URI;
		} else if(envName.toLowerCase().contains("cep")) {
			versionUrl = Constants.CEP_BASE_URI + Constants.ENVIRONMENT_VERSION_URI; 
		} else if(envName.toLowerCase().contains("qavpc")|| envName.toLowerCase().contains("QAVPCSanity")) {
			versionUrl =  Constants.QAVPC_BASE_URI+ Constants.ENVIRONMENT_VERSION_URI;						  
		} else if(envName.toLowerCase().contains("friends")) {
			versionUrl = Constants.FRIENDS_BASE_URI + Constants.ENVIRONMENT_VERSION_URI; 
		} 
		return versionUrl;
	}
}