package com.elastica.beatle.audit;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.testng.Reporter;

public class Test1 {
	
	
	public static final Comparator<Integer> brrComparator1 = new Comparator<Integer>(){

		final boolean order = true;
        @Override
        public int compare(Integer first, Integer second)
        {

            if (order)
            {

                return second.compareTo(first);
            }
            else
            {
                return first.compareTo(second);

            }
        }
      
    };

	
 public static void test1(Map<String,List<String>> listMap)
	    {
	        final boolean order = true;
	        try
	        {

	            Map<Integer, String> map = new TreeMap<Integer, String>(
	                    new Comparator<Integer>()
	                    {

	                        @Override
	                        public int compare(Integer first, Integer second)
	                        {

	                            if (order)
	                            {

	                                return second.compareTo(first);
	                            }
	                            else
	                            {
	                                return first.compareTo(second);

	                            }
	                        }
	                    });
	           

	         System.out.println(map);

	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	    }


	
	public static List<Integer> getEntries(Map<Integer,String> brrServicesMap) throws Exception
	{
		List<Integer> sortedBrrList = new ArrayList<Integer>(brrServicesMap.size());
		sortedBrrList.addAll(brrServicesMap.keySet());
		//Collections.sort(sortedBrrList);
		 Collections.sort(sortedBrrList);
		return sortedBrrList;
		
	}
	
	public static final Comparator<Integer> brrComparator = new Comparator<Integer>(){

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
      
    };
	
	public static Set<Integer> getSortByValues(Map<String,Integer> brrServicesMap) throws Exception
	{
		Set<Integer> sortedBrrList = new HashSet<Integer>(brrServicesMap.size());
		sortedBrrList.addAll(brrServicesMap.values());
		Reporter.log("sortedBrrList.."+sortedBrrList,true);
		//Collections.sort(sortedBrrList); new ArrayList<Integer>(sortedBrrList)
		Collections.sort(new ArrayList<Integer>(sortedBrrList),brrComparator);
		return sortedBrrList;
		
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
	
	public static TreeMap<Integer, List<String>> topNMap(TreeMap<Integer, List<String>> map, int n){
		Map<Integer, List<String>> maps=new HashMap<Integer,List<String>>();
	    return (TreeMap<Integer, List<String>>) map.subMap(0, n);
		
		
	}
	
	
	public static void getDate() throws Exception
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();
        String todate = dateFormat.format(date);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        Date todate1 = cal.getTime();    
        String fromdate = dateFormat.format(todate1);
        System.out.println("fromdate.."+fromdate);
        
        
        
        
        
        
        String dsDatestr="2016-01-22T02:53:19.772000";
        Date dsDate = dateFormat.parse(dsDatestr);
        System.out.println("dsDate.."+dateFormat.format(dsDate));
        //"setup_date": "2016-01-22T02:53:19.772000",
        
	}
	
	public static String getTodaysLogFile(String dateFormate) throws Exception
	{
		  DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		  String curDate=dateFormat.format(new Date());
		  Reporter.log("Current Date:"+curDate,true);
	      return curDate;

		
	}
	

	public static void main(String[] args) throws Exception {
		
	     
		//getTodaysLogFile();
		
		
		String status="";
		boolean flag=true;
		if("completed".equals(status) || flag){
			System.out.println("test....");
		}
				
		getDate();
		
		
		
		
		
		
		//test1();
		
		Reporter.log("===================",true);
		
		// TODO Auto-generated method stub
		Map<String,String> brrServiceCountMap=new HashMap<String,String>();
		brrServiceCountMap.put("facebook", "80");
		brrServiceCountMap.put("box","73");
		brrServiceCountMap.put("gmail","34");
		brrServiceCountMap.put("yahoo","25");
		brrServiceCountMap.put("gdrive","26");
		brrServiceCountMap.put("yammer","73");
		brrServiceCountMap.put("dropbox","25");
		brrServiceCountMap.put("mindmeister","80");
		brrServiceCountMap.put("onedrive","34");
		brrServiceCountMap.put("abcd","80");
		brrServiceCountMap.put("service1","73");
		brrServiceCountMap.put("service2","25");
		brrServiceCountMap.put("ser3","34");
		brrServiceCountMap.put("ser4","34");
		brrServiceCountMap.put("ser5","1");
		brrServiceCountMap.put("service6","2");
		brrServiceCountMap.put("service7","3");
		brrServiceCountMap.put("ser8","34");
		brrServiceCountMap.put("ser9","4");
		brrServiceCountMap.put("ser10","26");
		
		
		Map<Integer,List<String>> mapbrrServicesMap=getBrrServicesMap(brrServiceCountMap);
		Reporter.log("brr->servicesListMap..."+mapbrrServicesMap,true);
		
		Map<Integer,Integer> servicesGroupByBrrCountMap=getBrrServiceCount(mapbrrServicesMap);
		Reporter.log("servicesGroupByBrrCountMap..."+servicesGroupByBrrCountMap,true);
		
		Map<Integer,Integer> brrServiceDescMap=getTopNBrrServiceCount(servicesGroupByBrrCountMap);
		Reporter.log("brrServiceDescMap..."+brrServiceDescMap,true);
		
		
		Map<Integer,Integer> brrServiceTop5=subMap(brrServiceDescMap,5);
		Reporter.log("brrServiceTop5..."+brrServiceTop5,true);
		
		
		
		
		//List<Integer> valuesList = (List<Integer>)brrServiceCountMap.values();
		
		Set<String> uniqueValues = new HashSet<String>(brrServiceCountMap.values());
		Reporter.log("test.before."+uniqueValues,true);
		
		
		List<String> intList=new ArrayList<String>(uniqueValues);
		
		Collections.sort( intList );
		Reporter.log("ascending order:",true);
		for(String i:intList){
			Reporter.log(""+i,true);
			
		}
		
		//getKeysFromValue
		
		Collections.reverse(intList);
		Reporter.log("Decending order:",true);
		List<String> newList=null;
		List<String> newlist1=new ArrayList<String>();
		Map<Integer,List<String>> listMap=new HashMap<Integer, List<String>>();
		for(String i:intList){
			//Reporter.log(""+i,true);
			newlist1.add(i);
			
		
		}
		
		for (String str:newlist1 ){
			newList=new ArrayList<String>();
			newList= getKeysFromValue(brrServiceCountMap,str);
			listMap.put(Integer.parseInt(str), newList);
		}
		Reporter.log("newlist1"+newlist1,true);
		Reporter.log("listMap"+listMap,true);
		TreeMap<Integer, List<String>> treeMap = new TreeMap<Integer, List<String>>(
				new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}

			});
			treeMap.putAll(listMap);
			Reporter.log("treeMap"+treeMap,true);
			
			
		
			
	  //servicount group by brr top 5
		
			
			
