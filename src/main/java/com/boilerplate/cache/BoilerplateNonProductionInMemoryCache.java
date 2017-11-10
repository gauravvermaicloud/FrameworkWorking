package com.boilerplate.cache;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;

/**
 * This class is a quick in memory cache. This uses the IVM heap
 * and is not expected to be used on production. It may be used for 
 * testing only
 * @author gaurav
 *
 */
public class BoilerplateNonProductionInMemoryCache extends BaseCache implements ICache{

	private static String cacheKeyPrefix;
	
	/**
	 * This is the boiler plate map. The map will be used to
	 * store cache values
	 */
	private BoilerplateMap<String,Object> boilerplateMap =
			new BoilerplateMap<String,Object>();
	
	/**
	 * This is the private instance of the cache and will be used to
	 * manage the singleton.
	 */
	private static BoilerplateNonProductionInMemoryCache 
		boilerplateNonProductionInMemoryCache = null;
	
	/**
	 * This is the private constructor
	 */
	private BoilerplateNonProductionInMemoryCache(){
		
	}
	
	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key, T value) {
		this.boilerplateMap.put(this.cacheKeyPrefix+":"+key, value);
	}

	/**
	 * @see ICache.get
	 */
	@Override
	public <T extends Base> T get(String key,Class<T> typeOfClass) {
		return (T)this.boilerplateMap.get(this.cacheKeyPrefix+":"+key);
	}

	/**
	 * @see ICache.remove
	 */
	@Override
	public void remove(String key) {
		this.boilerplateMap.remove(this.cacheKeyPrefix+":"+key);
	}

	/**
	 * @see ICache.add
	 */
	@Override
	public <T extends Base> void add(String key,T value, int timeoutInSeconds){
		this.boilerplateMap.put(this.cacheKeyPrefix+":"+key, value);
	}
	
	/**
	 * @see ICache.getInstance
	 */
	public static ICache getInstance() {
		//This is not thread safe as it will not be used in production
		//Hence we just check for null in the method and create new.
		if (BoilerplateNonProductionInMemoryCache.boilerplateNonProductionInMemoryCache ==null){
			BoilerplateNonProductionInMemoryCache.boilerplateNonProductionInMemoryCache = 
					new BoilerplateNonProductionInMemoryCache();
			BoilerplateNonProductionInMemoryCache.cacheKeyPrefix = BaseCache.getCachePrefix();
		}
		
		return BoilerplateNonProductionInMemoryCache.boilerplateNonProductionInMemoryCache;
	}

	/**
	 * @see ICache.isCacheEnabled
	 */
	@Override
	public boolean isCacheEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @see ICache.resetCacheExceptionCount
	 */
	@Override
	public void resetCacheExceptionCount(){
		
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
