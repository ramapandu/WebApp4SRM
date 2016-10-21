package com.vaadin.addon.spreadsheet.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;

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

//        VerticalLayout content = new VerticalLayout();
//        content.setSpacing(true);

        
//        tree = new Tree();
//        tree.setImmediate(true);
//        tree.setHtmlContentAllowed(true);
////        tree.setContainerDataSource(getContainer());
//        tree.setItemCaptionPropertyId("displayName");
//        tree.setNullSelectionAllowed(false);
//        tree.setWidth("100%");
//        tree.addValueChangeListener(this);
//        for (Object itemId : tree.rootItemIds()) {
//            tree.expandItem(itemId);
//        }

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
            		//TEST CASE---------------------------------
//            		URL testSheetResource = this.getClass().getClassLoader()
//                            .getResource("testsheets/SAP-DEAL.xlsx");
//                   File testSheetFile1 = new File(testSheetResource.toURI());
//                    System.out.println(testSheetResource.toURI().toString());
//                    Spreadsheet sheet = new Spreadsheet(testSheetFile1);
//
//                    File tempFile = File.createTempFile("resultEmptyFile", "xlsx");
//                    FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
//                    sheet.write(tempOutputStream);
//                    tempOutputStream.close();
//                    tempFile.delete();
            		//---------------------------------
//            		URL testSheetResource1 = this.getClass().getClassLoader()
//                            .getResource("testsheets/SAP-DEAL4.xlsx");
//            		 System.out.println(testSheetResource1.toURI().toString());
//            		File tempFile = new File(testSheetResource1.toURI());
//                    
//                    FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
//                    spreadsheet.write(tempOutputStream);
//                    tempOutputStream.close();
//                    Spreadsheet sheet1 = new Spreadsheet(tempFile);
//                    System.out.println(sheet1.getCell("A4"));
//                    tempFile.delete();
            		//---------------------TEST2---begins
            		 URL testSheetResource1 = this.getClass().getClassLoader()
            	                .getResource("testsheets/SAP-DEAL4.xlsx");
            			 System.out.println(testSheetResource1.toURI().toString());
            			File tempFile = new File(testSheetResource1.toURI());
            	        
            	        FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
            	        spreadsheet.write(tempOutputStream);
            	        tempOutputStream.flush();
            	        tempOutputStream.close();
            	        Spreadsheet sheet1 = new Spreadsheet(tempFile);
//            	        System.out.println(sheet1.getCell("A4"));
            		//////-----------
            	        
				} catch (Exception e) {
					// TODO: handle exception
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
        File testSheetFile = new File(testSheetResource.toURI());
        System.out.println(testSheetResource.toURI().toString());
        spreadsheet = new Spreadsheet(testSheetFile);
        spreadsheet.getCell("A4").setCellValue("SAVE SUCCESS333");
        return spreadsheet;
    }

   

//	static String getVersion() {
//        return (String) prop.get("spreadsheet.version");
//    }



    @Override
    public void valueChange(ValueChangeEvent event) {
        Object value = event.getProperty().getValue();
        open(value);
    }

    private void open(Object value) {
        tabSheet.removeAllComponents();
        if (value instanceof File) {
            openFile((File) value);
            
        } 
    }

//    private void openExample(Class value) {
//        try {
//            SpreadsheetExample example = (SpreadsheetExample) value
//                    .newInstance();
//            tabSheet.addTab(example.getComponent(), "Demo");
//           
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void openFile(File file) {
    	 // opens the specified file as a spreadsheet
            spreadsheet = null;
            try {
                spreadsheet = new Spreadsheet(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            
     // Define the range
        CellRangeAddress range =new CellRangeAddress(1, 200, 0, 52);
        
     // Create a table in the range
        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,range);
        table.getPopupButtons();
        tabSheet.addTab(spreadsheet, "Demo");
       
        
    }

//    private void addResourceTab(Class clazz, String resourceName,
//            String tabName) {
//        try {
//            InputStream resourceAsStream = clazz
//                    .getResourceAsStream(resourceName);
//            String code = IOUtils.toString(resourceAsStream);
//
//            Panel p = getSourcePanel(code);
//
//            tabSheet.addTab(p, tabName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private Panel getSourcePanel(String code) {
//        Panel p = new Panel();
//        p.setWidth("100%");
//        p.setStyleName(ValoTheme.PANEL_BORDERLESS);
//        code = code.replace("&", "&amp;").replace("<", "&lt;").replace(">",
//                "&gt;");
//        Label c = new Label("<pre class='prettyprint'>" + code + "</pre>");
//        c.setContentMode(ContentMode.HTML);
//        c.setSizeUndefined();
//        p.setContent(c);
//        return p;
//    }

    
}
