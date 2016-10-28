package com.pg.webapp.domain;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 6789475091210638670L;
	
	private Long id;
	private String loggedInUser;
	public String getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	
}