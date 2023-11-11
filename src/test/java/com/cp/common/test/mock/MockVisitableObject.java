/*
 * MockVisitableObject.java (c) 15 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.5.3
 * @see com.cp.common.lang.Identifiable
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.test.mock;

import com.cp.common.lang.Assert;
import com.cp.common.lang.Identifiable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;

public class MockVisitableObject implements Identifiable<Integer>, Resettable, Visitable {

  private boolean visited;

  private Integer id;

  private final MockVisitableObject mockVisitableObject;

  public MockVisitableObject(final Integer id) {
    this(id, null);
  }

  public MockVisitableObject(final Integer id, final MockVisitableObject mockVisitableObject) {
    setId(id);
    this.mockVisitableObject = mockVisitableObject;
  }

  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    Assert.notNull(id, "The id cannot be null!");
    this.id = id;
  }

  public MockVisitableObject getMockVisitableObject() {
    return mockVisitableObject;
  }

  public boolean isVisited() {
    return visited;
  }

  public void setVisited(final boolean visited) {
    this.visited = visited;
  }

  public void accept(final Visitor visitor) {
    visitor.visit(this);

    if (ObjectUtil.isNotNull(getMockVisitableObject())) {
      getMockVisitableObject().accept(visitor);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof MockVisitableObject)) {
      return false;
    }

    final MockVisitableObject that = (MockVisitableObject) obj;

    return ObjectUtil.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    int hashValue = 17;
    hashValue = 37 * hashValue + ObjectUtil.hashCode(getId());
    return hashValue;
  }

  public void reset() {
    setVisited(false);
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer("{id = ");
    buffer.append(getId());
    buffer.append(", visited = ").append(isVisited());
    buffer.append("}:").append(getClass().getName());
    return buffer.toString();
  }

}
