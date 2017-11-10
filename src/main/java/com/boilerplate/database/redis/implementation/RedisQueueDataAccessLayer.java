package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IRedisQueueData;

/**
 * This is the class used for Redis Queue Data Access Layer
 * @author mohit
 *
 */
public class RedisQueueDataAccessLayer extends BaseRedisDataAccessLayer implements IRedisQueueData {

	/**
	 * @see IRedisQueueData.insert
	 */
	@Override
	public void insert(String subject, Object object) {
		super.insert(subject, object);
		
	}

	/**
	 * @see IRedisQueueData.remove
	 */
	@Override
	public <T> T remove(String subject, Class<T> typeOfClass){
		return super.remove(subject,typeOfClass);
	}
	
	/**
	 * @see IRedisQueueData.getQueueSize
	 */
	@Override
	public Long getQueueSize(String queueName){
		return super.getQueueSize(queueName);
	}
}
