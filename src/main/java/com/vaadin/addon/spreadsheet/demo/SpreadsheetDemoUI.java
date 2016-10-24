package com.vaadin.addon.spreadsheet.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;



@JavaScript("prettify.js")
@Theme("demo-theme")
@Title("Vaadin Spreadsheet Demo")
public class SpreadsheetDemoUI extends UI implements ValueChangeListener,Serializable {
	private static final long serialVersionUID = 1L;

	 CellRangeAddress range;
	 VerticalLayout rootLayout;
	Spreadsheet spreadsheet;
	HorizontalLayout topBar,sheetLayout;
	 Button editButton,saveButton,downlaodButton;
	 File testSheetFile;
	 File tempFile;
//    static final Properties prop = new Properties();
//    static {
//        try {
//            // load a properties file
//            prop.load(SpreadsheetDemoUI.class
//                    .getResourceAsStream("config.properties"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SpreadsheetDemoUI.class, widgetset = "com.vaadin.addon.spreadsheet.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet implements Serializable {

	
	private static final long serialVersionUID = -7814001723142929438L;
    	
    }

//    private Tree tree;
    private TabSheet tabSheet;
  

    public SpreadsheetDemoUI() {
    	
        super();

        setSizeFull();
        SpreadsheetFactory.logMemoryUsage();
    }

    @Override
    protected void init(VaadinRequest request) {
       
    	 VaadinSession.getCurrent().getSession().setMaxInactiveInterval(300); 
    	 //ERROR HANDLING
    	 UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            
			private static final long serialVersionUID = 1L;

			@Override
             public void error(com.vaadin.server.ErrorEvent event) {
                 for (Throwable t = event.getThrowable(); t != null;
                      t = t.getCause())
                     if (t.getCause() == null) {
					}

                 
                 // Do the default error handling (optional)
                 doDefault(event);
             }
         });
    	 
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
	      topBar.addComponent(getEditButton());
	      topBar.addComponent(getSaveButton());
	      topBar.addComponent(getDownloadButton());
	      return topBar;
	}

	private TabSheet getTabSheet() {
		tabSheet = new TabSheet();
		
	      tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
	         
			private static final long serialVersionUID = 1L;

			@Override
	          public void selectedTabChange(SelectedTabChangeEvent event) {
	              com.vaadin.ui.JavaScript
	                      .eval("setTimeout(function(){prettyPrint();},300);");
	          }
	      });
	      tabSheet.setSizeFull();
	      	      tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
	   try {
			tabSheet.addComponent(openSheet());
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   
		return tabSheet;
		}		
	

	private Button getDownloadButton() {
		downlaodButton=new Button("DOWNLOAD");
		downlaodButton.addStyleName("topbarbuttons");
		downlaodButton.addClickListener(new ClickListener() {
      	private static final long serialVersionUID = 1L;
          @Override
          public void buttonClick(ClickEvent event) {
        	  FileResource resource = new FileResource(testSheetFile);
              FileDownloader fileDownloader = new FileDownloader(
                      resource);
              fileDownloader.extend(downlaodButton);
          }
      });	
      return downlaodButton;
	}
	
	private Button getEditButton() {
		editButton=new Button("EDIT");
		editButton.addStyleName("topbarbuttons");
		editButton.addClickListener(new ClickListener() {
      	private static final long serialVersionUID = 1L;
          @Override
          public void buttonClick(ClickEvent event) {
          enableEdit();
          }
      });	
      return editButton;
	}

	private Button getSaveButton() {
		saveButton=new Button("SAVE");
		saveButton.addStyleName("topbarbuttons");
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
//          	        tempOutputStream.flush();
          	        tempOutputStream.close();
//          	        Spreadsheet sheet1 = new Spreadsheet(tempFile);
          	        copyFile(tempFile,testSheetFile);
          	        spreadsheet.setData(testSheetFile);
//          	      spreadsheet.setData(sheet1);
          	   
//          	        spreadsheet.reload();
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
      getPopUpButtonsForSheet(spreadsheet.getActiveSheet());
//        getPopUpButtonsForAllSheets();
        
        spreadsheet.addSheetChangeListener(new SheetChangeListener(){
        
					private static final long serialVersionUID = 1L;

		@Override
		public void onSheetChange(SheetChangeEvent event) {
getPopUpButtonsForSheet(spreadsheet.getActiveSheet());		

		}
    });
        return spreadsheet;
    }

    private void getPopUpButtonsForSheet(Sheet sheet)throws NullPointerException,ArrayIndexOutOfBoundsException {
    	try{
//    	 Row r = sheet.getRow(sheet.getFirstRowNum());
//    	 int lastColumnNum=0;
//    	 lastColumnNum=  r.getLastCellNum();
    	//OR Cell lastCellInRow = row.getCell(row.getLastCellNum() - 1);
    	//------------------
//    	int numberOfCells = 0;
//        Iterator rowIterator = sheet.rowIterator();
//        /**
//         * Escape the header row *
//         */
//        if (rowIterator.hasNext())
//        {
//            Row headerRow = (Row) rowIterator.next();
//            //get the number of cells in the header row
//            numberOfCells = headerRow.getPhysicalNumberOfCells();
//        }
//        System.out.println(sheet.getFirstRowNum()+""+sheet.getLastRowNum()+""+1+""+numberOfCells);
//    	 CellRangeAddress range =new CellRangeAddress(sheet.getFirstRowNum(),sheet.getLastRowNum(),1,numberOfCells); 
        //--------------
    	
    	 // Define the range
        range =new CellRangeAddress(0,300,0,52);  
        System.out.println(sheet.getFirstRowNum()+""+sheet.getLastRowNum()+""+1);
     // Create a table in the range
        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,sheet,range);
        table.getPopupButtons();
//        table.getSheet().
        
        
        
    	}
    	catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	private static void copyFile(File source, File dest)
    		throws IOException {
    	FileUtils.copyFile(source, dest);
    }   
public void getPopUpButtonsForAllSheets(){
	for(int i=0;i<spreadsheet.getNumberOfSheets();i++){
//		Sheet s=(Sheet) spreadsheet.getWorkbook().getSheetAt(i);
		Sheet s=spreadsheet.getWorkbook().getSheetAt(i);
		getPopUpButtonsForSheet(s);
	}
}

    @Override
    public void valueChange(ValueChangeEvent event) {
//    	spreadsheet.addSheetChangeListener(selectedSheetChangeListener);
//        selectedSheetChangeListener = new SheetChangeListener() {
//            @Override
//            public void onSheetChange(SheetChangeEvent event) {
////           getPopUpButtons();     
//            getPopUpButtons(spreadsheet.getActiveSheet());
//            }
//	   };
//        Object value = event.getProperty().getValue();
//        open(value);
////        getPopUpButtons();
    }
    
   public void enableEdit(){
//	   if(editButton.isEnabled())
// spreadsheet.setActiveSheetProtected(null);
   }

//    private void open(Object value) {
//        tabSheet.removeAllComponents();
//        if (value instanceof File) {
////            openFile((File) value);
//            
//        } 
//    }
    
}
