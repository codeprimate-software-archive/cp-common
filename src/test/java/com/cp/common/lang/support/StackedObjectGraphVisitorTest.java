/*
 * StackedObjectGraphVisitorTest.java (c) 3 May 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.7.13
 * @see com.cp.common.lang.support.StackedObjectGraphVisitor
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.support;

import com.cp.common.test.mock.MockVisitableObject;
import java.util.Stack;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StackedObjectGraphVisitorTest extends TestCase {

  public StackedObjectGraphVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(StackedObjectGraphVisitorTest.class);
    return suite;
  }

  public void testVisit() throws Exception {
    final MockVisitableObject mockVisitableObject0 = new MockVisitableObject(0);
    final MockVisitableObject mockVisitableObject1 = new MockVisitableObject(1, mockVisitableObject0);
    final MockVisitableObject mockVisitableObject2 = new MockVisitableObject(2, mockVisitableObject1);


    final StackedObjectGraphVisitor visitor = new StackedObjectGraphVisitor();

    mockVisitableObject2.accept(visitor);

    final Stack<Object> expectedMockVisitableObjectStack = new Stack<Object>();
    expectedMockVisitableObjectStack.push(mockVisitableObject2);
    expectedMockVisitableObjectStack.push(mockVisitableObject1);
    expectedMockVisitableObjectStack.push(mockVisitableObject0);

    final Stack<Object> actualMockVisitableObjectStack = visitor.getObjectStack();

    assertNotNull(actualMockVisitableObjectStack);
    assertFalse(actualMockVisitableObjectStack.isEmpty());
    assertEquals(expectedMockVisitableObjectStack.size(), actualMockVisitableObjectStack.size());

    while (!actualMockVisitableObjectStack.isEmpty()) {
      assertSame(expectedMockVisitableObjectStack.pop(), actualMockVisitableObjectStack.pop());
    }
  }

}
