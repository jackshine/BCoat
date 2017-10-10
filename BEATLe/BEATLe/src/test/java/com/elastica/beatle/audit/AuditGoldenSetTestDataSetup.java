/**
 * 
 */
package com.elastica.beatle.audit;

import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.ss.usermodel.Cell;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;

import com.elastica.beatle.RestClient.Client;
import com.elastica.beatle.RestClient.ClientUtil;

/**
 * @author Mallesh
 *
 */
public class AuditGoldenSetTestDataSetup extends AuditInitializeTests{

	private Client restClient;
	private String sourceID;
	protected AuditDSStatusDTO auditDSStatusDTO;
	protected ArrayList<AuditDSStatusDTO> inCompleteDsList=new ArrayList<AuditDSStatusDTO>();
	public ArrayList<String> goldenSetErrorList=new ArrayList<String>();
	public HashMap<String,String> serviceNameWithBrrMap=new HashMap<String,String>();
	public HashMap<String,String> sernameNameWithServiceIdMap=new HashMap<String,String>();
	public HashMap<String,String> serviceIDWithServiceNameMap=new HashMap<String,String>();


	protected  String datafilepath = null; 
	protected  String cwsSheetName=null;
	protected File inputFile=null;
	protected ArrayList<String> goldenSetDataList=new ArrayList<String>();
	protected Map<String, ArrayList<Cell>> knownGoodMap=new LinkedHashMap<String, ArrayList<Cell>>();
	org.apache.poi.ss.usermodel.Workbook wb=null;
	org.apache.poi.ss.usermodel.Sheet sheet=null;



	/**
	 * @param FireWallName
	 */
	public AuditGoldenSetTestDataSetup() throws Exception{
		restClient = new Client();


	}
	public AuditGoldenSetTestDataSetup(List<GoldenSetData> goldenSetDataList) throws Exception{
		restClient = new Client();

		testServiceBRRScores(goldenSetDataList);

	}


