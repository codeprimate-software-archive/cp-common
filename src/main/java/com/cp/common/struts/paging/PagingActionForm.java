/*
 * PagingActionForm.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.1.27
 */

package com.cp.common.struts.paging;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.struts.CPActionForm;
import java.util.Collection;
import org.apache.log4j.Logger;

public abstract class PagingActionForm extends CPActionForm {

  private static final Logger logger = Logger.getLogger(PagingActionForm.class);

  // The minimum value acceptable for the increment property.
  private static final int INCREMENT_THRESHOLD = 1;

  // Page constants used to refer to the properties of thie CXActionForm object.
  public static final String DIRECTION_KEY = "direction";
  public static final String LENGTH_KEY = "length";
  public static final String OFFSET_KEY = "offset";

  // The maximum number of elements from the Collection to view on the JSP page.
  private int increment = 0;

  // The actual number of elements that will be viewed on the JSP page, which can be less than the increment.
  private int length = 0;

  // The number of elements from the beginning of the Collection in which to start viewing elements.
  private int offset = 0;

  // The Collection of elements which will be viewed across multiple JSP pages.
  private Collection collection = null;

  // PageDirection enumerated type specifying the direction (forward or backward) to scroll in the Collection
  // of elements viewed across multiple JSP pages.
  private PagingDirection pagingDirection = PagingDirection.SAME;

  /**
   * Returns the Collection of objects to be viewed across multiple pages of a JSP.
   * @return the Collection to view across multiple pages.
   */
  public Collection getCollection() {
    return collection;
  }

  /**
   * Sets the Collection that will be viewed across multiple pages of a JSP.
   * @param collection the Collection of objects to view across multiple pages.
   */
  public void setCollection(final Collection collection) {
    if (logger.isDebugEnabled()) {
      logger.debug("collection type (" + collection.getClass().getName() + ")");
    }
    this.collection = collection;
  }

  /**
   * Returns the number of elements contained in the Collection.
   * @return an integer value specifying the number of elements in the Collection.
   */
  public int getCollectionSize() {
    return (ObjectUtil.isNull(getCollection()) ? 0 : getCollection().size());
  }

  /**
   * Returns the page number of the currently viewed JSP page.  This currentPage property value is a
   * function of the offset and value of the increment property.
   * @return an integer value specifying page number of the JSP page currently in view.
   * @see PagingActionForm#getPageCount
   */
  public int getCurrentPage() {
    return (getOffset() / getIncrement()) + 1;
  }

  /**
   * Returns the String value specifying the direction in which the page moves (forward, backward, etc).
   * @return a String value indicating the direction of the page movement.
   * @see PagingActionForm#getPagingDirection
   */
  public String getDirection() {
    return (ObjectUtil.isNull(pagingDirection) ? "" : pagingDirection.getDescription());
  }

  /**
   * Sets a String value specifying the direction of the page movement (forward, backward, etc).
   * @param direction a String value indicating the direction of the page movement.
   * @see PagingActionForm#setPagingDirection
   */
  public void setDirection(final String direction) {
    logger.debug("direction (" + direction + ")");
    setPagingDirection(PagingDirection.getPagingDirection(direction));
  }

  /**
   * Returns the maximum number of elements from the Collection to view per page.
   * @return a integer value specifying the maximum number of elements to view per page.
   */
  final int getIncrement() {
    if (increment < INCREMENT_THRESHOLD) {
      logger.warn("The increment property has not been initialized properly; "
        + "make sure the PagingAction is called before viewing the paged Collection.");
      throw new IllegalStateException("The increment property has not been initialized properly; "
        + "make sure the PagingAction is called before viewing the paged Collection.");
    }
    return increment;
  }

