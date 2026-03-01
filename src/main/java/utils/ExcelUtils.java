package utils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	//Read rows from Excel file and return as List of Maps
	public static List<Map<String, String>> getTestData(String filePath, String sheetName) {
		List<Map<String, String>> data = new ArrayList<>();
		try {
			//use Apache POI to read Excel file and populate data list
			//open excel file and sheet
			FileInputStream fis = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			
			//get sheet by name
			XSSFSheet sheet = workbook.getSheet(sheetName);
			
			if(sheet == null) {
				System.out.println("Sheet "+ sheetName + " not found in file "+ filePath);
				return data;
			}
			
			//get header row
			XSSFRow headerRow = sheet.getRow(0);
			List<String> headers = new ArrayList<>();
			
			for(Cell cell : headerRow) {
				headers.add(cell.getStringCellValue().trim());
			}
			
			//iterate through rows and create map for each row
			int totalRows = sheet.getLastRowNum();
			
			for(int i=1; i<= totalRows; i++) {
				XSSFRow row = sheet.getRow(i);
				
				//skip empty rows
				if(row == null) {
					continue;
				}
				//Map to hold column name and cell value for current row
				Map<String, String> rowData = new HashMap<>();
				
				for(int j=0; j< headers.size(); j++) {
					Cell cell = row.getCell(j);
					String cellValue = (cell != null) ? cell.toString().trim() : "";
					rowData.put(headers.get(j), cellValue);
				}
				data.add(rowData);
				System.out.println("Read row "+ i + ": " + rowData);
			}
			
			workbook.close();
			fis.close();
			
			System.out.println("Successfully read "+ data.size() + 
					" rows from Excel file: " + filePath);
			
		}
		catch(Exception e) {
			System.out.println("Error reading Excel file: " + e.getMessage());
		}
		return data;
	}
	
	//get TestDataArray for DataProvider
	public static Object[][] getTestDataArray(String filePath, String sheetName) {
		List<Map<String, String>> dataList = getTestData(filePath, sheetName);
		Object[][] dataArray = new Object[dataList.size()][1];
		
		for(int i=0; i< dataList.size(); i++) {
			dataArray[i][0] = dataList.get(i);
		}
		return dataArray;
	}
	
	//get cell value regardless of cell type
	private static String getCellValue(Cell cell) {
		if(cell == null) {
			return "";
		}
		switch(cell.getCellType()) {
			case STRING:
				return cell.getStringCellValue().trim();
			case NUMERIC:
				return String.valueOf(cell.getNumericCellValue()).trim();
			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue()).trim();
			default:
				return cell.toString().trim();
		}
	}

}
