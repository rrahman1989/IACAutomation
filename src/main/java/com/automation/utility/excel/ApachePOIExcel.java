package com.automation.utility.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.automation.utility.logger.LoggerClass;




/**
 * 
 * Apache POI Excel class to take care of some of the Excel Utility
 * @author reazur.rahman
 *
 */

public class ApachePOIExcel {
	
	public LoggerClass log;
	
	
	/**
	 * Reads and Compares Excel File
	 * @param firstFilePath First Excel file Path
	 * @param secondFilePath Second Excel file Path
	 */
	
	public void readAndCompareExcel(String firstFilePath, String secondFilePath, String resultFileName) {
		
		log = new LoggerClass();
        try {

            ArrayList<Comparable> arrayOne = new ArrayList<Comparable>();
            ArrayList<Comparable> arrayTwo= new ArrayList<Comparable>();
            ArrayList<Object> arrayThree = new ArrayList<Object>();

            FileInputStream file1 = new FileInputStream(new File(firstFilePath));

            FileInputStream file2 = new FileInputStream(new File(secondFilePath));

            // Get the workbook instance for XLS file
            Workbook workbook1 = new XSSFWorkbook(file1);
            Workbook workbook2 = new XSSFWorkbook(file2);

            // Get first sheet from the workbook
            Sheet sheet1 = workbook1.getSheetAt(0);
            Sheet sheet2 = workbook2.getSheetAt(0);

            
            // Compare sheets

            // Get iterator to all the rows in current sheet1
            Iterator<Row> rowIterator1= sheet1.iterator();
            Iterator<Row> rowIterator2 = sheet2.iterator();
            

            while (rowIterator1.hasNext()) {
                Row row = rowIterator1.next();
                // For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    
                    if (cell.getCellTypeEnum() == CellType.STRING) {
                    	log.info(ApachePOIExcel.class.getName(), cell.getStringCellValue() + "--");
                        arrayOne.add(cell.getStringCellValue());
                       
                    } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    	log.info(ApachePOIExcel.class.getName(), cell.getNumericCellValue() + "--");
                        arrayOne.add(cell.getNumericCellValue());
                    } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                    	log.info(ApachePOIExcel.class.getName(), cell.getBooleanCellValue() + "--");
                    	arrayOne.add(cell.getBooleanCellValue());
                    	
                    }
                    // This is for read only one column from excel
                    /*
                    if (cell.getColumnIndex() == 0) {
                        // Check the cell type and format accordingly
                    	
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue());
                            arr1.add(cell.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_STRING:
                            arr1.add(cell.getStringCellValue());
                            System.out.print(cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            arr1.add(cell.getBooleanCellValue());
                            System.out.print(cell.getBooleanCellValue());
                            break;
                           
                    	
                    	
                        }
 */
                //}

                }

              
            }

            file1.close();

            System.out.println("-----------------------------------");
            // For retrive the second excel data
            while (rowIterator2.hasNext()) {
                Row row1 = rowIterator2.next();
                // For each row, iterate through all the columns
                Iterator<Cell> cellIterator1 = row1.cellIterator();

                while (cellIterator1.hasNext()) {

                    Cell cell1 = cellIterator1.next();
                    // Check the cell type and format accordingly
                    
                    
                    if (cell1.getCellTypeEnum() == CellType.STRING) {
                    	log.info(ApachePOIExcel.class.getName(), cell1.getStringCellValue() + "--");
                        arrayTwo.add(cell1.getStringCellValue());
                       
                    } else if (cell1.getCellTypeEnum() == CellType.NUMERIC) {
                    	log.info(ApachePOIExcel.class.getName(), cell1.getNumericCellValue() + "--");
                        arrayTwo.add(cell1.getNumericCellValue());
                    } else if (cell1.getCellTypeEnum() == CellType.BOOLEAN) {
                    	log.info(ApachePOIExcel.class.getName(), cell1.getBooleanCellValue() + "--");
                    	arrayTwo.add(cell1.getBooleanCellValue());
                    	
                    }
                    

                    
                }

                System.out.println("");
            }
            log.info(ApachePOIExcel.class.getName(), new File(firstFilePath).getName().toString() +" "+ arrayOne.size());
            log.info(ApachePOIExcel.class.getName(), new File(secondFilePath).getName().toString() +" "+ arrayTwo.size());


            // compare two arrays
            for (Object process : arrayOne) {
                if (!arrayTwo.contains(process)) {
                    arrayThree.add(process);
                }
            }
            log.info(ApachePOIExcel.class.getName(), "Array Three list values - = - = + " + arrayThree);
            writeStudentsListToExcel(arrayThree, resultFileName);

            // closing the files
            file1.close();
            file2.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	
	/**
	 * Writes to the result file if there is data difference
	 * @param arr3 Array three to compare the value of the two files
	 */
		
    private static void writeStudentsListToExcel(ArrayList arr3, String resultFileName) {

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(
                    "C:\\Users\\reazur.rahman\\Desktop\\Test_output\\"+resultFileName);

            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet spreadSheet = workBook.createSheet("Missing Value");
            XSSFRow row;
            XSSFCell cell;
            // System.out.println("array size is :: "+minusArray.size());
            int cellnumber = 0;
            for (int i1 = 0; i1 < arr3.size(); i1++) {
                row = spreadSheet.createRow(i1);
                cell = row.createCell(cellnumber);
              //   System.out.print(cell.getCellStyle());

                cell.setCellValue(arr3.get(i1).toString().trim());
                
            }
            workBook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // end -write into new file
}

