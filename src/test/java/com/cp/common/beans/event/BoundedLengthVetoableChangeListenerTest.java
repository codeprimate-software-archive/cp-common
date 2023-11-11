/*
 * BoundedLengthVetoableChangeListenerTest.java (c) 8 September 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.9.13
 * @see com.cp.common.beans.event.BoundedLengthVetoableChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.annotation.BoundedLength;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import java.beans.PropertyChangeEvent;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class BoundedLengthVetoableChangeListenerTest extends TestCase {

  public BoundedLengthVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedLengthVetoableChangeListenerTest.class);
    //suite.addTest(new BoundedLengthVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final MockBean bean = new MockBean();
    BoundedLengthVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedLengthVetoableChangeListener(bean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the BoundedLengthVetoableChangeListener with a non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(bean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    BoundedLengthVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedLengthVetoableChangeListener(null);
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the BoundedLengthVetoableChangeListener with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testHandleWithDifferentRegisteredAndEventBeans() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedLengthVetoableChangeListener listener = new BoundedLengthVetoableChangeListener(bean);

    assertNotNull(listener);
    assertSame(bean, listener.getBean());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "lastName", null, "Doe"));
      fail("Firing a vetoableChange with a PropertyChangeEvent having an event bean different from the registered bean on the listener should have thrown an IllegalStateException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The registered bean and the event bean are not the same object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Firing a vetoableChange with a PropertyChangeEvent having an event bean different from the registered bean on the listener threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleUsingMalformedAnnotationDeclaration() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getInvalidMaxValue());

    try {
      bean.setInvalidMaxValue("test");
      fail("Calling setInvalidMaxValue should have thrown a MalformedAnnotationDeclarationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("The maximum length (5) must be greater than equal to the minimum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setInvalidMaxValue threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getInvalidMaxValue());
    assertNull(bean.getInvalidMinValue());

    try {
      bean.setInvalidMinValue("test");
      fail("Calling setInvalidMinValue should have thrown a MalformedAnnotationDeclarationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("The minimum length (-1) must be greater than or equal to 0!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setInvalidMinValue threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getInvalidMinValue());
  }

  public void testHandleAndIllegalUseOfAnnotation() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getIllegalAnnotatedValue());

    try {
      bean.setIllegalAnnotatedValue(Boolean.TRUE);
      fail("Calling setIllegalAnnotatedValue with a boolean value annotated with a BoundedLength Annotation should have thrown an IllegalUseOfAnnotationException!");
    }
    catch (IllegalUseOfAnnotationException e) {
      assertEquals("Property (illegalAnnotatedValue) on bean (" + bean.getClass().getName() +
        ") has a BoundedLength Annotation but is not of type String!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setIllegalAnnotatedValue with a boolean value annotated with a BoundedLength Annotation  threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getIllegalAnnotatedValue());
  }

  public void testGetBoundedLengthConstraint() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedLengthVetoableChangeListener listener = new BoundedLengthVetoableChangeListener(bean);

    assertNotNull(listener);
    assertSame(bean, listener.getBean());

    BoundedLengthVetoableChangeListener.BoundedLengthConstraint constraint = null;

    assertNull(constraint);

    try {
      constraint = listener.getBoundedLengthConstraint("minimalMaximalValue");
    }
    catch (Exception e) {
      fail("Getting the BoundedLengthConstraint for property (minimalMaximalValue) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(constraint);
    assertEquals(5, constraint.getMin());
    assertEquals(10, constraint.getMax());
  }

  public void testGetBoundedLengthConstraintForNonConstrainedProperty() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedLengthVetoableChangeListener listener = new BoundedLengthVetoableChangeListener(bean);

    assertNotNull(listener);
    assertSame(bean, listener.getBean());

    BoundedLengthVetoableChangeListener.BoundedLengthConstraint constraint = null;

    assertNull(constraint);

    try {
      constraint = listener.getBoundedLengthConstraint("nonConstrainedValue");
    }
    catch (Exception e) {
      fail("Getting the BoundedLengthConstraint for property (nonConstrainedValue) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(constraint);
  }

  public void testMaxLengthConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMaximalValue());

    try {
      bean.setMaximalValue("test");
    }
    catch (Exception e) {
      fail("Calling setMaximalValue with 'test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("test", bean.getMaximalValue());

    try {
      bean.setMaximalValue("acceptance test");
      fail("Calling setMaximalValue with 'acceptance test' should have thrown an ConstraintViolationExeption!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The length of value (acceptance test) for property (maximalValue) on bean ("
        + bean.getClass().getName() + ") must be between minimum length (0) and maximum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setMaximalValue with 'acceptance test' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("test", bean.getMaximalValue());

    try {
      bean.setMaximalValue(null);
    }
    catch (Exception e) {
      fail("Calling setMaximalValue with null threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getMaximalValue());
  }

  public void testMinLengthConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalValue());

    try {
      bean.setMinimalValue("acceptance test");
    }
    catch (Exception e) {
      fail("Calling setMinimalValue with 'acceptance test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("acceptance test", bean.getMinimalValue());

    try {
      bean.setMinimalValue("test");
      fail("Calling setMinimalValue with 'test' should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The length of value (test) for property (minimalValue) on bean (" + bean.getClass().getName()
        + ") must be between minimum length (5) and maximum length (" + Integer.MAX_VALUE + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setMinimalValue with 'test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("acceptance test", bean.getMinimalValue());

    try {
      bean.setMinimalValue(null);
    }
    catch (Exception e) {
      fail("Calling setMinimalValue with null threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalValue());
  }

  public void testMinAndMaxLengthConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue("test");
      fail("Calling setMinimalMaximalValue with 'test' should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The length of value (test) for property (minimalMaximalValue) on bean (" + bean.getClass().getName()
        + ") must be between minimum length (5) and maximum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setMinimalMaximalValue with 'test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue("acceptance test");
      fail("Calling setMinimalMaximalValue with 'acceptance test' should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The length of value (acceptance test) for property (minimalMaximalValue) on bean ("
        + bean.getClass().getName() + ") must be between minimum length (5) and maximum length (10)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setMinimalMaximalValue with 'acceptance test' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue("unit test");
    }
    catch (Exception e) {
      fail("Calling setMinimalMaximalValue with 'unit test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("unit test", bean.getMinimalMaximalValue());

    try {
      bean.setMinimalMaximalValue(null);
    }
    catch (Exception e) {
      fail("Calling setMinimalMaximalValue with null threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalValue());
  }

  public void testNoLengthConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue("test");
    }
    catch (Exception e) {
      fail("Calling setNonConstrainedValue with 'test' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals("test", bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue("acceptance test");
    }
    catch (Exception e) {
      fail("Calling setNonConstrainedValue with 'acceptance test' threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals("acceptance test", bean.getNonConstrainedValue());

    try {
      bean.setNonConstrainedValue(null);
    }
    catch (Exception e) {
      fail("Calling setNonConstrainedValue with null threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(bean.getNonConstrainedValue());
  }

  protected static final class MockBean extends AbstractBean<Integer> {

    private Object illegalAnnotatedValue;

    private String invalidMaxValue;
    private String invalidMinValue;
    private String maximalValue;
    private String minimalValue;
    private String minimalMaximalValue;
    private String nonConstrainedValue;

    public Object getIllegalAnnotatedValue() {
      return illegalAnnotatedValue;
    }

    @BoundedLength(min=5, max=10)
    public void setIllegalAnnotatedValue(final Object illegalAnnotatedValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.illegalAnnotatedValue = illegalAnnotatedValue;
        }
      };
      processChange("illegalAnnotatedValue", this.illegalAnnotatedValue, illegalAnnotatedValue, callbackHandler);
    }

    public String getInvalidMaxValue() {
      return invalidMaxValue;
    }

    @BoundedLength(min=10, max=5)
    public void setInvalidMaxValue(final String invalidMaxValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.invalidMaxValue = invalidMaxValue;
        }
      };
      processChange("invalidMaxValue", this.invalidMaxValue, invalidMaxValue, callbackHandler);
    }

    public String getInvalidMinValue() {
      return invalidMinValue;
    }

    @BoundedLength(min=-1, max=10)
    public void setInvalidMinValue(final String invalidMinValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.invalidMinValue = invalidMinValue;
        }
      };
      processChange("invalidMinValue", this.invalidMinValue, invalidMinValue, callbackHandler);
    }

    public String getMaximalValue() {
      return maximalValue;
    }

    @BoundedLength(min=0, max=10)
    public void setMaximalValue(final String maximalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.maximalValue = maximalValue;
        }
      };
      processChange("maximalValue", this.maximalValue, maximalValue, callbackHandler);
    }

    public String getMinimalValue() {
      return minimalValue;
    }

    @BoundedLength(min=5, max=Integer.MAX_VALUE)
    public void setMinimalValue(final String minimalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalValue = minimalValue;
        }
      };
      processChange("minimalValue", this.minimalValue, minimalValue, callbackHandler);
    }

    public String getMinimalMaximalValue() {
      return minimalMaximalValue;
    }

    @BoundedLength(min=5, max=10)
    public void setMinimalMaximalValue(final String minimalMaximalValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalMaximalValue = minimalMaximalValue;
        }
      };
      processChange("minimalMaximalValue", this.minimalMaximalValue, minimalMaximalValue, callbackHandler);
    }

    public String getNonConstrainedValue() {
      return nonConstrainedValue;
    }

    public void setNonConstrainedValue(final String nonConstrainedValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.nonConstrainedValue = nonConstrainedValue;
        }
      };
      processChange("nonConstrainedValue", this.nonConstrainedValue, nonConstrainedValue, callbackHandler);
    }
  }

}
