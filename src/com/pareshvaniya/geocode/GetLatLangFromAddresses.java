package com.pareshvaniya.geocode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetLatLangFromAddresses {

	public static void readAndwriteXLSXFile(){

		String latLongs[] = new String[2];
		
		int current_Row = 0;
		
		try {

			String excelFileName = "sample/GetLatLangFromAddress.xlsx";

			InputStream ExcelFileToRead = new FileInputStream(excelFileName);
			XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

			XSSFSheet sheet = wb.getSheetAt(0);

			int noofRows = sheet.getPhysicalNumberOfRows();

			GeoCodeAPIReq geoCodeAPI = new GeoCodeAPIReq();

			// iterating r number of rows
			for (int r = 1; r < noofRows; r++) {
				
				current_Row = r;
				
				XSSFRow row = sheet.getRow(r);

				XSSFCell addressCell = row.getCell(0);
				XSSFCell cityCell = row.getCell(1);
				XSSFCell stateCell = row.getCell(2);
				XSSFCell pincodeCell = row.getCell(3);

				String pincode = String.valueOf((int) pincodeCell.getNumericCellValue());
				latLongs = geoCodeAPI.getLatLongPositions(addressCell.getStringCellValue() + "," + pincode, "", "");

				if (latLongs.length == 0 || latLongs[0].isEmpty() || latLongs[1].isEmpty()) {

					latLongs = geoCodeAPI.getLatLongPositions(
							stateCell.getStringCellValue() + "," + cityCell.getStringCellValue() + "," + pincode,
							pincode, cityCell.getStringCellValue());

				}

				if (latLongs.length != 0) {
					XSSFCell latitudecell = row.createCell(4);

					latitudecell.setCellValue(latLongs[0]);

					XSSFCell longtiutdecell = row.createCell(5);

					longtiutdecell.setCellValue(latLongs[1]);
				}

			}

			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			// write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception ex) {
			System.err.println("Exception occured while getting LatLang in this row : "+current_Row+ " : "+ex.getMessage());

		}
	}

	public static void main(String[] args) throws Exception {
		readAndwriteXLSXFile();
		System.out.println("Latitude and Longitude is written in Given Excel File, Please check.");

	}

}