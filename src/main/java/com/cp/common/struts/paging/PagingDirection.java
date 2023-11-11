/*
 * PagingDirection.java (c) 3 May 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2004.5.22
 */

package com.cp.common.struts.paging;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

public final class PagingDirection {

  private static final Logger logger = Logger.getLogger(PagingDirection.class);

  // Predefined constants representing forward and backward paging directions (movements) for viewing
  // portions of a Collection.
  // NOTE: if you are adding new paging directions, make sure to add it to the pagingDirectionList.
  public static final PagingDirection FIRST = new PagingDirection(Integer.MIN_VALUE, 0, "First", false);
  public static final PagingDirection LAST = new PagingDirection(Integer.MAX_VALUE, 0, "Last", true);
  public static final PagingDirection NEXT = new PagingDirection(1, 0, "Next", true);
  public static final PagingDirection PREVIOUS = new PagingDirection(-1, 0, "Previous", false);
  static final PagingDirection SAME = new PagingDirection(0, 0, "Same", true);

  // A Collection of all PagingDirection enumerated types.
  private static final List pagingDirectionList = new ArrayList();

  static {
    pagingDirectionList.add(FIRST);
    pagingDirectionList.add(LAST);
    pagingDirectionList.add(NEXT);
    pagingDirectionList.add(PREVIOUS);
    pagingDirectionList.add(SAME);
  }

  // Indicates if this PagingDirections is a forward direction or not.
  private final boolean forward;

  // Determines the direction (based on sign) and magnitude (based on value) of the movement
  // for paging.
  private final int directionValue;
  private final int directionOffset;

  // Description of the movement defined by this PagingDirection.
  private final String description;

  /**
   * Private constructor used to enforce the non-instantiability property of an enumerated type.
   * All instances of this class enum are specified as public constant class members.  Specific
   * PagingDirection instances can also be obtained using the getPagingDirection factory method
   * to lookup enums by description.
   * @param directionValue an integer value specifying the increment of the page movement.
   * @param directionOffset an integer value specifying an adjustment to the directionvValue.
   * @param description a String describing the PagingDirection enumerated type.
   * @param forward a boolean value indicating whether this enumerated type represents a forward
   * paging movement, true, a backward movement, false.
   * @see PagingDirection#getPagingDirection
   */
  private PagingDirection(final int directionValue,
                          final int directionOffset,
                          final String description,
                          final boolean forward) {
    this.directionValue = directionValue;
    this.directionOffset = directionOffset;
    this.description = description;
    this.forward = forward;
  }

  /**
   * Returns a Collection containing all enumerated types that represent backward paging movements.
   * @return a Set with backward only PageDirection enums.
   * @see PagingDirection#getForwardDirections
   */
  public static Set getBackwardDirections() {
    final Set set = new HashSet();
    for (Iterator it = pagingDirectionList.iterator(); it.hasNext(); ) {
      final PagingDirection pagingDirection = (PagingDirection) it.next();
      if (!pagingDirection.isForward()) {
        set.add(pagingDirection);
      }
    }
    return set;
  }

  /**
   * Returns a Collection containing all enumerated types that represent forward paging movements.
   * @return a Set with forward only PageDirection enums.
   * @see PagingDirection#getBackwardDirections
   */
  public static Set getForwardDirections() {
    final Set set = new HashSet();
    for (Iterator it = pagingDirectionList.iterator(); it.hasNext(); ) {
      final PagingDirection pagingDirection = (PagingDirection) it.next();
      if (pagingDirection.isForward()) {
        set.add(pagingDirection);
      }
    }
    return set;
  }

  /**
   * A factory method returning a PagingDirection enumerated type with the specified description.
   * @param description the String describing the desired PagingDirection enumerated type.
   * @return a PagingDirection enumerated type with a matching description.
   */
  public static PagingDirection getPagingDirection(final String description) {
    logger.debug("description (" + description + ")");
    for (Iterator it = pagingDirectionList.iterator(); it.hasNext();) {
      final PagingDirection pagingDirection = (PagingDirection) it.next();
      if (pagingDirection.getDescription().equals(description)) {
        return pagingDirection;
      }
    }
    return null;
  }

  /**
   * Returns a String value describing this PagingDirection enumerated type.
   * @return a String that describes this PagingDirection instance.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns an numerical value that adjusts, or offsets, the directional value.
   * @return a integer value offset to adjust the directional value.
   * @see PagingDirection#getDirectionValue
   */
  public int getDirectionOffset() {
    return directionOffset;
  }

  /**
   * Returns a numerical value representing the magnitude of the page movement.
   * @return a integer value whose sign represents the direction of the page movement
   * and magnitude represents the distance of the movement.
   * @see PagingDirection#getDirectionOffset
   */
  public int getDirectionValue() {
    return directionValue;
  }

  /**
   * Determines whether this PagingDirection is a forward or backward movement.
   * @return a boolean value indicating if this PagingDirection represents a forward
   * or a backward movement.
   */
  public boolean isForward() {
    return forward;
  }

  /**
   * Returns a String containing the state information for this PagingDirection.
   * @return a String representation of this PagingDirection.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{description = ");
    buffer.append(getDescription());
    buffer.append(", directionOffset = ").append(getDirectionOffset());
    buffer.append(", directionValue = ").append(getDirectionValue());
    buffer.append(", forward = ").append(isForward());
    buffer.append("}:");
    buffer.append(getClass().getName());
    return buffer.toString();
  }

}
