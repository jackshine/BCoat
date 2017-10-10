package com.elastica.beatle.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Reporter;

import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class AuditGoldenSetDataController3 {
	
	protected  String datafilepath = null; 
	protected  String cwsSheetName=null;
	protected File inputFile=null;
	protected ArrayList<String> goldenSetDataList=new ArrayList<String>();
	protected Map<String, ArrayList<Cell>> knownGoodMap=new LinkedHashMap<String, ArrayList<Cell>>();
	org.apache.poi.ss.usermodel.Workbook wb=null;
	org.apache.poi.ss.usermodel.Sheet sheet=null;
	
	public AuditGoldenSetDataController3(String cwsSheetName) throws Exception{
	
		//goldenset data xl sheet file path
		datafilepath=FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_GOLDENSET_DATA_NEW_SHEET);
		//cwsSheetName=AuditTestConstants.CWS_DEVICEID_TSV_SHEET;
		this.cwsSheetName=cwsSheetName;
				
	    inputFile = new File(datafilepath);
		InputStream inStream = new FileInputStream(inputFile); //load the GoldenSetData.xlsx into the stream
		wb = WorkbookFactory.create(inStream); //get the workbook
		sheet = wb.getSheet(cwsSheetName); //get the sheet data of the passed sheet name
		
		//list for the goldenset xl sheet columns
		goldenSetDataList.add("Service Name");
		
		inStream=null;
		
		
	}
	
	public List<GoldenSetData> loadXlData1() throws Exception
	{
		 Iterator rowIter = sheet.rowIterator(); 
		 HashMap< String, List<String> > properties = new HashMap< String, List<String> >();
		 Cell cell=null;
		 Cell cell2=null;
		 Cell cell3=null;
		 final int ZERO_INDEX=0;
		 final int USERNAMES_COL_INDEX=1;
		 final int TOTALBYTES_COL_INDEX=2;
		 final int UPLOADS_COL_INDEX=3;
		 final int DOWNLOADS_COL_INDEX=4;
		 final int LOCATIONS_COL_INDEX=5;
		 final int SESSIONS_COL_INDEX=6;
		 
		 
	/*	 List<String> usernamesList=new ArrayList<String>();
		 List<String> totalBytesList=new ArrayList<String>();
		 List<String> uploadsList=new ArrayList<String>();
		 List<String> downloadsList=new ArrayList<String>();
		 List<String> locationsList=new ArrayList<String>();
		 List<String> sessionsList=new ArrayList<String>();*/
		 
		 Integer columnNo = null;
		 String key=null;
		 List<GoldenSetData> goldensetlist=new ArrayList<GoldenSetData>();
		 
		 List<String> testList=new ArrayList<String>();
		 
		 GoldenSetData data=null;
		 Set<String> usernameSet=new HashSet<>();
		 List<String> uniqueLocationsList=new ArrayList<String>();
		 List<String> totalBytesList=new ArrayList<String>();
		 List<String> uploadBytesList=new ArrayList<String>();
		 List<String> downloadBytesList=new ArrayList<String>();
		 List<String> sessionsForEachUserList=new ArrayList<String>();
		 boolean goldenSetObjFlag=false;
		 int i=0;
		 
		 while(rowIter.hasNext()){
	            Row myRow = (Row) rowIter.next();
	            if(myRow.getRowNum()==0)
	            	continue;
	             Iterator cellIter = myRow.cellIterator();
	             Vector<String> cellStoreVector=new Vector<String>();
	             
	             
	             do {
	            	 data=new GoldenSetData();
	             }while (goldenSetObjFlag);
	             
	             while(cellIter.hasNext()){
	            	
	                 cell = (Cell) cellIter.next();
	                 columnNo = cell.getColumnIndex();
	               // String value = "";
	            
	                String value = getCellValue(cell);
	                int size=testList.size();
	                
	                
	                if(ZERO_INDEX==cell.getColumnIndex())
	                {
	                	
	                	if(!value.equals("") && testList.isEmpty()){
	                		data.setServiceName(value);
		                	testList.add(data.getServiceName());
	                		
	                	}
	                	else if(!value.equals("") && !testList.contains(value)){
	                		data.setServiceName(testList.get(i));
	                		data.setUsernameSet(usernameSet);
	                		data.setTotalBytesList(totalBytesList);
	                		data.setUploadBytesList(uploadBytesList);
	                		data.setDownloadBytesList(downloadBytesList);
	                		data.setUniqueLocationsList(uniqueLocationsList);
	                		data.setSessionForEachUserList(sessionsForEachUserList);
	                		
	                		goldensetlist.add(data);
	                		data=new GoldenSetData();
	                		data.setServiceName(value);
		                	testList.add(data.getServiceName());
		                	usernameSet=new HashSet<String>();
		                	totalBytesList=new ArrayList<String>();
		                	uploadBytesList=new ArrayList<String>();
		                	downloadBytesList=new ArrayList<String>();
		                	uniqueLocationsList=new ArrayList<String>();
		                	sessionsForEachUserList=new ArrayList<String>();
		                	
		                	i++;
	                		
	                	}//else{data.setServiceName(value);}
		                	  //testList.add(data.getServiceName());}
	                	
	                }
	                // if(data.getServiceName() != null)
	                	// testList.add(data.getServiceName());  //ser1
	                 
	                else if((USERNAMES_COL_INDEX==cell.getColumnIndex()) ){
	                	
	                	// f || t=T  f ||  (t && t)
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		usernameSet.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		usernameSet.add(value);
	                	}
	                	/*else if (data.getServiceName()!=null && !testList.contains(data.getServiceName())) {
	                		goldenSetObjFlag=true;
	                		data.setUsernamesSet(usernameSet);
	                		
	                	}*/
	                	
	                	//data.setUserName(value);
	                }
	                else if(TOTALBYTES_COL_INDEX==cell.getColumnIndex()){
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		totalBytesList.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		totalBytesList.add(value);
	                	}
	                }
	                else if(UPLOADS_COL_INDEX==cell.getColumnIndex()){
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		uploadBytesList.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		uploadBytesList.add(value);
	                	}
					}
	                else if(DOWNLOADS_COL_INDEX==cell.getColumnIndex()){
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		downloadBytesList.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		downloadBytesList.add(value);
	                	}
					}
	                else if(LOCATIONS_COL_INDEX==cell.getColumnIndex()){
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		uniqueLocationsList.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		uniqueLocationsList.add(value);
	                	}
					}
	                else if(SESSIONS_COL_INDEX==cell.getColumnIndex()){
	                	if (data.getServiceName()==null && !testList.isEmpty() ) {
	                		sessionsForEachUserList.add(value);
	                	}
	                	
	                	else if (data.getServiceName()!=null && testList.contains(data.getServiceName())) {
	                		sessionsForEachUserList.add(value);
	                	}
					 }
	               
	                
	             }
	            
	  	         }
		Reporter.log(" xlDataSheet:" + goldensetlist,true);
		 return goldensetlist;
	}
	
	
	public List<GoldenSetData> loadXlData() throws Exception
	{
		
		Iterator<Row> iterator = sheet.iterator();
		GoldenSetData gsData = null;
		int servicenameColIndex = 0;
		int serviceIdColIndex = 1;
		int usernameColIndex = 2;
		int totBytesColIndex = 3;
		int uploadsColIndex = 4;
		int downloadsColIndex = 5;
		int uniqueLocationsColIndex = 6;
		int destinationsColIndex = 7;

		Set<String> usernameSet;
		List<String> totalBytesList;
		List<String> uploadBytesList;
		List<String> downloadBytesList;
		List<String> uniqueLocationsList;
		List<String> sessionForEachUserList;

		List<String> serviceNamesList = new ArrayList<String>();

		List<GoldenSetData> goldenSetDataList = new ArrayList<GoldenSetData>();

		usernameSet = new HashSet<String>();
		totalBytesList = new ArrayList<String>();
		uploadBytesList = new ArrayList<String>();
		downloadBytesList = new ArrayList<String>();
		uniqueLocationsList = new ArrayList<String>();
		sessionForEachUserList = new ArrayList<String>();

      //creating GoldenSetData class instance atleast one time
		int i = 0;
		do {

			gsData = new GoldenSetData();
		} while (i > 1);

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0)
				continue;
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				String cellValue = getCellValue(cell);

				// System.out.print("getCellValue::"+getCellValue(cell));

				if (servicenameColIndex == cell.getColumnIndex()) {
					if ((!cellValue.equals("")) && serviceNamesList.isEmpty()) {
						serviceNamesList.add(cellValue);
						gsData.setServiceName(cellValue);
						Reporter.log("first service..gsData.getServicename"+gsData.getServiceName(),true);
					} // enter in this loop very first time

					if ((!cellValue.equals("")) && !serviceNamesList.contains(cellValue)) {
						goldenSetDataList.add(gsData);
						gsData = new GoldenSetData();
						usernameSet = new HashSet<String>();
						totalBytesList = new ArrayList<String>();
						uploadBytesList = new ArrayList<String>();
						downloadBytesList = new ArrayList<String>();
						uniqueLocationsList = new ArrayList<String>();
						sessionForEachUserList = new ArrayList<String>();

						gsData.setServiceName(cellValue);
						serviceNamesList.add(cellValue);// enter here for the
														// second service
					}
					
				}
				if (serviceIdColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) { //!cellValue.equals("") check if the fields having empty value.
					gsData.setServiceID(cellValue);

				}
				if (usernameColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) { //!cellValue.equals("") check if the fields having empty value.
					usernameSet.add(cellValue);
					gsData.setUsernameSet(usernameSet);

				}
				if (totBytesColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) {
					totalBytesList.add(cellValue);
					gsData.setTotalBytesList(totalBytesList);

				}
				if (uploadsColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) {
					uploadBytesList.add(cellValue);
					gsData.setUploadBytesList(uploadBytesList);

				}
				if (downloadsColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) {
					downloadBytesList.add(cellValue);
					gsData.setDownloadBytesList(downloadBytesList);

				}
				if (uniqueLocationsColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) {
					uniqueLocationsList.add(cellValue);
					gsData.setUniqueLocationsList(uniqueLocationsList);

				}
				if (destinationsColIndex == cell.getColumnIndex() && (!cellValue.equals(""))) {
					sessionForEachUserList.add(cellValue);
					gsData.setSessionForEachUserList(sessionForEachUserList);

				}

			}

		}
	
		System.out.println("gsData..." + goldenSetDataList);
		return goldenSetDataList;
		
	}
	
	private String getCellValue(Cell cell) {
		String cellValue = null;
		switch (cell.getCellType()) {
  		  case Cell.CELL_TYPE_STRING:
		    cellValue = cell.getStringCellValue();
		  break;
		  case Cell.CELL_TYPE_FORMULA:
		    cellValue = cell.getCellFormula();
		  break;
		  case Cell.CELL_TYPE_NUMERIC:
			  int i = (int)cell.getNumericCellValue(); 
			  cellValue = String.valueOf(i);
		   break;
		  case Cell.CELL_TYPE_BLANK:
		    cellValue = "";
		  break;
		  case Cell.CELL_TYPE_BOOLEAN:
		    cellValue = Boolean.toString(cell.getBooleanCellValue());
		  break;
		}
		return cellValue;
	}
	
	
	
	
	public static void main(String[] str) throws Exception
	{
		//AuditTestConstants.FIREWALL_BARRACUDA_ACTIVITYLOG
		//AuditTestConstants.FIREWALL_BARRACUDA_SYSLOG
		//AuditTestConstants.FIREWALL_CISCO_WSA_W3C:
		//AuditTestConstants.FIREWALL_CISCO_WSA_ACCESS
		//AuditTestConstants.FIREWALL_ZSCALAR
		//AuditTestConstants.BLUECOATPROXY_DATA_SHEET
		//AuditTestConstants.MCAFEE_SEF_SHEET
		//AuditTestConstants.FIREWALL_CHECKPOINT_SMARTVVIEW_SHEET
		//AuditTestConstants.FIREWALL_CHECKPOINT_SMARTVVIEW_SHEET
		
		
		AuditGoldenSetDataController3 controller=new AuditGoldenSetDataController3(AuditTestConstants.FIREWALL_BE_BARRACUDA_SYS);
		controller.loadXlData();
	}

}
