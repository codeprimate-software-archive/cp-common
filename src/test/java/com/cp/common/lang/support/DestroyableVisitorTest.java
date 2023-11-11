/*
 * DestroyableVisitorTest.java (c) 13 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.13
 * @see com.cp.common.lang.support.DestroyableVisitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Destroyable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DestroyableVisitorTest extends TestCase {

  public DestroyableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DestroyableVisitorTest.class);
    //suite.addTest(new DestroyableVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final MockVisitable obj1 = new MockDestroyableVisitable(null);
    final MockVisitable obj2 = new MockVisitable(obj1);
    final MockVisitable obj3 = new MockDestroyableVisitable(obj2);

    assertFalse(obj1.isDestroyCalled());
    assertFalse(obj2.isDestroyCalled());
    assertFalse(obj3.isDestroyCalled());

    obj3.accept(new DestroyableVisitor());

    assertTrue(obj1.isDestroyCalled());
    assertFalse(obj2.isDestroyCalled());
    assertTrue(obj3.isDestroyCalled());
  }

  private static class MockVisitable implements Visitable {

    private boolean destroyCalled = false;

    private final Visitable visitableObject;

    public MockVisitable(final Visitable visitableObject) {
      this.visitableObject = visitableObject;
    }

    public boolean isDestroyCalled() {
      return destroyCalled;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(visitableObject)) {
        visitableObject.accept(visitor);
      }
    }

    public void destroy() {
      destroyCalled = true;
    }
  }

  private static class MockDestroyableVisitable extends MockVisitable implements Destroyable {

    public MockDestroyableVisitable(final Visitable visitableObject) {
      super(visitableObject);
    }

    public boolean isDestroyed() {
      throw new UnsupportedOperationException("Not Implemented");
    }
  }

}
