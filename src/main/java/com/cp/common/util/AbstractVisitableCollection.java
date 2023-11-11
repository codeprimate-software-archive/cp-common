/*
 * AbstractVisitableCollection.java (c) 15 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.15
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.VisitableCollection
 * @see com.cp.common.util.Visitor
 * @see java.util.List
 * @see java.util.Set
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractVisitableCollection<T extends Visitable> implements VisitableCollection<T> {

  private final Collection<T> visitableObjectCollection;

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Creates an instance of the AbstractVisitableCollection initialized with the specified Collection implementation
   * to contain Visitable objects.
   * @param visitableObjectCollection the Collection implementation to contain the Visitable objects.
   */
  protected AbstractVisitableCollection(final Collection<T> visitableObjectCollection) {
    Assert.notNull(visitableObjectCollection, "The Visitable object collection implementation cannot be null!");
    this.visitableObjectCollection = visitableObjectCollection;
  }

  /**
   * Creates an instance of the VisitableCollection using a List implementation.
   * @return a VisitableCollection object containing an ordered collection of Visitable objects.
   * @see AbstractVisitableCollection#getVisitableList(Visitable[])
   * @see AbstractVisitableCollection#getVisitableSet()
   */
  public static <T extends Visitable> VisitableCollection<T> getVisitableList() {
    return new VisitableList<T>();
  }

  /**
   * Creates an instance of the VisitableCollection using a List implementation.
   * @param visitableObjects an array of Visitable objects.
   * @return a VisitableCollection object containing an ordered collection of Visitable objects.
   * @see AbstractVisitableCollection#getVisitableSet()
   */
  public static <T extends Visitable> VisitableCollection<T> getVisitableList(final T... visitableObjects) {
    return new VisitableList<T>(Arrays.asList(visitableObjects));
  }

  /**
   * Creates an instance of the VisitableCollection using a Set implementation.
   * @return a VisitableCollection object containing an unique collection of Visitable objects.
   * @see AbstractVisitableCollection#getVisitableList()
   * @see AbstractVisitableCollection#getVisitableSet(Visitable[])
   */
  public static <T extends Visitable> VisitableCollection<T> getVisitableSet() {
    return new VisitableSet<T>();
  }

  /**
   * Creates an instance of the VisitableCollection using a Set implementation.
   * @param visitableObjects an array of Visitable objects.
   * @return a VisitableCollection object containing an unique collection of Visitable objects.
   * @see AbstractVisitableCollection#getVisitableList()
   */
  public static <T extends Visitable> VisitableCollection<T> getVisitableSet(final T... visitableObjects) {
    return new VisitableSet<T>(Arrays.asList(visitableObjects));
  }

  /**
   * Accepts a Visitor object to visit the Visitable objects in this collection.
   * @param visitor the Visitor object used to visit the Visitable objects in this collection.
   */
  public void accept(final Visitor visitor) {
    Assert.notNull(visitor, "The Visitor object cannot be null!");

    for (final Visitable visitableObject : visitableObjectCollection) {
      visitableObject.accept(visitor);
    }
  }

  /**
   * Adds the specified Visitable object to this collection of Visitable objects.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the Visitable object was successfully added to this
   * collection of Visitable objects.
   * @see AbstractVisitableCollection#remove(Visitable)
   */
  public boolean add(final T visitableObject) {
    return (ObjectUtil.isNotNull(visitableObject) && visitableObjectCollection.add(visitableObject));
  }

  /**
   * Determines whether the specified Visitable object is a member of this Visitable object collection.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the specified Visitable object is a member of this
   * Visitable object collection.
   */
  public boolean contains(final T visitableObject) {
    return visitableObjectCollection.contains(visitableObject);
  }

  /**
   * Removes the specified Visitable object from this collection of Visitable objects.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the Visitable object was successfully removed from this
   * collection of Visitable objects.
   * @see AbstractVisitableCollection#add(Visitable)
   */
  public boolean remove(final T visitableObject) {
    return visitableObjectCollection.remove(visitableObject);
  }

  /**
   * Determines whether this Visitable object collection is empty.
   * @return a boolean value indicating if this Visitable object collection is empty or not.
   */
  public boolean isEmpty() {
    return visitableObjectCollection.isEmpty();
  }

  /**
   * Gets the number of Visitable objects in this collection.
   * @return an integer value specifying the number of Visitable objects in this collection.
   */
  public int size() {
    return visitableObjectCollection.size();
  }

  protected static class VisitableList<T extends Visitable> extends AbstractVisitableCollection<T> {

    public VisitableList() {
      super(new LinkedList<T>());
    }

    public VisitableList(final Collection<T> collection) {
      super(new LinkedList<T>(collection));
    }
  }

  protected static class VisitableSet<T extends Visitable> extends AbstractVisitableCollection<T> {

    public VisitableSet() {
      super(new HashSet<T>());
    }

    public VisitableSet(final Collection<T> collection) {
      super(new HashSet<T>(collection));
    }
  }

}
