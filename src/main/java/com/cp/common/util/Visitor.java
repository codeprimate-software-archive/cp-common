/*
 * Visitor.java (c) 3 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2003.10.17
 */

package com.cp.common.util;

import com.cp.common.lang.Visitable;

public interface Visitor {

  /**
   * Visits the specified Visitable object performing an operation
   * defined by the implementing class on the Visitable object.
   * @param obj the Visitable object visited by this visitor.
   */
  public void visit(Visitable obj);

}
