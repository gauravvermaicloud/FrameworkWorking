package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.java.collections.BoilerplateMap;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="A generic metadata map entity"
, description="Represents metadatamap")
public class MetaDataEntity implements Serializable{

	/**
	 * This is the metadatamap
	 */
	@ApiModelProperty(value="The metadata map")
	private BoilerplateMap<String,String> metaDataMap;

	/**
	 * This gets the metadata map
	 * @return The metatdata ,a[
	 */
	public BoilerplateMap<String,String> getMetaDataMap() {
		return metaDataMap;
	}

	/**
	 * This sets the metadata map
	 * @param metaDataMap The metadata map
	 */
	public void setMetaDataMap(BoilerplateMap<String,String> metaDataMap) {
		this.metaDataMap = metaDataMap;
	}
	
	
}
