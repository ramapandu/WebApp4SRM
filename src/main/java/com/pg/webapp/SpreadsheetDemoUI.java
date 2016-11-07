package com.pg.webapp;

import java.io.Serializable;

import javax.servlet.annotation.WebServlet;

import com.pg.webapp.domain.LogTable;
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
@SuppressWarnings({ "rawtypes", "unchecked", "NotSerializableException" })
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
	Navigator navigator;

	protected static final String MAINVIEW = "main";
	private static final long serialVersionUID = -2636301182919370995L;

	// @PreserveOnRefresh
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SpreadsheetDemoUI.class, widgetset = "com.vaadin.addon.spreadsheet.demo.DemoWidgetSet")
	public static class Servlet extends VaadinServlet implements Serializable {

		private static final long serialVersionUID = -3633705842690837333L;
	}

	public SpreadsheetDemoUI() {
		user = new User();
		logTable = new LogTable();
	}

	@Override
	protected void init(VaadinRequest request) {
		navigator = new Navigator(this, this);
		navigator.addView("", new LoginView(navigator));
		navigator.addView(SheetView.NAME, SheetView.class);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	Navigator getNavigatorManager() {
		return navigator;
	}
}
