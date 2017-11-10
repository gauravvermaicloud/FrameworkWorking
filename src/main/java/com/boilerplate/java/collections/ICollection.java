package com.boilerplate.java.collections;

import java.io.Serializable;

/**
 * This interface is implemented by all collections
 * @author gaurav
 *
 * @param <E> The element in the collection.
 */
public interface ICollection<E> extends Serializable, Cloneable{
	/**
	 * This method returns a JSON equivalent of the collection
	 * @return
	 */
	public String toJSON();
}
