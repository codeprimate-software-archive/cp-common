/*
 * MonthTest.java (c) 6 April 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.4.6
 * @see com.cp.common.util.Month
 */

package com.cp.common.util;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MonthTest extends TestCase {

  public MonthTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MonthTest.class);
    return suite;
  }

  public void testJanuaryEnumeratedValue() throws Exception {
    assertEquals(Calendar.JANUARY, Month.JANUARY.getCalendarConstant());
    assertEquals("January", Month.JANUARY.getDescription());
    assertEquals(1, Month.JANUARY.getMonthOfYear());
    assertEquals("January", Month.JANUARY.toString());
  }

  public void testFebruaryEnumeratedValue() throws Exception {
    assertEquals(Calendar.FEBRUARY, Month.FEBRUARY.getCalendarConstant());
    assertEquals("February", Month.FEBRUARY.getDescription());
    assertEquals(2, Month.FEBRUARY.getMonthOfYear());
    assertEquals("February", Month.FEBRUARY.toString());
  }

  public void testMarchEnumeratedValue() throws Exception {
    assertEquals(Calendar.MARCH, Month.MARCH.getCalendarConstant());
    assertEquals("March", Month.MARCH.getDescription());
    assertEquals(3, Month.MARCH.getMonthOfYear());
    assertEquals("March", Month.MARCH.toString());
  }

  public void testAprilEnumeratedValue() throws Exception {
    assertEquals(Calendar.APRIL, Month.APRIL.getCalendarConstant());
    assertEquals("April", Month.APRIL.getDescription());
    assertEquals(4, Month.APRIL.getMonthOfYear());
    assertEquals("April", Month.APRIL.toString());
  }

  public void testMayEnumeratedValue() throws Exception {
    assertEquals(Calendar.MAY, Month.MAY.getCalendarConstant());
    assertEquals("May", Month.MAY.getDescription());
    assertEquals(5, Month.MAY.getMonthOfYear());
    assertEquals("May", Month.MAY.toString());
  }

  public void testJuneEnumeratedValue() throws Exception {
    assertEquals(Calendar.JUNE, Month.JUNE.getCalendarConstant());
    assertEquals("June", Month.JUNE.getDescription());
    assertEquals(6, Month.JUNE.getMonthOfYear());
    assertEquals("June", Month.JUNE.toString());
  }

  public void testJulyEnumeratedValue() throws Exception {
    assertEquals(Calendar.JULY, Month.JULY.getCalendarConstant());
    assertEquals("July", Month.JULY.getDescription());
    assertEquals(7, Month.JULY.getMonthOfYear());
    assertEquals("July", Month.JULY.toString());
  }

  public void testAugustEnumeratedValue() throws Exception {
    assertEquals(Calendar.AUGUST, Month.AUGUST.getCalendarConstant());
    assertEquals("August", Month.AUGUST.getDescription());
    assertEquals(8, Month.AUGUST.getMonthOfYear());
    assertEquals("August", Month.AUGUST.toString());
  }

  public void testSeptemberEnumeratedValue() throws Exception {
    assertEquals(Calendar.SEPTEMBER, Month.SEPTEMBER.getCalendarConstant());
    assertEquals("September", Month.SEPTEMBER.getDescription());
    assertEquals(9, Month.SEPTEMBER.getMonthOfYear());
    assertEquals("September", Month.SEPTEMBER.toString());
  }

  public void testOctoberEnumeratedValue() throws Exception {
    assertEquals(Calendar.OCTOBER, Month.OCTOBER.getCalendarConstant());
    assertEquals("October", Month.OCTOBER.getDescription());
    assertEquals(10, Month.OCTOBER.getMonthOfYear());
    assertEquals("October", Month.OCTOBER.toString());
  }

  public void testNovemberEnumeratedValue() throws Exception {
    assertEquals(Calendar.NOVEMBER, Month.NOVEMBER.getCalendarConstant());
    assertEquals("November", Month.NOVEMBER.getDescription());
    assertEquals(11, Month.NOVEMBER.getMonthOfYear());
    assertEquals("November", Month.NOVEMBER.toString());
  }

  public void testDecemberEnumeratedValue() throws Exception {
    assertEquals(Calendar.DECEMBER, Month.DECEMBER.getCalendarConstant());
    assertEquals("December", Month.DECEMBER.getDescription());
    assertEquals(12, Month.DECEMBER.getMonthOfYear());
    assertEquals("December", Month.DECEMBER.toString());
  }

  public void testGetMonthForCalendarConstant() throws Exception {
    assertNull(Month.getMonthForCalendarConstant(-1));
    assertEquals(Month.JANUARY, Month.getMonthForCalendarConstant(Calendar.JANUARY));
    assertEquals(Month.FEBRUARY, Month.getMonthForCalendarConstant(Calendar.FEBRUARY));
    assertEquals(Month.MARCH, Month.getMonthForCalendarConstant(Calendar.MARCH));
    assertEquals(Month.APRIL, Month.getMonthForCalendarConstant(Calendar.APRIL));
    assertEquals(Month.MAY, Month.getMonthForCalendarConstant(Calendar.MAY));
    assertEquals(Month.JUNE, Month.getMonthForCalendarConstant(Calendar.JUNE));
    assertEquals(Month.JULY, Month.getMonthForCalendarConstant(Calendar.JULY));
    assertEquals(Month.AUGUST, Month.getMonthForCalendarConstant(Calendar.AUGUST));
    assertEquals(Month.SEPTEMBER, Month.getMonthForCalendarConstant(Calendar.SEPTEMBER));
    assertEquals(Month.OCTOBER, Month.getMonthForCalendarConstant(Calendar.OCTOBER));
    assertEquals(Month.NOVEMBER, Month.getMonthForCalendarConstant(Calendar.NOVEMBER));
    assertEquals(Month.DECEMBER, Month.getMonthForCalendarConstant(Calendar.DECEMBER));
    assertNull(Month.getMonthForCalendarConstant(12));
  }

  public void testGetMonthForMonthOfYear() throws Exception {
    assertNull(Month.getMonthForMonthOfYear(0));
    assertEquals(Month.JANUARY, Month.getMonthForMonthOfYear(1));
    assertEquals(Month.FEBRUARY, Month.getMonthForMonthOfYear(2));
    assertEquals(Month.MARCH, Month.getMonthForMonthOfYear(3));
    assertEquals(Month.APRIL, Month.getMonthForMonthOfYear(4));
    assertEquals(Month.MAY, Month.getMonthForMonthOfYear(5));
    assertEquals(Month.JUNE, Month.getMonthForMonthOfYear(6));
    assertEquals(Month.JULY, Month.getMonthForMonthOfYear(7));
    assertEquals(Month.AUGUST, Month.getMonthForMonthOfYear(8));
    assertEquals(Month.SEPTEMBER, Month.getMonthForMonthOfYear(9));
    assertEquals(Month.OCTOBER, Month.getMonthForMonthOfYear(10));
    assertEquals(Month.NOVEMBER, Month.getMonthForMonthOfYear(11));
    assertEquals(Month.DECEMBER, Month.getMonthForMonthOfYear(12));
    assertNull(Month.getMonthForMonthOfYear(13));
  }

  public void testGetNumberOfDaysInLeapYear() throws Exception {
    assertEquals(31, Month.JANUARY.getNumberOfDays(2008));
    assertEquals(29, Month.FEBRUARY.getNumberOfDays(2004));
    assertEquals(31, Month.MARCH.getNumberOfDays(2000));
    assertEquals(30, Month.APRIL.getNumberOfDays(1996));
    assertEquals(31, Month.MAY.getNumberOfDays(1992));
    assertEquals(30, Month.JUNE.getNumberOfDays(1988));
    assertEquals(31, Month.JULY.getNumberOfDays(1984));
    assertEquals(31, Month.AUGUST.getNumberOfDays(1980));
    assertEquals(30, Month.SEPTEMBER.getNumberOfDays(1976));
    assertEquals(31, Month.OCTOBER.getNumberOfDays(1972));
    assertEquals(30, Month.NOVEMBER.getNumberOfDays(1968));
    assertEquals(31, Month.DECEMBER.getNumberOfDays(1964));
  }

  public void testGetNumberOfDaysInNonLeapYear() throws Exception {
    assertEquals(31, Month.JANUARY.getNumberOfDays(2009));
    assertEquals(28, Month.FEBRUARY.getNumberOfDays(2007));
    assertEquals(31, Month.MARCH.getNumberOfDays(2006));
    assertEquals(30, Month.APRIL.getNumberOfDays(2005));
    assertEquals(31, Month.MAY.getNumberOfDays(2003));
    assertEquals(30, Month.JUNE.getNumberOfDays(2002));
    assertEquals(31, Month.JULY.getNumberOfDays(2001));
    assertEquals(31, Month.AUGUST.getNumberOfDays(1999));
    assertEquals(30, Month.SEPTEMBER.getNumberOfDays(1998));
    assertEquals(31, Month.OCTOBER.getNumberOfDays(1997));
    assertEquals(30, Month.NOVEMBER.getNumberOfDays(1995));
    assertEquals(31, Month.DECEMBER.getNumberOfDays(1994));
  }

}
