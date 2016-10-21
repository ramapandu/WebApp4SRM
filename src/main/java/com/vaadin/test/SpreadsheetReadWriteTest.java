package com.vaadin.test;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.vaadin.addon.spreadsheet.Spreadsheet;

/*
 * Tests are performed with pure POI and Spreadsheet to find differences and bugs
 */
public class SpreadsheetReadWriteTest {

    @Test
    public void openAndSaveFileWithPOI_emptyXLSXFile_openAndSaveWorks()
            throws URISyntaxException, IOException, InvalidFormatException {
        URL testSheetResource = this.getClass().getClassLoader()
                .getResource("test_sheets/empty.xlsx");
        File testSheetFIle = new File(testSheetResource.toURI());

        FileInputStream fis = new FileInputStream(testSheetFIle);
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(fis);
        fis.close();

        File tempFile = File.createTempFile("resultEmptyFile", "xlsx");
        FileOutputStream fos = new FileOutputStream(tempFile);
        workbook.write(fos);
        fos.close();
        tempFile.delete();
        // no exceptions, everything ok
    }

    @Test
    public void openAndSaveFile_emptyXLSXFile_openAndSaveWorks()
            throws URISyntaxException, IOException {
    	//----------
    	Spreadsheet spreadsheet;
    	URL testSheetResource = this.getClass().getClassLoader()
                .getResource("testsheets/SAP-DEAL1.xlsx");
        File testSheetFile = new File(testSheetResource.toURI());
        System.out.println(testSheetResource.toURI().toString());
        spreadsheet = new Spreadsheet(testSheetFile);
        spreadsheet.getCell("A4").setCellValue("SAVE SUCCESS222");
        // no exceptions, everything ok
        //-----------
        
        URL testSheetResource1 = this.getClass().getClassLoader()
                .getResource("testsheets/SAP-DEAL4.xlsx");
		 System.out.println(testSheetResource1.toURI().toString());
		File tempFile = new File(testSheetResource1.toURI());
        
        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
        spreadsheet.write(tempOutputStream);
        tempOutputStream.close();
        Spreadsheet sheet1 = new Spreadsheet(tempFile);
        System.out.println(sheet1.getCell("A4"));
//        tempFile.delete();
    	//-------
//        URL testSheetResource = this.getClass().getClassLoader()
//                .getResource("SAP-DEAl3.xlsx");
//        File testSheetFIle = new File(testSheetResource.toURI());
//        Spreadsheet sheet = new Spreadsheet(testSheetFIle);
//        sheet.getCell("A4").setCellValue("TEST SUCCESS");
////        System.out.println(sheet.getCell("A4"));
//        File tempFile = new File("SAP-DEAl3.xlsx");
//        
//        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
//        sheet.write(tempOutputStream);
//        tempOutputStream.close();
//        Spreadsheet sheet1 = new Spreadsheet(tempFile);
//        System.out.println(sheet1.getCell("A4"));
//        tempFile.delete();

        // no exceptions, everything ok
    }


    @Test
    public void openAndSaveFile_emptyXLSXFile_FileDoesNotContainAdditionalDrawing()
            throws URISyntaxException, IOException {
        URL testSheetResource = this.getClass().getClassLoader()
                .getResource("test_sheets/empty.xlsx");
        File testSheetFIle = new File(testSheetResource.toURI());
        Spreadsheet sheet = new Spreadsheet(testSheetFIle);

        File tempFile = File.createTempFile("resultEmptyFile", "xlsx");
        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
        sheet.write(tempOutputStream);
        tempOutputStream.close();

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(
                tempFile));

        ZipEntry entry = zipInputStream.getNextEntry();
        do {
            String entryName = entry.getName();
            assertFalse("Empty XLSX contains drawing after import/export: "
                    + entryName, entryName.contains("drawing"));
            entry = zipInputStream.getNextEntry();
        } while (entry != null);

        zipInputStream.close();
        tempFile.delete();
    }

    @Test
    public void openAndSaveFileWithPOI_emptyXLSXFile_FileDoesNotContainAdditionalDrawing()
            throws URISyntaxException, IOException, InvalidFormatException {
        URL testSheetResource = this.getClass().getClassLoader()
                .getResource("test_sheets/empty.xlsx");
        File testSheetFIle = new File(testSheetResource.toURI());

        FileInputStream fis = new FileInputStream(testSheetFIle);
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(fis);
        fis.close();

        File tempFile = File.createTempFile("resultEmptyFile", "xlsx");
        FileOutputStream fos = new FileOutputStream(tempFile);
        workbook.write(fos);
        fos.close();

        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(
                tempFile));

        ZipEntry entry = zipInputStream.getNextEntry();
        do {
            String entryName = entry.getName();
            assertFalse("Empty XLSX contains drawing after import/export: "
                    + entryName, entryName.contains("drawing"));
            entry = zipInputStream.getNextEntry();
        } while (entry != null);

        zipInputStream.close();
        tempFile.delete();
    }
}