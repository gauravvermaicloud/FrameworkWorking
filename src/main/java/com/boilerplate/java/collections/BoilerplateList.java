package com.boilerplate.java.collections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.boilerplate.java.Base;

/**
 * This class extends an ArrayList and provides additional features such as
 * clone
 * @author gaurav
 *
 * @param <E> This is the type of element
 */
public class BoilerplateList<E> extends ArrayList implements ICollection,List,Serializable,Cloneable { 
	
	
	/**
	 * @see super.serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Returns a string representation of the hash set
	 */
	@Override
	public String toString(){
		return this.toJSON();
	}
	
	/**
	 * @see ICollectio.toJSON()
	 */
	@Override
	public String toJSON(){
		return Base.toJSON(this);
	}
	
	/**
	 * @see ArrayList
	 */
	public BoilerplateList(){
		super();
	}
	
	/**
	 * Creates a hash set based on a collection.
	 * @param c This is a collection.
	 */
	public BoilerplateList(Collection<? extends E> c){
		super(c);
	}
	
	/**
	 * @see ArrayList
	 */
	public BoilerplateList(int initialCapacity){
		super(initialCapacity);
	}

	/**
	 * This method checks if the two sets are equal
	 */
	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this.getClass() == object.getClass()){
			BoilerplateList<?> boilerPlateList = (BoilerplateList<?>)object;
			if(this.size() !=boilerPlateList.size()) return false;
			return boilerPlateList.containsAll(this);
		}
		return false;
	}
	
	/**
	 * This class clones an object 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BoilerplateList<E> clone(){
		return new BoilerplateList<E>((ArrayList<E>)super.clone());
	}
	
}
