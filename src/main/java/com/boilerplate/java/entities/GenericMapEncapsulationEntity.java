package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Map;

public class GenericMapEncapsulationEntity<T,U> implements Serializable {

	private Map<T,U> entityMap;

	public Map<T,U> getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(Map<T,U> entityMap) {
		this.entityMap = entityMap;
	}
	
}
