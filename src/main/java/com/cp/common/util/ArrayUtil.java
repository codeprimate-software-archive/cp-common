/*
 * ArrayUtil.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.3.28
 * @see com.cp.common.util.CollectionUtil
 * @see java.util.Arrays
 * @see java.util.Collection
 * @see java.util.Enumeration
 * @see java.util.Iterator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class ArrayUtil {

  /**
   * Default private constructor to prevent instantiation of the ArrayUtil utility class.
   */
  private ArrayUtil() {
  }

  /**
   * Converts a boolean array into a List of Boolean wrapper objects.
   * @param array the boolean array to convert to a List.
   * @return a List of Boolean wrapper objects representing the individual values of the boolean array.
   */
  public static List<Boolean> asList(final boolean[] array) {
    final List<Boolean> arrayList = new ArrayList<Boolean>(array.length);

    for (final boolean condition : array) {
      arrayList.add(condition ? Boolean.TRUE : Boolean.FALSE);
    }

    return arrayList;
  }

  /**
   * Converts a char array into a List of Character wrapper objects.
   * @param array the char array to convert to a List.
   * @return a List of Character wrapper objects representing the individual values of the char array.
   */
  public static List<Character> asList(final char[] array) {
    final List<Character> arrayList = new ArrayList<Character>(array.length);

    for (final char chr : array) {
      arrayList.add(chr);
    }

    return arrayList;
  }

  /**
   * Converts a byte array into a List of Byte wrapper objects.
   * @param array the byte array to convert to a List.
   * @return a List of Byte wrapper objects representing the individual values of the byte array.
   */
  public static List<Byte> asList(final byte[] array) {
    final List<Byte> arrayList = new ArrayList<Byte>(array.length);

    for (final byte value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Converts a short array into a List of Short wrapper objects.
   * @param array the short array to convert to a List.
   * @return a List of Short wrapper objects representing the individual values of the short array.
   */
  public static List<Short> asList(final short[] array) {
    final List<Short> arrayList = new ArrayList<Short>(array.length);

    for (final short value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Returns the specified primitive int array as a List of Integer wrapper objects.
   * @param array an array of int values being stored in a List object.
   * @return a List object containing Integer objects for elements of the int array.
   */
  public static List<Integer> asList(final int[] array) {
    final List<Integer> arrayList = new ArrayList<Integer>(array.length);

    for (final int value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Converts a long array into a List of Long wrapper objects.
   * @param array the long array to convert to a List.
   * @return a List of Long wrapper objects representing the individual values of the long array.
   */
  public static List<Long> asList(final long[] array) {
    final List<Long> arrayList = new ArrayList<Long>(array.length);

    for (final long value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Converts a float array into a List of Float wrapper objects.
   * @param array the float array to convert to a List.
   * @return a List of Float wrapper objects representing the individual values of the float array.
   */
  public static List<Float> asList(final float[] array) {
    final List<Float> arrayList = new ArrayList<Float>(array.length);

    for (final float value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Converts a double array into a List of Double wrapper objects.
   * @param array the double array to convert to a List.
   * @return a List of Double wrapper objects representing the individual values of the double array.
   */
  public static List<Double> asList(final double[] array) {
    final List<Double> arrayList = new ArrayList<Double>(array.length);

    for (final double value : array) {
      arrayList.add(value);
    }

    return arrayList;
  }

  /**
   * Returns an enumeration over the elements of the Object array.
   * @param array the Object array for which the enumeration is constructed.
   * @return an Enumeration implementation over the elements of the Object array.
   * @see ArrayUtil#getIterable(Object[])
   * @see ArrayUtil#iterator(Object[])
   * @see java.util.Enumeration
   */
  public static <T> Enumeration<T> enumeration(final T[] array) {
    return new ArrayEnumeration<T>(array);
  }

  /**
   * Finds all the elements in the specified array that satisfy the conditions of the given Filter.
   * @param elements an array of elements to filter.
   * @param filter a Filter object used to filter the elements in the specified array.
   * @return an array of elements from the original array filtered by the given Filter.  If no elements in the
   * specified array satisfy the conditions of the Filter, then an empty array is returned.
   * @see ArrayUtil#findBy(Object[], Filter)
   */
  public static <T> T[] findAllBy(final T[] elements, final Filter<T> filter) {
    Assert.notNull(elements, "The array of elements cannot be null!");
    Assert.notNull(filter, "The filter object cannot be null!");

    final Collection<T> filteredElements = new LinkedList<T>();

    for (final T element : elements) {
      if (filter.accept(element)) {
        filteredElements.add(element);
      }
    }

    return (T[]) filteredElements.toArray();
  }

  /**
   * Finds a single element in the specified array that satisfies the conditions of the given Filter.
   * @param elements an array of elements to filter.
   * @param filter a Filter object used to filter the elements in the specified array.
   * @return the first, single element from the array that satisfies the conditions of the given Filter,
   * or null if no element in the array satisfies the Filter's conditions.
   * @see ArrayUtil#findAllBy(Object[], Filter) 
   */
  public static <T> T findBy(final T[] elements, final Filter<T> filter) {
    Assert.notNull(elements, "The array of elements cannot be null!");
    Assert.notNull(filter, "The filter object cannot be null!");

    for (final T element : elements) {
      if (filter.accept(element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Gets the specified element in the generic-typed Object array at index.
   * @param elementArray the array of elements from which to retrieve the value at the specified index.
   * @param index an integer value specifying the index into the array of elements to retrieve the value.
   * @return the element at the specified index in the element array or null if the element array
   * does not have an element at the specified index.
   * @see ArrayUtil#getIndexOf(Object[], Object)
   * @see ArrayUtil#length(Object[])
   */
  public static <T> T getElementAt(final T[] elementArray, final int index) {
    return (index > -1 && length(elementArray) > index ? elementArray[index] : null);
  }

  /**
   * Gets the index of the specified element in the generic-typed Object array or returns a -1 if the specified element
   * does not exist in the generic-type Object array.
   * @param elementArray the array of elements from which to determine the index of the specified element.
   * @param element the generic-typed element to determine the index of in the array of elements.
   * @return an integer value specifying the index of the specified element in the generic-typed Object array or a -1
   * if the specified element does not exist in the generic-type Object array.
   * @see ArrayUtil#getElementAt(Object[], int)
   */
  public static <T> int getIndexOf(final T[] elementArray, final T element) {
    int index = 0;

    for (final T arrayElement : elementArray) {
      if (ObjectUtil.equals(element, arrayElement)) {
        return index;
      }

      index++;
    }

    return -1;
  }

  /**
   * Returns an implementation of the Iterable interface to traverse the elements of the specified generic-typed array.
   * @param array a generic-typed Object array for which the Iterable instance is constructed.
   * @return an Iterable instance to traverse the elements of the generic-typed Object array.
   * @see ArrayUtil#enumeration(Object[])
   * @see ArrayUtil#iterator(Object[])
   * @see java.lang.Iterable
   */
  public static <T> Iterable<T> getIterable(final T[] array) {
    return new Iterable<T>() {
      public Iterator<T> iterator() {
        return ArrayUtil.iterator(array);
      }
    };
  }

  /**
   * Determines whether the specified Object array is empty.  The array is empty if the array is null
   * or has a length equal to zero.
   * @param array the Object array being tested for empty.
   * @return a boolean value indicating whether the specified Object array is empty or not.
   * @see ArrayUtil#isNotEmpty(Object[])
   */
  public static boolean isEmpty(final Object[] array) {
    return (ObjectUtil.isNull(array) || (array.length == 0));
  }

  /**
   * Determines whether the specified Object array is not empty.  The array is empty if the array is null
   * or has a length equal to zero; the array is not empty otherwise.
   * @param array the Object array being tested for not empty.
   * @return a boolean value indicating whether the specified Object array is empty or not.
   * @see ArrayUtil#isEmpty(Object[])
   */
  public static boolean isNotEmpty(final Object[] array) {
    return (ObjectUtil.isNotNull(array) && (array.length != 0));
  }

  /**
   * Returns an Iterator over the elements of the Object array.
   * @param array the Object array for which the Iterator is constructed.
   * @return an Iterator implementation to iterate over the elements of the Object array.
   * @see ArrayUtil#enumeration(Object[])
   * @see ArrayUtil#getIterable(Object[])
   * @see java.util.Iterator
   */
  public static <T> Iterator<T> iterator(final T[] array) {
    return new ArrayIterator<T>(array);
  }

  /**
   * Returns the length of the specified Object array, or zero if the Object array is null.
   * @param array the Object array who's length is being determined.
   * @return an integer value specifying the number of elements in the Object array.
   */
  public static int length(final Object[] array) {
    return (isEmpty(array) ? 0 : array.length);
  }

  /**
   * Returns a String representation of the elements in the boolean array.
   * @param array a primitive boolean array containing values to represent in the String.
   * @return a String representation of the boolean array.
   */
  public static String toString(final boolean[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the char array.
   * @param array a primitive char array containing values to represent in the String.
   * @return a String representation of the char array.
   */
  public static String toString(final char[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the byte array.
   * @param array a primitive byte array containing values to represent in the String.
   * @return a String representation of the byte array.
   */
  public static String toString(final byte[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the short array.
   * @param array a primitive short array containing values to represent in the String.
   * @return a String representation of the short array.
   */
  public static String toString(final short[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the int array.
   * @param array a primitive int array containing values to represent in the String.
   * @return a String representation of the int array.
   */
  public static String toString(final int[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the long array.
   * @param array a primitive long array containing values to represent in the String.
   * @return a String representation of the long array.
   */
  public static String toString(final long[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the float array.
   * @param array a primitive float array containing values to represent in the String.
   * @return a String representation of the float array.
   */
  public static String toString(final float[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Returns a String representation of the elements in the double array.
   * @param array a primitive double array containing values to represent in the String.
   * @return a String representation of the double array.
   * @see com.cp.common.util.CollectionUtil#toString(java.util.Collection)
   */
  public static String toString(final double[] array) {
    return CollectionUtil.toString(asList(array));
  }

  /**
   * Converts the Object array into a String value.
   * @param array an array of Objects.
   * @return a String value for the specified Object array.
   */
  public static String toString(final Object[] array) {
    return (ObjectUtil.isNull(array) ? "[]" : CollectionUtil.toString(Arrays.asList(array)));
  }

}
