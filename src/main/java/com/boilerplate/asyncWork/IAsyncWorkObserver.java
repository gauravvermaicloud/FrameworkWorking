package com.boilerplate.asyncWork;

/**
 * This interface is implemented by any class which will observer
 * the async messages.
 * 
 * Once a class is created, it should be added in the map in
 * servlet-context.xml in the list of observer map
 * @author gaurav
 *
 */
public interface IAsyncWorkObserver {
	/**
	 * This method observe's and takes action for the work item.
	 * @param asyncWorkItem The work item
	 */
	void observe(AsyncWorkItem asyncWorkItem) throws Exception;
}
