/*
 * BoundedDateVetoableChangeListenerTest.java (c) 12 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.13
 * @see com.cp.common.beans.event.BoundedDateVetoableChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.AbstractBean;
import com.cp.common.beans.Bean;
import com.cp.common.beans.ConstraintViolationException;
import com.cp.common.beans.annotation.BoundedDate;
import com.cp.common.beans.annotation.IllegalUseOfAnnotationException;
import com.cp.common.beans.annotation.MalformedAnnotationDeclarationException;
import com.cp.common.util.DateUtil;
import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class BoundedDateVetoableChangeListenerTest extends TestCase {

  public BoundedDateVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BoundedDateVetoableChangeListenerTest.class);
    //suite.addTest(new BoundedDateVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final Bean mockBean = EasyMock.createMock(Bean.class);
    BoundedDateVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedDateVetoableChangeListener(mockBean);
    }
    catch (Exception e) {
      fail("Instantiating an instance of BoundedDateVetoableChangeListener with non-null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());
    assertNotNull(listener.getBeanInfo());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithNullBean() throws Exception {
    BoundedDateVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new BoundedDateVetoableChangeListener(null);
      fail("Instantiating an instance of BoundedDateVetoableChangeListener with a null Bean should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of BoundedDateVetoableChangeListener with a null Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testHandleWithDifferentRegisteredAndEventBeans() throws Exception {
    final Bean mockBean = new MockBean();
    final BoundedDateVetoableChangeListener listener = new BoundedDateVetoableChangeListener(mockBean);

    assertNotNull(listener);
    assertSame(mockBean, listener.getBean());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "dateOfBirth", null,
        DateUtil.getCalendar(2008, Calendar.SEPTEMBER, 5)));
      fail("Calling vetoableChange with an event Bean different from the registered Bean should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The registered bean and the event bean are not the same object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with an event Bean different from the registered Bean threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleUsingMalformedAnnotationDeclaration() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getDateValue());

    try {
      bean.setDateValue(DateUtil.getCalendar(2008, Calendar.SEPTEMBER, 7, 21, 1, 30, 0));
      fail("Calling setDateValue on the MockBean having an malformed Annotation declared for the dateValue property shouild have thrown an MalformedAnnotationDeclarationException!");
    }
    catch (MalformedAnnotationDeclarationException e) {
      assertEquals("(01/01/) is not a valid date as defined by pattern (MM/dd/yyyy hh:mm)!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setDateValue on the MockBean having an malformed Annotation declared for the dateValue property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getDateValue());
  }

  public void testHandleAndIllegalUseOfAnnotation() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getValue());

    try {
      bean.setValue("test");
      fail("Setting the non-Calendar value property of MockBean having a BoundedDate Annotation constraint should have thrown an IllegalUseOfAnnotationException!");
    }
    catch (IllegalUseOfAnnotationException e) {
      assertEquals("Property (value) on bean (" + bean.getClass().getName()
        + ") has a BoundedDate Annotation but is not of type Calendar!", e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the non-Calendar value property of MockBean having a BoundedDate Annotation constraint threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getValue());
  }

  public void testGetBoundedDateConstraint() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedDateVetoableChangeListener listener = new BoundedDateVetoableChangeListener(bean);
    final BoundedDateVetoableChangeListener.BoundedDateConstraint constraint = listener.getBoundedDateConstraint("minimalMaximalDate");

    assertNotNull(constraint);
    assertEquals(DateUtil.getCalendar(2007, Calendar.JANUARY, 1), constraint.getMinDate());
    assertEquals(DateUtil.getCalendar(2007, Calendar.DECEMBER, 31), constraint.getMaxDate());
    assertEquals("MM/dd/yyyy", constraint.getPattern());
  }

  public void testGetBoundedDateConstraintForNonBoundedDateAnnotatedProperty() throws Exception {
    final MockBean bean = new MockBean();
    final BoundedDateVetoableChangeListener listener = new BoundedDateVetoableChangeListener(bean);
    final BoundedDateVetoableChangeListener.BoundedDateConstraint constraint = listener.getBoundedDateConstraint("nonConstrainedDate");

    assertNull(constraint);
  }

  public void testMaxDateConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMaximalDate());

    final Calendar november222007 = DateUtil.getCalendar(2007, Calendar.NOVEMBER, 22);

    try {
      bean.setMaximalDate(november222007);
    }
    catch (Exception e) {
      fail("Setting the maximalDate property on MockBean to 11/22/2007 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(november222007, bean.getMaximalDate());

    final Calendar february222009 = DateUtil.getCalendar(2009, Calendar.FEBRUARY, 22);

    try {
      bean.setMaximalDate(february222009);
      fail("Setting the maximalDate property of MockBean to 02/22/2009 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (" + DateUtil.toString(february222009) + ") for property (maximalDate) on bean ("
        + bean.getClass().getName() + ") must be greater than equal (null) and less than equal (08/08/2008 12:00:00 AM)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the maximalDate property of MockBean to 02/22/2009 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(november222007, bean.getMaximalDate());

    try {
      bean.setMaximalDate(null);
    }
    catch (Exception e) {
      fail("Setting the maximalDate property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMaximalDate());
  }

  public void testMinDateConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalDate());

    final Calendar june022007 = DateUtil.getCalendar(2007, Calendar.JUNE, 2);

    try {
      bean.setMinimalDate(june022007);
    }
    catch (Exception e) {
      fail("Setting the minimalDate property on MockBean to 06/02/2007 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(june022007, bean.getMinimalDate());

    final Calendar november222005 = DateUtil.getCalendar(2005, Calendar.NOVEMBER, 22);

    try {
      bean.setMinimalDate(november222005);
      fail("Setting the minimalDate property of MockBean to 11/22/2005 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (" + DateUtil.toString(november222005) + ") for property (minimalDate) on bean ("
        + bean.getClass().getName() + ") must be greater than equal (08/08/2006 12:00:00 AM) and less than equal (null)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalDate property of MockBean to 11/22/2005 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(june022007, bean.getMinimalDate());

    try {
      bean.setMinimalDate(null);
    }
    catch (Exception e) {
      fail("Setting the minimalDate property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalDate());
  }

  public void testMinAndMaxDateConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getMinimalMaximalDate());

    final Calendar may272006 = DateUtil.getCalendar(2006, Calendar.MAY, 27);

    try {
      bean.setMinimalMaximalDate(may272006);
      fail("Setting the minimalMaximalDate property of MockBean to 05/27/2006 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (" + DateUtil.toString(may272006) + ") for property (minimalMaximalDate) on bean ("
        + bean.getClass().getName() + ") must be greater than equal (01/01/2007 12:00:00 AM) and less than equal (12/31/2007 12:00:00 AM)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalDate property of MockBean to 05/27/2006 threw unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalDate());

    final Calendar january012007 = DateUtil.getCalendar(2007, Calendar.JANUARY, 1);

    try {
      bean.setMinimalMaximalDate(january012007);
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalDate property of MockBean to 01/01/2007 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(january012007, bean.getMinimalMaximalDate());

    try {
      bean.setMinimalMaximalDate(null);
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalDate property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getMinimalMaximalDate());

    final Calendar december312007 = DateUtil.getCalendar(2007, Calendar.DECEMBER, 31);

    try {
      bean.setMinimalMaximalDate(december312007);
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalDate property of MockBean to 12/31/2007 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(december312007, bean.getMinimalMaximalDate());

    final Calendar february012008 = DateUtil.getCalendar(2008, Calendar.FEBRUARY, 1);

    try {
      bean.setMinimalMaximalDate(february012008);
      fail("Setting the minimalMaximalDate property of MockBean to 02/01/2008 should have thrown a ConstraintViolationException!");
    }
    catch (ConstraintViolationException e) {
      assertEquals("The value (" + DateUtil.toString(february012008) + ") for property (minimalMaximalDate) on bean ("
        + bean.getClass().getName() + ") must be greater than equal (01/01/2007 12:00:00 AM) and less than equal (12/31/2007 12:00:00 AM)!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the minimalMaximalDate property of MockBean to 02/01/2008 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(december312007, bean.getMinimalMaximalDate());
  }

  public void testNoDateConstraint() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getNonConstrainedDate());

    final Calendar historicDate = DateUtil.getCalendar(1776, Calendar.JULY, 4);

    try {
      bean.setNonConstrainedDate(historicDate);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedDate property of MockBean to the historic date (07/04/1776) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(historicDate, bean.getNonConstrainedDate());

    final Calendar futureDate = DateUtil.getCalendar(2074, Calendar.MAY, 27);

    try {
      bean.setNonConstrainedDate(futureDate);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedDate property of MockBean to the future date (05/27/2074) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(futureDate, bean.getNonConstrainedDate());

    try {
      bean.setNonConstrainedDate(null);
    }
    catch (Exception e) {
      fail("Setting the nonConstrainedDate property of MockBean to null threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getNonConstrainedDate());
  }

  protected static final class MockBean extends AbstractBean<Integer> {

    private Calendar dateValue;
    private Calendar maximalDate;
    private Calendar minimalDate;
    private Calendar minimalMaximalDate;
    private Calendar nonConstrainedDate;

    private Object value;

    public Calendar getDateValue() {
      return dateValue;
    }

    @BoundedDate(min="01/01/", max="12/31/2008", pattern="MM/dd/yyyy hh:mm")
    public void setDateValue(final Calendar dateValue) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.dateValue = dateValue;
        }
      };
      processChange("dateValue", this.dateValue, dateValue, callbackHandler);
    }

    public Calendar getMaximalDate() {
      return maximalDate;
    }

    // NOTE no defensive copying performed
    @BoundedDate(min="", max="08/08/2008", pattern="MM/dd/yyyy")
    public void setMaximalDate(final Calendar maximalDate) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.maximalDate = maximalDate;
        }
      };
      processChange("maximalDate", this.maximalDate, maximalDate, callbackHandler);
    }

    public Calendar getMinimalDate() {
      return minimalDate;
    }

    // NOTE no defensive copying performed
    @BoundedDate(min="08/08/2006", max="", pattern="MM/dd/yyyy")
    public void setMinimalDate(final Calendar minimalDate) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalDate = minimalDate;
        }
      };
      processChange("minimalDate", this.minimalDate, minimalDate, callbackHandler);
    }

    public Calendar getMinimalMaximalDate() {
      return minimalMaximalDate;
    }

    // NOTE no defensive copying performed
    @BoundedDate(min="01/01/2007", max="12/31/2007", pattern="MM/dd/yyyy")
    public void setMinimalMaximalDate(final Calendar minimalMaximalDate) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.minimalMaximalDate = minimalMaximalDate;
        }
      };
      processChange("minimalMaximalDate", this.minimalMaximalDate, minimalMaximalDate, callbackHandler);
    }

    public Calendar getNonConstrainedDate() {
      return nonConstrainedDate;
    }

    // NOTE no defensive copying performed
    public void setNonConstrainedDate(final Calendar nonConstrainedDate) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.nonConstrainedDate = nonConstrainedDate;
        }
      };
      processChange("nonConstrainedDate", this.nonConstrainedDate, nonConstrainedDate, callbackHandler);
    }

    public Object getValue() {
      return value;
    }

    @BoundedDate(min="08/01/2007", max="08/31/2007", pattern="MM/dd/yyyy")
    public void setValue(final Object value) {
      final StateChangeCallbackHandler callbackHandler = new StateChangeCallbackHandler() {
        public void changeState() {
          MockBean.this.value = value;
        }
      };
      processChange("value", this.value, value, callbackHandler);
    }
  }

}
