/*
 * MockMutableObject.java (c) 17 October 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @see com.cp.common.lang.Mutable
 * @see com.cp.common.lang.Visitable
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Mutable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MockMutableObject implements Mutable, Visitable {

  private boolean mutable = Mutable.MUTABLE;

  protected final Log logger = LogFactory.getLog(getClass());

  private MockMutableObject obj;

  public MockMutableObject() {
  }

  public MockMutableObject(final MockMutableObject obj) {
    this.obj = obj;
  }

  public boolean isMutable() {
    return mutable;
  }

  public void setMutable(boolean mutable) {
    this.mutable = mutable;
  }

  public void accept(final Visitor visitor) {
    visitor.visit(this);

    if (ObjectUtil.isNotNull(obj)) {
      obj.accept(visitor);
    }
  }

  public String toString() {
    final StringBuffer buffer = new StringBuffer("{mutable = ");
    buffer.append(isMutable());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
