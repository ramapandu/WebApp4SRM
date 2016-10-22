
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
    public void openAndSaveFile_emptyXLSXFile_openAndSaveWorks()
            throws URISyntaxException, IOException {
        URL testSheetResource = this.getClass().getClassLoader()
                .getResource("testsheets/sample.xlsx");
        File testSheetFIle = new File(testSheetResource.toURI());
        Spreadsheet sheet = new Spreadsheet(testSheetFIle);
        //Test
            sheet.getCell("A3").setCellValue("WRITE SUccess");
        File tempFile = File.createTempFile("resultEmptyFile", "xlsx");
        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
        sheet.write(tempOutputStream);
        tempOutputStream.close();
        tempFile.delete();

        // no exceptions, everything ok
    }



}