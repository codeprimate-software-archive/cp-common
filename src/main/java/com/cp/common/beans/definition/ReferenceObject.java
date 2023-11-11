/*
 * ReferenceObject.java (c) 27 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.8.21
 */

package com.cp.common.beans.definition;

public interface ReferenceObject {

  /**
   * Gets the id reference of some Object defined/declared in a configuration file.
   * @return a String value specifying the id reference of some Object defined/declared in a configuration file.
   */
  public String getReferenceId();

  /**
   * Gets the Class type of the Object referenced by the reference id property.
   * @return a Class object specifying the referenced Object's type.
   */
  public Class getType();

}
