package com.elastica.beatle.audit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.elastica.beatle.fileHandler.FileHandlingUtils;

public class AuditGoldenSetDataController {
	
	protected  String datafilepath = null; 
	protected  String cwsSheetName=null;
	protected File inputFile=null;
	protected ArrayList<String> goldenSetDataList=new ArrayList<String>();
	protected Map<String, ArrayList<Cell>> knownGoodMap=new LinkedHashMap<String, ArrayList<Cell>>();
	org.apache.poi.ss.usermodel.Workbook wb=null;
	org.apache.poi.ss.usermodel.Sheet sheet=null;
	
	public AuditGoldenSetDataController(String cwsSheetName) throws Exception{
	
		//goldenset data xl sheet file path
		datafilepath=FileHandlingUtils.getFileAbsolutePath(AuditTestConstants.AUDIT_GOLDENSET_DATA_SHEET);
		//cwsSheetName=AuditTestConstants.CWS_DEVICEID_TSV_SHEET;
		this.cwsSheetName=cwsSheetName;
				
	    inputFile = new File(datafilepath);
		InputStream inStream = new FileInputStream(inputFile); //load the GoldenSetData.xlsx into the stream
		wb = WorkbookFactory.create(inStream); //get the workbook
		sheet = wb.getSheet(cwsSheetName); //get the sheet data of the passed sheet name
		
		//list for the goldenset xl sheet columns
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_SCORE);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_SERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_USERS);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_DESTINATIONS);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_TOP_RISKY_SERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_TOP_USED_SERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_HIGH_RISKY_SERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_MED_RISKY_SERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_LOW_RISKYSERVICES);
		goldenSetDataList.add(AuditTestConstants.GOLDENSETDATA_AUDIT_DESTINATION_SESSIONS);	
		
		//closing the stream
		
		inStream=null;
		
		// knownGoodMap=readExcelFileDataAndPrepareGoldenSetData(cwsSheetName,goldenSetDataList);
	
		
	}
	
	
	/**
	 * The below method read the worsheet from the xl file and prepared map (key as columnname and values are list of cell values for that column)
	 * @param sheetName
	 * @param filePath
	 * @param knownGoodMap
	 * @param goldenSetDataList
	 * @return
	 */
	public  Map<String, ArrayList<Cell>>  readExcelFileDataAndPrepareGoldenSetData(){
		
		try {

			//Iterate all columns in the sheet
			for (String colWanted : goldenSetDataList) {
				ArrayList<Cell> list = new ArrayList<Cell>();
				String columnWanted = colWanted;
				Integer columnNo = null;
			
				Row firstRow = sheet.getRow(0);//getting firstrow for header names
				for (Cell cell : firstRow) {
					if (cell.getStringCellValue().equals(columnWanted)) {
						columnNo = cell.getColumnIndex();
						break;

					}
				}

				if (columnNo != null) {
					for (Row row : sheet) {
						Cell c = row.getCell(columnNo);
						if (c == null
								|| c.getCellType() == Cell.CELL_TYPE_BLANK) {
							// Nothing in the cell in this row, skip it
						} else {
							list.add(c);//store all the cells for that column
						}
					}
				} else {
					// System.out.println("could not find column " +
					//columnWanted + " in first row of " + fileIn.toString());
					// System.out.println("no col data");
				}

				list.remove(0);
				knownGoodMap.put(columnWanted, list);
				
			}

			
			System.out.println("FINISHED**********");
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		return knownGoodMap;
	}	
	
	
	
	public static void main(String[] str) throws Exception
	{
		AuditGoldenSetDataController controller=new AuditGoldenSetDataController(AuditTestConstants.CWS_DEVICEID_TSV_SHEET);
		controller.readExcelFileDataAndPrepareGoldenSetData();
	}

}
