package com.cp.common.lang.support;

import com.cp.common.lang.Interruptable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

/**
 * The InterruptVisitor class is...
 * <p/>
 * InterruptVisitor.java (c) 09 12 2010
 * @author jblum
 * @version $Revision: 1.1 $
 */
public class InterruptVisitor implements Visitor {

  public void visit(final Visitable obj) {
    if (obj instanceof Interruptable) {
      ((Interruptable) obj).interrupt();
    }
  }

}
