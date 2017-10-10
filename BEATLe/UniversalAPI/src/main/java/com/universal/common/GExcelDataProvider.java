package com.universal.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;

/**
 *
 * @author rahulkumar
 */
public class GExcelDataProvider {

    String jsonResponse;
    private List<String> dataKeysList = new ArrayList();
    private Map<String, Object> dataSet;
    private final boolean online;
    int maxRetryCount = 5;
    int retryCount = 0;

    /**
     *
     * @param uniqueExcelId
     */
    public GExcelDataProvider(String excelIDorLocation) {
        this.online = !excelIDorLocation.contains(File.separator);
        if (this.online) {
            this.dataSet = excelToMapOnLine(excelIDorLocation);
        } else {
            this.dataSet = excelToMapOffLine(excelIDorLocation);
            System.out.println("===>" + dataSet.size());
        }
        trimdata();
    }

    private Map<String, Object> excelToMapOffLine(String excelFileLocation) {
        File file = new File(excelFileLocation);
        FileInputStream inp = null;
        try {
            inp = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Reporter.log("File Not Found :" + ex.getLocalizedMessage(), true);
        }
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inp);
        } catch (IOException | InvalidFormatException ex) {
            Reporter.log("Exception in Excel To Json Conversion" + ex.getLocalizedMessage(), true);
        }
        int numberOfSheets = workbook.getNumberOfSheets();
        System.out.println("Total Number of Sheets :" + numberOfSheets);
        JSONObject json = new JSONObject();
        // Get the first Sheet.
        String sheetName = null;
        for (int sheetCount = 0; sheetCount < numberOfSheets; sheetCount++) {
            Sheet sheet = workbook.getSheetAt(sheetCount);
            sheetName = sheet.getSheetName();
            System.out.println("Sheet Name :" + sheetName);
            List<String> firstRow = new ArrayList();
            int rowCount = 0;
            // Iterate through the rows.
            JSONArray rows = new JSONArray();
            for (Iterator<Row> rowsIT = sheet.rowIterator();
                    rowsIT.hasNext();) {
                rowCount++;
                Row row = rowsIT.next();
                JSONObject jRow = new JSONObject();
                Map<String, Object> columnValues = new HashMap();
                // Iterate through the cells.
                JSONArray cells = new JSONArray();
                int i = 0;
                for (Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext();) {
                    Cell cell = cellsIT.next();
                    if (rowCount == 1) {
                        firstRow.add(cell.getStringCellValue());
                    } else {
                        String stringCellValue;
                        try {
                            stringCellValue = cell.getStringCellValue();
                            columnValues.put(firstRow.get(i++), stringCellValue);
                        } catch (Exception e) {
                            try {
                                boolean booleanCellValue = cell.getBooleanCellValue();
                                columnValues.put(firstRow.get(i++), booleanCellValue);
                            } catch (Exception ex) {
                                try {
                                    double numericCellValue = cell.getNumericCellValue();
                                    columnValues.put(firstRow.get(i++), numericCellValue+"");
                                } catch (Exception exx) {

                                }

                            }
                        }
                    }
                }
                if (rowCount != 1) {
                    boolean allColumnCheck = false;
                    for (Map.Entry<String, Object> entrySet : columnValues.entrySet()) {
                        String key = entrySet.getKey();
                        Object value = entrySet.getValue();
                        if (allColumnCheck == false) {
                            if (value.toString().length() > 0) {
                                allColumnCheck = true;
                            }
                        }
                    }
                    if (allColumnCheck) {
                        rows.put(columnValues);
                    }
                }
            }
            json.put(sheetName, rows);
        }
        System.out.println("Json String :" + json.toString());
        try {
            return new ObjectMapper().readValue(json.toString(), HashMap.class);
        } catch (IOException ex) {
            System.out.println("Isuue Found with Object Mapping :" + ex.getLocalizedMessage());
        }
        return null;
    }

    private Map<String, Object> excelToMapOnLine(String uniqueExcelId) {
        try {
            String url = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?"
                    + "id=" + uniqueExcelId;
            System.out.println("URL :" + url);
            HttpClient httpClient = CommonTestClient.getHttpClient();
            HttpUriRequest request = new HttpGet(url);
            HttpResponse execute = httpClient.execute(request);
            String getResponse = CommonTest.getResponseBody(execute);
            System.out.println(" ## Data Provider :" + getResponse);
            return new ObjectMapper().readValue(getResponse, HashMap.class);
        } catch (IOException ex) {
            retryCount++;
            if (retryCount <= maxRetryCount) {
                try {
                    Thread.sleep(5000);// Wait for 5 secs and try again..
                } catch (InterruptedException ex1) {
                    Logger.getLogger(GExcelDataProvider.class.getName()).log(Level.SEVERE, null, ex1);
                }
                excelToMapOnLine(uniqueExcelId);
            }
            Logger.getLogger(GExcelDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<Map<String, Object>> getDataAsMapList(String sheetName, String... args) {
        dataKeysList.clear();
        for (String arg : args) {
            if (this.online) {
                dataKeysList.add(arg.replaceAll(" ", "_").trim());
            } else {
                dataKeysList.add(arg.trim());
            }
        }
        return removeUnderScoreFromKey(filteredDataSet((List<Map<String, Object>>) dataSet.get(sheetName)));
    }
    
    public List<Map<String, Object>> getDataAsMapList(String sheetName, List<String> args) {
        dataKeysList.clear();
        for (String arg : args) {
            if (this.online) {
                dataKeysList.add(arg.replaceAll(" ", "_").trim());
            } else {
                dataKeysList.add(arg.trim());
            }
        }
        return removeUnderScoreFromKey(filteredDataSet((List<Map<String, Object>>) dataSet.get(sheetName)));
    }

    public List<Map<String, Object>> removeUnderScoreFromKey(List<Map<String, Object>> dataList) {
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            Map<String, Object> newData = new HashMap<>();
            for (Map.Entry<String, Object> entrySet : data.entrySet()) {
                String key = entrySet.getKey();
                Object value = entrySet.getValue();
                newData.put(key.replaceAll("_", " "), value);
            }
            maps.add(newData);
        }
        return maps;
    }

    /**
     *
     * @param sheetName
     * @param args
     * @return
     */
    public Object[][] getData(String sheetName, String... args) {
        this.dataKeysList.clear();
        for (String arg : args) {
            if (this.online) {
                this.dataKeysList.add(arg.replaceAll(" ", "_").trim());
            } else {
                this.dataKeysList.add(arg.trim());
            }
        }
        List<Map<String, Object>> dataMapList = filteredDataSet((List<Map<String, Object>>) dataSet.get(sheetName));
        System.out.println("Data Map List :" + dataMapList.toString());
        Object[][] dataProviderTwoDimensionalArray = new Object[dataMapList.size()][this.dataKeysList.size()];
        int count = 0;
        for (Map<String, Object> map : dataMapList) {
            int i = 0;
            for (String key : this.dataKeysList) {
                dataProviderTwoDimensionalArray[count][i++] = map.get(key);
            }
            count++;
        }
        System.out.println("==>" + Arrays.deepToString(dataProviderTwoDimensionalArray));
        return dataProviderTwoDimensionalArray;
    }

    private List<Map<String, Object>> filteredDataSet(List<Map<String, Object>> dataMapList) {

        List<Map<String, Object>> filteredDataMapList = new ArrayList<>();
        for (Map<String, Object> dataMapList1 : dataMapList) {
            boolean blankCheck = true;
            for (String key : dataKeysList) {
                if (dataMapList1.get(key) != null) {
                    String value = dataMapList1.get(key).toString();
                    if (value.length() == 0) {
                        blankCheck = false;
                    }
                } else {
                    blankCheck = false;
                }
            }
            if (blankCheck) {
                filteredDataMapList.add(dataMapList1);
            }
        }
        return filteredDataMapList;
    }

    /**
     *
     */
    private void trimdata() {
        for (Map.Entry<String, Object> entrySet : dataSet.entrySet()) {
            String key = entrySet.getKey();
            List<Map<String, Object>> value = new ArrayList<>();
            for (Map<String, Object> map : (List<Map<String, Object>>) entrySet.getValue()) {
                List<String> keyList = new ArrayList<>(map.keySet());
                for (String key1 : keyList) {
                    map.put(key1, map.get(key1).toString().trim());
                }
                value.add(map);
            }
            dataSet.put(key, value);
        }
    }

    /**
     *
     * @param currentValue
     * @param newValue
     */
    public void updatedata(String currentValue, String newValue) {
        for (Map.Entry<String, Object> entrySet : dataSet.entrySet()) {
            String key = entrySet.getKey();
            List<Map<String, Object>> value = new ArrayList<>();
            for (Map<String, Object> map : (List<Map<String, Object>>) entrySet.getValue()) {
                List<String> keyList = new ArrayList<>(map.keySet());
                for (String key1 : keyList) {
                    map.put(key1, map.get(key1).toString().replaceAll(currentValue, newValue));
                }
                value.add(map);
            }
            dataSet.put(key, value);
        }
    }

    /**
     *
     * @param sheetName
     * @param currentValue
     * @param newValue
     */
    public void updatedata(String sheetName, String currentValue, String newValue) {
        List<Map<String, Object>> value = new ArrayList<>();
        for (Map<String, Object> map : (List<Map<String, Object>>) dataSet.get(sheetName)) {
            List<String> keyList = new ArrayList<>(map.keySet());
            for (String key1 : keyList) {
                map.put(key1, map.get(key1).toString().replaceAll(currentValue, newValue));
            }
            value.add(map);
        }
        dataSet.put(sheetName, value);
    }

    /**
     *
     * @param sheetName
     * @param columnName
     * @param currentValue
     * @param newValue
     */
    public void updatedata(String sheetName, String columnName, String currentValue, String newValue) {
        columnName = columnName.replaceAll(" ", "_");
        List<Map<String, Object>> value = new ArrayList<>();
        for (Map<String, Object> map : (List<Map<String, Object>>) dataSet.get(sheetName)) {
            List<String> keyList = new ArrayList<>(map.keySet());
            for (String key1 : keyList) {
                if (columnName.equals(key1)) {
                    map.put(key1, map.get(key1).toString().replaceAll(currentValue, newValue));
                }
            }
            value.add(map);
        }
        dataSet.put(sheetName, value);
    }

    public static void main(String[] args) {

        String excelId = "/Users/rahulkumar/Desktop/ElasticaQADataProvider (1).xlsx";
        GExcelDataProvider dataProvider = new GExcelDataProvider(excelId);
        List<Map<String, Object>> dataAsMapList = dataProvider.getDataAsMapList("GdriveCredentials", "KEY", "VALUE");
        System.out.println("==>" + dataAsMapList.toString());
    }

}
