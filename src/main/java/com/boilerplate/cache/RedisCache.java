package com.boilerplate.cache;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.boilerplate.database.redis.implementation.BaseRedisDataAccessLayer;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This is a singleton access point for the Redis cache.
 * @author gaurav
 *
 */
public class RedisCache  extends BaseCache implements ICache{
	
	private static Logger  logger = Logger.getInstance(RedisCache.class);
	
	/**
	 * Static instance of cache for singleton
	 */
	private static RedisCache redisCache;
	
	/**
	 * The time after which cache should expire.
	 */
	private int timeOutInSeconds;
	
	/**
	 * This is the jedis command object
	 */
	private JedisCommands jedisCommands;
	
	/**
	 * This is the number of times cache had an error
	 */
	private int cacheExceptionCount =0;
	
	/**
	 * This is the maximum number of exceptions that can occur after
	 * which cache is turned off
	 */
	private int maximumCacheExceptionCount=0;
	
	/**
	 * The prefix for the cache.
	 */
	private static String cacheKeyPrefix;
	
	@Override
	public void resetCacheExceptionCount(){
		this.cacheExceptionCount = 0;
	}
	

	private static JedisPool pool = null;
	
	/**
	 * This creates an instance of the cache
	 * @param configurations This is the configs to the cache
	 */
	private RedisCache(BoilerplateMap<String,String> configurations){	
		//First check if this is just one local machine or a cluster
		
		//This is the connections to redis it is expected in format host:port;host:port;...
		String redisConnections = configurations.get("CacheServer");
		int cacheTimeoutInMinutes = Integer.parseInt(configurations.get("CacheTimeoutInMinutes"));
		this.cacheExceptionCount = 0;
		this.maximumCacheExceptionCount = Integer.parseInt(configurations.get("MaximumCacheExceptionCount"));
		this.isCacheEnabled = Boolean.parseBoolean(configurations.get("IsCacheEnabled"));
		this.timeOutInSeconds = cacheTimeoutInMinutes*60;
						
		String host;
		int port;
		String[] hostPort = redisConnections.split(":");
		host = hostPort[0];
		port = Integer.parseInt(hostPort[1]);
		//and create a non clustered command
				
		RedisCache.pool = new JedisPool(new JedisPoolConfig(),host,port);
		logger.logInfo("RedisDatabase", "getConnection", "Creating Single",host+":"+port);

	}
	
	/**
	 * This method returns an instance of the redis cache
	 * @return a singleton instance of the cache
	 */
	public static RedisCache getInstance(BoilerplateMap<String, String>confirgurations
			) throws Exception{
		try{
		//if the cacher is null then we have called this method for 1st time
		if(RedisCache.redisCache ==null){
			//only one thread will be allowed so that map is made thread safe and is now loaded
			//again  and again
			synchronized (RedisCache.class) {
				//it is possible that two threads came into this class and at same time
				//and when 2nd thread his this line of code the cache  was 
				//fully prepared hence we check this null again
				if(RedisCache.redisCache ==null){
					//create a new instance of the cache
					RedisCache.redisCache = new RedisCache(confirgurations);
				}//end 2nd if check
			}//end sync block
		}//end 1st if check
		//return the said insamce
		return RedisCache.redisCache;
		}
		catch(Exception ex){
			logger.logException("RedisCache", "getInstance", "catch",
					"Error in getting instance", ex);
			RedisCache.redisCache.cacheExceptionCount++;
			
			throw ex;
		}
	}
	
	/**
	 *@see ICache.add 
	 */
	@Override
	public <T extends Base> void add(String key, T value) {
		if(this.isCacheEnabled()){
			Jedis jedis = null;
			try{
				//TODO -Nice to have-  toXML may cause perf issues, concider looking at a map of object object as in the code of Experian
				jedis = RedisCache.pool.getResource();
				jedis.setex(this.cacheKeyPrefix+":"+key, this.timeOutInSeconds, value.toXML());
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				this.isCacheEnabled = this.cacheExceptionCount <= this.maximumCacheExceptionCount;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in adding a new key", ex);
			}
			finally{
				if(jedis != null){
					jedis.close();
				}
			}
		}
	}

	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key,T value, int timeoutInSeconds){
		if(this.isCacheEnabled()){
			Jedis jedis = null;
			try{
				jedis = RedisCache.pool.getResource();
				jedis.setex(this.cacheKeyPrefix+":"+key, timeoutInSeconds, value.toXML());
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				this.isCacheEnabled = this.cacheExceptionCount <= this.maximumCacheExceptionCount;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in adding a new key", ex);
			}
			finally{
				if(jedis != null){
					jedis.close();
				}
			}
		}
	}
	
	
	/**
	 * @see ICache.get
	 */
	@Override
	public <T extends Base> T get(String key,Class<T> typeOfClass) {
		Set<String> keys = this.jedisCommands.hkeys("USER:*");
			
		T t = null;
		if(this.isCacheEnabled()){
			Jedis jedis = null;
			try{
				jedis = RedisCache.pool.getResource();
				Object o =jedis.get(this.cacheKeyPrefix+":"+key);
				t= Base.fromXML(o.toString(),typeOfClass);
				this.jedisCommands.expire(key, this.timeOutInSeconds);
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				this.isCacheEnabled = this.cacheExceptionCount <= this.maximumCacheExceptionCount;
				logger.logException("RedisCache", "get", "Catch Block"
						, "Exception in getting", ex);
			}
			finally{
				if(jedis != null){
					jedis.close();
				}
			}
		}
		return t;
	}

	/**
	 * @see ICache.remove
	 */
	@Override
	public void remove(String key) {
		if(this.isCacheEnabled()){
			Jedis jedis = null;
			try{
				
				jedis = RedisCache.pool.getResource();
				jedis.del(this.cacheKeyPrefix+":"+key);
				if(this.cacheExceptionCount>0){
					this.cacheExceptionCount =0;
				}
			}
			catch(Exception ex){
				//The reason we do not throw the exceptions is because 
				//we expect the code to work without cache
				this.cacheExceptionCount++;
				this.isCacheEnabled = this.cacheExceptionCount <= this.maximumCacheExceptionCount;
				logger.logException("RedisCache", "add", "Catch Block"
						, "Exception in remove", ex);
			}
			finally{
				if(jedis != null){
					jedis.close();
				}
			}
		}
	}

	private boolean isCacheEnabled = false;
	
	/**
	 * @see ICache.isCacheEnabled
	 */
	@Override
	public boolean isCacheEnabled() {
		return isCacheEnabled;
	}
	
	/**
	 * @see ICache.writeToDB
	 */
	public <T extends Base> void writeToDB(String key,T value){
		this.jedisCommands.set(key, value.toXML());
	}
	
	/**
	 * @see ICache.removeFromDB
	 */
	public void removeFromDB(String key){
		this.jedisCommands.del(key);
	}

	@Override
	public <T extends Base> T readFromDB(String key,Class<T> typeOfClass) {
		Jedis jedis = null;
		T t = null;
		try{
			jedis = RedisCache.pool.getResource();
			Object o =jedis.get(key);
			t= Base.fromXML(o.toString(),typeOfClass);
			return t;
		}
		catch(Exception ex){
			//The reason we do not throw the exceptions is because 
			//we expect the code to work without cache
			this.cacheExceptionCount++;
			logger.logException("RedisCache", "add", "Catch Block"
					, "Exception in remove", ex);
		}
		finally{
			if(jedis != null){
				jedis.close();
			}
		}
		return t;
	}
	
	
}
