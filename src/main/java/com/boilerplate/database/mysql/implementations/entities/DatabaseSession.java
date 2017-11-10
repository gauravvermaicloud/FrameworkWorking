package com.boilerplate.database.mysql.implementations.entities;

import com.boilerplate.sessions.Session;

public class DatabaseSession extends Session {
	private String sessionEntity;

	public String getSessionEntity() {
		return sessionEntity;
	}

	public void setSessionEntity(String sessionEntity) {
		this.sessionEntity = sessionEntity;
	}

}
