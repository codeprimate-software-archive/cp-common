/*
 * Scope.java (c) 16 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.8.21
 */

package com.cp.common.beans.definition;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;

public enum Scope {
  PROTOTYPE(1, "prototype"),
  SINGLETON(2, "singleton");

  private final Integer id;
  private final String description;

  /**
   * Creates an instance of the Scope enum initialized with the specified id and description.
   * @param id the unique identifier for this Scope instance.
   * @param description a String value describing this Scope instance.
   */
  Scope(final Integer id, final String description) {
    Assert.notNull(id, "The scope id cannot be null!");
    Assert.notEmpty(description, "The scope description cannot be null or empty!");
    this.id = id;
    this.description = description;
  }

  /**
   * Gets a Scope enumerated value by description or returns null if no Scope instance has the specified description.
   * @param description a String value describing the Scope instance.
   * @return a Scope enumerated value for the specified description.
   */
  public static Scope getScopeByDescription(String description) {
    description = StringUtil.toLowerCase(description);

    for (final Scope scope : values()) {
      if (ObjectUtil.equals(scope.getDescription(), description)) {
        return scope;
      }
    }

    return null;
  }

  /**
   * Gets a Scope enumerated value by id or returns null if no Scope instance has the specified id.
   * @param id an Integer value uniquely identifying the Scope instance.
   * @return a Scope enumerated value for the specified id.
   */
  public static Scope getScopeById(final Integer id) {
    for (final Scope scope : values()) {
      if (ObjectUtil.equals(scope.getId(), id)) {
        return scope;
      }
    }

    return null;
  }

  /**
   * Gets the Scope's description.
   * @return a String value describing the Scope enumerated value.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the Scope's unique identifier.
   * @return an Integer value uniquely identifying the Scope enumerated value.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Gets a String value describing the state of this Scope instance.
   * @return a String description of the Scope's state.
   */
  public String toString() {
    return getDescription();
  }

}
