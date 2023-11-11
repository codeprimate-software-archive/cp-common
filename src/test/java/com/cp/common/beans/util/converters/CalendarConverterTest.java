/*
 * CalendarConverterTest.java (c) 27 May 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.10.7
 * @see com.cp.common.beans.util.converters.CalendarConverter
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.util.converters;

import com.cp.common.beans.util.BeanUtil;
import com.cp.common.beans.util.ConvertUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.util.CollectionUtil;
import com.cp.common.util.ConversionException;
import com.cp.common.util.DateUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CalendarConverterTest extends TestCase {

  public CalendarConverterTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CalendarConverterTest.class);
    //suite.addTest(new CalendarConverterTest("testName"));
    return suite;
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ConvertUtil.unregister(Calendar.class);
    CalendarConverter.removeDateFormatPattern("yyyy.MM.dd");
    CalendarConverter.removeDateFormatPattern("MMMMM dd, yyyy");
  }

  protected void assertEquals(final Calendar expected, final Calendar actual) {
    assertNotNull("The expected Calendar value cannot be null!", expected);
    assertNotNull("The actual Calendar value cannot be null!", actual);
    assertEquals(expected.get(Calendar.YEAR), actual.get(Calendar.YEAR));
    assertEquals(expected.get(Calendar.MONTH), actual.get(Calendar.MONTH));
    assertEquals(expected.get(Calendar.DAY_OF_MONTH), actual.get(Calendar.DAY_OF_MONTH));
    assertEquals(expected.get(Calendar.HOUR), actual.get(Calendar.HOUR));
    assertEquals(expected.get(Calendar.MINUTE), actual.get(Calendar.MINUTE));
  }

  public void testConvert() throws Exception {
    final AbstractConverter calendarConverter = new CalendarConverter();
    final Calendar now = DateUtil.getCalendar(2008, Calendar.OCTOBER, 7, 0, 51, 30, 500);

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    assertNull(calendarConverter.convert(Calendar.class, null));
    assertEquals(now, calendarConverter.convert(Calendar.class, now));
    assertEquals(now, calendarConverter.convert(Calendar.class, now.getTime()));
    assertEquals(now, calendarConverter.convert(Calendar.class, now.getTimeInMillis()));
    assertEquals(now, calendarConverter.convert(Calendar.class, StringUtil.toString(now.getTimeInMillis())));
    assertEquals(now, calendarConverter.convert(Calendar.class, "10/07/2008 12:51:30.500 AM"));
  }

  public void testConvertUsingDefaultValue() throws Exception {
    final AbstractConverter calendarConverter = new CalendarConverter(true);

    final Calendar expected = Calendar.getInstance();
    final Calendar now = DateUtil.getCalendar(2008, Calendar.OCTOBER, 7, 0, 57, 30, 500);

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertTrue(calendarConverter.isUsingDefaultValue());
    assertEquals(now, calendarConverter.convert(Calendar.class, "10/07/2008 12:57:30.500 AM"));
    assertEquals(expected, (Calendar) calendarConverter.convert(Calendar.class, null));
  }

  public void testConvertWithDefaultValue() throws Exception {
    final Calendar expected = Calendar.getInstance();
    final Calendar may31st2007 = DateUtil.getCalendar(2007, Calendar.MAY, 31, 0, 51, 15, 500);

    final AbstractConverter calendarConverter = new CalendarConverter(expected);

    assertNotNull(calendarConverter);
    assertEquals(expected, calendarConverter.getDefaultValue());
    assertTrue(calendarConverter.isUsingDefaultValue());
    assertEquals(may31st2007, calendarConverter.convert(Calendar.class, "05/31/2007 12:51:15.500 AM"));
    assertEquals(expected, (Calendar) calendarConverter.convert(Calendar.class, null));
  }

  public void testConvertWithInvalidValue() throws Exception {
    final AbstractConverter calendarConverter = new CalendarConverter();
    Calendar value = null;

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    assertNull(value);

    try {
      value = (Calendar) calendarConverter.convert(Calendar.class, Boolean.TRUE);
      fail("Calling convert with invalid value (Boolean.TRUE) should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("Failed to convert (true) of type (" + Boolean.class.getName() + ") to a Calendar object!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling convert with invalid value (Boolean.TRUE) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testGetCalendar() throws Exception {
    final CalendarConverter calendarConverter = new CalendarConverter();

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    TestCase.assertEquals(DateUtil.getCalendar(2007, Calendar.MAY, 1), calendarConverter.getCalendar("May, 2007"));
    TestCase.assertEquals(DateUtil.getCalendar(2007, Calendar.MAY, 31), calendarConverter.getCalendar("05/31/2007"));
    TestCase.assertEquals(DateUtil.getCalendar(2001, Calendar.OCTOBER, 11, 13, 1, 0, 0), calendarConverter.getCalendar("10/11/2001 01:01 PM"));
    TestCase.assertEquals(DateUtil.getCalendar(1999, Calendar.JULY, 4, 17, 30, 15, 0), calendarConverter.getCalendar("07/04/1999 05:30:15 PM"));
    TestCase.assertEquals(DateUtil.getCalendar(1991, Calendar.DECEMBER, 12, 20, 15, 15, 500), calendarConverter.getCalendar("12/12/1991 08:15:15.500 PM"));
  }

  public void testGetCalendarWithInvalidValue() throws Exception {
    final CalendarConverter calendarConverter = new CalendarConverter();
    Calendar value = null;

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    assertNull(value);

    try {
      value = calendarConverter.getCalendar("2007.05.31");
      fail("Calling getCalendar with value '2007.05.31' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      assertEquals("The String value (2007.05.31) cannot be converted to a Calendar object!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getCalendar with value '2007.05.31' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
  }

  public void testAddDateFormatPattern() throws Exception {
    final AbstractConverter calendarConverter = new CalendarConverter();
    Calendar value = null;

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    assertNull(value);

    try {
      value = (Calendar) calendarConverter.convert(Calendar.class, "2008.10.07");
      fail("Calling convert with value '2008.10.07' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling convert with value '2008.10.07' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(value);
    assertFalse(CalendarConverter.addDateFormatPattern(null));
    assertFalse(CalendarConverter.addDateFormatPattern(""));
    assertFalse(CalendarConverter.addDateFormatPattern(" "));
    assertTrue(CalendarConverter.addDateFormatPattern("yyyy.MM.dd"));

    try {
      value = (Calendar) calendarConverter.convert(Calendar.class, "2008.10.06");
    }
    catch (Exception e) {
      fail("Calling convert with value '2008.10.06' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(DateUtil.getCalendar(2008, Calendar.OCTOBER, 6), value);
  }

  public void testGetDateFormatPatterns() throws Exception {
    Collection<String> expectedDateFormatPatterns = Arrays.asList(CalendarConverter.DEFAULT_DATE_FORMAT_PATTERNS);
    Collection<String> actualDateFormatPatterns = CalendarConverter.getDateFormatPatterns();

    assertNotNull(actualDateFormatPatterns);
    assertEquals(expectedDateFormatPatterns, actualDateFormatPatterns);
    assertTrue(CalendarConverter.addDateFormatPattern("yyyy.MM.dd"));
    assertTrue(CalendarConverter.addDateFormatPattern("MMMMM dd, yyyy"));

    expectedDateFormatPatterns = new LinkedList<String>(CollectionUtil.getList("MMMMM dd, yyyy", "yyyy.MM.dd"));
    expectedDateFormatPatterns.addAll(Arrays.asList(CalendarConverter.DEFAULT_DATE_FORMAT_PATTERNS));
    actualDateFormatPatterns = CalendarConverter.getDateFormatPatterns();

    assertNotNull(actualDateFormatPatterns);
    assertEquals(expectedDateFormatPatterns, actualDateFormatPatterns);
  }

  public void testRemoveDateFormatPattern() throws Exception {
    CalendarConverter.addDateFormatPattern("yyyy.MM.dd");
    final AbstractConverter calendarConverter = new CalendarConverter();
    Calendar value = null;

    assertNotNull(calendarConverter);
    assertNull(calendarConverter.getDefaultValue());
    assertFalse(calendarConverter.isUsingDefaultValue());
    assertNull(value);

    try {
      value = (Calendar) calendarConverter.convert(Calendar.class, "2008.10.06");
    }
    catch (Exception e) {
      fail("Calling convert with value '2008.10.06' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(DateUtil.getCalendar(2008, Calendar.OCTOBER, 6), value);
    assertFalse(CalendarConverter.removeDateFormatPattern("MMMMM dd, yyyy"));
    assertTrue(CalendarConverter.removeDateFormatPattern("yyyy.MM.dd"));

    try {
      value = (Calendar) calendarConverter.convert(Calendar.class, "2008.10.07");
      fail("Calling convert with value '2008.10.07' should have thrown a ConversionException!");
    }
    catch (ConversionException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling convert with value '2008.10.07' threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertEquals(DateUtil.getCalendar(2008, Calendar.OCTOBER, 6), value);
  }

  public void testSetBeanProperty() throws Exception {
    final MockBean bean = new MockBean();

    assertNull(bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", "05/31/2007");

    assertEquals(DateUtil.getCalendar(2007, Calendar.MAY, 31), bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", null);

    assertNull(bean.getDate());
  }

  public void testSetBeanPropertyUsingDefaultValue() throws Exception {
    ConvertUtil.register(Calendar.class, new CalendarConverter(true));
    final MockBean bean = new MockBean();

    assertNull(bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", "10/07/2008");

    assertEquals(DateUtil.getCalendar(2008, Calendar.OCTOBER, 7), bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", null);

    assertEquals(Calendar.getInstance(), bean.getDate());
  }

  public void testSetBeanPropertyWithDefaultValue() throws Exception {
    final Calendar defaultValue = DateUtil.getCalendar(2007, Calendar.MAY, 31);
    ConvertUtil.register(Calendar.class, new CalendarConverter(defaultValue));
    final MockBean bean = new MockBean();

    assertNull(bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", "10/07/2008");

    assertEquals(DateUtil.getCalendar(2008, Calendar.OCTOBER, 7), bean.getDate());

    BeanUtil.setPropertyValue(bean, "date", null);

    assertEquals(defaultValue, bean.getDate());
  }

  public static final class MockBean {

    private Calendar date;

    public Calendar getDate() {
      return date;
    }

    public void setDate(final Calendar date) {
      this.date = date;
    }
  }

}
