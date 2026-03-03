package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDemo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileInputStream file = new FileInputStream("D:\\Eclipse\\SauceDemo-Framework"
				+ "\\src\\test\\resources\\testdata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		int sheets = workbook.getNumberOfSheets();
		
		//loop through all sheets
		for(int i=0; i<sheets; i++) {
			//check if sheet name is LoginTestData
			if(workbook.getSheetName(i).equalsIgnoreCase("LoginTestData")) {
			XSSFSheet sheet = workbook.getSheetAt(i);
			
			//loop through rows and columns to read data
			Iterator<Row> rows = sheet.iterator();
			Row firstRow  = rows.next();
			Iterator<Cell> cells = firstRow.cellIterator();
			int k=0;
			int column = 0;
			while(cells.hasNext())
			{
				Cell value = cells.next();
				if(value.getStringCellValue().equalsIgnoreCase("Username")) {
					column = k;
				};
				k++;
			}
			System.out.println("Username is in column: "+ column);
				
			}
			
			
		}
	}
	
	

}
