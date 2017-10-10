package com.elastica.dataHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class XLSFileReader {

	/**
	 * This function is used to read the parameters from the excel file
	 * @param parameter
	 * @param fileName
	 * @return String
	 * @throws IOException
	 */
	public String readExcelParameter(String parameter, String fileName) throws IOException{
        StackTraceElement[] element = new Throwable().getStackTrace();
        String workbookName = fileName;
        String sheetName = element[1].getFileName();
        sheetName = sheetName.substring(0, sheetName.lastIndexOf("."));
        String methodName = element[1].getMethodName();
        HSSFRow row = null;
        HSSFCell cell = null;
        String parameterValue = null;
        HSSFSheet sheet = defineSpreadSheet(workbookName, sheetName);
        int totalRows = sheet.getLastRowNum();
        for(int i=0; i<=totalRows; i++){
            row = sheet.getRow(i);
            cell = row.getCell(0);
            String cellValue = cell.getStringCellValue();
            if(cellValue.equals(methodName)){
                if(row.getCell(1).getStringCellValue().equals(parameter)){
                    parameterValue = row.getCell(2).getStringCellValue();
                    break;
                }
            }
        }
        return parameterValue;
    }
	
	/**
	 * This function is used to read the excel table through the dataprovider
	 * @param fileName
	 * @param sheetName
	 * @param tableName
	 * @return Object[][]
	 */
	public Object[][] readExcelDataProvider(String fileName, String sheetName, String tableName){
        String[][] testData = null;
        try{
            HSSFSheet sheet = defineSpreadSheet(fileName, sheetName);
            HSSFCell[] boundaryCells = findCell(sheet, tableName);
            HSSFCell startCell = boundaryCells[0];
            HSSFCell endCell = boundaryCells[1];
            int startRow = startCell.getRowIndex() + 1;
            int endRow = endCell.getRowIndex();
            int startCol = startCell.getColumnIndex() + 1;
            int endCol = endCell.getColumnIndex() - 1;
            testData = new String[endRow - startRow + 1][endCol - startCol + 1];
            for (int i = startRow; i < endRow + 1; i++) {
                for (int j = startCol; j < endCol + 1; j++) {
                    if (sheet.getRow(i).getCell(j).getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        testData[i - startRow][j - startCol] = sheet.getRow(i)
                                .getCell(j).getStringCellValue();
                    } else if (sheet.getRow(i).getCell(j).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        Double temp = sheet.getRow(i).getCell(j)
                                .getNumericCellValue();
                        testData[i - startRow][j - startCol] = String
                                .valueOf(temp.intValue());
                    }
                }
            }
        }catch(Exception e){

        }
        return testData;
    }
	
	/**
	 * This function is used to find the start and end cell of the excell table
	 * @param sheet
	 * @param text
	 * @return HSSFCell[]
	 */
	public HSSFCell[] findCell(HSSFSheet sheet, String text){
        String pos = "start";
        HSSFCell[] cells = new HSSFCell[2];
        for(Row row: sheet){
            for(Cell cell: row){
                if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING && text.equals(cell.getStringCellValue())){
                    if(pos.equalsIgnoreCase("start")){
                        cells[0] = (HSSFCell) cell;
                        pos = "end";
                    }else{
                        cells[1] = (HSSFCell) cell;
                    }
                }
            }
        }
        return cells;
    }
	
	/**
	 * This function is used to define the excel workbook and excel sheet
	 * @param workbookName
	 * @param sheetName
	 * @return HSSFSheet
	 */
	public HSSFSheet defineSpreadSheet(String workbookName, String sheetName){
        HSSFSheet sheet = null;
        try{
            String fileName = System.getProperty("user.dir") + File.separator + "excelsheet" + File.separator + workbookName+".xls";
            File xlsFile = new File(fileName);
            FileInputStream in = new FileInputStream(xlsFile);
            @SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(in);
            sheet = workbook.getSheet(sheetName);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return sheet;
    }
}
