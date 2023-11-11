/*
 * DateOutOfBoundsVetoableChangeListenerTest.java (c) 12 June 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.14
 * @see com.cp.common.beans.event.DateOutOfBoundsVetoableChangeListener
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.event;

import com.cp.common.beans.Bean;
import com.cp.common.test.mock.MockBean;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.util.DateUtil;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;

public class DateOutOfBoundsVetoableChangeListenerTest extends TestCase {

  public DateOutOfBoundsVetoableChangeListenerTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DateOutOfBoundsVetoableChangeListenerTest.class);
    //suite.addTest(new DateOutOfBoundsVetoableChangeListenerTest("testName"));
    return suite;
  }

  public void testInstantiation() throws Exception {
    final Calendar january221975 = DateUtil.getCalendar(1975, Calendar.JANUARY, 22);
    final Calendar may271974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);

    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener("dateOfBirth", may271974, january221975);
    }
    catch (Exception e) {
      fail("Instantiating and instance of DateOutOfBoundsVetoableChangeListener with a min date of May 27th, 1974 and max date of Janaury 22nd, 1975 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(may271974, listener.getMinDate());
    assertEquals(january221975, listener.getMaxDate());
    assertEquals("dateOfBirth", listener.getPropertyName());
  }

  public void testInstantiationWithNullMaxDate() throws Exception {
    final Calendar may271974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);

    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener("dateOfBirth", may271974, null);
    }
    catch (Exception e) {
      fail("Instantiating and instance of DateOutOfBoundsVetoableChangeListener with a min date of May 27th, 1974 and null max date threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(may271974, listener.getMinDate());
    assertNull(listener.getMaxDate());
    assertEquals("dateOfBirth", listener.getPropertyName());
  }

  public void testInstantiationWithNullMinDate() throws Exception {
    final Calendar january221975 = DateUtil.getCalendar(1975, Calendar.JANUARY, 22);

    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener(null, january221975);
    }
    catch (Exception e) {
      fail("Instantiating and instance of DateOutOfBoundsVetoableChangeListener with a null min date and max date of Janaury 22nd, 1975 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertNull(listener.getMinDate());
    assertEquals(january221975, listener.getMaxDate());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithEmptyProperty() throws Exception {
    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener(" ", null, null);
      fail("Instantiating an instance of DateOutOfBoundsVetoableChangeListener with an empty property should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The name of the property cannot be empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of DateOutOfBoundsVetoableChangeListener with an empty property  threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testInstantiationWithNullProperty() throws Exception {
    final Calendar january221975 = DateUtil.getCalendar(1975, Calendar.JANUARY, 22);
    final Calendar may271974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);

    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener(may271974, january221975);
    }
    catch (Exception e) {
      fail("Instantiating and instance of DateOutOfBoundsVetoableChangeListener with a min date of May 27th, 1974 and max date of Janaury 22nd, 1975 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(listener);
    assertNull(listener.getBean());
    assertNull(listener.getBeanInfo());
    assertEquals(may271974, listener.getMinDate());
    assertEquals(january221975, listener.getMaxDate());
    assertNull(listener.getPropertyName());
  }

  public void testInstantiationWithMinDateAfterMaxDate() throws Exception {
    final Calendar january221975 = DateUtil.getCalendar(1975, Calendar.JANUARY, 22);
    final Calendar may271974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);

    DateOutOfBoundsVetoableChangeListener listener = null;

    assertNull(listener);

    try {
      listener = new DateOutOfBoundsVetoableChangeListener(january221975, may271974);
      fail("Instantiating an instance of DateOutOfBoundsVetoableChangeListener with a min date after max date should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The min date (" + DateUtil.toString(january221975) + ") cannot be after max date ("
          + DateUtil.toString(may271974) + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of DateOutOfBoundsVetoableChangeListener with a min date after max date threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(listener);
  }

  public void testGetMaxDate() throws Exception {
    final Calendar september132008 = DateUtil.getCalendar(2008, Calendar.SEPTEMBER, 13);
    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(null, september132008);

    assertNotNull(listener);
    assertNull(listener.getMinDate());
    assertEquals(september132008, listener.getMaxDate());

    final Calendar date = listener.getMaxDate();
    date.set(Calendar.YEAR, 1998);
    date.set(Calendar.MONTH, Calendar.APRIL);
    date.set(Calendar.DAY_OF_MONTH, 26);

    assertFalse(date.equals(september132008));
    assertEquals(september132008, listener.getMaxDate());
  }

  public void testGetMinDate() throws Exception {
    final Calendar september142008 = DateUtil.getCalendar(2008, Calendar.SEPTEMBER, 14);
    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(september142008, null);

    assertNotNull(listener);
    assertEquals(september142008, listener.getMinDate());
    assertNull(listener.getMaxDate());

    final Calendar date = listener.getMinDate();
    date.set(Calendar.YEAR, 2018);
    date.set(Calendar.MONTH, Calendar.APRIL);
    date.set(Calendar.DAY_OF_MONTH, 26);

    assertFalse(date.equals(september142008));
    assertEquals(september142008, listener.getMinDate());
  }

  public void testAcceptWithMaxDateConstraint() throws Exception {
    final Calendar june302007 = DateUtil.getCalendar(2007, Calendar.JUNE, 30);
    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(null, june302007);

    assertNotNull(listener);
    assertNull(listener.getMinDate());
    assertEquals(june302007, listener.getMaxDate());
    assertTrue(listener.accept(DateUtil.getCalendar(2006, Calendar.AUGUST, 17)));
    assertFalse(listener.accept(DateUtil.getCalendar(2008, Calendar.FEBRUARY, 14)));
    assertTrue(listener.accept(june302007));
    assertFalse(listener.accept(Calendar.getInstance()));
  }

  public void testAcceptWithMinDateConstraint() throws Exception {
    final Calendar july12007 = DateUtil.getCalendar(2007, Calendar.JULY, 1);
    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(july12007, null);

    assertNotNull(listener);
    assertEquals(july12007, listener.getMinDate());
    assertNull(listener.getMaxDate());
    assertFalse(listener.accept(DateUtil.getCalendar(2006, Calendar.NOVEMBER, 11)));
    assertTrue(listener.accept(DateUtil.getCalendar(2008, Calendar.MAY, 27)));
    assertTrue(listener.accept(july12007));
    assertTrue(listener.accept(Calendar.getInstance()));
  }

  public void testAcceptWithMinMaxDateConstraint() throws Exception {
    final Calendar january1st2007 = DateUtil.getCalendar(2007, Calendar.JANUARY, 1);
    final Calendar december31st2007 = DateUtil.getCalendar(2007, Calendar.DECEMBER, 31);

    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(
      january1st2007, december31st2007);

    assertNotNull(listener);
    assertEquals(january1st2007, listener.getMinDate());
    assertEquals(december31st2007, listener.getMaxDate());
    assertTrue(listener.accept(DateUtil.getCalendar(2007, Calendar.JANUARY, 22)));
    assertFalse(listener.accept(DateUtil.getCalendar(2006, Calendar.DECEMBER, 31)));
    assertFalse(listener.accept(DateUtil.getCalendar(2008, Calendar.JANUARY, 1)));
    assertFalse(listener.accept(Calendar.getInstance()));
    assertTrue(listener.accept(DateUtil.getCalendar(2007, Calendar.MAY, 27)));
  }

  public void testAcceptNoDateConstraint() throws Exception {
    final Calendar today = Calendar.getInstance();
    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(null, null);

    assertNotNull(listener);
    assertNull(listener.getMinDate());
    assertNull(listener.getMaxDate());
    assertTrue(listener.accept(DateUtil.getCalendar(1776, Calendar.JULY, 4)));
    assertTrue(listener.accept(Calendar.getInstance()));
    assertTrue(listener.accept(DateUtil.getCalendar(today.get(Calendar.YEAR) + 100, Calendar.SEPTEMBER, 14)));
  }

  public void testHandleThrowingIllegalUseOfListenerException() throws Exception {
    final MockBean bean = new MockBeanImpl();
    bean.addVetoableChangeListener("value", new DateOutOfBoundsVetoableChangeListener(Calendar.getInstance(), null));

    assertNull(bean.getValue());

    try {
      bean.setValue("null");
      fail("Setting the value property of MockBean with the literal String 'null' having a DateOutOfBoundsVetoableChangeListener registered for the value property should have thrown an IllegalUseOfListenerException!");
    }
    catch (IllegalUseOfListenerException e) {
      assertEquals("property (value) of bean (" + bean.getClass().getName() + ") is not of type Calendar!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Setting the value property of MockBean with the literal String 'null' having a DateOutOfBoundsVetoableChangeListener registered for the value property threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(bean.getValue());
  }

  public void testHandleAcceptingDate() throws Exception {
    final Calendar january12008 = DateUtil.getCalendar(2008, Calendar.JANUARY, 1);
    final Calendar december312008 = DateUtil.getCalendar(2008, Calendar.DECEMBER, 31);

    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(
      january12008, december312008);

    assertNotNull(listener);
    assertEquals(january12008, listener.getMinDate());
    assertEquals(december312008, listener.getMaxDate());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null,
        DateUtil.getCalendar(2008, Calendar.JULY, 4)));
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a new property value of July 4th, 2008 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testHandleRejectingDate() throws Exception {
    final Calendar january12008 = DateUtil.getCalendar(2008, Calendar.JANUARY, 1);
    final Calendar july42009 = DateUtil.getCalendar(2009, Calendar.JULY, 4);
    final Calendar december312008 = DateUtil.getCalendar(2008, Calendar.DECEMBER, 31);

    final DateOutOfBoundsVetoableChangeListener listener = new DateOutOfBoundsVetoableChangeListener(
      january12008, december312008);

    assertNotNull(listener);
    assertEquals(january12008, listener.getMinDate());
    assertEquals(december312008, listener.getMaxDate());

    try {
      listener.vetoableChange(new PropertyChangeEvent(EasyMock.createMock(Bean.class), "value", null, july42009));
      fail("Calling vetoableChange with a new property value of July 4th, 2009 should have thrown a PropertyVetoException!");
    }
    catch (PropertyVetoException e) {
      assertEquals("The new date value (" + DateUtil.toString(july42009)
        + ") is out of bounds; the date is not between the min (" + DateUtil.toString(january12008) + ") and max ("
        + DateUtil.toString(december312008) + ") dates inclusive!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling vetoableChange with a new property value of July 4th, 2009 threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

}
