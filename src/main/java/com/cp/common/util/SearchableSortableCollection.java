/*
 * SearchableSortableCollection.java (c) 15 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.26
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Searchable;
import com.cp.common.lang.Sortable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class SearchableSortableCollection extends AbstractCollection implements Searchable, Sortable {

  private static final Logger logger = Logger.getLogger(SearchableSortableCollection.class);

  private final List collection;

  /**
   * Default constructor to create an empty instance of the SearchableSortableCollection class.
   */
  public SearchableSortableCollection() {
    logger.debug("Creating an empty Collection.");
    collection = new ArrayList();
  }

  /**
   * Creates an instance of the SearchableSortableCollection class decorating the specified
   * Collection object to have searching and sorting capabilities.
   * @param collection the Collection object to enhance with searching and sorting
   * capabilities.
   */
  public SearchableSortableCollection(final Collection collection) {
    if (ObjectUtil.isNull(collection)) {
      logger.warn("The collection parameter cannot be null!");
      throw new NullPointerException("The collection parameter cannot be null!");
    }
    if (logger.isDebugEnabled()) {
      logger.debug("collection: " + collection);
    }
    this.collection = new ArrayList(collection);
  }

  /**
   * Adds the specified object to the Collection.
   * @param obj the Object being added to the Collection.
   * @return a boolean value of true if the Object was successfully added
   * to the Collection.
   */
  public boolean add(final Object obj) {
    if (logger.isDebugEnabled()) {
      logger.debug("obj: " + obj);
    }
    return collection.add(obj);
  }

  /**
   * Returns the element at the specified index in the Collection.
   * @param index the index of the element in the Collection to
   * return.
   * @return a element from the Collection at the specified index.
   */
  public Object get(final int index) {
    logger.debug("index = " + index);
    return collection.get(index);
  }

  /**
   * Returns an Iterator of the elements of the Collection.
   * @return a java.util.Iterator.
   */
  public Iterator iterator() {
    return collection.iterator();
  }

  /**
   * Sets the element at the specified index in the Collection to be
   * the specified Object.
   * @param obj the Object to set at index position within the Collection.
   * @param index the index in the Collection in which to set the Object.
   */
  public void set(final Object obj,
                  final int index) {
    if (logger.isDebugEnabled()) {
      logger.debug("obj: " + obj);
      logger.debug("index = " + index);
    }
    collection.set(index, obj);
  }

  /**
   * Returns the size of the Collection (number of elements).
   * @return the number of elements in the Collection.
   */
  public int size() {
    return collection.size();
  }

}
