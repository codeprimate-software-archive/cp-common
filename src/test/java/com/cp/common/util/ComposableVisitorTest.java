/*
 * ComposableVisitorTest.java (c) 26 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.12.26
 */

package com.cp.common.util;

import com.cp.common.lang.Visitable;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ComposableVisitorTest extends TestCase {

  public ComposableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ComposableVisitorTest.class);
    //suite.addTest(new ComposableVisitorTest("testName"));
    return suite;
  }

  public void testCompose() throws Exception {
    final Visitor leftVisitor = new MockVisitor();
    final Visitor rightVisitor = new MockVisitor();

    assertNull(ComposableVisitor.compose(null, null));
    assertEquals(leftVisitor, ComposableVisitor.compose(leftVisitor, null));
    assertEquals(rightVisitor, ComposableVisitor.compose(null, rightVisitor));

    final Visitor composedVisitor = ComposableVisitor.compose(leftVisitor, rightVisitor);

    assertNotNull(composedVisitor);
    assertTrue(composedVisitor instanceof ComposableVisitor);
  }

  public void testVisit() throws Exception {
    final Visitable mockVisitable = new MockVisitable();

    final MockVisitor leftVisitor = (MockVisitor) ComposableVisitor.compose(new MockVisitor(), null);
    final MockVisitor rightVisitor = (MockVisitor) ComposableVisitor.compose(null, new MockVisitor());
    final Visitor composedVisitor = ComposableVisitor.compose(leftVisitor, rightVisitor);

    assertFalse(leftVisitor.isVisited());
    assertFalse(rightVisitor.isVisited());
    assertTrue(composedVisitor instanceof ComposableVisitor);

    mockVisitable.accept(composedVisitor);

    assertTrue(leftVisitor.isVisited());
    assertTrue(rightVisitor.isVisited());
  }

  private static final class MockVisitable implements Visitable {

    public void accept(final Visitor visitor) {
      visitor.visit(this);
    }
  }

  private static final class MockVisitor implements Visitor {

    private boolean visited = false;

    public boolean isVisited() {
      final boolean visited = this.visited;
      this.visited = false;
      return visited;
    }

    public void visit(final Visitable obj) {
      visited = true;
    }
  }

}
