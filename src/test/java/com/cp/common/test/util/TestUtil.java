/*
 * TestUtil.java (c) 3 December 2003
 *
 * Copyright (c) 2003, Codeprimate, LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.3.29
 * @see junit.framework.Assert
 * @see org.easymock.EasyMock
 * @see org.jmock.Mockery
 */

package com.cp.common.test.util;

import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import junit.framework.Assert;
import org.easymock.EasyMock;
import org.jmock.Mockery;

public final class TestUtil {

  public static final String PROJECT_DIR_SYSTEM_PROPERTY = "project.dir";
  private static final String DEFAULT_MESSAGE = "";

  /**
   * Default private constructor to enforce non-instantiability and non-extendability.
   */
  private TestUtil()  {
  }

  public static <T> void assertContainsAll(final Collection<T> collection, T... elements) {
    Assert.assertTrue(collection.containsAll(Arrays.asList(elements)));
  }

  public static void assertEquals(final Object[] expected, final Object[] actual) {
    assertEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertEquals(final String message, final Object[] expected, final Object[] actual) {
    if (ObjectUtil.isNull(expected)) {
      Assert.assertNull(message, actual);
    }
    else {
      Assert.assertNotNull(message, actual);
      Assert.assertEquals(message, expected.length, actual.length);

      for (int index = expected.length; --index >= 0; ) {
        Assert.assertEquals(message, expected[index], actual[index]);
      }
    }
  }

  public static void assertEquals(final Object[][] expected, final Object[][] actual) {
    assertEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertEquals(final String message, final Object[][] expected, final Object[][] actual) {
    if (ObjectUtil.isNull(expected)) {
      Assert.assertNull(message, actual);
    }
    else {
      Assert.assertNotNull(message, actual);
      Assert.assertEquals(message, expected.length, actual.length);

      for (int rowIndex = expected.length; --rowIndex >= 0; ) {
        assertEquals(message, expected[rowIndex], actual[rowIndex]);
      }
    }
  }

  public static void assertNegative(final long value) {
    assertNegative(DEFAULT_MESSAGE, value);
  }

  public static void assertNegative(final String message, final long value) {
    Assert.assertTrue(message, value < 0);
  }

  public static void assertNotEquals(final boolean expected, final boolean actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final boolean expected, final boolean actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final byte expected, final byte actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final byte expected, final byte actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final char expected, final char actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final char expected, final char actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final double expected, final double actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final double expected, final double actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final float expected, final float actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final float expected, final float actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final int expected, final int actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final int expected, final int actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final long expected, final long actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final long expected, final long actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final short expected, final short actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final short expected, final short actual) {
    Assert.assertTrue(message, (expected != actual));
  }

  public static void assertNotEquals(final Object expected, final Object actual) {
    assertNotEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNotEquals(final String message, final Object expected, final Object actual) {
    Assert.assertFalse(message, actual.equals(expected));
  }

  public static void assertNullEmpty(final String actual) {
    assertNullEquals(DEFAULT_MESSAGE, actual);
  }

  public static void assertNullEmpty(final String message, final String actual) {
    if (!StringUtil.isEmpty(actual)) {
      if (!StringUtil.isEmpty(message)) {
        Assert.fail(message);
      }
      else {
        Assert.fail("Expected null or empty, but was (" + actual + ")!");
      }
    }
  }

  public static void assertNullEquals(final Object expected, final Object actual) {
    assertNullEquals(DEFAULT_MESSAGE, expected, actual);
  }

  public static void assertNullEquals(final String message, final Object expected, final Object actual) {
    if (ObjectUtil.isNull(expected)) {
      Assert.assertNull(message, actual);
    }
    else {
      Assert.assertEquals(message, expected, actual);
    }
  }

  public static void assertPositive(final long value) {
    assertPositive(DEFAULT_MESSAGE, value);
  }

  public static void assertPositive(final String message, final long value) {
    Assert.assertTrue(message, value > 0);
  }

  public static void assertShuffled(final Number[] numbers) {
    double previousValue = Double.MIN_VALUE;

    for (final Number currentValue : numbers) {
      if (currentValue.doubleValue() < previousValue) {
        return;
      }
      else {
        previousValue = currentValue.doubleValue();
      }
    }

    Assert.fail("The numbers array is not shuffled!");
  }

  public static void assertSorted(final Number[] numbers) {
    double previousValue = Double.MIN_VALUE;

    for (final Number currentValue : numbers) {
      Assert.assertTrue(currentValue.doubleValue() >= previousValue);
      previousValue = currentValue.doubleValue();
    }
  }

  public static void assertZero(final int value) {
    assertZero(DEFAULT_MESSAGE, value);
  }

  public static void assertZero(final String message, final int value) {
    Assert.assertEquals(message, 0, value);
  }

  /**
   * Used by the DateUtilTest class.
   * @param year the Calendar year.
   * @param month the Calendar month within the year.
   * @param dayOfMonth the numerical day in the month.
   * @return an instance of the Calendar object preset with the specified date field values.
   * @see com.cp.common.util.DateUtil#getCalendar
   */
  public static Calendar getCalendar(final int year,
                                     final int month,
                                     final int dayOfMonth) {
    final Calendar date = Calendar.getInstance();
    date.clear();
    date.set(Calendar.YEAR, year);
    date.set(Calendar.MONTH, month);
    date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    return date;
  }

  /**
   * Used by the DateUtilTest class.
   * @param year the Calendar year.
   * @param month the Calendar month within the year.
   * @param dayOfMonth the numerical day in the month.
   * @param hour the hour of day.
   * @param minute the minute within the hour.
   * @param second the number of seconds within the minute.
   * @param millisecond the number of milliseconds within the second.
   * @return an instance of the Calendar object preset with the specified date field values.
   * @see com.cp.common.util.DateUtil#getCalendar
   */
  public static Calendar getCalendar(final int year,
                                     final int month,
                                     final int dayOfMonth,
                                     final int hour,
                                     final int minute,
                                     final int second,
                                     final int millisecond) {
    final Calendar calendar = getCalendar(year, month, dayOfMonth);
    calendar.set(Calendar.HOUR, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, second);
    calendar.set(Calendar.MILLISECOND, millisecond);
    return calendar;
  }

  public static Iterator getMockEmptyIterator() {
    final Iterator it = EasyMock.createMock(Iterator.class);
    EasyMock.expect(it.hasNext()).andReturn(false);
    EasyMock.replay(it);
    return it;
  }

  public static Verifier getEasyMockVerifier() {
    return new EasyMockVerifier();
  }

  public static Verifier getJMockVerifier(final Mockery context) {
    return new JMockVerifier(context);
  }

  public static Verifier getNoOpVerifier() {
    return new NoOpVerifier();
  }

  public static interface Verifier {

    public boolean add(Object objectToVerify);

    public void verify();

  }

  protected static abstract class AbstractVerifier implements Verifier {

    private final Collection<Object> objectsToVerifyCollection = new LinkedList<Object>();

    protected Collection<Object> getObjectsToVerify() {
      return Collections.unmodifiableCollection(objectsToVerifyCollection);
    }

    public boolean add(final Object objectToVerify) {
      com.cp.common.lang.Assert.notNull(objectToVerify, "The object to verify cannot be null!");
      return objectsToVerifyCollection.add(objectToVerify);
    }
  }

  protected static final class EasyMockVerifier extends AbstractVerifier {

    public void verify() {
      for (final Object objectToVerify : getObjectsToVerify()) {
        EasyMock.verify(objectToVerify);
      }
    }
  }

  protected static final class JMockVerifier extends AbstractVerifier {

    private final Mockery context;

    public JMockVerifier(final Mockery context) {
      com.cp.common.lang.Assert.notNull(context, "The jMock Mockery context cannot be null!");
      this.context = context;
    }

    public void verify() {
      context.assertIsSatisfied();
    }
  }

  protected static final class NoOpVerifier implements Verifier {

    public boolean add(final Object objectToVerify) {
      return false;
    }

    public void verify() {
    }
  }

}
