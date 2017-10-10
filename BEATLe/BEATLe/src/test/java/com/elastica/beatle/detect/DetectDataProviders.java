package com.elastica.beatle.detect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import com.elastica.beatle.detect.dto.AttributeBean;
import com.elastica.beatle.tests.detect.DetectUtils;

public class DetectDataProviders extends DetectUtils {
	
	@DataProvider()
    public static Object[][] BBIDataProvider(Method method)  {
		
		//List<AttributeBean> list = 	getAttributeList();
		Object[][] atrributesDP = new Object[1][2];
		
		Object[] enabled = {false};
		for(int i=0; i<enabled.length;i++){
			AttributeBean  ab = new AttributeBean(10, 10, 2, false);
			atrributesDP[i][0] = false;
			atrributesDP[i][1]  = ab;
			//System.out.println("atrributesDP :::::  "+list.get(i));
		}
			
		return atrributesDP;
	}
 
	
	
	public Set<AttributeBean> getListOfAttributeBeans(){
		Integer[] window = {1,3,5,10};
		Integer[] importence = {1,2,3,4};
		Integer[] threshold = {1,2,3,4,5,6,7,8,9,10};
		Set<AttributeBean> list = new HashSet<>();
		
		AttributeBean  ab = null;
		for (Integer thresh : threshold) {
			ab = new AttributeBean();
			ab.setThreshold(thresh);
			for (Integer imp : importence) {
				ab.setImportance(imp);
				for (Integer  win : window) {
					ab.setWindow(win);
					list.add(ab);
				}
			}
			
		}
		System.out.println("list:::::  "+list.toString());
		
		return list;
		
	}
	
	 @DataProvider()
	    public static  Object[][] dataProvider(Method method)  {
			
			List<AttributeBean> list = 	getAttributeList();
			Object[][] atrributesDP = new Object[list.size()][];
			
			
			for(int i=0; i<atrributesDP.length;i++){
				atrributesDP[i] = new Object[] { list.get(i), "" };
			}
				
			return atrributesDP;
		}
	 
	 
	 @DataProvider()
	    public static Object[][] suspiciousLocationdataProvider(Method method)  {
			
		 AttributeBean[] attributes = {new AttributeBean(4, 720, 1, true),
				 new AttributeBean(5, 720, 2, true),
				 new AttributeBean(2, 1440, 3, true),
				 new AttributeBean(2, 720, 4, true),
				 new AttributeBean(2, 1440, 1, true),
				 new AttributeBean(2, 720, 3, true),
				 new AttributeBean(2, 1440, 2, true),
				 new AttributeBean(2, 1440, 4, true),
				 new AttributeBean(2, 720, 2, false)};
			Object[][] atrributesDP = new Object[attributes.length][];
			
			
			for(int i=0; i<atrributesDP.length;i++){
				
				atrributesDP[i] = new Object[] { attributes[i], "" };
				
			}
				
			return atrributesDP;
		}
		
		
	 @DataProvider()
	    public Object[][] BBIDataProvider1(Method method)  {
		 List<AttributeBean> list = new ArrayList<>();
		 list.add(new AttributeBean(10, 1,true));
		 list.add(new AttributeBean(20, 2,true));
		 list.add(  new AttributeBean(30, 4,true));
		 list.add(new AttributeBean(40, 3,true));
		 list.add(new AttributeBean(50, 4,true));
		 list.add(new AttributeBean(60, 2,true));
		 list.add( new AttributeBean(70, 3,true));
		 list.add( new AttributeBean(80, 3,true));
		 list.add( new AttributeBean(90, 4,true));
		 list.add(new AttributeBean(10, 4,false));
		 
		 Object[][] atrributesDP = new Object[list.size()][];
			
			
			for(int i=0; i<atrributesDP.length;i++){
				atrributesDP[i] = new Object[] { "", list.get(i)};
			}
				
			return atrributesDP;
		}		

		
		protected static List<AttributeBean> getAttributeList() {
			
			List<AttributeBean> list = new ArrayList<>();
			
			//Event=2, duration=10, importance=less important
			AttributeBean  ab1 = new AttributeBean(2, 3, 2, true);
			AttributeBean  ab2 = new AttributeBean(10, 5, 4, true);
			AttributeBean  ab3 = new AttributeBean(2, 1, 3, true);
			AttributeBean  ab4 = new AttributeBean(5, 1, 2, true);
			AttributeBean  ab5 = new AttributeBean(5, 3, 3, true);
			AttributeBean  ab6 = new AttributeBean(10, 1, 4, true);
			AttributeBean  ab7 = new AttributeBean(10, 3, 4, true);
			AttributeBean  ab8 = new AttributeBean(10, 5, 2, true);
			AttributeBean  ab9 = new AttributeBean(10, 10, 3, true);
			AttributeBean  ab14 = new AttributeBean(10, 10, 2, false);
			
				list.add(ab1);
				list.add(ab2);
				list.add(ab3);
				list.add(ab4);
				list.add(ab5);
				list.add(ab6);
				list.add(ab7);
				list.add(ab8);
				list.add(ab9);
				list.add(ab14);
			
			
			System.out.println("list of attributes :::: "+list);
			
			return list;
		}
	

}
