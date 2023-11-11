/*
 * TimeUnitTest.java (c) 5 September 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.10
 * @see com.cp.common.util.TimeUnit
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimeUnitTest extends TestCase {

  public TimeUnitTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(TimeUnitTest.class);
    //suite.addTest(new TimeUnitTest("testConvertTo"));
    return suite;
  }

  public void testCompareTo() throws Exception {
    assertTrue(TimeUnit.MILLISECOND.compareTo(TimeUnit.MILLENNIUM) < 0);
    assertTrue(TimeUnit.MILLISECOND.compareTo(TimeUnit.MILLISECOND) == 0);
    assertTrue(TimeUnit.MILLISECOND.compareTo(TimeUnit.SECOND) < 0);
    assertTrue(TimeUnit.SECOND.compareTo(TimeUnit.MILLISECOND) > 0);
    assertTrue(TimeUnit.SECOND.compareTo(TimeUnit.MINUTE) < 0);
    assertTrue(TimeUnit.MINUTE.compareTo(TimeUnit.SECOND) > 0);
    assertTrue(TimeUnit.MINUTE.compareTo(TimeUnit.HOUR) < 0);
    assertTrue(TimeUnit.HOUR.compareTo(TimeUnit.MINUTE) > 0);
    assertTrue(TimeUnit.HOUR.compareTo(TimeUnit.DAY) < 0);
    assertTrue(TimeUnit.DAY.compareTo(TimeUnit.HOUR) > 0);
    assertTrue(TimeUnit.DAY.compareTo(TimeUnit.WEEK) < 0);
    assertTrue(TimeUnit.WEEK.compareTo(TimeUnit.DAY) > 0);
    assertTrue(TimeUnit.WEEK.compareTo(TimeUnit.JANUARY) < 0);
    assertTrue(TimeUnit.JANUARY.compareTo(TimeUnit.WEEK) > 0);
    assertTrue(TimeUnit.JANUARY.compareTo(TimeUnit.YEAR) < 0);
    assertTrue(TimeUnit.YEAR.compareTo(TimeUnit.JANUARY) > 0);
    assertTrue(TimeUnit.YEAR.compareTo(TimeUnit.LEAP_YEAR) < 0);
    assertTrue(TimeUnit.LEAP_YEAR.compareTo(TimeUnit.YEAR) > 0);
    assertTrue(TimeUnit.LEAP_YEAR.compareTo(TimeUnit.DECADE) < 0);
    assertTrue(TimeUnit.DECADE.compareTo(TimeUnit.LEAP_YEAR) > 0);
    assertTrue(TimeUnit.DECADE.compareTo(TimeUnit.SCORE) < 0);
    assertTrue(TimeUnit.SCORE.compareTo(TimeUnit.DECADE) > 0);
    assertTrue(TimeUnit.SCORE.compareTo(TimeUnit.CENTURY) < 0);
    assertTrue(TimeUnit.CENTURY.compareTo(TimeUnit.SCORE) > 0);
    assertTrue(TimeUnit.CENTURY.compareTo(TimeUnit.MILLENNIUM) < 0);
    assertTrue(TimeUnit.MILLENNIUM.compareTo(TimeUnit.MILLISECOND) > 0);
    assertTrue(TimeUnit.MAY.compareTo(TimeUnit.APRIL) > 0);
    assertTrue(TimeUnit.FEBRUARY.compareTo(TimeUnit.JUNE) < 0);
    assertTrue(TimeUnit.EXTENDED_FEBRUARY.compareTo(TimeUnit.FEBRUARY) > 0);
  }

  public void testConvertTo() throws Exception {
    assertEquals("2 Hours ", TimeUnit.convertTo(120, TimeUnit.MINUTE, TimeUnit.HOUR, TimeUnit.SECOND));
    assertEquals("2 Hours 24 Minutes ", TimeUnit.convertTo(144, TimeUnit.MINUTE, TimeUnit.HOUR, TimeUnit.SECOND));
    assertEquals("2 Hours 24 Minutes ", TimeUnit.convertTo(144, TimeUnit.MINUTE, TimeUnit.HOUR, TimeUnit.HOUR));
    assertEquals("1800 Seconds ", TimeUnit.convertTo(30, TimeUnit.MINUTE, TimeUnit.SECOND, TimeUnit.MILLISECOND));
    assertEquals("7 Days ", TimeUnit.convertTo(1, TimeUnit.WEEK, TimeUnit.DAY, TimeUnit.SECOND));
    assertEquals("31 Days ", TimeUnit.convertTo(1, TimeUnit.JANUARY, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("28 Days ", TimeUnit.convertTo(1, TimeUnit.FEBRUARY, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("29 Days ", TimeUnit.convertTo(1, TimeUnit.EXTENDED_FEBRUARY, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("30 Days ", TimeUnit.convertTo(1, TimeUnit.APRIL, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("44640 Minutes ", TimeUnit.convertTo(1, TimeUnit.AUGUST, TimeUnit.MINUTE, TimeUnit.MINUTE));
    assertEquals("365 Days ", TimeUnit.convertTo(1, TimeUnit.YEAR, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("1095 Days ", TimeUnit.convertTo(3, TimeUnit.YEAR, TimeUnit.DAY, TimeUnit.DAY));
    assertEquals("2 Hours 1 Second ", TimeUnit.convertTo(7201000, TimeUnit.MILLISECOND, TimeUnit.HOUR, TimeUnit.SECOND));
  }

  public void testEquals() throws Exception {
    assertTrue(TimeUnit.MILLISECOND.equals(TimeUnit.MILLISECOND));
    assertTrue(TimeUnit.SECOND.equals(TimeUnit.SECOND));
    assertTrue(TimeUnit.MINUTE.equals(TimeUnit.MINUTE));
    assertTrue(TimeUnit.HOUR.equals(TimeUnit.HOUR));
    assertTrue(TimeUnit.DAY.equals(TimeUnit.DAY));
    assertTrue(TimeUnit.WEEK.equals(TimeUnit.WEEK));
    assertTrue(TimeUnit.MAY.equals(TimeUnit.MAY));
    // NOTE: this test will pass for example on the following:
    // TimeUnit.JANUARY.equals(TimeUnit.AUGUST) because both January and August
    // are defined in terms of 31 days.
    assertFalse(TimeUnit.JANUARY.equals(TimeUnit.JUNE)); // Bad Test!
    assertTrue(TimeUnit.YEAR.equals(TimeUnit.YEAR));
    assertFalse(TimeUnit.YEAR.equals(TimeUnit.LEAP_YEAR));
    assertTrue(TimeUnit.DECADE.equals(TimeUnit.DECADE));
    assertTrue(TimeUnit.SCORE.equals(TimeUnit.SCORE));
    assertTrue(TimeUnit.CENTURY.equals(TimeUnit.CENTURY));
    assertTrue(TimeUnit.MILLENNIUM.equals(TimeUnit.MILLENNIUM));
    assertFalse(TimeUnit.OTHER_MILLENNIUM.equals(TimeUnit.MILLENNIUM));
    assertFalse(TimeUnit.MILLENNIUM.equals(TimeUnit.OTHER_MILLENNIUM));
  }

  public void testGetInTermsOf() throws Exception {
    assertEquals(7, TimeUnit.DAY.getInTermsOf(7, TimeUnit.DAY));
    assertEquals(2, TimeUnit.DAY.getInTermsOf(14, TimeUnit.WEEK));
    assertEquals(2, TimeUnit.DAY.getInTermsOf(16, TimeUnit.WEEK));
    assertEquals(0, TimeUnit.DAY.getInTermsOf(1, TimeUnit.WEEK));
    assertEquals(44640, TimeUnit.AUGUST.getInTermsOf(1, TimeUnit.MINUTE));
  }

  public void testGetTotalUnitValue() throws Exception {
    assertEquals(1, TimeUnit.MILLISECOND.getTotalUnitValue(1));
    assertEquals(500, TimeUnit.MILLISECOND.getTotalUnitValue(500));
    assertEquals(1000, TimeUnit.SECOND.getTotalUnitValue(1));
    assertEquals(30000, TimeUnit.SECOND.getTotalUnitValue(30));
    assertEquals(60000, TimeUnit.SECOND.getTotalUnitValue(60));
    assertEquals(60000, TimeUnit.MINUTE.getTotalUnitValue(1));
    assertEquals(300000, TimeUnit.MINUTE.getTotalUnitValue(5));
    assertEquals(3600000, TimeUnit.MINUTE.getTotalUnitValue(60));
    assertEquals(3600000, TimeUnit.HOUR.getTotalUnitValue(1));
    assertEquals(43200000, TimeUnit.HOUR.getTotalUnitValue(12));
    assertEquals(86400000, TimeUnit.HOUR.getTotalUnitValue(24));
    assertEquals(86400000, TimeUnit.DAY.getTotalUnitValue(1));
    assertEquals(604800000, TimeUnit.DAY.getTotalUnitValue(7));
    assertEquals(604800000, TimeUnit.WEEK.getTotalUnitValue(1));
    assertEquals(1209600000, TimeUnit.WEEK.getTotalUnitValue(2));
    //assertEquals(2678400000, TimeUnit.MAY.getTotalUnitValue(1));
  }

}
