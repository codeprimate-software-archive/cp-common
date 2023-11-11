/*
 * SortActionForm.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 */

package com.cp.common.struts.sorting;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Sortable;
import com.cp.common.struts.CPActionForm;
import org.apache.log4j.Logger;

public abstract class SortActionForm extends CPActionForm {

  private static final Logger logger = Logger.getLogger(SortActionForm.class);

  private boolean reverse = false;

  private Sortable collection = null;

  private String sortKey = null;

  /**
   * Determines whether the sort should be in descending order.
   * @return a boolean value indicating whether the sort should be reversed (in descending order).
   */
  public boolean isReverse() {
    return reverse;
  }

  /**
   * Determines wheter the to reverse the sort based on whether the user selected the same or a different sortKey.
   * If the sortKey is the same then reverse the sort, else sort ascending.
   * @param isTheSameSortKey is a boolean value indicating if the user selected the same sortKey again.
   */
  private void setReverse(final boolean isTheSameSortKey) {
    logger.debug("isTheSameSortKey = " + isTheSameSortKey);
    // if sortKey is the same then reverse the sort, else sort ascending.
    reverse = (isTheSameSortKey ? !reverse : false);
  }

  /**
   * Returns the Sortable Collection to be sorted by the SortAction.
   * @return the Sortable Collection.
   */
  public Sortable getSortable() {
    return collection;
  }

  /**
   * Sets the Sortable Collection to be sorted by the SortAction.
   * @param collection the Sortable Collection of elements to sort by SortAction.
   */
  public void setSortable(final Sortable collection) {
    if (logger.isDebugEnabled()) {
      logger.debug("collection (" + collection + ")");
    }
    this.collection = collection;
  }

  /**
   * The specified property in which to order elements of the Sortable Collection.
   * @return the Bean property name in which to sort elements of the specified Sortable Collection.
   */
  public String getSortKey() {
    return sortKey;
  }

  /**
   * Sets the specified Bean property used to order elements of the specified Sortable Collection.
   * @param sortKey the Bean property name used to order elements of the specified Sortable Collection.
   */
  public void setSortKey(final String sortKey) {
    logger.debug("sortKey (" + sortKey + ")");
    setReverse(ObjectUtil.equals(sortKey, this.sortKey));
    this.sortKey = sortKey;
  }

  /**
   * Returns a String description of the SortActionForm instance.
   * @return a String representation of the SortActionForm instance.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{sortable = ");
    buffer.append(getSortable());
    buffer.append(", sortKey = ").append(getSortKey());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
