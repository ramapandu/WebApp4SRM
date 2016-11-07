package com.pg.webapp.domain;

import java.io.Serializable;

import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.ui.Table;

public class Spreadsheet_DAO implements Serializable {

	private static final long serialVersionUID = -2859114674314421212L;
	private Long id;
	private Spreadsheet spreadsheet;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}
	public void setSpreadsheet(Spreadsheet spreadsheet) {
		this.spreadsheet = spreadsheet;
	}
	
	
	
}