	//@BeforeClass
	public void  testServiceBRRScores(List<GoldenSetData> goldenSetDataList) throws Exception
	{
		Reporter.log("testServiceBRRScorese.......",true);
		HttpResponse serviceBrrScoreResp = AuditFunctions.getServicesBrrRate(restClient, null);
		Assert.assertEquals(serviceBrrScoreResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

		JSONObject brrScoreJsonObj = (JSONObject) new JSONObject(ClientUtil.getResponseBody(serviceBrrScoreResp)).getJSONArray("objects").get(0);				
		JSONObject scoreObj=brrScoreJsonObj.getJSONObject("service_brr_map");
		//scoreObj response is {"10997":{"is_knockout":false,"brr":65},"10998":{"is_knockout":false,"brr":5},
		//Reporter.log("scoreObj:***********"+scoreObj.length(),true);
		//Reporter.log("******************************",true);

		HashMap<String, String> map=new HashMap<String,String>(); //map to store serviceid and brr rate (6700 services)
		List<String> slist = new ArrayList<String>();
		Iterator iter = scoreObj.keys();
		while(iter.hasNext()){
			String key = (String)iter.next();
			
			//slist.add(key); //list to store serviceids
			map.put(key, new JSONObject(scoreObj.getString(key)).getString("brr"));
			
		}
		
		HashMap<String, String> map1=new HashMap<String,String>(); 
		
		for (GoldenSetData gsData: goldenSetDataList)
		{
			if(map.containsKey(gsData.getServiceID()))
			{
				slist.add(gsData.getServiceID()); //list to store serviceids
				map1.put(gsData.getServiceID(), map.get(gsData.getServiceID()));
			}
		}

		int thousand=1000;
		List<String> serviceIds=new ArrayList<String>();

		for (int i = 0; i <= (slist.size()/thousand); i++) {
			serviceIds.add(StringUtils.join(slist.subList(i * thousand, Math.min(((i + 1) * thousand), slist.size())),','));

		}


		//map: {10997=65, 10998=5...
		Reporter.log("servicebrr scores list size"+slist.size(),true);
		//Sending 1000 serviceids to the getService call to get servicename for the serivce id
		//preparing the string with 1000 sevices ata time 
		/*String commaString1= StringUtils.join(slist.subList(0, 1000), ',');
		String commaString2= StringUtils.join(slist.subList(1000, 2000), ',');
		String commaString3= StringUtils.join(slist.subList(2000, 3000), ',');
		String commaString4= StringUtils.join(slist.subList(3000, 4000), ',');
		String commaString5= StringUtils.join(slist.subList(4000, 5000), ',');
		String commaString6= StringUtils.join(slist.subList(5000, 6000), ',');
		String commaString7= StringUtils.join(slist.subList(6000, slist.size()), ',');*/

		//String[] strServices={commaString1,commaString2,commaString3,commaString4,commaString5,commaString6,commaString7};
		String[] strServices=serviceIds.toArray(new String[]{});

		List<HashMap<String,String>> listMaps=getServicesMap(strServices,map1);
		serviceNameWithBrrMap=listMaps.get(0);
		Reporter.log("serviceNameWithBrrMap:"+serviceNameWithBrrMap,true);
	
		sernameNameWithServiceIdMap=listMaps.get(1);
		Reporter.log("sernameNameWithServiceIdMap:"+sernameNameWithServiceIdMap,true);
		
		serviceIDWithServiceNameMap=listMaps.get(2);
		Reporter.log("serviceIDWithServiceNameMap:"+serviceIDWithServiceNameMap,true);
		
		


	}

	public HashMap<String,String> getSernameNameWithServiceIdMap ()
	{
		return sernameNameWithServiceIdMap;
	}

	public  List<HashMap<String,String>>  getServicesMap(String[] strServices,HashMap<String, String> map) throws Exception

	{

		HashMap<String,String> finalMap1=new HashMap<String,String>();
		HashMap<String,String> finalMap2=new HashMap<String,String>();
		HashMap<String,String> finalMap3=new HashMap<String,String>();

		for(String str: strServices)
		{
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
			queryParam.add(new BasicNameValuePair("limit", ""+str.length()));
			queryParam.add(new BasicNameValuePair("service_id__in", str));
			HttpResponse response  = AuditFunctions.getServiceDetails(restClient, queryParam);//calling get service api by passing 1000 services at a time

			String strResponse=ClientUtil.getResponseBody(response);
			//Reporter.log("getServicesMap respon: "+strResponse,true);
			JSONObject serviceJsonObject = new JSONObject(strResponse);
			HashMap<String,String> _2ndMap=new HashMap<String,String>();
			HashMap<String,String> _3rdMap=new HashMap<String,String>();
			HashMap<String,String> serviceIDwithServiceNameMap=new HashMap<String,String>();
			JSONArray servicesJsonArray = serviceJsonObject.getJSONArray("objects");
			int serviceArrayCount=servicesJsonArray.length();
			String serviceId=null;
			String serviceName=null;

			for(int i=0; i<serviceArrayCount; i++)
			{

				serviceId=((JSONObject)servicesJsonArray.get(i)).getString("service_id");
				serviceName=((JSONObject)servicesJsonArray.get(i)).getString("service_name");
				if(map.containsKey(serviceId)) //verifying the serviceid exist in the db 
				{
					_2ndMap.put(serviceName, map.get(serviceId)); //
					_3rdMap.put(serviceName,serviceId);
					serviceIDwithServiceNameMap.put(serviceId,serviceName);
				}

			}
			finalMap1.putAll(_2ndMap);
			finalMap2.putAll(_3rdMap);
			finalMap3.putAll(serviceIDwithServiceNameMap);


		}


		Reporter.log("serviceNameWithBrrMap .."+finalMap1,true);
		Reporter.log("serviceNameWithBrrMap size.."+finalMap1.size(),true);

		Reporter.log("finalMap1 .."+finalMap2,true);
		Reporter.log("finalMap1 size.."+finalMap2.size(),true);

		List<HashMap<String,String>> listMaps=new ArrayList<HashMap<String,String>>();

		listMaps.add(finalMap1);
		listMaps.add(finalMap2);
		listMaps.add(finalMap3);

		return listMaps;

	}

	public  HashMap<String,String> getServiceIdWithServiceName(String[] strServices,HashMap<String, String> map) throws Exception

	{

		HashMap<String,String> finalMap=new HashMap<String,String>();
		for(String str: strServices)
		{
			List<NameValuePair> queryParam = new ArrayList<NameValuePair>();				
			queryParam.add(new BasicNameValuePair("limit", ""+str.length()));
			queryParam.add(new BasicNameValuePair("service_id__in", str));
			HttpResponse response  = AuditFunctions.getServiceDetails(restClient, queryParam);

			JSONObject serviceJsonObject = new JSONObject(ClientUtil.getResponseBody(response));
			HashMap<String,String> _2ndMap=new HashMap<String,String>();
			JSONArray servicesJsonArray = serviceJsonObject.getJSONArray("objects");
			int serviceArrayCount=servicesJsonArray.length();
			String serviceId=null;
			String serviceName=null;

			for(int i=0; i<serviceArrayCount; i++)
			{

				serviceId=((JSONObject)servicesJsonArray.get(i)).getString("service_id");
				serviceName=((JSONObject)servicesJsonArray.get(i)).getString("service_name");
				if(map.containsKey(serviceId))
				{
					_2ndMap.put(serviceName,serviceId);
				}

			}
			finalMap.putAll(_2ndMap);


		}


		Reporter.log("sernameNameWithServiceIdMap .."+finalMap,true);
		Reporter.log("sernameNameWithServiceIdMap size.."+finalMap.size(),true);
		return finalMap;

	}


	public boolean checkServiceExistInMongoDB(String servicename)
	{
		return sernameNameWithServiceIdMap.containsKey(servicename);

	}

	public static HashMap<String, Integer> riskTypeMap(String servicename, int brr)
	{
		String riskType=null;
		ArrayList<String> highRiskServices=new ArrayList<String>();
		ArrayList<String> medRiskServices=new ArrayList<String>();
		ArrayList<String> lowRiskServices=new ArrayList<String>();
		HashMap<String, Integer> riskTypeMap=new HashMap<String, Integer>();

		if(brr<=49)
		{
			highRiskServices.add(servicename);

		}
		else if(brr>49 && brr <80){
			medRiskServices.add(servicename);
		}
		else{
			lowRiskServices.add(servicename);
		}
		riskTypeMap.put("high_risky", highRiskServices.size());
		riskTypeMap.put("medium_risky", medRiskServices.size());
		riskTypeMap.put("low_risky", lowRiskServices.size());

		return riskTypeMap;
	}
	public static void company_BRR(){
		String str=String.format("%.10g%n", (Math.log10(775)+1));
		System.out.println("str."+str);

		Double logUsersValue=Double.parseDouble(str);
		double l=logUsersValue.doubleValue();
		double serviceBrrRate=92;
		double finalValue=serviceBrrRate*l;

		System.out.println("finalValue."+String.format("%.10g%n",finalValue));
	}






	public long bytesOrLocOrSessionsSum(List<String> set)
	{

		Iterator<String> it=set.iterator();
		double total_sum=0;
		while(it.hasNext())
		{
			total_sum=total_sum+Double.parseDouble(it.next());
		}
		return new Double(total_sum).longValue();
	}

	public boolean isMostUsedServiceCheck(Set<String> serviceUsers, long totalUsersCount){

		int seriviceUserCount=serviceUsers.size();
		boolean flag=false;
		
		/**Integer.parseInt(String.format("%d", (int)(totalUsersCount*20/100)))  
		 * ex: totoluserscount: 13
		 *     13*20/100= 2.6
		 *     String.format("%d", (int)(totalUsersCount*20/100)  value retunrs 2
		 */
		
		if( seriviceUserCount > (Integer.parseInt(String.format("%d", (int)(totalUsersCount*20/100))))){
			flag=true;
		}

		return flag;

	}
	
	public boolean isMostUsedServiceCheckForMostUsedServices(Set<String> serviceUsers, long totalUsersCount){

		int seriviceUserCount=serviceUsers.size();
		boolean flag=false;
		
		/**Integer.parseInt(String.format("%d", (int)(totalUsersCount*20/100)))  
		 * ex: totoluserscount: 13
		 *     13*20/100= 2.6
		 *     String.format("%d", (int)(totalUsersCount*20/100)  value retunrs 2
		 */
		
		if( seriviceUserCount >= (Integer.parseInt(String.format("%d", (int)(totalUsersCount*20/100))))){
			flag=true;
		}

		return flag;

	}



	public AuditReport prepareAuditReportData(List<GoldenSetData> firewallLogGSList, String firewallType,String datasourceid) throws Exception
	{


		//********************************Total caluclation*******************************************
		long sessions_count=0;
		long services_count=0;
		long total_traffic=0;
		long locations_count=0;
		long total_uploads=0;
		long total_downloads=0;
		List<String> totalServices=new ArrayList<String>();
		Set<String> serviceCategory=new HashSet<String>();
		Set<String> finalUserSet=new HashSet<String>();
		List<String> finalLocatons=new ArrayList<String>();
		String t_Service_Category;
		AuditReport auditReport=new AuditReport();

		for(GoldenSetData gsData: firewallLogGSList)
		{
			int service_i=1;
			finalUserSet.addAll(gsData.getUsernameSet());
			if(gsData.getSessionForEachUserList()!=null){
				sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());}
			// services_count=services_count+service_i;
			totalServices.add(gsData.getServiceName());

			if(gsData.getTotalBytesList()!=null){
				total_traffic=total_traffic+ bytesOrLocOrSessionsSum(gsData.getTotalBytesList());}
			if(gsData.getUploadBytesList()!=null){
				total_uploads=total_uploads+bytesOrLocOrSessionsSum(gsData.getUploadBytesList());}
			if(gsData.getDownloadBytesList()!=null){
				total_downloads=total_downloads+bytesOrLocOrSessionsSum(gsData.getDownloadBytesList());}
			// finalLocatons.addAll(if(gsData.getUniqueLocationsList() !=null);
			if(gsData.getUniqueLocationsList() !=null){
				finalLocatons.addAll(gsData.getUniqueLocationsList());
			}
			//locations_count=locations_count+bytesOrLocOrSessionsSum(gsData.getUniqueLocationsList());

			Reporter.log("servicename:"+gsData.getServiceName(),true); 
			t_Service_Category=getServiceName(sernameNameWithServiceIdMap.get(gsData.getServiceName())).getString("service_category");
			Reporter.log("_serviceCategory:"+t_Service_Category,true); 
			serviceCategory.add(t_Service_Category);
			service_i++;


		}



		//preparing audit report details
		//preparing totals

		AuditReportTotals arTotals=new AuditReportTotals();
		arTotals.setUsers(finalUserSet.size());
		arTotals.setSessions(sessions_count);
		arTotals.setServices(totalServices.size());
		arTotals.setLocations(finalLocatons.size());
		Reporter.log("Totals serviceCategories:::"+serviceCategory,true);
		arTotals.setCategories(serviceCategory.size());
		arTotals.setTraffic(total_traffic);
		arTotals.setUploads(total_uploads);
		arTotals.setDownloads(total_downloads);

		Reporter.log("Audit Report Totals:"+arTotals,true);
		auditReport.setAuditReportTotals(arTotals);


		//*******************************Risky services*********************************************************
		ArrayList<CompanyBRRDto> serivceBrrUserCountDetail=getSerivceBrrUserCountDetails(firewallLogGSList);
		Reporter.log("serivceBrrUserCountDetail::"+serivceBrrUserCountDetail,true);
		ArrayList<String> highRiskServicesList=new ArrayList<String>();
		ArrayList<String> medRiskServicesList=new ArrayList<String>();
		ArrayList<String> lowRiskServicesList=new ArrayList<String>();
		ArrayList<String> riskyServices=new ArrayList<String>();


		long highRiskServices=0;
		long medRiskServices=0;
		long lowRiskServices=0;

		for(CompanyBRRDto obj: serivceBrrUserCountDetail)
		{
			int brrRate=Integer.parseInt(obj.getBrr());
			String service=obj.getService();
			Reporter.log("service:"+service+" brr Rate: "+brrRate,true);

			String serviceID=sernameNameWithServiceIdMap.get(service);
			String serviceIsResearched=AuditTestUtils.serviceIsResearch(serviceID);
			
			if("yes".equals(serviceIsResearched)){
			if(( brrRate<=49))
			{
				highRiskServicesList.add(obj.getService());
				riskyServices.add(obj.getService());

			}
			else if(brrRate>49 && brrRate <80){
				medRiskServicesList.add(service);
				riskyServices.add(obj.getService());
			}
			else{
				lowRiskServicesList.add(service);
			}
			}

		}

		highRiskServices=highRiskServicesList.size();
		medRiskServices=medRiskServicesList.size();
		lowRiskServices=lowRiskServicesList.size();

		AuditReportRiskyServices auditReportRiskyServices=new AuditReportRiskyServices();
		auditReportRiskyServices.setHigh_risky_services(highRiskServices);
		auditReportRiskyServices.setMed_risky_services(medRiskServices);
		auditReportRiskyServices.setTotal_services(highRiskServices+medRiskServices);


		sessions_count=0;
		services_count=0;
		total_traffic=0;
		locations_count=0;
		total_uploads=0;
		total_downloads=0;
		Set<String> riskyServiceCategory=new HashSet<String>();
		Set<String> riskyServiceFinalUserSet=new HashSet<String>();
		List<String> riskyServiceFinalLocatons=new ArrayList<String>();


		Reporter.log("RiskyServices:"+riskyServices,true);

		int riskyServices_i=0;
		int riskyServices_servicecount_i=1;
		for(GoldenSetData gsData: firewallLogGSList)
		{
			if(riskyServices_i!=riskyServices.size()) {
				if(gsData.getServiceName().equals(riskyServices.get(riskyServices_i))){

					riskyServiceFinalUserSet.addAll(gsData.getUsernameSet());

					if(gsData.getSessionForEachUserList()!=null){
						sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());}

					services_count=services_count+riskyServices_servicecount_i;

					if(gsData.getTotalBytesList()!=null){
						total_traffic=total_traffic+ bytesOrLocOrSessionsSum(gsData.getTotalBytesList());}
					if(gsData.getUploadBytesList()!=null){
						total_uploads=total_uploads+bytesOrLocOrSessionsSum(gsData.getUploadBytesList());}
					if(gsData.getDownloadBytesList()!=null){
						total_downloads=total_downloads+bytesOrLocOrSessionsSum(gsData.getDownloadBytesList());}

					if(gsData.getUniqueLocationsList() !=null){
						riskyServiceFinalLocatons.addAll(gsData.getUniqueLocationsList());
					}


					// locations_count=locations_count+bytesOrLocOrSessionsSum(gsData.getUniqueLocationsList());

					riskyServiceCategory.add(getServiceName(sernameNameWithServiceIdMap.get(gsData.getServiceName())).getString("service_category"));
					riskyServices_servicecount_i++;
					riskyServices_i++;
				}
			}

		}
		auditReportRiskyServices.setCategories(riskyServiceCategory.size());
		auditReportRiskyServices.setDownloads(total_downloads);
		auditReportRiskyServices.setLocations(riskyServiceFinalLocatons.size());
		auditReportRiskyServices.setSessions(sessions_count);
		auditReportRiskyServices.setTotal_traffic(total_traffic);
		auditReportRiskyServices.setUploads(total_uploads);
		auditReportRiskyServices.setUsers(riskyServiceFinalUserSet.size());



