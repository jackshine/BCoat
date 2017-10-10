/**
 * 
 */
package co.elastica.Constants;

/**
 * @author anuvrath
 *
 */
public class Constants {
	/*
	 * Mail configurations
	 */
	public static final String EMIL_HOST = "smtp.gmail.com";
	public static final String EMAIL_FROM = "zookeeper@elastica.co";	
	public static final String GMAIL_PASSWORD = "!QWE@asd#123";
	
	public static final String SANITY_REPORT = "Sanity";
	public static final String REGRESSION_REPORT = "Regression";
	
	/*
	 * Report CSS 
	 */
	public static final String HTML_TABLE_CSS = "html>body>table:nth-of-type(1)>tbody>tr";
	public static final String HTML_TABLE_ROWDATA_CSS = "html>body>table:nth-of-type(1)>tbody>tr:nth-of-type({iterator})>td";	
	public static final String BUILD_ID_CSS = "#buildHistory>div:eq(1)>table>tbody>tr:nth-of-type(2)>td>div:eq(0)>a";
	
	/*
	 * BE Regression links 
	 */
	public static final String EOE_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/EOESlaveLinks.xml";		
	public static final String QAVPC_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/qaVpcSlaveLinks.xml";
	public static final String PROD_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/PRODSlaveLinks.xml";	
	public static final String CEP_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/CEPSlaveLinks.xml";	
	
	/*
	 * Sanity Machine links
	 */
	public static final String PROD_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/ProdSanitySlaveLinks.xml";
	public static final String CEP_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/CEPSanitySlaveLinks.xml";
	public static final String EOE_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/EoESanitySlaveLinks.xml";
	public static final String QAVPC_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/qaVpcSanitySlaveLinks.xml";
	public static final String FRIENDS_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/FriendsSanitySlaveLinks.xml";	
	
	/*
	 * Mailing list
	 */
	public static final String SANITY_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/QAMailingList.xml";
	public static final String PRODUCTION_SANITY_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/productionSanityEmailList.xml";
	public static final String ENG_AND_QA_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/EngAndQA.xml";
	public static final String OWNER_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/ownersList.xml";
	public static final String GATEWAY_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/gatewayMailinglist.xml";
	public static final String FRIENDS_SANITY_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/friendsMailinglist.xml";
	public static final String AUDIT_MAILINGLIST = "/src/test/resources/co/elastica/EmailList/AuditEmailList.xml";
	/*
	 * FE Jobs Links
	 */
	public static final String EOE_FE_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/EoEFELinks.xml";
	public static final String PROD_FE_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/PRODFELinks.xml";
	public static final String CEP_FE_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/CEPFELinks.xml";
	public static final String QAVPC_FE_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/QAVPCFELinks.xml";
	
	/*
	 * FE Sanity Jobs Links
	 */
	public static final String EOE_FE_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/EoEFESanityLinks.xml";
	public static final String PROD_FE_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/PRODFESanityLinks.xml";
	public static final String CEP_FE_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/CEPFESanityLinks.xml";
	public static final String QAVPC_FE_SANITY_SLAVE_MACHINE_LINKS = "/src/test/resources/co/elastica/SlaveURLs/QAVPCFESanityLinks.xml";
	
	/*
	 * AWS Constants
	 */
	public static final String S3_BUCKET_NAME = "qa-reports-bucket";
	public static final String AWS_ACCESSKEY = "AKIAIM2U2YPTKIHYDJ5A";
	public static final String AWS_SECREATE = "xbJEAvIA88t+2oonbwbbhQVazW/Zk9k6SMKBw7V5";
	public static final String JENKINS_AUTHORIZATION_TOKEN = "Basic cWEtamVua2luczopWWg8XXZ3XTJwfSNZakxm";
	
	public static final String EOE_BASE_URI= "https://eoe.elastica-inc.com";
	public static final String CEP_BASE_URI= "https://app.eu.elastica.net";
	public static final String PROD_BASE_URI= "https://app.elastica.net";
	public static final String QAVPC_BASE_URI= "https://qa-vpc-ui.elastica-inc.com";
	public static final String FRIENDS_BASE_URI= "https://friends.elastica-inc.com";
	public static final String ENVIRONMENT_VERSION_URI = "/static/ng/platform/views/version.html";
	public static final String BEATLE_LASTSUCCESSFUL_URI = "/lastSuccessfulBuild/artifact/BEATLe/target/surefire-reports/emailable-report.html";
	public static final String FEATLE_LASTSUCCESSFUL_URI = "/lastSuccessfulBuild/artifact/target/surefire-reports/emailable-report.html";
	
	public static final String PROD_REGIONALGW_TESTS_LINKS = "/src/test/resources/co/elastica/SlaveURLs/RGWTestLinks.xml";
	public static final String EOE_WEEKLY_REGRESSION_LINKs = "/src/test/resources/co/elastica/SlaveURLs/EoEWeeklyRegression.xml";
	
}