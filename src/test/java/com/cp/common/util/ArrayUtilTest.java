/*
 * ArrayUtilTest.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.7.27
 * @see com.cp.common.util.ArrayUtil
 * @see junit.framework.TestCase
 */

package com.cp.common.util;

import com.cp.common.lang.NumberUtil;
import com.cp.common.test.util.TestUtil;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jmock.Mockery;

public class ArrayUtilTest extends TestCase {

  private final Mockery context = new Mockery();

  public ArrayUtilTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(ArrayUtilTest.class);
    //suite.addTest(new ArrayUtilTest("testName"));
    return suite;
  }

  public void testAsListBooleanArray() throws Exception {
    final boolean[] array = { true, false, false, true };
    final List<Boolean> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Boolean.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListCharArray() throws Exception {
    final char[] array = { 'c', 'a', 'f', 'e', 'b', 'a', 'b', 'e' };
    final List<Character> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Character.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListByteArray() throws Exception {
    final byte[] array = { 0, 1 };
    final List<Byte> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Byte.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListShortArray() throws Exception {
    final short[] array = { 0, 1, 2, 4, 8 };
    final List<Short> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Short.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListIntArray() throws Exception {
    final int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    final List<Integer> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Integer.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListLongArray() throws Exception {
    final long[] array = { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55 };
    final List<Long> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(Long.valueOf(array[index]), list.get(index));
    }
  }

  public void testAsListFloatArray() throws Exception {
    final float[] array = { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f };
    final List<Float> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(array[index], list.get(index));
    }
  }

  public void testAsListDoubleArray() throws Exception {
    final double[] array = { -2.5, -1.2, 0.0, 2.1, 5.2, Math.PI };
    final List<Double> list = ArrayUtil.asList(array);

    assertNotNull(list);
    assertEquals(array.length, list.size());

    for (int index = 0; index < array.length; index++) {
      assertEquals(array[index], list.get(index));
    }
  }

  public void testEnumeration() throws Exception {
    final String[] array = { "test", "tester", "testing" };
    final Enumeration<String> enumx = ArrayUtil.enumeration(array);

    assertNotNull(enumx);

    for (final String element : array) {
      assertTrue(enumx.hasMoreElements());
      assertEquals(element, enumx.nextElement());
    }
  }

  public void testFindAllBy() throws Exception {
    final Filter<Integer> primeFilter = new Filter<Integer>() {
      public boolean accept(final Integer value) {
        return NumberUtil.isPrime(value);
      }
    };

    final Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    final Object[] expectedArray = { 2, 3, 5, 7 };
    final Object[] actualArray = ArrayUtil.findAllBy(array, primeFilter);

    assertNotNull(actualArray);
    TestUtil.assertEquals(expectedArray, actualArray);
  }

  public void testFindAllByReturnsEmptyArray() throws Exception {
    final Filter<Double> wholeFilter = new Filter<Double>() {
      public boolean accept(final Double value) {
        return NumberUtil.isWhole(value);
      }
    };

    final Double[] array = { 0.123, 1.1, 2.2, Math.PI };
    final Object[] actualArray = ArrayUtil.findAllBy(array, wholeFilter);

    assertNotNull(actualArray);
    assertEquals(0, actualArray.length);
  }

  public void testFindAllByWithNullArray() throws Exception {
    try {
      ArrayUtil.findAllBy(null, context.mock(Filter.class));
      fail("Calling findAllBy with a null array should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The array of elements cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findAllBy with a null array threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindAllByWithNullFilter() throws Exception {
    try {
      ArrayUtil.findAllBy(new Object[0], null);
      fail("Calling findAllBy with a null Filter should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The filter object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findAllBy with a null Filter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindBy() throws Exception {
    final Filter<Integer> negativeFilter = new Filter<Integer>() {
      public boolean accept(final Integer value) {
        return NumberUtil.isNegative(value);
      }
    };

    final Integer[] array = { 0, 2, 4, 8, -2, 16, 32, 64, 128 };
    final Integer actualValue = ArrayUtil.findBy(array, negativeFilter);

    assertNotNull(actualValue);
    assertEquals(-2, actualValue.intValue());
  }

  public void testFindByReturnsNull() throws Exception {
    final Filter<Integer> positiveFilter = new Filter<Integer>() {
      public boolean accept(final Integer value) {
        return NumberUtil.isPositive(value);
      }
    };

    final Integer[] array = { 0, -1, -2, -4, -8 };

    assertNull(ArrayUtil.findBy(array, positiveFilter));
  }

  public void testFindByWithNullArray() throws Exception {
    try {
      ArrayUtil.findBy(null, context.mock(Filter.class));
      fail("Calling findBy with a null array should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The array of elements cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findBy with a null array threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testFindByWithNullFilter() throws Exception {
    try {
      ArrayUtil.findBy(new Object[0], null);
      fail("Calling findBy with a null Filter should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The filter object cannot be null!", e.getMessage());
    }
    catch (Throwable t) {
      fail("Calling findBy with a null Filter threw an unexpected Throwable (" + t.getMessage() + ")!");
    }
  }

  public void testGetElementAt() throws Exception {
    final String[] elementArray = { "test", "mock", "null" };

    assertNull(ArrayUtil.getElementAt(null, -1));
    assertNull(ArrayUtil.getElementAt(null, 0));
    assertNull(ArrayUtil.getElementAt(elementArray, -1));
    assertEquals("test", ArrayUtil.getElementAt(elementArray, 0));
    assertEquals("mock", ArrayUtil.getElementAt(elementArray, 1));
    assertEquals("null", ArrayUtil.getElementAt(elementArray, 2));
    assertNull(ArrayUtil.getElementAt(elementArray, 3));
  }

  public void testGetIndexOf() throws Exception {
    final String[] elementArray = { "test", "mock", "null" };

    assertEquals(0, ArrayUtil.getIndexOf(elementArray, "test"));
    assertEquals(1, ArrayUtil.getIndexOf(elementArray, "mock"));
    assertEquals(2, ArrayUtil.getIndexOf(elementArray, "null"));
    assertEquals(-1, ArrayUtil.getIndexOf(elementArray, null));
    assertEquals(-1, ArrayUtil.getIndexOf(elementArray, "TEST"));
    assertEquals(-1, ArrayUtil.getIndexOf(elementArray, "stub"));
  }

  public void testGetIterable() throws Exception {
    final Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    int index = 0;

    for (final Integer value : ArrayUtil.getIterable(array)) {
      assertEquals(array[index++], value);
    }
  }

  public void testIsEmpty() throws Exception {
    assertTrue(ArrayUtil.isEmpty(null));
    assertTrue(ArrayUtil.isEmpty(new Object[] { }));
    assertTrue(ArrayUtil.isEmpty(new Integer[] { }));
    assertTrue(ArrayUtil.isEmpty(new Double[] { }));
    assertTrue(ArrayUtil.isEmpty(new String[] { }));
    assertFalse(ArrayUtil.isEmpty(new Object[10]));
    assertFalse(ArrayUtil.isEmpty(new Object[] { null }));
    assertFalse(ArrayUtil.isEmpty(new Integer[] { 1, 2, 3 }));
    assertFalse(ArrayUtil.isEmpty(new Double[] { 3.14159 }));
    assertFalse(ArrayUtil.isEmpty(new String[] { "" }));
  }

  public void testIsNotEmpty() throws Exception {
    assertFalse(ArrayUtil.isNotEmpty(null));
    assertFalse(ArrayUtil.isNotEmpty(new Object[] { }));
    assertFalse(ArrayUtil.isNotEmpty(new Integer[] { }));
    assertFalse(ArrayUtil.isNotEmpty(new Double[] { }));
    assertFalse(ArrayUtil.isNotEmpty(new String[] { }));
    assertTrue(ArrayUtil.isNotEmpty(new Object[10]));
    assertTrue(ArrayUtil.isNotEmpty(new Object[] { null }));
    assertTrue(ArrayUtil.isNotEmpty(new Integer[] { 1, 2, 3 }));
    assertTrue(ArrayUtil.isNotEmpty(new Double[] { 3.14159 }));
    assertTrue(ArrayUtil.isNotEmpty(new String[] { "" }));
  }

  public void testIterator() throws Exception {
    final Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    final Iterator<Integer> it = ArrayUtil.iterator(array);

    assertNotNull(it);

    for (final Integer value : array) {
      assertTrue(it.hasNext());
      assertEquals(value, it.next());
    }
  }

  public void testLength() throws Exception {
    assertEquals(0, ArrayUtil.length(null));
    assertEquals(0, ArrayUtil.length(new Object[] { }));
    assertEquals(3, ArrayUtil.length(new Integer[] { 1, 2, 3 }));
    assertEquals(100, ArrayUtil.length(new Object[100]));
  }

  public void testToStringBooleanArray() throws Exception {
    final boolean[] array = { true, false, true, false };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[true, false, true, false]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new boolean[0]));
  }

  public void testToStringCharArray() throws Exception {
    final char[] array = { 'c', 'a', 'f', 'e', 'b', 'a', 'b', 'e' };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[c, a, f, e, b, a, b, e]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new char[0]));
  }

  public void testToStringByteArray() throws Exception {
    final byte[] array = { 0, 1 };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[0, 1]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new byte[0]));
  }

  public void testToStringShortArray() throws Exception {
    final short[] array = { 0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192 };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new short[0]));
  }

  public void testToStringIntArray() throws Exception {
    final int[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new int[0]));
  }

  public void testToStringLongArray() throws Exception {
    final long[] array = { 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89 };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new long[0]));
  }

  public void testToStringFloatArray() throws Exception {
    final float[] array = { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new float[0]));
  }

  public void testToStringDoubleArray() throws Exception {
    final double[] array = { Math.PI };
    final String arrayString = ArrayUtil.toString(array);

    assertNotNull(arrayString);
    assertEquals("[" + Math.PI + "]", arrayString);
    assertEquals("[]", ArrayUtil.toString(new double[0]));
  }

  public void testToStringObjectArray() throws Exception {
    final Object[] array = { "zero", "one", "two" };

    final String expectedString = "[zero, one, two]";
    final String actualString = ArrayUtil.toString(array);

    assertNotNull(actualString);
    assertEquals(expectedString, actualString);
    assertEquals("[]", ArrayUtil.toString(new Object[0]));
  }

}
