/*
 * DestroyableVisitor.java (c) 13 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.13
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Destroyable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

public class DestroyableVisitor implements Visitor {

  /**
   * Visits all Destroyable objects in an object graph calling their destroy method.
   * @param visitableObject the Destroyable object who's destroy method will be called.
   */
  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof Destroyable) {
      ((Destroyable) visitableObject).destroy();
    }
  }

}
