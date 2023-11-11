/*
 * RollbackVetoableChangeListenerTest.java (c) 2 January 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.26
 * @see com.cp.common.beans.event.CommonEventTestCase
 * @see com.cp.common.beans.event.RollbackVetoableChangeListener
 */

package com.cp.common.beans.event;

import com.cp.common.beans.Bean;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class RollbackVetoableChangeListenerTest extends CommonEventTestCase {

  public RollbackVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(RollbackVetoableChangeListenerTest.class);
    //suite.addTest(new RollbackVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testHandle() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    EasyMock.expect(mockBean.isRollbackCalled()).andReturn(false);
    EasyMock.replay(mockBean);

    try {
      RollbackVetoableChangeListener.INSTANCE.handle(new PropertyChangeEvent(mockBean, "value", null, "test"));
    }
    catch (Exception e) {
      fail("Calling handle with a property change event on a bean with no rollback called and throw exception on rollback disabled threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockBean);
  }

  public void testHandleWithRollbackCalledAndThrowExceptionDisabled() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    EasyMock.expect(mockBean.isRollbackCalled()).andReturn(true);
    EasyMock.expect(mockBean.isThrowExceptionOnRollback()).andReturn(false);
    EasyMock.replay(mockBean);

    try {
      RollbackVetoableChangeListener.INSTANCE.handle(new PropertyChangeEvent(mockBean, "value", null, "test"));
    }
    catch (Exception e) {
      fail("Calling handle with a property change event on a bean with rollback called and throw exception on rollback disabled threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockBean);
  }

  public void testHandleWithRollbackCalledAndThrowExceptionEnabled() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    EasyMock.expect(mockBean.isRollbackCalled()).andReturn(true);
    EasyMock.expect(mockBean.isThrowExceptionOnRollback()).andReturn(true);
    EasyMock.replay(mockBean);

    try {
      RollbackVetoableChangeListener.INSTANCE.handle(new PropertyChangeEvent(mockBean, "value", null, "test"));
      fail("Calling handle with a property change event having a bean with rollback called and throw Exception on rollback enabled should have thrown a PropertyVetoException!");
    }
    catch (PropertyVetoException e) {
      assertEquals("Rollback has been called on bean (" + mockBean.getClass().getName()
        + "); unable to set property (value)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling handle with a property change event on a bean with rollback called and throw exception on rollback enabled threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    EasyMock.verify(mockBean);
  }

  public void testHandleUsingBean() throws Exception {
    final MockBean mockBean = new MockBeanImpl("test") {
      @Override
      public boolean isThrowExceptionOnRollback() {
        return true;
      }
    };

    assertNotNull(mockBean);
    assertFalse(mockBean.isRollbackCalled());
    assertTrue(mockBean.isThrowExceptionOnRollback());
    assertEquals("test", mockBean.getValue());

    try {
      mockBean.setValue("null");
    }
    catch (Exception e) {
      fail("Setting the value property of MockBean to String literal 'null' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getValue());

    mockBean.rollback();

    assertEquals("null", mockBean.getValue());
    assertTrue(mockBean.isRollbackCalled());
    assertTrue(mockBean.isThrowExceptionOnRollback());

    try {
      mockBean.setValue("myValue");
      fail("Setting the value property of MockBean to 'myValue' when rollback has been called and throw Exception on rollback is enabled should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("Rollback has been called on bean (" + mockBean.getClass().getName()
        + "); unable to set property (value)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the value property of MockBean to 'myValue' when rollback has been called and throw Exception on rollback is enabled threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("null", mockBean.getValue());

    mockBean.reset();

    assertEquals("null", mockBean.getValue());
    assertFalse(mockBean.isRollbackCalled());
    assertTrue(mockBean.isThrowExceptionOnRollback());

    try {
      mockBean.setValue("test");
    }
    catch (Exception e) {
      fail("Setting the value property of MockBean to 'test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("test", mockBean.getValue());
  }

}
