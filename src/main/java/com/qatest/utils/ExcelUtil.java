/**
 * 
 */
package com.qatest.utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author Divyang
 *
 */
public class ExcelUtil {
    private static Workbook book;
    private static Sheet sheet;
    //public static String TESTDATA_SHEET_PATH = "./src/test/resources/testdata/TestData.xlsx";

    public Object[][] getTestData(String excelFilePath, String sheetName, String filterColumn, String filterValue) {
        Object data[][] = null;
        try {
            FileInputStream ip = new FileInputStream(excelFilePath);
            book = WorkbookFactory.create(ip);
            sheet = book.getSheet(sheetName);

            // 1. Find the index of the filtering column
            int filterColIndex = -1;
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                if (headerRow.getCell(i).toString().equalsIgnoreCase(filterColumn)) {
                    filterColIndex = i;
                    break;
                }
            }

            if (filterColIndex == -1) {
                throw new RuntimeException("Column '" + filterColumn + "' not found in excel sheet.");
            }

            // 2. Filter rows and store in a temporary list
            List<Object[]> filteredData = new ArrayList<>();
            
            // Start loop from row 1 (skipping header)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Check if the filter column matches the filter value
                String cellValue = row.getCell(filterColIndex).toString();
                if (cellValue.equalsIgnoreCase(filterValue)) {
                    
                    Object[] rowData = new Object[headerRow.getLastCellNum()];
                    for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                        rowData[j] = row.getCell(j).toString();
                    }
                    filteredData.add(rowData);
                }
            }

            // 3. Convert List to 2D Object Array for TestNG DataProvider
            data = new Object[filteredData.size()][headerRow.getLastCellNum()];
            for (int i = 0; i < filteredData.size(); i++) {
                data[i] = filteredData.get(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public Object[][] getTestDataAsMap(String excelFilePath, String sheetName, String filterColumn, String filterValue) {
        Object[][] data = null;
        try {
            FileInputStream ip = new FileInputStream(excelFilePath);
            book = WorkbookFactory.create(ip);
            sheet = book.getSheet(sheetName);

            int rowCount = sheet.getLastRowNum();
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            List<Object[]> dataList = new ArrayList<>();
            
            for (int i = 1; i <= rowCount; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                // 1. Identify run_flag (Assuming it's a specific column name)
                Map<String, String> map = new HashMap<>();
                boolean isExecutable = false;

                for (int j = 0; j < colCount; j++) {
                    String columnName = headerRow.getCell(j).getStringCellValue();
                    String cellValue = (currentRow.getCell(j) == null) ? "" : currentRow.getCell(j).toString();
                    
                    // 2. Check if run_flag is 'Y' or 'y'
                    if (columnName.equalsIgnoreCase(filterColumn) && cellValue.equalsIgnoreCase(filterValue)) {
                        isExecutable = true;
                    }
                    // 3. Add everything to the map
                    map.put(columnName, cellValue);
                }

                // 4. Only add to list if run_flag was 'Y' or 'y'
                if (isExecutable) {
                    dataList.add(new Object[] { map });
                }
            }
            
            //Convert List to 2D Object Array for TestNG DataProvider
            data = dataList.toArray(new Object[0][0]);
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return data;
    }
}