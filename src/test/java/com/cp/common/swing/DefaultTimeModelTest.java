/*
 * DefaultTimeModelTest.java (c) 17 November 2004
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2007.2.6
 */

package com.cp.common.swing;

import com.cp.common.util.DateUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DefaultTimeModelTest extends TestCase {

  private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

  public DefaultTimeModelTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultTimeModelTest.class);
    //suite.addTest(new DefaultTimeModelTest("testName"));
    return suite;
  }

  public void testDrecementHour() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.JANUARY, 1, 1, 30, 15, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to January 1st, 2005 @ 1:30:15 AM!",
      currentCalendar, model.getCalendar());

    model.decrementHour(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2005 @ 1:30:15 AM!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 1, 0, 30, 15, 0), model.getCalendar());

    // decrement the Year, Month and Day
    currentCalendar = model.getCalendar();
    model.decrementHour(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to Decmenber 31st, 2004 @ 23:30:15 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 23, 30, 15, 0), model.getCalendar());
  }

  public void testDecrementMinute() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2000, Calendar.JANUARY, 1, 1, 30, 15, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to January 1st, 2000 @ 1:30:15 AM!",
      currentCalendar, model.getCalendar());

    model.decrementMinute(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2000 @ 1:29:15 AM!",
      DateUtil.getCalendar(2000, Calendar.JANUARY, 1, 1, 29, 15, 0), model.getCalendar());

    for (int count = 29; --count >= 0; ) {
      model.decrementMinute(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2000 @ 1:00:15 AM!",
      DateUtil.getCalendar(2000, Calendar.JANUARY, 1, 1, 0, 15, 0), model.getCalendar());

    // decrement Hour
    for (int count = 60; --count >= 0; ) {
      model.decrementMinute(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2000 @ 12:00:15 AM!",
      DateUtil.getCalendar(2000, Calendar.JANUARY, 1, 0, 0, 15, 0), model.getCalendar());

    // decrement Year, Month and Day
    currentCalendar = model.getCalendar();
    model.decrementMinute(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 1999 @ 23:59:15 PM!",
      DateUtil.getCalendar(1999, Calendar.DECEMBER, 31, 23, 59, 15, 0), model.getCalendar());
  }

  public void testDecrementSecond() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(1995, Calendar.JANUARY, 1, 1, 30, 1, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to January 1st, 1995 @ 1:30:01 AM!",
      currentCalendar, model.getCalendar());

    model.decrementSecond(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 1995 @ 1:30:00 AM!",
      DateUtil.getCalendar(1995, Calendar.JANUARY, 1, 1, 30, 0, 0), model.getCalendar());

    // decrement Minutes
    for (int count = 1800; --count >= 0; ) {
      model.decrementSecond(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 1995 @ 1:00:00 AM!",
      DateUtil.getCalendar(1995, Calendar.JANUARY, 1, 1, 0, 0, 0), model.getCalendar());

    // decrement Hour
    for (int count = 3600; --count >= 0; ) {
      model.decrementSecond(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 1995 @ 1:00:00 AM!",
      DateUtil.getCalendar(1995, Calendar.JANUARY, 1, 0, 0, 0, 0), model.getCalendar());

    // decrement Year, Month and Day
    currentCalendar = model.getCalendar();
    model.decrementSecond(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 1994 @ 23:59:59 PM!",
      DateUtil.getCalendar(1994, Calendar.DECEMBER, 31, 23, 59, 59, 0), model.getCalendar());
  }

  public void testIncrementHour() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 30, 15, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:30:15 PM!", currentCalendar, model.getCalendar());

    model.incrementHour(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 11:30:15 PM",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 23, 30, 15, 0), model.getCalendar());

    // increment Year, Month and Day
    currentCalendar = model.getCalendar();
    model.incrementHour(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 11:30:15 PM",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 1, 0, 30, 15, 0), model.getCalendar());
  }

  public void testIncrementMinute() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 30, 15, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:30:15 PM!", currentCalendar, model.getCalendar());

    model.incrementMinute(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:31:15 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 31, 15, 0), model.getCalendar());

    // increment Hour
    for (int count = 29; --count >= 0; ) {
      model.incrementMinute(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 11:00:15 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 23, 0, 15, 0), model.getCalendar());

    // increment Year, Month and Day
    for (int count = 60; --count >= 0; ) {
      model.incrementMinute(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2005 @ 12:00:15 AM!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 1, 0, 0, 15, 0), model.getCalendar());
  }

  public void testIncrementSecond() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 15, 30, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:15:30 PM!", currentCalendar, model.getCalendar());

    model.incrementSecond(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:15:31 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 15, 31, 0), model.getCalendar());

    // increment Minute
    for (int count = 29; --count >= 0; ) {
      model.incrementSecond(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 10:16:00 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 22, 16, 0, 0), model.getCalendar());

    // increment Hour
    for (int count = 2640; --count >= 0; ) {
      model.incrementSecond(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004 @ 11:00:00 PM!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31, 23, 0, 0, 0), model.getCalendar());

    // increment Year, Month and Day
    for (int count = 3600; --count >= 0; ) {
      model.incrementSecond(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2005 @ 00:00:00 PM!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 1, 0, 0, 0, 0), model.getCalendar());
  }

  public void testIsValidHour() throws Exception {
    assertTrue(DefaultTimeModel.isValidHour(0));
    assertTrue(DefaultTimeModel.isValidHour(12));
    assertFalse(DefaultTimeModel.isValidHour(24));
    assertTrue(DefaultTimeModel.isValidHour(23));
    assertTrue(DefaultTimeModel.isValidHour(2));
    assertFalse(DefaultTimeModel.isValidHour(-12));
    assertTrue(DefaultTimeModel.isValidHour(1));
    assertFalse(DefaultTimeModel.isValidHour(30));
  }

  public void testIsValidMinute() throws Exception {
    assertTrue(DefaultTimeModel.isValidMinute(0));
    assertTrue(DefaultTimeModel.isValidMinute(30));
    assertFalse(DefaultTimeModel.isValidMinute(60));
    assertTrue(DefaultTimeModel.isValidMinute(59));
    assertFalse(DefaultTimeModel.isValidMinute(-30));
    assertFalse(DefaultTimeModel.isValidMinute(-1));
    assertFalse(DefaultTimeModel.isValidMinute(100));
    assertTrue(DefaultTimeModel.isValidMinute(21));
  }

  public void testIsValidSecond() throws Exception {
    assertTrue(DefaultTimeModel.isValidSecond(0));
    assertTrue(DefaultTimeModel.isValidSecond(30));
    assertFalse(DefaultTimeModel.isValidSecond(60));
    assertTrue(DefaultTimeModel.isValidSecond(59));
    assertFalse(DefaultTimeModel.isValidSecond(-30));
    assertFalse(DefaultTimeModel.isValidSecond(-1));
    assertFalse(DefaultTimeModel.isValidSecond(100));
    assertTrue(DefaultTimeModel.isValidSecond(21));
  }

  public void testRoll() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 12, 30, 15, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 28, 2005 @ 12:30:15 PM!", currentCalendar, model.getCalendar());

    for (int count = 10; --count >= 0; ) {
      model.roll(Calendar.SECOND, true);
    }

    model.roll(Calendar.MINUTE, false);
    model.roll(Calendar.MINUTE, false);

    for (int count = 6; --count >= 0; ) {
      model.roll(Calendar.HOUR_OF_DAY, true);
    }

    assertEquals("The Model's Calendar should be set to February 28, 2005 @ 6:28:25 PM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 18, 28, 25, 0), model.getCalendar());

    for (int count = 4; --count >= 0; ) {
      model.roll(Calendar.HOUR_OF_DAY, true);
    }

    model.roll(Calendar.MINUTE, true);
    model.roll(Calendar.MINUTE, true);

    for (int count = 34; --count >= 0; ) {
      model.roll(Calendar.SECOND, true);
    }

    assertEquals("The Model's Calendar should be set to February 28, 2005 @ 10:30:59 PM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 22, 30, 59, 0), model.getCalendar());
  }

  public void testSet() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 20, 33, 3, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model);
    assertEquals("The Model's Calendar should be set to February 28, 2005 @ 10:33:03 PM!", currentCalendar, model.getCalendar());

    model.set(Calendar.HOUR_OF_DAY, 1);

    assertEquals("The Model's Calendar should bet set to February 28th, 2005, 1:33:03 AM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 1, 33, 3, 0), model.getCalendar());

    model.set(Calendar.HOUR_OF_DAY, 2);
    model.set(Calendar.MINUTE, 3);
    model.set(Calendar.SECOND, 3);

    assertEquals("The Model's Calendar should bet set to February 28th, 2005 @ 12:03:33 PM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 12, 3, 33, 0), model.getCalendar());

    model.set(Calendar.HOUR_OF_DAY, 20);
    model.set(Calendar.MINUTE, 0);
    model.set(Calendar.SECOND, 15);

    assertEquals("The Model's Calendar should bet set to February 28th, 2005 @ 10:30:15 PM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28, 20, 30, 15, 0), model.getCalendar());

    try {
      model.set(Calendar.HOUR_OF_DAY, 25);
      fail("Expected the DefaultTimeModel.set method to throw an IllegalArgumentException when setting hour to 25!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    try {
      model.set(Calendar.MINUTE, -1);
      fail("Expected the DefaultTimeModel.set method to throw an IllegalArgumentException when setting minute to -1!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }

    try {
      model.set(Calendar.SECOND, 100);
      fail("Expected the DefaultTimeModel.set method to throw an IllegalArgumentException when setting second to 100!");
    }
    catch (IllegalArgumentException e) {
      // expected behavior!
    }
  }

  public void testToggleAmPm() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 27, 21, 24, 28, 0);
    final DefaultTimeModel model = new DefaultTimeModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 27th, 2005 @ 09:24:29 PM!", currentCalendar, model.getCalendar());

    model.toggleAmPm();

    assertEquals("The Model's Calendar should be set to February 27th, 2005 @ 09:24:29 AM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 27, 9, 24, 28, 0), model.getCalendar());
    assertEquals("The currentCalendar should be set to February 27th, 2005 @ 21:24:28 PM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 27, 21, 24, 28, 0), currentCalendar);
    assertEquals(Calendar.AM, model.getCalendar().get(Calendar.AM_PM));

    model.toggleAmPm();

    assertEquals("The Model's Calendar should be set to February 27th, 2005 @ 09:24:29 AM!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 27, 21, 24, 28, 0), model.getCalendar());
    assertEquals(Calendar.PM, model.getCalendar().get(Calendar.AM_PM));
  }

}
