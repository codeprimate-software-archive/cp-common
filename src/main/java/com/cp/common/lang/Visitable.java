/*
 * Visitable.java (c) 3 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.17
 */

package com.cp.common.lang;

import com.cp.common.util.Visitor;

public interface Visitable {

  /**
   * Object implementing this interface defines the accept method to
   * allow the Visitor to perform it's operations on this object by
   * calling the visit method.  This Visitor will then determine if
   * the operation should be applied to this type of object depending
   * on it's function.
   * @param visitor the Visitor with the operation to perform on this
   * object.
   */
  public void accept(Visitor visitor);

}
