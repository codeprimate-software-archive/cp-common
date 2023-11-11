/*
 * IsNewVisitor.java (c) 14 August 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.lang.Auditable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Auditable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IsNewVisitor implements Visitor {

  protected final Log logger = LogFactory.getLog(getClass());

  private boolean xnew = false;

  /**
   * Determines whether this Visitor visited any beans in the object graph that were in a new state.
   * @return a boolean value indicating whether the Visitor visited any beans in the object graph that
   * were in a new state.
   */
  public boolean isNew() {
    return xnew;
  }

  /**
   * Sets whether the current bean visited by this Visitor is new or not.
   * @param condition a boolean value inidicating if the current bean visited by this Visitor is new.
   */
  private void setNew(final boolean condition) {
    if (logger.isDebugEnabled()) {
      logger.debug("condition (" + condition + ")");
    }
    xnew |= condition;
  }

  /**
   * Visits beans in an object graph to determine whether they are new.  The object graph consists of beans
   * composed of beans who's accept method is called by the accept method of the enclosing bean.
   * @param visitableObject a bean object visited by this Visitor.
   */
  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof Auditable) {
      final Auditable auditableObject = (Auditable) visitableObject;

      if (logger.isDebugEnabled()) {
        logger.debug("determining if Auditable object (" + auditableObject.getClass().getName() + ") is new.");
      }

      setNew(auditableObject.isNew());
    }
  }

}
