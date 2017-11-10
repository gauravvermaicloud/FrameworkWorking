package com.boilerplate.database.interfaces;

/**
 * This is the interface for Redis Queue Operations.
 * @author mohit
 *
 */
public interface IRedisQueueData {
	
	/**
	 * This method inserts an object into the queue
	 * @param subject The subject
	 * @param object The object
	 */
	public void insert(String subject,Object object);
	
	/**
	 * This method removes an object from the queue and then returns that object
	 * @param subject The subject
	 * @param typeOfClass The typeOfClass
	 * @return T The Object
	 */
	public <T> T remove(String subject, Class<T> typeOfClass);
	
	/**
	 * This method get the size of the queue
	 * @param queueName The name of the queue
	 * @return Integer Size of Queue
	 */
	public Long getQueueSize(String queueName);
}
