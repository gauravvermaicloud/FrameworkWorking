package com.boilerplate.asyncWork;

import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;

public class AsyncWorkItemList extends Base{
	
	private BoilerplateList<AsyncWorkItem> items;

	public BoilerplateList<AsyncWorkItem> getItems() {
		return items;
	}

	public void setItems(BoilerplateList<AsyncWorkItem> items) {
		this.items = items;
	}
}
