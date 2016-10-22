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
import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeEvent;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeListener;
import com.vaadin.addon.spreadsheet.SpreadsheetFactory;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.client.metadata.OnStateChangeMethod;
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

	 private SheetChangeListener selectedSheetChangeListener;
	 VerticalLayout rootLayout;
	Spreadsheet spreadsheet;
	HorizontalLayout topBar,sheetLayout;
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
    	
    	rootLayout = new VerticalLayout();
//    	rootLayout.setSizeFull();
        setContent(rootLayout);
        CreateUI();

    }
    
    private void CreateUI() {
    	rootLayout.addComponent(getTopBar());
    	rootLayout.addComponent(getSheetLayout());
	}
    
    public HorizontalLayout getSheetLayout(){
    	sheetLayout=new HorizontalLayout();
    	sheetLayout.setSizeFull();
    	sheetLayout.setHeight("100%");
    	sheetLayout.addComponent(getTabSheet());
    	sheetLayout.addStyleName("sheetlayout");
    	return sheetLayout;
    }

	private HorizontalLayout getTopBar() {
	      topBar=new HorizontalLayout();
	      topBar.setPrimaryStyleName("topbar");
	      topBar.addComponent(getSaveButton());
	      return topBar;
	}

	private TabSheet getTabSheet() {
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
	//   verticalLayout.addComponent(topBar);
	   try {
			tabSheet.addComponent(openSheet());
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
		return tabSheet;
		}		
	

	private Button getSaveButton() {
		saveButton=new Button("SAVE");
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
      return saveButton;
	}

	public Spreadsheet openSheet()
            throws URISyntaxException, IOException {
    	URL testSheetResource = this.getClass().getClassLoader()
                .getResource("testsheets/SAP-DEAL1.xlsx");
       testSheetFile = new File(testSheetResource.toURI());
        spreadsheet = new Spreadsheet(testSheetFile);
//      getPopUpButtons();
        getPopUpButtonsForAllSheets();
//        spreadsheet.addListener(new SheetChangeListener(selectedSheetChangeListener));
//        selectedSheetChangeListener = new SheetChangeListener() {
//            @Override
//            public void onSheetChange(SheetChangeEvent event) {
//           getPopUpButtons();     
//            }
//	   };
        return spreadsheet;
    }

    private void getPopUpButtons() {
    	 // Define the range
        CellRangeAddress range =new CellRangeAddress(1, 200, 0, 52);
        
     // Create a table in the range
        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,range);
//        table.getPopupButtons();
//        table.getSheet().
	}

	private static void copyFile(File source, File dest)
    		throws IOException {
    	FileUtils.copyFile(source, dest);
    }   
public void getPopUpButtonsForAllSheets(){
	for(int i=0;i<spreadsheet.getNumberOfSheets();i++){
//		Sheet s=(Sheet) spreadsheet.getWorkbook().getSheetAt(i);
		spreadsheet.getWorkbook().getSheetAt(i);
		getPopUpButtons();
	}
}

    @Override
    public void valueChange(ValueChangeEvent event) {
    	spreadsheet.addSheetChangeListener(selectedSheetChangeListener);
        selectedSheetChangeListener = new SheetChangeListener() {
            @Override
            public void onSheetChange(SheetChangeEvent event) {
           getPopUpButtons();     
            }
	   };
        Object value = event.getProperty().getValue();
        open(value);
        getPopUpButtons();
    }
    
   

    private void open(Object value) {
        tabSheet.removeAllComponents();
        if (value instanceof File) {
//            openFile((File) value);
            
        } 
    }
    
}
