/*
 * RollbackVisitorTest.java (c) 2007 July 24
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.27
 * @see com.cp.common.beans.support.RollbackVisitor
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.support;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RollbackVisitorTest extends TestCase {

  public RollbackVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RollbackVisitorTest.class);
    //suite.addTest(new RollbackVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final TestBean bean0 = new TestBean();
    final TestBean bean1 = new TestBean(bean0);
    final TestBean bean2 = new TestBean(bean1);

    assertFalse(bean0.isRollbackCalled());
    assertFalse(bean1.isRollbackCalled());
    assertFalse(bean2.isRollbackCalled());

    bean2.accept(new RollbackVisitor());

    assertTrue(bean0.isRollbackCalled());
    assertTrue(bean1.isRollbackCalled());
    assertTrue(bean2.isRollbackCalled());
  }

  protected static class TestBean extends MockBeanImpl {

    private boolean rollbackCalled = false;
    private TestBean bean;

    public TestBean() {
    }

    public TestBean(final TestBean bean) {
      this.bean = bean;
    }

    public boolean isRollbackCalled() {
      return rollbackCalled;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(bean)) {
        bean.accept(visitor);
      }
    }

    public void rollback() {
      rollbackCalled = true;
    }
  }

}
