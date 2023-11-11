/*
 * RequiredVetoableChangeListenerTest.java (c) 15 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.24
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.RequiredVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.annotation.Required;
import com.cp.common.lang.ObjectUtil;
import java.beans.PropertyChangeEvent;
import junit.framework.Test;
import junit.framework.TestSuite;

public class RequiredVetoableChangeListenerTest extends CommonEventTestCase {

  public RequiredVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RequiredVetoableChangeListenerTest.class);
    //suite.addTest(new RequiredVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final MockBean mockBean = new MockBeanImpl();
    RequiredVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new RequiredVetoableChangeListener(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RequiredVetoableChangeListener class with a non-null bean Object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    RequiredVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new RequiredVetoableChangeListener(null);
      fail("Instantiating an instance of the RequiredVetoableChangeListener class with a null bean Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the RequiredVetoableChangeListener class with a null bean Object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testHandleWithDifferentRegisteredAndEventBeans() throws Exception {
    final MockBean mockBean = new MockBeanImpl();
    final RequiredVetoableChangeListener listener = new RequiredVetoableChangeListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());

    try {
      listener.handle(new PropertyChangeEvent(new Object(), "value", null, "test"));
      fail("Calling handle with a different event bean from the registered bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The registered bean and event bean are not the same object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling handle with a different event bean from the registered bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleWithNonRequiredProperty() throws Exception {
    final MockBean mockBean = new MockBeanImpl();

    assertNull(mockBean.getNonRequiredValue());

    try {
      mockBean.setNonRequiredValue("test");
      assertEquals("test", mockBean.getNonRequiredValue());
      mockBean.setNonRequiredValue("null");
      assertEquals("null", mockBean.getNonRequiredValue());
      mockBean.setNonRequiredValue(null);
      assertNull(mockBean.getNonRequiredValue());
    }
    catch (Exception e) {
      fail("Setting the non-required property (nonRequiredValue) of MockBean with a series of values including null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleWithRequiredProperty() throws Exception {
    final MockBean mockBean = new MockBeanImpl();

    assertNull(mockBean.getRequiredValue());

    try {
      mockBean.setRequiredValue("test");
    }
    catch (Exception e) {
      fail("Setting the required property (requiredValue) of MockBean to 'test' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("test", mockBean.getRequiredValue());

    try {
      mockBean.setRequiredValue("null");
    }
    catch (Exception e) {
      fail("Setting the required property (requiredValue) of MockBean to String value 'null' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getRequiredValue());

    try {
      mockBean.setRequiredValue(null);
      fail("Setting the required property (requiredValue) of MockBean to null should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The property (requiredValue) of bean (" + mockBean.getClass().getName() + ") is required!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the required property (requiredValue) of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getRequiredValue());
  }

  public void testIsRequired() throws Exception {
    final MockBean mockBean = new MockBeanImpl();
    final RequiredVetoableChangeListener listener = new RequiredVetoableChangeListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
    assertTrue(listener.isRequired("requiredValue"));
    assertFalse(listener.isRequired("nonRequiredValue"));
  }

  public static interface MockBean extends Bean<Integer> {

    public String getNonRequiredValue();

    public void setNonRequiredValue(String nonRequiredValue);

    public String getRequiredValue();

    public void setRequiredValue(String requiredValue);

  }

  public static final class MockBeanImpl extends AbstractBean<Integer> implements MockBean {

    private String nonRequiredValue;
    private String requiredValue;

    public String getNonRequiredValue() {
      return nonRequiredValue;
    }

    public void setNonRequiredValue(final String nonRequiredValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBeanImpl.this.nonRequiredValue = nonRequiredValue;
        }
      };
      processChange("nonRequiredValue", this.nonRequiredValue, nonRequiredValue, callbackHandler);
    }

    public String getRequiredValue() {
      return requiredValue;
    }

    @Required
    public void setRequiredValue(final String requiredValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBeanImpl.this.requiredValue = requiredValue;
        }
      };
      processChange("requiredValue", this.requiredValue, requiredValue, callbackHandler);
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof MockBean)) {
        return false;
      }

      final MockBean that = (MockBean) obj;

      return ObjectUtil.equals(getNonRequiredValue(), that.getNonRequiredValue())
        && ObjectUtil.equals(getRequiredValue(), that.getRequiredValue());
    }

    @Override
    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 *  hashValue + ObjectUtil.hashCode(getNonRequiredValue());
      hashValue = 37 *  hashValue + ObjectUtil.hashCode(getRequiredValue());
      return hashValue;
    }
  }

}
