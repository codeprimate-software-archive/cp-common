/*
 * SortableListView.java (c) 19 October 2003
 *
 * NOTE: the AbstractList.iterator methods eat the ConcurrentModificationException
 * when the List is modified.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.4.25
 */

package com.cp.common.util;

import com.cp.common.lang.ObjectUtil;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class SortableListView extends AbstractList {

  // Logger used to record and track the state and operations of this List object.
  private static final Logger logger = Logger.getLogger(SortableListView.class);

  // The List of pointers into the original list.
  private final List indexList;

  // The original list
  private final List theList;

  /**
   * Creates a instance of the SortableListView class as a view to the
   * List parameter.  The elements of the original list maintain their
   * original order.
   * @param list the List for which the view is being created.
   */
  public SortableListView(final List list) {
    if (ObjectUtil.isNull(list)) {
      logger.warn("The List cannot be null!");
      throw new NullPointerException("The List cannot be null!");
    }

    theList = new ArrayList(list);
    indexList = new ArrayList(list.size());

    for (int index = 0, size = theList.size(); index < size; index++) {
      indexList.add(new Integer(index));
    }
  }

  /**
   * Inserts the specified element at the specified position in this list.
   *
   * NOTE: this implementation of the add method throws a IllegalStateException
   * the List and the index List are out of sync.
   *
   * @param index the index in the List to add the element.
   * @param element is the Object to add to the List at index.
   */
  public void add(final int index, final Object element) {
    logger.debug("Calling add(I,:Object)");
    if (logger.isDebugEnabled()) {
      logger.debug("index = " + index);
      logger.debug("element (" + element + ")");
    }

    verifyState();
    if (theList.add(element)) {
      indexList.add(index, new Integer(theList.size() - 1));
      // NOTE: we do not need to adjust the index List value since the elements
      // are being added (appended) to the end of the source List.
    }
  }

  /**
   * Returns the element at the specified position in this list.
   * @param index the index of the element to return in the source List.
   * @return the element at the specified index in the source List.
   */
  public Object get(final int index) {
    logger.debug("Calling get(I)");
    logger.debug("index = " + index);
    return theList.get(((Integer) indexList.get(index)).intValue());
  }

  /**
   * Removes the element at the specified position in this list.
   * @param index the index of the element in the source List to remove.
   * @return the element at index that has been removed from the source List.
   */
  public Object remove(final int index) {
    logger.debug("Calling remove(I)");
    logger.debug("index = " + index);

    final int actualIndex = ((Integer) indexList.get(index)).intValue();
    logger.debug("actualIndex = " + actualIndex);

    final Object element = theList.remove(actualIndex);
    if (logger.isDebugEnabled()) {
      logger.debug("element (" + element + ")");
    }

    indexList.remove(index);

    // Adjust all indices in the indexList after removed element in the source
    // List object.
    for (int indx = indexList.size(); --indx >= 0; ) {
      final Integer indice = (Integer) indexList.get(indx);
      if (indice.intValue() > actualIndex) {
        indexList.set(indx, new Integer(indice.intValue() - 1));
      }
    }

    return element;
  }

  /**
   * Replaces the element at the specified position in this list with the specified element.
   * Note that the SortableListView implementation of the set method in the List interface
   * will replace the element at index in the list if the element parameter already exists.
   * The pointer to the element at index is repositioned to refer to the element parameter
   * already in the list.  If the element does not exist, then the element at index in the
   * source List is replaced by the element parameter at the index will refer to the new
   * element.
   * @param index the index into the pointer List.
   * @param element the element to set at index in the List.
   * @return the origiginal element at index in the source List.
   */
  public Object set(final int index, final Object element) {
    logger.debug("Calling set(I,:Object)");
    if (logger.isDebugEnabled()) {
      logger.debug("index = " + index);
      logger.debug("element (" + element + ")");
    }

    final int actualIndex = theList.indexOf(element);
    if (actualIndex != -1) {
      final Object previousElement = theList.get(((Integer) indexList.get(index)).intValue());
      indexList.set(index, new Integer(actualIndex));
      return previousElement;
    }
    return theList.set(((Integer) indexList.get(index)).intValue(), element);
  }

  /**
   * Returns the number of elements in the source List.
   * @return the number of elements in the source List.
   */
  public int size() {
    logger.debug("Calling size");
    return theList.size();
  }

  /**
   * Verifies that the source List and the index List contain the same number of
   * contents.
   * @throws IllegalStateException if the source List and index List get out of sync.
   */
  private void verifyState() {
    logger.debug("Calling verifyState");
    if (indexList.size() != theList.size()) {
      logger.warn("The index List is not in sync with the source List!");
      throw new IllegalStateException("The index List is not in sync with the source List!");
    }
  }

}
