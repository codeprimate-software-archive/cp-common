/*
 * NumberOutOfBoundsVetoableChangeListenerTest.java (c) 30 January 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.16
 * @see com.cp.common.beans.event.NumberOutOfBoundsVetoableChangeListenerTest
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

public class NumberOutOfBoundsVetoableChangeListenerTest extends TestCase {

  public NumberOutOfBoundsVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(NumberOutOfBoundsVetoableChangeListenerTest.class);
    //suite.addTest(new NumberOutOfBoundsVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Integer> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Integer>("value", -9, 9);
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());
    assertEquals("value", listener.getPropertyName());
  }

  public void testInstantiationWithEmptyProperty() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Integer> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(" ", -9, 9);
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with an empty property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullProperty() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Integer> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(-9, 9);
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a null property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullMaxValue() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Double> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Double>(null, 9.0, null);
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a null maxium value should have thrown an NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The maximum value cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a null maximum value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullMinValue() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Double> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Double>(null, null, 9.0);
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a null minimum value should have thrown an NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The minimum value cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a null minimum value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithInvalidMaxValue() throws Exception {
    NumberOutOfBoundsVetoableChangeListener<Double> listener = null;

    assertNull(listener);

    try {
      listener = new NumberOutOfBoundsVetoableChangeListener<Double>(null, 2.0, -9.0);
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a maximum value less than the minimum value should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The minimum number value (2.0) cannot be greater than the maximum number value (-9.0)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of NumberOutOfBoundsVetoableChangeListener with a maximum value less than the minimum value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testAcceptWithMaxNumberConstraint() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      Integer.MIN_VALUE, 9);

    assertNotNull(listener);
    assertEquals(new Integer(Integer.MIN_VALUE), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());
    assertTrue(listener.accept(Integer.MIN_VALUE));
    assertTrue(listener.accept(-10));
    assertTrue(listener.accept(-1));
    assertTrue(listener.accept(0));
    assertTrue(listener.accept(1));
    assertTrue(listener.accept(7));
    assertTrue(listener.accept(9));
    assertTrue(listener.accept(9));
    assertFalse(listener.accept(10));
    assertFalse(listener.accept(11));
    assertFalse(listener.accept(100));
  }

  public void testAcceptWithMinNumberConstraint() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      -9, Integer.MAX_VALUE);

    assertNotNull(listener);
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(Integer.MAX_VALUE), listener.getMaxValue());
    assertFalse(listener.accept(Integer.MIN_VALUE));
    assertFalse(listener.accept(-11));
    assertFalse(listener.accept(-10));
    assertTrue(listener.accept(-9));
    assertTrue(listener.accept(-1));
    assertTrue(listener.accept(0));
    assertTrue(listener.accept(1));
    assertTrue(listener.accept(9));
    assertTrue(listener.accept(10));
    assertTrue(listener.accept(Integer.MAX_VALUE));
  }

  public void testAcceptWithMinAndMaxNumberConstraint() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      -9, 9);

    assertNotNull(listener);
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());
    assertFalse(listener.accept(Integer.MIN_VALUE));
    assertFalse(listener.accept(-10));
    assertTrue(listener.accept(-9));
    assertTrue(listener.accept(-1));
    assertTrue(listener.accept(0));
    assertTrue(listener.accept(1));
    assertTrue(listener.accept(9));
    assertFalse(listener.accept(10));
    assertFalse(listener.accept(Integer.MAX_VALUE));
  }

  public void testAcceptWithNoNumberConstraint() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      Integer.MIN_VALUE, Integer.MAX_VALUE);

    assertNotNull(listener);
    assertEquals(new Integer(Integer.MIN_VALUE), listener.getMinValue());
    assertEquals(new Integer(Integer.MAX_VALUE), listener.getMaxValue());
    assertTrue(listener.accept(Integer.MIN_VALUE));
    assertTrue(listener.accept(0));
    assertTrue(listener.accept(Integer.MAX_VALUE));
  }

  public void testHandleAcceptingNumber() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      -9, 9);

    assertNotNull(listener);
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null, 0));
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a property change event having a new Integer value of 0 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleRejectingNumber() throws Exception {
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      -9, 9);

    assertNotNull(listener);
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null, 100));
      fail("Calling vetoableChange withe a property change event having a new Integer value of 100 should have thrown a PropertyVetoException!");
    }
    catch (PropertyVetoException e) {
      assertEquals("The new numeric value (100) must be between the minimum (-9) and maximum (9) numerical values inclusive!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a property change event having a new Integer value of 100 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleThrowingIllegalUseOfListenerException() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    final NumberOutOfBoundsVetoableChangeListener<Integer> listener = new NumberOutOfBoundsVetoableChangeListener<Integer>(
      -9, 9);

    assertNotNull(listener);
    assertEquals(new Integer(-9), listener.getMinValue());
    assertEquals(new Integer(9), listener.getMaxValue());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null, Boolean.TRUE));
      fail("Calling vetoableChange with property change event having a new Boolean value should have thrown an IllegalUseOfListenerException!");
    }
    catch (IllegalUseOfListenerException e) {
      assertEquals("Property (value) of bean (" + mockBean.getClass().getName() + ") is not of type Number!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with property change event having a new Boolean value threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

}
