/*
 * VisitableCollection.java (c) 15 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.15
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.util;

import com.cp.common.lang.Visitable;

public interface VisitableCollection<T extends Visitable> {

  /**
   * Accepts a Visitor object to visit the Visitable objects in this collection.
   * @param visitor the Visitor object used to visit the Visitable objects in this collection.
   */
  public void accept(Visitor visitor);

  /**
   * Adds the specified Visitable object to this collection of Visitable objects.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the Visitable object was successfully added to this
   * collection of Visitable objects.
   * @see VisitableCollection#remove(Visitable)
   */
  public boolean add(T visitableObject);

  /**
   * Determines whether the specified Visitable object is a member of this Visitable object collection.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the specified Visitable object is a member of this
   * Visitable object collection.
   */
  public boolean contains(T visitableObject);

  /**
   * Removes the specified Visitable object from this collection of Visitable objects.
   * @param visitableObject an object implementing the Visitable interface.
   * @return a boolean value indicating whether the Visitable object was successfully removed from this
   * collection of Visitable objects.
   * @see VisitableCollection#add(Visitable)
   */
  public boolean remove(T visitableObject);

  /**
   * Determines whether this Visitable object collection is empty.
   * @return a boolean value indicating if this Visitable object collection is empty or not.
   */
  public boolean isEmpty();

  /**
   * Gets the number of Visitable objects in this collection.
   * @return an integer value specifying the number of Visitable objects in this collection.
   */
  public int size();

}
