/*
 * WeekdayTest.java (c) 6 April 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.4.6
 * @see com.cp.common.util.Weekday
 */

package com.cp.common.util;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WeekdayTest extends TestCase {

  public WeekdayTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(WeekdayTest.class);
    return suite;
  }

  public void testSundayEnumeratedValue() throws Exception {
    assertEquals(Calendar.SUNDAY, Weekday.SUNDAY.getCalendarConstant());
    assertEquals(1, Weekday.SUNDAY.getDayOfWeek());
    assertEquals("Sunday", Weekday.SUNDAY.getDescription());
    assertEquals("Sunday", Weekday.SUNDAY.toString());
    assertFalse(Weekday.SUNDAY.isWeekday());
    assertTrue(Weekday.SUNDAY.isWeekend());
  }

  public void testMondayEnumeratedValue() throws Exception {
    assertEquals(Calendar.MONDAY, Weekday.MONDAY.getCalendarConstant());
    assertEquals(2, Weekday.MONDAY.getDayOfWeek());
    assertEquals("Monday", Weekday.MONDAY.getDescription());
    assertEquals("Monday", Weekday.MONDAY.toString());
    assertTrue(Weekday.MONDAY.isWeekday());
    assertFalse(Weekday.MONDAY.isWeekend());
  }

  public void testTuesdayEnumeratedValue() throws Exception {
    assertEquals(Calendar.TUESDAY, Weekday.TUESDAY.getCalendarConstant());
    assertEquals(3, Weekday.TUESDAY.getDayOfWeek());
    assertEquals("Tuesday", Weekday.TUESDAY.getDescription());
    assertEquals("Tuesday", Weekday.TUESDAY.toString());
    assertTrue(Weekday.TUESDAY.isWeekday());
    assertFalse(Weekday.TUESDAY.isWeekend());
  }

  public void testWednesdayEnumeratedValue() throws Exception {
    assertEquals(Calendar.WEDNESDAY, Weekday.WEDNESDAY.getCalendarConstant());
    assertEquals(4, Weekday.WEDNESDAY.getDayOfWeek());
    assertEquals("Wednesday", Weekday.WEDNESDAY.getDescription());
    assertEquals("Wednesday", Weekday.WEDNESDAY.toString());
    assertTrue(Weekday.WEDNESDAY.isWeekday());
    assertFalse(Weekday.WEDNESDAY.isWeekend());
  }

  public void testThursdayEnumeratedValue() throws Exception {
    assertEquals(Calendar.THURSDAY, Weekday.THURSDAY.getCalendarConstant());
    assertEquals(5, Weekday.THURSDAY.getDayOfWeek());
    assertEquals("Thursday", Weekday.THURSDAY.getDescription());
    assertEquals("Thursday", Weekday.THURSDAY.toString());
    assertTrue(Weekday.THURSDAY.isWeekday());
    assertFalse(Weekday.THURSDAY.isWeekend());
  }

  public void testFridayEnumeratedValue() throws Exception {
    assertEquals(Calendar.FRIDAY, Weekday.FRIDAY.getCalendarConstant());
    assertEquals(6, Weekday.FRIDAY.getDayOfWeek());
    assertEquals("Friday", Weekday.FRIDAY.getDescription());
    assertEquals("Friday", Weekday.FRIDAY.toString());
    assertTrue(Weekday.FRIDAY.isWeekday());
    assertFalse(Weekday.FRIDAY.isWeekend());
  }

  public void testSaturdayEnumeratedValue() throws Exception {
    assertEquals(Calendar.SATURDAY, Weekday.SATURDAY.getCalendarConstant());
    assertEquals(7, Weekday.SATURDAY.getDayOfWeek());
    assertEquals("Saturday", Weekday.SATURDAY.getDescription());
    assertEquals("Saturday", Weekday.SATURDAY.toString());
    assertFalse(Weekday.SATURDAY.isWeekday());
    assertTrue(Weekday.SATURDAY.isWeekend());
  }

  public void testGetWeekdayForCalendarConstant() throws Exception {
    assertNull(Weekday.getWeekdayForCalendarConstant(-1));
    assertEquals(Weekday.SUNDAY, Weekday.getWeekdayForCalendarConstant(Calendar.SUNDAY));
    assertEquals(Weekday.MONDAY, Weekday.getWeekdayForCalendarConstant(Calendar.MONDAY));
    assertEquals(Weekday.TUESDAY, Weekday.getWeekdayForCalendarConstant(Calendar.TUESDAY));
    assertEquals(Weekday.WEDNESDAY, Weekday.getWeekdayForCalendarConstant(Calendar.WEDNESDAY));
    assertEquals(Weekday.THURSDAY, Weekday.getWeekdayForCalendarConstant(Calendar.THURSDAY));
    assertEquals(Weekday.FRIDAY, Weekday.getWeekdayForCalendarConstant(Calendar.FRIDAY));
    assertEquals(Weekday.SATURDAY, Weekday.getWeekdayForCalendarConstant(Calendar.SATURDAY));
  }

  public void testGetWeekdayForDayOfWeek() throws Exception {
    assertNull(Weekday.getWeekdayForDayOfWeek(0));
    assertEquals(Weekday.SUNDAY, Weekday.getWeekdayForDayOfWeek(1));
    assertEquals(Weekday.MONDAY, Weekday.getWeekdayForDayOfWeek(2));
    assertEquals(Weekday.TUESDAY, Weekday.getWeekdayForDayOfWeek(3));
    assertEquals(Weekday.WEDNESDAY, Weekday.getWeekdayForDayOfWeek(4));
    assertEquals(Weekday.THURSDAY, Weekday.getWeekdayForDayOfWeek(5));
    assertEquals(Weekday.FRIDAY, Weekday.getWeekdayForDayOfWeek(6));
    assertEquals(Weekday.SATURDAY, Weekday.getWeekdayForDayOfWeek(7));
    assertNull(Weekday.getWeekdayForDayOfWeek(8));
  }

}
