/**
 * 
 */
package com.elastica.beatle.fileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author anuvrath
 *
 */
public class ExcelFileHandler {

	/**
	 * @param filePath
	 */
	public void readExcel(String filePath){
		try
		{	
			
			if(filePath.contains("xlsx")){			
				readExcelFileContent(new HSSFWorkbook(new FileInputStream(new File(filePath))).getSheetAt(0).iterator());
			}	
			else if(filePath.contains("xls")){
				readExcelFileContent(new XSSFWorkbook(new FileInputStream(new File(filePath))).getSheetAt(0).iterator());
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
			
	}

	/**
	 * handle the cell content based on the requirement when required.
	 * @param rowIterator
	 */
	private void readExcelFileContent(Iterator<Row> rowIterator){
		try
        { 
            while (rowIterator.hasNext())
            {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                 
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType())
                    {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "t");
                            break;
                    }
                }
                System.out.println("");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}			
}
