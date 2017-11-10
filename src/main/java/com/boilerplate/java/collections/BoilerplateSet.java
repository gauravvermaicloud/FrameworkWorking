package com.boilerplate.java.collections;

import java.util.Collection;
import java.util.HashSet;

import com.boilerplate.java.Base;

/**
 * This class extends a HashSet. it provides deep copy and clone
 * functions over hash set.
 * @author gaurav
 * @param <E> - This is the type of elements in the collection and must implement clonable
 */
public class BoilerplateSet<E> extends HashSet<E> implements ICollection {
	
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
	public String toJSON(){
		return Base.toJSON(this);
	}
	
	/**
	 * @see HashSet
	 */
	public BoilerplateSet(){
		super();
	}
	
	/**
	 * Creates a hash set based on a collection.
	 * @param c This is a collection.
	 */
	public BoilerplateSet(Collection<? extends E> c){
		super(c);
	}
	
	/**
	 * @see HashSet
	 */
	public BoilerplateSet(int initialCapacity, float loadFactor){
		super(initialCapacity,loadFactor);
	}
	
	/**
	 * @see HashSet
	 */
	public BoilerplateSet(int initialCapacity){
		super(initialCapacity);
	}

	/**
	 * This method checks if the two sets are equal
	 */
	@Override
	public boolean equals(Object object){
		if(this == object) return true;
		if(this.getClass() == object.getClass()){
			BoilerplateSet<?> boilerPlateSet = (BoilerplateSet<?>)object;
			if(this.size() !=boilerPlateSet.size()) return false;
			return boilerPlateSet.containsAll(this);
		}
		return false;
	}
	/**
	 * This class clones an object 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BoilerplateSet<E> clone(){
		return new BoilerplateSet<E>((HashSet<E>)super.clone());
	}
	
}
