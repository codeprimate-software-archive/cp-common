/*
 * MockVisitor.java (c) 15 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.15
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

public class MockVisitor implements Visitor {

  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof MockVisitableObject) {
      ((MockVisitableObject) visitableObject).setVisited(true);
    }
  }

}
