/*
 * DefaultDateModelTest.java (c) 17 November 2004
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

public class DefaultDateModelTest extends TestCase {

  private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

  public DefaultDateModelTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DefaultDateModelTest.class);
    //suite.addTest(new DefaultDateModelTest("testName"));
    return suite;
  }

  public void testDecrementDay() throws Exception {
    final Calendar today = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 26);
    DefaultDateModel model = new DefaultDateModel(today);

    assertNotNull(model);
    assertNotSame(today, model.getCalendar());
    assertEquals("The model's Calendar should be set to February 26, 2005!", today, model.getCalendar());

    model.decrementDay(today);
    model.setCalendar(today);

    assertEquals(25, model.getCalendar().get(Calendar.DAY_OF_MONTH));

    for (int count = 5; --count >= 0; ) {
      model.decrementDay(today);
    }
    model.setCalendar(today);

    assertEquals(20, model.getCalendar().get(Calendar.DAY_OF_MONTH));

    today.roll(Calendar.DAY_OF_MONTH, true);

    assertEquals("The Day of Month for Today's Calendar should have been 21!", 21, today.get(Calendar.DAY_OF_MONTH));
    assertEquals("The Day of Month for the Model's Calendar should have been 20!", 20, model.getCalendar().get(Calendar.DAY_OF_MONTH));

    final Calendar firstOfMarch2004 = DateUtil.getCalendar(2004, Calendar.MARCH, 1); // leap year
    model = new DefaultDateModel(firstOfMarch2004);

    assertNotNull(model);
    assertEquals("The Model's Calendar should be set to March 1st, 2004!", firstOfMarch2004, model.getCalendar());

    model.decrementDay(firstOfMarch2004);
    model.setCalendar(firstOfMarch2004);

    assertEquals("The Model's Calendar should be set to February 29th, 2004!",
      DateUtil.getCalendar(2004, Calendar.FEBRUARY, 29), model.getCalendar());

    final Calendar firstOfMarch2005 = DateUtil.getCalendar(2005, Calendar.MARCH, 1);
    model = new DefaultDateModel(firstOfMarch2005);

    assertNotNull(model);
    assertEquals("The Model's Calendar should be set to March 1st, 2005!", firstOfMarch2005, model.getCalendar());

    model.decrementDay(firstOfMarch2005);
    model.setCalendar(firstOfMarch2005);

    assertEquals("The Model's Calendar should be set to February 28th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28), model.getCalendar());

    final Calendar firstOfJanuary2005 = DateUtil.getCalendar(2005, Calendar.JANUARY, 1);
    model = new DefaultDateModel(firstOfJanuary2005);

    assertNotNull(model);
    assertEquals("The Model's Calendar should be set to January 1st, 2005!", firstOfJanuary2005, model.getCalendar());

    model.decrementDay(firstOfJanuary2005);
    model.setCalendar(firstOfJanuary2005);

    assertEquals("The Model's Calendar should be set to December 31st, 2004!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31), model.getCalendar());
  }

  public void testDecrementMonth() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.MARCH, 30);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to March 30th, 2005!", currentCalendar, model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementMonth(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 28th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementMonth(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 30th, 2005!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 30), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementMonth(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 30th, 2005!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 30), model.getCalendar());
  }

  public void testDecrementYear() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2004, Calendar.FEBRUARY, 29);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 29th, 2004!", currentCalendar, model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 28th, 2003!",
      DateUtil.getCalendar(2003, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calenar should be set to February 28th, 2002!",
      DateUtil.getCalendar(2002, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.decrementYear(currentCalendar);
    model.decrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Models' Calendar should be set to February 29th, 2000!",
      DateUtil.getCalendar(2000, Calendar.FEBRUARY, 29), model.getCalendar());
  }

  public void testDetermineDay() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.JANUARY, 31);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to January 31, 2005!", currentCalendar, model.getCalendar());

    currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
    currentCalendar.set(Calendar.MONTH, Calendar.FEBRUARY);

    assertEquals(1, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.FEBRUARY, currentCalendar.get(Calendar.MONTH));

    model.determineDay(currentCalendar); // determine the day

    assertEquals(28, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.FEBRUARY, currentCalendar.get(Calendar.MONTH));

    currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
    currentCalendar.set(Calendar.MONTH, Calendar.MARCH);

    assertEquals(1, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.MARCH, currentCalendar.get(Calendar.MONTH));

    model.determineDay(currentCalendar); // determine the day

    assertEquals(31, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.MARCH, currentCalendar.get(Calendar.MONTH));

    currentCalendar.set(Calendar.DAY_OF_MONTH, 1);
    currentCalendar.set(Calendar.MONTH, Calendar.APRIL);

    assertEquals(1, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.APRIL, currentCalendar.get(Calendar.MONTH));

    model.determineDay(currentCalendar); // determine the day

    assertEquals(30, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(Calendar.APRIL, currentCalendar.get(Calendar.MONTH));
  }

  public void testIncrementDay() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 1);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 1st, 2005!", currentCalendar, model.getCalendar());

    model.incrementDay(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 2nd, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 2), model.getCalendar());

    for (int index = 5; --index >= 0; ) {
      model.incrementDay(currentCalendar);
    }
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 7th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 7), model.getCalendar());

    // set currentCalenar to February 28th, 2005 to increment month
    currentCalendar.set(Calendar.DAY_OF_MONTH, 28);

    assertEquals(28, currentCalendar.get(Calendar.DAY_OF_MONTH));
    assertEquals(7, model.getCalendar().get(Calendar.DAY_OF_MONTH));

    model.setCalendar(currentCalendar);

    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 28th, 2005!", currentCalendar, model.getCalendar());

    model.incrementDay(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to March 1st, 2005!",
      DateUtil.getCalendar(2005, Calendar.MARCH, 1), model.getCalendar());

    currentCalendar = DateUtil.getCalendar(2004, Calendar.FEBRUARY, 28);
    model.setCalendar(currentCalendar);

    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 28th, 2004!", currentCalendar, model.getCalendar());

    model.incrementDay(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 29th, 2004!",
      DateUtil.getCalendar(2004, Calendar.FEBRUARY, 29), model.getCalendar());

    // set currentCalendar to December 31st, 2005 to increment year
    currentCalendar = DateUtil.getCalendar(2005, Calendar.DECEMBER, 31);
    model.setCalendar(currentCalendar);

    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to December 31st, 2005!", currentCalendar, model.getCalendar());

    model.incrementDay(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 1st, 2006!",
      DateUtil.getCalendar(2006, Calendar.JANUARY, 1), model.getCalendar());
  }

  public void testIncrementMonth() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2004, Calendar.OCTOBER, 31);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to October 31st, 2004!", currentCalendar, model.getCalendar());

    model.incrementMonth(currentCalendar);

    assertEquals("The current Calendar should be set to November 30th, 2004!",
      DateUtil.getCalendar(2004, Calendar.NOVEMBER, 30), currentCalendar);
    assertEquals("The Model's Calendar should be set to October 31st, 2004!",
      DateUtil.getCalendar(2004, Calendar.OCTOBER, 31), model.getCalendar());

    model.setCalendar(currentCalendar);

    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to November 30th, 2004!", currentCalendar, model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementMonth(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to December 31st, 2004!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementMonth(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to January 31st, 2005!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 31), model.getCalendar());
  }

  public void testIncrementYear() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(1996, Calendar.FEBRUARY, 29);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 29, 1996!", currentCalendar, model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 28th, 1997!",
      DateUtil.getCalendar(1997, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 28th, 1998!",
      DateUtil.getCalendar(1998, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 28th, 1999!",
      DateUtil.getCalendar(1999, Calendar.FEBRUARY, 28), model.getCalendar());

    currentCalendar = model.getCalendar();
    model.incrementYear(currentCalendar);
    model.setCalendar(currentCalendar);

    assertEquals("The Model's Calendar should be set to February 29th, 2000!",
      DateUtil.getCalendar(2000, Calendar.FEBRUARY, 29), model.getCalendar());
  }

  public void testIsValidDay() throws Exception {
    assertTrue(DefaultDateModel.isValidDay(29, 2, 2004)); // February
    assertFalse(DefaultDateModel.isValidDay(29, 2, 2005)); // February
    assertTrue(DefaultDateModel.isValidDay(30, 11, 2006)); // November
    assertTrue(DefaultDateModel.isValidDay(30, 11, 2007)); // November
    assertFalse(DefaultDateModel.isValidDay(31, 4, 2000)); // April
    assertFalse(DefaultDateModel.isValidDay(31, 4, 2005)); // April
    assertTrue(DefaultDateModel.isValidDay(31, 1, 2002)); // January
  }

  public void testIsValidMonth() throws Exception {
    assertFalse(DefaultDateModel.isValidMonth(-6));
    assertFalse(DefaultDateModel.isValidMonth(Calendar.JANUARY));
    assertTrue(DefaultDateModel.isValidMonth(DefaultDateModel.normalizeMonth(Calendar.JANUARY)));
    assertTrue(DefaultDateModel.isValidMonth(Calendar.MAY));
    assertTrue(DefaultDateModel.isValidMonth(DefaultDateModel.normalizeMonth(Calendar.MAY)));
    assertTrue(DefaultDateModel.isValidMonth(6));
    assertTrue(DefaultDateModel.isValidMonth(12));
    assertFalse(DefaultDateModel.isValidMonth(13));
    assertFalse(DefaultDateModel.isValidMonth(60));
  }

  public void testIsValidYear() throws Exception {
    assertTrue(DefaultDateModel.isValidYear(2000));
    assertTrue(DefaultDateModel.isValidYear(2005));
    assertTrue(DefaultDateModel.isValidYear(2020));
    assertFalse(DefaultDateModel.isValidYear(-2000));
    assertFalse(DefaultDateModel.isValidYear(-1));
    assertTrue(DefaultDateModel.isValidYear(0));
    assertTrue(DefaultDateModel.isValidYear(Integer.MAX_VALUE));
    assertFalse(DefaultDateModel.isValidYear(Integer.MIN_VALUE));
  }

  public void testRoll() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.JANUARY, 1);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to January 1st, 2005!", currentCalendar, model.getCalendar());

    model.roll(Calendar.DAY_OF_MONTH, false);

    assertEquals("The Model's Calendar should be set to December 31st, 2004!",
      DateUtil.getCalendar(2004, Calendar.DECEMBER, 31), model.getCalendar());

    model.roll(Calendar.MONTH, true);

    assertEquals("The Model's Calendar should be set to January 31st, 2005!",
      DateUtil.getCalendar(2005, Calendar.JANUARY, 31), model.getCalendar());

    model.roll(Calendar.MONTH, true);

    assertEquals("The Model's Calendar should be set to February 28th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 28), model.getCalendar());

    model.roll(Calendar.YEAR, true);

    assertEquals("The Model's Calendar should be set to February 28th, 2006!",
      DateUtil.getCalendar(2006, Calendar.FEBRUARY, 28), model.getCalendar());

    model.roll(Calendar.YEAR, false);
    model.roll(Calendar.YEAR, false);

    assertEquals("The Model's Calendar should be set to February 29th, 2004!",
      DateUtil.getCalendar(2004, Calendar.FEBRUARY, 29), model.getCalendar());
  }

  public void testSet() throws Exception {
    Calendar currentCalendar = DateUtil.getCalendar(2005, Calendar.FEBRUARY, 27);
    final DefaultDateModel model = new DefaultDateModel(currentCalendar);

    assertNotNull(model);
    assertNotSame(currentCalendar, model.getCalendar());
    assertEquals("The Model's Calendar should be set to February 27th, 2005!", currentCalendar, model.getCalendar());

    model.set(Calendar.DAY_OF_MONTH, 1);

    assertEquals("The Model's Calendar should be set to February 1st, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 1), model.getCalendar());

    model.set(Calendar.DAY_OF_MONTH, 2);

    assertEquals("The Model's Calendar should be set to February 12th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 12), model.getCalendar());

    model.set(Calendar.DAY_OF_MONTH, 4);

    assertEquals("The Model's Calendar should be set to February 4th, 2005!",
      DateUtil.getCalendar(2005, Calendar.FEBRUARY, 4), model.getCalendar());

    model.set(Calendar.DAY_OF_MONTH, 31);

    assertEquals("The Model's Calendar should be set to March 3rd, 2005!",
      DateUtil.getCalendar(2005, Calendar.MARCH, 3), model.getCalendar());

    model.set(Calendar.DAY_OF_MONTH, 31);

    assertEquals("The Model's Calendar should be set to March 31st, 2005!",
      DateUtil.getCalendar(2005, Calendar.MARCH, 31), model.getCalendar());

    model.set(Calendar.MONTH, DefaultDateModel.normalizeMonth(Calendar.APRIL));

    assertEquals("The Model's Calendar should be set to April 30th, 2005!",
      DateUtil.getCalendar(2005, Calendar.APRIL, 30), model.getCalendar());

    model.set(Calendar.MONTH,  DefaultDateModel.normalizeMonth(Calendar.FEBRUARY));
    model.set(Calendar.YEAR, 2); // 2000

    assertEquals("The Model's Calendar should be set to February 29th, 2000!",
      DateUtil.getCalendar(2000, Calendar.FEBRUARY, 29), model.getCalendar());
  }

}