		// user count > 20% of total users
		Set<String> mostUsedService=new HashSet<String>();


		for(int i=0; i<firewallLogGSList.size(); i++)
		{
			GoldenSetData gsData=firewallLogGSList.get(i);

			if(riskyServices.contains(gsData.getServiceName()) )
			{

				if( isMostUsedServiceCheck(gsData.getUsernameSet(),auditReportRiskyServices.getUsers())){
					mostUsedService.add(gsData.getServiceName());}

			}

		}

		Reporter.log("Most Used Services.."+mostUsedService,true);
		auditReportRiskyServices.setMost_used_services(mostUsedService.size());
		Reporter.log("auditReportRiskyServices object::"+auditReportRiskyServices,true);

		auditReport.setAuditReportRiskyServices(auditReportRiskyServices);


		//*****************************most used services object preparation*******************************************************************
		AuditReportMostUsedServices auditReportMostUsedServices=new AuditReportMostUsedServices();

		//prepare the mostused services (service usercount >20% total usercount is a mostused services)
		List<String> mostUsedServices=new ArrayList<String>();

		for(GoldenSetData gsData: firewallLogGSList)
		{
			if(isMostUsedServiceCheckForMostUsedServices(gsData.getUsernameSet(),arTotals.getUsers())){
				mostUsedServices.add(gsData.getServiceName());
			}

		}



		ArrayList<String> mostUsedHighRiskServicesList=new ArrayList<String>();
		ArrayList<String> mostUsedRiskyServices=new ArrayList<String>();

		for(int k=0; k<serivceBrrUserCountDetail.size(); k++)
		{

			CompanyBRRDto obj=serivceBrrUserCountDetail.get(k);
			if(mostUsedServices.contains(obj.getService())){
				int brrRate=Integer.parseInt(obj.getBrr());
				String service=obj.getService();
				Reporter.log("service:"+service+" brr Rate: "+brrRate,true);
				
				String serviceID=sernameNameWithServiceIdMap.get(service);
				String serviceIsResearched=AuditTestUtils.serviceIsResearch(serviceID);
				
				if("yes".equals(serviceIsResearched)){

				if(( brrRate<=49))
				{
					Reporter.log("most used high Risky:"+obj.getService()+" "+brrRate,true);
					mostUsedHighRiskServicesList.add(obj.getService());
					mostUsedRiskyServices.add(obj.getService());

				}
				else if(brrRate>49 && brrRate <80){
					Reporter.log("most used medium Risky:"+obj.getService()+" "+brrRate,true);
					medRiskServicesList.add(service);
					mostUsedRiskyServices.add(obj.getService());
				}
				else{
					//lowRiskServicesList.add(service);
				}
				}

			}

		}
		auditReportMostUsedServices.setTotal_services(mostUsedServices.size());
		auditReportMostUsedServices.setHigh_risky_services(mostUsedHighRiskServicesList.size());
		auditReportMostUsedServices.setMed_risky_services(medRiskServicesList.size());
		auditReportMostUsedServices.setRiskyServices(mostUsedRiskyServices.size());

		sessions_count=0;
		services_count=0;
		total_traffic=0;
		locations_count=0;
		total_uploads=0;
		total_downloads=0;
		Set<String> mostUsedRiskyServiceCategory=new HashSet<String>();
		Set<String> mostUsedServicesFinalUserSet=new HashSet<String>();
		List<String> mostUsedServicesFinalLocations=new ArrayList<String>();

		Reporter.log("most Used Risky Services:"+mostUsedRiskyServices,true);
		int mostUsedRiskyServices_i=0;

