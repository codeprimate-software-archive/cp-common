/*
 * BeanHistory.java (c) 26 January 2008
 *
 * The BeanHistory class is a container for a collection of beans that is referrenced from a containing bean.
 * The containing bean will have a 1 to many relationship with the beans contained in this bean history object.  The
 * BeanHistory interface provides additional functionality outside of the usual collection classes in the Java API.
  *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.7.27
 * @see com.cp.common.beans.AbstractBeanHistory
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Filter
 * @see java.lang.Iterable
 * @see java.io.Serializable
 */

package com.cp.common.beans;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Filter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

public interface BeanHistory<T extends Bean> extends Iterable<T>, Mutable, Serializable, Visitable {

  /**
   * Adds the specified bean to this history.
   * @param bean the Bean object being added to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the specified bean was successfully added to this history object.
   * @throws NullPointerException if the bean object is null!
   * @throws IllegalStateException if the specified bean's history reference is not null!
   * @see BeanHistory#addAll(Bean[], Filter...)
   * @see BeanHistory#addAll(Collection, Filter...)
   * @see BeanHistory#addAll(BeanHistory, Filter...)
   */
  public boolean add(T bean, Filter<T>... filters);

  /**
   * Adds the array of bean objects to this history.
   * @param beans an array of Bean objects to add to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the beans were successfully added to this history object.
   * @throws NullPointerException if the bean array is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see BeanHistory#add(Bean, Filter...)
   * @see BeanHistory#addAll(Collection, Filter...)
   * @see BeanHistory#addAll(BeanHistory, Filter...)
   */
  public boolean addAll(T[] beans, Filter<T>... filters);

  /**
   * Adds the collection of bean objects to this history.
   * @param beans a collection of Bean objects to add to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the the beans were successfully added to this history object.
   * @throws NullPointerException if the bean collection is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see BeanHistory#add(Bean, Filter...)
   * @see BeanHistory#addAll(Bean[], Filter...)
   * @see BeanHistory#addAll(BeanHistory, Filter...)
   */
  public boolean addAll(Collection<T> beans, Filter<T>... filters);

  /**
   * Adds the bean objects from the specified bean history object to this history.
   * @param beanHistory a BeanHistory objects containing beans to be removed and add to this bean history.
   * @param filters an array Filter object used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the the beans were successfully added to this history object.
   * @throws NullPointerException if the bean collection is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see BeanHistory#add(Bean, Filter...)
   * @see BeanHistory#addAll(Bean[], Filter...)
   * @see BeanHistory#addAll(Collection, Filter[])
   */
  public boolean addAll(BeanHistory<T> beanHistory, Filter<T>... filters);

  /**
   * Removes all beans from this history.
   */
  public void clear();

  /**
   * Determines whether this history contains the specified bean.
   * @param bean a Bean object used to test for containment by this history.
   * @return a boolean value indicating whether this history object contains the specified bean.
   * @see BeanHistory#contains(Filter...)
   */
  public boolean contains(T bean);

  /**
   * Determines whether this history contains a bean satisfying the specified filter.
   * @param filters an array of Filter object used as criteria in determining whether this history contains a bean
   * satisfying the criteria of the filter.
   * @return a boolean value indicating if this history contains at least one bean satisfying the criteria
   * of the filter.
   * @see BeanHistory#contains(Bean)
   */
  public boolean contains(Filter<T>... filters);

  /**
   * Gets the first bean in the history that satisfies the criteria of the specified filter.
   * @param filters an array of Filter objects used to specify the criteria of the bean selection.
   * @return a Bean object in the history which satisfies the criteria of the filter, or null if no bean
   * satisfies the criteria of the filter.
   * @see BeanHistory#getAll(com.cp.common.util.Filter[]))
   * @see BeanHistory#getById(Object)
   */
  public T get(Filter<T>... filters);

  /**
   * Gets the all beans in the history that satisfy the criteria of the specified filter.
   * @param filters an array of Filter objects used to specify the criteria of the bean selection.
   * @return a collection of Bean objects in the history which satisfy the criteria of the filter, or
   * an empty collection if no beans satisfy the criteria of the filter.
   * @see BeanHistory#get(com.cp.common.util.Filter[]))
   * @see BeanHistory#getById(Object)
   */
  public Collection<T> getAll(Filter<T>... filters);

  /**
   * Gets a specified bean from this history by id.
   * @param id an Object specifying the identifier of the bean to retrieve from history.
   * @return a Bean of type T identified by the specified id, or null if no bean in history has the specified id.
   * @see BeanHistory#get(Filter...)
   * @see BeanHistory#getAll(Filter...)
   * @see com.cp.common.util.Filter
   */
  public T getById(Object id);

  /**
   * Determines whether this specified history contains any beans.
   * @return a boolean value indicating whether this history contains any beans.
   * @see BeanHistory#size(Filter[])
   */
  public boolean isEmpty();

  /**
   * Iterates over a subset of the beans in this history satisfying the given filter.
   * @param filters an array of Filter objects used to filter the beans in this history in which to iterate.
   * @return an Iterator over a subset of the beans in this history which satisfy the specified filter.
   */
  public Iterator<T> iterator(Filter<T>... filters);

  /**
   * Removes the specified bean from this history.
   * @param bean the Bean object being removed from this history object.
   * @return a boolean value indicating whether the specified bean was successfully removed from this history object.
   * @see BeanHistory#remove(Filter[])
   * @see BeanHistory#removeAll(Bean[])
   * @see BeanHistory#removeAll(Collection)
   */
  public boolean remove(T bean);

  /**
   * Removes beans from this history that satisfy the given filter array.
   * @param filters an array of Filter objects used to filter and remove beans from this history.
   * @return a boolean value if any bean was removed from the history satisfying the filter.
   * @see BeanHistory#remove(Bean)
   * @see BeanHistory#removeAll(Bean[])
   * @see BeanHistory#removeAll(Collection)
   */
  public boolean remove(Filter<T>... filters);

  /**
   * Removes the specified beans in the array from this history.
   * @param beans an array of Bean objects being removed from this history object.
   * @return a boolean value indicating whether the beans were successfully removed from this history object.
   * @see BeanHistory#remove(Bean)
   * @see BeanHistory#remove(Filter[])
   * @see BeanHistory#removeAll(Collection)
   */
  public boolean removeAll(T... beans);

  /**
   * Removes the specified beans in the collection from this history.
   * @param beans a collection of Bean objects being removed from this history object.
   * @return a boolean value indicating whether the beans were successfully removed from this history object.
   * @see BeanHistory#remove(Bean)
   * @see BeanHistory#remove(Filter[])
   * @see BeanHistory#removeAll(Bean[])
   */
  public boolean removeAll(Collection<T> beans);

  /**
   * Gets the number of beans in the history satisfying the specified filter.
   * @param filters an array of Filter objects used in determining the number of bean satisfying the filter.
   * @return an integer value specifying the number of beans in the history which satisfy the specified filter.
   * @see BeanHistory#isEmpty()
   */
  public int size(Filter<T>... filters);

}
