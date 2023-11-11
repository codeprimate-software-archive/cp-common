/*
 * BoundedNumberVetoableChangeListenerTest.java (c) 16 December 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.13
 * @see com.cp.common.beans.event.BoundedNumberVetoableChangeListener
 * @see com.cp.common.beans.event.CommonEventTestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.annotation.BoundedNumber;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import java.beans.PropertyChangeEvent;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class BoundedNumberVetoableChangeListenerTest extends CommonEventTestCase {

  public BoundedNumberVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedNumberVetoableChangeListenerTest.class);
    //suite.addTest(new BoundedNumberVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final MockBean bean = new MockBean();
    BoundedNumberVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedNumberVetoableChangeListener(bean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of BoundedNumberVetoableChangeListener with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(bean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    BoundedNumberVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedNumberVetoableChangeListener(null);
      fail("Instantiating an instance of BoundedNumberVetoableChangeListener with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BoundedNumberVetoableChangeListener with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testHandleWithDifferentRegisteredAndEventBeans() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedNumberVetoableChangeListener listener = new BoundedNumberVetoableChangeListener(bean);

    assertNotNull(listener);
    assertSame(bean, listener.getBean());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "nonConstraineValue", null, 2));
      fail("Calling handle with a different event Bean from the registered Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The registered bean and event bean are not the same object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling handle with a different event Bean from the registered Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleUsingMalformedAnnotationDeclaration() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getInvalidAnnotatedValue());

    try {
      bean.setInvalidAnnotatedValue(1);
      fail("Calling setInvalidAnnotatedValue with a malformed BoundedNumber Annotation should have thrown an MalformedAnnotationDeclarationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("The maximum value (-1.0) must be greater than equal to the minimum value (0.0)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setInvalidAnnotatedValue with a malformed BoundedNumber Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getInvalidAnnotatedValue());
  }

  public void testHandleAndIllegalUseOfAnnotation() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getIllegalAnnotatedValue());

    try {
      bean.setIllegalAnnotatedValue(Boolean.TRUE);
      fail("Calling setIllegalAnnotatedValue with Boolean true having a BoundedNumber Annotation should have thrown an IllegalUseOfAnnotationException!");
    }
    catch (IllegalUseOfAnnotationException e) {
      assertEquals("Property (illegalAnnotatedValue) on bean (" + bean.getClass().getName()
        + ") has a BoundedNumber Annotation but is not of type Number!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setIllegalAnnotatedValue with Boolean true having a BoundedNumber Annotation threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getIllegalAnnotatedValue());
  }

  public void testGetBoundedNumberConstraint() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedNumberVetoableChangeListener listener = new BoundedNumberVetoableChangeListener(bean);
    final BoundedNumberVetoableChangeListener.BoundedNumberConstraint constraint = listener.getBoundedNumberConstraint("minimalMaximalValue");

    assertNotNull(constraint);
    assertEquals(-9.0, constraint.getMin());
    assertEquals(9.0, constraint.getMax());
  }

  public void testGetBoundedNumberConstraintForNonConstrainedValue() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedNumberVetoableChangeListener listener = new BoundedNumberVetoableChangeListener(bean);
    final BoundedNumberVetoableChangeListener.BoundedNumberConstraint constraint = listener.getBoundedNumberConstraint("nonConstrainedValue");

    assertNull(constraint);
  }

  public void testMaxNumberConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMaximalValue());

    try {
      bean.setMaximalValue(6);
    }
    catch (Exception e) {
      fail("Setting the maximalValue property of MockBean to 6 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(6, bean.getMaximalValue());

    try {
      bean.setMaximalValue(21);
      fail("Setting the maximalValue property of MockBean to 21 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (21) for property (maximalValue) on bean (" + bean.getClass().getName()
        + ") must be greater than equal (" + (double) Integer.MIN_VALUE + ") and less than equal (9.0)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the maximalValue property of MockBean to 21 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(6, bean.getMaximalValue());

    try {
      bean.setMaximalValue(null);
    }
    catch (Exception e) {
      fail("Setting the maximalValue property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMaximalValue());
  }

  public void testMinNumberConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalValue());

    try {
      bean.setMinimalValue(10);
    }
    catch (Exception e) {
      fail("Setting the minimalValue property of MockBean to 10 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(10, bean.getMinimalValue());

    try {
      bean.setMinimalValue(-2);
      fail("Setting the minimalValue property of MockBean to -2 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (-2) for property (minimalValue) on bean (" + bean.getClass().getName()
        + ") must be greater than equal (0.0) and less than equal (" + (double) Integer.MAX_VALUE + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalValue property of MockBean to -2 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(10, bean.getMinimalValue());

    try {
      bean.setMinimalValue(null);
    }
    catch (Exception e) {
      fail("Setting the minimalValue property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalValue());
  }

  public void testMinMaxNumberConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue(-10);
      fail("Setting the minimalMaximalValue property of MockBean to -10 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (-10) for property (minimalMaximalValue) on bean (" + bean.getClass().getName()
        + ") must be greater than equal (-9.0) and less than equal (9.0)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalValue property of MockBean to -10 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue(10);
      fail("Setting the minimalMaximalValue property of MockBean to 10 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (10) for property (minimalMaximalValue) on bean (" + bean.getClass().getName()
        + ") must be greater than equal (-9.0) and less than equal (9.0)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalValue property of MockBean to 10 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue(0);
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalValue property of MockBean to 0 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(0, bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue(null);
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalValue property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());
  }

  public void testNoNumberConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue(Integer.MIN_VALUE);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedValue property of MockBean to Integer.MIN_VALUE threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(Integer.MIN_VALUE, bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue(Integer.MAX_VALUE);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedValue property of MockBean to Integer.MAX_VALUE threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(Integer.MAX_VALUE, bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue(0);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedValue property of MockBean to 0 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(0, bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue(null);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedValue property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getNonConstrainedValue());
  }

  protected static class MockBean extends AbstractBean {

    private Number invalidAnnotatedValue;
    private Number maximalValue;
    private Number minimalValue;
    private Number minimalMaximalValue;
    private Number nonConstrainedValue;

    private Object illegalAnnotatedValue;

    public Object getIllegalAnnotatedValue() {
      return illegalAnnotatedValue;
    }

    @BoundedNumber(min=-9, max=9)
    public void setIllegalAnnotatedValue(final Object illegalAnnotatedValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.illegalAnnotatedValue = illegalAnnotatedValue;
        }
      };
      processChange("illegalAnnotatedValue", this.illegalAnnotatedValue, illegalAnnotatedValue, callbackHandler);
    }

    public Number getInvalidAnnotatedValue() {
      return invalidAnnotatedValue;
    }

    @BoundedNumber(min=0, max=-1)
    public void setInvalidAnnotatedValue(final Number invalidAnnotatedValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.invalidAnnotatedValue = invalidAnnotatedValue;
        }
      };
      processChange("invalidAnnotatedValue", this.invalidAnnotatedValue, invalidAnnotatedValue, callbackHandler);
    }

    public Number getMaximalValue() {
      return maximalValue;
    }

    @BoundedNumber(min=Integer.MIN_VALUE, max=9)
    public void setMaximalValue(final Number maximalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.maximalValue = maximalValue;
        }
      };
      processChange("maximalValue", this.maximalValue, maximalValue, callbackHandler);
    }

    public Number getMinimalValue() {
      return minimalValue;
    }

    @BoundedNumber(min=0, max=Integer.MAX_VALUE)
    public void setMinimalValue(final Number minimalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalValue = minimalValue;
        }
      };
      processChange("minimalValue", this.minimalValue, minimalValue, callbackHandler);
    }

    public Number getMinimalMaximalValue() {
      return minimalMaximalValue;
    }

    @BoundedNumber(min=-9, max=9)
    public void setMinimalMaximalValue(final Number minimalMaximalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalMaximalValue = minimalMaximalValue;
        }
      };
      processChange("minimalMaximalValue", this.minimalMaximalValue, minimalMaximalValue, callbackHandler);
    }

    public Number getNonConstrainedValue() {
      return nonConstrainedValue;
    }

    public void setNonConstrainedValue(final Number nonConstrainedValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.nonConstrainedValue = nonConstrainedValue;
        }
      };
      processChange("nonConstrainedValue", this.nonConstrainedValue, nonConstrainedValue, callbackHandler);
    }
  }

}
