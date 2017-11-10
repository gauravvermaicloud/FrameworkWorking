package com.boilerplate.cache;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedClientException;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;

public class MemCache  extends BaseCache implements ICache{

	private static MemCache memCache = null;
	private MemcachedClient memcachedClient = null;
	private static int cacheErrorCount = 0;
	private static int maximumCacheErrorCount;
	private static int defaultExpiry;
	
	private static Logger logger = Logger.getInstance(MemCache.class);
	
	/**
	 * This is the cache key prefix
	 */
	private static String cacheKeyPrefix;
	
	private MemCache() throws Exception{
		//TODO - Read this from cpnfiguration
		String machines = "127.0.0.1:11211";
		MemCache.maximumCacheErrorCount = 5;
		MemCache.defaultExpiry = 60;
		
		MemcachedClientBuilder memcachedClientBuilder =
				new XMemcachedClientBuilder(machines);
		this.memcachedClient = memcachedClientBuilder.build();
		MemCache.cacheKeyPrefix = BaseCache.getCachePrefix();
	}
	
	/**
	* This method returns the singleton to memcache
	*@return This method retruns an instance of memcache
	*/
	public static MemCache getInstance() throws Exception{
		try{
			if(MemCache.memCache == null){
				synchronized (MemCache.class) {
					if(MemCache.memCache ==null){
						MemCache.memCache = new MemCache();
					}
				}
			}
			return MemCache.memCache;
		}
		catch(Exception ex){
			MemCache.logger.logException("MemCache", "getInstance"
					, "Catch Block", "Error creating cache", ex);
			MemCache.cacheErrorCount++;
			throw ex;
		}
		
	}
	
	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key, T value) {
		
		try{
			this.memcachedClient.set(this.cacheKeyPrefix+":"+key, MemCache.defaultExpiry,value.toXML());
		}
		catch(Exception ex){
			//we do not expect cache to fail hence we
			//this exception is not thrown.
			MemCache.logger.logException("MemCache", "getInstance"
					, "Catch Block", "Error setting into cache", ex);
			MemCache.cacheErrorCount++;
		}
	}

	/**
	 * @see ICache.get
	 */
	@Override
	public <T extends Base> T get(String key,Class<T> typeOfClass) {
		T t =null;
		try{
			String s = this.memcachedClient.get(key);
			t = Base.fromXML(s,typeOfClass);
			this.memcachedClient.touch(key, MemCache.defaultExpiry);
		}
		catch(Exception ex){
			//we do not expect cache to fail hence we
			//this exception is not thrown.
			MemCache.logger.logException("MemCache", "getInstance"
					, "Catch Block", "Error getting from cache", ex);
			MemCache.cacheErrorCount++;
		}
		return t;
	}

	/**
	 * @see ICache.remove
	 */
	@Override
	public void remove(String key) {
		try{
			this.memcachedClient.delete(this.cacheKeyPrefix+":"+key);
		}
		catch(Exception ex){
			//we do not expect cache to fail hence we
			//this exception is not thrown.
			MemCache.logger.logException("MemCache", "getInstance"
					, "Catch Block", "Error removing from cache", ex);
			MemCache.cacheErrorCount++;
		}
	}

	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key,T value, int timeoutInSeconds){
		try{
			this.memcachedClient.set(this.cacheKeyPrefix+":"+key, timeoutInSeconds,value.toXML());
		}
		catch(Exception ex){
			//we do not expect cache to fail hence we
			//this exception is not thrown.
			MemCache.logger.logException("MemCache", "getInstance"
					, "Catch Block", "Error setting into cache", ex);
			MemCache.cacheErrorCount++;
		}
	}

	/**
	*@see ICache.isCacheEnabled
	*/
	@Override
	public boolean isCacheEnabled() {
		return this.cacheErrorCount <= this.maximumCacheErrorCount;
	}

	/**
	 * @see ICache.resetCacheExceptionCount
	 */
	@Override
	public void resetCacheExceptionCount(){
		this.cacheErrorCount = 0;
	}

	@Override
	public <T extends Base> void writeToDB(String key, T value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromDB(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Base> T readFromDB(String key, Class<T> typeOfClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