		for(int j=0; j<firewallLogGSList.size(); j++){

			GoldenSetData gsData=firewallLogGSList.get(j);
			if(mostUsedServices.contains(gsData.getServiceName())){

				mostUsedServicesFinalUserSet.addAll(gsData.getUsernameSet());
				if(gsData.getSessionForEachUserList()!=null){
					sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());}

				if(gsData.getTotalBytesList()!=null){
					total_traffic=total_traffic+ bytesOrLocOrSessionsSum(gsData.getTotalBytesList());}
				if(gsData.getUploadBytesList()!=null){
					total_uploads=total_uploads+bytesOrLocOrSessionsSum(gsData.getUploadBytesList());}
				if(gsData.getDownloadBytesList()!=null){
					total_downloads=total_downloads+bytesOrLocOrSessionsSum(gsData.getDownloadBytesList());}

				if(gsData.getUniqueLocationsList()!=null){
					mostUsedServicesFinalLocations.addAll(gsData.getUniqueLocationsList());}
				//locations_count=locations_count+bytesOrLocOrSessionsSum(gsData.getUniqueLocationsList());

				mostUsedRiskyServiceCategory.add(getServiceName(sernameNameWithServiceIdMap.get(gsData.getServiceName())).getString("service_category"));

			}
		}


		auditReportMostUsedServices.setCategories(mostUsedRiskyServiceCategory.size());
		auditReportMostUsedServices.setDownloads(total_uploads);
		auditReportMostUsedServices.setLocations(mostUsedServicesFinalLocations.size());
		auditReportMostUsedServices.setSessions(sessions_count);
		auditReportMostUsedServices.setTotal_traffic(total_traffic);
		auditReportMostUsedServices.setUploads(total_uploads);
		auditReportMostUsedServices.setUsers(mostUsedServicesFinalUserSet.size());

		Reporter.log("Expected auditReportMostUsedServices *********:"+auditReportMostUsedServices,true);

		auditReport.setAuditMostUsedServices(auditReportMostUsedServices);

		//***********************Service categories object********************************************************************

		AuditReportServiceCategories auditReportServiceCategories= new AuditReportServiceCategories(); 

		//Find the categories of all services
		Map<String, String> serviceCat=new HashMap<String, String>();
		for(GoldenSetData gsData: firewallLogGSList)
		{
			t_Service_Category=getServiceName(sernameNameWithServiceIdMap.get(gsData.getServiceName())).getString("service_category");
			serviceCat.put(gsData.getServiceName(), t_Service_Category);
		}
		//service categories map will be like this.. [gmail=storage, google drive=storage]
		Reporter.log("original servicecategories map"+serviceCat,true);

		//risky on data

		Set<String> categoriesSet=new HashSet<String>();
		categoriesSet.addAll(serviceCat.values());

		Iterator<String> it=categoriesSet.iterator();
		Map<String,List<String>> newMap=new HashMap<String,List<String>>();
		while (it.hasNext())
		{
			String category=it.next();
			newMap.put(category, getKeysFromValue(serviceCat, category));

		}
		//converted service categories map will be like this.. [gmail=storage, google drive=storage]
		Reporter.log("converted servicecategories map"+newMap,true);

		Map<String, Long> category_risky_onDataCountMap=new HashMap<String,Long>();
		Map<String, Long> category_risky_onSessionCountMap=new HashMap<String,Long>();
		Map<String, String> category_risky_onDataMap=new HashMap<String,String>();
		Map<String, Long> category_risky_onUsesCount=new HashMap<String,Long>();

		int row=0;
		long catHishRiskyCount=0;
		long catMedRiskyCount=0;
		for (Map.Entry<String, List<String>> entry : newMap.entrySet()) {
			String key = entry.getKey();
			List<String> catServicesList = entry.getValue();
			category_risky_onDataCountMap.put(""+row, categoryTotalTraffic(firewallLogGSList,catServicesList));
			category_risky_onSessionCountMap.put(""+row, categorySessionCount(firewallLogGSList,catServicesList));
			category_risky_onDataMap.put(""+row, key);
			category_risky_onUsesCount.put(""+row, categoryUserCount(firewallLogGSList,catServicesList));
			catHishRiskyCount=catHishRiskyCount+categoryRiskyServices(firewallLogGSList,catServicesList,serivceBrrUserCountDetail,"high");
			catMedRiskyCount=catHishRiskyCount+categoryRiskyServices(firewallLogGSList,catServicesList,serivceBrrUserCountDetail,"med");

			row++;
		}

		auditReportServiceCategories.setRisky_Categories_onDataCount(category_risky_onDataCountMap);
		auditReportServiceCategories.setRisky_Categories_onData(category_risky_onDataMap);
		auditReportServiceCategories.setRisky_Categories_onSessionsCount(category_risky_onSessionCountMap);
		auditReportServiceCategories.setRisky_Categories_onUsersCount(category_risky_onUsesCount);
		auditReportServiceCategories.setHigh_risky_categories(catHishRiskyCount);
		auditReportServiceCategories.setMed_risky_categories(catMedRiskyCount);
		auditReportServiceCategories.setTotal_categories(newMap.size());

		Reporter.log("auditReportServiceCategories"+auditReportServiceCategories,true);
		auditReport.setAuditReportServiceCategories(auditReportServiceCategories);
		
		
		//**********************Service Details********************************

		
		//Verify serviceid
		//verify serviceurl
		//verify service_category
		//verify service_user_count
		
		//Verify serviceisnew or not
		//verify services_Most_used
		
		
		//verify service TotalUploads
		//verify service TotalDownloads
		//verify service total sessions
		//verify service locations_count
		//verify servicebrr
		
		List<AuditReportServiceDetails> auditReportServiceDetailsList=new ArrayList<AuditReportServiceDetails>();
		AuditReportServiceDetails  auditReportServiceDetails= null;
		for(GoldenSetData gsData: firewallLogGSList)
		{
			auditReportServiceDetails=new AuditReportServiceDetails();
			total_uploads=0;
			total_downloads=0;
			sessions_count=0;
			
			String serviceID=sernameNameWithServiceIdMap.get(gsData.getServiceName());
			auditReportServiceDetails.setServiceId(serviceID);
			ServiceObject serviceObject=AuditTestUtils.serviceDetails(serviceID);
			auditReportServiceDetails.setService_Url(serviceObject.getServiceUrl());
			auditReportServiceDetails.setCat1(serviceObject.getServiceCategory());
			auditReportServiceDetails.setUsers_Count(gsData.getUsernameSet().size());
			if(gsData.getUploadBytesList()!=null){
				total_uploads=total_uploads+bytesOrLocOrSessionsSum(gsData.getUploadBytesList());
				auditReportServiceDetails.setUploads(total_uploads);	}
			if(gsData.getDownloadBytesList()!=null){
				total_downloads=total_downloads+bytesOrLocOrSessionsSum(gsData.getDownloadBytesList());
				auditReportServiceDetails.setDownloads(total_downloads);	}

			if(gsData.getSessionForEachUserList()!=null){
				sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());
				auditReportServiceDetails.setSessions(sessions_count);}
			auditReportServiceDetails.setService_Brr(serviceNameWithBrrMap.get(gsData.getServiceName()));
			auditReportServiceDetailsList.add(auditReportServiceDetails);

		}
		auditReport.setAuditReportServiceDetailsList(auditReportServiceDetailsList);
		Reporter.log("auditReportServiceDetails Object::"+auditReportServiceDetailsList,true);
	
		
		
		 //***********************new services*************************************
		/*
		
		//serviceIDWithServiceNameMap
		Map<String, String> tenantServicesMap=AuditTestUtils.tenantServicesMap(datasourceid);
		Map<String, String> tenantServicesWithUtcTimeMap=new HashMap<String,String>();
		Properties firewallAuditReportDateRangeProps=null;
		firewallAuditReportDateRangeProps=new Properties();
        firewallAuditReportDateRangeProps.load(new FileInputStream(FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.FIREWALL_LOG_REPORT_DATA_DATERANGE)));
        
     
		
		Set<String> keys = tenantServicesMap.keySet();
        for(String key: keys){
        	if(serviceIDWithServiceNameMap.containsKey(key))
        		{
        		tenantServicesWithUtcTimeMap.put(serviceIDWithServiceNameMap.get(key), convertEpochTimeToUtc(tenantServicesMap.get(key)));
        		    
        			}
        }
        Reporter.log("final Tenant Services Map:"+tenantServicesWithUtcTimeMap,true);
		
        List<String> newServices=new ArrayList<String>();
        List<String> dsServicesList=new ArrayList<String>();
		
        for(GoldenSetData gsData: firewallLogGSList)
		{
        	String service=gsData.getServiceName();
        	dsServicesList.add(service);
        	if(tenantServicesWithUtcTimeMap.containsKey(service) && 
        			checkServiceDateinReportRange(tenantServicesWithUtcTimeMap.get(service),firewallAuditReportDateRangeProps,firewallType) )
        	{
        		newServices.add(service);
        		
        	}
        	
		}
        
        Reporter.log("expected Services.."+firewallType+"::"+dsServicesList,true);
        
         Reporter.log("newservices list.."+firewallType+"::"+newServices,true);
      	
*/
		//***********************Service hosting locations object********************************************************************
	  //**********************Top Active Users******************************
		//**********************UserDetails	      

		return auditReport;

	}


	public boolean checkServiceDateinReportRange(String serviceDate,Properties firewallAuditReportDateRangeProps,String firewallType) throws Exception
	{
		boolean flag=false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		Date serDate = sdf.parse(serviceDate);
		
		String[] reportDate=firewallAuditReportDateRangeProps.getProperty(firewallType).split("~");
		//String reportStartDate=reportDate[0];
		Date reportStartDate=sdf.parse(reportDate[0]);
		Date reportEndDate=sdf.parse(reportDate[1]);
		 Reporter.log("reportStartDate:"+reportStartDate+"reportEndDate:"+reportEndDate,true);
	      	
		flag=isWithinRange(serDate,reportStartDate,reportEndDate);
		
		return flag;
		
	}
	
	boolean isWithinRange(Date testDate,Date reportStartDate,Date reportEndDate) {
	    return testDate.getTime() >= reportStartDate.getTime() &&
	             testDate.getTime() <= reportEndDate.getTime();
	}

	//caluclate traffice for each category risky of data count
	public long categoryTotalTraffic(List<GoldenSetData> firewallLogGSList,List<String> categoryServicesList)
	{
		long total_traffic=0;
		for(int i=0; i<firewallLogGSList.size(); i++)
		{
			GoldenSetData gsData=firewallLogGSList.get(i);
			if(categoryServicesList.contains(gsData.getServiceName())){
				total_traffic=total_traffic+ bytesOrLocOrSessionsSum(gsData.getTotalBytesList());
			}
		}
		return total_traffic;
	}

	//caluclate session count for each category risky of session count
	public long categorySessionCount(List<GoldenSetData> firewallLogGSList,List<String> categoryServicesList)
	{
		long sessions_count=0;
		for(int i=0; i<firewallLogGSList.size(); i++)
		{
			GoldenSetData gsData=firewallLogGSList.get(i);
			if(categoryServicesList.contains(gsData.getServiceName())){
				if(gsData.getSessionForEachUserList()!=null){
					sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());}
				// sessions_count=sessions_count+bytesOrLocOrSessionsSum(gsData.getSessionForEachUserList());

			}
		}
		return sessions_count;
	}
	//caluclate user count for each category risky of user count
	public long categoryUserCount(List<GoldenSetData> firewallLogGSList,List<String> categoryServicesList)
	{
		Set<String> mostUsedServicesFinalUserSet=new HashSet<String>();
		for(int i=0; i<firewallLogGSList.size(); i++)
		{
			GoldenSetData gsData=firewallLogGSList.get(i);
			if(categoryServicesList.contains(gsData.getServiceName())){
				mostUsedServicesFinalUserSet.addAll(gsData.getUsernameSet());

			}
		}

		return mostUsedServicesFinalUserSet.size();
	}		
	//caluclate category high risky services
	public long categoryRiskyServices(List<GoldenSetData> firewallLogGSList,List<String> categoryServicesList, ArrayList<CompanyBRRDto> serivceBrrUserCountDetail,String riskType)
	{
		ArrayList<String> catHighRiskServicesList=new ArrayList<String>();
		ArrayList<String> catMedRiskyServicesList=new ArrayList<String>();

		for(int k=0; k<serivceBrrUserCountDetail.size(); k++)
		{

			CompanyBRRDto obj=serivceBrrUserCountDetail.get(k);
			if(categoryServicesList.contains(obj.getService())){
				int brrRate=Integer.parseInt(obj.getBrr());
				String service=obj.getService();
				Reporter.log("service:"+service+" brr Rate: "+brrRate,true);

				if(( brrRate<=49))
				{
					Reporter.log("most used high Risky:"+obj.getService()+" "+brrRate,true);
					catHighRiskServicesList.add(obj.getService());

				}
				else if(brrRate>49 && brrRate <80){
					Reporter.log("most used medium Risky:"+obj.getService()+" "+brrRate,true);
					catMedRiskyServicesList.add(obj.getService());
				}
			}

		}
		if("high".equals(riskType))
			return catHighRiskServicesList.size();
		else
			return catMedRiskyServicesList.size();

	}	

	public static List<String> getKeysFromValue(Map<String, String> hm, Object value){
		List <String>list = new ArrayList<String>();
		for(String o:hm.keySet()){
			if(hm.get(o).equals(value)) {
				list.add(o);
			}
		}
		return list;
	}
	public SortedSet<String> getExpectedAnonyUsers(List<GoldenSetData> firewallLogGSList) throws Exception
	{
		SortedSet<String> anonyUsersSet=new TreeSet<String>();
		for(GoldenSetData goldenSetData: firewallLogGSList)
		{
			if (sernameNameWithServiceIdMap.containsKey(goldenSetData.getServiceName()))
			{
			
				anonyUsersSet.addAll(goldenSetData.getUsernameSet());
			}
		}
		return anonyUsersSet;
	}


	public ArrayList<CompanyBRRDto> getSerivceBrrUserCountDetails( List<GoldenSetData> firewallLogGSList) throws Exception
	{

		ArrayList<CompanyBRRDto> companyBrrList= new ArrayList<CompanyBRRDto>();
		CompanyBRRDto brrObj;
		String serviceName;
		String userCount;
		String str=null;
		Double logUsersValue=null;

		for(GoldenSetData goldenSetData: firewallLogGSList)
		{
			brrObj=new CompanyBRRDto();
			if (sernameNameWithServiceIdMap.containsKey(goldenSetData.getServiceName()))
			{
				serviceName=goldenSetData.getServiceName();
				brrObj.setService(serviceName);
				brrObj.setServiceId(sernameNameWithServiceIdMap.get(serviceName));
				brrObj.setBrr(serviceNameWithBrrMap.get(serviceName));
				brrObj.setUsers_count(String.valueOf(goldenSetData.getUsernameSet().size()));

				brrObj.setUsernameSet(goldenSetData.getUsernameSet());
				brrObj.setSessionForEachUserList(goldenSetData.getSessionForEachUserList());
				brrObj.setDownloadBytesList(goldenSetData.getDownloadBytesList());
				brrObj.setUploadBytesList(goldenSetData.getUploadBytesList());
				brrObj.setUniqueLocationsList(goldenSetData.getUniqueLocationsList());

               
				
				String serviceID=sernameNameWithServiceIdMap.get(goldenSetData.getServiceName());
				String serviceIsResearched=AuditTestUtils.serviceIsResearch(serviceID);
				
				if("yes".equals(serviceIsResearched)){
				
				str=String.format("%.10g%n", (Math.log10(Double.parseDouble(brrObj.getUsers_count())+1)));
				logUsersValue=Double.parseDouble(str);
				double l1=logUsersValue.doubleValue();
				brrObj.setLog_users_count(l1);

				double finalValue1=Double.parseDouble(brrObj.getBrr())*brrObj.getLog_users_count();
				str=String.format("%.10g%n",finalValue1);
				logUsersValue=Double.parseDouble(str);
				brrObj.setBrr_log_users_count(logUsersValue.doubleValue());
				}
				companyBrrList.add(brrObj);
			}
		}

		return companyBrrList;
	}



	public AuditSummary populateAuditSummary(List<GoldenSetData> firewallLogGSList) throws Exception
	{
		AuditSummary auditSummary=new AuditSummary();
		HashMap<String, String> serviceBrrRateMap=new HashMap<String, String>();

		AuditSummaryTopRiskyServices auditSummaryTopRiskyServices=null;
		AuditSummaryUserObject auditSummaryUserObject=null;
		List<AuditSummaryUserObject> auditSummaryUserObjectList=new ArrayList<AuditSummaryUserObject>();

		List<AuditSummaryTopRiskyServices> auditSummaryTopRiskyServicesList=new ArrayList<AuditSummaryTopRiskyServices>();
		ArrayList<String> highRiskServicesList=new ArrayList<String>();
		ArrayList<String> medRiskServicesList=new ArrayList<String>();
		ArrayList<String> lowRiskServicesList=new ArrayList<String>();

		AuditSummaryServicesTabObj auditSummaryServicesTabObj= null;
		List<AuditSummaryServicesTabObj> auditSummaryServicesTabObjList=new ArrayList<AuditSummaryServicesTabObj>();




		//Audit Score caluclation
		String expected_audit_score;
		ArrayList<CompanyBRRDto> companyBrrList= getSerivceBrrUserCountDetails(firewallLogGSList);

		double log_users_count_sum=0.0;
		double brr_log_users_count_sum=0.0;
		for(CompanyBRRDto obj: companyBrrList)
		{
			auditSummaryTopRiskyServices= new AuditSummaryTopRiskyServices();
			auditSummaryServicesTabObj= new AuditSummaryServicesTabObj();
			log_users_count_sum=log_users_count_sum+obj.getLog_users_count();
			brr_log_users_count_sum=brr_log_users_count_sum+obj.getBrr_log_users_count();


			int brrRate=Integer.parseInt(obj.getBrr());
			String service=obj.getService();
			Reporter.log("service:"+service+" brr Rate: "+brrRate,true);
			
			//verify service researched or not
			
			String serviceID=sernameNameWithServiceIdMap.get(obj.getService());
			String serviceIsResearched=AuditTestUtils.serviceIsResearch(serviceID);
			
			if("yes".equals(serviceIsResearched)){
				if(( brrRate<=49))
				{
					auditSummaryTopRiskyServices.setServicename(obj.getService());
					auditSummaryTopRiskyServices.setService_brr(obj.getBrr());
					auditSummaryTopRiskyServices.setService_user_count(obj.getUsernameSet().size());
					serviceBrrRateMap.put(obj.getService(), obj.getBrr());



					/*Iterator<String> usernameIt=obj.getUsernameSet().iterator();
				while(usernameIt.hasNext())
				{
					auditSummaryUserObject=new AuditSummaryUserObject();
					auditSummaryUserObject.setServiceUser(usernameIt.next());
					auditSummaryUserObjectList.add(auditSummaryUserObject);
				}
				auditSummaryTopRiskyServices.setSummaryUserObjList(auditSummaryUserObjectList);*/
					auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);
					//user session totaltraffic

					highRiskServicesList.add(service);


				}
				else if(brrRate>49 && brrRate <80){
					auditSummaryTopRiskyServices.setServicename(obj.getService());
					auditSummaryTopRiskyServices.setService_brr(obj.getBrr());
					auditSummaryTopRiskyServices.setService_user_count(obj.getUsernameSet().size());
					

					/*Iterator<String> usernameIt=obj.getUsernameSet().iterator();
				while(usernameIt.hasNext())
				{
					auditSummaryUserObject=new AuditSummaryUserObject();
					auditSummaryUserObject.setServiceUser(usernameIt.next());
					auditSummaryUserObjectList.add(auditSummaryUserObject);
				}
				auditSummaryTopRiskyServices.setSummaryUserObjList(auditSummaryUserObjectList);*/
					auditSummaryTopRiskyServicesList.add(auditSummaryTopRiskyServices);


					medRiskServicesList.add(service);

				}
				else{
					lowRiskServicesList.add(service);
					
				}
            }

			auditSummaryServicesTabObj.setServiceName(obj.getService());
			auditSummaryServicesTabObj.setServiceRating(obj.getBrr());
			auditSummaryServicesTabObj.setServiceSessions(obj.getSessionForEachUserList().size());
			auditSummaryServicesTabObj.setServiceTotalDownloads(obj.getDownloadBytesList().size());
			auditSummaryServicesTabObj.setServiceTotalUploads(obj.getUploadBytesList().size());
			auditSummaryServicesTabObj.setServicesUsersCount(obj.getUsernameSet().size());
			auditSummaryServicesTabObj.setServiceDestinations(obj.getUniqueLocationsList().size());
			auditSummaryServicesTabObjList.add(auditSummaryServicesTabObj);


		}
		System.out.println("log_users_count_sum"+log_users_count_sum);
		System.out.println("brr_log_users_count_sum"+brr_log_users_count_sum);

		expected_audit_score=String.format("%.13g%n",brr_log_users_count_sum/log_users_count_sum);
		//Reporter.log("Audit company score"+expected_audit_score,true);

		auditSummary.setAuditScore(expected_audit_score);
		auditSummary.setSummaryTopRiskyServicesList(auditSummaryTopRiskyServicesList);
		auditSummary.setHigh_risky_services_count(highRiskServicesList.size());
		auditSummary.setMed_risky_services_count(medRiskServicesList.size());
		auditSummary.setLow_riksy_services_count(lowRiskServicesList.size());


		// service_count, userscount,destinations count
		List<String> totalServices = new ArrayList<String>();
		Set<String> finalUserSet = new HashSet<String>();
		//List<String> finalLocatons = new ArrayList<String>();
		Set<String> finalLocations=new HashSet<String>();

		for (GoldenSetData gsData : firewallLogGSList) {
			finalUserSet.addAll(gsData.getUsernameSet());
			totalServices.add(gsData.getServiceName());
			if (gsData.getUniqueLocationsList() != null) {
				finalLocations.addAll(gsData.getUniqueLocationsList());
				//finalLocatons.addAll(gsData.getUniqueLocationsList());
			}

		}
		auditSummary.setTotalAuditServicesList(totalServices);
		auditSummary.setSaas_services_count(totalServices.size());
		auditSummary.setUsers_count(finalUserSet.size());
		auditSummary.setDestination_count(finalLocations.size());

		auditSummary.setSummaryServicesTabObjList(auditSummaryServicesTabObjList);
		auditSummary.setServiceBrrMap(serviceBrrRateMap);
		Reporter.log("auditSummary.."+auditSummary,true);

		return auditSummary;
	}



	public  ArrayList<String> prepareGSData(List<GoldenSetData> firewallLogGSList, String datasourceId,JSONObject summaryObject,String firewallType,ArrayList<String> goldenSetErrorList) throws Exception
	{

		String expected_audit_score;
		String expected_audit_saasservices;
		String expected_audit_users;
		String expected_audit_destinations;

		List<String> servicesList = new ArrayList<String>();
		/* Map<String, ArrayList<Cell>>  map=readExcelFileDataAndPrepareGoldenSetData(goldenSetDataList);

		for(Cell c:map.get("Service Name")){
			servicesList.add(c.getStringCellValue());
			getServiceName(serviceId).getString("service_name");  service_category
		}*/

		for(GoldenSetData gsData: firewallLogGSList)
		{
			servicesList.add(gsData.getServiceName());

		}

		//actual list
		String label="top_risky_services";
		int topRiskyServicesArrayLength=0;
		int topUsedServicesArrayLength=0;
		String serviceId=null;
		String serName=null;
		Set<String> setServiceNames=new HashSet<String>();


		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topRiskyServicesArray = summaryObject.getJSONArray(label);
			topRiskyServicesArrayLength =topRiskyServicesArray.length();


			for(int i=0; i<topRiskyServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topRiskyServicesArray.get(i)).getString("service_id");
				serName=getServiceName(serviceId).getString("service_name");
				setServiceNames.add(serName);
			}
		}

		label="top_used_services";

		if (summaryObject.has(label) && !summaryObject.isNull(label)) {
			JSONArray topUsedServicesArray = summaryObject.getJSONArray(label);
			topUsedServicesArrayLength =topUsedServicesArray.length();

			for(int i=0; i<topUsedServicesArrayLength; i++)
			{
				serviceId=((JSONObject)topUsedServicesArray.get(i)).getString("service_id");
				serName=getServiceName(serviceId).getString("service_name");
				setServiceNames.add(serName);

			}

		}

		//actual map preparation
		HashMap<String,String> actualServicesMap=new HashMap<String,String>();
		Iterator it=setServiceNames.iterator();
		String ser_Name;
		while(it.hasNext())
		{
			ser_Name=(String)it.next();
			if(sernameNameWithServiceIdMap.containsKey(ser_Name))
			{
				actualServicesMap.put(ser_Name, sernameNameWithServiceIdMap.get(ser_Name));
			}
		}
		Reporter.log("Expected Services list"+servicesList,true);
		Reporter.log("Actual Services list"+setServiceNames,true);

		//sevice exist in db or not
		for(String service:servicesList){
			if(!sernameNameWithServiceIdMap.containsKey(service))
			{
				goldenSetErrorList.add("service:"+service+" not exist in the system");
			}
			else if(!actualServicesMap.containsKey(service))
			{
				goldenSetErrorList.add("service:"+service+" not exist in the actual api list");
			}
			else{
				//compare brr
			}
		}


		Reporter.log("servicesList..."+servicesList,true);

		String allowed_true="true";
		String blocked_false="false";
		//String ds_id="5635a6ddbf83126a28f271e4";
		String ds_id=datasourceId;

		String users_tab="services";
		String destinations_tab="locations";


		expected_audit_saasservices=""+servicesList.size();


		Map<String,String> serviceUsersMap=new HashMap<String,String>();

		//get UsersCount by service
		List<NameValuePair> usersCountRequestHeader = new ArrayList<NameValuePair>();
		usersCountRequestHeader.add(new BasicNameValuePair("allowed", allowed_true));
		usersCountRequestHeader.add(new BasicNameValuePair("blocked", blocked_false));
		usersCountRequestHeader.add(new BasicNameValuePair("ds_id", ds_id));
		usersCountRequestHeader.add(new BasicNameValuePair("tab", users_tab));
		HttpResponse serviceResp = getUsersCountByService(restClient, requestCookieHeader(),usersCountRequestHeader);
		Assert.assertEquals(serviceResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		String strServicesMap=ClientUtil.getResponseBody(serviceResp);
		Reporter.log("ServicesInfo:"+strServicesMap,true);

		JSONObject usersByServiceJson = (JSONObject) new JSONObject(strServicesMap);				
		JSONObject usersObj=usersByServiceJson.getJSONObject("services");
		Iterator iter = usersObj.keys();
		while(iter.hasNext()){
			String key = (String)iter.next();
			serviceUsersMap.put(key, usersObj.getString(key));
		}
		Reporter.log("serviceUsersMap"+serviceUsersMap,true);

		expected_audit_users=usersByServiceJson.getString("total_users");
		Reporter.log("Total users"+expected_audit_users,true);

		//expected_audit_users validation and checking users is in the range of +/-10 % range***********************************
		label="total_users";
		String actual_audit_users=summaryObject.getString(label);
		if(Integer.parseInt(actual_audit_users)==Integer.parseInt(expected_audit_users)){
			Assert.assertEquals(actual_audit_users, expected_audit_users,firewallType+" total_users should be ");//total_users

		}else{
			validateSummaryInfoNew(expected_audit_users,actual_audit_users,firewallType,label,goldenSetErrorList);
		}


		JSONObject locationObj=usersByServiceJson.getJSONObject("locations");
		expected_audit_destinations=""+locationObj.length();
		Reporter.log("expected_gs_destinations"+expected_audit_destinations,true);
		Reporter.log("expected_gs_saaas_services"+expected_audit_saasservices,true);

		//expected_audit_destinations validation and checking destinations/locations is in the range of +/-10% range********************
		label="total_destinations";
		String actual_audit_destinations=summaryObject.getString(label);
		if(Integer.parseInt(actual_audit_destinations)==Integer.parseInt(expected_audit_destinations)){
			Assert.assertEquals(actual_audit_destinations, expected_audit_destinations,firewallType+" TotalDestinations should be ");//TotalDestinations

		}else{
			validateSummaryInfoNew(expected_audit_destinations,actual_audit_destinations,firewallType,label,goldenSetErrorList);
		}

		//expected_audit_saasservices validation and checking saasservices is in the range of +/-10%************************
		label="total_services";
		String actual_audit_saasservices=summaryObject.getString(label);
		if(Integer.parseInt(actual_audit_saasservices)==Integer.parseInt(expected_audit_saasservices)){
			Assert.assertEquals(actual_audit_saasservices, expected_audit_saasservices,firewallType+" total_services should be ");//total_services
		}else{
			validateSummaryInfoNew(expected_audit_saasservices,actual_audit_saasservices,firewallType,label,goldenSetErrorList);
		}


		//get Users info
		List<NameValuePair> usersRequestHeader = new ArrayList<NameValuePair>();
		usersRequestHeader.add(new BasicNameValuePair("allowed", allowed_true));
		usersRequestHeader.add(new BasicNameValuePair("blocked", blocked_false));
		usersRequestHeader.add(new BasicNameValuePair("ds_id", ds_id));
		HttpResponse usersHttpResp = getUsers(restClient, requestCookieHeader(),usersRequestHeader);
		Assert.assertEquals(usersHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		Reporter.log("usersInfo:"+ClientUtil.getResponseBody(usersHttpResp),true);

		//get users_count by Destinations/Locations 
		List<NameValuePair> locationsHeader = new ArrayList<NameValuePair>();
		locationsHeader.add(new BasicNameValuePair("allowed", allowed_true));
		locationsHeader.add(new BasicNameValuePair("blocked", blocked_false));
		locationsHeader.add(new BasicNameValuePair("ds_id", ds_id));
		locationsHeader.add(new BasicNameValuePair("tab", destinations_tab));
		HttpResponse destinationsHttpResp = getUsersCountByService(restClient, requestCookieHeader(),locationsHeader);
		Assert.assertEquals(destinationsHttpResp.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
		Reporter.log("Destinations:"+ClientUtil.getResponseBody(destinationsHttpResp),true);

		//get the serviceid for the input service names

		//company brr caluclations
		ArrayList<CompanyBRRDto> companyBrrList= new ArrayList<CompanyBRRDto>();
		String str=null;
		Double logUsersValue=null;

		CompanyBRRDto brrObj=null;

		for(String serviceName: servicesList)
		{
			brrObj=new CompanyBRRDto();
			if (sernameNameWithServiceIdMap.containsKey(serviceName))
			{
				brrObj.setService(serviceName);
				brrObj.setServiceId(sernameNameWithServiceIdMap.get(serviceName));
				brrObj.setBrr(serviceNameWithBrrMap.get(serviceName));
				brrObj.setUsers_count(serviceUsersMap.get(sernameNameWithServiceIdMap.get(serviceName)));


				str=String.format("%.10g%n", (Math.log10(Double.parseDouble(brrObj.getUsers_count())+1)));
				logUsersValue=Double.parseDouble(str);
				double l1=logUsersValue.doubleValue();
				brrObj.setLog_users_count(l1);

				double finalValue1=Double.parseDouble(brrObj.getBrr())*brrObj.getLog_users_count();
				str=String.format("%.10g%n",finalValue1);
				logUsersValue=Double.parseDouble(str);
				brrObj.setBrr_log_users_count(logUsersValue.doubleValue());
				companyBrrList.add(brrObj);
			}
		}

		double log_users_count_sum=0.0;
		double brr_log_users_count_sum=0.0;
		for(CompanyBRRDto obj: companyBrrList)
		{
			log_users_count_sum=log_users_count_sum+obj.getLog_users_count();
			brr_log_users_count_sum=brr_log_users_count_sum+obj.getBrr_log_users_count();

		}
		System.out.println("log_users_count_sum"+log_users_count_sum);
		System.out.println("brr_log_users_count_sum"+brr_log_users_count_sum);

		expected_audit_score=String.format("%.13g%n",brr_log_users_count_sum/log_users_count_sum);
		Reporter.log("Audit company score"+expected_audit_score,true);

		//Verify Audit score is in the range of +/-10%*************************************
		JSONObject companyBrr=null;
		if(summaryObject.getJSONObject("company_brr") != null){
			companyBrr = summaryObject.getJSONObject("company_brr");}
		String actual_audit_score= companyBrr.getString("value"); 
		int ex_conv_auditScore=(int)Double.parseDouble(expected_audit_score);
		if(Integer.parseInt(actual_audit_score)==ex_conv_auditScore){ //Asserting audit score if both the scores are equal 
			Assert.assertEquals(actual_audit_score,ex_conv_auditScore, firewallType+" Company Audit score should be  ");


		}
		else{ //if audit_score is not matching applying +/- 10% (ex: Goldenset AScore=75, api  audit score should be expecting between or equal to [70,80] i.e., +/-10%
			int auditScoreRageMinValue=minusPercentValue(ex_conv_auditScore,AuditTestConstants.AUDIT_PERCENTAGE_10);
			int auditScoreRageMaxValue=plusPercentValue(ex_conv_auditScore,AuditTestConstants.AUDIT_PERCENTAGE_10);
			boolean auditScoreflag=checkScoreInBetweenPercentageRange(auditScoreRageMinValue,auditScoreRageMaxValue,Integer.parseInt(actual_audit_score));
			String str_score=firewallType+" Company Audit score value is between or equal to values  ["+auditScoreRageMinValue+","+auditScoreRageMaxValue+"] but Actual Company Audit score found ["+Integer.parseInt(actual_audit_score)+"]";
			if(!auditScoreflag)
				goldenSetErrorList.add(str_score);
			else
				Assert.assertTrue(auditScoreflag, str_score);

		}


		//Top Risky Services population with Brr


		//Low Risky Services/ Medium Risky Services/ High Risky Services- Count
		String riskType=null;
		ArrayList<String> highRiskServicesList=new ArrayList<String>();
		ArrayList<String> medRiskServicesList=new ArrayList<String>();
		ArrayList<String> lowRiskServicesList=new ArrayList<String>();
		HashMap<String, Integer> riskTypeMap=new HashMap<String, Integer>();

		int highRiskServices=0;
		int medRiskServices=0;
		int lowRiskServices=0;

		for(CompanyBRRDto obj: companyBrrList)
		{
			int brrRate=Integer.parseInt(obj.getBrr());
			String service=obj.getService();
			Reporter.log("service:"+service+" brr Rate: "+brrRate,true);

			if(( brrRate<=49))
			{
				highRiskServicesList.add(obj.getService());

			}
			else if(brrRate>49 && brrRate <80){
				medRiskServicesList.add(service);
			}
			else{
				lowRiskServicesList.add(service);
			}

		}



		highRiskServices=highRiskServicesList.size();
		medRiskServices=medRiskServicesList.size();
		lowRiskServices=lowRiskServicesList.size();

		Reporter.log("low Risky:"+lowRiskServices,true);
		Reporter.log("med Risky:"+medRiskServices,true);
		Reporter.log("high Risky:"+highRiskServices,true);

		//med risky services count validations and checking med_risk_services in the range of +/-10% range
		label="med_risk_services";
		String expected_audit_med_risk_services=""+medRiskServices;
		String actual_audit_med_risk_services=summaryObject.getString(label);
		if(actual_audit_med_risk_services==expected_audit_med_risk_services){
			Assert.assertEquals(actual_audit_med_risk_services, expected_audit_med_risk_services,firewallType+" med_risk_services should be ");//med_risk_services
		}else{
			//goldenSetErrorList.add(firewallType+"::"+label+" med_risk_services should be:"+actual_audit_med_risk_services+" but found:"+expected_audit_med_risk_services);
			validateSummaryInfoNew(expected_audit_med_risk_services,actual_audit_med_risk_services,firewallType,label,goldenSetErrorList);

		}

		//high risky services count validations and checking high_risk_services in the range of +/-10% range
		label="high_risk_services";
		String expected_audit_high_risky_services=""+highRiskServices;
		String actual_audit_high_risky_services=summaryObject.getString(label);
		if(actual_audit_high_risky_services==expected_audit_high_risky_services){
			Assert.assertEquals(actual_audit_high_risky_services, expected_audit_high_risky_services,firewallType+" high_risk_services should be ");//high_risk_services
		}else{
			//goldenSetErrorList.add(firewallType+"::"+label+" high_risk_services should be:"+actual_audit_high_risky_services+" but found:"+expected_audit_high_risky_services);
			validateSummaryInfoNew(expected_audit_high_risky_services,actual_audit_high_risky_services,firewallType,label,goldenSetErrorList);

		}


		//




		return goldenSetErrorList;

	}

	public static int minusPercentValue(int goldenSetData,int percentage)
	{
		return (goldenSetData-(goldenSetData * percentage / 100));
	}
	public static int plusPercentValue(int goldenSetData,int percentage)
	{
		return  ((goldenSetData * percentage / 100)+goldenSetData);
	}

	public static boolean checkScoreInBetweenPercentageRange(int minusPercentValue, int plusPercentValue,int auditSummaryValue) throws Exception 
	{
		boolean flag=false; 

		if(auditSummaryValue>=minusPercentValue && auditSummaryValue<=plusPercentValue) // check that number between two ranges
		{
			flag=true;
		} 

		else{
			flag=false;
		}
		return flag;


	}
	public static void validateSummaryInfoNew(String goldenSetDataField,String summaryValue,String firewallType, String summaryLable,ArrayList<String> goldenSetErrorList) throws Exception
	{
		int minValue=0;
		int maxValue=0;
		minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_10);
		boolean flag=false;
		if(minValue==maxValue){
			minValue=minusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			maxValue=plusPercentValue(Integer.parseInt(goldenSetDataField),AuditTestConstants.AUDIT_PERCENTAGE_20);
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			String str=  firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]";
			if(!flag)
				goldenSetErrorList.add(str);
			else	
				Assert.assertTrue(flag, str);

		}else{
			flag=checkScoreInBetweenPercentageRange(minValue,maxValue,Integer.parseInt(summaryValue));
			String str=firewallType+" "+summaryLable+" value is between or equal to values ["+minValue+","+maxValue+"] but Actual "+ summaryLable+" found ["+Integer.parseInt(summaryValue)+"]";
			if(!flag)
				goldenSetErrorList.add(str);
			else	
				Assert.assertTrue(flag, str);

		}
	}

	public HttpResponse getUsersCountByService(Client restClient, List<NameValuePair> requestCookieHeader, List<NameValuePair> queryParams) throws Exception {

		String requestUri = "/audit/user_count/";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getApiserverHostName(), requestUri, queryParams);
		return restClient.doGet(dataUri, requestCookieHeader);
	}
	public HttpResponse getUsers(Client restClient, List<NameValuePair> requestCookieHeader, List<NameValuePair> queryParams) throws Exception {

		String requestUri = "/audit/users/";
		URI dataUri = ClientUtil.BuidURI("https", suiteData.getApiserverHostName(), requestUri, queryParams);
		return restClient.doGet(dataUri, requestCookieHeader);
	}


	protected static List<NameValuePair> requestCookieHeader( ) throws Exception{
		List<NameValuePair> requestHeader = new ArrayList<NameValuePair>();	
		requestHeader.add(new BasicNameValuePair(HttpHeaders.AUTHORIZATION, suiteData.getAuthParam()));
		requestHeader.add(new BasicNameValuePair("X-CSRFToken", suiteData.getCSRFToken()));
		requestHeader.add(new BasicNameValuePair("X-Session", suiteData.getSessionID()));
		requestHeader.add(new BasicNameValuePair("X-TenantToken", "rs9cH+TLEU49nLtY3O2PSLoYaPBJMqUsszm+LOStr4k="));
		requestHeader.add(new BasicNameValuePair("X-User", suiteData.getUsername()));
		requestHeader.add(new BasicNameValuePair("Accept", "application/json"));
		requestHeader.add(new BasicNameValuePair("Content-Type", "application/json"));
		return requestHeader;
	}
	public static JSONObject getServiceName(String serviceId) throws Exception
	{
		List<NameValuePair> queryParam = new ArrayList<NameValuePair>();	

		queryParam.add(new BasicNameValuePair("service_id", serviceId));
		queryParam.add(new BasicNameValuePair("format", "json"));

		HttpResponse response  = AuditFunctions.getServiceDetails(new Client(), queryParam);

		JSONObject serviceObject = (JSONObject) new JSONObject(ClientUtil.getResponseBody(response)).getJSONArray("objects").get(0);

		return serviceObject;
		//return serviceObject.getString("service_name");

	}


	private String convertEpochTimeToUtc(String epochTime)
	{
		   DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
	        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
	          return format.format(new Date(Long.parseLong(epochTime)*1000));
	}



}
