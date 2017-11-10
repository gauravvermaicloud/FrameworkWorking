package com.boilerplate.database.redis.implementation;

import java.util.List;

import com.boilerplate.database.interfaces.IConfigurations;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.Configuration;

public class RedisConfiguration extends BaseRedisDataAccessLayer implements IConfigurations{

	@Override
	public List<Configuration> getConfirguations(String version, String enviornment) {
		String vAllEAllKey = "CONFIGURATION:V_ALL_E_ALL";
		String vVEAllKey = "CONFIGURATION:V_"+version.toUpperCase()+"_E_ALL";
		String vALLEeKey = "CONFIGURATION:V_ALL_E_"+enviornment.toUpperCase();
		String vVEeKey = "CONFIGURATION:V_"+version.toUpperCase()+"_E_"+enviornment.toUpperCase();
		
		String vAllEAllXML = super.get(vAllEAllKey);
		String vVEAllXML = super.get(vVEAllKey);
		String vALLEeXML = super.get(vALLEeKey);
		String vVEeXML = super.get(vVEeKey);
		
		BoilerplateMap<String,String> configurationMapMerged = new BoilerplateMap<>();
		if(vAllEAllXML  !=null){
			configurationMapMerged.putAll((BoilerplateMap<String,String>)Base.fromXML(vAllEAllXML, BoilerplateMap.class));
		}
		
		if(vVEAllXML  !=null){
			configurationMapMerged.putAll((BoilerplateMap<String,String>)Base.fromXML(vVEAllXML, BoilerplateMap.class));
		}
		
		if(vALLEeXML  !=null){
			configurationMapMerged.putAll((BoilerplateMap<String,String>)Base.fromXML(vALLEeXML, BoilerplateMap.class));
		}
		
		if(vVEeXML  !=null){
			configurationMapMerged.putAll((BoilerplateMap<String,String>)Base.fromXML(vVEeXML, BoilerplateMap.class));
		}
		
		BoilerplateList<Configuration>  configurationList = new BoilerplateList<>();
		Configuration configuration = null;
		for(String key : configurationMapMerged.keySet()){
			configuration = new Configuration();
			configuration.setEnviornment(enviornment);
			configuration.setVersion(version);
			configuration.setKey(key);
			configuration.setValue(configurationMapMerged.get(key).toString());
			configurationList.add(configuration);
		}
		
		return configurationList;
	}

}
