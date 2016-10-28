package com.vaadin.addon.spreadsheet.demo;

import java.io.Serializable;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.spreadsheet.SpreadsheetFactory;
import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;



@JavaScript("prettify.js")
@Theme("demo-theme")
@Title("Vaadin Spreadsheet Demo")
@SuppressWarnings({ "rawtypes", "unchecked" ,"NotSerializableException"})
@PreserveOnRefresh
public class SpreadsheetDemoUI extends UI implements Serializable {
	
	String user; 
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
//    	navigator.navigateTo("login");
//        SpreadsheetFactory.logMemoryUsage();
    }

    @Override
    protected void init(VaadinRequest request) {
    	navigator = new Navigator(this,this);
		navigator.addView("", new LoginView(navigator));
		navigator.addView(SheetView.NAME,SheetView.class);
//       navigator.navigateTo("login");
    }
   
       
   public void setLoggedInUser(String username) {
	   this.user=username;		
	   	} 
   
   public Object getLoggedInUser() {
		return user;
	}
   
   Navigator getNavigatorManager(){
		return navigator;
	}
}
