/*
 * AbstractBeanHistory.java (c) 26 January 2008
 *
 * The AbstractBeanHistory class is an implementation of the BeanHistory interface, which is a container
 * for a collection of beans contained by a single bean.  The AbstractBeanHistory class contains two factory
 * methods for acquiring either a List or Set implementation of the BeanHistory interface.  The AbstractBeanHistory
 * class provides Collection-like behavior and functionality over the collection of beans contained within.
 * In addition to the Collection-like behavior, the AbstractBeanHistory provides filtering capabilities on most of
 * the class's functions.  This filtering behavior gives the user greater control over the addition, removal and
 * containment selection criteria of beans in this BeanHistory.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.beans.AbstractBean
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.beans.BeanHistory
 * @see com.cp.common.util.ComposableFilter
 * @see com.cp.common.util.Filter
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.beans;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.util.ComposableFilter;
import com.cp.common.util.Filter;
import com.cp.common.util.Visitor;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractBeanHistory<T extends Bean> implements BeanHistory<T> {

  protected final Filter<T> defaultBeanFilter = new DefaultBeanFilter();

  protected final Log logger = LogFactory.getLog(getClass());

  private boolean mutable = Mutable.MUTABLE;

  private final Collection<T> beanCollection;

  /**
   * Creates an instance of the AbstractBeanHistory class initialized to the specified Collection object.
   * @param beanCollection the specified Collection object used to contain the bean objects.
   */
  public AbstractBeanHistory(final Collection<T> beanCollection) {
    Assert.notNull(beanCollection, "The bean collection cannot be null!");
    this.beanCollection = beanCollection;
  }

  /**
   * Creates an instance of the AbstractBeanHistory class backed by a List implementation to contain
   * the history of beans.
   * @return an AbstractBeanHistory object using a List to contain the history of beans.
   * @see AbstractBeanHistory#getBeanHistorySet(Comparator)
   */
  public static <T extends Bean> AbstractBeanHistory<T> getBeanHistoryList() {
    return new DefaultBeanHistory<T>(new LinkedList<T>());
  }

  /**
   * Creates an instance of the AbstractBeanHistory class backed by a Set implementation to contain
   * the history of beans.
   * @param beanComparator a Comparator object used to uniquely identify beans in the history.
   * @return an AbstractBeanHistory object using a Set to contain the history of beans.
   * @see AbstractBeanHistory#getBeanHistoryList()
   */
  public static <T extends Bean> AbstractBeanHistory<T> getBeanHistorySet(Comparator<T> beanComparator) {
    beanComparator = ObjectUtil.getDefaultValue(beanComparator, new DefaultBeanComparator<T>());
    return new DefaultBeanHistory<T>(new TreeSet<T>(beanComparator));
  }

  /**
   * Determines whether this bean history object is mutable or not.
   * @return a boolean value indicating whether this bean history object is mutable or not.
   */
  public boolean isMutable() {
    return mutable;
  }

  /**
   * Sets whether this bean history object is mutable or not.
   * @param mutable a boolean value indicating whether this bean history object is mutable or not.
   */
  public void setMutable(final boolean mutable) {
    this.mutable = mutable;
  }

  /**
   * Accepts a Visitor which will visit the beans in this bean history.
   * @param visitor a Visitor object which will visit the beans in this bean history.
   */
  public void accept(final Visitor visitor) {
    visitor.visit(this);

    for (final T bean : beanCollection) {
      bean.accept(visitor);
    }
  }

  /**
   * Adds the specified bean to this history.
   * @param bean the Bean object being added to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the specified bean was successfully added to this history object.
   * @throws NullPointerException if the bean object is null!
   * @throws IllegalStateException if the specified bean's history reference is not null!
   * @see AbstractBeanHistory#addAll(Bean[], com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(java.util.Collection, com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(BeanHistory, Filter[])
   */
  public boolean add(final T bean, final Filter<T>... filters) {
    Assert.state(isMutable(), "The bean history is not mutable!");

    Assert.notNull(bean, "The bean object being added to this bean history cannot be null!");

    Assert.state(ObjectUtil.isNull(bean.getBeanHistory()), "The bean history ("
      + ClassUtil.getClassName(bean.getBeanHistory()) + ") of bean (" + bean.getClass().getName()
      + ") identified by id (" + bean.getId() + ") is not null!");

    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);

    if (theFilter.accept(bean) && beanCollection.add(bean)) {
      bean.setBeanHistory(this);
      return true;
    }

    return false;
  }

  /**
   * Adds the array of bean objects to this history.
   * @param beans an array of Bean objects to add to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the beans were successfully added to this history object.
   * @throws NullPointerException if the bean array is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see AbstractBeanHistory#add(Bean, com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(java.util.Collection, com.cp.common.util.Filter[]) 
   * @see AbstractBeanHistory#addAll(BeanHistory, Filter[])
   */
  public boolean addAll(final T[] beans, final Filter<T>... filters) {
    Assert.state(isMutable(), "The bean history is not mutable!");
    Assert.notNull(beans, "The array of beans to add to this bean history cannot be null!");

    boolean success = true;

    for (final T bean : beans) {
      success &= add(bean, filters);
    }

    return success;
  }

  /**
   * Adds the collection of bean objects to this history.
   * @param beans a collection of Bean objects to add to this history object.
   * @param filters an array Filter objects used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the the beans were successfully added to this history object.
   * @throws NullPointerException if the bean collection is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see AbstractBeanHistory#add(Bean, com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(Bean[], com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(BeanHistory, Filter[])
   */
  public boolean addAll(final Collection<T> beans, final Filter<T>... filters) {
    Assert.state(isMutable(), "The bean history is not mutable!");
    Assert.notNull(beans, "The collection of beans to add to this bean history cannot be null!");

    boolean success = true;

    for (final T bean : beans) {
      success &= add(bean, filters);
    }

    return success;
  }

  /**
   * Adds the bean objects from the specified bean history object to this history.
   * @param beanHistory a BeanHistory objects containing beans to be removed and add to this bean history.
   * @param filters an array Filter object used to constrain or filter beans added to this history.
   * @return a boolean value indicating whether the the beans were successfully added to this history object.
   * @throws NullPointerException if the bean collection is null!
   * @throws IllegalStateException if the any bean's history reference is not null!
   * @see AbstractBeanHistory#add(Bean, com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(Bean[], com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#addAll(java.util.Collection, com.cp.common.util.Filter[])
   */
  public boolean addAll(final BeanHistory<T> beanHistory, final Filter<T>... filters) {
    Assert.state(isMutable(), "The bean history is not mutable!");
    Assert.notNull(beanHistory, "The history of beans to add to this bean hsitory cannot be null!");

    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);
    boolean success = true;

    // NOTE cannot iterate the BeanHistory object directly as this will cause a ConcurrentModificationException
    // on the Iterator when the bean is removed from the BeanHistory in the next line.
    for (final T bean : beanHistory.getAll((Filter<T>[]) null)) {
      if (theFilter.accept(bean) && beanHistory.remove(bean)) {
        success &= add(bean);
      }
    }

    return success;
  }

  /**
   * Removes all beans from this bean history.
   */
  public void clear() {
    Assert.state(isMutable(), "The bean history is not mutable!");

    for (final T bean : beanCollection) {
      bean.setBeanHistory(null);
    }

    beanCollection.clear();
  }

  /**
   * Determines whether this history contains the specified bean.
   * @param bean a Bean object used to test for containment by this history.
   * @return a boolean value indicating whether this history object contains the specified bean.
   * @see AbstractBeanHistory#contains(Filter[])
   */
  public boolean contains(final T bean) {
    for (final T memberBean : beanCollection) {
      if (ObjectUtil.equals(memberBean, bean)) {
        return true;
      }
    }

    return false;
    //return beanCollection.contains(bean);
  }

  /**
   * Determines whether this history contains a bean satisfying the specified filter.
   * @param filters an array of Filter object used as criteria in determining whether this history contains a bean
   * satisfying the criteria of the filter.
   * @return a boolean value indicating if this history contains at least one bean satisfying the criteria
   * of the filter.
   * @see AbstractBeanHistory#contains(Bean)
   */
  public boolean contains(final Filter<T>... filters) {
    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);

    for (final T bean : beanCollection) {
      if (theFilter.accept(bean)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets the first bean in the history that satisfies the criteria of the specified filter.
   * @param filters an array of Filter objects used to specify the criteria of the bean selection.
   * @return a Bean object in the history which satisfies the criteria of the filter, or null if no bean
   * satisfies the criteria of the filter.
   * @see AbstractBeanHistory#getAll(com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#getById(Object)
   */
  public T get(final Filter<T>... filters) {
    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);

    for (final T bean : beanCollection) {
      if (theFilter.accept(bean)) {
        return bean;
      }
    }

    return null;
  }

  /**
   * Gets the all beans in the history that satisfy the criteria of the specified filter.
   * @param filters an array of Filter objects used to specify the criteria of the bean selection.
   * @return a collection of Bean objects in the history which satisfy the criteria of the filter, or
   * an empty collection if no beans satisfy the criteria of the filter.
   * @see AbstractBeanHistory#get(com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#getById(Object)
   */
  public Collection<T> getAll(final Filter<T>... filters) {
    final Collection<T> beans = new LinkedList<T>();
    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);

    for (final T bean : beanCollection) {
      if (theFilter.accept(bean)) {
        beans.add(bean);
      }
    }

    return beans;
  }

  /**
   * Gets a specified bean from this history by id.
   * @param id an Object specifying the identifier of the bean to retrieve from history.
   * @return a Bean of type T identified by the specified id, or null if no bean in history has the specified id.
   * @see AbstractBeanHistory#get(com.cp.common.util.Filter[])
   * @see AbstractBeanHistory#getAll(com.cp.common.util.Filter[])
   * @see com.cp.common.util.Filter
   */
  public T getById(final Object id) {
    return get(new IdBeanFilter(id));
  }

  /**
   * Determines whether this specified history contains any beans.
   * @return a boolean value indicating whether this history contains any beans.
   * @see AbstractBeanHistory#size(Filter[])
   */
  public boolean isEmpty() {
    return beanCollection.isEmpty();
  }

  /**
   * Returns an iterator over the beans of type T in this history.
   * @return an Iterator object over the beans of type T in this history.
   */
  public Iterator<T> iterator() {
    return beanCollection.iterator();
  }

  /**
   * Iterates over a subset of the beans in this history satisfying the given filter.
   * @param filters an array of Filter objects used to filter the beans in this history in which to iterate.
   * @return an Iterator over a subset of the beans in this history which satisfy the specified filter.
   * @see AbstractBeanHistory#getAll(com.cp.common.util.Filter[])
   */
  public Iterator<T> iterator(final Filter<T>... filters) {
    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);
    return getAll(theFilter).iterator();
  }

  /**
   * Removes the specified bean from this history.
   * @param bean the Bean object being removed from this history object.
   * @return a boolean value indicating whether the specified bean was successfully removed from this history object.
   * @see AbstractBeanHistory#remove(Filter[])
   * @see AbstractBeanHistory#removeAll(Bean[])
   * @see AbstractBeanHistory#removeAll(Collection)
   */
  public boolean remove(final T bean) {
    Assert.state(isMutable(), "The bean history is not mutable!");

    if (beanCollection.remove(bean)) {
      bean.setBeanHistory(null);
      return true;
    }

    return false;
  }

  /**
   * Removes beans from this history that satisfy the given filter array.
   * @param filters an array of Filter objects used to filter and remove beans from this history.
   * @return a boolean value if any bean was removed from the history satisfying the filter.
   * @see AbstractBeanHistory#remove(Bean)
   * @see AbstractBeanHistory#removeAll(Bean[])
   * @see AbstractBeanHistory#removeAll(Collection)
   */
  public boolean remove(final Filter<T>... filters) {
    Assert.state(isMutable(), "The bean history is not mutable!");

    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);
    boolean success = false;

    for (final T bean : getAll(theFilter)) {
      success |= remove(bean);
    }

    return success;
  }

  /**
   * Removes the specified beans in the array from this history.
   * @param beans an array of Bean objects being removed from this history object.
   * @return a boolean value indicating whether the beans were successfully removed from this history object.
   * @see AbstractBeanHistory#remove(Bean)
   * @see AbstractBeanHistory#remove(Filter[])
   * @see AbstractBeanHistory#removeAll(Collection)
   */
  public boolean removeAll(final T... beans) {
    Assert.state(isMutable(), "The bean history is not mutable!");

    boolean success = true;

    for (final T bean : beans) {
      success &= remove(bean);
    }

    return success;
  }

  /**
   * Removes the specified beans in the collection from this history.
   * @param beans a collection of Bean objects being removed from this history object.
   * @return a boolean value indicating whether the beans were successfully removed from this history object.
   * @see AbstractBeanHistory#remove(Bean)
   * @see AbstractBeanHistory#remove(Filter[])
   * @see AbstractBeanHistory#removeAll(Bean[])
   */
  public boolean removeAll(final Collection<T> beans) {
    Assert.state(isMutable(), "The bean history is not mutable!");

    boolean success = true;

    for (final T bean : beans) {
      success &= remove(bean);
    }

    return success;
  }

  /**
   * Gets the number of beans in the history satisfying the specified filter.
   * @param filters an array of Filter objects used in determining the number of bean satisfying the filter.
   * @return an integer value specifying the number of beans in the history which satisfy the specified filter.
   * @see AbstractBeanHistory#isEmpty()
   */
  public int size(final Filter<T>... filters) {
    final Filter<T> theFilter = ObjectUtil.getDefaultValue(ComposableFilter.composeAnd(filters), defaultBeanFilter);
    int size = 0;

    for (final T bean : beanCollection) {
      if (theFilter.accept(bean)) {
        size++;
      }
    }

    return size;
    //return beanCollection.size();
  }

  protected static final class DefaultBeanComparator<T extends Bean> implements Comparator<T> {

    public int compare(final T bean0, final T bean1) {
      final Comparable bean0Id = bean0.getId();
      final Comparable bean1Id = bean1.getId();

      return (ObjectUtil.isNull(bean0Id) ? -1 : (ObjectUtil.isNull(bean1Id) ? 1 : bean0Id.compareTo(bean1Id)));
    }
  }

  /**
   * The DefaultBeanFilter class implements the Filter interface accepting all instances of the Bean interface.
   */
  protected final class DefaultBeanFilter implements Filter<T> {

    public boolean accept(final T bean) {
      return true;
    }
  }

  /**
   * The DefaultBeanHistory class is the defualt implementation of the AbstractBeanHistory class.
   * @see AbstractBeanHistory
   */
  protected static final class DefaultBeanHistory<T extends Bean> extends AbstractBeanHistory<T> {

    public DefaultBeanHistory(final Collection<T> beanCollection) {
      super(beanCollection);
    }
  }

  /**
   * The IdBeanFilter class filters beans in the bean history by id returning the one bean instance
   * uniquely identified by the specified identifier.
   */
  protected final class IdBeanFilter implements Filter<T> {

    private final Object id;

    public IdBeanFilter(final Object id) {
      Assert.notNull(id, "The id cannot be null!");
      this.id = id;
    }

    protected Object getId() {
      return id;
    }

    public boolean accept(final T bean) {
      return ObjectUtil.equals(bean.getId(), getId());
    }
  }

}
