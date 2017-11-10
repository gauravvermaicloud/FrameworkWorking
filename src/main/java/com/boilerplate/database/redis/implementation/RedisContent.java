package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IContent;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.GenericKeyValuePairEncapsulationEntity;

public class RedisContent extends BaseRedisDataAccessLayer implements IContent{

	@Override
	public BoilerplateMap<String, BoilerplateMap<String, String>> getContent(String enviornment,
			String organizationId) {
		String contentMasterKey ="CONTENT:"+organizationId.toUpperCase()+":VERSION_ALL:LOCALE_ALL";
		String contentListXML = super.get(contentMasterKey);
		BoilerplateMap<String, String> contentMap = null;
		if(contentListXML!=null){
			contentMap =(BoilerplateMap<String, String>) Base.fromXML(contentListXML, BoilerplateMap.class);
		}
		else{
			contentMap = new BoilerplateMap<>();
		}
		BoilerplateMap<String, BoilerplateMap<String, String>> returnedMap = new BoilerplateMap<String, BoilerplateMap<String, String>>();
		returnedMap.put("en-US", contentMap);
		return returnedMap;
	}

	@Override
	public void createOrUpdate(GenericKeyValuePairEncapsulationEntity keyValue, String organizationId, String userId,
			String locale) {
		
		//first lets create the key for this content's placeholder
		String contentMasterKey ="CONTENT:"+organizationId.toUpperCase()+":VERSION_ALL:LOCALE_ALL";
		String contentListXML = super.get(contentMasterKey);
		BoilerplateMap<String, String> contentMap = null;
		if(contentListXML!=null){
			contentMap =(BoilerplateMap<String, String>) Base.fromXML(contentListXML, BoilerplateMap.class);
		}
		else{
			contentMap = new BoilerplateMap<>();
		}
		contentMap.put(keyValue.getKey(), keyValue.getValue());
		contentListXML = Base.toXML(contentMap);
		super.set(contentMasterKey, contentListXML);
	}

}
