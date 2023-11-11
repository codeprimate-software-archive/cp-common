/*
 * ResetVisitorTest.java (c) 30 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.12.12
 * @see com.cp.common.lang.Resettable
 * @see com.cp.common.lang.Visitable
 * @see com.cp.common.lang.support.ResetVisitor
 * @see com.cp.common.util.Visitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Resettable;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ResetVisitorTest extends TestCase {

  public ResetVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ResetVisitorTest.class);
    //suite.addTest(new ResetVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final MockBean bean0 = new MockBeanImpl();
    final MockBean bean1 = new MockBeanImpl(bean0);
    final MockBean bean2 = new MockBeanImpl(bean1);

    assertFalse(bean0.isResetCalled());
    assertFalse(bean1.isResetCalled());
    assertFalse(bean2.isResetCalled());

    bean2.accept(new ResetVisitor());

    assertTrue(bean0.isResetCalled());
    assertTrue(bean1.isResetCalled());
    assertTrue(bean2.isResetCalled());
  }

  protected static interface MockBean extends Resettable, Visitable {

    public boolean isResetCalled();

  }

  protected static class MockBeanImpl implements MockBean {

    private boolean resetCalled;
    private Visitable visitableObject;

    public MockBeanImpl() {
    }

    public MockBeanImpl(final Visitable visitableObject) {
      this.visitableObject = visitableObject;
    }

    public boolean isResetCalled() {
      return resetCalled;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(visitableObject)) {
        visitableObject.accept(visitor);
      }
    }

    public void reset() {
      resetCalled = true;
    }
  }

}
