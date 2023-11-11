/*
 * CollectionUtil.java (c) 17 August 2003
 *
 * Copyright (c) 2003, Codeprimate LLC
 * All Rights Reserved
 * @author John J. Blum
 * @version 2010.5.16
 * @see com.cp.common.util.ArrayUtil
 * @see java.util.Arrays
 * @see java.util.Collection
 * @see java.util.Collections
 * @see java.util.Comparator
 * @see java.util.Enumeration
 * @see java.util.Iterator
 */

package com.cp.common.util;

import com.cp.common.lang.Assert;
import com.cp.common.lang.MathUtil;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.Visitable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public final class CollectionUtil {

  /**
   * Default private constructor to prevent instantiation of the CollectionUtil utility class.
   */
  private CollectionUtil() {
  }

  /**
   * Gets all the unique combinations of elements in the specified list.  For instance, given a list with elements
   * [A, B, C], this method would return a list of lists [[A], [B], [C], [A, B], [A, C], [B, C], [A, B, C]].
   * @param elementList the list containing the elements to build combinations for.
   * @return a list of lists containing all the unique combinations of elements in the specified list.
   * @see CollectionUtil#combos
   * @see CollectionUtil#getCombinationsComparator
   */
  public static <T> List<List<T>> combinations(final List<T> elementList) {
    final List<List<T>> comboList = new ArrayList<List<T>>(MathUtil.factorial(elementList.size()));

    for (int index = 0, size = elementList.size(); index < size; index++) {
      final List<T> currentList = Arrays.asList(elementList.get(index));
      comboList.add(currentList);
      combos(elementList, comboList, currentList, index + 1);
    }

    Collections.sort(comboList, getCombinationsComparator());

    return Collections.unmodifiableList(comboList);
  }

  /**
   * Helper method used by the getCombinations method to build the next cobmination in sequence using recursion.
   * @param elementList the list containing the original elements.
   * @param comboList the list containing all the combinations of elements in the element list.
   * @param currentList the list containing the current combination of elements.
   * @param startIndex the start index into the element list to build the next combination.
   * @see CollectionUtil#combinations
   */
  private static <T> void combos(final List<T> elementList,
                                 final List<List<T>> comboList,
                                 final List<T> currentList,
                                 final int startIndex) {
    for (int index = startIndex, size = elementList.size(); index < size; index++) {
      final List<T> newList = new LinkedList<T>(currentList);
      newList.add(elementList.get(index));
      comboList.add(newList);
      combos(elementList, comboList, newList, index + 1);
    }
  }

  /**
   * Copies the specified Stack to a new Stack.
   * @param stack the Stack object to copy.
   * @return an exact copy of the specified Stack.
   */
  public static <T> Stack<T> copy(final Stack<T> stack) {
    final Stack<T> stackCopy = new Stack<T>();

    for (final T element : stack) {
      stackCopy.push(element);
    }

    return stackCopy;
  }

  /**
   * Adapts the Iterator interface into the Enumeration interface.
   * @param it the Iterator object to treat as an instance of the Enumeration interface.
   * @return an Enumeration instance wrapping the Iterator.
   * @see CollectionUtil#iterator(java.util.Enumeration)
   */
  public static <T> Enumeration<T> enumeration(final Iterator<T> it) {
    Assert.notNull(it, "The Iterator object cannot be null!");
    return new IteratorToEnumeration<T>(it);
  }

  /**
   * Finds all the elements in the specified Collection that satisfy the conditions of the given Filter.
   * @param elements a Collection of elements to filter.
   * @param filter a Filter object used to filter the elements in the specified Collection.
   * @return a Collection of elements from the original Collection filtered by the given Filter.  If no elements in
   * the specified Collection satisfy the conditions of the Filter, then an empty Collection is returned.
   * @see CollectionUtil#findBy(java.util.Collection, Filter)
   */
  public static <T> Collection<T> findAllBy(final Collection<T> elements, final Filter<T> filter) {
    Assert.notNull(elements, "The collection of elements cannot be null!");
    Assert.notNull(filter, "The filter object cannot be null!");

    final Collection<T> filteredElements = new LinkedList<T>();

    for (final T element : elements) {
      if (filter.accept(element)) {
        filteredElements.add(element);
      }
    }

    return filteredElements;
  }

  /**
   * Finds a single element in the specified Collection that satisfies the conditions of the given Filter.
   * @param elements a Collection of elements to filter.
   * @param filter a Filter object used to filter the elements in the specified Collection.
   * @return the first, single element from the Collection that satisfies the conditions of the given Filter,
   * or null if no element in the Collection satisfies the Filter's conditions.
   * @see CollectionUtil#findAllBy(java.util.Collection, Filter)
   */
  public static <T> T findBy(final Collection<T> elements, final Filter<T> filter) {
    Assert.notNull(elements, "The collection of elements cannot be null!");
    Assert.notNull(filter, "The filter object cannot be null!");

    for (final T element : elements) {
      if (filter.accept(element)) {
        return element;
      }
    }

    return null;
  }

  /**
   * Gets a Comparator object to order the combination lists.
   * @return a Comparator to sort the list of lists.
   * @see CollectionUtil#combinations
   */
  private static Comparator<List> getCombinationsComparator() {
    return new Comparator<List>() {
      public int compare(final List list0, final List list1) {
        return (list0.size() - list1.size());
      }
    };
  }

  public static <T> T getFirstElement(final List<T> elements) {
    return (isEmpty(elements) ? null : elements.get(0));
  }

  /**
   * Gets an Iterable implementation backed by an Enumeration object.
   * @param enumeration the Enumeration object containing the elements for the Iterable object.
   * @return an Iterable implementation consisting of elements backed by an Enmeration object.
   * @see CollectionUtil#getIterable(java.util.Iterator)
   */
  public static <T> Iterable<T> getIterable(final Enumeration<T> enumeration) {
    return new Iterable<T>() {
      public Iterator<T> iterator() {
        return CollectionUtil.iterator(enumeration);
      }
    };
  }

  /**
   * Gets an Iterable implementation backed by an Iterator object.
   * @param iterator the Iterator object containing the elements for the Iterable object.
   * @return an Iterable implementation consisting of elements backed by an Iterable object.
   * @see CollectionUtil#getIterable(java.util.Enumeration)
   */
  public static <T> Iterable<T> getIterable(final Iterator<T> iterator) {
    return new Iterable<T>() {
      public Iterator<T> iterator() {
        return iterator;
      }
    };
  }

  public static <T> T getLastElement(final List<T> elements) {
    return (isEmpty(elements) ? null : elements.get(elements.size() - 1));
  }

  /**
   * Returns a List of elements of Generic type T for the given T element array.
   * @param elements the array of elements to convert into a List (ordered collection of elements).
   * @return a List object containing the elements of the array.
   * @see CollectionUtil#getSet(Object[])
   */
  public static <T> List<T> getList(final T... elements) {
    return Arrays.asList(elements);
  }

  /**
   * Returns a Set of elements of Generic type T for the given T element array.
   * @param elements the array of elements to convert into a Set (unique collection of elements).
   * @return a Set object containing the elements of the array.
   * @see CollectionUtil#getList(Object[])
   */
  public static <T> Set<T> getSet(final T... elements) {
    return new HashSet<T>(getList(elements));
  }

  /**
   * Determines whether the Collection object is empty or contains elements.  If the Collection is null or empty,
   * this method will return true, otherwise this method will return false.
   * @param collection the Collection object being tested for empty.
   * @return a boolean value indicating if the Collection object is empty or not.
   * @see CollectionUtil#isNotEmpty(java.util.Collection)
   */
  public static boolean isEmpty(final Collection collection) {
    return (ObjectUtil.isNull(collection) || collection.isEmpty());
  }

  public static boolean isEmpty(final Map map) {
    return (ObjectUtil.isNull(map) || map.isEmpty());
  }

  /**
   * Determines whether the specified Object is the first element of the specified List.
   * @param element item in question of whether it is first element in the List.
   * @param list the List in which to determine if the element is the first item.
   * @return a boolean value indicating whether the element is the first item in the List.
   * @see CollectionUtil#isLastElement(Object, java.util.List)
   */
  public static boolean isFirstElement(final Object element, final List list) {
    return (list.indexOf(element) == 0);
  }

  /**
   * Determines whether the specified Object is the last element in the specified List.
   * @param element item in question of whether it is the last element in the List.
   * @param list the List in which to determine if the element is the last item.
   * @return a boolean value indicating whether the element is the last item in the List.
   * @see CollectionUtil#isFirstElement(Object, java.util.List)
   */
  public static boolean isLastElement(final Object element, final List list) {
    return (list.indexOf(element) == (list.size() - 1));
  }

  /**
   * Determines whether the Collection object is empty or contains elements.  If the Collection is null or empty,
   * this method will return false, otherwise the method will return true.
   * @param collection the Collection object being tested for not empty.
   * @return a boolean value indicating if the Collection object is empty or not.
   * @see CollectionUtil#isEmpty(java.util.Collection)
   */
  public static boolean isNotEmpty(final Collection collection) {
    return (ObjectUtil.isNotNull(collection) && !collection.isEmpty());
  }

  public static boolean isNotEmpty(final Map map) {
    return (ObjectUtil.isNotNull(map) && !map.isEmpty());
  }

  /**
   * Adapts the Enumeration interface into the Iterator interface.
   * @param enumx the Enumeration object to treat as a Iterator.
   * @return an Iterator instance wrapping Enumeration.
   */
  public static <T> Iterator<T> iterator(final Enumeration<T> enumx) {
    Assert.notNull(enumx, "The Enumeration object cannot be null!");
    return new EnumerationToIterator<T>(enumx);
  }

  /**
   * Returns the difference of the two Sets by taking elements from set1 that are not in set2.
   * @param set1 a java.util.Set from which to retain elements that are not in set2.
   * @param set2 a java.util.Set used to remove elments from set1 that are also in set2.
   * @return a Set containing the difference of set1 and set2.
   * @see CollectionUtil#setIntersection(java.util.Set, java.util.Set)
   * @see CollectionUtil#setUnion(java.util.Set, java.util.Set)
   */
  public static <T> Set<T> setDifference(final Set<T> set1, final Set<T> set2) {
    final Set<T> resultSet = new HashSet<T>(set1);
    resultSet.removeAll(set2);
    return resultSet;
  }

  /**
   * Returns the intersection of the two Sets by taking elements from set1 that are also in set2.
   * @param set1 a java.util.Set from which to retain elements that are also in set2.
   * @param set2 a java.util.Set used to retain elments from set1 that are also in set2.
   * @return a Set containing the intersection of set1 and set2.
   * @see CollectionUtil#setDifference(java.util.Set, java.util.Set)
   * @see CollectionUtil#setUnion(java.util.Set, java.util.Set)
   */
  public static <T> Set<T> setIntersection(final Set<T> set1, final Set<T> set2) {
    final Set<T> resultSet = new HashSet<T>(set1);
    resultSet.retainAll(set2);
    return resultSet;
  }

  /**
   * Combines both Sets into one Set removing duplicate elements.
   * @param set1 a java.util.Set in which to merge with set2.
   * @param set2 a java.util.Set in which to merge with set1.
   * @return a Set containing the union of set1 and set2.
   * @see CollectionUtil#setDifference(java.util.Set, java.util.Set)
   * @see CollectionUtil#setIntersection(java.util.Set, java.util.Set)
   */
  public static <T> Set<T> setUnion(final Set<T> set1, final Set<T> set2) {
    final Set<T> resultSet = new HashSet<T>(set1);
    resultSet.addAll(set2);
    return resultSet;
  }

  /**
   * Determines the length of the specified Collection of elements.
   * @param collection the Collection who's length is being determined.
   * @return an integer value specifying the length of the Collection of zero if the Collection is null.
   */
  public static int size(final Collection collection) {
    return (isEmpty(collection) ? 0 : collection.size());
  }

  /**
   * Returns a String representation of the elements in the specified List object.
   * @param collection the List with elements to construct a String representation of.
   * @return a String representation of the List object.
   */
  public static String toString(final Collection collection) {
    final StringBuffer buffer = new StringBuffer("[");
    boolean addComma = false;

    for (final Object element : collection) {
      buffer.append(addComma ? ", " : "").append(element);
      addComma = true;
    }

    buffer.append("]");

    return buffer.toString();
  }

  /**
   * Cosntructs an instance of the Iterator interface that cannot modify the
   * backing List object.
   * @param it the Iterator for which an unmodifiable version will be returned.
   * @return an unmodifiable instance of the specified Iterator.
   */
  public static <T> Iterator<T> unmodifiableIterator(final Iterator<T> it) {
    return new Iterator<T>() {
      public boolean hasNext() {
        return it.hasNext();
      }

      public T next() {
        return it.next();
      }

      public void remove() {
        throw new UnsupportedOperationException("Not able to modify the underlying collection.");
      }
    };
  }

  /**
   * Constructs an instance of the ListIterator interface that cannot modify
   * the backing List object.
   * @param lit the ListIterator for which an unmodifiable version will be
   * returned.
   * @return an unmodifiable instance of the specified ListIterator.
   */
  public static <T> ListIterator<T> unmodifiableListIterator(final ListIterator<T> lit) {
    return new ListIterator<T>() {
      public void add(T o) {
        throw new UnsupportedOperationException("Unable to modify the underlying List object!");
      }

      public boolean hasNext() {
        return lit.hasNext();
      }

      public boolean hasPrevious() {
        return lit.hasPrevious();
      }

      public T next() {
        return lit.next();
      }

      public int nextIndex() {
        return lit.nextIndex();
      }

      public T previous() {
        return lit.previous();
      }

      public int previousIndex() {
        return lit.previousIndex();
      }

      public void remove() {
        throw new UnsupportedOperationException("Unable to modify the underlying List object!");
      }

      public void set(T o) {
        throw new UnsupportedOperationException("Unable to modify the underlying List object!");
      }
    };
  }

  public static void visit(final Collection<? extends Visitable> elements, final Visitor visitor) {
    for (final Visitable obj : elements) {
      obj.accept(visitor);
    }
  }

}
