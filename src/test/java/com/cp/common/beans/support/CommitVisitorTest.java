/*
 * CommitVisitorTest.java (c) 24 July 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.support.CommitVisitor
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.support;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CommitVisitorTest extends TestCase {

  public CommitVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CommitVisitorTest.class);
    //suite.addTest(new CommitVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final TestBean bean0 = new TestBean();
    final TestBean bean1 = new TestBean(bean0);
    final TestBean bean2 = new TestBean(bean1);

    assertFalse(bean0.isCommitCalled());
    assertFalse(bean1.isCommitCalled());
    assertFalse(bean2.isCommitCalled());

    bean2.accept(new CommitVisitor());

    assertTrue(bean0.isCommitCalled());
    assertTrue(bean1.isCommitCalled());
    assertTrue(bean2.isCommitCalled());
  }

  protected static class TestBean extends MockBeanImpl {

    private boolean commitCalled = false;
    private TestBean bean;

    public TestBean() {
    }

    public TestBean(final TestBean bean) {
      this.bean = bean;
    }

    public boolean isCommitCalled() {
      return commitCalled;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(bean)) {
        bean.accept(visitor);
      }
    }

    public void commit() {
      commitCalled = true;
    }
  }

}
