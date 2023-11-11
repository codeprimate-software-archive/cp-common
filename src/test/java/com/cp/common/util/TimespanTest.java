/*
 * TimespanTest.java (c) 6 April 2009
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.5.6
 * @see com.cp.common.util.Timespan
 */

package com.cp.common.util;

import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimespanTest extends TestCase {

  public TimespanTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TimespanTest.class);
    return suite;
  }

  protected Calendar getCalendar(final int calendarField, final int addValue) {
    final Calendar now = Calendar.getInstance();
    now.add(calendarField, addValue);
    return now;
  }

  public void testGetTimespan() throws Exception {
    final Calendar expectedBeginDate = DateUtil.getCalendar(2003, Calendar.OCTOBER, 15);
    final Calendar expectedEndDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Timespan timespan = Timespan.getTimespan(expectedBeginDate, expectedEndDate);

    assertNotNull(timespan);
    assertEquals(expectedBeginDate, timespan.getBeginDate());
    assertNotSame(expectedBeginDate, timespan.getBeginDate());
    assertEquals(expectedEndDate, timespan.getEndDate());
    assertNotSame(expectedEndDate, timespan.getEndDate());
  }

  public void testGetTimespanWithNullBeginDate() throws Exception {
    final Calendar expectedEndDate = DateUtil.getCalendar(2009, Calendar.MAY, 5);
    final Timespan timespan = Timespan.getTimespan(null, expectedEndDate);

    assertNotNull(timespan);
    assertNull(timespan.getBeginDate());
    assertEquals(expectedEndDate, timespan.getEndDate());
    assertNotSame(expectedEndDate, timespan.getEndDate());
  }

  public void testGetTimespanWithNullEndDate() throws Exception {
    final Calendar expectedBeginDate = DateUtil.getCalendar(2003, Calendar.OCTOBER, 15);
    final Timespan timespan = Timespan.getTimespan(expectedBeginDate, null);

    assertNotNull(timespan);
    assertEquals(expectedBeginDate, timespan.getBeginDate());
    assertNotSame(expectedBeginDate, timespan.getBeginDate());
    assertNull(timespan.getEndDate());
  }

  public void testGetTimespanWithNullBeginAndEndDates() throws Exception {
    final Timespan timespan = Timespan.getTimespan(null, null);

    assertNotNull(timespan);
    assertNull(timespan.getBeginDate());
    assertNull(timespan.getEndDate());
  }

  public void testGetTimespanWithInvalidBeginAndEndDate() throws Exception {
    final Calendar beginDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 1);
    final Calendar endDate = DateUtil.getCalendar(2007, Calendar.DECEMBER, 12);

    try {
      Timespan.getTimespan(beginDate, endDate);
      fail("Creating an instance of the Timespan class where the end date is before the begin date should have thrown an IllegalArugmentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The end date (" + DateUtil.toString(endDate) + " must come on or after the begin date ("
        + DateUtil.toString(beginDate) + "!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Creating an instance of the Timespan class where the end date is before the begin date threw an unexpected Throwable ("
        + t.getMessage() + ")!");
    }
  }

  public void testGetBeginDate() throws Exception {
    final Calendar beginDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 1);
    final Timespan timespan = Timespan.getTimespan(beginDate, null);

    assertNotNull(timespan);
    assertEquals(beginDate, timespan.getBeginDate());
    assertNotSame(beginDate, timespan.getBeginDate());
    assertNull(timespan.getEndDate());

    beginDate.set(Calendar.MONTH, Calendar.MAY);

    assertFalse(beginDate.equals(timespan.getBeginDate()));
  }

  public void testHasBeginning() throws Exception {
    assertFalse(Timespan.getTimespan(null, null).hasBeginning());
    assertFalse(Timespan.getTimespan(null, DateUtil.getCalendar(2009, Calendar.APRIL, 6)).hasBeginning());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 7), DateUtil.getCalendar(2009, Calendar.APRIL, 6)).hasBeginning());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(1998, Calendar.MAY, 18), null).hasBeginning());
  }

  public void testHasBegun() throws Exception {
    assertTrue(Timespan.getTimespan(null, null).hasBegun());
    assertTrue(Timespan.getTimespan(null, DateUtil.getCalendar(2000, Calendar.DECEMBER, 4)).hasBegun());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2001, Calendar.SEPTEMBER, 11), null).hasBegun());
    assertFalse(Timespan.getTimespan(getCalendar(Calendar.MONTH, 2), null).hasBegun());
  }

  public void testGetEndDate() throws Exception {
    final Calendar endDate = DateUtil.getCalendar(2009, Calendar.DECEMBER, 31);
    final Timespan timespan = Timespan.getTimespan(null, endDate);

    assertNotNull(timespan);
    assertNull(timespan.getBeginDate());
    assertEquals(endDate, timespan.getEndDate());
    assertNotSame(endDate, timespan.getEndDate());

    endDate.set(Calendar.MONTH, Calendar.MAY);

    assertFalse(endDate.equals(timespan.getEndDate()));
  }

  public void testHasEnded() throws Exception {
    assertFalse(Timespan.getTimespan(null, null).hasEnded());
    assertFalse(Timespan.getTimespan(getCalendar(Calendar.MONTH, 2), null).hasEnded());
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2009, Calendar.APRIL, 7), getCalendar(Calendar.MONTH, 6)).hasEnded());
    assertTrue(Timespan.getTimespan(null, DateUtil.getCalendar(2009, Calendar.APRIL, 6)).hasEnded());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2006, Calendar.DECEMBER, 6)).hasEnded());
  }

  public void testHasEnding() throws Exception {
    assertFalse(Timespan.getTimespan(null, null).hasEnding());
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2009, Calendar.APRIL, 6), null).hasEnding());
    assertTrue(Timespan.getTimespan(null, getCalendar(Calendar.MONTH, 2)).hasEnding());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.DECEMBER, 4), DateUtil.getCalendar(2002, Calendar.APRIL, 1)).hasEnding());
  }

  public void testIsDuring() throws Exception {
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 2), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isDuring(
      DateUtil.getCalendar(1999, Calendar.MAY, 15)));
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 2), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isDuring(
      DateUtil.getCalendar(2006, Calendar.JANUARY, 22)));
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 2), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isDuring(
      DateUtil.getCalendar(2010, Calendar.DECEMBER, 24)));
  }

  public void testIsDuringWithNullDate() throws Exception {
    try {
      Timespan.getTimespan(null, null).isDuring(null);
      fail("Calling isDuring with a null Calendar object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The date cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling isDuring with a null Calendar object threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testIsOngoing() throws Exception {
    assertTrue(Timespan.getTimespan(null, null).isOngoing());
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2009, Calendar.APRIL, 7), null).isOngoing());
    assertFalse(Timespan.getTimespan(null, DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOngoing());
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 2), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOngoing());
  }

  public void testIsOpenEnded() throws Exception {
    assertTrue(Timespan.getTimespan(null, null).isOpenEnded());
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2009, Calendar.APRIL, 2), null).isOpenEnded());
    assertFalse(Timespan.getTimespan(null, DateUtil.getCalendar(2003, Calendar.APRIL, 7)).isOpenEnded());
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2003, Calendar.APRIL, 2), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOpenEnded());
  }

  public void testIsOverlapping() throws Exception {
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(1995, Calendar.SEPTEMBER, 5), DateUtil.getCalendar(1999, Calendar.MAY, 18))));
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(1995, Calendar.SEPTEMBER, 5), DateUtil.getCalendar(2000, Calendar.NOVEMBER, 27))));
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(2002, Calendar.APRIL, 2), DateUtil.getCalendar(2002, Calendar.SEPTEMBER, 24))));
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(1998, Calendar.MAY, 18), DateUtil.getCalendar(2010, Calendar.DECEMBER, 12))));
    assertTrue(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(2002, Calendar.JULY, 4), DateUtil.getCalendar(2012, Calendar.FEBRUARY, 14))));
    assertFalse(Timespan.getTimespan(DateUtil.getCalendar(2000, Calendar.JANUARY, 1), DateUtil.getCalendar(2009, Calendar.APRIL, 7)).isOverlapping(
      Timespan.getTimespan(DateUtil.getCalendar(2010, Calendar.JANUARY, 22), DateUtil.getCalendar(2074, Calendar.MAY, 27))));
  }

}
