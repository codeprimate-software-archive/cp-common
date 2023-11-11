/*
 * MutableVisitorTest.java (c) 10 September 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.23
 * @com.cp.common.lang.support.MutableVisitor
 * @com.cp.common.test.mock.MockMutableObject
 * @see junit.framework.TestCase
 */

package com.cp.common.lang.support;

import com.cp.common.lang.Mutable;
import com.cp.common.test.mock.MockMutableObject;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MutableVisitorTest extends TestCase {

  public MutableVisitorTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MutableVisitorTest.class);
    //suite.addTest(new MutableVisitorTest("testName"));
    return suite;
  }

  public void testVisit() throws Exception {
    final MockMutableObject obj0 = new MockMutableObject();
    final MockMutableObject obj1 = new MockMutableObject(obj0);
    final MockMutableObject obj2 = new MockMutableObject(obj1);

    assertEquals(Mutable.MUTABLE, obj0.isMutable());
    assertEquals(Mutable.MUTABLE, obj1.isMutable());
    assertEquals(Mutable.MUTABLE, obj2.isMutable());

    obj2.accept(new MutableVisitor(Mutable.IMMUTABLE));

    assertEquals(Mutable.IMMUTABLE, obj0.isMutable());
    assertEquals(Mutable.IMMUTABLE, obj1.isMutable());
    assertEquals(Mutable.IMMUTABLE, obj2.isMutable());

    obj2.accept(new MutableVisitor(Mutable.MUTABLE));

    assertEquals(Mutable.MUTABLE, obj0.isMutable());
    assertEquals(Mutable.MUTABLE, obj1.isMutable());
    assertEquals(Mutable.MUTABLE, obj2.isMutable());
  }

}
