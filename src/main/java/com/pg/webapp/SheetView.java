package com.pg.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.pg.webapp.domain.User;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeEvent;
import com.vaadin.addon.spreadsheet.Spreadsheet.SheetChangeListener;
import com.vaadin.addon.spreadsheet.SpreadsheetFilterTable;
import com.vaadin.data.Item;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class SheetView extends CustomComponent implements View {

	private User user;
	public static final String NAME = "sheet";
	private static final long serialVersionUID = 6714096000861957459L;
	Button logoutButton;

	CellRangeAddress range;
	VerticalLayout rootLayout;
	Spreadsheet spreadsheet;
	HorizontalLayout topBar, sheetLayout;
	Button editButton, saveButton, downlaodButton, exportButton;
	File testSheetFile;
	Table logTable;
	private TabSheet tabSheet;

	VaadinSession ses;
	FileInputStream fis;

	@SuppressWarnings({ "unused" })
	public SheetView() {
		// ses= VaadinSession.getCurrent();
		setSizeFull();

		rootLayout = new VerticalLayout();
		setCompositionRoot(rootLayout);
		CreateUI();

	}

	private void CreateUI() {

		rootLayout.addComponent(getTopBar());
		rootLayout.addComponent(getSheetLayout());
	}

	public HorizontalLayout getSheetLayout() {
		sheetLayout = new HorizontalLayout();
		sheetLayout.setSizeFull();
		sheetLayout.setHeight("100%");
		sheetLayout.addComponent(getTabSheet());
		sheetLayout.addStyleName("sheetlayout");
		return sheetLayout;
	}

	private HorizontalLayout getTopBar() {
		topBar = new HorizontalLayout();
		getEditButton();
		getLogoutButton();
		getSaveButton();
		getDownloadButton();
		getExportButton();
		final GridLayout grid = new GridLayout(5, 1);
//		grid.setWidth(400, Unit.PIXELS);
		grid.setHeight(35, Unit.PIXELS);

		grid.addComponent(editButton, 0, 0);
		grid.setComponentAlignment(editButton, Alignment.TOP_LEFT);

		grid.addComponent(saveButton, 1, 0);
		grid.setComponentAlignment(saveButton, Alignment.TOP_CENTER);

		grid.addComponent(downlaodButton, 2, 0);
		grid.setComponentAlignment(downlaodButton, Alignment.TOP_RIGHT);

		grid.addComponent(exportButton, 3, 0);
		grid.setComponentAlignment(exportButton, Alignment.TOP_RIGHT);

		grid.addComponent(logoutButton, 4, 0);
		grid.setComponentAlignment(logoutButton, Alignment.TOP_RIGHT);

		topBar.setPrimaryStyleName("topbar");
		// topBar.addComponent(getEditButton());
		// topBar.addComponent(getSaveButton());
		// topBar.addComponent(getDownloadButton());
		// topBar.addComponent(getExportButton());
		// topBar.setComponentAlignment(exportButton,Alignment.TOP_CENTER);

		// topBar.addComponent(logoutButton);
		// topBar.setComponentAlignment(logoutButton,Alignment.TOP_RIGHT);

		// topBar.setComponentAlignment(logoutButton, Alignment.BOTTOM_RIGHT);
		topBar.addComponent(grid);
		return topBar;
	}

	@SuppressWarnings("unchecked")
	private TabSheet getTabSheet() {
		tabSheet = new TabSheet();

		// tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener()
		// {
		//
		//
		// private static final long serialVersionUID = -1698363226401049948L;
		//
		// @Override
		// public void selectedTabChange(SelectedTabChangeEvent event) {
		// com.vaadin.ui.JavaScript
		// .eval("setTimeout(function(){prettyPrint();},300);");
		// }
		// });
		tabSheet.setSizeFull();
		tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
		try {
			tabSheet.addTab(openSheet(), "Sheet");
			logTable = new Table("Logs");
//			logTable=getAppUI().getLogTable().getLogTable();
			logTable=getLogSheet();
//			logTable.addContainerProperty("User", String.class, null);
//			logTable.addContainerProperty("Action", String.class, null);
//			logTable.addContainerProperty("Date", String.class, null);
			logTable.setPageLength(logTable.size());
			tabSheet.addTab(logTable, "Logs");
			
			Date d = new Date();
			logTable.addItem(new Object[] { "Ravi", "Changed value  A to B",
					d.toString() },new Integer(1));
//			Item item = logTable.addItem("TIM");
//			item.getItemProperty("User").setValue("Kamal");
//			item.getItemProperty("Action").setValue("Changed value  A to B");
//			item.getItemProperty("Date").setValue(d.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tabSheet;
	}

	private Table getLogSheet() throws IOException{
		FileInputStream fs = new FileInputStream("C:/Users/rampa/Desktop/testsheets/logs.xlsx");
		File f=new File("C:/Users/rampa/Desktop/testsheets/logs.xlsx");
		Table logTable=new Table();
		logTable.addContainerProperty("User", String.class, null);
		logTable.addContainerProperty("Action", String.class, null);
		logTable.addContainerProperty("Date", String.class, null);
//		Spreadsheet logSheet= new Spreadsheet(fs);
		 
		 Workbook book = new XSSFWorkbook(fs);
		 Sheet sheet = book.getSheetAt(0);
		 int i=0;
		 for (Row row : sheet) {
//	            for (Cell cell : row) {
if(row.getRowNum()>0)
	            	logTable.addItem(new Object[] { row.getCell(0).toString(), row.getCell(1).toString(),
	            			row.getCell(2).toString() },new Integer(i));
//	            }
	            i++;
	        }
	        fs.close();
		
		return logTable;
	}
	
	private Button getLogoutButton() {
		logoutButton = new Button("Logout", new Button.ClickListener() {

			private static final long serialVersionUID = 2718672708618255597L;

			@Override
			public void buttonClick(ClickEvent event) {
				getSession().close();
				((SpreadsheetDemoUI) UI.getCurrent()).getUser()
						.setLoggedInUser(null);
				getUI().getPage().setLocation("/vaadin-spreadsheet-demo");
			}
		});
		logoutButton.addStyleName("topbarbuttons");
		return logoutButton;
	}

	private Button getExportButton() {
		exportButton = new Button("EXPORT");
		exportButton.addStyleName("topbarbuttons");
		exportButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -7614812368402111788L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		return exportButton;
	}

	private Button getDownloadButton() {
		downlaodButton = new Button("DOWNLOAD");
		downlaodButton.addStyleName("topbarbuttons");
		downlaodButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -8158301975694183254L;

			@Override
			public void buttonClick(ClickEvent event) {
				File file = new File("C:/Users/rampa/Desktop/testsheets/",
						"test.xlsx");
				FileResource resource = new FileResource(file);
				FileDownloader fileDownloader = new FileDownloader(resource);
				fileDownloader.extend(downlaodButton);
			}
		});
		return downlaodButton;
	}

	private Button getEditButton() {
		editButton = new Button("EDIT");
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
		saveButton = new Button("SAVE");
		saveButton.addStyleName("topbarbuttons");
		saveButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1792550832130526578L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					File tempFile = new File(
							"C:/Users/rampa/Desktop/testsheets/test.xlsx");
					FileOutputStream fos = new FileOutputStream(tempFile);
					spreadsheet.write(fos);
					fos.close();
//					------getAppUI().getLogTable().setLogTable(logTable);
					// ByteArrayOutputStream bos = new ByteArrayOutputStream();
					// spreadsheet.write(bos);
					// byte[] data = bos.toByteArray();
					// bos.close();
					//
					// URL testSheetResource1 = this.getClass().getClassLoader()
					// .getResource("testsheets/SAP-DEAL4.xlsx");
					// File tempFile = new File(testSheetResource1.toURI());
					// // tempFile.
					// FileOutputStream tempOutputStream = new
					// FileOutputStream(tempFile);
					// tempOutputStream.write(data);
					// tempOutputStream.flush();
					// tempOutputStream.close();
					// ------------FINAL SAVE WORKING COPY
					// URL testSheetResource1 = this.getClass().getClassLoader()
					// .getResource("testsheets/SAP-DEAL1.xlsx");
					// File tempFile = new File(testSheetResource1.toURI());
					//
					// // File tempFile1= File.createTempFile("testTemp",
					// ".xlsx");
					//
					// FileOutputStream tempOutputStream = new
					// FileOutputStream(tempFile);
					// spreadsheet.write(tempOutputStream);
					// tempOutputStream.flush();
					// tempOutputStream.close();
					// copyFile(tempFile,testSheetFile); //THIS CODE CAUSES
					// SESSION EXPIRED PROBLEM
					// tempFile.delete();
					// /////////////////////////////////77
					// setSession(getSession());
					// spreadsheet.reload();
					// spreadsheet.read(tempFile);
					// getAppUI().getPage().reload();
					// -----------------------------FINAL

					// FileOutputStream fos= new FileOutputStream("myfile");
					// ObjectOutputStream oos= new ObjectOutputStream(fos);
					// oos.writeObject(al);
					// oos.close();
					// fos.close();

					// -------------------------------------
					// Spreadsheet sheet1 = new Spreadsheet(tempFile);
					// spreadsheet= new Spreadsheet(tempFile);
					// copyFile(tempFile,testSheetFile);
					// spreadsheet.setData(testSheetFile);
					// spreadsheet.setData(sheet1);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		return saveButton;
	}

	public Spreadsheet openSheet() throws URISyntaxException, IOException {
		fis = new FileInputStream("C:/Users/rampa/Desktop/testsheets/test.xlsx");
		spreadsheet = new Spreadsheet(fis);
		fis.close();
		// getPopUpButtonsForSheet(spreadsheet.getActiveSheet());///////////TEST-----------
		// //TEST-BEGIN ----------
		// URL testSheetResource = this.getClass().getClassLoader()
		// .getResource("testsheets/SAP-DEAL1.xlsx");
		// testSheetFile = new File(testSheetResource.toURI());
		// spreadsheet = new Spreadsheet(testSheetFile);
		spreadsheet.setSizeFull();
		spreadsheet.setHeight("700px");
		getPopUpButtonsForSheet(spreadsheet.getActiveSheet());
//		------------getAppUI().getLogTable().setLogTable(logTable);
		spreadsheet.addSheetChangeListener(new SheetChangeListener() {

			private static final long serialVersionUID = -5585430837302587763L;

			@Override
			public void onSheetChange(SheetChangeEvent event) {
				getPopUpButtonsForSheet(spreadsheet.getActiveSheet());

			}
		});
		// TEST-END------------
		return spreadsheet;
	}

	public void getPopUpButtonsForSheet(Sheet sheet)
			throws NullPointerException, ArrayIndexOutOfBoundsException {

		try {
			// Row r = sheet.getRow(sheet.getFirstRowNum());
			// int lastColumnNum=0;
			// lastColumnNum= r.getLastCellNum();
			// OR Cell lastCellInRow = row.getCell(row.getLastCellNum() - 1);
			// ------------------
			// int numberOfCells = 0;
			// Iterator rowIterator = sheet.rowIterator();
			// /**
			// * Escape the header row *
			// */
			// if (rowIterator.hasNext())
			// {
			// Row headerRow = (Row) rowIterator.next();
			// //get the number of cells in the header row
			// numberOfCells = headerRow.getPhysicalNumberOfCells();
			// }
			// System.out.println(sheet.getFirstRowNum()+""+sheet.getLastRowNum()+""+1+""+numberOfCells);
			// CellRangeAddress range =new
			// CellRangeAddress(sheet.getFirstRowNum(),sheet.getLastRowNum(),1,numberOfCells);
			// --------------

			// Define the range
			CellRangeAddress range = new CellRangeAddress(0, 10, 0, 10);
			System.out.println(sheet.getFirstRowNum() + ""
					+ sheet.getLastRowNum() + "" + 1);
			// Create a table in the range
			SpreadsheetFilterTable table = new SpreadsheetFilterTable(
					spreadsheet, sheet, range);
			table.getPopupButtons();
			// table.getSheet().

		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void getPopUpButtonsForAllSheets() {
		for (int i = 0; i < spreadsheet.getNumberOfSheets(); i++) {
			Sheet s = spreadsheet.getWorkbook().getSheetAt(i);
			getPopUpButtonsForSheet(s);
		}
	}

	// @Override
	// public void valueChange(ValueChangeEvent event) {
	// // spreadsheet.addSheetChangeListener(selectedSheetChangeListener);
	// // selectedSheetChangeListener = new SheetChangeListener() {
	// // @Override
	// // public void onSheetChange(SheetChangeEvent event) {
	// //// getPopUpButtons();
	// // getPopUpButtons(spreadsheet.getActiveSheet());
	// // }
	// // };
	// // Object value = event.getProperty().getValue();
	// // open(value);
	// //// getPopUpButtons();
	// }

	public void enableEdit() {
		// if(editButton.isEnabled())
		// spreadsheet.setActiveSheetProtected(null);
	}

	// @Override
	// public void valueChange(ValueChangeEvent event) {
	// // TODO Auto-generated method stub
	//
	// }

	// private void open(Object value) {
	// tabSheet.removeAllComponents();
	// if (value instanceof File) {
	// // openFile((File) value);
	//
	// }
	// }

	private Component getUserProfile() {
		HorizontalLayout profileLayout = new HorizontalLayout();
		profileLayout.setStyleName("contentbar");
		Table profileTable = new Table();
		profileTable.setHeight("400px");
		profileTable.setWidth("250px");
		profileTable.setSizeUndefined();
		profileTable.addContainerProperty("Detail", String.class, null);
		profileTable.addContainerProperty("Value", String.class, null);
		profileTable.addItem(new Object[] { "Name :", "" }, 1);
		profileTable.addItem(new Object[] { "User Name :", "" }, 2);
		profileTable.addItem(new Object[] { "Password :", "******" }, 3);
		profileTable.addItem(new Object[] { "Email Id:", "" }, 4);

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
