package com.pg.webapp;

import java.io.Serializable;

import javax.servlet.annotation.WebServlet;

import com.pg.webapp.domain.LogTable;
import com.pg.webapp.domain.Spreadsheet_DAO;
import com.pg.webapp.domain.User;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;



@JavaScript("prettify.js")
@Theme("demo-theme")
@Title("Vaadin Spreadsheet Demo")
@SuppressWarnings({ "rawtypes", "unchecked" ,"NotSerializableException"})
@PreserveOnRefresh
public class SpreadsheetDemoUI extends UI implements Serializable {
	
	 public LogTable getLogTable() {
		return logTable;
	}

	public void setLogTable(LogTable logTable) {
		this.logTable = logTable;
	}
	private User user; 
	 private LogTable logTable;
	 private Spreadsheet_DAO sheet;
    Navigator navigator;
    
      protected static final String MAINVIEW = "main";
	private static final long serialVersionUID = -2636301182919370995L;
	 
    
//	@PreserveOnRefresh 
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SpreadsheetDemoUI.class, widgetset = "com.vaadin.addon.spreadsheet.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet implements Serializable  {

	

		private static final long serialVersionUID = -3633705842690837333L;

		
//		    @Override
//		    public void sessionInit(SessionInitEvent event) throws ServiceException {
//		        event.getSession().addBootstrapListener(new MyBootstrapListener());
//		    }
//
//		    private  class MyBootstrapListener implements BootstrapListener,Serializable {
//		        private static final long serialVersionUID = 1L;
//
//		        @Override
//		        public void modifyBootstrapPage(BootstrapPageResponse response) {
//		            //...
//		        }
//
//		        @Override
//		        public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
//		            //...
//		        }
//		    }
		}
    	
    public SpreadsheetDemoUI() {
    	user=new User();
    	logTable=new LogTable();
    	sheet=new Spreadsheet_DAO();
    	
    }

    public Spreadsheet_DAO getSheet() {
		return sheet;
	}

	public void setSheet(Spreadsheet_DAO sheet) {
		this.sheet = sheet;
	}

	@Override
    protected void init(VaadinRequest request) {
    	navigator = new Navigator(this,this);
		navigator.addView("", new LoginView(navigator));
		navigator.addView(SheetView.NAME,SheetView.class);
    }
   
    public User getUser() {
    	return user;
         }

         public void setUser(User user) {
    	this.user = user;
         }   
   Navigator getNavigatorManager(){
		return navigator;
	}
}
