/*
 * ComposableVisitor.java (c) 26 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.26
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;

public final class ComposableVisitor implements Visitor {

  private final Visitor leftVisitor;
  private final Visitor rightVisitor;

  /**
   * Creates an instance of the ComposableVisitor class with two Visitor objects.
   * @param leftVisitor a Visitor object in the composition.
   * @param rightVisitor a Visitor object in the composition.
   */
  private ComposableVisitor(final Visitor leftVisitor, final Visitor rightVisitor) {
    Assert.notNull(leftVisitor, "The left Visitor in the ComposableVisitor cannot be null!");
    Assert.notNull(rightVisitor, "The right Visitor in the ComposableVisitor cannot be null!");
    this.leftVisitor= leftVisitor;
    this.rightVisitor = rightVisitor;
  }

  /**
   * Composes mulitple Visitor objects into a single, unified, composed Visitor object.
   * @param leftVisitor a Visitor object in the composition.
   * @param rightVisitor a Visitor object in the composition.
   * @return the rightVisitor if the leftVisitor is null, the leftVisitor if the rightVisitor is null
   * or an instance of the ComposableVisitor composed of both the left and right Visitor objects.
   */
  public static Visitor compose(final Visitor leftVisitor, final Visitor rightVisitor) {
    return (ObjectUtil.isNull(leftVisitor) ? rightVisitor :
      (ObjectUtil.isNull(rightVisitor) ? leftVisitor :
      new ComposableVisitor(leftVisitor, rightVisitor)));
  }

  /**
   * Visits the specified Visitable object performing an operation defined by this implementing class
   * on the Visitable object.
   * @param obj the Visitable object visited by this visitor.
   */
  public void visit(final Visitable obj) {
    leftVisitor.visit(obj);
    rightVisitor.visit(obj);
  }

}
