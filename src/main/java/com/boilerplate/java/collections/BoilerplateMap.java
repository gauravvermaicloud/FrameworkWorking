package com.boilerplate.java.collections;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.boilerplate.java.Base;

/**
 * This class creates a boiler plate map by extending the Hashmap class.
 * @author gaurav
 * @param <K> This  is the key
 * @param <V> This is the value
 */
public class BoilerplateMap<K,V> extends HashMap<K, V> implements Serializable, Cloneable{
	
	/**
	 * @see HashMap
	 */
	public BoilerplateMap(){
		super();
	}
	
	/**
	 * @see HashMap
	 */
	public BoilerplateMap(Map<K,V> map){
		super(map);
	}
	
	/**
	 * @see Object.toString()
	 */
	@Override
	public String toString(){
		return this.toJSON();
	}
	
	/**
	 * This method returns a JSON equivalent of the map
	 * @return A JSON String
	 */
	public String toJSON(){
		return Base.toJSON(this);
	}
	
	/**
	 * @see Object.equals
	 */
	public boolean equals(Object object){
		if(this == object) return true;
		if(this.getClass() == object.getClass()){
			BoilerplateMap<?,?> boilerPlateMap = (BoilerplateMap<?,?>)object;
			if(this.size() !=boilerPlateMap.size()) return false;
			boolean notFound =true;
			for(K k:this.keySet()){
				if(boilerPlateMap.containsKey(k) == false){
					return false;
				}
				if(this.get(k) != boilerPlateMap.get(k)){
					return false;
				}
			}//end for
			return true;
		}
		return false;
	}
	
	/**
	 * This method creates a clone of the map.
	 * @return A clone of the map
	 */
	public BoilerplateMap<K,V> clone(){
		return new BoilerplateMap((HashMap)super.clone());
	}
}
