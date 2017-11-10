package com.boilerplate.queue;

import com.boilerplate.asyncWork.AsyncWorkItem;

/**
 * This interface is implemented by any queue in the system to fetch and
 * push data in queue.
 * @author gaurav
 *
 */
public interface IQueue {
	/**
	 * This method pushes an item into the queue.
	 * @param subject This is the subject on which this is to be published.
	 * @param item This is the item
	 * @throws Exception If there is an error in inserting into queue
	 */
	public <T> void insert (String subject,T item) throws Exception;
	
	/**
	 * This method gets an item from the queue. It will wait for timeoutInMilliSeconds
	 * before it gets back.This method should be thread safe so that same item is not
	 * given to multiple threads.
	 * @param timeoutInMilliSeconds This is the timeout after which the queue will return
	 * @param subject This is the subject of the message
	 * @return The value in queue or null if the queue times out.
	 * @throws Exception If there is an error in removing from queue
	 */
	public <T>  T remove(String subject,int timeoutInMilliSeconds) throws Exception;

	/**
	 * This method gets an item from the queue. It will wait for timeoutInMilliSeconds
	 * before it gets back. This method should be thread safe so that same item is not
	 * given to multiple threads.
	 * @param subject This is the subject of the message
	 * @return The value in queue or null if the queue times out.
	 * @throws Exception This is thrown if there is an exception in getting item from queue
	 */
	public <T> T remove(String subject) throws Exception;
	
	/**
	 * This method tells if the queue is enabled or not.
	 * @return True if the queue is enabled and false if it is not.
	 */
	public boolean isQueueEnabled();
	
	/**
	 * This method is used to reset the number of errors that occuerd in queue
	 * processing to enable it back again.
	 */
	public void resetQueueErrorCount();

	public long getQueueSize(String string) throws Exception;
}