  /**
   * Sets the maximum number of elements from the Collection that will be viewed per page.
   * @param increment an integer value specifying the maximum number of elements to view per page.
   */
  final void setIncrement(final int increment) {
    logger.debug("increment = " + increment);
    if (increment < INCREMENT_THRESHOLD) {
      logger.warn("The configuration/value of the increment property cannot be less than the threshold " + INCREMENT_THRESHOLD);
      throw new IllegalArgumentException("The configuration/value of the increment property cannot be less than the threshold "
        + INCREMENT_THRESHOLD);
    }
    this.increment = increment;
  }

  /**
   * Returns a numerical value specifying the actual number of the elements from the Collection that will be
   * viewed on page.
   * @return an integer value specifying the actual number of elements from the Collection that will be
   * viewed on page.
   */
  public int getLength() {
    return length;
  }

  /**
   * Sets the actual number of elements from the Collection to view on the page.
   * @param length an integer value specifying the actual number of elements that will be viewed on the
   * page.
   */
  public void setLength(final int length) {
    logger.debug("length = " + length);
    this.length = length;
  }

  /**
   * Returns an index, which refers to the number of elements from the beginning of the Collection, at which
   * to start viewing elements from the Collection.
   * @return an integer index specifying the offset from the beginning of the Collection at which to start
   * viewing elements.
   */
  public int getOffset() {
    return offset;
  }

  /**
   * Sets an index specifying is the number of elements from the beginning of the Collection to start viewing
   * elements of the Collection.
   * @param offset is an integer index specifying the offset from the beginning of the Collection at which
   * to start viewing elements.
   */
  public void setOffset(final int offset) {
    logger.debug("offset = " + offset);
    this.offset = offset;
  }

  /**
   * Returns the number of pages as a function of the Collection's size and the increment value.  Thus,
   * adjusting the value of the increment or dpending on the size of the Collection, determines the
   * number of pages for which the elements of the Collection will be partitioned.
   * @return a integer value specifing the number of pages in which to partition the view of the elements
   * in the Collection.
   * @see PagingActionForm#getCurrentPage
   */
  public int getPageCount() {
    final double collectionSize = (getCollectionSize() == 0 ? 1 : getCollectionSize());
    final double increment = getIncrement();
    return (new Double(Math.ceil(collectionSize / increment))).intValue();
  }

  /**
   * API-level method used by PagingAction to get the direction, as a PagingDirection enumerated type,
   * for the requested page movement by the user signified by the value of the direction property.
   * @return a PagingDirection enumerated type value representing the user's requested page movement.
   * @see PagingActionForm#getDirection
   */
  final PagingDirection getPagingDirection() {
    return pagingDirection;
  }

  /**
   * Called by setDirection to internally represent the user's requested page movement with a enumerated
   * type Object value.
   * @param pagingDirection the type-safe enum representing the direction of the page movement (forward,
   * backward, etc).
   */
  final void setPagingDirection(PagingDirection pagingDirection) {
    if (ObjectUtil.isNull(pagingDirection)) {
      pagingDirection = PagingDirection.SAME;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("pagingDirection (" + pagingDirection + ")");
    }
    this.pagingDirection = pagingDirection;
  }

  /**
   * Determines whehter there is another page after the current page.
   * @return a boolean value indicating if there is another page after the current page.
   */
  public boolean isNextPage() {
    return (getCollectionSize() > (getOffset() + getIncrement()));
  }

  /**
   * Determines whether there is a page before the current page.
   * @return a boolean value indicating if there is another page before the currrent page.
   */
  public boolean isPreviousPage() {
    return (getOffset() >= getIncrement());
  }

  /**
   * Return a String containing the state information of this PagingActionForm bean.
   * @return a String representation of this form bean.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{collection = ");
    buffer.append(getCollection());
    buffer.append(", collectionSize = ").append(getCollectionSize());
    buffer.append(", currentPage = ").append(getCurrentPage());
    buffer.append(", direction = ").append(getDirection());
    buffer.append(", length = ").append(getLength());
    buffer.append(", offset = ").append(getOffset());
    buffer.append(", pageCount = ").append(getPageCount());
    buffer.append(", nextPage = ").append(isNextPage());
    buffer.append(", previousPage = ").append(isPreviousPage());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
