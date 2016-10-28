package com.vaadin.addon.spreadsheet.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeEvent;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeListener;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SheetView extends CustomComponent implements View  {
	
	 public static final String NAME = "sheet";
	private static final long serialVersionUID = 6714096000861957459L;
		Button logoutButton;
	 
		CellRangeAddress range;
		 VerticalLayout rootLayout;
		 Spreadsheet spreadsheet;
		HorizontalLayout topBar,sheetLayout;
		 Button editButton;
		Button saveButton;
		Button downlaodButton;
		 File testSheetFile;
		 private TabSheet tabSheet;

		 VaadinSession ses;
		 FileInputStream fis;
		
	@SuppressWarnings({ "unused" })
	public	SheetView(){
//		ses= VaadinSession.getCurrent();
		setSizeFull();
	   	 
    	rootLayout = new VerticalLayout();
        setCompositionRoot(rootLayout);
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
		      topBar.addComponent(getLogoutButton());
//			    topBar.setComponentAlignment(logoutButton, Alignment.BOTTOM_RIGHT);	
		      return topBar;
		}

		private TabSheet getTabSheet() {
			tabSheet = new TabSheet();
			
//		      tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
//		         
//				
//					private static final long serialVersionUID = -1698363226401049948L;
	//
//				@Override
//		          public void selectedTabChange(SelectedTabChangeEvent event) {
//		              com.vaadin.ui.JavaScript
//		                      .eval("setTimeout(function(){prettyPrint();},300);");
//		          }
//		      });
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
		
       private Button getLogoutButton(){
	 logoutButton = new Button("Logout", new Button.ClickListener() {
	        
			private static final long serialVersionUID = 2718672708618255597L;

			@Override
	         public void buttonClick(ClickEvent event) {

	             ((SpreadsheetDemoUI) UI.getCurrent()).setLoggedInUser(null);
	             logoutButton.setCaption("Logout");
	         }
	     });
	 logoutButton.setStyleName("logoutbutton");
	 return logoutButton;    
}
		private Button getDownloadButton() {
			downlaodButton=new Button("DOWNLOAD");
			downlaodButton.addStyleName("topbarbuttons");
			downlaodButton.addClickListener(new ClickListener() {
	      	
	         
				private static final long serialVersionUID = -8158301975694183254L;

			@Override
	          public void buttonClick(ClickEvent event) {
				File file=new File("C:/Users/rampa/Desktop/testsheets/test.xlsx");
	        	  FileResource resource = new FileResource(file);
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
	      	
				private static final long serialVersionUID = 483869047651452271L;

			@Override
	          public void buttonClick(ClickEvent event) {
	          enableEdit();
	          }
	      });	
	      return editButton;
		}

		public Button getSaveButton() {
			saveButton=new Button("SAVE");
			saveButton.addStyleName("topbarbuttons");
	      saveButton.addClickListener(new ClickListener() {
	      	
	         private static final long serialVersionUID = 1792550832130526578L;

			@Override
	          public void buttonClick(ClickEvent event) {
	          	try {
	          		File	tempFile = new File("C:/Users/rampa/Desktop/testsheets/test.xlsx");
	          		FileOutputStream fos = new FileOutputStream(tempFile);
	          		spreadsheet.write(fos);
	          		fos.close();
//	          		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	          		spreadsheet.write(bos);
//	          		byte[] data = bos.toByteArray();
//	          		bos.close();
	//
//	        		 URL testSheetResource1 = this.getClass().getClassLoader()
//	              .getResource("testsheets/SAP-DEAL4.xlsx");
//		   File	tempFile = new File(testSheetResource1.toURI());
////		   tempFile.
//		   FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
//		   tempOutputStream.write(data);
//		   tempOutputStream.flush();
//		   tempOutputStream.close();
	          		//------------FINAL SAVE WORKING COPY
//	     		 URL testSheetResource1 = this.getClass().getClassLoader()
//	              .getResource("testsheets/SAP-DEAL1.xlsx");
//		   File	tempFile = new File(testSheetResource1.toURI());
//			
////	          		File	tempFile1= File.createTempFile("testTemp", ".xlsx");
//			
//			FileOutputStream tempOutputStream = new FileOutputStream(tempFile);
//			spreadsheet.write(tempOutputStream);
//	      tempOutputStream.flush();
//	      tempOutputStream.close(); 
//	      copyFile(tempFile,testSheetFile);     //THIS CODE CAUSES SESSION EXPIRED PROBLEM
//	      tempFile.delete();
	      ///////////////////////////////////77
//	      setSession(getSession());
//	      spreadsheet.reload();
//	    spreadsheet.read(tempFile);
//	      getAppUI().getPage().reload();
	      //-----------------------------FINAL
	     
//	      FileOutputStream fos= new FileOutputStream("myfile");
//	      ObjectOutputStream oos= new ObjectOutputStream(fos);
//	      oos.writeObject(al);
//	      oos.close();
//	      fos.close();
	          		

//	          		-------------------------------------
//	          	        Spreadsheet sheet1 = new Spreadsheet(tempFile);
//	          	      spreadsheet= new Spreadsheet(tempFile);
//	          	        copyFile(tempFile,testSheetFile);
//	          	        spreadsheet.setData(testSheetFile);
//	          	      spreadsheet.setData(sheet1);
	          	   
					} catch (Exception e) {
						e.printStackTrace();
					}
	          
	          }
	      });	
	      return saveButton;
		}
		
		public Spreadsheet openSheet()
	            throws URISyntaxException, IOException {
	    	fis=new FileInputStream("C:/Users/rampa/Desktop/testsheets/test.xlsx");
	        spreadsheet = new Spreadsheet(fis);
	        fis.close();
//	      getPopUpButtonsForSheet(spreadsheet.getActiveSheet());///////////TEST-----------
//	        //TEST-BEGIN ----------
//	        URL testSheetResource = this.getClass().getClassLoader()
//	                .getResource("testsheets/SAP-DEAL1.xlsx");
//	       testSheetFile = new File(testSheetResource.toURI());
//	        spreadsheet = new Spreadsheet(testSheetFile);
	      getPopUpButtonsForSheet(spreadsheet.getActiveSheet());
	        spreadsheet.addSheetChangeListener(new SheetChangeListener(){

				private static final long serialVersionUID = -5585430837302587763L;

			@Override
			public void onSheetChange(SheetChangeEvent event) {
	       getPopUpButtonsForSheet(spreadsheet.getActiveSheet());		

			}
	    });
	      //TEST-END------------
	        return spreadsheet;
	    }

	    public void getPopUpButtonsForSheet(Sheet sheet) throws NullPointerException,ArrayIndexOutOfBoundsException  {
	    	
	    	
	    	try{
//	    	 Row r = sheet.getRow(sheet.getFirstRowNum());
//	    	 int lastColumnNum=0;
//	    	 lastColumnNum=  r.getLastCellNum();
	    	//OR Cell lastCellInRow = row.getCell(row.getLastCellNum() - 1);
	    	//------------------
//	    	int numberOfCells = 0;
//	        Iterator rowIterator = sheet.rowIterator();
//	        /**
//	         * Escape the header row *
//	         */
//	        if (rowIterator.hasNext())
//	        {
//	            Row headerRow = (Row) rowIterator.next();
//	            //get the number of cells in the header row
//	            numberOfCells = headerRow.getPhysicalNumberOfCells();
//	        }
//	        System.out.println(sheet.getFirstRowNum()+""+sheet.getLastRowNum()+""+1+""+numberOfCells);
//	    	 CellRangeAddress range =new CellRangeAddress(sheet.getFirstRowNum(),sheet.getLastRowNum(),1,numberOfCells); 
	        //--------------
	    	
	    	 // Define the range
	    		CellRangeAddress range =new CellRangeAddress(0,10,0,10);  
	        System.out.println(sheet.getFirstRowNum()+""+sheet.getLastRowNum()+""+1);
	     // Create a table in the range
	        SpreadsheetFilterTable table = new SpreadsheetFilterTable(spreadsheet,sheet,range);
	        table.getPopupButtons();
//	        table.getSheet().
	        
	        
	        
	    	}
	    	catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		 
	public void getPopUpButtonsForAllSheets(){
		for(int i=0;i<spreadsheet.getNumberOfSheets();i++){
			Sheet s=spreadsheet.getWorkbook().getSheetAt(i);
			getPopUpButtonsForSheet(s);
		}
	}

//	    @Override
//	    public void valueChange(ValueChangeEvent event) {
////	    	spreadsheet.addSheetChangeListener(selectedSheetChangeListener);
////	        selectedSheetChangeListener = new SheetChangeListener() {
////	            @Override
////	            public void onSheetChange(SheetChangeEvent event) {
//////	           getPopUpButtons();     
////	            getPopUpButtons(spreadsheet.getActiveSheet());
////	            }
////		   };
////	        Object value = event.getProperty().getValue();
////	        open(value);
//////	        getPopUpButtons();
//	    }
	    
	   public void enableEdit(){
//		   if(editButton.isEnabled())
	// spreadsheet.setActiveSheetProtected(null);
	   }

	//@Override
	//public void valueChange(ValueChangeEvent event) {
//		// TODO Auto-generated method stub
	//	
	//}

//	    private void open(Object value) {
//	        tabSheet.removeAllComponents();
//	        if (value instanceof File) {
////	            openFile((File) value);
//	            
//	        } 
//	    }
	
	
	
	
	
	private Component getUserProfile(){
		HorizontalLayout profileLayout=new HorizontalLayout();
		profileLayout.setStyleName("contentbar");
		Table profileTable=new Table();
		profileTable.setHeight("400px");
		profileTable.setWidth("250px");
		profileTable.setSizeUndefined();
		profileTable.addContainerProperty("Detail", String.class, null);
		profileTable.addContainerProperty("Value", String.class, null);
profileTable.addItem(new Object[]{"Name :",""},1);
profileTable.addItem(new Object[]{"User Name :",""},2);
profileTable.addItem(new Object[]{"Password :","******"},3);
profileTable.addItem(new Object[]{"Email Id:",""},4);

profileLayout.addComponent(profileTable);
		return profileLayout;
	}
	
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}	
	 SpreadsheetDemoUI getAppUI() {
			return (SpreadsheetDemoUI) UI.getCurrent();
		}

}
