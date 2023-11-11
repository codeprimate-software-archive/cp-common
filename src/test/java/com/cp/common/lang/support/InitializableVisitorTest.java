/*
 * InitializableVisitorTest.java (c) 13 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.13
 * @see com.cp.common.lang.support.InitializableVisitor
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Initializable;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.Visitor;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class InitializableVisitorTest extends TestCase {

  public InitializableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(InitializableVisitorTest.class);
    //suite.addTest(new InitializableVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final Object configuration = new Object();

    final MockVisitable<Object> obj1 = new MockInitializableVisitable(configuration, null);
    final MockVisitable<Object> obj2 = new MockVisitable<Object>(configuration, obj1);
    final MockVisitable<Object> obj3 = new MockInitializableVisitable(configuration, obj2);

    assertFalse(obj1.isInitializeCalled());
    assertFalse(obj2.isInitializeCalled());
    assertFalse(obj3.isInitializeCalled());

    obj3.accept(new InitializableVisitor<Object>(configuration));

    assertTrue(obj1.isInitializeCalled());
    assertFalse(obj2.isInitializeCalled());
    assertTrue(obj3.isInitializeCalled());
  }

  private static class MockVisitable<T> implements Visitable {

    private boolean initializeCalled = false;

    private final T configuration;

    private final Visitable visitableObject;

    public MockVisitable(final T configuration, final Visitable visitableObject) {
      this.configuration = configuration;
      this.visitableObject = visitableObject;
    }

    public boolean isInitializeCalled() {
      return initializeCalled;
    }

    public void accept(final Visitor visitor) {
      visitor.visit(this);

      if (ObjectUtil.isNotNull(visitableObject)) {
        visitableObject.accept(visitor);
      }
    }

    public void initialize(T configuration) {
      assertSame(this.configuration, configuration);
      initializeCalled = true;
    }
  }

  private static class MockInitializableVisitable extends MockVisitable<Object> implements Initializable<Object> {

    public MockInitializableVisitable(final Object configuration, final Visitable visitableObject) {
      super(configuration, visitableObject);
    }

    public boolean isInitialized() {
      throw new UnsupportedOperationException("Not Implemented");
    }
  }

}
