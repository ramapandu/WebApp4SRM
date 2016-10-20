package com.vaadin.addon.spreadsheet.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.reflections.Reflections;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.SpreadsheetFactory;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.addon.spreadsheet.demo.examples.SpreadsheetExample;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
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
    public static class Servlet extends VaadinServlet {
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
            @Override
            public void buttonClick(ClickEvent event) {
            	 

            	        File tempFile;
						try {
							tempFile = File.createTempFile("resultEmptyFile", "xlsx");
							
							FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
							spreadsheet.write(tempOutputStream);
//							 tempOutputStream.close();
//		            	        tempFile.delete();
						} catch (IOException e1) {
							e1.printStackTrace();
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
                .getResource("SAP-DEAL.xlsx");
        testSheetFile = new File(testSheetResource.toURI());
        spreadsheet = new Spreadsheet(testSheetFile);

        // no exceptions, everything ok
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
