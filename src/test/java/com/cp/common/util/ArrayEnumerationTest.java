/*
 * ArrayEnumerationTest.java (c) 27 July 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.7.27
 */

package com.cp.common.util;

import java.util.Enumeration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ArrayEnumerationTest extends TestCase {

  public ArrayEnumerationTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ArrayEnumerationTest.class);
    //suite.addTest(new ArrayEnumerationTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    Enumeration<Object> arrayEnumeration = null;

    assertNull(arrayEnumeration);

    try {
      arrayEnumeration = new ArrayEnumeration<Object>(new Object[] { "test", "tester", "testing" });
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ArrayEnumeration class with a non-null Object array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(arrayEnumeration);
  }

  public void testInstantiationWithNullArray() throws Exception {
    try {
      new ArrayEnumeration<Object>(null);
      fail("Instantiating an instance of the ArrayEnumeration class with a null Object array should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The object array cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the ArrayEnumeration class with a null Object array threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testHasMoreElementsWithEmptyArray() throws Exception {
    final Enumeration<String> arrayEnumeration = new ArrayEnumeration<String>(new String[0]);

    assertNotNull(arrayEnumeration);
    assertFalse(arrayEnumeration.hasMoreElements());
  }

  public void testNextElement() throws Exception {
    final Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    final Enumeration<Integer> arrayEnumeration = new ArrayEnumeration<Integer>(array);

    assertNotNull(arrayEnumeration);

    for (final Integer value : array) {
      assertTrue(arrayEnumeration.hasMoreElements());
      assertEquals(value, arrayEnumeration.nextElement());
    }
  }

}
