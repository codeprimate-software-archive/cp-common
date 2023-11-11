/*
 * MutableVisitor.java (c) 10 September 2003
 *
 * The MutableVisitor walks the object graph, starting with the object who's accept method was called
 * with an instance of this Visitor, in search of object implementing the Mutable interface to set the state of their
 * mutable property appropriately as initialized using this Visitor.
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.6.1
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.ClassUtil;
import com.cp.common.lang.Mutable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MutableVisitor implements Visitor {

  protected final Log log = LogFactory.getLog(getClass());

  private final boolean mutable;

  /**
   * Constructs an instance of the MutableVisitor with a default
   * mutable property value equal to MutableConstansts.MUTABLE.
   */
  public MutableVisitor() {
    this(Mutable.MUTABLE);
  }

  /**
   * Constructs an instance of the MutableVisitor class initializing
   * the mutable property to the specified boolean value.
   * @param mutable the boolean value to initialize the mutable
   * property.
   */
  public MutableVisitor(final boolean mutable) {
    this.mutable = mutable;
  }

  /**
   * Returns whether the mutable property of this Visitor
   * is Mutable.MUTABLE or Mutable.IMMUTABLE.
   * @return a boolean value indicating whehter the Visitor's
   * mutable property is set to mutable (modifyiable) or
   * immutable (read-only).
   */
  public final boolean isMutable() {
    return mutable;
  }

  /**
   * Returns a String representation of this MutableVisitor.
   * @return a String representation of thsi MutableVisitor.
   */
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{mutable = ");
    buffer.append(isMutable());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

  /**
   * Visits the specified Mutable, Visitable object setting the value of the object's mutable property
   * to that of the Visitor's mutable property.
   * @param obj the Visitable object which is also Mutable.
   */
  public void visit(final Visitable obj) {
    if (obj instanceof Mutable) {
      ((Mutable) obj).setMutable(isMutable());
    }
    else if (!isMutable()) {
      log.warn("Cannot make objects of class (" + ClassUtil.getClassName(obj) + ") immutable!");
      throw new IllegalArgumentException("Cannot make objects of class (" + ClassUtil.getClassName(obj) + ") immutable!");
    }
  }

}
