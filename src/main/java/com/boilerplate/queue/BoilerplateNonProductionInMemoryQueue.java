package com.boilerplate.queue;

import java.util.concurrent.ArrayBlockingQueue;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.java.collections.BoilerplateMap;

public class BoilerplateNonProductionInMemoryQueue implements IQueue{

	/**
	 * This is the queue map for all data.
	 */
	private BoilerplateMap<String, ArrayBlockingQueue<Object>> queueMap   = new BoilerplateMap<>();
	
	/**
	 * The private static instance for making queue a singleton.
	 */
	private static BoilerplateNonProductionInMemoryQueue boilerplateNonProductionInMemoryQueue
		= new BoilerplateNonProductionInMemoryQueue();
	
	/**
	 * This method returns an instance of queue
	 * @return an instance of queue
	 */
	public static BoilerplateNonProductionInMemoryQueue getInstance(){
		return BoilerplateNonProductionInMemoryQueue.boilerplateNonProductionInMemoryQueue;
	}
	
	/**
	 * This is the private constructor of the queue
	 */
	private BoilerplateNonProductionInMemoryQueue(){
		
	}
	
	private synchronized ArrayBlockingQueue<Object> getOrCreateAndGetQueue(String subject){
		ArrayBlockingQueue<Object> queue= queueMap.get(subject);
		if(queue ==null){
			queue = new ArrayBlockingQueue(10000);
			queueMap.put(subject, queue);
		}
		return queue;
	}
	
	/**
	 * @see IQueue.push
	 */
	@Override
	public synchronized<T>  void insert(String subject,T item) {
		getOrCreateAndGetQueue(subject).add(item);
	}

	/**
	 * @see IQueue.pop
	 */
	@Override
	public synchronized<T> T remove(String subject,int timeoutInMilliSeconds) {
		return (T)getOrCreateAndGetQueue(subject).poll();
	}

	/**
	 * @see IQueue.pop
	 */
	@Override
	public synchronized<T> T remove(String subject) {
		return this.remove(subject,0);
	}
	
	/**
	 * @see IQueue.isQueueEnabled
	 */
	@Override
	public boolean isQueueEnabled(){
		return true;
	}
	
	/**
	 * @see IQueue.resetQueueErrorCount
	 */
	@Override
	public void resetQueueErrorCount(){
		
	}

	@Override
	public long getQueueSize(String string) {
		// TODO Auto-generated method stub
		return 0;
	}
}
