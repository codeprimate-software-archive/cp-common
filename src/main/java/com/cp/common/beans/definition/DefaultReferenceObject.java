/*
 * DefaultReferenceObject.java (c) 27 April 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.4.27
 * @see com.cp.common.beans.definition.AbstractReferenceObject
 */

package com.cp.common.beans.definition;

public class DefaultReferenceObject extends AbstractReferenceObject {

  /**
   * Creates an instance of the DefaultReferenceObject class initialized with the specified reference id
   * and Class type.
   * @param referenceId a String value specifying the id reference of the Object being referred.
   * @param type a Class object specifing the referred Object's type.
   */
  public DefaultReferenceObject(final String referenceId, final Class type) {
    super(referenceId, type);
  }

}
