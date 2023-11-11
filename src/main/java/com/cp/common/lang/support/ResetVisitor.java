/*
 * ResetVisitor.java (c) 30 December 2006
 *
 * The ResetVisitor walks the object graph, starting with the object who's accept method was called
 * with an instance of this Visitor, in search of object's implementing the Resettable interface.  All object's
 * implementing the Resettable interface will have their reset methods called.
 *
 * It is up to the individual objects in the object graph implementing the Resettable interface to decide what action
 * should be performed in the reset method call.
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResetVisitor implements Visitor {

  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Visits the specified Visitable object, determines if the Visitable object is an instance of Resettable,
   * and if so, resets the internal state of the specified object by calling the object's reset method.
   * @param obj the Visitable/Resettable object who's reset method will be called.
   */
  public void visit(final Visitable obj) {
    if (obj instanceof Resettable) {
      ((Resettable) obj).reset();
    }
  }

}
