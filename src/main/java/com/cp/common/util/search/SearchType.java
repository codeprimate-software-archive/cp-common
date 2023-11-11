/*
 * SearchType.java (c) 31 March 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.10.26
 */

package com.cp.common.util.search;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;

public final class SearchType {

  private static final Logger logger = Logger.getLogger(SearchType.class);

  public static final String BINARY_SEARCH_DESCRIPTION = "Binary Search";
  public static final String INDEXED_SEARCH_DESCRIPTION = "Indexed Search";
  public static final String LINEAR_SEARCH_DESCRIPTION = "Linear Search";

  public static final SearchType BINARY_SEARCH = new SearchType(1, BINARY_SEARCH_DESCRIPTION);
  public static final SearchType INDEXED_SEARCH = new SearchType(3, INDEXED_SEARCH_DESCRIPTION);
  public static final SearchType LINEAR_SEARCH = new SearchType(2, LINEAR_SEARCH_DESCRIPTION);

  public static final Set SEARCH_TYPE_SET = new HashSet();
  static {
    SEARCH_TYPE_SET.add(BINARY_SEARCH);
    SEARCH_TYPE_SET.add(LINEAR_SEARCH);
    SEARCH_TYPE_SET.add(INDEXED_SEARCH);
  }

  private final int id;
  private final String description;

  /**
   * Default private constructor to enforce non-instantiability.
   * @param id the unique identifier of the Search algorithm.
   * @param description a String description of the Search Algorithm.
   */
  private SearchType(final int id, final String description) {
    this.id = id;
    this.description = description;
  }

  /**
   * Returns a SearchType enumeration based on it's unique identifier, or null if not
   * SearchType type-safe enum matches the unique identifier.
   * @param id a unique identifier of the SearchType.
   * @return a SearchType matching the unique identifier.
   */
  public static SearchType getSearchType(final int id) {
    if (logger.isDebugEnabled()) {
      logger.debug("id = " + id);
    }
    for (Iterator it = SEARCH_TYPE_SET.iterator(); it.hasNext(); ) {
      final SearchType searchType = (SearchType) it.next();
      if (searchType.getId() == id) {
        return searchType;
      }
    }
    return null;
  }

  /**
   * Returns a SearchType enumeration based on it's description, or null if not
   * SearchType type-safe enum matches the description.
   * @param description a String respresentation of the SearchType.
   * @return a SearchType matching the description.
   */
  public static SearchType getSearchType(final String description) {
    logger.debug("description = " + description);
    for (Iterator it = SEARCH_TYPE_SET.iterator(); it.hasNext(); ) {
      final SearchType searchType = (SearchType) it.next();
      if (searchType.getDescription().equals(description)) {
        return searchType;
      }
    }
    return null;
  }

  /**
   * Returns a String describing the SearchType.
   * @return a description of the SearchType.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the unique identifier of the Search algorithm.
   * @return a integer value representing the unique identifier of the Search algorithm.
   */
  public int getId() {
    return id;
  }

  /**
   * Determines whether this SearchType is equaln to some Object, also a SearchType
   * enum.
   * @param obj the Object to determined equality with this SearchType enum.
   * @return a boolean value indiciating whether the specified Object is equal to
   * this SearchType.
   */
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SearchType)) {
      return false;
    }

    final SearchType that = (SearchType) obj;

    return getId() == that.getId()
      && getDescription().equals(that.getDescription());
  }

  /**
   * Computes the hash value of this SearchType.
   * @return a integer value representing the computed hash value of this SearchType.
   */
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + new Integer(getId()).hashCode();
    hashValue = 37 * hashValue + getDescription().hashCode();
    return hashValue;
  }

  /**
   * Returns a String representation of this SearchType containing state information.
   * @return a String representation of this SearchType.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{id = ");
    buffer.append(getId());
    buffer.append(", description = ").append(getDescription());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
