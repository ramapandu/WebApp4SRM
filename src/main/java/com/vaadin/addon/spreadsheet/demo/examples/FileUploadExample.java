package com.vaadin.addon.spreadsheet.demo.examples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.google.gwt.thirdparty.javascript.jscomp.parsing.parser.trees.GetAccessorTree;
import com.vaadin.addon.spreadsheet.ItemFilter;
import com.vaadin.addon.spreadsheet.PopupButton;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.addon.spreadsheet.SpreadsheetTable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class FileUploadExample implements SpreadsheetExample, Receiver,
        SucceededListener, ProgressListener, StartedListener, FinishedListener {

    private static final long serialVersionUID = -8658955787381939229L;

    private VerticalLayout layout;
    private Upload upload;
    private ProgressBar progressBar;
    private final long maxSize = 1000000;
    private Panel spreadsheetPanel;
    private ByteArrayOutputStream baos = null;

    public FileUploadExample() {
        layout = new VerticalLayout();
        layout.setSizeFull();
        initSpreadsheetPanel();
        layout.addComponent(createUploadLayout());
        layout.addComponent(spreadsheetPanel);
        layout.setExpandRatio(spreadsheetPanel, 1);
    }

    private HorizontalLayout createUploadLayout() {
        initUpload();
        initProgressBar();
        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        header.setMargin(true);
        header.addComponents(upload, progressBar);
        header.setComponentAlignment(progressBar, Alignment.MIDDLE_CENTER);
        header.setExpandRatio(progressBar, 1);
        return header;
    }

    private void initUpload() {
        upload = new Upload("Upload excel file", this);
        upload.setImmediate(true);
        upload.addStartedListener(this);
        upload.addProgressListener(this);
        upload.addFinishedListener(this);
        upload.addSucceededListener(this);
    }

    private void initProgressBar() {
        progressBar = new ProgressBar(0.0f);
        progressBar.setWidth("80%");
        progressBar.setVisible(false);
    }

    private void initSpreadsheetPanel() {
        Spreadsheet spreadsheet = new Spreadsheet();
        //enable editing
        spreadsheet.setActiveSheetProtected(null);
        
        
//        spreadsheet.getActiveSheet().groupColumn(2, 200);
//        com.vaadin.addon.spreadsheet.SpreadsheetFilterTable.class.
     // Create a table in the region  
//        CellRangeAddress range = new CellRangeAddress(1, 7, 1, 4);
//        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet, range);
//        com.vaadin.addon.spreadsheet.ItemFilter.
////        spreadsheet.registerTable(table);
        
        
        
        
        /////////////IMP----------------------------------
//     // Define the range
//        CellRangeAddress range =new CellRangeAddress(1, 7, 1, 4);
//        
//     // Create a table in the range
//        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,range);
        
        
        
        
        
        
//        spreadsheet.registerTable(table);
//        PopupButton button=table.getPopupButton(1);
//      ItemFilter ifilter=new  ItemFilter(range,spreadsheet,button,table);
        // Enable hiding each column
//        for (int col = range.getFirstColumn();col <= range.getLastColumn(); col++) {
//            final int c = col;
//            table.getPopupButton(col).addComponent(
//                new Button("Hide Column", e -> { // Java 8
//                    sheet.setColumnHidden(c, true);
//                    table.getPopupButton(c).closePopup();
//                }));
//        }      
        
        CellStyle backgroundColorStyle = spreadsheet.getWorkbook()
                .createCellStyle();
        backgroundColorStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
        Cell cell = spreadsheet.createCell(0, 0,
                "Click the upload button to choose and upload an excel file.");
        cell.setCellStyle(backgroundColorStyle);

        for (int i = 1; i <= 5; i++) {
            cell = spreadsheet.createCell(0, i, "");
            cell.setCellStyle(backgroundColorStyle);
        }

        spreadsheet.refreshCells(cell);

        spreadsheetPanel = new Panel();
        spreadsheetPanel.setSizeFull();
        spreadsheetPanel.setContent(spreadsheet);
    }

    @Override
    public Component getComponent() {
        return layout;
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        baos = new ByteArrayOutputStream();
        return baos;
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(baos.toByteArray());
            Spreadsheet spreadsheet = new Spreadsheet(bais);
            spreadsheetPanel.setContent(spreadsheet);
        } catch (IOException e) {
            Notification.show("Not a valid file!");
        } finally {
            baos = null;
            IOUtils.closeQuietly(bais);
        }
    }

    @Override
    public void updateProgress(long readBytes, long contentLength) {
        if (readBytes > maxSize || contentLength > maxSize) {
            upload.interruptUpload();
            Notification.show("File is to big. Maximum filesize: "
                    + maxSize / 1000 + "KB");
        }
        progressBar.setValue(new Float(readBytes / (float) contentLength));
    }

    @Override
    public void uploadFinished(FinishedEvent event) {
        progressBar.setVisible(false);
        layout.getUI().setPollInterval(-1);
    }

    @Override
    public void uploadStarted(StartedEvent event) {
        progressBar.setVisible(true);
        layout.getUI().setPollInterval(100);
    }

}
