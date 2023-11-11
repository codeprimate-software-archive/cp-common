/*
 * BinarySearch.java (c) 12 May 2002
 *
 * Copyright (c) 2001, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 * @see com.cp.common.lang.Searchable
 * @see com.cp.common.util.search.Searcher
 * @see com.cp.common.util.search.AbstractSearcher
 * @see com.cp.common.util.search.BinarySearchFilter
 */

package com.cp.common.util.search;

import com.cp.common.lang.Searchable;
import org.apache.log4j.Logger;

public class BinarySearch extends AbstractSearcher<BinarySearchFilter> {

  private static final Logger logger = Logger.getLogger(BinarySearch.class);

  /**
   * Constructs a new BinarySearch object with the specified
   * filter. The filter is used to locate the object in the
   * collection matching the criteria as defined by the filter.
   * Note, that the BinarySearch class assumes that the
   * elements of the Searchable Collection are ordered.  If not,
   * the results of the search are undetermined.
   *
   * @param filter: jjb.toolbox.util.SearchFilterIF used to
   * determine set membership of the objects in the collection
   * matching the search criteria.
   */
  public BinarySearch(final BinarySearchFilter filter) {
    super(filter);
  }

  /**
   * Performs binary search against a collection of objects
   * that are Searchable.  It is assumed that the collection
   * of objects are presorted when this method is called.
   *
   * @param collection jjb.toolbox.land.Searchable object in
   * which to search, and that implements the Searchable
   * interface.
   * @param startIndex is an integer value specifying the
   * beginning index into the data structure to begin the
   * search.
   * @param endIndex is an integer value specifying the end
   * index into the date structure to stop searching for the
   * desired object.
   * @return a java.lang.Object representing the found
   * element in the collection or null if the element is not
   * found.
   * @throws com.cp.common.util.search.SearchException if the search
   * operation fails.
   * @see search
   */
  public Object binarySearch(final Searchable collection, int startIndex, int endIndex) throws SearchException {
    if (logger.isDebugEnabled()) {
      logger.debug("startIndex (" + startIndex + ")");
      logger.debug("endIndex (" + endIndex + ")");
    }

    if (startIndex > endIndex) {
      return null;
    }

    final int compareIndex = (int) Math.round(((double) (endIndex + startIndex)) / 2.0);

    if (logger.isDebugEnabled()) {
      logger.debug("compareIndex (" + compareIndex + ")");
    }

    final Object element = collection.get(compareIndex);

    if (logger.isDebugEnabled()) {
      logger.debug("element (" + element + ")");
    }

    final int compareResult = getSearchFilter().compare(element);

    if (logger.isDebugEnabled()) {
      logger.debug("compareResult (" + compareResult + ")");
    }

    if (compareResult == 0) {
      return element;
    }
    else if (compareResult < 0) {
      return binarySearch(collection, startIndex, compareIndex - 1);
    }
    else {
      return binarySearch(collection, compareIndex + 1, endIndex);
    }
  }

  /**
   * Default method invoked to perform a binary searh operation
   * It uses the default begin and end indexes into the data
   * structure.
   *
   * @param collection jjb.toolbox.land.Searchable object in
   * which to search, and that implements the Searchable
   * interface.
   * @return a java.lang.Object representing the found element
   * in the collection or null if the element is not found.
   * @throws com.cp.common.util.search.SearchException if the search
   * operation fails.
   * @see binarySearch
   */
  public Object search(final Searchable collection) throws SearchException {
    logger.debug("Searching Searchable collection...");
    if (logger.isDebugEnabled()) {
      logger.debug("Searchable Collection: " + collection);
    }
    return binarySearch(collection, 0, collection.size() - 1);
  }

}
