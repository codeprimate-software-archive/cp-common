/*
 * DefaultCalendarModelTest.java (c) 1 April 2005
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 */

package com.cp.common.swing;

import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultCalendarModelTest extends TestCase {

  public DefaultCalendarModelTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultCalendarModelTest.class);
    //suite.addTest(new DefaultCalendarModelTest("testName"));
    return suite;
  }

  public void testGetFirstDayOfMonth() throws Exception {
    final CalendarModel model = new DefaultCalendarModel();
    assertEquals(Calendar.FRIDAY, model.getFirstDayOfMonth(5, Calendar.TUESDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(22, Calendar.SATURDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(21, Calendar.FRIDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(20, Calendar.THURSDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(16, Calendar.SUNDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(1, Calendar.SATURDAY));
    assertEquals(Calendar.TUESDAY, model.getFirstDayOfMonth(16, Calendar.WEDNESDAY));
    assertEquals(Calendar.TUESDAY, model.getFirstDayOfMonth(28, Calendar.MONDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(27, Calendar.THURSDAY));
    assertEquals(Calendar.SATURDAY, model.getFirstDayOfMonth(10, Calendar.MONDAY));
    assertEquals(Calendar.MONDAY, model.getFirstDayOfMonth(19, Calendar.FRIDAY));
    assertEquals(Calendar.MONDAY, model.getFirstDayOfMonth(5, Calendar.FRIDAY));
    assertEquals(Calendar.WEDNESDAY, model.getFirstDayOfMonth(15, Calendar.WEDNESDAY));
    assertEquals(Calendar.WEDNESDAY, model.getFirstDayOfMonth(12, Calendar.SUNDAY));
    assertEquals(Calendar.SUNDAY, model.getFirstDayOfMonth(29, Calendar.SUNDAY));
    assertEquals(Calendar.FRIDAY, model.getFirstDayOfMonth(14, Calendar.THURSDAY));
    assertEquals("1st Day of Month should have been on Sunday!", Calendar.SUNDAY, model.getFirstDayOfMonth(7, Calendar.SATURDAY));
    assertEquals("1st Day of Month should have been on Monday!", Calendar.MONDAY, model.getFirstDayOfMonth(7, Calendar.SUNDAY));
    assertEquals("1st Day of Month should have been on Tuesday!", Calendar.TUESDAY, model.getFirstDayOfMonth(7, Calendar.MONDAY));
    assertEquals("1st Day of Month should have been on Wednesday!", Calendar.WEDNESDAY, model.getFirstDayOfMonth(7, Calendar.TUESDAY));
    assertEquals("1st Day of Month should have been on Thursday!", Calendar.THURSDAY, model.getFirstDayOfMonth(7, Calendar.WEDNESDAY));
    assertEquals("1st Day of Month should have been on Friday!", Calendar.FRIDAY, model.getFirstDayOfMonth(7, Calendar.THURSDAY));
    assertEquals("1st Day of Month should have been on Saturday!", Calendar.SATURDAY, model.getFirstDayOfMonth(7, Calendar.FRIDAY));
  }

  public void testGetMonthYearChanged() throws Exception {
    final CalendarModel model = new DefaultCalendarModel(DateUtil.getCalendar(2005, Calendar.APRIL, 14));

    assertFalse(model.getMonthYearChanged());

    model.incrementMonth();

    assertEquals("Expected Date of 5/14/2005!", DateUtil.getCalendar(2005, Calendar.MAY, 14), model.getCalendar());
    assertTrue(model.getMonthYearChanged());
    assertFalse(model.getMonthYearChanged());

    model.incrementMonth();
    model.decrementYear();

    assertEquals("Expected Date of 6/14/2004!", DateUtil.getCalendar(2004, Calendar.JUNE, 14), model.getCalendar());
    assertTrue(model.getMonthYearChanged());
    assertFalse(model.getMonthYearChanged());

    model.set(Calendar.DAY_OF_MONTH, 21);

    assertEquals("Expected Date of 6/21/2004!", DateUtil.getCalendar(2004, Calendar.JUNE, 21), model.getCalendar());
    assertFalse(model.getMonthYearChanged());

    model.set(Calendar.DAY_OF_MONTH, 7);
    model.incrementYear();

    assertEquals("Expected Date of 6/7/2005!", DateUtil.getCalendar(2005, Calendar.JUNE, 7), model.getCalendar());
    assertTrue(model.getMonthYearChanged());
    assertFalse(model.getMonthYearChanged());
  }

  public void testIsMonthYearChanged() throws Exception {
    final CalendarModel model = new DefaultCalendarModel(DateUtil.getCalendar(2005, Calendar.MAY, 27));

    assertFalse(model.isMonthYearChanged());

    model.decrementMonth();
    model.incrementYear();

    assertEquals("Expected Date of 4/27/2006!", DateUtil.getCalendar(2006, Calendar.APRIL, 27), model.getCalendar());
    assertTrue("Month and Year should of changed!", model.isMonthYearChanged());
    assertTrue("Month and Year still should be changed!", model.isMonthYearChanged());

    model.set(Calendar.DAY_OF_MONTH, 7);

    assertEquals("Expected Date of 4/7/2006!", DateUtil.getCalendar(2006, Calendar.APRIL, 7), model.getCalendar());
    assertFalse("Month and Year should be the same!", model.isMonthYearChanged());

    model.set(Calendar.DAY_OF_MONTH, 17);
    model.incrementYear();

    assertEquals("Expected Date of 4/17/2007!", DateUtil.getCalendar(2007, Calendar.APRIL, 17), model.getCalendar());
    assertTrue("Year should of changed!", model.isMonthYearChanged());
  }

  public void testGetNumberOfDaysInMonth() throws Exception {
    final CalendarModel model = new DefaultCalendarModel();
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.JANUARY, 2002));
    assertEquals(28, model.getNumberOfDaysInMonth(Calendar.FEBRUARY, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.MARCH, 2002));
    assertEquals(30, model.getNumberOfDaysInMonth(Calendar.APRIL, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.MAY, 2002));
    assertEquals(30, model.getNumberOfDaysInMonth(Calendar.JUNE, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.JULY, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.AUGUST, 2002));
    assertEquals(30, model.getNumberOfDaysInMonth(Calendar.SEPTEMBER, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.OCTOBER, 2002));
    assertEquals(30, model.getNumberOfDaysInMonth(Calendar.NOVEMBER, 2002));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.DECEMBER, 2002));
    assertEquals(29, model.getNumberOfDaysInMonth(Calendar.FEBRUARY, 2004));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.DECEMBER, 2004));
    assertEquals(28, model.getNumberOfDaysInMonth(Calendar.FEBRUARY, 2005));
    assertEquals(31, model.getNumberOfDaysInMonth(Calendar.JANUARY, 2005));
  }

}