			//Reporter.log("topNMap"+topNMap(treeMap,5),true);

	}
	
	/**
	 * below method returns map(brr, List<Services>)
	 * @param brrServiceMap
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer,List<String>> getBrrServicesMap(Map<String,String> brrServiceMap) throws Exception
	{
		Set<String> uniqueValues = new HashSet<String>(brrServiceMap.values()); //get all map values and put it into set
		Reporter.log("get All map values as set::"+uniqueValues,true);
		List<String> brrValuesList=new ArrayList<String>(uniqueValues); //convert set into list
		List<String> servicesListofEachBrr=null;
		Map<Integer,List<String>> listMap=new HashMap<Integer, List<String>>(); //map<brr,List<services>
		
		for (String brr:brrValuesList ){
			servicesListofEachBrr=new ArrayList<String>();
			servicesListofEachBrr= getKeysFromValue(brrServiceMap,brr);
			listMap.put(Integer.parseInt(brr), servicesListofEachBrr);
		}
		return listMap;
	}
	
	/**
	 * below method returns map(brr, servicecount)
	 * @param brrServiceMap
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer,Integer> getBrrServiceCount(Map<Integer,List<String>> brrServiceListMap) throws Exception
	{
		
		Map<Integer,Integer> servicesGroupByBrrCountMap=new HashMap<Integer,Integer>();
		for (Map.Entry<Integer,List<String>> entry : brrServiceListMap.entrySet()) {
			servicesGroupByBrrCountMap.put(entry.getKey(), entry.getValue().size());
		}
		return servicesGroupByBrrCountMap;
	}
	
	public static Map<Integer,Integer> getTopNBrrServiceCount(Map<Integer,Integer> brrServiceCount) throws Exception
	{
		Map<Integer,Integer> sortedMap=sortByServiceCount(brrServiceCount, "desc");
		return sortedMap;
	}
	

//sort the hashmap values on asc and desc order	
public static <K, V extends Comparable<? super V>> Map<K, V>  sortByServiceCount( Map<K, V> map,final String orderBy )
{
    List<Map.Entry<K, V>> list =new LinkedList<>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        @Override
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
        	if("asc".equals(orderBy))
               return (o1.getValue()).compareTo( o2.getValue() );
        	else
        	   return (o2.getValue()).compareTo( o1.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}

public static Map<Integer,Integer> subMap(Map<Integer,Integer> sortedBrrServiceCountMap,int n) throws Exception
{
	
    Map<Integer,Integer> map=new HashMap<Integer,Integer>();

    Iterator it = sortedBrrServiceCountMap.entrySet().iterator();
    int i=0;
    while (it.hasNext())
    {
        Map.Entry<Integer,Integer> pairs = (Map.Entry)it.next();
        map.put(
        			pairs.getKey(),
        			pairs.getValue()
        			);
       i++;
       if(i>=n)
    	   break;
    }
	return map;
	
}
	
}

