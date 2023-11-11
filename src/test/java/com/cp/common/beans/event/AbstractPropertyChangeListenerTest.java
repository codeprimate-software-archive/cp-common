/*
 * AbstractPropertyChangeListenerTest.java (c) 7 August 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.4
 * @see com.cp.common.beans.event.AbstractPropertyChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import java.beans.PropertyChangeEvent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AbstractPropertyChangeListenerTest extends TestCase {

  public AbstractPropertyChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbstractPropertyChangeListenerTest.class);
    //suite.addTest(new AbstractPropertyChangeListenerTest("testName"));
    return suite;
  }

  public void testHandleAllProperties() throws Exception {
    final Object bean = new Object();
    MockPropertyChangeListener listener = new MockPropertyChangeListener();

    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "booleanValue", Boolean.FALSE, Boolean.TRUE));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "numberValue", 0, 1));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "stringValue", null, "null"));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());
  }

  public void testHandleSelectProperty() throws Exception {
    final Object bean = new Object();
    MockPropertyChangeListener listener = new MockPropertyChangeListener("numberValue");

    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals("numberValue", listener.getPropertyName());
    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "booleanValue", Boolean.TRUE, Boolean.FALSE));

    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "numberValue", 0, 1));

    assertTrue(listener.isCalled());
    assertFalse(listener.isCalled());

    listener.propertyChange(new PropertyChangeEvent(bean, "stringValue", null, "null"));

    assertFalse(listener.isCalled());
  }

  protected static final class MockPropertyChangeListener extends AbstractPropertyChangeListener {

    private boolean called = false;

    public MockPropertyChangeListener() {
    }

    public MockPropertyChangeListener(final String propertyName) {
      super(propertyName);
    }

    public boolean isCalled() {
      final boolean called = this.called;
      setCalled(false);
      return called;
    }

    private void setCalled(final boolean called) {
      this.called = called;
    }

    protected void handle(final PropertyChangeEvent event) {
      setCalled(true);
    }
  }

}
