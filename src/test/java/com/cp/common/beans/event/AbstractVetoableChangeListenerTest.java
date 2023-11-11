/*
 * AbstractVetoableChangeListenerTest.java (c) 7 August 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.4
 * @see com.cp.common.beans.event.AbstractVetoableChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractVetoableChangeListenerTest extends TestCase {

  public AbstractVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractVetoableChangeListenerTest.class);
    //suite.addTest(new AbstractVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testHandleAllProperties() throws Exception {
    final Object bean = new Object();
    MockVetoableChangeListener listener = new MockVetoableChangeListener();

    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "booleanValue", Boolean.FALSE, Boolean.TRUE));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "numberValue", 0, 1));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "stringValue", null, "null"));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());
  }

  public void testHandleSelectProperty() throws Exception {
    final Object bean = new Object();
    MockVetoableChangeListener listener = new MockVetoableChangeListener("numberValue");

    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals("numberValue", listener.getPropertyName());
    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "booleanValue", Boolean.TRUE, Boolean.FALSE));

    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "numberValue", 0, 1));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.vetoableChange(new PropertyChangeEvent(bean, "stringValue", null, "null"));

    assertFalse(listener.isCalled());
  }

  protected static final class MockVetoableChangeListener extends AbstractVetoableChangeListener {

    private boolean called = false;

    public MockVetoableChangeListener() {
    }

    public MockVetoableChangeListener(final String propertyName) {
      super(propertyName);
    }

    public boolean isCalled() {
      final boolean called = this.called;
      setCalled(false);
      return called;
    }

    private void setCalled(boolean called) {
      this.called = called;
    }

    protected void handle(final PropertyChangeEvent event) throws PropertyVetoException {
      setCalled(true);
    }
  }

}
