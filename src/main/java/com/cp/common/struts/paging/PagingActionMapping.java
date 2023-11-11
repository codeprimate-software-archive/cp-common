/*
 * PagingActionMapping.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.22
 */

package com.cp.common.struts.paging;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.struts.CPActionMapping;
import org.apache.log4j.Logger;

public class PagingActionMapping extends CPActionMapping {

  // Logger used to track and report the state and operations of objects of this class.
  private static final Logger logger = Logger.getLogger(PagingActionMapping.class);

  // Default increment value if a value is not specified in the struts config file.
  private static final int DEFAULT_INCREMENT = 10;

  // Increment value specifying the number of Collection elements to display on the
  // JSP page, initialized to the DEFAULT_INCREMENT value.
  private int increment = DEFAULT_INCREMENT;

  // PageDirection enumerated type specifying the direction (forward or backward)
  // to scroll in the Collection of elements displayed across multiple JSP pages.
  private PagingDirection pagingDirection = null;

  /**
   * Returns the configured PagingDirection (forward, backward, etc).
   * @return a PagingDirection object as delcared in the configuration file.
   */
  public PagingDirection getDirection() {
    return pagingDirection;
  }

  /**
   * Sets the direction of page movement from the configuration of the ActionMapping.
   * @param direction the String describing the direction of the paging operation,
   * which maps to a PagingDirection type-safe enum.
   * @throws IllegalArgumentException if the configuration of the paging direction
   * is incorrect.
   */
  public void setDirection(final String direction) {
    logger.debug("direction (" + direction + ")");
    final PagingDirection pagingDirection = PagingDirection.getPagingDirection(direction);

    if (ObjectUtil.isNull(direction)) {
      logger.warn(direction + " is not a valid direction.  Please check the mapping for the action.");
      throw new IllegalArgumentException(direction + " is not a valid direction.  Please check the mapping for the action.");
    }

    this.pagingDirection = pagingDirection;
  }

  /**
   * Returns the number of Collection elements to appear on the JSP page.
   * @return a integer value stating the number of Collection elements to appear on the JSP page.
   */
  public int getIncrement() {
    return increment;
  }

  /**
   * Sets the number of Collection elements to appear on the page as a String value.  The parameter is
   * of type String since the increment property may not be specified in the Struts configuration file
   * for the ActionMapping.  The increment property is not specified in the Struts configuration file,
   * then the DEFAULT_INCREMENT value is used.
   * @param increment a String containing a numerical value representing the number of Collection elements
   * that should appear on the JSP page.
   */
  public void setIncrement(String increment) {
    logger.debug("increment (" + increment + ")");
    try {
      this.increment = (ObjectUtil.isNull(increment) ? DEFAULT_INCREMENT : Integer.parseInt(increment.trim()));
    }
    catch (Exception ignore) { // handle NumberFormatException
      logger.warn("(" + increment + ") is not a valid numerical value for the number of Collection elements to appear on a page.");
      throw new IllegalArgumentException("(" + increment + ") is not a valid numerical value for the number of Collection elements to appear on a page.");
    }
  }

}
