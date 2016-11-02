package com.pg.webapp.domain;

import java.io.Serializable;

import com.vaadin.ui.Table;

public class LogTable implements Serializable {

	private static final long serialVersionUID = 7066463025553059192L;

	private Long id;
	private Table logTable;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Table getLogTable() {
		return logTable;
	}
	public void setLogTable(Table logTable) {
		this.logTable = logTable;
	}
	
	
}