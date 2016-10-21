package com.vaadin.addon.spreadsheet.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.util.CellRangeAddress;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.SpreadsheetFactory;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
@JavaScript("prettify.js")
@Theme("demo-theme")
@Title("Vaadin Spreadsheet Demo")
public class SpreadsheetDemoUI extends UI implements ValueChangeListener {
	private static final long serialVersionUID = 1L;

	Spreadsheet spreadsheet;
	HorizontalLayout topBar;
	 Button saveButton;
	 File testSheetFile;
	 File tempFile;
    static final Properties prop = new Properties();
    static {
        try {
            // load a properties file
            prop.load(SpreadsheetDemoUI.class
                    .getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SpreadsheetDemoUI.class, widgetset = "com.vaadin.addon.spreadsheet.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet implements Serializable {
    	private static final long serialVersionUID = 1L;
    }

    private Tree tree;
    private TabSheet tabSheet;
  

    public SpreadsheetDemoUI() {
        super();
        setSizeFull();
        SpreadsheetFactory.logMemoryUsage();
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void init(VaadinRequest request) {
    	
        VerticalLayout verticalLayout = new VerticalLayout();
verticalLayout.setSizeFull();
       
        setContent(verticalLayout);

        topBar=new HorizontalLayout();
        topBar.setHeight("5%");
        topBar.addStyleName("topbar");
        saveButton=new Button("SAVE");
        topBar.addComponent(saveButton);
        saveButton.addClickListener(new ClickListener() {
        	private static final long serialVersionUID = 1L;
            @Override
            public void buttonClick(ClickEvent event) {
            	try {
            		
            		 URL testSheetResource1 = this.getClass().getClassLoader()
            	                .getResource("testsheets/SAP-DEAL4.xlsx");
            			 System.out.println(testSheetResource1.toURI().toString());
            			tempFile = new File(testSheetResource1.toURI());
            	        
            	        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
            	        spreadsheet.write(tempOutputStream);
            	        tempOutputStream.flush();
            	        tempOutputStream.close();
            	        Spreadsheet sheet1 = new Spreadsheet(tempFile);
            	        copyFile(tempFile,testSheetFile);
            	        spreadsheet.setData(testSheetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
        
        
        tabSheet = new TabSheet();
        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                com.vaadin.ui.JavaScript
                        .eval("setTimeout(function(){prettyPrint();},300);");
            }
        });
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        //NEED TO CHECK STYLES
     verticalLayout.addComponent(topBar);
     try {
		tabSheet.addComponent(openSheet());
		
	} catch (URISyntaxException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
        verticalLayout.addComponent(tabSheet);

      
    }
    
    public Spreadsheet openSheet()
            throws URISyntaxException, IOException {
    	URL testSheetResource = this.getClass().getClassLoader()
                .getResource("testsheets/SAP-DEAL1.xlsx");
       testSheetFile = new File(testSheetResource.toURI());
        spreadsheet = new Spreadsheet(testSheetFile);
      getPopUpButtons();
        return spreadsheet;
    }

    private void getPopUpButtons() {
    	for(int i=0;i<spreadsheet.getNumberOfSheets();i++){
    		
    	}
    	 // Define the range
        CellRangeAddress range =new CellRangeAddress(1, 200, 0, 52);
        
     // Create a table in the range
        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,range);
        table.getPopupButtons();		
	}

	private static void copyFile(File source, File dest)
    		throws IOException {
    	FileUtils.copyFile(source, dest);
    }   


    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        open(value);
    }

    private void open(Object value) {
        tabSheet.removeAllComponents();
        if (value instanceof File) {
//            openFile((File) value);
            
        } 
    }
    
}
