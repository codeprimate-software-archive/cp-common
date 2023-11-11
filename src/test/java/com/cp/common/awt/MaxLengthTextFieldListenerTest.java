/*
 * MaxLengthTextFieldListenerTest.java (c) 29 May 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author jblum
 * @version 2009.5.29
 * @see com.cp.common.awt.MaxLengthTextFieldListener
 */

package com.cp.common.awt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MaxLengthTextFieldListenerTest extends TestCase {

  public MaxLengthTextFieldListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MaxLengthTextFieldListenerTest.class);
    return suite;
  }

  public void testInstantiation() throws Exception {
    MaxLengthTextFieldListener listener = null;

    assertNull(listener);

    try {
      listener = new MaxLengthTextFieldListener(25);
    }
    catch (Throwable t) {
      fail("Instantiating an instance of the MaxLengthTextFieldListener class threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertEquals(25, listener.getMaxLength());
  }

  public void testIsGreaterThanEqualToMaxLength() throws Exception {
    final MaxLengthTextFieldListener listener = new MaxLengthTextFieldListener(10);

    assertNotNull(listener);
    assertEquals(10, listener.getMaxLength());
    assertFalse(listener.isGreaterThanEqualToMaxLength(Integer.MIN_VALUE));
    assertFalse(listener.isGreaterThanEqualToMaxLength(-11));
    assertFalse(listener.isGreaterThanEqualToMaxLength(0));
    assertFalse(listener.isGreaterThanEqualToMaxLength(9));
    assertTrue(listener.isGreaterThanEqualToMaxLength(10));
    assertTrue(listener.isGreaterThanEqualToMaxLength(11));
    assertTrue(listener.isGreaterThanEqualToMaxLength(Integer.MAX_VALUE));
  }

  public void testSetMaxLength() throws Exception {
    final MaxLengthTextFieldListener listener = new MaxLengthTextFieldListener(0);

    assertNotNull(listener);
    assertEquals(0, listener.getMaxLength());

    listener.setMaxLength(25);

    assertEquals(25, listener.getMaxLength());
  }

  public void testSetMaxLengthWithInvalidValue() throws Exception {
    final MaxLengthTextFieldListener listener = new MaxLengthTextFieldListener(0);

    assertNotNull(listener);
    assertEquals(0, listener.getMaxLength());

    try {
      listener.setMaxLength(-25);
      fail("Calling setMaxLenth with a negative value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("-25 is not a valid maximum length constraint!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setMaxLenth with a negative value threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

}
