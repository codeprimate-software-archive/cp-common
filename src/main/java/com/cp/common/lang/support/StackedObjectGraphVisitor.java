/*
 * StackedObjectGraphVisitor.java (c) 3 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.7.13
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Visitable;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.Visitor;
import java.util.Stack;

public class StackedObjectGraphVisitor implements Visitor {

  private final Stack<Object> objectStack = new Stack<Object>();

  /**
   * Gets the the object stack from this Visitor object.  The stack of objects are formed by visiting an object graph
   * where the first object of visited by this Visitor is on the bottom of the stack and the last object visited
   * by this Visitor is on top of the stack.
   * @return a Stack of objects visited by this Visitor
   */
  public Stack<Object> getObjectStack() {
    return CollectionUtil.copy(objectStack);
  }

  /**
   * Visits an hierarchical object graph recording the objects in the graph visited.
   * @param obj the Object in the hierarchical object graph being visited by this Visitor.
   */
  public void visit(final Visitable obj) {
    objectStack.push(obj);
  }

}
