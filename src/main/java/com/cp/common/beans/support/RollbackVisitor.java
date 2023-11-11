/*
 * RollbackVisitor.java (c) 24 July 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.Bean
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.beans.support;

import com.cp.common.beans.Bean;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

public class RollbackVisitor implements Visitor {

  /**
   * Visits each Bean object in the object graph calling rollback to discard the changes.
   * @param obj the Object being visited by this Visitor.
   */
  public void visit(final Visitable obj) {
    if (obj instanceof Bean) {
      final Bean bean = (Bean) obj;
      bean.rollback();
    }
  }

}
