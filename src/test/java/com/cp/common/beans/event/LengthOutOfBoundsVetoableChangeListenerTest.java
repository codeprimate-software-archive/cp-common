/*
 * LengthOutOfBoundsVetoableChangeListenerTest.java (c) 14 September 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.14
 * @see com.cp.common.beans.event.LengthOutOfBoundsVetoableChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.Bean;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class LengthOutOfBoundsVetoableChangeListenerTest extends TestCase {

  public LengthOutOfBoundsVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(LengthOutOfBoundsVetoableChangeListenerTest.class);
    //suite.addTest(new LengthOutOfBoundsVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    LengthOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new LengthOutOfBoundsVetoableChangeListener("value", 0, 10);
    }
    catch (Exception e) {
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(10, listener.getMax());
    assertEquals(0, listener.getMin());
    assertEquals("value", listener.getPropertyName());
  }

  public void testInstantiationWithEmptyProperty() throws Exception {
    LengthOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new LengthOutOfBoundsVetoableChangeListener(" ", 0, 10);
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with an empty property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullProperty() throws Exception {
    LengthOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new LengthOutOfBoundsVetoableChangeListener(0, 10);
    }
    catch (Exception e) {
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(10, listener.getMax());
    assertEquals(0, listener.getMin());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithInvalidMaxLength() throws Exception {
    LengthOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new LengthOutOfBoundsVetoableChangeListener("myProperty", 10, 0);
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with a maximum length less than the minimum length should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The maximum length (0) must be greater than equal to the minimum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with a maximum length less than the minimum length threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithInvalidMinLength() throws Exception {
    LengthOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new LengthOutOfBoundsVetoableChangeListener("myProperty", -9, 9);
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with an invalid minimum length should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The mininum length (-9) must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of LengthOutOfBoundsVetoableChangeListener with an invalid minimum length threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testAcceptWithMaxLengthConstraint() throws Exception {
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 0, 5);

    assertNotNull(listener);
    assertEquals(0, listener.getMin());
    assertEquals(5, listener.getMax());
    assertTrue(listener.accept("01234"));
    assertTrue(listener.accept("abcde"));
    assertTrue(listener.accept("     "));
    assertTrue(listener.accept(" "));
    assertTrue(listener.accept(""));
    assertFalse(listener.accept("012345"));
    assertFalse(listener.accept("abc123"));
    assertFalse(listener.accept("threee"));
  }

  public void testAcceptWithMinLengthConstraint() throws Exception {
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 5, Integer.MAX_VALUE);

    assertNotNull(listener);
    assertEquals(5, listener.getMin());
    assertEquals(Integer.MAX_VALUE, listener.getMax());
    assertTrue(listener.accept("01234"));
    assertTrue(listener.accept("abcde"));
    assertTrue(listener.accept("abc123"));
    assertTrue(listener.accept("     "));
    assertFalse(listener.accept(""));
    assertFalse(listener.accept(" "));
    assertFalse(listener.accept("5555"));
  }

  public void testAcceptWithMinAndMaxLengthConstraint() throws Exception {
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 5, 10);

    assertNotNull(listener);
    assertEquals(5, listener.getMin());
    assertEquals(10, listener.getMax());
    assertFalse(listener.accept("five"));
    assertFalse(listener.accept(" "));
    assertTrue(listener.accept("01234"));
    assertTrue(listener.accept("0123456789"));
    assertTrue(listener.accept("333224444"));
    assertTrue(listener.accept("abc123"));
    assertTrue(listener.accept("abcdefghij"));
    assertFalse(listener.accept("333-22-4444"));
    assertFalse(listener.accept("0123456789A"));
  }

  public void testAcceptWithNoLengthConstraint() throws Exception {
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 0, Integer.MAX_VALUE);

    assertNotNull(listener);
    assertEquals(0, listener.getMin());
    assertEquals(Integer.MAX_VALUE, listener.getMax());
    assertTrue(listener.accept(""));
    assertTrue(listener.accept(" "));
    assertTrue(listener.accept("0123456789"));
    assertTrue(listener.accept("abcdefghijklmnopqrstuvwxyz"));
  }

  public void testHandleAcceptingLength() throws Exception {
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(5, 10);

    assertNotNull(listener);
    assertEquals(5, listener.getMin());
    assertEquals(10, listener.getMax());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "myProperty", null, "012345"));
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a property change event having new value (012345) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleRejectingLength() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 5, 10);

    assertNotNull(listener);
    assertEquals(5, listener.getMin());
    assertEquals(10, listener.getMax());

    try {
      listener.vetoableChange(new PropertyChangeEvent(mockBean, "myProperty", null, "0123"));
      fail("Calling vetoableChange with a property change event having new value (0123) should have thrown a PropertyVetoException!");
    }
    catch (PropertyVetoException e) {
      assertEquals("The length of value (0123) for property (myProperty) on bean (" + mockBean.getClass().getName()
        + ") must be between minimum length (5) and maximum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a property change event having new value (0123) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleThrowsIllegalUseOfListenerException() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final LengthOutOfBoundsVetoableChangeListener listener = new LengthOutOfBoundsVetoableChangeListener(
      "myProperty", 5, 10);

    assertNotNull(listener);
    assertEquals(5, listener.getMin());
    assertEquals(10, listener.getMax());

    try {
      listener.vetoableChange(new PropertyChangeEvent(mockBean, "myProperty", null, Boolean.TRUE));
      fail("Calling vetoableChange with a property change event having new boolean value should have thrown an IllegalUseOfListenerException!");
    }
    catch (IllegalUseOfListenerException e) {
      assertEquals("Property (myProperty) on bean (" + mockBean.getClass().getName() + ") is not of type String!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a property change event having new boolean value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

}
