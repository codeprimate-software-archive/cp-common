/*
 * InitializableVisitor.java (c) 13 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.13
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Initializable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

public class InitializableVisitor<T> implements Visitor {

  private final T configuration;

  /**
   * Constructs an instance of the InitializableVisitor class initialized with the specified configuration object
   * of the given type.
   * @param configuration an Object referring to the configuration for a classification of objects.
   */
  public InitializableVisitor(final T configuration) {
    this.configuration = configuration;
  }

  /**
   * Visits all Initializable objects in an object graph calling their initialize methods and passing in the
   * configuration object set on this Visitor.
   * @param visitableObject the Initializable object who's initialize method is called.
   */
  public void visit(final Visitable visitableObject) {
    if (visitableObject instanceof Initializable) {
      ((Initializable<T>) visitableObject).initialize(configuration);
    }
  }

}
