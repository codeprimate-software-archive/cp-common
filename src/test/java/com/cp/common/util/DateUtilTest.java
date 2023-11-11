/*
 * DateUtilTest.java (c) 24 October 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.6.12
 * @see com.cp.common.util.DateUtil
 * @see java.util.Calendar
 * @see java.util.Date
 * @see java.util.Locale
 * @see java.util.TimeZone
 */

package com.cp.common.util;

import com.cp.common.test.util.TestUtil;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DateUtilTest extends TestCase {

  public DateUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DateUtilTest.class);
    //suite.addTest(new DateUtilTest("testName"));
    return suite;
  }

  public void testGetCalendarForDate() throws Exception {
    final Calendar theDate = DateUtil.getCalendar(new Date());
    final Calendar now = Calendar.getInstance();

    assertEquals(theDate.get(Calendar.YEAR), now.get(Calendar.YEAR));
    assertEquals(theDate.get(Calendar.MONTH), now.get(Calendar.MONTH));
    assertEquals(theDate.get(Calendar.DAY_OF_MONTH), now.get(Calendar.DAY_OF_MONTH));
    assertEquals(theDate.get(Calendar.HOUR), now.get(Calendar.HOUR));
    assertEquals(theDate.get(Calendar.MINUTE), now.get(Calendar.MINUTE));
    assertEquals(theDate.get(Calendar.SECOND), now.get(Calendar.SECOND));
    //assertEquals(theDate.get(Calendar.MILLISECOND), now.get(Calendar.MILLISECOND));
    assertNull(DateUtil.getCalendar((Date) null));
  }

  public void testGetCalendarForMillis() throws Exception {
    final Calendar date = DateUtil.getCalendar(1980, Calendar.NOVEMBER, 27, 14, 15, 30, 300);

    assertNotNull(date);
    assertEquals(1980, date.get(Calendar.YEAR));
    assertEquals(Calendar.NOVEMBER, date.get(Calendar.MONTH));
    assertEquals(27, date.get(Calendar.DAY_OF_MONTH));
    assertEquals(14, date.get(Calendar.HOUR_OF_DAY));
    assertEquals(15, date.get(Calendar.MINUTE));
    assertEquals(30, date.get(Calendar.SECOND));
    assertEquals(300, date.get(Calendar.MILLISECOND));

    final Calendar calendarFromMillis = DateUtil.getCalendar(date.getTimeInMillis());

    assertNotNull(calendarFromMillis);
    assertEquals(1980, calendarFromMillis.get(Calendar.YEAR));
    assertEquals(Calendar.NOVEMBER, calendarFromMillis.get(Calendar.MONTH));
    assertEquals(27, calendarFromMillis.get(Calendar.DAY_OF_MONTH));
    assertEquals(14, calendarFromMillis.get(Calendar.HOUR_OF_DAY));
    assertEquals(15, calendarFromMillis.get(Calendar.MINUTE));
    assertEquals(30, calendarFromMillis.get(Calendar.SECOND));
    assertEquals(300, calendarFromMillis.get(Calendar.MILLISECOND));
  }

  public void testGetCalendarForString() throws Exception {
    assertEquals(DateUtil.getCalendar(2007, Calendar.OCTOBER, 3, 0, 40, 30, 0), DateUtil.getCalendar("10/03/2007 12:40:30 AM"));
  }

  public void testGetCalendarForTime() throws Exception {
    final Calendar date0 = DateUtil.getCalendar(2004, Calendar.MAY, 30);

    assertNotNull(date0);
    assertEquals(2004, date0.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, date0.get(Calendar.MONTH));
    assertEquals(30, date0.get(Calendar.DAY_OF_MONTH));
    TestUtil.assertZero(date0.get(Calendar.HOUR));
    TestUtil.assertZero(date0.get(Calendar.MINUTE));
    TestUtil.assertZero(date0.get(Calendar.SECOND));
    TestUtil.assertZero(date0.get(Calendar.MILLISECOND));
    assertEquals(TimeZone.getDefault(), date0.getTimeZone());

    final Calendar date1 = DateUtil.getCalendar(2001, Calendar.APRIL, 15, 20, 30, 2, 620);

    assertNotNull(date1);
    assertEquals(2001, date1.get(Calendar.YEAR));
    assertEquals(Calendar.APRIL, date1.get(Calendar.MONTH));
    assertEquals(15, date1.get(Calendar.DAY_OF_MONTH));
    assertEquals(8, date1.get(Calendar.HOUR));
    assertEquals(20, date1.get(Calendar.HOUR_OF_DAY));
    assertEquals(30, date1.get(Calendar.MINUTE));
    assertEquals(2, date1.get(Calendar.SECOND));
    assertEquals(620, date1.get(Calendar.MILLISECOND));
    assertEquals(TimeZone.getDefault(), date1.getTimeZone());

    final TimeZone americaChicagoTimeZone = TimeZone.getTimeZone("America/Chicago");
    final Calendar date2 = DateUtil.getCalendar(1974, Calendar.MAY, 27, 15, 45, 30, 0, Locale.getDefault(), americaChicagoTimeZone);

    assertNotNull(date2);
    assertEquals(1974, date2.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, date2.get(Calendar.MONTH));
    assertEquals(27, date2.get(Calendar.DAY_OF_MONTH));
    assertEquals(3, date2.get(Calendar.HOUR));
    assertEquals(15, date2.get(Calendar.HOUR_OF_DAY));
    assertEquals(45, date2.get(Calendar.MINUTE));
    assertEquals(30, date2.get(Calendar.SECOND));
    assertEquals(0, date2.get(Calendar.MILLISECOND));
    assertEquals(Calendar.PM, date2.get(Calendar.AM_PM));
    assertEquals(americaChicagoTimeZone, date2.getTimeZone());
  }

  public void testGetCalendarComparator() {
    final Comparator<Calendar> calendarComparator = DateUtil.getCalendarComparator();

    final Calendar MAY_27_1974 = TestUtil.getCalendar(1974, Calendar.MAY, 27);
    final Calendar MAY_27_1974_1_30_15_554 = TestUtil.getCalendar(1974, Calendar.MAY, 27, 1, 30, 15, 554);
    final Calendar MAY_27_2000_12_30_45 = TestUtil.getCalendar(2000, Calendar.MAY, 27, 12, 30, 45, 0);
    final Calendar MAY_5_2004_6_45_30 = TestUtil.getCalendar(2004, Calendar.MAY, 5, 6, 45, 30, 0);
    final Calendar JANUARY_1_2010_1_10_10 = TestUtil.getCalendar(2010, Calendar.JANUARY, 1, 1, 10, 10, 0);

    TestUtil.assertZero(calendarComparator.compare(MAY_27_1974, MAY_27_1974));
    TestUtil.assertNegative(calendarComparator.compare(MAY_27_1974, MAY_27_1974_1_30_15_554));
    TestUtil.assertPositive(calendarComparator.compare(MAY_27_1974_1_30_15_554, MAY_27_1974));
    TestUtil.assertNegative(calendarComparator.compare(MAY_27_1974_1_30_15_554, MAY_27_2000_12_30_45));
    TestUtil.assertPositive(calendarComparator.compare(MAY_27_2000_12_30_45, MAY_27_1974_1_30_15_554));
    TestUtil.assertPositive(calendarComparator.compare(MAY_5_2004_6_45_30, MAY_27_2000_12_30_45));
    TestUtil.assertNegative(calendarComparator.compare(MAY_5_2004_6_45_30, JANUARY_1_2010_1_10_10));
  }

  public void testGetCalendarMonth() throws Exception {
    assertEquals(Calendar.JANUARY, DateUtil.getCalendarMonth(1));
    assertEquals(Calendar.FEBRUARY, DateUtil.getCalendarMonth(2));
    assertEquals(Calendar.MARCH, DateUtil.getCalendarMonth(3));
    assertEquals(Calendar.APRIL, DateUtil.getCalendarMonth(4));
    assertEquals(Calendar.MAY, DateUtil.getCalendarMonth(5));
    assertEquals(Calendar.JUNE, DateUtil.getCalendarMonth(6));
    assertEquals(Calendar.JULY, DateUtil.getCalendarMonth(7));
    assertEquals(Calendar.AUGUST, DateUtil.getCalendarMonth(8));
    assertEquals(Calendar.SEPTEMBER, DateUtil.getCalendarMonth(9));
    assertEquals(Calendar.OCTOBER, DateUtil.getCalendarMonth(10));
    assertEquals(Calendar.NOVEMBER, DateUtil.getCalendarMonth(11));
    assertEquals(Calendar.DECEMBER, DateUtil.getCalendarMonth(12));
  }

  public void testGetCalendarMonthWithInvalidMonth() throws Exception {
    try {
      DateUtil.getCalendarMonth(0);
      fail("Calling getCalendarMonth with an invalid month of (0) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(0) is not a valid Calendar month!", e.getMessage());
    }

    try {
      DateUtil.getCalendarMonth(13);
      fail("Calling getCalendarMonth with an invalid month of (13) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(13) is not a valid Calendar month!", e.getMessage());
    }
  }

  public void testGetCalendarMonthDescription() throws Exception {
    assertEquals("January", DateUtil.getCalendarMonthDescription(Calendar.JANUARY));
    assertEquals("February", DateUtil.getCalendarMonthDescription(Calendar.FEBRUARY));
    assertEquals("March", DateUtil.getCalendarMonthDescription(Calendar.MARCH));
    assertEquals("April", DateUtil.getCalendarMonthDescription(Calendar.APRIL));
    assertEquals("May", DateUtil.getCalendarMonthDescription(Calendar.MAY));
    assertEquals("June", DateUtil.getCalendarMonthDescription(Calendar.JUNE));
    assertEquals("July", DateUtil.getCalendarMonthDescription(Calendar.JULY));
    assertEquals("August", DateUtil.getCalendarMonthDescription(Calendar.AUGUST));
    assertEquals("September", DateUtil.getCalendarMonthDescription(Calendar.SEPTEMBER));
    assertEquals("October", DateUtil.getCalendarMonthDescription(Calendar.OCTOBER));
    assertEquals("November", DateUtil.getCalendarMonthDescription(Calendar.NOVEMBER));
    assertEquals("December", DateUtil.getCalendarMonthDescription(Calendar.DECEMBER));
  }

  public void testGetCalendarMonthDescriptionWithInvalidMonth() throws Exception {
    try {
      DateUtil.getCalendarMonthDescription(12);
      fail("Calling getCalendarMonthDescription with an invalid Calendar month of (12) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(12) is not a valid Calendar month constant!", e.getMessage());
    }
  }

  public void testGetCalendarNoTimestamp() throws Exception {
    final Calendar today = Calendar.getInstance();
    final Calendar dateNoTimestamp = DateUtil.getCalendarNoTimestamp();

    assertNotNull(dateNoTimestamp);
    assertEquals(today.get(Calendar.YEAR), dateNoTimestamp.get(Calendar.YEAR));
    assertEquals(today.get(Calendar.MONTH), dateNoTimestamp.get(Calendar.MONTH));
    assertEquals(today.get(Calendar.DAY_OF_MONTH), dateNoTimestamp.get(Calendar.DAY_OF_MONTH));
    assertEquals(0, dateNoTimestamp.get(Calendar.HOUR));
    assertEquals(0, dateNoTimestamp.get(Calendar.MINUTE));
    assertEquals(0, dateNoTimestamp.get(Calendar.SECOND));
    assertEquals(0, dateNoTimestamp.get(Calendar.MILLISECOND));
  }

  public void testGetTomorrow() throws Exception {
    final Calendar tomorrow = DateUtil.truncate(Calendar.getInstance());
    tomorrow.add(Calendar.DAY_OF_MONTH, 1);

    assertEquals(tomorrow, DateUtil.truncate(DateUtil.getTomorrow()));
  }

  public void testGetYesterday() throws Exception {
    final Calendar yesterday = DateUtil.truncate(Calendar.getInstance());
    yesterday.add(Calendar.DAY_OF_MONTH, -1);

    assertEquals(yesterday, DateUtil.truncate(DateUtil.getYesterday()));
  }

  public void testGetDiffInDays() throws Exception {
    final Calendar dateTestWasWritten = DateUtil.getCalendar(2009, Calendar.MARCH, 18);

    final Calendar lastDayInFebruary2008 = DateUtil.setToLastDayOfMonth(
      DateUtil.getCalendar(2008, Calendar.FEBRUARY, 1));

    final Calendar lastDayInFebruary2009 = DateUtil.setToLastDayOfMonth(
      DateUtil.getCalendar(2009, Calendar.FEBRUARY, 1));

    assertEquals(12714, DateUtil.getDiffInDays(dateTestWasWritten, DateUtil.getCalendar(1974, Calendar.MAY, 27)));
    assertEquals(-3595, DateUtil.getDiffInDays(DateUtil.getCalendar(1999, Calendar.MAY, 15), dateTestWasWritten));
    assertEquals(3364, DateUtil.getDiffInDays(dateTestWasWritten, DateUtil.getCalendar(2000, Calendar.JANUARY, 1)));
    assertEquals(-205, DateUtil.getDiffInDays(DateUtil.getCalendar(2008, Calendar.AUGUST, 25), dateTestWasWritten));
    assertEquals(76, DateUtil.getDiffInDays(dateTestWasWritten, DateUtil.getCalendar(2009, Calendar.JANUARY, 1)));
    assertEquals(366, DateUtil.getDiffInDays(DateUtil.getCalendar(2008, Calendar.DECEMBER, 31),
      DateUtil.getCalendar(2007, Calendar.DECEMBER, 31)));
    assertEquals(-365, DateUtil.getDiffInDays(DateUtil.getCalendar(2006, Calendar.DECEMBER, 31),
      DateUtil.getCalendar(2007, Calendar.DECEMBER, 31)));
    assertEquals(-29, DateUtil.getDiffInDays(DateUtil.getCalendar(2008, Calendar.JANUARY, 31), lastDayInFebruary2008));
    assertEquals(28, DateUtil.getDiffInDays(lastDayInFebruary2009, DateUtil.getCalendar(2009, Calendar.JANUARY, 31)));
    assertEquals(31, DateUtil.getDiffInDays(DateUtil.getCalendar(2007, Calendar.MAY, 31),
      DateUtil.getCalendar(2007, Calendar.APRIL, 30)));
    assertEquals(-31, DateUtil.getDiffInDays(DateUtil.getCalendar(2008, Calendar.APRIL, 30), 
      DateUtil.getCalendar(2008, Calendar.MAY, 31)));
    assertEquals(-19, DateUtil.getDiffInDays(DateUtil.getCalendar(2009, Calendar.FEBRUARY, 27), dateTestWasWritten));
    assertEquals(20, DateUtil.getDiffInDays(DateUtil.getCalendar(2008, Calendar.MARCH, 18),
      DateUtil.getCalendar(2008, Calendar.FEBRUARY, 27)));
    assertEquals(-3534, DateUtil.getDiffInDays(DateUtil.getCalendar(1975, Calendar.OCTOBER, 31),
      DateUtil.getCalendar(1985, Calendar.JULY, 4)));
  }

  public void testGetDiffInDaysWithInvalidActualDate() throws Exception {
    try {
      DateUtil.getDiffInDays(null, Calendar.getInstance());
      fail("Calling getDiffInDays with a null actual date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The actual date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInDays with a null actual date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetDiffInDaysWithInvalidRelativeDate() throws Exception {
    try {
      DateUtil.getDiffInDays(Calendar.getInstance(), null);
      fail("Calling getDiffInDays with a null relative date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The relative date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInDays with a null relative date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  // TODO refactor test case
  public void testGetDiffInMonths() throws Exception {
    assertEquals(-393, DateUtil.getDiffInMonths(DateUtil.getCalendar(1974, Calendar.MAY, 27),
      DateUtil.getCalendar(2007, Calendar.FEBRUARY, 3)));
    assertEquals(-9, DateUtil.getDiffInMonths(DateUtil.getCalendar(2004, Calendar.MAY, 12),
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 8)));
    assertEquals(-27, DateUtil.getDiffInMonths(DateUtil.getCalendar(2003, Calendar.DECEMBER, 15),
      DateUtil.getCalendar(2006, Calendar.MARCH, 1)));
    assertEquals(-12, DateUtil.getDiffInMonths(DateUtil.getCalendar(2004, Calendar.JUNE, 5),
      DateUtil.getCalendar(2005, Calendar.JUNE, 24)));
    assertEquals(-19, DateUtil.getDiffInMonths(DateUtil.getCalendar(2004, Calendar.JANUARY, 12),
      DateUtil.getCalendar(2005, Calendar.AUGUST, 12)));
  }

  public void testGetDiffInMonthsWithInvalidActualDate() throws Exception {
    try {
      DateUtil.getDiffInMonths(null, Calendar.getInstance());
      fail("Calling getDiffInMonths with a null actual date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The actual date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInMonths with a null actual date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetDiffInMonthsWithInvalidRelativeDate() throws Exception {
    try {
      DateUtil.getDiffInMonths(Calendar.getInstance(), null);
      fail("Calling getDiffInMonths with a null relative date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The relative date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInMonths with a null relative date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  // TODO refactor test case
  public void testGetDiffInYears() throws Exception {
    final Calendar dateTestWasWritten = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 2);

    assertEquals(-32, DateUtil.getDiffInYears(DateUtil.getCalendar(1974, Calendar.MAY, 27), DateUtil.getCalendar(2007, Calendar.FEBRUARY, 3)));
    assertEquals(-14, DateUtil.getDiffInYears(DateUtil.getCalendar(1990, Calendar.JULY, 4), dateTestWasWritten));
    assertEquals(-20, DateUtil.getDiffInYears(DateUtil.getCalendar(1985, Calendar.FEBRUARY, 2), dateTestWasWritten));
    assertEquals(-30, DateUtil.getDiffInYears(DateUtil.getCalendar(1975, Calendar.FEBRUARY, 1), dateTestWasWritten));
    assertEquals(-29, DateUtil.getDiffInYears(DateUtil.getCalendar(1975, Calendar.FEBRUARY, 3), dateTestWasWritten));
  }

  public void testGetDiffInYearsWithInvalidActualDate() throws Exception {
    try {
      DateUtil.getDiffInYears(null, Calendar.getInstance());
      fail("Calling getDiffInYears with a null actual date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The actual date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInYears with a null actual date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetDiffInYearsWithInvalidRelativeDate() throws Exception {
    try {
      DateUtil.getDiffInYears(Calendar.getInstance(), null);
      fail("Calling getDiffInYears with a null relative date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The relative date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling getDiffInYears with a null relative date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetEarlierDate() throws Exception {
    final Calendar relativeDate = DateUtil.getCalendar(2008, Calendar.NOVEMBER, 11);
    Calendar comparableDate = DateUtil.getCalendar(2009, Calendar.MARCH, 9);

    assertNull(DateUtil.getEarlierDate(null, null));
    assertEquals(comparableDate, DateUtil.getEarlierDate(comparableDate, null));
    assertEquals(relativeDate, DateUtil.getEarlierDate(null, relativeDate));
    assertEquals(relativeDate, DateUtil.getEarlierDate(comparableDate, relativeDate));

    comparableDate = DateUtil.getCalendar(1988, Calendar.JULY,  4);

    assertEquals(comparableDate, DateUtil.getEarlierDate(comparableDate, relativeDate));
  }

  public void testGetEarliestDate() throws Exception {
    final Calendar may27of1974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);
    final Calendar may15of1999 = DateUtil.getCalendar(1999, Calendar.MAY, 15);
    final Calendar august25of2008 = DateUtil.getCalendar(2008, Calendar.AUGUST, 25);

    assertNull(DateUtil.getEarliestDate(null, null, null));
    assertEquals(may15of1999, DateUtil.getEarliestDate(null, null, may15of1999));
    assertEquals(may27of1974, DateUtil.getEarliestDate(null, may27of1974, null));
    assertEquals(august25of2008, DateUtil.getEarliestDate(august25of2008, null, null));
    assertEquals(may27of1974, DateUtil.getEarliestDate(may15of1999, null, may27of1974));
    assertEquals(may15of1999, DateUtil.getEarliestDate(null, august25of2008, may15of1999));
    assertEquals(may27of1974, DateUtil.getEarliestDate(may27of1974, null, august25of2008));
    assertEquals(may27of1974, DateUtil.getEarliestDate(august25of2008, may27of1974, may15of1999));
  }

  public void testGetLaterDate() throws Exception {
    final Calendar relativeDate = DateUtil.getCalendar(2008, Calendar.NOVEMBER, 11);
    Calendar comparableDate = DateUtil.getCalendar(2009, Calendar.MARCH, 9);

    assertNull(DateUtil.getLaterDate(null, null));
    assertEquals(comparableDate, DateUtil.getLaterDate(comparableDate, null));
    assertEquals(relativeDate, DateUtil.getLaterDate(null, relativeDate));
    assertEquals(comparableDate, DateUtil.getLaterDate(comparableDate, relativeDate));

    comparableDate = DateUtil.getCalendar(1988, Calendar.JULY,  4);

    assertEquals(relativeDate, DateUtil.getLaterDate(comparableDate, relativeDate));
  }

  public void testGetLatestDate() throws Exception {
    final Calendar may27of1974 = DateUtil.getCalendar(1974, Calendar.MAY, 27);
    final Calendar may15of1999 = DateUtil.getCalendar(1999, Calendar.MAY, 15);
    final Calendar august25of2008 = DateUtil.getCalendar(2008, Calendar.AUGUST, 25);

    assertNull(DateUtil.getLatestDate(null, null, null));
    assertEquals(may15of1999, DateUtil.getLatestDate(null, null, may15of1999));
    assertEquals(may27of1974, DateUtil.getLatestDate(null, may27of1974, null));
    assertEquals(august25of2008, DateUtil.getLatestDate(august25of2008, null, null));
    assertEquals(may15of1999, DateUtil.getLatestDate(may15of1999, null, may27of1974));
    assertEquals(august25of2008, DateUtil.getLatestDate(null, august25of2008, may15of1999));
    assertEquals(august25of2008, DateUtil.getLatestDate(may27of1974, null, august25of2008));
    assertEquals(august25of2008, DateUtil.getLatestDate(august25of2008, may27of1974, may15of1999));
  }

  public void testGetNextMonth() throws Exception {
    assertEquals(Calendar.FEBRUARY, DateUtil.getNextMonth(Calendar.JANUARY));
    assertEquals(Calendar.MARCH, DateUtil.getNextMonth(Calendar.FEBRUARY));
    assertEquals(Calendar.APRIL, DateUtil.getNextMonth(Calendar.MARCH));
    assertEquals(Calendar.MAY, DateUtil.getNextMonth(Calendar.APRIL));
    assertEquals(Calendar.JUNE, DateUtil.getNextMonth(Calendar.MAY));
    assertEquals(Calendar.JULY, DateUtil.getNextMonth(Calendar.JUNE));
    assertEquals(Calendar.AUGUST, DateUtil.getNextMonth(Calendar.JULY));
    assertEquals(Calendar.SEPTEMBER, DateUtil.getNextMonth(Calendar.AUGUST));
    assertEquals(Calendar.OCTOBER, DateUtil.getNextMonth(Calendar.SEPTEMBER));
    assertEquals(Calendar.NOVEMBER, DateUtil.getNextMonth(Calendar.OCTOBER));
    assertEquals(Calendar.DECEMBER, DateUtil.getNextMonth(Calendar.NOVEMBER));
    assertEquals(Calendar.JANUARY, DateUtil.getNextMonth(Calendar.DECEMBER));
  }

  public void testGetNextMonthWithInvalidMonth() throws Exception {
    try {
      DateUtil.getNextMonth(12);
      fail("Calling getNextMonth with an invalid month of (12) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(12) is not a valid Calendar month constant!", e.getMessage());
    }
  }

  public void testGetNormalizedMonth() throws Exception {
    assertEquals(1, DateUtil.getNormalizedMonth(Calendar.JANUARY));
    assertEquals(2, DateUtil.getNormalizedMonth(Calendar.FEBRUARY));
    assertEquals(3, DateUtil.getNormalizedMonth(Calendar.MARCH));
    assertEquals(4, DateUtil.getNormalizedMonth(Calendar.APRIL));
    assertEquals(5, DateUtil.getNormalizedMonth(Calendar.MAY));
    assertEquals(6, DateUtil.getNormalizedMonth(Calendar.JUNE));
    assertEquals(7, DateUtil.getNormalizedMonth(Calendar.JULY));
    assertEquals(8, DateUtil.getNormalizedMonth(Calendar.AUGUST));
    assertEquals(9, DateUtil.getNormalizedMonth(Calendar.SEPTEMBER));
    assertEquals(10, DateUtil.getNormalizedMonth(Calendar.OCTOBER));
    assertEquals(11, DateUtil.getNormalizedMonth(Calendar.NOVEMBER));
    assertEquals(12, DateUtil.getNormalizedMonth(Calendar.DECEMBER));
  }

  public void testGetNormalizedMonthWithInvalidMonth() throws Exception {
    try {
      DateUtil.getNormalizedMonth(12);
      fail("Calling getNormalizedMonth with an invalid month of (12) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(12) is not a valid Calendar month constant!", e.getMessage());
    }
  }

  public void testGetNormalizedMonthDescription() throws Exception {
    assertEquals("January", DateUtil.getNormalizedMonthDescription(1));
    assertEquals("February", DateUtil.getNormalizedMonthDescription(2));
    assertEquals("March", DateUtil.getNormalizedMonthDescription(3));
    assertEquals("April", DateUtil.getNormalizedMonthDescription(4));
    assertEquals("May", DateUtil.getNormalizedMonthDescription(5));
    assertEquals("June", DateUtil.getNormalizedMonthDescription(6));
    assertEquals("July", DateUtil.getNormalizedMonthDescription(7));
    assertEquals("August", DateUtil.getNormalizedMonthDescription(8));
    assertEquals("September", DateUtil.getNormalizedMonthDescription(9));
    assertEquals("October", DateUtil.getNormalizedMonthDescription(10));
    assertEquals("November", DateUtil.getNormalizedMonthDescription(11));
    assertEquals("December", DateUtil.getNormalizedMonthDescription(12));
  }

  public void testGetNormalizedMonthDescriptionWithInvalidMonth() throws Exception {
    try {
      DateUtil.getNormalizedMonthDescription(0);
      fail("Calling getNormalizedMonthDescription with an invalid Calendar month of (0) should have thrown an IllegalArgugmentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(0) is not a valid Calendar month!", e.getMessage());
    }
  }

  public void testGetNumberOfDaysInMonthOf2008() throws Exception {
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.JANUARY, 2008));
    assertEquals(29, DateUtil.getNumberOfDaysInMonth(Calendar.FEBRUARY, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.MARCH, 2008));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.APRIL, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.MAY, 2008));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.JUNE, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.JULY, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.AUGUST, 2008));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.SEPTEMBER, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.OCTOBER, 2008));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.NOVEMBER, 2008));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.DECEMBER, 2008));
  }

  public void testGetNumberOfDaysInMonthOf2009() throws Exception {
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.JANUARY, 2009));
    assertEquals(28, DateUtil.getNumberOfDaysInMonth(Calendar.FEBRUARY, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.MARCH, 2009));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.APRIL, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.MAY, 2009));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.JUNE, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.JULY, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.AUGUST, 2009));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.SEPTEMBER, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.OCTOBER, 2009));
    assertEquals(30, DateUtil.getNumberOfDaysInMonth(Calendar.NOVEMBER, 2009));
    assertEquals(31, DateUtil.getNumberOfDaysInMonth(Calendar.DECEMBER, 2009));
  }

  public void testGetNumberOfDaysInMonthWithInvalidMonth() throws Exception {
    try {
      DateUtil.getNumberOfDaysInMonth(-1);
      fail("Calling getNumberOfDaysInMonth with an invalid month (-1) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e){
      assertEquals("(-1) is not a valid Calendar month constant!", e.getMessage());
    }

    try {
      DateUtil.getNumberOfDaysInMonth(12);
      fail("Calling getNumberOfDaysInMonth with an invalid month (12) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(12) is not a valid Calendar month constant!", e.getMessage());
    }
  }

  public void testGetNumberOfDaysInMonthWithInvalidYear() throws Exception {
    try {
      DateUtil.getNumberOfDaysInMonth(Calendar.MAY, -2009);
      fail("Calling getNumberOfDaysInMonth with an invalid year (-2009) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(-2009) is not a valid Calendar year!", e.getMessage());
    }
  }

  public void testGetPreviousMonth() throws Exception {
    assertEquals(Calendar.DECEMBER, DateUtil.getPreviousMonth(Calendar.JANUARY));
    assertEquals(Calendar.JANUARY, DateUtil.getPreviousMonth(Calendar.FEBRUARY));
    assertEquals(Calendar.FEBRUARY, DateUtil.getPreviousMonth(Calendar.MARCH));
    assertEquals(Calendar.MARCH, DateUtil.getPreviousMonth(Calendar.APRIL));
    assertEquals(Calendar.APRIL, DateUtil.getPreviousMonth(Calendar.MAY));
    assertEquals(Calendar.MAY, DateUtil.getPreviousMonth(Calendar.JUNE));
    assertEquals(Calendar.JUNE, DateUtil.getPreviousMonth(Calendar.JULY));
    assertEquals(Calendar.JULY, DateUtil.getPreviousMonth(Calendar.AUGUST));
    assertEquals(Calendar.AUGUST, DateUtil.getPreviousMonth(Calendar.SEPTEMBER));
    assertEquals(Calendar.SEPTEMBER, DateUtil.getPreviousMonth(Calendar.OCTOBER));
    assertEquals(Calendar.OCTOBER, DateUtil.getPreviousMonth(Calendar.NOVEMBER));
    assertEquals(Calendar.NOVEMBER, DateUtil.getPreviousMonth(Calendar.DECEMBER));
  }

  public void testGetPreviousMonthWithInvalidMonth() throws Exception {
    try {
      DateUtil.getPreviousMonth(-1);
      fail("Calling getPreviousMonth with an invalid month of (-1) should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("(-1) is not a valid Calendar month constant!", e.getMessage());
    }
  }

  public void testIsAfter() throws Exception {
    final Calendar theDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Calendar expectedAfterDate = DateUtil.getCalendar(2010, Calendar.FEBRUARY, 2);
    final Calendar expectedBeforeDate = DateUtil.getCalendar(2007, Calendar.DECEMBER, 12);

    assertFalse(DateUtil.isAfter(null, null));
    assertFalse(DateUtil.isAfter(theDate, null));
    assertFalse(DateUtil.isAfter(null, expectedAfterDate));
    assertFalse(DateUtil.isAfter(theDate, expectedAfterDate));
    assertFalse(DateUtil.isAfter(theDate, theDate));
    assertTrue(DateUtil.isAfter(theDate, expectedBeforeDate));
  }

  public void testIsOnOrAfter() throws Exception {
    final Calendar theDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Calendar expectedAfterDate = DateUtil.getCalendar(2010, Calendar.FEBRUARY, 2);
    final Calendar expectedBeforeDate = DateUtil.getCalendar(2007, Calendar.DECEMBER, 12);

    assertFalse(DateUtil.isOnOrAfter(null, null));
    assertFalse(DateUtil.isOnOrAfter(theDate, null));
    assertFalse(DateUtil.isOnOrAfter(null, expectedAfterDate));
    assertFalse(DateUtil.isOnOrAfter(theDate, expectedAfterDate));
    assertTrue(DateUtil.isOnOrAfter(theDate, theDate));
    assertTrue(DateUtil.isOnOrAfter(theDate, expectedBeforeDate));
  }

  public void testIsBefore() throws Exception {
    final Calendar theDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Calendar expectedAfterDate = DateUtil.getCalendar(2010, Calendar.FEBRUARY, 2);
    final Calendar expectedBeforeDate = DateUtil.getCalendar(2007, Calendar.DECEMBER, 12);

    assertFalse(DateUtil.isBefore(null, null));
    assertFalse(DateUtil.isBefore(theDate, null));
    assertFalse(DateUtil.isBefore(null, expectedAfterDate));
    assertFalse(DateUtil.isBefore(theDate, expectedBeforeDate));
    assertFalse(DateUtil.isBefore(theDate, theDate));
    assertTrue(DateUtil.isBefore(theDate, expectedAfterDate));
  }

  public void testIsOnOrBefore() throws Exception {
    final Calendar theDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Calendar expectedAfterDate = DateUtil.getCalendar(2010, Calendar.FEBRUARY, 2);
    final Calendar expectedBeforeDate = DateUtil.getCalendar(2007, Calendar.DECEMBER, 12);

    assertFalse(DateUtil.isOnOrBefore(null, null));
    assertFalse(DateUtil.isOnOrBefore(theDate, null));
    assertFalse(DateUtil.isOnOrBefore(null, expectedAfterDate));
    assertFalse(DateUtil.isOnOrBefore(theDate, expectedBeforeDate));
    assertTrue(DateUtil.isOnOrBefore(theDate, theDate));
    assertTrue(DateUtil.isOnOrBefore(theDate, expectedAfterDate));
  }

  public void testIsBetween() throws Exception {
    final Calendar beginDate = DateUtil.getCalendar(2008, Calendar.JULY, 1);
    final Calendar endDate = DateUtil.getCalendar(2009, Calendar.JUNE, 30);

    assertFalse(DateUtil.isBetween(null, null, null));
    assertFalse(DateUtil.isBetween(null, null, endDate));
    assertFalse(DateUtil.isBetween(null, beginDate, null));
    assertFalse(DateUtil.isBetween(null, beginDate, endDate));
    assertFalse(DateUtil.isBetween(Calendar.getInstance(), null, null));
    assertFalse(DateUtil.isBetween(Calendar.getInstance(), null, endDate));
    assertFalse(DateUtil.isBetween(Calendar.getInstance(), beginDate, null));
    assertFalse(DateUtil.isBetween(DateUtil.getCalendar(2007, Calendar.DECEMBER, 12), beginDate, endDate));
    assertTrue(DateUtil.isBetween(DateUtil.getCalendar(2009, Calendar.JUNE, 11), beginDate, endDate));
    assertFalse(DateUtil.isBetween(DateUtil.getCalendar(2010, Calendar.JANUARY, 1), beginDate, endDate));
  }

  public void testIsOn() throws Exception {
    final Calendar givenDate = DateUtil.getCalendar(2009, Calendar.JUNE, 11, 11, 5, 30, 500);

    assertFalse(DateUtil.isOn(null, null));
    assertFalse(DateUtil.isOn(null, givenDate));
    assertFalse(DateUtil.isOn(Calendar.getInstance(), null));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2002, Calendar.JULY, 4, 12, 0, 0, 500), givenDate));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2012, Calendar.DECEMBER, 12, 15, 30, 45, 500), givenDate));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2009, Calendar.SEPTEMBER, 11, 11, 5, 30, 500), givenDate));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2009, Calendar.JUNE, 11, 0, 0, 0, 0), givenDate));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2009, Calendar.JUNE, 11, 23, 5, 30, 500), givenDate));
    //assertFalse(DateUtil.isOn(DateUtil.getCalendar(11, 5, 30, 500), givenDate));
    assertFalse(DateUtil.isOn(DateUtil.getCalendar(2009, Calendar.JUNE, 11, 11, 5, 30, 500,
      Locale.getDefault(), TimeZone.getTimeZone("America/New_York")), givenDate));
    assertTrue(DateUtil.isOn(DateUtil.getCalendar(2009, Calendar.JUNE, 11, 11, 5, 30, 500), givenDate));
  }

  public void testIsOutside() throws Exception {
    final Calendar minDate = DateUtil.getCalendar(2008, Calendar.JULY, 1);
    final Calendar maxDate = DateUtil.getCalendar(2009, Calendar.JUNE, 30);

    assertFalse(DateUtil.isOutside(null, null, null));
    assertFalse(DateUtil.isOutside(null, null, maxDate));
    assertFalse(DateUtil.isOutside(null, minDate, null));
    assertFalse(DateUtil.isOutside(null, minDate, maxDate));
    assertFalse(DateUtil.isOutside(Calendar.getInstance(), null, null));
    assertFalse(DateUtil.isOutside(Calendar.getInstance(), null, maxDate));
    assertFalse(DateUtil.isOutside(Calendar.getInstance(), minDate, null));
    assertTrue(DateUtil.isOutside(DateUtil.getCalendar(2007, Calendar.DECEMBER, 12), minDate, maxDate));
    assertFalse(DateUtil.isOutside(DateUtil.getCalendar(2009, Calendar.JUNE, 11), minDate, maxDate));
    assertTrue(DateUtil.isOutside(DateUtil.getCalendar(2010, Calendar.JANUARY, 1), minDate, maxDate));
  }

  public void testIsDaylightSavingTimeBegin() throws Exception {
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2003, Calendar.APRIL, 6)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2002, Calendar.APRIL, 6)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2004, Calendar.APRIL, 6)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2002, Calendar.APRIL, 7)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2003, Calendar.APRIL, 7)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2004, Calendar.APRIL, 7)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(1990, Calendar.APRIL, 1)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(1980, Calendar.APRIL, 6)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(1970, Calendar.APRIL, 5)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2000, Calendar.APRIL, 2)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2006, Calendar.APRIL, 2)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2007, Calendar.APRIL, 1)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2007, Calendar.MARCH, 11)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2008, Calendar.APRIL, 6)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2008, Calendar.MARCH, 9)));
    assertFalse(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2009, Calendar.APRIL, 5)));
    assertTrue(DateUtil.isDaylightSavingTimeBegin(DateUtil.getCalendar(2009, Calendar.MARCH, 8)));
  }

  public void testIsDaylightSavingTimeEnd() throws Exception {
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2003, Calendar.OCTOBER, 26)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2002, Calendar.OCTOBER, 26)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2004, Calendar.OCTOBER, 26)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2000, Calendar.OCTOBER, 29)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1999, Calendar.OCTOBER, 29)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2001, Calendar.OCTOBER, 29)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1987, Calendar.OCTOBER, 25)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1993, Calendar.OCTOBER, 31)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1983, Calendar.OCTOBER, 30)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1988, Calendar.OCTOBER, 30)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(1994, Calendar.OCTOBER, 30)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2005, Calendar.OCTOBER, 30)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2005, Calendar.JANUARY, 30)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2005, Calendar.APRIL, 3)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2007, Calendar.OCTOBER, 28)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2007, Calendar.NOVEMBER, 4)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2008, Calendar.OCTOBER, 26)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2008, Calendar.NOVEMBER, 2)));
    assertFalse(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2009, Calendar.OCTOBER, 25)));
    assertTrue(DateUtil.isDaylightSavingTimeEnd(DateUtil.getCalendar(2009, Calendar.NOVEMBER, 1)));
  }

  public void testIsFutureDate() throws Exception {
    final Calendar futureDate = Calendar.getInstance();
    futureDate.add(Calendar.YEAR, 2);

    assertFalse(DateUtil.isFutureDate(null));
    assertFalse(DateUtil.isFutureDate(DateUtil.getCalendar(2006, Calendar.DECEMBER, 12)));
    assertFalse(DateUtil.isFutureDate(Calendar.getInstance()));
    assertTrue(DateUtil.isFutureDate(futureDate));
  }

  public void testIsLeapYear() throws Exception {
    assertFalse(DateUtil.isLeapYear(2009));
    assertTrue(DateUtil.isLeapYear(2008));
    assertFalse(DateUtil.isLeapYear(2007));
    assertFalse(DateUtil.isLeapYear(2006));
    assertFalse(DateUtil.isLeapYear(2005));
    assertTrue(DateUtil.isLeapYear(2004));
    assertFalse(DateUtil.isLeapYear(2003));
    assertFalse(DateUtil.isLeapYear(2002));
    assertFalse(DateUtil.isLeapYear(2001));
    assertTrue(DateUtil.isLeapYear(2000));
    assertFalse(DateUtil.isLeapYear(1999));
    assertFalse(DateUtil.isLeapYear(1998));
    assertFalse(DateUtil.isLeapYear(1997));
    assertTrue(DateUtil.isLeapYear(1996));
    assertFalse(DateUtil.isLeapYear(1995));
    assertFalse(DateUtil.isLeapYear(1994));
    assertFalse(DateUtil.isLeapYear(1993));
    assertTrue(DateUtil.isLeapYear(1992));
    assertFalse(DateUtil.isLeapYear(1991));
    assertFalse(DateUtil.isLeapYear(1990));
    assertFalse(DateUtil.isLeapYear(1989));
    assertTrue(DateUtil.isLeapYear(1988));
    assertFalse(DateUtil.isLeapYear(1987));
    assertFalse(DateUtil.isLeapYear(1986));
    assertFalse(DateUtil.isLeapYear(1985));
    assertTrue(DateUtil.isLeapYear(1984));
    assertFalse(DateUtil.isLeapYear(1983));
    assertFalse(DateUtil.isLeapYear(1982));
    assertFalse(DateUtil.isLeapYear(1981));
    assertTrue(DateUtil.isLeapYear(1980));
    assertFalse(DateUtil.isLeapYear(1979));
    assertFalse(DateUtil.isLeapYear(1978));
    assertFalse(DateUtil.isLeapYear(1977));
    assertTrue(DateUtil.isLeapYear(1976));
    assertFalse(DateUtil.isLeapYear(1975));
    assertFalse(DateUtil.isLeapYear(1974));
    assertFalse(DateUtil.isLeapYear(1973));
    assertTrue(DateUtil.isLeapYear(1972));
    assertFalse(DateUtil.isLeapYear(1971));
    assertFalse(DateUtil.isLeapYear(1970));
  }

  public void testIsPastDate() throws Exception {
    final Calendar futureDate = Calendar.getInstance();
    futureDate.add(Calendar.YEAR, 2);

    assertFalse(DateUtil.isPastDate(null));
    assertFalse(DateUtil.isPastDate(futureDate));
    assertFalse(DateUtil.isPastDate(Calendar.getInstance()));
    assertTrue(DateUtil.isPastDate(DateUtil.getCalendar(2006, Calendar.MARCH, 8)));
  }

  public void testIsPresentDate() throws Exception {
    final Calendar futureDate = Calendar.getInstance();
    futureDate.add(Calendar.MONTH, 3);

    assertFalse(DateUtil.isPresentDate(null));
    assertFalse(DateUtil.isPresentDate(DateUtil.getCalendar(2000, Calendar.JULY,  1)));
    assertTrue(DateUtil.isPresentDate(Calendar.getInstance()));
    assertFalse(DateUtil.isPresentDate(futureDate));
  }

  public void testIsJanuary() throws Exception {
    assertFalse(DateUtil.isJanuary(Calendar.DECEMBER + 1));
    assertFalse(DateUtil.isJanuary(Calendar.FEBRUARY));
    assertFalse(DateUtil.isJanuary(1));
    assertTrue(DateUtil.isJanuary(Calendar.JANUARY));
    assertTrue(DateUtil.isJanuary(0));
  }

  public void testIsFebruary() throws Exception {
    assertFalse(DateUtil.isFebruary(Calendar.JANUARY));
    assertFalse(DateUtil.isFebruary(Calendar.MARCH));
    assertFalse(DateUtil.isFebruary(2));
    assertTrue(DateUtil.isFebruary(Calendar.FEBRUARY));
    assertTrue(DateUtil.isFebruary(1));
  }

  public void testIsMarch() throws Exception {
    assertFalse(DateUtil.isMarch(Calendar.FEBRUARY));
    assertFalse(DateUtil.isMarch(Calendar.APRIL));
    assertFalse(DateUtil.isMarch(3));
    assertTrue(DateUtil.isMarch(Calendar.MARCH));
    assertTrue(DateUtil.isMarch(2));
  }

  public void testIsApril() throws Exception {
    assertFalse(DateUtil.isApril(Calendar.MARCH));
    assertFalse(DateUtil.isApril(Calendar.MAY));
    assertFalse(DateUtil.isApril(4));
    assertTrue(DateUtil.isApril(Calendar.APRIL));
    assertTrue(DateUtil.isApril(3));
  }

  public void testIsMay() throws Exception {
    assertFalse(DateUtil.isMay(Calendar.APRIL));
    assertFalse(DateUtil.isMay(Calendar.JUNE));
    assertFalse(DateUtil.isMay(5));
    assertTrue(DateUtil.isMay(Calendar.MAY));
    assertTrue(DateUtil.isMay(4));
  }

  public void testIsJune() throws Exception {
    assertFalse(DateUtil.isJune(Calendar.MAY));
    assertFalse(DateUtil.isJune(Calendar.JULY));
    assertFalse(DateUtil.isJune(6));
    assertTrue(DateUtil.isJune(Calendar.JUNE));
    assertTrue(DateUtil.isJune(5));
  }

  public void testIsJuly() throws Exception {
    assertFalse(DateUtil.isJuly(Calendar.JUNE));
    assertFalse(DateUtil.isJuly(Calendar.AUGUST));
    assertFalse(DateUtil.isJuly(7));
    assertTrue(DateUtil.isJuly(Calendar.JULY));
    assertTrue(DateUtil.isJuly(6));
  }

  public void testIsAugust() throws Exception {
    assertFalse(DateUtil.isAugust(Calendar.JULY));
    assertFalse(DateUtil.isAugust(Calendar.SEPTEMBER));
    assertFalse(DateUtil.isAugust(8));
    assertTrue(DateUtil.isAugust(Calendar.AUGUST));
    assertTrue(DateUtil.isAugust(7));
  }

  public void testIsSeptember() throws Exception {
    assertFalse(DateUtil.isSeptember(Calendar.AUGUST));
    assertFalse(DateUtil.isSeptember(Calendar.OCTOBER));
    assertFalse(DateUtil.isSeptember(9));
    assertTrue(DateUtil.isSeptember(Calendar.SEPTEMBER));
    assertTrue(DateUtil.isSeptember(8));
  }

  public void testIsOctober() throws Exception {
    assertFalse(DateUtil.isOctober(Calendar.SEPTEMBER));
    assertFalse(DateUtil.isOctober(Calendar.NOVEMBER));
    assertFalse(DateUtil.isOctober(10));
    assertTrue(DateUtil.isOctober(Calendar.OCTOBER));
    assertTrue(DateUtil.isOctober(9));
  }

  public void testIsNovember() throws Exception {
    assertFalse(DateUtil.isNovember(Calendar.OCTOBER));
    assertFalse(DateUtil.isNovember(Calendar.DECEMBER));
    assertFalse(DateUtil.isNovember(11));
    assertTrue(DateUtil.isNovember(Calendar.NOVEMBER));
    assertTrue(DateUtil.isNovember(10));
  }

  public void testIsDecember() throws Exception {
    assertFalse(DateUtil.isDecember(Calendar.NOVEMBER));
    assertFalse(DateUtil.isDecember(Calendar.JANUARY - 1));
    assertFalse(DateUtil.isDecember(12));
    assertTrue(DateUtil.isDecember(Calendar.DECEMBER));
    assertTrue(DateUtil.isDecember(11));
  }

  public void testIsSunday() throws Exception {
    assertFalse(DateUtil.isSunday(Calendar.SATURDAY + 1));
    assertFalse(DateUtil.isSunday(Calendar.MONDAY));
    assertTrue(DateUtil.isSunday(Calendar.SUNDAY));
    assertTrue(DateUtil.isSunday(1));
  }

  public void testIsMonday() throws Exception {
    assertFalse(DateUtil.isMonday(Calendar.SUNDAY));
    assertFalse(DateUtil.isMonday(Calendar.TUESDAY));
    assertTrue(DateUtil.isMonday(Calendar.MONDAY));
    assertTrue(DateUtil.isMonday(2));
  }

  public void testIsTuesday() throws Exception {
    assertFalse(DateUtil.isTuesday(Calendar.MONDAY));
    assertFalse(DateUtil.isTuesday(Calendar.WEDNESDAY));
    assertTrue(DateUtil.isTuesday(Calendar.TUESDAY));
    assertTrue(DateUtil.isTuesday(3));
  }

  public void testIsWednesday() throws Exception {
    assertFalse(DateUtil.isWednesday(Calendar.TUESDAY));
    assertFalse(DateUtil.isWednesday(Calendar.THURSDAY));
    assertTrue(DateUtil.isWednesday(Calendar.WEDNESDAY));
    assertTrue(DateUtil.isWednesday(4));
  }

  public void testIsThursday() throws Exception {
    assertFalse(DateUtil.isThursday(Calendar.WEDNESDAY));
    assertFalse(DateUtil.isThursday(Calendar.FRIDAY));
    assertTrue(DateUtil.isThursday(Calendar.THURSDAY));
    assertTrue(DateUtil.isThursday(5));
  }

  public void testIsFriday() throws Exception {
    assertFalse(DateUtil.isFriday(Calendar.THURSDAY));
    assertFalse(DateUtil.isFriday(Calendar.SATURDAY));
    assertTrue(DateUtil.isFriday(Calendar.FRIDAY));
    assertTrue(DateUtil.isFriday(6));
  }

  public void testIsSaturday() throws Exception {
    assertFalse(DateUtil.isSaturday(Calendar.FRIDAY));
    assertFalse(DateUtil.isSaturday(Calendar.SUNDAY - 1));
    assertTrue(DateUtil.isSaturday(Calendar.SATURDAY));
    assertTrue(DateUtil.isSaturday(7));
  }

  public void testIsWeekday() throws Exception {
    assertFalse(DateUtil.isWeekday(Calendar.SUNDAY));
    assertTrue(DateUtil.isWeekday(Calendar.MONDAY));
    assertTrue(DateUtil.isWeekday(Calendar.TUESDAY));
    assertTrue(DateUtil.isWeekday(Calendar.WEDNESDAY));
    assertTrue(DateUtil.isWeekday(Calendar.THURSDAY));
    assertTrue(DateUtil.isWeekday(Calendar.FRIDAY));
    assertFalse(DateUtil.isWeekday(Calendar.SATURDAY));
  }

  public void testIsWeekend() throws Exception {
    assertTrue(DateUtil.isWeekend(Calendar.SUNDAY));
    assertFalse(DateUtil.isWeekend(Calendar.MONDAY));
    assertFalse(DateUtil.isWeekend(Calendar.TUESDAY));
    assertFalse(DateUtil.isWeekend(Calendar.WEDNESDAY));
    assertFalse(DateUtil.isWeekend(Calendar.THURSDAY));
    assertFalse(DateUtil.isWeekend(Calendar.FRIDAY));
    assertTrue(DateUtil.isWeekend(Calendar.SATURDAY));
  }

  // TODO add more assertions with time, time zone and locale properties of the Calendar class.
  public void testCopy() throws Exception {
    final Calendar date = DateUtil.getCalendar(1974, Calendar.MAY, 27);

    assertNotNull(date);
    assertEquals(1974, date.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, date.get(Calendar.MONTH));
    assertEquals(27, date.get(Calendar.DAY_OF_MONTH));

    date.set(Calendar.YEAR, 2000);
    date.set(Calendar.DAY_OF_MONTH, 5); // Cinco de Mayo of 2000

    assertNotNull(date);
    assertEquals(2000, date.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, date.get(Calendar.MONTH));
    assertEquals(5, date.get(Calendar.DAY_OF_MONTH));

    final Calendar dateCopy = DateUtil.copy(date);

    assertNotNull(dateCopy);
    assertEquals(2000, dateCopy.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, dateCopy.get(Calendar.MONTH));
    assertEquals(5, dateCopy.get(Calendar.DAY_OF_MONTH));
    assertNotNull(date);
    assertEquals(2000, date.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, date.get(Calendar.MONTH));
    assertEquals(5, date.get(Calendar.DAY_OF_MONTH));

    date.set(Calendar.YEAR, 1776);
    date.set(Calendar.MONTH, Calendar.JULY); // Independence Day
    date.set(Calendar.DAY_OF_MONTH, 4);

    assertEquals(1776, date.get(Calendar.YEAR));
    assertEquals(Calendar.JULY, date.get(Calendar.MONTH));
    assertEquals(4, date.get(Calendar.DAY_OF_MONTH));
    assertEquals(2000, dateCopy.get(Calendar.YEAR));
    assertEquals(Calendar.MAY, dateCopy.get(Calendar.MONTH));
    assertEquals(5, dateCopy.get(Calendar.DAY_OF_MONTH));

    dateCopy.set(Calendar.YEAR, 1492);
    dateCopy.set(Calendar.MONTH, Calendar.AUGUST); // In 1492, Christopher Columbus sailed the ocean blue!
    dateCopy.set(Calendar.DAY_OF_MONTH, 3);

    assertEquals(1492, dateCopy.get(Calendar.YEAR));
    assertEquals(Calendar.AUGUST, dateCopy.get(Calendar.MONTH));
    assertEquals(3, dateCopy.get(Calendar.DAY_OF_MONTH));
    assertEquals(1776, date.get(Calendar.YEAR));
    assertEquals(Calendar.JULY, date.get(Calendar.MONTH));
    assertEquals(4, date.get(Calendar.DAY_OF_MONTH));

    assertNull(DateUtil.copy(null));
  }

  // TODO add more assertions with time, time zone and locale properties of the Calendar class.
  public void testCopyCalendarFields() throws Exception {
    final Calendar expectedDate = Calendar.getInstance();
    Calendar actualDate = DateUtil.copy(expectedDate, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);

    assertEquals(expectedDate.get(Calendar.YEAR), actualDate.get(Calendar.YEAR));
    assertEquals(expectedDate.get(Calendar.MONTH), actualDate.get(Calendar.MONTH));
    assertEquals(expectedDate.get(Calendar.DAY_OF_MONTH), actualDate.get(Calendar.DAY_OF_MONTH));
    assertEquals(0, actualDate.get(Calendar.HOUR_OF_DAY));
    assertEquals(0, actualDate.get(Calendar.MINUTE));
    assertEquals(0, actualDate.get(Calendar.SECOND));

    actualDate = DateUtil.copy(expectedDate, Calendar.YEAR, Calendar.MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE);

    assertEquals(expectedDate.get(Calendar.YEAR), actualDate.get(Calendar.YEAR));
    assertEquals(expectedDate.get(Calendar.MONTH), actualDate.get(Calendar.MONTH));
    assertEquals(1, actualDate.get(Calendar.DAY_OF_MONTH));
    assertEquals(expectedDate.get(Calendar.HOUR_OF_DAY), actualDate.get(Calendar.HOUR_OF_DAY));
    assertEquals(expectedDate.get(Calendar.MINUTE), actualDate.get(Calendar.MINUTE));
    assertEquals(0, actualDate.get(Calendar.SECOND));
    assertNull(DateUtil.copy(null, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH));
  }

  public void testSetToFirstDayOfMonth() throws Exception {
    final Calendar now = DateUtil.getCalendar(2009, Calendar.MARCH, 18, 12, 57, 30, 500);

    assertEquals(18, now.get(Calendar.DAY_OF_MONTH));

    DateUtil.setToFirstDayOfMonth(now);

    assertEquals(1, now.get(Calendar.DAY_OF_MONTH));

    final Calendar firstOfMarch2009 = DateUtil.getCalendar(2009, Calendar.MARCH, 1, 12, 58, 30, 500);

    assertEquals(1, firstOfMarch2009.get(Calendar.DAY_OF_MONTH));

    DateUtil.setToFirstDayOfMonth(firstOfMarch2009);

    assertEquals(1, firstOfMarch2009.get(Calendar.DAY_OF_MONTH));
  }

  public void testSetToFirstDayOfMonthWithInvalidDate() throws Exception {
    try {
      DateUtil.setToFirstDayOfMonth(null);
      fail("Calling setToFirstDayOfMonth with a null date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setToFirstDayOfMonth with a null date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testSetToLastDayOfMonth() throws Exception {
    final Calendar firstOfFebruary2008 = DateUtil.getCalendar(2008, Calendar.FEBRUARY, 1);
    final Calendar firstOfFebruary2009 = DateUtil.getCalendar(2009, Calendar.FEBRUARY, 1);
    final Calendar firstOfMay2008 = DateUtil.getCalendar(2008, Calendar.MAY, 1);
    final Calendar firstOfMay2009 = DateUtil.getCalendar(2009, Calendar.MAY, 1);

    assertEquals(1, firstOfFebruary2008.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, firstOfFebruary2009.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, firstOfMay2008.get(Calendar.DAY_OF_MONTH));
    assertEquals(1, firstOfMay2009.get(Calendar.DAY_OF_MONTH));

    DateUtil.setToLastDayOfMonth(firstOfFebruary2008);
    DateUtil.setToLastDayOfMonth(firstOfFebruary2009);
    DateUtil.setToLastDayOfMonth(firstOfMay2008);
    DateUtil.setToLastDayOfMonth(firstOfMay2009);

    assertEquals(29, firstOfFebruary2008.get(Calendar.DAY_OF_MONTH));
    assertEquals(28, firstOfFebruary2009.get(Calendar.DAY_OF_MONTH));
    assertEquals(31, firstOfMay2008.get(Calendar.DAY_OF_MONTH));
    assertEquals(31, firstOfMay2009.get(Calendar.DAY_OF_MONTH));
  }

  public void testSetToLastDayOfMonthWithInvalidDate() throws Exception {
    try {
      DateUtil.setToLastDayOfMonth(null);
      fail("Calling setToLastDayOfMonth with a null date should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling setToLastDayOfMonth with a null date threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testToString() {
    assertEquals("01/20/2007 05:30:15 PM", DateUtil.toString(DateUtil.getCalendar(2007, Calendar.JANUARY, 20, 17, 30, 15, 0)));
    assertEquals("07/20/2007 10:30:15 PM", DateUtil.toString(DateUtil.getCalendar(2007, Calendar.JULY, 20, 22, 30, 15, 0)));
    assertEquals("01/20/2006 05:30:15 AM", DateUtil.toString(DateUtil.getCalendar(2006, Calendar.JANUARY, 20, 5, 30, 15, 0)));
    assertEquals("05/27/1974", DateUtil.toString(DateUtil.getCalendar(1974, Calendar.MAY, 27, 1, 2, 30, 0), "MM/dd/yyyy"));
    assertEquals("11:09:15 PM", DateUtil.toString(DateUtil.getCalendar(2007, Calendar.JANUARY, 21, 23, 9, 15, 0), "hh:mm:ss a"));
    assertNull(DateUtil.toString(null));
  }

  public void testTruncate() throws Exception {
    final Calendar date = DateUtil.getCalendar(2007, Calendar.JUNE, 25, 20, 15, 30, 500);

    assertEquals(2007, date.get(Calendar.YEAR));
    assertEquals(Calendar.JUNE, date.get(Calendar.MONTH));
    assertEquals(25, date.get(Calendar.DAY_OF_MONTH));
    assertEquals(20, date.get(Calendar.HOUR_OF_DAY));
    assertEquals(15, date.get(Calendar.MINUTE));
    assertEquals(30, date.get(Calendar.SECOND));
    assertEquals(500, date.get(Calendar.MILLISECOND));

    DateUtil.truncate(date);

    assertEquals(2007, date.get(Calendar.YEAR));
    assertEquals(Calendar.JUNE, date.get(Calendar.MONTH));
    assertEquals(25, date.get(Calendar.DAY_OF_MONTH));
    assertEquals(0, date.get(Calendar.HOUR_OF_DAY));
    assertEquals(0, date.get(Calendar.MINUTE));
    assertEquals(0, date.get(Calendar.SECOND));
    assertEquals(0, date.get(Calendar.MILLISECOND));
  }

}
