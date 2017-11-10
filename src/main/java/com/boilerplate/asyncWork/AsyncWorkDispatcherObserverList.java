package com.boilerplate.asyncWork;

import com.boilerplate.java.collections.BoilerplateList;

/**
 * This is a list of observers which will be used for a subject.
 * This class was created only for DI and could have been a simple
 * list in the map directly.
 * @author gaurav
 *
 */
public class AsyncWorkDispatcherObserverList {
	/**
	 * List of the observers
	 */
	private BoilerplateList<IAsyncWorkObserver> asyncJobList = new BoilerplateList<>();

	/**
	 * Gets the list of observers
	 * @return A list of observers
	 */
	public BoilerplateList<IAsyncWorkObserver> getAsyncJobList() {
		return asyncJobList;
	}

	/**
	 * Sets the list of observers
	 * @param asyncJobList list of observers
	 */
	public void setAsyncJobList(BoilerplateList<IAsyncWorkObserver> asyncJobList) {
		this.asyncJobList = asyncJobList;
	}
}
