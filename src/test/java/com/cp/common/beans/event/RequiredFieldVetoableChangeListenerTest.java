/*
 * RequiredFieldVetoableChangeListenerTest.java (c) 26 December 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.25
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.RequiredFieldVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RequiredFieldVetoableChangeListenerTest extends CommonEventTestCase {

  public RequiredFieldVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RequiredFieldVetoableChangeListenerTest.class);
    //suite.addTest(new RequiredFieldVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    RequiredFieldVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new RequiredFieldVetoableChangeListener("myProperty");
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RequiredFieldVetoableChangeListener class with a non-null property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals("myProperty", listener.getPropertyName());
  }

  public void testInstantiationWithEmptyProperty() throws Exception {
    RequiredFieldVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new RequiredFieldVetoableChangeListener(" ");
      fail("Instantiating an instance of the RequiredFieldVetoableChangeListener class with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RequiredFieldVetoableChangeListener class with an empty property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullProperty() throws Exception {
    RequiredFieldVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new RequiredFieldVetoableChangeListener(null);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RequiredFieldVetoableChangeListener class with a null property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testHandleUsingListenerInitializedWithPropertyName() throws Exception {
    final MockBean mockBean = new MockBeanImpl("test");
    mockBean.addVetoableChangeListener(new RequiredFieldVetoableChangeListener("value"));

    assertEquals("test", mockBean.getValue());

    try {
      mockBean.setValue("null");
      assertEquals("null", mockBean.getValue());
      mockBean.setValue(null);
      fail("Setting a required property (value) of MockBean to null should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (value) of bean (" + mockBean.getClass().getName()
        + ") is a required field and cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting a required property (value) of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getValue());
  }

  public void testHandleUsingListenerPropertyRegistration() throws Exception {
    final MockBean mockBean = new MockBeanImpl("test");
    mockBean.addVetoableChangeListener("value", RequiredFieldVetoableChangeListener.INSTANCE);

    assertEquals("test", mockBean.getValue());

    try {
      mockBean.setValue("null");
      assertEquals("null", mockBean.getValue());
      mockBean.setValue(null);
      fail("Setting a required property (value) of MockBean to null should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (value) of bean (" + mockBean.getClass().getName()
        + ") is a required field and cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting a required property (value) of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getValue());
  }

  public void testHandleWithNonRequiredPropertyChanges() throws Exception {
    final MockBean mockBean = new MockBeanImpl("test");
    mockBean.addVetoableChangeListener("val", RequiredFieldVetoableChangeListener.INSTANCE);

    assertEquals("test", mockBean.getValue());

    try {
      mockBean.setValue("null");
      assertEquals("null", mockBean.getValue());
      mockBean.setValue(null);
      assertNull(mockBean.getValue());
    }
    catch (Exception e) {
      fail("Setting the value of a non-required property (value) of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

}
