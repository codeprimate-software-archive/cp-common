/*
 * UtilityMethodsTest.java (c) 17 August 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.3.18
 * @see com.cp.common.lang.UtilityMethods
 */

package com.cp.common.lang;

import static com.cp.common.lang.UtilityMethods.copy;
import static com.cp.common.lang.UtilityMethods.is;
import static com.cp.common.test.util.TestUtil.assertNotEquals;
import com.cp.common.util.ComparableComparator;
import com.cp.common.util.DateUtil;
import java.util.Calendar;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UtilityMethodsTest extends TestCase {

  public UtilityMethodsTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(UtilityMethodsTest.class);
    //suite.addTest(new UtilityMethodsTest("testName"));
    return suite;
  }

  public void testCopy() throws Exception {
    final MockObject<String> fromObject = new MockObject<String>("test");
    final MockObject<String> toObject = new MockObject<String>();

    assertEquals("test", fromObject.getValue());
    assertNull(toObject.getValue());

    copy(fromObject).to(toObject);

    assertEquals("test", fromObject.getValue());
    assertEquals("test", toObject.getValue());
  }

  public void testIsAfter() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> toObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(2), toObject.getValue());
    assertNotEquals(fromObject, toObject);
    assertTrue(is(toObject).after(fromObject));
    assertFalse(is(fromObject).after(toObject));
  }

  public void testIsNotAfter() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> toObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(2), toObject.getValue());
    assertNotEquals(fromObject, toObject);
    assertFalse(is(toObject).not().after(fromObject));
    assertTrue(is(fromObject).not().after(toObject));
  }

  public void testIsBefore() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> toObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(2), toObject.getValue());
    assertNotEquals(fromObject, toObject);
    assertTrue(is(fromObject).before(toObject));
    assertFalse(is(toObject).before(fromObject));
  }

  public void testIsNotBefore() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> toObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(2), toObject.getValue());
    assertNotEquals(fromObject, toObject);
    assertFalse(is(fromObject).not().before(toObject));
    assertTrue(is(toObject).not().before(fromObject));
  }

  public void testIsEqual() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> equalToObject = new MockObject<Integer>(1);
    final MockObject<Integer> notEqualToObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(1), equalToObject.getValue());
    assertEquals(new Integer(2), notEqualToObject.getValue());
    assertEquals(fromObject, equalToObject);
    assertNotEquals(fromObject, notEqualToObject);
    assertTrue(is(fromObject).equal(equalToObject));
    assertFalse(is(fromObject).equal(notEqualToObject));
  }

  public void testIsNotEqual() throws Exception {
    final MockObject<Integer> fromObject = new MockObject<Integer>(1);
    final MockObject<Integer> equalToObject = new MockObject<Integer>(1);
    final MockObject<Integer> notEqualToObject = new MockObject<Integer>(2);

    assertEquals(new Integer(1), fromObject.getValue());
    assertEquals(new Integer(1), equalToObject.getValue());
    assertEquals(new Integer(2), notEqualToObject.getValue());
    assertEquals(fromObject, equalToObject);
    assertNotEquals(fromObject, notEqualToObject);
    assertFalse(is(fromObject).not().equal(equalToObject));
    assertTrue(is(fromObject).not().equal(notEqualToObject));
  }

  public void testIsFalse() throws Exception {
    assertTrue(is(false).False());
    assertTrue(is(Boolean.FALSE).False());
    assertTrue(is("false").False());
    assertTrue(is("0").False());
    assertTrue(is("1").False());
    assertFalse(is(true).False());
    assertFalse(is(Boolean.TRUE).False());
    assertFalse(is("true").False());
  }

  public void testIsNotFalse() throws Exception {
    assertFalse(is(false).not().False());
    assertFalse(is(Boolean.FALSE).not().False());
    assertFalse(is("false").not().False());
    assertFalse(is("0").not().False());
    assertFalse(is("1").not().False());
    assertTrue(is(true).not().False());
    assertTrue(is(Boolean.TRUE).not().False());
    assertTrue(is("true").not().False());
  }

  public void testIsNull() throws Exception {
    assertTrue(is(null).Null());
    assertFalse(is("null").Null());
  }

  public void testIsNotNull() throws Exception {
    assertFalse(is(null).not().Null());
    assertTrue(is("null").not().Null());
  }

  public void testIsOn() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertFalse(is(fromDate).on(beforeDate));
    assertTrue(is(fromDate).on(onDate));
    assertFalse(is(fromDate).on(afterDate));
  }

  public void testIsNotOn() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertTrue(is(fromDate).not().on(beforeDate));
    assertFalse(is(fromDate).not().on(onDate));
    assertTrue(is(fromDate).not().on(afterDate));
  }

  public void testIsOnOrAfter() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertTrue(is(fromDate).onOrAfter(beforeDate));
    assertTrue(is(fromDate).onOrAfter(onDate));
    assertFalse(is(fromDate).onOrAfter(afterDate));
  }

  public void testIsNotOnOrAfter() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertFalse(is(fromDate).not().onOrAfter(beforeDate));
    assertFalse(is(fromDate).not().onOrAfter(onDate));
    assertTrue(is(fromDate).not().onOrAfter(afterDate));
  }

  public void testIsOnOrBefore() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertFalse(is(fromDate).onOrBefore(beforeDate));
    assertTrue(is(fromDate).onOrBefore(onDate));
    assertTrue(is(fromDate).onOrBefore(afterDate));
  }

  public void testIsNotOnOrBefore() throws Exception {
    final Calendar fromDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar onDate = DateUtil.getCalendar(2008, Calendar.JANUARY, 12, 1, 0, 0, 500);
    final Calendar beforeDate = DateUtil.getCalendar(2007, Calendar.FEBRUARY, 13, 23, 59, 59, 100);
    final Calendar afterDate = DateUtil.getCalendar(2009, Calendar.JANUARY, 11, 0, 30, 15, 200);

    assertEquals(fromDate, onDate);
    assertTrue(beforeDate.before(fromDate));
    assertTrue(afterDate.after(fromDate));
    assertTrue(is(fromDate).not().onOrBefore(beforeDate));
    assertFalse(is(fromDate).not().onOrBefore(onDate));
    assertFalse(is(fromDate).not().onOrBefore(afterDate));
  }

  public void testIsSame() throws Exception {
    final String test = "test";
    final String otherTest = new String("test");

    assertEquals(test, otherTest);
    assertNotSame(test, otherTest);
    assertTrue(is(test).same(test));
    assertTrue(is(test).same("test"));
    assertFalse(is(test).same(otherTest));
  }

  public void testIsNotSame() throws Exception {
    final String test = "test";
    final String otherTest = new String("test");

    assertEquals(test, otherTest);
    assertNotSame(test, otherTest);
    assertFalse(is(test).not().same(test));
    assertFalse(is(test).not().same("test"));
    assertTrue(is(test).not().same(otherTest));
  }

  public void testIsTrue() throws Exception {
    assertTrue(is(true).True());
    assertTrue(is(Boolean.TRUE).True());
    assertTrue(is("true").True());
    assertFalse(is(false).True());
    assertFalse(is(Boolean.FALSE).True());
    assertFalse(is("false").True());
    assertFalse(is("0").True());
    assertFalse(is("1").True());
  }

  public void testIsNotTrue() throws Exception {
    assertFalse(is(true).not().True());
    assertFalse(is(Boolean.TRUE).not().True());
    assertFalse(is("true").not().True());
    assertTrue(is(false).not().True());
    assertTrue(is(Boolean.FALSE).not().True());
    assertTrue(is("false").not().True());
    assertTrue(is("0").not().True());
    assertTrue(is("1").not().True());
  }

  public static class MockObject<T extends Comparable<T>> implements Comparable<MockObject<T>> {

    private T value;

    public MockObject() {
    }

    public MockObject(final T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }

    public void setValue(final T value) {
      this.value = value;
    }

    public int compareTo(final MockObject<T> mockObj) {
      return ComparableComparator.<T>getInstance().compare(getValue(), mockObj.getValue());
    }

    public boolean equals(final Object obj) {
      if (obj == this) {
        return true;
      }

      if (!(obj instanceof MockObject)) {
        return false;
      }

      final MockObject that = (MockObject) obj;

      return ObjectUtil.equals(getValue(), that.getValue());
    }

    public int hashCode() {
      int hashValue = 17;
      hashValue = 37 * hashValue + ObjectUtil.hashCode(getValue());
      return hashValue;
    }

    public String toString() {
      return value.toString();
    }
  }

}
