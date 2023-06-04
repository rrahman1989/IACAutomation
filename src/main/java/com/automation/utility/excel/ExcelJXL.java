package com.automation.utility.excel;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 
 * JXL Library to read for report search terms 
 * @author reazur.rahman
 *
 */

public class ExcelJXL {
	
	public static Sheet workSheet;
	public static Workbook workBook = null;
	public static Hashtable dict = new Hashtable();

	
	/**
	 * This is a constructor that takes Report excel file Path
	 * @param excelPath Excel Path
	 * @param excelSheet Excel Sheet Name
	 */
	public ExcelJXL(String excelPath, String excelSheet) {
		
		try {
			System.out.println("Excel Path: " + excelPath);
			System.out.println("Shet Name: " + excelSheet);
			workBook = Workbook.getWorkbook(new File(excelPath));
			workSheet = workBook.getSheet(excelSheet);

		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param columnNumber Column Number
	 * @param rowNumber Row Number
	 * @return Returns Value by Column and Row Number
	 */
	
	public static String getCellValue(int columnNumber, int rowNumber) {
		
		return workBook.getSheet(0).getCell(columnNumber, rowNumber).getContents();
		
	}
	
	
	/**
	 * 
	 * @return Returns the Number of Rows
	 */
	public static int rowCount() {
		
		return workSheet.getRows();
	}
	
	/**
	 * 
	 * 
	 * @param column Column Number
	 * @param row Row Number
	 * @return Returns the cell value by taking column and row as parameter
	 */
	
	public static String readCell(int column, int row) {
		
		return workSheet.getCell(column, row).getContents();
	}
	

	public static void ColumnDictionary() {
		
		for (int col = 0; col < workSheet.getColumns(); col++) {
			
			dict.put(readCell(col, 0), col);
		}
		
	}
	
	/**
	 * 
	 * @param colName It takes Column Name as a parameter
	 * @return Returns Column values
	 */
	
	public static int getCell(String colName) {
		
		try {
			
			int value = 0;
			value = ((Integer)dict.get(colName)).intValue();
			return value;
			
		}catch (NullPointerException e) {
			
			return (0);
		}
		
		
	}
	
	
	
}